package com.alex.utils;

import java.util.HashMap;

import com.rs.cache.Cache;
import com.rs.cache.loaders.VarBitDefinitions;
import com.rs.constants.InterfaceVars;
import com.rs.game.player.Player;
import com.rs.game.player.content.Emotes.Emote;

public class VarsManager {

	private transient int[] values;
	private transient Player player;
	public HashMap<Integer,Integer> varMap = new HashMap<Integer,Integer>();
	public HashMap<Integer,Integer> varBitMap = new HashMap<Integer,Integer>();
	
	private transient final int[] masklookup = new int[32];

	{
		int i = 2;
		for (int i2 = 0; i2 < 32; i2++) {
			masklookup[i2] = i - 1;
			i += i;
		}
	}

	public VarsManager() {
		values = new int[Cache.STORE.getIndexes()[2].getLastFileId(16) + 1];
		if (varMap == null)
			varMap  = new HashMap<Integer,Integer>();
		if (varBitMap == null)
			varBitMap  = new HashMap<Integer,Integer>();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Bind {@link InterfaceVars} to the Player and load on login.
	 * Commonly used for {@link Emote}, such..
	 * @param id
	 * @param value
	 * @return
	 */
	public VarsManager submitVarToMap(int id, int value) {
		varMap.computeIfPresent(id, (k,v) -> v = value);
		varMap.putIfAbsent(id, value);
		return this;
	}
	
	/**
	 * Bind VarBits (Objects states, etc..) to the Player and load on login.
	 * Commonly used for Farming, such..
	 * @param id
	 * @param value
	 * @return
	 */
	public VarsManager submitVarBitToMap(int id, int value) {
		varBitMap.computeIfPresent(id, (k,v) -> v = value);
		varBitMap.putIfAbsent(id, value);
		return this;
	}

	public VarsManager sendVar(int id, int value) {
		sendVar(id, value, false);
		return this;
	}

	public VarsManager forceSendVar(int id, int value) {
		sendVar(id, value, true);
		return this;
	}
	
	public void sendVar(int id, int value, boolean force) {
		if (id < 0 || id >= values.length)
			return;
		if (force || values[id] == value)
			return;
		setVar(id, value);
		sendClientVarp(id);
	}

	public VarsManager setVar(int id, int value) {
		if (id == -1)
			return this;
		values[id] = value;
		return this;
	}

	public VarsManager forceSendVarBit(int id, int value) {
		setVarBit(id, value, 0x1 | 0x2);
		return this;
	}

	public VarsManager sendVarBit(int id, int value) {
		setVarBit(id, value, 0x1);
		return this;
	}

	public VarsManager setVarBit(int id, int value) {
		setVarBit(id, value, 0);
		return this;
	}

	public int getBitValue(int id) {
		VarBitDefinitions defs = VarBitDefinitions
				.getClientVarpBitDefinitions(id);
		return values[defs.baseVar] >> defs.startBit
				& masklookup[defs.endBit - defs.startBit];
	}

	private VarsManager setVarBit(int id, int value, int flag) {
		if (id == -1)
			return this;
		VarBitDefinitions defs = VarBitDefinitions
				.getClientVarpBitDefinitions(id);
		int mask = masklookup[defs.endBit - defs.startBit];
		if (value < 0 || value > mask)
			value = 0;
		mask <<= defs.startBit;
		int varpValue = (values[defs.baseVar] & (mask ^ 0xffffffff) | value << defs.startBit
				& mask);
		if ((flag & 0x2) != 0 || varpValue != values[defs.baseVar]) {
			setVar(defs.baseVar, varpValue);
			if ((flag & 0x1) != 0)
				sendClientVarp(defs.baseVar);
		}
		return this;
	}

	@SuppressWarnings("deprecation")
	private void sendClientVarp(int id) {
		player.getPackets().sendVar(id, values[id]);
	}
}