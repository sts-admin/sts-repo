package com.sts.core.service;

import java.util.List;

import com.sts.core.dto.UserDTO;
import com.sts.core.entity.ChatMessage;

public interface ChatMessageService {

	public ChatMessage saveMessage(ChatMessage chatMessage);

	public List<ChatMessage> listMyAllConversations(Long myUserId);

	public List<ChatMessage> listMyConversationsWith(Long myUserId, Long otherUserId);

	public List<ChatMessage> listMyUnreadMessages(Long myUserId);

	public List<ChatMessage> getAllMyUnreadMessagesCount(Long myUserId);

	public int getMyUnreadMessagesCount(Long myUserId);

	public void updateMessagesStatus(String[] messageIds);

	public List<UserDTO> listUsersByOnlineStatus(String onlineStatus);

	public int markMessagesAsRead(Long loggedInUserId, Long otherUserId);
}
