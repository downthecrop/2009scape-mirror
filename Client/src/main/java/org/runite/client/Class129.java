package org.runite.client;

import org.rs09.client.rendering.Toolkit;

abstract class Class129 {

    static int[] anIntArray1690;
    static int anInt1692 = 0;
    static int[] anIntArray1693 = new int[128];
    static int[] anIntArray1695;


    static int method1765(int var0) {
        try {
            return var0 >>> 7;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "s.D(" + var0 + ')');
        }
    }

    static void method1768(int var0, int var2) {
        try {
            if (KeyboardListener.anInt1908 > 0) {
                Class159.method2195(KeyboardListener.anInt1908);
                KeyboardListener.anInt1908 = 0;
            }

            short var3 = 256;
            int var4 = 0;
            int var5 = Toolkit.JAVA_TOOLKIT.width * var2;
            int var6 = 0;

            for (int var7 = 1; var7 < var3 + -1; ++var7) {
                int var8 = (var3 - var7) * Class3_Sub28_Sub5.anIntArray3592[var7] / var3;
                if (0 > var8) {
                    var8 = 0;
                }

                var4 += var8;

                int var9;
                for (var9 = var8; var9 < 128; ++var9) {
                    int var11 = Toolkit.JAVA_TOOLKIT.getBuffer()[var5++ + var0];
                    int var10 = Class159.anIntArray1681[var4++];
                    if (var10 == 0) {
                        Class84.aSoftwareSprite_1381.anIntArray4081[var6++] = var11;
                    } else {
                        int var12 = 18 + var10;
                        if (var12 > 255) {
                            var12 = 255;
                        }

                        int var13 = 256 - var10 - 18;
                        if (var13 > 255) {
                            var13 = 255;
                        }

                        var10 = Class52.anIntArray861[var10];
                        Class84.aSoftwareSprite_1381.anIntArray4081[var6++] = Unsorted.bitwiseAnd(var13 * Unsorted.bitwiseAnd(var11, 16711935) + Unsorted.bitwiseAnd(16711935, var10) * var12, -16711936) - -Unsorted.bitwiseAnd(Unsorted.bitwiseAnd(var10, 65280) * var12 - -(Unsorted.bitwiseAnd(65280, var11) * var13), 16711680) >> 8;
                    }
                }

                for (var9 = 0; var9 < var8; ++var9) {
                    Class84.aSoftwareSprite_1381.anIntArray4081[var6++] = Toolkit.JAVA_TOOLKIT.getBuffer()[var0 + var5++];
                }

                var5 += Toolkit.JAVA_TOOLKIT.width + -128;
            }

            if (HDToolKit.highDetail) {
                Class22.method926(Class84.aSoftwareSprite_1381.anIntArray4081, var0, var2, Class84.aSoftwareSprite_1381.width, Class84.aSoftwareSprite_1381.height);
            } else {
                Class84.aSoftwareSprite_1381.method635(var0, var2);
            }

        } catch (RuntimeException var14) {
            throw ClientErrorException.clientError(var14, "s.F(" + var0 + ',' + 95 + ',' + var2 + ')');
        }
    }

    static void method1769(float[][] var0, byte[][] var1, byte[][] var2, Class43[] var3, int var4, int var5, float[][] var6, byte[][] var7, byte[][] var8, int[][] var9, float[][] var11) {
        try {
            for (int var12 = 0; var5 > var12; ++var12) {
                Class43 var13 = var3[var12];
                if (var13.anInt704 == var4) {
                    int var15 = 0;
                    Class37 var14 = new Class37();
                    int var16 = -var13.anInt698 + (var13.anInt703 >> 7);
                    int var17 = -var13.anInt698 + (var13.anInt708 >> 7);
                    if (var17 < 0) {
                        var15 -= var17;
                        var17 = 0;
                    }

                    int var18 = var13.anInt698 + (var13.anInt708 >> 7);
                    if (var18 > 103) {
                        var18 = 103;
                    }

                    int var19;
                    int var21;
                    short var20;
                    int var23;
                    int var22;
                    int var25;
                    int var35;
                    boolean var32;
                    for (var19 = var17; var19 <= var18; ++var19) {
                        var20 = var13.aShortArray706[var15];
                        var21 = var16 + (var20 >> 8);
                        var22 = -1 + var21 - -(255 & var20);
                        if (103 < var22) {
                            var22 = 103;
                        }

                        if (var21 < 0) {
                            var21 = 0;
                        }

                        for (var23 = var21; var23 <= var22; ++var23) {
                            int var24 = 255 & var1[var23][var19];
                            var25 = 255 & var8[var23][var19];
                            boolean var26 = false;
                            FloorOverlayDefinition var27;
                            int[] var29;
                            int[] var28;
                            if (0 == var24) {
                                if (var25 == 0) {
                                    continue;
                                }

                                var27 = FloorOverlayDefinition.getFile(var25 + -1);
                                if (var27.anInt2103 == -1) {
                                    continue;
                                }

                                if (var7[var23][var19] != 0) {
                                    var28 = Class134.anIntArrayArray1763[var7[var23][var19]];
                                    var14.anInt651 += 3 * (-2 + (var28.length >> 1));
                                    var14.anInt657 += var28.length >> 1;
                                    continue;
                                }
                            } else if (var25 != 0) {
                                var27 = FloorOverlayDefinition.getFile(var25 - 1);
                                byte var42;
                                if (var27.anInt2103 == -1) {
                                    var42 = var7[var23][var19];
                                    if (var42 != 0) {
                                        var29 = Class25.anIntArrayArray499[var42];
                                        var14.anInt651 += 3 * (-2 + (var29.length >> 1));
                                        var14.anInt657 += var29.length >> 1;
                                    }
                                    continue;
                                }

                                var42 = var7[var23][var19];
                                if (0 != var42) {
                                    var26 = true;
                                }
                            }

                            Class25 var40 = Class75.method1336(var4, var23, var19);
                            if (null != var40) {
                                int var41 = (int) (var40.aLong498 >> 14) & 63;
                                if (var41 == 9) {
                                    var29 = null;
                                    int var30 = 3 & (int) (var40.aLong498 >> 20);
                                    boolean var31;
                                    int var34;
                                    short var33;
                                    if ((1 & var30) == 0) {
                                        var32 = var22 >= 1 + var23;
                                        var31 = var23 + -1 >= var21;
                                        if (!var31 && var18 >= var19 - -1) {
                                            var33 = var13.aShortArray706[1 + var15];
                                            var34 = var16 + (var33 >> 8);
                                            var35 = var34 + (255 & var33);
                                            var31 = var34 < var23 && var23 < var35;
                                        }

                                        if (!var32 && -1 + var19 >= var17) {
                                            var33 = var13.aShortArray706[var15 + -1];
                                            var34 = var16 + (var33 >> 8);
                                            var35 = var34 - -(var33 & 0xFF);
                                            var32 = var23 > var34 && var35 > var23;
                                        }

                                    } else {
                                        var31 = var21 <= -1 + var23;
                                        var32 = var22 >= var23 + 1;
                                        if (!var31 && -1 + var19 >= var17) {
                                            var33 = var13.aShortArray706[-1 + var15];
                                            var34 = (var33 >> 8) + var16;
                                            var35 = var34 + (255 & var33);
                                            var31 = var23 > var34 && var35 > var23;
                                        }

                                        if (!var32 && var18 >= 1 + var19) {
                                            var33 = var13.aShortArray706[var15 + 1];
                                            var34 = (var33 >> 8) + var16;
                                            var35 = var34 - -(255 & var33);
                                            var32 = var23 > var34 && var23 < var35;
                                        }

                                    }
                                    if (var31 && var32) {
                                        var29 = Class134.anIntArrayArray1763[0];
                                    } else if (var31) {
                                        var29 = Class134.anIntArrayArray1763[1];
                                    } else if (var32) {
                                        var29 = Class134.anIntArrayArray1763[1];
                                    }

                                    if (null != var29) {
                                        var14.anInt651 += 3 * (var29.length >> 1) - 6;
                                        var14.anInt657 += var29.length >> 1;
                                    }
                                    continue;
                                }
                            }

                            if (var26) {
                                var29 = Class25.anIntArrayArray499[var7[var23][var19]];
                                var28 = Class134.anIntArrayArray1763[var7[var23][var19]];
                                var14.anInt651 += (-2 + (var28.length >> 1)) * 3;
                                var14.anInt651 += ((var29.length >> 1) - 2) * 3;
                                var14.anInt657 += var28.length >> 1;
                                var14.anInt657 += var29.length >> 1;
                            } else {
                                var28 = Class134.anIntArrayArray1763[0];
                                var14.anInt651 += (-2 + (var28.length >> 1)) * 3;
                                var14.anInt657 += var28.length >> 1;
                            }
                        }

                        ++var15;
                    }

                    var15 = 0;
                    var14.method1020();
                    if (-var13.anInt698 + (var13.anInt708 >> 7) < 0) {
                        var15 -= -var13.anInt698 + (var13.anInt708 >> 7);
                    }

                    for (var19 = var17; var19 <= var18; ++var19) {
                        var20 = var13.aShortArray706[var15];
                        var21 = (var20 >> 8) + var16;
                        var22 = -1 + (255 & var20) + var21;
                        if (var22 > 103) {
                            var22 = 103;
                        }

                        if (0 > var21) {
                            var21 = 0;
                        }

                        for (var23 = var21; var22 >= var23; ++var23) {
                            int var43 = 255 & var8[var23][var19];
                            var25 = 255 & var1[var23][var19];
                            byte var38 = var2[var23][var19];
                            boolean var39 = false;
                            FloorOverlayDefinition var46;
                            if (var25 == 0) {
                                if (0 == var43) {
                                    continue;
                                }

                                var46 = FloorOverlayDefinition.getFile(var43 - 1);
                                if (-1 == var46.anInt2103) {
                                    continue;
                                }

                                if (var7[var23][var19] != 0) {
                                    TextureOperation18.method284(var0, var9, var23, var6, var19, Class134.anIntArrayArray1763[var7[var23][var19]], var14, (byte) -88, var13, var11, var2[var23][var19]);
                                    continue;
                                }
                            } else if (var43 != 0) {
                                var46 = FloorOverlayDefinition.getFile(-1 + var43);
                                if (-1 == var46.anInt2103) {
                                    TextureOperation18.method284(var0, var9, var23, var6, var19, Class25.anIntArrayArray499[var7[var23][var19]], var14, (byte) 116, var13, var11, var2[var23][var19]);
                                    continue;
                                }

                                byte var48 = var7[var23][var19];
                                if (var48 != 0) {
                                    var39 = true;
                                }
                            }

                            Class25 var44 = Class75.method1336(var4, var23, var19);
                            if (null != var44) {
                                int var49 = (int) (var44.aLong498 >> 14) & 63;
                                if (9 == var49) {
                                    int[] var45 = null;
                                    int var47 = 3 & (int) (var44.aLong498 >> 20);
                                    int var36;
                                    boolean var51;
                                    short var50;
                                    if ((1 & var47) == 0) {
                                        var32 = var21 <= -1 + var23;
                                        var51 = var23 + 1 <= var22;
                                        if (!var32 && var18 >= var19 - -1) {
                                            var50 = var13.aShortArray706[1 + var15];
                                            var35 = (var50 >> 8) + var16;
                                            var36 = (var50 & 0xFF) + var35;
                                            var32 = var23 > var35 && var23 < var36;
                                        }

                                        if (!var51 && var19 - 1 >= var17) {
                                            var50 = var13.aShortArray706[-1 + var15];
                                            var35 = var16 + (var50 >> 8);
                                            var36 = (255 & var50) + var35;
                                            var51 = var35 < var23 && var23 < var36;
                                        }

                                        if (var32 && var51) {
                                            var45 = Class134.anIntArrayArray1763[0];
                                        } else if (var32) {
                                            var45 = Class134.anIntArrayArray1763[1];
                                            var38 = 1;
                                        } else if (var51) {
                                            var45 = Class134.anIntArrayArray1763[1];
                                            var38 = 3;
                                        }
                                    } else {
                                        var32 = var23 - 1 >= var21;
                                        var51 = var22 >= 1 + var23;
                                        if (!var32 && var17 <= var19 - 1) {
                                            var50 = var13.aShortArray706[var15 - 1];
                                            var35 = var16 + (var50 >> 8);
                                            var36 = (255 & var50) + var35;
                                            var32 = var23 > var35 && var36 > var23;
                                        }

                                        if (!var51 && var19 + 1 <= var18) {
                                            var50 = var13.aShortArray706[var15 - -1];
                                            var35 = var16 + (var50 >> 8);
                                            var36 = (255 & var50) + var35;
                                            var51 = var23 > var35 && var23 < var36;
                                        }

                                        if (var32 && var51) {
                                            var45 = Class134.anIntArrayArray1763[0];
                                        } else if (var32) {
                                            var38 = 0;
                                            var45 = Class134.anIntArrayArray1763[1];
                                        } else if (var51) {
                                            var45 = Class134.anIntArrayArray1763[1];
                                            var38 = 2;
                                        }
                                    }

                                    if (null != var45) {
                                        TextureOperation18.method284(var0, var9, var23, var6, var19, var45, var14, (byte) 98, var13, var11, var38);
                                    }
                                    continue;
                                }
                            }

                            if (var39) {
                                TextureOperation18.method284(var0, var9, var23, var6, var19, Class25.anIntArrayArray499[var7[var23][var19]], var14, (byte) 96, var13, var11, var2[var23][var19]);
                                TextureOperation18.method284(var0, var9, var23, var6, var19, Class134.anIntArrayArray1763[var7[var23][var19]], var14, (byte) -117, var13, var11, var2[var23][var19]);
                            } else {
                                TextureOperation18.method284(var0, var9, var23, var6, var19, Class134.anIntArrayArray1763[0], var14, (byte) 61, var13, var11, var38);
                            }
                        }

                        ++var15;
                    }

                    if (var14.anInt653 > 0 && var14.anInt655 > 0) {
                        var14.method1019();
                        var13.aClass37_712 = var14;
                    }
                }
            }

        } catch (RuntimeException var37) {
            throw ClientErrorException.clientError(var37, "s.C(" + (var0 != null ? "{...}" : "null") + ',' + (var1 != null ? "{...}" : "null") + ',' + (var2 != null ? "{...}" : "null") + ',' + (var3 != null ? "{...}" : "null") + ',' + var4 + ',' + var5 + ',' + (var6 != null ? "{...}" : "null") + ',' + (var7 != null ? "{...}" : "null") + ',' + (var8 != null ? "{...}" : "null") + ',' + (var9 != null ? "{...}" : "null") + ',' + -8771 + ',' + (var11 != null ? "{...}" : "null") + ')');
        }
    }

    abstract int method1767(int var1, int var2, int var3);

    abstract void method1770();

}
