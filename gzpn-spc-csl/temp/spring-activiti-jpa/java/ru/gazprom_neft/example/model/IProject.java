package ru.gazprom_neft.example.model;

public interface IProject {

	public Long getId();

	public void setId(Long id);

	public String getName();

	public void setName(String name);

	public void setStage(IStage stage);

	public IStage getStage();
}
