package ru.gzpn.spc.csl.services.bl;

import org.activiti.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.gzpn.spc.csl.services.bl.interfaces.IProcessService;

@Service
@Transactional
public class ProcessService implements IProcessService {

	@Autowired
	ProcessEngine processEngine;

	@Override
	public ProcessEngine getProcessEngine() {
		return processEngine;
	}
}
