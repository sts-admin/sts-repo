package com.awacp.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: SiteSetting
 *
 */
@Entity
@XmlRootElement
public class SiteSetting extends BaseEntity{

	
	private static final long serialVersionUID = 1L;
	
	

	public SiteSetting() {
		super();
	}
   
}
