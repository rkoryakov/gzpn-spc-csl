package ru.gzpn.spc.csl.ui.common;

import java.util.List;

import com.vaadin.data.provider.AbstractBackEndDataProvider;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;

@SuppressWarnings("serial")
public abstract class AbstractRegisterDataProvider<T, F> extends AbstractBackEndDataProvider<T, F> {

	public IRegisterFilter filter;
	
	public IRegisterFilter getFilter() {
		return filter;
	}

	public void setFilter(IRegisterFilter filter) {
		this.filter = filter;
	}

	public abstract List<ColumnSettings> getShownColumns();
	public abstract void setShownColumns(List<ColumnSettings> shownColumns);
}
