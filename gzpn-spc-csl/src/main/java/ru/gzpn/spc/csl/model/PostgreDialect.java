package ru.gzpn.spc.csl.model;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL95Dialect;

public class PostgreDialect extends PostgreSQL95Dialect {
	public PostgreDialect() {
		registerColumnType(Types.JAVA_OBJECT, "jsonb");
	}
}
