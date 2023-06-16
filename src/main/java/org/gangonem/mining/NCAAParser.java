package org.gangonem.mining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.gangonem.model.Division;
import org.gangonem.model.Essentials;
import org.gangonem.model.HBCUState;
import org.gangonem.model.PublicPrivateState;
import org.gangonem.utils.GeneralUtils;
import org.jsoup.nodes.Document;

public class NCAAParser {

	// Parses the colleges by division, passed Div1, Div2, and Div3 urls
	public List<Essentials> parseCollegeByDivision(String url) throws IOException {

		// fetch page by url
		Document page = GeneralUtils.fetchDocument(url);

		// creates an empty list of essentials Data, we will fill this up
		List<Essentials> listOfEssentialsProfile = new ArrayList<>();

		// split the data string into an array of string,
		// each element is a string of a college's information
		String[] parts = page.text().split("nameOfficial");

		// iterate over the colleges
		for (int i = 1; i < parts.length; i++) {

			// create a new essentials per college
			Essentials essentials = new Essentials();

			// split the essentials string into an array of string,
			// each element is a trait of the college
			String[] essentialsParts = parts[i].split(",\"");

			// set essentials variables
			essentials.setName(essentialsParts[0].replace(":", "").replace("\"", ""));
			String divisionNum = essentialsParts[2].substring(essentialsParts[2].length() - 1);
			Division division;
			if (divisionNum.equals("1")) {
				division = Division.I;
			} else if (divisionNum.equals("2")) {
				division = Division.II;
			} else if (divisionNum.equals("3")) {
				division = Division.III;
			} else {
				throw new RuntimeException("While mining membership directory: division was not 1, 2, or 3");
			}
			essentials.setDivision(division);
			essentials.setConference(essentialsParts[9].replace("conferenceName\":", "").replace("\"", ""));
			essentials.setState(essentialsParts[21].replace("state\":", "").replace("\"", ""));
			essentials.setMainWebsiteURL(essentialsParts[13].replace("webSiteUrl\":", "").replace("\"", ""));
			essentials.setAthleticWebsiteURL(essentialsParts[14].replace("athleticWebUrl\":", "").replace("\"", ""));
			String publicPrivate = essentialsParts[28].replace("privateFlag\":", "").replace("\"", "");
			String hbcu = essentialsParts[29].replace("historicallyBlackFlag\":", "").replace("\"", "");
			PublicPrivateState pubPriv;
			if (publicPrivate.equals("Y")) {
				pubPriv = PublicPrivateState.PUBLIC;
			} else if (publicPrivate.equals("N")) {
				pubPriv = PublicPrivateState.PRIVATE;
			} else {
				pubPriv = PublicPrivateState.DEFAULT;
			}
			HBCUState isHBCU;
			if (hbcu.equals("Y")) {
				isHBCU = HBCUState.YES;
			} else if (hbcu.equals("N")) {
				isHBCU = HBCUState.NO;
			} else {
				isHBCU = HBCUState.DEFAULT;
			}
			essentials.setPublicOrPrivate(pubPriv);
			essentials.setHbcuOrNot(isHBCU);

			// add final profile
			listOfEssentialsProfile.add(essentials);
		}

		// return final list
		return listOfEssentialsProfile;

	}

}
