package org.gangonem.utils;

import java.math.BigDecimal;

import org.gangonem.model.Distance;
import org.gangonem.model.Mark;
import org.gangonem.model.Points;
import org.gangonem.model.Time;
import org.gangonem.model.UnhandledMark;

public class MarkConverter {

	public static Mark convertToMark(String input) {
		if (input.contains("m")) {
			return convertToDistance(input);
		} else if (input.contains(".")) {
			return convertToTime(input);
		} else if (input.matches("\\d+")) {
			return convertToPoints(input);
		} else {
			return new UnhandledMark();
		}
	}

	private static Time convertToTime(String input) {
		int minute = 0;
		int second = 0;
		String fracSecond = "";

		String[] parts = input.split("[:.]");
		int numParts = parts.length;

		if (numParts == 3) {
			minute = Integer.parseInt(parts[numParts - 3]);
			second = Integer.parseInt(parts[numParts - 2]);
			fracSecond = parts[numParts - 1];
		} else if (numParts == 2) {
			second = Integer.parseInt(parts[numParts - 2]);
			fracSecond = parts[numParts - 1];
		} else {
			throw new RuntimeException("String to time has invalid input: " + input);
		}

		return new Time(minute, second, fracSecond);
	}

	private static Distance convertToDistance(String input) {
		String meters = input.replace("m", "");
		BigDecimal value = new BigDecimal(meters);

		return new Distance(value);
	}

	private static Points convertToPoints(String input) {
		int points = Integer.parseInt(input);

		return new Points(points);
	}
}