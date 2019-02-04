package ru.gzpn.spc.csl.ui.createdoc;

import java.time.format.DateTimeFormatter;

import org.springframework.context.MessageSource;

import ru.gzpn.spc.csl.model.Document;
import ru.gzpn.spc.csl.model.enums.DocType;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public class DocumentPresenter extends Document implements IDocumentPresenter, I18n {
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

	@Override
	public IDocument getDocument() {
		return document;
	}

	@Override
	public void setDocument(IDocument document) {
		this.document = document;
	}

	@Override
	public String getTypeText(MessageSource source) {
		return this.getType().getText(source, getLocale());
	}

	@Override
	public String getWorkText() {
		return getWork().getName();
	}

	@Override
	public String getWorksetText() {
		// TODO Auto-generated method stub
		return getWorkset().getName();
	}
	
	@Override
	public String getCreateDateText() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(getCreateDate());
	}
	
	@Override
	public String getChangeDateText() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss").format(getChangeDate());
	}

	@Override
	public void setTypeByText(String text) {
		this.setType(DocType.getByText(text));
	}
}
