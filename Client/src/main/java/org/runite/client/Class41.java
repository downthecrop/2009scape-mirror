package org.runite.client;

import java.io.EOFException;
import java.io.IOException;

final class Class41 {

    static byte[] aByteArray2040 = new byte[520];
    private final Class30 aClass30_681;
    private final Class30 aClass30_683;
    private final int anInt687;
    int cacheIndex;

    Class41(int var1, Class30 var2, Class30 var3, int var4) {
        this.anInt687 = var4;
        this.aClass30_683 = var3;
        this.cacheIndex = var1;
        this.aClass30_681 = var2;
    }

    public final String toString() {
        return "Cache:" + this.cacheIndex;
    }

    final void write(int var1, int var2, byte[] var3) {
        synchronized (this.aClass30_681) {
            if (0 <= var2 && var2 <= this.anInt687) {
                boolean var6 = this.method1054((byte) 87, var2, var1, var3, true);
                if (!var6) {
                    var6 = this.method1054((byte) 87, var2, var1, var3, false);
                }

            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    final byte[] read(int var1) {
        synchronized (this.aClass30_681) {
            Object var10000;
            try {
                if ((long) (var1 * 6 + 6) > this.aClass30_683.method976(0)) {
                    var10000 = null;
                    return (byte[]) var10000;
                }

                this.aClass30_683.method984(-35, 6 * var1);
                this.aClass30_683.method978(0, aByteArray2040, 6);
                int var5 = ((255 & aByteArray2040[3]) << 16) - (-(aByteArray2040[4] << 8 & 65280) + -(255 & aByteArray2040[5]));
                int var4 = (aByteArray2040[2] & 0xFF) + (65280 & aByteArray2040[1] << 8) + (16711680 & aByteArray2040[0] << 16);
                if (this.anInt687 < var4) {
                    var10000 = null;
                    return (byte[]) var10000;
                }

                if (0 < var5 && this.aClass30_681.method976(0) / 520L >= (long) var5) {
                    byte[] var7 = new byte[var4];
                    int var8 = 0;

                    int var13;
                    for (int var9 = 0; var4 > var8; var5 = var13) {
                        if (0 == var5) {
                            var10000 = null;
                            return (byte[]) var10000;
                        }

                        int var10 = -var8 + var4;
                        this.aClass30_681.method984(-113, 520 * var5);
                        if (var10 > 512) {
                            var10 = 512;
                        }

                        this.aClass30_681.method978(0, aByteArray2040, 8 + var10);
                        int var11 = (aByteArray2040[0] << 8 & 65280) - -(255 & aByteArray2040[1]);
                        int var12 = (aByteArray2040[3] & 0xFF) + (65280 & aByteArray2040[2] << 8);
                        int var14 = 255 & aByteArray2040[7];
                        var13 = (aByteArray2040[6] & 0xFF) + (65280 & aByteArray2040[5] << 8) + (aByteArray2040[4] << 16 & 16711680);
                        if (var1 != var11 || var9 != var12 || this.cacheIndex != var14) {
                            var10000 = null;
                            return (byte[]) var10000;
                        }

                        if (var13 < 0 || (long) var13 > this.aClass30_681.method976(0) / 520L) {
                            var10000 = null;
                            return (byte[]) var10000;
                        }

                        for (int var15 = 0; var10 > var15; ++var15) {
                            var7[var8++] = aByteArray2040[var15 + 8];
                        }

                        ++var9;
                    }

                    return var7;
                }

                var10000 = null;
            } catch (IOException var17) {
                return null;
            }

            return (byte[]) var10000;
        }
    }

    private boolean method1054(byte var1, int var2, int var3, byte[] var4, boolean var5) {
        synchronized (this.aClass30_681) {
            try {
                int var7;
                if (var5) {
                    if (this.aClass30_683.method976(var1 ^ 87) < (long) (6 + var3 * 6)) {
                        return false;
                    }

                    this.aClass30_683.method984(-116, 6 * var3);
                    this.aClass30_683.method978(0, aByteArray2040, 6);
                    var7 = (16711680 & aByteArray2040[3] << 16) + (65280 & aByteArray2040[4] << 8) + (aByteArray2040[5] & 0xFF);
                    if (var7 <= 0 || (long) var7 > this.aClass30_681.method976(0) / 520L) {
                        return false;
                    }
                } else {
                    var7 = (int) ((this.aClass30_681.method976(var1 + -87) - -519L) / 520L);
                    if (var7 == 0) {
                        var7 = 1;
                    }
                }

                aByteArray2040[0] = (byte) (var2 >> 16);
                aByteArray2040[4] = (byte) (var7 >> 8);
                int var8 = 0;
                aByteArray2040[5] = (byte) var7;
                aByteArray2040[2] = (byte) var2;
                aByteArray2040[3] = (byte) (var7 >> 16);
                if (var1 != 87) {
                    this.method1054((byte) 41, 108, -107, null, true);
                }

                int var9 = 0;
                aByteArray2040[1] = (byte) (var2 >> 8);
                this.aClass30_683.method984(-14, var3 * 6);
                this.aClass30_683.method983(aByteArray2040, 0, var1 ^ -903171097, 6);

                while (true) {
                    if (var2 > var8) {
                        label146:
                        {
                            int var10 = 0;
                            int var11;
                            if (var5) {
                                this.aClass30_681.method984(-116, 520 * var7);

                                try {
                                    this.aClass30_681.method978(0, aByteArray2040, 8);
                                } catch (EOFException var15) {
                                    break label146;
                                }

                                var10 = ((aByteArray2040[4] & 0xFF) << 16) + (65280 & aByteArray2040[5] << 8) - -(aByteArray2040[6] & 0xFF);
                                var11 = (255 & aByteArray2040[1]) + ((aByteArray2040[0] & 0xFF) << 8);
                                int var13 = 255 & aByteArray2040[7];
                                int var12 = (aByteArray2040[3] & 0xFF) + (aByteArray2040[2] << 8 & 65280);
                                if (var11 != var3 || var12 != var9 || var13 != this.cacheIndex) {
                                    return false;
                                }

                                if (var10 < 0 || this.aClass30_681.method976(0) / 520L < (long) var10) {
                                    return false;
                                }
                            }

                            var11 = -var8 + var2;
                            if (var10 == 0) {
                                var5 = false;
                                var10 = (int) ((this.aClass30_681.method976(0) - -519L) / 520L);
                                if (var10 == 0) {
                                    ++var10;
                                }

                                if (var7 == var10) {
                                    ++var10;
                                }
                            }

                            aByteArray2040[7] = (byte) this.cacheIndex;
                            aByteArray2040[0] = (byte) (var3 >> 8);
                            if (-var8 + var2 <= 512) {
                                var10 = 0;
                            }

                            aByteArray2040[4] = (byte) (var10 >> 16);
                            if (var11 > 512) {
                                var11 = 512;
                            }

                            aByteArray2040[1] = (byte) var3;
                            aByteArray2040[6] = (byte) var10;
                            aByteArray2040[2] = (byte) (var9 >> 8);
                            aByteArray2040[3] = (byte) var9;
                            ++var9;
                            aByteArray2040[5] = (byte) (var10 >> 8);
                            this.aClass30_681.method984(var1 + -128, var7 * 520);
                            var7 = var10;
                            this.aClass30_681.method983(aByteArray2040, 0, -903171152, 8);
                            this.aClass30_681.method983(var4, var8, -903171152, var11);
                            var8 += var11;
                            continue;
                        }
                    }

                    return true;
                }
            } catch (IOException var16) {
                return false;
            }
        }
    }

}
