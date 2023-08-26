package com.rs.cache.loaders;

import java.lang.reflect.Field;
import java.util.Arrays;

import com.rs.cache.Cache;
import com.rs.io.InputStream;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import lombok.Data;

@Data
public class ObjectDefinitions {

	static Short2ObjectOpenHashMap<ObjectDefinitions> objectDefinitions = new Short2ObjectOpenHashMap<>();

	private short[] originalColors;
	int[] toObjectIds;
	static int anInt3832;
	int[] animations = null;
	private int anInt3834;
	int anInt3835;
	static int anInt3836;
	private byte aByte3837;
	int anInt3838 = -1;
	boolean aBoolean3839;
	private int anInt3840;
	private int anInt3841;
	static int anInt3842;
	static int anInt3843;
	int anInt3844;
	boolean aBoolean3845;
	static int anInt3846;
	private byte aByte3847;
	private byte aByte3849;
	int anInt3850;
	int anInt3851;
	public boolean secondBool;
	public boolean aBoolean3853;
	int anInt3855;
	public boolean ignoreClipOnAlternativeRoute;
	int anInt3857;
	private byte[] aByteArray3858;
	int[] anIntArray3859;
	int anInt3860;
	String[] options;
	public int configFileId;
	private short[] modifiedColors;
	int anInt3865;
	boolean aBoolean3866;
	boolean aBoolean3867;
	public boolean projectileCliped;
	private int[] anIntArray3869;
	boolean aBoolean3870;
	public int sizeY;
	boolean aBoolean3872;
	boolean aBoolean3873;
	public int thirdInt;
	private int anInt3875;
	public int objectAnimation;
	private int anInt3877;
	private int anInt3878;
	public int clipType;
	private int anInt3881;
	private int anInt3882;
	private int anInt3883;
	Object loader;
	private int anInt3889;
	public int sizeX;
	public boolean aBoolean3891;
	int anInt3892;
	public int secondInt;
	boolean aBoolean3894;
	boolean aBoolean3895;
	int anInt3896;
	int configId;
	private byte[] possibleTypes;
	int anInt3900;
	public String name;
	private int anInt3902;
	int anInt3904;
	int anInt3905;
	boolean aBoolean3906;
	int[] anIntArray3908;
	private byte aByte3912;
	int anInt3913;
	private byte aByte3914;
	private int anInt3915;
	public int[][] modelIds;
	private int anInt3917;
	/**
	 * Object anim shit 1
	 */
	private short[] aShortArray3919;
	/**
	 * Object anim shit 2
	 */
	private short[] aShortArray3920;
	int anInt3921;
	private Object2ObjectOpenHashMap<Integer, Object> parameters;
	boolean aBoolean3923;
	boolean aBoolean3924;
	int anInt3925;
	public int id;

	private int[] anIntArray4534;

	private byte[] unknownArray4;

	private byte[] unknownArray3;

	private int cflag;

	public String getFirstOption() {
		if (options == null || options.length < 1)
			return "";
		return options[0];
	}

	public String getSecondOption() {
		if (options == null || options.length < 2)
			return "";
		return options[1];
	}

	public String getOption(int option) {
		if (options == null || options.length < option || option == 0)
			return "";
		return options[option - 1];
	}

	public String getThirdOption() {
		if (options == null || options.length < 3)
			return "";
		return options[2];
	}

	public boolean containsOption(int i, String option) {
		if (options == null || options[i] == null || options.length <= i)
			return false;
		return options[i].equalsIgnoreCase(option);
	}

