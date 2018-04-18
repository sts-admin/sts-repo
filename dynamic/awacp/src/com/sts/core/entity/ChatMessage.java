package com.sts.core.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: Address
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ChatMessage.listMyAllConversations", query = "SELECT cm FROM ChatMessage cm WHERE cm.archived = 'false' AND cm.targetUserId =:myUserId ORDER BY cm.dateCreated DESC"),
		@NamedQuery(name = "ChatMessage.listMyConversationsWith", query = "SELECT cm FROM ChatMessage cm WHERE cm.archived = 'false' AND ((cm.sourceUserId =:myUserId AND cm.targetUserId =:otherUserId) OR (cm.sourceUserId =:otherUserId AND cm.targetUserId =:myUserId)) ORDER BY cm.dateCreated ASC"),
		@NamedQuery(name = "ChatMessage.listMyUnreadMessages", query = "SELECT cm FROM ChatMessage cm WHERE cm.archived = 'false' AND cm.seen = 'false' AND cm.targetUserId =:myUserId ORDER BY cm.dateCreated DESC"),
		@NamedQuery(name = "ChatMessage.getMyUnreadMessagesCount", query = "SELECT COUNT(cm.id) FROM ChatMessage cm WHERE cm.archived = 'false' AND cm.seen = 'false' AND cm.targetUserId =:myUserId ORDER BY cm.dateCreated DESC"),
		})

public class ChatMessage extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long sourceUserId; // Message sent by
	private Long targetUserId; // Message sent to
	private String message;

	private String image;

	private String sourceUserName;

	private String targetUserName;

	private boolean seen;

	private int msgCount;

	public ChatMessage() {
		super();
	}

	public Long getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(Long sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public Long getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(Long targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSourceUserName() {
		return sourceUserName;
	}

	public void setSourceUserName(String sourceUserName) {
		this.sourceUserName = sourceUserName;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	@Transient
	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

}
