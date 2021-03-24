package org.runite.client;

final class Class77 {

    static int anInt1111;


    static LDIndexedSprite method1364() {
        try {
            LDIndexedSprite var1 = new LDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], Unsorted.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Unsorted.anIntArray3076[0], Class163_Sub1.aByteArrayArray2987[0], TextureOperation38.spritePalette);

            Class39.method1035((byte) 127);
            return var1;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "kh.B(" + (byte) 82 + ')');
        }
    }

    static void method1367(int var0, int var1, int var2, byte var3, int var4, int var5) {
        try {
            TextureOperation18.method282(Class38.anIntArrayArray663[var2++], var5, 92, var1, var0);
            TextureOperation18.method282(Class38.anIntArrayArray663[var4--], var5, 97, var1, var0);
            if (var3 >= 23) {
                for (int var6 = var2; var4 >= var6; ++var6) {
                    int[] var7 = Class38.anIntArrayArray663[var6];
                    var7[var5] = var7[var1] = var0;
                }

            }
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "kh.A(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
        }
    }

    static void method1368() {
        try {
            TextureOperation12.outgoingBuffer.putOpcode(104);
            TextureOperation12.outgoingBuffer.writeLong(0L);
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "kh.D(" + -90 + ')');
        }
    }

}
