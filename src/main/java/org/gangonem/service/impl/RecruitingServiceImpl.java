package org.gangonem.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.gangonem.controller.FilterDTO;
import org.gangonem.model.CollegeProfile;
import org.gangonem.model.Division;
import org.gangonem.model.EssentialsBonus;
import org.gangonem.model.EventType;
import org.gangonem.model.Gender;
import org.gangonem.model.HBCUState;
import org.gangonem.model.Mark;
import org.gangonem.model.PublicPrivateState;
import org.gangonem.service.RecruitingService;
import org.gangonem.utils.GeneralUtils;
import org.gangonem.utils.MarkConverter;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;

@Service
public class RecruitingServiceImpl implements RecruitingService {

	List<CollegeProfile> collegeProfiles;
	List<EssentialsBonus> moreInfoCollegeProfiles;

	public RecruitingServiceImpl() throws StreamReadException, IOException {
		this.collegeProfiles = new ArrayList<>();
		this.moreInfoCollegeProfiles = new ArrayList<>();
		this.collegeProfiles = new GeneralUtils()
				.fileReaderCollegeProfileData(new File("./data/bigCleanFinalTest.json"));
		this.moreInfoCollegeProfiles = new GeneralUtils()
				.fileReaderEssentialsBonusData(new File("./data/essentialsBonus.json"));
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

	@Override
	public List<CollegeProfile> getMatchingColleges(boolean s, boolean d, FilterDTO filterDTO) {
		List<CollegeProfile> collegeProfilesReturn = this.collegeProfiles;

		List<CollegeProfile> collegeProfilesAuxilary = new ArrayList<>();

		Division divisionFilter = filterDTO.getDivision();
		String conferenceFilter = filterDTO.getConference();
		String stateFilter = filterDTO.getState();
		// String townFilter = filterDTO.getTown();
		PublicPrivateState publicOrPrivate = filterDTO.getPublicOrPrivate();
		HBCUState hbcuOrNot = filterDTO.getHbcuOrNot();
		Gender gender = filterDTO.getGender();
		Map<EventType, String> userInput = filterDTO.getUserInput();

		if (s && d) {
			collegeProfilesReturn = sMutate(collegeProfilesReturn, divisionFilter, conferenceFilter, stateFilter,
					publicOrPrivate, hbcuOrNot);

			collegeProfilesAuxilary = dMutate(collegeProfilesAuxilary, gender, userInput);

			Set<CollegeProfile> unionSet = new HashSet<>();
			unionSet.addAll(collegeProfilesReturn);
			unionSet.addAll(collegeProfilesAuxilary);

			List<CollegeProfile> unionList = new ArrayList<>(unionSet);

			return unionList;
		} else if (s) {
			collegeProfilesReturn = sMutate(collegeProfilesReturn, divisionFilter, conferenceFilter, stateFilter,
					publicOrPrivate, hbcuOrNot);

			return collegeProfilesReturn;
		} else if (d) {
			collegeProfilesAuxilary = dMutate(collegeProfilesAuxilary, gender, userInput);

			return collegeProfilesAuxilary;
		} else {
			throw new RuntimeException("This should never run!");
		}

	}

	private List<CollegeProfile> dMutate(List<CollegeProfile> collegeProfilesAuxilary, Gender gender,
			Map<EventType, String> userInput) {

		for (EventType eventType : userInput.keySet()) {
			List<CollegeProfile> cps = new ArrayList<>();
			Mark mark = MarkConverter.convertToMark(userInput.get(eventType));
			if (gender.equals(Gender.MALE)) {
				cps = this.collegeProfiles.stream()
						.filter(cp -> RecruitingServiceImpl.beatsMaleWalkOn(cp, eventType, mark)).toList();
			} else if (gender.equals(Gender.FEMALE)) {
				cps = this.collegeProfiles.stream()
						.filter(cp -> RecruitingServiceImpl.beatsFemaleWalkOn(cp, eventType, mark)).toList();
			} else {
				throw new RuntimeException("Should never happen!");
			}
			collegeProfilesAuxilary.addAll(cps);
		}

		// need to differentiate, not just show walk on
		return collegeProfilesAuxilary;
	}

	private List<CollegeProfile> sMutate(List<CollegeProfile> collegeProfilesReturn, Division divisionFilter,
			String conferenceFilter, String stateFilter, PublicPrivateState publicOrPrivate, HBCUState hbcuOrNot) {
		if (divisionFilter != null) {
			collegeProfilesReturn = collegeProfilesReturn.stream()
					.filter(cp -> cp.getEssentials().getDivision().equals(divisionFilter)).toList();
		}
		if (conferenceFilter != null) {
			collegeProfilesReturn = collegeProfilesReturn.stream()
					.filter(cp -> cp.getEssentials().getConference().equals(conferenceFilter)).toList();
		}
		if (stateFilter != null) {
			collegeProfilesReturn = collegeProfilesReturn.stream()
					.filter(cp -> cp.getEssentials().getState().equals(stateFilter)).toList();
		}
//			if (conferenceFilter != null) {
//				returnList.stream().filter(cp -> cp.getEb().getTown().equals(townFilter));
//			}
		if (publicOrPrivate != null) {
			collegeProfilesReturn = collegeProfilesReturn.stream()
					.filter(cp -> cp.getEssentials().getPublicOrPrivate().equals(publicOrPrivate)).toList();
		}
		if (hbcuOrNot != null) {
			collegeProfilesReturn = collegeProfilesReturn.stream()
					.filter(cp -> cp.getEssentials().getHbcuOrNot().equals(hbcuOrNot)).toList();
		}
		return collegeProfilesReturn;
	}

	private static boolean beatsMaleWalkOn(CollegeProfile cp, EventType eventType, Mark mark) {
		if (cp.getStandardsSet() == null) {
			// TODO: user feedback?
			return false;
		}
		if (cp.getStandardsSet().getMaleWalkOn().getExistingEventsMapAndTheirTargetStandard().get(eventType) == null) {
			return false;
		}
		System.out.println(cp.getEssentials().getName() + " | " + eventType + " | " + mark.debug());
		return cp.getStandardsSet().getMaleWalkOn().getExistingEventsMapAndTheirTargetStandard().get(eventType)
				.compare(mark) < 0;

	}

	private static boolean beatsFemaleWalkOn(CollegeProfile cp, EventType eventType, Mark mark) {
		if (cp.getStandardsSet() == null) {
			// TODO: user feedback?
			return false;
		}
		if (cp.getStandardsSet().getFemaleWalkOn().getExistingEventsMapAndTheirTargetStandard()
				.get(eventType) == null) {
			return false;
		}
		return cp.getStandardsSet().getFemaleWalkOn().getExistingEventsMapAndTheirTargetStandard().get(eventType)
				.compare(mark) < 0;

	}

}
