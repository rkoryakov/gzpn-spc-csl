package ru.gzpn.spc.csl.ui.admin.project;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataProjectService;

public class HProjectDataProvider extends AbstractBackEndDataProvider<IHProject, Void> {
	
	public static final Logger logger = LogManager.getLogger(HProjectDataProvider.class);
	private IDataProjectService service;
	
	HProjectDataProvider(IDataProjectService service) {
		this.service = service;
	}
	
	@Override
	protected Stream<IHProject> fetchFromBackEnd(Query<IHProject, Void> query) {
		return service.getHPRepository().findAll().stream().map(m -> (HProject)m);
	}

	@Override
	protected int sizeInBackEnd(Query<IHProject, Void> query) {
		return (int) service.getHPRepository().count();
	}

}
