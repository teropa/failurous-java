package com.failurous;

import java.util.ArrayList;
import java.util.List;

public class FailNotification {

	private String title;
	private String location;
	private boolean useTitleInChecksum = false;
	private boolean useLocationInChecksum = true;
	private List<FailNotificationSection> data = new ArrayList<FailNotificationSection>();

	public FailNotification(String title, String location) {
		this.title = title;
		this.location = location;
	}
	
	public void addSection(FailNotificationSection section) {
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

	public List<FailNotificationSection> getData() {
		return data;
	}
	
}
