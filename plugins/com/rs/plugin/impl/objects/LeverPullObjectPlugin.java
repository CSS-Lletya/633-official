package com.rs.plugin.impl.objects;

import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.task.LinkedTaskSequence;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.magic.TeleportType;

@ObjectSignature(objectId = {}, name = {"Lever"})
public class LeverPullObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		LinkedTaskSequence seq = new LinkedTaskSequence();
		seq.connect(1, () ->  {
			player.getAudioManager().sendSound(Sounds.PULLING_LEVER);
			player.setNextAnimation(Animations.LEVER_PULLING);
		});
		seq.connect(2, () -> {
			switch (object.getId()) {
			case 5959:// wildy to mage bank
				player.getMovement().move(false, new WorldTile(2539, 4712, 0), TeleportType.LEVER);
				break;
			case 5960://mage bank to wildy
				player.getMovement().move(false, new WorldTile(3090, 3956, 0), TeleportType.LEVER);
				break;
			case 1814://ardy to wildy
				player.getMovement().move(false, new WorldTile(3154, 3923, 0), TeleportType.LEVER);
				break;
			case 1815://wildy to ardy
				player.getMovement().move(false, new WorldTile(2561, 3311, 0), TeleportType.LEVER);
				break;
			}
		}).start();
	}
}