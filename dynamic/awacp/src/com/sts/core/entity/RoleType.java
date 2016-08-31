package com.sts.core.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum RoleType {

	GUEST(1), EMPLOYEE(2), POWERUSER(3), ADMIN(4), SUPERADMIN(5);

	private int type;

	private String name;

	private RoleType(int type) {
		this.type = type;

	}

	private RoleType(String name) {
		this.name = name;

	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static RoleType valueOf(int type) {
		for (RoleType usr : RoleType.values()) {
			if (usr.getType() == type) {
				return usr;
			}
		}
		return null;
	}
}
