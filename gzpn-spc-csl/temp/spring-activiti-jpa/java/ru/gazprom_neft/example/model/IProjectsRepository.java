package ru.gazprom_neft.example.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProjectsRepository extends JpaRepository<Project, Long> {
	// @Query(value = "SELECT * FROM USERS WHERE EMAIL_ADDRESS = ?1", nativeQuery =
	// true)
	@Query(value = "SELECT * FROM spk_model.spk_projects S WHERE S.location->'subLocation'->'streets'->>0 = 'street 4'", nativeQuery = true)
	public List<Project> findStageObjects();
}
