package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.ui.estimatereg.IEstimateCalculationPresenter;

public interface IEstimateCalculationService {

	List<IEstimateCalculation> getEstimateCalculation();
	Comparator<IEstimateCalculationPresenter> getSortComparator(List<QuerySortOrder> list);
}
