package com.rs.game;

import java.util.Arrays;
import java.util.Calendar;

import com.rs.GameLoader;
import com.rs.cores.WorldThread;
import com.rs.game.player.content.DayOfWeekManager;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the {@link GameLoader} calendar, this can handle events like
 * double experience weekend, or specific days of the week events, etc..
 * Much like {@link DayOfWeekManager}, it operates similar, without binding to a Player.
 * 
 * {@link WorldThread} handles the calendar updating.
 */
public final class GameCalendar {

	/**
	 * Represents the current day of the week in the game servers calendar
	 */
	@Getter
	@Setter
	public int day;

	/**
	 * Checks if the current calendar day is based around the weekend.
	 * @return
	 */
	public boolean isWeekend() {
		return getDay() == Calendar.FRIDAY || getDay() == Calendar.SATURDAY || getDay() == Calendar.SUNDAY;
	}
	
	/**
	 * Returns true if the following day(s) are not equal to {@link #getDay()}.
	 * @param days
	 * @return
	 */
	public boolean ifDaysMatch(int... days) {
		return Arrays.stream(days).anyMatch(day -> this.day == day);
	}
	
	/**
	 * Checks if {@link #getDay()} is equal to the parameter day, if so perform an action.
	 * @param today
	 * @param event
	 */
	public void doIf(int today, Runnable event) {
		if (today == getDay())
			event.run();
	}
}