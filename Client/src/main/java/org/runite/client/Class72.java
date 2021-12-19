package org.runite.client;

final class Class72 {

    static int[] anIntArray3045;
    static int anInt1672 = 0;
    static int anInt1071 = 0;
    static boolean aBoolean1074 = false;
    GameObject aClass140_1067;
    int anInt1068;
    GameObject aClass140_1069;
    GameObject aClass140_1073;
    int anInt1075;
    int anInt1077;
    int anInt1078;
    long aLong1079;


    static void method1293() {
        try {
            if (!Client.paramAdvertisementSuppressed && Class44.paramModeWhere != 2) {
                try {
                    TextCore.aString_38.method1577(Client.clientInstance);
                } catch (Throwable var2) {
                }

            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "jj.G(" + true + ')');
        }
    }

    static void method1294() {
        anInt1672 = 0;

        label188:
        for (int var0 = 0; var0 < Scenery.anInt2249; ++var0) {
            Class113 var1 = Class3_Sub28_Sub8.aClass113Array3610[var0];
            int var2;
            if (anIntArray3045 != null) {
                for (var2 = 0; var2 < anIntArray3045.length; ++var2) {
                    if (anIntArray3045[var2] != -1000000 && (var1.anInt1544 <= anIntArray3045[var2] || var1.anInt1548 <= anIntArray3045[var2]) && (var1.anInt1562 <= Class52.anIntArray859[var2] || var1.anInt1545 <= Class52.anIntArray859[var2]) && (var1.anInt1562 >= Unsorted.anIntArray1083[var2] || var1.anInt1545 >= Unsorted.anIntArray1083[var2]) && (var1.anInt1560 <= Class75_Sub4.anIntArray2663[var2] || var1.anInt1550 <= Class75_Sub4.anIntArray2663[var2]) && (var1.anInt1560 >= Unsorted.anIntArray39[var2] || var1.anInt1550 >= Unsorted.anIntArray39[var2])) {
                        continue label188;
                    }
                }
            }

            int var3;
            int var4;
            boolean var5;
            int var6;
            if (var1.anInt1554 == 1) {
                var2 = var1.anInt1553 - Class97.anInt1375 + TextureOperation8.renderDistanceTiles;
                if (var2 >= 0 && var2 <= TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles) {
                    var3 = var1.anInt1563 - Class145.anInt3340 + TextureOperation8.renderDistanceTiles;
                    if (var3 < 0) {
                        var3 = 0;
                    }

                    var4 = var1.anInt1566 - Class145.anInt3340 + TextureOperation8.renderDistanceTiles;
                    if (var4 > TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles) {
                        var4 = TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles;
                    }

                    var5 = false;

                    while (var3 <= var4) {
                        if (Class23.aBooleanArrayArray457[var2][var3++]) {
                            var5 = true;
                            break;
                        }
                    }

                    if (var5) {
                        var6 = Class145.anInt2697 - var1.anInt1562;
                        if (var6 > 32) {
                            var1.anInt1564 = 1;
                        } else {
                            if (var6 >= -32) {
                                continue;
                            }

                            var1.anInt1564 = 2;
                            var6 = -var6;
                        }

                        var1.anInt1555 = (var1.anInt1560 - TextureOperation13.anInt3363 << 8) / var6;
                        var1.anInt1551 = (var1.anInt1550 - TextureOperation13.anInt3363 << 8) / var6;
                        var1.anInt1561 = (var1.anInt1544 - Unsorted.anInt3657 << 8) / var6;
                        var1.anInt1565 = (var1.anInt1548 - Unsorted.anInt3657 << 8) / var6;
                        Class145.aClass113Array1895[anInt1672++] = var1;
                    }
                }
            } else if (var1.anInt1554 == 2) {
                var2 = var1.anInt1563 - Class145.anInt3340 + TextureOperation8.renderDistanceTiles;
                if (var2 >= 0 && var2 <= TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles) {
                    var3 = var1.anInt1553 - Class97.anInt1375 + TextureOperation8.renderDistanceTiles;
                    if (var3 < 0) {
                        var3 = 0;
                    }

                    var4 = var1.anInt1547 - Class97.anInt1375 + TextureOperation8.renderDistanceTiles;
                    if (var4 > TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles) {
                        var4 = TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles;
                    }

                    var5 = false;

                    while (var3 <= var4) {
                        if (Class23.aBooleanArrayArray457[var3++][var2]) {
                            var5 = true;
                            break;
                        }
                    }

                    if (var5) {
                        var6 = TextureOperation13.anInt3363 - var1.anInt1560;
                        if (var6 > 32) {
                            var1.anInt1564 = 3;
                        } else {
                            if (var6 >= -32) {
                                continue;
                            }

                            var1.anInt1564 = 4;
                            var6 = -var6;
                        }

                        var1.anInt1549 = (var1.anInt1562 - Class145.anInt2697 << 8) / var6;
                        var1.anInt1557 = (var1.anInt1545 - Class145.anInt2697 << 8) / var6;
                        var1.anInt1561 = (var1.anInt1544 - Unsorted.anInt3657 << 8) / var6;
                        var1.anInt1565 = (var1.anInt1548 - Unsorted.anInt3657 << 8) / var6;
                        Class145.aClass113Array1895[anInt1672++] = var1;
                    }
                }
            } else if (var1.anInt1554 == 4) {
                var2 = var1.anInt1544 - Unsorted.anInt3657;
                if (var2 > 128) {
                    var3 = var1.anInt1563 - Class145.anInt3340 + TextureOperation8.renderDistanceTiles;
                    if (var3 < 0) {
                        var3 = 0;
                    }

                    var4 = var1.anInt1566 - Class145.anInt3340 + TextureOperation8.renderDistanceTiles;
                    if (var4 > TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles) {
                        var4 = TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles;
                    }

                    if (var3 <= var4) {
                        int var10 = var1.anInt1553 - Class97.anInt1375 + TextureOperation8.renderDistanceTiles;
                        if (var10 < 0) {
                            var10 = 0;
                        }

                        var6 = var1.anInt1547 - Class97.anInt1375 + TextureOperation8.renderDistanceTiles;
                        if (var6 > TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles) {
                            var6 = TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles;
                        }

                        boolean var7 = false;

                        label114:
                        for (int var8 = var10; var8 <= var6; ++var8) {
                            for (int var9 = var3; var9 <= var4; ++var9) {
                                if (Class23.aBooleanArrayArray457[var8][var9]) {
                                    var7 = true;
                                    break label114;
                                }
                            }
                        }

                        if (var7) {
                            var1.anInt1564 = 5;
                            var1.anInt1549 = (var1.anInt1562 - Class145.anInt2697 << 8) / var2;
                            var1.anInt1557 = (var1.anInt1545 - Class145.anInt2697 << 8) / var2;
                            var1.anInt1555 = (var1.anInt1560 - TextureOperation13.anInt3363 << 8) / var2;
                            var1.anInt1551 = (var1.anInt1550 - TextureOperation13.anInt3363 << 8) / var2;
                            Class145.aClass113Array1895[anInt1672++] = var1;
                        }
                    }
                }
            }
        }

    }

    static float[] method1297() {
        try {
            float var1 = Class92.method1514() + Class92.getLightingModelAmbient();
            int var2 = Class92.screenColorRgb();
            float var3 = (float) (255 & var2 >> 16) / 255.0F;
            MouseListeningClass.aFloatArray1919[3] = 1.0F;
            float var4 = (float) (('\uff59' & var2) >> 8) / 255.0F;
            float var6 = 0.58823526F;
            float var5 = (float) (255 & var2) / 255.0F;
            MouseListeningClass.aFloatArray1919[2] = Unsorted.aFloatArray1934[2] * var5 * var6 * var1;
            MouseListeningClass.aFloatArray1919[0] = Unsorted.aFloatArray1934[0] * var3 * var6 * var1;
            MouseListeningClass.aFloatArray1919[1] = var1 * var6 * var4 * Unsorted.aFloatArray1934[1];
            return MouseListeningClass.aFloatArray1919;
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "jj.A(" + (byte) -50 + ')');
        }
    }

}
