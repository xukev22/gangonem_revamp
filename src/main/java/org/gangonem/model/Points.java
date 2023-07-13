package org.gangonem.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Points implements Mark {
	
	@JsonCreator
	public Points(@JsonProperty("points") int points) {
		if (points < 0) {
			throw new IllegalArgumentException("Points was less than 0!");
		}
		this.points = points;
	}

	int points;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public int compare(Mark other) {
		return other.comparePoints(this);
	}

	@Override
	public int compareTime(Time other) {
		throw new RuntimeException("Cannot compare Points and Time");
	}

	@Override
	public int compareDistance(Distance other) {
		throw new RuntimeException("Cannot compare Points and Distance");
	}

	@Override
	public int comparePoints(Points other) {
		return other.points - this.points;
	}

	@Override
	public int compareUnhandledMark(UnhandledMark other) {
		return Integer.MIN_VALUE;
	}
}
