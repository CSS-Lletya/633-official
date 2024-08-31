package com.rs;

import com.rs.game.GameCalendar;
import com.rs.utilities.LogUtility;
import com.rs.utilities.LogUtility.LogType;
import com.rs.utilities.Utility;

import lombok.Getter;

/**
 * The Runnable source of open633 This is where we start our start-up services,
 * etc.. to build our game. The Client then connects and communicates specific
 * data with the server concurrently
 * 
 * @author Dennis
 *
 */
public class Launcher {

	/**
	 * The Runnable, we're nothing without you <3
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GameProperties.getGameProperties().load();

		long currentTime = Utility.currentTimeMillis();
		GameLoader.load();

		LogUtility.log(LogType.INFO,
				"Server took " + (Utility.currentTimeMillis() - currentTime) + " milli seconds to launch.");
	}
	
	/**
	 * Represents the state of the current calendar.
	 */
	@Getter
	public static GameCalendar calendar = new GameCalendar();
	
}