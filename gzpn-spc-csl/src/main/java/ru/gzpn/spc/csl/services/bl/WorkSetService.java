package ru.gzpn.spc.csl.services.bl;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.gzpn.spc.csl.model.repositories.WorkSetRepository;

@Service
@Transactional
public class WorkSetService {
	public static final Logger logger = LoggerFactory.getLogger(WorkSetService.class);
	
	@Autowired
	private WorkSetRepository repository;
	
	public WorksetSort createSort(String fieldName, boolean descending) {
		return new WorksetSort(fieldName, descending);
	}
	
	public static class WorksetSort {
		private String fieldName;
		private boolean descending;
		
		public WorksetSort(String fieldName, boolean descending) {
			this.fieldName = fieldName;
			this.descending = descending;
		}
		
		public String getFieldName() {
			return fieldName;
		}
		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		public boolean isDescending() {
			return descending;
		}
		public void setDescending(boolean descending) {
			this.descending = descending;
		}
	}
}
