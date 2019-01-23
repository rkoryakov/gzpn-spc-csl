package ru.gzpn.spc.csl.ui.createdoc;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.context.i18n.LocaleContextHolder;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.services.bl.DocumentService.DocumentFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IDocumentService;

@SuppressWarnings("serial")
public class DocumentsDataProvider extends AbstractBackEndDataProvider<IDocumentPresenter, Void> {

	private IDocumentService documentService;
	private DocumentFilter filter;
	private List<ColumnSettings> shownColumns;
	private IWorkSet parentWorkSet;
	private Locale locale;
	
	public DocumentsDataProvider(IDocumentService documentService) {
		this.documentService = documentService;
		this.filter = new DocumentFilter(documentService.getMessageSource());
		this.locale = LocaleContextHolder.getLocale();
	}
	
	@Override
	protected Stream<IDocumentPresenter> fetchFromBackEnd(Query<IDocumentPresenter, Void> query) {
		Stream<IDocumentPresenter> result = Stream.empty();
		if (!Objects.isNull(parentWorkSet)) { 
			result = documentService.getDocuments(parentWorkSet).stream().map(
								item -> (IDocumentPresenter) new DocumentPresenter(item)
							).filter(getFilter().getFilterPredicate(shownColumns))
								.sorted(documentService.getSortComparator(query.getSortOrders()));
		}
		return result;
	}

	@Override
	protected int sizeInBackEnd(Query<IDocumentPresenter, Void> query) {
		int result = 0;
		if (!Objects.isNull(parentWorkSet)) { 
		result  = (int)documentService.getDocuments(parentWorkSet).stream().map(
					item -> (IDocumentPresenter) new DocumentPresenter(item)
					).filter(getFilter().getFilterPredicate(shownColumns)).count();
		}
		return result;
	}
	
	public DocumentFilter getFilter() {
		return filter;
	}

	public void setFilter(DocumentFilter filter) {
		this.filter = filter;
	}

	public List<ColumnSettings> getShownColumns() {
		return shownColumns;
	}

	public void setShownColumns(List<ColumnSettings> shownColumns) {
		this.shownColumns = shownColumns;
	}

	public IWorkSet getParentWorkSet() {
		return parentWorkSet;
	}

	public void setParentWorkSet(IWorkSet parentWorkSet) {
		this.parentWorkSet = parentWorkSet;
	}
}
