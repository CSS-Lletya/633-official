package com.rs.game.player.content;

import java.util.Calendar;
import java.util.TimeZone;

import com.rs.GameConstants;
import com.rs.game.player.Player;
import com.rs.net.host.HostListType;
import com.rs.net.host.HostManager;

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
	 * Processes the {@link DayOfWeekManager} system to consistently check for date changing
	 */
	public void process() {
		if (today != calendar.get(Calendar.DAY_OF_WEEK))
			today = calendar.get(Calendar.DAY_OF_WEEK);
		if (savedDay != calendar.get(Calendar.DAY_OF_WEEK)) {
			handleNewDay();
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
				handleNewWeek();
			savedDay = calendar.get(Calendar.DAY_OF_WEEK);
		}
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
		if (HostManager.contains(player.getUsername(), HostListType.BANNED_IP)) {
			if (player.getDetails().getDaysBanned().get() > 0)
				player.getDetails().getDaysBanned().decrementAndGet();
			if (player.getDetails().getDaysBanned().get() == 0) {
				HostManager.remove(player.getDisplayName(), HostListType.BANNED_IP);
			}
		}
		if (HostManager.contains(player.getUsername(), HostListType.MUTED_IP)) {
			player.getPackets()
			.sendGameMessage("You have been temporarily muted due to breaking a rule.")
			.sendGameMessage("This mute will remain for a further " + player.getDetails().getDaysBanned().get() + " days.")
			.sendGameMessage("To prevent further mute please read the rules.");
			if (player.getDetails().getDaysMuted().get() > 0)
				player.getDetails().getDaysMuted().decrementAndGet();
			if (player.getDetails().getDaysMuted().get() == 0) {
				HostManager.remove(player.getDisplayName(), HostListType.MUTED_IP);
			}
		}
		if (GameConstants.DEBUG)
			System.out.println("new day");
	}

	/**
	 * Handles a new week event
	 */
	private void handleNewWeek() {
		if (GameConstants.DEBUG)
			System.out.println("new week");
	}
}