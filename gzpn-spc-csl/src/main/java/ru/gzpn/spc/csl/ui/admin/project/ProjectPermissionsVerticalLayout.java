package ru.gzpn.spc.csl.ui.admin.project;

import java.util.EnumSet;

import org.activiti.engine.IdentityService;
import org.springframework.context.MessageSource;

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

import ru.gzpn.spc.csl.model.utils.Entities;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataProjectService;
import ru.gzpn.spc.csl.ui.admin.UsersAndRolesVerticalLayout;
import ru.gzpn.spc.csl.ui.common.I18n;

public class ProjectPermissionsVerticalLayout extends VerticalLayout implements I18n {
	
	private IdentityService identityService;
	private MessageSource messageSource;
	private TextField searchProject;
	private ComboBox<Entities> selectTypeProject;
	private Grid<IHProjectPresenter> gridHeavyProjects;
	private Grid<ICProjectPresenter> gridCapitalProjects;
	private HorizontalLayout headerHorizontal;
	private VerticalLayout resultPage;
	private MarginInfo marginForHeader;
	private HorizontalSplitPanel panel;
	private CProjectDataProvider cpDataProvider;
	private HProjectDataProvider hpDataProvider;
	private IDataProjectService projectService;

	public ProjectPermissionsVerticalLayout(IDataProjectService projectService, IdentityService identityService, MessageSource messageSource) {
		this.projectService = projectService;
		this.identityService = identityService;
		this.messageSource = messageSource;
		
		cpDataProvider = new CProjectDataProvider(projectService);
		hpDataProvider = new HProjectDataProvider(projectService);
		
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
		filterTextField.setPlaceholder(getI18nText(UsersAndRolesVerticalLayout.I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
		return filterTextField;
	}
	
	private ComboBox<Entities> createSelectTypeProject() {
		ComboBox<Entities> comboBox = new ComboBox<>();
		comboBox.setWidth(215, Unit.PIXELS);
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
	
	private Grid<IHProjectPresenter> createGridHeavyProjects() {
		Grid<IHProjectPresenter> grid = new Grid<>();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addColumn(IHProjectPresenter::getId).setCaption("ID");
		grid.addColumn(IHProjectPresenter::getName).setCaption("Name");
		grid.addColumn(IHProjectPresenter::getCode).setCaption("Code");
		grid.addColumn(IHProjectPresenter::getCreateDatePresenter).setCaption("Create Date");
		grid.addColumn(IHProjectPresenter::getChangeDatePresenter).setCaption("Change Date");
		grid.addColumn(IHProjectPresenter::getVersion).setCaption("Version");
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		grid.setDataProvider(hpDataProvider);
		return grid;
	}
	
	private Grid<ICProjectPresenter> createGridCapitalProjects() {
		Grid<ICProjectPresenter> grid = new Grid<>();
		grid.setSelectionMode(SelectionMode.MULTI);
		grid.addColumn(ICProjectPresenter::getId).setCaption("ID");
		grid.addColumn(ICProjectPresenter::getName).setCaption("Name");
		grid.addColumn(ICProjectPresenter::getCode).setCaption("Code");
		grid.addColumn(ICProjectPresenter::getStageCaption).setCaption("Stage");
		grid.addColumn(ICProjectPresenter::getPhaseCaption).setCaption("Phase");
		grid.addColumn(ICProjectPresenter::getHProjectCaption).setCaption("Heavy Project");
		grid.addColumn(ICProjectPresenter::getMilestoneCaption).setCaption("Milestone");
		grid.addColumn(ICProjectPresenter::getCreateDatePresenter).setCaption("Create Date");
		grid.addColumn(ICProjectPresenter::getChangeDatePresenter).setCaption("Change Date");
		grid.addColumn(ICProjectPresenter::getVersion).setCaption("Version");
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		grid.setDataProvider(cpDataProvider);
		return grid;
	}
}
