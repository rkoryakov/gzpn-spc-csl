package ru.gzpn.spc.csl.ui.js;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@JavaScript({"https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js",
	"jquery.flot.min.js", "jquery.flot.animator.min.js", "flot_connector.js"})
public class Flot extends AbstractJavaScriptComponent {
	
	public Flot() {
		setHeight("400px");
		setWidth("600px");
		
		addSeries(2,5);
		addSeries(3,6);
		addSeries(4,7);
		addSeries(5,8);
		addSeries(6,9);
		addSeries(7,6);
		addSeries(8,3);
		addSeries(9,10);
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
