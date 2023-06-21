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
package com.rs.game.player.content.doors;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.rs.game.map.GameObject;
import com.rs.game.player.Player;
import com.rs.utilities.GSONParser;

public class DoorPair {

	private static final String PATH = "./data/map/doorPairs.json";

	private static DoorPair[] DOOR_PAIRS;
	private static Map<Integer, Integer> PAIRING_MAP = new HashMap<>();

	private int closed;
	private int open;

	public static void loadPairs() {
		DOOR_PAIRS = (DoorPair[]) GSONParser.loadFile(new File(PATH).getAbsolutePath(), DoorPair[].class);
		for (DoorPair pair : DOOR_PAIRS) {
			PAIRING_MAP.put(pair.open, pair.closed);
			PAIRING_MAP.put(pair.closed, pair.open);
		}
	}

	public static int getOpposingDoor(Player player, GameObject door) {
		return PAIRING_MAP.get(door.getDefinitions().id) != null ? PAIRING_MAP.get(door.getDefinitions().id) : 1532;
	}

	public DoorPair(int closed, int open) {
		this.closed = closed;
		this.open = open;
	}

	public int getClosed() {
		return closed;
	}

	public int getOpen() {
		return open;
	}

}
