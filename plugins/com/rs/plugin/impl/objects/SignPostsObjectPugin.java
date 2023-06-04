package com.rs.plugin.impl.objects;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = {}, name = { "Signpost" })
public class SignPostsObjectPugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		player.getPackets().sendBlackOut(2);
		player.getInterfaceManager().sendInterface(135);
		player.setCloseInterfacesEvent(() -> player.getPackets().sendBlackOut(0));
		Signs.VALUES.stream().filter(signpost -> signpost.object == object.getId()).forEach(signpost -> {
			player.getPackets().sendIComponentText(135, 3, signpost.directions[0]); // North
			player.getPackets().sendIComponentText(135, 9, signpost.directions[1]); // South
			player.getPackets().sendIComponentText(135, 12, signpost.directions[2]); // West
			player.getPackets().sendIComponentText(135, 8, signpost.directions[3]); // East
		});
	}

	public enum Signs {
		NEAR_LUMBRIDGE(2754, "North to farms and<br> Varrock.", "The River Lum lies to<br> the south.", "West to<br>Lumbridge.", "East to Al<br>Kharid - toll<br>gate; bring some<br>money."),
		NEAR_VARROCK(24263, "Varrock", "Lumbridge", "Draynor Manor", "Dig Site"),
		NEAR_FALADOR(18493, "North to the glorious<br>White Knights city of<br>Falador", "South to Reimmington", "West to the<br>Crafting Guild", "East to Port<br>Sarim and<br> Draynor Village")
		;
		
		private static final ImmutableSet<Signs> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Signs.class));

		/**
		 * The object id.
		 */
		private int object;

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
		Signs(int object, String... directions) {
			this.object = object;
			this.directions = directions;
		}
	}
}