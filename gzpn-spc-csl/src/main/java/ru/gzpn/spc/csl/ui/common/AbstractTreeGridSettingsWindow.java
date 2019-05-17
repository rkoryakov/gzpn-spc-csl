package ru.gzpn.spc.csl.ui.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDropTarget;
import com.vaadin.ui.components.grid.TreeGridDragSource;
import com.vaadin.ui.components.grid.TreeGridDropTarget;
import com.vaadin.ui.themes.ValoTheme;

import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.createdoc.ProjectItemIconGenerator;

@SuppressWarnings("serial")
public abstract class AbstractTreeGridSettingsWindow extends AbstractUiSettingsWindow {

	public static final String I18N_WINDOW_CAPTION = "TreeGridSettingsWindow.cap";
	public static final String I18N_COLUMNSTAB_CAPTION = "TreeGridSettingsWindow.columnstab.cap";
	public static final String I18N_HEADERSTAB_CAPTION = "TreeGridSettingsWindow.headerstab.cap";
	public static final String I18N_TREE_CAPTION = "TreeGridSettingsWindow.tree.cap";
	public static final String I18N_SETTINGS_ADD_HEADER_BUTTON = "TreeGridSettingsWindow.addButton.cap";
	public static final String I18N_SETTINGS_ADD_HEADER_NEWITEM = "TreeGridSettingsWindow.newItem.cap";
	
	public static final int GRID_ROWS = 8;
	public static final int GRID_ROW_HEIGHT = 38;
	
	
	protected TabSheet tabSheet;
	protected HorizontalSplitPanel splitPanel;
	protected DraggableTree<NodeWrapper> projectTree;
	protected List<NodeWrapper> draggedItems;
	protected TreeData<NodeWrapper> treeData;
	protected TreeDataProvider<NodeWrapper> treeDataProvider;
	protected VerticalLayout columnsLayout;
	protected Grid<ColumnSettingsPresenter> columnsGrid;
	protected List<ColumnSettingsPresenter> columnsGridData;
	protected Grid<ColumnHeaderPresenter> headersGrid;
	protected VerticalLayout headersLayout;
	protected List<ColumnHeaderPresenter> headersGridData;
	protected Button addButton;

	
	public abstract NodeWrapper getTreeSettings();
	public abstract void setTreeSettings(NodeWrapper node);
	
	public abstract List<ColumnSettings> getColumnSettings();
	public abstract void setColumnSettings(List<ColumnSettings> columns);
	
	public abstract List<ColumnHeaderGroup> getHeaderSettings();
	public abstract void setHeaderSettings(List<ColumnHeaderGroup> headers);
	
	public AbstractTreeGridSettingsWindow(IUserSettingsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
		this.center();
		this.setCaption(getI18nText(I18N_WINDOW_CAPTION, messageSource));
		this.setModal(true);
		this.setWidth(70, Unit.PERCENTAGE);
		this.setHeight(60, Unit.PERCENTAGE);
	}

	@Override
	public ComponentContainer createBodyLayout() {
		VerticalLayout bodyLayout = new VerticalLayout();
		tabSheet = new TabSheet();
		
		VerticalLayout columnsSettings = new VerticalLayout();
		splitPanel = new HorizontalSplitPanel();
		splitPanel.setFirstComponent(createLeftTree());
		splitPanel.setSecondComponent(createRightColumnsGrid());
		splitPanel.setSplitPosition(30);
		columnsSettings.addComponent(splitPanel);
		
		tabSheet.addTab(splitPanel, getI18nText(I18N_COLUMNSTAB_CAPTION, messageSource));
		tabSheet.addTab(createColumnHeadersGrid(), getI18nText(I18N_HEADERSTAB_CAPTION, messageSource));
		
		bodyLayout.addComponent(tabSheet);
		
		return bodyLayout;
	}

	public Component createLeftTree() {
		VerticalLayout leftLayout = new VerticalLayout();
		leftLayout.setMargin(false);
		leftLayout.setSpacing(false);
		leftLayout.setSizeFull();
		leftLayout.addComponent(createProjectTree());
		return leftLayout;
	}

