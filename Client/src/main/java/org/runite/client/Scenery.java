package org.runite.client;

import org.rs09.client.Linkable;
import java.util.Objects;

final class Scenery extends Linkable {
    static LinkedList sceneryList = new LinkedList();
    static int[] anIntArray2386 = new int[]{1, -1, -1, 1};

    static int anInt2249;
    static int anInt2251;
    int y;
    int plane;
    int original_type;
    int original_id;
    int rotation;
    int original_rotation;
    int anInt2259 = -1;
    int anInt2261 = 0;
    int type;
    int typemask;
    int x;
    int object_id;


    static void initializeScene(int renderDistance, boolean var4) {
        Unsorted.width1234 = 104;
        TextureOperation17.height3179 = 104;
        TextureOperation8.renderDistanceTiles = renderDistance;
        Unsorted.aTileDataArrayArrayArray4070 = new TileData[4][Unsorted.width1234][TextureOperation17.height3179];
        Class58.anIntArrayArrayArray914 = new int[4][Unsorted.width1234 + 1][TextureOperation17.height3179 + 1];
        if (HDToolKit.highDetail) {
            Client.aClass3_Sub11ArrayArray2199 = new Class3_Sub11[4][];
        }

        if (var4) {
            Class166.aTileDataArrayArrayArray2065 = new TileData[1][Unsorted.width1234][TextureOperation17.height3179];
            TextureOperation16.anIntArrayArray3115 = new int[Unsorted.width1234][TextureOperation17.height3179];
            Unsorted.anIntArrayArrayArray3605 = new int[1][Unsorted.width1234 + 1][TextureOperation17.height3179 + 1];
            if (HDToolKit.highDetail) {
                TextureOperation32.aClass3_Sub11ArrayArray3346 = new Class3_Sub11[1][];
            }
        } else {
            Class166.aTileDataArrayArrayArray2065 = null;
            TextureOperation16.anIntArrayArray3115 = null;
            Unsorted.anIntArrayArrayArray3605 = null;
            TextureOperation32.aClass3_Sub11ArrayArray3346 = null;
        }

        Class167.method2264(false);
        Class3_Sub28_Sub8.aClass113Array3610 = new Class113[500];
        anInt2249 = 0;
        Class145.aClass113Array1895 = new Class113[500];
        Class72.anInt1672 = 0;
        Class81.anIntArrayArrayArray1142 = new int[4][Unsorted.width1234 + 1][TextureOperation17.height3179 + 1];
        SequenceDefinition.aClass25Array1868 = new Class25[5000];
        Unsorted.anInt3070 = 0;
        Unsorted.aClass25Array4060 = new Class25[100];
        Class23.aBooleanArrayArray457 = new boolean[TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles + 1][TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles + 1];
        Class49.aBooleanArrayArray814 = new boolean[TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles + 2][TextureOperation8.renderDistanceTiles + TextureOperation8.renderDistanceTiles + 2];
        Unsorted.possibleHeightmap1774 = new byte[4][Unsorted.width1234][TextureOperation17.height3179];
    }

