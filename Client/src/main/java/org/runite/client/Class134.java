package org.runite.client;

final class Class134 {

    static LinkedList aLinkedList_1758 = new LinkedList();
    static int anInt1759 = 0;
    static int anInt1761 = -1;
    static int anInt1762 = 0;
    static int[][] anIntArrayArray1763 = new int[][]{{0, 128, 0, 0, 128, 0, 128, 128}, {0, 128, 0, 0, 128, 0}, {0, 0, 64, 128, 0, 128}, {128, 128, 64, 128, 128, 0}, {0, 0, 128, 0, 128, 128, 64, 128}, {0, 128, 0, 0, 128, 0, 64, 128}, {64, 128, 0, 128, 0, 0, 64, 0}, {0, 0, 64, 0, 0, 64}, {128, 0, 128, 128, 0, 128, 0, 64, 64, 0}, {0, 128, 0, 0, 32, 64, 64, 96, 128, 128}, {0, 0, 128, 0, 128, 128, 64, 96, 32, 64}, {0, 0, 128, 0, 96, 32, 32, 32}};
    static long[] aLongArray1766 = new long[32];


    static void method1808(int var0, boolean var1, int var3, boolean var4) {
        try {
            Unsorted.method1047(var0, var3, Unsorted.aClass44_Sub1Array3201.length - 1, var4, 0, var1);

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "sh.C(" + var0 + ',' + var1 + ',' + (byte) 30 + ',' + var3 + ',' + var4 + ')');
        }
    }

    static void method1809(int var0, long[] var1, int var2, int var3, Object[] var4) {
        try {
            if (var2 < 85) {
                aLinkedList_1758 = null;
            }

            if (var0 > var3) {
                int var6 = var3;
                int var5 = (var3 - -var0) / 2;
                long var7 = var1[var5];
                var1[var5] = var1[var0];
                var1[var0] = var7;
                Object var9 = var4[var5];
                var4[var5] = var4[var0];
                var4[var0] = var9;

                for (int var10 = var3; var0 > var10; ++var10) {
                    if (var7 + (long) (1 & var10) > var1[var10]) {
                        long var11 = var1[var10];
                        var1[var10] = var1[var6];
                        var1[var6] = var11;
                        Object var13 = var4[var10];
                        var4[var10] = var4[var6];
                        var4[var6++] = var13;
                    }
                }

                var1[var0] = var1[var6];
                var1[var6] = var7;
                var4[var0] = var4[var6];
                var4[var6] = var9;
                method1809(-1 + var6, var1, 107, var3, var4);
                method1809(var0, var1, 89, var6 - -1, var4);
            }

        } catch (RuntimeException var14) {
            throw ClientErrorException.clientError(var14, "sh.B(" + var0 + ',' + (var1 != null ? "{...}" : "null") + ',' + var2 + ',' + var3 + ',' + (var4 != null ? "{...}" : "null") + ')');
        }
    }

}
