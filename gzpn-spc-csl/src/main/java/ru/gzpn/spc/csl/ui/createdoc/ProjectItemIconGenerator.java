package ru.gzpn.spc.csl.ui.createdoc;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.IconGenerator;

import ru.gzpn.spc.csl.model.enums.Entities;
import ru.gzpn.spc.csl.model.utils.NodeWrapper;

public class ProjectItemIconGenerator implements IconGenerator<NodeWrapper> {
		private static final long serialVersionUID = 1L;

		@Override
		public Resource apply(NodeWrapper item) {
			Resource result = null;
			
			Entities entity = Entities.valueOf(item.getEntityName().toUpperCase());
			switch (entity) {

			case CPROJECT:
				result = VaadinIcons.BRIEFCASE;
				break;
			case DOCUMENT:
				result = VaadinIcons.NEWSPAPER;
				break;
			case HPROJECT:
				result = VaadinIcons.OFFICE;
				break;
			case PLANOBJECT:
				result = VaadinIcons.PIN_POST;
				break;
			case STAGE:
				result = VaadinIcons.ROAD_BRANCH;
				break;
			case WORKSET:
				result = VaadinIcons.RECORDS;
				break;
			default:
				break;
			}
			
			return result;
		}
	}