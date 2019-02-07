package ru.gzpn.spc.csl.ui.js.flot;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.JavaScriptComponentState;

@SuppressWarnings("serial")
public class FlotState extends JavaScriptComponentState {
	public List<List<Double>> series = new ArrayList<List<Double>>();
}
