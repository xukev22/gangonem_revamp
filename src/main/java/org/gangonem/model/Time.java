package org.gangonem.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Time implements Mark {

	int minute;
	int second;
	String fracSecond; // should be "00" to "99"

	@JsonCreator
	public Time(@JsonProperty("minute") int minute, @JsonProperty("second") int second,
			@JsonProperty("fracSecond") String fracSecond) {
		this.minute = minute;
		this.second = second;
		if (fracSecond.length() < 2) {
			this.fracSecond = "0" + fracSecond;
		} else {
			this.fracSecond = fracSecond;
		}
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public String getFracSecond() {
		return this.fracSecond;
	}

	public void setFracSecond(String fracSecond) {
		this.fracSecond = fracSecond;
	}

	@Override
	public int compare(Mark other) {
		return other.compareTime(this);
	}

	@Override
	public int compareTime(Time other) {
		int latterTimeInSeconds = this.minute * 60 * 100 + this.second * 100 + Integer.parseInt(this.fracSecond);
		int formerTimeInSeconds = other.minute * 60 * 100 + other.second * 100 + Integer.parseInt(other.fracSecond);

		return latterTimeInSeconds - formerTimeInSeconds;
	}

	@Override
	public int compareDistance(Distance other) {
		throw new RuntimeException("Cannot compare Time and Distance");
	}

	@Override
	public int comparePoints(Points other) {
		throw new RuntimeException("Cannot compare Time and Points");
	}

	@Override
	public int compareUnhandledMark(UnhandledMark other) {
		return Integer.MIN_VALUE;
	}

}
