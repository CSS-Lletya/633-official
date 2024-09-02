package com.rs.game.player.queue.impl;

import com.rs.constants.Animations;
import com.rs.game.item.Item;
import com.rs.game.player.queue.PlayerScript;
import com.rs.game.player.queue.RSQueueType;

public class TestQueueScript extends PlayerScript {

	@Override
	public RSQueueType type() {
		return RSQueueType.Normal;
	}

	@Override
	public void process() {
		if (player.getInventory().contains(new Item(1050))) {
			player.getScripts().removeQueue(this);
			return;
		}
		System.out.println("Hey!");
		animate(Animations.WAVE);
//		super.delay(3);
//		player.getScripts().queue(this);
	}
}