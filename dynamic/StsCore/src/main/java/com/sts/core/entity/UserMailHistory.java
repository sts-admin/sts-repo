package com.sts.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: UserMailHistory
 *
 */
@Entity
@NamedQueries({ 
	@NamedQuery(name = "UserMailHistory.findByUserEmailAndEvent", query = "SELECT umh FROM UserMailHistory umh WHERE lower(umh.email) =:email AND umh.event =:eventName")
})
@XmlRootElement
public class UserMailHistory extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String email;
	private String content;
	private String event;

	public UserMailHistory() {
		super();
	}
	

	public UserMailHistory(String email, String content, String event) {
		super();
		this.email = email;
		this.content = content;
		this.event = event;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Lob
	@Column(name = "content", length = 2048)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
