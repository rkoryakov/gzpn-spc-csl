package ru.gzpn.spc.csl.ui.createdoc;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;

import com.vaadin.data.ValueProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.dataproviders.DocumentsDataProvider;
import ru.gzpn.spc.csl.model.enums.DocType;
import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.interfaces.IBaseEntity;
import ru.gzpn.spc.csl.model.interfaces.IDocument;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.presenters.interfaces.IDocumentPresenter;
import ru.gzpn.spc.csl.services.bl.interfaces.ICreateDocService;
import ru.gzpn.spc.csl.services.bl.interfaces.IDocumentService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.I18n;
import ru.gzpn.spc.csl.ui.common.JoinedLayout;
import ru.gzpn.spc.csl.ui.common.data.export.Exporter;

class WorkSetDocumentationComponent extends VerticalLayout implements I18n {
	private static final long serialVersionUID = -7505276213420043371L;

	public static final String I18N_SEARCHFIELD_PLACEHOLDER = "createdoc.WorkSetDocumentation.documentFilterField.placeholder";
	public static final String I18N_SEARCHSETTINGS_DESC = "createdoc.WorkSetDocumentation.documentFilterSettingsButton.desc";
	public static final String I18N_USERLAYOUTSETTINGS_DESC = CreateDocComponent.I18N_USERLAYOUTSETTINGS_DESC;
	public static final String I18N_ADDDOCUMENTBUTTON_DESC = "createdoc.WorkSetDocumentation.addDocumentButton.desc";
	public static final String I18N_DELDOCUMENTBUTTON_DESC = "createdoc.WorkSetDocumentation.delDocumentButton.desc";
	public static final String I18N_DOWNLOADDOCUMENTSBUTTON_DESC = "createdoc.WorkSetDocumentation.downloadDocumentsButton.desc";
	public static final String I18N_SENDBUTTON_CAP = "createdoc.WorkSetDocumentation.sendButton.cap";
	public static final String I18N_DESCRIPTIONFIELD_PLACEHOLDER = "createdoc.WorkSetDocumentation.descriptionField.placeholder";
	
	public static final double DOCUMENT_GRID_ROWS = 10;

	private String currentUser;
	private IUserSettigsService settingsService;
	private MessageSource messageSource;
	private IDocumentService documentService;
	
	private Grid<IDocumentPresenter> documentsGrid;	
	private DocumentsDataProvider documentsDataProvider;
	private CreateDocComponent parent;

	private TextField documentsFilterField;

	private Button documentsFilterSettingsButton;
	private Button addDocumentButton;
	private Button delDocumentButton;
	private Button downloadDocumentsButton;

	private Button userLayoutSettings;

	private Button sendButton;

	private TextArea descriptionField;
	
	public WorkSetDocumentationComponent(ICreateDocService service) {
		this.documentService = service.getDocumentService();
		this.settingsService = service.getUserSettingsService();
		this.messageSource = service.getMessageSource();
		this.currentUser = settingsService.getCurrentUser();
		refreshUiElements();
	}
	
	public DocumentsDataProvider getDocumentsDataProvider() {
		return documentsDataProvider;
	}
	
	public void setParent(CreateDocComponent parent) {
		this.parent = parent;
	}
	
	public void refreshUiElements() {
		this.removeAllComponents();
		setMargin(false);
		setSpacing(false);
		setSizeFull();
		
		addComponent(createRightHeadFeautures());
		addComponent(createDocumentGrid());
		addComponent(createFooterFeauteres());
	}

	public Component createFooterFeauteres() {
		HorizontalLayout layout = new HorizontalLayout();
		
		layout.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
		layout.addComponent(createDescriptionField());
		layout.setExpandRatio(descriptionField, 2);
		layout.addComponent(createSendButton());
		layout.setExpandRatio(sendButton, 1);
		
		layout.setSizeFull();
		layout.setMargin(true);
		
		return layout;
	}

	public Component createSendButton() {
		sendButton = new Button(getI18nText(I18N_SENDBUTTON_CAP, messageSource, I18N_SENDBUTTON_CAP));
		sendButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		sendButton.addClickListener(clickEvent -> {
			// TODO:
		});
		return sendButton;
	}

