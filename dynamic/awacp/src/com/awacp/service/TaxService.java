package com.awacp.service;

import com.awacp.entity.TaxEntry;
import com.sts.core.dto.StsResponse;

public interface TaxService {

	public TaxEntry updateTaxEntry(TaxEntry taxEntry);

	public TaxEntry saveTaxEntry(TaxEntry taxEntry);

	public TaxEntry getTaxEntry(Long taxEntryId);

	public StsResponse<TaxEntry> listTaxEntries(int pageNumber, int pageSize);

	public String delete(Long id);

}
