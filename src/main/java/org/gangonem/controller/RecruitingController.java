package org.gangonem.controller;

import java.util.List;

import org.gangonem.model.CollegeProfile;
import org.gangonem.model.EssentialsBonus;
import org.gangonem.service.RecruitingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecruitingController {
	@Autowired
	RecruitingService recruitingService;

	@CrossOrigin(origins = "http://localhost:5173")
	@GetMapping("/colleges/getDetailsForCollegeByName")
	public CollegeProfile getDetailsForCollegeByName(@RequestParam("collegeName") String collegeName) {
		// search for college name, return the full college profile

		String convertedAndToAmpersand = collegeName.replace("and", "&");

		if (recruitingService.getDetailsForCollegeByName(convertedAndToAmpersand).isPresent()) {
			return recruitingService.getDetailsForCollegeByName(convertedAndToAmpersand).get();
		} else {
			throw new RuntimeException("College profile not found for: " + convertedAndToAmpersand);
		}
	}
	
	@CrossOrigin(origins = "http://localhost:5173")
	@GetMapping("/colleges/getMoreDetailsForCollegeByName")
	public EssentialsBonus getMoreDetailsForCollegeByName(@RequestParam("collegeName") String collegeName) {
		// search for college name, return the full college profile

		String convertedAndToAmpersand = collegeName.replace("and", "&");

		if (recruitingService.getMoreDetailsForCollegeByName(convertedAndToAmpersand).isPresent()) {
			return recruitingService.getMoreDetailsForCollegeByName(convertedAndToAmpersand).get();
		} else {
			throw new RuntimeException("College profile not found for: " + convertedAndToAmpersand);
		}
	}

	@CrossOrigin(origins = "http://localhost:5173")
	@GetMapping("/colleges/getListOfAllCollegeNames")
	public List<String> getListOfAllCollegeNames() {
		if (recruitingService.getListOfAllCollegeNames().size() == 0) {
			throw new RuntimeException("List of college names is empty!");
		} else {
			return recruitingService.getListOfAllCollegeNames();
		}
	}
}
