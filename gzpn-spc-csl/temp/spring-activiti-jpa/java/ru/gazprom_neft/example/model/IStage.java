package ru.gazprom_neft.example.model;

import java.util.List;

public interface IStage {
	public String getName();

	public void setName(String name);

	public void addStageObject(StageObject stageObject);

	public List<IStageObject> getStageObjects();

	public void setStageObjects(List<IStageObject> stageObjects);

}
