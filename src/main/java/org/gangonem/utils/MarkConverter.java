package org.gangonem.utils;

import java.math.BigDecimal;

import org.gangonem.model.Distance;
import org.gangonem.model.Mark;
import org.gangonem.model.Points;
import org.gangonem.model.Time;
import org.gangonem.model.UnhandledMark;

public class MarkConverter {

	public static Mark convertToMark(String input) {
		if (input.contains("m") || input.contains("\"")) {
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
		} else if (numParts == 4) {
			System.out.println();
			minute = Integer.parseInt(parts[0]);
			second = Integer.parseInt(parts[1]);
			fracSecond = parts[2];
		} else {
			throw new RuntimeException("String to time has invalid input: " + input);
		}

		return new Time(minute, second, fracSecond);
	}

	private static Distance convertToDistance(String input) {

		if (input.contains("\"")) {
			BigDecimal value = new BigDecimal(convertToMeters(input));
			return new Distance(value);
		} else {

			String meters = input.replace("m", "");
			BigDecimal value = new BigDecimal(meters);

			return new Distance(value);
		}

	}

	private static double convertToMeters(String input) {
		// Trim spaces and extract feet and inches parts from the input string
		String[] parts = input.trim().split("'");
		double feet = Double.parseDouble(parts[0]);
		double inches = 0.0;

		if (parts.length > 1) {
			// If there is an inch part, extract it
			inches = Double.parseDouble(parts[1].replace("\"", "").trim());
		}

		// Conversion factors
		final double FEET_TO_METERS = 0.3048;
		final double INCH_TO_METERS = 0.0254;

		// Convert feet and inches to meters
		double totalFeet = feet + inches / 12.0;
		double meters = totalFeet * FEET_TO_METERS;

		// Round down to the nearest hundredth
		meters = Math.floor(meters * 100) / 100.0;

		return meters;
	}

	private static Points convertToPoints(String input) {
		int points = Integer.parseInt(input);

		return new Points(points);
	}
}