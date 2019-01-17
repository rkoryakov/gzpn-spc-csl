package ru.gzpn.spc.csl.model.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import ru.gzpn.spc.csl.model.enums.TaxType;

public interface IMilestone {
	
	public String getName();
	
	public void setName(String name);
	
	public String getCode();
	
	public void setCode(String code);
	
	public LocalDate getStartDate();
	
	public void setStartDate(LocalDate startDate);
	
	public LocalDate getEndDate();
	
	public void setEndDate(LocalDate endDate);
	
	public ICProject getProject();
	
	public void setProject(ICProject project);
	
	public BigDecimal getSum();
	
	public void setSum(BigDecimal sum);
	
	public TaxType getTaxType();
	
	public void setTaxType(TaxType taxType);
	
	public List<IWork> getWorks();
	
	public void setWorks(List<IWork> works);
	
	public IContract getContract();
	
	public void setContract(IContract contract);
	
	public int getPpNum();
	
	public void setPpNum(int ppNum);
	
	public int getMilNum();
	
	public void setMilNum(int milNum);
}
