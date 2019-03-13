package ru.gzpn.spc.csl.model.jsontypes;

import java.util.List;

import ru.gzpn.spc.csl.model.utils.NodeWrapper;

public interface ISettingsJson {
	boolean isShownTree();
	NodeWrapper getTreeSettings();
	List<ColumnSettings> getColumns();
	List<ColumnHeaderGroup> getHeaders();
}
