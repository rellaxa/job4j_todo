package ru.job4j.todo.controller.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TimeZone;

@Component
public class TimeZoneStorage {

	private final Collection<TimeZoneElement> zones = new ArrayList<>();

	public TimeZoneStorage() {
		for (String timeId : TimeZone.getAvailableIDs()) {
			var timeZone = TimeZone.getTimeZone(timeId);
			zones.add(new TimeZoneElement(timeZone.getID(), timeZone.getDisplayName()));
		}
	}

	public Collection<TimeZoneElement> getTimeZones() {
		return zones;
	}

	public TimeZone getDefaultTimeZone() {
		return TimeZone.getDefault();
	}

	public record TimeZoneElement(String timeId, String displayName) {
	}

}
