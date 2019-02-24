package ru.gzpn.spc.csl.services.bpm;

import com.vaadin.navigator.Navigator;

public interface ITaskNavigator {
	void navigate(Navigator navigator, String taskId);
}
