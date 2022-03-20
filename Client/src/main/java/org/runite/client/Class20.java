package org.runite.client;

final class Class20 {

    static short[][] aShortArrayArray435 = new short[][]{{(short) 6554, (short) 115, (short) 10304, (short) 28, (short) 5702, (short) 7756, (short) 5681, (short) 4510, (short) -31835, (short) 22437, (short) 2859, (short) -11339, (short) 16, (short) 5157, (short) 10446, (short) 3658, (short) -27314, (short) -21965, (short) 472, (short) 580, (short) 784, (short) 21966, (short) 28950, (short) -15697, (short) -14002}, {(short) 9104, (short) 10275, (short) 7595, (short) 3610, (short) 7975, (short) 8526, (short) 918, (short) -26734, (short) 24466, (short) 10145, (short) -6882, (short) 5027, (short) 1457, (short) 16565, (short) -30545, (short) 25486, (short) 24, (short) 5392, (short) 10429, (short) 3673, (short) -27335, (short) -21957, (short) 192, (short) 687, (short) 412, (short) 21821, (short) 28835, (short) -15460, (short) -14019}, new short[0], new short[0], new short[0]};
    static RSInterface aClass11_439;


    static void method907(int var0, int var1, int var2, int var3, int var4, GameObject var5, int var6, long var7, boolean var9) {
        if (var5 == null) {
        } else {
            int var10 = var1 - var4;
            int var11 = var2 - var4;
            int var12 = var1 + var4;
            int var13 = var2 + var4;
            if (var9) {
                if (var6 > 640 && var6 < 1408) {
                    var13 += 128;
                }

                if (var6 > 1152 && var6 < 1920) {
                    var12 += 128;
                }

                if (var6 > 1664 || var6 < 384) {
                    var11 -= 128;
                }

                if (var6 > 128 && var6 < 896) {
                    var10 -= 128;
                }
            }

            var10 /= 128;
            var11 /= 128;
            var12 /= 128;
            var13 /= 128;
            Scenery.method1189(var0, var10, var11, var12 - var10 + 1, var13 - var11 + 1, var1, var2, var3, var5, var6, true, var7);
        }
    }

    static void method909(RSInterface iface) {
        if (Class3_Sub23.anInt2535 == iface.anInt204) {
            Unsorted.aBooleanArray3674[iface.anInt292] = true;
        }
    }

}
