package org.gangonem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gangonem.cleaning.DataCleaner;
import org.gangonem.mining.NCAAParser;
import org.gangonem.mining.NCAAParserBonus;
import org.gangonem.mining.TFRRSParser;
import org.gangonem.model.CleanedData;
import org.gangonem.model.CollegeProfile;
import org.gangonem.model.Essentials;
import org.gangonem.model.EssentialsBonus;
import org.gangonem.model.Meet;
import org.gangonem.processing.DataProcessing;
import org.gangonem.utils.CSVUtils;
import org.gangonem.utils.GeneralUtils;

public class MainApp {

	public static void main(String[] args) throws IOException {
		// CONSTANTS //
		final String mainURL = "https://www.tfrrs.org/results_search.html";
		final int maxBound = GeneralUtils.getMaxBound(mainURL);
		final int LOWER_BOUND = 71;
		final int UPPER_BOUND = 86;

		final String division1MembersURL = "https://web3.ncaa.org/directory/api/directory/memberList?type=12&division=I&_=1674622759094";
		final String division2MembersURL = "https://web3.ncaa.org/directory/api/directory/memberList?type=12&division=II&_=1674791789361";
		final String division3MembersURL = "https://web3.ncaa.org/directory/api/directory/memberList?type=12&division=III&_=1674791804655";
		// END CONSTANTS //

		// TESTING SPACE:

//		GeneralUtils.writeObjectToJSON(new TFRRSParser().meetData(LOWER_BOUND, UPPER_BOUND, mainURL),
//				new File("./data/xc-track-full6.json"));

		List<Meet> meet1 = new GeneralUtils().fileReaderMeetData(new File("./data/xc-track-full1.json"));
		List<Meet> meet2 = new GeneralUtils().fileReaderMeetData(new File("./data/xc-track-full2.json"));
		List<Meet> meet3 = new GeneralUtils().fileReaderMeetData(new File("./data/xc-track-full3.json"));
		List<Meet> meet4 = new GeneralUtils().fileReaderMeetData(new File("./data/xc-track-full4.json"));
		List<Meet> meet5 = new GeneralUtils().fileReaderMeetData(new File("./data/xc-track-full5.json"));
		List<Meet> meet6 = new GeneralUtils().fileReaderMeetData(new File("./data/xc-track-full6.json"));

		List<Meet> allMeets = new ArrayList<>();
		allMeets.addAll(meet1);
		allMeets.addAll(meet2);
		allMeets.addAll(meet3);
		allMeets.addAll(meet4);
		allMeets.addAll(meet5);
		allMeets.addAll(meet6);

		List<Essentials> newLoe1 = new NCAAParser().parseCollegeByDivision(division1MembersURL);
		List<Essentials> newLoe2 = new NCAAParser().parseCollegeByDivision(division2MembersURL);
		List<Essentials> newLoe3 = new NCAAParser().parseCollegeByDivision(division3MembersURL);
		GeneralUtils.writeObjectToJSON(newLoe1, new File("./data/div1.json"));
		GeneralUtils.writeObjectToJSON(newLoe2, new File("./data/div2.json"));
		GeneralUtils.writeObjectToJSON(newLoe3, new File("./data/div3.json"));

		CleanedData cleanedData = new DataCleaner().cleanMeets(allMeets);
		GeneralUtils.writeObjectToJSON(cleanedData, new File("./data/xc-track-cleanedData.json"));

		Map<String, String> csvMappingBig = CSVUtils.csvToHashMap(new File("./data/NCAAToTFRRSMap_08_03_2023.csv"), 0);

		List<Essentials> loe1 = new GeneralUtils().fileReaderEssentialsData(new File("./data/div1.json"));
		List<Essentials> loe2 = new GeneralUtils().fileReaderEssentialsData(new File("./data/div2.json"));
		List<Essentials> loe3 = new GeneralUtils().fileReaderEssentialsData(new File("./data/div3.json"));

		List<Essentials> masterLoe = new ArrayList<>();
		masterLoe.addAll(loe1);
		masterLoe.addAll(loe2);
		masterLoe.addAll(loe3);

		Comparator<Essentials> essentialsComparator = Comparator.comparing(Essentials::getName);

		Set<Essentials> uniqueEssentialsSet = new HashSet<>(masterLoe);
		List<Essentials> uniqueEssentialsList = new ArrayList<>(uniqueEssentialsSet);
		Collections.sort(uniqueEssentialsList, essentialsComparator);

		List<CollegeProfile> cleanCollegeProfile = new DataProcessing().createCollegeProfiles(cleanedData,
				uniqueEssentialsList, csvMappingBig);
		GeneralUtils.writeObjectToJSON(cleanCollegeProfile, new File("./data/xc-track-data-2023.json"));

//		List<EssentialsBonus> listOfEssentialsBonus = new NCAAParserBonus()
//				.parseCollegeByNewNCAA("https://www.ncaa.com/schools-index");
//		Comparator<EssentialsBonus> essentialsBonusComparator = Comparator.comparing(EssentialsBonus::getName);
//
//		Collections.sort(listOfEssentialsBonus, essentialsBonusComparator);
//		GeneralUtils.writeObjectToJSON(listOfEssentialsBonus, new File("./data/essentialsBonus.json"));

		// END TESTING SPACE:

	}

}
