package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import ru.gzpn.spc.csl.model.Stage;
import ru.gzpn.spc.csl.model.interfaces.IStage;

public interface StageRepository extends BaseRepository<Stage> {
	public List<IStage> findByName(String stage);
}
