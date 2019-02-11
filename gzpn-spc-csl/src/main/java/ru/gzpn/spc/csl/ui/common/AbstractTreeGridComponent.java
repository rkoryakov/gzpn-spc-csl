package ru.gzpn.spc.csl.ui.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.MessageSource;

import com.vaadin.event.Action;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.model.dataproviders.AbstractRegistryDataProvider;
import ru.gzpn.spc.csl.model.dataproviders.ProjectTreeDataProvider;
import ru.gzpn.spc.csl.model.interfaces.IBaseEntity;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.data.export.Exporter;
import ru.gzpn.spc.csl.ui.createdoc.CreateDocSettingsWindow;

@SuppressWarnings("serial")
public abstract class AbstractTreeGridComponent<T extends IBaseEntity> extends VerticalLayout implements I18n {
	
	// event actions
	public static final Action GRID_ITEM_SELECTED_ACTION = new Action("gridItemSelectedAction");
	public static final Action TREE_ITEM_SELECTED_ACTION = new Action("treeItemSelectedAction");
	private static final String I18N_SEARCHSETTINGS_DESC = "AbstractTreeGridComponent.filtersettings.desc";
	private static final String I18N_SEARCHFIELD_PLACEHOLDER = "RegisterComponent.filter.placeholder";
	private static final String I18N_DOWNLOADWORKSETBUTTON_DESC = "AbstractTreeGridComponent.downloadWorksetButton.desc";
	private static final String I18N_USERLAYOUTSETTINGS_DESC = "AbstractTreeGridComponent.settings.desc";
	
	protected DraggableTree<NodeWrapper> tree;
	protected Grid<T> grid;
	protected ProjectTreeDataProvider projectTreeDataProvider;
	
	protected IDataService<T, ?> service;
	protected IUserSettigsService userSettingsService;
	protected MessageSource messageSource;
	protected String user;
	
	private Map<Action, Set<Listener>> listeners;
	private HorizontalLayout headFuturesLayout;
	private TextField registerFilterField;
	private Button registerFilterSettingsButton;
	private Button downloadWorksetButton;
	private Button userLayoutSettingsButton;
	
	
	public abstract boolean showTreeByDefault();
	public abstract <T, F> AbstractRegistryDataProvider<T, F> getGridDataProvider();
	public abstract ProjectTreeDataProvider getTreeDataProvider();
	public abstract Grid<T> getRegisterGrid();
	public abstract String getReportName();
	/* Fill these UI elements with data from the {@code UserSettingsJson uiSettings} */
	public abstract void refreshUiElements();
	
	
	public AbstractTreeGridComponent(IDataService<T, ?> service, IUserSettigsService userSettingsService) {
		this.service = service;
		this.userSettingsService = userSettingsService;
		this.user = userSettingsService.getCurrentUser();
		this.messageSource = service.getMessageSource();
		
		setSpacing(false);
		setMargin(false);

		initEventActions();
		createHeadFutures();
		createBody();
		createFooter();
		refreshUiElements();
	}

	private void initEventActions() {
		listeners = new HashMap<>();
		listeners.put(GRID_ITEM_SELECTED_ACTION, new HashSet<>());
		listeners.put(TREE_ITEM_SELECTED_ACTION, new HashSet<>());
	}

	private void createHeadFutures() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(false);
		verticalLayout.setSpacing(false);
		AbsoluteLayout layout = new AbsoluteLayout();
		
		layout.setStyleName("gzpn-head");
		layout.setHeight(50.0f, Unit.PIXELS);
		layout.setWidth(100.f, Unit.PERCENTAGE);
		headFuturesLayout = new HorizontalLayout();
		headFuturesLayout.addComponent(createRegisterFilter());

		headFuturesLayout.addComponent(createExcelButton());
		layout.addComponent(createSettingsButton(), "top:5px; left:5px");
		layout.addComponent(headFuturesLayout, "top:5px; right:5px");
		
		verticalLayout.addComponent(layout);
		this.addComponent(verticalLayout);	
	}

	private Component createRegisterFilter() {
		registerFilterField = new TextField();
		registerFilterField.setWidth(300.0f, Unit.PIXELS);
		registerFilterSettingsButton = new Button();
		registerFilterSettingsButton.setIcon(VaadinIcons.FILTER);
		registerFilterSettingsButton.setDescription(getI18nText(I18N_SEARCHSETTINGS_DESC, messageSource));
		JoinedLayout<TextField, Button> searchComp = new JoinedLayout<>(registerFilterField, registerFilterSettingsButton);
		registerFilterField.setPlaceholder(getI18nText(I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
		
		registerFilterField.addValueChangeListener(e -> {
			getGridDataProvider().getFilter().setCommonTextFilter(e.getValue());
			getGridDataProvider().refreshAll();
		});
		return searchComp;
	}

	private Component createExcelButton() {
		downloadWorksetButton = new Button(VaadinIcons.TABLE);
		downloadWorksetButton.setDescription(getI18nText(I18N_DOWNLOADWORKSETBUTTON_DESC, messageSource));
		StreamResource excelStreamResource = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(getRegisterGrid()), getReportName());
		
		FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
		excelFileDownloader.extend(downloadWorksetButton);
		
		return downloadWorksetButton;
	}

	private Component createSettingsButton() {
		userLayoutSettingsButton = new Button();
		userLayoutSettingsButton.setIcon(VaadinIcons.COG_O);
		userLayoutSettingsButton.setDescription(getI18nText(I18N_USERLAYOUTSETTINGS_DESC, messageSource, I18N_USERLAYOUTSETTINGS_DESC));
		userLayoutSettingsButton.addClickListener(event -> {
			CreateDocSettingsWindow settingsWindow = new CreateDocSettingsWindow(userSettingsService, messageSource);
			settingsWindow.addOnSaveAndCloseListener(closeEvent -> {
				refreshUiElements();
			});
			getUI().getUI().addWindow(settingsWindow);
		});
		return userLayoutSettingsButton;
	}

	private void createBody() {
		
	}

	private void createFooter() {
	}

}
