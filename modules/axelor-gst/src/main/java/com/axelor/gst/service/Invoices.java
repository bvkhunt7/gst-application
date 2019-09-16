package com.axelor.gst.service;

import com.axelor.gst.db.Invoice;

public interface Invoices {
	public Invoice computeInvoicePartyAttrs(Invoice invoice); 
	public Invoice computeInvoice(Invoice invoice);
	public Invoice computeInvoiceLineAttrs(Invoice invoice);
}
