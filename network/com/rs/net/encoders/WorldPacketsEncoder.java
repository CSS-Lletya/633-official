package com.rs.net.encoders;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import com.rs.GameConstants;
import com.rs.constants.InterfaceVars;
import com.rs.game.Entity;
import com.rs.game.item.FloorItem;
import com.rs.game.item.Item;
import com.rs.game.item.ItemsContainer;
import com.rs.game.map.DynamicRegion;
import com.rs.game.map.GameObject;
import com.rs.game.map.Region;
import com.rs.game.map.World;
import com.rs.game.map.WorldTile;
import com.rs.game.npc.NPC;
import com.rs.game.player.Player;
import com.rs.game.player.Rights;
import com.rs.game.player.attribute.Attribute;
import com.rs.game.player.content.FriendChatsManager;
import com.rs.io.OutputStream;
import com.rs.net.Huffman;
import com.rs.net.Session;
import com.rs.net.encoders.other.Animation;
import com.rs.net.encoders.other.ChatMessage;
import com.rs.net.encoders.other.Graphics;
import com.rs.net.encoders.other.HintIcon;
import com.rs.net.encoders.other.PublicChatMessage;
import com.rs.net.encoders.other.QuickChatMessage;
import com.rs.net.wordlist.WorldList;
import com.rs.utilities.IntegerInputAction;
import com.rs.utilities.RandomUtils;
import com.rs.utilities.StringInputAction;
import com.rs.utilities.Utility;
import com.rs.utilities.loaders.MapArchiveKeys;

public class WorldPacketsEncoder extends Encoder {

	private Player player;

	public WorldPacketsEncoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	// CONVERTED TO 633

