package com.sts.core.dto;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Menu implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private List<MenuItem> items;

	public Menu() {
		super();
	}

	public Menu(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public Menu(String id, String title, List<MenuItem> items) {
		super();
		this.id = id;
		this.title = title;
		this.items = items;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}

}
