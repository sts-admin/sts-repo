package com.sts.core.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum Role {

	ANONYMOUS(0), CUSTOMER(1), ADMIN(2), SUPERADMIN(3), BUSINESS(4);

	private int type;

	private String name;

	private Role(int type) {
		this.type = type;

	}

	private Role(String name) {
		this.name = name;

	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public static Role valueOf(int type) {
		for (Role usr : Role.values()) {
			if (usr.getType() == type) {
				return usr;
			}
		}
		return null;
	}
}
