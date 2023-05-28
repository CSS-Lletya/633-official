package com.rs.game.player;

import com.rs.cache.loaders.NPCDefinitions;
import com.rs.game.item.Item;
import com.rs.io.OutputStream;
import com.rs.utilities.Utility;

import lombok.Getter;

public class Appearance {

	private transient int renderEmote;
	private int title;
	private short[] lookI;
	private byte[] colour;
	
	@Getter
	private boolean male;
	@Getter
	private transient byte[] appeareanceData;
	@Getter
	private transient byte[] md5AppeareanceDataHash;
	
	private transient short transformedNpcId;
	
	@Getter
	private transient boolean hidePlayer;

	@Getter
	private transient int forcedWeapon, forcedShield, forcedAmulet;

	private transient Player player;

	public Appearance() {
		male = true;
		renderEmote = -1;
		title = -1;
		resetAppearence();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
		transformedNpcId = -1;
		renderEmote = -1;
		forcedWeapon = forcedShield = forcedAmulet = -1;
		resetAppearence();
	}

	public void transformIntoNPC(int id) {
		transformedNpcId = (short) id;
		generateAppearenceData();
	}

	public void switchHidden() {
		hidePlayer = !hidePlayer;
		generateAppearenceData();
	}

	public void setHidden(boolean hidden) {
		hidePlayer = hidden;
		generateAppearenceData();
	}
	
