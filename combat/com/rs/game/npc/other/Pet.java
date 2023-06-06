
package com.rs.game.npc.other;

import com.rs.constants.InterfaceVars;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.familiar.Familiar;
import com.rs.game.player.Player;
import com.rs.game.player.InterfaceManager.Tabs;
import com.rs.game.player.content.pet.PetDetails;
import com.rs.game.player.content.pet.Pets;
import com.rs.utilities.Utility;

/**
 * Represents a pet.
 * 
 * @author Emperor
 * 
 */
public final class Pet extends NPC {

	/**
	 * The owner.
	 */
	private transient final Player owner;

	/**
	 * The "near" directions.
	 */
	private final int[][] checkNearDirs;

	/**
	 * The item id.
	 */
	private final int itemId;

	/**
	 * The pet details.
	 */
	private final PetDetails details;

	/**
	 * The growth rate of the pet.
	 */
	private double growthRate;

	/**
	 * The pets type.
	 */
	private final Pets pet;

	/**
	 * Constructs a new {@code Pet} {@code Object}.
	 * 
	 * @param id     The NPC id.
	 * @param itemId The item id.
	 * @param owner  The owner.
	 * @param tile   The world tile.
	 */
	public Pet(short id, int itemId, Player owner, WorldTile tile, PetDetails details) {
		super(id, tile, null, false);
		this.owner = owner;
		this.itemId = itemId;
		this.checkNearDirs = Utility.getCoordOffsetsNear(super.getSize());
		this.details = details;
		this.pet = Pets.forId(itemId);
		setRun(true);
		sendMainConfigurations();
	}

