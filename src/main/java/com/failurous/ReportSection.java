package com.failurous;

import java.util.ArrayList;

public class ReportSection extends ArrayList<Object> {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ReportField> fields = new ArrayList<ReportField>();
	
	public ReportSection(String name) {
		this.add(name);
		this.add(fields);
	}
	
	public void addField(ReportField field) {
		fields.add(field);
	}
	
}
