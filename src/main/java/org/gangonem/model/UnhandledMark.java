package org.gangonem.model;

public class UnhandledMark implements Mark {
	static String unhandled_mark = "unhandled_mark";

	@Override
	public int compare(Mark other) {
		return other.compareUnhandledMark(this);
	}

	@Override
	public int compareTime(Time other) {
		return Integer.MAX_VALUE;
	}

	@Override
	public int compareDistance(Distance other) {
		return Integer.MAX_VALUE;
	}

	@Override
	public int comparePoints(Points other) {
		return Integer.MAX_VALUE;
	}

	public int compareUnhandledMark(UnhandledMark other) {
		return 0;
	}

}
