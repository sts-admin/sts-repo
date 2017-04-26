package com.awacp.service;

import com.awacp.entity.Invoice;

public interface InvoiceService {
	
	public Invoice getInvoice(Long invoiceId);
	
	public Invoice getInvoiceByJobOrder(Long jobOrderId);

	public Invoice saveInvoice(Invoice invoice);

	public String delete(Long id);
	
	public Invoice addDummyInvoice(Long jobOrderId);

}
