package org.gangonem.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StandardsSet {

	Standards maleWalkOn;
	Standards maleSoftRecruit;
	Standards maleHardRecruit;

	Standards femaleWalkOn;
	Standards femaleSoftRecruit;
	Standards femaleHardRecruit;

	@JsonCreator
	public StandardsSet(@JsonProperty("maleWalkOn") Standards maleWalkOn,
			@JsonProperty("maleSoftRecruit") Standards maleSoftRecruit,
			@JsonProperty("maleHardRecruit") Standards maleHardRecruit,
			@JsonProperty("femaleWalkOn") Standards femaleWalkOn,
			@JsonProperty("femaleSoftRecruit") Standards femaleSoftRecruit,
			@JsonProperty("femaleHardRecruit") Standards femaleHardRecruit) {
		this.maleWalkOn = maleWalkOn;
		this.maleSoftRecruit = maleSoftRecruit;
		this.maleHardRecruit = maleHardRecruit;

		this.femaleWalkOn = femaleWalkOn;
		this.femaleSoftRecruit = femaleSoftRecruit;
		this.femaleHardRecruit = femaleHardRecruit;
	}

	public Standards getMaleWalkOn() {
		return maleWalkOn;
	}

	public void setMaleWalkOn(Standards maleWalkOn) {
		this.maleWalkOn = maleWalkOn;
	}

	public Standards getMaleSoftRecruit() {
		return maleSoftRecruit;
	}

	public void setMaleSoftRecruit(Standards maleSoftRecruit) {
		this.maleSoftRecruit = maleSoftRecruit;
	}

	public Standards getMaleHardRecruit() {
		return maleHardRecruit;
	}

	public void setMaleHardRecruit(Standards maleHardRecruit) {
		this.maleHardRecruit = maleHardRecruit;
	}

	public Standards getFemaleWalkOn() {
		return femaleWalkOn;
	}

	public void setFemaleWalkOn(Standards femaleWalkOn) {
		this.femaleWalkOn = femaleWalkOn;
	}

	public Standards getFemaleSoftRecruit() {
		return femaleSoftRecruit;
	}

	public void setFemaleSoftRecruit(Standards femaleSoftRecruit) {
		this.femaleSoftRecruit = femaleSoftRecruit;
	}

	public Standards getFemaleHardRecruit() {
		return femaleHardRecruit;
	}

	public void setFemaleHardRecruit(Standards femaleHardRecruit) {
		this.femaleHardRecruit = femaleHardRecruit;
	}

	public StandardsSet(Map<EventType, Map<String, Mark>> eventsMap) {
		Map<EventType, Map<String, Mark>> maleEvents = new HashMap<>();
		Map<EventType, Map<String, Mark>> femaleEvents = new HashMap<>();

		List<EventType> maleEventKeys = eventsMap.keySet().stream()
				.filter(eventType -> eventType.name().startsWith("MALE")).collect(Collectors.toList());
		List<EventType> femaleEventKeys = eventsMap.keySet().stream()
				.filter(eventType -> eventType.name().startsWith("FEMALE")).collect(Collectors.toList());

		for (EventType maleEventKey : maleEventKeys) {
			maleEvents.put(maleEventKey, eventsMap.get(maleEventKey));
		}
		for (EventType femaleEventKey : femaleEventKeys) {
			femaleEvents.put(femaleEventKey, eventsMap.get(femaleEventKey));
		}

		this.maleWalkOn = new Standards(this.constructStandardsMap(maleEvents, 50));
		this.maleSoftRecruit = new Standards(this.constructStandardsMap(maleEvents, 75));
		this.maleHardRecruit = new Standards(this.constructStandardsMap(maleEvents, 90));

		this.femaleWalkOn = new Standards(this.constructStandardsMap(femaleEvents, 50));
		this.femaleSoftRecruit = new Standards(this.constructStandardsMap(femaleEvents, 75));
		this.femaleHardRecruit = new Standards(this.constructStandardsMap(femaleEvents, 90));
	}

	private Map<EventType, Mark> constructStandardsMap(Map<EventType, Map<String, Mark>> eventsMap, int percentile) {
		Map<EventType, Mark> returnMap = new HashMap<>();

		for (EventType eventType : eventsMap.keySet()) {
			System.out.println("flag: " + eventType);
			List<Mark> marks = new ArrayList<>(eventsMap.get(eventType).values());
			marks.sort(new MarkComparator());
			Mark mark = nPercentileMarkInList(marks, percentile);

			returnMap.put(eventType, mark);
		}

		return returnMap;
	}

	private Mark nPercentileMarkInList(List<Mark> marks, int percentile) {
		List<Mark> filteredMarks = new ArrayList<>(marks);
		filteredMarks.removeIf(mark -> mark instanceof UnhandledMark);

		if (filteredMarks.isEmpty()) {
			return new UnhandledMark();
		}

		double index = (percentile / 100.0) * (filteredMarks.size() - 1);
		int lowerIndex = (int) Math.floor(index);
		int upperIndex = (int) Math.ceil(index);

		if (lowerIndex == upperIndex) {
			return filteredMarks.get(lowerIndex);
		} else {
			Mark lowerMark = filteredMarks.get(lowerIndex);
			Mark upperMark = filteredMarks.get(upperIndex);

			double weight = index - lowerIndex;

			// Perform weighted interpolation
			if (lowerMark instanceof Distance && upperMark instanceof Distance) {
				Distance lowerDistance = (Distance) lowerMark;
				Distance upperDistance = (Distance) upperMark;

				BigDecimal lowerMeters = lowerDistance.getMeters();
				BigDecimal upperMeters = upperDistance.getMeters();

				BigDecimal interpolatedMeters = lowerMeters.multiply(BigDecimal.valueOf(1 - weight))
						.add(upperMeters.multiply(BigDecimal.valueOf(weight)));

				BigDecimal roundedInterpolatedMeters = interpolatedMeters.setScale(2, RoundingMode.HALF_UP);
				return new Distance(roundedInterpolatedMeters);

			} else if (lowerMark instanceof Time && upperMark instanceof Time) {
				Time lowerTime = (Time) lowerMark;
				Time upperTime = (Time) upperMark;

				int lowerMinute = lowerTime.getMinute();
				int lowerSecond = lowerTime.getSecond();
				int lowerFracSecond = Integer.parseInt(lowerTime.getFracSecond());

				int upperMinute = upperTime.getMinute();
				int upperSecond = upperTime.getSecond();
				int upperFracSecond = Integer.parseInt(upperTime.getFracSecond());

				int totalLowerFracSecond = lowerMinute * 60 * 100 + lowerSecond * 100 + lowerFracSecond;
				int totalUpperFracSecond = upperMinute * 60 * 100 + upperSecond * 100 + upperFracSecond;

				int interpolatedFracSecond = (int) (totalLowerFracSecond * (1 - weight)
						+ totalUpperFracSecond * weight);
				int finalMinute = (int) (interpolatedFracSecond / 60 / 100);
				interpolatedFracSecond -= finalMinute * 60 * 100;
				int finalSecond = (int) (interpolatedFracSecond / 100);
				interpolatedFracSecond -= finalSecond * 100;
				String finalFracSecond = String.valueOf(interpolatedFracSecond);

				return new Time(finalMinute, finalSecond, finalFracSecond);
			} else if (lowerMark instanceof Points && upperMark instanceof Points) {
				Points lowerPoints = (Points) lowerMark;
				Points upperPoints = (Points) upperMark;

				int lowerPointsValue = lowerPoints.getPoints();
				int upperPointsValue = upperPoints.getPoints();

				int interpolatedPoints = (int) (lowerPointsValue * (1 - weight) + upperPointsValue * weight);

				return new Points(interpolatedPoints);
			}
		}

		// Handle the situation differently based on your requirements.
		// For example, you could throw an exception, return a default value, or handle
		// it in another way.
		throw new IllegalStateException("Unable to determine the percentile mark.");
	}

	static class MarkComparator implements Comparator<Mark> {
		@Override
		public int compare(Mark mark1, Mark mark2) {
			System.out.println(mark1.debug() + " | " + mark2.debug());
			return mark1.compare(mark2);
		}
	}

}
