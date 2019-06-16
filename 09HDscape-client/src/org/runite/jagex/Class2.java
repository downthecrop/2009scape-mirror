package org.runite.jagex;

final class Class2 {
	
	private static RSString aClass94_68 = RSString.createRSString("glow1:");

	static int anInt59 = 0;
	static RSString aClass94_60 = aClass94_68;
	int anInt61;
	static RSString aClass94_62 = aClass94_68;
	static short[] aShortArray63 = new short[]{(short)960, (short)957, (short)-21568, (short)-21571, (short)22464};
	int anInt64;
	static int anInt65;
	static boolean isMember = false;
	static int anInt67;
	boolean aBoolean69 = false;
	static Interface5[] anInterface5Array70;


	static final boolean method73(short var0, int var1) {
		try {
			if(var0 != 47 && ~var0 != -6 && var0 != 43 && ~var0 != -36 && ~var0 != -59 && -23 != ~var0 && var0 != 40 && var0 != 3) {
				if(-10 != ~var0 && ~var0 != -13 && -1007 != ~var0 && -1004 != ~var0) {
					if(var1 <= 42) {
						anInt59 = 1;
					}

					return var0 != 25 && -24 != ~var0 && 48 != var0 && var0 != 7 && -14 != ~var0?var0 == 8 || var0 == 32 || ~var0 == -29 || ~var0 == -60 || ~var0 == -52 || -42 == ~var0:true;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "aa.C(" + var0 + ',' + var1 + ')');
		}
	}

	final void method74(byte var1, RSByteBuffer var2, int var3) {
		try {
			while(true) {
				int var4 = var2.getByte((byte)-34);
				if(-1 == ~var4) {
					if(var1 > -108) {
						this.method74((byte)85, (RSByteBuffer)null, 63);
					}

					return;
				}

				this.method79(var4, var2, var3, 95);
			}
		} catch (RuntimeException var5) {
			throw Class44.method1067(var5, "aa.F(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ')');
		}
	}

	static final void method75(RSInterface[] var0, boolean var1, int var2) {
		try {
			if(var1) {
				for(int var3 = 0; ~var3 > ~var0.length; ++var3) {
					RSInterface var4 = var0[var3];
					if(null != var4) {
						if(var4.type == 0) {
							if(null != var4.aClass11Array262) {
								method75(var4.aClass11Array262, true, var2);
							}

							Class3_Sub31 var5 = (Class3_Sub31)Class3_Sub13_Sub17.aClass130_3208.method1780((long)var4.anInt279, 0);
							if(null != var5) {
								Class3_Sub8.method124(49, var2, var5.anInt2602);
							}
						}

						CS2Script var7;
						if(~var2 == -1 && null != var4.anObjectArray206) {
							var7 = new CS2Script();
							var7.arguments = var4.anObjectArray206;
							var7.aClass11_2449 = var4;
							Class43.method1065(1073376993, var7);
						}

						if(-2 == ~var2 && var4.anObjectArray176 != null) {
							if(-1 >= ~var4.anInt191) {
								RSInterface var8 = Class7.getRSInterface((byte)121, var4.anInt279);
								if(null == var8 || null == var8.aClass11Array262 || ~var4.anInt191 <= ~var8.aClass11Array262.length || var8.aClass11Array262[var4.anInt191] != var4) {
									continue;
								}
							}

							var7 = new CS2Script();
							var7.arguments = var4.anObjectArray176;
							var7.aClass11_2449 = var4;
							Class43.method1065(1073376993, var7);
						}
					}
				}

			}
		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "aa.I(" + (var0 != null?"{...}":"null") + ',' + var1 + ',' + var2 + ')');
		}
	}

	static final boolean method76(int var0, int var1, int var2, int var3, int var4, boolean var5, int var6, int var7, int var8, int var9, int var10, int var11) {
		try {
			int var12;
			int var13;
			for(var12 = 0; 104 > var12; ++var12) {
				for(var13 = 0; -105 < ~var13; ++var13) {
					Class84.anIntArrayArray1160[var12][var13] = 0;
					Class97.anIntArrayArray1373[var12][var13] = 99999999;
				}
			}

			Class84.anIntArrayArray1160[var11][var3] = 99;
			Class97.anIntArrayArray1373[var11][var3] = 0;
			var13 = var3;
			var12 = var11;
			byte var14 = 0;
			Class3_Sub13_Sub38.anIntArray3456[var14] = var11;
			boolean var16 = false;
			int var15 = 0;
			int var27 = var14 + 1;
			Class45.anIntArray729[var14] = var3;
			int[][] var17 = Class86.aClass91Array1182[WorldListCountry.localPlane].anIntArrayArray1304;

			int var18;
			while(var15 != var27) {
				var12 = Class3_Sub13_Sub38.anIntArray3456[var15];
				var13 = Class45.anIntArray729[var15];
				var15 = 4095 & var15 - -1;
				if(var8 == var12 && ~var4 == ~var13) {
					var16 = true;
					break;
				}

				if(-1 != ~var1) {
					if((5 > var1 || -11 == ~var1) && Class86.aClass91Array1182[WorldListCountry.localPlane].method1488(var4, var12, false, var13, var8, -1 + var1, 2, var7)) {
						var16 = true;
						break;
					}

					if(var1 < 10 && Class86.aClass91Array1182[WorldListCountry.localPlane].method1492(var4, var1 + -1, var8, var13, 2, var7, var12, 88)) {
						var16 = true;
						break;
					}
				}

				if(0 != var0 && ~var6 != -1 && Class86.aClass91Array1182[WorldListCountry.localPlane].method1498(true, var8, var13, var12, 2, var0, var2, var4, var6)) {
					var16 = true;
					break;
				}

				var18 = Class97.anIntArrayArray1373[var12][var13] - -1;
				if(var12 > 0 && ~Class84.anIntArrayArray1160[var12 + -1][var13] == -1 && 0 == (var17[-1 + var12][var13] & 19661070) && ~(var17[-1 + var12][var13 + 1] & 19661112) == -1) {
					Class3_Sub13_Sub38.anIntArray3456[var27] = var12 + -1;
					Class45.anIntArray729[var27] = var13;
					var27 = 1 + var27 & 4095;
					Class84.anIntArrayArray1160[var12 - 1][var13] = 2;
					Class97.anIntArrayArray1373[-1 + var12][var13] = var18;
				}

				if(102 > var12 && ~Class84.anIntArrayArray1160[1 + var12][var13] == -1 && 0 == (var17[2 + var12][var13] & 19661187) && 0 == (var17[var12 + 2][1 + var13] & 19661280)) {
					Class3_Sub13_Sub38.anIntArray3456[var27] = 1 + var12;
					Class45.anIntArray729[var27] = var13;
					var27 = 4095 & var27 + 1;
					Class84.anIntArrayArray1160[var12 - -1][var13] = 8;
					Class97.anIntArrayArray1373[var12 - -1][var13] = var18;
				}

				if(var13 > 0 && ~Class84.anIntArrayArray1160[var12][var13 + -1] == -1 && (19661070 & var17[var12][-1 + var13]) == 0 && ~(var17[var12 + 1][var13 - 1] & 19661187) == -1) {
					Class3_Sub13_Sub38.anIntArray3456[var27] = var12;
					Class45.anIntArray729[var27] = -1 + var13;
					Class84.anIntArrayArray1160[var12][-1 + var13] = 1;
					Class97.anIntArrayArray1373[var12][-1 + var13] = var18;
					var27 = 4095 & 1 + var27;
				}

				if(~var13 > -103 && ~Class84.anIntArrayArray1160[var12][var13 + 1] == -1 && ~(var17[var12][2 + var13] & 19661112) == -1 && -1 == ~(var17[1 + var12][var13 + 2] & 19661280)) {
					Class3_Sub13_Sub38.anIntArray3456[var27] = var12;
					Class45.anIntArray729[var27] = var13 + 1;
					Class84.anIntArrayArray1160[var12][1 + var13] = 4;
					var27 = 1 + var27 & 4095;
					Class97.anIntArrayArray1373[var12][var13 - -1] = var18;
				}

				if(var12 > 0 && var13 > 0 && -1 == ~Class84.anIntArrayArray1160[-1 + var12][-1 + var13] && -1 == ~(19661112 & var17[-1 + var12][var13]) && 0 == (var17[-1 + var12][-1 + var13] & 19661070) && 0 == (19661187 & var17[var12][-1 + var13])) {
					Class3_Sub13_Sub38.anIntArray3456[var27] = -1 + var12;
					Class45.anIntArray729[var27] = -1 + var13;
					Class84.anIntArrayArray1160[-1 + var12][-1 + var13] = 3;
					Class97.anIntArrayArray1373[-1 + var12][-1 + var13] = var18;
					var27 = 4095 & var27 + 1;
				}

				if(-103 < ~var12 && 0 < var13 && ~Class84.anIntArrayArray1160[1 + var12][var13 + -1] == -1 && -1 == ~(var17[var12 - -1][var13 - 1] & 19661070) && -1 == ~(19661187 & var17[2 + var12][var13 + -1]) && -1 == ~(19661280 & var17[var12 + 2][var13])) {
					Class3_Sub13_Sub38.anIntArray3456[var27] = var12 - -1;
					Class45.anIntArray729[var27] = var13 - 1;
					var27 = 4095 & 1 + var27;
					Class84.anIntArrayArray1160[var12 - -1][-1 + var13] = 9;
					Class97.anIntArrayArray1373[1 + var12][var13 + -1] = var18;
				}

				if(-1 > ~var12 && -103 < ~var13 && 0 == Class84.anIntArrayArray1160[var12 - 1][var13 + 1] && ~(var17[-1 + var12][var13 - -1] & 19661070) == -1 && 0 == (19661112 & var17[-1 + var12][var13 - -2]) && ~(19661280 & var17[var12][var13 + 2]) == -1) {
					Class3_Sub13_Sub38.anIntArray3456[var27] = var12 - 1;
					Class45.anIntArray729[var27] = 1 + var13;
					Class84.anIntArrayArray1160[var12 - 1][1 + var13] = 6;
					Class97.anIntArrayArray1373[-1 + var12][1 + var13] = var18;
					var27 = 1 + var27 & 4095;
				}

				if(-103 < ~var12 && ~var13 > -103 && -1 == ~Class84.anIntArrayArray1160[1 + var12][1 + var13] && -1 == ~(19661112 & var17[var12 - -1][2 + var13]) && -1 == ~(19661280 & var17[var12 + 2][var13 - -2]) && (19661187 & var17[var12 - -2][var13 + 1]) == 0) {
					Class3_Sub13_Sub38.anIntArray3456[var27] = 1 + var12;
					Class45.anIntArray729[var27] = var13 + 1;
					var27 = var27 - -1 & 4095;
					Class84.anIntArrayArray1160[var12 - -1][var13 + 1] = 12;
					Class97.anIntArrayArray1373[1 + var12][1 + var13] = var18;
				}
			}

			Class129.anInt1692 = 0;
			int var19;
			if(!var16) {
				if(!var5) {
					return false;
				}

				var18 = 1000;
				var19 = 100;
				byte var20 = 10;

				for(int var21 = -var20 + var8; ~var21 >= ~(var20 + var8); ++var21) {
					for(int var22 = var4 + -var20; ~var22 >= ~(var20 + var4); ++var22) {
						if(-1 >= ~var21 && -1 >= ~var22 && -105 < ~var21 && 104 > var22 && Class97.anIntArrayArray1373[var21][var22] < 100) {
							int var23 = 0;
							int var24 = 0;
							if(~var21 <= ~var8) {
								if(~var21 < ~(-1 + var0 + var8)) {
									var23 = var21 + 1 + -var0 + -var8;
								}
							} else {
								var23 = var8 - var21;
							}

							if(~var4 >= ~var22) {
								if(~var22 < ~(-1 + var4 + var6)) {
									var24 = -var4 + -var6 + 1 + var22;
								}
							} else {
								var24 = -var22 + var4;
							}

							int var25 = var23 * var23 + var24 * var24;
							if(var25 < var18 || ~var18 == ~var25 && Class97.anIntArrayArray1373[var21][var22] < var19) {
								var13 = var22;
								var19 = Class97.anIntArrayArray1373[var21][var22];
								var18 = var25;
								var12 = var21;
							}
						}
					}
				}

				if(~var18 == -1001) {
					return false;
				}

				if(var11 == var12 && ~var13 == ~var3) {
					return false;
				}

				Class129.anInt1692 = 1;
			}

			byte var28 = 0;
			Class3_Sub13_Sub38.anIntArray3456[var28] = var12;
			var15 = var28 + 1;
			Class45.anIntArray729[var28] = var13;
			var18 = var19 = Class84.anIntArrayArray1160[var12][var13];
			if(var10 <= 2) {
				aClass94_60 = (RSString)null;
			}

			for(; ~var11 != ~var12 || ~var3 != ~var13; var18 = Class84.anIntArrayArray1160[var12][var13]) {
				if(var19 != var18) {
					Class3_Sub13_Sub38.anIntArray3456[var15] = var12;
					Class45.anIntArray729[var15++] = var13;
					var19 = var18;
				}

				if((var18 & 2) != 0) {
					++var12;
				} else if(~(8 & var18) != -1) {
					--var12;
				}

				if((var18 & 1) == 0) {
					if(~(var18 & 4) != -1) {
						--var13;
					}
				} else {
					++var13;
				}
			}

			if(~var15 >= -1) {
				if(1 != var9) {
					return true;
				} else {
					return false;
				}
			} else {
				Class3_Sub13_Sub27.method299(113, var15, var9);
				return true;
			}
		} catch (RuntimeException var26) {
			throw Class44.method1067(var26, "aa.A(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var10 + ',' + var11 + ')');
		}
	}

	final LDIndexedSprite method77(int var1, byte var2) {
		try {
			LDIndexedSprite var3 = (LDIndexedSprite)Class3_Sub13_Sub31.aClass93_3369.get((long)(var1 << 16 | this.anInt64), (byte)121);
			if(var3 != null) {
				return var3;
			} else {
				KeyboardListener.aClass153_1916.method2144(0, this.anInt64);
				int var4 = 125 % ((var2 - -21) / 50);
				var3 = RSString.method1539(0, true, this.anInt64, KeyboardListener.aClass153_1916);
				if(var3 != null) {
					var3.method1668(Class102.anInt2136, Class46.anInt740, Class158.anInt2015);
					var3.anInt1469 = var3.anInt1461;
					var3.anInt1467 = var3.anInt1468;

					for(int var5 = 0; ~var1 < ~var5; ++var5) {
						var3.method1674();
					}

					Class3_Sub13_Sub31.aClass93_3369.put((byte)-125, var3, (long)(var1 << 16 | this.anInt64));
				}

				return var3;
			}
		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "aa.D(" + var1 + ',' + var2 + ')');
		}
	}

	static final void method78(int var0, boolean var1, int var2) {
		try {
			++CacheIndex.anInt1944;
			Class3_Sub13_Sub1.outgoingBuffer.putOpcode(132);
			Class3_Sub13_Sub1.outgoingBuffer.putIntA(var2);
			if(!var1) {
				Class3_Sub13_Sub1.outgoingBuffer.putLEShort(-1, var0);
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "aa.E(" + var0 + ',' + var1 + ',' + var2 + ')');
		}
	}

	private final void method79(int var1, RSByteBuffer var2, int var3, int var4) {
		try {
			if(var1 == 1) {
				this.anInt64 = var2.getShort(1);
			} else if(var1 != 2) {
				if(~var1 != -4) {
					if(var1 == 4) {
						this.anInt64 = -1;
					}
				} else {
					this.aBoolean69 = true;
				}
			} else {
				this.anInt61 = var2.getTriByte((byte)96);
			}

			if(var4 <= 7) {
				anInt65 = 123;
			}

		} catch (RuntimeException var6) {
			throw Class44.method1067(var6, "aa.B(" + var1 + ',' + (var2 != null?"{...}":"null") + ',' + var3 + ',' + var4 + ')');
		}
	}

	public static void method80(int var0) {
		try {
			aClass94_60 = null;
			aShortArray63 = null;
			if(var0 != -27401) {
				anInt65 = 93;
			}

			aClass94_68 = null;
			anInterface5Array70 = null;
			aClass94_62 = null;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "aa.G(" + var0 + ')');
		}
	}

