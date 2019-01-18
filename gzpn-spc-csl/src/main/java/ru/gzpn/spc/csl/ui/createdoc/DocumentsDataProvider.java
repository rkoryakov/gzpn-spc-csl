package ru.gzpn.spc.csl.ui.createdoc;

import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.services.bl.DocumentService.DocumentFilter;
import ru.gzpn.spc.csl.services.bl.interfaces.IDocumentService;

@SuppressWarnings("serial")
public class DocumentsDataProvider extends AbstractBackEndDataProvider<IDocument, Void> {

	private IDocumentService documentService;
	private DocumentFilter filter;
	
	public DocumentsDataProvider(IDocumentService documentService) {
		this.documentService = documentService;
		this.filter = new DocumentFilter(documentService.getDocumentTypeCaptions());
	}
	
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
