package ru.gzpn.spc.csl.services.bpm;

import com.vaadin.navigator.Navigator;

public interface IUserTaskNavigator {
	void navigate(Navigator navigator, String taskId);
}
