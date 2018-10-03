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

import ru.gzpn.spc.csl.ui.admin.UsersAndRoles;

@SpringView(name = "adminView")
public class AdminView extends VerticalLayout implements View{
	
	@Autowired
	IdentityService identityService;
	
	public AdminView() {
		setMargin(true);
		setSpacing(true);
		TabSheet tabSet = new TabSheet();
		UsersAndRoles admin = new UsersAndRoles();
		UsersAndRoles business = new UsersAndRoles();
		UsersAndRoles directory = new UsersAndRoles();
		tabSet.addTab(admin, "Администрирование учетных записей");
		tabSet.addTab(business, "Анализ бизнес процесса");
		tabSet.addTab(directory, "Данные справочников");
		addComponent(tabSet);
	}
}
