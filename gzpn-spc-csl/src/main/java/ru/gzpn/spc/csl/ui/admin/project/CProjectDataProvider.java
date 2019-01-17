package ru.gzpn.spc.csl.ui.admin.project;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataProjectService;

public class CProjectDataProvider extends AbstractBackEndDataProvider<ICProject, Void> {
	
	public static final Logger logger = LogManager.getLogger(CProjectDataProvider.class);
	private IDataProjectService service;

	CProjectDataProvider(IDataProjectService service) {
		this.service = service;
	}
	
	@Override
	protected Stream<ICProject> fetchFromBackEnd(Query<ICProject, Void> query) {
		return service.getCPRepository().findAll().stream().map(m -> (CProject)m);
	}

	@Override
	protected int sizeInBackEnd(Query<ICProject, Void> query) {
		return (int) service.getCPRepository().count();
	}
}
