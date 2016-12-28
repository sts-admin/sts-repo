package com.awacp.service;

import com.awacp.entity.AwInventory;
import com.sts.core.dto.StsResponse;

public interface AwInventoryService {

	public AwInventory updateAwInventory(AwInventory AwInventory);

	public AwInventory saveAwInventory(AwInventory AwInventory);

	public AwInventory getAwInventory(Long AwInventoryId);

	public StsResponse<AwInventory> listAwInventories(int pageNumber, int pageSize);

	public String delete(Long id);

}
