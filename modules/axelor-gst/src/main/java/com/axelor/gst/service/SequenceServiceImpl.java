package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.CompanyRepository;
import com.axelor.gst.db.repo.InvoiceRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SequenceServiceImpl implements SequenceService {

	@Inject
	InvoiceRepository invoiceRepository;

	@Inject
	CompanyRepository companyRepository;

	public Sequence generateSequence(Sequence sequence) {
		String suffix = sequence.getSuffix();
		String prefix = sequence.getPrefix();
		int padding = sequence.getPadding();
		int len = padding;
		int no = 1;
		String str = String.format("%0" + len + "d", no);
		String paddingStr = prefix.concat(str);
		String string = paddingStr.concat(suffix);
		sequence.setNextnumber(string);

		return sequence;
	}
	
	public String generateNextnumber(Sequence sequence)
	{
		int padding = sequence.getPadding();
		String prefix = sequence.getPrefix();
		String suffix = sequence.getSuffix();
		String nextNumber = sequence.getNextnumber();
		int preLen = prefix.length();

		String pad = nextNumber.substring(preLen);
		String padlast = pad.substring(0, padding);
		int paddingIncrement = Integer.parseInt(padlast);
		int len = padding;
		String incrementString = String.format("%0" + len + "d", ++paddingIncrement);
		String string = prefix + incrementString + suffix;
		
		return string;
	}
}
