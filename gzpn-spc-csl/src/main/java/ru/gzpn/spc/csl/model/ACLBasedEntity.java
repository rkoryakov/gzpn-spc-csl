package ru.gzpn.spc.csl.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import ru.gzpn.spc.csl.model.jsontypes.ACLJson;

/**
 * Access control list of particular entity object
 * 
 * @author koryakov.rv
 */
@MappedSuperclass
public abstract class ACLBasedEntity extends BaseEntity {

	@Column
	@Type(type = "ACLJsonType")
	ACLJson acl;

	public ACLJson getAcl() {
		return acl;
	}

	public void setAcl(ACLJson acl) {
		this.acl = acl;
	}
}
