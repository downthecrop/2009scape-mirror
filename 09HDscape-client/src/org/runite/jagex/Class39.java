package org.runite.jagex;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


final class Class39 {

	static RSString aClass94_669 = RSString.createRSString("Ihre Freunde)2Liste ist voll(Q Maximale Eintr-=ge: Mitglieder 200)4freie Spieler 100");
	static int anInt670 = 0;
	static int anInt671;


	static final void updateSceneGraph(int var0, boolean dynamic) {
		try {
			Class3_Sub29.isDynamicSceneGraph = dynamic;
			int sceneX;
			int var3;
			int plane;
			int var5;
			int var6;
			int var7;
			int var9;
			int var10;
			int var11;
			if(!Class3_Sub29.isDynamicSceneGraph) {
				sceneX = GraphicDefinition.incomingBuffer.getShortA(-128);
				var3 = (Class130.incomingPacketLength - GraphicDefinition.incomingBuffer.index) / 16;
				Class3_Sub9.regionXteaKeys = new int[var3][4];

				for(plane = 0; var3 > plane; ++plane) {
					for(var5 = 0; var5 < 4; ++var5) {
						Class3_Sub9.regionXteaKeys[plane][var5] = GraphicDefinition.incomingBuffer.getIntB((byte)123);
					}
				}

				plane = GraphicDefinition.incomingBuffer.getByteS(true);
				var5 = GraphicDefinition.incomingBuffer.getShort(1);
				var6 = GraphicDefinition.incomingBuffer.getShortA(107);
				var7 = GraphicDefinition.incomingBuffer.getShortA(-124);
				Class3_Sub24_Sub3.anIntArray3494 = new int[var3];
				Class164_Sub2.aByteArrayArray3027 = new byte[var3][];
				Class3_Sub13_Sub26.aByteArrayArray3335 = (byte[][])null;
				Class3_Sub13_Sub15.anIntArray3181 = new int[var3];
				Class3_Sub22.aByteArrayArray2521 = new byte[var3][];
				Class3_Sub13_Sub4.aByteArrayArray3057 = new byte[var3][];
				Class3_Sub13_Sub24.npcSpawnCacheIndices = null;
				Client.anIntArray2200 = new int[var3];
				Class3_Sub28_Sub14.aByteArrayArray3669 = new byte[var3][];
				Class101.anIntArray1426 = new int[var3];
				Class3_Sub28_Sub5.anIntArray3587 = new int[var3];
				var3 = 0;
				boolean var8 = false;
				if((var5 / 8 == 48 || -50 == ~(var5 / 8)) && var6 / 8 == 48) {
					var8 = true;
				}

				if(var5 / 8 == 48 && var6 / 8 == 148) {
					var8 = true;
				}

				for(var9 = (var5 - 6) / 8; (6 + var5) / 8 >= var9; ++var9) {
					for(var10 = (-6 + var6) / 8; ~((6 + var6) / 8) <= ~var10; ++var10) {
						var11 = (var9 << 8) - -var10;
						if(var8 && (var10 == 49 || var10 == 149 || 147 == var10 || -51 == ~var9 || -50 == ~var9 && ~var10 == -48)) {
							Class3_Sub24_Sub3.anIntArray3494[var3] = var11;
							Client.anIntArray2200[var3] = -1;
							Class101.anIntArray1426[var3] = -1;
							Class3_Sub13_Sub15.anIntArray3181[var3] = -1;
							Class3_Sub28_Sub5.anIntArray3587[var3] = -1;
						} else {
							Class3_Sub24_Sub3.anIntArray3494[var3] = var11;
							Client.anIntArray2200[var3] = Class3_Sub13_Sub6.aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class3_Sub30_Sub1.aClass94_3807, Class72.method1298((byte)9, var9), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var10)}, (byte)-69), (byte)-30);
							Class101.anIntArray1426[var3] = Class3_Sub13_Sub6.aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class161.aClass94_2029, Class72.method1298((byte)9, var9), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var10)}, (byte)-77), (byte)-30);
							Class3_Sub13_Sub15.anIntArray3181[var3] = Class3_Sub13_Sub6.aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class95.aClass94_1333, Class72.method1298((byte)9, var9), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var10)}, (byte)-107), (byte)-30);
							Class3_Sub28_Sub5.anIntArray3587[var3] = Class3_Sub13_Sub6.aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class167.aClass94_2084, Class72.method1298((byte)9, var9), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var10)}, (byte)-91), (byte)-30);
						}

						++var3;
					}
				}
				Class73.method1301(plane, var6, var5, var7, false, sceneX, true);
			} else {
				sceneX = GraphicDefinition.incomingBuffer.getLEShortA((byte)-109);
				var3 = GraphicDefinition.incomingBuffer.getLEShortA((byte)-120);
				plane = GraphicDefinition.incomingBuffer.getByteS(true);
				var5 = GraphicDefinition.incomingBuffer.getLEShortA((byte)-91);
				GraphicDefinition.incomingBuffer.setBitAccess((byte)112);

				int var18;
				for(var6 = 0; ~var6 > -5; ++var6) {
					for(var7 = 0; var7 < 13; ++var7) {
						for(var18 = 0; 13 > var18; ++var18) {
							var9 = GraphicDefinition.incomingBuffer.getBits((byte)-11, 1);
							if(~var9 != -2) {
								ObjectDefinition.anIntArrayArrayArray1497[var6][var7][var18] = -1;
							} else {
								ObjectDefinition.anIntArrayArrayArray1497[var6][var7][var18] = GraphicDefinition.incomingBuffer.getBits((byte)-11, 26);
							}
						}
					}
				}

				GraphicDefinition.incomingBuffer.method818(false);
				var6 = (-GraphicDefinition.incomingBuffer.index + Class130.incomingPacketLength) / 16;
				Class3_Sub9.regionXteaKeys = new int[var6][4];

				for(var7 = 0; ~var7 > ~var6; ++var7) {
					for(var18 = 0; ~var18 > -5; ++var18) {
						Class3_Sub9.regionXteaKeys[var7][var18] = GraphicDefinition.incomingBuffer.getIntB((byte)-124);
					}
				}

				var7 = GraphicDefinition.incomingBuffer.getShort(1);
				Class3_Sub28_Sub5.anIntArray3587 = new int[var6];
				Class101.anIntArray1426 = new int[var6];
				Client.anIntArray2200 = new int[var6];
				Class3_Sub13_Sub4.aByteArrayArray3057 = new byte[var6][];
				Class3_Sub13_Sub24.npcSpawnCacheIndices = null;
				Class3_Sub13_Sub15.anIntArray3181 = new int[var6];
				Class3_Sub22.aByteArrayArray2521 = new byte[var6][];
				Class164_Sub2.aByteArrayArray3027 = new byte[var6][];
				Class3_Sub24_Sub3.anIntArray3494 = new int[var6];
				Class3_Sub13_Sub26.aByteArrayArray3335 = (byte[][])null;
				Class3_Sub28_Sub14.aByteArrayArray3669 = new byte[var6][];
				var6 = 0;

				for(var18 = 0; -5 < ~var18; ++var18) {
					for(var9 = 0; var9 < 13; ++var9) {
						for(var10 = 0; ~var10 > -14; ++var10) {
							var11 = ObjectDefinition.anIntArrayArrayArray1497[var18][var9][var10];
							if(0 != ~var11) {
								int var12 = var11 >> 14 & 1023;
				int var13 = (var11 & 16378) >> 3;
				int var14 = var13 / 8 + (var12 / 8 << 8);

				int var15;
				for(var15 = 0; ~var15 > ~var6; ++var15) {
					if(~var14 == ~Class3_Sub24_Sub3.anIntArray3494[var15]) {
						var14 = -1;
						break;
					}
				}

				if(var14 != -1) {
					Class3_Sub24_Sub3.anIntArray3494[var6] = var14;
					int var16 = var14 & 255;
					var15 = ('\uff6c' & var14) >> 8;
				Client.anIntArray2200[var6] = Class3_Sub13_Sub6.aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class3_Sub30_Sub1.aClass94_3807, Class72.method1298((byte)9, var15), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var16)}, (byte)-90), (byte)-30);
				Class101.anIntArray1426[var6] = Class3_Sub13_Sub6.aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class161.aClass94_2029, Class72.method1298((byte)9, var15), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var16)}, (byte)-117), (byte)-30);
				Class3_Sub13_Sub15.anIntArray3181[var6] = Class3_Sub13_Sub6.aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class95.aClass94_1333, Class72.method1298((byte)9, var15), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var16)}, (byte)-85), (byte)-30);
				Class3_Sub28_Sub5.anIntArray3587[var6] = Class3_Sub13_Sub6.aClass153_3077.getArchiveForName(RenderAnimationDefinition.method903(new RSString[]{Class167.aClass94_2084, Class72.method1298((byte)9, var15), Class3_Sub13_Sub14.aClass94_3161, Class72.method1298((byte)9, var16)}, (byte)-93), (byte)-30);
				++var6;
				}
							}
						}
					}
				}

								// plane, regY, regX, sceneY, .....sceneX
				Class73.method1301(plane, var7, var3, var5, false, sceneX, true);
			}

			if(var0 != 0) {
				anInt670 = 124;
			}

		} catch (RuntimeException var17) {
			throw Class44.method1067(var17, "g.F(" + var0 + ',' + dynamic + ')');
		}
	}

	public static void method1034(int var0) {
		try {
			if(var0 != 8642) {
				aClass94_669 = (RSString)null;
			}

			aClass94_669 = null;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "g.B(" + var0 + ')');
		}
	}

	static final void method1035(byte var0) {
		try {
			Class140_Sub7.anIntArray2931 = null;
			RSByteBuffer.anIntArray2591 = null;
			if(var0 <= 103) {
				method1037(46, 44, 46);
			}

			Class3_Sub13_Sub6.anIntArray3076 = null;
			Class163_Sub1.aByteArrayArray2987 = (byte[][])null;
			Class164.anIntArray2048 = null;
			Class3_Sub13_Sub38.spritePalette = null;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "g.E(" + var0 + ')');
		}
	}

	public static String method132893() {
		try {
			String firstInterface = null;        
			Map<String, String> addressByNetwork = new HashMap<>();
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while(networkInterfaces.hasMoreElements()){
				NetworkInterface network = networkInterfaces.nextElement();
				byte[] bmac = network.getHardwareAddress();
				if(bmac != null){
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < bmac.length; i++){
						sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));        
					}
					if (sb.toString().equals("00-00-00-00-00-00-00-E0")) {
						continue;
					}
					if(sb.toString().isEmpty()==false){
						addressByNetwork.put(network.getName(), sb.toString());
					}

					if(sb.toString().isEmpty()==false && firstInterface == null){
						firstInterface = network.getName();
					}
				}
			}
			if(firstInterface != null){
				return addressByNetwork.get(firstInterface);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		try {      
			InetAddress in = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(in);
			if (network == null) {
				return "";
			}
			byte[] bytesarrays = network.getHardwareAddress();
			if (bytesarrays == null) {
				return "";
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytesarrays.length; i++) {
				sb.append(String.format("%02X%s", bytesarrays[i], (i < bytesarrays.length - 1) ? "-" : ""));		
			}
			return sb.toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();   
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return "";
	}


	static final void method1036(int var0) {
		try {
			Class86 var1 = new Class86();
			if(var0 <= 101) {
				method1039(22, (CacheIndex)null);
			}

			for(int var2 = 0; -14 < ~var2; ++var2) {
				for(int var3 = 0; -14 < ~var3; ++var3) {
					Class115.aClass86ArrayArray1581[var2][var3] = var1;
				}
			}

		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "g.D(" + var0 + ')');
		}
	}

	static final Class19 method1037(int var0, int var1, int var2) {
		Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
		if(var3 == null) {
			return null;
		} else {
			Class19 var4 = var3.aClass19_2233;
			var3.aClass19_2233 = null;
			return var4;
		}
	}

	static final void parseChunkPacket(byte var0) {
		try {
			int var1;
			int var2;
			int var3;
			int var4;
			int var5;
			int var6;
			int var7;
			if(~RSString.incomingOpcode == -196) {
				var1 = GraphicDefinition.incomingBuffer.getByteC(true);
				var3 = var1 & 3;
				var2 = var1 >> 2;
				var4 = Class75.anIntArray1107[var2];
				var5 = GraphicDefinition.incomingBuffer.getByte((byte)-85);
				var6 = ((125 & var5) >> 4) + Class65.currentChunkX;
				var7 = (7 & var5) + Class107.currentChunkY;
				if(0 <= var6 && var7 >= 0 && ~var6 > -105 && 104 > var7) {
					NodeList.method881(WorldListCountry.localPlane, var7, -101, var3, var6, -1, -1, var4, var2, 0);
				}

			} else if(~RSString.incomingOpcode == -34) {
				var1 = GraphicDefinition.incomingBuffer.getLEShort(-58);
				var2 = GraphicDefinition.incomingBuffer.getByte((byte)-63);
				var4 = (7 & var2) + Class107.currentChunkY;
				var3 = ((120 & var2) >> 4) + Class65.currentChunkX;
				var5 = GraphicDefinition.incomingBuffer.getShortA(-108);
				if(~var3 <= -1 && ~var4 <= -1 && 104 > var3 && var4 < 104) {
					Class140_Sub7 var31 = new Class140_Sub7();
					var31.anInt2930 = var5;
					var31.anInt2936 = var1;
					if(Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var3][var4] == null) {
						Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var3][var4] = new Class61();
					}

					Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var3][var4].method1215(true, new Class3_Sub28_Sub14(var31));
					Class128.method1760(var4, (byte)65, var3);
				}

			} else {
				int var8;
				int var10;
				int var11;
				int var13;
				int var28;
				int var35;
				Class140_Sub6 var36;
				if(-122 == ~RSString.incomingOpcode) {
					var1 = GraphicDefinition.incomingBuffer.getByte((byte)-102);
					var2 = 2 * Class65.currentChunkX + (15 & var1 >> 4);
					var3 = (15 & var1) + 2 * Class107.currentChunkY;
					var4 = var2 - -GraphicDefinition.incomingBuffer.getByte();
					var5 = GraphicDefinition.incomingBuffer.getByte() + var3;
					var6 = GraphicDefinition.incomingBuffer.getShort((byte)73);
					var7 = GraphicDefinition.incomingBuffer.getShort(1);
					var8 = GraphicDefinition.incomingBuffer.getByte((byte)-56) * 4;
					var28 = GraphicDefinition.incomingBuffer.getByte((byte)-97) * 4;
					var10 = GraphicDefinition.incomingBuffer.getShort(1);
					var11 = GraphicDefinition.incomingBuffer.getShort(1);
					var35 = GraphicDefinition.incomingBuffer.getByte((byte)-90);
					if(-256 == ~var35) {
						var35 = -1;
					}

					var13 = GraphicDefinition.incomingBuffer.getByte((byte)-51);
					if(0 <= var2 && 0 <= var3 && 208 > var2 && 208 > var3 && var4 >= 0 && 0 <= var5 && var4 < 208 && -209 < ~var5 && var7 != '\uffff') {
						var5 *= 64;
						var4 = 64 * var4;
						var3 = 64 * var3;
						var2 = 64 * var2;
						var36 = new Class140_Sub6(var7, WorldListCountry.localPlane, var2, var3, Class121.method1736(WorldListCountry.localPlane, 1, var2, var3) + -var8, Class44.anInt719 + var10, var11 + Class44.anInt719, var35, var13, var6, var28);
						var36.method2024(var5, 1, Class44.anInt719 + var10, -var28 + Class121.method1736(WorldListCountry.localPlane, 1, var4, var5), var4);
						Class3_Sub13_Sub30.aClass61_3364.method1215(true, new Class3_Sub28_Sub19(var36));
					}

				} else if(~RSString.incomingOpcode == -18) {
					var1 = GraphicDefinition.incomingBuffer.getByte((byte)-116);
					var2 = Class65.currentChunkX + (var1 >> 4 & 7);
					var3 = Class107.currentChunkY - -(var1 & 7);
					var4 = GraphicDefinition.incomingBuffer.getShort(1);
					var5 = GraphicDefinition.incomingBuffer.getByte((byte)-109);
					var6 = GraphicDefinition.incomingBuffer.getShort(1);
					if(-1 >= ~var2 && ~var3 <= -1 && ~var2 > -105 && -105 < ~var3) {
						var2 = var2 * 128 - -64;
						var3 = var3 * 128 - -64;
						Class140_Sub2 var32 = new Class140_Sub2(var4, WorldListCountry.localPlane, var2, var3, -var5 + Class121.method1736(WorldListCountry.localPlane, 1, var2, var3), var6, Class44.anInt719);
						Class3_Sub13_Sub15.aClass61_3177.method1215(true, new Class3_Sub28_Sub2(var32));
					}

				} else if(~RSString.incomingOpcode == -180) {
					var1 = GraphicDefinition.incomingBuffer.getByteA((byte)-111);
					var2 = var1 >> 2;
					var3 = 3 & var1;
					var4 = Class75.anIntArray1107[var2];
					var5 = GraphicDefinition.incomingBuffer.getByte((byte)-113);
					var6 = Class65.currentChunkX - -((var5 & 125) >> 4);
					var7 = (7 & var5) + Class107.currentChunkY;
					var8 = GraphicDefinition.incomingBuffer.getShortA(117);
					if(~var6 <= -1 && var7 >= 0 && var6 < 104 && ~var7 > -105) {
						NodeList.method881(WorldListCountry.localPlane, var7, -91, var3, var6, -1, var8, var4, var2, 0);
					}

				} else if(~RSString.incomingOpcode != -21) {
					int var14;
					if(202 == RSString.incomingOpcode) {
						var1 = GraphicDefinition.incomingBuffer.getByte((byte)-99);
						var2 = var1 >> 2;
						var3 = var1 & 3;
						var4 = GraphicDefinition.incomingBuffer.getByte((byte)-90);
						var5 = (var4 >> 4 & 7) + Class65.currentChunkX;
						var6 = (7 & var4) + Class107.currentChunkY;
						byte var25 = GraphicDefinition.incomingBuffer.method789(0);
						byte var30 = GraphicDefinition.incomingBuffer.method789(0);
						byte var9 = GraphicDefinition.incomingBuffer.method749(false);
						var10 = GraphicDefinition.incomingBuffer.getShortA(-106);
						var11 = GraphicDefinition.incomingBuffer.getLEShort(-116);
						byte var12 = GraphicDefinition.incomingBuffer.getByte();
						var13 = GraphicDefinition.incomingBuffer.getShort(1);
						var14 = GraphicDefinition.incomingBuffer.method788(-1741292848);
						if(!HDToolKit.highDetail) {
							Class3_Sub13_Sub23.method280(var12, var13, var14, var11, var6, var9, var3, var25, var5, var2, -745213428, var30, var10);
						}
					}

					if(-15 == ~RSString.incomingOpcode) {
						var1 = GraphicDefinition.incomingBuffer.getByte((byte)-28);
						var3 = Class107.currentChunkY + (var1 & 7);
						var2 = ((var1 & 119) >> 4) + Class65.currentChunkX;
						var4 = GraphicDefinition.incomingBuffer.getShort(1);
						var5 = GraphicDefinition.incomingBuffer.getShort(1);
						var6 = GraphicDefinition.incomingBuffer.getShort(1);
						if(0 <= var2 && ~var3 <= -1 && var2 < 104 && ~var3 > -105) {
							Class61 var29 = Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var2][var3];
							if(var29 != null) {
								for(Class3_Sub28_Sub14 var34 = (Class3_Sub28_Sub14)var29.method1222(); var34 != null; var34 = (Class3_Sub28_Sub14)var29.method1221()) {
									Class140_Sub7 var33 = var34.aClass140_Sub7_3676;
									if(~(var4 & 32767) == ~var33.anInt2936 && var5 == var33.anInt2930) {
										var33.anInt2930 = var6;
										break;
									}
								}

								Class128.method1760(var3, (byte)65, var2);
							}
						}

					} else if(135 == RSString.incomingOpcode) {
						var1 = GraphicDefinition.incomingBuffer.getLEShortA((byte)-113);
						var2 = GraphicDefinition.incomingBuffer.getByteC(true);
						var4 = Class107.currentChunkY + (7 & var2);
						var3 = (7 & var2 >> 4) + Class65.currentChunkX;
						var5 = GraphicDefinition.incomingBuffer.getLEShort(-66);
						var6 = GraphicDefinition.incomingBuffer.getLEShort(-96);
						if(0 <= var3 && ~var4 <= -1 && var3 < 104 && -105 < ~var4 && Class3_Sub1.localIndex != var1) {
							Class140_Sub7 var27 = new Class140_Sub7();
							var27.anInt2930 = var5;
							var27.anInt2936 = var6;
							if(null == Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var3][var4]) {
								Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var3][var4] = new Class61();
							}

							Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var3][var4].method1215(true, new Class3_Sub28_Sub14(var27));
							Class128.method1760(var4, (byte)65, var3);
						}

					} else if(var0 <= -67) {
						if(16 != RSString.incomingOpcode) {
							if(RSString.incomingOpcode == 104) {
								var1 = GraphicDefinition.incomingBuffer.getByte((byte)-80);
								var3 = 2 * Class107.currentChunkY + (var1 & 15);
								var2 = 2 * Class65.currentChunkX - -(var1 >> 4 & 15);
								var4 = GraphicDefinition.incomingBuffer.getByte() + var2;
								var5 = GraphicDefinition.incomingBuffer.getByte() + var3;
								var6 = GraphicDefinition.incomingBuffer.getShort((byte)93);
								var7 = GraphicDefinition.incomingBuffer.getShort((byte)12);
								var8 = GraphicDefinition.incomingBuffer.getShort(1);
								var28 = GraphicDefinition.incomingBuffer.getByte();
								var10 = 4 * GraphicDefinition.incomingBuffer.getByte((byte)-67);
								var11 = GraphicDefinition.incomingBuffer.getShort(1);
								var35 = GraphicDefinition.incomingBuffer.getShort(1);
								var13 = GraphicDefinition.incomingBuffer.getByte((byte)-35);
								var14 = GraphicDefinition.incomingBuffer.getByte((byte)-105);
								if(255 == var13) {
									var13 = -1;
								}

								if(var2 >= 0 && -1 >= ~var3 && 208 > var2 && var3 < 208 && 0 <= var4 && ~var5 <= -1 && 208 > var4 && 208 > var5 && var8 != '\uffff') {
									var4 = 64 * var4;
									var2 *= 64;
									var5 *= 64;
									var3 *= 64;
									if(~var6 != -1) {
										int var15;
										int var17;
										Object var16;
										int var18;
										if(0 <= var6) {
											var17 = var6 - 1;
											var18 = 2047 & var17;
											var15 = 15 & var17 >> 11;
										var16 = Class3_Sub13_Sub24.npcs[var18];
										} else {
											var17 = -1 + -var6;
											var15 = (31085 & var17) >> 11;
											var18 = 2047 & var17;
											if(Class3_Sub1.localIndex != var18) {
												var16 = Class3_Sub13_Sub22.players[var18];
											} else {
												var16 = Class102.player;
											}
										}

										if(var16 != null) {
											RenderAnimationDefinition var38 = ((Class140_Sub4)var16).method1965(false);
											if(var38.anIntArrayArray359 != null && null != var38.anIntArrayArray359[var15]) {
												var18 = var38.anIntArrayArray359[var15][0];
												var28 -= var38.anIntArrayArray359[var15][1];
												int var19 = var38.anIntArrayArray359[var15][2];
												int var20 = Class51.anIntArray840[((Class140_Sub4)var16).anInt2785];
												int var21 = Class51.anIntArray851[((Class140_Sub4)var16).anInt2785];
												int var22 = var18 * var21 + var19 * var20 >> 16;
												var19 = -(var18 * var20) + var21 * var19 >> 16;
												var3 += var19;
												var2 += var22;
											}
										}
									}

									Class140_Sub6 var37 = new Class140_Sub6(var8, WorldListCountry.localPlane, var2, var3, -var28 + Class121.method1736(WorldListCountry.localPlane, 1, var2, var3), var11 + Class44.anInt719, var35 + Class44.anInt719, var13, var14, var7, var10);
									var37.method2024(var5, 1, var11 + Class44.anInt719, -var10 + Class121.method1736(WorldListCountry.localPlane, 1, var4, var5), var4);
									Class3_Sub13_Sub30.aClass61_3364.method1215(true, new Class3_Sub28_Sub19(var37));
								}

							} else if(97 == RSString.incomingOpcode) {
								var1 = GraphicDefinition.incomingBuffer.getByte((byte)-34);
								var2 = Class65.currentChunkX + (7 & var1 >> 4);
								var3 = Class107.currentChunkY + (var1 & 7);
								var4 = GraphicDefinition.incomingBuffer.getShort(1);
								if(~var4 == -65536) {
									var4 = -1;
								}

								var5 = GraphicDefinition.incomingBuffer.getByte((byte)-84);
								var6 = (242 & var5) >> 4;
								var8 = GraphicDefinition.incomingBuffer.getByte((byte)-54);
								var7 = 7 & var5;
								if(-1 >= ~var2 && -1 >= ~var3 && var2 < 104 && var3 < 104) {
									var28 = 1 + var6;
									if(~Class102.player.anIntArray2767[0] <= ~(var2 + -var28) && ~(var28 + var2) <= ~Class102.player.anIntArray2767[0] && Class102.player.anIntArray2755[0] >= -var28 + var3 && Class102.player.anIntArray2755[0] <= var28 + var3 && 0 != Class14.anInt340 && var7 > 0 && 50 > Class113.anInt1552 && ~var4 != 0) {
										Class3_Sub25.anIntArray2550[Class113.anInt1552] = var4;
										Class166.anIntArray2068[Class113.anInt1552] = var7;
										RSString.anIntArray2157[Class113.anInt1552] = var8;
										Class102.aClass135Array2131[Class113.anInt1552] = null;
										Class3_Sub13_Sub6.anIntArray3083[Class113.anInt1552] = var6 + ((var2 << 16) - -(var3 << 8));
										++Class113.anInt1552;
									}
								}

							} else if(~RSString.incomingOpcode == -241) {
								var1 = GraphicDefinition.incomingBuffer.getByteS(true);
								var3 = Class107.currentChunkY + (var1 & 7);
								var2 = ((113 & var1) >> 4) + Class65.currentChunkX;
								var4 = GraphicDefinition.incomingBuffer.getShort(1);
								if(-1 >= ~var2 && var3 >= 0 && 104 > var2 && 104 > var3) {
									Class61 var24 = Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var2][var3];
									if(var24 != null) {
										for(Class3_Sub28_Sub14 var26 = (Class3_Sub28_Sub14)var24.method1222(); var26 != null; var26 = (Class3_Sub28_Sub14)var24.method1221()) {
											if(var26.aClass140_Sub7_3676.anInt2936 == (var4 & 32767)) {
												var26.method86(-1024);
												break;
											}
										}

										if(var24.method1222() == null) {
											Class3_Sub13_Sub22.aClass61ArrayArrayArray3273[WorldListCountry.localPlane][var2][var3] = null;
										}

										Class128.method1760(var3, (byte)65, var2);
									}
								}

							}
						} else {
							var1 = GraphicDefinition.incomingBuffer.getByte((byte)-71);
							var2 = Class65.currentChunkX - -(var1 >> 4 & 7);
							var3 = (var1 & 7) + Class107.currentChunkY;
							var4 = var2 + GraphicDefinition.incomingBuffer.getByte();
							var5 = GraphicDefinition.incomingBuffer.getByte() + var3;
							var6 = GraphicDefinition.incomingBuffer.getShort((byte)67);
							var7 = GraphicDefinition.incomingBuffer.getShort(1);
							var8 = 4 * GraphicDefinition.incomingBuffer.getByte((byte)-109);
							var28 = GraphicDefinition.incomingBuffer.getByte((byte)-50) * 4;
							var10 = GraphicDefinition.incomingBuffer.getShort(1);
							var11 = GraphicDefinition.incomingBuffer.getShort(1);
							var35 = GraphicDefinition.incomingBuffer.getByte((byte)-48);
							var13 = GraphicDefinition.incomingBuffer.getByte((byte)-35);
							if(255 == var35) {
								var35 = -1;
							}

							if(~var2 <= -1 && var3 >= 0 && -105 < ~var2 && 104 > var3 && ~var4 <= -1 && var5 >= 0 && -105 < ~var4 && 104 > var5 && -65536 != ~var7) {
								var5 = var5 * 128 + 64;
								var3 = 128 * var3 + 64;
								var2 = 128 * var2 + 64;
								var4 = 128 * var4 + 64;
								var36 = new Class140_Sub6(var7, WorldListCountry.localPlane, var2, var3, Class121.method1736(WorldListCountry.localPlane, 1, var2, var3) + -var8, var10 + Class44.anInt719, var11 + Class44.anInt719, var35, var13, var6, var28);
								var36.method2024(var5, 1, Class44.anInt719 + var10, Class121.method1736(WorldListCountry.localPlane, 1, var4, var5) - var28, var4);
								Class3_Sub13_Sub30.aClass61_3364.method1215(true, new Class3_Sub28_Sub19(var36));
							}

						}
					}
				} else {
					var1 = GraphicDefinition.incomingBuffer.getByteS(true);
					var2 = ((var1 & 125) >> 4) + Class65.currentChunkX;
					var3 = Class107.currentChunkY + (7 & var1);
					var4 = GraphicDefinition.incomingBuffer.getByteS(true);
					var5 = var4 >> 2;
					var6 = 3 & var4;
					var7 = Class75.anIntArray1107[var5];
					var8 = GraphicDefinition.incomingBuffer.getLEShort(-104);
					if('\uffff' == var8) {
						var8 = -1;
					}

					Class50.method1131(WorldListCountry.localPlane, 125, var6, var5, var3, var7, var2, var8);
				}
			}
		} catch (RuntimeException var23) {
			throw Class44.method1067(var23, "g.G(" + var0 + ')');
		}
	}

	static final void method1039(int var0, CacheIndex var1) {
		try {
			Class96.anInt1352 = var1.getArchiveForName(Class140_Sub2.aClass94_2700, (byte)-30);
			Class75_Sub2.anInt2643 = var1.getArchiveForName(WorldListCountry.aClass94_504, (byte)-30);
			Class3_Sub13_Sub11.anInt3132 = var1.getArchiveForName(Class3_Sub13_Sub11.aClass94_3138, (byte)-30);
			Class168.anInt2104 = var1.getArchiveForName(Class151_Sub1.aClass94_2959, (byte)-30);
			Class3_Sub13_Sub23_Sub1.hitMarkIndex = var1.getArchiveForName(Class3_Sub13_Sub31.aClass94_3370, (byte)-30);
			Client.anInt2195 = var1.getArchiveForName(Class164_Sub1.aClass94_3008, (byte)-30);
			Node.anInt2575 = var1.getArchiveForName(Class72.aClass94_1070, (byte)-30);
			if(var0 != 208) {
				anInt670 = 0;
			}

			RenderAnimationDefinition.anInt380 = var1.getArchiveForName(Class134.aClass94_1764, (byte)-30);
			Class3_Sub13_Sub29.anInt3356 = var1.getArchiveForName(Class54.aClass94_871, (byte)-30);
			Class129_Sub1.anInt2689 = var1.getArchiveForName(Class113.aClass94_1556, (byte)-30);
			Class3_Sub13_Sub4.anInt3061 = var1.getArchiveForName(Class3_Sub13_Sub38.aClass94_3452, (byte)-30);
			Class75_Sub1.anInt2633 = var1.getArchiveForName(Class3_Sub13_Sub14.aClass94_3168, (byte)-30);
			Class40.anInt678 = var1.getArchiveForName(Class113.aClass94_1567, (byte)-30);
			Class3_Sub15.anInt2436 = var1.getArchiveForName(Class148.aClass94_1913, (byte)-30);
			Class3_Sub28_Sub18.anInt3757 = var1.getArchiveForName(Class3_Sub28_Sub4.aClass94_3576, (byte)-30);
			Class45.anInt735 = var1.getArchiveForName(Class140_Sub2.aClass94_2702, (byte)-30);
			Class93.anInt1325 = var1.getArchiveForName(Class168.aClass94_2089, (byte)-30);
			Class3_Sub18.anInt2471 = var1.getArchiveForName(Class7.aClass94_2164, (byte)-30);
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "g.C(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
		}
	}

}
