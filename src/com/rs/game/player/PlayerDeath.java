package com.rs.game.player;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rs.GameConstants;
import com.rs.constants.Animations;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemConstants;
import com.rs.game.map.WorldTile;
import com.rs.game.map.zone.MapZone.MapZoneSafetyCondition;
import com.rs.game.map.zone.impl.WildernessMapZone;
import com.rs.game.npc.other.Gravestone;
import com.rs.game.task.impl.ActorDeathTask;
import com.rs.net.host.HostManager;

import skills.Skills;

public class PlayerDeath extends ActorDeathTask<Player> {

	public PlayerDeath(Player actor) {
		super(actor);
	}

	@Override
	public void preDeath() {
		if (getActor().getMapZoneManager().execute(controller -> !controller.sendDeath(getActor())))
			return;
		getActor().getMovement().lock();
		getActor().setNextAnimation(Animations.DEATH_FALLING);
	}

	@Override
	public void death() {
		if (getActor().getPoisonDamage().get() > 0) {
			getActor().getPoisonDamage().set(0);
			getActor().getPackets().sendGlobalConfig(102, 0);
		}
		getActor().getDetails().setAntifireDetails(Optional.empty());	
		getActor().getDetails().getSkullTimer().set(0);
		getActor().getDetails().getTolerance().reset();
		getActor().getMovement().stopAll();
		if (getActor().getFamiliar() != null)
			getActor().getFamiliar().sendDeath(Optional.of(getActor()));
		
	}

	@Override
	public void postDeath() {
		getActor().getSkills().restoreSkills();
		if (getActor().getDetails().getDisableDeathPopup().isFalse())
			getActor().getInterfaceManager().sendInterface(153);
		getActor().getPackets().sendMusicEffect(90).sendGameMessage("Oh dear, you have died.");
		getActor().getDetails().getStatistics().addStatistic("Times_Died");
		getActor().setNextAnimation(Animations.RESET_ANIMATION);
		getActor().heal(getActor().getMaxHitpoints());
		final int maxPrayer = getActor().getSkills().getTrueLevel(Skills.PRAYER) * 10;
		getActor().getPrayer().restorePrayer(maxPrayer);
		getActor().getMovement().unlock();
		getActor().getCombatDefinitions().resetSpecialAttack();
		getActor().getPrayer().closeAllPrayers();
		getActor().getMovement().setRunEnergy(100);
		getActor().setNextWorldTile(new WorldTile(GameConstants.START_PLAYER_LOCATION));
		new Gravestone(getActor(), getActor());
		if (getActor().getMapZoneManager().getMapZone().isPresent() && getActor().getMapZoneManager().getMapZone().get().getSafety() == MapZoneSafetyCondition.SAFE)
			return;
		if (getActor().isPlayer()) {
			Player killer = (Player) getActor();
			killer.setAttackedByDelay(4);
			if (HostManager.same(getActor(), killer)) {
				killer.getPackets().sendGameMessage("You don't receive any points because you and "
						+ getActor().getDisplayName() + " are connected from the same network.");
				return;
			}
			sendItemsOnDeath(getActor().getMapZoneManager().getMapZone().isPresent()
					&& getActor().getMapZoneManager().getMapZone().get() instanceof WildernessMapZone ? killer
							: getActor());
		}
	}
	
	public void sendItemsOnDeath(Player killer) {
		if (getActor().getDetails().getRights().isStaff())
			return;
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
		int keptAmount = getActor().getAppearance().hasSkull() ? 0 : 3;
		if (getActor().getPrayer().isProtectingItem())
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
		keptItems.stream().filter(item -> item.getId() != 1).forEach(getActor().getInventory()::addItem);
		for (Item item : containedItems) {
			for (String string : ItemConstants.PROTECT_ON_DEATH) {
				if (item.getDefinitions().getName().toLowerCase().contains(string)) {
					containedItems.remove(item);
				}
			}
			if (!ItemConstants.isTradeable(item))
				continue;
			FloorItem.addGroundItem(item, getActor().getLastWorldTile(), killer == null ? getActor() : killer, true, 60);
		}
		FloorItem.addGroundItem(new Item(526), getActor().getLastWorldTile(), killer == null ? getActor() : killer, true, 60);
	}
}