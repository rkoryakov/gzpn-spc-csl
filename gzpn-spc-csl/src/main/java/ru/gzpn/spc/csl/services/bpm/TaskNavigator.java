package ru.gzpn.spc.csl.services.bpm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vaadin.navigator.Navigator;

@Service
public class TaskNavigator implements ITaskNavigator {
	@Autowired
	
	@Override
	public void navigate(Navigator navigator, String taskId) {
		
	}

}
