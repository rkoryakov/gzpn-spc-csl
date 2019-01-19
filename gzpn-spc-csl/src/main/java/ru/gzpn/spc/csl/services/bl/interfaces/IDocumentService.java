package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;

import org.springframework.context.MessageSource;

import com.vaadin.data.provider.QuerySortOrder;

import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

public interface IDocumentService {
	List<IDocument> getDocuments(IWorkSet workset);
	long getDocumentsCount(IWorkSet workset);
	Comparator<IDocument> getSortComparator(List<QuerySortOrder> list);
	MessageSource getMessageSource();
}