	public Component createProjectTree() {
		projectTree = new DraggableTree<>();
		Panel panel = new Panel(getI18nText(I18N_TREE_CAPTION, messageSource));
		
		projectTree.setItemCaptionGenerator(item -> item.getNodeSettingsCaption(messageSource));
		projectTree.setItemIconGenerator(new ProjectItemIconGenerator());
		projectTree.setSizeFull();
		projectTree.setHeight(GRID_ROW_HEIGHT*(GRID_ROWS-1)+GRID_ROW_HEIGHT-1, Unit.PIXELS);
		
		refreshUiTreeData();
		
		TreeGridDragSource<NodeWrapper> dragSource = new TreeGridDragSource<>(projectTree.getCompositionRoot());
		TreeGridDropTarget<NodeWrapper> dropTarget = new TreeGridDropTarget<>(projectTree.getCompositionRoot(), DropMode.ON_TOP);
		
		dragSource.addGridDragStartListener(event -> 
			draggedItems = event.getDraggedItems()
		);
		
		dropTarget.addTreeGridDropListener(event -> {
			event.getDropTargetRow().ifPresent(this::putDraggedItemsAt);
		});
		
		panel.setContent(projectTree);
		panel.setSizeFull();
		
		return panel;
	}
	
	private void putDraggedItemsAt(NodeWrapper to) {
		if (CollectionUtils.isNotEmpty(draggedItems)) {
			NodeWrapper draggedNode = draggedItems.get(0);
			
			List<NodeWrapper> listNodes = new LinkedList<>();
			TreeData<NodeWrapper> newTreeData = new TreeData<>();
			NodeWrapper root = null;
			List<NodeWrapper> children = treeData.getChildren(root);
			
			while (CollectionUtils.isNotEmpty(children)) {
				listNodes.add(children.get(0));
				children = treeData.getChildren(children.get(0));
			}
			
			int pasteIndex = listNodes.indexOf(to);
			listNodes.remove(draggedNode);
			
			// when we drag a node from top to bottom
			pasteIndex = (pasteIndex < 0 || pasteIndex > listNodes.size()) ? listNodes.size() : pasteIndex;
			
			listNodes.add(pasteIndex, draggedNode);
			ListIterator<NodeWrapper> it = listNodes.listIterator();
			while (it.hasNext()) {
				NodeWrapper node = it.next();
				node.setParent(root);
				
				if (Objects.nonNull(root)) {
					root.setChild(node);
				}
				newTreeData.addItem(root, node);
				root = node;
			}
			NodeWrapper lastNode = it.previous();
			lastNode.setChild(null);
			
			treeData = newTreeData;
			onRefreshUiTreeData(newTreeData);
		}
	}
	
	public void onRefreshUiTreeData(TreeData<NodeWrapper> newTreeData) {
		treeDataProvider = new TreeDataProvider<>(treeData);
		projectTree.setDataProvider(treeDataProvider);
		projectTree.expandRecursively(treeData.getRootItems(), 10);
	}

	public void refreshUiTreeData() {
		NodeWrapper rootNode = getTreeSettings();

		if (rootNode != null) {
			treeData = new TreeData<>();
			treeData.addItem(null, rootNode);

			while (rootNode.hasChild()) {
				treeData.addItem(rootNode, rootNode.getChild());
				rootNode = rootNode.getChild();
			}

			onRefreshUiTreeData(treeData);
		}
	}
	
