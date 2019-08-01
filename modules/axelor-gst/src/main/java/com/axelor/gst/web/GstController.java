package com.axelor.gst.web;

import java.util.List;

import javax.persistence.EntityManager;

import com.axelor.app.AppSettings;
import com.axelor.gst.db.repo.SequenceRepository;
import com.axelor.gst.service.SequenceService;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;

public class GstController {

	@Inject
	SequenceService gstService;

	@SuppressWarnings("unchecked")
	public void printInvoices(ActionRequest request, ActionResponse response) {

		List<Long> ids = (List<Long>) request.getContext().get("_ids");
		String string = "";

		if (ids == null) {
			long idss = (long) request.getContext().get("id");
			String stringform = " ";
			stringform = stringform + idss;
			request.getContext().put("_ids", stringform);
			String uploadDir = AppSettings.get().get("file.upload.dir");
			request.getContext().put("file_path", uploadDir);

		} else if (!ids.isEmpty()) {
			for (int i = 0; i < ids.size(); i++) {
				if (i == ((ids.size()) - 1)) {
					string = string + ids.get(i);
				} else {
					string = string + ids.get(i) + ",";
				}
			}
			request.getContext().put("_ids", string);
			String uploadDir = AppSettings.get().get("file.upload.dir");
			request.getContext().put("file_path", uploadDir);
		}
	}
}
