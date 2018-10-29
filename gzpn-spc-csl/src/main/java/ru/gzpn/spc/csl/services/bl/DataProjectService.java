package ru.gzpn.spc.csl.services.bl;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.repositories.CProjectRepository;
import ru.gzpn.spc.csl.model.repositories.HProjectRepository;
import ru.gzpn.spc.csl.model.repositories.PhaseRepository;

@Service
@Transactional
public class DataProjectService {
	public static final Logger logger = LoggerFactory.getLogger(DataProjectService.class);
	@Autowired
	private HProjectRepository hpRepository;
	@Autowired
	private CProjectRepository cpRepository;
	@Autowired
	private PhaseRepository phaseRepository;

	public <T> T executeByField(Supplier<T> suplier, String field) {
		T result = null;
		
		return result;
	}
}
