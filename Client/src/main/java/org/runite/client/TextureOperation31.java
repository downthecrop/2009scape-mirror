package org.runite.client;

import org.rs09.client.config.GameConfig;

import java.math.BigInteger;

final class TextureOperation31 extends TextureOperation {

    static CacheResourceWorker aCacheResourceWorker_3159;
    private int anInt3160 = 0;
    static BigInteger MODULUS = GameConfig.MODULUS;
    private int anInt3163 = 20;
    private int anInt3164 = 1365;
    private int anInt3165 = 0;
    static boolean aBoolean3166 = false;

    final void decode(int var1, DataBuffer var2) {
        try {
            if (var1 == 0) {
                this.anInt3164 = var2.readUnsignedShort();
            } else if (var1 == 1) {
                this.anInt3163 = var2.readUnsignedShort();
            } else if (var1 == 2) {
                this.anInt3160 = var2.readUnsignedShort();
            } else if (var1 == 3) {
                this.anInt3165 = var2.readUnsignedShort();
            }

        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "gm.A(" + var1 + ',' + (var2 != null ? "{...}" : "null") + ',' + true + ')');
        }
    }

    static void method236() {
        try {
            TextureOperation14.aBoolean3387 = true;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "gm.C(" + (byte) 64 + ')');
        }
    }

    final int[] method154(int var1, byte var2) {
        try {
            int[] var3 = this.aClass114_2382.method1709(var1);
            if (this.aClass114_2382.aBoolean1580) {
                for (int var5 = 0; var5 < Class113.anInt1559; ++var5) {
                    int var7 = this.anInt3165 + (Class163_Sub3.anIntArray2999[var1] << 12) / this.anInt3164;
                    int var6 = this.anInt3160 + (Class102.anIntArray2125[var5] << 12) / this.anInt3164;
                    int var10 = var6;
                    int var11 = var7;
                    int var14 = 0;
                    int var12 = var6 * var6 >> 12;

                    for (int var13 = var7 * var7 >> 12; var12 - -var13 < 16384 && var14 < this.anInt3163; var12 = var10 * var10 >> 12) {
                        var11 = (var10 * var11 >> 12) * 2 + var7;
                        ++var14;
                        var10 = var12 + -var13 + var6;
                        var13 = var11 * var11 >> 12;
                    }

                    var3[var5] = this.anInt3163 + -1 <= var14 ? 0 : (var14 << 12) / this.anInt3163;
                }
            }

            return var3;
        } catch (RuntimeException var15) {
            throw ClientErrorException.clientError(var15, "gm.D(" + var1 + ',' + var2 + ')');
        }
    }

    public TextureOperation31() {
        super(0, true);
    }

}
