package com.rs.plugin.impl.objects;

import com.rs.game.dialogue.Mood;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.plugin.listener.ObjectListener;
import com.rs.plugin.wrapper.ObjectSignature;

@ObjectSignature(objectId = { 2412, 2413, 17404, 17405, 2083, 2084, 14304, 14305, 2593, 2415, 2414, 2081, 2082, 17398,
		17399, 17394, 17395, 69, 17401, 17400, 2078, 2088, 2086, 2085, 17402, 17403, 17392, 17393, 11209, 14307, 14306,
		2590, 2592, 2594 }, name = {})
public class PortObjectsObjectPlugin extends ObjectListener {

	@Override
	public void execute(Player player, GameObject object, int optionId) throws Exception {
		switch(object.getId()) {
		case 2594:
			cross(player, new WorldTile(3047, 3204, 0));
			break;
		case 14306:
			cross(player, PEST_CONTROL[0]);
			player.getPackets().sendGameMessage("You board the ship.");
			break;
		case 14307:
			cross(player, PEST_CONTROL[1]);
			player.getPackets().sendGameMessage("You disembark the ship.");
			break;
		case 2593:
			cross(player, new WorldTile(3047, 3207, 1));
			break;
		case 14304:
			cross(player, PORT_SARIM[6]);
			player.getPackets().sendGameMessage("You board the ship.");
			break;
		case 14305:
			cross(player, PORT_SARIM[7]);
			player.getPackets().sendGameMessage("You disembark the ship.");
			break;
		case 2083:
			cross(player, PORT_SARIM[4]);//port - sarim random boat.
			break;
		case 2084:
			cross(player, PORT_SARIM[5]);//port - sarim random boat.
			break;
		case 17404:
			cross(player, PORT_SARIM[3]);//port sarim charter boat.
			break;
		case 17405:
			cross(player, PORT_SARIM[2]);//port sarim charter boat.
			break;
		case 11209:
			player.dialogue(d -> d.player(Mood.sad, "I don't think that whoever owns this ship will be happy with me wandering all over it."));
			break;
		case 17392:
			cross(player, PORT_PHASMATYS[0]);//port phasmatys on.
			break;
		case 17393:
			cross(player, PORT_PHASMATYS[1]);//port phasmatys off.
			break;
		case 17402:
			cross(player, PORT_KHAZARD[0]);//port khazard on.
			break;
		case 17403:
			cross(player, PORT_KHAZARD[1]);//port khazard off.
			break;
		case 2086:
			cross(player, ARDOUGNE[1]);//ardougne off
			break;
		case 2085:
			cross(player, ARDOUGNE[0]);//ardougne on.
			player.getPackets().sendGameMessage("You must speak to Captain Barnaby before it will set sail.");
			break;
		case 2087:
			cross(player, BRIMHAVEN[2]);//brimhaven second ship(pay fare)
			player.getPackets().sendGameMessage("You must speak to the Customs Officer before it will set sail.");
			break;
		case 2088:
			cross(player, BRIMHAVEN[3]);//brimhaven second ship(pay fare)
			break;
		case 17401:
			cross(player, BRIMHAVEN[1]);//entrana on boat.
			break;
		case 17400:
			cross(player, BRIMHAVEN[0]);//entrana off boat.
			break;
		case 69://arhein ship.
//			player.getDialogueInterpreter().open(563, Entity.findNPC(563), true);
			break;
		case 17394:
			cross(player, CATHERBY[0]);//catherby on
			break;
		case 17395:
			cross(player, CATHERBY[1]);//catherby off
			break;
		case 2412://port-sarim to entrana(on boat)
			cross(player, PORT_SARIM[0]);
			break;
		case 2413://port-sarim to entrana(off boat)
			cross(player, PORT_SARIM[1]);
			break;
		case 2414://entrana on boat.
			cross(player, ENTRANA[0]);
			break;
		case 2415://entrana off boat.
			cross(player, ENTRANA[1]);
			break;
		case 2081:
			cross(player, KARAMJA[0]);
			break;
		case 2082:
			cross(player, KARAMJA[1]);
			break;
		case 17398:
			cross(player, KARAMJA[2]);
			player.getPackets().sendGameMessage("You must speak to the Customs Officer before it will set sail.");
			break;
		case 17399:
			cross(player, KARAMJA[3]);
			player.getPackets().sendGameMessage("You must speak to the Customs Officer before it will set sail.");
			break;
		}
	}
	
	/**
	 * Represents the WorldTiles of crossing a gang plank at port sarim.
	 */
	private final WorldTile PORT_SARIM[] = new WorldTile[] {/*port sarim - entrana boat*/new WorldTile(3048, 3231, 1), new WorldTile(3048, 3234, 0), new WorldTile(3038, 3192, 0), new WorldTile(3038, 3189, 1), new WorldTile(3032, 3217, 1), new WorldTile(3029, 3217, 0), new WorldTile(3041, 3199, 1), new WorldTile(3041, 3202, 0)};

	/**
	 * Represents the entrana WorldTiles.
	 */
	private final WorldTile ENTRANA[] = new WorldTile [] { /*entrana  boat*/ new WorldTile(2834, 3331, 1), new WorldTile(2834, 3335, 0)};

	/**
	 * Represents the karamja WorldTiles.
	 */
	private final WorldTile KARAMJA[] = new WorldTile [] {new WorldTile(2956, 3143, 1), new WorldTile(2956, 3146, 0), new WorldTile(2957, 3158, 1), new WorldTile(2954, 3158, 0)};

	/**
	 * Represents the catherby WorldTiles.
	 */
	private final WorldTile CATHERBY[] = new WorldTile [] {new WorldTile(2792, 3417, 1), new WorldTile(2792, 3414, 0)};

	/**
	 * Represents the WorldTile teleport to in brimhaven.
	 */
	private final WorldTile BRIMHAVEN[] = new WorldTile [] {new WorldTile(2763, 3238, 1), new WorldTile(2760, 3238, 0), new WorldTile(2775, 3234, 1), new WorldTile(2772, 3234, 0)};

	/**
	 * Represents the ardougne WorldTile.
	 */
	private final WorldTile ARDOUGNE[] = new WorldTile[] {new WorldTile(2683, 3268, 1), new WorldTile(2683, 3271, 0)};

	/**
	 * Represents the port khazard WorldTile.
	 */
	private final WorldTile PORT_KHAZARD[] = new WorldTile[] {new WorldTile(2674, 3141, 1), new WorldTile(2674, 3144, 0)};

	/**
	 * represents the port phastmatys WorldTiles.
	 */
	private final WorldTile PORT_PHASMATYS[] = new WorldTile[] {new WorldTile(3705, 3503, 1), new WorldTile(3702, 3503, 0)};

	/**
	 * Represents the pest control WorldTile.
	 */
	private final WorldTile PEST_CONTROL[] = new WorldTile[] {new WorldTile(2662, 2676, 1), new WorldTile(2659, 2676, 0)};
	
	/**
	 * Method used to cross a gang plank.
	 * @param player the player.
	 * @param location
	 */
	public final void cross(final Player player, WorldTile location) {
		player.setNextWorldTile(location);
	}
}