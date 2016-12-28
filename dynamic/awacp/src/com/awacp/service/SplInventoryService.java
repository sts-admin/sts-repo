package com.awacp.service;

import com.awacp.entity.SplInventory;
import com.sts.core.dto.StsResponse;

public interface SplInventoryService {

	public SplInventory updateSplInventory(SplInventory SplInventory) ;

	public SplInventory saveSplInventory(SplInventory SplInventory) ;

	public SplInventory getSplInventory(Long SplInventoryId);

	public StsResponse<SplInventory> listSplInventories(int pageNumber, int pageSize);
	
	public String delete(Long id);

}
