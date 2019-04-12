package ru.gzpn.spc.csl.ui.processmanager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.context.MessageSource;

import com.vaadin.event.Action;
import com.vaadin.ui.VerticalLayout;

import ru.gzpn.spc.csl.services.bl.interfaces.IProcessManagerService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUIService;
import ru.gzpn.spc.csl.services.bl.interfaces.IUserSettingsService;
import ru.gzpn.spc.csl.ui.common.I18n;

@SuppressWarnings("serial")
public abstract class AbstractBpmnViewer extends VerticalLayout implements I18n {
	protected IProcessManagerService service;
	protected MessageSource messageSource;
	protected IUserSettingsService userSettingsService;
	protected String user;
	protected VerticalLayout bodyLayout;
	protected ProcessInstance processInstance;
	
	public static final Action ELEMENT_CLICK_ACTION = new Action("elementClickAction");
	
	protected Map<Action, Set<Listener>> listeners;
	
	public AbstractBpmnViewer(IUIService service) {
		this.service = (IProcessManagerService)service;
		this.messageSource = service.getMessageSource();
		this.userSettingsService = service.getUserSettingsService();
		this.user = userSettingsService.getCurrentUser();		
		
		setSpacing(false);
		setMargin(false);

		initEventActions();
		createHeadFutures();
		createBody();
		createFooter();
		refreshUiElements();
	}

	
	protected void refreshUiElements() {
		
	}

	protected void createFooter() {
		
	}

	public void createBody() {
		this.bodyLayout = createBodyLayout();
		this.addComponent(bodyLayout);
	}

	public abstract VerticalLayout createBodyLayout();

	private void createHeadFutures() {
		// TODO Auto-generated method stub
		
	}
	
	protected void initEventActions() {
		listeners = new HashMap<>();
		listeners.put(ELEMENT_CLICK_ACTION, new HashSet<>());
	}
	
	protected void handleAction(Action action) {
		for (Listener listener : listeners.get(action)) {
			listener.componentEvent(new Event(this));
		}
	}
	
	public void addOnElementClickListener(Listener listener) {
		listeners.get(ELEMENT_CLICK_ACTION).add(listener);
	}
	
	public Set<Listener> getElementClickListeners() {
		return listeners.get(ELEMENT_CLICK_ACTION);
	}
}
