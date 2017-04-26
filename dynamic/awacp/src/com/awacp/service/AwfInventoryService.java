package com.awacp.service;

import java.util.List;

import com.awacp.entity.AwfInventory;
import com.sts.core.dto.InventoryDTO;
import com.sts.core.dto.StsResponse;

public interface AwfInventoryService {

	public AwfInventory updateAwfInventory(AwfInventory AwfInventory);

	public AwfInventory saveAwfInventory(AwfInventory AwfInventory);

	public AwfInventory getAwfInventory(Long AwfInventoryId);

	public StsResponse<AwfInventory> listAwfInventories(int pageNumber, int pageSize);

	public List<InventoryDTO> listInvItems();

	public String delete(Long id);

}
