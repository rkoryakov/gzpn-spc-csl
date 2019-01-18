package ru.gzpn.spc.csl.ui.createdoc;

import ru.gzpn.spc.csl.model.Document;
import ru.gzpn.spc.csl.model.interfaces.IDocument;

@SuppressWarnings("serial")
public class DocumentPresenter extends Document implements IDocumentPresenter { 
	private IDocument document;

	public DocumentPresenter(IDocument document) {
		setId(document.getId());
		setChangeDate(document.getChangeDate());
		setCode(document.getCode());
		setCreateDate(document.getCreateDate());
		setLocalEstimates(document.getLocalEstimates());
		setName(document.getName());
		setType(document.getType());
		setVersion(document.getVersion());
		setWork(document.getWork());
		setWorkset(document.getWorkset());
		
		setDocument(document);
	}

	public IDocument getDocument() {
		return document;
	}

	public void setDocument(IDocument document) {
		this.document = document;
	}
}
