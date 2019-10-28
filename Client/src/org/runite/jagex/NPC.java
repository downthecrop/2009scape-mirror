package org.runite.jagex;

final class NPC extends Class140_Sub4 {

	static boolean aBoolean3975 = false;
	NPCDefinition definition;
	static Class3_Sub28_Sub16[] aClass3_Sub28_Sub16Array3977;
	private static RSString aClass94_3978 = RSString.createRSString("Dec");
	static float aFloat3979;
	private static RSString aClass94_3980 = RSString.createRSString("Jul");
	private static RSString aClass94_3982 = RSString.createRSString("May");
	private static RSString aClass94_3983 = RSString.createRSString("Nov");
	private static RSString aClass94_3984 = RSString.createRSString("Mar");
	static int[] anIntArray3986 = new int[32];
	private static RSString aClass94_3987 = RSString.createRSString("flash3:");
	static RSString aClass94_3988 = aClass94_3987;
	static RSString aClass94_3981 = aClass94_3987;
	private static RSString aClass94_3989 = RSString.createRSString("Jan");
	private static RSString aClass94_3990 = RSString.createRSString("Feb");
	static RSString aClass94_3991 = RSString.createRSString("ondulation:");
	static RSString aClass94_3992 = RSString.createRSString("loginscreen");
	static CacheIndex aClass153_3993;
	static CacheIndex aClass153_3994;
	static int anInt3995;
	private static RSString aClass94_3996 = RSString.createRSString("Aug");
	static int[] anIntArray3997 = new int[]{19, 55, 38, 155, 255, 110, 137, 205, 76};
	static RSString aClass94_3998 = RSString.createRSString(":trade:");
	private static RSString aClass94_3999 = RSString.createRSString("Apr");
	private static RSString aClass94_4000 = RSString.createRSString("Jun");
	static int anInt4001;
	static int anInt4002 = 0;
	private static RSString aClass94_4003 = RSString.createRSString("Sep");
	private static RSString aClass94_4004 = RSString.createRSString("Oct");
	static RSString[] aClass94Array3985 = new RSString[]{aClass94_3989, aClass94_3990, aClass94_3984, aClass94_3999, aClass94_3982, aClass94_4000, aClass94_3980, aClass94_3996, aClass94_4003, aClass94_4004, aClass94_3983, aClass94_3978};

