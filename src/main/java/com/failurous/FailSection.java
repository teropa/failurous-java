package com.failurous;

import java.util.ArrayList;

public class FailSection extends ArrayList<Object> {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<FailField> fields = new ArrayList<FailField>();
	
	public FailSection(String name) {
		this.add(name);
		this.add(fields);
	}
	
	public void addField(FailField field) {
		fields.add(field);
	}

	public void addField(String name, String value, String... options) {
		addField(new FailField(name, value, options));
	}

}
