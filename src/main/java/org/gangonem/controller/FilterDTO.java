package org.gangonem.controller;

import java.util.Map;

import org.gangonem.model.Division;
import org.gangonem.model.EventType;
import org.gangonem.model.Gender;
import org.gangonem.model.HBCUState;
import org.gangonem.model.Mark;
import org.gangonem.model.PublicPrivateState;

//FilterDTO.java
public class FilterDTO {
	Division division;
	String conference;
	String state;
	String town;
	PublicPrivateState publicOrPrivate;
	HBCUState hbcuOrNot;

	Gender gender;

	Map<EventType, String> userInput;

	public Division getDivision() {
		return division;
	}

	public void setDivision(Division division) {
		this.division = division;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public PublicPrivateState getPublicOrPrivate() {
		return publicOrPrivate;
	}

	public void setPublicOrPrivate(PublicPrivateState publicOrPrivate) {
		this.publicOrPrivate = publicOrPrivate;
	}

	public HBCUState getHbcuOrNot() {
		return hbcuOrNot;
	}

	public void setHbcuOrNot(HBCUState hbcuOrNot) {
		this.hbcuOrNot = hbcuOrNot;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Map<EventType, String> getUserInput() {
		return userInput;
	}

	public void setUserInput(Map<EventType, String> userInput) {
		this.userInput = userInput;
	}

}