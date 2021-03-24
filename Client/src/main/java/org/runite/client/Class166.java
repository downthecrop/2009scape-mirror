package org.runite.client;

final class Class166 {

    static Class3_Sub2[][][] aClass3_Sub2ArrayArrayArray2065;
    static int[] anIntArray2068 = new int[50];
    static Class3_Sub28_Sub5[] aClass3_Sub28_Sub5Array2070 = new Class3_Sub28_Sub5[14];
    static AbstractSprite[] aAbstractSpriteArray2072;
    static int[] anIntArray2073 = new int[5];
    static int anInt2079 = 0;
    int anInt2063;
    byte[] aByteArray2064;
    int anInt2066;
    int anInt2067;
    int anInt2069;
    int anInt2071;
    byte[] aByteArray2076;
    int anInt2077;
    int anInt2078;

    static boolean method2256(int var0, int var1, int var2, int var3) {
        if (Class8.method846(var0, var1, var2)) {
            int var4 = var1 << 7;
            int var5 = var2 << 7;
            return !TextureOperation10.method349(var4 + 1, Class44.anIntArrayArrayArray723[var0][var1][var2] + var3, var5 + 1) || !TextureOperation10.method349(var4 + 128 - 1, Class44.anIntArrayArrayArray723[var0][var1 + 1][var2] + var3, var5 + 1) || !TextureOperation10.method349(var4 + 128 - 1, Class44.anIntArrayArrayArray723[var0][var1 + 1][var2 + 1] + var3, var5 + 128 - 1) || !TextureOperation10.method349(var4 + 1, Class44.anIntArrayArrayArray723[var0][var1][var2 + 1] + var3, var5 + 128 - 1);
        } else {
            return true;
        }
    }

    static void method2257() {
        try {

            Class163_Sub2_Sub1.aReferenceCache_4015.clear();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "wh.F(" + 96 + ')');
        }
    }

    static void method2258(int var0, RSString var2) {
        try {
            RSString var3 = var2.method1579().longToRSString();
            boolean var4 = false;

            for (int var5 = 0; Class159.localPlayerCount > var5; ++var5) {
                Player var6 = TextureOperation0.players[Class56.localPlayerIndexes[var5]];
                if (null != var6 && null != var6.displayName && var6.displayName.equalsStringIgnoreCase(var3)) {
                    var4 = true;
                    Class3_Sub28_Sub9.method582(Class102.player.anIntArray2755[0], 0, 1, false, 0, 2, var6.anIntArray2767[0], 1, 0, 2, var6.anIntArray2755[0], Class102.player.anIntArray2767[0]);
                    if (1 == var0) {
                        TextureOperation12.outgoingBuffer.putOpcode(68);
                        TextureOperation12.outgoingBuffer.writeShort128LE(Class56.localPlayerIndexes[var5]);
                    } else if (4 == var0) {
                        TextureOperation12.outgoingBuffer.putOpcode(180);
                        TextureOperation12.outgoingBuffer.writeShort128LE(Class56.localPlayerIndexes[var5]);
                    } else if (5 == var0) {
                        TextureOperation12.outgoingBuffer.putOpcode(4);
                        TextureOperation12.outgoingBuffer.writeShortLE(Class56.localPlayerIndexes[var5]);
                    } else if (var0 == 6) {
                        TextureOperation12.outgoingBuffer.putOpcode(133);
                        TextureOperation12.outgoingBuffer.writeShortLE(Class56.localPlayerIndexes[var5]);
                    } else if (var0 == 7) {
                        TextureOperation12.outgoingBuffer.putOpcode(114);
                        TextureOperation12.outgoingBuffer.writeShort128LE(Class56.localPlayerIndexes[var5]);
                    }
                    break;
                }
            }

            if (!var4) {
                Class3_Sub30_Sub1.addChatMessage(TextCore.emptyJagexString, 0, RSString.stringCombiner(new RSString[]{TextCore.HasUnableFind, var3}), -1);
            }

        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "wh.D(" + var0 + ',' + 0 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    static AbstractIndexedSprite method2259() {
        try {
            Object var1;
            if (HDToolKit.highDetail) {
                var1 = new HDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], Unsorted.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Unsorted.anIntArray3076[0], Class163_Sub1.aByteArrayArray2987[0], TextureOperation38.spritePalette);
            } else {
                var1 = new LDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[0], Unsorted.anIntArray2591[0], Class140_Sub7.anIntArray2931[0], Unsorted.anIntArray3076[0], Class163_Sub1.aByteArrayArray2987[0], TextureOperation38.spritePalette);
            }

            Class39.method1035((byte) 116);
            return (AbstractIndexedSprite) var1;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "wh.A(" + (byte) -40 + ')');
        }
    }

}
