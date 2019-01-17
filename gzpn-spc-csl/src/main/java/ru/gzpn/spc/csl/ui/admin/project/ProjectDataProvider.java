package ru.gzpn.spc.csl.ui.admin.project;

import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataProjectService;

public class ProjectDataProvider extends AbstractBackEndDataProvider<ICProject, Void> {

	private IDataProjectService service;
	private NodeWrapper parentNode;
	
	ProjectDataProvider(IDataProjectService service) {
		this.service = service;
		this.parentNode = null;
	}
	
	@Override
	protected Stream<ICProject> fetchFromBackEnd(Query<ICProject, Void> query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int sizeInBackEnd(Query<ICProject, Void> query) {
		// TODO Auto-generated method stub
		return 0;
	}
}
