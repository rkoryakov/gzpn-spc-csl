package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;

@Repository
public interface HProjectRepository extends BaseRepository<HProject> {
	@Query(value = "SELECT hp FROM HProject hp WHERE hp.code = ?1")
	public List<IHProject> findByCode(String code);
}
