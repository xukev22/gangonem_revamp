package org.gangonem.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CollegeProfile {

	Essentials essentials;
	StandardsSet standardsSet;
 
	@JsonCreator
	public CollegeProfile(@JsonProperty("essentials") Essentials essentials, @JsonProperty("standardsSet") StandardsSet standardsSet) {
		this.essentials = essentials;
		this.standardsSet = standardsSet;
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

}
