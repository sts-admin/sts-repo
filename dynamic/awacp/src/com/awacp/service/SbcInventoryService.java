package com.awacp.service;

import com.awacp.entity.SbcInventory;
import com.sts.core.dto.StsResponse;

public interface SbcInventoryService {

	public SbcInventory updateSbcInventory(SbcInventory SbcInventory);

	public SbcInventory saveSbcInventory(SbcInventory SbcInventory);

	public SbcInventory getSbcInventory(Long SbcInventoryId);

	public StsResponse<SbcInventory> listSbcInventories(int pageNumber, int pageSize);

	public String delete(Long id);

}
