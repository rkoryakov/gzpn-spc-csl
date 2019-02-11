package ru.gzpn.spc.csl.services.bl.interfaces;

import java.util.Comparator;
import java.util.List;

import org.springframework.context.MessageSource;

import com.vaadin.data.provider.QuerySortOrder;

public interface IDataService<T, P> {
	
	Comparator<P> getSortComparator(List<QuerySortOrder> list);
	MessageSource getMessageSource();
	
	/**
	 * Save existing bean. If the bean doesn't exist then create it
	 * @param bean
	 */
	void save(T bean);
	void remove(T bean);
}