    static void method1798(int var0, Scenery var1) {
        try {
            long var2 = 0L;
            int var4 = -1;
            if (var0 <= 17) {
                Class159.anInt1740 = -43;
            }

            int var5 = 0;
            if (var1.typemask == 0) {
                var2 = Scenery.lookupTypemask0(var1.plane, var1.x, var1.y);
            }

            int var6 = 0;
            if (var1.typemask == 1) {
                var2 = Scenery.lookupTypemask1(var1.plane, var1.x, var1.y);
            }

            if (var1.typemask == 2) {
                var2 = Scenery.lookupTypemask2(var1.plane, var1.x, var1.y);
            }

            if (var1.typemask == 3) {
                var2 = Scenery.lookupTypeMask3(var1.plane, var1.x, var1.y);
            }

            if (var2 != 0L) {
                var4 = Integer.MAX_VALUE & (int) (var2 >>> 32);
                var6 = (int) var2 >> 20 & 3;
                var5 = ((int) var2 & 516214) >> 14;
            }

            var1.original_id = var4;
            var1.original_type = var5;
            var1.original_rotation = var6;
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "sf.B(" + var0 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static long lookupTypemask0(int var0, int var1, int var2) {
        TileData var3 = TileData.aTileDataArrayArrayArray2638[var0][var1][var2];
        return var3 != null && var3.aClass70_2234 != null ? var3.aClass70_2234.aLong1048 : 0L;
    }

    public static long lookupTypemask1(int z, int x, int y) {
        TileData var3 = TileData.aTileDataArrayArrayArray2638[z][x][y];
        return var3 != null && var3.aClass19_2233 != null ? var3.aClass19_2233.aLong428 : 0L;
    }

    static long lookupTypemask2(int var0, int var1, int var2) {
        TileData var3 = TileData.aTileDataArrayArrayArray2638[var0][var1][var2];
        if (var3 != null) {
            for (int var4 = 0; var4 < var3.anInt2223; ++var4) {
                Class25 var5 = var3.aClass25Array2221[var4];
                if ((var5.aLong498 >> 29 & 3L) == 2L && var5.anInt483 == var1 && var5.anInt478 == var2) {
                return var5.aLong498;
                }
            }

        }
        return 0L;
    }

    static long lookupTypeMask3(int var0, int var1, int var2) {
        TileData var3 = TileData.aTileDataArrayArrayArray2638[var0][var1][var2];
        return var3 != null && var3.aClass12_2230 != null ? var3.aClass12_2230.aLong328 : 0L;
    }

    static void cleanupOldScenery() {
        try {
            Scenery var1 = (Scenery) Scenery.sceneryList.startIteration();
            for (; var1 != null; var1 = (Scenery) Scenery.sceneryList.nextIteration()) {
                if (var1.anInt2259 > 0) {
                    var1.anInt2259 -= 1;
                }
                if (var1.anInt2259 != 0) {
                    if (var1.anInt2261 > 0) {
                        var1.anInt2261 -= 1;
                    }
                    if ((var1.anInt2261 == 0) && (1 <= var1.x) && (1 <= var1.y) && (102 >= var1.x) && (var1.y <= 102) && ((var1.object_id < 0) || (Unsorted.method590((byte) -34, var1.object_id, var1.type)))) {
                        Scenery.method1048(var1.object_id, var1.x, var1.plane, var1.rotation, var1.y, -65, var1.type, var1.typemask);
                        var1.anInt2261 = -1;
                        if ((var1.object_id == var1.original_id) && (var1.original_id == -1)) {
                            var1.unlink();
                        } else if ((var1.original_id == var1.object_id) && (var1.rotation == var1.original_rotation) && (var1.type == var1.original_type)) {
                            var1.unlink();
                        }
                    }
                } else if ((var1.original_id < 0) || (Unsorted.method590((byte) -66, var1.original_id, var1.original_type))) {
                    Scenery.method1048(var1.original_id, var1.x, var1.plane, var1.original_rotation, var1.y, -71, var1.original_type, var1.typemask);
                    var1.unlink();
                }
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ug.A(" + (byte) -82 + ')');
        }
    }

    static void method1048(int object_id, int x, int plane, int rotation, int y, int var5, int type, int typemask) {
        try {
            if (var5 > -15) {
                Unsorted.anInt688 = -64;
            }

            if (x >= 1 && y >= 1 && 102 >= x && y <= 102) {
                int maxPlane;
                if (!NPC.isHighDetail(41) && 0 == (2 & Unsorted.sceneryTypeMaskGrid[0][x][y])) {
                    maxPlane = plane;
                    if ((8 & Unsorted.sceneryTypeMaskGrid[plane][x][y]) != 0) {
                        maxPlane = 0;
                    }

                    if (maxPlane != Class140_Sub3.viewportZ) {
                        return;
                    }
                }

                maxPlane = plane;
                if (plane < 3 && (2 & Unsorted.sceneryTypeMaskGrid[1][x][y]) == 2) {
                    maxPlane = plane + 1;
                }

                Scenery.method910(y, x, plane, typemask, maxPlane, AtmosphereParser.aClass91Array1182[plane]);
                if (0 <= object_id) {
                    boolean var9 = KeyboardListener.aBoolean1905;
                    KeyboardListener.aBoolean1905 = true;
                    Scenery.method1683(maxPlane, false, plane, false, AtmosphereParser.aClass91Array1182[plane], object_id, type, x, y, rotation);
                    KeyboardListener.aBoolean1905 = var9;
                }
            }

        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "ge.H(" + object_id + ',' + x + ',' + plane + ',' + rotation + ',' + y + ',' + var5 + ',' + type + ',' + typemask + ')');
        }
    }

    static void method910(int y, int x, int plane, int typemask, int var5, Class91 var6) {
        try {
            long var7 = 0L;
            if (typemask == 0) {
                var7 = Scenery.lookupTypemask0(plane, x, y);
            } else if (typemask == 1) {
                var7 = Scenery.lookupTypemask1(plane, x, y);
            } else if (typemask == 2) {
                var7 = Scenery.lookupTypemask2(plane, x, y);
            } else if (3 == typemask) {
                var7 = Scenery.lookupTypeMask3(plane, x, y);
            }

            int var19 = (519128 & (int) var7) >> 14;
            int var17 = (int) (var7 >>> 32) & Integer.MAX_VALUE;
            ObjectDefinition var12 = ObjectDefinition.getObjectDefinition(var17);
            if (var12.method1690()) {
                Class140_Sub6.method2020(x, var12, y, plane);
            }

            int var18 = ((int) var7 & 4109484) >> 20;
            if (var7 != 0) {
                GameObject var13 = null;
                GameObject var14 = null;
                if (0 == typemask) {
                    Class70 var15 = LinkedList.method1209(plane, x, y);
                    if (null != var15) {
                        var13 = var15.aClass140_1049;
                        var14 = var15.aClass140_1052;
                    }

                    if (var12.ClipType != 0) {
                        var6.method1485(var18, var12.ProjectileClipped, y, var19, x);
                    }
                } else if (typemask == 1) {
                    Class19 x1 = Class39.method1037(plane, x, y);
                    if (x1 != null) {
                        var13 = x1.aClass140_429;
                        var14 = x1.aClass140_423;
                    }
                } else if (2 == typemask) {
                    Class25 x0 = Class163_Sub2.method2217(plane, x, y);
                    if (null != x0) {
                        var13 = x0.aClass140_479;
                    }

                    if (var12.ClipType != 0 && var12.SizeX + x < 104 && var12.SizeX + y < 104 && 104 > x + var12.SizeY && y + var12.SizeY < 104) {
                        var6.method1502(x, var12.SizeX, var12.ProjectileClipped, var18, var12.SizeY, y);
                    }
                } else {
                    Class12 x2 = Class159.method2193(plane, x, y);
                    if (x2 != null) {
                        var13 = x2.object;
                    }

                    if (var12.ClipType == 1) {
                        var6.method1499(y, x);
                    }
                }

                if (HDToolKit.highDetail && var12.aBoolean1503) {
                    if (2 == var19) {
                        if (var13 instanceof Class140_Sub3) {
                            ((Class140_Sub3) var13).method1960();
                        } else {
                            Scenery.method840(var12, (byte) -76, 0, var18 + 4, 0, var19, x, y, var5);
                        }

                        if (var14 instanceof Class140_Sub3) {
                            ((Class140_Sub3) var14).method1960();
                        } else {
                            Scenery.method840(var12, (byte) -100, 0, 3 & var18 - -1, 0, var19, x, y, var5);
                        }
                    } else if (5 != var19) {
                        if (var19 == 6) {
                            if (var13 instanceof Class140_Sub3) {
                                ((Class140_Sub3) var13).method1960();
                            } else {
                                Scenery.method840(var12, (byte) -28, 8 * Class163_Sub3.anIntArray3007[var18], 4 - -var18, 8 * Scenery.anIntArray2386[var18], 4, x, y, var5);
                            }
                        } else if (var19 == 7) {
                            if (var13 instanceof Class140_Sub3) {
                                ((Class140_Sub3) var13).method1960();
                            } else {
                                Scenery.method840(var12, (byte) -120, 0, 4 - -(3 & 2 + var18), 0, 4, x, y, var5);
                            }
                        } else if (var19 == 8) {
                            if (var13 instanceof Class140_Sub3) {
                                ((Class140_Sub3) var13).method1960();
                            } else {
                                Scenery.method840(var12, (byte) -45, Class163_Sub3.anIntArray3007[var18] * 8, var18 + 4, 8 * Scenery.anIntArray2386[var18], 4, x, y, var5);
                            }

                            if (var14 instanceof Class140_Sub3) {
                                ((Class140_Sub3) var14).method1960();
                            } else {
                                Scenery.method840(var12, (byte) -24, Class163_Sub3.anIntArray3007[var18] * 8, 4 - -(3 & 2 + var18), Scenery.anIntArray2386[var18] * 8, 4, x, y, var5);
                            }
                        } else if (11 != var19) {
                            if (var13 instanceof Class140_Sub3) {
                                ((Class140_Sub3) var13).method1960();
                            } else {
                                Scenery.method840(var12, (byte) -113, 0, var18, 0, var19, x, y, var5);
                            }
                        } else if (var13 instanceof Class140_Sub3) {
                            ((Class140_Sub3) var13).method1960();
                        } else {
                            Scenery.method840(var12, (byte) -115, 0, 4 + var18, 0, 10, x, y, var5);
                        }
                    } else if (var13 instanceof Class140_Sub3) {
                        ((Class140_Sub3) var13).method1960();
                    } else {
                        Scenery.method840(var12, (byte) -119, Class3_Sub24_Sub3.anIntArray3491[var18] * 8, var18, RenderAnimationDefinition.anIntArray356[var18] * 8, 4, x, y, var5);
                    }
                }
            }

        } catch (RuntimeException var16) {
            throw ClientErrorException.clientError(var16, "dg.B(" + -96 + ',' + y + ',' + x + ',' + plane + ',' + typemask + ',' + var5 + ',' + (var6 != null ? "{...}" : "null") + ')');
        }
    }

    static void method1683(int maxPlane, boolean var1, int plane, boolean var3, Class91 var4, int objectId, int type, int objectX, int objectY, int objectRot) {
        try {
            plane %= 4;
            if (var1 && !NPC.isHighDetail(103) && 0 == (2 & Unsorted.sceneryTypeMaskGrid[0][objectX][objectY])) {
                if (0 != (16 & Unsorted.sceneryTypeMaskGrid[plane][objectX][objectY])) {
                    return;
                }

                if (Class140_Sub3.viewportZ != PacketParser.method823(objectY, objectX, (byte) 50 ^ -127, plane)) {
                    return;
                }
            }

            if (Class85.anInt1174 > plane) {
                Class85.anInt1174 = plane;
            }

            ObjectDefinition def = ObjectDefinition.getObjectDefinition(objectId);
            if (!HDToolKit.highDetail || !def.aBoolean1530) {
                int var12;
                int var13;
                if (objectRot == 1 || objectRot == 3) {
                    var13 = def.SizeX;
                    var12 = def.SizeY;
                } else {
                    var12 = def.SizeX;
                    var13 = def.SizeY;
                }

                int var14;
                int var15;
                if (objectX + var12 <= 104) {
                    var14 = objectX + (var12 >> 1);
                    var15 = objectX - -(1 + var12 >> 1);
                } else {
                    var15 = 1 + objectX;
                    var14 = objectX;
                }

                int var17;
                int var16;
                if (var13 + objectY > 104) {
                    var16 = objectY;
                    var17 = objectY - -1;
                } else {
                    var16 = (var13 >> 1) + objectY;
                    var17 = objectY + (var13 + 1 >> 1);
                }

                int[][] var18 = Class44.anIntArrayArrayArray723[maxPlane];
                int var20 = (var12 << 6) + (objectX << 7);
                int var21 = (var13 << 6) + (objectY << 7);
                int var19 = var18[var14][var17] + var18[var15][var16] + var18[var14][var16] + var18[var15][var17] >> 2;
                int var22 = 0;
                int[][] var23;
                if (HDToolKit.highDetail && maxPlane != 0) {
                    var23 = Class44.anIntArrayArrayArray723[0];
                    var22 = var19 - (var23[var15][var17] + var23[var15][var16] + var23[var14][var16] + var23[var14][var17] >> 2);
                }

                var23 = null;
                long var24 = 1073741824 | objectX | objectY << 7 | type << 14 | objectRot << 20;
                if (var3) {
                    var23 = Class58.anIntArrayArrayArray914[0];
                } else if (maxPlane < 3) {
                    var23 = Class44.anIntArrayArrayArray723[1 + maxPlane];
                }

                if (0 == def.SecondInt || var3) {
                    var24 |= Long.MIN_VALUE;
                }

                if (1 == def.anInt1540) {
                    var24 |= 4194304L;
                }

                if (def.aBoolean1507) {
                    var24 |= 2147483648L;
                }

                if (def.method1690()) {
                    Class70.method1286(objectY, def, objectRot, null, objectX, plane, null);
                }

                boolean var26 = def.aBoolean1503 & !var3;
                var24 |= (long) objectId << 32;
                Object object;
                Class136 var28;
                if (22 != type) {
                    if (10 == type || 11 == type) {
                        if (def.animationId == -1 && def.ChildrenIds == null && !def.aBoolean1510) {
                            var28 = def.method1696(type == 11 ? 4 + objectRot : objectRot, var20, var18, 10, var19, var23, var1, null, (byte) -26, var26, var21);
                            if (HDToolKit.highDetail && var26) {
                                Class141.method2051(Objects.requireNonNull(var28).aClass109_Sub1_1770, var20, var22, var21);
                            }

                            object = Objects.requireNonNull(var28).aClass140_1777;
                        } else {
                            object = new Class140_Sub3(objectId, 10, 11 == type ? 4 - -objectRot : objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                        }

                        if (object != null) {
                            boolean var37 = method835(plane, objectX, objectY, var19, var12, var13, (GameObject) object, var24);
                            if (def.aBoolean1525 && var37 && var1) {
                                int var29 = 15;
                                if (object instanceof Model) {
                                    var29 = ((Model) object).method1888() / 4;
                                    if (var29 > 30) {
                                        var29 = 30;
                                    }
                                }

                                for (int var30 = 0; var30 <= var12; ++var30) {
                                    for (int var31 = 0; var13 >= var31; ++var31) {
                                        if (var29 > Class67.aByteArrayArrayArray1014[plane][objectX + var30][var31 + objectY]) {
                                            Class67.aByteArrayArrayArray1014[plane][objectX - -var30][objectY - -var31] = (byte) var29;
                                        }
                                    }
                                }
                            }
                        }

                        if (0 != def.ClipType && null != var4) {
                            var4.method1489(objectX, def.ProjectileClipped, (byte) 96, objectY, var12, var13);
                        }

                    } else if (12 <= type) {
                        if (-1 == def.animationId && null == def.ChildrenIds && !def.aBoolean1510) {
                            var28 = def.method1696(objectRot, var20, var18, type, var19, var23, var1, null, (byte) -82, var26, var21);
                            if (HDToolKit.highDetail && var26) {
                                Class141.method2051(Objects.requireNonNull(var28).aClass109_Sub1_1770, var20, var22, var21);
                            }

                            object = Objects.requireNonNull(var28).aClass140_1777;
                        } else {
                            object = new Class140_Sub3(objectId, type, objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                        }

                        method835(plane, objectX, objectY, var19, 1, 1, (GameObject) object, var24);
                        if (var1 && type <= 17 && type != 13 && plane > 0) {
                            Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY], 4);
                        }

                        if (def.ClipType != 0 && null != var4) {
                            var4.method1489(objectX, def.ProjectileClipped, (byte) 73, objectY, var12, var13);
                        }

                    } else if (0 == type) {
                        if (def.animationId == -1 && null == def.ChildrenIds && !def.aBoolean1510) {
                            var28 = def.method1696(objectRot, var20, var18, 0, var19, var23, var1, null, (byte) -74, var26, var21);
                            if (HDToolKit.highDetail && var26) {
                                Class141.method2051(Objects.requireNonNull(var28).aClass109_Sub1_1770, var20, var22, var21);
                            }

                            object = Objects.requireNonNull(var28).aClass140_1777;
                        } else {
                            object = new Class140_Sub3(objectId, 0, objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                        }

                        Class154.method2146(plane, objectX, objectY, var19, (GameObject) object, null, Class159.anIntArray2017[objectRot], 0, var24);
                        if (var1) {
                            if (objectRot == 0) {
                                if (def.aBoolean1525) {
                                    Class67.aByteArrayArrayArray1014[plane][objectX][objectY] = 50;
                                    Class67.aByteArrayArrayArray1014[plane][objectX][1 + objectY] = 50;
                                }

                                if (def.aBoolean1542) {
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY], 1);
                                }
                            } else if (1 == objectRot) {
                                if (def.aBoolean1525) {
                                    Class67.aByteArrayArrayArray1014[plane][objectX][objectY - -1] = 50;
                                    Class67.aByteArrayArrayArray1014[plane][objectX - -1][objectY + 1] = 50;
                                }

                                if (def.aBoolean1542) {
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][1 + objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][1 + objectY], 2);
                                }
                            } else if (objectRot == 2) {
                                if (def.aBoolean1525) {
                                    Class67.aByteArrayArrayArray1014[plane][objectX + 1][objectY] = 50;
                                    Class67.aByteArrayArrayArray1014[plane][1 + objectX][1 + objectY] = 50;
                                }

                                if (def.aBoolean1542) {
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX - -1][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX - -1][objectY], 1);
                                }
                            } else if (objectRot == 3) {
                                if (def.aBoolean1525) {
                                    Class67.aByteArrayArrayArray1014[plane][objectX][objectY] = 50;
                                    Class67.aByteArrayArrayArray1014[plane][1 + objectX][objectY] = 50;
                                }

                                if (def.aBoolean1542) {
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY], 2);
                                }
                            }
                        }

                        if (0 != def.ClipType && var4 != null) {
                            var4.method1486(objectRot, type, def.ProjectileClipped, objectY, objectX);
                        }

                        if (def.anInt1528 != 16) {
                            PositionedGraphicObject.method1956(plane, objectX, objectY, def.anInt1528);
                        }

                    } else if (type == 1) {
                        if (-1 == def.animationId && def.ChildrenIds == null && !def.aBoolean1510) {
                            var28 = def.method1696(objectRot, var20, var18, 1, var19, var23, var1, null, (byte) -83, var26, var21);
                            if (HDToolKit.highDetail && var26) {
                                Class141.method2051(Objects.requireNonNull(var28).aClass109_Sub1_1770, var20, var22, var21);
                            }

                            object = Objects.requireNonNull(var28).aClass140_1777;
                        } else {
                            object = new Class140_Sub3(objectId, 1, objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                        }

                        Class154.method2146(plane, objectX, objectY, var19, (GameObject) object, null, Class40.anIntArray675[objectRot], 0, var24);
                        if (def.aBoolean1525 && var1) {
                            if (0 == objectRot) {
                                Class67.aByteArrayArrayArray1014[plane][objectX][objectY + 1] = 50;
                            } else if (objectRot == 1) {
                                Class67.aByteArrayArrayArray1014[plane][objectX - -1][1 + objectY] = 50;
                            } else if (objectRot == 2) {
                                Class67.aByteArrayArrayArray1014[plane][1 + objectX][objectY] = 50;
                            } else if (3 == objectRot) {
                                Class67.aByteArrayArrayArray1014[plane][objectX][objectY] = 50;
                            }
                        }

                        if (def.ClipType != 0 && null != var4) {
                            var4.method1486(objectRot, type, def.ProjectileClipped, objectY, objectX);
                        }

                    } else {
                        int var43;
                        if (type == 2) {
                            var43 = 1 + objectRot & 3;
                            Object var38;
                            Object var42;
                            if (def.animationId == -1 && def.ChildrenIds == null && !def.aBoolean1510) {
                                Class136 var45 = def.method1696(objectRot + 4, var20, var18, 2, var19, var23, var1, null, (byte) -108, var26, var21);
                                if (HDToolKit.highDetail && var26) {
                                    Class141.method2051(Objects.requireNonNull(var45).aClass109_Sub1_1770, var20, var22, var21);
                                }

                                var42 = Objects.requireNonNull(var45).aClass140_1777;
                                var45 = def.method1696(var43, var20, var18, 2, var19, var23, var1, null, (byte) -69, var26, var21);
                                if (HDToolKit.highDetail && var26) {
                                    Class141.method2051(Objects.requireNonNull(var45).aClass109_Sub1_1770, var20, var22, var21);
                                }

                                var38 = Objects.requireNonNull(var45).aClass140_1777;
                            } else {
                                var42 = new Class140_Sub3(objectId, 2, 4 + objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                                var38 = new Class140_Sub3(objectId, 2, var43, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                            }

                            Class154.method2146(plane, objectX, objectY, var19, (GameObject) var42, (GameObject) var38, Class159.anIntArray2017[objectRot], Class159.anIntArray2017[var43], var24);
                            if (def.aBoolean1542 && var1) {
                                if (objectRot == 0) {
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY], 1);
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][1 + objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][1 + objectY], 2);
                                } else if (objectRot == 1) {
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY - -1] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY - -1], 2);
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX - -1][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX - -1][objectY], 1);
                                } else if (objectRot == 2) {
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][1 + objectX][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][1 + objectX][objectY], 1);
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY], 2);
                                } else if (objectRot == 3) {
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY], 2);
                                    Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[plane][objectX][objectY], 1);
                                }
                            }

                            if (def.ClipType != 0 && var4 != null) {
                                var4.method1486(objectRot, type, def.ProjectileClipped, objectY, objectX);
                            }

                            if (def.anInt1528 != 16) {
                                PositionedGraphicObject.method1956(plane, objectX, objectY, def.anInt1528);
                            }

                        } else if (type == 3) {
                            if (def.animationId == -1 && null == def.ChildrenIds && !def.aBoolean1510) {
                                var28 = def.method1696(objectRot, var20, var18, 3, var19, var23, var1, null, (byte) -54, var26, var21);
                                if (HDToolKit.highDetail && var26) {
                                    Class141.method2051(Objects.requireNonNull(var28).aClass109_Sub1_1770, var20, var22, var21);
                                }

                                object = Objects.requireNonNull(var28).aClass140_1777;
                            } else {
                                object = new Class140_Sub3(objectId, 3, objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                            }

                            Class154.method2146(plane, objectX, objectY, var19, (GameObject) object, null, Class40.anIntArray675[objectRot], 0, var24);
                            if (def.aBoolean1525 && var1) {
                                if (0 == objectRot) {
                                    Class67.aByteArrayArrayArray1014[plane][objectX][objectY + 1] = 50;
                                } else if (objectRot == 1) {
                                    Class67.aByteArrayArrayArray1014[plane][1 + objectX][objectY + 1] = 50;
                                } else if (objectRot == 2) {
                                    Class67.aByteArrayArrayArray1014[plane][1 + objectX][objectY] = 50;
                                } else if (objectRot == 3) {
                                    Class67.aByteArrayArrayArray1014[plane][objectX][objectY] = 50;
                                }
                            }

                            if (0 != def.ClipType && var4 != null) {
                                var4.method1486(objectRot, type, def.ProjectileClipped, objectY, objectX);
                            }

                        } else if (type == 9) {
                            if (def.animationId == -1 && def.ChildrenIds == null && !def.aBoolean1510) {
                                var28 = def.method1696(objectRot, var20, var18, type, var19, var23, var1, null, (byte) -30, var26, var21);
                                if (HDToolKit.highDetail && var26) {
                                    Class141.method2051(Objects.requireNonNull(var28).aClass109_Sub1_1770, var20, var22, var21);
                                }

                                object = Objects.requireNonNull(var28).aClass140_1777;
                            } else {
                                object = new Class140_Sub3(objectId, type, objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                            }

                            method835(plane, objectX, objectY, var19, 1, 1, (GameObject) object, var24);
                            if (def.ClipType != 0 && var4 != null) {
                                var4.method1489(objectX, def.ProjectileClipped, (byte) 127, objectY, var12, var13);
                            }

                            if (def.anInt1528 != 16) {
                                PositionedGraphicObject.method1956(plane, objectX, objectY, def.anInt1528);
                            }

                        } else if (4 == type) {
                            if (def.animationId == -1 && null == def.ChildrenIds && !def.aBoolean1510) {
                                var28 = def.method1696(objectRot, var20, var18, 4, var19, var23, var1, null, (byte) -103, var26, var21);
                                if (HDToolKit.highDetail && var26) {
                                    Class141.method2051(Objects.requireNonNull(var28).aClass109_Sub1_1770, var20, var22, var21);
                                }

                                object = Objects.requireNonNull(var28).aClass140_1777;
                            } else {
                                object = new Class140_Sub3(objectId, 4, objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                            }

                            Class3_Sub28_Sub8.method577(plane, objectX, objectY, var19, (GameObject) object, null, Class159.anIntArray2017[objectRot], 0, 0, 0, var24);
                        } else {
                            Object var39;
                            Class136 var47;
                            long var44;
                            if (type == 5) {
                                var43 = 16;
                                var44 = Scenery.lookupTypemask0(plane, objectX, objectY);
                                if (var44 != 0) {
                                    var43 = ObjectDefinition.getObjectDefinition(Integer.MAX_VALUE & (int) (var44 >>> 32)).anInt1528;
                                }

                                if (def.animationId == -1 && null == def.ChildrenIds && !def.aBoolean1510) {
                                    var47 = def.method1696(objectRot, var20, var18, 4, var19, var23, var1, null, (byte) -125, var26, var21);
                                    if (HDToolKit.highDetail && var26) {
                                        Class141.method2051(Objects.requireNonNull(var47).aClass109_Sub1_1770, var20 + -(RenderAnimationDefinition.anIntArray356[objectRot] * 8), var22, -(Class3_Sub24_Sub3.anIntArray3491[objectRot] * 8) + var21);
                                    }

                                    var39 = Objects.requireNonNull(var47).aClass140_1777;
                                } else {
                                    var39 = new Class140_Sub3(objectId, 4, objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                                }

                                Class3_Sub28_Sub8.method577(plane, objectX, objectY, var19, (GameObject) var39, null, Class159.anIntArray2017[objectRot], 0, var43 * RenderAnimationDefinition.anIntArray356[objectRot], Class3_Sub24_Sub3.anIntArray3491[objectRot] * var43, var24);
                            } else if (type == 6) {
                                var43 = 8;
                                var44 = Scenery.lookupTypemask0(plane, objectX, objectY);
                                if (var44 != 0) {
                                    var43 = ObjectDefinition.getObjectDefinition(Integer.MAX_VALUE & (int) (var44 >>> 32)).anInt1528 / 2;
                                }

                                if (def.animationId == -1 && def.ChildrenIds == null && !def.aBoolean1510) {
                                    var47 = def.method1696(objectRot + 4, var20, var18, 4, var19, var23, var1, null, (byte) -65, var26, var21);
                                    if (HDToolKit.highDetail && var26) {
                                        Class141.method2051(Objects.requireNonNull(var47).aClass109_Sub1_1770, -(8 * anIntArray2386[objectRot]) + var20, var22, -(8 * Class163_Sub3.anIntArray3007[objectRot]) + var21);
                                    }

                                    var39 = Objects.requireNonNull(var47).aClass140_1777;
                                } else {
                                    var39 = new Class140_Sub3(objectId, 4, 4 + objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                                }

                                Class3_Sub28_Sub8.method577(plane, objectX, objectY, var19, (GameObject) var39, null, 256, objectRot, var43 * anIntArray2386[objectRot], var43 * Class163_Sub3.anIntArray3007[objectRot], var24);
                            } else if (7 == type) {
                                int var40 = 3 & objectRot - -2;
                                if (def.animationId == -1 && def.ChildrenIds == null && !def.aBoolean1510) {
                                    Class136 var41 = def.method1696(var40 - -4, var20, var18, 4, var19, var23, var1, null, (byte) -39, var26, var21);
                                    if (HDToolKit.highDetail && var26) {
                                        Class141.method2051(Objects.requireNonNull(var41).aClass109_Sub1_1770, var20, var22, var21);
                                    }

                                    object = Objects.requireNonNull(var41).aClass140_1777;
                                } else {
                                    object = new Class140_Sub3(objectId, 4, var40 + 4, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                                }

                                Class3_Sub28_Sub8.method577(plane, objectX, objectY, var19, (GameObject) object, null, 256, var40, 0, 0, var24);
                            } else if (type == 8) {
                                var43 = 8;
                                var44 = Scenery.lookupTypemask0(plane, objectX, objectY);
                                if (var44 != 0) {
                                    var43 = ObjectDefinition.getObjectDefinition(Integer.MAX_VALUE & (int) (var44 >>> 32)).anInt1528 / 2;
                                }

                                int var32 = objectRot + 2 & 3;
                                Object var46;
                                if (-1 == def.animationId && null == def.ChildrenIds && !def.aBoolean1510) {
                                    int var34 = 8 * Class163_Sub3.anIntArray3007[objectRot];
                                    int var33 = anIntArray2386[objectRot] * 8;
                                    Class136 var35 = def.method1696(4 + objectRot, var20, var18, 4, var19, var23, var1, null, (byte) -25, var26, var21);
                                    if (HDToolKit.highDetail && var26) {
                                        Class141.method2051(Objects.requireNonNull(var35).aClass109_Sub1_1770, var20 + -var33, var22, -var34 + var21);
                                    }

                                    var39 = Objects.requireNonNull(var35).aClass140_1777;
                                    var35 = def.method1696(var32 - -4, var20, var18, 4, var19, var23, var1, null, (byte) -101, var26, var21);
                                    if (HDToolKit.highDetail && var26) {
                                        Class141.method2051(Objects.requireNonNull(var35).aClass109_Sub1_1770, var20 - var33, var22, -var34 + var21);
                                    }

                                    var46 = Objects.requireNonNull(var35).aClass140_1777;
                                } else {
                                    var39 = new Class140_Sub3(objectId, 4, 4 + objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                                    var46 = new Class140_Sub3(objectId, 4, var32 + 4, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                                }

                                Class3_Sub28_Sub8.method577(plane, objectX, objectY, var19, (GameObject) var39, (GameObject) var46, 256, objectRot, var43 * anIntArray2386[objectRot], Class163_Sub3.anIntArray3007[objectRot] * var43, var24);
                            }
                        }
                    }
                } else if (KeyboardListener.aBoolean1905 || def.SecondInt != 0 || def.ClipType == 1 || def.aBoolean1483) {
                    if (def.animationId == -1 && def.ChildrenIds == null && !def.aBoolean1510) {
                        var28 = def.method1696(objectRot, var20, var18, 22, var19, var23, var1, null, (byte) -126, var26, var21);
                        if (HDToolKit.highDetail && var26) {
                            Class141.method2051(Objects.requireNonNull(var28).aClass109_Sub1_1770, var20, var22, var21);
                        }

                        object = Objects.requireNonNull(var28).aClass140_1777;
                    } else {
                        object = new Class140_Sub3(objectId, 22, objectRot, maxPlane, objectX, objectY, def.animationId, def.aBoolean1492, null);
                    }
                    TextureOperation39.method276(plane, objectX, objectY, var19, (GameObject) object, var24, def.aBoolean1502);
                    if (def.ClipType == 1 && null != var4) {
                        var4.method1503(objectX, objectY);
                    }

                }
            }
        } catch (RuntimeException var36) {
            throw ClientErrorException.clientError(var36, "p.B(" + maxPlane + ',' + var1 + ',' + plane + ',' + var3 + ',' + (var4 != null ? "{...}" : "null") + ',' + objectId + ',' + type + ',' + objectX + ',' + (byte) 50 + ',' + objectY + ',' + objectRot + ')');
        }
    }

    static boolean method835(int var0, int var1, int var2, int var3, int var4, int var5, GameObject var6, long var8) {
        if (var6 == null) {
            return true;
        } else {
            int var10 = var1 * 128 + 64 * var4;
            int var11 = var2 * 128 + 64 * var5;
            return Scenery.method1189(var0, var1, var2, var4, var5, var10, var11, var3, var6, 0, false, var8);
        }
    }

    static boolean method1189(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, GameObject var8, int var9, boolean var10, long var11) {
        boolean var13 = Class44.anIntArrayArrayArray723 == Unsorted.anIntArrayArrayArray3605;
        int var14 = 0;

        int var16;
        for (int var15 = var1; var15 < var1 + var3; ++var15) {
            for (var16 = var2; var16 < var2 + var4; ++var16) {
                if (var15 < 0 || var16 < 0 || var15 >= Unsorted.width1234 || var16 >= TextureOperation17.height3179) {
                    return false;
                }

                TileData var17 = TileData.aTileDataArrayArrayArray2638[var0][var15][var16];
                if (var17 != null && var17.anInt2223 >= 5) {//@splinter
                    return false;
                }
            }
        }

        Class25 var20 = new Class25();
        var20.aLong498 = var11;
        var20.anInt493 = var0;
        var20.anInt482 = var5;
        var20.anInt484 = var6;
        var20.anInt489 = var7;
        var20.aClass140_479 = var8;
        var20.anInt496 = var9;
        var20.anInt483 = var1;
        var20.anInt478 = var2;
        var20.anInt495 = var1 + var3 - 1;
        var20.anInt481 = var2 + var4 - 1;

        int var21;
        for (var16 = var1; var16 < var1 + var3; ++var16) {
            for (var21 = var2; var21 < var2 + var4; ++var21) {
                int var18 = 0;
                if (var16 > var1) {
                    ++var18;
                }

                if (var16 < var1 + var3 - 1) {
                    var18 += 4;
                }

                if (var21 > var2) {
                    var18 += 8;
                }

                if (var21 < var2 + var4 - 1) {
                    var18 += 2;
                }

                for (int var19 = var0; var19 >= 0; --var19) {
                    if (TileData.aTileDataArrayArrayArray2638[var19][var16][var21] == null) {
                        TileData.aTileDataArrayArrayArray2638[var19][var16][var21] = new TileData(var19, var16, var21);
                    }
                }

                TileData var22 = TileData.aTileDataArrayArrayArray2638[var0][var16][var21];
                var22.aClass25Array2221[var22.anInt2223] = var20;
                var22.anIntArray2237[var22.anInt2223] = var18;
                var22.anInt2228 |= var18;
                ++var22.anInt2223;
                if (var13 && TextureOperation16.anIntArrayArray3115[var16][var21] != 0) {
                    var14 = TextureOperation16.anIntArrayArray3115[var16][var21];
                }
            }
        }

        if (var13 && var14 != 0) {
            for (var16 = var1; var16 < var1 + var3; ++var16) {
                for (var21 = var2; var21 < var2 + var4; ++var21) {
                    if (TextureOperation16.anIntArrayArray3115[var16][var21] == 0) {
                        TextureOperation16.anIntArrayArray3115[var16][var21] = var14;
                    }
                }
            }
        }

        if (var10) {
            SequenceDefinition.aClass25Array1868[Unsorted.anInt3070++] = var20;
        }

        return true;
    }

    static void method840(ObjectDefinition var0, byte var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        try {
            int var9 = 3 & var3;
            if (var1 >= -1) {
                TextCore.aString_106 = null;
            }

            int var10;
            int var11;
            if (var9 == 1 || var9 == 3) {
                var10 = var0.SizeY;
                var11 = var0.SizeX;
            } else {
                var11 = var0.SizeY;
                var10 = var0.SizeX;
            }

            int var14;
            int var15;
            if (var7 - -var11 > 104) {
                var15 = 1 + var7;
                var14 = var7;
            } else {
                var14 = var7 - -(var11 >> 1);
                var15 = var7 - -(1 + var11 >> 1);
            }

            int var16 = (var6 << 7) - -(var10 << 6);
            int var17 = (var7 << 7) + (var11 << 6);
            int var12;
            int var13;
            if (104 < var6 - -var10) {
                var12 = var6;
                var13 = var6 + 1;
            } else {
                var12 = var6 + (var10 >> 1);
                var13 = (var10 - -1 >> 1) + var6;
            }

            int[][] var18 = Class44.anIntArrayArrayArray723[var8];
            int var20 = 0;
            int var19 = var18[var12][var15] + var18[var12][var14] + var18[var13][var14] + var18[var13][var15] >> 2;
            int[][] var21;
            if (var8 != 0) {
                var21 = Class44.anIntArrayArrayArray723[0];
                var20 = -(var21[var12][var15] + var21[var13][var14] + (var21[var12][var14] - -var21[var13][var15]) >> 2) + var19;
            }

            var21 = null;
            if (3 > var8) {
                var21 = Class44.anIntArrayArrayArray723[1 + var8];
            }

            Class136 var22 = var0.method1696(var3, var16, var18, var5, var19, var21, false, null, (byte) -69, true, var17);
            Class141.method2047(Objects.requireNonNull(var22).aClass109_Sub1_1770, -var4 + var16, var20, var17 + -var2);
        } catch (RuntimeException var23) {
            throw ClientErrorException.clientError(var23, "al.K(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ')');
        }
    }

    static int sceneryPositionHash(int plane, int var1, int tileX, int tileY) {
        try {
            if (null == Class44.anIntArrayArrayArray723) {
                return 0;
            } else {
                int x = tileX >> 7;
                int y = tileY >> 7;
                if (x >= 0 && 0 <= y && x <= 103 && 103 >= y) {
                    int var7 = 127 & tileX;
                    int var8 = tileY & 127;
                    int z = plane;
                    if (3 > plane && (2 & Unsorted.sceneryTypeMaskGrid[1][x][y]) == 2) {
                        z = plane + 1;
                    }

                    int var10 = var7 * Class44.anIntArrayArrayArray723[z][var1 + x][1 + y] + Class44.anIntArrayArrayArray723[z][x][y + 1] * (-var7 + 128) >> 7;
                    int var9 = var7 * Class44.anIntArrayArrayArray723[z][x + 1][y] + (-var7 + 128) * Class44.anIntArrayArrayArray723[z][x][y] >> 7;
                    return var8 * var10 + (128 - var8) * var9 >> 7;
                } else {
                    return 0;
                }
            }
        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "ql.B(" + plane + ',' + var1 + ',' + tileX + ',' + tileY + ')');
        }
    }
}
