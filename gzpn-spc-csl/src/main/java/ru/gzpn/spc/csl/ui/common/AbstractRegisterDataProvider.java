package ru.gzpn.spc.csl.ui.common;

import java.util.List;

import com.vaadin.data.provider.AbstractBackEndDataProvider;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;

@SuppressWarnings("serial")
public abstract class AbstractRegisterDataProvider<T, F> extends AbstractBackEndDataProvider<T, F> {
	
	public abstract IRegisterFilter getFilter();

	public abstract List<ColumnSettings> getShownColumns();
	public abstract void setShownColumns(List<ColumnSettings> shownColumns);
}
