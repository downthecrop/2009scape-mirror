package org.runite.client;

import org.rs09.client.Linkable;

public final class LinkableRSString extends Linkable {

    static int anInt2579 = 1;
    static RSString[] aClass94Array2580 = new RSString[100];
    static CacheIndex aClass153_2581;
    public static int anInt2582 = 0;
    static boolean isDynamicSceneGraph = false;
    public RSString value;
    static int anInt2587;
    static GameShell anApplet_Sub1_2588 = null;
    static int anInt2589 = 0;


    static void method727() {
        try {
            KeyboardListener.aReferenceCache_1911.clear();
            Unsorted.aReferenceCache_1131.clear();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "sj.O(" + 91 + ')');
        }
    }

    static void method728() {
        try {

            try {
                if (Unsorted.anInt154 == 1) {
                    int var1 = Class101.aClass3_Sub24_Sub4_1421.method499();
                    if (var1 > 0 && Class101.aClass3_Sub24_Sub4_1421.method473(-124)) {
                        var1 -= GraphicDefinition.anInt546;
                        if (var1 < 0) {
                            var1 = 0;
                        }

                        Class101.aClass3_Sub24_Sub4_1421.method506(var1);
                        return;
                    }

                    Class101.aClass3_Sub24_Sub4_1421.method505((byte) -128);
                    Class101.aClass3_Sub24_Sub4_1421.method485();
                    NPC.aClass3_Sub27_1154 = null;
                    QuickChatDefinition.aClass83_3579 = null;
                    if (Class101.aClass153_1423 == null) {
                        Unsorted.anInt154 = 0;
                    } else {
                        Unsorted.anInt154 = 2;
                    }
                }
            } catch (Exception var2) {
                var2.printStackTrace();
                Class101.aClass3_Sub24_Sub4_1421.method505((byte) -127);
                Class101.aClass153_1423 = null;
                NPC.aClass3_Sub27_1154 = null;
                Unsorted.anInt154 = 0;
                QuickChatDefinition.aClass83_3579 = null;
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sj.A(" + false + ')');
        }
    }

    static int method729(byte var0, int var1, int var2) {
        try {
            if (var0 > -32) {
                return 88;
            } else if (var1 == -2) {
                return 12345678;
            } else if (var1 == -1) {
                if (2 > var2) {
                    var2 = 2;
                } else if (var2 > 126) {
                    var2 = 126;
                }

                return var2;
            } else {
                var2 = (127 & var1) * var2 >> 7;
                if (var2 < 2) {
                    var2 = 2;
                } else if (var2 > 126) {
                    var2 = 126;
                }

                return (var1 & '\uff80') - -var2;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "sj.E(" + var0 + ',' + var1 + ',' + var2 + ')');
        }
    }

    static void method730(int var0, int var1, int var3, int var4, int var5) {
        try {
            if (Class101.anInt1425 <= var0 && var4 <= Class3_Sub28_Sub18.anInt3765 && Class159.anInt2020 <= var5 && Class57.anInt902 >= var3) {
                WaterfallShader.method1632(95, var3, var4, var5, var0, var1);
            } else {
                Unsorted.method1525(var1, var4, var5, var0, var3);
            }

        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "sj.R(" + var0 + ',' + var1 + ',' + (byte) 121 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
        }
    }

    static void method731(CacheIndex var0) {
        try {
            TextureOperation20.aClass153_3154 = var0;
            Class95.anInt1344 = TextureOperation20.aClass153_3154.getFileAmount(16);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sj.B(" + (var0 != null ? "{...}" : "null") + ',' + (byte) -113 + ')');
        }
    }

    public LinkableRSString() {
    }

    static void method734(RSString var1) {
        try {
            Class163_Sub2.paramSettings = var1;
            if (null != Class38.gameSignlink.gameApplet) {
                try {
                    RSString var2 = TextCore.aClass94_2044.getParamValue(Class38.gameSignlink.gameApplet);
                    RSString var3 = TextCore.aClass94_1885.getParamValue(Class38.gameSignlink.gameApplet);
                    RSString var4 = RSString.stringCombiner(new RSString[]{var2, TextCore.aClass94_1151, var1, TextCore.aClass94_2074, var3});
                    if (0 == var1.length()) {
                        var4 = RSString.stringCombiner(new RSString[]{var4, TextCore.HasAgeExpire});
                    } else {
                        var4 = RSString.stringCombiner(new RSString[]{var4, TextCore.HasExpires, Class15.method894(94608000000L + TimeUtils.time()), TextCore.HasMaxAge, QuickChatDefinition.method612(94608000L)});
                    }

                    RSString.stringCombiner(new RSString[]{TextCore.aClass94_1694, var4, TextCore.aClass94_1698}).method1554(Class38.gameSignlink.gameApplet);
                } catch (Throwable var5) {
                }

            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "sj.F(" + 0 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static void method736(int var0, int var1) {
        try {
            if (var1 <= 61) {
                method736(-60, -93);
            }

            if (Unsorted.anInt154 == 0) {
                Class101.aClass3_Sub24_Sub4_1421.method506(var0);
            } else {
                TextureOperation36.anInt3423 = var0;
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sj.P(" + var0 + ',' + var1 + ')');
        }
    }

    public LinkableRSString(RSString var1) {
        try {
            this.value = var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sj.<init>(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

}
