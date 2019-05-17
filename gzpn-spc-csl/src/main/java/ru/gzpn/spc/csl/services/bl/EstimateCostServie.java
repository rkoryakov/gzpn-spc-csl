package ru.gzpn.spc.csl.services.bl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.model.EstimateCost;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.repositories.EstimateCalculationRepository;
import ru.gzpn.spc.csl.model.repositories.EstimateCostRepository;
import ru.gzpn.spc.csl.model.repositories.LocalEstimateRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCostService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@Service
@Transactional
public class EstimateCostServie implements IEstimateCostService {

	@Autowired
	private EstimateCostRepository estimateCostRepository;
	@Autowired
	private EstimateCalculationRepository estimateCalculationsRepository;
	@Autowired
	LocalEstimateRepository localEstimateRepository;
	
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private IUserSettingsService userSettings;
	
	@Override
	public List<IEstimateCost> getEstimateCostsByLocal(Long localEstimateId) {
		List<IEstimateCost> result = new ArrayList<>();
		if (localEstimateId != null) {
			Optional<LocalEstimate> c = localEstimateRepository.findById(localEstimateId);
			if (c.isPresent()) {
				result = c.get().getEstimateCosts();
				Hibernate.initialize(result);
			}
		}
		return result;
	}
	
	/**
	 * Persists the given le and links it to the calculationId
	 */
	@Override
	public IEstimateCost createEstimateCostByLocal(IEstimateCost ec, Long localEstimateId) {
		IEstimateCost result = null;
		
		result = estimateCostRepository.save((EstimateCost)ec);
		
		if (localEstimateId != null) {
			Optional<LocalEstimate> localEstimate = localEstimateRepository.findById(localEstimateId);
			if (localEstimate.isPresent()) {
				result.setLocalEstimate(localEstimate.get());
				result = estimateCostRepository.save((EstimateCost)result);
			}
		}
		
		return result;
	}
	
	
	@Override
	public Comparator<IEstimateCost> getSortComparator(List<QuerySortOrder> list) {

		return (a, b) -> {
			int result = 0;
			for (QuerySortOrder qso : list) {
				switch (qso.getSorted()) {		
				case IEstimateCost.FIELD_CREATE_DATE:
					break;
				}
				if (qso.getDirection() == SortDirection.DESCENDING) {
					result *= -1;
				}
			}
			return result;
		};
	}
	
	
	public static final class EstimateCostFilter implements IGridFilter<IEstimateCost> {
		private String commonTextFilter;
		
		public EstimateCostFilter() {

		}

		public String getCommonTextFilter() {
			return commonTextFilter;
		}

		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}

		public Predicate<IEstimateCost> getFilterPredicate(List<ColumnSettings> shownColumns) {
			// only common filter is working now
			return p -> {
				boolean result = false;
				if (StringUtils.isNoneBlank(commonTextFilter) && Objects.nonNull(shownColumns)) {
					for (ColumnSettings column : shownColumns) {
						if (applyColumnFilter((IEstimateCost) p, column)) {
							result = true;
							break;
						}
					}
				} else {
					result = true;
				}
				return result;
			};
		}

		private boolean applyColumnFilter(IEstimateCost estimateCost, ColumnSettings column) {
			boolean result = false;

			IEstimateCost localEstimatePresenter = (IEstimateCost)estimateCost;
			switch (column.getEntityFieldName()) {
			
			}
			
			return result;
		}
	}


	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	
	@Override
	public void save(IEstimateCost bean) {
		estimateCostRepository.save((EstimateCost)bean);
	}

	@Override
	public void remove(IEstimateCost bean) {
		if (bean.getId() != null) {
			estimateCostRepository.deleteById(bean.getId());
		}
	}

}
