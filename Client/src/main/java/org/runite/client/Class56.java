package org.runite.client;

import org.rs09.client.data.NodeCache;

public final class Class56 {

    static Class3_Sub26 aClass3_Sub26_884 = new Class3_Sub26(0, 0);
    static NodeCache aClass47_885 = new NodeCache(128);
    static RSInterface aClass11_886 = null;
    public static int[] localPlayerIndexes = new int[2048];
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
            TextureOperation36.aByteArrayArrayArray3430 = null;
            Unsorted.anIntArray1138 = null;
            Class129.anIntArray1695 = null;
            Class3_Sub31.anIntArray2606 = null;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "ib.D(" + -113 + ')');
        }
    }

}
