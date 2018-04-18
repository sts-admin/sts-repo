package com.sts.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sts.core.dto.UserDTO;
import com.sts.core.entity.ChatMessage;
import com.sts.core.service.ChatMessageService;
import com.sts.core.service.UserService;

public class ChatMessageServiceImpl implements ChatMessageService {

	private EntityManager entityManager;

	@Autowired
	UserService userService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@Transactional
	public ChatMessage saveMessage(ChatMessage chatMessage) {
		if (chatMessage.getId() != null) {
			getEntityManager().merge(chatMessage);
		} else {
			getEntityManager().persist(chatMessage);
		}
		getEntityManager().flush();
		return chatMessage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChatMessage> listMyAllConversations(Long myUserId) {
		return getEntityManager().createNamedQuery("ChatMessage.listMyAllConversations")
				.setParameter("myUserId", myUserId).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChatMessage> listMyConversationsWith(Long myUserId, Long otherUserId) {
		return getEntityManager().createNamedQuery("ChatMessage.listMyConversationsWith")
				.setParameter("myUserId", myUserId).setParameter("otherUserId", otherUserId).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChatMessage> listMyUnreadMessages(Long myUserId) {
		return getEntityManager().createNamedQuery("ChatMessage.listMyUnreadMessages")
				.setParameter("targetUserId", myUserId).getResultList();
	}

	@Override
	@Transactional
	public void updateMessagesStatus(String[] messageIds) {
		if (messageIds != null && messageIds.length > 0) {
			boolean merged = false;
			for (String id : messageIds) {
				ChatMessage message = getEntityManager().find(ChatMessage.class, Long.valueOf(id));
				if (!message.isArchived()) {
					message.setSeen(true);
					getEntityManager().merge(message);
					merged = true;
				}

			}
			if (merged) {
				getEntityManager().flush();
			}
		}
	}

	@Override
	public int getMyUnreadMessagesCount(Long myUserId) {

		Object object = getEntityManager().createNamedQuery("ChatMessage.getMyUnreadMessagesCount")
				.setParameter("myUserId", myUserId).getSingleResult();
		if (object != null) {
			return (((Long) object).intValue());
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserDTO> listUsersByOnlineStatus(String onlineStatus) {
		String query = "User.listOnlineUsers";
		if (onlineStatus != null && onlineStatus.trim().equalsIgnoreCase("offline")) {
			query = "User.listOfflineUsers";
		}
		List<UserDTO> users = getEntityManager().createNamedQuery(query).getResultList();
		if (users != null && !users.isEmpty()) {
			for (UserDTO u : users) {
				u.setUnreadMessageCount(getMyUnreadMessagesCount(u.getId()));
			}
		}

		return users;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChatMessage> getAllMyUnreadMessagesCount(Long myUserId) {
		String query = "SELECT cm.sourceUserId, COUNT(cm.id) FROM ChatMessage cm WHERE cm.targetUserId =:myUserId AND cm.seen = 'false' GROUP BY cm.sourceUserId";
		List<Object[]> results = getEntityManager().createQuery(query).setParameter("myUserId", myUserId)
				.getResultList();
		List<ChatMessage> messages = new ArrayList<ChatMessage>();
		if (results != null & !results.isEmpty()) {
			ChatMessage cm = null;
			for (Object[] result : results) {
				cm = new ChatMessage();
				cm.setSourceUserId((Long) result[0]);
				cm.setMsgCount(((Long) result[1]).intValue());
				messages.add(cm);
			}
		}
		return messages;
	}

}
