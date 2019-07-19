package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface GstService {
 
	public BigDecimal net_amount(BigDecimal price, int qty);
	public InvoiceLine invoiceLineData(Invoice invoice,InvoiceLine invoiceLine);
	public Invoice partSelectData(Invoice invoice); 
	public Invoice computeInvoice(Invoice invoice);
	public Invoice partyChangeInvoiceLine(Invoice invoice);
}
