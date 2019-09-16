package com.axelor.gst.web;

import com.axelor.gst.db.Party;
import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

@Transactional
public class PartyController {

	@Inject
	SequenceService sequenceService;
	@Inject
	MetaModelRepository metaModelRepository;
	@Inject
	SequenceRepository sequenceRepository;

	public void setPartyReference(ActionRequest request, ActionResponse response) {

		Party party = request.getContext().asType(Party.class);
		MetaModel metamodel = Beans.get(MetaModelRepository.class).findByName("Party");

		Long id = metamodel.getId();
		Sequence sequence = Beans.get(SequenceRepository.class).all().filter("self.model = ?", id).fetchOne();
		if (sequence == null) {
			
			response.setError("Sequence does not specified for this model");
			
		} else {
			String reference = sequence.getNextnumber();
			if (party.getReference() == null) {
				party.setReference(reference);
				response.setValues(party);
			}
			String nextnumber = sequenceService.generateNextnumber(sequence);
			Beans.get(SequenceRepository.class).all().filter("self.model = ?", id).update("nextnumber", nextnumber);
		}
	}
}
