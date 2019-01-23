package ru.gzpn.spc.csl.ui.admin.project;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.interfaces.ICProject;
import ru.gzpn.spc.csl.model.interfaces.IHProject;
import ru.gzpn.spc.csl.model.jsontypes.ACLJson;
import ru.gzpn.spc.csl.services.bl.interfaces.IProjectService;
import ru.gzpn.spc.csl.ui.admin.GroupTemplate;
import ru.gzpn.spc.csl.ui.admin.UsersAndRolesVerticalLayout;
import ru.gzpn.spc.csl.ui.common.ConfirmDialogWindow;
import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;

@SuppressWarnings("serial")
public class ProjectAddGroupVerticalLayout extends VerticalLayout implements I18n{
	
	private MessageSource messageSource;
	private IdentityService identityService;
	private IProjectService projectService;
	
	private HorizontalLayout headerHorizont;
	private ComboBox<String> selectGroup;
	private Button addGroupButton;
	private DataProvider<String, String> selectGroupProvider = createSelectGroupProvider();
	private ConfigurableFilterDataProvider<String, Void, String> selectGroupFilter;
	private Button editButton;
	private Button saveButton;
	private DataProvider<GroupTemplate, String> groupForHProject = createHProjectDataProvider();
	private DataProvider<GroupTemplate, String> groupForCProject = createCProjectDataProvider();
	private ICProject currentICProject;
	private IHProject currentIHProject;
	private Grid<GroupTemplate> gridGroupAddProject;
	private JoinedLayout<AbstractComponent, AbstractComponent> joinedComponent;
	private CProjectDataProvider cpDataProvider;
	private HProjectDataProvider hpDataProvider;
	
	public static final String I18N_NOTIFICATION_ADDGROUPMESSAGE = "adminView.project.notification.addGroupMes";
	public static final String I18N_NOTIFICATION_ADDGROUPERROR = "adminView.project.notification.addGroupErr";
	public static final String I18N_NOTIFICATION_ADDGROUPREPEAT = "adminView.project.notification.addGroupRepeat";
	public static final String I18N_NOTIFICATION_DELGROUPMESSAGE = "adminView.project.notification.delGroupMes";
	public static final String I18N_NOTIFICATION_DELGROUPERROR = "adminView.project.notification.delGroupErr";
	
