package org.gangonem.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.gangonem.controller.FilterDTO;
import org.gangonem.model.CollegeProfile;
import org.gangonem.model.CollegeProfileSummary;
import org.gangonem.model.CollegeProfileTagWrapper;
import org.gangonem.model.Division;
import org.gangonem.model.EssentialsBonus;
import org.gangonem.model.EventType;
import org.gangonem.model.Gender;
import org.gangonem.model.HBCUState;
import org.gangonem.model.Level;
import org.gangonem.model.Mark;
import org.gangonem.model.PublicPrivateState;
import org.gangonem.model.Standards;
import org.gangonem.model.StandardsSet;
import org.gangonem.service.RecruitingService;
import org.gangonem.utils.GeneralUtils;
import org.gangonem.utils.MarkConverter;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;

@Service
public class RecruitingServiceImpl implements RecruitingService {

	private List<CollegeProfile> collegeProfiles;
	private List<EssentialsBonus> moreInfoCollegeProfiles;

	public RecruitingServiceImpl() throws StreamReadException, IOException {
		loadCollegeProfileData();
		loadEssentialsBonusData();
	}

	@Override
	public Optional<CollegeProfile> getDetailsForCollegeByName(String collegeName) {
		for (CollegeProfile cp : this.collegeProfiles) {
			if (cp.getEssentials().getName().equals(collegeName)) {
				return Optional.of(cp);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<String> getListOfAllCollegeNames() {
		List<String> returnList = new ArrayList<>();
		for (CollegeProfile cp : this.collegeProfiles) {
			returnList.add(cp.getEssentials().getName());
		}

		return returnList;
	}

	@Override
	public Optional<EssentialsBonus> getMoreDetailsForCollegeByName(String collegeName) {
		for (EssentialsBonus eb : this.moreInfoCollegeProfiles) {
			if (eb.getName().equals(collegeName)) {
				return Optional.of(eb);
			}
		}
		return Optional.empty();
	}

	private void loadCollegeProfileData() throws StreamReadException, IOException {
		File collegeProfileFile = new File("./data/xc-track-data.json");
		collegeProfiles = new GeneralUtils().fileReaderCollegeProfileData(collegeProfileFile);
	}

	private void loadEssentialsBonusData() throws StreamReadException, IOException {
		File essentialsBonusFile = new File("./data/essentialsBonus.json");
		moreInfoCollegeProfiles = new GeneralUtils().fileReaderEssentialsBonusData(essentialsBonusFile);
	}

	@Override
	public List<CollegeProfileTagWrapper> getMatchingColleges(boolean hasStaticFilters, boolean hasDynamicFilters,
			FilterDTO filterDTO) {
		Stream<CollegeProfile> collegeProfileStream = collegeProfiles.stream();

		if (hasStaticFilters) {
			collegeProfileStream = applyStaticFilters(collegeProfileStream, filterDTO);
		}

		List<CollegeProfile> staticFilteredColleges = collegeProfileStream.collect(Collectors.toList());

		List<CollegeProfileTagWrapper> staticFilteredCPWrapper = staticFilteredColleges.stream()
				.map(cp -> new CollegeProfileTagWrapper(cp.toSummary())).collect(Collectors.toList());

		List<CollegeProfileTagWrapper> dynamicFilteredCPWrapper = new ArrayList<>();

		if (hasDynamicFilters) {
			dynamicFilteredCPWrapper = applyDynamicFilters(filterDTO);
		} else {
			return staticFilteredCPWrapper;
		}

		Set<CollegeProfileTagWrapper> intersectionSet = new HashSet<>(dynamicFilteredCPWrapper);
		intersectionSet.retainAll(staticFilteredCPWrapper);

		return new ArrayList<>(intersectionSet);

	}

	private Stream<CollegeProfile> applyStaticFilters(Stream<CollegeProfile> collegeProfileStream,
			FilterDTO filterDTO) {
		Division divisionFilter = filterDTO.getDivision();
		String conferenceFilter = filterDTO.getConference();
		String stateFilter = filterDTO.getState();
		PublicPrivateState publicOrPrivate = filterDTO.getPublicOrPrivate();
		HBCUState hbcuOrNot = filterDTO.getHbcuOrNot();

		if (divisionFilter != null) {
			collegeProfileStream = collegeProfileStream
					.filter(cp -> cp.getEssentials().getDivision().equals(divisionFilter));
		}
		if (conferenceFilter != null) {
			collegeProfileStream = collegeProfileStream
					.filter(cp -> cp.getEssentials().getConference().equals(conferenceFilter));
		}
		if (stateFilter != null) {
			collegeProfileStream = collegeProfileStream.filter(cp -> cp.getEssentials().getState().equals(stateFilter));
		}
		if (publicOrPrivate != null) {
			collegeProfileStream = collegeProfileStream
					.filter(cp -> cp.getEssentials().getPublicOrPrivate().equals(publicOrPrivate));
		}
		if (hbcuOrNot != null) {
			collegeProfileStream = collegeProfileStream
					.filter(cp -> cp.getEssentials().getHbcuOrNot().equals(hbcuOrNot));
		}

		return collegeProfileStream;
	}

	private List<CollegeProfileTagWrapper> applyDynamicFilters(FilterDTO filterDTO) {

		List<CollegeProfileTagWrapper> returnList = new ArrayList<>();

		Gender gender = filterDTO.getGender();
		Map<EventType, String> userInput = filterDTO.getUserInput();

		for (EventType eventType : userInput.keySet()) {
			Mark mark = MarkConverter.convertToMark(userInput.get(eventType));
			returnList.addAll(getCPWrappersForOneEvent(eventType, mark, gender, userInput));
			// this.collegeProfiles.stream().filter(cp -> beatsWalkOn(cp, eventType, mark,
			// gender))
			// .collect(Collectors.toList())
		}

		return returnList;
	}

	private List<CollegeProfileTagWrapper> getCPWrappersForOneEvent(EventType eventType, Mark mark, Gender gender,
			Map<EventType, String> userInput) {
		List<CollegeProfileTagWrapper> returnList = new ArrayList<>();

		for (CollegeProfile cp : this.collegeProfiles) {
			if (this.beatsLevel(cp, eventType, mark, gender, Level.WALK_ON)) {
				returnList.add(new CollegeProfileTagWrapper(cp.toSummary(), generateTags(cp, gender, userInput)));
			}
		}

		return returnList;

	}

	private List<SimpleEntry<EventType, Level>> generateTags(CollegeProfile cp, Gender gender,
			Map<EventType, String> userInput) {
		List<SimpleEntry<EventType, Level>> returnList = new ArrayList<>();

		for (EventType eventType : userInput.keySet()) {
			Mark mark = MarkConverter.convertToMark(userInput.get(eventType));

			if (this.beatsLevel(cp, eventType, mark, gender, Level.HARD_RECRUIT)) {
				returnList.add(new AbstractMap.SimpleEntry<>(eventType, Level.HARD_RECRUIT));
			} else if (this.beatsLevel(cp, eventType, mark, gender, Level.SOFT_RECRUIT)) {
				returnList.add(new AbstractMap.SimpleEntry<>(eventType, Level.SOFT_RECRUIT));
			} else if (this.beatsLevel(cp, eventType, mark, gender, Level.WALK_ON)) {
				returnList.add(new AbstractMap.SimpleEntry<>(eventType, Level.WALK_ON));
			}
		}

		return returnList;
	}

	public CollegeProfileTagWrapper convertToWrapper(CollegeProfile collegeProfile, EventType eventType, Mark mark) {
		// Perform the conversion from CollegeProfile to CollegeProfileWrapper here
		// For example:
		return new CollegeProfileTagWrapper(collegeProfile.toSummary());
	}

	private boolean beatsLevel(CollegeProfile cp, EventType eventType, Mark mark, Gender gender, Level level) {

		Standards singleGenderStandards = null;

		if (cp.getStandardsSet() == null) {
			// tag no data
			return false;
		}

		if (level.equals(Level.HARD_RECRUIT)) {
			if (gender.equals(Gender.MALE) && cp.getStandardsSet().getMaleHardRecruit() == null) {
				return false;
			} else if (gender.equals(Gender.FEMALE) && cp.getStandardsSet().getFemaleHardRecruit() == null) {
				return false;
			}
			singleGenderStandards = gender.equals(Gender.MALE) ? cp.getStandardsSet().getMaleHardRecruit()
					: cp.getStandardsSet().getFemaleHardRecruit();
		} else if (level.equals(Level.SOFT_RECRUIT)) {
			if (gender.equals(Gender.MALE) && cp.getStandardsSet().getMaleSoftRecruit() == null) {
				return false;
			} else if (gender.equals(Gender.FEMALE) && cp.getStandardsSet().getFemaleSoftRecruit() == null) {
				return false;
			}
			singleGenderStandards = gender.equals(Gender.MALE) ? cp.getStandardsSet().getMaleSoftRecruit()
					: cp.getStandardsSet().getFemaleSoftRecruit();
		} else {
			if (gender.equals(Gender.MALE) && cp.getStandardsSet().getMaleWalkOn() == null) {
				return false;
			} else if (gender.equals(Gender.FEMALE) && cp.getStandardsSet().getFemaleWalkOn() == null) {
				return false;
			}
			singleGenderStandards = gender.equals(Gender.MALE) ? cp.getStandardsSet().getMaleWalkOn()
					: cp.getStandardsSet().getFemaleWalkOn();
		}

		Mark collegeMark = singleGenderStandards.getExistingEventsMapAndTheirTargetStandard().get(eventType);
		if (collegeMark == null) {
			return false;
		}

		return collegeMark.compare(mark) < 0;
	}

	@Override
	public List<String> getListOfAllStates() {
		List<String> returnList = new ArrayList<>();
		for (CollegeProfile cp : this.collegeProfiles) {
			returnList.add(cp.getEssentials().getState());
		}
		HashSet<String> uniqueNames = new HashSet<>(returnList);
		returnList.clear();
		returnList.addAll(uniqueNames);
		returnList.sort(new AlphabeticalComparator());
		return returnList;
	}

	@Override
	public List<String> getListOfAllConferences() {
		List<String> returnList = new ArrayList<>();
		for (CollegeProfile cp : this.collegeProfiles) {
			returnList.add(cp.getEssentials().getConference());
		}
		HashSet<String> uniqueNames = new HashSet<>(returnList);
		returnList.clear();
		returnList.addAll(uniqueNames);
		returnList.sort(new AlphabeticalComparator());
		return returnList;
	}

	@Override
	public List<CollegeProfileTagWrapper> getAllColleges() {
		List<CollegeProfile> cps = this.collegeProfiles;
		return cps.stream().map(cp -> cp.toSummary()).map(cp -> new CollegeProfileTagWrapper(cp))
				.collect(Collectors.toList());

	}

}

//Custom comparator to sort strings in alphabetical order
class AlphabeticalComparator implements Comparator<String> {
	@Override
	public int compare(String str1, String str2) {
		return str1.compareTo(str2);
	}
}
