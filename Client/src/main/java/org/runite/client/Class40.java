package org.runite.client;

import org.rs09.client.config.GameConfig;
import org.runite.client.drawcalls.LoadingBox;

public final class Class40 {

    static RSString aClass94_672 = RSString.parse("null");
    static int[] anIntArray675 = new int[]{16, 32, 64, 128};
    static int anInt677 = 0;
    static CacheIndex aClass153_679;
    public static AbstractSprite aAbstractSprite_680;
    static byte[][] aByteArrayArray3669;
    static byte[][] aByteArrayArray3057;
    static int anInt3293 = 0;


    static int method1040(int var0, int var1, int var3) {
        try {
            return var1 < var3 ? var3 : var1 > var0 ? var0 : var1;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "gd.C(" + var0 + ',' + var1 + ',' + (byte) 0 + ',' + var3 + ')');
        }
    }

    static void method1041(long var0, RSString name) {
        try {
            // System.out.println("Class 40 " + var0 + ", " + var2 + ", " + name.toString());
            TextureOperation12.outgoingBuffer.index = 0;
            TextureOperation12.outgoingBuffer.writeByte(186);
            TextureOperation12.outgoingBuffer.writeString(name);
            // Class3_Sub13_Sub1.outgoingBuffer.putLong(var0, var2 + -2037463204);
            Unsorted.registryStage = 1;
            Class132.anInt1734 = 0;
            GraphicDefinition.anInt548 = 0;
            Unsorted.anInt1711 = -3;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "gd.D(" + var0 + ',' + -28236 + ')');
        }
    }

    static Class3_Sub28_Sub16_Sub2 method1043(int var0, CacheIndex var1, int archiveId) {
        try {
            // System.out.println("Class 40 " + archiveId);
            return Class75_Sub4.method1351(var1, var0, archiveId) ? Class117.method1722(-53) : null;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "gd.G(" + var0 + ',' + (var1 != null ? "{...}" : "null") + ',' + -3178 + ',' + archiveId + ')');
        }
    }

    static void method1045() {
        try {

            Class128.aReferenceCache_1683.clear();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "gd.B(" + -19761 + ')');
        }
    }

    static void method1046() {
        try {
            Class163_Sub1.ping(false);
            anInt3293 = 0;
            boolean var1 = true;

            int var2;
            for (var2 = 0; Class164_Sub2.aByteArrayArray3027.length > var2; ++var2) {
                if (Client.anIntArray2200[var2] != -1 && null == Class164_Sub2.aByteArrayArray3027[var2]) {
                    Class164_Sub2.aByteArrayArray3027[var2] = CacheIndex.landscapesIndex.getFile(Client.anIntArray2200[var2], 0);
                    if (Class164_Sub2.aByteArrayArray3027[var2] == null) {
                        ++anInt3293;
                        var1 = false;
                    }
                }

                if (-1 != Class101.anIntArray1426[var2] && null == Class3_Sub22.aByteArrayArray2521[var2]) {
                    Class3_Sub22.aByteArrayArray2521[var2] = CacheIndex.landscapesIndex.getFile(Class101.anIntArray1426[var2], 0, Class39.regionXteaKeys[var2]);
                    if (null == Class3_Sub22.aByteArrayArray2521[var2]) {
                        var1 = false;
                        ++anInt3293;
                    }
                }

                if (HDToolKit.highDetail) {
                    if (TextureOperation17.anIntArray3181[var2] != -1 && aByteArrayArray3669[var2] == null) {
                        aByteArrayArray3669[var2] = CacheIndex.landscapesIndex.getFile(TextureOperation17.anIntArray3181[var2], 0);
                        if (null == aByteArrayArray3669[var2]) {
                            var1 = false;
                            ++anInt3293;
                        }
                    }

                    if (Class3_Sub28_Sub5.anIntArray3587[var2] != -1 && null == aByteArrayArray3057[var2]) {
                        aByteArrayArray3057[var2] = CacheIndex.landscapesIndex.getFile(Class3_Sub28_Sub5.anIntArray3587[var2], 0);
                        if (null == aByteArrayArray3057[var2]) {
                            ++anInt3293;
                            var1 = false;
                        }
                    }
                }

                if (null != NPC.npcSpawnCacheIndices && null == TextureOperation35.aByteArrayArray3335[var2] && NPC.npcSpawnCacheIndices[var2] != -1) {
                    TextureOperation35.aByteArrayArray3335[var2] = CacheIndex.landscapesIndex.getFile(NPC.npcSpawnCacheIndices[var2], 0, Class39.regionXteaKeys[var2]);
                    if (TextureOperation35.aByteArrayArray3335[var2] == null) {
                        ++anInt3293;
                        var1 = false;
                    }
                }
            }

            if (TextureOperation22.aClass131_3421 == null) {
                if (null != TextureOperation37.aClass3_Sub28_Sub3_3264 && CacheIndex.worldmapIndex.method2135(RSString.stringCombiner(new RSString[]{TextureOperation37.aClass3_Sub28_Sub3_3264.aClass94_3561, TextCore.HasPlayerLabels}))) {
                    if (CacheIndex.worldmapIndex.method2127(RSString.stringCombiner(new RSString[]{TextureOperation37.aClass3_Sub28_Sub3_3264.aClass94_3561, TextCore.HasPlayerLabels}))) {
                        TextureOperation22.aClass131_3421 = Class81.getWorldMapArchive(RSString.stringCombiner(new RSString[]{TextureOperation37.aClass3_Sub28_Sub3_3264.aClass94_3561, TextCore.HasPlayerLabels}), CacheIndex.worldmapIndex);
                    } else {
                        var1 = false;
                        ++anInt3293;
                    }
                } else {
                    TextureOperation22.aClass131_3421 = new Class131(0);
                }
            }

            if (var1) {
                Class162.anInt2038 = 0;
                var1 = true;

                int var4;
                int var5;
                for (var2 = 0; var2 < Class164_Sub2.aByteArrayArray3027.length; ++var2) {
                    byte[] var3 = Class3_Sub22.aByteArrayArray2521[var2];
                    if (null != var3) {
                        var5 = -Texture.anInt1152 + (Class3_Sub24_Sub3.anIntArray3494[var2] & 0xFF) * 64;
                        var4 = -Class131.anInt1716 + (Class3_Sub24_Sub3.anIntArray3494[var2] >> 8) * 64;
                        if (LinkableRSString.isDynamicSceneGraph) {
                            var5 = 10;
                            var4 = 10;
                        }

                        var1 &= Class24.isValidObjectMapping((byte) -97, var4, var5, var3);
                    }

                    if (HDToolKit.highDetail) {
                        var3 = aByteArrayArray3057[var2];
                        if (null != var3) {
                            var4 = -Class131.anInt1716 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var2] >> 8);
                            var5 = -Texture.anInt1152 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var2] & 0xFF);
                            if (LinkableRSString.isDynamicSceneGraph) {
                                var5 = 10;
                                var4 = 10;
                            }

                            var1 &= Class24.isValidObjectMapping((byte) -74, var4, var5, var3);
                        }
                    }
                }

                if (var1) {
                    if (Class163_Sub2_Sub1.anInt4019 != 0) {
                        LoadingBox.draw(true, RSString.stringCombiner(new RSString[]{TextCore.LoadingPleaseWait2, TextCore.aClass94_2707}));
                    }

                    Class58.method1194();
                    TextureOperation13.method313((byte) 58);
                    boolean var11 = false;
                    int var12;
                    if (HDToolKit.highDetail && Class128.aBoolean1685) {
                        for (var12 = 0; var12 < Class164_Sub2.aByteArrayArray3027.length; ++var12) {
                            if (null != aByteArrayArray3057[var12] || aByteArrayArray3669[var12] != null) {
                                var11 = true;
                                break;
                            }
                        }
                    }

                    Class3_Sub4.initializeScene(HDToolKit.highDetail ? GameConfig.RENDER_DISTANCE_TILE_VALUE : 25, var11);

                    for (var12 = 0; 4 > var12; ++var12) {
                        AtmosphereParser.aClass91Array1182[var12].method1496();
                    }

                    for (var12 = 0; var12 < 4; ++var12) {
                        for (var4 = 0; var4 < 104; ++var4) {
                            for (var5 = 0; var5 < 104; ++var5) {
                                Unsorted.aByteArrayArrayArray113[var12][var4][var5] = 0;
                            }
                        }
                    }

                    Class164_Sub1.method2241((byte) -115, false);
                    if (HDToolKit.highDetail) {
                        Class141.aClass109_Sub1_1840.method1671();

                        for (var12 = 0; var12 < 13; ++var12) {
                            for (var4 = 0; var4 < 13; ++var4) {
                                Class141.aClass169ArrayArray1841[var12][var4].aBoolean2106 = true;
                            }
                        }
                    }

                    if (HDToolKit.highDetail) {
                        Class68.method1279();
                    }

                    if (HDToolKit.highDetail) {
                        Class39.method1036();
                    }

                    Class58.method1194();
                    System.gc();
                    Class163_Sub1.ping(true);
                    Class117.method1720(false, 105);
                    if (!LinkableRSString.isDynamicSceneGraph) {
                        Unsorted.method1091(false, -93);
                        Class163_Sub1.ping(true);
                        if (HDToolKit.highDetail) {
                            var12 = Class102.player.anIntArray2767[0] >> 3;
                            var4 = Class102.player.anIntArray2755[0] >> 3;
                            TextureOperation1.method220(var4, var12);
                        }

                        TextureOperation26.method198(false);
                        if (null != TextureOperation35.aByteArrayArray3335) {
                            TextureOperation37.method272((byte) -124);
                        }
                    }

                    if (LinkableRSString.isDynamicSceneGraph) {
                        Class49.method1121(false, (byte) 98);
                        Class163_Sub1.ping(true);
                        if (HDToolKit.highDetail) {
                            var12 = Class102.player.anIntArray2767[0] >> 3;
                            var4 = Class102.player.anIntArray2755[0] >> 3;
                            TextureOperation1.method220(var4, var12);
                        }

                        Class163_Sub2_Sub1.method2223(false, (byte) -121);
                    }

                    TextureOperation13.method313((byte) 90);
                    Class163_Sub1.ping(true);
                    method2189(AtmosphereParser.aClass91Array1182, false, 66);
                    if (HDToolKit.highDetail) {
                        Class68.method1270();
                    }

                    Class163_Sub1.ping(true);
                    var12 = Class85.anInt1174;
                    if (var12 > WorldListCountry.localPlane) {
                        var12 = WorldListCountry.localPlane;
                    }

                    if (WorldListCountry.localPlane + -1 > var12) {
                    }

                    if (NPC.method1986(39)) {
                        Class85.method1425(0);
                    } else {
                        Class85.method1425(Class85.anInt1174);
                    }

                    Class56.method1188();
                    if (HDToolKit.highDetail && var11) {
                        Class167.method2264(true);
                        Class117.method1720(true, 105);
                        if (!LinkableRSString.isDynamicSceneGraph) {
                            Unsorted.method1091(true, -121);
                            Class163_Sub1.ping(true);
                            TextureOperation26.method198(true);
                        }

                        if (LinkableRSString.isDynamicSceneGraph) {
                            Class49.method1121(true, (byte) 56);
                            Class163_Sub1.ping(true);
                            Class163_Sub2_Sub1.method2223(true, (byte) -105);
                        }

                        TextureOperation13.method313((byte) 102);
                        Class163_Sub1.ping(true);
                        method2189(AtmosphereParser.aClass91Array1182, true, 112);
                        Class163_Sub1.ping(true);
                        Class56.method1188();
                        Class167.method2264(false);
                    }

                    if (HDToolKit.highDetail) {
                        for (var4 = 0; var4 < 13; ++var4) {
                            for (var5 = 0; var5 < 13; ++var5) {
                                Class141.aClass169ArrayArray1841[var4][var5].method2281(Class44.anIntArrayArrayArray723[0], var4 * 8, var5 * 8);
                            }
                        }
                    }

                    for (var4 = 0; var4 < 104; ++var4) {
                        for (var5 = 0; 104 > var5; ++var5) {
                            Class128.method1760(var5, var4);
                        }
                    }

                    Unsorted.method792();
                    Class58.method1194();
                    method318();
                    TextureOperation13.method313((byte) 100);
                    TextureOperation25.aBoolean3416 = false;
                    if (GameShell.frame != null && null != Class3_Sub15.activeConnection && 25 == Class143.gameStage) {
                        TextureOperation12.outgoingBuffer.putOpcode(20);
                        TextureOperation12.outgoingBuffer.writeInt(1057001181);
                    }

                    if (!LinkableRSString.isDynamicSceneGraph) {
                        int var7 = (Class3_Sub7.anInt2294 + 6) / 8;
                        int var6 = (Class3_Sub7.anInt2294 - 6) / 8;
                        var4 = (Unsorted.anInt3606 - 6) / 8;
                        var5 = (Unsorted.anInt3606 - -6) / 8;

                        for (int var8 = var4 - 1; var5 - -1 >= var8; ++var8) {
                            for (int var9 = -1 + var6; var7 - -1 >= var9; ++var9) {
                                if (var4 > var8 || var8 > var5 || var6 > var9 || var9 > var7) {
                                    CacheIndex.landscapesIndex.method2124(-124, RSString.stringCombiner(new RSString[]{RSString.parse("m"), RSString.stringAnimator(var8), RSString.parse("_"), RSString.stringAnimator(var9)}));
                                    CacheIndex.landscapesIndex.method2124(-123, RSString.stringCombiner(new RSString[]{RSString.parse("l"), RSString.stringAnimator(var8), RSString.parse("_"), RSString.stringAnimator(var9)}));
                                }
                            }
                        }
                    }

                    if (Class143.gameStage == 28) {
                        Class117.method1719(10);
                    } else {
                        Class117.method1719(30);
                        if (null != Class3_Sub15.activeConnection) {
                            TextureOperation12.outgoingBuffer.putOpcode(110);
                        }
                    }

                    Class3_Sub20.method388((byte) 116);
                    Class58.method1194();
                    Class75_Sub4.method1355();
                } else {
                    Class163_Sub2_Sub1.anInt4019 = 2;
                }
            } else {
                Class163_Sub2_Sub1.anInt4019 = 1;
            }
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "gd.F(" + -117 + ')');
        }
    }

    static void method318() {
        try {
            Class3_Sub4 var1 = (Class3_Sub4) TextureOperation26.aLinkedList_3075.method1222();

            for (; null != var1; var1 = (Class3_Sub4) TextureOperation26.aLinkedList_3075.method1221()) {
                if (var1.anInt2259 == -1) {
                    var1.anInt2261 = 0;
                    Class132.method1798(56, var1);
                } else {
                    var1.unlink();
                }
            }

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rl.B(" + 7759444 + ')');
        }
    }

    static void method2189(Class91[] var0, boolean var1, int var2) {
        try {
            int var4;
            int var5;
            if (!var1) {
                for (var4 = 0; var4 < 4; ++var4) {
                    for (var5 = 0; var5 < 104; ++var5) {
                        for (int var6 = 0; var6 < 104; ++var6) {
                            if ((1 & Unsorted.aByteArrayArrayArray113[var4][var5][var6]) == 1) {
                                int var7 = var4;
                                if ((2 & Unsorted.aByteArrayArrayArray113[1][var5][var6]) == 2) {
                                    var7 = var4 - 1;
                                }

                                if (var7 >= 0) {
                                    var0[var7].method1497(var6, var5);
                                }
                            }
                        }
                    }
                }

                Class158_Sub1.anInt1463 += (int) (Math.random() * 5.0D) - 2;
                if (Class158_Sub1.anInt1463 < -16) {
                    Class158_Sub1.anInt1463 = -16;
                }

                if (Class158_Sub1.anInt1463 > 16) {
                    Class158_Sub1.anInt1463 = 16;
                }

                Class158_Sub1.anInt3158 += (int) (Math.random() * 5.0D) - 2;
                if (-8 > Class158_Sub1.anInt3158) {
                    Class158_Sub1.anInt3158 = -8;
                }

                if (Class158_Sub1.anInt3158 > 8) {
                    Class158_Sub1.anInt3158 = 8;
                }
            }

            byte var3;
            if (var1) {
                var3 = 1;
            } else {
                var3 = 4;
            }

            var4 = Class158_Sub1.anInt3158 >> 2 << 10;
            int[][] var34 = new int[104][104];
            int[][] var35 = new int[104][104];
            var5 = Class158_Sub1.anInt1463 >> 1;

            int var8;
            int var10;
            int var11;
            int var13;
            int var14;
            int var15;
            int var16;
            int var19;
            int var18;
            int var20;
            int var37;
            int var44;
            for (var8 = 0; var3 > var8; ++var8) {
                byte[][] var9 = Class67.aByteArrayArrayArray1014[var8];
                int var21;
                int var23;
                int var22;
                int var24;
                if (HDToolKit.highDetail) {
                    if (Class106.aBoolean1441) {
                        for (var10 = 1; var10 < 103; ++var10) {
                            for (var11 = 1; var11 < 103; ++var11) {
                                var13 = (var9[1 + var11][var10] >> 3) + (var9[-1 + var11][var10] >> 2) - -(var9[var11][-1 + var10] >> 2) - -(var9[var11][1 + var10] >> 3) - -(var9[var11][var10] >> 1);
                                byte var12 = 74;
                                var35[var11][var10] = -var13 + var12;
                            }
                        }
                    } else {
                        var10 = (int) Class92.light0Position[0];
                        var11 = (int) Class92.light0Position[1];
                        var37 = (int) Class92.light0Position[2];
                        var13 = (int) Math.sqrt(var11 * var11 + (var10 * var10 - -(var37 * var37)));
                        var14 = 1024 * var13 >> 8;

                        for (var15 = 1; var15 < 103; ++var15) {
                            for (var16 = 1; var16 < 103; ++var16) {
                                byte var17 = 96;
                                var18 = Class44.anIntArrayArrayArray723[var8][var16 - -1][var15] - Class44.anIntArrayArrayArray723[var8][-1 + var16][var15];
                                var19 = Class44.anIntArrayArrayArray723[var8][var16][var15 + 1] - Class44.anIntArrayArrayArray723[var8][var16][-1 + var15];
                                var20 = (int) Math.sqrt(var18 * var18 + 65536 + var19 * var19);
                                var21 = (var18 << 8) / var20;
                                var24 = (var9[var16][1 + var15] >> 3) + (var9[var16][var15 - 1] >> 2) + ((var9[var16 - 1][var15] >> 2) + (var9[var16 + 1][var15] >> 3) - -(var9[var16][var15] >> 1));
                                var22 = -65536 / var20;
                                var23 = (var19 << 8) / var20;
                                var44 = var17 + (var37 * var23 + (var10 * var21 - -(var22 * var11))) / var14;
                                var35[var16][var15] = var44 + -((int) ((float) var24 * 1.7F));
                            }
                        }
                    }
                } else {
                    var10 = (int) Math.sqrt(5100.0D);
                    var11 = 768 * var10 >> 8;

                    for (var37 = 1; var37 < 103; ++var37) {
                        for (var13 = 1; 103 > var13; ++var13) {
                            var16 = -Class44.anIntArrayArrayArray723[var8][var13][-1 + var37] + Class44.anIntArrayArrayArray723[var8][var13][var37 + 1];
                            byte var41 = 74;
                            var15 = -Class44.anIntArrayArrayArray723[var8][var13 + -1][var37] + Class44.anIntArrayArrayArray723[var8][var13 - -1][var37];
                            var44 = (int) Math.sqrt(var15 * var15 - -65536 - -(var16 * var16));
                            var20 = (var16 << 8) / var44;
                            var19 = -65536 / var44;
                            var18 = (var15 << 8) / var44;
                            var21 = (var9[var13][var37] >> 1) + (var9[var13][-1 + var37] >> 2) + (var9[var13 - -1][var37] >> 3) + ((var9[var13 - 1][var37] >> 2) - -(var9[var13][var37 + 1] >> 3));
                            var14 = var41 + (var20 * -50 + var18 * -50 - -(var19 * -10)) / var11;
                            var35[var13][var37] = var14 - var21;
                        }
                    }
                }

                for (var10 = 0; 104 > var10; ++var10) {
                    Class129.anIntArray1695[var10] = 0;
                    Unsorted.anIntArray1138[var10] = 0;
                    Class3_Sub31.anIntArray2606[var10] = 0;
                    MouseListeningClass.anIntArray1920[var10] = 0;
                    Unsorted.anIntArray2469[var10] = 0;
                }

                for (var10 = -5; var10 < 104; ++var10) {
                    for (var11 = 0; 104 > var11; ++var11) {
                        var37 = var10 - -5;
                        if (var37 < 104) {
                            var13 = 255 & TextureOperation36.aByteArrayArrayArray3430[var8][var37][var11];
                            if (var13 > 0) {
                                MapUnderlayColorDefinition var39 = MapUnderlayColorDefinition.method629(-1 + var13);
                                Class129.anIntArray1695[var11] += var39.anInt1408;
                                Unsorted.anIntArray1138[var11] += var39.anInt1406;
                                Class3_Sub31.anIntArray2606[var11] += var39.anInt1417;
                                MouseListeningClass.anIntArray1920[var11] += var39.anInt1418;
                                ++Unsorted.anIntArray2469[var11];
                            }
                        }

                        var13 = -5 + var10;
                        if (0 <= var13) {
                            var14 = 255 & TextureOperation36.aByteArrayArrayArray3430[var8][var13][var11];
                            if (var14 > 0) {
                                MapUnderlayColorDefinition var42 = MapUnderlayColorDefinition.method629(-1 + var14);
                                Class129.anIntArray1695[var11] -= var42.anInt1408;
                                Unsorted.anIntArray1138[var11] -= var42.anInt1406;
                                Class3_Sub31.anIntArray2606[var11] -= var42.anInt1417;
                                MouseListeningClass.anIntArray1920[var11] -= var42.anInt1418;
                                --Unsorted.anIntArray2469[var11];
                            }
                        }
                    }

                    if (var10 >= 0) {
                        var11 = 0;
                        var13 = 0;
                        var37 = 0;
                        var14 = 0;
                        var15 = 0;

                        for (var16 = -5; var16 < 104; ++var16) {
                            var44 = var16 - -5;
                            if (104 > var44) {
                                var37 += Unsorted.anIntArray1138[var44];
                                var15 += Unsorted.anIntArray2469[var44];
                                var11 += Class129.anIntArray1695[var44];
                                var14 += MouseListeningClass.anIntArray1920[var44];
                                var13 += Class3_Sub31.anIntArray2606[var44];
                            }

                            var18 = var16 + -5;
                            if (var18 >= 0) {
                                var37 -= Unsorted.anIntArray1138[var18];
                                var14 -= MouseListeningClass.anIntArray1920[var18];
                                var11 -= Class129.anIntArray1695[var18];
                                var15 -= Unsorted.anIntArray2469[var18];
                                var13 -= Class3_Sub31.anIntArray2606[var18];
                            }

                            if (0 <= var16 && var15 > 0 && var14 != 0) {
                                var34[var10][var16] = Class3_Sub8.method129(var13 / var15, var37 / var15, 256 * var11 / var14);
                            }
                        }
                    }
                }

                for (var10 = 1; var10 < 103; ++var10) {
                    label754:
                    for (var11 = 1; var11 < 103; ++var11) {
                        if (var1 || NPC.method1986(66) || (2 & Unsorted.aByteArrayArrayArray113[0][var10][var11]) != 0 || (16 & Unsorted.aByteArrayArrayArray113[var8][var10][var11]) == 0 && PacketParser.method823(var11, var10, -87, var8) == Class140_Sub3.anInt2745) {
                            if (var8 < Class85.anInt1174) {
                                Class85.anInt1174 = var8;
                            }

                            var37 = 255 & TextureOperation36.aByteArrayArrayArray3430[var8][var10][var11];
                            var13 = Class158_Sub1.aByteArrayArrayArray1828[var8][var10][var11] & 0xFF;
                            if (0 < var37 || var13 > 0) {
                                var15 = Class44.anIntArrayArrayArray723[var8][var10 + 1][var11];
                                var14 = Class44.anIntArrayArrayArray723[var8][var10][var11];
                                var44 = Class44.anIntArrayArrayArray723[var8][var10][1 + var11];
                                var16 = Class44.anIntArrayArrayArray723[var8][1 + var10][var11 + 1];
                                if (0 < var8) {
                                    boolean var47 = true;
                                    if (var37 == 0 && Unsorted.aByteArrayArrayArray1328[var8][var10][var11] != 0) {
                                        var47 = false;
                                    }

                                    if (var13 > 0 && !Class168.method350(var13 + -1).aBoolean2102) {
                                        var47 = false;
                                    }

                                    if (var47 && var14 == var15 && var16 == var14 && var14 == var44) {
                                        Class38_Sub1.anIntArrayArrayArray2609[var8][var10][var11] = TextureOperation3.bitwiseOr(Class38_Sub1.anIntArrayArrayArray2609[var8][var10][var11], 4);
                                    }
                                }

                                if (var37 <= 0) {
                                    var18 = -1;
                                    var19 = 0;
                                } else {
                                    var18 = var34[var10][var11];
                                    var20 = (var18 & 127) + var5;
                                    if (var20 >= 0) {
                                        if (var20 > 127) {
                                            var20 = 127;
                                        }
                                    } else {
                                        var20 = 0;
                                    }

                                    var21 = (896 & var18) + (var18 + var4 & '\ufc00') + var20;
                                    var19 = Class51.anIntArray834[Unsorted.method1100(96, var21)];
                                }

                                var20 = var35[var10][var11];
                                var23 = var35[var10][var11 + 1];
                                var21 = var35[1 + var10][var11];
                                var22 = var35[var10 - -1][var11 - -1];
                                if (var13 == 0) {
                                    method1629(var8, var10, var11, 0, 0, -1, var14, var15, var16, var44, Unsorted.method1100(var20, var18), Unsorted.method1100(var21, var18), Unsorted.method1100(var22, var18), Unsorted.method1100(var23, var18), 0, 0, 0, 0, var19, 0);
                                    if (HDToolKit.highDetail && var8 > 0 && var18 != -1 && MapUnderlayColorDefinition.method629(-1 + var37).aBoolean1411) {
                                        Class141.method2037(0, 0, true, false, var10, var11, var14 - Class44.anIntArrayArrayArray723[0][var10][var11], -Class44.anIntArrayArrayArray723[0][1 + var10][var11] + var15, var16 - Class44.anIntArrayArrayArray723[0][1 + var10][1 + var11], var44 - Class44.anIntArrayArrayArray723[0][var10][1 + var11]);
                                    }

                                    if (HDToolKit.highDetail && !var1 && TextureOperation16.anIntArrayArray3115 != null && 0 == var8) {
                                        for (var24 = var10 + -1; var10 - -1 >= var24; ++var24) {
                                            for (int var52 = -1 + var11; var52 <= 1 + var11; ++var52) {
                                                if ((var24 != var10 || var11 != var52) && var24 >= 0 && var24 < 104 && 0 <= var52 && var52 < 104) {
                                                    int var54 = Class158_Sub1.aByteArrayArrayArray1828[var8][var24][var52] & 0xFF;
                                                    if (var54 != 0) {
                                                        Class168 var53 = Class168.method350(-1 + var54);
                                                        if (var53.anInt2095 != -1 && 4 == Class51.anInterface2_838.method18(var53.anInt2095, 255)) {
                                                            TextureOperation16.anIntArrayArray3115[var10][var11] = var53.anInt2094 + (var53.anInt2101 << 24);
                                                            continue label754;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    var24 = 1 + Unsorted.aByteArrayArrayArray1328[var8][var10][var11];
                                    byte var25 = PacketParser.aByteArrayArrayArray81[var8][var10][var11];
                                    Class168 var26 = Class168.method350(var13 + -1);
                                    int var27;
                                    int var29;
                                    int var28;
                                    if (HDToolKit.highDetail && !var1 && null != TextureOperation16.anIntArrayArray3115 && 0 == var8) {
                                        if (-1 != var26.anInt2095 && Class51.anInterface2_838.method18(var26.anInt2095, 255) == 4) {
                                            TextureOperation16.anIntArrayArray3115[var10][var11] = (var26.anInt2101 << 24) + var26.anInt2094;
                                        } else {
                                            label722:
                                            for (var27 = var10 + -1; 1 + var10 >= var27; ++var27) {
                                                for (var28 = var11 + -1; 1 + var11 >= var28; ++var28) {
                                                    if ((var27 != var10 || var11 != var28) && var27 >= 0 && var27 < 104 && var28 >= 0 && var28 < 104) {
                                                        var29 = Class158_Sub1.aByteArrayArrayArray1828[var8][var27][var28] & 0xFF;
                                                        if (var29 != 0) {
                                                            Class168 var30 = Class168.method350(-1 + var29);
                                                            if (var30.anInt2095 != -1 && Class51.anInterface2_838.method18(var30.anInt2095, 255) == 4) {
                                                                TextureOperation16.anIntArrayArray3115[var10][var11] = var30.anInt2094 + (var30.anInt2101 << 24);
                                                                break label722;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    var27 = var26.anInt2095;
                                    if (0 <= var27 && !Class51.anInterface2_838.method17(var27, 101)) {
                                        var27 = -1;
                                    }

                                    int var31;
                                    int var55;
                                    if (var27 < 0) {
                                        if (var26.anInt2103 == -1) {
                                            var28 = -2;
                                            var29 = 0;
                                        } else {
                                            var28 = var26.anInt2103;
                                            var55 = var5 + (var28 & 127);
                                            if (var55 >= 0) {
                                                if (var55 > 127) {
                                                    var55 = 127;
                                                }
                                            } else {
                                                var55 = 0;
                                            }

                                            var31 = (var28 & 896) + (('\ufc00' & var28 + var4) - -var55);
                                            var29 = Class51.anIntArray834[LinkableRSString.method729((byte) -85, var31, 96)];
                                        }
                                    } else {
                                        var28 = -1;
                                        var29 = Class51.anIntArray834[LinkableRSString.method729((byte) -126, Class51.anInterface2_838.method15(var27, 65535), 96)];
                                    }

                                    if (var26.anInt2098 >= 0) {
                                        var55 = var26.anInt2098;
                                        var31 = var5 + (var55 & 127);
                                        if (var31 >= 0) {
                                            if (127 < var31) {
                                                var31 = 127;
                                            }
                                        } else {
                                            var31 = 0;
                                        }

                                        int var32 = (896 & var55) + (('\ufc00' & var55 + var4) - -var31);
                                        var29 = Class51.anIntArray834[LinkableRSString.method729((byte) -101, var32, 96)];
                                    }

                                    method1629(var8, var10, var11, var24, var25, var27, var14, var15, var16, var44, Unsorted.method1100(var20, var18), Unsorted.method1100(var21, var18), Unsorted.method1100(var22, var18), Unsorted.method1100(var23, var18), LinkableRSString.method729((byte) -72, var28, var20), LinkableRSString.method729((byte) -107, var28, var21), LinkableRSString.method729((byte) -82, var28, var22), LinkableRSString.method729((byte) -93, var28, var23), var19, var29);
                                    if (HDToolKit.highDetail && var8 > 0) {
                                        Class141.method2037(var24, var25, var28 == -2 || !var26.aBoolean2093, -1 == var18 || !MapUnderlayColorDefinition.method629(-1 + var37).aBoolean1411, var10, var11, -Class44.anIntArrayArrayArray723[0][var10][var11] + var14, var15 - Class44.anIntArrayArrayArray723[0][1 + var10][var11], -Class44.anIntArrayArrayArray723[0][1 + var10][var11 + 1] + var16, -Class44.anIntArrayArrayArray723[0][var10][1 + var11] + var44);
                                    }
                                }
                            }
                        }
                    }
                }

                if (HDToolKit.highDetail) {
                    float[][] var38 = new float[105][105];
                    int[][] var45 = Class44.anIntArrayArrayArray723[var8];
                    float[][] var40 = new float[105][105];
                    float[][] var43 = new float[105][105];

                    for (var14 = 1; var14 <= 103; ++var14) {
                        for (var15 = 1; var15 <= 103; ++var15) {
                            var44 = var45[var15][var14 - -1] + -var45[var15][-1 + var14];
                            var16 = -var45[var15 - 1][var14] + var45[var15 + 1][var14];
                            float var51 = (float) Math.sqrt(var16 * var16 - -65536 - -(var44 * var44));
                            var38[var15][var14] = (float) var16 / var51;
                            var40[var15][var14] = -256.0F / var51;
                            var43[var15][var14] = (float) var44 / var51;
                        }
                    }

                    Class3_Sub11[] var50;
                    if (var1) {
                        var50 = TextureOperation7.method298(Unsorted.aByteArrayArrayArray113, Unsorted.aByteArrayArrayArray1328[var8], TextureOperation36.aByteArrayArrayArray3430[var8], var35, var40, TextureOperation16.anIntArrayArray3115, Class158_Sub1.aByteArrayArrayArray1828[var8], PacketParser.aByteArrayArrayArray81[var8], var38, var8, var43, var34, Class44.anIntArrayArrayArray723[var8], Class58.anIntArrayArrayArray914[0]);
                        LinkedList.method1213(var8, var50);
                    } else {
                        var50 = TextureOperation7.method298(Unsorted.aByteArrayArrayArray113, Unsorted.aByteArrayArrayArray1328[var8], TextureOperation36.aByteArrayArrayArray3430[var8], var35, var40, null, Class158_Sub1.aByteArrayArrayArray1828[var8], PacketParser.aByteArrayArrayArray81[var8], var38, var8, var43, var34, Class44.anIntArrayArrayArray723[var8], null);
                        Class3_Sub11[] var46 = Class1.method70(var40, var38, Class44.anIntArrayArrayArray723[var8], var8, var43, PacketParser.aByteArrayArrayArray81[var8], var35, Unsorted.aByteArrayArrayArray1328[var8], TextureOperation36.aByteArrayArrayArray3430[var8], Class158_Sub1.aByteArrayArrayArray1828[var8], Unsorted.aByteArrayArrayArray113);
                        Class3_Sub11[] var49 = new Class3_Sub11[var50.length - -var46.length];

                        for (var44 = 0; var44 < var50.length; ++var44) {
                            var49[var44] = var50[var44];
                        }

                        for (var44 = 0; var44 < var46.length; ++var44) {
                            var49[var50.length + var44] = var46[var44];
                        }

                        LinkedList.method1213(var8, var49);
                        Class129.method1769(var43, TextureOperation36.aByteArrayArrayArray3430[var8], PacketParser.aByteArrayArrayArray81[var8], Class68.aClass43Array1021, var8, Class68.anInt1032, var40, Unsorted.aByteArrayArrayArray1328[var8], Class158_Sub1.aByteArrayArrayArray1828[var8], Class44.anIntArrayArrayArray723[var8], var38);
                    }
                }

                TextureOperation36.aByteArrayArrayArray3430[var8] = null;
                Class158_Sub1.aByteArrayArrayArray1828[var8] = null;
                Unsorted.aByteArrayArrayArray1328[var8] = null;
                PacketParser.aByteArrayArrayArray81[var8] = null;
                Class67.aByteArrayArrayArray1014[var8] = null;
            }

            if (var2 <= 26) {
                method2187(86);
            }

            Class128.method1764();
            if (!var1) {
                int var36;
                for (var8 = 0; 104 > var8; ++var8) {
                    for (var36 = 0; var36 < 104; ++var36) {
                        if ((Unsorted.aByteArrayArrayArray113[1][var8][var36] & 2) == 2) {
                            Class3_Sub28_Sub18.method709(var8, var36);
                        }
                    }
                }

                for (var8 = 0; 4 > var8; ++var8) {
                    for (var36 = 0; var36 <= 104; ++var36) {
                        for (var10 = 0; var10 <= 104; ++var10) {
                            short var48;
                            if ((Class38_Sub1.anIntArrayArrayArray2609[var8][var10][var36] & 1) != 0) {
                                var14 = var8;

                                for (var11 = var36; var11 > 0 && (1 & Class38_Sub1.anIntArrayArrayArray2609[var8][var10][-1 + var11]) != 0; --var11) {
                                }

                                var13 = var8;

                                for (var37 = var36; var37 < 104 && (1 & Class38_Sub1.anIntArrayArrayArray2609[var8][var10][var37 - -1]) != 0; ++var37) {
                                }

                                label453:
                                while (var13 > 0) {
                                    for (var15 = var11; var15 <= var37; ++var15) {
                                        if ((Class38_Sub1.anIntArrayArrayArray2609[var13 + -1][var10][var15] & 1) == 0) {
                                            break label453;
                                        }
                                    }

                                    --var13;
                                }

                                label464:
                                while (var14 < 3) {
                                    for (var15 = var11; var15 <= var37; ++var15) {
                                        if ((1 & Class38_Sub1.anIntArrayArrayArray2609[var14 + 1][var10][var15]) == 0) {
                                            break label464;
                                        }
                                    }

                                    ++var14;
                                }

                                var15 = (var14 - (-1 + var13)) * (-var11 + (var37 - -1));
                                if (var15 >= 8) {
                                    var48 = 240;
                                    var44 = -var48 + Class44.anIntArrayArrayArray723[var14][var10][var11];
                                    var18 = Class44.anIntArrayArrayArray723[var13][var10][var11];
                                    Class167.method2263(1, 128 * var10, 128 * var10, 128 * var11, var37 * 128 + 128, var44, var18);

                                    for (var19 = var13; var19 <= var14; ++var19) {
                                        for (var20 = var11; var37 >= var20; ++var20) {
                                            Class38_Sub1.anIntArrayArrayArray2609[var19][var10][var20] = Unsorted.bitwiseAnd(Class38_Sub1.anIntArrayArrayArray2609[var19][var10][var20], -2);
                                        }
                                    }
                                }
                            }

                            if ((2 & Class38_Sub1.anIntArrayArrayArray2609[var8][var10][var36]) != 0) {
                                for (var11 = var10; 0 < var11 && (Class38_Sub1.anIntArrayArrayArray2609[var8][-1 + var11][var36] & 2) != 0; --var11) {
                                }

                                var14 = var8;
                                var13 = var8;

                                for (var37 = var10; 104 > var37 && (2 & Class38_Sub1.anIntArrayArrayArray2609[var8][var37 - -1][var36]) != 0; ++var37) {
                                }

                                label503:
                                while (var13 > 0) {
                                    for (var15 = var11; var15 <= var37; ++var15) {
                                        if (0 == (2 & Class38_Sub1.anIntArrayArrayArray2609[-1 + var13][var15][var36])) {
                                            break label503;
                                        }
                                    }

                                    --var13;
                                }

                                label514:
                                while (var14 < 3) {
                                    for (var15 = var11; var15 <= var37; ++var15) {
                                        if ((2 & Class38_Sub1.anIntArrayArrayArray2609[var14 + 1][var15][var36]) == 0) {
                                            break label514;
                                        }
                                    }

                                    ++var14;
                                }

                                var15 = (-var11 + var37 - -1) * (-var13 + var14 - -1);
                                if (8 <= var15) {
                                    var48 = 240;
                                    var44 = Class44.anIntArrayArrayArray723[var14][var11][var36] - var48;
                                    var18 = Class44.anIntArrayArrayArray723[var13][var11][var36];
                                    Class167.method2263(2, var11 * 128, 128 * var37 + 128, 128 * var36, var36 * 128, var44, var18);

                                    for (var19 = var13; var14 >= var19; ++var19) {
                                        for (var20 = var11; var20 <= var37; ++var20) {
                                            Class38_Sub1.anIntArrayArrayArray2609[var19][var20][var36] = Unsorted.bitwiseAnd(Class38_Sub1.anIntArrayArrayArray2609[var19][var20][var36], -3);
                                        }
                                    }
                                }
                            }

                            if ((4 & Class38_Sub1.anIntArrayArrayArray2609[var8][var10][var36]) != 0) {
                                var11 = var10;
                                var37 = var10;

                                for (var13 = var36; 0 < var13 && 0 != (4 & Class38_Sub1.anIntArrayArrayArray2609[var8][var10][-1 + var13]); --var13) {
                                }

                                for (var14 = var36; var14 < 104 && (Class38_Sub1.anIntArrayArrayArray2609[var8][var10][var14 + 1] & 4) != 0; ++var14) {
                                }

                                label554:
                                while (var11 > 0) {
                                    for (var15 = var13; var14 >= var15; ++var15) {
                                        if (0 == (Class38_Sub1.anIntArrayArrayArray2609[var8][var11 + -1][var15] & 4)) {
                                            break label554;
                                        }
                                    }

                                    --var11;
                                }

                                label565:
                                while (var37 < 104) {
                                    for (var15 = var13; var14 >= var15; ++var15) {
                                        if (0 == (4 & Class38_Sub1.anIntArrayArrayArray2609[var8][1 + var37][var15])) {
                                            break label565;
                                        }
                                    }

                                    ++var37;
                                }

                                if (4 <= (1 + -var11 + var37) * (var14 - (var13 - 1))) {
                                    var15 = Class44.anIntArrayArrayArray723[var8][var11][var13];
                                    Class167.method2263(4, var11 * 128, 128 * var37 - -128, var13 * 128, 128 + 128 * var14, var15, var15);

                                    for (var16 = var11; var37 >= var16; ++var16) {
                                        for (var44 = var13; var14 >= var44; ++var44) {
                                            Class38_Sub1.anIntArrayArrayArray2609[var8][var16][var44] = Unsorted.bitwiseAnd(Class38_Sub1.anIntArrayArrayArray2609[var8][var16][var44], -5);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (RuntimeException var33) {
            throw ClientErrorException.clientError(var33, "di.K(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ',' + var2 + ')');
        }
    }

    public static void method2187(int var0) {
        try {
            Class158_Sub1.aClass94Array2977 = null;
            Class158_Sub1.aReferenceCache_2982 = null;
            Class158_Sub1.aClass3_Sub1_2980 = null;
            if (var0 != 27316) {
                Class158_Sub1.aBoolean2981 = true;
            }

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "di.I(" + var0 + ')');
        }
    }

    static void method1629(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19) {
        int var21;
        Class126 var20;
        if (var3 == 0) {
            var20 = new Class126(var10, var11, var12, var13, -1, var18, false);

            for (var21 = var0; var21 >= 0; --var21) {
                if (Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var21][var1][var2] == null) {
                    Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var21][var1][var2] = new Class3_Sub2(var21, var1, var2);
                }
            }

            Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2].aClass126_2240 = var20;
        } else if (var3 == 1) {
            var20 = new Class126(var14, var15, var16, var17, var5, var19, var6 == var7 && var6 == var8 && var6 == var9);

            for (var21 = var0; var21 >= 0; --var21) {
                if (Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var21][var1][var2] == null) {
                    Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var21][var1][var2] = new Class3_Sub2(var21, var1, var2);
                }
            }

            Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2].aClass126_2240 = var20;
        } else {
            Class35 var22 = new Class35(var3, var4, var5, var1, var2, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19);

            for (var21 = var0; var21 >= 0; --var21) {
                if (Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var21][var1][var2] == null) {
                    Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var21][var1][var2] = new Class3_Sub2(var21, var1, var2);
                }
            }

            Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2].aClass35_2226 = var22;
        }
    }
}
