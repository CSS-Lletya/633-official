package com.rs.net.decoders;

import java.util.stream.IntStream;

import com.rs.GameConstants;
import com.rs.game.player.Player;
import com.rs.io.InputStream;
import com.rs.net.LogicPacket;
import com.rs.net.Session;
import com.rs.net.packets.outgoing.OutgoingPacketDispatcher;

public final class WorldPacketsDecoder extends Decoder {

	/**
	 * The packet sizes.
	 */
	public static final byte[] PACKET_SIZES = new byte[256];

	// Converted
	private final static int WALKING_PACKET = 36;
	private final static int MINI_WALKING_PACKET = 43;
	private final static int ATTACK_NPC = 21;
	public final static int ACTION_BUTTON1_PACKET = 11;
	public final static int ACTION_BUTTON2_PACKET = 29;
	public final static int ACTION_BUTTON3_PACKET = 31;
	public final static int ACTION_BUTTON4_PACKET = 9;
	public final static int ACTION_BUTTON5_PACKET = 32;
	public final static int ACTION_BUTTON6_PACKET = 72;
	public final static int ACTION_BUTTON7_PACKET = 19;
	public final static int ACTION_BUTTON8_PACKET = 12;
	public final static int ACTION_BUTTON9_PACKET = 18;
	public final static int ACTION_BUTTON10_PACKET = 72;
	private final static int NPC_CLICK1_PACKET = 22;
	private final static int NPC_CLICK2_PACKET = 24;
	private final static int NPC_CLICK3_PACKET = 27;
	private final static int NPC_CLICK4_PACKET = 80;
	private final static int INTERFACE_ON_NPC = 2;
	private final static int INTERFACE_ON_OBJECT = 58;
	private final static int INTERFACE_ON_PLAYER = 34;
	private final static int SWITCH_INTERFACE_COMPONENTS_PACKET = 10;
	private final static int PLAYER_OPTION_1_PACKET = 25;
	private final static int PLAYER_OPTION_2_PACKET = 76;
	private final static int PLAYER_OPTION_3_PACKET = 44;
	private final static int PLAYER_OPTION_4_PACKET = 51;
	private final static int ITEM_TAKE_PACKET = 30;
	private final static int ITEM_SECOND_OPTION_PACKET = 84;
	public final static int WORLD_MAP_CLICK = 23;
	public final static int RECEIVE_PACKET_COUNT_PACKET = 71;
	private final static int OBJECT_CLICK1_PACKET = 75;
	private final static int OBJECT_CLICK2_PACKET = 26;
	private final static int OBJECT_CLICK3_PACKET = 37;
	private final static int OBJECT_CLICK4_PACKET = 13; 
	private final static int CAMERA_MOVEMENT_PACKET = 5;
	//World List Decoder 
	@SuppressWarnings("unused")
	private final static int WORLD_LIST_UPDATE = 60;
	
	//TODO: Convert; check -1 packets in new handler as well.
	private final static int PLAYER_OPTION_6_PACKET = -1;
	private final static int PLAYER_OPTION_9_PACKET = -1;

	private final Player player;

