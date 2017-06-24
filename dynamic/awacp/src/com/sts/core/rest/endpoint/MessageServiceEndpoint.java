package com.sts.core.rest.endpoint;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.sts.core.dto.UserDTO;
import com.sts.core.entity.ChatMessage;
import com.sts.core.service.ChatMessageService;
import com.sts.core.service.UserService;
import com.sts.core.web.filter.CrossOriginFilter;

public class MessageServiceEndpoint extends CrossOriginFilter {
	@Autowired
	private ChatMessageService chatMessageService;

	@Autowired
	private UserService userService;

	@GET
	@Path("/listUsersByOnlineStatus/{onlineStatus}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserDTO> listUsersByOnlineStatus(@PathParam("onlineStatus") String onlineStatus,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.userService.listUsersByOnlineStatus(onlineStatus);
	}

	@POST
	@Path("/saveChatMessage")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ChatMessage saveChatMessage(ChatMessage chatMessage) throws IOException {
		return chatMessageService.saveMessage(chatMessage);
	}

	@GET
	@Path("/listMyAllConversations/{myUserId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ChatMessage> listMyAllConversations(@PathParam("myUserId") Long myUserId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.chatMessageService.listMyAllConversations(myUserId);
	}

	@GET
	@Path("/listMyConversationsWith/{myUserId}/{otherUserId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ChatMessage> listMyConversationsWith(@PathParam("myUserId") Long myUserId,
			@PathParam("otherUserId") Long otherUserId, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.chatMessageService.listMyConversationsWith(myUserId, otherUserId);
	}

	@GET
	@Path("/getMyUnreadMessagesCount/{myUserId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getMyUnreadMessagesCount(@PathParam("myUserId") Long myUserId,
			@Context HttpServletResponse servletResponse) throws IOException {

		int myUnreadMessageCount = this.chatMessageService.getMyUnreadMessagesCount(myUserId);

		return "{\"myUnreadMessageCount\":\"" + myUnreadMessageCount + "\"}";
	}

	@GET
	@Path("/listMyUnreadMessages/{myUserId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ChatMessage> listMyUnreadMessages(@PathParam("myUserId") Long myUserId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.chatMessageService.listMyUnreadMessages(myUserId);
	}

	public void setChatMessageService(ChatMessageService chatMessageService) {
		this.chatMessageService = chatMessageService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
