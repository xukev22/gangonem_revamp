package org.gangonem.cleaning;

import java.util.ArrayList;
import java.util.List;

import org.gangonem.model.CleanCompetitor;
import org.gangonem.model.CleanEvent;
import org.gangonem.model.CleanedData;
import org.gangonem.model.Competitor;
import org.gangonem.model.Event;
import org.gangonem.model.Meet;
import org.gangonem.model.EventType;

public class DataCleaner {
	// Returns the events cleaned up
	public CleanedData cleanMeets(List<Meet> meets) {

		// list of clean events
		List<CleanEvent> allCleanEvents = new ArrayList<>();

		// return type
		CleanedData cleanedData = new CleanedData(allCleanEvents);

		// TODO:
		// try to recover more with pre-cleaner (josh)
		// remember purpose statements and comments if func is complex, private static
		// helpers

		// iterate over meet
		for (Meet meet : meets) {
			// iterate over an event
			for (Event event : meet.getEvents()) {

				// for every event, we create a list of clean competitors for it
				List<CleanCompetitor> allCleanCompetitors = new ArrayList<>();

				// convert the string to an eventtype enum with our helper
				EventType calculatedEventType = this.convertStringToEventType(event.getName().toLowerCase());

				// construct the clean event to be added, we will modify allCleanCompetitors now
				CleanEvent cleanEvent = new CleanEvent(calculatedEventType, allCleanCompetitors);

				// iterate over the competitors in this event
				for (Competitor competitor : event.getCompetitors()) {
					if (meet.getSport().equals("Cross Country")) {
						// XC case, we assign accordingly
						// name: f2
						// aff: f4
						// mark: f6
						CleanCompetitor cleanCompetitor = new CleanCompetitor(competitor.getField2(),
								competitor.getField4(), competitor.getField6());
						allCleanCompetitors.add(cleanCompetitor);
					} else if (meet.getSport().equals("Track & Field")) {
						// TF case, we assign accordingly
						// name: f2
						// aff: f4
						// mark: f5
						CleanCompetitor cleanCompetitor = new CleanCompetitor(competitor.getField2(),
								competitor.getField4(), competitor.getField5());
						allCleanCompetitors.add(cleanCompetitor);
					} else {
						// weird sport field, we want to catch
						throw new RuntimeException("Sport is invalid while cleaning data!");
					}
				}

				// add final event to list
				allCleanEvents.add(cleanEvent);
			}
		}

		// return packaged data
		return cleanedData;
	}

