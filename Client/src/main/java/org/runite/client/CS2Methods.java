package org.runite.client;

public class CS2Methods {
    static RSString method27(RSString var0) {
        try {

            int var2 = Unsorted.method1602(var0);
            return var2 != -1 ? Class119.aClass131_1624.aClass94Array1721[var2].method1560(TextCore.aClass94_3192, TextCore.aClass94_4066) : TextCore.aClass94_4049;
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
}
