package ru.gzpn.spc.csl.services.bl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.repositories.PlanObjectRepository;
import ru.gzpn.spc.csl.model.repositories.WorkSetRepository;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IWorkSetService;

@Service
@Transactional
public class WorkSetService implements IWorkSetService {
	public static final Logger logger = LoggerFactory.getLogger(WorkSetService.class);
	public static final int PAGE_SIZE = 1000;
	
	@Autowired
	private WorkSetRepository repository;
	@Autowired
	private PlanObjectRepository planObjectRepository;
	
	@Override
	public Order createSortOrder(String fieldName, Direction direction) {
		return new Order(direction, fieldName);
	}
	
	@Override
	public WorkSetFilter createWorkSetFilter() {
		return new WorkSetFilter();
	}
	
	@Override
	public Stream<IWorkSet> getAllItems(List<Order> sortOrders, int offset, int limit) {
//		int pageNumber = offset/limit;
//		PageRequest pageRequest = PageRequest.of(pageNumber, limit, Sort.by(sortOrders));
		return repository.findAll().stream().map(e -> (IWorkSet)e);
	}
	
	@Override
	public Stream<IWorkSet> getItemsByNode(NodeWrapper node, int offset, int limit) {
//		int pageNumber = offset/limit;
//		PageRequest pageRequest = PageRequest.of(pageNumber, limit);
		return getItemsByNode(node, null);
	}
	
	protected Stream<IWorkSet> getItemsByNode(NodeWrapper node, Pageable pageable) {
		List<IWorkSet> result = new ArrayList<>();
		if (node.hasParent() && node.hasId()) {
			switch(node.getEntityEnum()) {
			case HPROJECT:
				break;
			case STAGE:
				if (node.getParent().getEntityEnum() == Entities.CPROJECT) {
					result = repository.findWorkSetByCProjectId(node.getParent().getId());
				} else {
					result = repository.findWorkSetByStageId(node.getId());
				}
				break;
			case CPROJECT:
				result = repository.findWorkSetByCProjectId(node.getId());
				break;
			case PLANOBJECT:
				result = repository.findByPlanObjectId(node.getId());
				break;
			case WORKSET:
				result = repository.getItemsGroupedByFieldValue(node.getEntityName(), 
							node.getGroupField(), node.getGroupFiledValue(), IWorkSet.class).collect(Collectors.toList());
				break;
			default:
				break;
			}
		}

		return result.stream();
	}
	
	@Override
	public Comparator<IWorkSet> getSortComparator(List<QuerySortOrder> list) {
		return (a, b) -> {
			int result = 0;
			for (QuerySortOrder qso : list) {
				switch (qso.getSorted()) {		
				case IWorkSet.FIELD_NAME:
					result = a.getName().compareTo(b.getName());
					break;
				case IWorkSet.FIELD_CODE:
					result = a.getCode().compareTo(b.getCode());
					break;
				case IWorkSet.FIELD_PIR:
					result = a.getPir().getCode().compareTo(b.getPir().getCode());
					break;
				case IWorkSet.FIELD_SMR:
					result = a.getSmr().getCode().compareTo(b.getSmr().getCode());
					break;
				case IPlanObject.FIELD_MARK:
					result = a.getPlanObject().getMark().getName().compareTo(b.getPlanObject().getMark().getName());
					break;
				case IWorkSet.FIELD_ID:
					result = a.getId().compareTo(b.getId());
					break;
				case IWorkSet.FIELD_VERSION:
					result = a.getVersion().compareTo(b.getVersion());
					break;
				case IWorkSet.FIELD_CREATE_DATE:
					result = a.getCreateDate().compareTo(b.getCreateDate());
					break;
				case IWorkSet.FIELD_CHANGE_DATE:
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
	
	public static class WorkSetFilter {
		private String commonTextFilter;
		private String codeFilter;
		private String  nameFilter;
		// and some other field filters...
		
		private WorkSetFilter() {
			
		}

		public String getCommonTextFilter() {
			return commonTextFilter;
		}

		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}

		public String getCodeFilter() {
			return codeFilter;
		}

		public void setCodeFilter(String codeFilter) {
			this.codeFilter = codeFilter;
		}

		public String getNameFilter() {
			return nameFilter;
		}

		public void setNameFilter(String nameFilter) {
			this.nameFilter = nameFilter;
		}

		public Predicate<IWorkSet> filter(List<ColumnSettings> shownColumns) {
			// only common filter is working now
			return p -> {
				boolean result = false;
				//logger.debug("[filter] shownColumns {}", shownColumns);
				if (StringUtils.isNoneBlank(commonTextFilter) && Objects.nonNull(shownColumns)) {
					for (ColumnSettings column : shownColumns) {
						if (applyColumnFilter(p, column)) {
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
		
		
		private boolean applyColumnFilter(IWorkSet workset, ColumnSettings column) {
			boolean result = false;
			
			switch (column.getEntityFieldName()) {		
			case IWorkSet.FIELD_NAME:
				result = workset.getName().toLowerCase().startsWith(commonTextFilter);
				break;
			case IWorkSet.FIELD_CODE:
				result = workset.getCode().toLowerCase().startsWith(commonTextFilter);
				break;
			case IWorkSet.FIELD_PIR:
				result = workset.getPir().getCode().toLowerCase().startsWith(commonTextFilter);
				break;
			case IWorkSet.FIELD_SMR:
				result = workset.getSmr().getCode().toLowerCase().startsWith(commonTextFilter);
				break;
			case IPlanObject.FIELD_MARK:
				result = workset.getPlanObject().getMark().getName().toLowerCase().startsWith(commonTextFilter);
				break;
			case IWorkSet.FIELD_ID:
				result = workset.getId().toString().startsWith(commonTextFilter);
				break;
			case IWorkSet.FIELD_VERSION:
				result = workset.getVersion().toString().toLowerCase().startsWith(commonTextFilter);
				break;
			case IWorkSet.FIELD_CREATE_DATE:
				result = workset.getCreateDate().toString().startsWith(commonTextFilter);
				break;
			case IWorkSet.FIELD_CHANGE_DATE:
				result = workset.getChangeDate().toString().startsWith(commonTextFilter);
				break;
				default:
			}
			
			return result;
		}
	}
}