	public boolean containsOption(String o) {
		if (options == null)
			return false;
		for (String option : options) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase(o))
				return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private void readValues(InputStream stream, int opcode) {
		// System.out.println(opcode);
		if (opcode != 1 && opcode != 5) {
			if (opcode != 2) {
				if (opcode != 14) {
					if (opcode != 15) {
						if (opcode == 17) { // nocliped
							projectileCliped = false;
							clipType = 0;
						} else if (opcode != 18) {
							if (opcode == 19)
								secondInt = stream.readUnsignedByte();
							else if (opcode == 21)
								aByte3912 = (byte) 1;
							else if (opcode != 22) {
								if (opcode != 23) {
									if (opcode != 24) {
										if (opcode == 27) // cliped, no idea
											// diff between 2
											// and 1
											clipType = 1;
										else if (opcode == 28)
											anInt3892 = (stream.readUnsignedByte() << 2);
										else if (opcode != 29) {
											if (opcode != 39) {
												if (opcode < 30 || opcode >= 35) {
													if (opcode == 40) {
														int i_53_ = (stream.readUnsignedByte());
														originalColors = new short[i_53_];
														modifiedColors = new short[i_53_];
														for (int i_54_ = 0; i_53_ > i_54_; i_54_++) {
															originalColors[i_54_] = (short) (stream
																	.readUnsignedShort());
															modifiedColors[i_54_] = (short) (stream
																	.readUnsignedShort());
														}
													} else if (44 == opcode) {
														int i_86_ = (short) stream.readUnsignedShort();
														int i_87_ = 0;
														for (int i_88_ = i_86_; i_88_ > 0; i_88_ >>= 1)
															i_87_++;
														unknownArray3 = new byte[i_87_];
														byte i_89_ = 0;
														for (int i_90_ = 0; i_90_ < i_87_; i_90_++) {
															if ((i_86_ & 1 << i_90_) > 0) {
																unknownArray3[i_90_] = i_89_;
																i_89_++;
															} else
																unknownArray3[i_90_] = (byte) -1;
														}
													} else if (opcode == 45) {
														int i_91_ = (short) stream.readUnsignedShort();
														int i_92_ = 0;
														for (int i_93_ = i_91_; i_93_ > 0; i_93_ >>= 1)
															i_92_++;
														unknownArray4 = new byte[i_92_];
														byte i_94_ = 0;
														for (int i_95_ = 0; i_95_ < i_92_; i_95_++) {
															if ((i_91_ & 1 << i_95_) > 0) {
																unknownArray4[i_95_] = i_94_;
																i_94_++;
															} else
																unknownArray4[i_95_] = (byte) -1;
														}
													} else if (opcode != 41) { // object
														// anim
														if (opcode != 42) {
															if (opcode != 62) {
																if (opcode != 64) {
																	if (opcode == 65)
																		anInt3902 = stream.readUnsignedShort();
																	else if (opcode != 66) {
																		if (opcode != 67) {
																			if (opcode == 69)
																				cflag = stream.readUnsignedByte();
																			else if (opcode != 70) {
																				if (opcode == 71)
																					anInt3889 = stream.readShort() << 2;
																				else if (opcode != 72) {
																					if (opcode == 73)
																						secondBool = true;
																					else if (opcode == 74)
																						ignoreClipOnAlternativeRoute = true;
																					else if (opcode != 75) {
																						if (opcode != 77
																								&& opcode != 92) {
																							if (opcode == 78) {
																								anInt3860 = stream
																										.readUnsignedShort();
																								anInt3904 = stream
																										.readUnsignedByte();
																							} else if (opcode != 79) {
																								if (opcode == 81) {
																									aByte3912 = (byte) 2;
																									anInt3882 = 256
																											* stream.readUnsignedByte();
																								} else if (opcode != 82) {
																									if (opcode == 88)
																										aBoolean3853 = false;
																									else if (opcode != 89) {
																										if (opcode == 90)
																											aBoolean3870 = true;
																										else if (opcode != 91) {
																											if (opcode != 93) {
																												if (opcode == 94)
																													aByte3912 = (byte) 4;
																												else if (opcode != 95) {
																													if (opcode != 96) {
																														if (opcode == 97)
																															aBoolean3866 = true;
																														else if (opcode == 98)
																															aBoolean3923 = true;
																														else if (opcode == 99) {
																															anInt3857 = stream
																																	.readUnsignedByte();
																															anInt3835 = stream
																																	.readUnsignedShort();
																														} else if (opcode == 100) {
																															anInt3844 = stream
																																	.readUnsignedByte();
																															anInt3913 = stream
																																	.readUnsignedShort();
																														} else if (opcode != 101) {
																															if (opcode == 102)
																																anInt3838 = stream
																																		.readUnsignedShort();
																															else if (opcode == 103)
																																thirdInt = 0;
																															else if (opcode != 104) {
																																if (opcode == 105)
																																	aBoolean3906 = true;
																																else if (opcode == 106) {
																																	int i_55_ = stream
																																			.readUnsignedByte();
																																	anIntArray3869 = new int[i_55_];
																																	animations = new int[i_55_];
																																	for (int i_56_ = 0; i_56_ < i_55_; i_56_++) {
																																		animations[i_56_] = stream
																																				.readBigSmart();
																																		int i_57_ = stream
																																				.readUnsignedByte();
																																		anIntArray3869[i_56_] = i_57_;
																																		anInt3881 += i_57_;
																																	}
																																} else if (opcode == 107)
																																	anInt3851 = stream
																																			.readUnsignedShort();
																																else if (opcode >= 150
																																		&& opcode < 155) {
																																	options[opcode
																																			+ -150] = stream
																																					.readString();
																																} else if (opcode != 160) {
																																	if (opcode == 162) {
																																		aByte3912 = (byte) 3;
																																		anInt3882 = stream
																																				.readInt();
																																	} else if (opcode == 163) {
																																		aByte3847 = (byte) stream
																																				.readByte();
																																		aByte3849 = (byte) stream
																																				.readByte();
																																		aByte3837 = (byte) stream
																																				.readByte();
																																		aByte3914 = (byte) stream
																																				.readByte();
																																	} else if (opcode != 164) {
																																		if (opcode != 165) {
																																			if (opcode != 166) {
																																				if (opcode == 167)
																																					anInt3921 = stream
																																							.readUnsignedShort();
																																				else if (opcode != 168) {
																																					if (opcode == 169) {
																																						aBoolean3845 = true;
																																						// added
																																						// opcode
																																					} else if (opcode == 170) {
																																						int anInt3383 = stream
																																								.readUnsignedSmart();
																																						// added
																																						// opcode
																																					} else if (opcode == 171) {
																																						int anInt3362 = stream
																																								.readUnsignedSmart();
																																						// added
																																						// opcode
																																					} else if (opcode == 173) {
																																						int anInt3302 = stream
																																								.readUnsignedShort();
																																						int anInt3336 = stream
																																								.readUnsignedShort();
																																						// added
																																						// opcode
																																					} else if (opcode == 177) {
																																						boolean ub = true;
																																						// added
																																						// opcode
																																					} else if (opcode == 178) {
																																						int db = stream
																																								.readUnsignedByte();
																																					} else if (opcode == 189) {
																																						boolean bloom = true;
																																					} else if (opcode >= 190
																																							&& opcode < 196) {
																																						if (anIntArray4534 == null) {
																																							anIntArray4534 = new int[6];
																																							Arrays.fill(
																																									anIntArray4534,
																																									-1);
																																						}
																																						anIntArray4534[opcode
																																								- 190] = stream
																																										.readUnsignedShort();
																																					} else if (opcode == 249) {
																																						int length = stream
																																								.readUnsignedByte();
																																						if (parameters == null)
																																							parameters = new Object2ObjectOpenHashMap<Integer, Object>(
																																									length);
																																						for (int i_60_ = 0; i_60_ < length; i_60_++) {
																																							boolean bool = stream
																																									.readUnsignedByte() == 1;
																																							int i_61_ = stream
																																									.read24BitInt();
																																							if (!bool)
																																								parameters
																																										.put(i_61_,
																																												stream.readInt());
																																							else
																																								parameters
																																										.put(i_61_,
																																												stream.readString());

																																						}
																																					}
																																				} else
																																					aBoolean3894 = true;
																																			} else
																																				anInt3877 = stream
																																						.readShort();
																																		} else
																																			anInt3875 = stream
																																					.readShort();
																																	} else
																																		anInt3834 = stream
																																				.readShort();
																																} else {
																																	int i_62_ = stream
																																			.readUnsignedByte();
																																	anIntArray3908 = new int[i_62_];
																																	for (int i_63_ = 0; i_62_ > i_63_; i_63_++)
																																		anIntArray3908[i_63_] = stream
																																				.readUnsignedShort();
																																}
																															} else
																																anInt3865 = stream
																																		.readUnsignedByte();
																														} else
																															anInt3850 = stream
																																	.readUnsignedByte();
																													} else
																														aBoolean3924 = true;
																												} else {
																													aByte3912 = (byte) 5;
																													anInt3882 = stream
																															.readShort();
																												}
																											} else {
																												aByte3912 = (byte) 3;
																												anInt3882 = stream
																														.readUnsignedShort();
																											}
																										} else
																											aBoolean3873 = true;
																									} else
																										aBoolean3895 = false;
																								} else
																									aBoolean3891 = true;
																							} else {
																								anInt3900 = stream
																										.readUnsignedShort();
																								anInt3905 = stream
																										.readUnsignedShort();
																								anInt3904 = stream
																										.readUnsignedByte();
																								int i_64_ = stream
																										.readUnsignedByte();
																								anIntArray3859 = new int[i_64_];
																								for (int i_65_ = 0; i_65_ < i_64_; i_65_++)
																									anIntArray3859[i_65_] = stream
																											.readUnsignedShort();
																							}
																						} else {
																							configFileId = stream
																									.readUnsignedShort();
																							if (configFileId == 65535)
																								configFileId = -1;
																							configId = stream
																									.readUnsignedShort();
																							if (configId == 65535)
																								configId = -1;
																							int i_66_ = -1;
																							if (opcode == 92) {
																								i_66_ = stream
																										.readBigSmart();
																							}
																							int i_67_ = stream
																									.readUnsignedByte();
																							toObjectIds = new int[i_67_
																									- -2];
																							for (int i_68_ = 0; i_67_ >= i_68_; i_68_++) {
																								toObjectIds[i_68_] = stream
																										.readBigSmart();
																							}
																							toObjectIds[i_67_
																									+ 1] = i_66_;
																						}
																					} else
																						anInt3855 = stream
																								.readUnsignedByte();
																				} else
																					anInt3915 = stream.readShort() << 2;
																			} else
																				anInt3883 = stream.readShort() << 2;
																		} else
																			anInt3917 = stream.readUnsignedShort();
																	} else
																		anInt3841 = stream.readUnsignedShort();
																} else
																	// 64
																	aBoolean3872 = false;
															} else
																aBoolean3839 = true;
														} else {
															int i_69_ = (stream.readUnsignedByte());
															aByteArray3858 = (new byte[i_69_]);
															for (int i_70_ = 0; i_70_ < i_69_; i_70_++)
																aByteArray3858[i_70_] = (byte) (stream.readByte());
														}
													} else { // object anim?
														int i_71_ = (stream.readUnsignedByte());
														aShortArray3920 = new short[i_71_];
														aShortArray3919 = new short[i_71_];
														for (int i_72_ = 0; i_71_ > i_72_; i_72_++) {
															aShortArray3920[i_72_] = (short) (stream
																	.readUnsignedShort());
															aShortArray3919[i_72_] = (short) (stream
																	.readUnsignedShort());
														}
													}
												} else {
													options[-30 + opcode] = (stream.readString());
												}
											} else
												// 39
												anInt3840 = (stream.readByte() * 5);
										} else {// 29
											anInt3878 = stream.readByte();
										}
									} else {
										objectAnimation = stream.readBigSmart();
									}
								} else
									thirdInt = 1;
							} else
								aBoolean3867 = true;
						} else
							projectileCliped = false;
					} else
						// 15
						sizeY = stream.readUnsignedByte();
				} else
					// 14
					sizeX = stream.readUnsignedByte();
			} else {
				name = stream.readString();
			}
		} else {
			boolean aBoolean1162 = false;
			if (opcode == 5 && aBoolean1162)
				skipReadModelIds(stream);
			int i_73_ = stream.readUnsignedByte();
			modelIds = new int[i_73_][];
			possibleTypes = new byte[i_73_];
			for (int i_74_ = 0; i_74_ < i_73_; i_74_++) {
				possibleTypes[i_74_] = (byte) stream.readByte();
				int i_75_ = stream.readUnsignedByte();
				modelIds[i_74_] = new int[i_75_];
				for (int i_76_ = 0; i_75_ > i_76_; i_76_++)
					modelIds[i_74_][i_76_] = stream.readBigSmart();
			}
			if (opcode == 5 && !aBoolean1162)
				skipReadModelIds(stream);
		}
	}

