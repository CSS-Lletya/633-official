package com.rs.net.updating;

import java.util.Iterator;
import java.util.LinkedList;

import com.rs.GameConstants;
import com.rs.game.map.World;
import com.rs.game.npc.NPC;
import com.rs.game.player.Hit;
import com.rs.game.player.Player;
import com.rs.io.OutputStream;
import com.rs.utilities.Utility;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public final class LocalNPCUpdate {

	private transient Player player;
	private LinkedList<NPC> localNPCs;

	public void reset() {
		localNPCs.clear();
	}

	public LocalNPCUpdate(Player player) {
		this.player = player;
		localNPCs = new LinkedList<NPC>();
	}

	public OutputStream createPacketAndProcess() {
		OutputStream stream = new OutputStream();
		OutputStream updateBlockData = new OutputStream();
		stream.writePacketVarShort(player, 16);
		processLocalNPCsInform(stream, updateBlockData);
		stream.writeBytes(updateBlockData.getBuffer(), 0,
				updateBlockData.getOffset());
		stream.endPacketVarShort();
		return stream;
	}

	private void processLocalNPCsInform(OutputStream stream,
			OutputStream updateBlockData) {
		stream.initBitAccess();
		processInScreenNPCs(stream, updateBlockData);
		addInScreenNPCs(stream, updateBlockData);
		if (updateBlockData.getOffset() > 0)
			stream.writeBits(15, 32767);
		stream.finishBitAccess();
	}

	private void processInScreenNPCs(OutputStream stream,
			OutputStream updateBlockData) {
		stream.writeBits(8, localNPCs.size());
		// for (NPC n : localNPCs.toArray(new NPC[localNPCs.size()])) {
		for (Iterator<NPC> it = localNPCs.iterator(); it.hasNext();) {
			NPC n = it.next();
			if (n.isFinished() || !n.withinDistance(player, 14)
					|| n.isTeleported()) {
				stream.writeBits(1, 1);
				stream.writeBits(2, 3);
				it.remove();
				continue;
			}
			boolean needUpdate = n.needMasksUpdate();
			boolean walkUpdate = n.getNextWalkDirection() != -1;
			stream.writeBits(1, (needUpdate || walkUpdate) ? 1 : 0);
			if (walkUpdate) {
				stream.writeBits(2, n.getNextRunDirection() == -1 ? 1 : 2);
				if (n.getNextRunDirection() != -1)
					stream.writeBits(1, 1);
				stream.writeBits(3,
						Utility.getNpcMoveDirection(n.getNextWalkDirection()));
				if (n.getNextRunDirection() != -1)
					stream.writeBits(3,
							Utility.getNpcMoveDirection(n.getNextRunDirection()));
				stream.writeBits(1, needUpdate ? 1 : 0);
			} else if (needUpdate)
				stream.writeBits(2, 0);
			if (needUpdate)
				appendUpdateBlock(n, updateBlockData, false);
		}
	}

	private void addInScreenNPCs(OutputStream stream,
			OutputStream updateBlockData) {
		for (int regionId : player.getMapRegionsIds()) {
			ObjectArrayList<Short> indexes = World.getRegion(regionId).getNpcsIndexes();
			if (indexes == null)
				continue;
			for (int npcIndex : indexes) {
				if (localNPCs.size() == GameConstants.LOCAL_NPCS_LIMIT)
					break;
				NPC n = World.getNPCs().get(npcIndex);
				if (n == null || n.isFinished() || localNPCs.contains(n)
						|| !n.withinDistance(player, 14) || n.isDead())
					continue;
				boolean needUpdate = n.needMasksUpdate() || n.getLastFaceEntity() != -1;
				int x = n.getX() - player.getX();
				int y = n.getY() - player.getY();
				stream.writeBits(15, n.getIndex());
				
			
				stream.writeBits(1,  n.isTeleported()  ? 1: 0);
				stream.writeBits(1, needUpdate ? 1 : 0);
				stream.writeBits(14, n.getId());
				stream.writeBits(3, (n.getDirection() >> 11) - 4);
				if (x < 15)
					x += 32;
				stream.writeBits(5, x);
				stream.writeBits(2, n.getPlane());
				if (y < 15)
					y += 32;
				stream.writeBits(5, y);
				localNPCs.add(n);
				if (needUpdate)
					appendUpdateBlock(n, updateBlockData, true);
			}
		}
	}

	private void appendUpdateBlock(NPC n, OutputStream data, boolean added) {
		int maskData = 0;
		
		if (n.getNextForceTalk() != null) { //3
			maskData |= 0x3;
		}
		
		if (!n.getNextHits().isEmpty()) { //4
			maskData |= 0x40;
		}

		
		if (n.getNextFaceWorldTile() != null && n.getNextRunDirection() == -1
				&& n.getNextWalkDirection() == -1) { //5
			maskData |= 0x20;
		}
		
		if (n.getNextFaceEntity() != -2
				|| (added && n.getLastFaceEntity() != -1)) { //6
			maskData |= 0x10;
		}
		
		if (n.getNextGraphics1() != null) { //8
			maskData |= 0x4;
		}
		
		if (n.getNextGraphics2() != null) { //9
			maskData |= 0x2000;
		}
		
		
		if (n.getNextAnimation() != null) { //11
			maskData |= 0x80;
		}
		
		if (n.getNextTransformation() != null) {//12
			maskData |= 0x8;
		}
		
		if (maskData > 128)
			maskData |= 0x2;
		data.writeByte(maskData);
		if (maskData > 128)
			data.writeByte(maskData >> 8);
		
		if (n.getNextForceTalk() != null) { //3
			applyForceTalkMask(n, data);
		}
		
		if (!n.getNextHits().isEmpty()) { //4
			applyHitMask(n, data);
		}

		
		
		if (n.getNextFaceWorldTile() != null) { //5
			applyFaceWorldTileMask(n, data);
		}

		if (n.getNextFaceEntity() != -2
				|| (added && n.getLastFaceEntity() != -1)) { //6
			applyFaceEntityMask(n, data);
		}

		if (n.getNextGraphics1() != null) { //8
			applyGraphicsMask1(n, data);
		}

		if (n.getNextGraphics2() != null) { //9
			applyGraphicsMask2(n, data);
		}
		
		if (n.getNextAnimation() != null) { //11
			applyAnimationMask(n, data);
		}
		

		if (n.getNextTransformation() != null) { //12
			applyTransformationMask(n, data);
		}

	}
	
	private void applyTransformationMask(NPC n, OutputStream data) {
		data.writeShort128(n.getNextTransformation().getToNPCId());
	}

	private void applyForceTalkMask(NPC n, OutputStream data) {
		data.writeString(n.getNextForceTalk().getText());
	}

	//don't think any npcs used this in this revision?
	@SuppressWarnings("unused")
	private void applyForceMovementMask(NPC n, OutputStream data) {
//		data.write128Byte(n.getNextForceMovement().getFirst().getX()
//				- n.getX());
//		data.writeByte(n.getNextForceMovement().getFirst().getY()
//				- n.getY());
//		data.writeByteC(n.getNextForceMovement().getSecond() == null ? 0
//				: n.getNextForceMovement().getSecond().getX() - n.getX());
//		data.writeByteC(n.getNextForceMovement().getSecond() == null ? 0
//				: n.getNextForceMovement().getSecond().getY() - n.getY());
//		data.writeShortLE((n.getNextForceMovement().getFirstSpeed() * 600) / 20);
//		data.writeShortLE128(n.getNextForceMovement().getSecond() == null ? 0
//				: ((n.getNextForceMovement().getSecondSpeed() * 600) / 20));
//		data.writeShort128(n.getNextForceMovement().getDirection().getId());
	}

	private void applyFaceWorldTileMask(NPC n, OutputStream data) {
		data.writeShortLE((n.getNextFaceWorldTile().getX() << 1) + 1);
		data.writeShort128((n.getNextFaceWorldTile().getY() << 1) + 1);
	}

	private void applyHitMask(NPC n, OutputStream data) {
		
		// TODO fix this as this was just for testing
		Hit hit = n.getNextHits().get(0);
		data.writeSmart(hit.getDamage());
		data.writeByte128(hit.getMark(player, n));
		int hp = n.getHitpoints();
		int maxHp = n.getMaxHitpoints();
		if (hp > maxHp)
			hp = maxHp;
		int hpBarPercentage = hp * 255 / maxHp;
		data.writeByte128(hpBarPercentage);
	}

	private void applyFaceEntityMask(NPC n, OutputStream data) {
		data.writeShort128(n.getNextFaceEntity() == -2 ? n.getLastFaceEntity() : n
				.getNextFaceEntity());
	}

	private void applyAnimationMask(NPC n, OutputStream data) {
		for (int id : n.getNextAnimation().getIds())
			data.writeShort128(id);
		data.writeByteC(n.getNextAnimation().getSpeed());
	}

	private void applyGraphicsMask2(NPC n, OutputStream data) {
		data.writeShort(n.getNextGraphics2().getId());
		data.writeIntV1(n.getNextGraphics2().getSettingsHash());
		data.writeByte(n.getNextGraphics2().getSettings2Hash());
	}

	private void applyGraphicsMask1(NPC n, OutputStream data) {
		data.writeShort128(n.getNextGraphics1().getId());
		data.writeIntV2(n.getNextGraphics1().getSettingsHash());
		data.writeByteC(n.getNextGraphics1().getSettings2Hash());
	}

}