	private void refreshColumnsGrid() {
		columnsLayout.removeAllComponents();
		columnsGrid = new Grid<>();
		columnsGrid.setSizeFull();
		columnsGrid.setHeightByRows(GRID_ROWS);
		columnsLayout.addComponent(columnsGrid);
		
		GridDragSource<ColumnSettingsPresenter> dragSource = new GridDragSource<>(columnsGrid);
		if (projectTree != null) {
			GridDropTarget<NodeWrapper> dropTarget = new GridDropTarget<>(projectTree.getCompositionRoot(), DropMode.ON_TOP);
		}
		
		dragSource.addGridDragStartListener(dragEvent -> {
			List<ColumnSettingsPresenter> items = dragEvent.getDraggedItems();
			this.draggedItems = items.stream().map(item -> 
				new NodeWrapper(item.getEntityName(), item.getEntityFieldName().substring(item.getEntityName().length() + 1)))
					.collect(Collectors.toList());
		});

		List<ColumnSettings> columns = getColumnSettings();
		List<ColumnHeaderGroup> headers = getHeaderSettings();
		Set<ColumnHeaderGroup> flatHeaders = toFlatSet(headers);
		
		setColumnAttribures("id", columnsGrid.addColumn(presenter -> presenter.getEntityFieldNameText(messageSource)), I18N_SETTINGS_COLUMN_ID);
		setColumnAttribures("entity", columnsGrid.addColumn(presenter -> presenter.getEntityNameText(messageSource)), I18N_SETTINGS_COLUMN_ENTITY);
		setColumnAttribures("visibility", columnsGrid.addComponentColumn(ColumnSettingsPresenter::getVisibilityCheckBox), I18N_SETTINGS_COLUMN_VISIBLE);
		setColumnAttribures("headers", columnsGrid.addComponentColumn(ColumnSettingsPresenter::getMergedHeadComboBox), I18N_SETTINGS_COLUMN_MERGEDHEAD);
		
		this.columnsGridData = columns.stream().map(item -> {
			ColumnSettingsPresenter resultItem = new ColumnSettingsPresenter(item, settingsService);
			resultItem.setVisibilityCheckBox(cretateGridColumnVisibleCheckBox(item));
			resultItem.setMergedHeadComboBox(cretateGridColumnMergedHeadComboBox(item, flatHeaders));
			return resultItem;
		}).collect(Collectors.toList());
		
		columnsGrid.setItems(this.columnsGridData);
	}

	private Set<ColumnHeaderGroup> toFlatSet(Collection<ColumnHeaderGroup> leftColumnHeaders) {
		Set<ColumnHeaderGroup> result = new HashSet<>();
		result.addAll(leftColumnHeaders);
		
		for (ColumnHeaderGroup g : leftColumnHeaders) {
			if (g.hasChildrenGroups()) {
				result.addAll(g.getChildren());
			}
		}
		
		return result;
	}
	
	public <C, T> void setColumnAttribures(String id, Column<C, T> column, String i18nCaption) {
		column.setSortProperty(id);
		column.setSortable(true);
		column.setCaption(getI18nText(i18nCaption, messageSource, i18nCaption));
		column.setId(id);
	}
	
	public CheckBox cretateGridColumnVisibleCheckBox(ColumnSettings item) {
		CheckBox checkBox = new CheckBox();
		checkBox.setValue(item.isShown());
	
		return checkBox;
	}
	
	public ComboBox<String> cretateGridColumnMergedHeadComboBox(ColumnSettings item, Set<ColumnHeaderGroup> flatHeaders) {
		ComboBox<String> mergedHeadComboBox = new ComboBox<>();

		mergedHeadComboBox.setItems(flatHeaders.stream().map(element -> element.getCaption()));
		ColumnHeaderGroup header = flatHeaders.stream()
				.filter(element -> element.hasColumns() && element.getColumns().contains(item)).findFirst().orElse(null);
		if (header != null) {
			mergedHeadComboBox.setSelectedItem(header.getCaption());
		}
		
		return mergedHeadComboBox;
	}
	
	private void refreshHeadersGrid() {
		if (headersGrid != null) {
			headersLayout.removeComponent(headersGrid);
		}
		
		headersGrid = new Grid<>();
		headersLayout.addComponent(headersGrid);
		headersGrid.setSizeFull();
		headersGrid.setHeightByRows(GRID_ROWS);
		
		List<ColumnHeaderGroup> headers = getHeaderSettings();
		Set<ColumnHeaderGroup> flatHeaders = toFlatSet(headers);
		
		setColumnAttribures("id", headersGrid.addColumn(ColumnHeaderPresenter::getCaption).setEditorComponent(new TextField(), ColumnHeaderPresenter::setCaption).setEditable(true), I18N_SETTINGS_HEADERS_COLUMN_ID);
		setColumnAttribures("visibility", headersGrid.addComponentColumn(ColumnHeaderPresenter::getVisibilityCheckBox), I18N_SETTINGS_HEADERS_COLUMN_VISIBLE);
		setColumnAttribures("headers", headersGrid.addComponentColumn(ColumnHeaderPresenter::getMergedHeadComboBox), I18N_SETTINGS_HEADERS_COLUMN_HEAD);
		setColumnAttribures("remove", headersGrid.addComponentColumn(ColumnHeaderPresenter::getRemoveButton), "");
		
		headersGrid.getEditor().addSaveListener(saveEvent -> {
			save();
			headersGrid.setItems(this.headersGridData);
		});
		
		headersGrid.getEditor().setEnabled(true);
		
		this.headersGridData = flatHeaders.stream().map(item -> {
			ColumnHeaderPresenter resultItem = new ColumnHeaderPresenter(item, settingsService);
			resultItem.setVisibilityCheckBox(cretateGridColumnHeaderVisibleCheckBox(item));
			resultItem.setMergedHeadComboBox(cretateGridColumnHeaderMergedHeadComboBox(item, headers, flatHeaders));
			resultItem.setRemoveButton(cretateGridColumnHeaderRemoveButton(resultItem));
			return resultItem;
		}).collect(Collectors.toList());
		
		headersGrid.setItems(this.headersGridData);
	}

