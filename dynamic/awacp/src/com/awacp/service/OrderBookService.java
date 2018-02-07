package com.awacp.service;

import java.util.List;

import com.awacp.entity.ClaimFollowup;
import com.awacp.entity.Orbf;
import com.awacp.entity.OrderBook;
import com.awacp.entity.ShipmentStatus;
import com.sts.core.dto.StsResponse;

public interface OrderBookService {

	public OrderBook saveOrderBook(OrderBook orderBook);

	public OrderBook getOrderBook(Long orderBookId);

	public StsResponse<OrderBook> listOrderBooks(int pageNumber, int pageSize);

	public String delete(Long id);

	public int updateInvItemQty(Long bookId, Long lineItemId, String invName, int orderQty);

	public List<OrderBook> listByJobOrder(Long jobOrderId);

	public Double getPrice(Long lineItemId, String invName);

	public String cancelOrderBook(Long orderBookId);

	public String uncancellOrderBook(Long orderBookId);

	public StsResponse<OrderBook> generateOrderBookReport(OrderBook orderBook);

	public Orbf getOrbf(Long id, Long obId);

	public Orbf getOrbf(Long orderBookId);

	public Orbf saveOrbf(Orbf orbf);

	public StsResponse<OrderBook> getOrders(String invType, int pageNumber, int pageSize);

	public OrderBook fetchPremiumOrder(Long orderBookId);

	public OrderBook getOrderBook(String orderBookNumber);

	public List<ShipmentStatus> getShipmentStatus(Long orderBookId);

	public ShipmentStatus saveShipmentStatus(ShipmentStatus shipmentStatus);

	public ClaimFollowup saveClaimFollowup(ClaimFollowup cf);

	public ClaimFollowup getClaimFollowup(Long id);
	
	public List<ClaimFollowup> getFollowupsByClaim(Long claimId, String source);

	public List<ClaimFollowup> getFollowupsByOrderBook(Long orderBookId);

}