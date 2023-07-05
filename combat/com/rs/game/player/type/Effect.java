// This program is free software: you can redistribute it and/or modify
package com.rs.game.player.type;

import com.rs.game.Entity;
import com.rs.utilities.Colors;

import skills.Skills;
import skills.herblore.Potions;

public enum Effect {
	ANTIPOISON("poison immunity") {
		@Override
		public void apply(Entity player) {
			player.getPoisonDamage().set(0);
		}
	},
	ANTIFIRE("antifire"),
	SUPER_ANTIFIRE("super-antifire"),
	JUJU_MINING("juju mining potion"),
	JUJU_MINE_BANK,
	JUJU_WOODCUTTING("juju woodcutting potion"),
	JUJU_WC_BANK,
	JUJU_FARMING("juju farming potion"),
	JUJU_FISHING("juju fishing potion"),
	SCENTLESS("scentless potion"),
	JUJU_HUNTER("juju hunter potion"),
	SARA_BLESSING("Saradomin's blessing"),
	GUTHIX_GIFT("Guthix's gift"),
	ZAMMY_FAVOR("Zamorak's favour"),

	OVERLOAD("overload") {
		@Override
		public void apply(Entity entity) {
			entity.ifPlayer(player -> Potions.applyOverLoadEffect(player));
		}

		@Override
		public void tick(Entity entity, long tick) {
			entity.ifPlayer(player -> {
				if (tick % 25 == 0)
					Potions.applyOverLoadEffect(player);
			});
		}

		@Override
		public void expire(Entity entity) {
			entity.ifPlayer(player -> {
				if (!player.isDead()) {
					int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
					int realLevel = player.getSkills().getTrueLevel(Skills.ATTACK);
					if (actualLevel > realLevel)
						player.getSkills().set(Skills.ATTACK, realLevel);
					actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
					realLevel = player.getSkills().getTrueLevel(Skills.STRENGTH);
					if (actualLevel > realLevel)
						player.getSkills().set(Skills.STRENGTH, realLevel);
					actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
					realLevel = player.getSkills().getTrueLevel(Skills.DEFENCE);
					if (actualLevel > realLevel)
						player.getSkills().set(Skills.DEFENCE, realLevel);
					actualLevel = player.getSkills().getLevel(Skills.MAGIC);
					realLevel = player.getSkills().getTrueLevel(Skills.MAGIC);
					if (actualLevel > realLevel)
						player.getSkills().set(Skills.MAGIC, realLevel);
					actualLevel = player.getSkills().getLevel(Skills.RANGE);
					realLevel = player.getSkills().getTrueLevel(Skills.RANGE);
					if (actualLevel > realLevel)
						player.getSkills().set(Skills.RANGE, realLevel);
					player.heal(500);
				}
				player.getPackets().sendGameMessage("<col=480000>The effects of overload have worn off and you feel normal again.");
			});
		}
	},

	;

	private String name;

	private Effect(String name) {
		this.name = name;
	}

	private Effect() {
		name = null;
	}

	public void apply(Entity player) {

	}

	public void tick(Entity player, long tickNumber) {

	}

	public void expire(Entity player) {

	}

	public boolean sendWarnings() {
		return name != null;
	}

	public String get30SecWarning() {
		return Colors.red + "Your " + name + " is going to run out in 30 seconds!";
	}

	public String getExpiryMessage() {
		return Colors.red + "Your " + name + " has run out!";
	}
}