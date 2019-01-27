package ru.gzpn.spc.csl.ui.contractreg;

import ru.gzpn.spc.csl.model.interfaces.IContract;

public interface IContractPresenter extends IContract {
	
	public IContract getContract();
	public void setContract(IContract contract);
	public String getSigningDatePresenter();
	public String getCreateDatePresenter();
	public String getChangeDatePresenter();
}
