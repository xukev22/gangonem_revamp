package org.gangonem.utils;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class CSVUtils {
	
	public static Map<String, String> csvToHashMap(File csvFile, int mainNameIndex) {
		Map<String, String> hashMap = new HashMap<>();

		try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
			String[] header = reader.readNext(); // skip the header row

			// iterate over each row and add the mappings to the hashmap
			String[] row;
			while ((row = reader.readNext()) != null) {
				String mainName = row[mainNameIndex];
				if (row.length == 2) {
					hashMap.put(mainName, row[1]);
				} else {
					System.err.println("Error: Row " + reader.getLinesRead() + " contains multiple nicknames!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hashMap;
	}
}