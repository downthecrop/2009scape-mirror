package org.runite.client;

public class CS2Methods {
    static QuickChat aQuickChat_1056;

    static RSString method27(RSString var0) {
        try {

            int var2 = Unsorted.method1602(var0);
            return var2 != -1 ? Class119.aClass131_1624.aClass94Array1721[var2].method1560(RSString.parse(" "), TextCore.aClass94_4066) : TextCore.aClass94_4049;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rc.V(" + (var0 != null ? "{...}" : "null") + ',' + true + ')');
        }
    }

    static void method28() {
        try {
            Class143.aReferenceCache_1874.clear();

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rc.Q(" + true + ')');
        }
    }

    static void method852(int var1) {
        try {
            Class3_Sub25 var2 = (Class3_Sub25) Class3_Sub2.aHashTable_2220.get(var1);
            if (var2 != null) {

                for (int var3 = 0; var2.anIntArray2547.length > var3; ++var3) {
                    var2.anIntArray2547[var3] = -1;
                    var2.anIntArray2551[var3] = 0;
                }

            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "bd.C(" + (byte) 114 + ',' + var1 + ')');
        }
    }
}
