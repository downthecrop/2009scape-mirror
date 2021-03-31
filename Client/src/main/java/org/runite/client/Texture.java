package org.runite.client;

import org.rs09.client.data.ReferenceCache;

import java.util.Objects;

public final class Texture {

    static int anInt2208 = -1;
    static int[] anIntArray2213 = new int[]{16776960, 16711680, 65280, 65535, 16711935, 16777215};
    static RSString[] aClass94Array3317 = new RSString[TextureOperation35.anInt3332];
    static int[] anIntArray3318 = new int[TextureOperation35.anInt3332];
    static int[] anIntArray3319 = new int[TextureOperation35.anInt3332];
    static int[] anIntArray3327 = new int[TextureOperation35.anInt3332];
    static int[] anIntArray3329 = new int[TextureOperation35.anInt3332];
    static int[] anIntArray3331 = new int[TextureOperation35.anInt3332];
    static int[] anIntArray3336 = new int[TextureOperation35.anInt3332];
    static int[] anIntArray3337 = new int[TextureOperation35.anInt3332];
    static int anInt1668 = -1;
    static ReferenceCache aReferenceCache_1146 = new ReferenceCache(64);
    static int anInt1150 = -1;
    public static int anInt1152;
    private final int[] anIntArray1144;
    private final TextureOperation aClass3_Sub13_1145;
    private final TextureOperation[] aClass3_Sub13Array1147;
    private final TextureOperation aClass3_Sub13_1148;
    private final int[] anIntArray1149;


