package org.runite.client;

import org.rs09.client.Linkable;
import org.rs09.client.data.HashTable;

final class Class3_Sub27 extends Linkable {

    HashTable aHashTable_2564;
    byte[] aByteArray2565;


    final void method515() {
        this.aHashTable_2564 = null;
    }

    final void method516() {
        if (this.aHashTable_2564 == null) {
            this.aHashTable_2564 = new HashTable(16);
            int[] var1 = new int[16];
            int[] var2 = new int[16];
            var1[9] = var2[9] = 128;
            Class78 var3 = new Class78(this.aByteArray2565);
            int var4 = var3.method1374();

            int var5;
            for (var5 = 0; var5 < var4; ++var5) {
                var3.method1376(var5);
                var3.method1377(var5);
                var3.method1381(var5);
            }

            while (true) {
                var5 = var3.method1382();
                int var6 = var3.anIntArray1114[var5];

                while (var3.anIntArray1114[var5] == var6) {
                    var3.method1376(var5);
                    int var7 = var3.method1375(var5);
                    if (var7 == 1) {
                        var3.method1384();
                        var3.method1381(var5);
                        if (var3.method1371()) {
                            return;
                        }
                        break;
                    }

                    int var8 = var7 & 240;
                    int var9;
                    int var10;
                    int var11;
                    if (var8 == 176) {
                        var9 = var7 & 15;
                        var10 = var7 >> 8 & 127;
                        var11 = var7 >> 16 & 127;
                        if (var10 == 0) {
                            var1[var9] = (var1[var9] & -2080769) + (var11 << 14);
                        }

                        if (var10 == 32) {
                            var1[var9] = (var1[var9] & -16257) + (var11 << 7);
                        }
                    }

                    if (var8 == 192) {
                        var9 = var7 & 15;
                        var10 = var7 >> 8 & 127;
                        var2[var9] = var1[var9] + var10;
                    }

                    if (var8 == 144) {
                        var9 = var7 & 15;
                        var10 = var7 >> 8 & 127;
                        var11 = var7 >> 16 & 127;
                        if (var11 > 0) {
                            int var12 = var2[var9];
                            Class3_Sub6 var13 = (Class3_Sub6) this.aHashTable_2564.get(var12);
                            if (var13 == null) {
                                var13 = new Class3_Sub6(new byte[128]);
                                this.aHashTable_2564.put(var12, var13);
                            }

                            var13.aByteArray2289[var10] = 1;
                        }
                    }

                    var3.method1377(var5);
                    var3.method1381(var5);
                }
            }
        }
    }

    static Class3_Sub27 method517(CacheIndex var0, int var1, int var2) {
        byte[] var3 = var0.getFile(var1, var2);
        return var3 == null ? null : new Class3_Sub27(new DataBuffer(var3));
    }

