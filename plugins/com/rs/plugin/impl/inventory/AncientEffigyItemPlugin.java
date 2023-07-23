package com.rs.plugin.impl.inventory;

import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.net.encoders.other.Animation;
import com.rs.plugin.listener.InventoryListener;
import com.rs.plugin.wrapper.InventoryWrapper;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.Utility;

import skills.AncientEffigies;
import skills.Skills;

@InventoryWrapper(itemId = { 18778, 18779, 18780, 18781 }, itemNames = {})
public class AncientEffigyItemPlugin extends InventoryListener {

	private int skill1;
	private int skill2;

	@Override
	public void execute(Player player, Item item, int slot, int option) {
		int random = RandomUtils.random(8);
		skill1 = AncientEffigies.SKILL_1[random];
		skill2 = AncientEffigies.SKILL_2[random];
		player.dialogue(d -> {
			d.mes("As you inspect the ancient effigy, you begin to feel a",
					"strange sensation of the relic searching your mind,", "drawing on your knowledge.");
			d.mes("Images from your experiences of " + AncientEffigies.getMessage(skill1), "fill your mind.");
			d.option("Focus on " + Skills.SKILL_NAME[skill1], () -> {
				if (player.getSkills().getTrueLevel(skill1) < AncientEffigies.getRequiredLevel(item.getId())) {
					d.mes("The images in your mind fade; the ancient effigy seems",
							"to desire knowledge of experiences you have not yet", "had.");
					player.setNextAnimation(new Animation(4067));
					player.getPackets().sendGameMessage("You require at lest level " + AncientEffigies.getRequiredLevel(item.getId()) + " " + Skills.SKILL_NAME[skill1] + " to investigate the ancient effigy further.");
					return;
				} else {
					if (item.getId() != 18781)
						player.setNextAnimation(new Animation(4068));
					d.mes("As you focus on your memories, you can almost hear a", "voice in the back of your mind whispering to you...");
					d.event(() -> {
						int xp = AncientEffigies.getExp(item.getId());
						player.getSkills().addExperience(skill1, xp);
						player.getPackets().sendGameMessage("You have gained " + Utility.getFormattedNumber(xp) + " "
								+ Skills.SKILL_NAME[skill1] + " experience!");
						AncientEffigies.effigyInvestigation(player, item.getId());
						player.dialogue(end -> end.mes("The ancient effigy glows briefly; it seems changed",
								"somehow and no longer responds to the same memories", "as before."));
					});
				}

			}, "Focus on " + Skills.SKILL_NAME[skill2], () -> {
				if (player.getSkills().getTrueLevel(skill2) < AncientEffigies.getRequiredLevel(item.getId())) {
					d.mes("The images in your mind fade; the ancient effigy seems",
							"to desire knowledge of experiences you have not yet", "had.");
					player.setNextAnimation(new Animation(4067));
					player.getPackets().sendGameMessage("You require at lest level " + AncientEffigies.getRequiredLevel(item.getId()) + " " + Skills.SKILL_NAME[skill2] + " to investigate the ancient effigy further.");
					return;
				} else {
					if (item.getId() != 18781)
						player.setNextAnimation(new Animation(4068));
					d.mes("As you focus on your memories, you can almost hear a", "voice in the back of your mind whispering to you...");
					d.event(() -> {
						int xp = AncientEffigies.getExp(item.getId());
						player.getSkills().addExperience(skill2, xp);
						player.getPackets().sendGameMessage("You have gained " + Utility.getFormattedNumber(xp) + " "
								+ Skills.SKILL_NAME[skill2] + " experience!");
						AncientEffigies.effigyInvestigation(player, item.getId());
						player.dialogue(end -> end.mes("The ancient effigy glows briefly; it seems changed",
								"somehow and no longer responds to the same memories", "as before."));
					});
				}
			});
		});
	}
}