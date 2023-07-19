package org.gangonem.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.gangonem.controller.FilterDTO;
import org.gangonem.model.CollegeProfile;
import org.gangonem.model.Division;
import org.gangonem.model.EssentialsBonus;
import org.gangonem.model.EventType;
import org.gangonem.model.Gender;
import org.gangonem.model.HBCUState;
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
		File collegeProfileFile = new File("./data/bigCleanFinalTest.json");
		collegeProfiles = new GeneralUtils().fileReaderCollegeProfileData(collegeProfileFile);
	}

	private void loadEssentialsBonusData() throws StreamReadException, IOException {
		File essentialsBonusFile = new File("./data/essentialsBonus.json");
		moreInfoCollegeProfiles = new GeneralUtils().fileReaderEssentialsBonusData(essentialsBonusFile);
	}

	@Override
	public List<CollegeProfile> getMatchingColleges(boolean hasStaticFilters, boolean hasDynamicFilters,
			FilterDTO filterDTO) {
		Stream<CollegeProfile> collegeProfileStream = collegeProfiles.stream();

		if (hasStaticFilters) {
			collegeProfileStream = applyStaticFilters(collegeProfileStream, filterDTO);
		} else {
			collegeProfileStream = Stream.empty();
		}

		List<CollegeProfile> staticFilteredColleges = collegeProfileStream.collect(Collectors.toList());

		List<CollegeProfile> dynamicFilteredColleges = new ArrayList<>();

		if (hasDynamicFilters) {
			dynamicFilteredColleges = applyDynamicFilters(collegeProfiles.stream(), filterDTO)
					.collect(Collectors.toList());
		}

		Set<CollegeProfile> unionSet = new HashSet<>(staticFilteredColleges);
		unionSet.addAll(dynamicFilteredColleges);

		return new ArrayList<>(unionSet);
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

	private Stream<CollegeProfile> applyDynamicFilters(Stream<CollegeProfile> collegeProfileStream,
			FilterDTO filterDTO) {
		Gender gender = filterDTO.getGender();
		Map<EventType, String> userInput = filterDTO.getUserInput();

		for (EventType eventType : userInput.keySet()) {
			Mark mark = MarkConverter.convertToMark(userInput.get(eventType));
			collegeProfileStream = collegeProfileStream.filter(cp -> beatsWalkOn(cp, eventType, mark, gender));
		}

		return collegeProfileStream;
	}

	private boolean beatsWalkOn(CollegeProfile cp, EventType eventType, Mark mark, Gender gender) {

		if (cp.getStandardsSet() == null) {
			// tag no data
			return false;
		}

		// tag difficulties
		Standards singleGenderStandards = gender.equals(Gender.MALE) ? cp.getStandardsSet().getMaleWalkOn()
				: cp.getStandardsSet().getFemaleWalkOn();

		if (singleGenderStandards == null) {
			// tag no data for gender
			return false;
		}

		Mark collegeMark = singleGenderStandards.getExistingEventsMapAndTheirTargetStandard().get(eventType);
		if (collegeMark == null) {
			// tag?
			return false;
		}

		return collegeMark.compare(mark) < 0;
	}

}
