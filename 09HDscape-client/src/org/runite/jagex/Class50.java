package org.runite.jagex;

final class Class50 {

	static int anInt820 = 0;
	int anInt821;
	private static RSString aClass94_832 = RSString.createRSString(" has logged out)3");
	private static RSString aClass94_827 = RSString.createRSString("purple:");
	static RSString aClass94_822 = aClass94_832;
	int anInt823;
	static Class3_Sub28_Sub16 aClass3_Sub28_Sub16_824;
	static RSString aClass94_825 = aClass94_827;
	static long[] aLongArray826 = new long[200];
	static int anInt828 = 0;
	static RSString aClass94_829 = RSString.createRSString("null");
	int anInt830;
	int anInt831;

	static RSString aClass94_833 = aClass94_827;


	static final void method1131(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		try {
			if(~var6 <= -1 && 0 <= var4 && -104 < ~var6 && 103 > var4) {
				int var9;
				if(-1 == ~var5) {
					Class70 var8 = Class154.method2147(var0, var6, var4);
					if(var8 != null) {
						var9 = Integer.MAX_VALUE & (int)(var8.aLong1048 >>> 32);
						if(-3 == ~var3) {
							var8.aClass140_1049 = new Class140_Sub3(var9, 2, 4 + var2, var0, var6, var4, var7, false, var8.aClass140_1049);
							var8.aClass140_1052 = new Class140_Sub3(var9, 2, 3 & 1 + var2, var0, var6, var4, var7, false, var8.aClass140_1052);
						} else {
							var8.aClass140_1049 = new Class140_Sub3(var9, var3, var2, var0, var6, var4, var7, false, var8.aClass140_1049);
						}
					}
				}

				if(-2 == ~var5) {
					Class19 var12 = Class44.method1068(var0, var6, var4);
					if(null != var12) {
						var9 = (int)(var12.aLong428 >>> 32) & Integer.MAX_VALUE;
						if(-5 != ~var3 && -6 != ~var3) {
							if(~var3 != -7) {
								if(7 == var3) {
									var12.aClass140_429 = new Class140_Sub3(var9, 4, (var2 - -2 & 3) - -4, var0, var6, var4, var7, false, var12.aClass140_429);
								} else if(var3 == 8) {
									var12.aClass140_429 = new Class140_Sub3(var9, 4, 4 + var2, var0, var6, var4, var7, false, var12.aClass140_429);
									var12.aClass140_423 = new Class140_Sub3(var9, 4, (2 + var2 & 3) + 4, var0, var6, var4, var7, false, var12.aClass140_423);
								}
							} else {
								var12.aClass140_429 = new Class140_Sub3(var9, 4, var2 - -4, var0, var6, var4, var7, false, var12.aClass140_429);
							}
						} else {
							var12.aClass140_429 = new Class140_Sub3(var9, 4, var2, var0, var6, var4, var7, false, var12.aClass140_429);
						}
					}
				}

				if(-3 == ~var5) {
					if(~var3 == -12) {
						var3 = 10;
					}

					Class25 var11 = Class75.method1336(var0, var6, var4);
					if(var11 != null) {
						var11.aClass140_479 = new Class140_Sub3((int)(var11.aLong498 >>> 32) & Integer.MAX_VALUE, var3, var2, var0, var6, var4, var7, false, var11.aClass140_479);
					}
				}

				if(~var5 == -4) {
					Class12 var13 = RSByteBuffer.method784(var0, var6, var4);
					if(null != var13) {
						var13.object = new Class140_Sub3(Integer.MAX_VALUE & (int)(var13.aLong328 >>> 32), 22, var2, var0, var6, var4, var7, false, var13.object);
					}
				}
			}

			if(var1 <= 104) {
				method1132(-79);
			}

		} catch (RuntimeException var10) {
			throw Class44.method1067(var10, "hd.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
		}
	}

	static final void method1132(int var0) {
		try {
			Class128.aClass93_1683.method1523((byte)-111);
			if(var0 != 103) {
				method1132(14);
			}

		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "hd.A(" + var0 + ')');
		}
	}

	public static void method1133(byte var0) {
		try {
			aClass94_833 = null;
			aClass94_822 = null;
			aClass94_827 = null;
			aClass94_829 = null;
			if(var0 != 81) {
				aClass94_833 = (RSString)null;
			}

			aClass94_832 = null;
			aClass94_825 = null;
			aLongArray826 = null;
			aClass3_Sub28_Sub16_824 = null;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "hd.C(" + var0 + ')');
		}
	}

	public Class50() {}

	Class50(Class50 var1) {
		try {
			this.anInt823 = var1.anInt823;
			this.anInt831 = var1.anInt831;
			this.anInt821 = var1.anInt821;
			this.anInt830 = var1.anInt830;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "hd.<init>(" + (var1 != null?"{...}":"null") + ')');
		}
	}

}
