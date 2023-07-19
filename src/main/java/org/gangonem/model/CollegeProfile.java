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

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CollegeProfile other = (CollegeProfile) obj;
		return essentials != null && other.essentials != null
				&& essentials.getName().equals(other.essentials.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(essentials != null ? essentials.getName() : null);
	}

}
