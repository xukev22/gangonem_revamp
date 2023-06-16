package org.gangonem.model;

import java.util.List;

public class CleanedData {
	List<CleanEvent> events;

	public CleanedData(List<CleanEvent> events) {
		this.events = events;
	}

	public List<CleanEvent> getEvents() {
		return this.events;
	}
}
