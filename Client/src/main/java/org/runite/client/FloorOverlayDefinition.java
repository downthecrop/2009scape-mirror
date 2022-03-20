package org.runite.client;

final class FloorOverlayDefinition {

    int anInt2090 = 128;
    boolean aBoolean2092 = false;
    boolean aBoolean2093 = true;
    int anInt2094 = 1190717;
    int anInt2095 = -1;
    int anInt2098 = -1;
    int anInt2100 = 8;
    int anInt2101 = 16;
    boolean aBoolean2102 = true;
    int anInt2103 = 0;

    static FloorOverlayDefinition getFile(int var1) {
        try {
            FloorOverlayDefinition var2 = (FloorOverlayDefinition) Class163_Sub2_Sub1.aReferenceCache_4015.get(var1);
            if (null == var2) {
                System.out.println("Retrieving file " + var1);
                byte[] var4 = Class3_Sub28_Sub5.configurationsIndex_3580.getFile(4, var1);
                var2 = new FloorOverlayDefinition();
                if (var4 != null) {
                    var2.parseDefinition(new DataBuffer(var4), var1);
                }

                Class163_Sub2_Sub1.aReferenceCache_4015.put(var2, var1);
            }
            return var2;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "um.C(" + ',' + var1 + ')');
        }
    }

    static void method1014(int var1, int var2) {
        try {

            for (int var4 = 0; var4 < Client.anInt869; ++var4) {
                FloorOverlayDefinition var5 = getFile(var4);
                if (null != var5) {
                    int var6 = var5.anInt2095;
                    if (0 <= var6 && !Class51.anInterface2_838.method17(var6, 126)) {
                        var6 = -1;
                    }

                    int var7;
                    int var8;
                    int var9;
                    int var10;
                    if (var5.anInt2098 < 0) {
                        if (var6 >= 0) {
                            var7 = Class51.anIntArray834[LinkableRSString.method729((byte) -74, Class51.anInterface2_838.method15(var6, 65535), 96)];
                        } else if (-1 == var5.anInt2103) {
                            var7 = -1;
                        } else {
                            var8 = var5.anInt2103;
                            var9 = var1 + (var8 & 127);
                            if (var9 < 0) {
                                var9 = 0;
                            } else if (var9 > 127) {
                                var9 = 127;
                            }

                            var10 = var9 + (896 & var8) + ('\ufc00' & var8 + var2);
                            var7 = Class51.anIntArray834[LinkableRSString.method729((byte) -127, var10, 96)];
                        }
                    } else {
                        var8 = var5.anInt2098;
                        var9 = (127 & var8) + var1;
                        if (var9 < 0) {
                            var9 = 0;
                        } else if (var9 > 127) {
                            var9 = 127;
                        }

                        var10 = (896 & var8) + ('\ufc00' & var2 + var8) + var9;
                        var7 = Class51.anIntArray834[LinkableRSString.method729((byte) -63, var10, 96)];
                    }

                    Class83.anIntArray1161[1 + var4] = var7;
                }
            }

        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "fi.B(" + -120 + ',' + var1 + ',' + var2 + ')');
        }
    }

    static void method631(CacheIndex var1) {
        try {
            Class3_Sub28_Sub5.configurationsIndex_3580 = var1;
            Client.anInt869 = Class3_Sub28_Sub5.configurationsIndex_3580.getFileAmount(4);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "qc.D(" + false + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    final void parseDefinition(DataBuffer var2, int var3) {
        try {
            while (true) {
                int var4 = var2.readUnsignedByte();
                if (var4 == 0) {

                    return;
                }

                this.decode(var4, var2, var3);
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "wl.H(" + 24559 + ',' + (var2 != null ? "{...}" : "null") + ',' + var3 + ')');
        }
    }

    private void decode(int opcode, DataBuffer buffer, int var4) {
        try {
            if (opcode == 1) {
                this.anInt2103 = RSInterface.method869(buffer.readMedium());
            } else if (opcode == 2) {
                this.anInt2095 = buffer.readUnsignedByte();
            } else if (3 == opcode) {
                this.anInt2095 = buffer.readUnsignedShort();
                if (this.anInt2095 == 65535) {
                    this.anInt2095 = -1;
                }
            } else if (5 == opcode) {
                this.aBoolean2102 = false;
            } else if (opcode == 7) {
                this.anInt2098 = RSInterface.method869(buffer.readMedium());
            } else if (opcode == 8) {
                TextureOperation26.anInt3081 = var4;
            } else if (opcode == 9) {
                this.anInt2090 = buffer.readUnsignedShort();
            } else if (opcode == 10) {
                this.aBoolean2093 = false;
            } else if (opcode == 11) {
                this.anInt2100 = buffer.readUnsignedByte();
            } else if (12 == opcode) {
                this.aBoolean2092 = true;
            } else if (13 == opcode) {
                this.anInt2094 = buffer.readMedium();
            } else if (opcode == 14) {
                this.anInt2101 = buffer.readUnsignedByte();
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "wl.E(" + 0 + ',' + opcode + ',' + (buffer != null ? "{...}" : "null") + ',' + var4 + ')');
        }
    }

}