	public void generateAppearenceData() {
		OutputStream stream = new OutputStream();
		int flag = 0;
		if (!male)
			flag |= 0x1;
		if (transformedNpcId >= 0 && NPCDefinitions.getNPCDefinitions(transformedNpcId).aBoolean3190)
			flag |= 0x2;

		stream.writeByte(flag);
		stream.writeByte(title);
		stream.writeByte(hasSkull() ? player.getDetails().getSkullId() : -1);
		stream.writeByte(player.getPrayer().getPrayerHeadIcon()); // prayer icon
		stream.writeByte(hidePlayer ? 1 : 0);
		// npc
		if (transformedNpcId >= 0) {
			stream.writeShort(-1); // 65535 tells it a npc
			stream.writeShort(transformedNpcId);
			stream.writeByte(0);
		} else {
			for (int index = 0; index < 4; index++) {
				if (index == Equipment.SLOT_WEAPON && forcedWeapon != -1)
					stream.writeShort(16384 + forcedWeapon);
				else if (index == Equipment.SLOT_AMULET && forcedAmulet != -1)
					stream.writeShort(16384 + forcedAmulet);
				else {
					Item item = player.getEquipment().getItems().get(index);
					if (item == null)
						stream.writeByte(0);
					else
						stream.writeShort(16384 + item.getId());
				}
			}
			Item item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			stream.writeShort(item == null ? 0x100 + lookI[2] : 16384 + item.getId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);
			if (item == null || forcedShield != -1) {
				if (forcedShield == -1)
					stream.writeByte(0);
				else
					stream.writeShort(16384 + forcedShield);
			} else
				stream.writeShort(16384 + item.getId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			if (item == null || !Equipment.hideArms(item))
				stream.writeShort(0x100 + lookI[3]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
			stream.writeShort(item == null ? 0x100 + lookI[5] : 16384 + item.getId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
			if ((item == null || !Equipment.hideHair(item)))
				stream.writeShort(0x100 + lookI[0]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
			stream.writeShort(item == null ? 0x100 + lookI[4] : 16384 + item.getId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
			stream.writeShort(item == null ? 0x100 + lookI[6] : 16384 + item.getId());
			// tits for female, bear for male
			item = player.getEquipment().getItems().get(male ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
			if (item == null || (male && Equipment.showBeard(item)))
				stream.writeShort(0x100 + lookI[1]);
			else
				stream.writeByte(0);
		}

		for (int index = 0; index < colour.length; index++)
			// colour length 10
			stream.writeByte(colour[index]);

		stream.writeShort(getRenderEmote());
		stream.writeString(player.getDisplayName());
		boolean pvpArea = player.isPvpArea(player);
		stream.writeByte(
				pvpArea ? player.getSkills().getCombatLevel() : player.getSkills().getCombatLevelWithSummoning());
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevelWithSummoning() : 0);
		stream.writeByte(-1); // higher level acc name appears in front :P
		stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
		// need to send more
		// data
		if (transformedNpcId >= 0) {
			NPCDefinitions defs = NPCDefinitions.getNPCDefinitions(transformedNpcId);
			stream.writeShort(defs.anInt876);
			stream.writeShort(defs.anInt842);
			stream.writeShort(defs.anInt884);
			stream.writeShort(defs.anInt875);
			stream.writeByte(defs.anInt875);
		}

		// done separated for safe because of synchronization
		byte[] appeareanceData = new byte[stream.getOffset()];
		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0, appeareanceData.length);
		byte[] md5Hash = Utility.encryptUsingMD5(appeareanceData);
		this.appeareanceData = appeareanceData;
		md5AppeareanceDataHash = md5Hash;
	}

	public int getSize() {
		if (transformedNpcId >= 0)
			return NPCDefinitions.getNPCDefinitions(transformedNpcId).size;
		return 1;
	}

	public void setRenderEmote(int id) {
		this.renderEmote = id;
		generateAppearenceData();
	}

	public int getRenderEmote() {
		if (renderEmote >= 0)
			return renderEmote;
		if (transformedNpcId >= 0)
			return NPCDefinitions.getNPCDefinitions(transformedNpcId).renderEmote;
		return player.getEquipment().getWeaponRenderEmote();
	}

	public void resetAppearence() {
		lookI = new short[7];
		colour = new byte[5];
		male();
	}

	public void male() {
		lookI[0] = 310; // Hair
		lookI[1] = 14; // Beard
		lookI[2] = 473; // Torso
		lookI[3] = 26; // Arms
		lookI[4] = 34; // Bracelets
		lookI[5] = 626; // Legs
		lookI[6] = 42; // Shoes~

		colour[2] = 3;
		colour[1] = 2;
		colour[0] = 0; // hair color
		male = true;
	}

	public void female() {
		lookI[0] = 48; // Hair
		lookI[1] = 48; // Beard
		lookI[2] = 57; // Torso
		lookI[3] = 65; // Arms
		lookI[4] = 68; // Bracelets
		lookI[5] = 77; // Legs
		lookI[6] = 80; // Shoes

		colour[2] = 16;
		colour[1] = 16;
		colour[0] = 3;
		male = false;
	}

	public void setLook(int i, short i2) {
		lookI[i] = i2;
	}

	public void setColor(int i, short i2) {
		colour[i] = (byte) i2;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public void setHairStyle(short i) {
		lookI[0] = i;
	}

	public void setTopStyle(short i) {
		lookI[2] = i;
	}

	public void setBootsStyle(short i) {
		lookI[6] = i;
	}

	public int getTopStyle() {
		return lookI[2];
	}

	public void setArmsStyle(short i) {
		lookI[3] = i;
	}

	public void setWristsStyle(short i) {
		lookI[4] = i;
	}

	public void setLegsStyle(short i) {
		lookI[5] = i;
	}

	public int getHairStyle() {
		return lookI[0];
	}

	public void setBeardStyle(short i) {
		lookI[1] = i;
	}

	public int getBeardStyle() {
		return lookI[1];
	}

	public void setSkinColor(int color) {
		colour[4] = (byte) color;
	}

	public int getSkinColor() {
		return colour[4];
	}

	public void setHairColor(int color) {
		colour[0] = (byte) color;
	}

	public void setTopColor(int color) {
		colour[1] = (byte) color;
	}

	public void setLegsColor(int color) {
		colour[2] = (byte) color;
	}

	public int getHairColor() {
		return colour[0];
	}

	public int getBootColor() {
		return colour[5];
	}

	public void setBootsColor(int color) {
		colour[3] = (byte) color;
	}

	public void setTitle(int title) {
		this.title = title;
		generateAppearenceData();
	}

	public boolean isNPC() {
		return transformedNpcId != -1;
	}

	public void setForcedWeapon(int forcedWeapon) {
		this.forcedWeapon = forcedWeapon;
		generateAppearenceData();
	}
	
	public void setForcedShield(int forcedShield) {
		this.forcedShield = forcedShield;
		generateAppearenceData();
	}

	public void setForcedAmulet(int forcedAmulet) {
		this.forcedAmulet = forcedAmulet;
		generateAppearenceData();
	}

	public boolean hasSkull() {
		return player.getDetails().getSkullTimer().get() > 0;
	}
}