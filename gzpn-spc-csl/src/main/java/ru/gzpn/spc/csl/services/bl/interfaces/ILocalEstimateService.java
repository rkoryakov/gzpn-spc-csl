package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;

public interface ILocalEstimateService extends IDataService<ILocalEstimate, ILocalEstimatePresenter> {

	List<ILocalEstimate> getLocalEstimates();
	Comparator<ILocalEstimatePresenter> getSortComparator(List<QuerySortOrder> list);
	List<ILocalEstimate> getLocalEstimatesByCalculationId(Long calculationId);
	ILocalEstimate cretaeLocalEstimateByCalculationId(ILocalEstimate le, Long calculationId);
	List<ILocalEstimate> getLocalEstimatesByIds(Collection<Long> ids);
}
