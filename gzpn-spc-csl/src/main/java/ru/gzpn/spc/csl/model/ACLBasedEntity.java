package ru.gzpn.spc.csl.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Type;

import ru.gzpn.spc.csl.model.interfaces.IAccessControlList;

/**
 * Access control list of particular entity object
 * 
 * @author koryakov.rv
 */
@MappedSuperclass
public abstract class ACLBasedEntity extends BaseEntity {

	@Column
	@Type(type = "ACLJsonType")
	IAccessControlList acl;
}
