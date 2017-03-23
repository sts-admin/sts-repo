package com.awacp.service;

import com.awacp.entity.OrderBook;
import com.sts.core.dto.StsResponse;

public interface OrderBookService {

	public OrderBook save(OrderBook orderBook);

	public OrderBook getOrderBook(Long orderBookId);

	public StsResponse<OrderBook> listOrderBooks(int pageNumber, int pageSize);

	public String delete(Long id);

}