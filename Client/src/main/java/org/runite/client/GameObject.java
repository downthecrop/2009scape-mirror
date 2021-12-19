package org.runite.client;

abstract class GameObject {

    static AbstractIndexedSprite[] aClass109Array1831;
    static RSInterface[][] interfaces1834;
    static boolean aBoolean1837 = false;
    static int[] anIntArray1838;
    static SoftwareSprite[] aSoftwareSpriteArray1839;


    static void method1859(double var0) {
        try {
            if (Class70.aDouble1050 != var0) {
                for (int var3 = 0; 256 > var3; ++var3) {
                    int var4 = (int) (255.0D * Math.pow((double) var3 / 255.0D, var0));
                    BufferedDataStream.anIntArray3804[var3] = var4 > 255 ? 255 : var4;
                }

                Class70.aDouble1050 = var0;
            }

        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "th.KC(" + var0 + ',' + 32258 + ')');
        }
    }

    static void graphicsSettings(boolean var0, int var1, int var3, int var4) {
        try {
            Class53.aLong866 = 0L;
            int var5 = Class83.getWindowType();
            if (var1 == 3 || 3 == var5) {
                var0 = true;
            }

            if (Signlink.osName.startsWith("mac") && var1 > 0) {
                var0 = true;
            }

            boolean var6 = false;
            if (var5 > 0 != var1 > 0) {
                var6 = true;
            }

            if (var0 && var1 > 0) {
                var6 = true;
            }

            Unsorted.method598(var0, var1, var6, var5, false, var3, var4);
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "th.EC(" + var0 + ',' + var1 + ',' + -8914 + ',' + var3 + ',' + var4 + ')');
        }
    }

    static int method1863(int var0, int var1, int var3, int var4, int var5, int var6) {
        try {
            int var7;
            if ((1 & var5) == 1) {
                var7 = var0;
                var0 = var3;
                var3 = var7;
            }

            var1 &= 3;
            return var1 == 0 ? var6 : (1 != var1 ? (var1 != 2 ? var4 : -var3 + 1 + -var6 + 7) : -var4 + 7 + -var0 - -1);
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "th.JC(" + var0 + ',' + var1 + ',' + (byte) 126 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    static void method1864(CacheIndex var2, Class3_Sub28_Sub17_Sub1 var3, CacheIndex var4) {
        try {
            Class139.aBoolean1827 = true;
            LinkableRSString.modelsIndex_2581 = var4;
            Class97.itemConfigIndex_1370 = var2;
            int var5 = Class97.itemConfigIndex_1370.method2121() - 1;
            TextureOperation39.itemDefinitionSize = Class97.itemConfigIndex_1370.getFileAmount(var5) + var5 * 256;
            ClientErrorException.aStringArray2119 = new RSString[]{null, null, null, null, TextCore.HasDrop};
            Unsorted.aStringArray2596 = new RSString[]{null, null, TextCore.HasTake, null, null};
            TextureOperation10.aClass3_Sub28_Sub17_Sub1_3440 = var3;
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "th.FC(" + true + ',' + (byte) -126 + ',' + (var2 != null ? "{...}" : "null") + ',' + (var3 != null ? "{...}" : "null") + ',' + (var4 != null ? "{...}" : "null") + ')');
        }
    }

    static void method1869(byte var0, int var1, int var2, int var3, int var4, int var5) {
        try {
            int var6 = var2 - var3;
            int var7 = var4 + -var5;
            if (var7 == 0) {
                if (var6 != 0) {
                    TextureOperation15.method244(var3, var5, var2, var1);
                }

            } else if (0 == var6) {
                TextureOperation14.method320(var1, var3, var4, (byte) -107, var5);
            } else {
                if (0 > var6) {
                    var6 = -var6;
                }

                if (var7 < 0) {
                    var7 = -var7;
                }

                boolean var9 = var6 > var7;
                int var10;
                int var11;
                if (var9) {
                    var10 = var5;
                    var5 = var3;
                    var11 = var4;
                    var3 = var10;
                    var4 = var2;
                    var2 = var11;
                }

                if (var4 < var5) {
                    var10 = var5;
                    var5 = var4;
                    var4 = var10;
                    var11 = var3;
                    var3 = var2;
                    var2 = var11;
                }

                var10 = var3;
                var11 = var4 - var5;
                int var12 = var2 + -var3;
                int var13 = -(var11 >> 1);
                int var14 = var2 <= var3 ? -1 : 1;
                if (var12 < 0) {
                    var12 = -var12;
                }

                int var15;
                if (var9) {
                    for (var15 = var5; var4 >= var15; ++var15) {
                        Class38.anIntArrayArray663[var15][var10] = var1;
                        var13 += var12;
                        if (var13 > 0) {
                            var10 += var14;
                            var13 -= var11;
                        }
                    }
                } else {
                    for (var15 = var5; var15 <= var4; ++var15) {
                        var13 += var12;
                        Class38.anIntArrayArray663[var10][var15] = var1;
                        if (var13 > 0) {
                            var10 += var14;
                            var13 -= var11;
                        }
                    }
                }

            }
        } catch (RuntimeException var16) {
            throw ClientErrorException.clientError(var16, "th.IC(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
        }
    }

    static void method1870() {
        try {
            Class101.aClass3_Sub24_Sub4_1421.method505((byte) -128);
            Unsorted.anInt154 = 1;
            Class101.musicIndex_1423 = null;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "th.GC(" + false + ')');
        }
    }

    GameObject method1861() {
        try {
            return this;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "th.JB(" + -50 + ',' + -10 + ',' + -50 + ')');
        }
    }

    boolean method1865() {
        try {
            return false;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "th.AB()");
        }
    }

    void method1866(GameObject var1, int var2, int var3, int var4, boolean var5) {
    }

    abstract void method1867(int var1, int var2, int var3, int var4, int var5);

    abstract void animate(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, int var11, Class127_Sub1 var12);

    abstract int method1871();

}
