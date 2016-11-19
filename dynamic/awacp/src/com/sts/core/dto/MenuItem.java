package com.sts.core.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MenuItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String link;
	private Double displayOrder;
	private Integer hierarchy;

	/**
	 * 
	 */
	public MenuItem() {
		super();
	}

	public MenuItem(String name, String link) {
		super();
		this.name = name;
		this.link = link;
	}

	public MenuItem(String name, String link, Double displayOrder, Integer hierarchy) {
		super();
		this.name = name;
		this.link = link;
		this.displayOrder = displayOrder;
		this.hierarchy = hierarchy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Double getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Double displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}

}
