package org.runite.client;

import org.rs09.client.data.NodeCache;

final class Class56 {

    static Class3_Sub26 aClass3_Sub26_884 = new Class3_Sub26(0, 0);
    static NodeCache aClass47_885 = new NodeCache(128);
    static RSInterface aClass11_886 = null;
    static int[] localPlayerIndexes = new int[2048];
    static Class106[] aClass106Array890;

    static int anInt893 = 0;

    static int method1186(int var1) {
        try {
            double var2 = (double) (255 & var1 >> 16) / 256.0D;
            double var4 = (double) (255 & var1 >> 8) / 256.0D;
            double var12 = 0.0D;
            double var6 = (double) (255 & var1) / 256.0D;
            double var8 = var2;
            double var14 = 0;
            double var10 = var2;
            if (var2 > var4) {
                var8 = var4;
            }

            if (var6 < var8) {
                var8 = var6;
            }

            if (var4 > var2) {
                var10 = var4;
            }

            if (var6 > var10) {
                var10 = var6;
            }

            double var16 = (var8 + var10) / 2.0D;
            if (var8 != var10) {
                if (0.5D > var16) {
                    var14 = (-var8 + var10) / (var8 + var10);
                }

                if (var16 >= 0.5D) {
                    var14 = (-var8 + var10) / (2.0D - var10 - var8);
                }

                if (var10 == var2) {
                    var12 = (var4 - var6) / (-var8 + var10);
                } else if (var4 == var10) {
                    var12 = 2.0D + (-var2 + var6) / (var10 - var8);
                } else if (var10 == var6) {
                    var12 = 4.0D + (-var4 + var2) / (-var8 + var10);
                }
            }

            int var19 = (int) (var14 * 256.0D);
            int var20 = (int) (256.0D * var16);
            var12 /= 6.0D;
            if (0 > var20) {
                var20 = 0;
            } else if (var20 > 255) {
                var20 = 255;
            }

            int var18 = (int) (var12 * 256.0D);
            if (var19 >= 0) {
                if (var19 > 255) {
                    var19 = 255;
                }
            } else {
                var19 = 0;
            }

            if (var20 <= 243) {
                if (var20 <= 217) {
                    if (var20 > 192) {
                        var19 >>= 2;
                    } else if (var20 > 179) {
                        var19 >>= 1;
                    }
                } else {
                    var19 >>= 3;
                }
            } else {
                var19 >>= 4;
            }

            return (var18 >> 2 << 10) + (var19 >> 5 << 7) + (var20 >> 1);
        } catch (RuntimeException var21) {
            throw ClientErrorException.clientError(var21, "ib.A(" + 0 + ',' + var1 + ')');
        }
    }

    static void method1188() {
        try {
            MouseListeningClass.anIntArray1920 = null;
            Class38_Sub1.anIntArrayArrayArray2609 = null;
            Unsorted.anIntArray2469 = null;
            Unsorted.aByteArrayArrayArray1328 = null;
            PacketParser.aByteArrayArrayArray81 = null;
            Class67.aByteArrayArrayArray1014 = null;
            Class158_Sub1.aByteArrayArrayArray1828 = null;
            Class3_Sub13_Sub36.aByteArrayArrayArray3430 = null;
            Unsorted.anIntArray1138 = null;
            Class129.anIntArray1695 = null;
            Class3_Sub31.anIntArray2606 = null;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "ib.D(" + -113 + ')');
        }
    }

    static boolean method1189(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, GameObject var8, int var9, boolean var10, long var11) {
        boolean var13 = Class44.anIntArrayArrayArray723 == Unsorted.anIntArrayArrayArray3605;
        int var14 = 0;

        int var16;
        for (int var15 = var1; var15 < var1 + var3; ++var15) {
            for (var16 = var2; var16 < var2 + var4; ++var16) {
                if (var15 < 0 || var16 < 0 || var15 >= Unsorted.anInt1234 || var16 >= Class3_Sub13_Sub15.anInt3179) {
                    return false;
                }

                Class3_Sub2 var17 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var15][var16];
                if (var17 != null && var17.anInt2223 >= 5) {//@splinter
                    return false;
                }
            }
        }

        Class25 var20 = new Class25();
        var20.aLong498 = var11;
        var20.anInt493 = var0;
        var20.anInt482 = var5;
        var20.anInt484 = var6;
        var20.anInt489 = var7;
        var20.aClass140_479 = var8;
        var20.anInt496 = var9;
        var20.anInt483 = var1;
        var20.anInt478 = var2;
        var20.anInt495 = var1 + var3 - 1;
        var20.anInt481 = var2 + var4 - 1;

        int var21;
        for (var16 = var1; var16 < var1 + var3; ++var16) {
            for (var21 = var2; var21 < var2 + var4; ++var21) {
                int var18 = 0;
                if (var16 > var1) {
                    ++var18;
                }

                if (var16 < var1 + var3 - 1) {
                    var18 += 4;
                }

                if (var21 > var2) {
                    var18 += 8;
                }

                if (var21 < var2 + var4 - 1) {
                    var18 += 2;
                }

                for (int var19 = var0; var19 >= 0; --var19) {
                    if (Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var19][var16][var21] == null) {
                        Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var19][var16][var21] = new Class3_Sub2(var19, var16, var21);
                    }
                }

                Class3_Sub2 var22 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var16][var21];
                var22.aClass25Array2221[var22.anInt2223] = var20;
                var22.anIntArray2237[var22.anInt2223] = var18;
                var22.anInt2228 |= var18;
                ++var22.anInt2223;
                if (var13 && Class3_Sub13_Sub9.anIntArrayArray3115[var16][var21] != 0) {
                    var14 = Class3_Sub13_Sub9.anIntArrayArray3115[var16][var21];
                }
            }
        }

        if (var13 && var14 != 0) {
            for (var16 = var1; var16 < var1 + var3; ++var16) {
                for (var21 = var2; var21 < var2 + var4; ++var21) {
                    if (Class3_Sub13_Sub9.anIntArrayArray3115[var16][var21] == 0) {
                        Class3_Sub13_Sub9.anIntArrayArray3115[var16][var21] = var14;
                    }
                }
            }
        }

        if (var10) {
            SequenceDefinition.aClass25Array1868[Unsorted.anInt3070++] = var20;
        }

        return true;
    }

}
