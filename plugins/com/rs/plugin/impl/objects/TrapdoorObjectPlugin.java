package com.rs.plugin.impl.objects;

import com.rs.constants.ItemNames;
import com.rs.constants.Sounds;
import com.rs.game.map.GameObject;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.game.task.Task;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;
import com.rs.utilities.RandomUtils;

import skills.Skills;
import skills.magic.TeleportType;

@ObjectSignature(objectId = { 5492, 26933 }, name = {})
public class TrapdoorObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		// options for this object actions returns null, so have to use legacy style.
		if (optionId == 2 && player.getVarsManager().getBitValue(235) == 1) {
			player.getAudioManager().sendSound(Sounds.CLOSING_TRAPDOOR);
			player.getVarsManager().sendVarBit(235, 0);
		}
		if (optionId == 1) {
			if (object.getId() == 5492) {
				if (player.getVarsManager().getBitValue(235) == 1) {
					player.getMovement().move(false, new WorldTile(3149, 9652, 0), TeleportType.BLANK);
				} else
					player.getPackets().sendGameMessage("This trapdoor is sealed shut.");
			}
			if (object.getId() == 26933) {
				player.getMovement().move(false, new WorldTile(3096, 9867, 0), TeleportType.BLANK);
			}
		}
		if (optionId == 3) {
			if (!player.getInventory().containsAny(ItemNames.LOCKPICK_1523)) {
				player.getPackets().sendGameMessage("You need a lockpick to unlock this trapdoor.");
				return;
			}
			player.getPackets().sendGameMessage("You attempt to picklock the trapdoor..");
			player.getMovement().lock(3);
			World.get().submit(new Task(3) {
				@Override
				protected void execute() {
					int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
					int increasedChance = (int) (thievingLevel * 0.5);
					double ratio = RandomUtils.getRandom(100) - increasedChance;
					if (ratio * thievingLevel < 10) {
						player.getPackets().sendGameMessage(
								"You fail to picklock the trapdoor and your hands begin to numb down.");
						player.getSkills().drainLevel(Skills.THIEVING, 1);
						player.getSkills().addXp(Skills.THIEVING, 1);
						cancel();
						return;
					}
					player.getAudioManager().sendSound(Sounds.OPENING_TRAPDOOR);
					player.getPackets().sendGameMessage("You successfully picklock the trapdoor.");
					player.getSkills().addXp(Skills.THIEVING, 4);
					new LinkedTaskSequence().connect(1, () -> player.getVarsManager().sendVarBit(235, 1))
							.connect(15, () -> player.getVarsManager().sendVarBit(235, 0)).start();
					cancel();
				}
			});
		}
	}
}