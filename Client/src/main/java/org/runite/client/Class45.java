package org.runite.client;

public final class Class45 {

    static int[] anIntArray729 = new int[4096];
    static float aFloat730;
    public static int anInt733 = 0;
    static int anInt734 = 0;
    public static AbstractSprite aAbstractSprite_736;
    static boolean[] aBooleanArray3272;


    static void method1082(byte[] var0, int var1) {
        try {
            DataBuffer var2 = new DataBuffer(var0);
            var2.index = -2 + var0.length;
            Class95.anInt1338 = var2.readUnsignedShort();
            Unsorted.anIntArray3076 = new int[Class95.anInt1338];
            Class140_Sub7.anIntArray2931 = new int[Class95.anInt1338];
            Class164.anIntArray2048 = new int[Class95.anInt1338];
            aBooleanArray3272 = new boolean[Class95.anInt1338];
            Class163_Sub3.aByteArrayArray3005 = new byte[Class95.anInt1338][];
            Unsorted.anIntArray2591 = new int[Class95.anInt1338];
            Class163_Sub1.aByteArrayArray2987 = new byte[Class95.anInt1338][];
            var2.index = -(8 * Class95.anInt1338) + var0.length - 7;
            Class3_Sub15.anInt2426 = var2.readUnsignedShort();
            Class133.anInt1748 = var2.readUnsignedShort();
            int var3 = (var2.readUnsignedByte() & 0xFF) - -1;

            int var4;
            for (var4 = 0; Class95.anInt1338 > var4; ++var4) {
                Class164.anIntArray2048[var4] = var2.readUnsignedShort();
            }

            for (var4 = 0; Class95.anInt1338 > var4; ++var4) {
                Unsorted.anIntArray2591[var4] = var2.readUnsignedShort();
            }

            for (var4 = 0; Class95.anInt1338 > var4; ++var4) {
                Class140_Sub7.anIntArray2931[var4] = var2.readUnsignedShort();
            }

            for (var4 = 0; Class95.anInt1338 > var4; ++var4) {
                Unsorted.anIntArray3076[var4] = var2.readUnsignedShort();
            }

            var2.index = -(8 * Class95.anInt1338) + var0.length + -7 + 3 + -(var3 * 3);
            TextureOperation38.spritePalette = new int[var3];

            for (var4 = 1; var3 > var4; ++var4) {
                TextureOperation38.spritePalette[var4] = var2.readMedium();
                if (0 == TextureOperation38.spritePalette[var4]) {
                    TextureOperation38.spritePalette[var4] = 1;
                }
            }

            var2.index = 0;

            for (var4 = 0; var4 < Class95.anInt1338; ++var4) {
                int var5 = Class140_Sub7.anIntArray2931[var4];
                int var6 = Unsorted.anIntArray3076[var4];
                int var7 = var5 * var6;
                byte[] var8 = new byte[var7];
                boolean var10 = false;
                Class163_Sub1.aByteArrayArray2987[var4] = var8;
                byte[] var9 = new byte[var7];
                Class163_Sub3.aByteArrayArray3005[var4] = var9;
                int var11 = var2.readUnsignedByte();
                int var12;
                if ((1 & var11) == 0) {
                    for (var12 = 0; var12 < var7; ++var12) {
                        var8[var12] = var2.readSignedByte();
                    }

                    if ((2 & var11) != 0) {
                        for (var12 = 0; var7 > var12; ++var12) {
                            byte var16 = var9[var12] = var2.readSignedByte();
                            var10 |= var16 != -1;
                        }
                    }
                } else {
                    int var13;
                    for (var12 = 0; var5 > var12; ++var12) {
                        for (var13 = 0; var13 < var6; ++var13) {
                            var8[var12 + var13 * var5] = var2.readSignedByte();
                        }
                    }

                    if ((var11 & 2) != 0) {
                        for (var12 = 0; var5 > var12; ++var12) {
                            for (var13 = 0; var13 < var6; ++var13) {
                                byte var14 = var9[var5 * var13 + var12] = var2.readSignedByte();
                                var10 |= -1 != var14;
                            }
                        }
                    }
                }

                aBooleanArray3272[var4] = var10;
            }

        } catch (RuntimeException var15) {
            throw ClientErrorException.clientError(var15, "gk.B(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ')');
        }
    }

    static void method1083(byte var0) {
        try {
            Class43.anIntArray3107 = Unsorted.method62();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "gk.C(" + var0 + ')');
        }
    }

}
