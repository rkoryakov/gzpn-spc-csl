package ru.gzpn.spc.csl.ui.createdoc;

import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.services.bl.interfaces.IDocumentService;

@SuppressWarnings("serial")
public class DocumentsDataProvider extends AbstractBackEndDataProvider<IDocument, Void> {
	
	private IDocumentService service;
	private DocumentFilter filter;
	
	@Override
	protected Stream<IDocument> fetchFromBackEnd(Query<IDocument, Void> query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int sizeInBackEnd(Query<IDocument, Void> query) {
		// TODO Auto-generated method stub
		return 0;
	}

}
