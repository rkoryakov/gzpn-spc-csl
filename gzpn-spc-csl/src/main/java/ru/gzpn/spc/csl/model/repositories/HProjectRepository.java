package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;

@Repository
public interface HProjectRepository extends BaseRepository<HProject> {
	public List<IHProject> findByCode(String code);
}
