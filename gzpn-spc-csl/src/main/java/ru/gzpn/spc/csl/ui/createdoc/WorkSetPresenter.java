package ru.gzpn.spc.csl.ui.createdoc;

import java.time.format.DateTimeFormatter;

import ru.gzpn.spc.csl.model.WorkSet;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public class WorkSetPresenter extends WorkSet implements IWorkSetPresenter, I18n {

	private IWorkSet workset;
	
	public WorkSetPresenter(IWorkSet workset) {
		this.setId(workset.getId());
		this.setName(workset.getName());
		this.setPir(workset.getPir());
		this.setPlanObject(workset.getPlanObject());
		this.setSmr(workset.getSmr());
		this.setVersion(workset.getVersion());
		this.setChangeDate(workset.getChangeDate());
		this.setCode(workset.getCode());
		this.setCreateDate(workset.getCreateDate());
		
		this.setWorkset(workset);
	}
	
	@Override
	public void setWorkset(IWorkSet workset) {
		this.workset = workset;
	}

	@Override
	public IWorkSet getWorkset() {
		return this.workset;
	}

	@Override
	public String getPirText() {
		return getPir().getCode();
	}

	@Override
	public String getSmrText() {
		return getSmr().getCode();
	}
	
	@Override
	public String getCreateDateText() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(getCreateDate());
	}
	
	@Override
	public String getChangeDateText() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(getChangeDate());
	}

	@Override
	public String getPlanObjectMarkText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlanObjectCodeText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlanObjectNameText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCProjectNameText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCProjectCodeText() {
		// TODO Auto-generated method stub
		return null;
	}
}
