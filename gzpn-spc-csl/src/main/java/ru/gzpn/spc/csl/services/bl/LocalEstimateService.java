package ru.gzpn.spc.csl.services.bl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.model.EstimateCalculation;
import ru.gzpn.spc.csl.model.EstimateCost;
import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IEstimateCost;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.IStage;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.interfaces.ILocalEstimatePresenter;
import ru.gzpn.spc.csl.model.repositories.EstimateCalculationRepository;
import ru.gzpn.spc.csl.model.repositories.EstimateCostRepository;
import ru.gzpn.spc.csl.model.repositories.LocalEstimateRepository;
import ru.gzpn.spc.csl.model.repositories.StageRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;


@Service
@Transactional
public class LocalEstimateService implements ILocalEstimateService {
	public static final Logger logger = LoggerFactory.getLogger(LocalEstimateService.class);
	
	@Autowired
	private LocalEstimateRepository localEstimateRepository;
	@Autowired
	private EstimateCalculationRepository estimateCalculationsRepository;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private IUserSettingsService userSettings;
	@Autowired
	StageRepository stageRepository;
	@Autowired 
	EstimateCostRepository estimateCost;
	
	@Override
	public List<ILocalEstimate> getLocalEstimatesByCalculationId(Long calculationId) {
		List<ILocalEstimate> result = new ArrayList<>();
		if (calculationId != null) {
			Optional<EstimateCalculation> c = estimateCalculationsRepository.findById(calculationId);
			if (c.isPresent()) {
				result = localEstimateRepository.findByEstimateCalculation(c.get());
			}
		}
		return result;
	}
	
	/**
	 * Persists the given le and links it to the calculationId
	 */
	@Override
	public ILocalEstimate cretaeLocalEstimateByCalculationId(ILocalEstimate le, Long calculationId) {
		ILocalEstimate result = null;
		
		result = localEstimateRepository.save((LocalEstimate)le);
		
		if (calculationId != null) {
			Optional<EstimateCalculation> c = estimateCalculationsRepository.findById(calculationId);
			if (c.isPresent()) {
				result.setEstimateCalculation(c.get());
				result = localEstimateRepository.save((LocalEstimate)result);
			}
		}
		
		return result;
	}
	
	@Override
	public List<ILocalEstimate> getLocalEstimatesByIds(Collection<Long> ids) {
		List<ILocalEstimate> result = java.util.Collections.emptyList();
		result = localEstimateRepository.findAllById(ids)
					.stream().map(item -> (ILocalEstimate)item)
						.collect(Collectors.toList());
		return result;
	}
	
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
	
	
	public static final class LocalEstimateFilter implements IGridFilter<ILocalEstimatePresenter> {
		private String commonTextFilter;
		
		public LocalEstimateFilter() {

		}

		public String getCommonTextFilter() {
			return commonTextFilter;
		}

		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}

		public Predicate<ILocalEstimatePresenter> getFilterPredicate(List<ColumnSettings> shownColumns) {
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

			if (localEstimate instanceof ILocalEstimate) {
				ILocalEstimatePresenter localEstimatePresenter = (ILocalEstimatePresenter)localEstimate;
				switch (column.getEntityFieldName()) {
				case ILocalEstimate.FIELD_NAME:
					result = localEstimatePresenter.getName().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_CODE:
					result = localEstimatePresenter.getCode().toLowerCase().startsWith(commonTextFilter);
					break;
				case ILocalEstimate.FIELD_CHANGEDBY:
					if (localEstimatePresenter.getChangedBy() != null) {
						result = localEstimatePresenter.getChangedBy().toLowerCase().startsWith(commonTextFilter);
					}
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
		Optional<LocalEstimate> ole;
		if (bean.getId() != null) {
			ole = localEstimateRepository.findById(bean.getId());
		} else if (bean.getCode() != null) {
			ole = localEstimateRepository.findByCode(bean.getCode()).or(() -> Optional.of((LocalEstimate)bean));
		} else {
			ole = Optional.of((LocalEstimate)bean);
		}
		
		if (ole.isPresent()) {
			LocalEstimate le = ole.get();
			
			if (bean.getStage() != null) {
				IStage stage = stageRepository.findByCode(bean.getStage().getCode());
				if (stage != null) {
					le.setStage(stage);
				} else {
					le.setStage(null);
				}
			}
			
			le.setChangedBy(bean.getChangedBy());
			le.setCode(bean.getCode());
			le.setComment(bean.getComment());
			
			le.setDocument(bean.getDocument());
			le.setDrawing(bean.getDrawing());
			le.setEstimateCalculation(bean.getEstimateCalculation());
			
			if (bean.getEstimateCosts() != null) {
				List<IEstimateCost> estimateCosts = bean.getEstimateCosts();
				List<IEstimateCost> savedCosts = new ArrayList<>();
				for (IEstimateCost cost : estimateCosts) {
					savedCosts.add(estimateCost.save((EstimateCost)cost));
				}
				le.getEstimateCosts().addAll(savedCosts);
			}
			
			le.setEstimateHead(bean.getEstimateHead());
			le.setHistory(bean.getHistory());
			le.setName(bean.getName());
			le.setObjectEstimate(bean.getObjectEstimate());
			
			le.setStatus(bean.getStatus());
			le.setWorks(bean.getWorks());
			
			localEstimateRepository.save(le);
		}
	}

	@Override
	public void remove(ILocalEstimate bean) {
		if (bean.getId() != null) {
			localEstimateRepository.deleteById(bean.getId());
		}
	}
}
