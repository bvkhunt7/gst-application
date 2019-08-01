package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;
import com.google.inject.Singleton;

@Singleton
public class InvoiceLineImpl implements InvoiceLines {

	public BigDecimal net_amount(BigDecimal price, int qty) {
		BigDecimal d = new BigDecimal(qty);
		BigDecimal count = d.multiply(price);
		return count;
	}

	public InvoiceLine invoiceLineData(Invoice invoice, InvoiceLine invoiceline) {
		if (invoice.getInvoiceaddress() != null && invoice.getCompany().getAddress() != null) {
			State invoiceaddress = invoice.getInvoiceaddress().getState();
			State companyaddress = invoice.getCompany().getAddress().getState();
			BigDecimal zero = BigDecimal.ZERO;

			if (companyaddress == null || invoiceaddress == null) {
				BigDecimal netamount = invoiceline.getNetamount();
				BigDecimal gross = netamount;
				invoiceline.setIgst(zero);
				invoiceline.setCsgt(zero);
				invoiceline.setSgst(zero);
				invoiceline.setGrossamount(gross);
			} else {
				if (invoiceaddress.equals(companyaddress)) {
					int div = 2;
					BigDecimal d = new BigDecimal(div);
					BigDecimal netamount = invoiceline.getNetamount();
					BigDecimal gstrate = invoiceline.getGstrate();
					BigDecimal count = (gstrate.divide(d)).multiply(netamount);
					BigDecimal gross = netamount.add(count.add(count));
					invoiceline.setCsgt(count);
					invoiceline.setSgst(count);
					invoiceline.setGrossamount(gross);
				} else if (!invoiceaddress.equals(companyaddress)) {
					BigDecimal netamount = invoiceline.getNetamount();
					BigDecimal gstrate = invoiceline.getGstrate();
					BigDecimal count = gstrate.multiply(netamount);
					BigDecimal gross = netamount.add(count);
					invoiceline.setIgst(count);
					invoiceline.setGrossamount(gross);
				}
			}
		}
		return invoiceline;
	}
}
