package org.runite.client;

public final class WorldMapOverlay2Decoder {

    static void decode(DataBuffer var1) {
        try {

            while (var1.index < var1.buffer.length) {
                int var4 = 0;
                boolean var3 = false;
                int var5 = 0;
                if (var1.readUnsignedByte() == 1) {
                    var3 = true;
                    var4 = var1.readUnsignedByte();
                    var5 = var1.readUnsignedByte();
                }

                int var6 = var1.readUnsignedByte();
                int var7 = var1.readUnsignedByte();
                int var8 = -TextureOperation37.anInt3256 + var6 * 64;
                int var9 = Class108.anInt1460 + -1 - -Unsorted.anInt65 - 64 * var7;
                byte var2;
                int var10;
                if (var8 >= 0 && (var9 - 63) >= 0 && Class23.anInt455 > var8 + 63 && Class108.anInt1460 > var9) {
                    var10 = var8 >> 6;
                    int var11 = var9 >> 6;

                    for (int var12 = 0; 64 > var12; ++var12) {
                        for (int var13 = 0; var13 < 64; ++var13) {
                            if (!var3 || var12 >= (var4 * 8) && 8 + 8 * var4 > var12 && var13 >= var5 * 8 && 8 + var5 * 8 > var13) {
                                var2 = var1.readSignedByte();
                                if (var2 != 0) {
                                    if (null == TextureOperation29.aByteArrayArrayArray3390[var10][var11]) {
                                        TextureOperation29.aByteArrayArrayArray3390[var10][var11] = new byte[4096];
                                    }

                                    TextureOperation29.aByteArrayArrayArray3390[var10][var11][(63 + -var13 << 6) + var12] = var2;
                                    byte var14 = var1.readSignedByte();
                                    if (null == CS2Script.aByteArrayArrayArray2452[var10][var11]) {
                                        CS2Script.aByteArrayArrayArray2452[var10][var11] = new byte[4096];
                                    }

                                    CS2Script.aByteArrayArrayArray2452[var10][var11][var12 + (-var13 + 63 << 6)] = var14;
                                }
                            }
                        }
                    }
                } else {
                    for (var10 = 0; (!var3 ? 4096 : 64) > var10; ++var10) {
                        var2 = var1.readSignedByte();
                        if (var2 != 0) {
                            ++var1.index;
                        }
                    }
                }
            }

        } catch (RuntimeException var15) {
            throw ClientErrorException.clientError(var15, "sk.F(" + -21774 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }
}
