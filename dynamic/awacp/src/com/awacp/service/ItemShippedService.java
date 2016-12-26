package com.awacp.service;

import com.awacp.entity.ItemShipped;
import com.sts.core.dto.StsResponse;

public interface ItemShippedService {

	public ItemShipped updateItemShipped(ItemShipped ItemShipped);

	public ItemShipped saveItemShipped(ItemShipped ItemShipped);

	public ItemShipped getItemShipped(Long architectId);

	public StsResponse<ItemShipped> listItemShippeds(int pageNumber, int pageSize);

	public String delete(Long id);

}
