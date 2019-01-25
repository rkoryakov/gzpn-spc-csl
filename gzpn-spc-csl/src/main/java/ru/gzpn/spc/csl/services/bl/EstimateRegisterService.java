package ru.gzpn.spc.csl.services.bl;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.repositories.EstimateRegisterRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateRegisterService;
import ru.gzpn.spc.csl.ui.estimatereg.IEstimateCalculationPresenter;

@Service
@Transactional
public class EstimateRegisterService implements IEstimateRegisterService {
	
	@Autowired
	EstimateRegisterRepository estimateRegisterRepository;

	@Override
	public List<IEstimateCalculation> getEstimateCalculation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparator<IEstimateCalculationPresenter> getSortComparator(List<QuerySortOrder> list) {
		// TODO Auto-generated method stub
		return null;
	}

}
