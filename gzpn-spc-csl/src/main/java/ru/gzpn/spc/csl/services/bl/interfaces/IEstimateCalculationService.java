package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.presenters.interfaces.IEstimateCalculationPresenter;

public interface IEstimateCalculationService extends IDataService<IEstimateCalculation, IEstimateCalculationPresenter> {

	List<IEstimateCalculation> getEstimateCalculations();
	Optional<IEstimateCalculation> getEstimateCalculation(long id);
	Comparator<IEstimateCalculationPresenter> getSortComparator(List<QuerySortOrder> list);
}
