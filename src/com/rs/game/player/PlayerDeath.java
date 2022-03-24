package com.rs.game.player;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.GameConstants;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.WorldTile;
import com.rs.game.player.controller.Controller;
import com.rs.game.player.controller.Controller.ControllerSafety;
import com.rs.game.player.controller.ControllerHandler;
import com.rs.game.task.impl.ActorDeathTask;
import com.rs.net.encoders.other.Animation;
import com.rs.net.host.HostManager;

import skills.Skills;

public class PlayerDeath extends ActorDeathTask<Player> {

	public PlayerDeath(Player actor) {
		super(actor);
	}

	@Override
	public void preDeath() {
		if (!ControllerHandler.execute(getActor(), controller -> controller.sendDeath(getActor())))
			return;
		getActor().getMovement().lock();
		getActor().setNextAnimation(new Animation(836));
	}

	@Override
	public void death() {
		if (getActor().getPoisonDamage().get() > 0) {
			getActor().getPoisonDamage().set(0);
			getActor().getPackets().sendGlobalConfig(102, 0);
		}
		getActor().getDetails().setAntifireDetails(Optional.empty());	
		getActor().getDetails().getSkullTimer().set(0);
		getActor().getDetails().getWatchMap().get("TOLERANCE").reset();
		getActor().getMovement().stopAll();
		if (getActor().getFamiliar() != null)
			getActor().getFamiliar().sendDeath(Optional.of(getActor()));
	}

	@Override
	public void postDeath() {
		getActor().getInterfaceManager().sendInterface(153);
		getActor().getPackets().sendMusicEffect(90).sendGameMessage("Oh dear, you have died.");
		getActor().setNextAnimation(new Animation(-1));
		getActor().heal(getActor().getMaxHitpoints());
		final int maxPrayer = getActor().getSkills().getLevelForXp(Skills.PRAYER) * 10;
		getActor().getPrayer().restorePrayer(maxPrayer);
		getActor().setNextAnimation(new Animation(-1));
		getActor().getMovement().unlock();
		getActor().getCombatDefinitions().resetSpecialAttack();
		getActor().getPrayer().closeAllPrayers();
		getActor().getMovement().setRunEnergy(100);
		getActor().safeForceMoveTile(new WorldTile(GameConstants.START_PLAYER_LOCATION));
		
		Optional<Controller> controller = ControllerHandler.getController(getActor());
		if (controller.isPresent()) {
			if (controller.get().getSafety() == ControllerSafety.SAFE)
				return;
		} else {
			if (getActor().isPlayer()) {
				Player killer = (Player) getActor();
				killer.setAttackedByDelay(4);
				if(HostManager.same(getActor(), killer)) {
					killer.getPackets().sendGameMessage("You don't receive any points because you and " + getActor().getDisplayName() + " are connected from the same network.");
					return;
				}
				//TODO: This
//				if (getActor().getControllerManager().getController() instanceof Wilderness) {
//					if (getActor().getControllerManager().getController() != null) {
//						sendItemsOnDeath(killer);
//					}
//				}
			}
		}
	}
	
	public void sendItemsOnDeath(Player killer) {
		if (getActor().getDetails().getRights().isStaff())
			return;
		getActor().getDetails().getCharges().die();
		CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<Item>();
		for (int i = 0; i < 14; i++) {
			if (getActor().getEquipment().getItem(i) != null && getActor().getEquipment().getItem(i).getId() != -1
					&& getActor().getEquipment().getItem(i).getAmount() != -1)
				containedItems.add(new Item(getActor().getEquipment().getItem(i).getId(), getActor().getEquipment().getItem(i).getAmount()));
		}
		for (int i = 0; i < getActor().getInventory().getItemsContainerSize(); i++) {
			if (getActor().getInventory().getItem(i) != null && getActor().getInventory().getItem(i).getId() != -1
					&& getActor().getInventory().getItem(i).getAmount() != -1)
				containedItems.add(new Item(getActor().getInventory().getItem(i).getId(), getActor().getInventory().getItem(i).getAmount()));
		}
		if (containedItems.isEmpty())
			return;
		int keptAmount = 0;

		keptAmount = getActor().getAppearance().hasSkull() ? 0 : 3;
		if (getActor().getPrayer().usingPrayer(0, 10) || getActor().getPrayer().usingPrayer(1, 0))
			keptAmount++;
		
		CopyOnWriteArrayList<Item> keptItems = new CopyOnWriteArrayList<Item>();
		Item lastItem = new Item(1, 1);
		for (int i = 0; i < keptAmount; i++) {
			for (Item item : containedItems) {
				int price = item.getDefinitions().getValue();
				if (price >= lastItem.getDefinitions().getValue()) {
					lastItem = item;
				}
			}
			keptItems.add(lastItem);
			containedItems.remove(lastItem);
			lastItem = new Item(1, 1);
		}
		getActor().getInventory().reset();
		getActor().getEquipment().reset();
		for (Item item : keptItems) {
			getActor().getInventory().addItem(item);
		}
		/** This Checks which items that is listed in the 'PROTECT_ON_DEATH' **/
		for (Item item : containedItems) {	// This checks the items you had in your inventory or equipped
			for (String string : GameConstants.PROTECT_ON_DEATH) {	//	This checks the matched items from the list 'PROTECT_ON_DEATH'
				if (item.getDefinitions().getName().toLowerCase().contains(string) || item.getDefinitions().exchangableItem) {
					getActor().getInventory().addItem(item);	//	This adds the items that is matched and listed in 'PROTECT_ON_DEATH'
					containedItems.remove(item);	//	This remove the whole list of the contained items that is matched
				}
			}
		}

		/** This to avoid items to be dropped in the list 'PROTECT_ON_DEATH' **/
		for (Item item : containedItems) {	//	This checks the items you had in your inventory or equipped
			for (String string : GameConstants.PROTECT_ON_DEATH) {	//	This checks the matched items from the list 'PROTECT_ON_DEATH'
				if (item.getDefinitions().getName().toLowerCase().contains(string)) {
					containedItems.remove(item);	//	This remove the whole list of the contained items that is matched
				}
			}
			FloorItem.createGroundItem(item, getActor().getLastWorldTile(), killer == null ? getActor() : killer, false, 180, true, true);	//	This dropps the items to the killer, and is showed for 180 seconds
		}
		for (Item item : containedItems) {
			FloorItem.createGroundItem(item, getActor().getLastWorldTile(), killer == null ? getActor() : killer, false, 180, true, true);
		}
	}
}