    private Class3_Sub27(DataBuffer buffer) {
        buffer.index = buffer.buffer.length - 3;
        int var2 = buffer.readUnsignedByte();
        int var3 = buffer.readUnsignedShort();
        int var4 = 14 + var2 * 10;
        buffer.index = 0;
        int var5 = 0;
        int var6 = 0;
        int var7 = 0;
        int var8 = 0;
        int var9 = 0;
        int var10 = 0;
        int var11 = 0;
        int var12 = 0;
        int var13 = 0;

        int var14;
        int var15;
        while (var13 < var2) {
            var14 = -1;

            while (true) {
                var15 = buffer.readUnsignedByte();
                if (var15 != var14) {
                    ++var4;
                }

                var14 = var15 & 15;
                if (var15 == 7) {
                    ++var13;
                    break;
                }

                if (var15 == 23) {
                    ++var5;
                } else if (var14 == 0) {
                    ++var7;
                } else if (var14 == 1) {
                    ++var8;
                } else if (var14 == 2) {
                    ++var6;
                } else if (var14 == 3) {
                    ++var9;
                } else if (var14 == 4) {
                    ++var10;
                } else if (var14 == 5) {
                    ++var11;
                } else {
                    if (var14 != 6) {
                        throw new RuntimeException();
                    }

                    ++var12;
                }
            }
        }

        var4 += 5 * var5;
        var4 += 2 * (var7 + var8 + var6 + var9 + var11);
        var4 += var10 + var12;
        var13 = buffer.index;
        var14 = var2 + var5 + var6 + var7 + var8 + var9 + var10 + var11 + var12;

        for (var15 = 0; var15 < var14; ++var15) {
            buffer.method741();
        }

        var4 += buffer.index - var13;
        var15 = buffer.index;
        int var16 = 0;
        int var17 = 0;
        int var18 = 0;
        int var19 = 0;
        int var20 = 0;
        int var21 = 0;
        int var22 = 0;
        int var23 = 0;
        int var24 = 0;
        int var25 = 0;
        int var26 = 0;
        int var27 = 0;
        int var28 = 0;

        int var29;
        for (var29 = 0; var29 < var6; ++var29) {
            var28 = var28 + buffer.readUnsignedByte() & 127;
            if (var28 == 0 || var28 == 32) {
                ++var12;
            } else if (var28 == 1) {
                ++var16;
            } else if (var28 == 33) {
                ++var17;
            } else if (var28 == 7) {
                ++var18;
            } else if (var28 == 39) {
                ++var19;
            } else if (var28 == 10) {
                ++var20;
            } else if (var28 == 42) {
                ++var21;
            } else if (var28 == 99) {
                ++var22;
            } else if (var28 == 98) {
                ++var23;
            } else if (var28 == 101) {
                ++var24;
            } else if (var28 == 100) {
                ++var25;
            } else if (var28 != 64 && var28 != 65 && var28 != 120 && var28 != 121 && var28 != 123) {
                ++var27;
            } else {
                ++var26;
            }
        }

        var29 = 0;
        int var30 = buffer.index;
        buffer.index += var26;
        int var31 = buffer.index;
        buffer.index += var11;
        int var32 = buffer.index;
        buffer.index += var10;
        int var33 = buffer.index;
        buffer.index += var9;
        int var34 = buffer.index;
        buffer.index += var16;
        int var35 = buffer.index;
        buffer.index += var18;
        int var36 = buffer.index;
        buffer.index += var20;
        int var37 = buffer.index;
        buffer.index += var7 + var8 + var11;
        int var38 = buffer.index;
        buffer.index += var7;
        int var39 = buffer.index;
        buffer.index += var27;
        int var40 = buffer.index;
        buffer.index += var8;
        int var41 = buffer.index;
        buffer.index += var17;
        int var42 = buffer.index;
        buffer.index += var19;
        int var43 = buffer.index;
        buffer.index += var21;
        int var44 = buffer.index;
        buffer.index += var12;
        int var45 = buffer.index;
        buffer.index += var9;
        int var46 = buffer.index;
        buffer.index += var22;
        int var47 = buffer.index;
        buffer.index += var23;
        int var48 = buffer.index;
        buffer.index += var24;
        int var49 = buffer.index;
        buffer.index += var25;
        int var50 = buffer.index;
        buffer.index += var5 * 3;
        this.aByteArray2565 = new byte[var4];
        DataBuffer var51 = new DataBuffer(this.aByteArray2565);
        var51.writeInt(1297377380);
        var51.writeInt(6);
        var51.writeShort(var2 > 1 ? 1 : 0);
        var51.writeShort(var2);
        var51.writeShort(var3);
        buffer.index = var13;
        int var52 = 0;
        int var53 = 0;
        int var54 = 0;
        int var55 = 0;
        int var56 = 0;
        int var57 = 0;
        int var58 = 0;
        int[] var59 = new int[128];
        var28 = 0;
        int var60 = 0;

        while (var60 < var2) {
            var51.writeInt(1297379947);
            var51.index += 4;
            int var61 = var51.index;
            int var62 = -1;

            while (true) {
                int var63 = buffer.method741();
                var51.method771(var63);
                int var64 = buffer.buffer[var29++] & 0xFF;
                boolean var65 = var64 != var62;
                var62 = var64 & 15;
                if (var64 == 7) {
                    if (var65) {
                        var51.writeByte(255);
                    }

                    var51.writeByte(47);
                    var51.writeByte(0);
                    var51.method742(var51.index - var61);
                    ++var60;
                    break;
                }

                if (var64 == 23) {
                    if (var65) {
                        var51.writeByte(255);
                    }

                    var51.writeByte(81);
                    var51.writeByte(3);
                    var51.writeByte(buffer.buffer[var50++]);
                    var51.writeByte(buffer.buffer[var50++]);
                    var51.writeByte(buffer.buffer[var50++]);
                } else {
                    var52 ^= var64 >> 4;
                    if (var62 == 0) {
                        if (var65) {
                            var51.writeByte(144 + var52);
                        }

                        var53 += buffer.buffer[var37++];
                        var54 += buffer.buffer[var38++];
                        var51.writeByte(var53 & 127);
                        var51.writeByte(var54 & 127);
                    } else if (var62 == 1) {
                        if (var65) {
                            var51.writeByte(128 + var52);
                        }

                        var53 += buffer.buffer[var37++];
                        var55 += buffer.buffer[var40++];
                        var51.writeByte(var53 & 127);
                        var51.writeByte(var55 & 127);
                    } else if (var62 == 2) {
                        if (var65) {
                            var51.writeByte(176 + var52);
                        }

                        var28 = var28 + buffer.buffer[var15++] & 127;
                        var51.writeByte(var28);
                        byte var66;
                        if (var28 == 0 || var28 == 32) {
                            var66 = buffer.buffer[var44++];
                        } else if (var28 == 1) {
                            var66 = buffer.buffer[var34++];
                        } else if (var28 == 33) {
                            var66 = buffer.buffer[var41++];
                        } else if (var28 == 7) {
                            var66 = buffer.buffer[var35++];
                        } else if (var28 == 39) {
                            var66 = buffer.buffer[var42++];
                        } else if (var28 == 10) {
                            var66 = buffer.buffer[var36++];
                        } else if (var28 == 42) {
                            var66 = buffer.buffer[var43++];
                        } else if (var28 == 99) {
                            var66 = buffer.buffer[var46++];
                        } else if (var28 == 98) {
                            var66 = buffer.buffer[var47++];
                        } else if (var28 == 101) {
                            var66 = buffer.buffer[var48++];
                        } else if (var28 == 100) {
                            var66 = buffer.buffer[var49++];
                        } else if (var28 != 64 && var28 != 65 && var28 != 120 && var28 != 121 && var28 != 123) {
                            var66 = buffer.buffer[var39++];
                        } else {
                            var66 = buffer.buffer[var30++];
                        }

                        int var67 = var66 + var59[var28];
                        var59[var28] = var67;
                        var51.writeByte(var67 & 127);
                    } else if (var62 == 3) {
                        if (var65) {
                            var51.writeByte(224 + var52);
                        }

                        var56 += buffer.buffer[var45++];
                        var56 += buffer.buffer[var33++] << 7;
                        var51.writeByte(var56 & 127);
                        var51.writeByte(var56 >> 7 & 127);
                    } else if (var62 == 4) {
                        if (var65) {
                            var51.writeByte(208 + var52);
                        }

                        var57 += buffer.buffer[var32++];
                        var51.writeByte(var57 & 127);
                    } else if (var62 == 5) {
                        if (var65) {
                            var51.writeByte(160 + var52);
                        }

                        var53 += buffer.buffer[var37++];
                        var58 += buffer.buffer[var31++];
                        var51.writeByte(var53 & 127);
                        var51.writeByte(var58 & 127);
                    } else {
                        if (var62 != 6) {
                            throw new RuntimeException();
                        }

                        if (var65) {
                            var51.writeByte(192 + var52);
                        }

                        var51.writeByte(buffer.buffer[var44++]);
                    }
                }
            }
        }

    }
}
