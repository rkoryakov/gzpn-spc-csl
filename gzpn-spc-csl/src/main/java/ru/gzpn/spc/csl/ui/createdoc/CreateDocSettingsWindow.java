package ru.gzpn.spc.csl.ui.createdoc;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.TreeGridDragSource;
import com.vaadin.ui.components.grid.TreeGridDropTarget;

import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettigsService;
import ru.gzpn.spc.csl.ui.common.DraggableTree;
import ru.gzpn.spc.csl.ui.settings.UISettingsWindow;

public class CreateDocSettingsWindow extends UISettingsWindow {

	private static final long serialVersionUID = 1L;
	public static final String I18N_TREE_CAPTION = "createDocSettingsWindow.projectTree.caption";
	public static final String I18N_WINDOW_CAPTION = "createDocSettingsWindow.caption";
	
	private VerticalLayout leftLayout;
	private HorizontalSplitPanel splitPanel;
	private DraggableTree<NodeWrapper> projectTree;
	private List<NodeWrapper> draggedItems;
	private TreeDataProvider<NodeWrapper> treeDataProvider;
	private TreeData<NodeWrapper> treeData;
	
	public CreateDocSettingsWindow(IUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
		this.center();
		this.setCaption(getI18nText(I18N_WINDOW_CAPTION, messageSource));
		this.setModal(true);
		this.setWidth(70, Unit.PERCENTAGE);
		this.setHeight(60, Unit.PERCENTAGE);
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
		projectTree.setHeight(90, Unit.PERCENTAGE);
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshUiElements() {
		refreshUiTreeData();
	}

	public void refreshUiTreeData() {
		NodeWrapper rootNode = settingsService.getUserSettings().getLeftTreeGroup();
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
		
		this.userSettings.setLeftTreeGroup(result);
	}
}
