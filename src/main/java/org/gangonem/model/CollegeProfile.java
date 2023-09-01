package org.gangonem.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CollegeProfile {

	Essentials essentials;
	StandardsSet standardsSet;
	EssentialsBonus eb;

	@JsonCreator
	public CollegeProfile(@JsonProperty("essentials") Essentials essentials,
			@JsonProperty("standardsSet") StandardsSet standardsSet, @JsonProperty("eb") EssentialsBonus eb) {
		this.essentials = essentials;
		this.standardsSet = standardsSet;
		this.eb = eb;
	}

	public CollegeProfile() {
	}

	public Essentials getEssentials() {
		return essentials;
	}

	public void setEssentials(Essentials essentials) {
		this.essentials = essentials;
	}

	public StandardsSet getStandardsSet() {
		return standardsSet;
	}

	public void setStandardsSet(StandardsSet standardsSet) {
		this.standardsSet = standardsSet;
	}

	public EssentialsBonus getEb() {
		return eb;
	}

	public void setEb(EssentialsBonus eb) {
		this.eb = eb;
	}

	public CollegeProfileSummary toSummary() {
		return new CollegeProfileSummary(this.getEssentials().getName(), this.getEssentials().getState(),
				this.getEssentials().getDivision());
	}

}
