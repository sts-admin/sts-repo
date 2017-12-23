package com.sts.core.constant;

public enum ShipmentSource {
	UPS("ups");

	private String name;

	private ShipmentSource(String name) {
		this.name = name;
	}

	public static String getByName(String name) {
		for (ShipmentSource site : ShipmentSource.values()) {
			if (site.name().equalsIgnoreCase(name)) {
				return site.name;
			}
		}
		return "";
	}

}