	private void skipReadModelIds(InputStream stream) {
		int length = stream.readUnsignedByte();
		for (int index = 0; index < length; index++) {
			stream.skip(1);
			int length2 = stream.readUnsignedByte();
			for (int i = 0; i < length2; i++)
				stream.readBigSmart();
		}
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				// System.out.println("Remaining: "+stream.getRemaining());
				break;
			}
			readValues(stream, opcode);
		}
	}

	private ObjectDefinitions() {
		anInt3835 = -1;
		anInt3860 = -1;
		configFileId = -1;
		aBoolean3866 = false;
		anInt3851 = -1;
		anInt3865 = 255;
		aBoolean3845 = false;
		aBoolean3867 = false;
		anInt3850 = 0;
		anInt3844 = -1;
		anInt3881 = 0;
		anInt3857 = -1;
		aBoolean3872 = true;
		anInt3882 = -1;
		anInt3834 = 0;
		options = new String[5];
		anInt3875 = 0;
		aBoolean3839 = false;
		anIntArray3869 = null;
		sizeY = 1;
		thirdInt = -1;
		anInt3883 = 0;
		aBoolean3895 = true;
		anInt3840 = 0;
		aBoolean3870 = false;
		anInt3889 = 0;
		aBoolean3853 = true;
		secondBool = false;
		clipType = 2;
		projectileCliped = true;
		ignoreClipOnAlternativeRoute = false;
		anInt3855 = -1;
		anInt3878 = 0;
		anInt3904 = 0;
		sizeX = 1;
		objectAnimation = -1;
		aBoolean3891 = false;
		anInt3905 = 0;
		name = "null";
		anInt3913 = -1;
		aBoolean3906 = false;
		aBoolean3873 = false;
		aByte3914 = (byte) 0;
		anInt3915 = 0;
		anInt3900 = 0;
		secondInt = -1;
		aBoolean3894 = false;
		aByte3912 = (byte) 0;
		anInt3921 = 0;
		anInt3902 = 128;
		configId = -1;
		anInt3877 = 0;
		anInt3925 = 0;
		anInt3892 = 64;
		aBoolean3923 = false;
		aBoolean3924 = false;
		anInt3841 = 128;
		anInt3917 = 128;
	}

	final void method3287() {
		if (secondInt == -1) {
			secondInt = 0;
			if (possibleTypes != null && possibleTypes.length == 1 && possibleTypes[0] == 10)
				secondInt = 1;
			for (int i_13_ = 0; i_13_ < 5; i_13_++) {
				if (options[i_13_] != null) {
					secondInt = 1;
					break;
				}
			}
		}
		if (anInt3855 == -1)
			anInt3855 = clipType != 0 ? 1 : 0;
	}

	private static int getArchiveId(int i_0_) {
		return i_0_ >>> -1135990488;
	}

	public static ObjectDefinitions getObjectDefinitions(int id) {
		ObjectDefinitions def = objectDefinitions.get((short) id);
		if (def == null) {
			def = new ObjectDefinitions();
			def.id = id;
			byte[] data = Cache.STORE.getIndexes()[16].getFile(getArchiveId(id), id & 0xff);
			if (data == null) {
				// System.out.println("Failed loading Object " + id + ".");
			} else
				def.readValueLoop(new InputStream(data));
			def.method3287();
			/*
			 * if(def.name.equalsIgnoreCase("bank booth") ||
			 * def.name.equalsIgnoreCase("counter")) { def.notCliped = false;
			 * def.projectileCliped = true; if (def.clipType == 0) def.clipType = 1; } else
			 * if (DungeonUtils.isDoor(id) || DungeonUtils.isBossDoor(id)) { def.notCliped =
			 * false; def.projectileCliped = true; if (def.clipType == 0) def.clipType = 1;
			 * } if (def.notCliped) { def.projectileCliped = false; def.clipType = 0; }
			 */

			objectDefinitions.put((short) id, def);
		}
		return def;
	}

	public static void clearObjectDefinitions() {
		objectDefinitions.clear();
	}

	@SuppressWarnings("unused")
	private Object getValue(Field field) throws Throwable {
		field.setAccessible(true);
		Class<?> type = field.getType();
		if (type == int[][].class) {
			return Arrays.toString((int[][]) field.get(this));
		} else if (type == int[].class) {
			return Arrays.toString((int[]) field.get(this));
		} else if (type == byte[].class) {
			return Arrays.toString((byte[]) field.get(this));
		} else if (type == short[].class) {
			return Arrays.toString((short[]) field.get(this));
		} else if (type == double[].class) {
			return Arrays.toString((double[]) field.get(this));
		} else if (type == float[].class) {
			return Arrays.toString((float[]) field.get(this));
		} else if (type == Object[].class) {
			return Arrays.toString((Object[]) field.get(this));
		}
		return field.get(this);
	}

	public boolean getNameContaining(String value) {
		return getName().equalsIgnoreCase(value);
	}
}