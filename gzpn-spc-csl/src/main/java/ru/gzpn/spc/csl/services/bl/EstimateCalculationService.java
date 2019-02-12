package ru.gzpn.spc.csl.services.bl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.interfaces.IEstimateCalculationPresenter;
import ru.gzpn.spc.csl.model.repositories.EstimateCalculationRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@Service
@Transactional
public class EstimateCalculationService implements IEstimateCalculationService {

	@Autowired
	EstimateCalculationRepository estimateCalculationRepository;
	
	@Autowired
	MessageSource messageSource;
	
	@Override
	public List<IEstimateCalculation> getEstimateCalculation() {
		return estimateCalculationRepository.findAll().stream().map(item -> (IEstimateCalculation)item).collect(Collectors.toList());
	}
	@Override
	public Comparator<IEstimateCalculationPresenter> getSortComparator(List<QuerySortOrder> list) {

		return (a, b) -> {
			int result = 0;
			for (QuerySortOrder qso : list) {
				switch (qso.getSorted()) {		
				case IEstimateCalculation.FIELD_NAME:
					result = a.getName().compareTo(b.getName());
					break;
				case IEstimateCalculation.FIELD_CODE:
					result = a.getCode().compareTo(b.getCode());
					break;
				case IEstimateCalculation.FILED_HANDLER:
					result = a.getHandler().compareTo(b.getHandler());
					break;
				case IEstimateCalculation.FILED_CPROJECT:
					result = a.getCProjectCaption().compareTo(b.getCProjectCaption());
					break;
				case IEstimateCalculation.FIELD_ID:
					result = a.getId().compareTo(b.getId());
					break;
				case IEstimateCalculation.FIELD_VERSION:
					result = a.getVersion().compareTo(b.getVersion());
					break;
				case IEstimateCalculation.FIELD_CREATE_DATE:
					result = a.getCreateDate().compareTo(b.getCreateDate());
					break;
				case IEstimateCalculation.FIELD_CHANGE_DATE:
					result = a.getChangeDate().compareTo(b.getChangeDate());
					break;
					default:
				}
				if (qso.getDirection() == SortDirection.DESCENDING) {
					result *= -1;
				}
			}
			return result;
		};
	}
	
	public static final class EstimateCalculationFilter implements IGridFilter<IEstimateCalculationPresenter> {
		private String commonTextFilter;
		
		public EstimateCalculationFilter() {

		}

		public String getCommonTextFilter() {
			return commonTextFilter;
		}

		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}

		public Predicate<IEstimateCalculationPresenter> getFilterPredicate(List<ColumnSettings> shownColumns) {
			// only common filter is working now
			return p -> {
				boolean result = false;
				if (StringUtils.isNoneBlank(commonTextFilter) && Objects.nonNull(shownColumns)) {
					for (ColumnSettings column : shownColumns) {
						if (applyColumnFilter((IEstimateCalculation)p, column)) {
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

		private boolean applyColumnFilter(IEstimateCalculation estimateCalculation, ColumnSettings column) {
			boolean result = false;

			if (estimateCalculation instanceof IEstimateCalculationPresenter) {
				IEstimateCalculationPresenter estimateCalculationPresenter = (IEstimateCalculationPresenter)estimateCalculation;
				switch (column.getEntityFieldName()) {
				case IEstimateCalculation.FIELD_NAME:
					result = estimateCalculationPresenter.getName().toLowerCase().startsWith(commonTextFilter);
					break;
				case IEstimateCalculation.FIELD_CODE:
					result = estimateCalculationPresenter.getCode().toLowerCase().startsWith(commonTextFilter);
					break;
				case IEstimateCalculation.FILED_HANDLER:
					result = estimateCalculationPresenter.getHandler().toLowerCase().startsWith(commonTextFilter);
					break;
				case IEstimateCalculation.FILED_CPROJECT:
					result = estimateCalculationPresenter.getCProjectCaption().toLowerCase().startsWith(commonTextFilter);
					break;
				case IEstimateCalculation.FIELD_ID:
					result = estimateCalculationPresenter.getId().toString().startsWith(commonTextFilter);
					break;
				case IEstimateCalculation.FIELD_VERSION:
					result = estimateCalculationPresenter.getVersion().toString().toLowerCase().startsWith(commonTextFilter);
					break;
				case IEstimateCalculation.FIELD_CREATE_DATE:
					result = estimateCalculationPresenter.getCreateDatePresenter().toLowerCase().startsWith(commonTextFilter);
					break;
				case IEstimateCalculation.FIELD_CHANGE_DATE:
					result = estimateCalculationPresenter.getChangeDatePresenter().toLowerCase().startsWith(commonTextFilter);
					break;
				default:
				}
			}
			return result;
		}
	}

	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}
	
	@Override
	public void save(IEstimateCalculation bean) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void remove(IEstimateCalculation bean) {
		// TODO Auto-generated method stub
		
	}
}
