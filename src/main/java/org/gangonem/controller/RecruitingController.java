package org.gangonem.controller;

import java.util.List;
import java.util.Map;

import org.gangonem.model.CollegeProfile;
import org.gangonem.model.CollegeProfileTagWrapper;
import org.gangonem.model.Division;
import org.gangonem.model.EventType;
import org.gangonem.model.Gender;
import org.gangonem.model.HBCUState;
import org.gangonem.model.PublicPrivateState;
import org.gangonem.service.RecruitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecruitingController {
	static final String ORIGIN_DOMAIN = "http://www.blockstart.org";
//	static final String ORIGIN_DOMAIN = "http://localhost";
	
	
	@Autowired
	RecruitingService recruitingService;

	@CrossOrigin(origins = ORIGIN_DOMAIN)
	@GetMapping("/colleges/getDetailsForCollegeByName")
	public CollegeProfile getDetailsForCollegeByName(@RequestParam("collegeName") String collegeName) {
		// search for college name, return the full college profile

		CollegeProfile returnProfile = new CollegeProfile();

		String convertedAndToAmpersand = collegeName.replace("---and---", "&");

		if (recruitingService.getDetailsForCollegeByName(convertedAndToAmpersand).isPresent()) {
			returnProfile = recruitingService.getDetailsForCollegeByName(convertedAndToAmpersand).get();
		} else {
			throw new RuntimeException("College profile not found for: " + convertedAndToAmpersand);
		}

		if (recruitingService.getMoreDetailsForCollegeByName(convertedAndToAmpersand).isPresent()) {
			returnProfile.setEb(recruitingService.getMoreDetailsForCollegeByName(convertedAndToAmpersand).get());
		}

		return returnProfile;
	}
	
	@CrossOrigin(origins = ORIGIN_DOMAIN)
	@GetMapping("/colleges/getAllColleges")
	public List<CollegeProfileTagWrapper> getAllColleges() {
		return recruitingService.getAllColleges();
	}

	@CrossOrigin(origins = ORIGIN_DOMAIN)
	@PostMapping("/colleges/getMatchingColleges")
	public ResponseEntity<List<CollegeProfileTagWrapper>> getMatchingColleges(@RequestBody FilterDTO filterDTO) {
		Division divisionFilter = filterDTO.getDivision();
		String conferenceFilter = filterDTO.getConference();
		String stateFilter = filterDTO.getState();
		// String townFilter = filterDTO.getTown();
		PublicPrivateState publicOrPrivate = filterDTO.getPublicOrPrivate();
		HBCUState hbcuOrNot = filterDTO.getHbcuOrNot();
		Gender gender = filterDTO.getGender();
		Map<EventType, String> userInput = filterDTO.getUserInput();

		boolean statics = divisionFilter != null || conferenceFilter != null || stateFilter != null ||
		// townFilter != null ||
				publicOrPrivate != null || hbcuOrNot != null;
		boolean dynamics = gender != null && userInput != null;

		if (statics && dynamics) {
			// At least one of the fields is not null, perform the desired action
			// e.g., process the filters, execute the search, etc.
			List<CollegeProfileTagWrapper> returnList = recruitingService.getMatchingColleges(true, true, filterDTO);
			return new ResponseEntity<>(returnList, HttpStatus.OK);
		} else if (statics) {
			List<CollegeProfileTagWrapper> returnList = recruitingService.getMatchingColleges(true, false, filterDTO);
			return new ResponseEntity<>(returnList, HttpStatus.OK);
		} else if (dynamics) {
			List<CollegeProfileTagWrapper> returnList = recruitingService.getMatchingColleges(false, true, filterDTO);
			return new ResponseEntity<>(returnList, HttpStatus.OK);
		} else {
			// Handle the case where all fields are null
			// e.g., return a response indicating no filters were provided
			throw new RuntimeException("Invalid request");
		}
	}

//	@CrossOrigin(origins = ORIGIN_DOMAIN)
//	@GetMapping("/colleges/getMoreDetailsForCollegeByName")
//	public EssentialsBonus getMoreDetailsForCollegeByName(@RequestParam("collegeName") String collegeName) {
//		// search for college name, return the full college profile
//
//		String convertedAndToAmpersand = collegeName.replace("---and---", "&");
//
//		if (recruitingService.getMoreDetailsForCollegeByName(convertedAndToAmpersand).isPresent()) {
//			return recruitingService.getMoreDetailsForCollegeByName(convertedAndToAmpersand).get();
//		} else {
//			throw new RuntimeException("College profile not found for: " + convertedAndToAmpersand);
//		}
//	}

	@CrossOrigin(origins = ORIGIN_DOMAIN)
	@GetMapping("/colleges/getListOfAllCollegeNames")
	public List<String> getListOfAllCollegeNames() {
		if (recruitingService.getListOfAllCollegeNames().size() == 0) {
			throw new RuntimeException("List of college names is empty!");
		} else {
			return recruitingService.getListOfAllCollegeNames();
		}
	}
	
	@CrossOrigin(origins = ORIGIN_DOMAIN)
	@GetMapping("/colleges/getListOfAllStates")
	public List<String> getListOfAllStates() {
		if (recruitingService.getListOfAllStates().size() == 0) {
			throw new RuntimeException("List of college states is empty!");
		} else {
			return recruitingService.getListOfAllStates();
		}
	}
	
	@CrossOrigin(origins = ORIGIN_DOMAIN)
	@GetMapping("/colleges/getListOfAllConferences")
	public List<String> getListOfAllConferences() {
		if (recruitingService.getListOfAllConferences().size() == 0) {
			throw new RuntimeException("List of college conferences is empty!");
		} else {
			return recruitingService.getListOfAllConferences();
		}
	}
}
