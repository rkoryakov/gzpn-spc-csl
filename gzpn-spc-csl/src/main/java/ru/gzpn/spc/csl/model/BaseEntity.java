package ru.gzpn.spc.csl.model;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import ru.gzpn.spc.csl.model.interfaces.IBaseEntity;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;


@MappedSuperclass
public abstract class BaseEntity implements IBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spc_csl_gen")
	@SequenceGenerator(name = "spc_csl_gen", initialValue = 1, allocationSize = 1, schema = "spc_csl_schema")
	@Column(updatable = false, nullable = false)
	private Long id;

	@Version
	private Integer version;

	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private ZonedDateTime createDate;

	@UpdateTimestamp
	@Column(updatable = true, nullable = false)
	private ZonedDateTime changeDate;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
	public ZonedDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(ZonedDateTime createDate) {
		this.createDate = createDate;
	}

	public ZonedDateTime getChangeDate() {
		return changeDate;
	}

	public void setChangeDate(ZonedDateTime changeDate) {
		this.changeDate = changeDate;
	}

	/* NOT USED */
	public static Set<String> getEntityFields(Class<? extends IBaseEntity> c) {
		return Arrays.asList(c.getDeclaredFields()).stream()
					.filter(item -> !Modifier.isStatic(item.getModifiers()) 
								&& !Modifier.isFinal(item.getModifiers()))
						.map(Field::getName)
							.collect(Collectors.toSet());
	}
	/* NOT USED */
	public static <S, R> Supplier<R> getFildValueGetter(Class<S> c, String fieldName, R returnType) {
		Supplier<R> result = () -> {R r = null; return r;};
		try {
			
	        MethodType type = MethodType.methodType( Long.class, c);
	        MethodType invokeType = MethodType.methodType(result.getClass());
	        MethodHandles.Lookup lookup = MethodHandles.lookup();
	        Method method = c.getDeclaredMethod("get" + StringUtils.capitalize(fieldName));
	       
	          if (!Modifier.isStatic(method.getModifiers())) {
	        	  MethodHandle mh = lookup.unreflect(method);
		        if (mh.type().equals(type)) 
		        	  result = (Supplier<R>)LambdaMetafactory.metafactory(
		        		  lookup, "get", invokeType, type, mh, type).getTarget().invokeExact();
	          }
	          
	        } catch(Throwable ex) {
	            throw new ExceptionInInitializerError(ex);
	        }
		
		return result;
	}
	
	public static void main(String[] args) {
		IWorkSet workSet = new WorkSet();
		workSet.setId(1L);
		Long s = 1L;
		System.out.println("getFildValueGetter: " + getEntityFields(workSet.getClass()));
	}
}
