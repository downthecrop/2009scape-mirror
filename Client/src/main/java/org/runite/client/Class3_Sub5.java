package org.runite.client;

import org.rs09.SystemLogger;
import org.rs09.client.Linkable;

final class Class3_Sub5 extends Linkable {

    int anInt2266;

    int anInt2268;
    int anInt2270;
    int anInt2271;
    int anInt2272;
    int anInt2273;
    int anInt2277;
    int anInt2278;
    int anInt2279;
    int anInt2282;
    int anInt2283;
    int anInt2284;

    static void method111(byte var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        try {
            if (var0 < -47) {
                for (int var9 = 0; var9 < Class119.aClass131_1624.anInt1720; ++var9) {
                    if (Class119.aClass131_1624.method1787(var9)) {
                        int var10 = -TextureOperation37.anInt3256 + Class119.aClass131_1624.xArray1727[var9];
                        int var11 = Unsorted.anInt65 - (Class119.aClass131_1624.yArray1718[var9] - -1 - Class108.anInt1460);
                        int var12 = var1 + (-var1 + var4) * (var10 - var3) / (-var3 + var7);
                        int var14 = Class119.aClass131_1624.method1791(var9, 8);
                        int var13 = (var8 + -var2) * (var11 - var6) / (var5 + -var6) + var2;
                        int var15 = 16777215;
                        Class33 var16 = null;
                        if (var14 == 0) {
                            if ((double) Class44.aFloat727 == 3.0D) {
                                var16 = Class164_Sub2.aClass33_3019;
                            }

                            if (4.0D == (double) Class44.aFloat727) {
                                var16 = Unsorted.aClass33_1238;
                            }

                            if ((double) Class44.aFloat727 == 6.0D) {
                                var16 = Class99.aClass33_1399;
                            }

                            if ((double) Class44.aFloat727 >= 8.0D) {
                                var16 = Class75_Sub2.aClass33_2637;
                            }
                        }

                        if (var14 == 1) {
                            if (3.0D == (double) Class44.aFloat727) {
                                var16 = Class99.aClass33_1399;
                            }

                            if ((double) Class44.aFloat727 == 4.0D) {
                                var16 = Class75_Sub2.aClass33_2637;
                            }

                            if ((double) Class44.aFloat727 == 6.0D) {
                                var16 = Class119.aClass33_1626;
                            }

                            if (8.0D <= (double) Class44.aFloat727) {
                                var16 = Class75_Sub2.aClass33_2648;
                            }
                        }

                        if (var14 == 2) {
                            if ((double) Class44.aFloat727 == 3.0D) {
                                var16 = Class119.aClass33_1626;
                            }

                            var15 = 16755200;
                            if ((double) Class44.aFloat727 == 4.0D) {
                                var16 = Class75_Sub2.aClass33_2648;
                            }

                            if (6.0D == (double) Class44.aFloat727) {
                                var16 = Class161.aClass33_2034;
                            }

                            if ((double) Class44.aFloat727 >= 8.0D) {
                                var16 = Class91.aClass33_1305;
                            }
                        }

                        if (Class119.aClass131_1624.anIntArray1725[var9] != -1) {
                            var15 = Class119.aClass131_1624.anIntArray1725[var9];
                        }

                        if (null != var16) {
                            int var17 = FontType.smallFont.method691(Class119.aClass131_1624.aStringArray1721[var9], null, Class158_Sub1.aStringArray2977);
                            var13 -= var16.method998() * (var17 + -1) / 2;
                            var13 += var16.method1006() / 2;

                            for (int var18 = 0; var18 < var17; ++var18) {
                                RSString var19 = Class158_Sub1.aStringArray2977[var18];
                                if (-1 + var17 > var18) {
                                    var19.method1553(-4 + var19.length());
                                }

                                var16.method1003(var19, var12, var13, var15);
                                var13 += var16.method998();
                            }
                        }
                    }
                }

            }
        } catch (RuntimeException var20) {
            throw ClientErrorException.clientError(var20, "cn.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ')');
        }
    }

    static void method112(byte var0) {
        try {
            if (null == Class158.aByteArrayArrayArray2008) {
                Class158.aByteArrayArrayArray2008 = new byte[4][104][104];
            }

            for (int var2 = 0; var2 < 4; ++var2) {
                for (int var3 = 0; var3 < 104; ++var3) {
                    for (int var4 = 0; 104 > var4; ++var4) {
                        Class158.aByteArrayArrayArray2008[var2][var3][var4] = var0;
                    }
                }
            }

        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "cn.A(" + var0 + ',' + (byte) 55 + ')');
        }
    }

