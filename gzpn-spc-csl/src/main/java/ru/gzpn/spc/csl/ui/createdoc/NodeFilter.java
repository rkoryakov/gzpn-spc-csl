package ru.gzpn.spc.csl.ui.createdoc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.persistence.JoinColumn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.gzpn.spc.csl.model.BaseEntity;

public class NodeFilter {
	public static final Logger logger = LogManager.getLogger(NodeFilter.class);
	private String commonFilter;
	
	private Map<String, String> queryNodeFilters = new HashMap<>();
	
	public NodeFilter(String commonFilter) {
		this.commonFilter = commonFilter;
	}
	public void add(String key, String value) {
		queryNodeFilters.put(key, value);
	}
	
	public String get (String key) {
		return queryNodeFilters.get(key);
	}
	
	public void setCommonFilter(String filter) {
		this.commonFilter = filter;
	}
	
	public String getCommonFilter() {
		return this.commonFilter;
	}
	
	/**
	 * Filtering by commonFilter in the all entity's fields
	 * if any matches return true
	 * @param item
	 * @return
	 */
	protected boolean applyCommonFilterOnEntity(NodeWrapper item) {
		boolean isShown = false;
		if (commonFilter != null && !commonFilter.isEmpty() && item.hasEntityItem()) {
			BaseEntity entity = item.getItem();
			for (Field e : entity.getClass().getDeclaredFields()) {
				int mod = e.getModifiers();
				boolean isNotJoinColumnAnnotation = Stream.of(e.getDeclaredAnnotations())
						.noneMatch(a -> a.annotationType() == JoinColumn.class);
				if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod) && isNotJoinColumnAnnotation) {
					e.setAccessible(true);
					Object value = null;
					try {
						value = e.get(entity);
					} catch (IllegalArgumentException | IllegalAccessException e1) {
						logger.error(e1.getMessage());
					}
					if (value != null) {
						isShown = value.toString().startsWith(commonFilter);
						if (isShown) {
							break;
						}
					}
				}
			}
		} else {
			isShown = true;
		}
		
		return isShown;
	}
	
	/**
	 * Filtering by queryNodeFilters if the entity item isn't null
	 * @param item
	 * @return
	 */
	protected boolean applyQueryNodeFiltersOnEntity(NodeWrapper item) {
		boolean isShown = true;
		BaseEntity entity = item.getItem();
		for (Field e : entity.getClass().getDeclaredFields()) {
			int mod = e.getModifiers();
			boolean isNotJoinColumnAnnotation = Stream.of(e.getDeclaredAnnotations())
					.noneMatch(a -> a.annotationType() == JoinColumn.class);
			if (!Modifier.isStatic(mod) && !Modifier.isFinal(mod) && isNotJoinColumnAnnotation
					&& queryNodeFilters.containsKey(e.getName())) {
				e.setAccessible(true);
				Object value = null;
				try {
					value = e.get(entity);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					logger.error(e1.getMessage());
				}
				if (value != null) {
					isShown = value.toString().startsWith(queryNodeFilters.get(e.getName()));
					if (!isShown) {
						break;
					}
				}
			}
		}
		return isShown;
	}
	
	public Predicate<NodeWrapper> filter() {
		return item -> {
			boolean isShown = false;
			
			if (queryNodeFilters.isEmpty()) {
				isShown = applyCommonFilterOnEntity(item);
			}
			else if (item.hasEntityItem()) {
				isShown = applyQueryNodeFiltersOnEntity(item);
			} else {
				if (commonFilter != null && commonFilter.isEmpty()) {
					isShown = true;
				} else {
					isShown = item.getGroupFiledValue().toString().startsWith(commonFilter);
				}
			}
			return isShown;
		};
	}
}
