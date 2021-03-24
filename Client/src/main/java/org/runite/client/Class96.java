package org.runite.client;

public final class Class96 {

    static int anInt1358 = 0;
    public int anInt1346;
    public int anInt1347;
    public int anInt1350;
    public int anInt1351;
    public int anInt1353;
    public int anInt1355 = -1;
    public int anInt1356;
    public int anInt1359;
    public int anInt1360;

    static void method1587(DataBuffer var1) {
        try {
            label134:
            while (true) {
                if (var1.index < var1.buffer.length) {
                    boolean var18 = false;
                    int var5 = 0;
                    int var6 = 0;
                    if (var1.readUnsignedByte() == 1) {
                        var5 = var1.readUnsignedByte();
                        var18 = true;
                        var6 = var1.readUnsignedByte();
                    }

                    int var7 = var1.readUnsignedByte();
                    int var8 = var1.readUnsignedByte();
                    int var9 = -TextureOperation37.anInt3256 + 64 * var7;
                    int var10 = -(var8 * 64) - (-Unsorted.anInt65 - -1) + Class108.anInt1460;
                    int var11;
                    int var12;
                    if (var9 >= 0 && -63 + var10 >= 0 && Class23.anInt455 > var9 + 63 && var10 < Class108.anInt1460) {
                        var11 = var9 >> 6;
                        var12 = var10 >> 6;
                        int var13 = 0;

                        while (true) {
                            if (var13 >= 64) {
                                continue label134;
                            }

                            for (int var14 = 0; var14 < 64; ++var14) {
                                if (!var18 || var13 >= 8 * var5 && 8 * var5 - -8 > var13 && 8 * var6 <= var14 && var14 < var6 * 8 - -8) {
                                    int var15 = var1.readUnsignedByte();
                                    if (0 != var15) {
                                        int var2;
                                        if (1 == (1 & var15)) {
                                            var2 = var1.readUnsignedByte();
                                            if (Class36.aByteArrayArrayArray640[var11][var12] == null) {
                                                Class36.aByteArrayArrayArray640[var11][var12] = new byte[4096];
                                            }

                                            Class36.aByteArrayArrayArray640[var11][var12][var13 + (-var14 + 63 << 6)] = (byte) var2;
                                        }

                                        if (2 == (var15 & 2)) {
                                            var2 = var1.readMedium();
                                            if (null == Class29.anIntArrayArrayArray558[var11][var12]) {
                                                Class29.anIntArrayArrayArray558[var11][var12] = new int[4096];
                                            }

                                            Class29.anIntArrayArrayArray558[var11][var12][(-var14 + 63 << 6) + var13] = var2;
                                        }

                                        if (4 == (var15 & 4)) {
                                            var2 = var1.readMedium();
                                            if (null == Class44.anIntArrayArrayArray720[var11][var12]) {
                                                Class44.anIntArrayArrayArray720[var11][var12] = new int[4096];
                                            }

                                            --var2;
                                            ObjectDefinition var3 = ObjectDefinition.getObjectDefinition(var2);
                                            if (null != var3.ChildrenIds) {
                                                var3 = var3.method1685(0);
                                                if (var3 == null || var3.MapIcon == -1) {
                                                    continue;
                                                }
                                            }

                                            Class44.anIntArrayArrayArray720[var11][var12][(-var14 + 63 << 6) + var13] = 1 + var3.objectId;
                                            Class3_Sub23 var16 = new Class3_Sub23();
                                            var16.anInt2532 = var3.MapIcon;
                                            var16.anInt2531 = var9;
                                            var16.anInt2539 = var10;
                                            Class84.aLinkedList_1162.method1215(var16);
                                        }
                                    }
                                }
                            }

                            ++var13;
                        }
                    }

                    var11 = 0;

                    while (true) {
                        if (var11 >= (var18 ? 64 : 4096)) {
                            continue label134;
                        }

                        var12 = var1.readUnsignedByte();
                        if (var12 != 0) {
                            if ((var12 & 1) == 1) {
                                ++var1.index;
                            }

                            if (2 == (var12 & 2)) {
                                var1.index += 2;
                            }

                            if (4 == (var12 & 4)) {
                                var1.index += 3;
                            }
                        }

                        ++var11;
                    }
                }
                return;
            }
        } catch (RuntimeException var17) {
            throw ClientErrorException.clientError(var17, "nc.A(" + (byte) -83 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }
}
