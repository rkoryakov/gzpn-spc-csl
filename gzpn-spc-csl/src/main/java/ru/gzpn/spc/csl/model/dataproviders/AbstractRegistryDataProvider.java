package ru.gzpn.spc.csl.model.dataproviders;

import java.util.List;

import com.vaadin.data.provider.AbstractBackEndDataProvider;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.ui.common.IGridFilter;

@SuppressWarnings("serial")
public abstract class AbstractRegistryDataProvider<T, F> extends AbstractBackEndDataProvider<T, F> {
	
	public abstract IGridFilter getFilter();

	public abstract List<ColumnSettings> getShownColumns();
	public abstract void setShownColumns(List<ColumnSettings> shownColumns);
}
