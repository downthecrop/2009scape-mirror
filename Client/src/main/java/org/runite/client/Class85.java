package org.runite.client;

final class Class85 {

    static boolean aBoolean1167 = false;
    static CacheIndex aClass153_1171;
    static int anInt1174 = 99;


    static void method1423(DataBuffer var1, RSString var2) {
        try {
            byte[] var4 = var2.method1568();
            var1.method768(var4.length);
            var1.index += TextureOperation16.aClass36_3112.method1015(var4.length, var1.buffer, var4, 0, var1.index);
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "lg.A(" + false + ',' + (var1 != null ? "{...}" : "null") + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    static AbstractIndexedSprite[] method1424(CacheIndex var0, int var3) {
        try {

            return Class75_Sub4.method1351(var0, 0, var3) ? TextureOperation36.method343() : null;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "lg.C(" + (var0 != null ? "{...}" : "null") + ',' + (byte) -12 + ',' + 0 + ',' + var3 + ')');
        }
    }

    static void method1425(int var0) {
        TextureOperation22.anInt3419 = var0;

        for (int var1 = 0; var1 < Unsorted.anInt1234; ++var1) {
            for (int var2 = 0; var2 < TextureOperation17.anInt3179; ++var2) {
                if (Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2] == null) {
                    Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2] = new Class3_Sub2(var0, var1, var2);
                }
            }
        }

    }

}
