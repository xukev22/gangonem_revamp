package org.gangonem.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = Distance.class, name = "distance"),
		@JsonSubTypes.Type(value = Time.class, name = "time"),
		@JsonSubTypes.Type(value = Points.class, name = "points"),
		@JsonSubTypes.Type(value = UnhandledMark.class, name = "unhandled_mark") })

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
