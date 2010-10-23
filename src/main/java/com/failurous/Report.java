package com.failurous;

import java.util.ArrayList;
import java.util.List;

public class Report {

	private String title;
	private String location;
	private List<ReportSection> data = new ArrayList<ReportSection>();

	public Report(String title, String location) {
		this.title = title;
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public String getLocation() {
		return location;
	}

	public List<ReportSection> getData() {
		return data;
	}


	public void addSection(ReportSection section) {
		data.add(section);
	}
	
}
