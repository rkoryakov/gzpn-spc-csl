package ru.gzpn.spc.csl.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import ru.gzpn.spc.csl.model.interfaces.IACLBasedEntity;
import ru.gzpn.spc.csl.model.jsontypes.ACLJson;

/**
 * Access control list of particular entity object
 */
@MappedSuperclass
public abstract class ACLBasedEntity extends BaseEntity implements IACLBasedEntity {

	@Column
	@Type(type = "ACLJsonType")
	ACLJson acl;

	@Override
	public ACLJson getAcl() {
		return acl;
	}

	@Override
	public void setAcl(ACLJson acl) {
		this.acl = acl;
	}
}
