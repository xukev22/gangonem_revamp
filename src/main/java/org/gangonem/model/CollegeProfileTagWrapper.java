package org.gangonem.model;

import java.util.AbstractMap;
import java.util.List;
import java.util.Objects;

public class CollegeProfileTagWrapper {
	CollegeProfileSummary collegeProfileSummary;
	List<AbstractMap.SimpleEntry<EventType, Level>> tags;

	public CollegeProfileTagWrapper(CollegeProfileSummary collegeProfileSummary,
			List<AbstractMap.SimpleEntry<EventType, Level>> tags) {
		this.collegeProfileSummary = collegeProfileSummary;
		this.tags = tags;
	}

	public CollegeProfileTagWrapper(CollegeProfileSummary collegeProfileSummary) {
		this.collegeProfileSummary = collegeProfileSummary;
		this.tags = null;
	}

	public CollegeProfileSummary getCollegeProfileSummary() {
		return collegeProfileSummary;
	}

	public void setCollegeProfileSummary(CollegeProfileSummary collegeProfileSummary) {
		this.collegeProfileSummary = collegeProfileSummary;
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

		if (collegeProfileSummary == null || other.collegeProfileSummary == null) {
			return false;
		}

		String thisName = collegeProfileSummary.getName();
		String otherName = other.collegeProfileSummary.getName();
		return Objects.equals(thisName, otherName);
	}

	@Override
	public int hashCode() {
		if (collegeProfileSummary != null && collegeProfileSummary.getName() != null) {
			return Objects.hash(collegeProfileSummary.getName());
		}
		return Objects.hash(collegeProfileSummary);
	}

}
