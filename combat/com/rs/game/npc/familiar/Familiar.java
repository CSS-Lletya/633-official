package com.rs.game.npc.familiar;

import java.util.Optional;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.InterfaceVars;
import com.rs.game.Entity;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.npc.combat.NPCCombatDefinitions;
import com.rs.game.player.Player;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.Summoning;
import com.rs.game.player.content.Summoning.Pouch;
import com.rs.game.task.Task;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.Graphics;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.Utility;

public abstract class Familiar extends NPC {

	private transient Player owner;
	private int ticks;
	private int trackTimer;
	private int specialEnergy;
	private transient boolean finished = false;
	private boolean trackDrain;

	private BeastOfBurden bob;
	private Pouch pouch;

	public Familiar(Player owner, Pouch pouch, WorldTile tile, boolean canBeAttackFromOutOfArea) {
		super((short) Summoning.getNPCId(pouch.getRealPouchId()), tile,
				canBeAttackFromOutOfArea, false);
		this.owner = owner;
		this.pouch = pouch;
		resetTickets();
		specialEnergy = 60;
		if (getBOBSize() > 0)
			bob = new BeastOfBurden(canDepositOnly(), getBOBSize());
		call(true);
		setRun(true);
	}

	public void store() {
		if (bob == null)
			return;
		bob.open();
	}

	public boolean canDepositOnly() {
		return getDefinitions().hasOptions("withdraw");
	}

	public boolean canStoreEssOnly() {
		return pouch == Pouch.ABYSSAL_LURKER || pouch == Pouch.ABYSSAL_PARASITE || pouch == Pouch.ABYSSAL_TITAN;
	}

	public int getOriginalId() {
		return Summoning.getNPCId(pouch.getRealPouchId());
	}