	public Component createDescriptionField() {
		descriptionField = new TextArea();
		descriptionField.setPlaceholder(getI18nText(I18N_DESCRIPTIONFIELD_PLACEHOLDER, messageSource));
		descriptionField.setSizeFull();
		descriptionField.setHeight(100, Unit.PIXELS);
		return descriptionField;
	}

	public Component createRightHeadFeautures() {
		AbsoluteLayout layout = new AbsoluteLayout();
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		layout.setStyleName("gzpn-head");
		layout.setHeight(50.0f, Unit.PIXELS);
		layout.setWidth(100.f, Unit.PERCENTAGE);
		horizontalLayout.addComponent(createDocumentsFilter());
		horizontalLayout.addComponent(createDocumentsButtons());
		horizontalLayout.addComponent(createExcelButton());
		layout.addComponent(createSettingsButton(), "top:5px; left:5px");
		layout.addComponent(horizontalLayout, "top:5px; right:5px");
		return layout;
	}

	public Component createDocumentsFilter() {
		documentsFilterField = new TextField();
		documentsFilterField.setWidth(200.0f, Unit.PIXELS);
		documentsFilterSettingsButton = new Button();
		documentsFilterSettingsButton.setIcon(VaadinIcons.FILTER);
		documentsFilterSettingsButton.setDescription(getI18nText(I18N_SEARCHSETTINGS_DESC, messageSource));
		JoinedLayout<TextField, Button> searchComp = new JoinedLayout<>(documentsFilterField, documentsFilterSettingsButton);
		documentsFilterField.setPlaceholder(getI18nText(I18N_SEARCHFIELD_PLACEHOLDER, messageSource));
		
		documentsFilterField.addValueChangeListener(e -> {
			documentsDataProvider.getFilter().setCommonTextFilter(e.getValue());
			documentsDataProvider.refreshAll();
		});
		return searchComp;
	}

	public Component createDocumentsButtons() {
		JoinedLayout<Button, Button> buttons = new JoinedLayout<>();
		addDocumentButton = new Button(VaadinIcons.PLUS);
		addDocumentButton.addClickListener(clickEvent -> {
			DocumentsDataProvider provider = (DocumentsDataProvider)documentsGrid.getDataProvider();
			provider.createEmptyItem();
			provider.refreshAll();
		});
		
		delDocumentButton = new Button(VaadinIcons.MINUS);
		delDocumentButton.addClickListener(clickEvent -> {
			documentsGrid.getSelectedItems().forEach(item -> {
					documentService.remove(item);
			});
			
			documentsGrid.deselectAll();
			((DocumentsDataProvider)documentsGrid.getDataProvider()).refreshAll();
		});
		
		addDocumentButton.setDescription(getI18nText(I18N_ADDDOCUMENTBUTTON_DESC, messageSource));
		delDocumentButton.setDescription(getI18nText(I18N_DELDOCUMENTBUTTON_DESC, messageSource));
		addDocumentButton.setEnabled(true);
		delDocumentButton.setEnabled(true);
		
		buttons.addComponent(addDocumentButton);
		buttons.addComponent(delDocumentButton);
		
		return buttons;
	}

	public Component createExcelButton() {
		downloadDocumentsButton = new Button(VaadinIcons.TABLE);
		downloadDocumentsButton.setDescription(getI18nText(I18N_DOWNLOADDOCUMENTSBUTTON_DESC, messageSource));
		StreamResource excelStreamResource = new StreamResource(
				(StreamResource.StreamSource) () -> Exporter.exportAsExcel(documentsGrid), "documents.xls");
		FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
		excelFileDownloader.extend(downloadDocumentsButton);
		
		return downloadDocumentsButton;
	}
	
