package ru.gzpn.spc.csl.ui.js;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@JavaScript({"jquery.min.js",
	"jquery.flot.min.js", "jquery.flot.time.min.js", "jquery.flot.growraf.js", "flot_connector.js"})
public class Flot extends AbstractJavaScriptComponent {
	
	public Flot() {
		setHeight("400px");
		setWidth("600px");
	}
	
	public void addSeries(double... points) {
	    List<Double> pointList = new ArrayList<Double>();
	    for (int i = 0; i < points.length-1; i++) {
	      pointList.add(Double.valueOf(points[i]));
	      pointList.add(Double.valueOf(points[i+1]));
	       
	    }

	    getState().series.add(pointList);
	  }


	  @Override
	  public FlotState getState() {
	    return (FlotState) super.getState();
	  }
}
