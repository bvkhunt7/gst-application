package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;

public interface GstService {
 
	public BigDecimal net_amount(BigDecimal price, int qty);
	public BigDecimal igst_value(Invoice invoice);
}
