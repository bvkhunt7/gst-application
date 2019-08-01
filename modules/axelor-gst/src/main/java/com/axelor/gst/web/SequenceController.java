package com.axelor.gst.web;

import javax.persistence.EntityManager;

import com.axelor.gst.db.Sequence;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.GstService;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

@Transactional
public class SequenceController {

	@Inject
	GstService gstService;
	@Inject
	MetaModelRepository metaModelRepository;
	@Inject
	SequenceRepository sequenceRepository;
	@Inject
	Provider<EntityManager> em;
	
	public void generateSequence(ActionRequest request, ActionResponse response) {

		Sequence sequence = request.getContext().asType(Sequence.class);

		gstService.generateSequence(sequence);
		response.setValues(sequence);

	}
}
