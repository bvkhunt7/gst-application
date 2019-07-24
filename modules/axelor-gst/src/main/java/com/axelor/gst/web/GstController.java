package com.axelor.gst.web;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import com.axelor.app.AppSettings;
import com.axelor.db.Query;
import com.axelor.gst.db.Company;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.PartyRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.GstService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaField;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
@Transactional
public class GstController {

	@Inject
	GstService gstService;
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
		System.out.println("ojiogwerjpgqrog");
		responses.setValue("netamount", gstService.net_amount(price, qty));

	}

	/*
	 * public void computeInvoiceBtn(ActionRequest request, ActionResponse response)
	 * { Invoice invoice = request.getContext().asType(Invoice.class); String status
	 * = invoice.getStatus(); response.setValue("status",
	 * gstService.computeInvoiceBtn(status)); }
	 */

	public void generateSequence(ActionRequest request, ActionResponse response) {

		Sequence sequence = request.getContext().asType(Sequence.class);

		
		gstService.generateSequence(sequence);
		response.setValues(sequence);


	}

	public void setPartyReference(ActionRequest request, ActionResponse response) {

		Party party = request.getContext().asType(Party.class);
		MetaModel mm = Beans.get(MetaModelRepository.class).findByName("Party");
		System.out.println(mm);
		Long id = mm.getId();
		System.out.println(mm.getId());
		Sequence sequence = Beans.get(SequenceRepository.class).all().filter("self.model = ?", id).fetchOne();
		System.out.println(sequence);
		String reference = sequence.getNextnumber();
		System.out.println(reference);
        if(party.getReference() == null)
		{party.setReference(reference);
		response.setValues(party);
		}
        int padding = sequence.getPadding();
		String prefix = sequence.getPrefix();
		String suffix = sequence.getSuffix();
		System.out.println(mm.getId());
		String nextNumber = sequence.getNextnumber();
	
		
		System.out.println(reference);
//		if (nextNumber.equals(reference)) {
			int preLen = prefix.length();
			int sufLen = suffix.length();
			
			String pad = nextNumber.substring(preLen);
			String padlast = pad.substring(0,padding);
			int paddingIncrement = Integer.parseInt(padlast);
			int len = padding;
	          System.out.println(pad);
	          System.out.println(padlast);
	          String incrementString = String.format("%0" + len + "d", ++paddingIncrement);
	          System.out.println(incrementString);
	         String string = prefix + incrementString + suffix;
				System.out.println(string);
				//sequence.setNextnumber(string);
	        
		Beans.get(SequenceRepository.class).all().filter("self.model = ?", id).update("nextnumber", string);
			

		}
		
		
	//}

	public void getInvoice(ActionRequest request, ActionResponse response) {
		Invoice invoice = request.getContext().asType(Invoice.class);

		gstService.computeInvoicePartyAttrs(invoice);
		response.setValues(invoice);
	}

	public void setInvoiceline(ActionRequest request, ActionResponse response) {
		InvoiceLine invoiceline = request.getContext().asType(InvoiceLine.class);

		Invoice invoice = null;
		
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

		Invoice data = gstService.computeInvoiceLineAttrs(invoice);
		response.setValue("invoiceitems", data.getInvoiceitems());

	}

//	public void printInvoices(ActionRequest request, ActionResponse response) {
//
//		// grid
//		System.out.println(request.getContext().get("_ids"));
//		List<Long> ids = (List<Long>) request.getContext().get("_ids");
//		String string = "";
//
//		// form
//		if (ids == null) {
//			System.out.println(request.getContext().get("id"));
//			long idss = (long) request.getContext().get("id");
//			String stringform = " ";
//			stringform = stringform + idss;
//
//			// form
//			System.out.println(stringform);
//			request.getContext().put("_ids", stringform);
//			System.out.println(request.getContext().get("_ids"));
//
//			String uploadDir = AppSettings.get().get("file.upload.dir");
//			request.getContext().put("file_path", uploadDir);
//			System.out.println(request.getContext().get("file_path"));
//
//		} else if (!ids.isEmpty()) {
//
//			// grid
//			for (int i = 0; i < ids.size(); i++) {
//				if (i == ((ids.size()) - 1)) {
//					string = string + ids.get(i);
//				} else {
//					string = string + ids.get(i) + ",";
//				}
//			}
//
//			// grid
//			System.out.println(string);
//			request.getContext().put("_ids", string);
//			System.out.println(request.getContext().get("_ids"));
//
//			String uploadDir = AppSettings.get().get("file.upload.dir");
//			request.getContext().put("file_path", uploadDir);
//			System.out.println(request.getContext().get("file_path"));
//
//		}
//	}

}
