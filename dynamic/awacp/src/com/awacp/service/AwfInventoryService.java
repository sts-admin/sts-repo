package com.awacp.service;

import com.awacp.entity.AwfInventory;
import com.sts.core.dto.StsResponse;

public interface AwfInventoryService {

	public AwfInventory updateAwfInventory(AwfInventory AwfInventory) ;

	public AwfInventory saveAwfInventory(AwfInventory AwfInventory) ;

	public AwfInventory getAwfInventory(Long AwfInventoryId);

	public StsResponse<AwfInventory> listAwfInventories(int pageNumber, int pageSize);
	
	public String delete(Long id);

}
