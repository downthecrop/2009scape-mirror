package org.runite.client;

final class Class157 {


    static int anInt1996;
    static Class3_Sub28_Sub17_Sub1 aClass3_Sub28_Sub17_Sub1_2000;
    private int anInt1997;
    private int[][] anIntArrayArray1999;
    private int anInt2001;

    Class157(int var1, int var2) {
        try {
            if (var2 != var1) {
                int var3 = Class107.method1651(var2, var1);
                var2 /= var3;
                this.anInt2001 = var2;
                var1 /= var3;
                this.anIntArrayArray1999 = new int[var1][14];
                this.anInt1997 = var1;

                for (int var4 = 0; var4 < var1; ++var4) {
                    int[] var5 = this.anIntArrayArray1999[var4];
                    double var6 = (double) var4 / (double) var1 + 6.0D;
                    double var10 = (double) var2 / (double) var1;
                    int var8 = (int) Math.floor(-7.0D + var6 + 1.0D);
                    int var9 = (int) Math.ceil(7.0D + var6);
                    if (var8 < 0) {
                        var8 = 0;
                    }

                    if (var9 > 14) {
                        var9 = 14;
                    }

                    while (var8 < var9) {
                        double var12 = ((double) var8 - var6) * 3.141592653589793D;
                        double var14 = var10;
                        if (-1.0E-4D > var12 || var12 > 1.0E-4D) {
                            var14 = var10 * (Math.sin(var12) / var12);
                        }

                        var14 *= 0.54D + 0.46D * Math.cos(((double) var8 - var6) * 0.2243994752564138D);
                        var5[var8] = (int) Math.floor(65536.0D * var14 + 0.5D);
                        ++var8;
                    }
                }

            }
        } catch (RuntimeException var16) {
            throw ClientErrorException.clientError(var16, "vj.<init>(" + var1 + ',' + var2 + ')');
        }
    }

    static SoftwareSprite[] method2176(int var0, CacheIndex var1) {
        try {
            //System.out.println("Class 157 " + var2);
            if (Class75_Sub4.method1351(var1, 0, var0)) {
                return method851();
            } else {
                return null;
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "vj.A(" + 0 + ',' + var0 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static SoftwareSprite[] method851() {
        try {
            SoftwareSprite[] var1 = new SoftwareSprite[Class95.anInt1338];

            for (int var2 = 0; Class95.anInt1338 > var2; ++var2) {
                int var3 = Unsorted.anIntArray3076[var2] * GroundItem.anIntArray2931[var2];
                byte[] var4 = Class163_Sub1.aByteArrayArray2987[var2];
                int[] var5 = new int[var3];

                for (int var6 = 0; var3 > var6; ++var6) {
                    var5[var6] = TextureOperation38.spritePalette[Unsorted.bitwiseAnd(255, var4[var6])];
                }

                var1[var2] = new SoftwareSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[var2], Unsorted.anIntArray2591[var2], GroundItem.anIntArray2931[var2], Unsorted.anIntArray3076[var2], var5);
            }

            Class39.method1035((byte) 113);
            return var1;
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "bd.B(" + true + ')');
        }
    }

    final byte[] method2173(byte[] var1) {
        try {
            if (null != this.anIntArrayArray1999) {
                int var4 = 14 + (int) ((long) var1.length * (long) this.anInt2001 / (long) this.anInt1997);
                int[] var5 = new int[var4];
                int var6 = 0;
                int var7 = 0;

                int var8;
                for (var8 = 0; var1.length > var8; ++var8) {
                    int[] var10 = this.anIntArrayArray1999[var7];
                    byte var9 = var1[var8];

                    int var11;
                    for (var11 = 0; var11 < 14; ++var11) {
                        var5[var6 - -var11] += var10[var11] * var9;
                    }

                    var7 += this.anInt2001;
                    var11 = var7 / this.anInt1997;
                    var6 += var11;
                    var7 -= var11 * this.anInt1997;
                }

                var1 = new byte[var4];

                for (var8 = 0; var4 > var8; ++var8) {
                    int var13 = var5[var8] - -32768 >> 16;
                    if (var13 >= -128) {
                        if (127 >= var13) {
                            var1[var8] = (byte) var13;
                        } else {
                            var1[var8] = 127;
                        }
                    } else {
                        var1[var8] = -128;
                    }
                }
            }

            return var1;
        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "vj.E(" + (var1 != null ? "{...}" : "null") + ',' + (byte) -105 + ')');
        }
    }

    final int method2177(int var1) {
        try {
            if (null != this.anIntArrayArray1999) {
                var1 = (int) ((long) this.anInt2001 * (long) var1 / (long) this.anInt1997);
            }

            return var1;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "vj.C(" + var1 + ',' + (byte) 90 + ')');
        }
    }

    final int method2178(int var2) {
        try {
            if (null != this.anIntArrayArray1999) {
                var2 = (int) ((long) this.anInt2001 * (long) var2 / (long) this.anInt1997) + 6;
            }

            return var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "vj.D(" + false + ',' + var2 + ')');
        }
    }

}
