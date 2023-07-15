package org.gangonem.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.gangonem.model.CollegeProfile;
import org.gangonem.model.EssentialsBonus;
import org.gangonem.service.RecruitingService;
import org.springframework.stereotype.Service;
import org.gangonem.utils.GeneralUtils;

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

}
