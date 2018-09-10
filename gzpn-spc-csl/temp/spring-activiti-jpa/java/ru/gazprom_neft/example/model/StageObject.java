package ru.gazprom_neft.example.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@SuppressWarnings("serial")
@Entity
@Table(name = "SPK_STAGEOBJ", schema = "spk_model")
public class StageObject extends BaseEntity implements IStageObject, Serializable {

	private String name;

	@Column
	@Type(type = "CustomJsonType")
	private Location location;

	public StageObject() {
		super();
	}

	public StageObject(String name) {
		super();
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
