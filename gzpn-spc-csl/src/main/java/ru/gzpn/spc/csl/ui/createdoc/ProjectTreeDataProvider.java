package ru.gzpn.spc.csl.ui.createdoc;

import java.util.stream.Stream;

import com.vaadin.data.provider.AbstractBackEndHierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;

import ru.gzpn.spc.csl.model.BaseEntity;
import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IPhase;
import ru.gzpn.spc.csl.model.interfaces.IStage;

public class ProjectTreeDataProvider extends AbstractBackEndHierarchicalDataProvider<BaseEntity, EntityFilter> {

	@Override
	public int getChildCount(HierarchicalQuery<BaseEntity, EntityFilter> query) {
		int result = 0;
		BaseEntity parent = query.getParent();

		if (parent instanceof ICProject) {

		} else if (parent instanceof IStage) {

		} else if (parent instanceof IPhase) {

		} else {
			// there is no parent in the IHProject

		}

		return result;
	}

	@Override
	public boolean hasChildren(BaseEntity item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Stream<BaseEntity> fetchChildrenFromBackEnd(HierarchicalQuery<BaseEntity, EntityFilter> query) {
		// TODO Auto-generated method stub
		return null;
	}
}
