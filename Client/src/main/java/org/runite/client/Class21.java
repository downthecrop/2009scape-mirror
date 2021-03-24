package org.runite.client;

import org.rs09.client.config.GameConfig;


public final class Class21 {

    static boolean aBoolean440 = false;
    static int[] maskUpdateIndexes = new int[2048];
    static int anInt443;
    public static int anInt3537;
    public static int anInt3552;
    public static int anInt3395;
    public static int anInt1462;


    static void method912() {
        try {
            TextureOperation12.outgoingBuffer.index = 0;
            Class7.anInt2166 = -1;
            Class38_Sub1.aBoolean2615 = false;
            Unsorted.incomingPacketLength = 0;
            Class65.anInt987 = 0;
            Unsorted.menuOptionCount = 0;
            LinkableRSString.anInt2582 = -1;
            Class161.anInt2028 = 0;
            Class38_Sub1.anInt2617 = 0;
            Class24.anInt469 = -1;
            BufferedDataStream.incomingBuffer.index = 0;
            AbstractSprite.anInt3699 = 0;
            Unsorted.incomingOpcode = -1;

            int var1;
            for (var1 = 0; Unsorted.players.length > var1; ++var1) {
                if (null != Unsorted.players[var1]) {
                    Unsorted.players[var1].anInt2772 = -1;
                }
            }

            for (var1 = 0; NPC.npcs.length > var1; ++var1) {
                if (NPC.npcs[var1] != null) {
                    NPC.npcs[var1].anInt2772 = -1;
                }
            }

            Class3_Sub28_Sub9.method580((byte) 80);
            Class133.anInt1753 = 1;
            Class117.method1719(30);

            for (var1 = 0; var1 < 100; ++var1) {
                Unsorted.aBooleanArray3674[var1] = true;
            }

            TextureOperation9.method204(-3);
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "dh.F(" + false + ')');
        }
    }

    static Class118 method913() {
        try {
            try {
                return (Class118) Class.forName(GameConfig.PACKAGE_NAME + ".Class118_Sub1").newInstance();
            } catch (Throwable var2) {
                return null;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "dh.C(" + ')');
        }
    }

    static void method914(int interfaceId, int interfaceHash, int walkable) {
        try {
            Class3_Sub31 var4 = new Class3_Sub31();
            var4.anInt2603 = walkable;
            var4.anInt2602 = interfaceId;
            TextureOperation23.aHashTable_3208.put(interfaceHash, var4);
            TextureOperation20.method232(interfaceId);
            RSInterface var5 = Class7.getRSInterface(interfaceHash);
            if (var5 == null) {
                System.out.println("Invalid interface opened - [window=" + (interfaceHash >> 16) + ", child=" + (interfaceHash & 0xFF) + ", id=" + interfaceId + "]");
            } else {
                Class20.method909(var5);
            }

            if (null != TextureOperation27.aClass11_3087) {
                Class20.method909(TextureOperation27.aClass11_3087);
                TextureOperation27.aClass11_3087 = null;
            }

            int var6 = Unsorted.menuOptionCount;

            int var7;
            for (var7 = 0; var6 > var7; ++var7) {
                if (Unsorted.method73(TextureOperation27.aShortArray3095[var7])) {
                    Class3_Sub25.method509(var7);
                }
            }

            if (1 == Unsorted.menuOptionCount) {
                Class38_Sub1.aBoolean2615 = false;
                method1340(anInt1462, anInt3552, anInt3395, anInt3537);
            } else {
                method1340(anInt1462, anInt3552, anInt3395, anInt3537);
                var7 = FontType.bold.method682(RSString.parse(GameConfig.RCM_TITLE));

                for (int var8 = 0; Unsorted.menuOptionCount > var8; ++var8) {
                    int var9 = FontType.bold.method682(Unsorted.method802(var8));
                    if (var7 < var9) {
                        var7 = var9;
                    }
                }

                anInt3552 = 8 + var7;
                anInt3537 = 15 * Unsorted.menuOptionCount + (!Unsorted.aBoolean1951 ? 22 : 26);
            }

            if (var5 != null) {
                Unsorted.method2104(var5, false, 55);
            }

            TextureOperation24.method226(interfaceId);
            if (Class3_Sub28_Sub12.anInt3655 != -1) {
                Class3_Sub8.method124(6422 ^ 6509, 1, Class3_Sub28_Sub12.anInt3655);
            }

        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "dh.D(" + 6422 + ',' + interfaceId + ',' + interfaceHash + ',' + walkable + ')');
        }
    }

    static void method915(RSString var0) {
        try {
            int var2 = Class3_Sub28_Sub8.method576(var0);
            if (-1 != var2) {
                Unsorted.method565(Class119.aClass131_1624.aShortArray1727[var2], Class119.aClass131_1624.aShortArray1718[var2]);
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "dh.A(" + (var0 != null ? "{...}" : "null") + ',' + -1 + ')');
        }
    }

    static Class146 method916() {
        try {

            try {
                return (Class146) Class.forName(GameConfig.PACKAGE_NAME + ".MouseWheel").newInstance();
            } catch (Throwable var2) {
                return null;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "dh.E(" + (byte) 15 + ')');
        }
    }

    public static void method1340(int var0, int var1, int var3, int var4) {
        try {

            for (int var5 = 0; var5 < Class3_Sub28_Sub3.anInt3557; ++var5) {
                if (var0 < Class3_Sub28_Sub18.anIntArray3768[var5] + Class155.anIntArray1969[var5] && Class155.anIntArray1969[var5] < var1 + var0 && Class140_Sub4.anIntArray2794[var5] + Player.anIntArray3954[var5] > var3 && Player.anIntArray3954[var5] < var3 - -var4) {
                    Unsorted.aBooleanArray3674[var5] = true;
                }
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "kf.I(" + var0 + ',' + var1 + ',' + (byte) -40 + ',' + var3 + ',' + var4 + ')');
        }
    }
}
