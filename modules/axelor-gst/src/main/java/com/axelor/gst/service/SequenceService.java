package com.axelor.gst.service;

import com.axelor.gst.db.Sequence;

public interface SequenceService {
 
	public Sequence generateSequence(Sequence sequence);
	public String generateNextnumber(Sequence sequence);
	
}
