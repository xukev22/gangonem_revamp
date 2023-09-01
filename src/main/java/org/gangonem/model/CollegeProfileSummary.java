package org.gangonem.model;

public class CollegeProfileSummary {

	String name;
	String state;
	Division division;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public CollegeProfileSummary(String name, String state, Division division) {
		this.name = name;
		this.state = state;
		this.division = division;
	}

}
