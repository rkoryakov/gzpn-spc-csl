package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.model.interfaces.IHProjectRepository;

@Service
@Transactional
public class ProjectDataService {
	@Autowired
	IHProjectRepository repository;

}
