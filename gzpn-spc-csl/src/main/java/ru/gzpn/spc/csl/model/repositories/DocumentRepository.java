package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.Document;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.interfaces.IWork;
import ru.gzpn.spc.csl.model.interfaces.IWorkSet;

@Repository
public interface DocumentRepository extends BaseRepository<Document> {
	public List<IDocument> findDocumentsByWork(IWork work);
	public List<IDocument> findDocumentsByWorkset(IWorkSet workset);
	public long getCountByWorkId(long id);
}
