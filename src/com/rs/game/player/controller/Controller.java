package com.rs.game.player.controller;

import java.util.Optional;

import com.rs.game.Entity;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.map.GameObject;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.content.Foods.Food;
import com.rs.game.player.content.Pots.Pot;

import lombok.Data;

@Data
public abstract class Controller {

	/**
	 * The string which defines the current controller.
	 */
	private final String controller;

	/**
	 * The current name of this controller.
	 */
	private final ControllerSafety safety;

	/**
	 * The current type of this controller.
	 */
	private final ControllerType type;

	public Optional<Controller> copy() {
		return Optional.empty();
	}

	public abstract void start(Player player);

	public boolean canEat(Player player, Food food) {
		return true;
	}

	public boolean canPot(Player player, Pot pot) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean canTakeItem(Player player, FloorItem item) {
		return true;
	}

	/**
	 * after the normal checks, extra checks, only called when you attacking
	 */
	public boolean keepCombating(Player player, Entity target) {
		return true;
	}

	public boolean canEquip(Player player, int slotId, int itemId) {
		return true;
	}

	/**
	 * after the normal checks, extra checks, only called when you start trying to
	 * attack
	 */
	public boolean canAttack(Player player, Entity target) {
		return true;
	}

	public boolean canDeleteInventoryItem(Player player, int itemId, int amount) {
		return true;
	}

	public boolean canUseItemOnItem(Player player, Item itemUsed, Item usedWith) {
		return true;
	}

	public boolean canAddInventoryItem(Player player, int itemId, int amount) {
		return true;
	}

	public boolean canPlayerOption1(Player player, Player target) {
		return true;
	}

	public boolean canPlayerOption2(Player player, Player target) {
		return true;
	}

	public boolean canPlayerOption3(Player target) {
		return true;
	}

	public boolean canPlayerOption4(Player target) {
		return true;
	}

	/**
	 * hits as ice barrage and that on multi areas
	 */
	public boolean canHit(Player player, Entity entity) {
		return true;
	}

	/**
	 * processes every game ticket, usualy not used
	 */
	public void process(Player player) {

	}

	public void moved(Player player) {

	}

	/**
	 * called once teleport is performed
	 */
	public void magicTeleported(Player player, int type) {

	}

	public void sendInterfaces(Player player) {

	}

	/**
	 * return can use script
	 */
	public boolean useDialogueScript(Player player, Object key) {
		return true;
	}

	/**
	 * return can teleport
	 */
	public boolean processMagicTeleport(Player player, WorldTile toTile) {
		return true;
	}

	/**
	 * return can teleport
	 */
	public boolean processItemTeleport(Player player, WorldTile toTile) {
		return true;
	}

	/**
	 * return can teleport
	 */
	public boolean processObjectTeleport(Player player, WorldTile toTile) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processObjectClick1(Player player, GameObject object) {
		return true;
	}

	/**
	 * return process normaly
	 * 
	 * @param slotId2 TODO
	 */
	public boolean processButtonClick(Player player, int interfaceId, int componentId, int slotId, int slotId2, int packetId) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick1(Player player, NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick2(Player player, NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick3(Player player, NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processNPCClick4(Player player, NPC npc) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processObjectClick2(Player player, GameObject object) {
		return true;
	}

	/**
	 * return process normaly
	 */
	public boolean processObjectClick3(Player player, GameObject object) {
		return true;
	}

	public boolean processObjectClick4(Player player, GameObject object) {
		return true;
	}

	public boolean processObjectClick5(Player player, GameObject object) {
		return true;
	}

	public boolean handleItemOnObject(Player player, GameObject object, Item item) {
		return true;
	}

	/**
	 * return let default death
	 */
	public boolean sendDeath(Player player) {
		return true;
	}

	/**
	 * return can move that step
	 */
	public boolean canMove(Player player, int dir) {
		return true;
	}

	/**
	 * return can set that step
	 */
	public boolean checkWalkStep(Player player, int lastX, int lastY, int nextX, int nextY) {
		return true;
	}

	/**
	 * return remove controller
	 */
	public boolean login(Player player) {
		return true;
	}

	/**
	 * return remove controller
	 */
	public boolean logout(Player player) {
		return true;
	}

	public void forceClose(Player player) {
	}

	public boolean processItemOnNPC(Player player, NPC npc, Item item) {
		return true;
	}

	public boolean canDropItem(Player player, Item item) {
		return true;
	}

	public boolean canSummonFamiliar(Player player) {
		return true;
	}

	public boolean processItemOnPlayer(Player player, Player target, Item item) {
		return true;
	}

	public void processNPCDeath(Player player, int id) {

	}

	/**
	 * Determines if {@code player} is in this minigame.
	 * 
	 * @param player the player to determine this for.
	 * @return <true> if this minigame contains the player, <false> otherwise.
	 */
	public abstract boolean contains(Player player);

	/**
	 * The enumerated type whose elements represent the minigame types.
	 * 
	 * @author lare96 <http://github.com/lare96>
	 */
	public enum ControllerType {
		NORMAL, SEQUENCED
	}

	/**
	 * The enumerated type whose elements represent the item safety of a player who
	 * is playing the minigame.
	 * 
	 * @author <a href="http://www.rune-server.org/members/stand+up/">Stand Up</a>
	 */
	public enum ControllerSafety {
		/**
		 * This safety is similar to when a player dies while he is skulled.
		 */
		DANGEROUS,
		/**
		 * /** Indicates the minigame is fully safe and no items will be lost on death
		 */
		SAFE
	}
}