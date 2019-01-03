package ru.gzpn.spc.csl.services.bl;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import ru.gzpn.spc.csl.model.interfaces.IPlanObject;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.repositories.PlanObjectRepository;
import ru.gzpn.spc.csl.model.repositories.WorkSetRepository;
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
	public Stream<IWorkSet> getItems(List<Order> sortOrders, int offset, int limit) {
		int pageNumber = offset/limit;
		PageRequest pageRequest = PageRequest.of(pageNumber, limit, Sort.by(sortOrders));
		return repository.findAll(pageRequest).stream().map(e -> (IWorkSet)e);
	}
	
	@Override
	// TODO: check for improving...
	public Stream<IWorkSet> getItems(Long planObjId, List<Order> sortOrders, int offset, int limit) {
		int pageNumber = offset/limit;
		IPlanObject planObject = planObjectRepository.findById(planObjId).get();
		//planObject.setId(planObjId);
		PageRequest pageRequest = PageRequest.of(pageNumber, limit, Sort.by(sortOrders));
		return repository.findByPlanObject(planObject, pageRequest).stream();
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
			// only common filter works
			return p -> {
				if (StringUtils.isNoneBlank(commonTextFilter)) {
					for (ColumnSettings column : shownColumns) {
						if (applyColumnFilter(p, column)) {
							return true;
						}
					}		
				}
				return false;
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
			case IWorkSet.FIELD_PLAN_OBJECT:
				result = workset.getPlanObject().getCode().toLowerCase().startsWith(commonTextFilter);
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
