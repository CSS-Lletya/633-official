package com.rs.utilities;

public class Ticks {

	public static int fromHours(int hours) {
		return fromMinutes(hours * 60);
	}

	public static int fromMinutes(int minutes) {
		return minutes * 100;
	}

	public static int fromMinutes(double minutes) {
		return (int) (minutes * 100);
	}

	public static int fromSeconds(int seconds) {
		return (int) (seconds / 0.6);
	}
}