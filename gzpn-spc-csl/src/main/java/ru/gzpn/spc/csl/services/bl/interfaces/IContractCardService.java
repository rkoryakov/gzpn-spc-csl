package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IContract;
import ru.gzpn.spc.csl.model.presenters.interfaces.IDocumentPresenter;

public interface IContractCardService extends IUIService {
	List<IContract> getContracts();
	Comparator<IContract> getSortComparator(List<QuerySortOrder> list);
	ILocalEstimateService getLocalEstimateService();
	IProjectService getProjectService();
	IContract getContract(Long id);
	IContract createContract(Set<IDocumentPresenter> docs);
}
