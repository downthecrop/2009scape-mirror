package org.runite.client;

import org.rs09.client.data.ReferenceCache;

final class Class154 {

    static int anInt1957;
    static int[] anIntArray1960 = new int[14];
    static ReferenceCache aReferenceCache_1964 = new ReferenceCache(5);
    static int anInt1966 = -1;

    static void method2146(int var0, int var1, int var2, int var3, GameObject var4, GameObject var5, int var6, int var7, long var8) {
        if (var4 != null || var5 != null) {
            Class70 var10 = new Class70();
            var10.aLong1048 = var8;
            var10.anInt1054 = var1 * 128 + 64;
            var10.anInt1045 = var2 * 128 + 64;
            var10.anInt1057 = var3;
            var10.aClass140_1049 = var4;
            var10.aClass140_1052 = var5;
            var10.anInt1055 = var6;
            var10.anInt1059 = var7;

            for (int var11 = var0; var11 >= 0; --var11) {
                if (Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var11][var1][var2] == null) {
                    Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var11][var1][var2] = new Class3_Sub2(var11, var1, var2);
                }
            }

            Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2].aClass70_2234 = var10;
        }
    }

    static Class70 method2147(int var0, int var1, int var2) {
        Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
        return var3 == null ? null : var3.aClass70_2234;
    }

    static RSString method2148(int var0) {
        try {
            if (999999999 <= var0) {

                return TextCore.aClass94_1687;
            } else {
                return RSString.stringAnimator(var0);
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "vf.C(" + var0 + ',' + (byte) -78 + ')');
        }
    }

}
