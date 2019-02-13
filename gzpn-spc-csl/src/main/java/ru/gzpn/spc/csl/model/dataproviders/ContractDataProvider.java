package ru.gzpn.spc.csl.model.dataproviders;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.presenters.ContractPresenter;
import ru.gzpn.spc.csl.model.presenters.interfaces.IContractPresenter;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.ContractService.ContractFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IContractService;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@SuppressWarnings("serial")
public class ContractDataProvider extends AbstractRegistryDataProvider<IContractPresenter, Void> {
	
	private IContractService contractService;
	private List<ColumnSettings> shownColumns;
	private Locale locale;
	private IGridFilter<IContractPresenter> filter;
	
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
	public IGridFilter<IContractPresenter> getFilter() {
		if (filter == null) {
			filter = new ContractFilter();
		}
		return filter;
	}

	@Override
	public NodeWrapper getParentNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParentNode(NodeWrapper node) {
		// TODO Auto-generated method stub
		
	}
}
