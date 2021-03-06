package ru.gzpn.spc.csl.ui.processmanager;

import java.time.Duration;
import java.time.Instant;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.ui.js.flot.Flot;

@SuppressWarnings("serial")
public class AnalyticsComponent extends AbstractAnalyticsComponent {

	private static final String I18N_APPROVE_ESTIMATES_CALC_CAP = "AnalyticsComponent.estimateCalc.approveEstimateCalc.cap";
	private static final String I18N_REJECT_ESTIMATES_CALC_CAP = "AnalyticsComponent.estimateCalc.rejectEstimateCalc.cap";
	private static final String I18N_LASTWEEK_APPROVE_ESTIMATES_CALC_CAP = "AnalyticsComponent.estimateCalc.lastWeek.cap";
	private static final String I18N_LASTMONTH_APPROVE_ESTIMATES_CALC_CAP = "AnalyticsComponent.estimateCalc.lastMonth.cap";

	private Flot approvedLastWeekEstimatesCalcPlot;
	private Flot rejectedLastWeekEstimatesCalcPlot;
	private Flot approvedLastMonthEstimatesCalcPlot;
	private Flot rejectedLastMonthEstimatesCalcPlot;

	public AnalyticsComponent(IUIService service) {
		super(service);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout body = new VerticalLayout();
		body.setSpacing(false);
		body.setMargin(false);
		body.setSizeFull();
		
		body.addComponent(createLastWeekEstimatesCalcPlotLayout());
		body.addComponent(createLastMonthEstimatesCalcPlotLayout());
		
		return body;
	}

	public Component createLastWeekEstimatesCalcPlotLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(new MarginInfo(true, true, false, true));
		Label caption = new Label(getI18nText(I18N_LASTWEEK_APPROVE_ESTIMATES_CALC_CAP, service.getMessageSource()));
		layout.addComponent(caption);
		HorizontalLayout horizontalLayout = new HorizontalLayout();		
		approvedLastWeekEstimatesCalcPlot = createPlot(7, getI18nText(I18N_APPROVE_ESTIMATES_CALC_CAP, service.getMessageSource()),"600px", "300px", false);
		rejectedLastWeekEstimatesCalcPlot = createPlot(7, getI18nText(I18N_REJECT_ESTIMATES_CALC_CAP, service.getMessageSource()), "600px", "300px", true);
		rejectedLastWeekEstimatesCalcPlot.getState().color = "#FFA07A";
		horizontalLayout.addComponents(approvedLastWeekEstimatesCalcPlot, rejectedLastWeekEstimatesCalcPlot);
		
		layout.addComponent(horizontalLayout);
		return layout;
	}

	public Component createLastMonthEstimatesCalcPlotLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(new MarginInfo(false, true));
		Label caption = new Label(getI18nText(I18N_LASTMONTH_APPROVE_ESTIMATES_CALC_CAP, service.getMessageSource()));
		layout.addComponent(caption);
		HorizontalLayout horizontalLayout = new HorizontalLayout();		
		approvedLastMonthEstimatesCalcPlot = createPlot(30, getI18nText(I18N_APPROVE_ESTIMATES_CALC_CAP, service.getMessageSource()), "600px", "300px", false);
		rejectedLastMonthEstimatesCalcPlot = createPlot(30, getI18nText(I18N_REJECT_ESTIMATES_CALC_CAP, service.getMessageSource()),  "600px", "300px", true);
		rejectedLastMonthEstimatesCalcPlot.getState().color = "#FFA07A";
		horizontalLayout.addComponents(approvedLastMonthEstimatesCalcPlot, rejectedLastMonthEstimatesCalcPlot);
		
		layout.addComponent(horizontalLayout);
		return layout;
	}
	
	private Flot createPlot(int days, String label, String width, String height, boolean notApproved) {
		Flot plot = new Flot(width, height);
		//List<HistoricTaskInstance> listTasks = (int)service.getProcessService().getProcessEngine().getHistoryService().createHistoricTaskInstanceQuery().processDefinitionKey("EstimatesRegistration").list();
		
		
		for (int i = 0; i < days; i ++) {
//			Date closeDate = new Date (Instant.now().minus(Duration.ofDays(i)).getEpochSecond());
//			for (HistoricTaskInstance task : listTasks) {
//				if (task.getEndTime().before(when))
//			}
			plot.addSeries(Instant.now().minus(Duration.ofDays(i)).getEpochSecond()*1000, Math.random()*10);
		}
		
		plot.getState().label = label;
		return plot;
	}
}
