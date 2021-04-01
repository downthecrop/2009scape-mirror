package org.runite.client;

import org.rs09.SystemLogger;

import java.util.Objects;

public final class BufferedDataStream extends DataBuffer {

    public static BufferedDataStream incomingBuffer = new BufferedDataStream();
    static RSString[] aClass94Array3802;
    static int anInt872;
    static int anInt2330 = 0;
    static float aFloat3044;
    static int anInt4037;
    static int anInt1345;
    static int anInt1407;
    static int anInt1473;
    static float aFloat1475;
    static int anInt1736;
    static int[] anIntArray2709 = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, Integer.MAX_VALUE, -1};
    static int anInt1971;
    static int anInt3652;
    private ISAACCipher isaacCipher;
    static int[] anIntArray3804 = new int[256];
    static int[] anIntArray3805;
    private int headiconsPrayerSpriteArchive6;


    static void addChatMessage(RSString var0, int type, RSString message, int var3) {
        try {
            MessageManager.sendGameMessage(var3, type, message, null, var0);
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "i.W(" + (var0 != null ? "{...}" : "null") + ',' + type + ',' + (message != null ? "{...}" : "null") + ',' + var3 + ')');
        }
    }

    static void method806(int var1) {
        try {
            if (var1 >= 0) {
                int var2 = Class117.anIntArray1613[var1];
                int var3 = Class27.anIntArray512[var1];
                int var4 = TextureOperation27.aShortArray3095[var1];
                if (var4 >= 2000) {
                    var4 -= 2000;
                }

                long var6 = Unsorted.aLongArray3271[var1];
                int var5 = (int) Unsorted.aLongArray3271[var1];
                Player var8;
                if (31 == var4) {
                    var8 = Unsorted.players[var5];
                    if (null != var8) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                        Class36.anInt638 = 2;
                        Unsorted.anInt2958 = 0;
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        TextureOperation12.outgoingBuffer.putOpcode(71);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                    }
                }

                if (var4 == 46) {
                    Class163_Sub2_Sub1.method2224(var6, var3, var2);
                    TextureOperation12.outgoingBuffer.putOpcode(247);
                    TextureOperation12.outgoingBuffer.writeShortLE(Texture.anInt1152 + var3);
                    TextureOperation12.outgoingBuffer.writeShort128LE(var2 + Class131.anInt1716);
                    TextureOperation12.outgoingBuffer.writeShort(Integer.MAX_VALUE & (int) (var6 >>> 32));
                }

                if (var4 == 40) {
                    TextureOperation12.outgoingBuffer.putOpcode(27);
                    TextureOperation12.outgoingBuffer.writeShort(anInt1473);
                    TextureOperation12.outgoingBuffer.writeIntLE2(var3);
                    TextureOperation12.outgoingBuffer.writeShortLE(var2);
                    TextureOperation12.outgoingBuffer.writeIntLE2(Class3_Sub28_Sub18.anInt3764);
                    TextureOperation12.outgoingBuffer.writeShort128LE(Class164.anInt2050);
                    TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                    anInt2330 = 0;
                    Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                    KeyboardListener.anInt1918 = var2;
                    System.out.println(anInt1473 + ", " + var3 + ", " + var2 + ", " + Class3_Sub28_Sub18.anInt3764 + ", " + Class164.anInt2050 + ", " + var5);
                }

                NPC var11;
                if (var4 == 19) {
                    var11 = NPC.npcs[var5];
                    if (null != var11) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var11.anIntArray2767[0], 1, 0, 2, var11.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Class36.anInt638 = 2;
                        Unsorted.anInt2958 = 0;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        TextureOperation12.outgoingBuffer.putOpcode(30);
                        TextureOperation12.outgoingBuffer.writeShort(var5);
                    }
                }

                if (17 == var4) {
                    var11 = NPC.npcs[var5];
                    if (var11 != null) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var11.anIntArray2767[0], 1, 0, 2, var11.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Unsorted.anInt2958 = 0;
                        Class36.anInt638 = 2;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        TextureOperation12.outgoingBuffer.putOpcode(78);
                        TextureOperation12.outgoingBuffer.writeShortLE(var5);
                    }
                }

                if (var4 == 44) {
                    var8 = Unsorted.players[var5];
                    if (null != var8) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Class36.anInt638 = 2;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        Unsorted.anInt2958 = 0;
                        TextureOperation12.outgoingBuffer.putOpcode(133);
                        TextureOperation12.outgoingBuffer.writeShortLE(var5);
                    }
                }

                if (var4 == 58) {
                    TextureOperation12.outgoingBuffer.putOpcode(135);
                    TextureOperation12.outgoingBuffer.putShortA(var5);
                    TextureOperation12.outgoingBuffer.putShortA(var2);
                    TextureOperation12.outgoingBuffer.writeIntV2(var3);
                    anInt2330 = 0;
                    Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                    KeyboardListener.anInt1918 = var2;
                }

                if (var4 == 42) {
                    Class163_Sub2_Sub1.method2224(var6, var3, var2);
                    TextureOperation12.outgoingBuffer.putOpcode(254);
                    TextureOperation12.outgoingBuffer.writeShortLE(var2 + Class131.anInt1716);
                    TextureOperation12.outgoingBuffer.putShortA((int) (var6 >>> 32) & Integer.MAX_VALUE);
                    TextureOperation12.outgoingBuffer.writeShort(var3 - -Texture.anInt1152);
                }

                if (28 == var4) {
                    TextureOperation4.method264((byte) 122);
                }

                if (var4 == 45) {
                    var11 = NPC.npcs[var5];
                    if (var11 != null) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var11.anIntArray2767[0], 1, 0, 2, var11.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Class36.anInt638 = 2;
                        Unsorted.anInt2958 = 0;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        TextureOperation12.outgoingBuffer.putOpcode(239);
                        TextureOperation12.outgoingBuffer.writeIntLE2(anInt872);
                        TextureOperation12.outgoingBuffer.putShortA(RSInterface.anInt278);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                    }
                }

                boolean var14;
                if (18 == var4) {
                    if (Class158.paramGameTypeID == 1) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                    } else {
                        var14 = Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 0, false, 0, 2597 ^ 2599, var2, 0, 0, 2, var3, Class102.player.anIntArray2767[0]);
                        if (!var14) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                        }
                    }

                    Class70.anInt1053 = Class163_Sub1.anInt2993;
                    Unsorted.anInt2958 = 0;
                    Class36.anInt638 = 2;
                    Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                    TextureOperation12.outgoingBuffer.putOpcode(66);
                    TextureOperation12.outgoingBuffer.writeShortLE(Class131.anInt1716 + var2);
                    TextureOperation12.outgoingBuffer.writeShort(var5);
                    TextureOperation12.outgoingBuffer.writeShort128LE(var3 - -Texture.anInt1152);
                }

                if (var4 == 1001) {
                    Class163_Sub2_Sub1.method2224(var6, var3, var2);
                    TextureOperation12.outgoingBuffer.putOpcode(170);
                    TextureOperation12.outgoingBuffer.writeShort128LE(Integer.MAX_VALUE & (int) (var6 >>> 32));
                    TextureOperation12.outgoingBuffer.writeShort128LE(var2 - -Class131.anInt1716);
                    TextureOperation12.outgoingBuffer.writeShort128LE(var3 - -Texture.anInt1152);
                }

                if (var4 == 1002) {
                    Class36.anInt638 = 2;
                    Class70.anInt1053 = Class163_Sub1.anInt2993;
                    Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                    Unsorted.anInt2958 = 0;
                    TextureOperation12.outgoingBuffer.putOpcode(92);
                    TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                }

                RSInterface var13;
                if (var4 == 1006) {
                    var13 = Unsorted.getRSInterface(var3);
                    if (null != var13 && var13.itemIds[var2] >= 100000) {
                        addChatMessage(RSString.parse(""), 0, RSString.stringCombiner(new RSString[]{RSString.stringAnimator(var13.itemIds[var2]), TextCore.aClass94_3777, ItemDefinition.getItemDefinition(var5).name}), -1);
                    } else {
                        TextureOperation12.outgoingBuffer.putOpcode(92);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                    }

                    anInt2330 = 0;
                    Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                    KeyboardListener.anInt1918 = var2;
                }

                if (var4 == 60) {
                    if (var5 == 0) {
                        Unsorted.method589(WorldListCountry.localPlane, var2, var3);
                    } else if (var5 == 1) {
                        if (0 < Player.rights && ObjectDefinition.aBooleanArray1490[82] && ObjectDefinition.aBooleanArray1490[81]) {
                            Class30.method979(Class131.anInt1716 + var2, Texture.anInt1152 + var3, WorldListCountry.localPlane);
                        } else if (Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 0, true, 0, 2, var2, 0, 0, 1, var3, Class102.player.anIntArray2767[0])) {
                            TextureOperation12.outgoingBuffer.writeByte(Class1.anInt56);
                            TextureOperation12.outgoingBuffer.writeByte(Class58.anInt916);
                            TextureOperation12.outgoingBuffer.writeShort(GraphicDefinition.CAMERA_DIRECTION);
                            TextureOperation12.outgoingBuffer.writeByte(57);
                            TextureOperation12.outgoingBuffer.writeByte(TextureOperation9.anInt3102);
                            TextureOperation12.outgoingBuffer.writeByte(Class164_Sub2.anInt3020);
                            TextureOperation12.outgoingBuffer.writeByte(89);
                            TextureOperation12.outgoingBuffer.writeShort(Class102.player.xAxis);
                            TextureOperation12.outgoingBuffer.writeShort(Class102.player.zAxis);
                            TextureOperation12.outgoingBuffer.writeByte(Class129.anInt1692);
                            TextureOperation12.outgoingBuffer.writeByte(63);
                        }
                    }
                }

                if (1007 == var4) {
                    Unsorted.anInt2958 = 0;
                    Class36.anInt638 = 2;
                    Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                    Class70.anInt1053 = Class163_Sub1.anInt2993;
                    var11 = NPC.npcs[var5];
                    if (var11 != null) {
                        NPCDefinition var9 = var11.definition;
                        if (var9.childNPCs != null) {
                            var9 = var9.method1471((byte) 80);
                        }

                        if (null != var9) {
                            TextureOperation12.outgoingBuffer.putOpcode(72);
                            TextureOperation12.outgoingBuffer.writeShort(var9.npcId);
                        }
                    }
                }

                if (var4 == 47) {
                    if (MouseWheel.shiftDown) {

                        TextureOperation12.outgoingBuffer.putOpcode(135);
                        TextureOperation12.outgoingBuffer.putShortA(var5);
                        TextureOperation12.outgoingBuffer.putShortA(var2);
                        TextureOperation12.outgoingBuffer.writeIntV2(var3);
                        anInt2330 = 0;
                        Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);


                    } else {
                        TextureOperation12.outgoingBuffer.putOpcode(156);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var2);
                        TextureOperation12.outgoingBuffer.putShortA(var5);
                        TextureOperation12.outgoingBuffer.writeIntLE2(var3);
                        anInt2330 = 0;
                        Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                    }
                    KeyboardListener.anInt1918 = var2;
                }

                if (var4 == 3) {
                    TextureOperation12.outgoingBuffer.putOpcode(253);
                    TextureOperation12.outgoingBuffer.writeIntLE2(anInt872);
                    TextureOperation12.outgoingBuffer.writeShort128LE(var2);
                    TextureOperation12.outgoingBuffer.writeIntLE2(var3);
                    TextureOperation12.outgoingBuffer.putShortA(var5);
                    TextureOperation12.outgoingBuffer.writeShortLE(RSInterface.anInt278);
                    anInt2330 = 0;
                    Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                    KeyboardListener.anInt1918 = var2;
                }

                if (var4 == 10) {
                    var8 = Unsorted.players[var5];
                    if (var8 != null) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                        Class36.anInt638 = 2;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Unsorted.anInt2958 = 0;
                        TextureOperation12.outgoingBuffer.putOpcode(4);
                        TextureOperation12.outgoingBuffer.writeShortLE(var5);
                    }
                }

                if (41 == var4 && TextureOperation27.aClass11_3087 == null) {
                    Unsorted.method78(var2, var3);
                    TextureOperation27.aClass11_3087 = AbstractSprite.method638(var3, var2);
                    Class20.method909(TextureOperation27.aClass11_3087);
                }

                if (49 == var4) {
                    Class163_Sub2_Sub1.method2224(var6, var3, var2);
                    TextureOperation12.outgoingBuffer.putOpcode(84);
                    TextureOperation12.outgoingBuffer.writeShort128LE(Integer.MAX_VALUE & (int) (var6 >>> 32));
                    TextureOperation12.outgoingBuffer.writeShort128LE(Texture.anInt1152 + var3);
                    TextureOperation12.outgoingBuffer.writeShortLE(var2 - -Class131.anInt1716);
                }

                if (var4 == 23) {
                    TextureOperation12.outgoingBuffer.putOpcode(206);
                    TextureOperation12.outgoingBuffer.putShortA(var5);//itemId
                    TextureOperation12.outgoingBuffer.writeShortLE(var2);//data
                    TextureOperation12.outgoingBuffer.writeIntLE2(var3);//slot
                    anInt2330 = 0;
                    Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                    KeyboardListener.anInt1918 = var2;
                }

                if (var4 == 14) {
                    Class163_Sub2_Sub1.method2224(var6, var3, var2);
                    TextureOperation12.outgoingBuffer.putOpcode(134);
                    TextureOperation12.outgoingBuffer.putShortA(Class131.anInt1716 + var2);
                    TextureOperation12.outgoingBuffer.writeShort(Class164.anInt2050);
                    TextureOperation12.outgoingBuffer.writeShortLE(var3 - -Texture.anInt1152);
                    TextureOperation12.outgoingBuffer.writeShort(anInt1473);
                    TextureOperation12.outgoingBuffer.writeIntV2(Class3_Sub28_Sub18.anInt3764);
                    TextureOperation12.outgoingBuffer.putShortA((int) (var6 >>> 32) & Integer.MAX_VALUE);
                }

                if (var4 == 37) {
                    var8 = Unsorted.players[var5];
                    if (var8 != null) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                        Unsorted.anInt2958 = 0;
                        Class36.anInt638 = 2;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        TextureOperation12.outgoingBuffer.putOpcode(114);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                    }
                }

                if (var4 == 9 || 1003 == var4) {
                    Unsorted.method66(Class163_Sub2_Sub1.aClass94Array4016[var1], var2, var5, (byte) -19, var3);
                }

                if (var4 == 5) {
                    if (MouseWheel.shiftDown) {
                        TextureOperation12.outgoingBuffer.putOpcode(135);
                        TextureOperation12.outgoingBuffer.putShortA(var5);
                        TextureOperation12.outgoingBuffer.putShortA(var2);
                        TextureOperation12.outgoingBuffer.writeIntV2(var3);
                        anInt2330 = 0;
                        Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);

                    } else {
                        TextureOperation12.outgoingBuffer.putOpcode(55);
                        TextureOperation12.outgoingBuffer.writeShortLE(var5);
                        TextureOperation12.outgoingBuffer.putShortA(var2);
                        TextureOperation12.outgoingBuffer.writeIntV1(var3);
                        anInt2330 = 0;
                        Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);

                    }
                    KeyboardListener.anInt1918 = var2;
                }

                if (var4 == 21) {
                    if (Class158.paramGameTypeID == 1) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2597 + -2595, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                    } else {
                        var14 = Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 0, false, 0, 2597 + -2595, var2, 0, 0, 2, var3, Class102.player.anIntArray2767[0]);
                        if (!var14) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2597 + -2595, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                        }
                    }

                    Class36.anInt638 = 2;
                    Class70.anInt1053 = Class163_Sub1.anInt2993;
                    Unsorted.anInt2958 = 0;
                    Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                    TextureOperation12.outgoingBuffer.putOpcode(228);
                    TextureOperation12.outgoingBuffer.writeShort(var5);
                    TextureOperation12.outgoingBuffer.writeShortLE(Class131.anInt1716 + var2);
                    TextureOperation12.outgoingBuffer.writeShort128LE(Texture.anInt1152 + var3);
                }

                if (var4 == 4) {
                    var11 = NPC.npcs[var5];
                    if (var11 != null) {
                        Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var11.anIntArray2767[0], 1, 0, 2, var11.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                        Class36.anInt638 = 2;
                        Unsorted.anInt2958 = 0;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        TextureOperation12.outgoingBuffer.putOpcode(148);
                        TextureOperation12.outgoingBuffer.putShortA(var5);
                    }
                }

                if (32 == var4) {
                    var13 = AbstractSprite.method638(var3, var2);
                    if (null != var13) {
                        Class25.method958((byte) -126);
                        Class3_Sub1 var16 = Client.method44(var13);
                        Class145.method2074(var3, var2, var16.method101(), var16.anInt2202, var13.anInt266, var13.anInt238);
                        Class164_Sub1.anInt3012 = 0;
                        Class3_Sub28_Sub9.aClass94_3621 = Class53.method1174(var13, (byte) -94);
                        if (Class3_Sub28_Sub9.aClass94_3621 == null) {
                            Class3_Sub28_Sub9.aClass94_3621 = TextCore.aClass94_1915;
                        }

                        if (var13.usingScripts) {
                            TextCore.aClass94_676 = RSString.stringCombiner(new RSString[]{var13.aClass94_277, ColorCore.ContextColor});
                        } else {
                            TextCore.aClass94_676 = RSString.stringCombiner(new RSString[]{RSString.parse("<col=00ff00>"), var13.aClass94_243, ColorCore.ContextColor});
                        }
                    }

                } else {
                    if (29 == var4) {
                        var8 = Unsorted.players[var5];
                        if (null != var8) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                            Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                            Unsorted.anInt2958 = 0;
                            Class36.anInt638 = 2;
                            Class70.anInt1053 = Class163_Sub1.anInt2993;
                            TextureOperation12.outgoingBuffer.putOpcode(180);
                            TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                        }
                    }

                    if (var4 == 35) {
                        TextureOperation12.outgoingBuffer.putOpcode(161);
                        TextureOperation12.outgoingBuffer.writeIntLE2(var3);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var2);
                        anInt2330 = 0;
                        Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                        KeyboardListener.anInt1918 = var2;
                    }

                    if (15 == var4) {
                        var8 = Unsorted.players[var5];
                        if (var8 != null) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                            Unsorted.anInt2958 = 0;
                            Class36.anInt638 = 2;
                            Class70.anInt1053 = Class163_Sub1.anInt2993;
                            Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                            TextureOperation12.outgoingBuffer.putOpcode(195);
                            TextureOperation12.outgoingBuffer.putShortA(RSInterface.anInt278);
                            TextureOperation12.outgoingBuffer.writeIntLE2(anInt872);
                            TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                        }
                    }

                    if (34 == var4) {
                        if (Class158.paramGameTypeID == 1) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                        } else {
                            var14 = Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 0, false, 0, 2597 ^ 2599, var2, 0, 0, 2, var3, Class102.player.anIntArray2767[0]);
                            if (!var14) {
                                Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                            }
                        }

                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Class36.anInt638 = 2;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        Unsorted.anInt2958 = 0;
                        TextureOperation12.outgoingBuffer.putOpcode(109);
                        TextureOperation12.outgoingBuffer.writeShortLE(var3 - -Texture.anInt1152);
                        TextureOperation12.outgoingBuffer.writeShort(var2 + Class131.anInt1716);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                    }

                    if (var4 == 25) {
                        TextureOperation12.outgoingBuffer.putOpcode(81);
                        TextureOperation12.outgoingBuffer.putShortA(var2);
                        TextureOperation12.outgoingBuffer.writeShort(var5);
                        TextureOperation12.outgoingBuffer.writeIntV1(var3);
                        anInt2330 = 0;
                        Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                        KeyboardListener.anInt1918 = var2;
                    }

                    if (var4 == 2) {
                        var11 = NPC.npcs[var5];
                        if (var11 != null) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2597 + -2595, var11.anIntArray2767[0], 1, 0, 2, var11.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                            Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                            Class36.anInt638 = 2;
                            Class70.anInt1053 = Class163_Sub1.anInt2993;
                            Unsorted.anInt2958 = 0;
                            TextureOperation12.outgoingBuffer.putOpcode(218);
                            TextureOperation12.outgoingBuffer.writeShortLE(var5);
                        }
                    }

                    int var12;
                    if (var4 == 51) {
                        TextureOperation12.outgoingBuffer.putOpcode(10);
                        TextureOperation12.outgoingBuffer.writeInt(var3);
                        var13 = Unsorted.getRSInterface(var3);
                        if (Objects.requireNonNull(var13).childDataBuffers != null && var13.childDataBuffers[0][0] == 5) {
                            var12 = var13.childDataBuffers[0][1];
                            if (ItemDefinition.ram[var12] != var13.anIntArray307[0]) {
                                ItemDefinition.ram[var12] = var13.anIntArray307[0];
                                Class46.method1087(98, var12);
                            }
                        }
                    }

                    if (var4 == 26) {
                        var11 = NPC.npcs[var5];
                        if (var11 != null) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var11.anIntArray2767[0], 1, 0, 2, var11.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                            Class36.anInt638 = 2;
                            Unsorted.anInt2958 = 0;
                            Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                            Class70.anInt1053 = Class163_Sub1.anInt2993;
                            TextureOperation12.outgoingBuffer.putOpcode(115);//Item on NPC
                            TextureOperation12.outgoingBuffer.writeIntV2(Class3_Sub28_Sub18.anInt3764);
                            TextureOperation12.outgoingBuffer.writeShortLE(anInt1473);
                            TextureOperation12.outgoingBuffer.writeShortLE(var5);
                            TextureOperation12.outgoingBuffer.writeShort128LE(Class164.anInt2050);
//							System.out.println(Class3_Sub28_Sub18.anInt3764 + ", " + Class110.anInt1473 + ", " + var5 + ", " + Class164.anInt2050);
                        }
                    }

                    if (59 == var4) {
                        TextureOperation12.outgoingBuffer.putOpcode(10);
                        TextureOperation12.outgoingBuffer.writeInt(var3);
                        var13 = Unsorted.getRSInterface(var3);
                        if (Objects.requireNonNull(var13).childDataBuffers != null && var13.childDataBuffers[0][0] == 5) {
                            var12 = var13.childDataBuffers[0][1];
                            ItemDefinition.ram[var12] = -ItemDefinition.ram[var12] + 1;
                            Class46.method1087(68, var12);
                        }
                    }

                    if (var4 == 33) {
                        var14 = Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 0, false, 0, 2, var2, 0, 0, 2, var3, Class102.player.anIntArray2767[0]);
                        if (!var14) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2597 + -2595, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                        }

                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Unsorted.anInt2958 = 0;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        Class36.anInt638 = 2;
                        TextureOperation12.outgoingBuffer.putOpcode(101);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var2 - -Class131.anInt1716);
                        TextureOperation12.outgoingBuffer.writeShortLE(anInt1473);
                        TextureOperation12.outgoingBuffer.writeShortLE(Class164.anInt2050);
                        TextureOperation12.outgoingBuffer.writeShortLE(var5);
                        TextureOperation12.outgoingBuffer.writeShort128LE(Texture.anInt1152 + var3);
                        TextureOperation12.outgoingBuffer.writeIntV2(Class3_Sub28_Sub18.anInt3764);
                    }

                    if (var4 == 1004) {
                        Unsorted.anInt2958 = 0;
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Class36.anInt638 = 2;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        TextureOperation12.outgoingBuffer.putOpcode(94);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                    }

                    if (11 == var4) {
                        if (var5 == 0) {
                            CS2Script.anInt2440 = 1;
                            Unsorted.method589(WorldListCountry.localPlane, var2, var3);
                        } else if (1 == var5) {
                            TextureOperation12.outgoingBuffer.putOpcode(131);
                            TextureOperation12.outgoingBuffer.writeIntV2(anInt872);
                            TextureOperation12.outgoingBuffer.putShortA(Class131.anInt1716 + var2);
                            TextureOperation12.outgoingBuffer.writeShort128LE(RSInterface.anInt278);
                            TextureOperation12.outgoingBuffer.putShortA(var3 + Texture.anInt1152);
                        }
                    }

                    if (8 == var4) {
                        var13 = Unsorted.getRSInterface(var3);
                        boolean var15 = true;
                        if (0 < Objects.requireNonNull(var13).anInt189) {
                            var15 = method715(var13);
                        }

                        if (var15) {
                            TextureOperation12.outgoingBuffer.putOpcode(10);
                            TextureOperation12.outgoingBuffer.writeInt(var3);
                        }
                    }

                    if (var4 == 1) {
                        var8 = Unsorted.players[var5];
                        if (var8 != null) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                            Unsorted.anInt2958 = 0;
                            Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                            Class36.anInt638 = 2;
                            Class70.anInt1053 = Class163_Sub1.anInt2993;
                            TextureOperation12.outgoingBuffer.putOpcode(248);
                            TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                            TextureOperation12.outgoingBuffer.writeShort(Class164.anInt2050);
                            TextureOperation12.outgoingBuffer.writeShort(anInt1473);
                            TextureOperation12.outgoingBuffer.writeIntV2(Class3_Sub28_Sub18.anInt3764);
                        }
                    }

                    if (var4 == 7) {
                        TextureOperation12.outgoingBuffer.putOpcode(85);
                        TextureOperation12.outgoingBuffer.writeIntV1(var3);
                        TextureOperation12.outgoingBuffer.writeShort(var2);
                        TextureOperation12.outgoingBuffer.putShortA(var5);
                        anInt2330 = 0;
                        Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                        KeyboardListener.anInt1918 = var2;
                    }

                    if (var4 == 24) {
                        if (Class158.paramGameTypeID == 1) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2597 + -2595, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                        } else {
                            var14 = Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 0, false, 0, 2, var2, 0, 0, 2, var3, Class102.player.anIntArray2767[0]);
                            if (!var14) {
                                Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                            }
                        }

                        Class36.anInt638 = 2;
                        Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                        Class70.anInt1053 = Class163_Sub1.anInt2993;
                        Unsorted.anInt2958 = 0;
                        TextureOperation12.outgoingBuffer.putOpcode(48);
                        TextureOperation12.outgoingBuffer.putShortA(var2 - -Class131.anInt1716);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                        TextureOperation12.outgoingBuffer.writeShortLE(Texture.anInt1152 + var3);
                    }

                    if (var4 == 38) {
                        Class163_Sub2_Sub1.method2224(var6, var3, var2);
                        TextureOperation12.outgoingBuffer.putOpcode(233);
                        TextureOperation12.outgoingBuffer.writeShort128LE(var3 + Texture.anInt1152);
                        TextureOperation12.outgoingBuffer.putShortA(Class131.anInt1716 + var2);
                        TextureOperation12.outgoingBuffer.writeShort128LE(RSInterface.anInt278);
                        TextureOperation12.outgoingBuffer.writeIntV1(anInt872);
                        TextureOperation12.outgoingBuffer.putShortA((int) (var6 >>> 32) & Integer.MAX_VALUE);
                    }

                    if (var4 == 13) {
                        TextureOperation12.outgoingBuffer.putOpcode(6);
                        TextureOperation12.outgoingBuffer.writeInt(var3);
                        TextureOperation12.outgoingBuffer.putShortA(var2);
                        TextureOperation12.outgoingBuffer.writeShortLE(var5);
                        anInt2330 = 0;
                        Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                        KeyboardListener.anInt1918 = var2;
                    }

                    if (57 == var4) {
                        var8 = Unsorted.players[var5];
                        if (null != var8) {
                            Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                            Class36.anInt638 = 2;
                            Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                            Class70.anInt1053 = Class163_Sub1.anInt2993;
                            Unsorted.anInt2958 = 0;
                            TextureOperation12.outgoingBuffer.putOpcode(175);
                            TextureOperation12.outgoingBuffer.putShortA(var5);
                        }
                    }

                    if (var4 == 22) {

                        if (MouseWheel.shiftDown) {
                            TextureOperation12.outgoingBuffer.putOpcode(135);
                            TextureOperation12.outgoingBuffer.putShortA(var5);
                            TextureOperation12.outgoingBuffer.putShortA(var2);
                            TextureOperation12.outgoingBuffer.writeIntV2(var3);
                            anInt2330 = 0;
                            Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                            KeyboardListener.anInt1918 = var2;

                        } else {

                            Class25.method958((byte) -86);
                            var13 = Unsorted.getRSInterface(var3);
                            Class3_Sub28_Sub18.anInt3764 = var3;
                            anInt1473 = var2;
                            Class164_Sub1.anInt3012 = 1;
                            Class164.anInt2050 = var5;
                            Class20.method909(var13);
                            RenderAnimationDefinition.aClass94_378 = RSString.stringCombiner(new RSString[]{
                                    ColorCore.ContextColor2, ItemDefinition.getItemDefinition(var5).name, ColorCore.ContextColor
                            });

                        }
                    } else {
                        if (var4 == 50) {
                            Class163_Sub2_Sub1.method2224(var6, var3, var2);
                            TextureOperation12.outgoingBuffer.putOpcode(194);
                            TextureOperation12.outgoingBuffer.writeShort128LE(var3 + Texture.anInt1152);
                            TextureOperation12.outgoingBuffer.writeShortLE(Class131.anInt1716 + var2);
                            TextureOperation12.outgoingBuffer.writeShort((int) (var6 >>> 32) & Integer.MAX_VALUE);
                        }

                        if (var4 == 48) {
                            TextureOperation12.outgoingBuffer.putOpcode(154);
                            TextureOperation12.outgoingBuffer.writeShortLE(var2);
                            TextureOperation12.outgoingBuffer.writeIntV1(var3);
                            TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                            anInt2330 = 0;
                            Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                            KeyboardListener.anInt1918 = var2;
                        }

                        if (var4 == 30) {
                            var8 = Unsorted.players[var5];
                            if (null != var8) {
                                Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                                Unsorted.anInt2958 = 0;
                                Class70.anInt1053 = Class163_Sub1.anInt2993;
                                Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                                Class36.anInt638 = 2;
                                TextureOperation12.outgoingBuffer.putOpcode(68);
                                TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                            }
                        }

                        if (var4 == 43) {
                            TextureOperation12.outgoingBuffer.putOpcode(153);
                            TextureOperation12.outgoingBuffer.writeIntLE2(var3);
                            TextureOperation12.outgoingBuffer.writeShortLE(var2);
                            TextureOperation12.outgoingBuffer.writeShortLE(var5);
                            anInt2330 = 0;
                            Unsorted.aClass11_1933 = Unsorted.getRSInterface(var3);
                            KeyboardListener.anInt1918 = var2;
                        }

                        if (var4 == 39) {
                            var14 = Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 0, false, 0, 2, var2, 0, 0, 2, var3, Class102.player.anIntArray2767[0]);
                            if (!var14) {
                                Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                            }

                            Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                            Class70.anInt1053 = Class163_Sub1.anInt2993;
                            Class36.anInt638 = 2;
                            Unsorted.anInt2958 = 0;
                            TextureOperation12.outgoingBuffer.putOpcode(73);
                            TextureOperation12.outgoingBuffer.writeIntV1(anInt872);
                            TextureOperation12.outgoingBuffer.writeShort(Texture.anInt1152 + var3);
                            TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                            TextureOperation12.outgoingBuffer.writeShort128LE(var2 + Class131.anInt1716);
                            TextureOperation12.outgoingBuffer.writeShortLE(RSInterface.anInt278);
                        }

                        if (var4 == 12) {
                            TextureOperation12.outgoingBuffer.putOpcode(82);
                            TextureOperation12.outgoingBuffer.writeShort(RSInterface.anInt278);
                            TextureOperation12.outgoingBuffer.writeIntV1(var3);
                            TextureOperation12.outgoingBuffer.writeInt(anInt872);
                            TextureOperation12.outgoingBuffer.writeShort128LE(var2);
                        }

                        if (var4 == 36) {
                            if (var5 == 0) {
                                ObjectDefinition.anInt1521 = 1;
                                Unsorted.method589(WorldListCountry.localPlane, var2, var3);
                            } else if (Player.rights > 0 && ObjectDefinition.aBooleanArray1490[82] && ObjectDefinition.aBooleanArray1490[81]) {
                                Class30.method979(var2 + Class131.anInt1716, Texture.anInt1152 - -var3, WorldListCountry.localPlane);
                            } else {
                                TextureOperation12.outgoingBuffer.putOpcode(179);
                                TextureOperation12.outgoingBuffer.writeShort(var3 + Texture.anInt1152);
                                TextureOperation12.outgoingBuffer.writeShort(var2 + Class131.anInt1716);
                            }
                        }

                        if (6 == var4) {
                            var8 = Unsorted.players[var5];
                            if (var8 != null) {
                                Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var8.anIntArray2767[0], 1, 0, 2, var8.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                                Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                                Unsorted.anInt2958 = 0;
                                Class36.anInt638 = 2;
                                Class70.anInt1053 = Class163_Sub1.anInt2993;
                                TextureOperation12.outgoingBuffer.putOpcode(106);
                                TextureOperation12.outgoingBuffer.writeShort(var5);
                            }
                        }

                        if (var4 == 20) {
                            if (1 == Class158.paramGameTypeID) {
                                Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                            } else {
                                var14 = Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 0, false, 0, 2, var2, 0, 0, 2, var3, Class102.player.anIntArray2767[0]);
                                if (!var14) {
                                    Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var2, 1, 0, 2, var3, Class102.player.anIntArray2767[0]);
                                }
                            }

                            Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                            Unsorted.anInt2958 = 0;
                            Class70.anInt1053 = Class163_Sub1.anInt2993;
                            Class36.anInt638 = 2;
                            TextureOperation12.outgoingBuffer.putOpcode(33);
                            TextureOperation12.outgoingBuffer.writeShort(var5);
                            TextureOperation12.outgoingBuffer.writeShort(Class131.anInt1716 + var2);
                            TextureOperation12.outgoingBuffer.writeShortLE(Texture.anInt1152 + var3);
                        }

                        if (var4 == 16) {
                            var11 = NPC.npcs[var5];
                            if (null != var11) {
                                Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var11.anIntArray2767[0], 1, 0, 2, var11.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                                Class70.anInt1053 = Class163_Sub1.anInt2993;
                                Unsorted.anInt2958 = 0;
                                Unsorted.anInt4062 = Class38_Sub1.anInt2614;
                                Class36.anInt638 = 2;
                                TextureOperation12.outgoingBuffer.putOpcode(3);
                                TextureOperation12.outgoingBuffer.writeShort128LE(var5);
                            }
                        }

                        if (Class164_Sub1.anInt3012 != 0) {
                            Class164_Sub1.anInt3012 = 0;
                            Class20.method909(Unsorted.getRSInterface(Class3_Sub28_Sub18.anInt3764));
                        }

                        if (GameObject.aBoolean1837) {
                            Class25.method958((byte) -36);
                        }

                        if (Unsorted.aClass11_1933 != null && anInt2330 == 0) {
                            Class20.method909(Unsorted.aClass11_1933);
                        }

                    }
                }
            }
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "i.E(" + 2597 + ',' + var1 + ')');
        }
    }

    static boolean method715(RSInterface var1) {
        try {
            if (205 == var1.anInt189) {
                Class159.anInt2023 = 250;
                return true;
            } else {
                return false;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ud.B(" + 205 + ',' + "null" + ')');
        }
    }

    final void setBitAccess() {
        try {
            this.headiconsPrayerSpriteArchive6 = this.index * 8;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "i.R(" + ')');
        }
    }

    public BufferedDataStream() {
        super(5000);
    }

    static int method809(int var0, int var1, int var2, int var3) {
        try {
            if (TextureOperation14.aBoolean3387) {
                var0 = 1000000;
                TextureOperation14.aBoolean3387 = false;
            }

            AtmosphereParser var5 = AtmosphereParser.aAtmosphereParserArrayArray1581[var3][var1];
            float var7 = ((float) var2 * 0.1F + 0.7F) * var5.aFloat1187;
            float var8 = var5.aFloat1190;
            int var6 = var5.anInt1177;
            int var11 = var5.anInt1184;
            int var10 = var5.anInt1175;
            if (!Class38.aBoolean661) {
                var11 = 0;
            }

            float var9 = var5.aFloat1189;
            if (var6 != Class60.anInt932 || Class3_Sub17.aFloat2457 != var7 || aFloat3044 != var8 || var9 != RSInterface.aFloat246 || anInt1345 != var10 || anInt1736 != var11) {
                Class3_Sub17.aFloat2457 = var7;
                TextureOperation36.aFloat3435 = TextureOperation36.aFloat3424;
                TextureOperation9.aFloat3105 = Class30.aFloat578;
                Class60.anInt932 = var6;
                anInt1971 = anInt3652;
                anInt1407 = Unsorted.anInt689;
                RSInterface.aFloat246 = var9;
                Unsorted.anInt72 = 0;
                anInt4037 = Unsorted.anInt1950;
                anInt1736 = var11;
                aFloat3044 = var8;
                anInt1345 = var10;
                aFloat1475 = Class12.aFloat319;
            }

            if (65536 > Unsorted.anInt72) {
                Unsorted.anInt72 += 250 * var0;
                if (Unsorted.anInt72 >= 65536) {
                    Unsorted.anInt72 = 65536;
                }

                float var15 = (float) Unsorted.anInt72 / 65536.0F;
                int var13 = Unsorted.anInt72 >> 8;
                int var12 = -Unsorted.anInt72 + 65536 >> 8;
                anInt3652 = (-16711936 & var13 * (anInt1345 & 16711935) + (16711935 & anInt1971) * var12) + (16711680 & var12 * (anInt1971 & 65280) + (65280 & anInt1345) * var13) >> 8;
                float var14 = (float) (65536 - Unsorted.anInt72) / 65536.0F;
                Class30.aFloat578 = var14 * TextureOperation9.aFloat3105 + var15 * Class3_Sub17.aFloat2457;
                TextureOperation36.aFloat3424 = TextureOperation36.aFloat3435 * var14 + var15 * aFloat3044;
                Class12.aFloat319 = var15 * RSInterface.aFloat246 + var14 * aFloat1475;
                Unsorted.anInt1950 = (16711680 & (Class60.anInt932 & 65280) * var13 + var12 * (anInt4037 & 65280)) + ((16711935 & anInt4037) * var12 - -((Class60.anInt932 & 16711935) * var13) & -16711936) >> 8;
                Unsorted.anInt689 = var13 * anInt1736 + var12 * anInt1407 >> 8;
            }

            Class92.setLightParams(Unsorted.anInt1950, Class30.aFloat578, TextureOperation36.aFloat3424, Class12.aFloat319);
            Class92.setFogValues(anInt3652, Unsorted.anInt689);

            Class92.setLightPosition((float) Class46.anInt741, (float) TextureOperation1.anInt3274, (float) AtmosphereParser.anInt1191);
            Class92.method1504();
            return anInt3652;
        } catch (RuntimeException var16) {
            throw ClientErrorException.clientError(var16, "i.F(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + 1 + ')');
        }
    }

    static int method810(int var1) {
        return 255 & var1;
    }

    final void method811(byte var1, int var2, byte[] var3, int var4) {
        try {
            if (var1 < 16) {
                addChatMessage(null, 126, null, -28);
            }

            for (int var5 = 0; var5 < var4; ++var5) {
                var3[var2 + var5] = (byte) (this.buffer[this.index++] + -this.isaacCipher.nextOpcode());
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "i.S(" + var1 + ',' + var2 + ',' + (var3 != null ? "{...}" : "null") + ',' + var4 + ')');
        }
    }

    final int getBits(int var2) {
        try {
            int var3 = this.headiconsPrayerSpriteArchive6 >> 3;
            int var4 = 8 + -(7 & this.headiconsPrayerSpriteArchive6);
            int var5 = 0;
            this.headiconsPrayerSpriteArchive6 += var2;
            while (var2 > var4) {
                var5 += (anIntArray2709[var4] & this.buffer[var3++]) << -var4 + var2;
                var2 -= var4;
                var4 = 8;
            }

            if (var2 == var4) {
                var5 += this.buffer[var3] & anIntArray2709[var4];
            } else {
                var5 += this.buffer[var3] >> var4 - var2 & anIntArray2709[var2];
            }

            return var5;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "i.C(" + (byte) -11 + ',' + var2 + ')');
        }
    }

    final void method814(int[] var1) {
        try {
            this.isaacCipher = new ISAACCipher(var1);

        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "i.T(" + (var1 != null ? "{...}" : "null") + ',' + false + ')');
        }
    }

    final int method815(int var1) {
        try {

            return var1 * 8 - this.headiconsPrayerSpriteArchive6;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "i.U(" + var1 + ',' + 32666 + ')');
        }
    }

    public final void putOpcode(int opcode) {
        if (buffer == null || isaacCipher == null) {
            SystemLogger.logInfo("Buffer/cipher null. ISAAC likely disabled or not implemented.");
            return;
        }
        this.buffer[this.index++] = (byte) (opcode + this.isaacCipher.nextOpcode());
    }

    public final int getOpcode() {
        try {
            return 255 & this.buffer[this.index++] - this.isaacCipher.nextOpcode();
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "i.P(" + 0 + ')');
        }
    }

    final void method818() {
        try {
            this.index = (this.headiconsPrayerSpriteArchive6 + 7) / 8;

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "i.Q(" + false + ')');
        }
    }

    static void method819() {
        try {
            Class3_Sub31 var1 = TextureOperation23.aHashTable_3208.first();
            for (; var1 != null; var1 = TextureOperation23.aHashTable_3208.next()) {
                int var2 = var1.anInt2602;
                if (Unsorted.loadInterface(var2)) {
                    boolean var3 = true;
                    RSInterface[] var4 = GameObject.aClass11ArrayArray1834[var2];

                    int var5;
                    for (var5 = 0; var5 < var4.length; ++var5) {
                        if (var4[var5] != null) {
                            var3 = var4[var5].usingScripts;
                            break;
                        }
                    }

                    if (!var3) {
                        var5 = (int) var1.linkableKey;
                        RSInterface var6 = Unsorted.getRSInterface(var5);
                        if (null != var6) {
                            Class20.method909(var6);
                        }
                    }
                }
            }

        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "i.A(" + false + ')');
        }
    }

}
