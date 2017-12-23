package com.awacp.rest.endpoint;

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

import com.awacp.entity.ClaimFollowup;
import com.awacp.entity.Orbf;
import com.awacp.entity.OrderBook;
import com.awacp.service.MailService;
import com.awacp.service.OrderBookService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class OrderBookServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private OrderBookService orderBookService;

	@Autowired
	private MailService mailService;

	@POST
	@Path("/saveClaimFollowup")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ClaimFollowup saveClaimFollowup(ClaimFollowup saveClaimFollowup,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.saveClaimFollowup(saveClaimFollowup);

	}

	@GET
	@Path("/getClaimFollowup/{claimId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ClaimFollowup getClaimFollowup(@PathParam("claimId") Long claimId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.getClaimFollowup(claimId);
	}

	@GET
	@Path("/getFollowupsByClaim/{claimId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ClaimFollowup> getFollowupsByClaim(@PathParam("claimId") Long claimId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.getFollowupsByClaim(claimId);
	}

	@GET
	@Path("/sendPremiumOrderMail/{emailOrId}/{source}/{jobOrderId}")
	@Produces(MediaType.APPLICATION_JSON)
	public String sendPremiumOrderMail(@PathParam("emailOrId") String emailOrId, @PathParam("source") String source,
			@PathParam("jobOrderId") Long jobOrderId, @Context HttpServletResponse servletResponse) throws Exception {
		boolean status = mailService.sendPremiumOrderEmail(emailOrId, source, jobOrderId);
		return "{\"status\":\"" + status + "\"}";
	}

	@GET
	@Path("/fetchPremiumOrder/{orderBookId}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderBook fetchPremiumOrder(@PathParam("orderBookId") Long orderBookId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.fetchPremiumOrder(orderBookId);
	}

	@GET
	@Path("/listInventoryOrders/{invType}/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<OrderBook> listInventoryOrders(@PathParam("invType") String invType,
			@PathParam("pageNumber") int pageNumber, @PathParam("pageSize") int pageSize,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.getOrders(invType, pageNumber, pageSize);
	}

	@POST
	@Path("/generateOrderBookReport")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StsResponse<OrderBook> generateOrderBookReport(OrderBook orderBook,
			@Context HttpServletResponse servletResponse) throws Exception {
		return this.orderBookService.generateOrderBookReport(orderBook);
	}

	@GET
	@Path("/listOrderBooks/{pageNumber}/{pageSize}")
	@Produces(MediaType.APPLICATION_JSON)
	public StsResponse<OrderBook> listOrderBooks(@PathParam("pageNumber") int pageNumber,
			@PathParam("pageSize") int pageSize, @Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.listOrderBooks(pageNumber, pageSize);
	}

	@GET
	@Path("/getOrbf/{id}/{obId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Orbf getOrderBook(@PathParam("id") Long id, @PathParam("obId") Long obId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.getOrbf(id, obId);
	}

	@GET
	@Path("/getOrbfByOrderBook/{orderBookId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Orbf getOrbfByOrderBook(@PathParam("orderBookId") Long orderBookId,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.getOrbf(orderBookId);
	}

	@GET
	@Path("/getOrderBookByNumber/{obNumber}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderBook getOrderBookByNumber(@PathParam("obNumber") String obNumber,
			@Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.getOrderBook(obNumber);
	}

	@GET
	@Path("/getOrderBook/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public OrderBook getOrderBook(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.orderBookService.getOrderBook(id);
	}

	@GET
	@Path("/cancelOrderBook/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String cancelOrderBook(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = this.orderBookService.cancelOrderBook(id);
		return "{\"result\":\"" + result + "\"}";
	}

	@GET
	@Path("/uncancellOrderBook/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String uncancellOrderBook(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = this.orderBookService.uncancellOrderBook(id);
		return "{\"result\":\"" + result + "\"}";
	}

	@POST
	@Path("/saveOrderBook")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public OrderBook saveOrderBook(OrderBook orderBook, @Context HttpServletResponse servletResponse)
			throws IOException {
		return this.orderBookService.saveOrderBook(orderBook);

	}

	@POST
	@Path("/saveOrbf")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Orbf saveOrbf(Orbf orbf, @Context HttpServletResponse servletResponse) throws IOException {
		return this.orderBookService.saveOrbf(orbf);

	}

	@GET
	@Path("/deleteOrderBook/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteOrderBook(@PathParam("id") Long id, @Context HttpServletResponse servletResponse)
			throws IOException {
		String result = orderBookService.delete(id);
		if ("fail".equalsIgnoreCase(result)) {
			servletResponse.sendError(666666, "delete_error");
		}
		return "{\"result\":\"" + result + "\"}";
	}

	public void setOrderBookService(OrderBookService orderBookService) {
		this.orderBookService = orderBookService;
	}

}
