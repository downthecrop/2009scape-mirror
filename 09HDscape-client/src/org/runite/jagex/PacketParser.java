package org.runite.jagex;
import java.io.IOException;

final class PacketParser {

	static int anInt79;
	static int anInt80 = 0;
	static byte[][][] aByteArrayArrayArray81;
	static Class61 aClass61_82 = new Class61();
	static short aShort83 = 32767;
	static RenderAnimationDefinition aClass16_84 = new RenderAnimationDefinition();
	static RSString aClass94_85 = RSString.createRSString("overlay");
	static int anInt86 = 0;
	static int anInt87 = 0;
	static RSInterface aClass11_88 = null;


	static final int method823(int var0, int var1, int var2, int var3) {
		try {
			if(var2 >= -76) {
				aShort83 = -91;
			}

			return (8 & Class9.aByteArrayArrayArray113[var3][var1][var0]) == 0?(~var3 < -1 && -1 != ~(Class9.aByteArrayArrayArray113[1][var1][var0] & 2)?-1 + var3:var3):0;
		} catch (RuntimeException var5) {
			throw Class44.method1067(var5, "ac.G(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
		}
	}

	static final void method824(long[] var0, Object[] var1, int var2) {
		try {
			Class134.method1809(var0.length - 1, var0, 122, 0, var1);
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "ac.E(" + (var0 != null?"{...}":"null") + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ')');
		}
	}