	public ProjectAddGroupVerticalLayout(MessageSource messageSource, IdentityService identityService, IProjectService projectService, 
										 CProjectDataProvider cpDataProvider, HProjectDataProvider hpDataProvider) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		this.projectService = projectService;
		this.cpDataProvider = cpDataProvider;
		this.hpDataProvider = hpDataProvider;
		setHeight(100, Unit.PERCENTAGE);
		selectGroup = createSelectGroup(selectGroupProvider);
		addGroupButton = createAddGroupButton();
		editButton = createEditButton();
		saveButton = createViewButton();
		joinedComponent = new JoinedLayout<>(selectGroup, addGroupButton);
		headerHorizont = new HorizontalLayout();
		joinedComponent.setVisible(false);
		joinedComponent.setEnabled(false);
		saveButton.setVisible(false);
		saveButton.setEnabled(false);
		headerHorizont.setSizeFull();
		headerHorizont.addComponents(joinedComponent, editButton, saveButton);
		headerHorizont.setComponentAlignment(editButton, Alignment.MIDDLE_RIGHT);
		headerHorizont.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
		gridGroupAddProject = createGroupAddProject();
		addComponents(headerHorizont, gridGroupAddProject);
	}

	public Button getSaveButton() {
		return this.saveButton;
	}
	
	private DataProvider<String, String> createSelectGroupProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {
			List<String> groupList = identityService.createGroupQuery().list().stream().
					filter(group -> StringUtils.startsWithIgnoreCase(group.getId(), query.getFilter().orElse(""))
						).map(Group :: getId).collect(Collectors.toList());
			return groupList.stream();
			
		}, query -> identityService.createGroupQuery().list().stream().
					filter(group -> StringUtils.startsWithIgnoreCase(group.getId(), query.getFilter().orElse(""))
					).collect(Collectors.toList()).size());
	}

	private DataProvider<GroupTemplate, String> createHProjectDataProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {
			Stream<GroupTemplate> gs = Stream.empty();
			if(Objects.nonNull(currentIHProject.getAcl()) && Objects.nonNull(currentIHProject)) {
			 gs = currentIHProject.getAcl().getRoles()
						.stream().flatMap(item -> identityService.createGroupQuery().groupId(item).list().stream())
							.map(item -> {
								GroupTemplate group = new GroupTemplate();
								group.setId(item.getId());
								group.setName(item.getName());
								group.setType(item.getType());
								group.setDelete(buttonDeleteGroupMemberProject(currentIHProject.getId().toString(), item));
								return group;
							});
			}
			return gs;
		}
		, query -> {
			int result = 0;
			if(Objects.nonNull(currentIHProject.getAcl()) && Objects.nonNull(currentIHProject)) {
				result = currentIHProject.getAcl().getRoles().size();
			}
			return result;
		});
	}
	
	private DataProvider<GroupTemplate, String> createCProjectDataProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {
			Stream<GroupTemplate> gs = Stream.empty();
			if(Objects.nonNull(currentICProject.getAcl()) && Objects.nonNull(currentICProject)) {
			 gs = currentICProject.getAcl().getRoles()
						.stream().flatMap(item -> identityService.createGroupQuery().groupId(item).list().stream())
							.map(item -> {
								GroupTemplate group = new GroupTemplate();
								group.setId(item.getId());
								group.setName(item.getName());
								group.setType(item.getType());
								group.setDelete(buttonDeleteGroupMemberProject(currentICProject.getId().toString(), item));
								return group;
							});
			}
			return gs;
		}
		, query -> {
			int result = 0;
			if(Objects.nonNull(currentICProject.getAcl()) && Objects.nonNull(currentICProject)) {
				result = currentICProject.getAcl().getRoles().size();
			}
			return result;
		});
	}


	private Button buttonDeleteGroupMemberProject(String projectID, Group group) {
		Button deleteButton = new Button();
		ClickListener okDeleteClick = event -> {
			String[] paramsForDelete = new String[] {group.getId(), projectID};
			try {
				if(currentICProject == null) {
					ACLJson acljson = currentIHProject.getAcl();
	 				acljson.getRoles().remove(group.getId());
	 				currentIHProject.setAcl(acljson);
	 				projectService.saveHProject(currentIHProject);
	 				groupForHProject.refreshAll();
	 				hpDataProvider.refreshAll();
				}
				else if(currentIHProject == null) {
					ACLJson acljson = currentICProject.getAcl();
	 				acljson.getRoles().remove(group.getId());
	 				currentICProject.setAcl(acljson);
	 				projectService.saveCProject(currentICProject);
	 				groupForCProject.refreshAll();
	 				cpDataProvider.refreshAll();
				}
 				String notificationDeleted = getI18nText(I18N_NOTIFICATION_DELGROUPMESSAGE, paramsForDelete, messageSource);
 				Notification.show(notificationDeleted, Type.WARNING_MESSAGE);
 			}
 			catch(Exception e) {
 				String notificationTextErr = getI18nText(I18N_NOTIFICATION_DELGROUPERROR, paramsForDelete, messageSource);
 				Notification.show(notificationTextErr, Type.ERROR_MESSAGE);
 			}
			selectGroup.setValue("");
		};
		deleteButton.addClickListener(event -> {
			ConfirmDialogWindow box = new ConfirmDialogWindow(getI18nText(UsersAndRolesVerticalLayout.I18N_CONFIRMDIALOG_INFO_DELETEGROUP, messageSource), 
					 										  getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_OK, messageSource), 
					 										  getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_CLOSE, messageSource), 
					 										  okDeleteClick);
			getUI().addWindow(box);
		});
		return deleteButton;
	}


	public void setCurrentICProject(ICProjectPresenter currentICProject) {
		this.currentICProject = currentICProject.getCProject();
		this.currentIHProject = null;
		groupForCProject.refreshAll();
		gridGroupAddProject.setDataProvider(groupForCProject);
	}

	public void setCurrentIHProject(IHProjectPresenter currentIHProject) {
		this.currentIHProject = currentIHProject.getHProject();
		this.currentICProject = null;
		groupForHProject.refreshAll();
		gridGroupAddProject.setDataProvider(groupForHProject);
	}

	private ComboBox<String> createSelectGroup(DataProvider<String, String> selectGroupProvider) {
		ComboBox<String> groupSelect = new ComboBox<>();
		groupSelect.setPlaceholder(getI18nText(UsersAndRolesVerticalLayout.I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
		groupSelect.setDataProvider(selectGroupProvider);
		groupSelect.setPageLength(5);
		groupSelect.addSelectionListener(event -> {
			selectGroupProvider.refreshAll();
			selectGroupFilter = selectGroupProvider.withConfigurableFilter();
			addGroupButton.setEnabled(StringUtils.isNotEmpty(selectGroup.getValue()));
		});
		groupSelect.addValueChangeListener(event -> {
			selectGroupProvider.refreshAll();
			selectGroupFilter.setFilter(groupSelect.getValue());
			selectGroupFilter = selectGroupProvider.withConfigurableFilter();
			addGroupButton.setEnabled(StringUtils.isNotEmpty(selectGroup.getValue()));
		});
		return groupSelect;
	}
	
	private Button createAddGroupButton() {
		Button createButton = new Button();
		createButton.setIcon(VaadinIcons.PLUS);
 		createButton.addClickListener(event -> {
	 		String[] paramsForAdd = new String[] {selectGroup.getValue()};
	 		try {
	 			String notificationTextAdd;
	 			if(currentICProject == null) {	
	 				paramsForAdd = new String[] {selectGroup.getValue(), currentIHProject.getId().toString()};
	 				ACLJson acljson = currentIHProject.getAcl();
	 				Optional<String> result = acljson.getRoles().stream().filter(selectGroup.getValue()::equals).findFirst();
	 				if(result.isPresent()) {
	 					notificationTextAdd = getI18nText(I18N_NOTIFICATION_ADDGROUPREPEAT, paramsForAdd, messageSource);
	 				}
	 				else {
	 					acljson.getRoles().add(selectGroup.getValue());
	 					notificationTextAdd = getI18nText(I18N_NOTIFICATION_ADDGROUPMESSAGE, paramsForAdd, messageSource);
	 				}
	 				Notification.show(notificationTextAdd, Type.WARNING_MESSAGE);
	 	 			currentIHProject.setAcl(acljson);
	 	 			projectService.saveHProject(currentIHProject);
	 	 			groupForHProject.refreshAll();
	 	 			hpDataProvider.refreshAll();
				}
				else if(currentIHProject == null) {
					paramsForAdd = new String[] {selectGroup.getValue(), currentICProject.getId().toString()};
					ACLJson acljson = currentICProject.getAcl();
					Optional<String> result = acljson.getRoles().stream().filter(selectGroup.getValue()::equals).findFirst();
	 				if(result.isPresent()) {
	 					notificationTextAdd = getI18nText(I18N_NOTIFICATION_ADDGROUPREPEAT, paramsForAdd, messageSource);
	 				}
	 				else {
	 					acljson.getRoles().add(selectGroup.getValue());
	 					notificationTextAdd = getI18nText(I18N_NOTIFICATION_ADDGROUPMESSAGE, paramsForAdd, messageSource);	
	 				}
	 				Notification.show(notificationTextAdd, Type.WARNING_MESSAGE);
		 			currentICProject.setAcl(acljson);
		 			projectService.saveCProject(currentICProject);
		 			groupForCProject.refreshAll();
		 			cpDataProvider.refreshAll();
				}
	 		}
	 		catch(Exception e) {
	 			String notificationTextErr = getI18nText(I18N_NOTIFICATION_ADDGROUPERROR, paramsForAdd, messageSource);
	 			Notification.show(notificationTextErr, Type.ERROR_MESSAGE);
	 		}
	 		selectGroup.setValue("");
 		});
		return createButton;
	}
	
	private Button createEditButton() {
		Button edit = new Button(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_EDIT, messageSource));
		edit.setStyleName(ValoTheme.BUTTON_PRIMARY);
		edit.addClickListener(event ->{
			joinedComponent.setVisible(true);
			joinedComponent.setEnabled(true);
			addGroupButton.setEnabled(false);
			saveButton.setVisible(true);
			saveButton.setEnabled(true);
			edit.setVisible(false);
			edit.setEnabled(false);
			gridGroupAddProject.getColumn("del").setHidden(false);
		});
		return edit;
	}
	
	private Button createViewButton() {
		Button view = new Button(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_BUTTON_OK, messageSource));
		view.addClickListener(event ->{
			joinedComponent.setVisible(false);
			joinedComponent.setEnabled(false);
			addGroupButton.setEnabled(false);
			view.setVisible(false);
			view.setEnabled(false);
			editButton.setVisible(true);
			editButton.setEnabled(true);
			gridGroupAddProject.getColumn("del").setHidden(true);
		});
		return view;
	}
	
	private Grid<GroupTemplate> createGroupAddProject() {
		Grid<GroupTemplate> grid = new Grid<>();
		grid.setSizeFull();
		grid.addColumn(GroupTemplate::getId).setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_ID, messageSource));
		grid.addColumn(GroupTemplate::getName).setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_NAME, messageSource));
		grid.addColumn(GroupTemplate::getType).setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_TYPE, messageSource));
		grid.addComponentColumn(GroupTemplate::getDelete).setHidden(true).setId("del").setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_DELETE, messageSource)).setWidth(95.0);
		grid.setWidth(100, Unit.PERCENTAGE);
		return grid;
	}
}
