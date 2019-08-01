package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.SequenceService;
import com.axelor.gst.service.SequenceServiceImpl;
import com.axelor.gst.service.Invoices;
import com.axelor.gst.service.InvoiceImpl;
import com.axelor.gst.service.InvoiceLines;
import com.axelor.gst.service.InvoiceLineImpl;
import com.axelor.gst.web.GstController;

public class GstModule extends AxelorModule{
	
	@Override
	public void configure() {
		bind(SequenceService.class).to(SequenceServiceImpl.class);
		bind(Invoices.class).to(InvoiceImpl.class);
		bind(InvoiceLines.class).to(InvoiceLineImpl.class);
	}

}
