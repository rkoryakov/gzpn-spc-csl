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
public class WorkSetDocumentationSettingsWindow extends AbstractTreeGridSettingsWindow {

	private CreateDocSettingsJson changedCreateDocSettingsJson = null;
	
	public WorkSetDocumentationSettingsWindow(IUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
	}

	@Override
	public NodeWrapper getTreeSettings() {
		return ((CreateDocSettingsJson)getUserSettings()).getRightTreeGroup();
	}

	@Override
	public void setTreeSettings(NodeWrapper node) {
		((CreateDocSettingsJson)getUserSettings()).setRightTreeGroup(node);
	}

	@Override
	public List<ColumnSettings> getColumnSettings() {
		return ((CreateDocSettingsJson)getUserSettings()).getRightResultColumns();
	}

	@Override
	public void setColumnSettings(List<ColumnSettings> columns) {
		((CreateDocSettingsJson)getUserSettings()).setRightResultColumns(columns);
	}

	@Override
	public List<ColumnHeaderGroup> getHeaderSettings() {
		return ((CreateDocSettingsJson)getUserSettings()).getRightColumnHeaders();
	}

	@Override
	public void setHeaderSettings(List<ColumnHeaderGroup> headers) {
		((CreateDocSettingsJson)getUserSettings()).setRightColumnHeaders(headers);
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
