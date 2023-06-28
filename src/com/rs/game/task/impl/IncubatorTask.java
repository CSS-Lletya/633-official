package com.rs.game.task.impl;

import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.task.Task;

import lombok.Getter;
import skills.summoning.IncubatorEgg;

public final class IncubatorTask extends Task {

	private final Player player;

	/**
	 * The egg.
	 */
	@Getter
	private final IncubatorEgg egg;

	/**
	 * The end time.
	 */
	public transient int endTime;

	/**
	 * Creates a new {@link IncubatorTask}.
	 */
	public IncubatorTask(Player player, int ticks, int currentTick, IncubatorEgg egg) {
		super(2, false);
		this.player = player;
		this.egg = egg;
		player.getDetails().getIncubatorTimer().set(currentTick);
		this.endTime = getEndTime();
		player.getVarsManager().sendVarBit(4221, 1).submitVarBitToMap(4221, 1);
		player.getVarsManager().sendVarBit(4227, 1).submitVarBitToMap(4227, 1);
		player.getInventory().deleteItem(egg.getEgg());
		player.getPackets().sendGameMessage("You store the egg in the incubator. It will hatch after a while.");
		player.getAttributes().get(Attribute.INCUBATOR_EGG).set(egg.ordinal());
	}

	@Override
	public void execute() {
		player.getDetails().getIncubatorTimer().incrementAndGet();
		System.out.println(player.getDetails().getIncubatorTimer().get());
		if (player.getDetails().getIncubatorTimer().get() >= endTime) {
			player.getPackets()
					.sendGameMessage("Your " + egg.getProduct().getName().toLowerCase() + " has finished hatching.");
			cancel();
		}
	}

	@Override
	public void onCancel() {
	}

	/**
	 * Gets the end time.
	 * 
	 * @return the time.
	 */
	public int getEndTime() {
		return egg == null ? 60000 :  (int) ((egg.getInucbationTime() * 100));
	}
}