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
package com.rs.game.player.actions;

import java.util.EnumSet;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.rs.cache.loaders.ItemDefinitions;
import com.rs.constants.Animations;
import com.rs.constants.Sounds;
import com.rs.game.item.Item;
import com.rs.game.player.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class FillAction extends Action {

	private int amount;

	@AllArgsConstructor
	public enum Filler {
		VIAL(new Item(229, 1), new Item(227, 1)),
		CLAY(new Item(434, 1), new Item(1761, 1)),
		BOWL(new Item(1923, 1), new Item(1921, 1)),
		BUCKET(new Item(1925, 1), new Item(1929, 1)),
		VASE(new Item(3734, 1), new Item(3735, 1)),
		JUJU_VIAL(new Item(19996, 1), new Item(19994, 1)),
		JUG(new Item(1935, 1), new Item(1937, 1)),
		WATERING_CAN(new Item(5331, 1), new Item(5340, 1)),
		WATERING_CAN1(new Item(5333, 1), new Item(5340, 1)),
		WATERING_CAN2(new Item(5334, 1), new Item(5340, 1)),
		WATERING_CAN3(new Item(5335, 1), new Item(5340, 1)),
		WATERING_CAN4(new Item(5336, 1), new Item(5340, 1)),
		WATERING_CAN5(new Item(5337, 1), new Item(5340, 1)),
		WATERING_CAN6(new Item(5338, 1), new Item(5340, 1)),
		WATERING_CAN7(new Item(5339, 1), new Item(5340, 1)),
		KETTLE(new Item(7688, 1), new Item(7690, 1));
		
		public static final ImmutableSet<Filler> VALUES = Sets.immutableEnumSet(EnumSet.allOf(Filler.class));

		@Getter
		private Item empty;
		@Getter
		private Item filled;
	}

	public FillAction(int amount) {
		this.amount = amount;
	}

	@Override
	public boolean start(Player player) {
		return true;
	}

	@Override
	public boolean process(Player player) {
		return Filler.VALUES.stream().anyMatch(id -> player.getInventory().containsAny(id.empty.getId()));
	}

	@Override
	public int processWithDelay(Player player) {
		amount--;
		Filler.VALUES.stream().filter(id -> player.getInventory().containsAny(id.empty.getId())).forEach(fillable -> {
			player.getAudioManager().sendSound(Sounds.FILL_FROM_WATER_SOURCE);
			player.setNextAnimation(Animations.LEANING_FORWARD_USING_BOTH_HANDS);
			player.getInventory().deleteItem(fillable.getEmpty().getId(), 1);
			player.getInventory().addItem(fillable.getFilled().getId(), 1);
			player.getDetails().getStatistics()
					.addStatistic(ItemDefinitions.getItemDefinitions(fillable.getFilled().getId()).getName() + "_Filled")
					.addStatistic("Items_Filled");
		});
		return amount > 0 ? 1 : -1;
	}

	@Override
	public void stop(Player player) {

	}
}