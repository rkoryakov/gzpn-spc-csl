package ru.gzpn.spc.csl.ui.admin.project;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;

@SuppressWarnings("serial")
public class CProjectDataProvider extends AbstractBackEndDataProvider<ICProjectPresenter, Void> {
	
	public static final Logger logger = LogManager.getLogger(CProjectDataProvider.class);
	private IProjectService service;
	private CProjectPresenterFilter filter;
	private String[] columnIDs = new String[] {ICProjectPresenter.FIELD_NAME, 
											   ICProjectPresenter.FIELD_CODE, 
											   ICProjectPresenter.FIELD_ID,
											   ICProjectPresenter.FILED_STAGE,
											   ICProjectPresenter.FILED_PHASE, 
											   ICProjectPresenter.FILED_HPROJECT, 
											   ICProjectPresenter.FILED_MILESTONE,
											   ICProjectPresenter.FIELD_VERSION, 
											   ICProjectPresenter.FIELD_CREATE_DATE, 
											   ICProjectPresenter.FIELD_CHANGE_DATE};

	CProjectDataProvider(IProjectService service) {
		this.service = service;
		this.filter = new CProjectPresenterFilter();
	}
	
	@Override
	protected Stream<ICProjectPresenter> fetchFromBackEnd(Query<ICProjectPresenter, Void> query) {
		return service.getCPRepository().findAll().stream().map(item -> (ICProjectPresenter)new CProjectPresenter(item))
				.filter(getFilter().getFilterPredicate(columnIDs)).sorted(getSort(query.getSortOrders()));
	}

	@Override
	protected int sizeInBackEnd(Query<ICProjectPresenter, Void> query) {
		return (int) service.getCPRepository().findAll().stream().map(item -> (ICProjectPresenter)new CProjectPresenter(item))
				.filter(getFilter().getFilterPredicate(columnIDs)).count();
	}
	
	public CProjectPresenterFilter getFilter() {
		return this.filter;
	}
	
	public Comparator<ICProjectPresenter> getSort(List<QuerySortOrder> orders) {
		return this.sort(orders);
	}
	
	public Comparator<ICProjectPresenter> sort(List<QuerySortOrder> list) {
		return (a, b) -> {
			int result = 0;
			for (QuerySortOrder qso : list) {
				switch (qso.getSorted()) {		
				case ICProjectPresenter.FIELD_NAME:
					result = a.getName().compareTo(b.getName());
					break;
				case ICProjectPresenter.FIELD_CODE:
					result = a.getCode().compareTo(b.getCode());
					break;
				case ICProjectPresenter.FIELD_ID:
					result = a.getId().compareTo(b.getId());
					break;
				case ICProjectPresenter.FILED_STAGE:
					result = a.getStageCaption().compareTo(b.getStageCaption());
					break;
				case ICProjectPresenter.FILED_PHASE:
					result = a.getPhaseCaption().compareTo(b.getPhaseCaption());
					break;
				case ICProjectPresenter.FILED_HPROJECT:
					result = a.getHProjectCaption().compareTo(b.getHProjectCaption());
					break;
				case ICProjectPresenter.FILED_MILESTONE:
					result = a.getMilestoneCaption().compareTo(b.getMilestoneCaption());
					break;
				case ICProjectPresenter.FIELD_VERSION:
					result = a.getVersion().compareTo(b.getVersion());
					break;
				case ICProjectPresenter.FIELD_CREATE_DATE:
					result = a.getCreateDate().compareTo(b.getCreateDate());
					break;
				case ICProjectPresenter.FIELD_CHANGE_DATE:
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
	
	public static class CProjectPresenterFilter {
		private String commonTextFilter;
		
		private CProjectPresenterFilter() {
			
		}
	
		public String getCommonTextFilter() {
			return commonTextFilter;
		}
	
		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}
	
		public Predicate<ICProjectPresenter> getFilterPredicate(String[] columnIDs) {
			return p -> {
				boolean result = false;
				if (StringUtils.isNoneBlank(commonTextFilter)) {
					for (String column : columnIDs) {
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
		
		private boolean applyColumnFilter(ICProjectPresenter cproject, String columnID) {
			boolean result = false;
			switch (columnID) {		
			case ICProjectPresenter.FIELD_NAME:
				result = cproject.getName().toLowerCase().startsWith(commonTextFilter);
				break;
			case ICProjectPresenter.FIELD_CODE:
				result = cproject.getCode().toLowerCase().startsWith(commonTextFilter);
				break;
			case ICProjectPresenter.FIELD_ID:
				result = cproject.getId().toString().startsWith(commonTextFilter);
				break;
			case ICProjectPresenter.FILED_STAGE:
				result = cproject.getStageCaption().toLowerCase().startsWith(commonTextFilter);
				break;
			case ICProjectPresenter.FILED_PHASE:
				result = cproject.getPhaseCaption().toLowerCase().startsWith(commonTextFilter);
				break;
			case ICProjectPresenter.FILED_HPROJECT:
				result = cproject.getHProjectCaption().toLowerCase().startsWith(commonTextFilter);
				break;
			case ICProjectPresenter.FILED_MILESTONE:
				result = cproject.getMilestoneCaption().toLowerCase().startsWith(commonTextFilter);
				break;
			case ICProjectPresenter.FIELD_VERSION:
				result = cproject.getVersion().toString().toLowerCase().startsWith(commonTextFilter);
				break;
			case ICProjectPresenter.FIELD_CREATE_DATE:
				result = cproject.getCreateDate().toString().startsWith(commonTextFilter);
				break;
			case ICProjectPresenter.FIELD_CHANGE_DATE:
				result = cproject.getChangeDate().toString().startsWith(commonTextFilter);
				break;
				default:
			}
			return result;
		}
	}
}
