package org.gangonem.model;

public interface Mark {
	// returns positive if the first mark is a PR compared to the other mark
	// returns 0 if PR is a tie
	// returns negative if the other mark is a PR compared to the first mark
	int compare(Mark other);

	// double dispatch compare helper
	int compareTime(Time other);

	// double dispatch compare helper
	int compareDistance(Distance other);

	// double dispatch compare helper
	int comparePoints(Points other);

	// double dispatch compare helper
	int compareUnhandledMark(UnhandledMark other);
}
