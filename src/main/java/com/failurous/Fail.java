package com.failurous;

import java.util.ArrayList;
import java.util.List;

public class Fail {

	private String title;
	private String location;
	private boolean useTitleInChecksum = false;
	private boolean useLocationInChecksum = true;
	private List<FailSection> data = new ArrayList<FailSection>();

	public Fail(String title, String location) {
		this.title = title;
		this.location = location;
	}
	
	public void addSection(FailSection section) {
		data.add(section);
	}
	
	public String getTitle() {
		return title;
	}

	public String getLocation() {
		return location;
	}

	public boolean isUseTitleInChecksum() {
		return useTitleInChecksum;
	}
	
	public void setUseTitleInChecksum(boolean useTitleInChecksum) {
		this.useTitleInChecksum = useTitleInChecksum;
	}

	public boolean isUseLocationInChecksum() {
		return useLocationInChecksum;
	}

	public void setUseLocationInChecksum(boolean useLocationInChecksum) {
		this.useLocationInChecksum = useLocationInChecksum;
	}

	public List<FailSection> getData() {
		return data;
	}
	
}
