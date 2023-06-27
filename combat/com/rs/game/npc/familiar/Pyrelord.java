package com.rs.game.npc.familiar;

import com.rs.game.map.WorldTile;
import com.rs.game.player.Player;

import skills.summoning.Summoning.Pouch;

public class Pyrelord extends Familiar {

	public Pyrelord(Player owner, Pouch pouch, WorldTile tile,
			boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Immense Heat";
	}

	@Override
	public String getSpecialDescription() {
		return "Craft a gold bar (and a gem if one wishes) into an item of Jewellery without using a furnace.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 5;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.ITEM;
	}

	@Override
	public boolean submitSpecial(Object object) {
		return false;
	}

//    public static boolean lightLog(final Familiar familiar, final Item item) {
//	final Player player = familiar.getOwner();
//	final Fire fire = Firemaking.getFire(item.getId());
//	if (fire == null)
//	    return false;
//	Long time = (Long) familiar.getTemporaryAttributtes().get("Fire");
//	if (Firemaking.checkAll(player, fire, true) && (time != null && time < Utils.currentTimeMillis() || time == null)) {
//	    player.getPackets().sendGameMessage("The pyrelord attempts to light the logs.");
//	    player.getInventory().deleteItem(fire.getLogId(), 1);
//	    final WorldTile tile = new WorldTile(familiar);
//	    World.addGroundItem(new Item(fire.getLogId(), 1), tile, player, true, 180);
//	    familiar.setNextAnimation(new Animation(8085));
//	    WorldTasksManager.schedule(new WorldTask() {
//
//		@Override
//		public void run() {
//		    if (!familiar.addWalkSteps(familiar.getX() - 1, familiar.getY(), 1))
//			if (!familiar.addWalkSteps(familiar.getX() + 1, familiar.getY(), 1))
//			    if (!familiar.addWalkSteps(familiar.getX(), familiar.getY() + 1, 1))
//				familiar.addWalkSteps(familiar.getX(), familiar.getY() - 1, 1);
//		    player.getPackets().sendGameMessage("The pyrelord uses its intense heat to light the logs.");
//		    WorldTasksManager.schedule(new WorldTask() {
//			@Override
//			public void run() {
//			    final FloorItem item = World.getRegion(tile.getRegionId()).getGroundItem(fire.getLogId(), tile, player);
//			    if (item == null)
//				return;
//			    if (!World.removeGroundItem(player, item, false))
//				return;
//			    World.spawnTempGroundObject(new WorldObject(fire.getFireId(), 10, 0, tile.getX(), tile.getY(), tile.getPlane()), 592, fire.getLife());
//			    player.getSkills().addXp(Skills.FIREMAKING, fire.getExperience());
//			    familiar.setNextFaceWorldTile(tile);
//			}
//		    }, 1);
//		    familiar.getTemporaryAttributtes().put("Fire", Utils.currentTimeMillis() + 1800);
//		}
//	    }, 2);
//	}
//	return true;
//    }
}