	public CheckBox cretateGridColumnHeaderVisibleCheckBox(ColumnHeaderGroup item) {
		CheckBox checkBox = new CheckBox();
		checkBox.setValue(item.isShown());
		return checkBox;
	}
	
	public ComboBox<String> cretateGridColumnHeaderMergedHeadComboBox(ColumnHeaderGroup item, List<ColumnHeaderGroup> headers, Set<ColumnHeaderGroup> flatHeaders) {
		ComboBox<String> mergedHeadComboBox = new ComboBox<>();

		mergedHeadComboBox.setItems(flatHeaders.stream().map(element -> element.getCaption()));
		ColumnHeaderGroup parentHeader = getParentHeader(headers, item);
		if (parentHeader != null) {
			mergedHeadComboBox.setSelectedItem(parentHeader.getCaption());
		}
		
		return mergedHeadComboBox;
	}
	
	protected ColumnHeaderGroup getParentHeader(List<ColumnHeaderGroup> headers, ColumnHeaderGroup item) {
		ColumnHeaderGroup result = null;
		for (ColumnHeaderGroup header : headers) {
			if (header.hasChildrenGroups() && header.getChildren().contains(item)) {
				result = header;
				break;
			} else if (header.hasChildrenGroups()) {
				getParentHeader(header.getChildren(), item);
			}
		}
		return result;
	}
	
	protected Button cretateGridColumnHeaderRemoveButton(ColumnHeaderPresenter item) {
		Button removeButton = new Button(VaadinIcons.TRASH);
		removeButton.setStyleName(ValoTheme.BUTTON_BORDERLESS);
		removeButton.addClickListener(clickEvent -> {
			int index = this.headersGridData.indexOf(item);
			this.headersGridData.remove(index);
			save();
			headersGrid.setItems(this.headersGridData);
		});
		return removeButton;
	}
	
	protected Component createRightColumnsGrid() {
		columnsLayout = new VerticalLayout();
		columnsLayout.setSizeFull();
		columnsLayout.setMargin(false);
		columnsLayout.setSpacing(false);
		refreshColumnsGrid();
		
		return columnsLayout;
	}

	protected Component createColumnHeadersGrid() {
		headersLayout = new VerticalLayout();
		
		headersLayout.setSizeFull();
		headersLayout.setMargin(true);
		headersLayout.setSpacing(true);
		
		refreshHeadersGrid();
		headersLayout.addComponentAsFirst(createColumnHeadersButtons());
		
		return headersLayout;
	}

	protected Component createColumnHeadersButtons() {
		HorizontalLayout layout = new HorizontalLayout();
		addButton = new Button();
		addButton.setIcon(VaadinIcons.PLUS);
		addButton.setDescription(getI18nText(I18N_SETTINGS_ADD_HEADER_BUTTON, messageSource));
		layout.addComponent(addButton);
		
		addButton.addClickListener(clickEvent -> {
			
			List<ColumnHeaderGroup> headers = getHeaderSettings();
			Set<ColumnHeaderGroup> flatHeaders = toFlatSet(headers);

			StringBuilder newItemText = new StringBuilder(getI18nText(I18N_SETTINGS_ADD_HEADER_NEWITEM, messageSource));
			newItemText = newItemText.append(headersGridData.size() + 1);
			ColumnHeaderGroup newHeaderGroup = new ColumnHeaderGroup(newItemText.toString());
			ColumnHeaderPresenter newGridRow = new ColumnHeaderPresenter(newHeaderGroup, settingsService);
			newGridRow.setVisibilityCheckBox(cretateGridColumnHeaderVisibleCheckBox(newGridRow));
			newGridRow.setMergedHeadComboBox(cretateGridColumnHeaderMergedHeadComboBox(newHeaderGroup, headers, flatHeaders));
			newGridRow.setRemoveButton(cretateGridColumnHeaderRemoveButton(newGridRow));
			
			this.headersGridData.add(newGridRow);
			save();
	
			this.headersGrid.setItems(headersGridData);
		});
		return layout;
	}
	
