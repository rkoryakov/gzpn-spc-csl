package ru.gzpn.spc.csl.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import ru.gzpn.spc.csl.model.interfaces.IPlanObject;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "PlanObject.groupByNameCount", query = "SELECT COUNT(p) FROM PlanObject p GROUP BY p.name"),
	@NamedQuery(name = "PlanObject.groupByObjIdCount", query = "SELECT COUNT(p) FROM PlanObject p GROUP BY p.objectId"),
	
	@NamedQuery(name = "PlanObject.groupByName", query = "SELECT p.name FROM PlanObject p GROUP BY p.name"),
	@NamedQuery(name = "PlanObject.groupByObjId", query = "SELECT p.objectId FROM PlanObject p GROUP BY p.objectId")
})
@Table(schema = "spc_csl_schema", name = "plan_object")
public class PlanObject extends BaseEntity implements IPlanObject {

	private String objectId;
	private String name;
	
	
}
