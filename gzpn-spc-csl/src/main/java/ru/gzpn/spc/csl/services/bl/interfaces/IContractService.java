package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IContract;
import ru.gzpn.spc.csl.ui.estimatereg.IContractPresenter;

public interface IContractService {

	List<IContract> getContracts();
	Comparator<IContractPresenter> getSortComparator(List<QuerySortOrder> list);
}
