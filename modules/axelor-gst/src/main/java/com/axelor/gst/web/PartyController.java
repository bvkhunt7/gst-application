package com.axelor.gst.web;

import javax.persistence.EntityManager;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.GstService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

@Transactional
public class PartyController {

	@Inject
	GstService gstService;
	@Inject
	MetaModelRepository metaModelRepository;
	@Inject
	SequenceRepository sequenceRepository;
	@Inject
	Provider<EntityManager> em;
	
	public void setPartyReference(ActionRequest request, ActionResponse response) {

		Party party = request.getContext().asType(Party.class);
		MetaModel mm = Beans.get(MetaModelRepository.class).findByName("Party");
		
		Long id = mm.getId();
		Sequence sequence = Beans.get(SequenceRepository.class).all().filter("self.model = ?", id).fetchOne();
		String reference = sequence.getNextnumber();
		if (party.getReference() == null) {
			party.setReference(reference);
			response.setValues(party);
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
