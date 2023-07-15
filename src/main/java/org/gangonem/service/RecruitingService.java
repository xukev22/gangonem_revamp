package org.gangonem.service;

import java.util.List;
import java.util.Optional;

import org.gangonem.model.CollegeProfile;
import org.gangonem.model.EssentialsBonus;

public interface RecruitingService {

	Optional<CollegeProfile> getDetailsForCollegeByName(String collegeName);

	List<String> getListOfAllCollegeNames();

	Optional<EssentialsBonus> getMoreDetailsForCollegeByName(String collegeName);
}
