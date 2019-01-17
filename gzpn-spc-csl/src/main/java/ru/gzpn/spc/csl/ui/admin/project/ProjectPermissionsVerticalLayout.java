package ru.gzpn.spc.csl.ui.admin.project;

import java.util.EnumSet;

import org.activiti.engine.IdentityService;
import org.springframework.context.MessageSource;

import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.model.CProject;
import ru.gzpn.spc.csl.model.HProject;
import ru.gzpn.spc.csl.model.utils.Entities;
import ru.gzpn.spc.csl.ui.admin.UsersAndRolesVerticalLayout;

public class ProjectPermissionsVerticalLayout extends VerticalLayout {
	
	private IdentityService identityService;
	private MessageSource messageSource;
	private TextField searchProject;
	private ComboBox<Entities> selectTypeProject;
	private Grid<HProject> gridHeavyProjects;
	private Grid<CProject> gridCapitalProjects;
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
		gridHeavyProjects = createGridHeavyProjects();
		gridCapitalProjects = createGridCapitalProjects();
		searchProject = createSearchProject();
		selectTypeProject = createSelectTypeProject();
		marginForHeader = new MarginInfo(true, false, false, false);
		headerHorizontal.setDefaultComponentAlignment(Alignment.BOTTOM_LEFT);
		headerHorizontal.addComponents(searchProject, selectTypeProject);
		headerHorizontal.setMargin(marginForHeader);
		resultPage.addComponents(headerHorizontal, gridHeavyProjects, gridCapitalProjects);
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
		filterTextField.setWidth(240, Unit.PIXELS);
		filterTextField.setPlaceholder(getI18nText(UsersAndRolesVerticalLayout.I18N_SEARCHFIELD_PLACEHOLDER));
		return filterTextField;
	}
	
	private ComboBox<Entities> createSelectTypeProject() {
		ComboBox<Entities> comboBox = new ComboBox<>();
		comboBox.setWidth(280, Unit.PIXELS);
		comboBox.setItems(EnumSet.of(Entities.HPROJECT, Entities.CPROJECT));
		comboBox.setTextInputAllowed(false);
		comboBox.setEmptySelectionAllowed(false);
		comboBox.setSelectedItem(Entities.HPROJECT);
		gridCapitalProjects.setVisible(false);
		comboBox.setItemCaptionGenerator(item -> {
			String result = "Крупные проекты";
			if (item == Entities.CPROJECT) {
				result = "Капитальные проекты";
			}
			return result;
		});
		comboBox.addSelectionListener(event -> {
			if (event.getSelectedItem().get().equals(Entities.HPROJECT)) {
				gridHeavyProjects.setVisible(true);
				gridCapitalProjects.setVisible(false);
			} else if (event.getSelectedItem().get().equals(Entities.CPROJECT)) {
				gridHeavyProjects.setVisible(false);
				gridCapitalProjects.setVisible(true);
			}
			searchProject.setValue("");
		});
		return comboBox;
	}
	
	private Grid<HProject> createGridHeavyProjects() {
		Grid<HProject> grid = new Grid<>();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addColumn(HProject::getId).setCaption("ID");
		grid.addColumn(HProject::getName).setCaption("Name");
		grid.addColumn(HProject::getCode).setCaption("Code");
		grid.addColumn(HProject::getCreateDate).setCaption("Create Date");
		grid.addColumn(HProject::getChangeDate).setCaption("Change Date");
		grid.addColumn(HProject::getVersion).setCaption("Version");
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		return grid;
	}
	
	private Grid<CProject> createGridCapitalProjects() {
		Grid<CProject> grid = new Grid<>();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addColumn(CProject::getId).setCaption("ID");
		grid.addColumn(CProject::getName).setCaption("Name");
		grid.addColumn(CProject::getCode).setCaption("Code");
		grid.addColumn(CProject::getStageCaption).setCaption("Stage");
		grid.addColumn(CProject::getPhaseCaption).setCaption("Phase");
		grid.addColumn(CProject::getHProjectCaption).setCaption("Heavy Project");
		grid.addColumn(CProject::getMilestoneCaption).setCaption("Milestone");
		grid.addColumn(CProject::getCreateDate).setCaption("Create Date");
		grid.addColumn(CProject::getChangeDate).setCaption("Change Date");
		grid.addColumn(CProject::getVersion).setCaption("Version");
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