	static final void method825(byte var0, int var1) {
		try {
			Class3_Sub28_Sub6 var3 = Class3_Sub24_Sub3.method466(4, 1, var1);
			var3.a(true);
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "ac.D(" + var0 + ',' + var1 + ')');
		}
	}

	static final int method826(RSString var0, int var1) {
		try {
			if(var1 != -1) {
				method826((RSString)null, 65);
			}

			if(var0 != null) {
				for(int var2 = 0; Class8.anInt104 > var2; ++var2) {
					if(var0.equals(var1 ^ 107, Class70.aClass94Array1046[var2])) {
						return var2;
					}
				}

				return -1;
			} else {
				return -1;
			}
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "ac.B(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
		}
	}

	static final boolean parseIncomingPackets(byte var0) throws IOException {
		try {
			if(Class3_Sub15.aClass89_2429 == null) {
				return false;
			} else {
				int var1 = Class3_Sub15.aClass89_2429.availableBytes(-18358);
				if(0 == var1) {
					return false;
				} else {
					if(~RSString.incomingOpcode == 0) {
						--var1;
						Class3_Sub15.aClass89_2429.readBytes(0, 1, var0 ^ 18500, GraphicDefinition.incomingBuffer.buffer);
						GraphicDefinition.incomingBuffer.index = 0;
						RSString.incomingOpcode = GraphicDefinition.incomingBuffer.getOpcode(0);
						Class130.incomingPacketLength = Class75_Sub4.anIntArray2668[RSString.incomingOpcode];
					}

					if(Class130.incomingPacketLength == -1) {
						if(0 >= var1) {
							return false;
						}

						Class3_Sub15.aClass89_2429.readBytes(0, 1, -18455, GraphicDefinition.incomingBuffer.buffer);
						--var1;
						Class130.incomingPacketLength = GraphicDefinition.incomingBuffer.buffer[0] & 255;
					}

					if(-2 == Class130.incomingPacketLength) {
						if(~var1 >= -2) {
							return false;
						}

						var1 -= 2;
						Class3_Sub15.aClass89_2429.readBytes(0, 2, -18455, GraphicDefinition.incomingBuffer.buffer);
						GraphicDefinition.incomingBuffer.index = 0;
						Class130.incomingPacketLength = GraphicDefinition.incomingBuffer.getShort(1);
					}

					if(~Class130.incomingPacketLength < ~var1) {
						return false;
					} else {
						GraphicDefinition.incomingBuffer.index = 0;
						Class3_Sub15.aClass89_2429.readBytes(0, Class130.incomingPacketLength, -18455, GraphicDefinition.incomingBuffer.buffer);
						Class24.anInt469 = Class7.anInt2166;
						Class7.anInt2166 = Class3_Sub29.anInt2582;
						Class3_Sub29.anInt2582 = RSString.incomingOpcode;
						Class3_Sub28_Sub16.anInt3699 = 0;
						int nodeModelId;
						if(60 == RSString.incomingOpcode) {
							nodeModelId = GraphicDefinition.incomingBuffer.getShortA(var0 + 188);
							byte var69 = GraphicDefinition.incomingBuffer.method763((byte)100);
							Class3_Sub13_Sub23.method281((byte)99, var69, nodeModelId);
							RSString.incomingOpcode = -1;
							return true;
						} else {
							int counter;
							RSString var24;
							if(RSString.incomingOpcode == 115) {
								nodeModelId = GraphicDefinition.incomingBuffer.getShort(1);
								var24 = GraphicDefinition.incomingBuffer.getString();
								Object[] var71 = new Object[var24.length(-92) - -1];
								for(counter = var24.length(var0 ^ 79) + -1; -1 >= ~counter; --counter) {
									if (115 == var24.charAt(counter, (byte)-45)) {
										var71[1 + counter] = GraphicDefinition.incomingBuffer.getString();
									} else {
										var71[1 + counter] = new Integer(GraphicDefinition.incomingBuffer.getInt());
									}
								}

								var71[0] = new Integer(GraphicDefinition.incomingBuffer.getInt());
								if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
									CS2Script var66 = new CS2Script();
									var66.arguments = var71;
									Class43.method1065(1073376993, var66);
								}

								RSString.incomingOpcode = -1;
								return true;
							} else {
								long var4;
								boolean var31;
								int var30;
								RSString var41;
								if(-71 == ~RSString.incomingOpcode) {
									RSString var70 = GraphicDefinition.incomingBuffer.getString();
									if(!var70.method1550((byte)-60, Class117.aClass94_1614)) {
										if(!var70.method1550((byte)-47, Class30.aClass94_567)) {
											if(var70.method1550((byte)-98, Class3_Sub13_Sub26.aClass94_3330)) {
												var31 = false;
												var24 = var70.method1557(var70.method1551(Class155.aClass94_1970, 96), 0, 0);
												var4 = var24.toLong(-109);

												for(var30 = 0; ~Class3_Sub28_Sub5.anInt3591 < ~var30; ++var30) {
													if(var4 == Class114.aLongArray1574[var30]) {
														var31 = true;
														break;
													}
												}

												if(!var31 && ~WorldListEntry.anInt2622 == -1) {
													Class3_Sub30_Sub1.sendMessage(var24, 10, Class3_Sub28_Sub14.aClass94_3672, -1);
												}
											} else if(var70.method1550((byte)-128, Class3_Sub20.aClass94_2482)) {
												var24 = var70.method1557(var70.method1551(Class3_Sub20.aClass94_2482, var0 ^ -50), 0, 0);
												Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 11, var24, -1);
											} else if(!var70.method1550((byte)-29, NPC.aClass94_3998)) {
												if(var70.method1550((byte)-80, Class143.aClass94_1877)) {
													var24 = var70.method1557(var70.method1551(Class143.aClass94_1877, 121), 0, 0);
													if(WorldListEntry.anInt2622 == 0) {
														Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 13, var24, -1);
													}
												} else if(!var70.method1550((byte)-42, Class27.aClass94_514)) {
													if(!var70.method1550((byte)-41, ISAACCipher.aClass94_965)) {
														if(var70.method1550((byte)-110, Class3_Sub13_Sub30.aClass94_3368)) {
															var24 = var70.method1557(var70.method1551(Class155.aClass94_1970, var0 + 138), 0, 0);
															var4 = var24.toLong(var0 + -23);
															var31 = false;

															for(var30 = 0; ~Class3_Sub28_Sub5.anInt3591 < ~var30; ++var30) {
																if(~var4 == ~Class114.aLongArray1574[var30]) {
																	var31 = true;
																	break;
																}
															}

															if(!var31 && WorldListEntry.anInt2622 == 0) {
																Class3_Sub30_Sub1.sendMessage(var24, 16, Class3_Sub28_Sub14.aClass94_3672, -1);
															}
														} else if(var70.method1550((byte)-41, RSString.aClass94_2155)) {
															var24 = var70.method1557(var70.method1551(Class155.aClass94_1970, var0 + 189), var0 + 83, 0);
															var31 = false;
															var4 = var24.toLong(-122);

															for(var30 = 0; ~Class3_Sub28_Sub5.anInt3591 < ~var30; ++var30) {
																if(~Class114.aLongArray1574[var30] == ~var4) {
																	var31 = true;
																	break;
																}
															}

															if(!var31 && WorldListEntry.anInt2622 == 0) {
																var41 = var70.method1557(var70.length(-32) - 9, var0 ^ -83, 1 + var70.method1551(Class155.aClass94_1970, 92));
																Class3_Sub30_Sub1.sendMessage(var24, 21, var41, -1);
															}
														} else {
															Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 0, var70, var0 + 82);
														}
													} else {
														var24 = var70.method1557(var70.method1551(Class155.aClass94_1970, 118), 0, 0);
														var31 = false;
														var4 = var24.toLong(-120);

														for(var30 = 0; ~Class3_Sub28_Sub5.anInt3591 < ~var30; ++var30) {
															if(~Class114.aLongArray1574[var30] == ~var4) {
																var31 = true;
																break;
															}
														}

														if(!var31 && 0 == WorldListEntry.anInt2622) {
															Class3_Sub30_Sub1.sendMessage(var24, 15, Class3_Sub28_Sub14.aClass94_3672, -1);
														}
													}
												} else {
													var31 = false;
													var24 = var70.method1557(var70.method1551(Class155.aClass94_1970, 115), 0, 0);
													var4 = var24.toLong(-118);

													for(var30 = 0; Class3_Sub28_Sub5.anInt3591 > var30; ++var30) {
														if(var4 == Class114.aLongArray1574[var30]) {
															var31 = true;
															break;
														}
													}

													if(!var31 && -1 == ~WorldListEntry.anInt2622) {
														Class3_Sub30_Sub1.sendMessage(var24, 14, Class3_Sub28_Sub14.aClass94_3672, -1);
													}
												}
											} else {
												var24 = var70.method1557(var70.method1551(NPC.aClass94_3998, 102), 0, 0);
												if(0 == WorldListEntry.anInt2622) {
													Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 12, var24, -1);
												}
											}
										} else {
											var24 = var70.method1557(var70.method1551(Class155.aClass94_1970, 75), 0, 0);
											var4 = var24.toLong(-110);
											var31 = false;

											for(var30 = 0; ~Class3_Sub28_Sub5.anInt3591 < ~var30; ++var30) {
												if(Class114.aLongArray1574[var30] == var4) {
													var31 = true;
													break;
												}
											}

											if(!var31 && WorldListEntry.anInt2622 == 0) {
												var41 = var70.method1557(var70.length(var0 + -16) + -9, var0 ^ -83, 1 + var70.method1551(Class155.aClass94_1970, 101));
												Class3_Sub30_Sub1.sendMessage(var24, 8, var41, var0 ^ 82);
											}
										}
									} else {
										var24 = var70.method1557(var70.method1551(Class155.aClass94_1970, 65), 0, 0);
										var4 = var24.toLong(-128);
										var31 = false;

										for(var30 = 0; ~var30 > ~Class3_Sub28_Sub5.anInt3591; ++var30) {
											if(~Class114.aLongArray1574[var30] == ~var4) {
												var31 = true;
												break;
											}
										}

										if(!var31 && ~WorldListEntry.anInt2622 == -1) {
											Class3_Sub30_Sub1.sendMessage(var24, 4, Class3_Sub6.aClass94_2285, var0 + 82);
										}
									}

									RSString.incomingOpcode = -1;
									return true;
								} else {
									int var19;
									RSString var58;
									if(~RSString.incomingOpcode == -124) {
										nodeModelId = GraphicDefinition.incomingBuffer.getLEShort(-69);
										var19 = GraphicDefinition.incomingBuffer.getShortA(-126);
										var58 = GraphicDefinition.incomingBuffer.getString();
										if(Class146.updateInterfacePacketCounter(var19, (byte)-25)) {
											Class3_Sub13_Sub27.method295(var58, (byte)40, nodeModelId);
										}

										RSString.incomingOpcode = -1;
										return true;
									} else if(RSString.incomingOpcode != 230) {
										if(153 == RSString.incomingOpcode) {
											RSString.incomingOpcode = -1;
											Class65.anInt987 = 0;
											return true;
										} else {
											int modelId;
											if(-221 == ~RSString.incomingOpcode) {
												nodeModelId = GraphicDefinition.incomingBuffer.getIntB((byte)-59);
												var19 = GraphicDefinition.incomingBuffer.getLEShort(-75);
												modelId = GraphicDefinition.incomingBuffer.getShort(var0 + 84);
												if(Class146.updateInterfacePacketCounter(modelId, (byte)-25)) {
													Class3_Sub13_Sub33.method327(var19, nodeModelId, (byte)68);
												}

												RSString.incomingOpcode = -1;
												return true;
											} else {
												long var2;
												int clanChatIcon;
												int var11;
												long var29;
												long var36;
												if(81 == RSString.incomingOpcode) {
													var2 = GraphicDefinition.incomingBuffer.getLong(-120);
													GraphicDefinition.incomingBuffer.getByte();
													var4 = GraphicDefinition.incomingBuffer.getLong(var0 ^ 54);
													var29 = (long)GraphicDefinition.incomingBuffer.getShort(1);
													var36 = (long)GraphicDefinition.incomingBuffer.getTriByte((byte)104);
													clanChatIcon = GraphicDefinition.incomingBuffer.getByte((byte)-80);
													boolean var63 = false;
													var11 = GraphicDefinition.incomingBuffer.getShort(var0 ^ -84);
													long var55 = (var29 << 32) + var36;
													int var54 = 0;

													label1521:
														while(true) {
															if(100 > var54) {
																if(~var55 != ~Class163_Sub2_Sub1.aLongArray4017[var54]) {
																	++var54;
																	continue;
																}

																var63 = true;
																break;
															}

															if(1 >= clanChatIcon) {
																for(var54 = 0; ~Class3_Sub28_Sub5.anInt3591 < ~var54; ++var54) {
																	if(~Class114.aLongArray1574[var54] == ~var2) {
																		var63 = true;
																		break label1521;
																	}
																}
															}
															break;
														}

													if(!var63 && 0 == WorldListEntry.anInt2622) {
														Class163_Sub2_Sub1.aLongArray4017[Class149.anInt1921] = var55;
														Class149.anInt1921 = (1 + Class149.anInt1921) % 100;
														RSString var61 = Class3_Sub29.method733(12345678, var11).method555(28021, GraphicDefinition.incomingBuffer);
														if(-3 != ~clanChatIcon && 3 != clanChatIcon) {
															if(~clanChatIcon != -2) {
																Class3_Sub28_Sub12.sendGameMessage(var11, 20, var61, Class41.method1052(var0 + -29581, var4).method1545((byte)-50), (byte)50, Class41.method1052(-29664, var2).method1545((byte)-50));
															} else {
																Class3_Sub28_Sub12.sendGameMessage(var11, 20, var61, Class41.method1052(-29664, var4).method1545((byte)-50), (byte)50, RenderAnimationDefinition.method903(new RSString[]{Class32.aClass94_592, Class41.method1052(var0 + -29581, var2).method1545((byte)-50)}, (byte)-109));
															}
														} else {
															Class3_Sub28_Sub12.sendGameMessage(var11, 20, var61, Class41.method1052(var0 + -29581, var4).method1545((byte)-50), (byte)50, RenderAnimationDefinition.method903(new RSString[]{Class21.aClass94_444, Class41.method1052(-29664, var2).method1545((byte)-50)}, (byte)-124));
														}
													}

													RSString.incomingOpcode = -1;
													return true;
												} else {
													int var6;
													int chatIcon;
													boolean var32;
													if(~RSString.incomingOpcode != -56) {
														if(RSString.incomingOpcode == 164) {
															nodeModelId = GraphicDefinition.incomingBuffer.getIntA(-1);
															Class136.aClass64_1778 = Class38.aClass87_665.method1449(var0 ^ -82, nodeModelId);
															RSString.incomingOpcode = -1;
															return true;
														} else if(-226 != ~RSString.incomingOpcode) {
															if(RSString.incomingOpcode == 48) {
																nodeModelId = GraphicDefinition.incomingBuffer.getShort(1);
																var24 = GraphicDefinition.incomingBuffer.getString();
																modelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-115);
																if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																	Class3_Sub13_Sub27.method295(var24, (byte)40, modelId);
																}

																RSString.incomingOpcode = -1;
																return true;
															} else if(232 == RSString.incomingOpcode) {
																Class3_Sub13_Sub8.anInt3101 = GraphicDefinition.incomingBuffer.getByte((byte)-72);
																Class24.anInt467 = GraphicDefinition.incomingBuffer.getByte((byte)-128);
																Class45.anInt734 = GraphicDefinition.incomingBuffer.getByte((byte)-57);
																RSString.incomingOpcode = -1;
																return true;
															} else {
																RSString var56;
																if(RSString.incomingOpcode == 44) {
																	nodeModelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-88);
																	if(nodeModelId == '\uffff') {
																		nodeModelId = -1;
																	}

																	var19 = GraphicDefinition.incomingBuffer.getByte((byte)-68);
																	modelId = GraphicDefinition.incomingBuffer.getByte((byte)-122);
																	var56 = GraphicDefinition.incomingBuffer.getString();
																	if(1 <= modelId && ~modelId >= -9) {
																		if(var56.equals(-121, Class50.aClass94_829)) {
																			var56 = null;
																		}

																		Class91.aClass94Array1299[-1 + modelId] = var56;
																		Class3_Sub13_Sub26.anIntArray3328[modelId + -1] = nodeModelId;
																		Class1.aBooleanArray54[modelId + -1] = ~var19 == -1;
																	}

																	RSString.incomingOpcode = -1;
																	return true;
																} else if(RSString.incomingOpcode != 226) {
																	if(RSString.incomingOpcode == 21) {
																		nodeModelId = GraphicDefinition.incomingBuffer.getByteC(true);
																		var19 = GraphicDefinition.incomingBuffer.getShort(1);
																		modelId = GraphicDefinition.incomingBuffer.getLEInt(var0 ^ 19);
																		if(Class146.updateInterfacePacketCounter(var19, (byte)-25)) {
																			Class3_Sub13_Sub19.method260(-16207, modelId, nodeModelId);
																		}

																		RSString.incomingOpcode = -1;
																		return true;
																	} else if(-146 == ~RSString.incomingOpcode) {
																		nodeModelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-110);
																		var19 = GraphicDefinition.incomingBuffer.getByteA((byte)-101);
																		modelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-120);
																		if(Class146.updateInterfacePacketCounter(modelId, (byte)-25)) {
																			if(-3 == ~var19) {
																				Class7.method834((byte)-86);
																			}

																			Class3_Sub28_Sub12.anInt3655 = nodeModelId;
																			Class3_Sub13_Sub13.method232(nodeModelId, 16182);
																			Class124.method1746(false, (byte)-64);
																			Class3_Sub13_Sub12.method226(Class3_Sub28_Sub12.anInt3655, 69);

																			for(counter = 0; -101 < ~counter; ++counter) {
																				Class3_Sub28_Sub14.aBooleanArray3674[counter] = true;
																			}
																		}

																		RSString.incomingOpcode = -1;
																		return true;
																	} else if(-70 != ~RSString.incomingOpcode) {
																		if(141 == RSString.incomingOpcode) {
																			var2 = GraphicDefinition.incomingBuffer.getLong(-125);
																			modelId = GraphicDefinition.incomingBuffer.getShort(1);
																			var56 = Class3_Sub29.method733(var0 + 12345761, modelId).method555(28021, GraphicDefinition.incomingBuffer);
																			Class3_Sub28_Sub12.sendGameMessage(modelId, 19, var56, (RSString)null, (byte)50, Class41.method1052(-29664, var2).method1545((byte)-50));
																			RSString.incomingOpcode = -1;
																			return true;
																		} else if(-170 != ~RSString.incomingOpcode) {
																			if(89 == RSString.incomingOpcode) {
																				Class3_Sub13_Sub2.method176(-117);
																				Class3_Sub30_Sub1.method819(false);
																				Class36.anInt641 += 32;
																				RSString.incomingOpcode = -1;
																				return true;
																			} else if(-126 == ~RSString.incomingOpcode) {//camera rotation
																				nodeModelId = GraphicDefinition.incomingBuffer.getShort(1);
																				var19 = GraphicDefinition.incomingBuffer.getByte((byte)-37);
																				modelId = GraphicDefinition.incomingBuffer.getByte((byte)-114);
																				counter = GraphicDefinition.incomingBuffer.getShort(1);
																				var6 = GraphicDefinition.incomingBuffer.getByte((byte)-59);
																				var30 = GraphicDefinition.incomingBuffer.getByte((byte)-96);
																				if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																					Class164_Sub1.method2238(counter, modelId, var6, var19, (byte)-21, var30);
																				}

																				RSString.incomingOpcode = -1;
																				return true;
																			} else if(-37 == ~RSString.incomingOpcode) {
																				nodeModelId = GraphicDefinition.incomingBuffer.getIntB((byte)122);
																				var19 = GraphicDefinition.incomingBuffer.getLEShort((byte)10);
																				modelId = GraphicDefinition.incomingBuffer.getShortA(114);
																				if(Class146.updateInterfacePacketCounter(modelId, (byte)-25)) {
																					Class131.method1790(nodeModelId, var19, var0 + 178);
																				}

																				RSString.incomingOpcode = -1;
																				return true;
																			} else {
																				Class3_Sub1 var38;
																				Class3_Sub1 var47;
																				if(-10 == ~RSString.incomingOpcode) {
																					nodeModelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-97);
																					var19 = GraphicDefinition.incomingBuffer.getLEInt(-101);
																					modelId = GraphicDefinition.incomingBuffer.getShortA(-105);
																					counter = GraphicDefinition.incomingBuffer.getLEShort(var0 ^ 23);
																					if(-65536 == ~counter) {
																						counter = -1;
																					}

																					var6 = GraphicDefinition.incomingBuffer.getShortA(127);
																					if(-65536 == ~var6) {
																						var6 = -1;
																					}

																					if(Class146.updateInterfacePacketCounter(modelId, (byte)-25)) {
																						for(var30 = var6; counter >= var30; ++var30) {
																							var36 = (long)var30 + ((long)var19 << 32);
																							var47 = (Class3_Sub1)Class124.aClass130_1659.method1780(var36, 0);
																							if(null != var47) {
																								var38 = new Class3_Sub1(var47.anInt2205, nodeModelId);
																								var47.method86(-1024);
																							} else if(0 != ~var30) {
																								var38 = new Class3_Sub1(0, nodeModelId);
																							} else {
																								var38 = new Class3_Sub1(Class7.getRSInterface((byte)119, var19).aClass3_Sub1_257.anInt2205, nodeModelId);
																							}

																							Class124.aClass130_1659.method1779(1, var38, var36);
																						}
																					}

																					RSString.incomingOpcode = -1;
																					return true;
																				} else {
																					int var33;
																					if(RSString.incomingOpcode == 56) {
																						nodeModelId = GraphicDefinition.incomingBuffer.getShort(1);
																						var19 = GraphicDefinition.incomingBuffer.getLEShort(var0 + -8);
																						modelId = GraphicDefinition.incomingBuffer.getIntA(-1);
																						counter = GraphicDefinition.incomingBuffer.getLEShortA((byte)-119);
																						if(~(modelId >> 30) == -1) {
																							AnimationDefinition var53;
																							if(modelId >> 29 != 0) {
																								var6 = '\uffff' & modelId;
																								NPC var62 = Class3_Sub13_Sub24.npcs[var6];
																								if(null != var62) {
																									if(-65536 == ~counter) {
																										counter = -1;
																									}

																									var32 = true;
																									if(0 != ~counter && -1 != var62.anInt2842 && ~Client.getAnimationDefinition(RenderAnimationDefinition.getGraphicDefinition((byte)42, counter).anInt542, (byte)-20).anInt1857 > ~Client.getAnimationDefinition(RenderAnimationDefinition.getGraphicDefinition((byte)42, var62.anInt2842).anInt542, (byte)-20).anInt1857) {
																										var32 = false;
																									}

																									if(var32) {
																										var62.anInt2761 = 0;
																										var62.anInt2842 = counter;
																										var62.anInt2759 = Class44.anInt719 - -nodeModelId;
																										var62.anInt2805 = 0;
																										if(var62.anInt2759 > Class44.anInt719) {
																											var62.anInt2805 = -1;
																										}

																										var62.anInt2799 = var19;
																										var62.anInt2826 = 1;
																										if(~var62.anInt2842 != 0 && Class44.anInt719 == var62.anInt2759) {
																											var33 = RenderAnimationDefinition.getGraphicDefinition((byte)42, var62.anInt2842).anInt542;
																											if(~var33 != 0) {
																												var53 = Client.getAnimationDefinition(var33, (byte)-20);
																												if(var53 != null && null != var53.frames) {
																													IOHandler.method1470(var62.anInt2829, var53, 183921384, var62.anInt2819, false, 0);
																												}
																											}
																										}
																									}
																								}
																							} else if(-1 != ~(modelId >> 28)) {
																								var6 = modelId & '\uffff';
																								Player var60;
																								if(~Class3_Sub1.localIndex == ~var6) {
																									var60 = Class102.player;
																								} else {
																									var60 = Class3_Sub13_Sub22.players[var6];
																								}

																								if(null != var60) {
																									if(counter == '\uffff') {
																										counter = -1;
																									}

																									var32 = true;
																									if(counter != -1 && ~var60.anInt2842 != 0 && ~Client.getAnimationDefinition(RenderAnimationDefinition.getGraphicDefinition((byte)42, counter).anInt542, (byte)-20).anInt1857 > ~Client.getAnimationDefinition(RenderAnimationDefinition.getGraphicDefinition((byte)42, var60.anInt2842).anInt542, (byte)-20).anInt1857) {
																										var32 = false;
																									}

																									if(var32) {
																										var60.anInt2759 = nodeModelId + Class44.anInt719;
																										var60.anInt2799 = var19;
																										var60.anInt2842 = counter;
																										if(~var60.anInt2842 == -65536) {
																											var60.anInt2842 = -1;
																										}

																										var60.anInt2826 = 1;
																										var60.anInt2761 = 0;
																										var60.anInt2805 = 0;
																										if(var60.anInt2759 > Class44.anInt719) {
																											var60.anInt2805 = -1;
																										}

																										if(0 != ~var60.anInt2842 && ~var60.anInt2759 == ~Class44.anInt719) {
																											var33 = RenderAnimationDefinition.getGraphicDefinition((byte)42, var60.anInt2842).anInt542;
																											if(0 != ~var33) {
																												var53 = Client.getAnimationDefinition(var33, (byte)-20);
																												if(null != var53 && null != var53.frames) {
																													IOHandler.method1470(var60.anInt2829, var53, 183921384, var60.anInt2819, var60 == Class102.player, 0);
																												}
																											}
																										}
																									}
																								}
																							}
																						} else {
																							var6 = 3 & modelId >> 28;
																							var30 = ((modelId & 268434277) >> 14) + -Class131.anInt1716;
																							chatIcon = (modelId & 16383) + -Class82.anInt1152;
																							if(~var30 <= -1 && chatIcon >= 0 && 104 > var30 && ~chatIcon > -105) {
																								chatIcon = chatIcon * 128 - -64;
																								var30 = 128 * var30 + 64;
																								Class140_Sub2 var50 = new Class140_Sub2(counter, var6, var30, chatIcon, -var19 + Class121.method1736(var6, 1, var30, chatIcon), nodeModelId, Class44.anInt719);
																								Class3_Sub13_Sub15.aClass61_3177.method1215(true, new Class3_Sub28_Sub2(var50));
																							}
																						}

																						RSString.incomingOpcode = -1;
																						return true;
																					} else if(-208 != ~RSString.incomingOpcode) {
																						if(~RSString.incomingOpcode == -39) {
																							Class3_Sub30_Sub1.method819(false);
																							nodeModelId = GraphicDefinition.incomingBuffer.getByteA((byte)-111);
																							var19 = GraphicDefinition.incomingBuffer.getIntA(-1);
																							modelId = GraphicDefinition.incomingBuffer.getByte((byte)-92);
																							Class133.anIntArray1743[modelId] = var19;
																							Class3_Sub13_Sub15.anIntArray3185[modelId] = nodeModelId;
																							Class3_Sub20.anIntArray2480[modelId] = 1;

																							for(counter = 0; 98 > counter; ++counter) {
																								if(ItemDefinition.anIntArray781[counter] <= var19) {
																									Class3_Sub20.anIntArray2480[modelId] = counter + 2;
																								}
																							}

																							Class3_Sub28_Sub19.anIntArray3780[Class3_Sub28_Sub15.method633(31, Class49.anInt815++)] = modelId;
																							RSString.incomingOpcode = -1;
																							return true;
																						} else if(RSString.incomingOpcode != 104 && 121 != RSString.incomingOpcode && -98 != ~RSString.incomingOpcode && ~RSString.incomingOpcode != -15 && ~RSString.incomingOpcode != -203 && ~RSString.incomingOpcode != -136 && ~RSString.incomingOpcode != -18 && RSString.incomingOpcode != 16 && RSString.incomingOpcode != 240 && RSString.incomingOpcode != 33 && -21 != ~RSString.incomingOpcode && 195 != RSString.incomingOpcode && 179 != RSString.incomingOpcode) {
																							if(RSString.incomingOpcode == 149) {
																								nodeModelId = GraphicDefinition.incomingBuffer.getShort(1);
																								var19 = GraphicDefinition.incomingBuffer.getInt();
																								if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																									Class3_Sub31 var67 = (Class3_Sub31)Class3_Sub13_Sub17.aClass130_3208.method1780((long)var19, 0);
																									if(null != var67) {
																										Class3_Sub13_Sub18.method254(true, var67, false);
																									}

																									if(null != Class3_Sub13_Sub7.aClass11_3087) {
																										Class20.method909(115, Class3_Sub13_Sub7.aClass11_3087);
																										Class3_Sub13_Sub7.aClass11_3087 = null;
																									}
																								}

																								RSString.incomingOpcode = -1;
																								return true;
																							} else if(RSString.incomingOpcode != 187) {
																								if(RSString.incomingOpcode == 132) {
																									nodeModelId = GraphicDefinition.incomingBuffer.getShort(1);
																									var19 = GraphicDefinition.incomingBuffer.getShortA(31);
																									modelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-117);
																									counter = GraphicDefinition.incomingBuffer.getLEShortA((byte)-90);
																									var6 = GraphicDefinition.incomingBuffer.getInt();
																									if(Class146.updateInterfacePacketCounter(var19, (byte)-25)) {
																										CacheIndex.method2143((byte)-124, modelId, var6, counter, nodeModelId);
																									}

																									RSString.incomingOpcode = -1;
																									return true;
																								} else if(112 == RSString.incomingOpcode) {
																									Class65.currentChunkX = GraphicDefinition.incomingBuffer.getByte((byte)-126);
																									Class107.currentChunkY = GraphicDefinition.incomingBuffer.getByteC(true);

																									for(nodeModelId = Class65.currentChunkX; nodeModelId < 8 + Class65.currentChunkX; ++nodeModelId) {
																										for(var19 = Class107.currentChunkY; ~var19 > ~(8 + Class107.currentChunkY); ++var19) {
																											if(null != Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][nodeModelId][var19]) {
																												Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][nodeModelId][var19] = null;
																												Class128.method1760(var19, (byte)65, nodeModelId);
																											}
																										}
																									}

																									for(Class3_Sub4 var68 = (Class3_Sub4)Class3_Sub13_Sub6.aClass61_3075.method1222(); null != var68; var68 = (Class3_Sub4)Class3_Sub13_Sub6.aClass61_3075.method1221()) {
																										if(~var68.anInt2264 <= ~Class65.currentChunkX && 8 + Class65.currentChunkX > var68.anInt2264 && var68.anInt2248 >= Class107.currentChunkY && ~var68.anInt2248 > ~(8 + Class107.currentChunkY) && var68.anInt2250 == WorldListCountry.localPlane) {
																											var68.anInt2259 = 0;
																										}
																									}

																									RSString.incomingOpcode = -1;
																									return true;
																								} else if(RSString.incomingOpcode == 144) {
																									nodeModelId = GraphicDefinition.incomingBuffer.getIntB((byte)72);
																									RSInterface var65 = Class7.getRSInterface((byte)111, nodeModelId);

																									for(modelId = 0; var65.itemAmounts.length > modelId; ++modelId) {
																										var65.itemAmounts[modelId] = -1;
																										var65.itemAmounts[modelId] = 0;
																									}

																									Class20.method909(123, var65);
																									RSString.incomingOpcode = -1;
																									return true;
																								} else if(-131 == ~RSString.incomingOpcode) {
																									nodeModelId = GraphicDefinition.incomingBuffer.getLEInt(-104);
																									var19 = GraphicDefinition.incomingBuffer.getLEShortA((byte)-125);
																									modelId = GraphicDefinition.incomingBuffer.getShortA(var0 ^ -2);
																									if(modelId == '\uffff') {
																										modelId = -1;
																									}

																									if(Class146.updateInterfacePacketCounter(var19, (byte)-25)) {
																										Class3_Sub13_Sub18.method256(-1, 1, nodeModelId, (byte)-109, modelId);
																									}

																									RSString.incomingOpcode = -1;
																									return true;
																								} else if(-193 == ~RSString.incomingOpcode) {
																									Class161.anInt2028 = GraphicDefinition.incomingBuffer.getByte((byte)-59);
																									RSString.incomingOpcode = -1;
																									return true;
																								} else if(~RSString.incomingOpcode == -14) {
																									nodeModelId = GraphicDefinition.incomingBuffer.getByteS(true);
																									var19 = GraphicDefinition.incomingBuffer.getByteA((byte)108);
																									modelId = GraphicDefinition.incomingBuffer.getByte((byte)-41);
																									WorldListCountry.localPlane = var19 >> 1;
																									Class102.player.method1981((byte)126, nodeModelId, ~(var19 & 1) == -2, modelId);
																									RSString.incomingOpcode = -1;
																									return true;
																								} else {
																									int var12;
																									RSString var57;
																									RSString var64;
																									if(-63 == ~RSString.incomingOpcode) {
																										var2 = GraphicDefinition.incomingBuffer.getLong(-127);
																										modelId = GraphicDefinition.incomingBuffer.getShort(1);
																										counter = GraphicDefinition.incomingBuffer.getByte((byte)-99);
																										var31 = true;
																										if(var2 < 0L) {
																											var2 &= Long.MAX_VALUE;
																											var31 = false;
																										}

																										var41 = Class3_Sub28_Sub14.aClass94_3672;
																										if(-1 > ~modelId) {
																											var41 = GraphicDefinition.incomingBuffer.getString();
																										}

																										RSString var46 = Class41.method1052(-29664, var2).method1545((byte)-50);

																										for(var33 = 0; var33 < Class8.anInt104; ++var33) {
																											if(var2 == Class50.aLongArray826[var33]) {
																												if(~modelId != ~Class55.anIntArray882[var33]) {
																													Class55.anIntArray882[var33] = modelId;
																													if(0 < modelId) {
																														Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 5, RenderAnimationDefinition.method903(new RSString[]{var46, Class3_Sub28_Sub10_Sub1.aClass94_4058}, (byte)-77), -1);
																													}

																													if(modelId == 0) {
																														Class3_Sub30_Sub1.sendMessage(Class3_Sub28_Sub14.aClass94_3672, 5, RenderAnimationDefinition.method903(new RSString[]{var46, Class50.aClass94_822}, (byte)-97), -1);
																													}
																												}

																												Node.aClass94Array2566[var33] = var41;
																												Class57.anIntArray904[var33] = counter;
																												var46 = null;
																												Class3.aBooleanArray73[var33] = var31;
																												break;
																											}
																										}

																										boolean var45 = false;
																										if(null != var46 && 200 > Class8.anInt104) {
																											Class50.aLongArray826[Class8.anInt104] = var2;
																											Class70.aClass94Array1046[Class8.anInt104] = var46;
																											Class55.anIntArray882[Class8.anInt104] = modelId;
																											Node.aClass94Array2566[Class8.anInt104] = var41;
																											Class57.anIntArray904[Class8.anInt104] = counter;
																											Class3.aBooleanArray73[Class8.anInt104] = var31;
																											++Class8.anInt104;
																										}

																										Class110.anInt1472 = Class3_Sub13_Sub17.anInt3213;
																										clanChatIcon = Class8.anInt104;

																										while(~clanChatIcon < -1) {
																											--clanChatIcon;
																											var45 = true;

																											for(var11 = 0; var11 < clanChatIcon; ++var11) {
																												if(~Class55.anIntArray882[var11] != ~CS2Script.anInt2451 && ~CS2Script.anInt2451 == ~Class55.anIntArray882[var11 - -1] || Class55.anIntArray882[var11] == 0 && Class55.anIntArray882[var11 - -1] != 0) {
																													var45 = false;
																													var12 = Class55.anIntArray882[var11];
																													Class55.anIntArray882[var11] = Class55.anIntArray882[var11 - -1];
																													Class55.anIntArray882[1 + var11] = var12;
																													var64 = Node.aClass94Array2566[var11];
																													Node.aClass94Array2566[var11] = Node.aClass94Array2566[var11 + 1];
																													Node.aClass94Array2566[var11 - -1] = var64;
																													var57 = Class70.aClass94Array1046[var11];
																													Class70.aClass94Array1046[var11] = Class70.aClass94Array1046[var11 + 1];
																													Class70.aClass94Array1046[var11 - -1] = var57;
																													long var15 = Class50.aLongArray826[var11];
																													Class50.aLongArray826[var11] = Class50.aLongArray826[var11 - -1];
																													Class50.aLongArray826[var11 - -1] = var15;
																													int var17 = Class57.anIntArray904[var11];
																													Class57.anIntArray904[var11] = Class57.anIntArray904[var11 - -1];
																													Class57.anIntArray904[1 + var11] = var17;
																													boolean var18 = Class3.aBooleanArray73[var11];
																													Class3.aBooleanArray73[var11] = Class3.aBooleanArray73[var11 - -1];
																													Class3.aBooleanArray73[var11 - -1] = var18;
																												}
																											}

																											if(var45) {
																												break;
																											}
																										}

																										RSString.incomingOpcode = -1;
																										return true;
																									} else if(-161 == ~RSString.incomingOpcode) {
																										if(0 != Class130.incomingPacketLength) {
																											Class3_Sub13_Sub28.aClass94_3353 = GraphicDefinition.incomingBuffer.getString();
																										} else {
																											Class3_Sub13_Sub28.aClass94_3353 = Class56.aClass94_891;
																										}

																										RSString.incomingOpcode = -1;
																										return true;
																									} else if(128 != RSString.incomingOpcode) {
																										if(~RSString.incomingOpcode == -155) {//camera position
																											nodeModelId = GraphicDefinition.incomingBuffer.getShort(var0 ^ -84);
																											var19 = GraphicDefinition.incomingBuffer.getByte((byte)-56);
																											modelId = GraphicDefinition.incomingBuffer.getByte((byte)-23);
																											counter = GraphicDefinition.incomingBuffer.getShort(1);
																											var6 = GraphicDefinition.incomingBuffer.getByte((byte)-123);
																											var30 = GraphicDefinition.incomingBuffer.getByte((byte)-92);
																											if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																												Class3_Sub20.method390(true, var6, counter, var30, (byte)-124, modelId, var19);
																											}

																											RSString.incomingOpcode = -1;
																											return true;
																										} else if(247 == RSString.incomingOpcode) {
																											var2 = GraphicDefinition.incomingBuffer.getLong(-115);
																											var4 = (long)GraphicDefinition.incomingBuffer.getShort(1);
																											var29 = (long)GraphicDefinition.incomingBuffer.getTriByte((byte)77);
																											chatIcon = GraphicDefinition.incomingBuffer.getByte((byte)-28);
																											var33 = GraphicDefinition.incomingBuffer.getShort(1);
																											boolean var49 = false;
																											long var51 = (var4 << -737495840) - -var29;
																											int var59 = 0;

																											label1603:
																												while(true) {
																													if(100 > var59) {
																														if(~var51 != ~Class163_Sub2_Sub1.aLongArray4017[var59]) {
																															++var59;
																															continue;
																														}

																														var49 = true;
																														break;
																													}

																													if(chatIcon <= 1) {
																														for(var59 = 0; ~var59 > ~Class3_Sub28_Sub5.anInt3591; ++var59) {
																															if(var2 == Class114.aLongArray1574[var59]) {
																																var49 = true;
																																break label1603;
																															}
																														}
																													}
																													break;
																												}

																											if(!var49 && ~WorldListEntry.anInt2622 == -1) {
																												Class163_Sub2_Sub1.aLongArray4017[Class149.anInt1921] = var51;
																												Class149.anInt1921 = (1 + Class149.anInt1921) % 100;
																												var64 = Class3_Sub29.method733(12345678, var33).method555(28021, GraphicDefinition.incomingBuffer);
																												if(-3 == ~chatIcon) {
																													Class3_Sub28_Sub12.sendGameMessage(var33, 18, var64, (RSString)null, (byte)50, RenderAnimationDefinition.method903(new RSString[]{Class21.aClass94_444, Class41.method1052(-29664, var2).method1545((byte)-50)}, (byte)-105));
																												} else if(1 == chatIcon) {
																													Class3_Sub28_Sub12.sendGameMessage(var33, 18, var64, (RSString)null, (byte)50, RenderAnimationDefinition.method903(new RSString[]{Class32.aClass94_592, Class41.method1052(-29664, var2).method1545((byte)-50)}, (byte)-113));
																												} else {
																													Class3_Sub28_Sub12.sendGameMessage(var33, 18, var64, (RSString)null, (byte)50, Class41.method1052(var0 + -29581, var2).method1545((byte)-50));
																												}
																											}

																											RSString.incomingOpcode = -1;
																											return true;
																										} else {
																											Class3_Sub31 var26;
																											if(~RSString.incomingOpcode != -177) {
																												if(RSString.incomingOpcode != 27) {
																													if(RSString.incomingOpcode == 2) {
																														nodeModelId = GraphicDefinition.incomingBuffer.getIntA(-1);
																														var19 = GraphicDefinition.incomingBuffer.getShortA(-114);
																														modelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-119);
																														if(Class146.updateInterfacePacketCounter(var19, (byte)-25)) {
																															Class79.method1385(modelId, nodeModelId, (byte)-127);
																														}

																														RSString.incomingOpcode = -1;
																														return true;
																													} else if(-86 == ~RSString.incomingOpcode) {
																														Class38_Sub1.anInt2617 = GraphicDefinition.incomingBuffer.getShort(1) * 30;
																														RSString.incomingOpcode = -1;
																														Class140_Sub6.anInt2905 = Class3_Sub13_Sub17.anInt3213;
																														return true;
																													} else if(~RSString.incomingOpcode == -115) {
																														Class3_Sub13_Sub29.method305(Class38.aClass87_665, GraphicDefinition.incomingBuffer, Class130.incomingPacketLength, (byte)-126);
																														RSString.incomingOpcode = -1;
																														return true;
																													} else if(65 == RSString.incomingOpcode) {
																														nodeModelId = GraphicDefinition.incomingBuffer.getLEShort(var0 ^ 13);
																														var19 = GraphicDefinition.incomingBuffer.getByteC(true);
																														modelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-100);
																														if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																															Class3_Sub13_Sub18.method255(modelId, var19, var0 ^ -84);
																														}

																														RSString.incomingOpcode = -1;
																														return true;
																													} else if(RSString.incomingOpcode == 234) {
																														Class3_Sub30_Sub1.method819(false);
																														Class9.anInt136 = GraphicDefinition.incomingBuffer.getByte((byte)-104);
																														Class140_Sub6.anInt2905 = Class3_Sub13_Sub17.anInt3213;
																														RSString.incomingOpcode = -1;
																														return true;
																													} else if(var0 != -83) {
																														return false;
																													} else if(~RSString.incomingOpcode == -210) {
																														if(-1 != Class3_Sub28_Sub12.anInt3655) {
																															Class3_Sub8.method124(48, 0, Class3_Sub28_Sub12.anInt3655);
																														}

																														RSString.incomingOpcode = -1;
																														return true;
																													} else if(~RSString.incomingOpcode != -192) {
																														if(-103 == ~RSString.incomingOpcode) {
																															nodeModelId = GraphicDefinition.incomingBuffer.getLEShort(-116);
																															var19 = GraphicDefinition.incomingBuffer.getByteS(true);
																															modelId = GraphicDefinition.incomingBuffer.getShort(var0 ^ -84);
																															NPC var39 = Class3_Sub13_Sub24.npcs[nodeModelId];
																															if(null != var39) {
																																Class130.method1772(var19, modelId, 39, var39);
																															}

																															RSString.incomingOpcode = -1;
																															return true;
																														} else if(RSString.incomingOpcode != 159) {
																															if(~RSString.incomingOpcode == -72) {
																																var2 = GraphicDefinition.incomingBuffer.getLong(var0 ^ 28);
																																var58 = Class3_Sub28_Sub17.method686(Class32.method992(GraphicDefinition.incomingBuffer, var0 ^ -29539).method1536(121));
																																Class3_Sub30_Sub1.sendMessage(Class41.method1052(-29664, var2).method1545((byte)-50), 6, var58, var0 ^ 82);
																																RSString.incomingOpcode = -1;
																																return true;
																															} else if(-43 != ~RSString.incomingOpcode) {
																																if(-112 == ~RSString.incomingOpcode) {
																																	nodeModelId = GraphicDefinition.incomingBuffer.getShortA(-123);
																																	var19 = GraphicDefinition.incomingBuffer.getIntB((byte)-45);
																																	modelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-109);
																																	counter = GraphicDefinition.incomingBuffer.getLEShort(var0 + 19);
																																	var6 = GraphicDefinition.incomingBuffer.getLEShortA((byte)-107);
																																	if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																																		Class3_Sub13_Sub18.method256(modelId, 7, var19, (byte)-126, counter << -311274832 | var6);
																																	}

																																	RSString.incomingOpcode = -1;
																																	return true;
																																} else if(37 == RSString.incomingOpcode) {
																																	nodeModelId = GraphicDefinition.incomingBuffer.getByteA((byte)122);
																																	var19 = GraphicDefinition.incomingBuffer.getLEShort(-124);
																																	Class163.method2209((byte)-122, nodeModelId, var19);
																																	RSString.incomingOpcode = -1;
																																	return true;
																																} else if(~RSString.incomingOpcode == -156) {
																																	nodeModelId = GraphicDefinition.incomingBuffer.getByte((byte)-66);
																																	var19 = GraphicDefinition.incomingBuffer.getIntB((byte)-51);
																																	modelId = GraphicDefinition.incomingBuffer.getShortA(var0 + 163);
																																	counter = GraphicDefinition.incomingBuffer.getShort(1);
																																	if(Class146.updateInterfacePacketCounter(modelId, (byte)-25)) {
																																		var26 = (Class3_Sub31)Class3_Sub13_Sub17.aClass130_3208.method1780((long)var19, 0);
																																		if(null != var26) {
																																			Class3_Sub13_Sub18.method254(var26.anInt2602 != counter, var26, false);
																																		}

																																		Class21.method914(6422, counter, var19, nodeModelId);
																																	}

																																	RSString.incomingOpcode = -1;
																																	return true;
																																} else if(~RSString.incomingOpcode == -132) {
																																	for(nodeModelId = 0; nodeModelId < Class3_Sub13_Sub22.players.length; ++nodeModelId) {
																																		if(Class3_Sub13_Sub22.players[nodeModelId] != null) {
																																			Class3_Sub13_Sub22.players[nodeModelId].anInt2771 = -1;
																																		}
																																	}

																																	for(nodeModelId = 0; ~Class3_Sub13_Sub24.npcs.length < ~nodeModelId; ++nodeModelId) {
																																		if(null != Class3_Sub13_Sub24.npcs[nodeModelId]) {
																																			Class3_Sub13_Sub24.npcs[nodeModelId].anInt2771 = -1;
																																		}
																																	}

																																	RSString.incomingOpcode = -1;
																																	return true;
																																} else if(~RSString.incomingOpcode == -218) {
																																	nodeModelId = GraphicDefinition.incomingBuffer.getByte((byte)-30);
																																	Class96 var48 = new Class96();
																																	var19 = nodeModelId >> 340093798;
																																	var48.anInt1360 = nodeModelId & 63;
																																	var48.anInt1351 = GraphicDefinition.incomingBuffer.getByte((byte)-49);
																																	if(~var48.anInt1351 <= -1 && ~var48.anInt1351 > ~Class166.aClass3_Sub28_Sub16Array2072.length) {
																																		if(~var48.anInt1360 != -2 && 10 != var48.anInt1360) {
																																			if(-3 >= ~var48.anInt1360 && 6 >= var48.anInt1360) {
																																				if(var48.anInt1360 == 2) {
																																					var48.anInt1346 = 64;
																																					var48.anInt1350 = 64;
																																				}

																																				if(-4 == ~var48.anInt1360) {
																																					var48.anInt1346 = 0;
																																					var48.anInt1350 = 64;
																																				}

																																				if(4 == var48.anInt1360) {
																																					var48.anInt1346 = 128;
																																					var48.anInt1350 = 64;
																																				}

																																				if(5 == var48.anInt1360) {
																																					var48.anInt1346 = 64;
																																					var48.anInt1350 = 0;
																																				}

																																				if(-7 == ~var48.anInt1360) {
																																					var48.anInt1346 = 64;
																																					var48.anInt1350 = 128;
																																				}

																																				var48.anInt1360 = 2;
																																				var48.anInt1356 = GraphicDefinition.incomingBuffer.getShort(var0 + 84);
																																				var48.anInt1347 = GraphicDefinition.incomingBuffer.getShort(1);
																																				var48.anInt1353 = GraphicDefinition.incomingBuffer.getByte((byte)-32);
																																			}
																																		} else {
																																			var48.anInt1359 = GraphicDefinition.incomingBuffer.getShort(var0 ^ -84);
																																			GraphicDefinition.incomingBuffer.index += 3;
																																		}

																																		var48.anInt1355 = GraphicDefinition.incomingBuffer.getShort(1);
																																		if(var48.anInt1355 == '\uffff') {
																																			var48.anInt1355 = -1;
																																		}

																																		RuntimeException_Sub1.aClass96Array2114[var19] = var48;
																																	}

																																	RSString.incomingOpcode = -1;
																																	return true;
																																} else if(126 == RSString.incomingOpcode) {
																																	Class3_Sub28_Sub5.anInt3591 = Class130.incomingPacketLength / 8;

																																	for(nodeModelId = 0; ~nodeModelId > ~Class3_Sub28_Sub5.anInt3591; ++nodeModelId) {
																																		Class114.aLongArray1574[nodeModelId] = GraphicDefinition.incomingBuffer.getLong(-120);
																																		Class3_Sub13_Sub27.aClass94Array3341[nodeModelId] = Class41.method1052(-29664, Class114.aLongArray1574[nodeModelId]);
																																	}

																																	Class110.anInt1472 = Class3_Sub13_Sub17.anInt3213;
																																	RSString.incomingOpcode = -1;
																																	return true;
																																} else if(~RSString.incomingOpcode == -33) {
																																	Class3_Sub13_Sub14.renderNPCs(8169);
																																	RSString.incomingOpcode = -1;
																																	return true;
																																} else if(-120 == ~RSString.incomingOpcode) {
																																	//Reposition child?
																																	nodeModelId = GraphicDefinition.incomingBuffer.getShortA(-125);
																																	var19 = GraphicDefinition.incomingBuffer.getLEInt(-48);
																																	modelId = GraphicDefinition.incomingBuffer.getShort((byte)74);
																																	counter = GraphicDefinition.incomingBuffer.getShortAs(-58);
																																	if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																																		Class168.method2271(modelId, var19, 1, counter);
																																	}

																																	RSString.incomingOpcode = -1;
																																	return true;
																																} else if(RSString.incomingOpcode == 235) {
																																	nodeModelId = GraphicDefinition.incomingBuffer.getByteS(true);
																																	var19 = nodeModelId >> -518983614;
																											modelId = 3 & nodeModelId;
																											counter = Class75.anIntArray1107[var19];
																											var6 = GraphicDefinition.incomingBuffer.getShort(1);
																											var30 = GraphicDefinition.incomingBuffer.getInt();
																											if('\uffff' == var6) {
																												var6 = -1;
																											}

																											clanChatIcon = 16383 & var30;
																											var33 = 16383 & var30 >> 2070792462;
																										var33 -= Class131.anInt1716;
																										clanChatIcon -= Class82.anInt1152;
																										chatIcon = 3 & var30 >> 765199868;
																										Class50.method1131(chatIcon, 110, modelId, var19, clanChatIcon, counter, var33, var6);
																										RSString.incomingOpcode = -1;
																										return true;
																																} else if(RSString.incomingOpcode == 0) {
																																	var2 = GraphicDefinition.incomingBuffer.getLong(-85);
																																	var4 = (long)GraphicDefinition.incomingBuffer.getShort(var0 + 84);
																																	var29 = (long)GraphicDefinition.incomingBuffer.getTriByte((byte)93);
																																	chatIcon = GraphicDefinition.incomingBuffer.getByte((byte)-106);
																																	boolean var42 = false;
																																	long var35 = var29 + (var4 << -1802335520);
																																	var12 = 0;
																																	label1651:
																																		while(true) {
																																			if(-101 >= ~var12) {
																																				if(chatIcon <= 1) {
																																					if((!Class3_Sub15.aBoolean2433 || Class121.aBoolean1641) && !Class3_Sub13_Sub14.aBoolean3166) {
																																						for(var12 = 0; var12 < Class3_Sub28_Sub5.anInt3591; ++var12) {
																																							if(~var2 == ~Class114.aLongArray1574[var12]) {
																																								var42 = true;
																																								break label1651;
																																							}
																																						}
																																					} else {
																																						var42 = true;
																																					}
																																				}
																																				break;
																																			}

																																			if(~var35 == ~Class163_Sub2_Sub1.aLongArray4017[var12]) {
																																				var42 = true;
																																				break;
																																			}

																																			++var12;
																																		}

																																	if(!var42 && -1 == ~WorldListEntry.anInt2622) {
																																		Class163_Sub2_Sub1.aLongArray4017[Class149.anInt1921] = var35;
																																		Class149.anInt1921 = (Class149.anInt1921 - -1) % 100;
																																		RSString var52 = Class3_Sub28_Sub17.method686(Class32.method992(GraphicDefinition.incomingBuffer, var0 ^ -29539).method1536(96));
																																		if(-3 != ~chatIcon && ~chatIcon != -4) {
																																			if(chatIcon != 1) {
																																				Class3_Sub30_Sub1.sendMessage(RenderAnimationDefinition.method903(new RSString[]{RSString.createRSString("<img=" + (chatIcon -1) + ">"), Class41.method1052(-29664, var2).method1545((byte)-50)}, (byte)-71), 7, var52, -1);
																																				} else {
																																				Class3_Sub30_Sub1.sendMessage(RenderAnimationDefinition.method903(new RSString[]{RSString.createRSString("<img=" + (chatIcon -1) + ">"), Class41.method1052(-29664, var2).method1545((byte)-50)}, (byte)-71), 7, var52, -1);
																																			}
																																		} else {
																																			Class3_Sub30_Sub1.sendMessage(RenderAnimationDefinition.method903(new RSString[]{Class21.aClass94_444, Class41.method1052(-29664, var2).method1545((byte)-50)}, (byte)-105), 7, var52, -1);
																																		}
																																	}

																																	RSString.incomingOpcode = -1;
																																	return true;
																																} else if(-55 != ~RSString.incomingOpcode) {
																																	if(-215 == ~RSString.incomingOpcode) {
																																		Class39.updateSceneGraph(0, true);
																																		RSString.incomingOpcode = -1;
																																		return true;
																																	} else if(~RSString.incomingOpcode != -173) {
																																		if(-67 == ~RSString.incomingOpcode) {
																																			nodeModelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-87);
																																			var19 = GraphicDefinition.incomingBuffer.getIntA(-1);
																																			if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																																				modelId = 0;
																																				if(Class102.player.class52 != null) {
																																					modelId = Class102.player.class52.method1163(-24861);
																																				}

																																				Class3_Sub13_Sub18.method256(-1, 3, var19, (byte)-110, modelId);
																																			}

																																			RSString.incomingOpcode = -1;
																																			return true;
																																		} else if(RSString.incomingOpcode == 171) {
																																			nodeModelId = GraphicDefinition.incomingBuffer.getIntB((byte)-55);
																																			var24 = GraphicDefinition.incomingBuffer.getString();
																																			modelId = GraphicDefinition.incomingBuffer.getShortA(103);
																																			if(Class146.updateInterfacePacketCounter(modelId, (byte)-25)) {
																																				Class3_Sub28_Sub7.method566(var24, 0, nodeModelId);
																																			}

																																			RSString.incomingOpcode = -1;
																																			return true;
																																		} else if(~RSString.incomingOpcode == -85) {
																																			nodeModelId = GraphicDefinition.incomingBuffer.getLEInt(-79);
																																			var19 = GraphicDefinition.incomingBuffer.getLEShortA((byte)-96);
																																			Class163.method2209((byte)-106, nodeModelId, var19);
																																			RSString.incomingOpcode = -1;
																																			return true;
																																		} else {
																																			RSInterface var25;
																																			if(RSString.incomingOpcode != 22) {
																																				if(RSString.incomingOpcode == 24) {
																																					nodeModelId = GraphicDefinition.incomingBuffer.getShort(1);
																																					if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																																						Class3_Sub28_Sub5.method560(-21556);
																																					}

																																					RSString.incomingOpcode = -1;
																																					return true;
																																				} else if(~RSString.incomingOpcode == -87) {
																																					Class167.method2269((byte)46);
																																					RSString.incomingOpcode = -1;
																																					return false;
																																				} else if(116 == RSString.incomingOpcode) {
																																					nodeModelId = GraphicDefinition.incomingBuffer.getByte((byte)-62);
																																					if(-1 != ~GraphicDefinition.incomingBuffer.getByte((byte)-124)) {
																																						--GraphicDefinition.incomingBuffer.index;
																																						Class3_Sub13_Sub33.aClass133Array3393[nodeModelId] = new Class133(GraphicDefinition.incomingBuffer);
																																					} else {
																																						Class3_Sub13_Sub33.aClass133Array3393[nodeModelId] = new Class133();
																																					}

																																					RSString.incomingOpcode = -1;
																																					Class121.anInt1642 = Class3_Sub13_Sub17.anInt3213;
																																					return true;
																																				} else if(-74 == ~RSString.incomingOpcode) {//npc model
																																					nodeModelId = GraphicDefinition.incomingBuffer.getShortA(-121);
																																					var19 = GraphicDefinition.incomingBuffer.getLEInt(-105);
																																					if(~nodeModelId == -65536) {
																																						nodeModelId = -1;
																																					}

																																					modelId = GraphicDefinition.incomingBuffer.getLEShort(var0 ^ 19);
																																					if(Class146.updateInterfacePacketCounter(modelId, (byte)-25)) {
																																						Class3_Sub13_Sub18.method256(-1, 2, var19, (byte)-113, nodeModelId);
																																					}

																																					RSString.incomingOpcode = -1;
																																					return true;
																																				} else if(~RSString.incomingOpcode == -163) {
																																					Class39.updateSceneGraph(var0 ^ -83, false);
																																					RSString.incomingOpcode = -1;
																																					return true;
																																				} else if(165 != RSString.incomingOpcode) {
																																					if(RSString.incomingOpcode == 197) {
																																						Class96.anInt1357 = GraphicDefinition.incomingBuffer.getByte((byte)-104);
																																						Class110.anInt1472 = Class3_Sub13_Sub17.anInt3213;
																																						RSString.incomingOpcode = -1;
																																						return true;
																																					} else if(RSString.incomingOpcode != 196) {
																																						if(50 != RSString.incomingOpcode) {
																																							if(~RSString.incomingOpcode != -106) {
																																								if(~RSString.incomingOpcode == -143) {
																																									Class3_Sub29.method734(0, GraphicDefinition.incomingBuffer.getString());
																																									RSString.incomingOpcode = -1;
																																									return true;
																																								} else if(RSString.incomingOpcode != 26) {
																																									if(4 == RSString.incomingOpcode) {
																																										nodeModelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-121);
																																										if(nodeModelId == '\uffff') {
																																											nodeModelId = -1;
																																										}

																																										Class86.method1427(true, nodeModelId);
																																										RSString.incomingOpcode = -1;
																																										return true;
																																									} else if(RSString.incomingOpcode != 208) {
																																										Class49.method1125("T1 - " + RSString.incomingOpcode + "," + Class7.anInt2166 + "," + Class24.anInt469 + " - " + Class130.incomingPacketLength, (Throwable)null, (byte)117);
																																										Class167.method2269((byte)46);
																																										return true;
																																									} else {
																																										nodeModelId = GraphicDefinition.incomingBuffer.getTriByte2((byte)-118);
																																										var19 = GraphicDefinition.incomingBuffer.getLEShort(-57);
																																										if(var19 == '\uffff') {
																																											var19 = -1;
																																										}

																																										Class167.method2266(nodeModelId, var19, (byte)-1);
																																										RSString.incomingOpcode = -1;
																																										return true;
																																									}
																																								} else {
																																									Class65.currentChunkX = GraphicDefinition.incomingBuffer.getByteC(true);
																																									Class107.currentChunkY = GraphicDefinition.incomingBuffer.getByte((byte)-62);
																																									RSString.incomingOpcode = -1;
																																									return true;
																																								}
																																							} else {
																																								nodeModelId = GraphicDefinition.incomingBuffer.getInt();
																																								var19 = GraphicDefinition.incomingBuffer.getShort(var0 ^ -84);
																																								if(~nodeModelId > 69999) {
																																									var19 += '\u8000';
																																								}

																																								if(0 <= nodeModelId) {
																																									var25 = Class7.getRSInterface((byte)118, nodeModelId);
																																								} else {
																																									var25 = null;
																																								}

																																								if(var25 != null) {
																																									for(counter = 0; var25.itemAmounts.length > counter; ++counter) {
																																										var25.itemAmounts[counter] = 0;
																																										var25.itemIds[counter] = 0;
																																									}
																																								}

																																								Class10.method852((byte)114, var19);
																																								counter = GraphicDefinition.incomingBuffer.getShort(1);

																																								for(var6 = 0; counter > var6; ++var6) {
																																									var30 = GraphicDefinition.incomingBuffer.getByteS(true);
																																									if(255 == var30) {
																																										var30 = GraphicDefinition.incomingBuffer.getInt();
																																									}

																																									chatIcon = GraphicDefinition.incomingBuffer.getShort(1);
																																									if(null != var25 && ~var6 > ~var25.itemAmounts.length) {
																																										var25.itemAmounts[var6] = chatIcon;
																																										var25.itemIds[var6] = var30;
																																									}

																																									Class168.method2277(-1 + chatIcon, var6, var30, var19, (byte)41);
																																								}

																																								if(var25 != null) {
																																									Class20.method909(-9, var25);
																																								}

																																								Class3_Sub30_Sub1.method819(false);
																																								Class3_Sub28_Sub4.anIntArray3565[Class3_Sub28_Sub15.method633(Class62.anInt944++, 31)] = Class3_Sub28_Sub15.method633(32767, var19);
																																								RSString.incomingOpcode = -1;
																																								return true;
																																							}
																																						} else {
																																							nodeModelId = GraphicDefinition.incomingBuffer.getInt();
																																							var19 = GraphicDefinition.incomingBuffer.getIntB((byte)-79);
																																							modelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-118);
																																							if('\uffff' == modelId) {
																																								modelId = -1;
																																							}

																																							counter = GraphicDefinition.incomingBuffer.getLEShort(-85);
																																							if(Class146.updateInterfacePacketCounter(counter, (byte)-25)) {
																																								RSInterface var34 = Class7.getRSInterface((byte)115, var19);
																																								ItemDefinition var43;
																																								if(var34.usingScripts) {
																																									Class140_Sub6.method2026((byte)122, var19, nodeModelId, modelId);
																																									var43 = Class38.getItemDefinition(modelId, (byte)70);
																																									CacheIndex.method2143((byte)-128, var43.anInt810, var19, var43.anInt799, var43.anInt786);
																																									Class84.method1420(var19, var43.anInt768, var43.anInt754, var43.anInt792, (byte)-85);
																																								} else {
																																									if(-1 == modelId) {
																																										var34.modelType = 0;
																																										RSString.incomingOpcode = -1;
																																										return true;
																																									}

																																									var43 = Class38.getItemDefinition(modelId, (byte)91);
																																									var34.anInt182 = var43.anInt786;
																																									var34.anInt164 = 100 * var43.anInt810 / nodeModelId;
																																									var34.modelType = 4;
																																									var34.itemId = modelId;
																																									var34.anInt308 = var43.anInt799;
																																									Class20.method909(117, var34);
																																								}
																																							}

																																							RSString.incomingOpcode = -1;
																																							return true;
																																						}
																																					} else {
																																						var2 = GraphicDefinition.incomingBuffer.getLong(-93);
																																						modelId = GraphicDefinition.incomingBuffer.getShort(1);
																																						byte var28 = GraphicDefinition.incomingBuffer.getByte();
																																						var31 = false;
																																						if(-1L != ~(Long.MIN_VALUE & var2)) {
																																							var31 = true;
																																						}

																																						if(!var31) {
																																							var41 = GraphicDefinition.incomingBuffer.getString();
																																							Class3_Sub19 var40 = new Class3_Sub19();
																																							var40.aLong71 = var2;
																																							var40.aClass94_2476 = Class41.method1052(-29664, var40.aLong71);
																																							var40.aByte2472 = var28;
																																							var40.aClass94_2473 = var41;
																																							var40.anInt2478 = modelId;

																																							for(var33 = -1 + Node.clanSize; ~var33 <= -1; --var33) {
																																								clanChatIcon = Class3_Sub28_Sub15.aClass3_Sub19Array3694[var33].aClass94_2476.method1559(var40.aClass94_2476, var0 ^ 82);
																																								if(-1 == ~clanChatIcon) {
																																									Class3_Sub28_Sub15.aClass3_Sub19Array3694[var33].anInt2478 = modelId;
																																									Class3_Sub28_Sub15.aClass3_Sub19Array3694[var33].aByte2472 = var28;
																																									Class3_Sub28_Sub15.aClass3_Sub19Array3694[var33].aClass94_2473 = var41;
																																									if(~var2 == ~Class3_Sub13_Sub16.aLong3202) {
																																										Class91.aByte1308 = var28;
																																									}

																																									Class167.anInt2087 = Class3_Sub13_Sub17.anInt3213;
																																									RSString.incomingOpcode = -1;
																																									return true;
																																								}

																																								if(clanChatIcon < 0) {
																																									break;
																																								}
																																							}

																																							if(Class3_Sub28_Sub15.aClass3_Sub19Array3694.length <= Node.clanSize) {
																																								RSString.incomingOpcode = -1;
																																								return true;
																																							}

																																							for(clanChatIcon = Node.clanSize + -1; ~var33 > ~clanChatIcon; --clanChatIcon) {
																																								Class3_Sub28_Sub15.aClass3_Sub19Array3694[1 + clanChatIcon] = Class3_Sub28_Sub15.aClass3_Sub19Array3694[clanChatIcon];
																																							}

																																							if(-1 == ~Node.clanSize) {
																																								Class3_Sub28_Sub15.aClass3_Sub19Array3694 = new Class3_Sub19[100];
																																							}

																																							Class3_Sub28_Sub15.aClass3_Sub19Array3694[1 + var33] = var40;
																																							if(Class3_Sub13_Sub16.aLong3202 == var2) {
																																								Class91.aByte1308 = var28;
																																							}

																																							++Node.clanSize;
																																						} else {
																																							if(~Node.clanSize == -1) {
																																								RSString.incomingOpcode = -1;
																																								return true;
																																							}

																																							var2 &= Long.MAX_VALUE;

																																							for(var30 = 0; ~Node.clanSize < ~var30 && (var2 != Class3_Sub28_Sub15.aClass3_Sub19Array3694[var30].aLong71 || ~modelId != ~Class3_Sub28_Sub15.aClass3_Sub19Array3694[var30].anInt2478); ++var30) {
																																								;
																																							}

																																							if(var30 < Node.clanSize) {
																																								while(~(-1 + Node.clanSize) < ~var30) {
																																									Class3_Sub28_Sub15.aClass3_Sub19Array3694[var30] = Class3_Sub28_Sub15.aClass3_Sub19Array3694[1 + var30];
																																									++var30;
																																								}

																																								--Node.clanSize;
																																								Class3_Sub28_Sub15.aClass3_Sub19Array3694[Node.clanSize] = null;
																																							}
																																						}

																																						RSString.incomingOpcode = -1;
																																						Class167.anInt2087 = Class3_Sub13_Sub17.anInt3213;
																																						return true;
																																					}
																																				} else {
																																					nodeModelId = GraphicDefinition.incomingBuffer.getLEShort(-95);
																																					var19 = GraphicDefinition.incomingBuffer.getLEShort(-72);
																																					if(var19 == '\uffff') {
																																						var19 = -1;
																																					}

																																					modelId = GraphicDefinition.incomingBuffer.getInt();
																																					counter = GraphicDefinition.incomingBuffer.getShortA(23);
																																					var6 = GraphicDefinition.incomingBuffer.getIntA(-1);
																																					if(~counter == -65536) {
																																						counter = -1;
																																					}

																																					if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																																						for(var30 = counter; ~var19 <= ~var30; ++var30) {
																																							var36 = ((long)modelId << -1381724512) - -((long)var30);
																																							var47 = (Class3_Sub1)Class124.aClass130_1659.method1780(var36, 0);
																																							if(var47 == null) {
																																								if(-1 == var30) {
																																									var38 = new Class3_Sub1(var6, Class7.getRSInterface((byte)116, modelId).aClass3_Sub1_257.anInt2202);
																																								} else {
																																									var38 = new Class3_Sub1(var6, -1);
																																								}
																																							} else {
																																								var38 = new Class3_Sub1(var6, var47.anInt2202);
																																								var47.method86(-1024);
																																							}

																																							Class124.aClass130_1659.method1779(1, var38, var36);
																																						}
																																					}

																																					RSString.incomingOpcode = -1;
																																					return true;
																																				}
																																			} else {
																																				nodeModelId = GraphicDefinition.incomingBuffer.getInt();
																																				var19 = GraphicDefinition.incomingBuffer.getShort(1);
																																				if(69999 < ~nodeModelId) {
																																					var19 += '\u8000';
																																				}

																																				if(nodeModelId < 0) {
																																					var25 = null;
																																				} else {
																																					var25 = Class7.getRSInterface((byte)127, nodeModelId);
																																				}

																																				for(; ~GraphicDefinition.incomingBuffer.index > ~Class130.incomingPacketLength; Class168.method2277(var6 + -1, counter, var30, var19, (byte)46)) {
																																					counter = GraphicDefinition.incomingBuffer.getSmart(true);
																																					var6 = GraphicDefinition.incomingBuffer.getShort(1);
																																					var30 = 0;
																																					if(var6 != 0) {
																																						var30 = GraphicDefinition.incomingBuffer.getByte((byte)-52);
																																						if(-256 == ~var30) {
																																							var30 = GraphicDefinition.incomingBuffer.getInt();
																																						}
																																					}

																																					if(var25 != null && ~counter <= -1 && ~var25.itemAmounts.length < ~counter) {
																																						var25.itemAmounts[counter] = var6;
																																						var25.itemIds[counter] = var30;
																																					}
																																				}

																																				if(var25 != null) {
																																					Class20.method909(-128, var25);
																																				}

																																				Class3_Sub30_Sub1.method819(false);
																																				Class3_Sub28_Sub4.anIntArray3565[Class3_Sub28_Sub15.method633(Class62.anInt944++, 31)] = Class3_Sub28_Sub15.method633(32767, var19);
																																				RSString.incomingOpcode = -1;
																																				return true;
																																			}
																																		}
																																	} else {
																																		nodeModelId = GraphicDefinition.incomingBuffer.getShort(1);
																																		var19 = GraphicDefinition.incomingBuffer.getByte((byte)-102);
																																		if(-65536 == ~nodeModelId) {
																																			nodeModelId = -1;
																																		}

																																		modelId = GraphicDefinition.incomingBuffer.getShort(1);
																																		Class3_Sub13_Sub6.method199(var19, nodeModelId, modelId, -799);
																																		RSString.incomingOpcode = -1;
																																		return true;
																																	}
																																} else {//cc messahe
																																	var2 = GraphicDefinition.incomingBuffer.getLong(-122);
																																	GraphicDefinition.incomingBuffer.getByte();
																																	var4 = GraphicDefinition.incomingBuffer.getLong(-124);
																																	var29 = (long)GraphicDefinition.incomingBuffer.getShort(1);
																																	var36 = (long)GraphicDefinition.incomingBuffer.getTriByte((byte)81);
																																	long var44 = (var29 << -164903776) + var36;
																																	clanChatIcon = GraphicDefinition.incomingBuffer.getByte((byte)-40);
																																	boolean var13 = false;
																																	int var14 = 0;

																																	label1774:
																																		while(true) {
																																			if(var14 >= 100) {
																																				if(1 >= clanChatIcon) {
																																					if((!Class3_Sub15.aBoolean2433 || Class121.aBoolean1641) && !Class3_Sub13_Sub14.aBoolean3166) {
																																						for(var14 = 0; Class3_Sub28_Sub5.anInt3591 > var14; ++var14) {
																																							if(Class114.aLongArray1574[var14] == var2) {
																																								var13 = true;
																																								break label1774;
																																							}
																																						}
																																					} else {
																																						var13 = true;
																																					}
																																				}
																																				break;
																																			}

																																			if(Class163_Sub2_Sub1.aLongArray4017[var14] == var44) {
																																				var13 = true;
																																				break;
																																			}

																																			++var14;
																																		}

																																	if(!var13 && 0 == WorldListEntry.anInt2622) {
																																		Class163_Sub2_Sub1.aLongArray4017[Class149.anInt1921] = var44;
																																		Class149.anInt1921 = (Class149.anInt1921 + 1) % 100;
																																		var57 = Class3_Sub28_Sub17.method686(Class32.method992(GraphicDefinition.incomingBuffer, 29488).method1536(116));
																																		if(-3 != ~clanChatIcon && -4 != ~clanChatIcon) {
																																			if(~clanChatIcon == -2) {
																																				Class3_Sub13_Sub11.method221(-1, var57, RenderAnimationDefinition.method903(new RSString[]{Class32.aClass94_592, Class41.method1052(var0 ^ 29581, var2).method1545((byte)-50)}, (byte)-85), Class41.method1052(-29664, var4).method1545((byte)-50), 9);
																																			} else {
														
																																				Class3_Sub13_Sub11.method221(-1, var57, RenderAnimationDefinition.method903(new RSString[]{RSString.createRSString("<img=" + (clanChatIcon -1) + ">"), Class41.method1052(var0 ^ 29581, var2).method1545((byte)-50)}, (byte)-85), Class41.method1052(-29664, var4).method1545((byte)-50), 9);
																																				
																																			}
																																		} else {
																																			Class3_Sub13_Sub11.method221(-1, var57, RenderAnimationDefinition.method903(new RSString[]{Class21.aClass94_444, Class41.method1052(-29664, var2).method1545((byte)-50)}, (byte)-59), Class41.method1052(var0 + -29581, var4).method1545((byte)-50), 9);
																																		}
																																	}

																																	RSString.incomingOpcode = -1;
																																	return true;
																																}
																															} else {
																																if(null != Class3_Sub13_Sub10.aFrame3121) {
																																	GameObject.method1862(false, Node.anInt2577, -8914, -1, -1);
																																}

																																byte[] var22 = new byte[Class130.incomingPacketLength];
																																GraphicDefinition.incomingBuffer.method811((byte)30, 0, var22, Class130.incomingPacketLength);
																																var24 = Class3_Sub13_Sub3.method178(var22, -4114, Class130.incomingPacketLength, 0);
																																if(null == GameShell.frame && (3 == Signlink.anInt1214 || !Signlink.osName.startsWith("win") || Class106.hasInternetExplorer6)) {
																																	Class99.method1596(var24, (byte)127, true);
																																} else {
																																	Class3_Sub13_Sub24.aClass94_3295 = var24;
																																	RSString.aBoolean2154 = true;
																																	Class15.aClass64_351 = Class38.aClass87_665.method1452(new String(var24.method1568(0), "ISO-8859-1"), true);
																																}

																																RSString.incomingOpcode = -1;
																																return true;
																															}
																														} else {
																															Class3_Sub30_Sub1.method819(false);
																															Class149.anInt1925 = GraphicDefinition.incomingBuffer.getShort((byte)59);
																															Class140_Sub6.anInt2905 = Class3_Sub13_Sub17.anInt3213;
																															RSString.incomingOpcode = -1;
																															return true;
																														}
																													} else {
																														nodeModelId = GraphicDefinition.incomingBuffer.getLEShort(-59);
																														Class3_Sub28_Sub1.method532(nodeModelId, var0 ^ 28185);
																														Class3_Sub28_Sub4.anIntArray3565[Class3_Sub28_Sub15.method633(31, Class62.anInt944++)] = Class3_Sub28_Sub15.method633(nodeModelId, 32767);
																														RSString.incomingOpcode = -1;
																														return true;
																													}
																												} else {
																													nodeModelId = GraphicDefinition.incomingBuffer.getShort(var0 + 84);
																													var19 = GraphicDefinition.incomingBuffer.getByte((byte)-104);
																													modelId = GraphicDefinition.incomingBuffer.getByte((byte)-128);
																													counter = GraphicDefinition.incomingBuffer.getByte((byte)-102);
																													var6 = GraphicDefinition.incomingBuffer.getByte((byte)-81);
																													var30 = GraphicDefinition.incomingBuffer.getShort(1);
																													if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																														Class104.aBooleanArray2169[var19] = true;
																														Class3_Sub13_Sub32.anIntArray3383[var19] = modelId;
																														Class166.anIntArray2073[var19] = counter;
																														Class3_Sub13_Sub29.anIntArray3359[var19] = var6;
																														Class163_Sub1_Sub1.anIntArray4009[var19] = var30;
																													}

																													RSString.incomingOpcode = -1;
																													return true;
																												}
																											} else {
																												nodeModelId = GraphicDefinition.incomingBuffer.getIntA(var0 ^ 82);
																												var19 = GraphicDefinition.incomingBuffer.getShortA(19);
																												modelId = GraphicDefinition.incomingBuffer.getIntA(-1);
																												if(Class146.updateInterfacePacketCounter(var19, (byte)-25)) {
																													Class3_Sub31 var23 = (Class3_Sub31)Class3_Sub13_Sub17.aClass130_3208.method1780((long)nodeModelId, var0 ^ -83);
																													var26 = (Class3_Sub31)Class3_Sub13_Sub17.aClass130_3208.method1780((long)modelId, 0);
																													if(null != var26) {
																														Class3_Sub13_Sub18.method254(null == var23 || var26.anInt2602 != var23.anInt2602, var26, false);
																													}

																													if(null != var23) {
																														var23.method86(-1024);
																														Class3_Sub13_Sub17.aClass130_3208.method1779(1, var23, (long)modelId);
																													}

																													RSInterface var27 = Class7.getRSInterface((byte)113, nodeModelId);
																													if(var27 != null) {
																														Class20.method909(var0 + 57, var27);
																													}

																													var27 = Class7.getRSInterface((byte)114, modelId);
																													if(null != var27) {
																														Class20.method909(119, var27);
																														Class151_Sub1.method2104(var27, true, 48);
																													}

																													if(0 != ~Class3_Sub28_Sub12.anInt3655) {
																														Class3_Sub8.method124(28, 1, Class3_Sub28_Sub12.anInt3655);
																													}
																												}

																												RSString.incomingOpcode = -1;
																												return true;
																											}
																										}
																									} else {
																										for(nodeModelId = 0; ~Class163_Sub1.anIntArray2985.length < ~nodeModelId; ++nodeModelId) {
																											if(~Class57.anIntArray898[nodeModelId] != ~Class163_Sub1.anIntArray2985[nodeModelId]) {
																												Class163_Sub1.anIntArray2985[nodeModelId] = Class57.anIntArray898[nodeModelId];
																												Class46.method1087(98, nodeModelId);
																												Class44.anIntArray726[Class3_Sub28_Sub15.method633(Class36.anInt641++, 31)] = nodeModelId;
																											}
																										}

																										RSString.incomingOpcode = -1;
																										return true;
																									}
																								}
																							} else {
																								//set camera
																								nodeModelId = GraphicDefinition.incomingBuffer.getLEShort(-107);
																								var19 = GraphicDefinition.incomingBuffer.getShort(1);
																								modelId = GraphicDefinition.incomingBuffer.getShort(1);
																								if(Class146.updateInterfacePacketCounter(var19, (byte)-25)) {
																									GraphicDefinition.CAMERA_DIRECTION = nodeModelId;
																									Class3_Sub9.anInt2309 = modelId;
																									if(-3 == ~Class133.anInt1753) {
																										Class139.anInt1823 = Class3_Sub9.anInt2309;
																										Class3_Sub13_Sub25.anInt3315 = GraphicDefinition.CAMERA_DIRECTION;
																									}

																									Class47.method1098((byte)-117);
																								}

																								RSString.incomingOpcode = -1;
																								return true;
																							}
																						} else {
																							Class39.parseChunkPacket((byte)-99);
																							RSString.incomingOpcode = -1;
																							return true;
																						}
																					} else {
																						nodeModelId = GraphicDefinition.incomingBuffer.getIntB((byte)-87);
																						var19 = GraphicDefinition.incomingBuffer.getShortA(var0 + 92);
																						modelId = GraphicDefinition.incomingBuffer.getShort(var0 ^ -84);
																						counter = GraphicDefinition.incomingBuffer.getShortA(-113);
																						if(Class146.updateInterfacePacketCounter(var19, (byte)-25)) {
																							Class114.method1708(counter + (modelId << 16), nodeModelId, var0 ^ 2474);
																						}

																						RSString.incomingOpcode = -1;
																						return true;
																					}
																				}
																			}
																		} else {
																			Class162.method2204(GraphicDefinition.incomingBuffer, var0 ^ -43);
																			RSString.incomingOpcode = -1;
																			return true;
																		}
																	} else {
																		nodeModelId = GraphicDefinition.incomingBuffer.getLEShortA((byte)-113);
																		var19 = GraphicDefinition.incomingBuffer.getInt();
																		modelId = GraphicDefinition.incomingBuffer.getShortA(-107);
																		if(Class146.updateInterfacePacketCounter(nodeModelId, (byte)-25)) {
																			Class3_Sub13_Sub18.method255(modelId, var19, 1);
																		}

																		RSString.incomingOpcode = -1;
																		return true;
																	}
																} else {
																	nodeModelId = GraphicDefinition.incomingBuffer.getInt();
																	var19 = GraphicDefinition.incomingBuffer.getShortA(-112);
																	Class3_Sub13_Sub23.method281((byte)99, nodeModelId, var19);
																	RSString.incomingOpcode = -1;
																	return true;
																}
															}
														} else {
															Class163_Sub3.renderPlayers((byte)-122);
															RSString.incomingOpcode = -1;
															return true;
														}
													} else {
														Class167.anInt2087 = Class3_Sub13_Sub17.anInt3213;
														var2 = GraphicDefinition.incomingBuffer.getLong(-110);
														if(~var2 == -1L) {
															Class161.aClass94_2035 = null;
															RSString.incomingOpcode = -1;
															RSInterface.aClass94_251 = null;
															Class3_Sub28_Sub15.aClass3_Sub19Array3694 = null;
															Node.clanSize = 0;
															return true;
														} else {
															var4 = GraphicDefinition.incomingBuffer.getLong(-126);
															RSInterface.aClass94_251 = Class41.method1052(-29664, var4);
															Class161.aClass94_2035 = Class41.method1052(var0 + -29581, var2);
															Player.aByte3953 = GraphicDefinition.incomingBuffer.getByte();
															var6 = GraphicDefinition.incomingBuffer.getByte((byte)-86);
															if(255 == var6) {
																RSString.incomingOpcode = -1;
																return true;
															} else {
																Node.clanSize = var6;
																Class3_Sub19[] var7 = new Class3_Sub19[100];

																for(chatIcon = 0; ~Node.clanSize < ~chatIcon; ++chatIcon) {
																	var7[chatIcon] = new Class3_Sub19();
																	var7[chatIcon].aLong71 = GraphicDefinition.incomingBuffer.getLong(var0 + 4);
																	var7[chatIcon].aClass94_2476 = Class41.method1052(var0 + -29581, var7[chatIcon].aLong71);
																	var7[chatIcon].anInt2478 = GraphicDefinition.incomingBuffer.getShort(1);
																	var7[chatIcon].aByte2472 = GraphicDefinition.incomingBuffer.getByte();
																	var7[chatIcon].aClass94_2473 = GraphicDefinition.incomingBuffer.getString();
																	if(~Class3_Sub13_Sub16.aLong3202 == ~var7[chatIcon].aLong71) {
																		Class91.aByte1308 = var7[chatIcon].aByte2472;
																	}
																}

																var32 = false;
																clanChatIcon = Node.clanSize;

																while(-1 > ~clanChatIcon) {
																	var32 = true;
																	--clanChatIcon;

																	for(var11 = 0; ~clanChatIcon < ~var11; ++var11) {
																		if(-1 > ~var7[var11].aClass94_2476.method1559(var7[var11 - -1].aClass94_2476, var0 ^ 82)) {
																			var32 = false;
																			Class3_Sub19 var9 = var7[var11];
																			var7[var11] = var7[1 + var11];
																			var7[var11 + 1] = var9;
																		}
																	}

																	if(var32) {
																		break;
																	}
																}

																Class3_Sub28_Sub15.aClass3_Sub19Array3694 = var7;
																RSString.incomingOpcode = -1;
																return true;
															}
														}
													}
												}
											}
										}
									} else {
										Class107.currentChunkY = GraphicDefinition.incomingBuffer.getByteA((byte)-88);
										Class65.currentChunkX = GraphicDefinition.incomingBuffer.getByteS(true);

										while(~Class130.incomingPacketLength < ~GraphicDefinition.incomingBuffer.index) {
											RSString.incomingOpcode = GraphicDefinition.incomingBuffer.getByte((byte)-60);
											Class39.parseChunkPacket((byte)-82);
										}

										RSString.incomingOpcode = -1;
										return true;
									}
								}
							}
						}
					}
				}
			}
		} catch (RuntimeException var19) {
			throw Class44.method1067(var19, "ac.C(" + var0 + ')');
		}
	}

	public static void method828(int var0) {
		try {
			aClass16_84 = null;
			aByteArrayArrayArray81 = (byte[][][])null;
			aClass94_85 = null;
			if(var0 > -88) {
				method828(-84);
			}

			aClass61_82 = null;
			aClass11_88 = null;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "ac.A(" + var0 + ')');
		}
	}

	static final void method829(int var0) {
		try {
			Class20.method909(var0 + 111, Class56.aClass11_886);
			++Class75_Sub3.anInt2658;
			if(Class21.aBoolean440 && Class85.aBoolean1167) {
				int var1 = Class126.anInt1676;
				var1 -= Class144.anInt1881;
				if(Class3_Sub13_Sub13.anInt3156 > var1) {
					var1 = Class3_Sub13_Sub13.anInt3156;
				}

				int var2 = Class130.anInt1709;
				if(~(Class3_Sub13_Sub13.anInt3156 + aClass11_88.anInt168) > ~(var1 - -Class56.aClass11_886.anInt168)) {
					var1 = -Class56.aClass11_886.anInt168 + Class3_Sub13_Sub13.anInt3156 + aClass11_88.anInt168;
				}

				var2 -= Class95.anInt1336;
				if(~var2 > ~Class134.anInt1761) {
					var2 = Class134.anInt1761;
				}

				if(Class134.anInt1761 - -aClass11_88.anInt193 < var2 - -Class56.aClass11_886.anInt193) {
					var2 = Class134.anInt1761 + aClass11_88.anInt193 + -Class56.aClass11_886.anInt193;
				}

				if(var0 != -1) {
					aClass61_82 = (Class61)null;
				}

				int var4 = var2 - Class3_Sub2.anInt2218;
				int var3 = var1 + -Class3_Sub15.anInt2421;
				int var6 = var1 + -Class3_Sub13_Sub13.anInt3156 + aClass11_88.anInt247;
				int var7 = aClass11_88.anInt208 + -Class134.anInt1761 + var2;
				int var5 = Class56.aClass11_886.anInt214;
				if(~Class75_Sub3.anInt2658 < ~Class56.aClass11_886.anInt179 && (~var5 > ~var3 || ~(-var5) < ~var3 || var4 > var5 || var4 < -var5)) {
					NPC.aBoolean3975 = true;
				}

				CS2Script var8;
				if(Class56.aClass11_886.anObjectArray295 != null && NPC.aBoolean3975) {
					var8 = new CS2Script();
					var8.aClass11_2449 = Class56.aClass11_886;
					var8.arguments = Class56.aClass11_886.anObjectArray295;
					var8.anInt2447 = var6;
					var8.anInt2441 = var7;
					Class43.method1065(1073376993, var8);
				}

				if(0 == Class3_Sub13_Sub5.anInt3069) {
					if(NPC.aBoolean3975) {
						if(Class56.aClass11_886.anObjectArray229 != null) {
							var8 = new CS2Script();
							var8.anInt2441 = var7;
							var8.aClass11_2438 = Class27.aClass11_526;
							var8.anInt2447 = var6;
							var8.arguments = Class56.aClass11_886.anObjectArray229;
							var8.aClass11_2449 = Class56.aClass11_886;
							Class43.method1065(1073376993, var8);
						}
						RSInterface withInter = Class27.aClass11_526;
						if (withInter == null) {
							withInter = Class56.aClass11_886;
						}
						Class3_Sub13_Sub1.outgoingBuffer.putOpcode(79);
						++Class23.anInt456;
						Class3_Sub13_Sub1.outgoingBuffer.putIntB(-93, Class56.aClass11_886.anInt279);
						Class3_Sub13_Sub1.outgoingBuffer.putLEShort(var0 ^ 0, withInter.anInt191);
						Class3_Sub13_Sub1.outgoingBuffer.putInt(-125, withInter.anInt279);
						Class3_Sub13_Sub1.outgoingBuffer.putLEShort(-1, Class56.aClass11_886.anInt191);
					
						if(Class27.aClass11_526 != null) {// && client.method42(Class56.aClass11_886) != null) {
							if (Client.method42(Class56.aClass11_886) != null) {
								System.out.println("Shouldn't be sending packet, enabled to fix banking tabs though.");
							}
						}
						else {
							System.out.println("Could not send switch item packet, second interface is null!");
						}
					} else if((-2 == ~Class66.anInt998 || Class3_Sub13_Sub39.method353(-1 + Class3_Sub13_Sub34.anInt3415, ~var0)) && Class3_Sub13_Sub34.anInt3415 > 2) {
						Class132.method1801((byte)-97);
					} else if(~Class3_Sub13_Sub34.anInt3415 < -1) {
						Class3_Sub13_Sub8.method203(96);
					}

					Class56.aClass11_886 = null;
				}

			} else {
				if(-2 > ~Class75_Sub3.anInt2658) {
					Class56.aClass11_886 = null;
				}

			}
		} catch (RuntimeException var9) {
			throw Class44.method1067(var9, "ac.F(" + var0 + ')');
		}
	}

}
