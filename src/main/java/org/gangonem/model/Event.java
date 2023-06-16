package org.gangonem.model;

import java.util.List;

public class Event {
	private String name;
	private List<Competitor> competitors;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Competitor> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(List<Competitor> competitors) {
		this.competitors = competitors;
	}

	public void debugPrint() {
		System.out.println("========== NEW EVENT ==========");
		System.out.println(this.name + " is the name of the event");
		if (this.competitors == null) {
			System.out.println("no competitors for this instance");
			return;
		}
		for (Competitor competitor : this.competitors) {
			competitor.debugCompetitors();
		}
	}
}
