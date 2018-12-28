package ru.gzpn.spc.csl.ui.createdoc;

import java.util.Objects;
import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

import ru.gzpn.spc.csl.model.WorkSet;
import ru.gzpn.spc.csl.ui.common.NodeFilter;

public class WorksetDataProvider extends AbstractBackEndDataProvider<WorkSet, Void> {
	private static final long serialVersionUID = 1L;

	private NodeFilter filter;
	
	WorksetDataProvider() {
		filter = new NodeFilter("");
	}
	
	WorksetDataProvider(NodeFilter filter) {
		if (!Objects.isNull(filter)) {
			this.filter = filter;
		}
	}
	
	@Override
	protected Stream<WorkSet> fetchFromBackEnd(Query<WorkSet, Void> query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int sizeInBackEnd(Query<WorkSet, Void> query) {
		// TODO Auto-generated method stub
		return 0;
	}
}
