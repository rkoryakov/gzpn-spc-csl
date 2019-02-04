package ru.gzpn.spc.csl.ui.createdoc;

import org.springframework.context.MessageSource;

import ru.gzpn.spc.csl.model.interfaces.IDocument;

public interface IDocumentPresenter extends IDocument {
	String getTypeText(MessageSource source);
	String getWorkText();
	String getWorksetText();
	
	void setTypeByText(String code);
	
	void setDocument(IDocument document);
	IDocument getDocument();
	
	String getCreateDateText();
	String getChangeDateText();
}
