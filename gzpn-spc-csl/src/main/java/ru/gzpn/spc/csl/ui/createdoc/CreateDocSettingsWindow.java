package ru.gzpn.spc.csl.ui.createdoc;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.GridDragSource;
import com.vaadin.ui.components.grid.GridDropTarget;
import com.vaadin.ui.components.grid.TreeGridDragSource;
import com.vaadin.ui.components.grid.TreeGridDropTarget;

import ru.gzpn.spc.csl.model.jsontypes.ColumnHeaderGroup;
import ru.gzpn.spc.csl.model.jsontypes.ColumnSettings;
import ru.gzpn.spc.csl.model.jsontypes.CreateDocSettingsJson;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.DraggableTree;
import ru.gzpn.spc.csl.ui.common.UISettingsWindow;

public class CreateDocSettingsWindow extends UISettingsWindow {

	public static final Logger logger = LogManager.getLogger(CreateDocSettingsWindow.class);
	public static final String I18N_TREE_CAPTION = "createDocSettingsWindow.projectTree.caption";
	public static final String I18N_WINDOW_CAPTION = "createDocSettingsWindow.caption";
	
	public static final String I18N_DOCSETTINGS_COLUMN_ID = "createdoc.CreateDocSettingsWindow.columnsGrid.columns.id";
	public static final String I18N_DOCSETTINGS_COLUMN_ENTITY = "createdoc.CreateDocSettingsWindow.columnsGrid.columns.entity";
	public static final String I18N_DOCSETTINGS_COLUMN_ADDTOGROUP = "createdoc.CreateDocSettingsWindow.columnsGrid.columns.addToGroup";
	public static final String I18N_DOCSETTINGS_COLUMN_VISIBLE = "createdoc.CreateDocSettingsWindow.columnsGrid.columns.visible";
	public static final String I18N_DOCSETTINGS_COLUMN_MERGEDHEAD = "createdoc.CreateDocSettingsWindow.columnsGrid.columns.mergedHeadColumns";
	
	private VerticalLayout leftLayout;
	private HorizontalSplitPanel splitPanel;
	private DraggableTree<NodeWrapper> projectTree;
	private List<NodeWrapper> draggedItems;
	private TreeDataProvider<NodeWrapper> treeDataProvider;
	private TreeData<NodeWrapper> treeData;
	private Grid<ColumnSettingsPresenter> columnsGrid;
	
	public CreateDocSettingsWindow(IUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
		this.userSettings = settingsService.getUserSettings(user, new CreateDocSettingsJson());
		this.center();
		this.setCaption(getI18nText(I18N_WINDOW_CAPTION, messageSource));
		this.setModal(true);
		this.setWidth(70, Unit.PERCENTAGE);
		this.setHeight(50, Unit.PERCENTAGE);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout layout = new VerticalLayout();
		splitPanel = new HorizontalSplitPanel();
		splitPanel.setFirstComponent(createLeftLayout());
		splitPanel.setSecondComponent(createRightLayout());
		splitPanel.setSplitPosition(30);
		layout.addComponent(splitPanel);
		return layout;
	}

	public Component createLeftLayout() {
		leftLayout = new VerticalLayout();
		leftLayout.setMargin(false);
		leftLayout.setSpacing(false);
		leftLayout.setSizeFull();
		leftLayout.addComponent(createProjectTree());
		return leftLayout;
	}
	
	public Component createProjectTree() {
		projectTree = new DraggableTree<>();
		Panel panel = new Panel(getI18nText(I18N_TREE_CAPTION, messageSource));
		
		projectTree.setItemCaptionGenerator(NodeWrapper::getNodeSettingsCaption);
		projectTree.setItemIconGenerator(new ProjectItemIconGenerator());
		projectTree.setSizeFull();
		projectTree.setHeight(300, Unit.PIXELS);
		refreshUiTreeData();
		
		TreeGridDragSource<NodeWrapper> dragSource = new TreeGridDragSource<>(projectTree.getCompositionRoot());
		TreeGridDropTarget<NodeWrapper> dropTarget = new TreeGridDropTarget<>(projectTree.getCompositionRoot(), DropMode.ON_TOP);
		
		dragSource.addGridDragStartListener(event -> {
			draggedItems = event.getDraggedItems();
		});
		
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
			int fromIndex = listNodes.indexOf(draggedNode);
			listNodes.remove(draggedNode);
			
			// when we drag a node from top to bottom
			if (pasteIndex - fromIndex > 0) {
				pasteIndex = pasteIndex + 1;
			}
			
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
	
	public Component createRightLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		layout.setSpacing(false);
		columnsGrid = new Grid<>();
		GridDragSource<ColumnSettingsPresenter> dragSource = new GridDragSource<>(columnsGrid);
		GridDropTarget<NodeWrapper> dropTarget = new GridDropTarget<>(projectTree.getCompositionRoot(), DropMode.ON_TOP);
		
		dragSource.addGridDragStartListener(dragEvent -> {
			List<ColumnSettingsPresenter> items = dragEvent.getDraggedItems();
			this.draggedItems = items.stream().map(item -> 
				new NodeWrapper(item.getEntityName(), item.getEntityFieldName().substring(item.getEntityName().length() + 1)))
					.collect(Collectors.toList());
		});
		
		columnsGrid.setSizeFull();
		columnsGrid.setHeightByRows(8);
		refreshColumnsGrid();
		layout.addComponent(columnsGrid);
		
		return layout;
	}