	public static void method1983(int var0) {
		try {
			anIntArray3986 = null;
			aClass94_3984 = null;
			aClass94_3983 = null;
			aClass94_3989 = null;
			if(var0 == -3) {
				aClass153_3994 = null;
				anIntArray3997 = null;
				aClass94_4004 = null;
				aClass94_3991 = null;
				aClass94_3998 = null;
				aClass94_3999 = null;
				aClass3_Sub28_Sub16Array3977 = null;
				aClass94_3978 = null;
				aClass94_3992 = null;
				aClass94_3990 = null;
				aClass94_3996 = null;
				aClass94_3981 = null;
				aClass153_3993 = null;
				aClass94_3987 = null;
				aClass94Array3985 = null;
				aClass94_3982 = null;
				aClass94_3988 = null;
				aClass94_3980 = null;
				aClass94_4003 = null;
				aClass94_4000 = null;
			}
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "km.N(" + var0 + ')');
		}
	}

	static final int method1984(int var0, int var1, int var2) {
		try {
			if(var1 != 38) {
				return 88;
			} else {
				int var3 = 57 * var2 + var0;
				var3 ^= var3 << 13;
				int var4 = Integer.MAX_VALUE & 1376312589 + (var3 * var3 * 15731 - -789221) * var3;
				return (var4 & 133802063) >> 19;
			}
		} catch (RuntimeException var5) {
			throw Class44.method1067(var5, "km.S(" + var0 + ',' + var1 + ',' + var2 + ')');
		}
	}

	protected final void finalize() {}

	final int method1871() {
		try {
			return this.anInt2820;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "km.MA()");
		}
	}

	static final byte[] method1985(int var0, Object var1, boolean var2) {
		try {
			if(var1 == null) {
				return null;
			} else if(var1 instanceof byte[]) {
				byte[] var5 = (byte[])((byte[])var1);
				return var2?Class12.method873((byte)62, var5):var5;
			} else {
				if(var0 > -118) {
					method1983(19);
				}

				if(!(var1 instanceof Class144)) {
					throw new IllegalArgumentException();
				} else {
					Class144 var3 = (Class144)var1;
					return var3.method2064(26);
				}
			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "km.Q(" + var0 + ',' + (var1 != null?"{...}":"null") + ',' + var2 + ')');
		}
	}

	final void animate(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, int var11, Class127_Sub1 var12) {
		try {
			if(this.definition != null) {
				AnimationDefinition var13 = 0 != ~this.anInt2771 && -1 == ~this.anInt2828?Client.getAnimationDefinition(this.anInt2771, (byte)-20):null;
				AnimationDefinition var14 = -1 != this.anInt2764 && (~this.anInt2764 != ~this.method1965(false).anInt368 || var13 == null)?Client.getAnimationDefinition(this.anInt2764, (byte)-20):null;
				Model var15 = this.definition.method1476(this.aClass145Array2809, this.anInt2793, (byte)-116, this.anInt2813, this.anInt2776, this.anInt2760, this.anInt2832, var14, this.anInt2802, var13);
				if(var15 != null) {
					this.anInt2820 = var15.method1871();
					NPCDefinition var16 = this.definition;
					if(null != var16.childNPCs) {
						var16 = var16.method1471((byte)-110);
					}

					Model var17;
					if(Class140_Sub6.aBoolean2910 && var16.aBoolean1249) {
						var17 = Class140_Sub3.method1957(this.definition.aByte1287, this.aBoolean2810, null == var14?var13:var14, this.anInt2819, this.definition.aShort1256, this.anInt2829, this.definition.aShort1286, this.definition.size, var15, var1, null != var14?this.anInt2813:this.anInt2832, this.anInt2831, this.definition.aByte1275, (byte)-49);
						if(HDToolKit.highDetail) {
							float var18 = HDToolKit.method1852();
							float var19 = HDToolKit.method1839();
							HDToolKit.method1851();
							HDToolKit.method1825(var18, -150.0F + var19);
							var17.animate(0, var2, var3, var4, var5, var6, var7, var8, -1L, var11, this.aClass127_Sub1_2801);
							HDToolKit.method1830();
							HDToolKit.method1825(var18, var19);
						} else {
							var17.animate(0, var2, var3, var4, var5, var6, var7, var8, -1L, var11, this.aClass127_Sub1_2801);
						}
					}

					this.method1971(var15, (byte)-111);
					this.method1969((byte)115, var15, var1);
					var17 = null;
					if(~this.anInt2842 != 0 && -1 != this.anInt2805) {
						GraphicDefinition var21 = RenderAnimationDefinition.getGraphicDefinition((byte)42, this.anInt2842);
						var17 = var21.method966(this.anInt2826, (byte)-30, this.anInt2805, this.anInt2761);
						if(var17 != null) {
							var17.method1897(0, -this.anInt2799, 0);
							if(var21.aBoolean536) {
								if(-1 != ~Class3_Sub13_Sub16.anInt3198) {
									var17.method1896(Class3_Sub13_Sub16.anInt3198);
								}

								if(~Class3_Sub28_Sub9.anInt3623 != -1) {
									var17.method1886(Class3_Sub28_Sub9.anInt3623);
								}

								if(0 != Class3_Sub13_Sub9.anInt3111) {
									var17.method1897(0, Class3_Sub13_Sub9.anInt3111, 0);
								}
							}
						}
					}

					if(!HDToolKit.highDetail) {
						if(null != var17) {
							var15 = ((Class140_Sub1_Sub2)var15).method1943(var17);
						}

						if(this.definition.size == 1) {
							var15.aBoolean2699 = true;
						}

						var15.animate(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, this.aClass127_Sub1_2801);
					} else {
						if(-2 == ~this.definition.size) {
							var15.aBoolean2699 = true;
						}

						var15.animate(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, this.aClass127_Sub1_2801);
						if(var17 != null) {
							if(-2 == ~this.definition.size) {
								var17.aBoolean2699 = true;
							}

							var17.animate(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, this.aClass127_Sub1_2801);
						}
					}

				}
			}
		} catch (RuntimeException var20) {
			throw Class44.method1067(var20, "km.IA(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var11 + ',' + (var12 != null?"{...}":"null") + ')');
		}
	}

	final int getRenderAnimationId(int var1) {
		try {
			if(~Class158.anInt2014 != var1 && this.definition.childNPCs != null) {
				NPCDefinition var2 = this.definition.method1471((byte)21);
				if(var2 != null && 0 != ~var2.renderAnimationId) {
					return var2.renderAnimationId;
				}
			}

			return this.renderAnimationId;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "km.B(" + var1 + ')');
		}
	}

	final void method1867(int var1, int var2, int var3, int var4, int var5) {
		try {
			if(this.definition == null) {
				;
			}
		} catch (RuntimeException var7) {
			throw Class44.method1067(var7, "km.IB(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
		}
	}

	final boolean hasDefinitions(byte var1) {
		try {
			if(var1 != 17) {
				method1984(-101, -40, 63);
			}

			return null != this.definition;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "km.L(" + var1 + ')');
		}
	}

	static final boolean method1986(int var0) {
		try {
			if(var0 <= 22) {
				method1984(-48, 88, 31);
			}

			return HDToolKit.highDetail?true:Class3_Sub28_Sub13.aBoolean3665;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "km.O(" + var0 + ')');
		}
	}

	final void setDefinitions(int var1, NPCDefinition var2) {
		try {
			this.definition = var2;
			if(var1 == -1) {
				if(this.aClass127_Sub1_2801 != null) {
					this.aClass127_Sub1_2801.method1759();
				}

			}
		} catch (RuntimeException var4) {
			throw Class44.method1067(var4, "km.R(" + var1 + ',' + (var2 != null?"{...}":"null") + ')');
		}
	}

	static final boolean method1988(boolean var0) {
		try {
			if(var0) {
				aClass94_3980 = (RSString)null;
			}

			try {
				if(~Class10.anInt154 == -3) {
					if(Class83.aClass3_Sub27_1154 == null) {
						Class83.aClass3_Sub27_1154 = Class3_Sub27.method517(Class101.aClass153_1423, Class3_Sub13_Sub39.anInt3463, Class132.anInt1741);
						if(null == Class83.aClass3_Sub27_1154) {
							return false;
						}
					}

					if(Class3_Sub28_Sub4.aClass83_3579 == null) {
						Class3_Sub28_Sub4.aClass83_3579 = new Class83(Class40.aClass153_679, Class3_Sub28_Sub20.aClass153_3786);
					}

					if(Class101.aClass3_Sub24_Sub4_1421.method470(Class83.aClass3_Sub27_1154, -122, Class124.aClass153_1661, Class3_Sub28_Sub4.aClass83_3579, 22050)) {
						Class101.aClass3_Sub24_Sub4_1421.method471((byte)53);
						Class101.aClass3_Sub24_Sub4_1421.method506(128, Class3_Sub13_Sub36.anInt3423);
						Class101.aClass3_Sub24_Sub4_1421.method490(Class3_Sub9.aBoolean2311, Class83.aClass3_Sub27_1154, 17774);
						Class10.anInt154 = 0;
						Class83.aClass3_Sub27_1154 = null;
						Class3_Sub28_Sub4.aClass83_3579 = null;
						Class101.aClass153_1423 = null;
						return true;
					}
				}
			} catch (Exception var2) {
				var2.printStackTrace();
				Class101.aClass3_Sub24_Sub4_1421.method505((byte)-128);
				Class101.aClass153_1423 = null;
				Class83.aClass3_Sub27_1154 = null;
				Class10.anInt154 = 0;
				Class3_Sub28_Sub4.aClass83_3579 = null;
			}

			return false;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "km.P(" + var0 + ')');
		}
	}

}