	public Component createSettingsButton() {
		userLayoutSettings = new Button();
		userLayoutSettings.setIcon(VaadinIcons.COG_O);
		userLayoutSettings.setDescription(getI18nText(I18N_USERLAYOUTSETTINGS_DESC, messageSource));
		userLayoutSettings.addClickListener(event -> {
			WorkSetDocumentationSettingsWindow settingsWindow = new WorkSetDocumentationSettingsWindow(settingsService, messageSource);
			settingsWindow.addOnSaveListener(closeEvent -> 
				refreshUiElements()
			);
			getUI().getUI().addWindow(settingsWindow);
		});
		return userLayoutSettings;
	}
	
	public Component createDocumentGrid() {
		documentsGrid = new Grid<>();
		refreshDocumentGrid();
		return documentsGrid;
	}

	private void refreshDocumentGrid() {
		documentsGrid.removeAllColumns();
		documentsDataProvider = new DocumentsDataProvider(documentService);
		CreateDocSettingsJson userSettings = (CreateDocSettingsJson)settingsService.getCreateDocUserSettings(currentUser, new CreateDocSettingsJson());
		List<ColumnSettings> columnSettings = userSettings.getRightResultColumns();
		
		columnSettings.sort((cs1, cs2) -> 
			Integer.compare(cs1.getOrderIndex(), cs2.getOrderIndex())
		);
		columnSettings.forEach(this::addDocumentGridColumn);
		documentsGrid.setSizeFull();
		documentsGrid.setHeightByRows(DOCUMENT_GRID_ROWS);
		documentsGrid.setColumnReorderingAllowed(true);
		documentsDataProvider.setShownColumns(columnSettings);
		documentsGrid.setDataProvider(documentsDataProvider);
		documentsGrid.getEditor().setSaveCaption(getI18nText(CreateDocComponent.I18N_SAVE_ITEM_CAP, messageSource));
		documentsGrid.getEditor().setCancelCaption(getI18nText(CreateDocComponent.I18N_CANCELSAVE_ITEM_CAP, messageSource));
		
		documentsGrid.getEditor().addSaveListener(saveEvent -> {
			saveDocumentItem(saveEvent.getBean());
		});
		documentsGrid.getEditor().addCancelListener(cancelSaveEvent -> {
			documentsGrid.getDataProvider().refreshItem(cancelSaveEvent.getBean());
		});
		
		documentsGrid.getEditor().setEnabled(true);
		documentsGrid.setSelectionMode(SelectionMode.MULTI);
		// test column headers
		userSettings.getRightColumnHeaders();
		addDocumentHeaderColumns(userSettings);
	}
	
	private void saveDocumentItem(IDocumentPresenter bean) {
		this.documentService.save(bean);
		this.documentsGrid.getDataProvider().refreshItem(bean);
		
	}

	public void addDocumentGridColumn(ColumnSettings settings) {
		switch (settings.getEntityFieldName()) {		
		case IDocument.FIELD_NAME:
			addDocumnentGridColumn(settings, IDocumentPresenter::getName, IDocument.FIELD_NAME)
				.setEditorComponent(new TextField(), IDocumentPresenter::setName).setEditable(true);
			break;
		case IDocument.FIELD_CODE:
			addDocumnentGridColumn(settings, IDocumentPresenter::getCode, IDocument.FIELD_CODE)
				.setEditorComponent(new TextField(), IDocumentPresenter::setCode).setEditable(true);
			break;
		case IDocument.FIELD_TYPE:
			addDocumnentGridColumn(settings, IDocumentPresenter::getType, IDocument.FIELD_TYPE)
				.setEditorComponent(new ComboBox<DocType>("", DocType.getAll()), IDocumentPresenter::setType).setEditable(true);
			break;
		case IDocument.FIELD_WORK:
			addDocumnentGridColumn(settings, IDocumentPresenter::getWorkText, IDocument.FIELD_WORK);
			break;
		case IDocument.FIELD_WORKSET:
			addDocumnentGridColumn(settings, IDocumentPresenter::getWorksetText, IDocument.FIELD_WORKSET);
			break;
		case IDocument.FIELD_ID:
			addDocumnentGridColumn(settings, IDocumentPresenter::getId, IDocument.FIELD_ID);
			break;
		case IDocument.FIELD_VERSION:
			addDocumnentGridColumn(settings, IDocumentPresenter::getVersion, IDocument.FIELD_VERSION);
			break;
		case IDocument.FIELD_CREATE_DATE:
			addDocumnentGridColumn(settings, IDocumentPresenter::getCreateDateText, IBaseEntity.BASE_FIELD_CREATE_DATE);
			break;
		case IDocument.FIELD_CHANGE_DATE:
			addDocumnentGridColumn(settings, IDocumentPresenter::getChangeDateText, IBaseEntity.BASE_FIELD_CHANGE_DATE);
			break;
			default:
		}
	}
	
