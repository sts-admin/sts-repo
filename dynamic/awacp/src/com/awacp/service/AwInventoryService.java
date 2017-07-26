package com.awacp.service;

import java.util.List;

import com.awacp.entity.AwInventory;
import com.sts.core.dto.InventoryDTO;
import com.sts.core.dto.StsResponse;

public interface AwInventoryService {

	public AwInventory updateAwInventory(AwInventory AwInventory);

	public AwInventory saveAwInventory(AwInventory AwInventory);

	public AwInventory getAwInventory(Long AwInventoryId);

	public StsResponse<AwInventory> listAwInventories(int pageNumber, int pageSize);

	public List<InventoryDTO> listInvItems();

	public String delete(Long id);
}
