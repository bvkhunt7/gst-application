package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.introspector.BeanAccess;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GstServiceImpl implements GstService {

	@Inject
	InvoiceRepository invoiceRepository;

	@Inject
	CompanyRepository companyRepository;

	public BigDecimal net_amount(BigDecimal price, int qty) {
		BigDecimal d = new BigDecimal(qty);
		BigDecimal count = d.multiply(price);
		return count;
	}

	public InvoiceLine invoiceLineData(Invoice invoice, InvoiceLine invoiceline) {
		if (invoice.getInvoiceaddress() != null) {
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
				invoiceline.setCsgt(count);
				invoiceline.setSgst(count);
				invoiceline.setGrossamount(gross);
			} else {
				BigDecimal netamount = invoiceline.getNetamount();
				BigDecimal gstrate = invoiceline.getGstrate();
				BigDecimal count = gstrate.multiply(netamount);
				System.out.println(count);
				BigDecimal gross = netamount.add(count);
				invoiceline.setIgst(count);
				invoiceline.setGrossamount(gross);
			}
		}
		return invoiceline;
	}

	public Invoice computeInvoicePartyAttrs(Invoice invoice) {
		invoice.setContact(null);

		final String ADDRESS_PRIMARY = "primary";
		final String ADDRESS_INVOICE = "invoice";
		final String ADDRESS_SHIPPING = "shipping";
		final String ADDRESS_DEFAULT = "default";
		String type = "";
		String addtype = "";
		List<Contact> con = null;
		List<Contact> contactList = invoice.getParty().getContact();
		List<Address> addressList = invoice.getParty().getAddress();

		for (Contact contact : contactList) {
			type = contact.getType();
			System.out.println(type);
			if (type.toLowerCase().equals(ADDRESS_PRIMARY)) {
				System.out.println(con);
				invoice.setContact(contact);
			} else {
				invoice.setContact(null);
			}
		}

		for (Address address : addressList) {
			addtype = address.getType();

			if (addtype.toLowerCase().equals(ADDRESS_INVOICE)) {
				invoice.setInvoiceaddress(address);
			} else if (addtype.toLowerCase().equals(ADDRESS_SHIPPING)) {
				invoice.setShippingaddress(address);
			} else if (addtype.toLowerCase().equals(ADDRESS_DEFAULT)) {
				invoice.setInvoiceaddress(address);
				invoice.setShippingaddress(address);
			}
		}
		System.out.println(invoice.getUseinvoiceaddress());
		if (invoice.getUseinvoiceaddress() == true) {
			invoice.setShippingaddress(invoice.getInvoiceaddress());
		} else if (invoice.getUseinvoiceaddress() == false) {
			invoice.setShippingaddress(invoice.getShippingaddress());
		}
		return invoice;
	}

	public Invoice computeInvoice(Invoice invoice) {
		if (invoice.getInvoiceitems() != null) {
			BigDecimal netamount = new BigDecimal(0);
			BigDecimal igst = new BigDecimal(0);
			BigDecimal csgt = new BigDecimal(0);
			BigDecimal sgst = new BigDecimal(0);
			BigDecimal grossamount = new BigDecimal(0);
			BigDecimal zero = new BigDecimal(0);

			System.out.println(invoice.getInvoiceitems());

			for (InvoiceLine item : invoice.getInvoiceitems()) {
				System.out.println(item);
				netamount = netamount.add(item.getNetamount());
				igst = igst.add(item.getIgst());
				csgt = csgt.add(item.getCsgt());
				sgst = sgst.add(item.getSgst());
				grossamount = grossamount.add(item.getGrossamount());
			}
			if (netamount == null) {
				invoice.setNetamount(zero);
			} else {
				invoice.setNetamount(netamount);
			}

			if (igst == null) {
				invoice.setNetigst(zero);
			} else {
				invoice.setNetigst(igst);
			}

			if (csgt == null && sgst == null) {
				invoice.setNetcsgt(zero);
				invoice.setNetsgst(zero);
			} else {
				invoice.setNetcsgt(csgt);
				invoice.setNetsgst(sgst);
			}

			if (grossamount == null) {
				invoice.setGrossamount(zero);
			} else {
				invoice.setGrossamount(grossamount);
			}
		}
		return invoice;
	}

	public Invoice computeInvoiceLineAttrs(Invoice invoice) {
		if (invoice.getInvoiceaddress() != null) {
			State invoiceaddress = invoice.getInvoiceaddress().getState();
			State companyaddress = invoice.getCompany().getAddress().getState();
			List<InvoiceLine> list = invoice.getInvoiceitems();
			List<InvoiceLine> list1 = new ArrayList<InvoiceLine>();
			BigDecimal netamount = new BigDecimal(0);
			BigDecimal gstrate = new BigDecimal(0);
			BigDecimal zero = new BigDecimal(0);

			if (invoiceaddress.equals(companyaddress)) {
				System.out.println("khgvfkhftyifvyt");
				int div = 2;
				BigDecimal d = new BigDecimal(div);

				for (InvoiceLine invoiceLine : list) {

					netamount = invoiceLine.getNetamount();
					gstrate = invoiceLine.getGstrate();
					BigDecimal count = (gstrate.divide(d)).multiply(netamount);
					System.out.println(count);
					BigDecimal gross = netamount.add(count.add(count));
					invoiceLine.setCsgt(count);
					invoiceLine.setSgst(count);
					invoiceLine.setIgst(zero);
					invoiceLine.setGrossamount(gross);
					list1.add(invoiceLine);
				}
				invoice.setInvoiceitems(list1);
			} else if (!invoiceaddress.equals(companyaddress)) {

				for (InvoiceLine invoiceLine : list) {

					netamount = invoiceLine.getNetamount();
					gstrate = invoiceLine.getGstrate();
					BigDecimal count = gstrate.multiply(netamount);
					System.out.println(count);
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

	public Sequence generateSequence(Sequence sequence) {
		String suffix = sequence.getSuffix();
		String prefix = sequence.getPrefix();
		int padding = sequence.getPadding();
		MetaModel model = sequence.getModel();

		int len = padding;
		int no = 1;
		String str = String.format("%0" + len + "d", no);
		String paddingStr = prefix.concat(str);
		String string = paddingStr.concat(suffix);
		sequence.setNextnumber(string);
		System.out.println(string);
		System.out.println(str);
		System.out.println(model);

		return sequence;
	}
}
