package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.AddressRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.google.inject.Singleton;

@Singleton
public class InvoiceImpl implements Invoices {

	public Invoice computeInvoice(Invoice invoice) {
		if (invoice.getInvoiceitems() != null) {
			BigDecimal netamount = BigDecimal.ZERO;
			BigDecimal igst = BigDecimal.ZERO;
			BigDecimal csgt = BigDecimal.ZERO;
			BigDecimal sgst = BigDecimal.ZERO;
			BigDecimal grossamount = BigDecimal.ZERO;

			for (InvoiceLine item : invoice.getInvoiceitems()) {
				netamount = netamount.add(item.getNetamount());
				igst = igst.add(item.getIgst());
				csgt = csgt.add(item.getCsgt());
				sgst = sgst.add(item.getSgst());
				grossamount = grossamount.add(item.getGrossamount());
			}
			invoice.setNetamount(netamount);
			invoice.setNetigst(igst);
			invoice.setNetcsgt(csgt);
			invoice.setNetsgst(sgst);
			invoice.setGrossamount(grossamount);
		}
		return invoice;
	}

	public Invoice computeInvoiceLineAttrs(Invoice invoice) {

		List<InvoiceLine> list = invoice.getInvoiceitems();
		List<InvoiceLine> list1 = new ArrayList<InvoiceLine>();
		BigDecimal netamount = BigDecimal.ZERO;
		BigDecimal gstrate = BigDecimal.ZERO;
		BigDecimal zero = BigDecimal.ZERO;

		if (invoice.getParty() == null || invoice.getCompany() == null) {
			if (invoice.getInvoiceitems() != null) {
				for (InvoiceLine invoiceLine : list) {
					netamount = invoiceLine.getNetamount();
					BigDecimal gross = netamount.add(zero);
					invoiceLine.setIgst(zero);
					invoiceLine.setCsgt(zero);
					invoiceLine.setSgst(zero);
					invoiceLine.setGrossamount(gross);
					list1.add(invoiceLine);
				}
				invoice.setInvoiceitems(list1);
			}
		} else if (invoice.getInvoiceaddress() != null && invoice.getCompany().getAddress() != null) {
			State invoiceaddress = invoice.getInvoiceaddress().getState();
			State companyaddress = invoice.getCompany().getAddress().getState();

			if (invoiceaddress.equals(companyaddress)) {
				int div = 2;
				BigDecimal d = new BigDecimal(div);

				for (InvoiceLine invoiceLine : list) {
					netamount = invoiceLine.getNetamount();
					gstrate = invoiceLine.getGstrate();
					BigDecimal count = (gstrate.multiply(netamount)).divide(d);
					BigDecimal gross = netamount.add(count.add(count));
					invoiceLine.setCsgt(count);
					invoiceLine.setSgst(count);
					invoiceLine.setIgst(zero);
					invoiceLine.setGrossamount(gross);
					list1.add(invoiceLine);
				}
				invoice.setInvoiceitems(list1);
			} else {
				for (InvoiceLine invoiceLine : list) {
					netamount = invoiceLine.getNetamount();
					gstrate = invoiceLine.getGstrate();
					BigDecimal count = gstrate.multiply(netamount);
					BigDecimal gross = netamount.add(count);
					invoiceLine.setIgst(count);
					invoiceLine.setSgst(zero);
					invoiceLine.setCsgt(zero);
					invoiceLine.setGrossamount(gross);
					list1.add(invoiceLine);
				}
				invoice.setInvoiceitems(list1);
			}
		}
		return invoice;
	}

	public Invoice computeInvoicePartyAttrs(Invoice invoice) {
		if (invoice.getParty() != null) {
			invoice.setContact(null);
			String type = "";
			String addtype = "";
			List<Contact> contactList = invoice.getParty().getContact();
			List<Address> addressList = invoice.getParty().getAddress();

			for (Contact contact : contactList) {
				type = contact.getType();
				if (type.toLowerCase().equals(AddressRepository.ADDRESS_PRIMARY)) {
					invoice.setContact(contact);
				} else {
					invoice.setContact(null);
				}
			}
			for (Address address : addressList) {
				addtype = address.getType();

				if (addtype.toLowerCase().equals(AddressRepository.ADDRESS_INVOICE)) {
					invoice.setInvoiceaddress(address);
				} else if (addtype.toLowerCase().equals(AddressRepository.ADDRESS_SHIPPING)) {
					invoice.setShippingaddress(address);
				} else if (addtype.toLowerCase().equals(AddressRepository.ADDRESS_DEFAULT)) {
					invoice.setInvoiceaddress(address);
					invoice.setShippingaddress(address);
				}
			}
			if (invoice.getUseinvoiceaddress() == true) {
				invoice.setShippingaddress(invoice.getInvoiceaddress());
			} else if (invoice.getUseinvoiceaddress() == false) {
				invoice.setShippingaddress(invoice.getShippingaddress());
			}
		}
		return invoice;
	}
}
