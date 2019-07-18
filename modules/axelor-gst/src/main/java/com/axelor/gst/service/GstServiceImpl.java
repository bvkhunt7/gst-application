package com.axelor.gst.service;

import java.math.BigDecimal;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;



@Singleton
public class GstServiceImpl implements GstService{

@Inject InvoiceRepository invoiceRepository;

@Inject CompanyRepository companyRepository;

	public BigDecimal net_amount(BigDecimal price, int qty) {
		BigDecimal d = new BigDecimal(qty);

		BigDecimal count = d.multiply(price);

		return count;
	}	

	public BigDecimal igst_value(Invoice invoiceline) {
             Invoice invoice = null;
             Company company = null;
		invoice = invoiceRepository.find(invoiceline.getId());
       company =  companyRepository.find(invoice.getId());
        
       
       State invoiceaddress = invoice.getInvoiceaddress().getState();
		State companyaddress = company.getAddress().getState();
         BigDecimal calculated = null;
		if(invoiceaddress == companyaddress)
		{    int div = 2;
			BigDecimal d = new BigDecimal(div);
		BigDecimal netamount = invoice.getInvoiceitems().get(0).getNetamount();
		BigDecimal gstrate = invoice.getInvoiceitems().get(0).getGstrate();
		BigDecimal count = netamount.multiply(gstrate).divide(d);
		calculated = count;
		}
		return calculated;
		
	}


}
