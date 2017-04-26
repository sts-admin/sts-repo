package com.awacp.service;

import java.util.List;

import com.awacp.entity.OrderBook;
import com.sts.core.dto.StsResponse;

public interface OrderBookService {

	public OrderBook saveOrderBook(OrderBook orderBook);

	public OrderBook getOrderBook(Long orderBookId);

	public StsResponse<OrderBook> listOrderBooks(int pageNumber, int pageSize);

	public String delete(Long id);

	public int debitInvItemQty(Long lineItemId, String invName, int orderQty);

	public List<OrderBook> listByJobOrder(Long jobOrderId);
	
	public Double getPrice(Long lineItemId, String invName);

}