package ru.gzpn.spc.csl.model.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.CProject;

@Repository
public interface CProjectRepository extends BaseRepository<CProject> {
	public Optional<CProject> findCProjectByCode(String code);
}
