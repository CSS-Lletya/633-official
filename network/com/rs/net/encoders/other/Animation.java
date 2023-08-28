package com.rs.net.encoders.other;

import com.rs.game.player.Player;

import lombok.Data;

@Data
public final class Animation {

	private int[] ids;
	private int speed;

	public Animation(int id) {
		this(id, 0);
	}

	public Animation(int id, int speed) {
		this(id, id, id, id, speed);
	}

	public Animation(int id1, int id2, int id3, int id4, int speed) {
		this.ids = new int[] { id1, id2, id3, id4 };
		this.speed = speed;
	}
	
	/**
	 * Creates an animation event and locks the player for set duration
	 * (Helps minimize codebases)
	 * @param id
	 * @param delay
	 * @param player
	 * @return
	 */
	public Animation lockable(int id, int delay, Player player) {
		player.getMovement().lock(delay);
		player.setNextAnimation(new Animation(id));
		return this;
	}
}
