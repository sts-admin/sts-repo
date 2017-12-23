package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: SystemLog
 *
 */
@Entity
@XmlRootElement
public class SystemLog extends BaseEntity {
	private String section;
	private String description;
	private String uc;

	private static final long serialVersionUID = 1L;

	public SystemLog() {
		super();
	}

	public String getUc() {
		return uc;
	}

	public void setUc(String uc) {
		this.uc = uc;
	}

	@Lob
	@Column(length = 1024)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
}
