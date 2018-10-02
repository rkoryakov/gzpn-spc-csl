package ru.gzpn.spc.csl.ui.views;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TwinColSelect;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = "adminView")
public class AdminView extends VerticalLayout implements View{
	
	@Autowired
	IdentityService identityService;
	
	public AdminView() {
		setMargin(true);
		setSpacing(true);
		TabSheet tabSet = new TabSheet();
		tabSet.addTab(adminViewCreateUserOrRole(), "Администрирование учетных записей");
		tabSet.addTab(adminViewCreateUserOrRole(), "Анализ бизнес процесса");
		tabSet.addTab(adminViewCreateUserOrRole(), "Данные справочников");
		addComponent(tabSet);
	}
	
	public VerticalLayout adminViewCreateUserOrRole() {
		setMargin(true);
		setSpacing(true);
		HorizontalLayout headerHorizont = new HorizontalLayout();
		HorizontalLayout bodyHorizontTop = new HorizontalLayout();
		HorizontalLayout bodyHorizontBottom = new HorizontalLayout();
		HorizontalLayout bottomHorizont = new HorizontalLayout();
		VerticalLayout resultPage = new VerticalLayout();
		headerHorizont.addComponents(new ComboBox<>(), new NativeSelect<>(), new Button("Create"));
		bodyHorizontTop.addComponents(new TextField(), new Button("Find"), new TextField(), new Button("Find"));
		bodyHorizontBottom.addComponent(new TwinColSelect<>());
		bottomHorizont.addComponents(new Button("Save"), new Button("Cancel"));
		resultPage.addComponents(headerHorizont,bodyHorizontTop,bodyHorizontBottom,bottomHorizont);
		return resultPage;
	}
}
