package ru.gzpn.spc.csl.ui.createdoc;

import ru.gzpn.spc.csl.model.interfaces.IDocument;

public interface IDocumentPresenter extends IDocument {
	String getWorkText();
	String getWorksetText();
	
	void setDocument(IDocument document);
	IDocument getDocument();
	
	String getCreateDateText();
	String getChangeDateText();
}
