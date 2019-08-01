package com.axelor.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.gst.db.Address;
import com.axelor.gst.db.Contact;
import com.axelor.gst.db.Invoice;
import com.axelor.gst.db.InvoiceLine;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.State;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.axelor.meta.db.MetaModel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GstServiceImpl implements GstService {

	@Inject
	InvoiceRepository invoiceRepository;

	@Inject
	CompanyRepository companyRepository;

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

		return sequence;
	}
}
