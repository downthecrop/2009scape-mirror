package org.runite.client.drawcalls;

import org.runite.client.*;

public final class Compass {

    public static void drawCompass(int var0, int var1, RSInterface var2, int var3) {
        if (HDToolKit.highDetail) {
            Class22.setClipping(var0, var1, var2.width + var0, var2.height + var1);
        }

        if (Class161.anInt2028 >= 3) {
            if (HDToolKit.highDetail) {
                AbstractSprite var5 = var2.method866(false);
                if (null != var5) {
                    var5.drawAt(var0, var1);
                }
            } else {
                Class74.method1332(var0, var1, var2.anIntArray207, var2.anIntArray291);
            }
        } else if (HDToolKit.highDetail) {
            ((HDSprite) Class57.aAbstractSprite_895).drawMinimapRegion(var0, var1, var2.width, var2.height, Class57.aAbstractSprite_895.width / 2, Class57.aAbstractSprite_895.height / 2, GraphicDefinition.CAMERA_DIRECTION, 256, (HDSprite) var2.method866(false));
        } else {
            ((SoftwareSprite) Class57.aAbstractSprite_895).method667(var0, var1, var2.width, var2.height, Class57.aAbstractSprite_895.width / 2, Class57.aAbstractSprite_895.height / 2, GraphicDefinition.CAMERA_DIRECTION, var2.anIntArray207, var2.anIntArray291);
        }

        Class163_Sub1_Sub1.aBooleanArray4008[var3] = true;
    }
}
