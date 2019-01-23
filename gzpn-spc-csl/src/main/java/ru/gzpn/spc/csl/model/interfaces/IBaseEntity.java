package ru.gzpn.spc.csl.model.interfaces;

import java.time.ZonedDateTime;

public interface IBaseEntity {
	public static final String FIELD_ID = "id";
	public static final String FIELD_VERSION = "version";
	public static final String FIELD_CREATE_DATE = "createDate";
	public static final String FIELD_CHANGE_DATE = "changeDate";
	
	public Long getId();
	public void setId(Long id);
	public Integer getVersion();
	public void setVersion(Integer version);
	public ZonedDateTime getCreateDate();
	public void setCreateDate(ZonedDateTime createDate);
	public ZonedDateTime getChangeDate();
	public void setChangeDate(ZonedDateTime changeDate);
	
}
