package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.List;
import java.util.Map;

import ru.gzpn.spc.csl.model.enums.DocType;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

public interface IDocumentService {
	List<IDocument> getDocuments(IWorkSet workset);
	Map<DocType, String> getDocumentTypeCaptions();
}
