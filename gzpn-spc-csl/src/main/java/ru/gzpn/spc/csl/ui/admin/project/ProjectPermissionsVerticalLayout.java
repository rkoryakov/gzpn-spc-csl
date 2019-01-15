package ru.gzpn.spc.csl.ui.admin.project;

import org.activiti.engine.IdentityService;
import org.springframework.context.MessageSource;

import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.ui.admin.UsersAndRolesVerticalLayout;

public class ProjectPermissionsVerticalLayout extends VerticalLayout {
	
	private IdentityService identityService;
	private MessageSource messageSource;
	private TextField searchProject;
	private ComboBox<String> selectTypeProject;
	private Grid<String> gridProjects;
	private HorizontalLayout headerHorizontal;
	private VerticalLayout resultPage;
	private MarginInfo marginForHeader;
	private HorizontalSplitPanel panel;
	
	public ProjectPermissionsVerticalLayout(IdentityService identityService, MessageSource messageSource) {
		this.identityService = identityService;
		this.messageSource = messageSource;
		panel = new HorizontalSplitPanel();
		headerHorizontal = new HorizontalLayout();
		resultPage = new VerticalLayout();
		searchProject = createSearchProject();
		selectTypeProject = createSelectTypeProject();
		marginForHeader = new MarginInfo(true, false, false, false);
		headerHorizontal.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		headerHorizontal.addComponents(searchProject, selectTypeProject);
		headerHorizontal.setMargin(marginForHeader);
		gridProjects = createGridProjects();
		resultPage.addComponents(headerHorizontal, gridProjects);
		resultPage.setMargin(false);
		panel.setSplitPosition(70, Unit.PERCENTAGE);
		panel.setMaxSplitPosition(70, Unit.PERCENTAGE);
		panel.setMinSplitPosition(30, Unit.PERCENTAGE);
		panel.setFirstComponent(resultPage);
		panel.setHeight(100, Unit.PERCENTAGE);
		setHeight(100, Unit.PERCENTAGE);
		addComponent(panel);
	}

	private TextField createSearchProject() {
		TextField filterTextField = new TextField();
		filterTextField.setWidth(300, Unit.PIXELS);
		filterTextField.setPlaceholder(getI18nText(UsersAndRolesVerticalLayout.I18N_SEARCHFIELD_PLACEHOLDER));
		return filterTextField;
	}
	
	private ComboBox<String> createSelectTypeProject() {
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.setTextInputAllowed(false);
		comboBox.setEmptySelectionAllowed(false);
		return comboBox;
	}
	
	private Grid<String> createGridProjects() {
		Grid<String> grid = new Grid<>();
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		return grid;
	}
	
	private String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}
