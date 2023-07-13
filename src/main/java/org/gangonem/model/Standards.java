package org.gangonem.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Standards {
	Map<EventType, Mark> existingEventsMapAndTheirTargetStandard;

	@JsonCreator
	public Standards(
			@JsonProperty("existingEventsMapAndTheirTargetStandard") Map<EventType, Mark> existingEventsMapAndTheirTargetStandard) {
		this.existingEventsMapAndTheirTargetStandard = existingEventsMapAndTheirTargetStandard;
	}

	public Map<EventType, Mark> getExistingEventsMapAndTheirTargetStandard() {
		return existingEventsMapAndTheirTargetStandard;
	}

	public void setExistingEventsMapAndTheirTargetStandard(
			Map<EventType, Mark> existingEventsMapAndTheirTargetStandard) {
		this.existingEventsMapAndTheirTargetStandard = existingEventsMapAndTheirTargetStandard;
	}

}
