package com.rs.game.player.actions;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtils;

import skills.Skills;

public class WineTask extends Action {

	int totalWine;
	public WineTask(int totalWine) {
		this.totalWine = totalWine;
	}
	
	int count;

	@Override
	public boolean start(Player player) {
		return true;
	}

	@Override
	public boolean process(Player player) {
		return true;
	}

	private boolean determineBadWine(Player player) {
		if (player.getSkills().getLevel(Skills.COOKING) >= 92)
			return true;
		double badChance = (50.0 - 5.0);
		double cookLevel = player.getSkills().getLevel(Skills.COOKING);
		double levelNeeded = 35;
		double badStop = 92;
		double multi_a = (badStop - levelNeeded);
		double burn_dec = (badChance / multi_a);
		double multi_b = (cookLevel - levelNeeded);
		badChance -= (multi_b * burn_dec);
		double randNum = RandomUtils.nextDouble() * 100.0;
		return badChance <= randNum;
	}

	@Override
	public int processWithDelay(Player player) {
		count++;
		if (count == 16) {
			for (int i = 0; i <= totalWine; i++) {
				if (player.getInventory().containsAny(1995)) {
					player.getInventory().deleteItem(new Item(1995, 1));
					player.getInventory().addItem(new Item(determineBadWine(player) ? 1993 : 1991));
					player.getSkills().addXp(Skills.COOKING, 200 * totalWine);
				}
				if (player.getBank().hasItem(1995)) {
					player.getBank().addItem(new Item(determineBadWine(player) ? 1993 : 1991, player.getBank().getAmountOf(1995)), true);
					player.getBank().forceDeleteItem(player.getBank().getItemSlot(1995), Integer.MAX_VALUE);
					player.getSkills().addXp(Skills.COOKING, 200 * totalWine);
				}
				player.getDetails().getStatistics().addStatistic("Wine_Crafted").addStatistic("Food_Prepared");
			}
			player.getSkillAction().get().cancel();
			player.getAction().forceStop();
			return -1;
		}
		return 1;
	}

	@Override
	public void stop(Player player) {
	}
}