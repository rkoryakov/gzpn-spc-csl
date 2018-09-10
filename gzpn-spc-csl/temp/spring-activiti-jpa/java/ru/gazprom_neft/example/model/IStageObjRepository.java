package ru.gazprom_neft.example.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IStageObjRepository extends JpaRepository<StageObject, Long> {
	@Query(value = "SELECT * FROM spk_model.spk_stageobj S WHERE S.location->'streets'->>0 = 'street 1'", nativeQuery = true)
	public List<StageObject> findStageObjects();
}
