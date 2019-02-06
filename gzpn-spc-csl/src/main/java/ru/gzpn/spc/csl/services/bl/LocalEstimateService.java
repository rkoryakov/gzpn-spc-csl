package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.repositories.LocalEstimateRepository;
import ru.gzpn.spc.csl.services.bl.interfaces.ILocalEstimateService;


@Service
@Transactional
public class LocalEstimateService implements ILocalEstimateService {

	@Autowired
	private LocalEstimateRepository localEstimateRepository;
	
	
}
