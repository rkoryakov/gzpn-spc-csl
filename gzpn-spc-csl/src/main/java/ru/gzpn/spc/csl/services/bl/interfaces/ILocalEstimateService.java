package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;

public interface ILocalEstimateService {

	List<ILocalEstimate> getLocalEstimates();
	Comparator<ILocalEstimatePresenter> getSortComparator(List<QuerySortOrder> list);

}
