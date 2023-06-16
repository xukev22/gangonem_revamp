package org.gangonem.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.gangonem.model.CleanedData;
import org.gangonem.model.CollegeProfile;
import org.gangonem.model.Competitor;
import org.gangonem.model.Essentials;
import org.gangonem.model.Event;
import org.gangonem.model.Meet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeneralUtils {

	// Fetch the document with unlimited size
	public static Document fetchDocument(String url) throws IOException {
		return fetchDocument(url, 0);
	}

	// Fetch the document with sizeInMB limit
	public static Document fetchDocument(String url, int sizeInMB) throws IOException {
		Document page = Jsoup.connect(url).userAgent("Mozilla/5.0").maxBodySize(sizeInMB * 1024 * 1024)
				.ignoreContentType(true).get();
		return page;
	}

	// Wrtie an object to json file
	public static <T> void writeObjectToJSON(T object, File file) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(file, object);
	}

	// Read from a file with list of meet data
	public List<Meet> fileReaderMeetData(File file) throws StreamReadException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(file, new TypeReference<List<Meet>>() {
		});

	}

	// TODO: Fix?
	// Read from a file with Cleaned Data
	public CleanedData fileReaderCleanedData(File file) throws StreamReadException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(file, new TypeReference<CleanedData>() {
		});

	}

	// Read from a file with list of essentials
	public List<Essentials> fileReaderEssentialsData(File file) throws StreamReadException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(file, new TypeReference<List<Essentials>>() {
		});

	}

	// Read from a file with list of college profile data
	public List<CollegeProfile> fileReaderCollegeProfileData(File file) throws StreamReadException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(file, new TypeReference<List<CollegeProfile>>() {
		});

	}

	// Get the max bound on the URL
	public static int getMaxBound(String url) throws IOException {
		Document mainDoc = GeneralUtils.fetchDocument(url);
		Elements pageElement = mainDoc.select(".page-item");
		Element secondToLastPageElement = pageElement.get(pageElement.size() - 2);
		return Integer.parseInt(secondToLastPageElement.text());
	}

	// Print a list of meet data
	public static void printListOfMeetData(List<Meet> listOfMeetData) {
		for (Meet meetData : listOfMeetData) {
			meetData.debugPrint();
		}
	}

	// print list of essentials
	public static void printListOfEssentialsProfiles(List<Essentials> listOfEP) {
		for (Essentials ep : listOfEP) {
			System.out.println(ep);
		}
	}

	// Append 3 lists of type T
	public static <T> List<T> appendThreeLists(List<T> list1, List<T> list2, List<T> list3) {
		List<T> allLocp = new ArrayList<>();
		allLocp.addAll(list1);
		allLocp.addAll(list2);
		allLocp.addAll(list3);
		return allLocp;
	}
}
