package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.Document;
import ru.gzpn.spc.csl.model.interfaces.IWork;

@Repository
public interface DocumentRepository extends BaseRepository<Document> {
	public List<Document> findDocumentsByWork(IWork work);
}