    static void method114(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
        try {

            if (var8 == var9 && var3 == var7 && var5 == var4 && var1 == var6) {
                GameObject.method1869((byte) 84, var2, var6, var7, var5, var9);
            } else {
                int var11 = var7;
                int var10 = var9;
                int var12 = var9 * 3;
                int var13 = 3 * var7;
                int var14 = var8 * 3;
                int var15 = var3 * 3;
                int var16 = var4 * 3;
                int var17 = var1 * 3;
                int var18 = var5 + -var16 + var14 + -var9;
                int var19 = -var7 + var6 - (var17 + -var15);
                int var20 = var12 + -var14 + -var14 + var16;
                int var21 = var13 + -var15 + var17 + -var15;
                int var22 = -var12 + var14;
                int var23 = var15 + -var13;

                for (int var24 = 128; var24 <= 4096; var24 += 128) {
                    int var25 = var24 * var24 >> 12;
                    int var26 = var24 * var25 >> 12;
                    int var28 = var19 * var26;
                    int var29 = var25 * var20;
                    int var27 = var18 * var26;
                    int var30 = var25 * var21;
                    int var31 = var22 * var24;
                    int var32 = var23 * var24;
                    int var33 = (var31 + (var27 - -var29) >> 12) + var9;
                    int var34 = var7 + (var32 + var28 + var30 >> 12);
                    GameObject.method1869((byte) -119, var2, var34, var11, var33, var10);
                    var10 = var33;
                    var11 = var34;
                }
            }

        } catch (RuntimeException var35) {
            throw ClientErrorException.clientError(var35, "cn.O(" + 3 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ')');
        }
    }

