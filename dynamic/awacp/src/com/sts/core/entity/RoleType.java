package com.sts.core.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum RoleType {

	GUEST("GUEST"), EMPLOYEE("EMPLOYEE"), POWERUSER("POWERUSER"), ADMIN("ADMIN"), SUPERADMIN("SUPERADMIN");

	private String name;

	private RoleType(String name) {
		this.name = name;

	}

	public String getName() {
		return name;
	}
}