	/*
	 * normal map region
	 */
	public WorldPacketsEncoder sendGameScene(boolean sendLswp) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 96);
		if (sendLswp) {
			getPlayer().getLocalPlayerUpdate().init(stream);
		}
		stream.writeShortLE(getPlayer().getChunkY());
		stream.writeShortLE128(getPlayer().getChunkX());
		stream.writeByte128(getPlayer().getDetails().isForceNextMapLoadRefresh() ? 1 : 0);
		stream.writeByte128(getPlayer().getMapSize());
		getPlayer().getMapRegionsIds().forEach(region -> {
			int[] xteas = MapArchiveKeys.getMapKeys(region);
			if (xteas == null)
				xteas = new int[4];
			for (int index = 0; index < 4; index++) {
				stream.writeInt(xteas[index]);
			}
		});
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	/*
	 * sets the pane interface
	 */
	public WorldPacketsEncoder sendRootInterface(int id, int type) {
		getPlayer().getInterfaceManager().setRootInterface(id);
		OutputStream stream = new OutputStream(4);
		stream.writePacket(getPlayer(), 113);
		stream.writeShortLE128(id);
		stream.write128Byte(type);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendSkillLevel(int skill) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 91);
		stream.writeIntV1((int) getPlayer().getSkills().getXp(skill));
		stream.writeByte128(skill);
		stream.writeByte((byte) getPlayer().getSkills().getLevel(skill));
		getSession().write(stream);
		return this;

	}

	/**
	 * This will blackout specified area.
	 * 
	 * @param area which will be blackout (0 = unblackout; 1 = blackout orb; 2 =
	 *             blackout map; 5 = blackout orb and map)
	 */
	public WorldPacketsEncoder sendBlackOut(int area) {
		OutputStream out = new OutputStream(2);
		out.writePacket(getPlayer(), 103);
		out.writeByte(area);
		getSession().write(out);
		return this;
	}

	/*
	 * sends local players update
	 */
	public WorldPacketsEncoder sendLocalPlayersUpdate() {
		getSession().write(getPlayer().getLocalPlayerUpdate().createPacketAndProcess());
		return this;
	}

	/*
	 * sends local npcs update
	 */
	public WorldPacketsEncoder sendLocalNPCsUpdate() {
		getSession().write(getPlayer().getLocalNPCUpdate().createPacketAndProcess());
		return this;
	}

	public WorldPacketsEncoder sendInterface(boolean clickThrought, int parentUID, int interfaceId) {
		OutputStream stream = new OutputStream(24);
		stream.writePacket(getPlayer(), 65);
		stream.writeByteC(clickThrought ? 1 : 0);
		stream.writeInt(parentUID);
		stream.writeShort(interfaceId);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendMusic(int id) {
		sendMusic(id, 100, 255);
		return this;
	}

	public WorldPacketsEncoder sendMusic(int id, int delay, int volume) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(getPlayer(), 62);
		stream.writeByte128(delay);
		stream.writeShort(id);
		stream.writeByteC(volume);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendMusicEffect(int id) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 63);
		stream.writeShort(id);
		stream.write128Byte(255);
		stream.write24BitInteger(0);
		getSession().write(stream);
		return this;
	}

	// effect type 1 or 2(index4 or index14 format, index15 format unusused by
	// jagex for now)
	public WorldPacketsEncoder sendSound(int id, int delay, int effectType) {
		if (effectType == 1)
			sendIndex14Sound(id, delay);
		else if (effectType == 2)
			sendIndex15Sound(id, delay);
		return this;
	}

	public WorldPacketsEncoder sendVoice(int id) {
		resetSounds();
		sendSound(id, 0, 2);
		return this;
	}

	public WorldPacketsEncoder resetSounds() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(getPlayer(), 70);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendIndex15Sound(int soundId, int delay) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 67); // also 37 as well but with one more
										// short and byte
		stream.writeShort(soundId);
		stream.writeByte(1);
		stream.writeShort(delay);
		stream.writeByte(255);
		getSession().write(stream);
		return this;

	}

	public WorldPacketsEncoder sendIndex14Sound(int id, int delay) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(getPlayer(), 66);
		stream.writeShort(id);
		stream.writeByte(1);
		stream.writeShort(delay);
		stream.writeByte(255);
		stream.writeShort(256);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendInterFlashScript(int interfaceId, int componentId, int width, int height, int slot) {
		Object[] parameters = new Object[4];
		int index = 0;
		parameters[index++] = slot;
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(143, parameters);
		return this;
	}

	public WorldPacketsEncoder sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key, int width,
			int height, String... options) {
		sendInterSetItemsOptionsScript(interfaceId, componentId, key, false, width, height, options);
		return this;
	}

	public WorldPacketsEncoder sendInterSetItemsOptionsScript(int interfaceId, int componentId, int key,
			boolean negativeKey, int width, int height, String... options) {
		Object[] parameters = new Object[6 + options.length];
		int index = 0;
		for (int count = options.length - 1; count >= 0; count--)
			parameters[index++] = options[count];
		parameters[index++] = -1; // dunno but always this
		parameters[index++] = 0;// dunno but always this, maybe startslot?
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = key;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(negativeKey ? 695 : 150, parameters); // scriptid 150 does
		// that the method
		// name says*/
		return this;
	}

	public WorldPacketsEncoder sendPouchInfusionOptionsScript(int interfaceId, int componentId, int slotLength,
			int width, int height, String... options) {
		Object[] parameters = new Object[5 + options.length];
		int index = 0;
		parameters[index++] = slotLength;
		parameters[index++] = 1; // dunno
		for (int count = options.length - 1; count >= 0; count--)
			parameters[index++] = options[count];
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(757, parameters);
		return this;
	}

	public WorldPacketsEncoder sendScrollInfusionOptionsScript(int interfaceId, int componentId, int slotLength,
			int width, int height, String... options) {
		Object[] parameters = new Object[5 + options.length];
		int index = 0;
		parameters[index++] = slotLength;
		parameters[index++] = 1; // dunno are u sure it contains this 1? yeah
		for (int count = options.length - 1; count >= 0; count--)
			parameters[index++] = options[count];
		parameters[index++] = height;
		parameters[index++] = width;
		parameters[index++] = interfaceId << 16 | componentId;
		sendRunScript(763, parameters);
		return this;
	}

	public WorldPacketsEncoder sendInputNameScript(String message) {
		sendRunScript(109, new Object[] { message });
		return this;
	}

	public WorldPacketsEncoder sendInputIntegerScript(String message) {
		sendRunScript(108, new Object[] { message });
		return this;
	}

	public WorldPacketsEncoder sendInputLongTextScript(String message) {
		sendRunScript(110, new Object[] { message });
		return this;
	}

	public WorldPacketsEncoder sendRunScriptBlank(int scriptId) {
		sendRunScript(scriptId, new Object[] {});
		return this;
	}

	public WorldPacketsEncoder sendRunScript(int scriptId, Object... params) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 53);
		String parameterTypes = "";
		if (params != null) {
			for (int count = params.length - 1; count >= 0; count--) {
				if (params[count] instanceof String)
					parameterTypes += "s"; // string
				else
					parameterTypes += "i"; // integer
			}
		}
		stream.writeString(parameterTypes);
		if (params != null) {
			int index = 0;
			for (int count = parameterTypes.length() - 1; count >= 0; count--) {
				if (parameterTypes.charAt(count) == 's')
					stream.writeString((String) params[index++]);
				else
					stream.writeInt((Integer) params[index++]);
			}
		}
		stream.writeInt(scriptId);
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendRunEnergy() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(getPlayer(), 83);
		stream.writeByte((int) getPlayer().getDetails().getRunEnergy());
		getSession().write(stream);
		return this;
	}
	
	public WorldPacketsEncoder sendWeight() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(getPlayer(), 79);
		stream.writeShort((int) getPlayer().getWeight());
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendMessage(int type, String text, Player p) {
		int maskData = 0;
		if (p != null) {
			maskData |= 0x1;
			if (p.getDisplayName() != null)
				maskData |= 0x2;
		}
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 101);
		stream.writeSmart(type);
		stream.writeInt(getPlayer().getTileHash()); // junk, not used by client
		stream.writeByte(maskData);
		if ((maskData & 0x1) != 0) {
			stream.writeString(Utility.formatPlayerNameForDisplay(p.getUsername()));
			if (p.getDisplayName() != null)
				stream.writeString(p.getDisplayName());
		}
		stream.writeString(text);
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendGameMessage(String text) {
		sendGameMessage(text, false);
		return this;
	}

	public WorldPacketsEncoder sendGameMessage(String text, boolean filter) {
		sendMessage(filter ? 109 : 0, text, null);
		return this;
	}

	public WorldPacketsEncoder sendPanelBoxMessage(String text) {
		sendMessage(getPlayer().getDetails().getRights() == Rights.ADMINISTRATOR ? 99 : 0, text, null);
		return this;
	}

	public WorldPacketsEncoder sendTradeRequestMessage(Player p) {
		sendMessage(100, "wishes to trade with you.", p);//not showing up
		return this;
	}

	public WorldPacketsEncoder sendClanWarsRequestMessage(Player p) {
		sendMessage(101, "wishes to challenge your clan to a clan war.", p);
		return this;
	}

	public WorldPacketsEncoder sendClanInviteMessage(Player p) {
		sendMessage(117, p.getDisplayName() + " is inviting you to join their clan.", p);
		return this;
	}

	public WorldPacketsEncoder sendDuelChallengeRequestMessage(Player p, boolean friendly) {
		sendMessage(101, "wishes to duel with you(" + (friendly ? "friendly" : "stake") + ").", p);
		return this;
	}

	public WorldPacketsEncoder sendMinimapFlag(int x, int y) {
		OutputStream stream = new OutputStream();
		stream.writePacket(getPlayer(), 93);
		stream.writeByte128(x);
		stream.writeByte128(y);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendResetMinimapFlag() {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(getPlayer(), 93);
		stream.writeByte128(255);
		stream.writeByte128(255);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendUpdateItems(int key, ItemsContainer<Item> items, int... slots) {
		sendUpdateItems(key, items.getItems(), slots);
		return this;
	}

	public WorldPacketsEncoder sendUpdateItems(int key, Item[] items, int... slots) {
		sendUpdateItems(key, key < 0, items, slots);
		return this;
	}

	public WorldPacketsEncoder sendUpdateItems(int key, boolean negativeKey, Item[] items, int... slots) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 38);
		stream.writeShort(key);
		stream.writeByte(negativeKey ? 1 : 0);
		for (int slotId : slots) {
			if (slotId >= items.length)
				continue;
			stream.writeSmart(slotId);
			int id = -1;
			int amount = 0;
			Item item = items[slotId];
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShort(id + 1);
			if (id != -1) {
				stream.writeByte(amount >= 255 ? 255 : amount);
				if (amount >= 255)
					stream.writeInt(amount);
			}
		}
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendItemsContainer(int key, ItemsContainer<Item> container) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 19);
		stream.writeShort(key);
		stream.writeByte(key < 0 ? 1 : 0);
		stream.writeShort(container.getSize());
		for (int i = 0; i < container.getSize(); i++) {
			int id = -1;
			int amount = 0;
			Item item = container.get(i);
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShortLE(id + 1);
			stream.writeByte(amount >= 255 ? 255 : amount);
			if (amount >= 255)
				stream.writeIntV1(amount);

		}
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendItems(int key, ItemsContainer<Item> items) {
		sendItems(key, key < 0, items);
		return this;
	}

	public WorldPacketsEncoder sendItems(int key, boolean negativeKey, ItemsContainer<Item> items) {
		sendItems(key, negativeKey, items.getItems());
		return this;
	}

	public WorldPacketsEncoder sendItems(int key, Item[] items) {
		sendItems(key, key < 0, items);
		return this;
	}

	public WorldPacketsEncoder sendItems(int key, boolean negativeKey, Item[] items) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 19);
		stream.writeShort(key); // negativeKey ? -key : key
		stream.writeByte(negativeKey ? 1 : 0);
		stream.writeShort(items.length);
		for (int index = 0; index < items.length; index++) {
			Item item = items[index];
			int id = -1;
			int amount = 0;
			if (item != null) {
				id = item.getId();
				amount = item.getAmount();
			}
			stream.writeShortLE(id + 1);
			stream.writeByte(amount >= 255 ? 255 : amount);
			if (amount >= 255)
				stream.writeIntV1(amount);

		}
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendLogout(boolean lobby) {
		OutputStream stream = new OutputStream();
		stream.writePacket(getPlayer(), lobby ? 26 : 10);//need to fix regular logout
		ChannelFuture future = getSession().write(stream);
		if (future != null)
			future.addListener(ChannelFutureListener.CLOSE);
		else
			session.getChannel().close();
		return this;
	}

	public WorldPacketsEncoder sendPublicMessage(Player p, PublicChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 107);
		stream.writeShort(p.getIndex());
		stream.writeShort(message.getEffects());
		stream.writeByte(ChatMessage.getMessageIcon(p));
		if (message instanceof QuickChatMessage) {
			QuickChatMessage qcMessage = (QuickChatMessage) message;
			stream.writeShort(qcMessage.getFileId());
			if (qcMessage.getMessage(false) != null)
				stream.writeBytes(message.getMessage(false).getBytes());
		} else {
			byte[] chatStr = new byte[250];
			chatStr[0] = (byte) message.getMessage(getPlayer().getDetails().isProfanityFilter()).length();
			int offset = 1
					+ Huffman.encryptMessage(1, message.getMessage(getPlayer().getDetails().isProfanityFilter()).length(),
							chatStr, 0, message.getMessage(getPlayer().getDetails().isProfanityFilter()).getBytes());
			stream.writeBytes(chatStr, 0, offset);
		}
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	@Deprecated
	public WorldPacketsEncoder sendVar(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendVar2(id, value);
		else
			sendVar1(id, value);
		return this;
	}

	@Deprecated
	public WorldPacketsEncoder sendVarBit(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendVarBit2(id, value);
		else
			sendVarBit1(id, value);
		return this;
	}

	public WorldPacketsEncoder sendVar1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(getPlayer(), 7);
		stream.writeShortLE(id);
		stream.write128Byte(value);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendVar2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 8);
		stream.writeShortLE(id);
		stream.writeIntV2(value);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendVarBit1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(getPlayer(), 81);
		stream.writeShort128(id);
		stream.writeByte128(value);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendVarBit2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 73);
		stream.writeIntLE(value);
		stream.writeShortLE128(id);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder closeInterface(int parentUID) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(getPlayer(), 118);
		stream.writeInt(parentUID);
		getSession().write(stream);

		return this;
	}

	public WorldPacketsEncoder sendIComponentSettings(int interfaceId, int componentId, int fromSlot, int toSlot,
			int settingsHash) {
		OutputStream stream = new OutputStream(13);
		stream.writePacket(getPlayer(), 75);
		stream.writeIntV1(interfaceId << 16 | componentId);
		stream.writeShortLE(fromSlot);
		stream.writeShortLE128(toSlot);
		stream.writeIntV1(settingsHash);
		getSession().write(stream);

		return this;
	}

	public WorldPacketsEncoder sendHideIComponent(int interfaceId, int componentId, boolean hidden) {
		OutputStream stream = new OutputStream(6);
		stream.writePacket(getPlayer(), 85);
		stream.writeInt(interfaceId << 16 | componentId);
		stream.writeByteC(hidden ? 1 : 0);
		getSession().write(stream);

		return this;
	}

	public WorldPacketsEncoder sendGlobalConfig(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE)
			sendGlobalConfig2(id, value);
		else
			sendGlobalConfig1(id, value);
		return this;
	}

	public WorldPacketsEncoder sendGlobalConfig1(int id, int value) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(getPlayer(), 11);
		stream.writeByte(value);
		stream.writeShortLE(id);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendGlobalConfig2(int id, int value) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 95);
		stream.writeShort128(id);
		stream.writeInt(value);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendIComponentText(int interfaceId, int componentId, String text) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 64);
		stream.writeIntV2(interfaceId << 16 | componentId);
		stream.writeString(text);
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendGlobalString(int id, String string) {
		OutputStream stream = new OutputStream();
		if (string.length() >= 253) {
			stream.writePacketVarShort(getPlayer(), 114);
			stream.writeString(string);
			stream.writeShort128(id);
			stream.endPacketVarShort();
		} else {
			stream.writePacketVarByte(getPlayer(), 42);
            stream.writeShort(id);
			stream.writeString(string);
			stream.endPacketVarByte();
		}
		getSession().write(stream);
		return this;
	}

	@Deprecated
	public WorldPacketsEncoder sendProjectile(Entity receiver, WorldTile startTile, WorldTile endTile, int gfxId,
			int startHeight, int endHeight, int speed, int delay, int curve, int startDistanceOffset, int creatorSize) {
		sendProjectileProper(startTile, creatorSize, creatorSize, endTile, receiver != null ? receiver.getSize() : 1,
				receiver != null ? receiver.getSize() : 1, receiver, gfxId, startHeight, endHeight, delay,
				(Utility.getDistance(startTile.getX(), startTile.getY(), endTile.getX(), endTile.getY()) * 30
						/ ((speed / 10) < 1 ? 1 : (speed / 10))),
				startDistanceOffset, curve);

		return this;
	}

	public WorldPacketsEncoder sendProjectileProper(WorldTile from, int fromSizeX, int fromSizeY, WorldTile to,
			int toSizeX, int toSizeY, Entity lockOn, int gfxId, int startHeight, int endHeight, int delay, int speed,
			int slope, int angle) {
		WorldTile src = new WorldTile(((from.getX() << 1) + fromSizeX) >> 1, ((from.getY() << 1) + fromSizeY) >> 1,
				from.getPlane());
		WorldTile dst = new WorldTile(((to.getX() << 1) + toSizeX) >> 1, ((to.getY() << 1) + toSizeY) >> 1,
				to.getPlane());
		OutputStream stream = createWorldTileStream(src);
		stream.writePacket(getPlayer(), 28);
		stream.writeByte(((src.getX() & 0x7) << 3) | (src.getY() & 0x7));
		stream.writeByte(dst.getX() - src.getX());
		stream.writeByte(dst.getY() - src.getY());
		stream.writeShort(
				lockOn == null ? 0 : (lockOn.isPlayer() ? -(lockOn.getIndex() + 1) : lockOn.getIndex() + 1));
		stream.writeShort(gfxId);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeShort(delay);
		stream.writeShort(delay + speed);
		stream.writeByte(angle);
		stream.writeShort(angle);
		getSession().write(stream);
		return this;
	}

	public OutputStream createWorldTileStream(WorldTile tile) {
		OutputStream stream = new OutputStream(4);
		stream.writePacket(getPlayer(), 115);
		stream.writeByte128(tile.getPlane());
		stream.writeByte(tile.getLocalY(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize()) >> 3);
		stream.writeByte128(tile.getLocalX(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize()) >> 3);
		return stream;
	}

	public WorldPacketsEncoder sendGroundItem(FloorItem item) {
		OutputStream stream = createWorldTileStream(item.getTile());
		int localX = item.getTile().getLocalX(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int localY = item.getTile().getLocalY(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(getPlayer(), 48);
		stream.writeShort(item.getAmount());
		stream.writeByte128((offsetX << 4) | offsetY);
		stream.writeShortLE128(item.getId());
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendRemoveGroundItem(FloorItem item) {
		OutputStream stream = createWorldTileStream(item.getTile());
		int localX = item.getTile().getLocalX(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int localY = item.getTile().getLocalY(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(getPlayer(), 0);
		stream.writeShort128(item.getId());
		stream.write128Byte((offsetX << 4) | offsetY);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendSpawnedObject(GameObject object) {
		OutputStream stream = createWorldTileStream(object);
		int localX = object.getLocalX(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int localY = object.getLocalY(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(getPlayer(), 1);
		stream.writeShort(object.getId());
		stream.writeByteC((offsetX << 4) | offsetY);
		stream.writeByte((object.getType() << 2) + (object.getRotation() & 0x3));
		getSession().write(stream);

		return this;
	}

	public WorldPacketsEncoder sendDestroyObject(GameObject object) {
		OutputStream stream = createWorldTileStream(object);
		int localX = object.getLocalX(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int localY = object.getLocalY(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writePacket(getPlayer(), 13);
		stream.write128Byte((object.getType() << 2) + (object.getRotation() & 0x3));
		stream.writeByte((offsetX << 4) | offsetY);
		getSession().write(stream);

		return this;
	}

	public WorldPacketsEncoder sendSystemUpdate(int delay) {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(getPlayer(), 100);
		stream.writeShort((int) (delay * 1.6));
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendGraphics(Graphics graphics, WorldTile tile) {
		OutputStream stream = new OutputStream(13);
		stream.writePacket(getPlayer(), 80);
		stream.writeShort128(graphics.getSpeed());
		stream.writeShortLE128(graphics.getId());
		stream.writeShort128(graphics.getHeight());
		stream.writeIntLE(tile.getPlane() << 28 | tile.getX() << 14 | tile.getY() & 0x3fff | 1 << 30);
		stream.writeByte(graphics.getSettings2Hash());
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendIComponentSprite(int interfaceId, int componentId, int spriteId) {
		OutputStream stream = new OutputStream(11);
		stream.writePacket(getPlayer(), 106);
		stream.writeIntLE(spriteId);
		stream.writeShortLE128(interfaceId << 16 | componentId);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendIComponentAnimation(int emoteId, int interfaceId, int componentId) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(getPlayer(), 112);
		stream.writeShortLE(emoteId);
		stream.writeIntLE(interfaceId << 16 | componentId);
		getSession().write(stream);

		return this;
	}

	public WorldPacketsEncoder sendItemOnIComponent(int interfaceid, int componentId, int id, int amount) {
		OutputStream stream = new OutputStream(11);
		stream.writePacket(getPlayer(), 15);
		stream.writeInt(interfaceid << 16 | componentId);
		stream.writeShortLE128(id);
		stream.writeIntLE(amount);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendEntityOnIComponent(boolean isPlayer, int entityId, int interfaceId,
			int componentId) {
		if (isPlayer)
			sendPlayerOnIComponent(interfaceId, componentId);
		else
			sendNPCOnIComponent(interfaceId, componentId, entityId);
		return this;
	}

	public WorldPacketsEncoder sendPlayerOnIComponent(int interfaceId, int componentId) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(getPlayer(), 59);
		stream.writeIntLE(interfaceId << 16 | componentId);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendNPCOnIComponent(int interfaceId, int componentId, int npcId) {
		OutputStream stream = new OutputStream(9);
		stream.writePacket(getPlayer(), 109);
		stream.writeShort(npcId);
		stream.writeIntV1(interfaceId << 16 | componentId);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendObjectAnimation(GameObject object, Animation animation) {
		OutputStream stream = new OutputStream(10);
		stream.writePacket(getPlayer(), 20);
		stream.writeIntV2(object.getTileHash());
		stream.writeByte128((object.getType() << 2) + (object.getRotation() & 0x3));
		stream.writeShort128(animation.getIds()[0]);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendFriendsChatChannel() {
		FriendChatsManager manager = getPlayer().getCurrentFriendChat();
		OutputStream stream = new OutputStream(manager == null ? 3 : manager.getDataBlock().length + 3);
		stream.writePacketVarShort(getPlayer(), 22);
		if (manager != null)
			stream.writeBytes(manager.getDataBlock());
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendFriends() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 34);
		for (String username : player.getFriendsIgnores().getFriends()) {
			String displayName;
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null)
				p2 = World.getLobbyPlayerByDisplayName(username);
			if (p2 != null)
				displayName = p2.getDisplayName();
			else
				displayName = Utility.formatPlayerNameForDisplay(username);
			player.getPackets().sendFriend(Utility.formatPlayerNameForDisplay(username), displayName, 1, p2 != null && player.getFriendsIgnores().isOnline(p2), false, p2 != null && World.containsLobbyPlayer(p2.getUsername()), stream);
		}
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendFriend(String username, String displayName, int world, boolean putOnline,
			boolean warnMessage, boolean isLobby) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 34);
		sendFriend(username, displayName, world, putOnline, warnMessage, isLobby, stream);
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendFriend(String username, String displayName, int world, boolean putOnline,
			boolean warnMessage, boolean isLobby, OutputStream stream) {
		stream.writeByte(warnMessage ? 0 : 1);
		stream.writeString(displayName);
		stream.writeString(displayName.equals(username) ? "" : username);
		stream.writeShort(putOnline ? world : 0);
		stream.writeByte(getPlayer().getFriendsIgnores().getRank(Utility.formatPlayerNameForProtocol(username)));
		if (putOnline) {
			stream.writeString(isLobby ? "<col=FFFF00>Lobby </col>" : "<col=00FF00>" + GameConstants.SERVER_NAME);
			stream.writeByte(0);
		}
		return this;
	}

	public WorldPacketsEncoder sendPrivateMessage(String username, ChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 4);
		stream.writeString(username);
		Huffman.sendEncryptMessage(stream, message.getMessage(getPlayer().getDetails().isProfanityFilter()));
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder receivePrivateMessage(String name, String display, int rights, ChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 2);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display))
			stream.writeString(name);
		for (int i = 0; i < 5; i++)
			stream.writeByte(RandomUtils.inclusive(255));
		stream.writeByte(rights);
		Huffman.sendEncryptMessage(stream, message.getMessage(getPlayer().getDetails().isProfanityFilter()));
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendPlayerOption(String option, int slot, boolean top) {
		sendPlayerOption(option, slot, top, -1);
		return this;
	}

	public WorldPacketsEncoder sendPlayerOption(String option, int slot, boolean top, int cursor) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 6);
		stream.writeString(option);
		stream.writeShort(cursor);
		stream.writeByte(top ? 1 : 0);
		stream.writeByte128(slot);
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendPlayerUnderNPCPriority(boolean priority) {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(getPlayer(), 77);
		stream.writeByte(priority ? 1 : 0);
		getSession().write(stream);
		return this;
	}

	// *********************************************************

	public WorldPacketsEncoder sendCutscene(int id) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 70);
		stream.writeShort(id);
		stream.writeShort(20); // xteas count
		for (int count = 0; count < 20; count++)
			// xteas
			for (int i = 0; i < 4; i++)
				stream.writeInt(0);
		byte[] appearence = getPlayer().getAppearance().getAppeareanceData();
		stream.writeByte(appearence.length);
		stream.addBytes128(appearence, 0, appearence.length);
		stream.endPacketVarShort();
		// //getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendUnlockIComponentOptionSlots(int interfaceId, int componentId, int fromSlot,
			int toSlot, int... optionsSlots) {
		int settingsHash = 0;
		for (int slot : optionsSlots)
			settingsHash |= 2 << slot;
		sendIComponentSettings(interfaceId, componentId, fromSlot, toSlot, settingsHash);
		return this;
	}

	public WorldPacketsEncoder sendUnlockIComponentOptionSlots(int interfaceId, int componentId, int fromSlot,
			int toSlot, boolean unlockEvent, int... optionsSlots) {
		int settingsHash = unlockEvent ? 1 : 0;
		for (int slot : optionsSlots)
			settingsHash |= 2 << slot;
		sendIComponentSettings(interfaceId, componentId, fromSlot, toSlot, settingsHash);
		return this;
	}

	// CUTSCENE PACKETS START

	public WorldPacketsEncoder sendHintIcon(HintIcon icon) {
		OutputStream stream = new OutputStream(13);
		stream.writePacket(getPlayer(), 60);
		stream.writeByte((icon.getTargetType() & 0x1f) | (icon.getIndex() << 5));
		if (icon.getTargetType() == 0)
			stream.skip(11);
		else {
			stream.writeByte(icon.getArrowType());
			if (icon.getTargetType() == 1 || icon.getTargetType() == 10) {
				stream.writeShort(icon.getTargetIndex());
				stream.writeShort(2500); // how often the arrow flashes, 2500
				// ideal, 0 never
				stream.skip(4);
			} else if ((icon.getTargetType() >= 2 && icon.getTargetType() <= 6)) { // directions
				stream.writeByte(icon.getPlane()); // unknown
				stream.writeShort(icon.getCoordX());
				stream.writeShort(icon.getCoordY());
				stream.writeByte(icon.getDistanceFromFloor() * 4 >> 2);
				stream.writeShort(-1); // distance to start showing on minimap,
				// 0 doesnt show, -1 infinite
			}
			stream.writeShort(icon.getModelId());
		}
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendCameraShake(int slotId, int b, int c, int d, int e) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 35);
		stream.writeByteC(b);
		stream.write128Byte(slotId);
		stream.writeByte128(d);
        stream.writeShort128(e);
		stream.writeByte128(c);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendStopCameraShake() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(getPlayer(), 3);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendIComponentModel(int interfaceId, int componentId, int modelId) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 82);
		stream.writeShortLE(modelId);
		stream.writeIntV2(interfaceId << 16 | componentId);
		getSession().write(stream);
		return this;
	}

//	public WorldPacketsEncoder sendGrandExchangeOffer(Offer offer) {
//		OutputStream stream = new OutputStream(21);
//		stream.writePacket(getPlayer(), 53);
//		stream.writeByte(offer.getSlot());
//		stream.writeByte(offer.getStage());
//		if (offer.forceRemove())
//			stream.skip(18);
//		else {
//			stream.writeShort(offer.getId());
//			stream.writeInt(offer.getPrice());
//			stream.writeInt(offer.getAmount());
//			stream.writeInt(offer.getTotalAmmountSoFar());
//			stream.writeInt(offer.getTotalPriceSoFar());
//		}
//		// ////getSession().write(stream);
//	}

	public WorldPacketsEncoder sendTileMessage(String message, WorldTile tile, int color) {
		sendTileMessage(message, tile, 5000, 255, color);
		return this;
	}

	public WorldPacketsEncoder sendTileMessage(String message, WorldTile tile, int delay, int height, int color) {
		OutputStream stream = createWorldTileStream(tile);
		stream.writePacketVarByte(getPlayer(), 107);
		stream.skip(1);
		int localX = tile.getLocalX(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int localY = tile.getLocalY(getPlayer().getLastLoadedMapRegionTile(), getPlayer().getMapSize());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		stream.writeByte((offsetX << 4) | offsetY);
		stream.writeShort(delay / 30);
		stream.writeByte(height);
		stream.write24BitInteger(color);
		stream.writeString(message);
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendRandomOnIComponent(int interfaceId, int componentId, int id) {
		/*
		 * OutputStream stream = new OutputStream(); stream.writePacket(getPlayer(), 235);
		 * stream.writeShort(id); stream.writeIntV1(interfaceId << 16 | componentId);
		 * stream.writeShort(interPacketsCount++); //////getSession().write(stream);
		 */
		return this;
	}

	public WorldPacketsEncoder sendFaceOnIComponent(int interfaceId, int componentId, int look1, int look2, int look3) {
		/*
		 * OutputStream stream = new OutputStream(); stream.writePacket(getPlayer(), 192);
		 * stream.writeIntV2(interfaceId << 16 | componentId);
		 * stream.writeShortLE128(interPacketsCount++); stream.writeShortLE128(look1);
		 * stream.writeShortLE128(look2); stream.writeShort128(look2);
		 * //////getSession().write(stream);
		 */
		return this;
	}

//	public WorldPacketsEncoder sendClanChannel(ClansManager manager, boolean myClan) {
//		OutputStream stream = new OutputStream(manager == null ? 4
//				: manager.getClanChannelDataBlock().length + 4);
//		stream.writePacketVarShort(getPlayer(), 85);
//		stream.writeByte(myClan ? 1 : 0);
//		if (manager != null)
//			stream.writeBytes(manager.getClanChannelDataBlock());
//		stream.endPacketVarShort();
//		// ////getSession().write(stream);
//	}

//	public WorldPacketsEncoder sendClanSettings(ClansManager manager, boolean myClan) {
//		OutputStream stream = new OutputStream(manager == null ? 4
//				: manager.getClanSettingsDataBlock().length + 4);
//		stream.writePacketVarShort(getPlayer(), 133);
//		stream.writeByte(myClan ? 1 : 0);
//		if (manager != null)
//			stream.writeBytes(manager.getClanSettingsDataBlock());
//		stream.endPacketVarShort();
//		// ////getSession().write(stream);
//	}

	public WorldPacketsEncoder sendIgnores() {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 41);
		stream.writeByte(getPlayer().getFriendsIgnores().getIgnores().size());
		for (String username : player.getFriendsIgnores().getIgnores()) {
			String display;
			Player p2 = World.getPlayerByDisplayName(username);
			if (p2 == null)
				p2 = World.getLobbyPlayerByDisplayName(username);
			if (p2 != null)
				display = p2.getDisplayName();
			else
				display = Utility.formatPlayerNameForDisplay(username);
			String name = Utility.formatPlayerNameForDisplay(username);
			stream.writeString(display.equals(name) ? name : display);
			stream.writeString("");
			stream.writeString(display.equals(name) ? "" : name);
			stream.writeString("");
		}
		stream.endPacketVarShort();
        getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendIgnore(String name, String display, boolean updateName) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 51);
		stream.writeByte(0x2);
		stream.writeString(display.equals(name) ? name : display);
		stream.writeString("");
		stream.writeString(display.equals(name) ? "" : name);
		stream.writeString("");
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendGameBarStages() {
		getPlayer().getVarsManager().sendVar(InterfaceVars.GAME_BAR_STATUS_CLAN, getPlayer().getDetails().getClanStatus());
		getPlayer().getVarsManager().sendVar(InterfaceVars.GAME_BAR_STATUS_ASSIST, getPlayer().getDetails().getAssistStatus());
		getPlayer().getVarsManager().sendVarBit(6161, getPlayer().getDetails().isFilterGame() ? 1 : 0);
		getPlayer().getVarsManager().sendVar(InterfaceVars.GAME_BAR_STATUS_FRIENDS_IGNORE, getPlayer().getFriendsIgnores().getFriendsChatStatus());
		sendOtherGameBarStages();
		sendPrivateGameBarStage();
		return this;
	}

	public WorldPacketsEncoder sendOtherGameBarStages() {
		OutputStream stream = new OutputStream(3);
		stream.writePacket(getPlayer(), 104);
		stream.write128Byte(getPlayer().getDetails().getTradeStatus());
		stream.writeByte(getPlayer().getDetails().getPublicStatus());
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendPrivateGameBarStage() {
		OutputStream stream = new OutputStream(2);
		stream.writePacket(getPlayer(), 77);
		stream.writeByte(getPlayer().getFriendsIgnores().getPrivateStatus());
		getSession().write(stream);
		return this;
	}
	// 131 clan chat quick message

	public WorldPacketsEncoder receivePrivateChatQuickMessage(String name, String display, int rights,
			QuickChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 30);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display))
			stream.writeString(name);
		for (int i = 0; i < 5; i++)
			stream.writeByte(RandomUtils.inclusive(255));
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getMessage(false) != null)
			stream.writeBytes(message.getMessage(false).getBytes());
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendPrivateQuickMessageMessage(String username, QuickChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 61);
		stream.writeString(username);
		stream.writeShort(message.getFileId());
		if (message.getMessage(false) != null)
			stream.writeBytes(message.getMessage(false).getBytes());
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder receiveClanChatMessage(boolean myClan, String display, int rights, ChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 3);
		stream.writeByte(myClan ? 1 : 0);
		stream.writeString(display);
		for (int i = 0; i < 5; i++)
			stream.writeByte(RandomUtils.inclusive(255));
		stream.writeByte(rights);
		Huffman.sendEncryptMessage(stream, message.getMessage(getPlayer().getDetails().isProfanityFilter()));
		stream.endPacketVarByte();
		// ////getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder receiveClanChatQuickMessage(boolean myClan, String display, int rights,
			QuickChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 1);
		stream.writeByte(myClan ? 1 : 0);
		stream.writeString(display);
		for (int i = 0; i < 5; i++)
			stream.writeByte(RandomUtils.inclusive(255));
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getMessage(false) != null)
			stream.writeBytes(message.getMessage(false).getBytes());
		stream.endPacketVarByte();
		// ////getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder receiveFriendChatMessage(String name, String display, int rights, String chatName,
			ChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 139);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display))
			stream.writeString(name);
		stream.writeLong(Utility.stringToLong(chatName));
		for (int i = 0; i < 5; i++)
			stream.writeByte(RandomUtils.inclusive(255));
		stream.writeByte(rights);
		Huffman.sendEncryptMessage(stream, message.getMessage(getPlayer().getDetails().isProfanityFilter()));
		stream.endPacketVarByte();
		// ////getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder receiveFriendChatQuickMessage(String name, String display, int rights, String chatName,
			QuickChatMessage message) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 32);
		stream.writeByte(name.equals(display) ? 0 : 1);
		stream.writeString(display);
		if (!name.equals(display))
			stream.writeString(name);
		stream.writeLong(Utility.stringToLong(chatName));
		for (int i = 0; i < 5; i++)
			stream.writeByte(RandomUtils.inclusive(255));
		stream.writeByte(rights);
		stream.writeShort(message.getFileId());
		if (message.getMessage(false) != null)
			stream.writeBytes(message.getMessage(false).getBytes());
		stream.endPacketVarByte();
		// ////getSession().write(stream);
		return this;
	}

	/*
	 * useless, sending friends unlocks it
	 */
	public WorldPacketsEncoder sendUnlockIgnoreList() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(getPlayer(), 74);
		getSession().write(stream);
		return this;
	}

	/*
	 * dynamic map region
	 */
	public WorldPacketsEncoder sendDynamicGameScene(boolean sendLswp) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(getPlayer(), 12);
		if (sendLswp) // exists on newer protocol, sends all player encoded
			// region ids, afterwards new pupdate protocol is
			// regionbased
			getPlayer().getLocalPlayerUpdate().init(stream);
		int middleChunkX = getPlayer().getChunkX();
		int middleChunkY = getPlayer().getChunkY();
		stream.writeByte(1);
		stream.writeByteC(getPlayer().getDetails().isForceNextMapLoadRefresh() ? 1 : 0);
		stream.writeShort128(middleChunkY);
		stream.writeShort(middleChunkX);
		stream.write128Byte(getPlayer().getMapSize());
		stream.initBitAccess();
		/*
		 * cene length in chunks. scene tiles length / 16, 8 is a chunk size, 16 because
		 * the code behind its signed and goes from middle-length to middle+length
		 */
		int sceneLength = GameConstants.MAP_SIZES[getPlayer().getMapSize()] >> 4;
		// the regionids(maps files) that will be used to load this scene
		int[] regionIds = new int[4 * sceneLength * sceneLength];
		int newRegionIdsCount = 0;
		for (int plane = 0; plane < 4; plane++) {
			for (int realChunkX = (middleChunkX - sceneLength); realChunkX <= ((middleChunkX
					+ sceneLength)); realChunkX++) {
				int regionX = realChunkX / 8;
				y: for (int realChunkY = (middleChunkY - sceneLength); realChunkY <= ((middleChunkY
						+ sceneLength)); realChunkY++) {
					int regionY = realChunkY / 8;
					// rcx / 8 = rx, rcy / 8 = ry, regionid is encoded region x
					// and y
					int regionId = (regionX << 8) + regionY;
					Region region = World.getRegions().get(regionId);
					int newChunkX;
					int newChunkY;
					int newPlane;
					int rotation;
					if (region instanceof DynamicRegion) { // generated map
						DynamicRegion dynamicRegion = (DynamicRegion) region;
						int[] pallete = dynamicRegion.getRegionCoords()[plane][realChunkX - (regionX * 8)][realChunkY
								- (regionY * 8)];
						newChunkX = pallete[0];
						newChunkY = pallete[1];
						newPlane = pallete[2];
						rotation = pallete[3];
					} else { // real map
						newChunkX = realChunkX;
						newChunkY = realChunkY;
						newPlane = plane;
						rotation = 0;// no rotation
					}
					// invalid chunk, not built chunk
					if (newChunkX == 0 || newChunkY == 0)
						stream.writeBits(1, 0);
					else {
						stream.writeBits(1, 1);
						// chunk encoding = (x << 14) | (y << 3) | (plane <<
						// 24), theres addition of two more bits for rotation
						stream.writeBits(26, (rotation << 1) | (newPlane << 24) | (newChunkX << 14) | (newChunkY << 3));
						int newRegionId = (((newChunkX / 8) << 8) + (newChunkY / 8));
						for (int index = 0; index < newRegionIdsCount; index++)
							if (regionIds[index] == newRegionId)
								continue y;
						regionIds[newRegionIdsCount++] = newRegionId;
					}

				}
			}
		}
		stream.finishBitAccess();
		for (int index = 0; index < newRegionIdsCount; index++) {
			int[] xteas = MapArchiveKeys.getMapKeys(regionIds[index]);
			if (xteas == null)
				xteas = new int[4];
			for (int keyIndex = 0; keyIndex < 4; keyIndex++)
				stream.writeInt(xteas[keyIndex]);
		}
		stream.endPacketVarShort();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendDelayedGraphics(Graphics graphics, int delay, WorldTile tile) {

		return this;
	}

	public WorldPacketsEncoder sendNPCInterface(NPC npc, boolean nocliped, int windowId, int windowComponentId,
			int interfaceId) {
		int[] xteas = new int[4];
		OutputStream stream = new OutputStream(26);
		stream.writePacket(getPlayer(), 57);
		stream.writeIntV2(xteas[0]);
		stream.writeShortLE128(npc.getIndex());
		stream.writeByte128(nocliped ? 1 : 0);
		stream.writeInt(xteas[3]);
		stream.writeShortLE128(interfaceId);
		stream.writeIntLE(xteas[2]);
		stream.writeIntV2(xteas[1]);
		stream.writeIntV1(windowId << 16 | windowComponentId);
		// ////getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendObjectInterface(GameObject object, boolean nocliped, int windowId,
			int windowComponentId, int interfaceId) {
		int[] xteas = new int[4];
		OutputStream stream = new OutputStream(33);
		stream.writePacket(getPlayer(), 143);
		stream.writeIntV2(xteas[1]);
		stream.writeByte(nocliped ? 1 : 0);
		stream.writeIntLE(xteas[2]);
		stream.writeIntV1(object.getId());
		stream.writeByte128((object.getType() << 2) | (object.getRotation() & 0x3));
		stream.writeInt((object.getPlane() << 28) | (object.getX() << 14) | object.getY()); // the
		// hash
		// for
		// coords,
		stream.writeIntV2((windowId << 16) | windowComponentId);
		stream.writeShort(interfaceId);
		stream.writeInt(xteas[3]);
		stream.writeInt(xteas[0]);
		// ////getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendNPCMessage(int border, int color, NPC npc, String message) {
		sendGameMessage(message);
		sendGlobalString(306, message);
		sendGlobalConfig(1699, color);
		sendGlobalConfig(1700, border);
		sendGlobalConfig(1695, 1);
		sendNPCInterface(npc, true, 746, 0, 1177);
		return this;
	}

	public WorldPacketsEncoder sendObjectMessage(int border, int color, GameObject object, String message) {
		sendGameMessage(message);
		sendGlobalString(306, message);
		sendGlobalConfig(1699, color);
		sendGlobalConfig(1700, border);
		sendGlobalConfig(1695, 1);
		sendObjectInterface(object, true, 746, 0, 1177);
		return this;
	}

	// instant
	public WorldPacketsEncoder sendCameraLook(int viewLocalX, int viewLocalY, int viewZ) {
		sendCameraLook(viewLocalX, viewLocalY, viewZ, -1, -1);
		return this;
	}

	public WorldPacketsEncoder sendCameraLook(int viewLocalX, int viewLocalY, int viewZ, int speed1, int speed2) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 25);
        stream.writeByte(speed1);
        stream.writeByte(speed2);
        stream.writeShort128(viewZ >> 2);
        stream.writeByte128(viewLocalX);
		stream.writeByte(viewLocalY);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendResetCamera() {
		OutputStream stream = new OutputStream(1);
		stream.writePacket(getPlayer(), 44);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendCameraRotation(int unknown1, int unknown2) {
		OutputStream stream = new OutputStream(5);
		stream.writePacket(getPlayer(), 56);
		stream.writeShortLE(unknown1);
		stream.writeShortLE128(unknown1);
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendCameraPos(int moveLocalX, int moveLocalY, int moveZ) {
		sendCameraPos(moveLocalX, moveLocalY, moveZ, -1, -1);
		return this;
	}
	
	public WorldPacketsEncoder sendClientConsoleCommand(String command) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 61);
		stream.writeString(command);
		stream.endPacketVarByte();
		return this;
	}

	public WorldPacketsEncoder sendOpenURL(String url) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 49);
		stream.writeString(url);
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendSetMouse(String walkHereReplace, int cursor) {
		OutputStream stream = new OutputStream();
		stream.writePacketVarByte(getPlayer(), 90);
		stream.writeString(walkHereReplace);
		stream.writeShort(cursor);
		stream.endPacketVarByte();
		getSession().write(stream);
		return this;
	}

	public WorldPacketsEncoder sendCameraPos(int moveLocalX, int moveLocalY, int moveZ, int speed1, int speed2) {
		OutputStream stream = new OutputStream(7);
		stream.writePacket(getPlayer(), 102);
        stream.writeByte(speed1);
        stream.writeByteC(moveLocalY);
        stream.write128Byte(moveLocalX);
		stream.writeByteC(speed2);
		stream.writeShort(moveZ >> 2);
		getSession().write(stream);
		return this;
	}
	
	public void sendWorldList(boolean full) {
		OutputStream packet = new OutputStream();
		packet.writePacketVarShort(player, 119); //world list packet #encoder
		packet.writeByte(1);
		packet.writeByte(2);
		packet.writeByte(full ? 1 : 0);
		int size = WorldList.WORLDS.size();
		if (full) {
			packet.writeSmart(size);
			for (int world = 1; world <= WorldList.WORLDS.size(); world++) {
				packet.writeSmart(WorldList.getWorld(world).getCountryId()); //Country Flag Icon
				packet.writeGJString(WorldList.getWorld(world).getCountryName());
			}
			packet.writeSmart(0);
			packet.writeSmart(size + 1);
			packet.writeSmart(size);
			for (int world = 1; world <= WorldList.WORLDS.size(); world++) {
				packet.writeSmart(world); 
				packet.writeByte(0); 
				int flags = 0;
				if (WorldList.WORLDS.get(world).isMembers()) flags |= 0x1;
				flags |= 0x8;
				packet.writeInt(flags); 
				packet.writeGJString(WorldList.getWorld(world).getActivity());
				packet.writeGJString(WorldList.WORLDS.get(world).getIP());
			}
			packet.writeInt(-1723296702);
		}
		for (int world = 1; world <= WorldList.WORLDS.size(); world++) {
			packet.writeSmart(world);
			packet.writeShort(World.players.getSize());
		}
		packet.endPacketVarShort();
		session.write(packet);
	}

	public void sendInputIntegerScript(String message, IntegerInputAction onInputGivenAction) {
		getPlayer().getAttributes().get(Attribute.INTEGER_INPUT_ACTION).set(onInputGivenAction);
		sendRunScript(108, new Object[] { message });
	}
	
	public void sendInputStringScript(String message, StringInputAction onInputGivenAction) {
		getPlayer().getAttributes().get(Attribute.STRING_INPUT_ACTION).set(onInputGivenAction);
		sendRunScript(110, new Object[] { message });
	}

	public void test(int item, int zoom) {
		sendRunScript(3449, new Object[] { item, 650 });
	}
}