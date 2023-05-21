package com.rs.plugin.impl.objects;

import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectType;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = { 2465, 2466, 2467, 2468, 2469, 2470, 2471, 2472, 2743, 2474, 2475, 2477, 7133, 7132, 7141,
		7129, 7130, 7131, 7140, 7139, 7137, 7136, 7135, 7134, 2473, 17010 }, name = {})
public class RunecraftingExitPortalsObjectPlugin extends ObjectType {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		int id = object.getId();
		if (id == 2465) {// air
			player.setNextWorldTile(new WorldTile(3127, 3408, 0));
		} else if (id == 2466 && object.getX() == 2793 && object.getY() == 4827) {
			player.setNextWorldTile(new WorldTile(2980, 3512, 0));
		} else if (id == 2467) {// water
			player.setNextWorldTile(new WorldTile(3185, 3163, 0));
		} else if (id == 2468) {// earth
			player.setNextWorldTile(new WorldTile(3304, 3474, 0));
		} else if (id == 2469) {// fire
			player.setNextWorldTile(new WorldTile(3312, 3253, 0));
		} else if (id == 2470) { // body
			player.setNextWorldTile(new WorldTile(3055, 3444, 0));
		} else if (id == 2471) {// cosmic
			player.setNextWorldTile(new WorldTile(2408, 4379, 0));
		} else if (id == 2472) {// chaos
			player.setNextWorldTile(new WorldTile(2857, 3379, 0));
		} else if (id == 2473) {// nature
			player.setNextWorldTile(new WorldTile(2869, 3021, 0));
		} else if (id == 2474) {// law
			player.setNextWorldTile(new WorldTile(3060, 3588, 0));
		} else if (id == 2475) {// death
			player.setNextWorldTile(new WorldTile(1863, 4639, 0));
		} else if (id == 2477) {// blood
			player.setNextWorldTile(new WorldTile(3561, 9779, 0));
		}
	}
}