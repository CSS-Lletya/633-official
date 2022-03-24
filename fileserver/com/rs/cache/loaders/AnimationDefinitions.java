package com.rs.cache.loaders;

import java.io.IOException;

import com.rs.cache.Cache;
import com.rs.io.InputStream;
import com.rs.utilities.Utility;

import io.vavr.control.Try;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public class AnimationDefinitions {

	public int anInt2136;
	public int anInt2137;
	public int[] anIntArray2139;
	public int anInt2140;
	public boolean aBoolean2141 = false;
	public int anInt2142;
	public int emoteItem;
	public int anInt2144 = -1;
	public int[][] handledSounds;
	public boolean[] aBooleanArray2149;
	public int[] anIntArray2151;
	public boolean aBoolean2152;
	public int[] anIntArray2153;
	public int anInt2155;
	public boolean aBoolean2158;
	public boolean aBoolean2159;
	public int anInt2162;
	public int anInt2163;

	// added
	public int[] soundMinDelay;
	public int[] soundMaxDelay;
	public int[] anIntArray1362;
	public boolean effect2Sound;

	static Object2ObjectArrayMap<Integer, AnimationDefinitions> animDefs = new Object2ObjectArrayMap<>();

	public static void main(String[] args) throws IOException {
		Cache.init();

		int[] buffer = new int[100000];
		int size = 0;

		for (int i = 0; i < Utility.getAnimationDefinitionsSize(); i++) {
			AnimationDefinitions defs = getAnimationDefinitions(i);
			if (defs == null || defs.emoteItem == -1)
				continue;

			ItemDefinitions item = ItemDefinitions
					.getItemDefinitions(defs.emoteItem);
			if (item == null)
				continue;
			System.out.println("[" + i + "]: item " + defs.emoteItem + " ["
					+ item.getName() + "]");
			buffer[size++] = i;
		}

		/*
		 * for (int i = 0; i < 50000; i++) { RenderAnimDefinitions defs =
		 * RenderAnimDefinitions.getRenderAnimDefinitions(i); if (defs == null)
		 * continue;
		 * 
		 * for (int x = 0; x < size; x++) { int anim = buffer[x]; if
		 * (defs.walkAnimation == anim || defs.runAnimation == anim ||
		 * defs.defaultStandAnimation == anim || defs.walkBackwardsAnimation ==
		 * anim || defs.walkLeftAnimation == anim || defs.walkRightAnimation ==
		 * anim || defs.walkUpwardsAnimation == anim) {
		 * System.out.println("Render:" + i + " uses anim " + anim); break; } }
		 * }
		 */

	}

	public static final AnimationDefinitions getAnimationDefinitions(int emoteId) {
		return (AnimationDefinitions) Try.run(() -> {
			AnimationDefinitions defs = animDefs.get(emoteId);
			if (defs != null)
				return;
			byte[] data = Cache.STORE.getIndexes()[20].getFile(emoteId >>> 7,
					emoteId & 0x7f);
			defs = new AnimationDefinitions();
			if (data != null)
				defs.readValueLoop(new InputStream(data));
			defs.method2394();
			animDefs.put(emoteId, defs);
		});
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	public int getEmoteTime() {
		if (anIntArray2153 == null)
			return 0;
		int ms = 0;
		for (int i : anIntArray2153)
			ms += i;
		return ms * 30;// * 30
	}

	public int getEmoteGameTickets() {
		return getEmoteTime() / 1000;
	}

	private void readValues(InputStream stream, int opcode) {
		if ((opcode ^ 0xffffffff) == -2) {
			int i = stream.readUnsignedShort();
			anIntArray2153 = new int[i];
			for (int i_16_ = 0; (i ^ 0xffffffff) < (i_16_ ^ 0xffffffff); i_16_++)
				anIntArray2153[i_16_] = stream.readUnsignedShort();
			anIntArray2139 = new int[i];
			for (int i_17_ = 0; (i_17_ ^ 0xffffffff) > (i ^ 0xffffffff); i_17_++)
				anIntArray2139[i_17_] = stream.readUnsignedShort();
			for (int i_18_ = 0; i_18_ < i; i_18_++)
				anIntArray2139[i_18_] = ((stream.readUnsignedShort() << 16) + anIntArray2139[i_18_]);
		} else if ((opcode ^ 0xffffffff) != -3) {
			if ((opcode ^ 0xffffffff) != -4) {
				if ((opcode ^ 0xffffffff) == -5)
					aBoolean2152 = true;
				else if (opcode == 5)
					anInt2142 = stream.readUnsignedByte();
				else if (opcode != 6) {
					if ((opcode ^ 0xffffffff) == -8)
						emoteItem = stream.readUnsignedShort();
					else if ((opcode ^ 0xffffffff) != -9) {
						if (opcode != 9) {
							if ((opcode ^ 0xffffffff) != -11) {
								if ((opcode ^ 0xffffffff) == -12)
									anInt2155 = stream.readUnsignedByte();
								else if (opcode == 12) {
									int i = stream.readUnsignedByte();
									anIntArray2151 = new int[i];
									for (int i_19_ = 0; ((i_19_ ^ 0xffffffff) > (i ^ 0xffffffff)); i_19_++)
										anIntArray2151[i_19_] = stream
												.readUnsignedShort();
									for (int i_20_ = 0; i > i_20_; i_20_++)
										anIntArray2151[i_20_] = ((stream
												.readUnsignedShort() << 16) + anIntArray2151[i_20_]);
								} else if ((opcode ^ 0xffffffff) != -14) {
									if (opcode != 14) {
										if (opcode != 15) {
											if (opcode == 16)
												aBoolean2158 = true;
											// added opcode
											else if (opcode == 17) {
												@SuppressWarnings("unused")
												int anInt2145 = stream
														.readUnsignedByte();
												// added opcode
											} else if (opcode == 18) {
												effect2Sound = true;
											} else if (opcode == 19) {
												if (anIntArray1362 == null) {
													anIntArray1362 = new int[handledSounds.length];
													for (int index = 0; index < handledSounds.length; index++)
														anIntArray1362[index] = 255;
												}
												anIntArray1362[stream
														.readUnsignedByte()] = stream
														.readUnsignedByte();
												// added opcode
											} else if (opcode == 20) {
												if ((soundMaxDelay == null)
														|| (soundMinDelay == null)) {
													soundMaxDelay = (new int[handledSounds.length]);
													soundMinDelay = (new int[handledSounds.length]);
													for (int i_34_ = 0; (i_34_ < handledSounds.length); i_34_++) {
														soundMaxDelay[i_34_] = 256;
														soundMinDelay[i_34_] = 256;
													}
												}
												int index = stream
														.readUnsignedByte();
												soundMaxDelay[index] = stream
														.readUnsignedShort();
												soundMinDelay[index] = stream
														.readUnsignedShort();
											}
										} else
											aBoolean2159 = true;
									} else
										aBoolean2141 = true;
								} else {
									// opcode 13
									int i = stream.readUnsignedShort();
									handledSounds = new int[i][];
									for (int i_21_ = 0; i_21_ < i; i_21_++) {
										int i_22_ = stream.readUnsignedByte();
										if ((i_22_ ^ 0xffffffff) < -1) {
											handledSounds[i_21_] = new int[i_22_];
											handledSounds[i_21_][0] = stream
													.read24BitInt();
											for (int i_23_ = 1; ((i_22_ ^ 0xffffffff) < (i_23_ ^ 0xffffffff)); i_23_++) {
												handledSounds[i_21_][i_23_] = stream
														.readUnsignedShort();
											}
										}
									}
								}
							} else
								anInt2162 = stream.readUnsignedByte();
						} else
							anInt2140 = stream.readUnsignedByte();
					} else
						anInt2136 = stream.readUnsignedByte();
				} else
					anInt2144 = stream.readUnsignedShort();
			} else {
				aBooleanArray2149 = new boolean[256];
				int i = stream.readUnsignedByte();
				for (int i_24_ = 0; (i ^ 0xffffffff) < (i_24_ ^ 0xffffffff); i_24_++)
					aBooleanArray2149[stream.readUnsignedByte()] = true;
			}
		} else
			anInt2163 = stream.readUnsignedShort();
	}

	public void method2394() {
		if (anInt2140 == -1) {
			if (aBooleanArray2149 == null)
				anInt2140 = 0;
			else
				anInt2140 = 2;
		}
		if (anInt2162 == -1) {
			if (aBooleanArray2149 == null)
				anInt2162 = 0;
			else
				anInt2162 = 2;
		}
	}

	public AnimationDefinitions() {
		anInt2136 = 99;
		emoteItem = -1;
		anInt2140 = -1;
		aBoolean2152 = false;
		anInt2142 = 5;
		aBoolean2159 = false;
		anInt2163 = -1;
		anInt2155 = 2;
		aBoolean2158 = false;
		anInt2162 = -1;
	}

}
