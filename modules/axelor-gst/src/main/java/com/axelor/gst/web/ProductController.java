package com.axelor.gst.web;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Product;
import com.axelor.gst.db.repo.ProductRepository;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.GstService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

@Transactional
public class ProductController {
	@Inject
	GstService gstService;
	@Inject
	MetaModelRepository metaModelRepository;
	@Inject
	SequenceRepository sequenceRepository;
	@Inject
	Provider<EntityManager> em;
	
	public void createProductInvoiceLine(ActionRequest request, ActionResponse response) {

		Product product = request.getContext().asType(Product.class);
		System.out.println(request.getContext().get("_ids"));
		List<Integer> requestIds = (List<Integer>) request.getContext().get("_ids");
		if (requestIds == null) {

		} else {
			List<InvoiceLine> listinvoice = new ArrayList<InvoiceLine>();
			InvoiceLine invoiceLine = new InvoiceLine();
			for (Integer item : requestIds) {
				product = Beans.get(ProductRepository.class).find((long) item);
				invoiceLine.setProduct(product);
				invoiceLine.setItem(product.getName());
				invoiceLine.setPrice(product.getSaleprice());
				invoiceLine.setHsbn(product.getHsbn());

				listinvoice.add(invoiceLine);
			}

			if (requestIds != null) {
				response.setView(
						ActionView.define("Invoice").model("com.axelor.gst.db.Invoice").add("form", "invoice-form")
								.context("productIds", listinvoice).param("forceEdit", "true").map());
			}
		}
	}
	
	public void getProductContext(ActionRequest request, ActionResponse response) {
		List<InvoiceLine> invoiceLines = (List<InvoiceLine>) request.getContext().get("productIds");
		response.setValue("invoiceitems", invoiceLines);
	}
}
