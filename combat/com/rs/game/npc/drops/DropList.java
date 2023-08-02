// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Copyright (C) 2021 Trenton Kress
//  This file is part of project: Darkan
//
package com.rs.game.npc.drops;

import java.util.Arrays;

import com.rs.cache.loaders.ItemDefinitions;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.utilities.RandomUtility;
import com.rs.utilities.Rational;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class DropList {

	private static double MAX_ROLL = Math.nextDown(1.0);

	private ObjectArrayList<DropEntry> drops = new ObjectArrayList<>();
	private double nothingRate = 0.0;
	private boolean overflowed;
	private double overflow;

	public DropList(DropTable... tables) {
		this(new ObjectArrayList<>(Arrays.asList(tables)));
	}

	public DropList(ObjectArrayList<DropTable> tables) {
		double curr = 0.0;
		tables.sort((o1, o2) -> {
			if ((o1 == null) || (o2 == null)) return Integer.MAX_VALUE;
			return Double.compare(o1.chance / o1.outOf, o2.chance / o2.outOf);
		});
		for (DropTable table : tables) {
			if (table == null)
				continue;
			double rate = table.getRate();
			if (rate == 0.0) {
				drops.add(new DropEntry(table));
				continue;
			}
			drops.add(new DropEntry(table, curr, curr + rate));
			curr += rate;
			if (curr > 1.0000000000000009) {
				overflowed = true;
				overflow = curr - 1.0;
				System.err.println("Drop rate overflow for table: " + curr + ", " + table.toString());
			}
		}
		double emptySlots = 1.0-curr;
		nothingRate = emptySlots;
		drops.add(new DropEntry(null, curr, curr+emptySlots));
	}

	public boolean isOverflowed() {
		return overflowed;
	}

	public double getOverflow() {
		return overflow;
	}

	public String getNothingFracString() {
		return Rational.toRational(nothingRate).toString();
	}

	public double getNothingRate() {
		return Utility.round(nothingRate, 10);
	}

	public ObjectArrayList<Item> genDrop() {
		return genDrop(1.0);
	}

	public ObjectArrayList<Item> genDrop(double modifier) {
		return genDrop(null, modifier);
	}

	public ObjectArrayList<DropEntry> getDrops() {
		return drops;
	}

	public ObjectArrayList<Item> genDrop(Player killer, double modifier) {
		ObjectArrayList<Item> finals = new ObjectArrayList<>();

		modifier *= 1;

		double roll = Utility.clampD(Utility.randomD() * modifier, -100, MAX_ROLL);
		for (DropEntry drop : drops) {
			if ((!drop.isAlways() && roll < drop.getMin()) || (!drop.isAlways() && roll >= drop.getMax()))
				continue;
			DropTable table = drop.getTable();
			if (table == null)
				continue;
			if (table.getRollTable() != null) {
				if (killer != null)
					switch(table.getRollTable().getNames()[0]) {
					case "rdt_gem":
						if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth"))
							killer.getPackets().sendGameMessage("<col=FACC2E>Your ring of wealth shines brightly!");
						break;
					case "rdt_standard":
						if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth"))
							killer.getPackets().sendGameMessage("<col=FACC2E>Your ring of wealth shines brightly!");
						break;
					case "rdt_mega_rare":
						if (killer.getEquipment().getRingId() != -1 && ItemDefinitions.getItemDefinitions(killer.getEquipment().getRingId()).getName().toLowerCase().contains("ring of wealth"))
							killer.getPackets().sendGameMessage("<col=FACC2E>Your ring of wealth shines brightly!");
						break;
					}
				finals.addAll(table.getRollTable().getDropList().genDrop(modifier));
				continue;
			}
			if (table.isDropOne()) {
				Drop d = table.getDrops()[RandomUtility.random(table.getDrops().length)];
				if (d.getRollTable() == null)
					finals.add(d.toItem());
				else
					for(int i = 0;i < d.getAmount();i++)
						finals.addAll(d.getRollTable().getDropList().genDrop(modifier));
			} else
				for (Drop d : table.getDrops())
					if (d.getRollTable() == null)
						finals.add(d.toItem());
					else
						for(int i = 0;i < d.getAmount();i++)
							finals.addAll(d.getRollTable().getDropList().genDrop(modifier));
		}
		return finals;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (DropEntry d : drops)
			str.append(d + "\n");
		return str.toString();
	}

}
