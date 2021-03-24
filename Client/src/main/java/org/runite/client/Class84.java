package org.runite.client;

public final class Class84 {

    static int[][] anIntArrayArray1160 = new int[104][104];
    static LinkedList aLinkedList_1162 = new LinkedList();
    public static int[] anIntArray1163 = new int[1000];
    static int anInt1164 = 0;
    static Class3_Sub28_Sub16_Sub2 aClass3_Sub28_Sub16_Sub2_1381;
    static int[] anIntArray1729 = new int[]{12543016, 15504954, 15914854, 16773818};

    static void method1417() {
        try {
            if (Class143.gameStage == 10 && HDToolKit.highDetail) {
                Class117.method1719(28);
            }

            if (Class143.gameStage == 30) {
                Class117.method1719(25);
            }

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "lf.D(" + ')');
        }
    }

    static void method1418(CacheIndex var1) {
        try {
            Class163_Sub2_Sub1.aClass109_Sub1Array4027 = Unsorted.method619((byte) 65, NPC.anInt4001, var1);
            Class52.anIntArray861 = new int[256];

            int var2;
            for (var2 = 0; var2 < 3; ++var2) {
                int var4 = (anIntArray1729[1 + var2] & 16711680) >> 16;
                float var3 = (float) ((anIntArray1729[var2] & 16711680) >> 16);
                float var6 = (float) (anIntArray1729[var2] >> 8 & 0xFF);
                float var9 = (float) (anIntArray1729[var2] & 0xFF);
                float var5 = ((float) var4 - var3) / 64.0F;
                int var7 = (anIntArray1729[var2 + 1] & 65280) >> 8;
                float var8 = (-var6 + (float) var7) / 64.0F;
                int var10 = anIntArray1729[var2 + 1] & 0xFF;
                float var11 = ((float) var10 - var9) / 64.0F;

                for (int var12 = 0; var12 < 64; ++var12) {
                    Class52.anIntArray861[var12 + 64 * var2] = TextureOperation3.bitwiseOr((int) var9, TextureOperation3.bitwiseOr((int) var6 << 8, (int) var3 << 16));
                    var6 += var8;
                    var9 += var11;
                    var3 += var5;
                }
            }

            for (var2 = 192; var2 < 255; ++var2) {
                Class52.anIntArray861[var2] = anIntArray1729[3];
            }

            Class161.anIntArray2026 = new int[32768];
            Unsorted.anIntArray49 = new int[32768];
            TextureOperation30.method215((byte) -89, null);
            BufferedDataStream.anIntArray3805 = new int[32768];
            Class159.anIntArray1681 = new int[32768];
            aClass3_Sub28_Sub16_Sub2_1381 = new Class3_Sub28_Sub16_Sub2(128, 254);
        } catch (RuntimeException var13) {
            throw ClientErrorException.clientError(var13, "lf.E(" + -110 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static void method1420(int var0, int var1, int var2, int var3) {
        try {
            InterfaceWidget var5 = InterfaceWidget.getWidget(10, var0);
            var5.flagUpdate();
            var5.anInt3597 = var2;
            var5.anInt3598 = var3;
            var5.anInt3596 = var1;

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "lf.C(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + (byte) -85 + ')');
        }
    }

    static int method1421() {
        try {

            return ((TextureOperation17.aBoolean3184 ? 1 : 0) << 19) + (((Class38.aBoolean661 ? 1 : 0) << 16) + ((!Class128.aBoolean1685 ? 0 : 1) << 15) + ((!Class106.aBoolean1441 ? 0 : 1) << 13) + ((Class140_Sub6.aBoolean2910 ? 1 : 0) << 10) + ((Unsorted.aBoolean3275 ? 1 : 0) << 9) + ((RSInterface.aBoolean236 ? 1 : 0) << 7) + ((!Class25.aBoolean488 ? 0 : 1) << 6) + ((KeyboardListener.aBoolean1905 ? 1 : 0) << 5) + (((!Unsorted.aBoolean3665 ? 0 : 1) << 3) + (Unsorted.anInt3625 & 7) - (-((!Unsorted.aBoolean3604 ? 0 : 1) << 4) + -((WorldListEntry.aBoolean2623 ? 1 : 0) << 8)) - (-(Unsorted.anInt1137 << 11 & 6144) + -((CS2Script.anInt2453 == 0 ? 0 : 1) << 20) - (((Unsorted.anInt120 != 0 ? 1 : 0) << 21) + ((Sprites.anInt340 == 0 ? 0 : 1) << 22)))) - -(Class127_Sub1.method1757() << 23));
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "lf.F(" + -2 + ')');
        }
    }

}
