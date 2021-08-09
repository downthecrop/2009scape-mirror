package org.runite.client;

import org.rs09.client.Linkable;

final class Class3_Sub6 extends Linkable {


    static byte[][] softReferenceTestArray;
    static int[] expectedMinimumValues = new int[32];
    static int anInt2291;

    static {
        int accumulator = 2;

        for (int i = 0; i < 32; ++i) {
            expectedMinimumValues[i] = -1 + accumulator;
            accumulator += accumulator;
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
