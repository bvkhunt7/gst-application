package com.axelor.gst.module;

import com.axelor.app.AxelorModule;
import com.axelor.gst.service.GstService;
import com.axelor.gst.service.GstServiceImpl;
import com.axelor.gst.web.GstController;

public class GstModule extends AxelorModule{
	
	@Override
	public void configure() {
		bind(GstService.class).to(GstServiceImpl.class);
	}

}
