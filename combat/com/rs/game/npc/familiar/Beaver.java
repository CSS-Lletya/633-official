package com.rs.game.npc.familiar;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;
import com.rs.game.player.content.Summoning.Pouch;

public class Beaver extends Familiar {

	public Beaver(Player owner, Pouch pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Multichop";
	}

	@Override
	public String getSpecialDescription() {
		return "Chops a tree, giving the owner its logs. There is also a chance that random logs may be produced.";
	}

	@Override
	public int getBOBSize() {
		return 30;
	}

	@Override
	public int getSpecialAmount() {
		return 3;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ENTITY;
	}

	@Override
	public boolean submitSpecial(Object context) {
		/*
		 * Player player = (Player) context; objectLoop : for (WorldObject object :
		 * World.getRegion(player.getRegionId()).getAllObjects()) { if
		 * (object.getDefinitions().containsOption("Chop down") &&
		 * player.withinDistance(object, 2)) { player.setNextWorldTile(object);
		 * setForceWalk(object); setNextFaceWorldTile(object); ObjectDefinitions
		 * objectDef = object.getDefinitions(); switch(objectDef.name.toLowerCase()) {
		 * case "tree": if (objectDef.containsOption(0, "Chop down"))
		 * player.getActionManager().setAction(new Woodcutting(object,
		 * TreeDefinitions.NORMAL, true)); break objectLoop; case "evergreen": if
		 * (objectDef.containsOption(0, "Chop down"))
		 * player.getActionManager().setAction(new Woodcutting(object,
		 * TreeDefinitions.EVERGREEN, true)); break objectLoop; case "dead tree": if
		 * (objectDef.containsOption(0, "Chop down"))
		 * player.getActionManager().setAction(new Woodcutting(object,
		 * TreeDefinitions.DEAD, true)); break objectLoop; case "oak": if
		 * (objectDef.containsOption(0, "Chop down"))
		 * player.getActionManager().setAction(new Woodcutting(object,
		 * TreeDefinitions.OAK, true)); break objectLoop; case "willow": if
		 * (objectDef.containsOption(0, "Chop down"))
		 * player.getActionManager().setAction(new Woodcutting(object,
		 * TreeDefinitions.WILLOW, true)); break objectLoop; case "maple tree": if
		 * (objectDef.containsOption(0, "Chop down"))
		 * player.getActionManager().setAction(new Woodcutting(object,
		 * TreeDefinitions.MAPLE, true)); break objectLoop; case "ivy": if
		 * (objectDef.containsOption(0, "Chop")) player.getActionManager().setAction(new
		 * Woodcutting(object, TreeDefinitions.IVY, true)); break objectLoop; case
		 * "yew": if (objectDef.containsOption(0, "Chop down"))
		 * player.getActionManager().setAction(new Woodcutting(object,
		 * TreeDefinitions.YEW, true)); break objectLoop; case "magic tree": if
		 * (objectDef.containsOption(0, "Chop down"))
		 * player.getActionManager().setAction(new Woodcutting(object,
		 * TreeDefinitions.MAGIC, true)); break objectLoop; case "cursed magic tree": if
		 * (objectDef.containsOption(0, "Chop down"))
		 * player.getActionManager().setAction(new Woodcutting(object,
		 * TreeDefinitions.CURSED_MAGIC, true)); break objectLoop; } } return true; }
		 * System.out.println("FALSE"); player.getPackets().sendGameMessage
		 * ("Your beaver cannot seem to find any trees around your location."); return
		 * false;
		 */
		return false;
	}
}
