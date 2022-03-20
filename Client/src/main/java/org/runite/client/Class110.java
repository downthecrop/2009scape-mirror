package org.runite.client;


import java.util.Objects;

final class Class110 {

    static int anInt1472 = 0;


    static void method1681(int var0) {
        try {
            if (LoginHandler.loginStage == 5) {
                if (var0 != -1) {
                    TextCore.COMMAND_BREAK_JS5_SERVER_CONNECTION = null;
                }

                LoginHandler.loginStage = 6;
            }
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "p.A(" + var0 + ')');
        }
    }
}
