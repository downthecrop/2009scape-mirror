package org.runite.client;

import java.util.Objects;

final class Class46 {

    static CacheIndex aClass153_737;
    static int anInt740;
    static int anInt741;


    static void method1087(int var0, int var1) {
        try {
            Class3_Sub30_Sub1.method819();
            Class3_Sub13_Sub17.method252();
            if (var0 < 38) {
                anInt741 = 118;
            }

            int var2 = Objects.requireNonNull(Class145.method2076(var1)).anInt556;
            if (var2 != 0) {
                int var3 = ItemDefinition.ram[var1];
                if (6 == var2) {
                    Unsorted.anInt688 = var3;
                }

                if (var2 == 5) {
                    Unsorted.anInt998 = var3;
                }

                if (var2 == 9) {
                    Unsorted.anInt15 = var3;
                }

            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "gl.A(" + var0 + ',' + var1 + ')');
        }
    }

}
