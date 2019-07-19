package com.axelor.gst.web;

import java.math.BigDecimal;

import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.service.GstService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class GstController {

	@Inject
	GstService gstService;

	public void calculate(ActionRequest requests, ActionResponse responses) {

		InvoiceLine invoiceline = requests.getContext().asType(InvoiceLine.class);

		int qty = invoiceline.getQty();
		BigDecimal price = invoiceline.getPrice();
		System.out.println("ojiogwerjpgqrog");
		responses.setValue("netamount", gstService.net_amount(price, qty));

	}

	public void getInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);

		gstService.partSelectData(invoice);
		response.setValues(invoice);
	}

	public void setInvoiceline(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);

		Invoice invoice = null;
		Company company = null;
		invoice = request.getContext().getParent().asType(Invoice.class);

		gstService.invoiceLineData(invoice, invoiceline);
		response.setValues(invoiceline);

	}

	public void setInvoice(ActionRequest requests, ActionResponse responses) {
		Invoice invoice = requests.getContext().asType(Invoice.class);

		gstService.computeInvoice(invoice);
		responses.setValues(invoice);

	}

	public void setPartyInvoiceInvoiceLine(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);

		Invoice data = gstService.partyChangeInvoiceLine(invoice);
		response.setValue("invoiceitems", data.getInvoiceitems());

	}
}
