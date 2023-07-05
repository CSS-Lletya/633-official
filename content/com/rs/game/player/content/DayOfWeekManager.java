package com.rs.game.player.content;

import java.util.Calendar;
import java.util.TimeZone;

import com.rs.game.player.Player;

import lombok.Data;

/**
 * Handles new days and new weeks checks that begin on login.
 * If it's a new week or day an event will take place using the handleNewDay / handleNewWeek.
 * 
 * Great for daily tasks, such.
 * @author Dennis
 *
 */
@Data
public class DayOfWeekManager {

	/**
	 * Represents the current calendar & timezone
	 */
	public transient Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
	
	/**
	 * Represents the player involved in this week / day change
	 */
	private transient Player player;
	
	/**
	 * Represents the current day of the week according to the calendar.
	 */
	private transient int today = calendar.get(Calendar.DAY_OF_WEEK);
	
	/**
	 * Represents the saved day itself (Starting off on Sunday)
	 */
	private int savedDay = -1;

	/**
	 * Initializes the {@link DayOfWeekManager} system on login, updates as well
	 */
	public void init() {
		if (savedDay != calendar.get(Calendar.DAY_OF_WEEK)) {
			handleNewDay();
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
				handleNewWeek();
			savedDay = calendar.get(Calendar.DAY_OF_WEEK);
		}
		if (today != calendar.get(Calendar.DAY_OF_WEEK))
			today = calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Processes the {@link DayOfWeekManager} system to consistently check for date changing
	 */
	public void process() {
		if (savedDay != calendar.get(Calendar.DAY_OF_WEEK)) {
			handleNewDay();
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
				handleNewWeek();
			savedDay = calendar.get(Calendar.DAY_OF_WEEK);
		}
		today = calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Represents the day check used for Weekend events (Double experience)
	 * @return
	 */
	public boolean isWeekend() {
		return today == Calendar.FRIDAY || today == Calendar.SATURDAY || today == Calendar.SUNDAY;
	}

	/**
	 * Handles a new day event
	 */
	private void handleNewDay() {
		System.out.println("new day");
	}

	/**
	 * Handles a new week event
	 */
	private void handleNewWeek() {
		System.out.println("new week");
	}
}