package com.rs.game.npc;

import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

import com.rs.GameConstants;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.map.World;
import com.rs.game.player.Player;
import com.rs.utilities.Chance;
import com.rs.utilities.RandomUtils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import lombok.val;

/**
 * A container that holds the unique and common drop tables.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public final class DropTable {
	
	/**
	 * The drop list that consists of common drops.
	 */
	private final ObjectList<Drop> common;
	
	/**
	 * The drop list that consists of rare drops.
	 */
	private final ObjectList<Drop> rare;
	
	/**
	 * Creates a new {@link DropTable}.
	 */
	public DropTable(Drop[] common, Drop... rare) {
		this.common = new ObjectArrayList<>(common);
		this.rare = new ObjectArrayList<>(rare);
	}
	
	/**
	 * Creates a new {@link DropTable}.
	 */
	public DropTable(ObjectList<Drop> common, ObjectList<Drop> rare) {
		this.common = common;
		this.rare = rare;
	}
	
	/**
	 * Creates a new {@link DropTable}.
	 */
	public DropTable() {
		this.common = new ObjectArrayList<>();
		this.rare = new ObjectArrayList<>();
	}
	
	/**
	 * Performs the necessary calculations on all of the tables in this
	 * container to determine an array of items to drop. Please note that this
	 * is not a static implementation meaning that calling this multiple times
	 * will return a different array of items.
	 * @param player the player that these calculations are being performed for.
	 * @param victim the npc that was killed.
	 * @return the array of items that were calculated.
	 */
	ObjectList<Item> toItems(Player player, NPC victim) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		ObjectList<Item> items = new ObjectArrayList<>();
		int amount = 0;
		if(!common.isEmpty()) {
			for(Drop drop : common) {
				if(drop == null)
					continue;
				if(drop.getChance() == Chance.ALWAYS) {
					items.add(drop.toItem());
				} else if(!drop.isRare()) {
					if(amount++ <= GameConstants.DROP_THRESHOLD && drop.roll(random)) {
						items.add(drop.toItem());
					}
				}
			}
		}
		if(!rare.isEmpty()) {
			boolean row = player.getEquipment().getRingId() == 2572;
			int count = rare.size() / 4;
			if(count <= 0)
				count = 1;
			if(count > GameConstants.DROP_RARE_ATTEMPTS)
				count = GameConstants.DROP_RARE_ATTEMPTS;
			if(row)
				count += 1;
			for(int i = 0; i < count; i++) {
				Drop rareDrop = RandomUtils.random(rare);
				if(rareDrop.roll(random)) {
					Item item = rareDrop.toItem();
					items.add(item);
					val dropPrice = item.getDefinitions().getValue();
					if (dropPrice >= 2_000_000)
						World.sendWorldMessage("[Drops] " + player.getDisplayName() + " has just recieved a " + item.getDefinitions().getName() + " from " + victim.getDefinitions().getName() + "!");
				}
			}
		}
		return items;
	}
	
	/**
	 * Sorting the drop table by chance tiers.
	 */
	public void sort() {
		common.sort(Comparator.comparingInt(o -> o.getChance().ordinal()));
		rare.sort(Comparator.comparingInt(o -> o.getChance().ordinal()));
	}
	
	/**
	 * Looks to find if this drop has a specific drop.
	 * @param drop the drop seeking for.
	 * @return if found, true otherwise false.
	 */
	public boolean contains(Drop drop) {
		ItemDefinitions defDrop = ItemDefinitions.getItemDefinitions(drop.getId());
		if(drop.isRare()) {
			for(Drop d : common) {
				if(d == null)
					continue;
				if(d.getId() == drop.getId() && d.getChance() == drop.getChance() && d.getMinimum() == drop.getMinimum())
					return true;
				ItemDefinitions def = ItemDefinitions.getItemDefinitions(d.getId());
				if(def.getName().equalsIgnoreCase(defDrop.getName())) {
					return true;
				}
			}
		} else {
			for(Drop d : rare) {
				if(d == null)
					continue;
				if(d.getId() == drop.getId() && d.getChance() == drop.getChance() && d.getMinimum() == drop.getMinimum())
					return true;
				ItemDefinitions def = ItemDefinitions.getItemDefinitions(d.getId());
				if(def.getName().equalsIgnoreCase(defDrop.getName())) {
					return true;
				}
			}
		}
		return false;
	}
}