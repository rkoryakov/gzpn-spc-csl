package ru.gzpn.spc.csl.ui.createdoc;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

public interface IWorkSetPresenter extends IWorkSet {
	public void setWorkset(IWorkSet workset);
	public IWorkSet getWorkset();
	
	public String getPirText();
	public String getSmrText();
	public String getChangeDateText();
	public String getCreateDateText();
	public String getPlanObjectMarkText();
	public String getPlanObjectCodeText();
	public String getPlanObjectNameText();
	public String getCProjectNameText();
	public String getCProjectCodeText();
	
	
}
