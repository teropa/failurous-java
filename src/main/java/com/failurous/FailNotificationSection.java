package com.failurous;

import java.util.ArrayList;

public class FailNotificationSection extends ArrayList<Object> {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<FailNotificationField> fields = new ArrayList<FailNotificationField>();
	
	public FailNotificationSection(String name) {
		this.add(name);
		this.add(fields);
	}
	
	public void addField(FailNotificationField field) {
		fields.add(field);
	}
	
}
