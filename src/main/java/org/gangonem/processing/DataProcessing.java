package org.gangonem.processing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.gangonem.model.CleanCompetitor;
import org.gangonem.model.CleanEvent;
import org.gangonem.model.CleanedData;
import org.gangonem.model.CollegeProfile;
import org.gangonem.model.Essentials;
import org.gangonem.model.EventType;
import org.gangonem.model.Mark;
import org.gangonem.model.StandardsSet;
import org.gangonem.utils.MarkConverter;

public class DataProcessing {
	// Constructs the college profile given the cleaned data and list of essentials,
	// as well as mapping from ncaa to tfrrs
	public List<CollegeProfile> createCollegeProfiles(CleanedData cleanedData, List<Essentials> essentials,
			Map<String, String> ncaaToTfrrs) {

		// get big map
		Map<String, Map<EventType, Map<String, Mark>>> tffrsCollegeNameToEventsMap = this
				.tffrsCollegeNameToEventsMapCreator(cleanedData, ncaaToTfrrs);

		return essentials.stream().map(essentialInfo -> {
			// Multiline lambda function

			StandardsSet standardsSet = createSingleStandardSet(essentialInfo.getName(), tffrsCollegeNameToEventsMap,
					ncaaToTfrrs);

			CollegeProfile singleCollegeProfile = new CollegeProfile(essentialInfo, standardsSet, null);
			return singleCollegeProfile;
		}).collect(Collectors.toList());

	}

	// Creates the single standard set given a target name and the name mapping and
	// tfrrs data
	private StandardsSet createSingleStandardSet(String ncaaName,
			Map<String, Map<EventType, Map<String, Mark>>> tffrsCollegeNameToEventsMap,
			Map<String, String> ncaaToTfrrs) {

		if (ncaaToTfrrs.get(ncaaName) == null) {
			throw new RuntimeException(ncaaName + " not an existing key in the ncaaToTfrrs map");
		}

		if (ncaaToTfrrs.get(ncaaName).equals("!NONE")) {
			System.out.println(ncaaName + " has no tfrrs profile");
			return null;
		}

		if (tffrsCollegeNameToEventsMap.get(ncaaToTfrrs.get(ncaaName)) == null) {
			// not enough data
			System.out.println(ncaaName + " MAPPED NAME NOT IN tffrsCollegeNameToEventsMap!!!");
			return null;
		}

		return new StandardsSet(tffrsCollegeNameToEventsMap.get(ncaaToTfrrs.get(ncaaName)));
	}

	// constructs the mapping of tfrrs data based on the cleaned data
	private Map<String, Map<EventType, Map<String, Mark>>> tffrsCollegeNameToEventsMapCreator(CleanedData cleanedData,
			Map<String, String> ncaaToTfrrs) {
		Map<String, Map<EventType, Map<String, Mark>>> tffrsCollegeNameToEventsMap = new HashMap<>();

		// iterate over the clean events
		for (CleanEvent cleanEvent : cleanedData.getEvents()) {

			// get the event type
			EventType eventType = cleanEvent.getEvent();

			// if unhandled, skip event
			if (eventType.equals(EventType.UNHANDLED)) {
				System.out.println(cleanEvent.getCompetitors());
				continue;
			}

			// iterate over the clean competitors
			for (CleanCompetitor cleanCompetitor : cleanEvent.getCompetitors()) {

				// get comp affiliation, name, and convert string to a mark
				String competitorAffiliation = cleanCompetitor.getAffiliation();
				String competitorName = cleanCompetitor.getName();
				Mark competitorMark = MarkConverter.convertToMark(cleanCompetitor.getMark());

				// if the comp affiliation is not in ncaa value list, then skip it and print it
				if (!ncaaToTfrrs.containsValue(competitorAffiliation)) {
					System.out.println(competitorAffiliation);
					continue;
				}

				// if never seen this affiliation before, initialize a map
				if (tffrsCollegeNameToEventsMap.get(competitorAffiliation) == null) {
					tffrsCollegeNameToEventsMap.put(competitorAffiliation, new HashMap<>());
				}

				// if never seen this affiliation's event before, initialize a map
				if (tffrsCollegeNameToEventsMap.get(competitorAffiliation).get(eventType) == null) {
					tffrsCollegeNameToEventsMap.get(competitorAffiliation).put(eventType, new HashMap<>());
				}

				if (tffrsCollegeNameToEventsMap.get(competitorAffiliation).get(eventType).get(competitorName) == null) {
					// if never seen this affiliation's event's person before
					// enter the competitor -> mark pair into the map
					tffrsCollegeNameToEventsMap.get(competitorAffiliation).get(eventType).put(competitorName,
							competitorMark);
				} else {
					// attempt replace when new mark is PR
					if (competitorMark.compare(tffrsCollegeNameToEventsMap.get(competitorAffiliation).get(eventType)
							.get(competitorName)) > 0) {
						tffrsCollegeNameToEventsMap.get(competitorAffiliation).get(eventType).put(competitorName,
								competitorMark);
					}
				}

			}
		}

		return tffrsCollegeNameToEventsMap;
	}

}
