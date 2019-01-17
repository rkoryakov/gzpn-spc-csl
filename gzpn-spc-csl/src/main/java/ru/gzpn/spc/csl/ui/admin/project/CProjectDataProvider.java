package ru.gzpn.spc.csl.ui.admin.project;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.services.bl.interfaces.IDataProjectService;

public class CProjectDataProvider extends AbstractBackEndDataProvider<ICProjectPresenter, Void> {
	
	public static final Logger logger = LogManager.getLogger(CProjectDataProvider.class);
	private IDataProjectService service;

	CProjectDataProvider(IDataProjectService service) {
		this.service = service;
	}
	
	@Override
	protected Stream<ICProjectPresenter> fetchFromBackEnd(Query<ICProjectPresenter, Void> query) {
		return service.getCPRepository().findAll().stream().map(item -> new CProjectPresenter(item));
	}

	@Override
	protected int sizeInBackEnd(Query<ICProjectPresenter, Void> query) {
		return (int) service.getCPRepository().count();
	}
}
