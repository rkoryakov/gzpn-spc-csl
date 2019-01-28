package ru.gzpn.spc.csl.ui.contractreg;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.services.bl.ContractService.ContractFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IContractService;
import ru.gzpn.spc.csl.ui.common.AbstractRegisterDataProvider;
import ru.gzpn.spc.csl.ui.common.IRegisterFilter;

@SuppressWarnings("serial")
public class ContractDataProvider extends AbstractRegisterDataProvider<IContractPresenter, Void> {
	
	private IContractService contractService;
	private List<ColumnSettings> shownColumns;
	private Locale locale;
	private IRegisterFilter filter;
	
	public ContractDataProvider(IContractService contractService) {
		this.contractService = contractService;
		this.locale = LocaleContextHolder.getLocale();
	}

	@Override
	protected Stream<IContractPresenter> fetchFromBackEnd(Query<IContractPresenter, Void> query) {
		return contractService.getContracts().stream().map(
				item -> (IContractPresenter) new ContractPresenter(item)
					).filter(getFilter().getFilterPredicate(shownColumns))
						.sorted(contractService.getSortComparator(query.getSortOrders()));
	}

	@Override
	protected int sizeInBackEnd(Query<IContractPresenter, Void> query) {
		return (int)contractService.getContracts().stream().map(
				item -> (IContractPresenter) new ContractPresenter(item)
					).filter(getFilter().getFilterPredicate(shownColumns)).count();
	}

	public List<ColumnSettings> getShownColumns() {
		return shownColumns;
	}

	public void setShownColumns(List<ColumnSettings> shownColumns) {
		this.shownColumns = shownColumns;
	}

	@Override
	public IRegisterFilter getFilter() {
		if (filter == null) {
			filter = new ContractFilter();
		}
		return filter;
	}
}
