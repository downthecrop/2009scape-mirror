package org.runite.client;

public final class WorldMapUnderlayDecoder {

    static void decode(DataBuffer var1) {
        try {
            int var3 = Class158.anInt2010 >> 1;
            int var2 = AudioThread.anInt4034 >> 2 << 10;
            byte[][] var4 = new byte[Class23.anInt455][Class108.anInt1460];

            int var6;
            int var12;
            int var14;
            while (var1.index < var1.buffer.length) {
                int var7 = 0;
                var6 = 0;
                boolean var5 = false;
                if (var1.readUnsignedByte() == 1) {
                    var6 = var1.readUnsignedByte();
                    var7 = var1.readUnsignedByte();
                    var5 = true;
                }

                int var8 = var1.readUnsignedByte();
                int var9 = var1.readUnsignedByte();
                int var10 = -TextureOperation37.anInt3256 + var8 * 64;
                int var11 = -1 + Class108.anInt1460 - var9 * 64 + Unsorted.anInt65;
                if (var10 >= 0 && 0 <= -63 + var11 && Class23.anInt455 > var10 - -63 && var11 < Class108.anInt1460) {
                    for (var12 = 0; var12 < 64; ++var12) {
                        byte[] var13 = var4[var10 - -var12];

                        for (var14 = 0; 64 > var14; ++var14) {
                            if (!var5 || var12 >= 8 * var6 && 8 + 8 * var6 > var12 && var14 >= var7 * 8 && var14 < 8 + 8 * var7) {
                                var13[var11 - var14] = var1.readSignedByte();
                            }
                        }
                    }
                } else if (var5) {
                    var1.index += 64;
                } else {
                    var1.index += 4096;
                }
            }

            int var27 = Class23.anInt455;
            var6 = Class108.anInt1460;
            int[] var29 = new int[var6];
            int[] var28 = new int[var6];
            int[] var30 = new int[var6];
            int[] var32 = new int[var6];
            int[] var31 = new int[var6];

            for (var12 = -5; var27 > var12; ++var12) {
                int var15;
                int var35;
                for (int var34 = 0; var6 > var34; ++var34) {
                    var14 = var12 + 5;
                    if (var27 > var14) {
                        var15 = 255 & var4[var14][var34];
                        if (var15 > 0) {
                            FloorUnderlayDefinition var16 = FloorUnderlayDefinition.method629(var15 - 1);
                            var28[var34] += var16.anInt1408;
                            var29[var34] += var16.anInt1406;
                            var30[var34] += var16.anInt1417;
                            var32[var34] += var16.anInt1418;
                            ++var31[var34];
                        }
                    }

                    var15 = var12 + -5;
                    if (var15 >= 0) {
                        var35 = var4[var15][var34] & 0xFF;
                        if (0 < var35) {
                            FloorUnderlayDefinition var17 = FloorUnderlayDefinition.method629(-1 + var35);
                            var28[var34] -= var17.anInt1408;
                            var29[var34] -= var17.anInt1406;
                            var30[var34] -= var17.anInt1417;
                            var32[var34] -= var17.anInt1418;
                            --var31[var34];
                        }
                    }
                }

                if (var12 >= 0) {
                    int[][] var33 = Class146.anIntArrayArrayArray1903[var12 >> 6];
                    var14 = 0;
                    var15 = 0;
                    int var36 = 0;
                    int var18 = 0;
                    var35 = 0;

                    for (int var19 = -5; var6 > var19; ++var19) {
                        int var20 = var19 - -5;
                        if (var6 > var20) {
                            var18 += var31[var20];
                            var15 += var29[var20];
                            var35 += var30[var20];
                            var14 += var28[var20];
                            var36 += var32[var20];
                        }

                        int var21 = -5 + var19;
                        if (var21 >= 0) {
                            var35 -= var30[var21];
                            var36 -= var32[var21];
                            var14 -= var28[var21];
                            var18 -= var31[var21];
                            var15 -= var29[var21];
                        }

                        if (var19 >= 0 && 0 < var18) {
                            int[] var22 = var33[var19 >> 6];
                            int var23 = var36 != 0 ? Class3_Sub8.method129(var35 / var18, var15 / var18, var14 * 256 / var36) : 0;
                            if (var4[var12][var19] == 0) {
                                if (var22 != null) {
                                    var22[Unsorted.bitwiseAnd(4032, var19 << 6) - -Unsorted.bitwiseAnd(var12, 63)] = 0;
                                }
                            } else {
                                if (var22 == null) {
                                    var22 = var33[var19 >> 6] = new int[4096];
                                }

                                int var24 = var3 + (var23 & 127);
                                if (var24 < 0) {
                                    var24 = 0;
                                } else if (var24 > 127) {
                                    var24 = 127;
                                }

                                int var25 = var24 + (896 & var23) + (var23 + var2 & '\ufc00');
                                var22[Unsorted.bitwiseAnd(4032, var19 << 6) + Unsorted.bitwiseAnd(63, var12)] = Class51.anIntArray834[Unsorted.method1100(96, var25)];
                            }
                        }
                    }
                }
            }

        } catch (RuntimeException var26) {
            throw ClientErrorException.clientError(var26, "cj.H(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }
}
