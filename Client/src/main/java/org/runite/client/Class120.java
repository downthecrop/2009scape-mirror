package org.runite.client;

final class Class120 {

    static long[] aLongArray1631 = new long[256];
    static int[] anIntArray1638;

    static {
        for (int var2 = 0; 256 > var2; ++var2) {
            long var0 = var2;

            for (int var3 = 0; 8 > var3; ++var3) {
                if ((1L & var0) == 1) {
                    var0 = var0 >>> 1 ^ -3932672073523589310L;
                } else {
                    var0 >>>= 1;
                }
            }

            aLongArray1631[var2] = var0;
        }

        anIntArray1638 = new int[128];
    }

    int anInt1632;
    int anInt1634;
    int anInt1635;

    static RSString method1732(RSInterface var0, byte var1, int var2) {
        try {
            if (var1 >= -8) {
                anIntArray1638 = null;
            }

            return !Client.method44(var0).method92(var2, (byte) -110) && var0.anObjectArray314 == null ? null : (null != var0.aStringArray171 && var0.aStringArray171.length > var2 && var0.aStringArray171[var2] != null && var0.aStringArray171[var2].trim(1).length() != 0 ? var0.aStringArray171[var2] : (!ClientCommands.commandQaOpEnabled ? null : RSString.stringCombiner(new RSString[]{RSString.parse("Hidden)2"), RSString.stringAnimator(var2)})));
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "qj.B(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ',' + var2 + ')');
        }
    }
}
