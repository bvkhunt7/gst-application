package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.service.GstService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class GstController {

	@Inject
	GstService gstService;

	@Inject
	InvoiceRepository invoiceRepository;

	@Inject
	CompanyRepository companyRepository;

	public void calculate(ActionRequest requests, ActionResponse responses) {

		InvoiceLine invoiceline = requests.getContext().asType(InvoiceLine.class);

		int qty = invoiceline.getQty();
		BigDecimal price = invoiceline.getPrice();
		System.out.println("ojiogwerjpgqrog");
		responses.setValue("netamount", gstService.net_amount(price, qty));

	}

	public void igst(ActionRequest requests, ActionResponse responses) {
		Invoice invoice = requests.getContext().asType(Invoice.class);

		gstService.igst_value(invoice);

	}

//	public void getInvoiceAddress(ActionRequest request, ActionResponse response) {
//		Invoice invoice = request.getContext().asType(Invoice.class);
//
//		//Party party = null;
//		// Company company = null;
//		// party = request.getContext().getParent().asType(Party.class);
//		String type = "";
//		String addtype = "";
//		
//		List<Address> add = null;
//		List<Address> list = invoice.getParty().getAddress();
//		for (Address address : list) {
//			type = address.getType();
//
//			if (type.toLowerCase().equals("invoice")) {
//				invoice.setInvoiceaddress(address);
//
//			} else if (type.toLowerCase().equals("shipping")) {
//				
//				invoice.setShippingaddress(address);
//			
//			} else if (type.toLowerCase().equals("default")) {
//			
//				invoice.setInvoiceaddress(address);
//				invoice.setShippingaddress(address);
//			
//			}
//		}
//
//		if (invoice.getUseinvoiceaddress()) {
//                 invoice.setShippingaddress(invoice.getInvoiceaddress());
//		}else {
//			    invoice.setShippingaddress(invoice.getShippingaddress());
//		}
//
//		response.setValues(invoice);
//
//	}

	public void getInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);

		
		// Company company = null;
		// party = request.getContext().getParent().asType(Party.class);
		String type = "";
		String addtype = "";
		List<Contact> con = null;
		List<Contact> list = invoice.getParty().getContact();
		List<Address> lists = invoice.getParty().getAddress();
		
		for (Contact contact : list) {
			type = contact.getType();
			System.out.println(type);
			if (type.toLowerCase().equals("primary")) { // invoice.getParty().getContact();

				System.out.println(con);
				invoice.setContact(contact);
			}

		}
		
		for (Address address : lists) {
			addtype = address.getType();

			if (addtype.toLowerCase().equals("invoice")) {
				invoice.setInvoiceaddress(address);

			} else if (addtype.toLowerCase().equals("shipping")) {
				
				invoice.setShippingaddress(address);
			
			} else if (addtype.toLowerCase().equals("default")) {
			
				invoice.setInvoiceaddress(address);
				invoice.setShippingaddress(address);
			
			}
		}
     System.out.println(invoice.getUseinvoiceaddress());
		if (invoice.getUseinvoiceaddress() == true) {
                 invoice.setShippingaddress(invoice.getInvoiceaddress());
		}else if (invoice.getUseinvoiceaddress() == false) {
			invoice.setShippingaddress(invoice.getShippingaddress());
		}
		

		response.setValues(invoice);

	}

	public void setInvoiceline(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);

		Invoice invoice = null;
		Company company = null;
		invoice = request.getContext().getParent().asType(Invoice.class);

		State invoiceaddress = invoice.getInvoiceaddress().getState();
		State companyaddress = invoice.getCompany().getAddress().getState();

		if (invoiceaddress.equals(companyaddress)) {
			System.out.println("khgvfkhftyifvyt");
			int div = 2;
			BigDecimal d = new BigDecimal(div);
			BigDecimal netamount = invoiceline.getNetamount();
			BigDecimal gstrate = invoiceline.getGstrate();

			BigDecimal count = (gstrate.divide(d)).multiply(netamount);
			System.out.println(count);

			BigDecimal gross = netamount.add(count.add(count));

			response.setValue("sgst", count);
			response.setValue("csgt", count);
			response.setValue("grossamount", gross);
		} else if (!invoiceaddress.equals(companyaddress)) {

			BigDecimal netamount = invoiceline.getNetamount();
			BigDecimal gstrate = invoiceline.getGstrate();

			BigDecimal count = gstrate.multiply(netamount);
			System.out.println(count);

			BigDecimal gross = netamount.add(count);

			response.setValue("igst", count);
			response.setValue("grossamount", gross);

		}

	}

	public void setInvoice(ActionRequest requests, ActionResponse responses) {
		Invoice invoice = requests.getContext().asType(Invoice.class);

		BigDecimal netamount = new BigDecimal(0);
		BigDecimal igst = new BigDecimal(0);
		BigDecimal cgst = new BigDecimal(0);
		BigDecimal sgst = new BigDecimal(0);;
		BigDecimal grossamount = new BigDecimal(0);

		System.out.println(invoice.getInvoiceitems());

		for (InvoiceLine item : invoice.getInvoiceitems()) {
			System.out.println(item);
			
			  netamount = netamount.add(item.getNetamount()); 
			  igst = igst.add(item.getIgst()); 
			  cgst = cgst.add(item.getCsgt()); 
			  sgst =  sgst.add(item.getSgst()); 
			  grossamount = grossamount.add(item.getGrossamount());
			 

		}

		if (netamount == null) {
			responses.setValue("netamount", 0);
		} else {
			responses.setValue("netamount", netamount);
		}
		if (igst == null) {
			responses.setValue("netigst", 0);
		} else {
			responses.setValue("netigst", igst);
		}

		if (cgst == null && sgst == null) {
			responses.setValue("netcgst", 0);
			responses.setValue("netsgst", 0);
		} else {
			responses.setValue("netcgst", cgst);
			responses.setValue("netsgst", sgst);
		}
		if (grossamount == null) {
			responses.setValue("grossamount", 0);
		} else {
			responses.setValue("grossamount", grossamount);
		}

	}

}
