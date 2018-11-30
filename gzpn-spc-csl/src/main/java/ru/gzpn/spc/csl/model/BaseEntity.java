package ru.gzpn.spc.csl.model;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class BaseEntity {

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
}
