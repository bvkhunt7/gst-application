package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;

public interface GstService {
 
	public BigDecimal net_amount(BigDecimal price, int qty);
	public InvoiceLine invoiceLineData(Invoice invoice,InvoiceLine invoiceLine);
	public Invoice computeInvoicePartyAttrs(Invoice invoice); 
	public Invoice computeInvoice(Invoice invoice);
	public Invoice computeInvoiceLineAttrs(Invoice invoice);
	//public String computeInvoiceBtn(String status);
	public Sequence generateSequence(Sequence sequence);
//	public Party setPartyReference(Party party);
	
	
}
