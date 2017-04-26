package com.awacp.service;

import java.util.List;

import com.awacp.entity.SplInventory;
import com.sts.core.dto.InventoryDTO;
import com.sts.core.dto.StsResponse;

public interface SplInventoryService {

	public SplInventory updateSplInventory(SplInventory SplInventory) ;

	public SplInventory saveSplInventory(SplInventory SplInventory) ;

	public SplInventory getSplInventory(Long SplInventoryId);

	public StsResponse<SplInventory> listSplInventories(int pageNumber, int pageSize);
	
	public List<InventoryDTO> listInvItems();
	
	public String delete(Long id);

}
