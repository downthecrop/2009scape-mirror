package org.runite.client;

final class Class32 {

    static int anInt590 = -1;
    static int anInt1744 = 0;


    static RSString method992(DataBuffer var0) {
        try {

            return Class140_Sub7.method2033(var0);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "fc.C(" + (var0 != null ? "{...}" : "null") + ',' + 29488 + ')');
        }
    }

    static int method993(int var0, int var2) {
        try {
            int var3 = -128 + (Class65.method1234(4, var2 + '\ub135', var0 - -91923) + (Class65.method1234(2, var2 + 10294, '\u93bd' + var0) + -128 >> 1) - -(Class65.method1234(1, var2, var0) + -128 >> 2));
            var3 = 35 + (int) (0.3D * (double) var3);
            if (var3 >= 10) {
                if (var3 > 60) {
                    var3 = 60;
                }
            } else {
                var3 = 10;
            }

            return var3;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "fc.B(" + var0 + ',' + 125 + ',' + var2 + ')');
        }
    }

    static void method995() {
        int var0;
        int var1;
        int var2;
        if (Unsorted.aClass3_Sub2ArrayArrayArray4070 != null) {
            for (var0 = 0; var0 < Unsorted.aClass3_Sub2ArrayArrayArray4070.length; ++var0) {
                for (var1 = 0; var1 < Unsorted.anInt1234; ++var1) {
                    for (var2 = 0; var2 < Class3_Sub13_Sub15.anInt3179; ++var2) {
                        Unsorted.aClass3_Sub2ArrayArrayArray4070[var0][var1][var2] = null;
                    }
                }
            }
        }

        Client.aClass3_Sub11ArrayArray2199 = null;
        if (Class166.aClass3_Sub2ArrayArrayArray2065 != null) {
            for (var0 = 0; var0 < Class166.aClass3_Sub2ArrayArrayArray2065.length; ++var0) {
                for (var1 = 0; var1 < Unsorted.anInt1234; ++var1) {
                    for (var2 = 0; var2 < Class3_Sub13_Sub15.anInt3179; ++var2) {
                        Class166.aClass3_Sub2ArrayArrayArray2065[var0][var1][var2] = null;
                    }
                }
            }
        }

        Class3_Sub13_Sub28.aClass3_Sub11ArrayArray3346 = null;
        Class3_Sub4.anInt2249 = 0;
        if (Class3_Sub28_Sub8.aClass113Array3610 != null) {
        }

        if (SequenceDefinition.aClass25Array1868 != null) {
            for (var0 = 0; var0 < Unsorted.anInt3070; ++var0) {
                SequenceDefinition.aClass25Array1868[var0] = null;
            }

            Unsorted.anInt3070 = 0;
        }

        if (Unsorted.aClass25Array4060 != null) {
            for (var0 = 0; var0 < Unsorted.aClass25Array4060.length; ++var0) {
                Unsorted.aClass25Array4060[var0] = null;
            }
        }

    }

    static void method996() {
        try {

            KeyboardListener var1 = Class3_Sub13_Sub3.aClass148_3049;
            synchronized (var1) {
                Class3_Sub28_Sub9.anInt3620 = Class134.anInt1762;
                ++Class3_Sub13_Sub33.anInt3398;
                int var2;
                if (KeyboardListener.anInt2384 < 0) {
                    for (var2 = 0; var2 < 112; ++var2) {
                        ObjectDefinition.aBooleanArray1490[var2] = false;
                    }

                    KeyboardListener.anInt2384 = anInt1744;
                } else {
                    while (KeyboardListener.anInt2384 != anInt1744) {
                        var2 = Unsorted.anIntArray2952[anInt1744];
                        anInt1744 = 127 & 1 + anInt1744;
                        if (0 <= var2) {
                            ObjectDefinition.aBooleanArray1490[var2] = true;
                        } else {
                            ObjectDefinition.aBooleanArray1490[~var2] = false;
                        }
                    }
                }

                Class134.anInt1762 = Class25.anInt491;
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "fc.E(" + -43 + ')');
        }
    }

}
