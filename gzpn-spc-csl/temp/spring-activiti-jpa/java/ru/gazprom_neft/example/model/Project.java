package ru.gazprom_neft.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "SPK_PROJECTS", schema = "spk_model")
public class Project extends BaseEntity implements IProject, Serializable {

	private String name;
	@Column
	@Type(type = "CustomJsonType")
	private Location location;

	@ManyToOne(targetEntity = Stage.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "stage_id")
	private IStage stage;

	@CreationTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime createDate;

	@UpdateTimestamp
	@Column(updatable = false, nullable = false)
	private LocalDateTime changeTime;

	public Project() {
		super();
	}

	public Project(String name) {
		this.name = name;
	}

	public Project(String name, Stage stage) {
		this.name = name;
		this.stage = stage;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setStage(IStage stage) {
		this.stage = stage;
	}

	@Override
	public IStage getStage() {
		return this.stage;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
