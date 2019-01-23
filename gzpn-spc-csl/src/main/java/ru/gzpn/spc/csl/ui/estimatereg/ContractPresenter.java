package ru.gzpn.spc.csl.ui.estimatereg;

import java.time.format.DateTimeFormatter;

import ru.gzpn.spc.csl.model.Contract;
import ru.gzpn.spc.csl.model.interfaces.IContract;

@SuppressWarnings("serial")
public class ContractPresenter extends Contract implements IContractPresenter {
	
	private IContract contract;

	public ContractPresenter(IContract icontract) {
		this.setId(icontract.getId());
		this.setName(icontract.getName());
		this.setCode(icontract.getCode());
		this.setSigningDate(icontract.getSigningDate());
		this.setCustomerName(icontract.getCustomerName());
		this.setCustomerINN(icontract.getCustomerINN());
		this.setCustormerBank(icontract.getCustormerBank());
		this.setExecutorName(icontract.getExecutorName());
		this.setExecutorINN(icontract.getExecutorINN());
		this.setExecutorBank(icontract.getExecutorBank());
		this.setCreateDate(icontract.getCreateDate());
		this.setChangeDate(icontract.getChangeDate());
		this.setVersion(icontract.getVersion());
		this.contract = icontract;
	}
	
	@Override
	public IContract getContract() {
		return contract;
	}
	
	@Override
	public void setContract(IContract contract) {
		this.contract = contract;
	}
	
	@Override
	public String getSigningDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(this.getSigningDate());
	}
	
	@Override
	public String getCreateDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(this.getCreateDate());
	}
	
	@Override
	public String getChangeDatePresenter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(this.getChangeDate());
	}

}
