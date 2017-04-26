package com.awacp.service;

import java.util.List;

import com.awacp.entity.JInventory;
import com.sts.core.dto.InventoryDTO;
import com.sts.core.dto.StsResponse;

public interface JInventoryService {

	public JInventory updateJInventory(JInventory JInventory);

	public JInventory saveJInventory(JInventory JInventory);

	public JInventory getJInventory(Long JInventoryId);

	public StsResponse<JInventory> listJInventories(int pageNumber, int pageSize);
	
	public List<InventoryDTO> listInvItems();
	
	public String delete(Long id);

}
