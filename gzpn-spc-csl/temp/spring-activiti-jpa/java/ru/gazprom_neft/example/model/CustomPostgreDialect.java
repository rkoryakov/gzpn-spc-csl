package ru.gazprom_neft.example.model;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL95Dialect;

public class CustomPostgreDialect extends PostgreSQL95Dialect {

	public CustomPostgreDialect() {
		this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
	}
}
