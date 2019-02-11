package ru.gzpn.spc.csl.ui.common;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDropTarget;
import com.vaadin.ui.components.grid.TreeGridDragSource;
import com.vaadin.ui.components.grid.TreeGridDropTarget;

import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.createdoc.ProjectItemIconGenerator;

@SuppressWarnings("serial")
public abstract class AbstractTreeGridSettingsWindow extends AbstractUiSettingsWindow {

	private static final String I18N_WINDOW_CAPTION = "TreeGridSettingsWindow.cap";
	private static final String I18N_COLUMNSTAB_CAPTION = "TreeGridSettingsWindow.columnstab.cap";
	private static final String I18N_HEADERSTAB_CAPTION = "TreeGridSettingsWindow.headerstab.cap";
	private static final String I18N_TREE_CAPTION = "TreeGridSettingsWindow.tree.cap";
	
	public static final int GRID_ROWS = 8;
	public static final int GRID_ROW_HEIGHT = 38;
	
	
	private TabSheet tabSheet;
	private HorizontalSplitPanel splitPanel;
	private DraggableTree<NodeWrapper> projectTree;
	private List<NodeWrapper> draggedItems;
	private TreeData<NodeWrapper> treeData;
	private TreeDataProvider<NodeWrapper> treeDataProvider;
	private ComponentContainer columnsLayout;
	private Grid<ColumnSettingsPresenter> columnsGrid;
	private List<ColumnSettingsPresenter> columnsGridData;

	
	public abstract NodeWrapper getTreeSettings();
	public abstract List<ColumnSettings> getColumnSettings();
	public abstract List<ColumnHeaderGroup> getHeaderSettings();
	
	public AbstractTreeGridSettingsWindow(IUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
		this.userSettings = settingsService.getCreateDocUserSettings(user, new CreateDocSettingsJson());
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
		treeData = new TreeData<>();
		treeData.addItem(null, rootNode);

		while (rootNode.hasChild()) {
			treeData.addItem(rootNode, rootNode.getChild());
			rootNode = rootNode.getChild();
		}

		onRefreshUiTreeData(treeData);
	}
	
	private void refreshColumnsGrid() {
		columnsLayout.removeAllComponents();
		columnsGrid = new Grid<>();
		columnsGrid.setSizeFull();
		columnsGrid.setHeightByRows(GRID_ROWS);
		columnsLayout.addComponent(columnsGrid);
		
		GridDragSource<ColumnSettingsPresenter> dragSource = new GridDragSource<>(columnsGrid);
		GridDropTarget<NodeWrapper> dropTarget = new GridDropTarget<>(projectTree.getCompositionRoot(), DropMode.ON_TOP);
		
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
	
	private void refreshHeadersGrid() {
		// TODO Auto-generated method stub
		
	}

	private Component createRightColumnsGrid() {
		// TODO Auto-generated method stub
		return null;
	}

	private Component createColumnHeadersGrid() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void refreshSettings() {
		// TODO Auto-generated method stub

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
		IUserSettigsService settingsService;
		
		public ColumnSettingsPresenter(ColumnSettings columnSettings, IUserSettigsService settingsService) {
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
		IUserSettigsService settingsService;
		
		public ColumnHeaderPresenter() {
			super();
		}
		public ColumnHeaderPresenter(ColumnHeaderGroup item, IUserSettigsService settingsService) {
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

		public IUserSettigsService getSettingsService() {
			return settingsService;
		}

		public void setSettingsService(IUserSettigsService settingsService) {
			this.settingsService = settingsService;
		}
	
		@Override
		public boolean isShown() {
			return getVisibilityCheckBox().getValue();
		}
	}
}
