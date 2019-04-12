package ru.gzpn.spc.csl.services.bl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

import ru.gzpn.spc.csl.model.Contract;
import ru.gzpn.spc.csl.model.enums.ContractType;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IContract;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.interfaces.IDocumentPresenter;
import ru.gzpn.spc.csl.model.repositories.ContractRepository;
import ru.gzpn.spc.csl.model.repositories.DocumentRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.IContractCardService;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@Service
@Transactional
public class ContractCardService implements IContractCardService {

	@Autowired
	ContractRepository contractRepository;
	@Autowired 
	MessageSource messageSource;
	@Autowired
	IUserSettingsService userSettingsService; 
	@Autowired
	IProcessService processService;
	@Autowired
	IProjectService projectService;
	@Autowired
	ILocalEstimateService estimatesService;
	@Autowired
	private DocumentRepository documentRepository;
	
	@Override
	public List<IContract> getContracts() {
		return contractRepository.findAll().stream().map(item -> (IContract)item).collect(Collectors.toList());
	}
	
	@Override
	public IContract getContract(Long id) {
		return (IContract)contractRepository.getOne(id);
	}
	
	@Override
	public Comparator<IContract> getSortComparator(List<QuerySortOrder> list) {

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
	
	
	public static final class ContractFilter implements IGridFilter<IContract> {
		private String commonTextFilter;
		
		public ContractFilter() {

		}

		public String getCommonTextFilter() {
			return commonTextFilter;
		}

		public void setCommonTextFilter(String commonTextFilter) {
			this.commonTextFilter = commonTextFilter.toLowerCase();
		}

		public Predicate<IContract> getFilterPredicate(List<ColumnSettings> shownColumns) {
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

			if (contract instanceof IContract) {
				IContract contractPresenter = (IContract)contract;
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
	public IUserSettingsService getUserSettingsService() {
		return userSettingsService;
	}

	@Override
	public IProcessService getProcessService() {
		return processService;
	}
	
	@Override
	public ILocalEstimateService getLocalEstimateService() {
		return this.estimatesService;
	}
	
	@Override
	public IProjectService getProjectService() {
		return this.projectService;
	}
	
	@Override
	public IContract createContract(Set<IDocumentPresenter> docs) {
		IDocument firstDocument = docs.iterator().next();
		
		firstDocument = documentRepository.findById(firstDocument.getId()).get();
		ICProject project = firstDocument.getWorkset().getPlanObject().getCproject();
		
		IContract contract = new Contract();
		contract = contractRepository.save((Contract)contract);
		contract.setCode(project.getCode() + "-" + contract.getId());
		contract.setContractType(ContractType.CONTRACT);
		contract.setProject(project);
		return contract;
	}
}