    public Texture() {
        try {
            this.anIntArray1149 = new int[0];
            this.anIntArray1144 = new int[0];
            this.aClass3_Sub13_1145 = new TextureOperation0();
            this.aClass3_Sub13_1145.imageCacheCapacity = 1;
            this.aClass3_Sub13_1148 = new TextureOperation0();
            this.aClass3_Sub13Array1147 = new TextureOperation[]{this.aClass3_Sub13_1145, this.aClass3_Sub13_1148};
            this.aClass3_Sub13_1148.imageCacheCapacity = 1;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "lc.<init>()");
        }
    }

    Texture(DataBuffer var1) {
        try {
            int var2 = var1.readUnsignedByte();
            this.aClass3_Sub13Array1147 = new TextureOperation[var2];
            int[][] var5 = new int[var2][];
            int var4 = 0;
            int var3 = 0;

            int var6;
            TextureOperation textureOperation;
            int var8;
            int var9;
            for (var6 = 0; var2 > var6; ++var6) {
                textureOperation = decodeTexture(var1);
                if (0 <= textureOperation.method159(4)) {
                    ++var3;
                }

                if (textureOperation.getSpriteFrame() >= 0) {
                    ++var4;
                }

                var8 = textureOperation.subOperations.length;
                var5[var6] = new int[var8];

                for (var9 = 0; var9 < var8; ++var9) {
                    var5[var6][var9] = var1.readUnsignedByte();
                }

                this.aClass3_Sub13Array1147[var6] = textureOperation;
            }

            this.anIntArray1144 = new int[var3];
            this.anIntArray1149 = new int[var4];
            var3 = 0;
            var4 = 0;

            for (var6 = 0; var6 < var2; ++var6) {
                textureOperation = this.aClass3_Sub13Array1147[var6];
                var8 = textureOperation.subOperations.length;

                for (var9 = 0; var8 > var9; ++var9) {
                    textureOperation.subOperations[var9] = this.aClass3_Sub13Array1147[var5[var6][var9]];
                }

                var9 = textureOperation.method159(4);
                int var10 = textureOperation.getSpriteFrame();
                if (var9 > 0) {
                    this.anIntArray1144[var3++] = var9;
                }

                if (var10 > 0) {
                    this.anIntArray1149[var4++] = var10;
                }

                var5[var6] = null;
            }

            this.aClass3_Sub13_1145 = this.aClass3_Sub13Array1147[var1.readUnsignedByte()];
            this.aClass3_Sub13_1148 = this.aClass3_Sub13Array1147[var1.readUnsignedByte()];
        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "lc.<init>(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static void method1405(int var0, int var1, int var2, int var3, int var4, int var5, int var6) {
        try {
            TextureOperation8.anInt3464 = 0;

            int var7;
            int var15;
            int var19;
            int var21;
            int var22;
            int var29;
            int var32;
            for (var7 = -1; var7 < Class159.localPlayerCount + Class163.localNPCCount; ++var7) {
                Class140_Sub4 var8;
                if (var7 == -1) {
                    var8 = Class102.player;
                } else if (var7 < Class159.localPlayerCount) {
                    var8 = Unsorted.players[Class56.localPlayerIndexes[var7]];
                } else {
                    var8 = NPC.npcs[AudioThread.localNPCIndexes[-Class159.localPlayerCount + var7]];
                }

                if (null != var8 && var8.hasDefinitions()) {
                    NPCDefinition var9;
                    if (var8 instanceof NPC) {
                        var9 = ((NPC) var8).definition;
                        if (null != var9.childNPCs) {
                            var9 = var9.method1471((byte) -93);
                        }

                        if (var9 == null) {
                            continue;
                        }
                    }

                    int var12;
                    if (var7 < Class159.localPlayerCount) {
                        var19 = 30;
                        Player var10 = (Player) var8;
                        if (var10.skullIcon != -1 || -1 != var10.headIcon) {
                            Class107.method1647(var4 >> 1, var3, var8, var5, var8.method1975(var6 ^ -28716) - -15, var1 >> 1);
                            if (-1 < Class32.anInt590) {
                                if (var10.skullIcon != -1) {
                                    TextureOperation2.aAbstractSpriteArray3373[var10.skullIcon].drawAt(-12 + Class32.anInt590 + var2, -var19 + var0 + anInt2208);
                                    var19 += 25;
                                }

                                if (var10.headIcon != -1) {
                                    NPC.aAbstractSpriteArray3977[var10.headIcon].drawAt(-12 + var2 + Class32.anInt590, var0 - (-anInt2208 + var19));
                                    var19 += 25;
                                }
                            }
                        }

                        if (var7 >= 0) {
                            Class96[] var11 = ClientErrorException.aClass96Array2114;

                            for (var12 = 0; var12 < var11.length; ++var12) {
                                Class96 var13 = var11[var12];
                                if (null != var13 && var13.anInt1360 == 10 && Class56.localPlayerIndexes[var7] == var13.anInt1359) {
                                    Class107.method1647(var4 >> 1, var3, var8, var5, var8.method1975(var6 ^ -28716) - -15, var1 >> 1);
                                    if (Class32.anInt590 > -1) {
                                        Class166.aAbstractSpriteArray2072[var13.anInt1351].drawAt(var2 - (-Class32.anInt590 + 12), var0 + (anInt2208 - var19));
                                    }
                                }
                            }
                        }
                    } else {
                        var9 = ((NPC) var8).definition;
                        if (var9.childNPCs != null) {
                            var9 = var9.method1471((byte) 102);
                        }

                        if (Objects.requireNonNull(var9).anInt1269 >= 0 && NPC.aAbstractSpriteArray3977.length > var9.anInt1269) {
                            if (var9.anInt1265 == -1) {
                                var22 = 15 + var8.method1975(27855);
                            } else {
                                var22 = 15 + var9.anInt1265;
                            }

                            Class107.method1647(var4 >> 1, var3, var8, var5, var22, var1 >> 1);
                            if (Class32.anInt590 > -1) {
                                NPC.aAbstractSpriteArray3977[var9.anInt1269].drawAt(var2 - -Class32.anInt590 - 12, -30 + var0 - -anInt2208);
                            }
                        }

                        Class96[] var20 = ClientErrorException.aClass96Array2114;

                        for (var21 = 0; var20.length > var21; ++var21) {
                            Class96 var24 = var20[var21];
                            if (null != var24 && var24.anInt1360 == 1 && AudioThread.localNPCIndexes[-Class159.localPlayerCount + var7] == var24.anInt1359 && Class44.anInt719 % 20 < 10) {
                                if (-1 == var9.anInt1265) {
                                    var29 = 15 + var8.method1975(var6 + '\u89b4');
                                } else {
                                    var29 = 15 + var9.anInt1265;
                                }

                                Class107.method1647(var4 >> 1, var3, var8, var5, var29, var1 >> 1);
                                if (Class32.anInt590 > -1) {
                                    Class166.aAbstractSpriteArray2072[var24.anInt1351].drawAt(-12 + var2 + Class32.anInt590, -28 + anInt2208 + var0);
                                }
                            }
                        }
                    }

                    if (var8.textSpoken != null && (var7 >= Class159.localPlayerCount || CS2Script.anInt3101 == 0 || 3 == CS2Script.anInt3101 || 1 == CS2Script.anInt3101 && ItemDefinition.method1176(((Player) var8).displayName))) {
                        Class107.method1647(var4 >> 1, var3, var8, var5, var8.method1975(27855), var1 >> 1);
                        if (-1 < Class32.anInt590 && TextureOperation8.anInt3464 < TextureOperation35.anInt3332) {
                            anIntArray3329[TextureOperation8.anInt3464] = FontType.bold.method682(var8.textSpoken) / 2;
                            anIntArray3327[TextureOperation8.anInt3464] = FontType.bold.anInt3727;
                            anIntArray3319[TextureOperation8.anInt3464] = Class32.anInt590;
                            anIntArray3337[TextureOperation8.anInt3464] = anInt2208;
                            anIntArray3331[TextureOperation8.anInt3464] = var8.textColor;
                            anIntArray3336[TextureOperation8.anInt3464] = var8.textEffect;
                            anIntArray3318[TextureOperation8.anInt3464] = var8.textCycle;
                            aClass94Array3317[TextureOperation8.anInt3464] = var8.textSpoken;
                            ++TextureOperation8.anInt3464;
                        }
                    }

                    if (Class44.anInt719 < var8.anInt2781) {
                        AbstractSprite var23 = Unsorted.aAbstractSpriteArray996[0];
                        AbstractSprite var25 = Unsorted.aAbstractSpriteArray996[1];
                        if (var8 instanceof NPC) {
                            NPC var28 = (NPC) var8;
                            AbstractSprite[] var31 = (AbstractSprite[]) TextureOperation1.aReferenceCache_3130.get(var28.definition.anInt1279);
                            if (var31 == null) {
                                var31 = Class140_Sub6.getSprites(var28.definition.anInt1279, CacheIndex.spritesIndex);
                                if (null != var31) {
                                    TextureOperation1.aReferenceCache_3130.put(var31, var28.definition.anInt1279);
                                }
                            }

                            if (null != var31 && var31.length == 2) {
                                var25 = var31[1];
                                var23 = var31[0];
                            }

                            NPCDefinition var14 = var28.definition;
                            if (-1 == var14.anInt1265) {
                                var21 = var8.method1975(27855);
                            } else {
                                var21 = var14.anInt1265;
                            }
                        } else {
                            var21 = var8.method1975(27855);
                        }

                        Class107.method1647(var4 >> 1, var3, var8, var5, var23.height + 10 + var21, var1 >> 1);
                        if (-1 < Class32.anInt590) {
                            var12 = -(var23.width >> 1) + Class32.anInt590 + var2;
                            var29 = anInt2208 + var0 + -3;
                            var23.drawAt(var12, var29);
                            var32 = var23.width * var8.anInt2775 / 255;
                            var15 = var23.height;
                            if (HDToolKit.highDetail) {
                                Class22.method931(var12, var29, var12 + var32, var29 + var15);
                            } else {
                                Class74.method1326(var12, var29, var12 + var32, var15 + var29);
                            }

                            var25.drawAt(var12, var29);
                            if (HDToolKit.highDetail) {
                                Class22.setClipping(var2, var0, var1 + var2, var0 - -var4);
                            } else {
                                Class74.setClipping(var2, var0, var1 + var2, var4 + var0);
                            }
                        }
                    }

                    for (var19 = 0; var19 < 4; ++var19) {
                        if (Class44.anInt719 < var8.anIntArray2768[var19]) {
                            if (var8 instanceof NPC) {
                                NPC var30 = (NPC) var8;
                                NPCDefinition var26 = var30.definition;
                                if (var26.anInt1265 == -1) {
                                    var22 = var8.method1975(27855) / 2;
                                } else {
                                    var22 = var26.anInt1265 / 2;
                                }
                            } else {
                                var22 = var8.method1975(var6 ^ -28716) / 2;
                            }

                            Class107.method1647(var4 >> 1, var3, var8, var5, var22, var1 >> 1);
                            if (-1 < Class32.anInt590) {
                                if (var19 == 1) {
                                    anInt2208 -= 20;
                                }

                                if (var19 == 2) {
                                    anInt2208 -= 10;
                                    Class32.anInt590 -= 15;
                                }

                                if (3 == var19) {
                                    anInt2208 -= 10;
                                    Class32.anInt590 += 15;
                                }

                                Class75_Sub3.aAbstractSpriteArray2656[var8.anIntArray2815[var19]].drawAt(-12 + var2 + Class32.anInt590, var0 + anInt2208 - 12);
                                FontType.smallFont.method699(RSString.stringAnimator(var8.anIntArray2836[var19]), -1 + Class32.anInt590 + var2, 3 + anInt2208 + var0, 16777215, 0);
                            }
                        }
                    }
                }
            }

            var7 = 0;
            if (var6 != -7397) {
                method1409(true);
            }

            for (; TextureOperation8.anInt3464 > var7; ++var7) {
                var19 = anIntArray3337[var7];
                int var18 = anIntArray3319[var7];
                var21 = anIntArray3327[var7];
                var22 = anIntArray3329[var7];
                boolean var27 = true;

                while (var27) {
                    var27 = false;

                    for (var29 = 0; var7 > var29; ++var29) {
                        if (anIntArray3337[var29] - anIntArray3327[var29] < 2 + var19 && -var21 + var19 < anIntArray3337[var29] - -2 && -var22 + var18 < anIntArray3319[var29] + anIntArray3329[var29] && anIntArray3319[var29] - anIntArray3329[var29] < var22 + var18 && -anIntArray3327[var29] + anIntArray3337[var29] < var19) {
                            var19 = anIntArray3337[var29] - anIntArray3327[var29];
                            var27 = true;
                        }
                    }
                }

                Class32.anInt590 = anIntArray3319[var7];
                anInt2208 = anIntArray3337[var7] = var19;
                RSString var33 = aClass94Array3317[var7];
                if (Unsorted.anInt688 == 0) {
                    var32 = 16776960;
                    if (anIntArray3331[var7] < 6) {
                        var32 = anIntArray2213[anIntArray3331[var7]];
                    }

                    if (6 == anIntArray3331[var7]) {
                        var32 = 10 <= Class79.anInt1127 % 20 ? 16776960 : 16711680;
                    }

                    if (anIntArray3331[var7] == 7) {
                        var32 = Class79.anInt1127 % 20 < 10 ? 255 : 65535;
                    }

                    if (8 == anIntArray3331[var7]) {
                        var32 = Class79.anInt1127 % 20 >= 10 ? 8454016 : '\ub000';
                    }

                    if (9 == anIntArray3331[var7]) {
                        var15 = -anIntArray3318[var7] + 150;
                        if (var15 >= 50) {
                            if (var15 >= 100) {
                                if (150 > var15) {
                                    var32 = -500 - (-(5 * var15) - 65280);
                                }
                            } else {
                                var32 = 16776960 + 16384000 + -(327680 * var15);
                            }
                        } else {
                            var32 = var15 * 1280 + 16711680;
                        }
                    }

                    if (10 == anIntArray3331[var7]) {
                        var15 = -anIntArray3318[var7] + 150;
                        if (50 <= var15) {
                            if (var15 < 100) {
                                var32 = -(327680 * (-50 + var15)) + 16711935;
                            } else if (150 > var15) {
                                var32 = 327680 * var15 - (32768000 - (255 + -(5 * var15) + 500));
                            }
                        } else {
                            var32 = 16711680 + var15 * 5;
                        }
                    }

                    if (anIntArray3331[var7] == 11) {
                        var15 = 150 + -anIntArray3318[var7];
                        if (var15 >= 50) {
                            if (var15 >= 100) {
                                if (var15 < 150) {
                                    var32 = 16777215 - var15 * 327680 + 32768000;
                                }
                            } else {
                                var32 = 65280 - (-(327685 * var15) - -16384250);
                            }
                        } else {
                            var32 = 16777215 - 327685 * var15;
                        }
                    }

                    if (0 == anIntArray3336[var7]) {
                        FontType.bold.method699(var33, Class32.anInt590 + var2, var0 + anInt2208, var32, 0);
                    }

                    if (1 == anIntArray3336[var7]) {
                        FontType.bold.method696(var33, var2 - -Class32.anInt590, anInt2208 + var0, var32, Class79.anInt1127);
                    }

                    if (anIntArray3336[var7] == 2) {
                        FontType.bold.method695(var33, var2 - -Class32.anInt590, var0 - -anInt2208, var32, Class79.anInt1127);
                    }

                    if (anIntArray3336[var7] == 3) {
                        FontType.bold.method692(var33, var2 + Class32.anInt590, anInt2208 + var0, var32, Class79.anInt1127, 150 - anIntArray3318[var7]);
                    }

                    if (4 == anIntArray3336[var7]) {
                        var15 = (-anIntArray3318[var7] + 150) * (FontType.bold.method682(var33) - -100) / 150;
                        if (HDToolKit.highDetail) {
                            Class22.method931(Class32.anInt590 + var2 + -50, var0, Class32.anInt590 + var2 - -50, var4 + var0);
                        } else {
                            Class74.method1326(-50 + (var2 - -Class32.anInt590), var0, 50 + Class32.anInt590 + var2, var4 + var0);
                        }

                        FontType.bold.method681(var33, var2 - (-Class32.anInt590 + -50) + -var15, var0 + anInt2208, var32, 0);
                        if (HDToolKit.highDetail) {
                            Class22.setClipping(var2, var0, var1 + var2, var4 + var0);
                        } else {
                            Class74.setClipping(var2, var0, var2 - -var1, var0 + var4);
                        }
                    }

                    if (anIntArray3336[var7] == 5) {
                        int var16 = 0;
                        var15 = -anIntArray3318[var7] + 150;
                        if (HDToolKit.highDetail) {
                            Class22.method931(var2, -1 + -FontType.bold.anInt3727 + anInt2208 + var0, var1 + var2, 5 + var0 - -anInt2208);
                        } else {
                            Class74.method1326(var2, -1 + -FontType.bold.anInt3727 + anInt2208 + var0, var2 + var1, 5 + anInt2208 + var0);
                        }

                        if (25 > var15) {
                            var16 = var15 + -25;
                        } else if (var15 > 125) {
                            var16 = var15 - 125;
                        }

                        FontType.bold.method699(var33, Class32.anInt590 + var2, var16 + var0 + anInt2208, var32, 0);
                        if (HDToolKit.highDetail) {
                            Class22.setClipping(var2, var0, var2 - -var1, var0 + var4);
                        } else {
                            Class74.setClipping(var2, var0, var2 + var1, var0 + var4);
                        }
                    }
                } else {
                    FontType.bold.method699(var33, var2 - -Class32.anInt590, var0 + anInt2208, 16776960, 0);
                }
            }

        } catch (RuntimeException var17) {
            throw ClientErrorException.clientError(var17, "lc.D(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    static int method1406() {
        try {
            return Unsorted.anInt4045;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "lc.E(" + (byte) -43 + ')');
        }
    }

    public static void method1409(boolean var0) {
        try {
            aReferenceCache_1146 = null;
            if (var0) {
                TextCore.aClass94_1151 = null;
            }

            TextCore.aClass94_1151 = null;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "lc.A(" + var0 + ')');
        }
    }

    public static TextureOperation decodeTexture(DataBuffer var1) {
        try {
            var1.readUnsignedByte();
            int type = var1.readUnsignedByte();
            TextureOperation var3 = create(type);
            Objects.requireNonNull(var3).imageCacheCapacity = var1.readUnsignedByte();
            int codes = var1.readUnsignedByte();
            for (int var5 = 0; var5 < codes; ++var5) {
                int opcode = var1.readUnsignedByte();
                var3.decode(opcode, var1);
            }

            var3.postDecode();
            return var3;
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "qk.B(" + (byte) -67 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    public static TextureOperation create(int type) {
        if (type == 0) {
            return new TextureOperation0();
        } else if (type == 1) {
            return new TextureOperation1();
        } else if (2 == type) {
            return new TextureOperation2();
        } else if (type == 3) {
            return new TextureOperation3();
        } else if (type == 4) {
            return new TextureOperation4();
        } else if (type == 5) {
            return new TextureOperation5();
        } else if (type == 6) {
            return new TextureOperation6();
        } else if (type == 7) {
            return new TextureOperation7();
        } else if (type == 8) {
            return new TextureOperation8();
        } else if (9 == type) {
            return new TextureOperation9();
        } else if (10 == type) {
            return new TextureOperation10();
        } else if (type == 11) {
            return new TextureOperation11();
        } else if (type == 12) {
            return new TextureOperation12();
        } else if (type == 13) {
            return new TextureOperation13();
        } else if (14 == type) {
            return new TextureOperation14();
        } else if (type == 15) {
            return new TextureOperation15();
        } else if (type == 16) {
            return new TextureOperation16();
        } else if (17 == type) {
            return new TextureOperation17();
        } else if (type == 18) {
            return new TextureOperation18();
        } else if (type == 19) {
            return new TextureOperation19();
        } else if (type == 20) {
            return new TextureOperation20();
        } else if (21 == type) {
            return new TextureOperation21();
        } else if (22 == type) {
            return new TextureOperation22();
        } else if (type == 23) {
            return new TextureOperation23();
        } else if (24 == type) {
            return new TextureOperation24();
        } else if (type == 25) {
            return new TextureOperation25();
        } else if (type == 26) {
            return new TextureOperation26();
        } else if (27 == type) {
            return new TextureOperation27();
        } else if (type == 28) {
            return new TextureOperation28();
        } else if (type == 29) {
            return new TextureOperation29();
        } else if (type == 30) {
            return new TextureOperation30();
        } else if (31 == type) {
            return new TextureOperation31();
        } else if (32 == type) {
            return new TextureOperation32();
        } else if (33 == type) {
            return new TextureOperation33();
        } else if (type == 34) {
            return new TextureOperation34();
        } else if (type == 35) {
            return new TextureOperation35();
        } else if (type == 36) {
            return new TextureOperation36();
        } else if (type == 37) {
            return new TextureOperation37();
        } else if (38 == type) {
            return new TextureOperation38();
        } else if (39 == type) {
            return new TextureOperation39();
        } else {
            return null;
        }
    }

    final int[] method1404(int var1, boolean var2, int var3, double var4, CacheIndex var7, Interface2 var8, boolean var9) {
        try {
            GameObject.method1859(var4);
            Class17.anInterface2_408 = var8;
            WaterfallShader.aClass153_2172 = var7;
            TextureOperation33.method180(-1, var1, var3);

            int var11;
            for (var11 = 0; var11 < this.aClass3_Sub13Array1147.length; ++var11) {
                this.aClass3_Sub13Array1147[var11].method160(var1, var3);
            }

            int[] var10 = new int[var1 * var3];
            int var12;
            byte var13;
            if (var9) {
                var13 = -1;
                var12 = -1;
                var11 = var3 - 1;
            } else {
                var13 = 1;
                var11 = 0;
                var12 = var3;
            }

            int var14 = 0;

            int var15;
            for (var15 = 0; var1 > var15; ++var15) {
                if (var2) {
                    var14 = var15;
                }

                int[] var17;
                int[] var16;
                int[] var18;
                if (this.aClass3_Sub13_1145.aBoolean2375) {
                    int[] var19 = this.aClass3_Sub13_1145.method154(var15, (byte) 109);
                    var16 = var19;
                    var17 = var19;
                    var18 = var19;
                } else {
                    int[][] var24 = this.aClass3_Sub13_1145.method166(var15);
                    var16 = var24[0];
                    var18 = var24[2];
                    var17 = var24[1];
                }

                for (int var25 = var11; var25 != var12; var25 += var13) {
                    int var20 = var16[var25] >> 4;
                    if (var20 > 255) {
                        var20 = 255;
                    }

                    if (var20 < 0) {
                        var20 = 0;
                    }

                    var20 = BufferedDataStream.anIntArray3804[var20];
                    int var22 = var18[var25] >> 4;
                    int var21 = var17[var25] >> 4;
                    if (var21 > 255) {
                        var21 = 255;
                    }

                    if (0 > var21) {
                        var21 = 0;
                    }

                    if (var22 > 255) {
                        var22 = 255;
                    }

                    var21 = BufferedDataStream.anIntArray3804[var21];
                    if (var22 < 0) {
                        var22 = 0;
                    }

                    var22 = BufferedDataStream.anIntArray3804[var22];
                    var10[var14++] = (var20 << 16) - -(var21 << 8) + var22;
                    if (var2) {
                        var14 += var3 + -1;
                    }
                }
            }

            for (var15 = 0; var15 < this.aClass3_Sub13Array1147.length; ++var15) {
                this.aClass3_Sub13Array1147[var15].method161((byte) -45);
            }

            return var10;
        } catch (RuntimeException var23) {
            throw ClientErrorException.clientError(var23, "lc.C(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + 327680 + ',' + (var7 != null ? "{...}" : "null") + ',' + (var8 != null ? "{...}" : "null") + ',' + var9 + ')');
        }
    }

    final byte[] method1407(int var1, int var2, boolean var3, Interface2 var4, CacheIndex var8) {
        try {
            byte[] var9 = new byte[4 * var2 * var1];
            GameObject.method1859(0.7);
            WaterfallShader.aClass153_2172 = var8;
            Class17.anInterface2_408 = var4;
            TextureOperation33.method180(-32, var1, var2);

            int var10;
            for (var10 = 0; this.aClass3_Sub13Array1147.length > var10; ++var10) {
                this.aClass3_Sub13Array1147[var10].method160(var1, var2);
            }

            var10 = 0;

            int var11;
            for (var11 = 0; var11 < var1; ++var11) {
                if (var3) {
                    var10 = var11 << 2;
                }

                int[] var12;
                int[] var13;
                int[] var14;
                int[] var15;
                if (this.aClass3_Sub13_1145.aBoolean2375) {
                    var15 = this.aClass3_Sub13_1145.method154(var11, (byte) -98);
                    var12 = var15;
                    var13 = var15;
                    var14 = var15;
                } else {
                    int[][] var22 = this.aClass3_Sub13_1145.method166(var11);
                    var12 = var22[0];
                    var13 = var22[1];
                    var14 = var22[2];
                }

                if (this.aClass3_Sub13_1148.aBoolean2375) {
                    var15 = this.aClass3_Sub13_1148.method154(var11, (byte) -103);
                } else {
                    var15 = this.aClass3_Sub13_1148.method166(var11)[0];
                }

                for (int var16 = var2 - 1; var16 >= 0; --var16) {
                    int var17 = var12[var16] >> 4;
                    if (var17 > 255) {
                        var17 = 255;
                    }

                    if (var17 < 0) {
                        var17 = 0;
                    }

                    int var18 = var13[var16] >> 4;
                    if (var18 > 255) {
                        var18 = 255;
                    }

                    int var19 = var14[var16] >> 4;
                    if (var19 > 255) {
                        var19 = 255;
                    }

                    var17 = BufferedDataStream.anIntArray3804[var17];
                    if (var19 < 0) {
                        var19 = 0;
                    }

                    if (var18 < 0) {
                        var18 = 0;
                    }

                    var18 = BufferedDataStream.anIntArray3804[var18];
                    var19 = BufferedDataStream.anIntArray3804[var19];
                    int var20;
                    if (var17 == 0 && var18 == 0 && var19 == 0) {
                        var20 = 0;
                    } else {
                        var20 = var15[var16] >> 4;
                        if (255 < var20) {
                            var20 = 255;
                        }

                        if (var20 < 0) {
                            var20 = 0;
                        }
                    }

                    var9[var10++] = (byte) var17;
                    var9[var10++] = (byte) var18;
                    var9[var10++] = (byte) var19;
                    var9[var10++] = (byte) var20;
                    if (var3) {
                        var10 += -4 + (var2 << 2);
                    }
                }
            }

            for (var11 = 0; this.aClass3_Sub13Array1147.length > var11; ++var11) {
                this.aClass3_Sub13Array1147[var11].method161((byte) -45);
            }

            return var9;
        } catch (RuntimeException var21) {
            throw ClientErrorException.clientError(var21, "lc.F(" + var1 + ',' + var2 + ',' + var3 + ',' + (var4 != null ? "{...}" : "null") + ',' + 0.7 + ',' + 8839 + ',' + (var8 != null ? "{...}" : "null") + ')');
        }
    }

    final boolean method1408(Interface2 var2, CacheIndex var3) {
        try {
            int var4;
            if (0 < anInt1668) {
                for (var4 = 0; this.anIntArray1144.length > var4; ++var4) {
                    if (!var3.method2129((byte) -78, this.anIntArray1144[var4], anInt1668)) {
                        return false;
                    }
                }
            } else {
                for (var4 = 0; this.anIntArray1144.length > var4; ++var4) {
                    if (!var3.retrieveSpriteFile(this.anIntArray1144[var4])) {
                        return false;
                    }
                }
            }

            for (var4 = 0; var4 < this.anIntArray1149.length; ++var4) {
                if (!var2.method11(21, this.anIntArray1149[var4])) {
                    return false;
                }
            }

            return true;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "lc.B(" + true + ',' + (var2 != null ? "{...}" : "null") + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

}
