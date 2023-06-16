package org.gangonem.model;

public class CleanCompetitor {
	String name;
	String affiliation;
	String mark;
	
	public CleanCompetitor(String name, String affiliation, String mark) {
		this.name = name;
		this.affiliation = affiliation;
		this.mark = mark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}
	
	
}
