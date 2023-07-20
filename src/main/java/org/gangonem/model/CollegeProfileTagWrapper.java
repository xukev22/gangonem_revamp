package org.gangonem.model;

import java.util.AbstractMap;
import java.util.List;
import java.util.Objects;

public class CollegeProfileTagWrapper {
	CollegeProfile collegeProfile;
	List<AbstractMap.SimpleEntry<EventType, Level>> tags;

	public CollegeProfileTagWrapper(CollegeProfile collegeProfile,
			List<AbstractMap.SimpleEntry<EventType, Level>> tags) {
		this.collegeProfile = collegeProfile;
		this.tags = tags;
	}

	public CollegeProfileTagWrapper(CollegeProfile collegeProfile) {
		this.collegeProfile = collegeProfile;
		this.tags = null;
	}

	public CollegeProfile getCollegeProfile() {
		return collegeProfile;
	}

	public void setCollegeProfile(CollegeProfile collegeProfile) {
		this.collegeProfile = collegeProfile;
	}

	public List<AbstractMap.SimpleEntry<EventType, Level>> getTags() {
		return tags;
	}

	public void setTags(List<AbstractMap.SimpleEntry<EventType, Level>> tags) {
		this.tags = tags;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		CollegeProfileTagWrapper other = (CollegeProfileTagWrapper) obj;

		if (collegeProfile == null && other.collegeProfile == null) {
			return false;
		} else if (collegeProfile == null || other.collegeProfile == null) {
			return false;
		}

		String thisName = collegeProfile.getEssentials() != null ? collegeProfile.getEssentials().getName() : null;
		String otherName = other.collegeProfile.getEssentials() != null ? other.collegeProfile.getEssentials().getName()
				: null;
		return Objects.equals(thisName, otherName);
	}

	@Override
	public int hashCode() {
		if (collegeProfile != null && collegeProfile.getEssentials() != null) {
			return Objects.hash(collegeProfile.getEssentials().getName());
		}
		return Objects.hash(collegeProfile);
	}

}