	// Returns the corresponding event type for our string
	private EventType convertStringToEventType(String eventName) {
		if (eventName.contains("4 x") || eventName.contains("4x") || eventName.contains("relay")
				|| eventName.contains("team r") || eventName.contains("medley")) {
			return EventType.UNHANDLED;
		} else if ((eventName.contains("(3k)") || eventName.contains("3000")) && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_3000;
		} else if (eventName.contains("(5k)") && eventName.contains("women")) {
			return EventType.FEMALE_XC_5000;
		} else if (eventName.contains("(6k)") && eventName.contains("women")) {
			return EventType.FEMALE_XC_6000;
		} else if (eventName.contains("(8k)") && eventName.contains("women")) {
			return EventType.FEMALE_XC_8000;
		} else if (eventName.contains("(10k)") && eventName.contains("women")) {
			return EventType.FEMALE_XC_10000;
		} else if (eventName.contains("mile") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_MILE;
		} else if (eventName.contains("60 h") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_60_HURDLE;
		} else if (eventName.contains("100 h") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_STRAIGHT_HURDLE;
		} else if (eventName.contains("400 h") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_LONG_HURDLE;
		} else if (eventName.contains("3000 s") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_3000_SC;
		} else if ((eventName.contains("10,000") || eventName.contains("10000")) && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_10000;
		} else if (eventName.contains("5000") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_5000;
		} else if (eventName.contains("3000") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_3000;
		} else if (eventName.contains("3200") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_3200;
		} else if (eventName.contains("1600") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_1600;

		} else if (eventName.contains("1500") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_1500;
		} else if (eventName.contains("1000") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_1000;
		} else if (eventName.contains("800") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_800;
		} else if (eventName.contains("600") && !eventName.contains("600g") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_600;
		} else if (eventName.contains("500") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_500;
		} else if (eventName.contains("400") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_400;
		} else if (eventName.contains("300") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_300;
		} else if (eventName.contains("200") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_200;
		} else if (eventName.contains("100") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_100;
		} else if (eventName.contains("60") && eventName.contains("women")) {
			return EventType.FEMALE_TRACK_60;
		} else if (eventName.contains("high") && eventName.contains("women")) {
			return EventType.FEMALE_FIELD_HIGH_JUMP;
		} else if (eventName.contains("long") && eventName.contains("women")) {
			return EventType.FEMALE_FIELD_LONG_JUMP;
		} else if (eventName.contains("triple") && eventName.contains("women")) {
			return EventType.FEMALE_FIELD_TRIPLE_JUMP;
		} else if (eventName.contains("pole") && eventName.contains("women")) {
			return EventType.FEMALE_FIELD_POLE_VAULT;
		} else if (eventName.contains("shot") && eventName.contains("women")) {
			return EventType.FEMALE_FIELD_SHOT_PUT;
		} else if (eventName.contains("discus") && eventName.contains("women")) {
			return EventType.FEMALE_FIELD_DISCUS;
		} else if (eventName.contains("jav") && eventName.contains("women")) {
			return EventType.FEMALE_FIELD_JAVELIN;
		} else if (eventName.contains("hammer") && eventName.contains("women")) {
			return EventType.FEMALE_FIELD_HAMMER;
		} else if (eventName.contains("weight") && eventName.contains("women")) {
			return EventType.FEMALE_FIELD_WEIGHT;
		} else if (eventName.contains("decath") && eventName.contains("women")) {
			return EventType.FEMALE_MULTI_DEC;
		} else if (eventName.contains("hept") && eventName.contains("women")) {
			return EventType.FEMALE_MULTI_HEP;
		} else if (eventName.contains("pent") && eventName.contains("women")) {
			return EventType.FEMALE_MULTI_PENTA;
		}

		else if ((eventName.contains("(3k)") || eventName.contains("3000")) && eventName.contains("men")) {
			return EventType.MALE_TRACK_3000;
		} else if (eventName.contains("(5k)") && eventName.contains("men")) {
			return EventType.MALE_XC_5000;
		} else if (eventName.contains("(6k)") && eventName.contains("men")) {
			return EventType.MALE_XC_6000;
		} else if (eventName.contains("(8k)") && eventName.contains("men")) {
			return EventType.MALE_XC_8000;
		} else if (eventName.contains("(10k)") && eventName.contains("men")) {
			return EventType.MALE_XC_10000;
		} else if (eventName.contains("mile") && eventName.contains("men")) {
			return EventType.MALE_TRACK_MILE;
		} else if (eventName.contains("60 h") && eventName.contains("men")) {
			return EventType.MALE_TRACK_60_HURDLE;
		} else if (eventName.contains("110 h") && eventName.contains("men")) {
			return EventType.MALE_TRACK_STRAIGHT_HURDLE;
		} else if (eventName.contains("400 h") && eventName.contains("men")) {
			return EventType.MALE_TRACK_LONG_HURDLE;
		} else if (eventName.contains("3000 s") && eventName.contains("men")) {
			return EventType.MALE_TRACK_3000_SC;
		} else if ((eventName.contains("10,000") || eventName.contains("10000")) && eventName.contains("men")) {
			return EventType.MALE_TRACK_10000;
		} else if (eventName.contains("5000") && eventName.contains("men")) {
			return EventType.MALE_TRACK_5000;
		} else if (eventName.contains("3000") && eventName.contains("men")) {
			return EventType.MALE_TRACK_3000;
		} else if (eventName.contains("3200") && eventName.contains("men")) {
			return EventType.MALE_TRACK_3200;
		} else if (eventName.contains("1600") && eventName.contains("men")) {
			return EventType.MALE_TRACK_1600;

		} else if (eventName.contains("1500") && eventName.contains("men")) {
			return EventType.MALE_TRACK_1500;
		} else if (eventName.contains("1000") && eventName.contains("men")) {
			return EventType.MALE_TRACK_1000;
		} else if (eventName.contains("800") && !eventName.contains("800g") && eventName.contains("men")) {
			return EventType.MALE_TRACK_800;
		} else if (eventName.contains("600") && eventName.contains("men")) {
			return EventType.MALE_TRACK_600;
		} else if (eventName.contains("500") && eventName.contains("men")) {
			return EventType.MALE_TRACK_500;
		} else if (eventName.contains("400") && eventName.contains("men")) {
			return EventType.MALE_TRACK_400;
		} else if (eventName.contains("300") && eventName.contains("men")) {
			return EventType.MALE_TRACK_300;
		} else if (eventName.contains("200") && eventName.contains("men")) {
			return EventType.MALE_TRACK_200;
		} else if (eventName.contains("100") && eventName.contains("men")) {
			return EventType.MALE_TRACK_100;
		} else if (eventName.contains("60") && eventName.contains("men")) {
			return EventType.MALE_TRACK_60;
		} else if (eventName.contains("high") && eventName.contains("men")) {
			return EventType.MALE_FIELD_HIGH_JUMP;
		} else if (eventName.contains("long") && eventName.contains("men")) {
			return EventType.MALE_FIELD_LONG_JUMP;
		} else if (eventName.contains("triple") && eventName.contains("men")) {
			return EventType.MALE_FIELD_TRIPLE_JUMP;
		} else if (eventName.contains("pole") && eventName.contains("men")) {
			return EventType.MALE_FIELD_POLE_VAULT;
		} else if (eventName.contains("shot") && eventName.contains("men")) {
			return EventType.MALE_FIELD_SHOT_PUT;
		} else if (eventName.contains("discus") && eventName.contains("men")) {
			return EventType.MALE_FIELD_DISCUS;
		} else if (eventName.contains("jav") && eventName.contains("men")) {
			return EventType.MALE_FIELD_JAVELIN;
		} else if (eventName.contains("hammer") && eventName.contains("men")) {
			return EventType.MALE_FIELD_HAMMER;
		} else if (eventName.contains("weight") && eventName.contains("men")) {
			return EventType.MALE_FIELD_WEIGHT;
		} else if (eventName.contains("decath") && eventName.contains("men")) {
			return EventType.MALE_MULTI_DEC;
		} else if (eventName.contains("hept") && eventName.contains("men")) {
			return EventType.MALE_MULTI_HEP;
		} else if (eventName.contains("pent") && eventName.contains("men")) {
			return EventType.MALE_MULTI_PENTA;
		} else {
			return EventType.UNHANDLED;
		}
	}
}
