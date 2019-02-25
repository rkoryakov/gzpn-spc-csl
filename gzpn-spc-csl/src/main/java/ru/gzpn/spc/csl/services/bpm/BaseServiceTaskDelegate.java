package ru.gzpn.spc.csl.services.bpm;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseServiceTaskDelegate implements JavaDelegate {
	public static final Logger logger = LoggerFactory.getLogger(BaseServiceTaskDelegate.class);
	protected Expression field1;
	 
	@Override
	public void execute(DelegateExecution execution) {
		String field = (String) field1.getValue(execution);
		logger.debug("viewId", field);
	}
}