	public void resetTickets() {
		ticks = (int) (pouch.getPouchTime() / 1000 / 30);
		trackTimer = 0;
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

	@Override
	public void processNPC() {
		if (isDead())
			return;
		unlockOrb();
		trackTimer++;
		if (trackTimer == 50) {
			trackTimer = 0;
			ticks--;
			if (trackDrain)
				owner.getSkills().drainSummoning(1);
			trackDrain = !trackDrain;
			if (ticks == 2)
				owner.getPackets().sendGameMessage("You have 1 minute before your familiar vanishes.");
			else if (ticks == 1)
				owner.getPackets().sendGameMessage("You have 30 seconds before your familiar vanishes.");
			else if (ticks == 0) {
				removeFamiliar();
				dissmissFamiliar(false);
				return;
			}
			sendTimeRemaining();
		}
		short originalId = (short) (getOriginalId() + 1);
		if (owner.isCanPvp() && getId() == getOriginalId()) {
			setNextNPCTransformation(originalId);
			call(false);
			return;
		} else if (!owner.isCanPvp() && getId() == originalId && pouch != Pouch.MAGPIE && pouch != Pouch.IBIS
				&& pouch != Pouch.BEAVER && pouch != Pouch.MACAW && pouch != Pouch.FRUIT_BAT) {
			setNextNPCTransformation((short) (originalId - 1));
			call(false);
			return;
		} else if (!withinDistance(owner, 12)) {
			call(false);
			return;
		}
		if (!getCombat().process()) {
			if (isAgressive() && owner.getAttackedBy() != null
					&& owner.getAttackedByDelay() > Utility.currentTimeMillis() && canAttack(owner.getAttackedBy())
					&& RandomUtils.inclusive(25) == 0)
				getCombat().setTarget(owner.getAttackedBy());
			else
				sendFollow();
		}
	}

	public boolean canAttack(Entity target) {
		target.ifPlayer(targetPlayer -> {
			if (!owner.isCanPvp() || !targetPlayer.isCanPvp())
				return;
		});
		return !target.isDead()
				&& ((owner.isMultiArea() && isMultiArea() && target.isMultiArea())
						|| (owner.isForceMultiArea() && target.isForceMultiArea()))
				&& owner.getMapZoneManager().execute(owner, controller -> controller.canAttack(owner, target));
	}

	public boolean renewFamiliar() {
		if (ticks > 5) {
			owner.getPackets().sendGameMessage(
					"You need to have at least two minutes and fifty seconds remaining before you can renew your familiar.",
					true);
			return false;
		} else if (!owner.getInventory().getItems().contains(new Item(pouch.getRealPouchId(), 1))) {
			owner.getPackets()
					.sendGameMessage("You need a "
							+ ItemDefinitions.getItemDefinitions(pouch.getRealPouchId()).getName().toLowerCase()
							+ " to renew your familiar's timer.");
			return false;
		}
		resetTickets();
		owner.getInventory().deleteItem(pouch.getRealPouchId(), 1);
		call(true);
		owner.getPackets().sendGameMessage("You use your remaining pouch to renew your familiar.");
		return true;
	}

	public void takeBob() {
		if (bob == null)
			return;
		bob.takeBob();
	}

	public void sendTimeRemaining() {
		owner.getVarsManager().sendVar(InterfaceVars.SUMMONING_TIME_REMAINING, ticks * 65);
	}

	public void sendMainConfigs() {
		switchOrb(true);
		owner.getVarsManager().sendVar(InterfaceVars.SUMMONING_POUCH_ID, pouch.getRealPouchId());
		owner.getVarsManager().sendVar(InterfaceVars.SUMMONING_HEAD_ANIMATION, 243269632);
		refreshSpecialEnergy();
		sendTimeRemaining();
		owner.getVarsManager().sendVar(InterfaceVars.SUMMONING_SPECIAL_AMOUNT, getSpecialAmount() << 23);// check
		owner.getPackets().sendGlobalString(204, getSpecialName());
		owner.getPackets().sendGlobalString(205, getSpecialDescription());
		owner.getPackets().sendGlobalConfig(1436, getSpecialAttack() == SpecialAttack.CLICK ? 1 : 0);
		unlockOrb(); // temporary
	}

	public void sendFollowerDetails() {
		boolean res = owner.getInterfaceManager().isResizableScreen();
		owner.getInterfaceManager().setWindowInterface(res ? 128 : 188, 662);
		owner.getPackets().sendHideIComponent(662, 44, true);
		owner.getPackets().sendHideIComponent(662, 45, true);
		owner.getPackets().sendHideIComponent(662, 46, true);
		owner.getPackets().sendHideIComponent(662, 47, true);
		owner.getPackets().sendHideIComponent(662, 48, true);
		owner.getPackets().sendHideIComponent(662, 71, false);
		owner.getPackets().sendHideIComponent(662, 72, false);
		unlock();
	}

	public void switchOrb(boolean on) {
		owner.getVarsManager().sendVar(InterfaceVars.SUMMONING_SWITCH_ORB, on ? -1 : 0);
		if (on)
			unlock();
		else
			lockOrb();
	}

	public void unlockOrb() {
		owner.getVarsManager().sendVar(1160, -1); // unlock summoning orb
		owner.getPackets().sendHideIComponent(747, 9, false);
		sendLeftClickOption(owner);
		unlock();
	}

	public static void selectLeftOption(Player player) {
		boolean res = player.getInterfaceManager().isResizableScreen();
		sendLeftClickOption(player);
		player.getInterfaceManager().setWindowInterface(res ? 128 : 188, 880);
		player.getInterfaceManager().openGameTab(95);
	}

	public static void confirmLeftOption(Player player) {
		player.getPackets().sendGlobalConfig(168, 4);// inv tab id
		boolean res = player.getInterfaceManager().isResizableScreen();
		player.getInterfaceManager().removeWindowInterface(res ? 128 : 188);
	}

	public static void setLeftclickOption(Player player, byte summoningLeftClickOption) {
		if (summoningLeftClickOption == player.getDetails().getSummoningLeftClickOption())
			return;
		player.getDetails().setSummoningLeftClickOption(summoningLeftClickOption);
		sendLeftClickOption(player);
	}

	public static void sendLeftClickOption(Player player) {
		player.getVarsManager().sendVar(InterfaceVars.SUMMONING_LEFT_CLICK_OPTION, player.getDetails().getSummoningLeftClickOption());
		player.getVarsManager().sendVar(InterfaceVars.SUMMONING_EXTRA_LEFT_CLICK_OPTION, player.getDetails().getSummoningLeftClickOption());
	}

	public void unlock() {
		switch (getSpecialAttack()) {
		case CLICK:
			owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 2);
			owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 2);
			break;
		case ENTITY:
			owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 20480);
			owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 20480);
			break;
		case OBJECT:
			owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 78321);
			owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 78321);
			break;
		case ITEM:
			owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 65536);
			owner.getPackets().sendIComponentSettings(662, 74, 0, 0, 65536);
			break;
		}
		owner.getPackets().sendHideIComponent(747, 9, false);
	}

	public void lockOrb() {
		owner.getVarsManager().sendVar(1160, 0); // unlock summoning orb
		owner.getPackets().sendHideIComponent(747, 9, true);
	}

	private transient int[][] checkNearDirs;
	private transient boolean sentRequestMoveMessage;

	public void call() {
		if (isDead())
			return;
		if (getAttackedBy() != null && getAttackedByDelay() > Utility.currentTimeMillis()) {
			// TODO or something as this
			owner.getPackets().sendGameMessage("You cant call your familiar while it under combat.");
			return;
		}
		call(false);
		sendFollowerDetails();
	}

	public void call(boolean login) {
		int size = getSize();
		if (login) {
			if (bob != null)
				bob.setEntitys(owner, this);
			checkNearDirs = Utility.getCoordOffsetsNear(size);
			sendMainConfigs();
		} else
			getCombat().removeTarget();
		WorldTile teleTile = null;
		for (int dir = 0; dir < checkNearDirs[0].length; dir++) {
			final WorldTile tile = new WorldTile(new WorldTile(owner.getX() + checkNearDirs[0][dir],
					owner.getY() + checkNearDirs[1][dir], owner.getPlane()));
			if (World.isTileFree(tile.getPlane(), tile.getX(), tile.getY(), size)) { // if
				// found
				// done
				teleTile = tile;
				break;
			}
		}
		if (login || teleTile != null)
			World.get().submit(new Task(1) {
				@Override
				protected void execute() {
					setNextGraphics(new Graphics(getDefinitions().size > 1 ? 1315 : 1314));
					this.cancel();
				}
			});
		if (teleTile == null) {
			if (!sentRequestMoveMessage) {
				owner.getPackets().sendGameMessage("Theres not enough space for your familiar appear.");
				sentRequestMoveMessage = true;
			}
			return;
		}
		sentRequestMoveMessage = false;
		setNextWorldTile(teleTile);
	}

	public void removeFamiliar() {
		owner.setFamiliar(null);
	}

	public void dissmissFamiliar(boolean logged) {
		deregister();
		if (!logged && !isFinished()) {
			setFinished(true);
			switchOrb(false);
			owner.getInterfaceManager()
					.removeWindowInterface(owner.getInterfaceManager().isResizableScreen() ? 128 : 188);
			owner.getPackets().sendIComponentSettings(747, 18, 0, 0, 0);
			if (bob != null)
				bob.dropBob();
		}
	}

	private transient boolean dead;

	@Override
	public boolean isDead() {
		return dead || super.isDead();
	}

	@Override
	public void sendDeath(Optional<Entity> source) {
		if (dead)
			return;
		dead = true;
		removeFamiliar();
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		setCantInteract(true);
		getCombat().removeTarget();
		setNextAnimation(null);
		World.get().submit(new Task(1) {
			int loop;

			@Override
			protected void execute() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathAnim()));
					owner.getPackets().sendGameMessage("Your familiar slowly begins to fade away..");
				} else if (loop >= defs.getDeathDelay()) {
					dissmissFamiliar(false);
					this.cancel();
				}
				loop++;
				this.cancel();
			}
		});
	}

	public void respawnFamiliar(Player owner) {
		this.owner = owner;
		initEntity();
		spawn();
		call(true);
	}

	public abstract String getSpecialName();

	public abstract String getSpecialDescription();

	public abstract int getBOBSize();

	public abstract int getSpecialAmount();

	public abstract SpecialAttack getSpecialAttack();

	public abstract boolean submitSpecial(Object object);

	public boolean isAgressive() {
		return true;
	}

	public static enum SpecialAttack {
		ITEM, ENTITY, CLICK, OBJECT
	}

	public BeastOfBurden getBob() {
		return bob;
	}

	public void refreshSpecialEnergy() {
		owner.getVarsManager().sendVar(InterfaceVars.SUMMONING_REFRESH_SPECIAL_ENGERY, specialEnergy);
	}

	public void restoreSpecialAttack(int energy) {
		if (specialEnergy >= 60)
			return;
		specialEnergy = energy + specialEnergy >= 60 ? 60 : specialEnergy + energy;
		refreshSpecialEnergy();
	}

	public void setSpecial(boolean on) {
		if (!on)
			owner.getAttributes().get(Attribute.FAMILIAR_SPECIAL).set(false);
		else {
			if (specialEnergy < getSpecialAmount()) {
				owner.getPackets().sendGameMessage("Your special move bar is too low to use this scroll.");
				return;
			}
			owner.getAttributes().get(Attribute.FAMILIAR_SPECIAL).set(true);
		}
	}

	public void drainSpecial(int specialReduction) {
		specialEnergy -= specialReduction;
		if (specialEnergy < 0) {
			specialEnergy = 0;
		}
		refreshSpecialEnergy();
	}

	public void drainSpecial() {
		specialEnergy -= getSpecialAmount();
		refreshSpecialEnergy();
	}

	public boolean hasSpecialOn() {
		if (owner.getAttributes().get("FamiliarSpec").get() != null) {
			int scrollId = Summoning.getScrollId(pouch.getRealPouchId());
			if (!owner.getInventory().containsItem(scrollId, 1)) {
				owner.getPackets().sendGameMessage("You don't have the scrolls to use this move.");
				return false;
			}
			owner.getInventory().deleteItem(scrollId, 1);
			drainSpecial();
			return true;
		}
		return false;
	}

	public Player getOwner() {
		return owner;
	}

	public boolean isFinished() {
		return finished;
	}

	public Pouch getPouch() {
		return pouch;
	}
}
