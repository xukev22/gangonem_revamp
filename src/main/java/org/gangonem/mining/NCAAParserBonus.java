package org.gangonem.mining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gangonem.model.Division;
import org.gangonem.model.EssentialsBonus;
import org.gangonem.utils.GeneralUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NCAAParserBonus {
	// https://www.ncaa.com/schools-index

	public List<EssentialsBonus> parseCollegeByNewNCAA(String url) throws IOException {

		// fetch page by url
		Document page = GeneralUtils.fetchDocument(url);

		List<EssentialsBonus> listOfEssentialsProfile = new ArrayList<>();

		Elements elements = page.select("a:matchesOwn((?i).*\\bLast\\b.*)");

		int lastPageIndex = Integer.parseInt(elements.get(0).attr("href").substring(15));

		List<String> partialCollegeURLs = new ArrayList<>();
		for (int i = 0; i <= lastPageIndex; i++) {
			System.out.println("https://www.ncaa.com/schools-index/" + i);
			collectSchoolURLs(partialCollegeURLs, "https://www.ncaa.com/schools-index/" + i);
		}

		System.out.println(partialCollegeURLs);

		for (String partialCollegeURL : partialCollegeURLs) {
			createSingleEssentialsBonus(listOfEssentialsProfile, partialCollegeURL);
		}

		// return final list
		return listOfEssentialsProfile;

	}

	// mutate the given list, adding the mined details
	private void createSingleEssentialsBonus(List<EssentialsBonus> listOfEssentialsProfile, String partialUrl)
			throws IOException {
		Document page = GeneralUtils.fetchDocument("https://www.ncaa.com" + partialUrl);

		String targetName = partialUrl.substring(9);

		EssentialsBonus essentialsBonus = new EssentialsBonus();

		String trueName = page.select(
				"img[src='https://www.ncaa.com/sites/default/files/images/logos/schools/bgd/" + targetName + ".svg']")
				.get(0).attr("alt");

		trueName = trueName.replace("&amp;", "&");

		String divisionAndLocation = page.select(".division-location").get(0).text();
		String townParsed = this.extractTown(divisionAndLocation);

		Elements elements = page.select(".dl-group");

		String nickname = elements.get(1).select("dd").text();
		String color = page.select(".school-header").get(0).attr("style").substring(18).toUpperCase();

		System.out.println(trueName + " | " + townParsed + " | " + nickname + " | " + color);

		essentialsBonus.setName(trueName);
		essentialsBonus.setTown(townParsed);
		essentialsBonus.setNickname(nickname);
		essentialsBonus.setHexColor(color);

		listOfEssentialsProfile.add(essentialsBonus);
	}

	private String extractTown(String divisionAndLocation) {
		// Check if the string contains a division
		int divisionIndex = divisionAndLocation.indexOf(" - ");
		if (divisionIndex >= 0) {
			// Extract the portion after the division and trim any leading/trailing
			// whitespace
			String locationString = divisionAndLocation.substring(divisionIndex + 3).trim();
			// Find the index of the comma separator
			int commaIndex = locationString.indexOf(",");
			// Extract the town portion and trim any leading/trailing whitespace
			String town = locationString.substring(0, commaIndex).trim();
			return town;
		} else {
			// No division found, so assume the entire string is the location
			int commaIndex = divisionAndLocation.indexOf(",");
			String town = divisionAndLocation.substring(0, commaIndex).trim();
			return town;
		}
	}

	// mutates the given list, and adds any found college urls to the list
	private void collectSchoolURLs(List<String> schoolURLs, String url) throws IOException {
		Document page = GeneralUtils.fetchDocument(url);
		Elements elements = page.select("tbody a:not(td div a)");
		for (Element aTag : elements) {
			// Get the href attribute value and add it to the list
			String hrefValue = aTag.attr("href");
			if (hrefValue.equals("/schools/jwu-denver") || hrefValue.equals("/schools/md-east-shore")
					|| hrefValue.equals("/schools/reneau-university") || hrefValue.equals("/schools/st-francis-pa")
					|| hrefValue.equals("/schools/suny-morrisville")) {
				// hard coded school, shut down due to low enrollment LMAO
				// umes/md-east-shore, st-francis-pa/saint-francis-pa: no idea, note the naming
				// difference?
				// reneau, suny-morrisville: idek
				return;
			}
			schoolURLs.add(hrefValue);
		}
	}
}
