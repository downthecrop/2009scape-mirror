package org.runite.client;

import org.rs09.client.Linkable;
import org.rs09.client.rendering.Toolkit;

import java.util.Objects;

public final class Class3_Sub23 extends Linkable {

    static RSString aString_3080 = Class95.method1586();
    int anInt2531;
    int anInt2532;
    public static int[] anIntArray2533;
    static int anInt2535 = -2;
    static CacheIndex configurationsIndex_2536;
    static int anInt2537 = 0;
    static boolean[] aBooleanArray2538 = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false};
    int anInt2539;
    static Class3_Sub11[][] aClass3_Sub11ArrayArray2542;


   static RSString method407(int var0, boolean var1, int var2, long var3) {
        try {
            RSString var6 = Unsorted.emptyString(0);
            if (var3 < 0) {
                var3 = -var3;
                Objects.requireNonNull(var6).append(RSString.parse(")2"));
            }

            RSString var8 = TextCore.aString_1880;
            RSString var7 = TextCore.aString_341;
            if (var0 == 1) {
                var8 = TextCore.aString_341;
                var7 = TextCore.aString_1880;
            }

            if (var0 == 2) {
                var7 = TextCore.aString_1880;
                var8 = aString_3080;
            }

            if (var0 == 3) {
                var8 = TextCore.aString_341;
                var7 = TextCore.aString_1880;
            }

            RSString var10 = Unsorted.emptyString(0);

            int var11;
            for (var11 = 0; var2 > var11; ++var11) {
                Objects.requireNonNull(var10).append(RSString.stringAnimator((int) (var3 % 10L)));
                var3 /= 10L;
            }

            var11 = 0;
            RSString var9;
            if (var3 == 0L) {
                var9 = RSString.parse("0");
            } else {
                RSString var12;
                for (var12 = Unsorted.emptyString(0); var3 > 0L; var3 /= 10L) {
                    if (var1 && var11 != 0 && var11 % 3 == 0) {
                        var12.append(var8);
                    }

                    Objects.requireNonNull(var12).append(RSString.stringAnimator((int) (var3 % 10L)));
                    ++var11;
                }

                var9 = var12;
            }

            if (Objects.requireNonNull(var10).length() > 0) {
                var10.append(var7);
            }

            return RSString.stringCombiner(new RSString[]{var6, Objects.requireNonNull(var9).method1544(true), var10.method1544(true)});
        } catch (RuntimeException var13) {
            throw ClientErrorException.clientError(var13, "oj.C(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + 2 + ')');
        }
    }

    static int method408(int var0, byte var1, int var2, int[][] var3, int var4, int var5) {
        try {
            int var6 = var0 * var3[1 + var4][var2] + (128 - var0) * var3[var4][var2] >> 7;
            int var7 = var3[var4][1 + var2] * (-var0 + 128) + var3[var4 - -1][var2 - -1] * var0 >> 7;
            return var6 * (128 + -var5) - -(var5 * var7) >> 7;
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "oj.A(" + var0 + ',' + var1 + ',' + var2 + ',' + (var3 != null ? "{...}" : "null") + ',' + var4 + ',' + var5 + ')');
        }
    }

}
