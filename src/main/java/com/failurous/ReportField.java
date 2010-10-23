package com.failurous;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportField extends ArrayList<Object> {
	private static final long serialVersionUID = 1L;
	
	public ReportField(String name, String value) {
		this.add(0, name);
		this.add(1, value);
		this.add(new LinkedHashMap<String, String>());
	}

	public void setOption(String key, String val) {
		getOptions().put(key, val);
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> getOptions() {
		return (Map<String,String>)this.get(2);
	}

	
}
