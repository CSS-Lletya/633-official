package com.rs.cache.loaders;

import com.rs.cache.Cache;
import com.rs.io.InputStream;
import com.rs.utilities.Utility;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public class GraphicDefinitions {

	public short[] aShortArray1435;
	public short[] aShortArray1438;
	public int anInt1440;
	public boolean aBoolean1442;
	public int defaultModel;
	public int anInt1446;
	public boolean aBoolean1448 = false;
	public int anInt1449;
	public int anInt1450;
	public int anInt1451;
	public int graphicsId;
	public int anInt1454;
	public short[] aShortArray1455;
	public short[] aShortArray1456;

	// added
	public byte byteValue;
	// added
	public int intValue;

	static Object2ObjectArrayMap<Integer, GraphicDefinitions> animDefs = new Object2ObjectArrayMap<>();

	public static final GraphicDefinitions getAnimationDefinitions(int emoteId) {
		GraphicDefinitions defs = animDefs.get(emoteId);
		if (defs != null)
			return defs;
		byte[] data = Cache.STORE.getIndexes()[21].getFile(emoteId >>> 735411752, emoteId & 0xff);
		defs = new GraphicDefinitions();
		defs.graphicsId = emoteId;
		if (data != null)
			defs.readValueLoop(new InputStream(data));
		animDefs.put(emoteId, defs);
		return defs;
	}

	public static final void main(String... s) {
		Try.run(() -> Cache.init());
		int model = NPCDefinitions.getNPCDefinitions(1).modelIds[0];
		System.out.println(model);
		int offset = 300;
		for (int i = 0; i < Utility.getGraphicDefinitionsSize(); i++) {
			GraphicDefinitions def = GraphicDefinitions.getAnimationDefinitions(i);
			if (def == null)
				continue;
			if (def.defaultModel >= model - offset && def.defaultModel <= model + offset)
				System.out.println("Possible match [id=" + i + ", model=" + def.defaultModel + "].");
		}
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public void readValues(InputStream stream, int opcode) {
		if (opcode != 1) {
			if ((opcode ^ 0xffffffff) == -3)
				anInt1450 = stream.readBigSmart();
			else if (opcode == 4)
				anInt1446 = stream.readUnsignedShort();
			else if (opcode != 5) {
				if ((opcode ^ 0xffffffff) != -7) {
					if (opcode == 7)
						anInt1440 = stream.readUnsignedByte();
					else if ((opcode ^ 0xffffffff) == -9)
						anInt1451 = stream.readUnsignedByte();
					else if (opcode != 9) {
						if (opcode != 10) {
							if (opcode == 11) { // added opcode
								// aBoolean1442 = true;
								byteValue = (byte) 1;
							} else if (opcode == 12) { // added opcode
								// aBoolean1442 = true;
								byteValue = (byte) 4;
							} else if (opcode == 13) { // added opcode
								// aBoolean1442 = true;
								byteValue = (byte) 5;
							} else if (opcode == 14) { // added opcode
								// aBoolean1442 = true;
								// aByte2856 = 2;
								byteValue = (byte) 2;
								intValue = stream.readUnsignedByte() * 256;
							} else if (opcode == 15) {
								// aByte2856 = 3;
								byteValue = (byte) 3;
								intValue = stream.readUnsignedShort();
							} else if (opcode == 16) {
								// aByte2856 = 3;
								byteValue = (byte) 3;
								intValue = stream.readInt();
							} else if (opcode != 40) {
								if ((opcode ^ 0xffffffff) == -42) {
									int i = stream.readUnsignedByte();
									aShortArray1455 = new short[i];
									aShortArray1435 = new short[i];
									for (int i_0_ = 0; i > i_0_; i_0_++) {
										aShortArray1455[i_0_] = (short) (stream.readUnsignedShort());
										aShortArray1435[i_0_] = (short) (stream.readUnsignedShort());
									}
								}
							} else {
								int i = stream.readUnsignedByte();
								aShortArray1438 = new short[i];
								aShortArray1456 = new short[i];
								for (int i_1_ = 0; ((i ^ 0xffffffff) < (i_1_ ^ 0xffffffff)); i_1_++) {
									aShortArray1438[i_1_] = (short) stream.readUnsignedShort();
									aShortArray1456[i_1_] = (short) stream.readUnsignedShort();
								}
							}
						} else
							aBoolean1448 = true;
					} else {
						// aBoolean1442 = true;
						byteValue = (byte) 3;
						intValue = 8224;
					}
				} else
					anInt1454 = stream.readUnsignedShort();
			} else
				anInt1449 = stream.readUnsignedShort();
		} else
			defaultModel = stream.readBigSmart();
	}

	public GraphicDefinitions() {
		byteValue = 0;
		intValue = -1;
		anInt1446 = 128;
		aBoolean1442 = false;
		anInt1449 = 128;
		anInt1451 = 0;
		anInt1450 = -1;
		anInt1454 = 0;
		anInt1440 = 0;
	}

}
