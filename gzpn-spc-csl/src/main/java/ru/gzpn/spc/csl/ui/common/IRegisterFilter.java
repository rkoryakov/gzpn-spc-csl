package ru.gzpn.spc.csl.ui.common;

import java.util.List;
import java.util.function.Predicate;

import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;

public interface IRegisterFilter {
	public String getCommonTextFilter();
	public void setCommonTextFilter(String commonTextFilter);
	public <T> Predicate<T> getFilterPredicate(List<ColumnSettings> shownColumns);
}
