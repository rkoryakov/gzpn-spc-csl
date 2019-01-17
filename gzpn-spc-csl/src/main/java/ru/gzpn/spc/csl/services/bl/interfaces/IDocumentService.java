package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.List;

import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

public interface IDocumentService {
	List<IDocument> getDocuments(IWorkSet workset);

	void initI18n();
}
