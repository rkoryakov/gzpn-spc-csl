package ru.gzpn.spc.csl.model.jsontypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ru.gzpn.spc.csl.model.LocalEstimate;
import ru.gzpn.spc.csl.model.interfaces.ILocalEstimate;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class LocalEstimatesApprovalJson implements ISettingsJson, Serializable {

	private List<ColumnSettings> columns;
	private List<ColumnHeaderGroup> headers;
	
	@Override
	@JsonIgnore
	public boolean isShownTree() {
		return false;
	}

	@Override
	@JsonIgnore
	public NodeWrapper getTreeSettings() {
		return null;
	}

	@Override
	public List<ColumnSettings> getColumns() {
		return columns;
	}

	@JsonIgnore
	public List<ColumnSettings> getDefaultColums() {
		List<ColumnSettings> result = new ArrayList<>();
		String entityName = LocalEstimate.class.getSimpleName();

		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_ID, null, false, 0));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_NAME, null, true, 1));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_CODE, null, true, 2));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_CHANGEDBY, null, true, 3));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_ESTIMATEHEAD, null, true, 4));

		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_CREATE_DATE, null, true, 6));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_CHANGE_DATE, null, false, 7));
		result.add(new ColumnSettings(entityName, ILocalEstimate.FIELD_VERSION, null, false, 8));
		return result;
	}
	
	@Override
	public List<ColumnHeaderGroup> getHeaders() {
		if (Objects.isNull(headers)) {
			headers = getDefaultHeaders();
		}

		return headers;
	}

	@JsonIgnore
	public List<ColumnHeaderGroup> getDefaultHeaders() {

		List<ColumnHeaderGroup> result = new ArrayList<>();
		List<ColumnHeaderGroup> subGroups = new ArrayList<>();

		return result;
	}
}
