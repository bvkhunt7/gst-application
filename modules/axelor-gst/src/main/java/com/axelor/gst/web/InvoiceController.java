package com.axelor.gst.web;

import javax.persistence.EntityManager;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.GstService;
import com.axelor.gst.service.Invoices;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

@Transactional
public class InvoiceController {
	@Inject
	Invoices invoiceService;
	@Inject
	MetaModelRepository metaModelRepository;
	@Inject
	SequenceRepository sequenceRepository;
	@Inject
	Provider<EntityManager> em;

	public void setInvoice(ActionRequest requests, ActionResponse responses) {
		Invoice invoice = requests.getContext().asType(Invoice.class);
		invoiceService.computeInvoice(invoice);
		responses.setValues(invoice);
	}

	public void getInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		invoiceService.computeInvoicePartyAttrs(invoice);
		response.setValues(invoice);
	}

	public void setPartyInvoiceInvoiceLine(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);
		Invoice data = invoiceService.computeInvoiceLineAttrs(invoice);
		response.setValue("invoiceitems", data.getInvoiceitems());
		invoiceService.computeInvoice(invoice);
		response.setValues(invoice);
		System.out.println("jkbyugg");
	}

	public void setInvoiceReference(ActionRequest request, ActionResponse response) {

		Invoice invoice = request.getContext().asType(Invoice.class);
		MetaModel mm = Beans.get(MetaModelRepository.class).findByName("Invoice");
		Long id = mm.getId();
		Sequence sequence = Beans.get(SequenceRepository.class).all().filter("self.model = ?", id).fetchOne();
		String reference = sequence.getNextnumber();

		if (invoice.getReference() == null && invoice.getStatus().equals("1")) {
			invoice.setReference(reference);
			response.setValues(invoice);
		}
		int padding = sequence.getPadding();
		String prefix = sequence.getPrefix();
		String suffix = sequence.getSuffix();
		String nextNumber = sequence.getNextnumber();
		int preLen = prefix.length();
		int sufLen = suffix.length();
		String pad = nextNumber.substring(preLen);
		String padlast = pad.substring(0, padding);
		int paddingIncrement = Integer.parseInt(padlast);
		int len = padding;
		String incrementString = String.format("%0" + len + "d", ++paddingIncrement);
		String string = prefix + incrementString + suffix;
		Beans.get(SequenceRepository.class).all().filter("self.model = ?", id).update("nextnumber", string);
	}
}
