package com.sts.core.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum UserType {

	ANONYMOUS(0), CUSTOMER(1), ADMIN(2), SUPERADMIN(3), BUSINESS(4);

	private int type;

	private String name;

	private UserType(int type) {
		this.type = type;

	}

	private UserType(String name) {
		this.name = name;

	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static UserType valueOf(int type) {
		for (UserType usr : UserType.values()) {
			if (usr.getType() == type) {
				return usr;
			}
		}
		return null;
	}
}