	public void refreshColumnsGrid() {
		columnsGrid.removeAllColumns();
		CreateDocSettingsJson settingsJson = (CreateDocSettingsJson)settingsService
				.getUserSettings(user, new CreateDocSettingsJson());
		List<ColumnSettings> columns = settingsJson.getLeftResultColumns();

		addGridColumn("id", columnsGrid.addColumn(ColumnSettingsPresenter::getEntityFieldName), I18N_DOCSETTINGS_COLUMN_ID);
		addGridColumn("entity", columnsGrid.addColumn(ColumnSettingsPresenter::getEntityName), I18N_DOCSETTINGS_COLUMN_ENTITY);
		//addGridColumn("addToGroup", columnsGrid.addComponentColumn(ColumnSettingsPresenter::getAddToGroup), I18N_DOCSETTINGS_COLUMN_ADDTOGROUP);
		addGridColumn("visibility", columnsGrid.addComponentColumn(ColumnSettingsPresenter::getVisibilityCheckBox), I18N_DOCSETTINGS_COLUMN_VISIBLE);
		addGridColumn("headers", columnsGrid.addComponentColumn(ColumnSettingsPresenter::getMergedHeadComboBox), I18N_DOCSETTINGS_COLUMN_MERGEDHEAD);
		
		columnsGrid.setItems(columns.stream().map(item -> {
			ColumnSettingsPresenter resultItem = new ColumnSettingsPresenter(item);
			resultItem.setAddToGroup(createGridColumnAddToGroupButton(item));
			resultItem.setVisibilityCheckBox(cretateGridColumnVisibleCheckBox(item));
			resultItem.setMergedHeadComboBox(cretateGridColumnMergedHeadComboBox(item));
			return resultItem;
		}).collect(Collectors.toList()));
	}
	
	public <T> void addGridColumn(String id, Column<ColumnSettingsPresenter, T> column, String i18nCaption) {
		column.setSortProperty(id);
		column.setSortable(true);
		column.setCaption(getI18nText(i18nCaption, messageSource));
		
		column.setId(id);
	}
	
	public Button createGridColumnAddToGroupButton(ColumnSettings settings) {
		Button addToGroup = new Button(VaadinIcons.ARROW_LONG_LEFT);
		addToGroup.addClickListener(clickEvent -> clickEvent.getSource());
		return addToGroup;
	}
	
	public CheckBox cretateGridColumnVisibleCheckBox(ColumnSettings settings) {
		CheckBox checkBox = new CheckBox();
		checkBox.setValue(settings.isShown());
		checkBox.addValueChangeListener(changeEvent -> changeEvent.getSource());
		return checkBox;
	}
	
	public ComboBox<String> cretateGridColumnMergedHeadComboBox(ColumnSettings settings) {
		ComboBox<String> mergedHeadComboBox = new ComboBox<>();
		ColumnHeaderGroup mergedHeadGroup = null;
		CreateDocSettingsJson settingsJson = (CreateDocSettingsJson)settingsService
				.getUserSettings(user, new CreateDocSettingsJson());
		List<ColumnHeaderGroup> headers = settingsJson.getLeftColumnHeaders();
		ListIterator<ColumnHeaderGroup> it = headers.listIterator();
		
		while (it.hasNext()) {
			ColumnHeaderGroup headGroup = it.next();
			if (headGroup.hasChildrenGroups()) {
				it = headGroup.getChildren().listIterator();
			} else if (headGroup.hasColumns()
					&& headGroup.getColumns().stream().anyMatch(item -> item.equals(settings))) {
					mergedHeadComboBox.setValue(headGroup.getCaption());
					mergedHeadGroup = headGroup;
			}
		}
		
		return mergedHeadComboBox;
	}
	
	@Override
	public void refreshUiElements() {
		refreshUiTreeData();
	}

	public void refreshUiTreeData() {
		CreateDocSettingsJson settingsJson = (CreateDocSettingsJson)settingsService
									.getUserSettings(user, new CreateDocSettingsJson());
		NodeWrapper rootNode = settingsJson.getLeftTreeGroup();
		treeData = new TreeData<>();
		treeData.addItem(null, rootNode);
		
		while (rootNode.hasChild()) {
			treeData.addItem(rootNode, rootNode.getChild());
			rootNode = rootNode.getChild();
		}
		
		onRefreshUiTreeData(treeData);
	}
	
	public void onRefreshUiTreeData(TreeData<NodeWrapper> treeData) {
		treeDataProvider = new TreeDataProvider<>(treeData);
		projectTree.setDataProvider(treeDataProvider);
		projectTree.expandRecursively(treeData.getRootItems(), 10);
	}

	@Override
	public void refreshSettings() {
		NodeWrapper next = treeData.getRootItems().get(0);
		NodeWrapper result = next;
		result.setParent(null);
		
		while (!treeData.getChildren(next).isEmpty()) {
			NodeWrapper child = treeData.getChildren(next).get(0);
			child.setParent(next);
			next.setChild(child);
			next = child;
		}
		
		((CreateDocSettingsJson)this.userSettings).setLeftTreeGroup(result);
	}
	
	@SuppressWarnings("serial")
	public static class ColumnSettingsPresenter extends ColumnSettings {
		Button addToGroup;
		CheckBox visibilityCheckBox;
		ComboBox<String> mergedHeadComboBox;
		
		public ColumnSettingsPresenter(ColumnSettings columnSettings) {
			this.setWidth(columnSettings.getWidth());
			this.setEntityFieldName(columnSettings.getEntityFieldName());
			this.setEntityName(columnSettings.getEntityName());
			this.setOrderIndex(columnSettings.getOrderIndex());
			this.setShown(columnSettings.isShown());
		}
		
		
		public Button getAddToGroup() {
			return addToGroup;
		}
		public void setAddToGroup(Button addToGroup) {
			this.addToGroup = addToGroup;
		}
		public CheckBox getVisibilityCheckBox() {
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
	}
}
