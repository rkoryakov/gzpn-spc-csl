package ru.gzpn.spc.csl.model.interfaces;

import java.time.LocalDate;
import java.util.List;

import ru.gzpn.spc.csl.model.enums.WorkType;

public interface IWork extends IBaseEntity {
	public ILocalEstimate getLocalEstimate();

	public void setLocalEstimate(ILocalEstimate localEstimate);

	public String getCode();

	public void setCode(String code);

	public String getName();

	public void setName(String name);

	public WorkType getType();

	public void setType(WorkType type);

	public IPlanObject getPlanObj();

	public void setPlanObj(IPlanObject planObj);

	List<IDocument> getDocuments();

	void setDocuments(List<IDocument> documents);

	IMilestone getMilestone();

	void setMilestone(IMilestone milestone);

	IWorkSet getWorkSet();

	void setWorkSet(IWorkSet workSet);

	LocalDate getBeginDate();

	void setBeginDate(LocalDate beginDate);

	LocalDate getEndDate();

	void setEndDate(LocalDate endDate);

}