	public static void loadPacketSizes() {
		IntStream.range(0, PACKET_SIZES.length).forEach(size -> PACKET_SIZES[size] = -3);
		PACKET_SIZES[17] = -1;
		PACKET_SIZES[76] = 3;
		PACKET_SIZES[46] = 3;
		PACKET_SIZES[82] = -1;
		PACKET_SIZES[71] = 2;
		PACKET_SIZES[28] = -1;
		PACKET_SIZES[35] = -1;
		PACKET_SIZES[10] = 16;
		PACKET_SIZES[1] = -1;
		PACKET_SIZES[5] = 4;
		PACKET_SIZES[69] = -1;
		PACKET_SIZES[62] = 6;
		PACKET_SIZES[64] = -1;
		PACKET_SIZES[21] = 3;
		PACKET_SIZES[75] = 7;
		PACKET_SIZES[68] = -1;
		PACKET_SIZES[45] = -1;
		PACKET_SIZES[36] = 5;
		PACKET_SIZES[77] = 1;
		PACKET_SIZES[8] = 2;
		PACKET_SIZES[52] = 3;
		PACKET_SIZES[74] = 8;
		PACKET_SIZES[3] = -1;
		PACKET_SIZES[12] = 8;
		PACKET_SIZES[16] = 1;
		PACKET_SIZES[19] = 8;
		PACKET_SIZES[61] = 6;
		PACKET_SIZES[7] = 2;
		PACKET_SIZES[32] = 8;
		PACKET_SIZES[56] = 4;
		PACKET_SIZES[41] = 2;
		PACKET_SIZES[24] = 3;
		PACKET_SIZES[44] = 3;
		PACKET_SIZES[84] = 7;
		PACKET_SIZES[37] = 7;
		PACKET_SIZES[83] = 4;
		PACKET_SIZES[27] = 3;
		PACKET_SIZES[78] = -1;
		PACKET_SIZES[25] = 3;
		PACKET_SIZES[18] = 8;
		PACKET_SIZES[23] = 4;
		PACKET_SIZES[40] = -1;
		PACKET_SIZES[63] = -1;
		PACKET_SIZES[58] = 15;
		PACKET_SIZES[39] = 0;
		PACKET_SIZES[42] = 2;
		PACKET_SIZES[72] = 8;
		PACKET_SIZES[50] = 0;
		PACKET_SIZES[70] = 4;
		PACKET_SIZES[34] = 11;
		PACKET_SIZES[20] = -1;
		PACKET_SIZES[29] = 8;
		PACKET_SIZES[53] = 7;
		PACKET_SIZES[79] = -1;
		PACKET_SIZES[80] = 3;
		PACKET_SIZES[60] = 4;
		PACKET_SIZES[2] = 11;
		PACKET_SIZES[51] = 3;
		PACKET_SIZES[30] = 7;
		PACKET_SIZES[55] = 6;
		PACKET_SIZES[59] = 7;
		PACKET_SIZES[9] = 8;
		PACKET_SIZES[43] = 18;
		PACKET_SIZES[38] = 3;
		PACKET_SIZES[33] = 16;
		PACKET_SIZES[49] = -1;
		PACKET_SIZES[65] = -1;
		PACKET_SIZES[26] = 7;
		PACKET_SIZES[13] = 7;
		PACKET_SIZES[22] = 3;
		PACKET_SIZES[67] = 2;
		PACKET_SIZES[57] = 12;
		PACKET_SIZES[11] = 8;
		PACKET_SIZES[54] = -1;
		PACKET_SIZES[81] = 4;
		PACKET_SIZES[48] = 2;
		PACKET_SIZES[14] = 3;
		PACKET_SIZES[31] = 8;
		PACKET_SIZES[4] = 0;
		PACKET_SIZES[66] = 15;
		PACKET_SIZES[73] = -1;
		PACKET_SIZES[15] = 2;
		PACKET_SIZES[47] = 3;
		PACKET_SIZES[0] = 7;
		PACKET_SIZES[60] = 4;
	}
	public WorldPacketsDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected()
				&& !player.isFinished()) {
			int packetId = stream.readPacket(player);
			if (packetId >= PACKET_SIZES.length || packetId < 0) {
				if (GameConstants.DEBUG)
					System.out.println("PacketId " + packetId
							+ " has fake packet id.");
				break;
			}
			int finalLength;
			int length = PACKET_SIZES[packetId];
			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();
			else if (length == -3)
				length = stream.readInt();
			else if (length == -4) {
				length = stream.getRemaining();
				if (GameConstants.DEBUG)
					System.out.println("Invalid size for PacketId " + packetId
							+ ". Size guessed to be " + length);
			}
			finalLength = length;
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				if (GameConstants.DEBUG)
					System.out.println("PacketId " + packetId
							+ " has fake size. - expected size " + length);
			}
			int startOffset = stream.getOffset();
			
			IntStream.of(LOGICAL_PACKETS).filter(size -> size == packetId).forEach(packet -> player.addLogicPacketToQueue(new LogicPacket(packetId, finalLength, stream)));
			OutgoingPacketDispatcher.execute(player, stream, packetId);
			
			stream.setOffset(startOffset + length);
//			if (GameConstants.DEBUG && packetId != 39)
//				System.out.println( "Missing packet " + packetId
//						+ ", expected size: " + length + ", actual size: "
//						+ PACKET_SIZES[packetId]);
		}
	}
	
	private final int[] LOGICAL_PACKETS = {
			WALKING_PACKET,MINI_WALKING_PACKET,ITEM_TAKE_PACKET,PLAYER_OPTION_1_PACKET,PLAYER_OPTION_2_PACKET,PLAYER_OPTION_3_PACKET,PLAYER_OPTION_4_PACKET,PLAYER_OPTION_6_PACKET, PLAYER_OPTION_9_PACKET,
			ATTACK_NPC,INTERFACE_ON_PLAYER,INTERFACE_ON_NPC,NPC_CLICK1_PACKET,NPC_CLICK2_PACKET,NPC_CLICK3_PACKET,NPC_CLICK4_PACKET, 16, CAMERA_MOVEMENT_PACKET, 
			OBJECT_CLICK1_PACKET,OBJECT_CLICK2_PACKET,OBJECT_CLICK3_PACKET,OBJECT_CLICK4_PACKET,INTERFACE_ON_OBJECT,SWITCH_INTERFACE_COMPONENTS_PACKET,ITEM_SECOND_OPTION_PACKET
	};
}