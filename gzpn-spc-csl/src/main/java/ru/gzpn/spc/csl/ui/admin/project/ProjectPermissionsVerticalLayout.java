package ru.gzpn.spc.csl.ui.admin.project;

import java.util.EnumSet;

import org.activiti.engine.IdentityService;
import org.springframework.context.MessageSource;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import ru.gzpn.spc.csl.model.utils.Entities;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.ui.admin.UsersAndRolesVerticalLayout;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
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
	private IProjectService projectService;
	private ProjectAddGroupVerticalLayout projectAddGroup;
	public static final String I18N_CAPTION_CAPITAL = "adminView.project.combobox.capital";
	public static final String I18N_CAPTION_HEAVYS = "adminView.project.combobox.heavy";
	public static final String I18N_CAPTION_ID = "adminView.caption.id";
	public static final String I18N_CAPTION_NAME = "adminView.caption.name";
	public static final String I18N_CAPTION_CODE = "adminView.caption.code";
	public static final String I18N_CAPTION_CREATEDATE = "adminView.caption.createdate";
	public static final String I18N_CAPTION_CHANGEEDATE = "adminView.caption.changedate";
	public static final String I18N_CAPTION_VERSION = "adminView.caption.version";
	public static final String I18N_CAPTION_STAGE = "adminView.caption.stage";
	public static final String I18N_CAPTION_PHASE = "adminView.caption.phase";
	public static final String I18N_CAPTION_HEAVY = "adminView.caption.hp";
	public static final String I18N_CAPTION_MILESTONE = "adminView.caption.milestone";
	
	public ProjectPermissionsVerticalLayout(IProjectService projectService, 
											IdentityService identityService, 
											MessageSource messageSource) {
		this.projectService = projectService;
		this.identityService = identityService;
		this.messageSource = messageSource;
		
		cpDataProvider = new CProjectDataProvider(projectService);
		hpDataProvider = new HProjectDataProvider(projectService);

		projectAddGroup = new ProjectAddGroupVerticalLayout(messageSource, identityService, projectService, cpDataProvider, hpDataProvider);
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
		panel.setSecondComponent(projectAddGroup);
		panel.setHeight(100, Unit.PERCENTAGE);
		setHeight(100, Unit.PERCENTAGE);
		addComponent(panel);
	}

	public ProjectAddGroupVerticalLayout getProjectAddGroup() {
		return projectAddGroup;
	}

	private TextField createSearchProject() {
		TextField filterTextField = new TextField();
		filterTextField.setWidth(240, Unit.PIXELS);
		filterTextField.setPlaceholder(getI18nText(UsersAndRolesVerticalLayout.I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
		filterTextField.addValueChangeListener(event -> {
			if (selectTypeProject.getSelectedItem().get().equals(Entities.HPROJECT)) {
				hpDataProvider.getFilter().setCommonTextFilter(event.getValue());
				hpDataProvider.refreshAll();
			} else if (selectTypeProject.getSelectedItem().get().equals(Entities.CPROJECT)) {
				cpDataProvider.getFilter().setCommonTextFilter(event.getValue());
				cpDataProvider.refreshAll();
			}
		});
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
			String result = getI18nText(I18N_CAPTION_HEAVYS, messageSource);
			if (item == Entities.CPROJECT) {
				result = getI18nText(I18N_CAPTION_CAPITAL, messageSource);
			}
			return result;
		});
		comboBox.addSelectionListener(event -> {
			if (event.getSelectedItem().get().equals(Entities.HPROJECT)) {
				gridHeavyProjects.setVisible(true);
				gridCapitalProjects.setVisible(false);
				hpDataProvider.refreshAll();
			} else if (event.getSelectedItem().get().equals(Entities.CPROJECT)) {
				gridHeavyProjects.setVisible(false);
				gridCapitalProjects.setVisible(true);
				cpDataProvider.refreshAll();
			}
			searchProject.setValue("");
		});
		return comboBox;
	}
	
	private Grid<IHProjectPresenter> createGridHeavyProjects() {
		Grid<IHProjectPresenter> grid = new Grid<>();
		grid.addColumn(IHProjectPresenter::getId).setCaption(getI18nText(I18N_CAPTION_ID, messageSource)).setId(IHProjectPresenter.FIELD_ID).setSortable(true);
		grid.addColumn(IHProjectPresenter::getName).setCaption(getI18nText(I18N_CAPTION_NAME, messageSource)).setId(IHProjectPresenter.FIELD_NAME).setSortable(true);
		grid.addColumn(IHProjectPresenter::getCode).setCaption(getI18nText(I18N_CAPTION_CODE, messageSource)).setId(IHProjectPresenter.FIELD_CODE).setSortable(true);
		grid.addColumn(IHProjectPresenter::getCreateDatePresenter).setCaption(getI18nText(I18N_CAPTION_CREATEDATE, messageSource)).setId(IHProjectPresenter.FIELD_CREATE_DATE).setSortable(true);
		grid.addColumn(IHProjectPresenter::getChangeDatePresenter).setCaption(getI18nText(I18N_CAPTION_CHANGEEDATE, messageSource)).setId(IHProjectPresenter.FIELD_CHANGE_DATE).setSortable(true);
		grid.addColumn(IHProjectPresenter::getVersion).setCaption(getI18nText(I18N_CAPTION_VERSION, messageSource)).setId(IHProjectPresenter.FIELD_VERSION).setSortable(true);
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		grid.setDataProvider(hpDataProvider);
		
		projectAddGroup.setVisible(false);	
		SingleSelectionModel<IHProjectPresenter> singleSelect = (SingleSelectionModel<IHProjectPresenter>) grid.getSelectionModel();
		singleSelect.addSingleSelectionListener(event -> 
			singleSelect.getSelectedItem().ifPresent(item -> {
				this.getProjectAddGroup().setCurrentIHProject(item);
				projectAddGroup.setVisible(true);
				hpDataProvider.refreshAll();
			})
		);
		return grid;
	}
	
	private Grid<ICProjectPresenter> createGridCapitalProjects() {
		Grid<ICProjectPresenter> grid = new Grid<>();
		grid.addColumn(ICProjectPresenter::getId).setCaption(getI18nText(I18N_CAPTION_ID, messageSource)).setId(ICProjectPresenter.FIELD_ID).setSortable(true);
		grid.addColumn(ICProjectPresenter::getName).setCaption(getI18nText(I18N_CAPTION_NAME, messageSource)).setId(ICProjectPresenter.FIELD_NAME).setSortable(true);
		grid.addColumn(ICProjectPresenter::getCode).setCaption(getI18nText(I18N_CAPTION_CODE, messageSource)).setId(ICProjectPresenter.FIELD_CODE).setSortable(true);
		grid.addColumn(ICProjectPresenter::getStageCaption).setCaption(getI18nText(I18N_CAPTION_STAGE, messageSource)).setId(ICProjectPresenter.FILED_STAGE).setSortable(true);
		grid.addColumn(ICProjectPresenter::getPhaseCaption).setCaption(getI18nText(I18N_CAPTION_PHASE, messageSource)).setId(ICProjectPresenter.FILED_PHASE).setSortable(true);
		grid.addColumn(ICProjectPresenter::getHProjectCaption).setCaption(getI18nText(I18N_CAPTION_HEAVY, messageSource)).setId(ICProjectPresenter.FILED_HPROJECT).setSortable(true);
		grid.addColumn(ICProjectPresenter::getMilestoneCaption).setCaption(getI18nText(I18N_CAPTION_MILESTONE, messageSource)).setId(ICProjectPresenter.FILED_MILESTONE).setSortable(true);
		grid.addColumn(ICProjectPresenter::getCreateDatePresenter).setCaption(getI18nText(I18N_CAPTION_CREATEDATE, messageSource)).setId(ICProjectPresenter.FIELD_CREATE_DATE).setSortable(true);
		grid.addColumn(ICProjectPresenter::getChangeDatePresenter).setCaption(getI18nText(I18N_CAPTION_CHANGEEDATE, messageSource)).setId(ICProjectPresenter.FIELD_CHANGE_DATE).setSortable(true);
		grid.addColumn(ICProjectPresenter::getVersion).setCaption(getI18nText(I18N_CAPTION_VERSION, messageSource)).setId(ICProjectPresenter.FIELD_VERSION).setSortable(true);
		grid.setColumnReorderingAllowed(true);
		grid.setSizeFull();
		grid.setHeightMode(HeightMode.ROW);
		grid.setHeightByRows(14);
		grid.setDataProvider(cpDataProvider);
		
		projectAddGroup.setVisible(false);	
		SingleSelectionModel<ICProjectPresenter> singleSelect = (SingleSelectionModel<ICProjectPresenter>) grid.getSelectionModel();
		singleSelect.addSingleSelectionListener(event -> 
			singleSelect.getSelectedItem().ifPresent(item -> {
				this.getProjectAddGroup().setCurrentICProject(item);
				projectAddGroup.setVisible(true);
				cpDataProvider.refreshAll();
			})
		);
		return grid;
	}
}
