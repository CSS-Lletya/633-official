package com.rs.plugin.impl.objects;

import java.util.Arrays;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

import skills.woodcutting.Tree;
import skills.woodcutting.Woodcutting;

@ObjectSignature(objectId = {}, name = {"Tree", "Oak", "Yew", "Willow", "Maple", "Magic tree", "Teak", "Mahogany"})
public class TreeObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		object.doAction(optionId, object.getId(), "Chop down", () -> {
			Arrays.stream(Tree.values())
			.filter(data -> Arrays.stream(data.getObject()).anyMatch(ore -> ore.getObjectId() == object.getId())).findFirst()
			.ifPresent(data -> new Woodcutting(player, data, object).start());
		});
	}
}