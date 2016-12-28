package com.awacp.service;

import com.awacp.entity.JInventory;
import com.sts.core.dto.StsResponse;

public interface JInventoryService {

	public JInventory updateJInventory(JInventory JInventory);

	public JInventory saveJInventory(JInventory JInventory);

	public JInventory getJInventory(Long JInventoryId);

	public StsResponse<JInventory> listJInventories(int pageNumber, int pageSize);
	
	public String delete(Long id);

}
