package ru.gzpn.spc.csl.ui.createdoc;

import java.util.Map;

import com.google.gwt.dev.util.collect.HashMap;

public class NodeFilter {
	private String configuredFilter;
	
	private Map<String, Object> queryColumnsFilter = new HashMap<>();
	
	public void add(String key, Object value) {
		queryColumnsFilter.put(key, value);
	}
	
	public Object get (String key) {
		return queryColumnsFilter.get(key);
	}
	
	public void setCommonFilter(String filter) {
		this.configuredFilter = filter;
	}
}
