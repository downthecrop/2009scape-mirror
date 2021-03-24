package org.runite.client;

import org.rs09.client.data.ReferenceCache;

public final class Class159 {

    static ReferenceCache aReferenceCache_2016 = new ReferenceCache(100);
    static int[] anIntArray2017 = new int[]{1, 2, 4, 8};
    static int anInt2020 = 0;
    static int[] anIntArray2021 = new int[2];
    public static int localPlayerCount = 0;
    static int anInt2023 = 0;
    static int anInt2024 = 0;
    static int[] anIntArray1681;
    static int anInt1740 = 0;


    static Class12 method2193(int var0, int var1, int var2) {
        Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
        if (var3 == null) {
            return null;
        } else {
            Class12 var4 = var3.aClass12_2230;
            var3.aClass12_2230 = null;
            return var4;
        }
    }

    static boolean method2194() {
        try {
            if (Unsorted.paramJavaScriptEnabled) {
                try {
                    return !(Boolean) TextCore.aClass94_3209.method1577(Class38.signlink.gameApplet);
                } catch (Throwable var2) {
                }
            }

            return true;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "vl.B(" + 255 + ')');
        }
    }

    static void method2195(int var0) {
        try {
            short var2 = 256;
            if (var0 > var2) {
                var0 = var2;
            }

            if (var0 > 10) {
                var0 = 10;
            }

            Class72.anInt1071 += var0 * 128;
            int var3;
            if (Class161.anIntArray2026.length < Class72.anInt1071) {
                Class72.anInt1071 -= Class161.anIntArray2026.length;
                var3 = (int) (12.0D * Math.random());
                TextureOperation30.method215((byte) -119, Class163_Sub2_Sub1.aClass109_Sub1Array4027[var3]);
            }

            var3 = 0;
            int var5 = (var2 + -var0) * 128;
            int var4 = 128 * var0;

            int var6;
            int var7;
            for (var6 = 0; var6 < var5; ++var6) {
                var7 = anIntArray1681[var3 - -var4] - var0 * Class161.anIntArray2026[-1 + Class161.anIntArray2026.length & Class72.anInt1071 + var3] / 6;
                if (0 > var7) {
                    var7 = 0;
                }

                anIntArray1681[var3++] = var7;
            }

            int var8;
            int var9;
            for (var6 = var2 + -var0; var2 > var6; ++var6) {
                var7 = var6 * 128;

                for (var8 = 0; 128 > var8; ++var8) {
                    var9 = (int) (100.0D * Math.random());
                    if (var9 < 50 && var8 > 10 && var8 < 118) {
                        anIntArray1681[var8 + var7] = 255;
                    } else {
                        anIntArray1681[var8 + var7] = 0;
                    }
                }
            }

            for (var6 = 0; var2 + -var0 > var6; ++var6) {
                Class3_Sub28_Sub5.anIntArray3592[var6] = Class3_Sub28_Sub5.anIntArray3592[var6 - -var0];
            }

            for (var6 = var2 - var0; var2 > var6; ++var6) {
                Class3_Sub28_Sub5.anIntArray3592[var6] = (int) (Math.sin((double) Class1.anInt57 / 14.0D) * 16.0D + 14.0D * Math.sin((double) Class1.anInt57 / 15.0D) + 12.0D * Math.sin((double) Class1.anInt57 / 16.0D));
                ++Class1.anInt57;
            }

            anInt1740 += var0;
            var6 = (var0 - -(1 & Class44.anInt719)) / 2;
            if (var6 > 0) {
                for (var7 = 0; anInt1740 > var7; ++var7) {
                    var8 = 2 + (int) (124.0D * Math.random());
                    var9 = (int) (128.0D * Math.random()) + 128;
                    anIntArray1681[var8 - -(var9 << 7)] = 192;
                }

                anInt1740 = 0;

                int var10;
                for (var7 = 0; var7 < var2; ++var7) {
                    var9 = var7 * 128;
                    var8 = 0;

                    for (var10 = -var6; var10 < 128; ++var10) {
                        if (128 > var6 + var10) {
                            var8 += anIntArray1681[var9 + (var10 - -var6)];
                        }

                        if (-1 + -var6 + var10 >= 0) {
                            var8 -= anIntArray1681[-var6 + -1 + var10 + var9];
                        }

                        if (0 <= var10) {
                            BufferedDataStream.anIntArray3805[var10 + var9] = var8 / (1 + var6 * 2);
                        }
                    }
                }

                for (var7 = 0; 128 > var7; ++var7) {
                    var8 = 0;

                    for (var9 = -var6; var2 > var9; ++var9) {
                        var10 = var9 * 128;
                        if (var2 > var9 + var6) {
                            var8 += BufferedDataStream.anIntArray3805[var6 * 128 + (var7 - -var10)];
                        }

                        if (0 <= var9 - var6 - 1) {
                            var8 -= BufferedDataStream.anIntArray3805[-((1 + var6) * 128) + (var7 - -var10)];
                        }

                        if (var9 >= 0) {
                            anIntArray1681[var10 + var7] = var8 / (var6 * 2 - -1);
                        }
                    }
                }
            }

        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "vl.E(" + var0 + ',' + 0 + ')');
        }
    }

//   static void method2196() {
//      try {
//         Class3_Sub13_Sub34.aReferenceCache_3412.clearSoftReferences();
//
//         Class3_Sub13_Sub31.aReferenceCache_3369.clearSoftReferences();
//      } catch (RuntimeException var2) {
//         throw ClientErrorException.clientError(var2, "vl.D(" + 128 + ')');
//      }
//   }

}
