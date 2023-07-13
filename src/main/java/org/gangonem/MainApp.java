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
import org.gangonem.model.CleanedData;
import org.gangonem.model.CollegeProfile;
import org.gangonem.model.Essentials;
import org.gangonem.processing.DataProcessing;
import org.gangonem.utils.CSVUtils;
import org.gangonem.utils.GeneralUtils;

public class MainApp {

	public static void main(String[] args) throws IOException {
		// CONSTANTS //
		final String mainURL = "https://www.tfrrs.org/results_search.html";
		final int maxBound = GeneralUtils.getMaxBound(mainURL);
		final int LOWER_BOUND = 114;
		final int UPPER_BOUND = 116;

		final String division1MembersURL = "https://web3.ncaa.org/directory/api/directory/memberList?type=12&division=I&_=1674622759094";
		final String division2MembersURL = "https://web3.ncaa.org/directory/api/directory/memberList?type=12&division=II&_=1674791789361";
		final String division3MembersURL = "https://web3.ncaa.org/directory/api/directory/memberList?type=12&division=III&_=1674791804655";
		// END CONSTANTS //

		// TESTING SPACE:

		CleanedData cleanedData = new DataCleaner()
				.cleanMeets(new GeneralUtils().fileReaderMeetData(new File("./data/meetsSample.json")));

		Map<String, String> csvMappingBig = CSVUtils.csvToHashMap(new File("./data/NCAAToTfrrsMapNew.csv"), 0);

		List<Essentials> loe1 = new GeneralUtils().fileReaderEssentialsData(new File("./data/div1Test.json"));
		List<Essentials> loe2 = new GeneralUtils().fileReaderEssentialsData(new File("./data/div2Test.json"));
		List<Essentials> loe3 = new GeneralUtils().fileReaderEssentialsData(new File("./data/div3Test.json"));

		List<Essentials> masterLoe = new ArrayList<>();
		masterLoe.addAll(loe1);
		masterLoe.addAll(loe2);
		masterLoe.addAll(loe3);
		
		Comparator<Essentials> essentialsComparator = Comparator.comparing(Essentials::getName);

		Set<Essentials> uniqueEssentialsSet = new HashSet<>(masterLoe);
		List<Essentials> uniqueEssentialsList = new ArrayList<>(uniqueEssentialsSet);
		Collections.sort(uniqueEssentialsList, essentialsComparator);


		List<CollegeProfile> cleanCollegeProfile = new DataProcessing().createCollegeProfiles(cleanedData, uniqueEssentialsList,
				csvMappingBig);

		GeneralUtils.writeObjectToJSON(cleanCollegeProfile, new File("./data/bigCleanFinalTest.json"));
		
		//new GeneralUtils().fileReaderCollegeProfileData(new File("./data/bigCleanFinalTest.json"));

		// END TESTING SPACE:

	}

}
