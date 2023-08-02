package com.rs.game.task.impl;

import com.rs.game.map.World;
import com.rs.game.task.Task;
import com.rs.utilities.RandomUtility;

import skills.Skills;
import skills.prayer.book.Prayer;

public final class RestoreSkillTask extends Task {

	/**
	 * Creates a new {@link RestoreSkillTask}.
	 */
	public RestoreSkillTask() {
		super(30, false);
	}

	@Override
	public void execute() {
		World.players().forEach(player -> {
			 int ammountTimes = player.getPrayer().active(Prayer.RAPID_RESTORE) ? 2 : 1;
			if (player.getMovement().isResting())
				ammountTimes += 1;
			   boolean berserker = player.getPrayer().active(Prayer.BERSERKER);
			b: for (int skill = 0; skill < 25; skill++) {
				if (skill == Skills.SUMMONING || skill == Skills.PRAYER)
					continue b;
				c: for (int time = 0; time < ammountTimes; time++) {
					int currentLevel = player.getSkills().getLevel(skill);
					int normalLevel = player.getSkills().getTrueLevel(skill);
					if (currentLevel > normalLevel && time == 0) {
						if (skill == Skills.ATTACK || skill == Skills.STRENGTH || skill == Skills.DEFENCE
								|| skill == Skills.RANGE || skill == Skills.MAGIC) {
							if (berserker && RandomUtility.inclusive(100) <= 15)
								continue c;
						}
						player.getSkills().set(skill, currentLevel - 1);
					} else if (currentLevel < normalLevel)
						player.getSkills().set(skill, currentLevel + 1);
					else
						break c;
				}
			}
		});
	}

	@Override
	public void onCancel() {
		World.get().submit(new RestoreSkillTask());
	}
}