	@Override
	public void refreshSettings() {
		if (treeData != null) {
			NodeWrapper next = treeData.getRootItems().get(0);
			NodeWrapper result = next;
			result.setParent(null);

			while (!treeData.getChildren(next).isEmpty()) {
				NodeWrapper child = treeData.getChildren(next).get(0);
				child.setParent(next);
				next.setChild(child);
				next = child;
			}

			setTreeSettings(result);
		}
		List<ColumnHeaderGroup> headers = headersGridData.stream()
				.map(item -> (ColumnHeaderGroup)item)
					.peek(item -> {item.setChildren(null); item.setColumns(null);})
						.collect(Collectors.toList());
		
		refreshHeaders(headers);
		refreshColumnsSettings(headers);
		
		setHeaderSettings(this.headersGridData.stream()
					.filter(item -> !item.mergedHeadComboBox.getSelectedItem().isPresent())
						.map(this::creteHeaderGroupByPresenterHeader).collect(Collectors.toList()));
		
		setColumnSettings(this.columnsGridData.stream()
					.map(this::createColumnSettingsByPresenter).collect(Collectors.toList()));
	}
	
	
	public void refreshHeaders(List<ColumnHeaderGroup> headers) {	
		for (ColumnHeaderPresenter chp : this.headersGridData) {
			if (chp.getMergedHeadComboBox().getSelectedItem().isPresent()) {
				String caption = chp.getMergedHeadComboBox().getSelectedItem().get();
				Optional<ColumnHeaderGroup>  parent = getHeaderByCaption(caption, headers);
				if (parent.isPresent()) {
					List<ColumnHeaderGroup> children = parent.get().getChildren();
				
					if (children == null) {
						children = new ArrayList<>();
					}
					ColumnHeaderGroup ch = creteHeaderGroupByPresenterHeader(chp);
					if (!children.contains(ch)) {
						children.add(ch);
					}
					parent.get().setChildren(children);
				}
			}
		}
	}

	protected Optional<ColumnHeaderGroup> getHeaderByCaption(String caption, List<ColumnHeaderGroup> headers) {
		Optional<ColumnHeaderGroup> result = Optional.empty();
		for (ColumnHeaderGroup chg : headers) {
			if (chg.hasChildrenGroups()) {
				result = getHeaderByCaption(caption, chg.getChildren());
			}
			
			if (!result.isPresent() && chg.getCaption().equals(caption)) {
				result = Optional.of(chg);
			}
		}
		return result;
	}
	
	protected ColumnHeaderGroup creteHeaderGroupByPresenterHeader(ColumnHeaderPresenter header) {
		ColumnHeaderGroup result = new ColumnHeaderGroup();
		result.setCaption(header.getCaption());
		result.setChildren(header.getChildren());
		result.setColumns(header.getColumns());
		result.setShown(header.isShown());
		return result;
	}
	
	protected ColumnSettings createColumnSettingsByPresenter(ColumnSettingsPresenter column) {
		ColumnSettings result = new ColumnSettings();
		result.setEntityFieldName(column.getEntityFieldName());
		result.setEntityName(column.getEntityName());
		result.setOrderIndex(column.getOrderIndex());
		result.setShown(column.isShown());
		result.setWidth(column.getWidth());
		return result;
	}
	
