package org.gangonem.model;

import java.util.List;

public class CleanEvent {
	EventType event;
	List<CleanCompetitor> competitors;

	public CleanEvent(EventType event, List<CleanCompetitor> competitors) {
		this.event = event;
		this.competitors = competitors;
	}

	public EventType getEvent() {
		return event;
	}

	public void setEvent(EventType event) {
		this.event = event;
	}

	public List<CleanCompetitor> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(List<CleanCompetitor> competitors) {
		this.competitors = competitors;
	}

}
