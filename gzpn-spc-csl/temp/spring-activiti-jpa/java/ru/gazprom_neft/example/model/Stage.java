package ru.gazprom_neft.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SPK_STAGES", schema = "spk_model")
public class Stage extends BaseEntity implements IStage, Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	@OneToMany(targetEntity = StageObject.class, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(schema = "spk_model", joinColumns = @JoinColumn(name = "stage_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "stageobj_id", referencedColumnName = "id"))
	private List<IStageObject> stageObjects = new ArrayList<>();

	public Stage() {
		super();
	}

	public Stage(String name, IStageObject stageObject) {
		super();
		this.name = name;
		this.stageObjects.add(stageObject);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void addStageObject(StageObject stageObject) {
		this.stageObjects.add(stageObject);
	}

	public List<IStageObject> getStageObjects() {
		return stageObjects;
	}

	public void setStageObjects(List<IStageObject> stageObjects) {
		this.stageObjects = stageObjects;
	}

}