	protected void refreshColumnsSettings(List<ColumnHeaderGroup> headers) {	
		for (ColumnSettingsPresenter csp : this.columnsGridData) {
			if (csp.getMergedHeadComboBox().getSelectedItem().isPresent()) {
				String caption = csp.getMergedHeadComboBox().getSelectedItem().get();
				Optional<ColumnHeaderGroup> header = getHeaderByCaption(caption, headers);
				if (header.isPresent()) {
					List<ColumnSettings> columns = header.get().getColumns();
					if (columns == null) {
						columns = new ArrayList<>();
					}
					csp.setOrderIndex(caption.hashCode());
					ColumnSettings cs = createColumnSettingsByPresenter(csp);
					
					if (!columns.contains(cs)) {
						columns.add(cs);
					}
					header.get().setColumns(columns);
				}
			}
		}
	}

	@Override
	public void refreshUiElements() {
		refreshUiTreeData();
		refreshColumnsGrid();
		refreshHeadersGrid();
	}

	@SuppressWarnings("serial")
	public static class ColumnSettingsPresenter extends ColumnSettings implements I18n {
		CheckBox visibilityCheckBox;
		ComboBox<String> mergedHeadComboBox;
		IUserSettingsService settingsService;
		
		public ColumnSettingsPresenter(ColumnSettings columnSettings, IUserSettingsService settingsService) {
			this.setWidth(columnSettings.getWidth());
			this.setEntityFieldName(columnSettings.getEntityFieldName());
			this.setEntityName(columnSettings.getEntityName());
			this.setOrderIndex(columnSettings.getOrderIndex());
			this.setShown(columnSettings.isShown());
			this.settingsService = settingsService;
		}
		
		public String getEntityFieldNameText(MessageSource messageSource) {
			return Entities.getEntityFieldText(this.getEntityFieldName(), messageSource);
		}

		public String getEntityNameText(MessageSource messageSource) {
			Entities entity = Entities.valueOf(this.getEntityName().toUpperCase());
			return entity.getEntityText(messageSource);
		}

		public CheckBox getVisibilityCheckBox() {
			if (visibilityCheckBox == null) {
				visibilityCheckBox = new CheckBox();
			}
			return visibilityCheckBox;
		}
		public void setVisibilityCheckBox(CheckBox visibilityCheckBox) {
			this.visibilityCheckBox = visibilityCheckBox;
		}
		public ComboBox<String> getMergedHeadComboBox() {
			return mergedHeadComboBox;
		}
		public void setMergedHeadComboBox(ComboBox<String> mergedHeadComboBox) {
			this.mergedHeadComboBox = mergedHeadComboBox;
		}
		
		@Override
		public Boolean isShown() {
			return getVisibilityCheckBox().getValue();
		}
	}
	
	@SuppressWarnings("serial")
	public static class ColumnHeaderPresenter extends ColumnHeaderGroup implements I18n {
		CheckBox visibilityCheckBox;
		ComboBox<String> mergedHeadComboBox;
		Button removeButton;
		IUserSettingsService settingsService;
		
		public ColumnHeaderPresenter() {
			super();
		}
		public ColumnHeaderPresenter(ColumnHeaderGroup item, IUserSettingsService settingsService) {
			this.setCaption(item.getCaption());
			this.setShown(item.isShown());
			this.setChildren(item.getChildren());
			this.setColumns(item.getColumns());
			this.settingsService = settingsService;
		}

		
		public CheckBox getVisibilityCheckBox() {
			if (visibilityCheckBox == null) {
				visibilityCheckBox = new CheckBox();
			}
			return visibilityCheckBox;
		}

		public void setVisibilityCheckBox(CheckBox visibilityCheckBox) {
			this.visibilityCheckBox = visibilityCheckBox;
		}

		public ComboBox<String> getMergedHeadComboBox() {
			return mergedHeadComboBox;
		}

		public void setMergedHeadComboBox(ComboBox<String> mergedHeadComboBox) {
			this.mergedHeadComboBox = mergedHeadComboBox;
		}

		public Button getRemoveButton() {
			return removeButton;
		}

		public void setRemoveButton(Button removeButton) {
			this.removeButton = removeButton;
		}

		public IUserSettingsService getSettingsService() {
			return settingsService;
		}

		public void setSettingsService(IUserSettingsService settingsService) {
			this.settingsService = settingsService;
		}
	
		@Override
		public boolean isShown() {
			return getVisibilityCheckBox().getValue();
		}
	}
}
