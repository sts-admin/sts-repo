package com.awacp.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: PageSetting
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PageSetting.getPageSizeByViewName", query = "SELECT a FROM PageSetting a WHERE a.archived = 'false' AND a.viewName = :viewName") })
public class PageSetting extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String viewName;
	private Integer pageSize;

	public PageSetting() {
		super();
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
