package org.runite.client;

import org.rs09.client.config.GameConfig;
import org.rs09.client.rendering.Toolkit;


final class ISAACCipher {

    private int anInt966;
    private int anInt967;
    private int anInt968;
    private int[] anIntArray970;
    private int[] anIntArray971;
    private int anInt972;


    private void method1227() {
        try {

            int var10 = -1640531527;
            int var9 = -1640531527;
            int var8 = -1640531527;
            int var7 = -1640531527;
            int var6 = -1640531527;
            int var5 = -1640531527;
            int var4 = -1640531527;
            int var3 = -1640531527;

            int var2;
            for (var2 = 0; 4 > var2; ++var2) {
                var3 ^= var4 << 11;
                var6 += var3;
                var4 += var5;
                var4 ^= var5 >>> 2;
                var5 += var6;
                var5 ^= var6 << 8;
                var8 += var5;
                var7 += var4;
                var6 += var7;
                var6 ^= var7 >>> 16;
                var7 += var8;
                var9 += var6;
                var7 ^= var8 << 10;
                var10 += var7;
                var8 += var9;
                var8 ^= var9 >>> 4;
                var9 += var10;
                var9 ^= var10 << 8;
                var4 += var9;
                var3 += var8;
                var10 += var3;
                var10 ^= var3 >>> 9;
                var5 += var10;
                var3 += var4;
            }

            for (var2 = 0; var2 < 256; var2 += 8) {
                var6 += this.anIntArray970[3 + var2];
                var7 += this.anIntArray970[var2 - -4];
                var9 += this.anIntArray970[var2 + 6];
                var3 += this.anIntArray970[var2];
                var5 += this.anIntArray970[2 + var2];
                var8 += this.anIntArray970[var2 - -5];
                var10 += this.anIntArray970[7 + var2];
                var4 += this.anIntArray970[var2 - -1];
                var3 ^= var4 << 11;
                var4 += var5;
                var4 ^= var5 >>> 2;
                var6 += var3;
                var5 += var6;
                var5 ^= var6 << 8;
                var7 += var4;
                var6 += var7;
                var6 ^= var7 >>> 16;
                var8 += var5;
                var7 += var8;
                var7 ^= var8 << 10;
                var10 += var7;
                var9 += var6;
                var8 += var9;
                var8 ^= var9 >>> 4;
                var9 += var10;
                var9 ^= var10 << 8;
                var3 += var8;
                var10 += var3;
                var4 += var9;
                var10 ^= var3 >>> 9;
                var3 += var4;
                this.anIntArray971[var2] = var3;
                this.anIntArray971[1 + var2] = var4;
                var5 += var10;
                this.anIntArray971[2 + var2] = var5;
                this.anIntArray971[var2 + 3] = var6;
                this.anIntArray971[4 + var2] = var7;
                this.anIntArray971[5 + var2] = var8;
                this.anIntArray971[var2 - -6] = var9;
                this.anIntArray971[7 + var2] = var10;
            }

            for (var2 = 0; var2 < 256; var2 += 8) {
                var9 += this.anIntArray971[6 + var2];
                var8 += this.anIntArray971[var2 + 5];
                var7 += this.anIntArray971[4 + var2];
                var4 += this.anIntArray971[var2 + 1];
                var5 += this.anIntArray971[2 + var2];
                var3 += this.anIntArray971[var2];
                var3 ^= var4 << 11;
                var6 += this.anIntArray971[var2 + 3];
                var10 += this.anIntArray971[var2 + 7];
                var6 += var3;
                var4 += var5;
                var4 ^= var5 >>> 2;
                var5 += var6;
                var7 += var4;
                var5 ^= var6 << 8;
                var8 += var5;
                var6 += var7;
                var6 ^= var7 >>> 16;
                var7 += var8;
                var7 ^= var8 << 10;
                var9 += var6;
                var8 += var9;
                var8 ^= var9 >>> 4;
                var10 += var7;
                var9 += var10;
                var3 += var8;
                var9 ^= var10 << 8;
                var4 += var9;
                var10 += var3;
                var10 ^= var3 >>> 9;
                var5 += var10;
                var3 += var4;
                this.anIntArray971[var2] = var3;
                this.anIntArray971[1 + var2] = var4;
                this.anIntArray971[var2 - -2] = var5;
                this.anIntArray971[3 + var2] = var6;
                this.anIntArray971[4 + var2] = var7;
                this.anIntArray971[5 + var2] = var8;
                this.anIntArray971[var2 + 6] = var9;
                this.anIntArray971[var2 - -7] = var10;
            }

            this.method1229();
            this.anInt968 = 256;
        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "ij.E(" + true + ')');
        }
    }

    final int nextOpcode() {
        try {
            if (this.anInt968-- == 0) {
                this.method1229();
                this.anInt968 = 255;
            }
            return GameConfig.ISAAC ? this.anIntArray970[this.anInt968] : 0;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ij.C(" + ')');
        }
    }

    private void method1229() {
        try {
            this.anInt967 += ++this.anInt966;
            int var2 = 0;
            while (var2 < 256) {
                int var3 = this.anIntArray971[var2];
                if ((2 & var2) != 0) {
                    if ((var2 & 1) == 0) {
                        this.anInt972 ^= this.anInt972 << 2;
                    } else {
                        this.anInt972 ^= this.anInt972 >>> 16;
                    }
                } else if ((var2 & 1) == 0) {
                    this.anInt972 ^= this.anInt972 << 13;
                } else {
                    this.anInt972 ^= this.anInt972 >>> 6;
                }

                this.anInt972 += this.anIntArray971[128 + var2 & 255];
                int var4;
                this.anIntArray971[var2] = var4 = this.anInt967 + this.anInt972 + this.anIntArray971[Unsorted.bitwiseAnd(var3, 1020) >> 2];
                this.anIntArray970[var2] = this.anInt967 = var3 + this.anIntArray971[Unsorted.bitwiseAnd(261347, var4) >> 8 >> 2];
                ++var2;
            }

        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ij.A(" + ')');
        }
    }

    ISAACCipher(int[] var1) {
        try {
            this.anIntArray970 = new int[256];
            this.anIntArray971 = new int[256];

            System.arraycopy(var1, 0, this.anIntArray970, 0, var1.length);

            this.method1227();
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ij.<init>(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

}
