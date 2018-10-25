package ru.gzpn.spc.csl.ui.createdoc;

import org.springframework.context.MessageSource;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TreeGrid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.services.bl.DataProjectService;

public class DocCreatingLayout extends HorizontalSplitPanel {

	private DataProjectService projectService;
	private MessageSource messageSource;
	private VerticalLayout leftLayot;
	private VerticalLayout rightLayout;

	private TreeGrid<NodeHolder> projectTree;
	
	public DocCreatingLayout(DataProjectService projectService, MessageSource messageSource) {
		this.projectService = projectService;
		this.messageSource = messageSource;
		setSizeFull();
		setSplitPosition(50.0f, Unit.PERCENTAGE);
		addStyleName(ValoTheme.SPLITPANEL_LARGE);
		setFirstComponent(createLeftLayout());
		setSecondComponent(createRightLayout());
	}

	private Component createLeftLayout() {
		leftLayot = new VerticalLayout();
		
		return leftLayot;
	}
	
	private Component createRightLayout() {
		rightLayout = new VerticalLayout();
		
		return rightLayout;
	}
}
