package com.axelor.gst.web;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.GstService;
import com.axelor.gst.service.InvoiceLines;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
@Transactional
public class InvoiceLineController {

	@Inject
	InvoiceLines invoiceLinesService;
	@Inject
	MetaModelRepository metaModelRepository;
	@Inject
	SequenceRepository sequenceRepository;
	@Inject
	Provider<EntityManager> em;
	
	public void calculate(ActionRequest requests, ActionResponse responses) {

		InvoiceLine invoiceline = requests.getContext().asType(InvoiceLine.class);
		int qty = invoiceline.getQty();
		BigDecimal price = invoiceline.getPrice();
		responses.setValue("netamount", invoiceLinesService.net_amount(price, qty));
	}

	public void setInvoiceline(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);
		Invoice invoice = null;
		invoice = request.getContext().getParent().asType(Invoice.class);

		invoiceLinesService.invoiceLineData(invoice, invoiceline);
		response.setValues(invoiceline);
	}
	
}
