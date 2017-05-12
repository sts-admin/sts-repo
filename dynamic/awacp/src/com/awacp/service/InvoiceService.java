package com.awacp.service;

import com.awacp.entity.Invoice;
import com.awacp.entity.ProfitSheetItem;

public interface InvoiceService {

	public Invoice getInvoice(Long invoiceId);

	public Invoice getInvoiceByJobOrder(Long jobOrderId);

	public Invoice saveInvoice(Invoice invoice);

	public String delete(Long id);

	public Invoice addOrUpdateInvoice(Long jobOrderId);

	public Long saveProfitSheetItem(ProfitSheetItem psi);
}
