package ru.gzpn.spc.csl.model.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.CProject;

@Repository
public interface ICProjectRepository extends JpaRepository<CProject, Long> {

	
}
