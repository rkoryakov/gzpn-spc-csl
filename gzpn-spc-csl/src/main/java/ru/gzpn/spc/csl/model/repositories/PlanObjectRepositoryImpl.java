package ru.gzpn.spc.csl.model.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PlanObjectRepositoryImpl implements IPlanObjectRepository {
	@PersistenceContext
	private EntityManager em;
	
	
}
