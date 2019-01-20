package ru.gzpn.spc.csl.model.interfaces;

import ru.gzpn.spc.csl.model.jsontypes.ACLJson;

public interface IACLBasedEntity extends IBaseEntity {

	ACLJson getAcl();
	void setAcl(ACLJson acl);
}