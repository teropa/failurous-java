package com.failurous.fail;

import java.util.ArrayList;
import java.util.List;

public class Fail {

	private String title;
	private String location;
	private boolean useTitleInChecksum = false;
	private boolean useLocationInChecksum = true;
	private List<FailSection> data = new ArrayList<FailSection>();

	public Fail(String title) {
		this.title = title;
	}
	
	public Fail(String title, String location) {
		this.title = title;
		this.location = location;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public void addSection(FailSection section) {
		data.add(section);
	}
	
	public FailSection addSection(String name) {
		FailSection section = new FailSection(name);
		addSection(section);
		return section;
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
