package org.runite.jagex;

final class Class3_Sub4 extends Class3 {

	int anInt2248;
	static int anInt2249;
	int anInt2250;
	static int anInt2251;
	static RSString aClass94_2252 = RSString.createRSString("Atteindre");
	int anInt2253;
	int anInt2254;
	static RSString aClass94_2255 = RSString.createRSString("Suche nach Updates )2 ");
	int anInt2256;
	int anInt2257;
	static CacheIndex aClass153_2258;
	int anInt2259 = -1;
	static RSString aClass94_2260 = RSString.createRSString("Lade Wordpack )2 ");
	int anInt2261 = 0;
	int anInt2262;
	int anInt2263;
	int anInt2264;
	int anInt2265;


	static final int method107(CacheIndex var0, byte var1) {
		try {
			int var2 = 0;
			if(var0.method2144(0, Class168.anInt2104)) {
				++var2;
			}

			if(var0.method2144(0, Class3_Sub13_Sub23_Sub1.hitMarkIndex)) {
				++var2;
			}

			if(var0.method2144(0, Client.anInt2195)) {
				++var2;
			}

			if(var0.method2144(0, Node.anInt2575)) {
				++var2;
			}

			if(var0.method2144(0, RenderAnimationDefinition.anInt380)) {
				++var2;
			}

			if(var0.method2144(0, Class3_Sub13_Sub29.anInt3356)) {
				++var2;
			}

			if(var0.method2144(0, Class129_Sub1.anInt2689)) {
				++var2;
			}

			if(var1 > -124) {
				method109(68);
			}

			if(var0.method2144(0, Class3_Sub13_Sub4.anInt3061)) {
				++var2;
			}

			if(var0.method2144(0, Class75_Sub1.anInt2633)) {
				++var2;
			}

			if(var0.method2144(0, Class40.anInt678)) {
				++var2;
			}

			if(var0.method2144(0, Class3_Sub15.anInt2436)) {
				++var2;
			}

			if(var0.method2144(0, Class3_Sub28_Sub18.anInt3757)) {
				++var2;
			}

			if(var0.method2144(0, Class45.anInt735)) {
				++var2;
			}

			if(var0.method2144(0, Class93.anInt1325)) {
				++var2;
			}

			if(var0.method2144(0, Class3_Sub18.anInt2471)) {
				++var2;
			}

			return var2;
		} catch (RuntimeException var3) {
			throw Class44.method1067(var3, "cd.B(" + (var0 != null?"{...}":"null") + ',' + var1 + ')');
		}
	}

	public static void method109(int var0) {
		try {
			aClass94_2255 = null;
			aClass94_2260 = null;
			aClass94_2252 = null;
			if(var0 != 2) {
				method109(-22);
			}

			aClass153_2258 = null;
		} catch (RuntimeException var2) {
			throw Class44.method1067(var2, "cd.A(" + var0 + ')');
		}
	}

	static final void method110(int var0, int var1, int var2, int var3, boolean var4) {
		IOHandler.anInt1234 = var1;
		Class3_Sub13_Sub15.anInt3179 = var2;
		Class3_Sub13_Sub39.anInt3466 = var3;
		Class3_Sub28_Sub10_Sub2.aClass3_Sub2ArrayArrayArray4070 = new Class3_Sub2[var0][IOHandler.anInt1234][Class3_Sub13_Sub15.anInt3179];
		Class58.anIntArrayArrayArray914 = new int[var0][IOHandler.anInt1234 + 1][Class3_Sub13_Sub15.anInt3179 + 1];
		if(HDToolKit.highDetail) {
			Client.aClass3_Sub11ArrayArray2199 = new Class3_Sub11[4][];
		}

		if(var4) {
			Class166.aClass3_Sub2ArrayArrayArray2065 = new Class3_Sub2[1][IOHandler.anInt1234][Class3_Sub13_Sub15.anInt3179];
			Class3_Sub13_Sub9.anIntArrayArray3115 = new int[IOHandler.anInt1234][Class3_Sub13_Sub15.anInt3179];
			Class3_Sub28_Sub7.anIntArrayArrayArray3605 = new int[1][IOHandler.anInt1234 + 1][Class3_Sub13_Sub15.anInt3179 + 1];
			if(HDToolKit.highDetail) {
				Class3_Sub13_Sub28.aClass3_Sub11ArrayArray3346 = new Class3_Sub11[1][];
			}
		} else {
			Class166.aClass3_Sub2ArrayArrayArray2065 = (Class3_Sub2[][][])null;
			Class3_Sub13_Sub9.anIntArrayArray3115 = (int[][])null;
			Class3_Sub28_Sub7.anIntArrayArrayArray3605 = (int[][][])null;
			Class3_Sub13_Sub28.aClass3_Sub11ArrayArray3346 = (Class3_Sub11[][])null;
		}

		Class167.method2264(false);
		Class3_Sub28_Sub8.aClass113Array3610 = new Class113[500];
		anInt2249 = 0;
		Class145.aClass113Array1895 = new Class113[500];
		Class126.anInt1672 = 0;
		Class81.anIntArrayArrayArray1142 = new int[var0][IOHandler.anInt1234 + 1][Class3_Sub13_Sub15.anInt3179 + 1];
		AnimationDefinition.aClass25Array1868 = new Class25[5000];
		Class3_Sub13_Sub5.anInt3070 = 0;
		Class3_Sub28_Sub10_Sub1.aClass25Array4060 = new Class25[100];
		Class23.aBooleanArrayArray457 = new boolean[Class3_Sub13_Sub39.anInt3466 + Class3_Sub13_Sub39.anInt3466 + 1][Class3_Sub13_Sub39.anInt3466 + Class3_Sub13_Sub39.anInt3466 + 1];
		Class49.aBooleanArrayArray814 = new boolean[Class3_Sub13_Sub39.anInt3466 + Class3_Sub13_Sub39.anInt3466 + 2][Class3_Sub13_Sub39.anInt3466 + Class3_Sub13_Sub39.anInt3466 + 2];
		Class136.aByteArrayArrayArray1774 = new byte[var0][IOHandler.anInt1234][Class3_Sub13_Sub15.anInt3179];
	}

}
