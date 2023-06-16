package org.gangonem.model;

import java.math.BigDecimal;

public class Distance implements Mark {
	// we want this to always be rounded to nearest hundredth
	BigDecimal meters;

	public Distance(BigDecimal meters) {
		this.meters = meters;
	}

	public BigDecimal getMeters() {
		return meters;
	}

	public void setMeters(BigDecimal meters) {
		this.meters = meters;
	}

	@Override
	public int compare(Mark other) {
		// TODO Auto-generated method stub
		return other.compareDistance(this);
	}

	@Override
	public int compareTime(Time other) {
		throw new RuntimeException("Cannot compare Distance and Time");
	}

	@Override
	public int compareDistance(Distance other) {
		return other.meters.compareTo(this.meters);
	}

	@Override
	public int comparePoints(Points other) {
		throw new RuntimeException("Cannot compare Distance and Points");
	}

	@Override
	public int compareUnhandledMark(UnhandledMark other) {
		return Integer.MIN_VALUE;
	}
}
