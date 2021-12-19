package org.runite.client;

final class Class60 {

    static int anInt930;
    static int anInt932;
    static int anInt934;
    static int anInt936;

    static void method1208(byte var0, int var1) {
        try {
            if (-1 != var1) {
                if (Unsorted.aBooleanArray1703[var1]) {
                    Unsorted.interfacesIndex_3361.method2128(var1);
                    if (null != GameObject.interfaces1834[var1]) {
                        boolean var2 = true;

                        for (int var3 = 0; GameObject.interfaces1834[var1].length > var3; ++var3) {
                            if (GameObject.interfaces1834[var1][var3] != null) {
                                if (GameObject.interfaces1834[var1][var3].type == 2) {
                                    var2 = false;
                                } else {
                                    GameObject.interfaces1834[var1][var3] = null;
                                }
                            }
                        }

                        if (var2) {
                            GameObject.interfaces1834[var1] = null;
                        }

                        Unsorted.aBooleanArray1703[var1] = false;
                    }
                }
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ig.B(" + var0 + ',' + var1 + ')');
        }
    }

}
