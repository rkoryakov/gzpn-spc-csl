package ru.gzpn.spc.csl.ui.admin.project;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
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
	private DataProvider<GroupTemplate, String> groupForProject = createDataProvider();
	private ConfigurableFilterDataProvider<GroupTemplate, Void, String> groupProjectIDFilter;
	private ICProject currentICProject;
	private IHProject currentIHProject;
	private Grid<GroupTemplate> gridGroupAddProject;
	private JoinedLayout<AbstractComponent, AbstractComponent> joinedComponent;
	
	public static final String I18N_NOTIFICATION_DELETEDFROMUSER = "adminView.notification.group.deletedFromUser";
	public static final String I18N_NOTIFICATION_USERADDGROUP = "adminView.notification.user.addGroup";
	public static final String I18N_NOTIFICATION_USERADDGROUPERR = "adminView.notification.user.addGroupErr";
	
	public ProjectAddGroupVerticalLayout(MessageSource messageSource, IdentityService identityService, IProjectService projectService) {
		this.messageSource = messageSource;
		this.identityService = identityService;
		this.projectService = projectService;
		
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
					filter(group -> group.getId().startsWith(query.getFilter().orElse("")) 
									|| group.getName().startsWith(query.getFilter().orElse(""))
						).map(Group :: getId).collect(Collectors.toList());
			return groupList.stream();
			
		}, query -> identityService.createGroupQuery().list().stream().
					filter(group -> group.getId().startsWith(query.getFilter().orElse(""))
								|| group.getName().startsWith(query.getFilter().orElse(""))
					).collect(Collectors.toList()).size());
	}

	private DataProvider<GroupTemplate, String> createDataProvider() {
		return DataProvider.fromFilteringCallbacks(query -> {
			Stream<GroupTemplate> gs = Stream.empty();
			if(Objects.nonNull(currentIHProject.getAcl())) {
			 gs = currentIHProject.getAcl().getReadOnlyRoles()
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
			if(Objects.nonNull(currentIHProject.getAcl())) {
				result = currentIHProject.getAcl().getReadOnlyRoles().size();
			}
			return result;
		});
	}

	private Button buttonDeleteGroupMemberProject(String projectID, Group group) {
		Button deleteButton = new Button();
		ClickListener okDeleteClick = event -> {
			//identityService.deleteMembership(projectID, group.getId());
			groupForProject.refreshAll();
			String[] paramsForDelete = new String[] {group.getId(), projectID};
			String notificationDeleted = getI18nText(I18N_NOTIFICATION_DELETEDFROMUSER, paramsForDelete, messageSource);
			Notification.show(notificationDeleted, Type.WARNING_MESSAGE);
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
		groupForProject.refreshAll();
	}

	public void setCurrentIHProject(IHProjectPresenter currentIHProject) {
		this.currentIHProject = currentIHProject.getHProject();
		groupForProject.refreshAll();
	}

	private ComboBox<String> createSelectGroup(DataProvider<String, String> selectGroupProvider) {
		ComboBox<String> groupSelect = new ComboBox<>();
		groupSelect.setPlaceholder(getI18nText(UsersAndRolesVerticalLayout.I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
		groupSelect.setDataProvider(selectGroupProvider);
		groupSelect.setPageLength(5);
		groupSelect.addSelectionListener(event -> {
			selectGroupProvider.refreshAll();
			selectGroupFilter = selectGroupProvider.withConfigurableFilter();
		});
		groupSelect.addValueChangeListener(event -> {
			selectGroupProvider.refreshAll();
			selectGroupFilter.setFilter(groupSelect.getValue());
			selectGroupFilter = selectGroupProvider.withConfigurableFilter();
		});
		return groupSelect;
	}
	
	private Button createAddGroupButton() {
		Button createButton = new Button();
		createButton.setIcon(VaadinIcons.PLUS);
 		createButton.addClickListener(event -> {
 			String[] paramsForAdd = new String[] {selectGroup.getValue(), currentIHProject.getId().toString()};
// 			try {
 				//Set<String> groupSet = currentIHProject.getAcl().getReadOnlyRoles();
 			//	groupSet.add(selectGroup.getValue());
 				ACLJson acljson = currentIHProject.getAcl();
 				acljson.getReadOnlyRoles().add(selectGroup.getValue());
 				//acljson.setReadOnlyRoles(groupSet);
 				currentIHProject.setAcl(acljson);
 				projectService.saveHProject(currentIHProject);
 				//identityService.createMembership(currentIHProject.getId().toString(), selectGroup.getValue());
 				groupForProject.refreshAll();
 				String notificationTextAdd = getI18nText(I18N_NOTIFICATION_USERADDGROUP, paramsForAdd, messageSource);
 				Notification.show(notificationTextAdd, Type.WARNING_MESSAGE);
// 			}
// 			catch(Exception e) {
// 				String notificationTextErr = getI18nText(I18N_NOTIFICATION_USERADDGROUPERR, paramsForAdd, messageSource);
// 				Notification.show(notificationTextErr, Type.ERROR_MESSAGE);
// 			}
 		});
		return createButton;
	}
	
	private Button createEditButton() {
		Button edit = new Button(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_EDIT, messageSource));
		edit.setStyleName(ValoTheme.BUTTON_PRIMARY);
		edit.addClickListener(event ->{
			joinedComponent.setVisible(true);
			joinedComponent.setEnabled(true);
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
		groupProjectIDFilter = groupForProject.withConfigurableFilter();
		grid.setSizeFull();
		grid.addColumn(GroupTemplate::getId).setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_IDROLES, messageSource));
		grid.addColumn(GroupTemplate::getName).setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_NAMEROLES, messageSource));
		grid.addColumn(GroupTemplate::getType).setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_TYPEROLES, messageSource));
		grid.addComponentColumn(GroupTemplate::getDelete).setHidden(true).setId("del").setCaption(getI18nText(UsersAndRolesVerticalLayout.I18N_CAPTION_DELETE, messageSource)).setWidth(95.0);
		grid.setWidth(100, Unit.PERCENTAGE);
		grid.setDataProvider(groupProjectIDFilter);
		return grid;
	}
}
