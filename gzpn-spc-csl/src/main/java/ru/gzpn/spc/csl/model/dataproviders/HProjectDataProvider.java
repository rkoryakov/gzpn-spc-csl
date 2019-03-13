package ru.gzpn.spc.csl.model.dataproviders;

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

import ru.gzpn.spc.csl.model.presenters.HProjectPresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.IHProjectPresenter;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;

@SuppressWarnings("serial")
public class HProjectDataProvider extends AbstractBackEndDataProvider<IHProjectPresenter, Void> {
	
	public static final Logger logger = LogManager.getLogger(HProjectDataProvider.class);
	private IProjectService service;
	private HProjectPresenterFilter filter;
	private String[] columnIDs = new String[] {IHProjectPresenter.FIELD_NAME, 
											   IHProjectPresenter.FIELD_CODE, 
											   IHProjectPresenter.FIELD_ID,
											   IHProjectPresenter.FIELD_VERSION, 
											   IHProjectPresenter.FIELD_CREATE_DATE, 
											   IHProjectPresenter.FIELD_CHANGE_DATE};
	
	public HProjectDataProvider(IProjectService service) {
		this.service = service;
		this.filter = new HProjectPresenterFilter();
	}
	
	@Override
	protected Stream<IHProjectPresenter> fetchFromBackEnd(Query<IHProjectPresenter, Void> query) {

		return service.getHPRepository().findAll().stream().map(item -> (IHProjectPresenter)new HProjectPresenter(item))
					.filter(getFilter().getFilterPredicate(columnIDs)).sorted(getSort(query.getSortOrders()));
	}

	@Override
	protected int sizeInBackEnd(Query<IHProjectPresenter, Void> query) {
		return (int) service.getHPRepository().findAll().stream().map(item -> (IHProjectPresenter)new HProjectPresenter(item))
				.filter(getFilter().getFilterPredicate(columnIDs)).count();
	}
	
	public HProjectPresenterFilter getFilter() {
		return this.filter;
	}
	
	public Comparator<IHProjectPresenter> getSort(List<QuerySortOrder> orders) {
		return this.sort(orders);
	}
	
	public Comparator<IHProjectPresenter> sort(List<QuerySortOrder> list) {
		return (a, b) -> {
			int result = 0;
			for (QuerySortOrder qso : list) {
				switch (qso.getSorted()) {		
				case IHProjectPresenter.FIELD_NAME:
					result = a.getName().compareTo(b.getName());
					break;
				case IHProjectPresenter.FIELD_CODE:
					result = a.getCode().compareTo(b.getCode());
					break;
				case IHProjectPresenter.FIELD_ID:
					result = a.getId().compareTo(b.getId());
					break;
				case IHProjectPresenter.FIELD_VERSION:
					result = a.getVersion().compareTo(b.getVersion());
					break;
				case IHProjectPresenter.FIELD_CREATE_DATE:
					result = a.getCreateDate().compareTo(b.getCreateDate());
					break;
				case IHProjectPresenter.FIELD_CHANGE_DATE:
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

	public static class HProjectPresenterFilter {
		private String commonTextFilter;
		
		private HProjectPresenterFilter() {
			
		}
	
		public String getCommonTextFilter() {
			return commonTextFilter;
		}
	
		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}
	
		public Predicate<IHProjectPresenter> getFilterPredicate(String[] columnIDs) {
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
		
		private boolean applyColumnFilter(IHProjectPresenter hproject, String columnID) {
			boolean result = false;
			switch (columnID) {		
			case IHProjectPresenter.FIELD_NAME:
				result = hproject.getName().toLowerCase().startsWith(commonTextFilter);
				break;
			case IHProjectPresenter.FIELD_CODE:
				result = hproject.getCode().toLowerCase().startsWith(commonTextFilter);
				break;
			case IHProjectPresenter.FIELD_ID:
				result = hproject.getId().toString().startsWith(commonTextFilter);
				break;
			case IHProjectPresenter.FIELD_VERSION:
				result = hproject.getVersion().toString().toLowerCase().startsWith(commonTextFilter);
				break;
			case IHProjectPresenter.FIELD_CREATE_DATE:
				result = hproject.getCreateDate().toString().startsWith(commonTextFilter);
				break;
			case IHProjectPresenter.FIELD_CHANGE_DATE:
				result = hproject.getChangeDate().toString().startsWith(commonTextFilter);
				break;
				default:
			}
			return result;
		}
	}
}
