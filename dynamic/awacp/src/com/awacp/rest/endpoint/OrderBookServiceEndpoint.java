package com.awacp.rest.endpoint;

import java.io.IOException;

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

import com.awacp.entity.JobOrder;
import com.awacp.entity.OrderBook;
import com.awacp.service.OrderBookService;
import com.sts.core.dto.StsResponse;
import com.sts.core.web.filter.CrossOriginFilter;

public class OrderBookServiceEndpoint extends CrossOriginFilter {

	@Autowired
	private OrderBookService orderBookService;
	
	@POST
	@Path("/generateOrderBookReport")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public StsResponse<OrderBook> generateOrderBookReport(OrderBook orderBook, @Context HttpServletResponse servletResponse)
			throws Exception {
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