	@Override
	public void processNPC() {
		unlockOrb();
		if (details.getHunger() >= 90.0 && details.getHunger() < 90.025) {
			owner.getPackets().sendGameMessage("<col=ff0000>Your pet is starving, feed it before it runs off.</col>");
		} else if (details.getHunger() == 100.0) {
			owner.getPetManager().setNpcId(-1);
			owner.getPetManager().setItemId(-1);
			owner.setPet(null);
			owner.getPetManager().removeDetails(itemId);
			owner.getPackets().sendGameMessage("Your pet has ran away to find some food!");
			switchOrb(false);
			owner.getInterfaceManager()
					.removeWindowInterface(owner.getInterfaceManager().isResizableScreen() ? 128 : 188);
			owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 0);
			deregister();
			return;
		}
		if (growthRate > 0.000) {
			details.updateGrowth(growthRate);
			owner.getVarsManager().sendVarBit(4285, (int) details.getGrowth());
			if (details.getGrowth() == 100.0) {
				growNextStage();
			}
		}
		if (!withinDistance(owner, 12)) {
			call();
			return;
		}
		sendFollow();
	}

	/**
	 * Grows into the next stage of this pet (if any).
	 */
	public void growNextStage() {
		if (details.getStage() == 3) {
			return;
		}
		if (pet == null) {
			return;
		}
		int npcId = pet.getNpcId(details.getStage() + 1);
		if (npcId < 1) {
			return;
		}
		details.setStage(details.getStage() + 1);
		int itemId = pet.getItemId(details.getStage());
		if (pet.getNpcId(details.getStage() + 1) > 0) {
			details.updateGrowth(-100.0);
		}
		owner.getPetManager().setItemId(itemId);
		owner.getPetManager().setNpcId(npcId);
		deregister();
		Pet newPet = new Pet((short) npcId, itemId, owner, owner, details);
		newPet.growthRate = growthRate;
		owner.setPet(newPet);
		owner.getPackets().sendGameMessage("<col=ff0000>Your pet has grown larger.</col>");
	}

	/**
	 * Picks up the pet.
	 */
	public void pickup() {
		owner.getInterfaceManager().sendTab(Tabs.INVENTORY);
		owner.getInventory().addItem(itemId, 1);
		owner.setPet(null);
		owner.getPetManager().setNpcId(-1);
		owner.getPetManager().setItemId(-1);
		switchOrb(false);
		owner.getInterfaceManager().removeWindowInterface(owner.getInterfaceManager().isResizableScreen() ? 128 : 188);
		owner.getPackets().sendIComponentSettings(747, 17, 0, 0, 0);
		deregister();
	}

	/**
	 * Calls the pet.
	 */
	public void call() {
		int size = getSize();
		WorldTile teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final WorldTile tile = new WorldTile(new WorldTile(owner.getX() + checkNearDirs[0][dir],
					owner.getY() + checkNearDirs[1][dir], owner.getPlane()));
			if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(), size)) {
				teleTile = tile;
				break;
			}
		}
		if (teleTile == null) {
			return;
		}
		setNextWorldTile(teleTile);
	}

	private void sendFollow() {
		if (getLastFaceEntity() != owner.getClientIndex())
			setNextFaceEntity(owner);
		if (getMovement().isFrozen())
			return;
		int size = getSize();
		int targetSize = owner.getSize();
		if (Utility.colides(getX(), getY(), size, owner.getX(), owner.getY(), targetSize) && !owner.hasWalkSteps()) {
			resetWalkSteps();
			if (!addWalkSteps(owner.getX() + targetSize, getY())) {
				resetWalkSteps();
				if (!addWalkSteps(owner.getX() - size, getY())) {
					resetWalkSteps();
					if (!addWalkSteps(getX(), owner.getY() + targetSize)) {
						resetWalkSteps();
						if (!addWalkSteps(getX(), owner.getY() - size)) {
							return;
						}
					}
				}
			}
			return;
		}
		resetWalkSteps();
		if (!clipedProjectile(owner, true)
				|| !Utility.isOnRange(getX(), getY(), size, owner.getX(), owner.getY(), targetSize, 0))
			calcFollow(owner, 2, true, false);
	}

	/**
	 * Sends the main configurations for the Pet interface (+ summoning orb).
	 */
	public void sendMainConfigurations() {
		switchOrb(true);
		owner.getVarsManager().sendVar(InterfaceVars.PET_ITEM_ID, itemId);// configures
		owner.getVarsManager().sendVar(InterfaceVars.PET_HEAD_ANIMATION, 243269632); // sets npc emote
		unlockOrb(); // temporary
		owner.getInterfaceManager().sendTab(Tabs.FAMILIAR);
	}

	/**
	 * Sends the follower details.
	 */
	public void sendFollowerDetails() {
		owner.getVarsManager().sendVarBit(4285, (int) details.getGrowth());
		owner.getVarsManager().sendVarBit(4286, (int) details.getHunger());
		boolean res = owner.getInterfaceManager().isResizableScreen();
		owner.getInterfaceManager().setWindowInterface(res ? 128 : 188, 662);
		unlock();
	}

	/**
	 * Switch the Summoning orb state.
	 * 
	 * @param enable If the orb should be enabled.
	 */
	public void switchOrb(boolean enable) {
		owner.getVarsManager().sendVar(InterfaceVars.SUMMONING_SWITCH_ORB, enable ? getId() : 0);
		if (enable) {
			unlock();
			return;
		}
		lockOrb();
	}

	/**
	 * Unlocks the orb.
	 */
	public void unlockOrb() {
		owner.getPackets().sendHideIComponent(747, 9, false);
		owner.getVarsManager().sendVar(1160, -1); // unlock summoning orb
		Familiar.sendLeftClickOption(owner);
	}

	/**
	 * Unlocks the interfaces.
	 */
	public void unlock() {
		owner.getPackets().sendHideIComponent(747, 9, false);
	}

	/**
	 * Locks the orb.
	 */
	public void lockOrb() {
		owner.getPackets().sendHideIComponent(747, 9, true);
		owner.getVarsManager().sendVar(1160, 0); // unlock summoning orb
	}

	/**
	 * Gets the details.
	 * 
	 * @return The details.
	 */
	public PetDetails getDetails() {
		return details;
	}

	/**
	 * Gets the growthRate.
	 * 
	 * @return The growthRate.
	 */
	public double getGrowthRate() {
		return growthRate;
	}

	/**
	 * Sets the growthRate.
	 * 
	 * @param growthRate The growthRate to set.
	 */
	public void setGrowthRate(double growthRate) {
		this.growthRate = growthRate;
	}

	/**
	 * Gets the item id of the pet.
	 * 
	 * @return The item id.
	 */
	public int getItemId() {
		return itemId;
	}

}