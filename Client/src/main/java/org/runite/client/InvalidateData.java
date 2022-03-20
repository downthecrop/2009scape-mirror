package org.runite.client;

public class InvalidateData {
    static void method165() {
        try {
            WorldListEntry.aAbstractSprite_1339 = null;
            WorldListEntry.aAbstractSprite_3099 = null;
            Class50.aAbstractSprite_824 = null;

            WorldListEntry.aAbstractSprite_1457 = null;
            Class3_Sub26.aAbstractSprite_2560 = null;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "j.VA(" + -7878 + ')');
        }
    }
}
