package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import ru.gzpn.spc.csl.model.Phase;
import ru.gzpn.spc.csl.model.interfaces.IPhase;

public interface PhaseRepository extends BaseRepository<Phase>{
	public List<IPhase> findByName(String name);
}
