package org.runite.client;

import org.rs09.client.Linkable;

final class Class3_Sub6 extends Linkable {


    static byte[][] aByteArrayArray2287;
    static int[] anIntArray2288 = new int[32];
    static int anInt2291;

    static {
        int var0 = 2;

        for (int var1 = 0; var1 < 32; ++var1) {
            anIntArray2288[var1] = -1 + var0;
            var0 += var0;
        }

        anInt2291 = 1;
    }

    byte[] aByteArray2289;

    Class3_Sub6(byte[] var1) {
        try {
            this.aByteArray2289 = var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ea.<init>(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }
}
