package ru.gzpn.spc.csl.ui.createdoc;


import java.util.List;

import org.springframework.context.MessageSource;

import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.jsontypes.ISettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.AbstractTreeGridSettingsWindow;

@SuppressWarnings("serial")
public class CreateDocSettingsWindow extends AbstractTreeGridSettingsWindow {
	private CreateDocSettingsJson changedCreateDocSettingsJson = null;
	
	public CreateDocSettingsWindow(IUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
	}

	@Override
	public NodeWrapper getTreeSettings() {
		return ((CreateDocSettingsJson)getUserSettings()).getLeftTreeGroup();
	}

	@Override
	public void setTreeSettings(NodeWrapper node) {
		((CreateDocSettingsJson)getUserSettings()).setLeftTreeGroup(node);
	}

	@Override
	public List<ColumnSettings> getColumnSettings() {
		return ((CreateDocSettingsJson)getUserSettings()).getLeftResultColumns();
	}

	@Override
	public void setColumnSettings(List<ColumnSettings> columns) {
		((CreateDocSettingsJson)getUserSettings()).setLeftResultColumns(columns);
	}

	@Override
	public List<ColumnHeaderGroup> getHeaderSettings() {
		return ((CreateDocSettingsJson)getUserSettings()).getLeftColumnHeaders();
	}

	@Override
	public void setHeaderSettings(List<ColumnHeaderGroup> headers) {
		((CreateDocSettingsJson)getUserSettings()).setLeftColumnHeaders(headers);
	}

	@Override
	public ISettingsJson getUserSettings() {
		if (changedCreateDocSettingsJson == null) {
			changedCreateDocSettingsJson = (CreateDocSettingsJson)
					settingsService.getCreateDocUserSettings(currentUser, new CreateDocSettingsJson());
		}
		return changedCreateDocSettingsJson;
	}
}