    static int method115(boolean var0) {
        try {
            long var2 = TimeUtils.time();

            for (Class3_Sub7 var4 = !var0 ? (Class3_Sub7) AtmosphereParser.aHashTable_3679.next() : (Class3_Sub7) AtmosphereParser.aHashTable_3679.first(); var4 != null; var4 = (Class3_Sub7) AtmosphereParser.aHashTable_3679.next()) {
                if ((4611686018427387903L & var4.aLong2295) < var2) {
                    if ((4611686018427387904L & var4.aLong2295) != 0) {
                        int var5 = (int) var4.linkableKey;
                        ItemDefinition.ram[var5] = Class57.varpArray[var5];
                        SystemLogger.logVerbose("Reading value " + Class57.varpArray[var5] + " from varp " + var5);
                        var4.unlink();
                        return var5;
                    }

                    var4.unlink();
                }
            }

            return -1;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "cn.F(" + var0 + ',' + -1 + ')');
        }
    }

    static void method116(boolean var0) {
        try {
            int var2 = Class159.localPlayerCount;
            if (Class65.anInt987 == Class102.player.xAxis >> 7 && Class45.anInt733 == Class102.player.yAxis >> 7) {
                Class65.anInt987 = 0;
            }

            if (var0) {
                var2 = 1;
            }

            int var3;
            Player var4;
            int var7;
            int var8;
            int var9;
            int var10;
            int var11;
            for (var3 = 0; var3 < var2; ++var3) {
                if (var0) {
                    var4 = Class102.player;
                } else {
                    var4 = Unsorted.players[Class56.localPlayerIndexes[var3]];
                }

                if (null != var4 && var4.hasDefinitions()) {
                    int var5 = var4.getSize();
                    int var6;
                    if (var5 == 1) {
                        if ((127 & var4.xAxis) == 64 && 64 == (127 & var4.yAxis)) {
                            var6 = var4.xAxis >> 7;
                            var7 = var4.yAxis >> 7;
                            if (var6 >= 0 && var6 < 104 && var7 >= 0 && var7 < 104) {
                                ++Class163_Sub1_Sub1.anIntArrayArray4010[var6][var7];
                            }
                        }
                    } else if (((1 & var5) != 0 || (var4.xAxis & 127) == 0 && (127 & var4.yAxis) == 0) && ((1 & var5) != 1 || (var4.xAxis & 127) == 64 && (127 & var4.yAxis) == 64)) {
                        var6 = var4.xAxis + -(var5 * 64) >> 7;
                        var7 = var4.yAxis + -(var5 * 64) >> 7;
                        var8 = var4.getSize() + var6;
                        if (104 < var8) {
                            var8 = 104;
                        }

                        if (var6 < 0) {
                            var6 = 0;
                        }

                        var9 = var7 + var4.getSize();
                        if (var7 < 0) {
                            var7 = 0;
                        }

                        if (var9 > 104) {
                            var9 = 104;
                        }

                        for (var10 = var6; var8 > var10; ++var10) {
                            for (var11 = var7; var11 < var9; ++var11) {
                                ++Class163_Sub1_Sub1.anIntArrayArray4010[var10][var11];
                            }
                        }
                    }
                }
            }

            label226:
            for (var3 = 0; var3 < var2; ++var3) {
                long var16;
                if (var0) {
                    var4 = Class102.player;
                    var16 = 8791798054912L;
                } else {
                    var4 = Unsorted.players[Class56.localPlayerIndexes[var3]];
                    var16 = (long) Class56.localPlayerIndexes[var3] << 32;
                }

                if (var4 != null && var4.hasDefinitions()) {
                    var4.aBoolean3968 = (RSInterface.aBoolean236 && Class159.localPlayerCount > 200 || 50 < Class159.localPlayerCount) && !var0 && var4.anInt2764 == var4.getRenderAnimationType().stand_animation;

                    var7 = var4.getSize();
                    if (var7 == 1) {
                        if (64 == (127 & var4.xAxis) && (127 & var4.yAxis) == 64) {
                            var8 = var4.xAxis >> 7;
                            var9 = var4.yAxis >> 7;
                            if (var8 < 0 || var8 >= 104 || var9 < 0 || 104 <= var9) {
                                continue;
                            }

                            if (Class163_Sub1_Sub1.anIntArrayArray4010[var8][var9] > 1) {
                                --Class163_Sub1_Sub1.anIntArrayArray4010[var8][var9];
                                continue;
                            }
                        }
                    } else if ((1 & var7) == 0 && (127 & var4.xAxis) == 0 && (var4.yAxis & 127) == 0 || 1 == (1 & var7) && (127 & var4.xAxis) == 64 && (var4.yAxis & 127) == 0) {
                        var8 = var4.xAxis + -(64 * var7) >> 7;
                        var10 = var7 + var8;
                        var9 = -(var7 * 64) + var4.yAxis >> 7;
                        if (var10 > 104) {
                            var10 = 104;
                        }

                        if (0 > var8) {
                            var8 = 0;
                        }

                        var11 = var7 + var9;
                        if (var9 < 0) {
                            var9 = 0;
                        }

                        boolean var12 = true;
                        if (var11 > 104) {
                            var11 = 104;
                        }

                        int var13;
                        int var14;
                        for (var13 = var8; var10 > var13; ++var13) {
                            for (var14 = var9; var14 < var11; ++var14) {
                                if (Class163_Sub1_Sub1.anIntArrayArray4010[var13][var14] <= 1) {
                                    var12 = false;
                                    break;
                                }
                            }
                        }

                        if (var12) {
                            var13 = var8;

                            while (true) {
                                if (var10 <= var13) {
                                    continue label226;
                                }

                                for (var14 = var9; var14 < var11; ++var14) {
                                    --Class163_Sub1_Sub1.anIntArrayArray4010[var13][var14];
                                }

                                ++var13;
                            }
                        }
                    }

                    if (null != var4.anObject2796 && var4.anInt2797 <= Class44.anInt719 && Class44.anInt719 < var4.anInt2778) {
                        var4.aBoolean3968 = false;
                        var4.anInt2831 = Scenery.sceneryPositionHash(WorldListCountry.localPlane, 1, var4.xAxis, var4.yAxis);
                        TextureOperation28.method292(WorldListCountry.localPlane, var4.xAxis, var4.yAxis, var4.anInt2831, var4, var4.anInt2785, var16, var4.anInt2788, var4.anInt2777, var4.anInt2818, var4.anInt2817);
                    } else {
                        var4.anInt2831 = Scenery.sceneryPositionHash(WorldListCountry.localPlane, 1, var4.xAxis, var4.yAxis);
                        Class20.method907(WorldListCountry.localPlane, var4.xAxis, var4.yAxis, var4.anInt2831, 64 * (var7 - 1) + 60, var4, var4.anInt2785, var16, var4.aBoolean2810);
                    }
                }
            }

        } catch (RuntimeException var15) {
            throw ClientErrorException.clientError(var15, "cn.C(" + var0 + ',' + 670232012 + ')');
        }
    }

    static void method117() {
        try {
            if (0 > Class3_Sub28_Sub1.anInt3536) {
                TextureOperation13.anInt3362 = -1;
                Class3_Sub28_Sub1.anInt3536 = 0;
                Texture.anInt1150 = -1;
            }

            if (Class23.anInt455 < Class3_Sub28_Sub1.anInt3536) {
                TextureOperation13.anInt3362 = -1;
                Class3_Sub28_Sub1.anInt3536 = Class23.anInt455;
                Texture.anInt1150 = -1;
            }

            if (Scenery.anInt2251 < 0) {
                Texture.anInt1150 = -1;
                TextureOperation13.anInt3362 = -1;
                Scenery.anInt2251 = 0;
            }

            if (Class108.anInt1460 < Scenery.anInt2251) {
                Scenery.anInt2251 = Class108.anInt1460;
                TextureOperation13.anInt3362 = -1;
                Texture.anInt1150 = -1;
            }

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "cn.E(" + (byte) 87 + ')');
        }
    }

}
