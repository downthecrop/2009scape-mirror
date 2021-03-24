package org.runite.client;

public final class Class95 {

    static int anInt1336 = 0;
    static int anInt1338;
    static int anInt1343;
    static int anInt1344;


    static void method1583(int var0, int var1, int var2, int var4, int var5, int var6, int var7, int var8, int var9) {
        try {

            if (var1 == var0 && var2 == var9 && var8 == var7 && var5 == var6) {
                TextureOperation25.method330(var4, 3 + -111, var5, var0, var9, var7);
            } else {
                int var10 = var0;
                int var13 = 3 * var9;
                int var12 = 3 * var0;
                int var11 = var9;
                int var14 = var1 * 3;
                int var15 = 3 * var2;
                int var16 = 3 * var8;
                int var17 = var6 * 3;
                int var18 = var14 + var7 - (var16 + var0);
                int var20 = var16 - (var14 - -var14) - -var12;
                int var19 = var15 + -var17 + (var5 - var9);
                int var21 = var13 + -var15 + -var15 + var17;
                int var22 = var14 + -var12;
                int var23 = -var13 + var15;

                for (int var24 = 128; var24 <= 4096; var24 += 128) {
                    int var25 = var24 * var24 >> 12;
                    int var26 = var24 * var25 >> 12;
                    int var30 = var21 * var25;
                    int var28 = var26 * var19;
                    int var29 = var25 * var20;
                    int var27 = var18 * var26;
                    int var31 = var22 * var24;
                    int var33 = var0 - -(var29 + (var27 - -var31) >> 12);
                    int var32 = var24 * var23;
                    int var34 = var9 - -(var32 + var28 - -var30 >> 12);
                    TextureOperation25.method330(var4, -88, var34, var10, var11, var33);
                    var10 = var33;
                    var11 = var34;
                }
            }

        } catch (RuntimeException var35) {
            throw ClientErrorException.clientError(var35, "nb.B(" + var0 + ',' + var1 + ',' + var2 + ',' + 3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ')');
        }
    }

    static void method1584(int var0, int var1, int var2, int var3, int var5) {
        try {

            if (var5 == var1) {
                Class43.method1058(var5, var2, var0, var3);
            } else if (Class101.anInt1425 <= var2 - var5 && var2 + var5 <= Class3_Sub28_Sub18.anInt3765 && -var1 + var3 >= Class159.anInt2020 && Class57.anInt902 >= var3 - -var1) {
                Class3_Sub9.method135(var3, var2, var5, var1, var0);
            } else {
                Class3_Sub28_Sub5.method556(var0, var1, var5, (byte) -123, var2, var3);
            }
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "nb.E(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + -26571 + ',' + var5 + ')');
        }
    }

    public static int method1585(byte var0, int var1) {
        try {
            --var1;
            var1 |= var1 >>> 1;
            var1 |= var1 >>> 2;
            var1 |= var1 >>> 4;
            var1 |= var1 >>> 8;
            var1 |= var1 >>> 16;
            return 1 + var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "nb.C(" + var0 + ',' + var1 + ')');
        }
    }

    static RSString method1586() {
        try {
            RSString var2 = new RSString();

            var2.length = 1;
            var2.buffer = new byte[1];
            var2.buffer[0] = (byte) 160;
            return var2;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "nb.A(" + 23161 + ',' + 160 + ')');
        }
    }

}
