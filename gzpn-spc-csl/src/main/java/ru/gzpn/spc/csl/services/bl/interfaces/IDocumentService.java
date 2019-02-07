package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;

import org.springframework.context.MessageSource;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;
import ru.gzpn.spc.csl.ui.createdoc.IDocumentPresenter;

public interface IDocumentService {
	List<IDocument> getDocuments(IWorkSet workset);
	long getDocumentsCount(IWorkSet workset);
	Comparator<IDocumentPresenter> getSortComparator(List<QuerySortOrder> list);
	MessageSource getMessageSource();
	/**
	 * Save existing bean. If the bean doesn't exist then create it
	 * @param bean
	 */
	void save(IDocument bean);
	void remove(IDocument bean);
}
