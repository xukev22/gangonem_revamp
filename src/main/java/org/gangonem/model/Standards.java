package org.gangonem.model;

import java.util.Map;

public class Standards {
	Map<EventType, Mark> existingEventsMapAndTheirTargetStandard;

	public Standards(Map<EventType, Mark> existingEventsMapAndTheirTargetStandard) {
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
