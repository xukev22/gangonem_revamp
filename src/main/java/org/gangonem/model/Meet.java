package org.gangonem.model;

import java.util.List;

public class Meet {
	private String date;
	private String meet;
	private String sport;
	private String stateOrProv;
	private List<Event> events;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMeet() {
		return meet;
	}

	public void setMeet(String meet) {
		this.meet = meet;
	}

	public String getSport() {
		return sport;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getStateOrProv() {
		return stateOrProv;
	}

	public void setStateOrProv(String stateOrProv) {
		this.stateOrProv = stateOrProv;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public void debugPrint() {
		System.out.println("========== NEW MEET ==========");
		System.out.println(this.date + " is the date");
		System.out.println(this.meet + " is the meet");
		System.out.println(this.sport + " is the sport");
		System.out.println(this.stateOrProv + " is the stateOrProv");
		if (this.events == null) {
			System.out.println("no events field for this instance");
			return;
		}
		for (Event event : this.events) {
			event.debugPrint();
		}
	}
}
