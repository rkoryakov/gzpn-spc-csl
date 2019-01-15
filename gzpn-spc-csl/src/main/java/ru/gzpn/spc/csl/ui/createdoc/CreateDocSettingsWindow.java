package ru.gzpn.spc.csl.ui.createdoc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.model.utils.NodeWrapper;
import ru.gzpn.spc.csl.services.bl.interfaces.IDataUserSettigsService;
import ru.gzpn.spc.csl.ui.common.DraggableTree;
import ru.gzpn.spc.csl.ui.settings.UISettingsWindow;

public class CreateDocSettingsWindow extends UISettingsWindow {

	private static final long serialVersionUID = 1L;
	public static final String I18N_TREE_CAPTION = "createDocSettingsWindow.projectTree.caption";
	public static final String I18N_WINDOW_CAPTION = "createDocSettingsWindow.caption";
	
	private VerticalLayout leftLayout;
	private HorizontalSplitPanel splitPanel;
	private Tree<NodeWrapper> projectTree;
	private List<NodeWrapper> draggedItems;
	private TreeDataProvider<NodeWrapper> treeDataProvider;
	private TreeData<NodeWrapper> treeData;
	
	public CreateDocSettingsWindow(IDataUserSettigsService settingsService, MessageSource messageSource) {
		super(settingsService, messageSource);
		this.center();
		this.setCaption(getI18nText(I18N_WINDOW_CAPTION));
		this.setModal(true);
		this.setWidth(70, Unit.PERCENTAGE);
		this.setHeight(80, Unit.PERCENTAGE);
	}

	@Override
	public VerticalLayout createBodyLayout() {
		VerticalLayout layout = new VerticalLayout();
		splitPanel = new HorizontalSplitPanel();
		splitPanel.setFirstComponent(createLeftLayout());
		splitPanel.setSecondComponent(createRightLayout());
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
		Panel panel = new Panel(getI18nText(I18N_TREE_CAPTION));
		
		projectTree.setItemCaptionGenerator(NodeWrapper::getNodeSettingsCaption);
		projectTree.setItemIconGenerator(new ProjectItemIconGenerator());
		projectTree.setSizeFull();
		projectTree.setHeight(300, Unit.PIXELS);
		refreshUiTreeData();
		
//		TreeGridDragSource<NodeWrapper> dragSource = new TreeGridDragSource<>(projectTree.getCompositionRoot());
//		TreeGridDropTarget<NodeWrapper> dropTarget = new TreeGridDropTarget<>(projectTree.getCompositionRoot(), DropMode.ON_TOP);
//		
//		dragSource.addGridDragStartListener(event -> {
//			draggedItems = event.getDraggedItems();
//		});
//		
//		dropTarget.addTreeGridDropListener(event -> {
//			event.getDropTargetRow().ifPresent(this::putDraggedItemsAt);
//		});
		
		panel.setContent(projectTree);
		panel.setSizeFull();
		
		projectTree.expand(treeDataProvider.fetchChildren(new HierarchicalQuery<NodeWrapper, SerializablePredicate<NodeWrapper>>(e -> true, null)).collect(Collectors.toList()));
		return panel;
	}
	
	private void putDraggedItemsAt(NodeWrapper to) {
		
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
		NodeWrapper rootNode = settingsService.getUserSettings().getLeftHierarchySettings();
		NodeWrapper exp = rootNode;
		treeData = new TreeData<>();
		treeData.addItem(null, rootNode);
		
		while (rootNode.hasChild()) {
			treeData.addItem(rootNode, rootNode.getChild());
			rootNode = rootNode.getChild();
		}
		
		treeDataProvider = new TreeDataProvider<>(treeData);
		projectTree.setDataProvider(treeDataProvider);
		projectTree.expand(exp);
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
		
		this.userSettings.setLeftHierarchySettings(result);
	}

	@Override
	public String getI18nText(String key) {
		return messageSource.getMessage(key, null, VaadinSession.getCurrent().getLocale());
	}
}