	public <T> Column<IDocumentPresenter, T> addDocumnentGridColumn(ColumnSettings settings, ValueProvider<IDocumentPresenter, T> provider, String field) {
		Column<IDocumentPresenter, T> column = documentsGrid.addColumn(provider);
		column.setSortProperty(settings.getEntityFieldName());
		column.setSortable(true);
		column.setCaption(Entities.getEntityFieldText(field, messageSource));
		Double width = settings.getWidth();
		
		if (Objects.nonNull(width) && Double.isFinite(width) && width > 1) {
			column.setWidth(width);
		} else {
			column.setWidthUndefined();
		}
		
		if (!settings.isShown()) {	
			column.setHidden(true);
		}
		
		column.setId(settings.getEntityFieldName());
		
		return column;
	}
	
	public void addDocumentHeaderColumns(CreateDocSettingsJson userSettings) {
		if (userSettings.hasRightColumnHeaders()) {
			refreshColumnHeaderGroups(userSettings.getRightColumnHeaders());
		}
		documentsGrid.setHeightByRows(DOCUMENT_GRID_ROWS - documentsGrid.getHeaderRowCount() + 1);
	}

	public void refreshColumnHeaderGroups(List<ColumnHeaderGroup> groups) {
		final String headStyle = "v-grid-header-align-left";
		Deque<List<ColumnHeaderGroup>> childGroups = new LinkedList<>();
		childGroups.push(groups);
		removePrepandedHeaderRows();
		
		HeaderRow headerRow = documentsGrid.prependHeaderRow();
		headerRow.setStyleName(headStyle);

		while (!childGroups.isEmpty()) {
			List<ColumnHeaderGroup> list = childGroups.pop();
			Iterator<ColumnHeaderGroup> it = list.iterator();
			
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();

				if (g.hasChildrenGroups()) {
					HeaderRow subRow = documentsGrid.prependHeaderRow();
					subRow.setStyleName(headStyle);
					childGroups.push(g.getChildren());
					HeaderCell groupCell = subRow.join(getDocumentColumnIds(g.getChildren()));
					groupCell.setText(g.getCaption());
					break;
				
				} else if (g.hasColumns()) {
					
						HeaderCell groupCell = headerRow.join(g.getColumns().stream().map(column -> 
								column.getEntityFieldName())
										.toArray(String[]::new));
						
						groupCell.setText(g.getCaption());
						childGroups.pollFirst();
				}
			}
		}
	}
	
	public void removePrepandedHeaderRows() {
		int count = documentsGrid.getHeaderRowCount();
		if (count > 1) {
			for (int i = 0; i < count - 1; i ++) {
				documentsGrid.removeHeaderRow(0);
			}
		}
	}
	
	/**
	 * Get column Ids that are contained in the given head groups:
	 *  _____________________________________
	 * |	 Header1	 |  	Header2	     |
	 * |colum1 | column2 | column3 | column4 |
	 * 
	 */
	public String[] getDocumentColumnIds(List<ColumnHeaderGroup> groups) {
		Set<String> columnIds = new HashSet<>();

		for (int i = 0; i < groups.size(); i ++) {
			Iterator<ColumnHeaderGroup> it = groups.listIterator(i);
			while (it.hasNext()) {
				ColumnHeaderGroup g = it.next();
				if (g.hasChildrenGroups()) {
					it = g.getChildren().iterator();
				} else if (g.hasColumns()) {
					columnIds.addAll(g.getColumns().stream().map(c -> c.getEntityFieldName()).collect(Collectors.toSet()));
				}
			}
		}
		return columnIds.toArray(new String[0]);
	}
	
}