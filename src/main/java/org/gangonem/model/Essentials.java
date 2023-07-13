package org.gangonem.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Essentials {

	String name;
	Division division;
	String conference;
	String state;
	String mainWebsiteURL;
	String athleticWebsiteURL;
	PublicPrivateState publicOrPrivate;
	HBCUState hbcuOrNot;

	@JsonCreator
	public Essentials(@JsonProperty("name") String name, @JsonProperty("division") Division division,
			@JsonProperty("conference") String conference, @JsonProperty("state") String state,
			@JsonProperty("mainWebsiteURL") String mainWebsiteURL,
			@JsonProperty("athleticWebsiteURL") String athleticWebsiteURL,
			@JsonProperty("publicOrPrivate") PublicPrivateState publicOrPrivate,
			@JsonProperty("hbcuOrNot") HBCUState hbcuOrNot) {
		this.name = name;
		this.division = division;
		this.conference = conference;
		this.state = state;
		this.mainWebsiteURL = mainWebsiteURL;
		this.athleticWebsiteURL = athleticWebsiteURL;
		this.publicOrPrivate = publicOrPrivate;
		this.hbcuOrNot = hbcuOrNot;
	}
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Essentials that = (Essentials) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

	public Essentials() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getMainWebsiteURL() {
		return mainWebsiteURL;
	}

	public void setMainWebsiteURL(String mainWebsiteURL) {
		this.mainWebsiteURL = mainWebsiteURL;
	}

	public String getAthleticWebsiteURL() {
		return athleticWebsiteURL;
	}

	public void setAthleticWebsiteURL(String athleticWebsiteURL) {
		this.athleticWebsiteURL = athleticWebsiteURL;
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

}
