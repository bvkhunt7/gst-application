package com.axelor.gst.web;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.Invoices;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

@Transactional
public class InvoiceController {
	@Inject
	Invoices invoiceService;
	@Inject
	SequenceService sequenceService;
	@Inject
	MetaModelRepository metaModelRepository;
	@Inject
	SequenceRepository sequenceRepository;

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
		String nextnumber = sequenceService.generateNextnumber(sequence);
		Beans.get(SequenceRepository.class).all().filter("self.model = ?", id).update("nextnumber", nextnumber);
	}
}
