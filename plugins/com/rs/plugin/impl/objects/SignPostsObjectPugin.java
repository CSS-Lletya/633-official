package com.rs.plugin.impl.objects;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = { "Signpost" })
public class SignPostsObjectPugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		Signs.VALUES.stream().filter(signpost -> signpost.object == object.getId() && player.withinDistance(signpost.postTile)).forEach(signpost -> {
			player.getPackets().sendBlackOut(2);
			player.getInterfaceManager().sendFullscreenInterface(0,135);
			player.setCloseInterfacesEvent(() -> player.getPackets().sendBlackOut(0));
			player.getPackets().sendIComponentText(135, 3, signpost.directions[0]); // North
			player.getPackets().sendIComponentText(135, 9, signpost.directions[1]); // South
			player.getPackets().sendIComponentText(135, 12, signpost.directions[2]); // West
			player.getPackets().sendIComponentText(135, 8, signpost.directions[3]); // East
		});
	}

	public enum Signs {
		NEAR_LUMBRIDGE(2754, new WorldTile(3235,3228), "North to farms and<br> Varrock.", "The River Lum lies to<br> the south.", "West to<br>Lumbridge.", "East to Al<br>Kharid - toll<br>gate; bring some<br>money."),
		NEAR_VARROCK_1(24263, new WorldTile(3268,3332), "Varrock", "Lumbridge", "Draynor Manor", "Dig Site"),
		NEAR_VARROCK_2(24263, new WorldTile(3283,3333), "Varrock", "Lumbridge", "Draynor Manor", "Dig Site"),
		NEAR_FALADOR(18493, new WorldTile(0,0), "North to the glorious<br>White Knights city of<br>Falador", "South to Reimmington", "West to the<br>Crafting Guild", "East to Port<br>Sarim and<br> Draynor Village"),
		NEAR_EXAM_CENTER(2371, new WorldTile(3335,3348), "Digsite", "Mage Training Arena", "Varrock", "Morytania"),
		NEAR_DRAYNOR(18493, new WorldTile(3107, 3296), "Draynor Manor", "Wizards' Tower", "Port Sarim", "Lumbridge"),
		NEAR_CRAFTING_GUILD(18493, new WorldTile(2983, 3278), "Dark Wizards' Tower", "Melzar's Maze", "Entrana", "Falador"),
		;
		
		private static final ImmutableSet<Signs> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Signs.class));

		/**
		 * The object id.
		 */
		private int object;
		
		private WorldTile postTile;

		/**
		 * The directions.
		 */
		private String directions[];

		/**
		 * Constructs a new {@code ReadSignPostPlugin.java} {@code Object}.
		 * 
		 * @param object
		 *            the object.
		 * @param directions
		 *            the directions.
		 */
		Signs(int object, WorldTile postTile, String... directions) {
			this.object = object;
			this.postTile = postTile;
			this.directions = directions;
		}
	}
}