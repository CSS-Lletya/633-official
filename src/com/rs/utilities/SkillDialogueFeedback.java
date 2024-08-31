package com.rs.utilities;

/**
 * Handles a response from the Skills chatbox interface(interface 905)
 * @author Dennis
 *
 */
public interface SkillDialogueFeedback {

	/**
	 * Handles a specific interaction upon button selection
	 * @param button
	 */
	public void run(int input);

}