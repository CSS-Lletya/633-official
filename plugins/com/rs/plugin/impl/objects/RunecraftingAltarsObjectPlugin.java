package com.rs.plugin.impl.objects;

import java.util.Arrays;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.runecrafting.Altar;
import skills.runecrafting.Runecrafting;

@ObjectSignature(objectId = {2478, 2479, 2480, 2481, 2482, 2483, 2484, 2485, 2486, 2487, 2488, 30624}, name = {})
public class RunecraftingAltarsObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		Arrays.stream(Altar.values()).filter(altar -> altar.getObjectId() == object.getId())
		.forEach(altar -> new Runecrafting(player, object, altar).start());
	}
}