package org.gangonem.model;

import java.util.List;

public class CollegeProfileTagWrapper {
	CollegeProfile collegeProfile;
	List<EventType> tags;

	public CollegeProfileTagWrapper(CollegeProfile collegeProfile, List<EventType> tags) {
		this.collegeProfile = collegeProfile;
		this.tags = tags;
	}

	public CollegeProfile getCollegeProfile() {
		return collegeProfile;
	}

	public void setCollegeProfile(CollegeProfile collegeProfile) {
		this.collegeProfile = collegeProfile;
	}

	public List<EventType> getTags() {
		return tags;
	}

	public void setTags(List<EventType> tags) {
		this.tags = tags;
	}

}
