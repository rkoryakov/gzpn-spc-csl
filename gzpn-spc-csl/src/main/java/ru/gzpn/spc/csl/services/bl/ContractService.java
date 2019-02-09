package ru.gzpn.spc.csl.services.bl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;

import ru.gzpn.spc.csl.model.interfaces.IContract;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.interfaces.IContractPresenter;
import ru.gzpn.spc.csl.model.repositories.ContractRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IContractService;
import ru.gzpn.spc.csl.ui.common.IRegisterFilter;

@Service
@Transactional
public class ContractService implements IContractService {

	@Autowired
	ContractRepository contractRepository;
	
	@Override
	public List<IContract> getContracts() {
		return contractRepository.findAll().stream().map(item -> (IContract)item).collect(Collectors.toList());
	}
	
	@Override
	public Comparator<IContractPresenter> getSortComparator(List<QuerySortOrder> list) {

		return (a, b) -> {
			int result = 0;
			for (QuerySortOrder qso : list) {
				switch (qso.getSorted()) {		
				case IContract.FIELD_NAME:
					result = a.getName().compareTo(b.getName());
					break;
				case IContract.FIELD_CODE:
					result = a.getCode().compareTo(b.getCode());
					break;
				case IContract.FIELD_SIGNINGDATE:
					result = a.getSigningDate().compareTo(b.getSigningDate());
					break;
				case IContract.FIELD_CUSTOMERNAME:
					result = a.getCustomerName().compareTo(b.getCustomerName());
					break;
				case IContract.FIELD_CUSTOMERINN:
					result = a.getCustomerINN().compareTo(b.getCustomerINN());
					break;
				case IContract.FIELD_CUSTORMERBANK:
					result = a.getCustormerBank().compareTo(b.getCustormerBank());
					break;
				case IContract.FIELD_EXECUTORNAME:
					result = a.getExecutorName().compareTo(b.getExecutorName());
					break;
				case IContract.FIELD_EXECUTORINN:
					result = a.getExecutorINN().compareTo(b.getExecutorINN());
					break;
				case IContract.FIELD_EXECUTORBANK:
					result = a.getExecutorBank().compareTo(b.getExecutorBank());
					break;
				case IContract.FIELD_ID:
					result = a.getId().compareTo(b.getId());
					break;
				case IContract.FIELD_VERSION:
					result = a.getVersion().compareTo(b.getVersion());
					break;
				case IContract.FIELD_CREATE_DATE:
					result = a.getCreateDate().compareTo(b.getCreateDate());
					break;
				case IContract.FIELD_CHANGE_DATE:
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
	
	
	public static final class ContractFilter implements IRegisterFilter {
		private String commonTextFilter;
		
		public ContractFilter() {

		}

		public String getCommonTextFilter() {
			return commonTextFilter;
		}

		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}

		public <T> Predicate<T> getFilterPredicate(List<ColumnSettings> shownColumns) {
			// only common filter is working now
			return p -> {
				boolean result = false;
				if (StringUtils.isNoneBlank(commonTextFilter) && Objects.nonNull(shownColumns)) {
					for (ColumnSettings column : shownColumns) {
						if (applyColumnFilter((IContract) p, column)) {
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

		private boolean applyColumnFilter(IContract contract, ColumnSettings column) {
			boolean result = false;

			if (contract instanceof IContractPresenter) {
				IContractPresenter contractPresenter = (IContractPresenter)contract;
				switch (column.getEntityFieldName()) {
				case IContract.FIELD_NAME:
					result = contractPresenter.getName().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_CODE:
					result = contractPresenter.getCode().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_SIGNINGDATE:
					result = contractPresenter.getSigningDate().toString().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_CUSTOMERNAME:
					result = contractPresenter.getCustomerName().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_CUSTOMERINN:
					result = contractPresenter.getCustomerINN().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_CUSTORMERBANK:
					result = contractPresenter.getCustormerBank().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_EXECUTORNAME:
					result = contractPresenter.getExecutorName().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_EXECUTORINN:
					result = contractPresenter.getExecutorINN().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_EXECUTORBANK:
					result = contractPresenter.getExecutorBank().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_ID:
					result = contractPresenter.getId().toString().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_VERSION:
					result = contractPresenter.getVersion().toString().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_CREATE_DATE:
					result = contractPresenter.getCreateDatePresenter().toLowerCase().startsWith(commonTextFilter);
					break;
				case IContract.FIELD_CHANGE_DATE:
					result = contractPresenter.getChangeDatePresenter().toLowerCase().startsWith(commonTextFilter);
					break;
				default:
				}
			}
			return result;
		}
	}
}