	static final void method81(byte var0, RSInterface var1) {
		try {
			if(var0 != -128) {
				method75((RSInterface[])null, true, 21);
			}

			int var2 = var1.anInt189;
			if(324 != var2) {
				if(-326 != ~var2) {
					if(var2 == 327) {
						var1.anInt182 = 150;
						var1.anInt308 = 2047 & (int)(Math.sin((double)Class44.anInt719 / 40.0D) * 256.0D);
						var1.modelType = 5;
						var1.itemId = -1;
					} else if(var2 == 328) {
						if(null == Class102.player.displayName) {
							var1.itemId = 0;
						} else {
							var1.anInt182 = 150;
							var1.anInt308 = 2047 & (int)(256.0D * Math.sin((double)Class44.anInt719 / 40.0D));
							var1.modelType = 5;
							var1.itemId = 2047 + ((int)Class102.player.displayName.toLong(var0 + 18) << 11);
							var1.anInt260 = Class102.player.anInt2793;
							var1.anInt267 = 0;
							var1.animationId = Class102.player.anInt2764;
							var1.anInt283 = Class102.player.anInt2813;
						}
					}
				} else {
					if(-1 == Class3_Sub13_Sub21.anInt3260) {
						Class84.anInt1165 = var1.anInt296;
						Class3_Sub13_Sub21.anInt3260 = var1.spriteArchiveId;
					}

					if(!Class77.aClass52_1112.aBoolean864) {
						var1.spriteArchiveId = Class3_Sub13_Sub21.anInt3260;
					} else {
						var1.spriteArchiveId = Class84.anInt1165;
					}

				}
			} else {
				if(-1 == Class3_Sub13_Sub21.anInt3260) {
					Class3_Sub13_Sub21.anInt3260 = var1.spriteArchiveId;
					Class84.anInt1165 = var1.anInt296;
				}

				if(Class77.aClass52_1112.aBoolean864) {
					var1.spriteArchiveId = Class3_Sub13_Sub21.anInt3260;
				} else {
					var1.spriteArchiveId = Class84.anInt1165;
				}

			}
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "aa.H(" + var0 + ',' + (var1 != null?"{...}":"null") + ')');
		}
	}

}
