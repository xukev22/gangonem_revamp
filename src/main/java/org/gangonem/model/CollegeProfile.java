package org.gangonem.model;

public class CollegeProfile {

	Essentials essentials;
	StandardsSet standardsSet;

	public CollegeProfile(Essentials essentials, StandardsSet standardsSet) {
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
