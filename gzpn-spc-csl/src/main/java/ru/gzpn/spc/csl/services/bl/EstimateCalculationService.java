package ru.gzpn.spc.csl.services.bl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.model.Document;
import ru.gzpn.spc.csl.model.EstimateCalculation;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.enums.DocType;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCalculation;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.interfaces.IDocumentPresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.IEstimateCalculationPresenter;
import ru.gzpn.spc.csl.model.repositories.DocumentRepository;
import ru.gzpn.spc.csl.model.repositories.EstimateCalculationRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IEstimateCalculationService;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@Service
@Transactional
public class EstimateCalculationService implements IEstimateCalculationService {

	@Autowired
	EstimateCalculationRepository estimateCalculationRepository;
	@Autowired
	DocumentRepository documentRepository;
	@Autowired
	ILocalEstimateService localEstimatesService;
	
	@Autowired
	MessageSource messageSource;
	
	@Override
	public Optional<IEstimateCalculation> getEstimateCalculation(Long id) {
		Optional<IEstimateCalculation> result = Optional.empty();
		
		if (id != null) {
			result = estimateCalculationRepository.findById(id)
						.map(value -> (IEstimateCalculation)value);
		}
		
		return result;
	}
	
	@Override
	public List<IEstimateCalculation> getEstimateCalculations() {
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

	
	@Override
	public IEstimateCalculation createEstimateCalculationByDocuments(Set<IDocumentPresenter> docs) {
		IDocument firstDocument = docs.iterator().next();
		
		firstDocument = documentRepository.findById(firstDocument.getId()).get();
		ICProject project = firstDocument.getWorkset().getPlanObject().getCproject();
		
		/* creating SSR card*/
		String code  = project.getCode();
		IEstimateCalculation calculation = new EstimateCalculation();
		calculation = estimateCalculationRepository.save((EstimateCalculation)calculation);
		calculation.setCode(code + "-" + calculation.getId());
		calculation.setProject(project);
		calculation = estimateCalculationRepository.save((EstimateCalculation)calculation);
		
		/* creating local estimates */
		for (IDocumentPresenter dp : docs) {
			if (dp.getType() == DocType.LOCAL_ESTIMATE) {
				ILocalEstimate le = new LocalEstimate();
				le.setStage(dp.getWorkset().getPlanObject().getStage());
				Optional<Document> doc = documentRepository.findById(dp.getId());
				le.setDocument(doc.orElseThrow());
				le.setEstimateCalculation(calculation);
				le.setCode(dp.getCode());
				localEstimatesService.save(le);
			}
		}
		
		return calculation;
	}
}
