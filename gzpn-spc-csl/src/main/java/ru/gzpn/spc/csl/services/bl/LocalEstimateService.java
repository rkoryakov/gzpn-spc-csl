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

import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;
import ru.gzpn.spc.csl.model.repositories.LocalEstimateRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;


@Service
@Transactional
public class LocalEstimateService implements ILocalEstimateService {

	@Autowired
	private LocalEstimateRepository localEstimateRepository;
	
	@Autowired
	MessageSource messageSource;
	
	@Override
	public List<ILocalEstimate> getLocalEstimates() {
		return localEstimateRepository.findAll().stream().map(item -> (ILocalEstimate)item).collect(Collectors.toList());
	}
	
	@Override
	public Comparator<ILocalEstimatePresenter> getSortComparator(List<QuerySortOrder> list) {

		return (a, b) -> {
			int result = 0;
			for (QuerySortOrder qso : list) {
				switch (qso.getSorted()) {		
				case ILocalEstimate.FIELD_NAME:
					result = a.getName().compareTo(b.getName());
					break;
				case ILocalEstimate.FIELD_CODE:
					result = a.getCode().compareTo(b.getCode());
					break;
				case ILocalEstimate.FIELD_CHANGEDBY:
					result = a.getChangedBy().compareTo(b.getChangedBy());
					break;
				case ILocalEstimate.FIELD_DRAWING:
					result = a.getDrawing().compareTo(b.getDrawing());
					break;
				case ILocalEstimate.FIELD_STATUS:
					result = a.getStatus().compareTo(b.getStatus());
					break;
				case ILocalEstimate.FIELD_COMMENT:
					result = a.getComment().compareTo(b.getComment());
					break;
				case ILocalEstimate.FIELD_DOCUMENT:
					result = a.getDocumentCaption().compareTo(b.getDocumentCaption());
					break;
				case ILocalEstimate.FIELD_STAGE:
					result = a.getStageCaption().compareTo(b.getStageCaption());
					break;
				case ILocalEstimate.FIELD_ESTIMATECALCULATION:
					result = a.getEstimateCalculationCaption().compareTo(b.getEstimateCalculationCaption());
					break;
				case ILocalEstimate.FIELD_OBJECTESTIMATE:
					result = a.getObjectEstimateCaption().compareTo(b.getObjectEstimateCaption());
					break;
				case ILocalEstimate.FIELD_ESTIMATEHEAD:
					result = a.getEstimateHeadCaption().compareTo(b.getEstimateHeadCaption());
					break;
				case ILocalEstimate.FIELD_ID:
					result = a.getId().compareTo(b.getId());
					break;
				case ILocalEstimate.FIELD_VERSION:
					result = a.getVersion().compareTo(b.getVersion());
					break;
				case ILocalEstimate.FIELD_CREATE_DATE:
					result = a.getCreateDate().compareTo(b.getCreateDate());
					break;
				case ILocalEstimate.FIELD_CHANGE_DATE:
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
	
	
	public static final class LocalEstimateFilter implements IGridFilter<ILocalEstimate> {
		private String commonTextFilter;
		
		public LocalEstimateFilter() {

		}

		public String getCommonTextFilter() {
			return commonTextFilter;
		}

		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}

		public Predicate<ILocalEstimate> getFilterPredicate(List<ColumnSettings> shownColumns) {
			// only common filter is working now
			return p -> {
				boolean result = false;
				if (StringUtils.isNoneBlank(commonTextFilter) && Objects.nonNull(shownColumns)) {
					for (ColumnSettings column : shownColumns) {
						if (applyColumnFilter((ILocalEstimate) p, column)) {
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

		private boolean applyColumnFilter(ILocalEstimate localEstimate, ColumnSettings column) {
			boolean result = false;

			if (localEstimate instanceof ILocalEstimatePresenter) {
				ILocalEstimatePresenter localEstimatePresenter = (ILocalEstimatePresenter)localEstimate;
				switch (column.getEntityFieldName()) {
				case ILocalEstimate.FIELD_NAME:
					result = localEstimatePresenter.getName().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_CODE:
					result = localEstimatePresenter.getCode().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_CHANGEDBY:
					result = localEstimatePresenter.getChangedBy().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_DRAWING:
					result = localEstimatePresenter.getDrawing().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_STATUS:
					result = localEstimatePresenter.getStatus().toString().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_COMMENT:
					result = localEstimatePresenter.getComment().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_DOCUMENT:
					result = localEstimatePresenter.getDocumentCaption().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_STAGE:
					result = localEstimatePresenter.getStageCaption().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_ESTIMATECALCULATION:
					result = localEstimatePresenter.getEstimateCalculationCaption().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_OBJECTESTIMATE:
					result = localEstimatePresenter.getObjectEstimateCaption().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_ESTIMATEHEAD:
					result = localEstimatePresenter.getEstimateHeadCaption().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_ID:
					result = localEstimatePresenter.getId().toString().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_VERSION:
					result = localEstimatePresenter.getVersion().toString().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_CREATE_DATE:
					result = localEstimatePresenter.getCreateDatePresenter().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_CHANGE_DATE:
					result = localEstimatePresenter.getChangeDatePresenter().toLowerCase().startsWith(commonTextFilter);
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
	public void save(ILocalEstimate bean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(ILocalEstimate bean) {
		// TODO Auto-generated method stub
		
	}
}
