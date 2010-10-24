package com.failurous;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class FailField extends ArrayList<Object> {
	private static final long serialVersionUID = 1L;
	
	public FailField(String name, String value) {
		this.add(name);
		this.add(value);
		this.add(new LinkedHashMap<String, String>());
	}

	public FailField(String name, String value, String... options) {
		this(name, value);
		if (options.length % 2 != 0) {
			throw new IllegalArgumentException("Please give the field options as key,value,key,value,..");
		}
		for (int i=0 ; i<options.length ; i += 2) {
			setOption(options[i], options[i+1]);
		}		
	}
	
	public void setOption(String key, String val) {
		getOptions().put(key, val);
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> getOptions() {
		return (Map<String,String>)this.get(2);
	}

	
}
