package org.runite.client;

import org.rs09.client.config.GameConfig;

final class Class132 {

    static int anInt1734 = 0;
    static RSString[] aStringArray1739 = new RSString[1000];
    static int anInt1741;


    static void method1799(byte var0, CacheIndex var1) {
        try {
            TextureOperation27.configurationsIndex_3098 = var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sf.C(" + var0 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static void method1801() {
        try {
            int var1 = FontType.bold.method682(RSString.parse(GameConfig.RCM_TITLE));
            int var2;
            int var3;
            for (var2 = 0; Unsorted.menuOptionCount > var2; ++var2) {
                var3 = FontType.bold.method682(Unsorted.method802(var2));
                if (var3 > var1) {
                    var1 = var3;
                }
            }
            var2 = 15 * Unsorted.menuOptionCount + 21;
            int var4 = Class38_Sub1.anInt2612;
            var1 += 8;
            var3 = NPCDefinition.anInt1297 + -(var1 / 2);
            if (GroundItem.canvasHeight < var4 + var2) {
                var4 = GroundItem.canvasHeight + -var2;
            }

            if (Class23.canvasWidth < var3 + var1) {
                var3 = -var1 + Class23.canvasWidth;
            }

            if (var3 < 0) {
                var3 = 0;
            }

            if (var4 < 0) {
                var4 = 0;
            }

            if (Unsorted.anInt3660 == 1) {
                if (TextureOperation8.anInt3460 == NPCDefinition.anInt1297 && Class38_Sub1.anInt2612 == Unsorted.anInt2099) {
                    Class21.anInt3537 = Unsorted.menuOptionCount * 15 - -(!Unsorted.aBoolean1951 ? 22 : 26);
                    Unsorted.anInt3660 = 0;
                    Class21.anInt3395 = var4;
                    Class21.anInt1462 = var3;
                    Class38_Sub1.aBoolean2615 = true;
                    Class21.anInt3552 = var1;
                }
            } else if (Class163_Sub1.anInt2993 == NPCDefinition.anInt1297 && Class38_Sub1.anInt2614 == Class38_Sub1.anInt2612) {
                Class21.anInt1462 = var3;
                Unsorted.anInt3660 = 0;
                Class21.anInt3552 = var1;
                Class21.anInt3395 = var4;
                Class21.anInt3537 = (Unsorted.aBoolean1951 ? 26 : 22) + Unsorted.menuOptionCount * 15;
                Class38_Sub1.aBoolean2615 = true;
            } else {
                Unsorted.anInt2099 = Class38_Sub1.anInt2614;
                TextureOperation8.anInt3460 = Class163_Sub1.anInt2993;
                Unsorted.anInt3660 = 1;
            }

        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "sf.D(" + ')');
        }
    }

}
