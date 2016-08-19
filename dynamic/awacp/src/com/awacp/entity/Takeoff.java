package com.awacp.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Takeoff
 *
 */
@Entity
@XmlRootElement
public class Takeoff extends BaseEntity{

	
	private static final long serialVersionUID = 1L;
	
	

	public Takeoff() {
		super();
	}
   
}
