package org.gangonem.model;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class CollegeProfileTagWrapper {
	CollegeProfile collegeProfile;
	List<Pair<EventType, Level>> tags;
	MissingInfo missingInfo;

	public CollegeProfileTagWrapper(CollegeProfile collegeProfile, List<Pair<EventType, Level>> tags,
			MissingInfo missingInfo) {
		this.collegeProfile = collegeProfile;
		this.tags = tags;
		this.missingInfo = missingInfo;
	}

	public CollegeProfile getCollegeProfile() {
		return collegeProfile;
	}

	public void setCollegeProfile(CollegeProfile collegeProfile) {
		this.collegeProfile = collegeProfile;
	}

	public List<Pair<EventType, Level>> getTags() {
		return tags;
	}

	public void setTags(List<Pair<EventType, Level>> tags) {
		this.tags = tags;
	}

	public MissingInfo getMissingInfo() {
		return missingInfo;
	}

	public void setMissingInfo(MissingInfo missingInfo) {
		this.missingInfo = missingInfo;
	}

}
