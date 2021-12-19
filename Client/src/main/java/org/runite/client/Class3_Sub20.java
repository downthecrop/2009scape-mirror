package org.runite.client;

import org.rs09.client.Linkable;
import org.rs09.client.data.NodeCache;

final class Class3_Sub20 extends Linkable {

    static int[] anIntArray2480 = new int[25];
    static RSString aString_3220;
    static NodeCache aClass47_3801 = new NodeCache(4);

    int anInt2483;
    static int wlPacketIndex = 0;
    static int paramLanguage = 0;
    static int anInt2487;
    static int anInt2488 = 0;
    int anInt2489;

    static void method388(byte var0) {
        try {
            //int var1 = 44 / ((-2 - var0) / 52);
            if (aString_3220 != null) {
                Class3_Sub10.method138(aString_3220);
                aString_3220 = null;
            }

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "lb.A(" + var0 + ')');
        }
    }

    static void method389() {
        try {
            Class32.method995();
            Class49.aAbstractSprite_812 = null;
            Class58.anInt909 = -1;
            TextureOperation13.method313((byte) 55);
            aClass47_3801.clear();
            ObjectDefinition.aClass136_1413 = new Class136();
            ((Class102) Class51.anInterface2_838).method1618();
            Class68.anInt1032 = 0;
            Class68.aClass43Array1021 = new Class43[255];
            Class140_Sub1_Sub1.method1929();
            Class141.method2043();
            Class65.method1240();
            Unsorted.method1250(17, false);
            TextureOperation23.method247((byte) 51);

            for (int var1 = 0; 2048 > var1; ++var1) {
                Player var2 = Unsorted.players[var1];
                if (null != var2) {
                    var2.anObject2796 = null;
                }
            }

            if (HDToolKit.highDetail) {
                Class141.method2041();
                Class127_Sub1.method1755();
            }

            Class3_Sub28_Sub9.method581(CacheIndex.fontsIndex, CacheIndex.spritesIndex);
            Sprites.method887(CacheIndex.spritesIndex);
            Class3_Sub26.aAbstractSprite_2560 = null;
            WorldListEntry.aAbstractSprite_3099 = null;
            Class50.aAbstractSprite_824 = null;
            WorldListEntry.aAbstractSprite_1339 = null;
            WorldListEntry.aAbstractSprite_1457 = null;
            if (Class143.gameStage == 5) {
                Class108.method1656(CacheIndex.spritesIndex, (byte) -60);
            }

            if (10 == Class143.gameStage) {
                TextureOperation1.method219(false);
            }

            if (Class143.gameStage == 30) {
                Class117.method1719(25);
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "lb.C(" + false + ')');
        }
    }

    static void method390(boolean var0, int var1, int var2, int var3, byte var4, int var5, int var6) {
        try {
            Unsorted.anInt3631 = var3;
            TextureOperation25.anInt3414 = var2;
            Unsorted.anInt30 = var6;
            Class163_Sub2_Sub1.anInt4021 = var1;
            Class146.anInt1904 = var5;
            if (var0 && Unsorted.anInt3631 >= 100) {
                NPC.anInt3995 = 128 * Unsorted.anInt30 + 64;
                Class77.anInt1111 = 128 * Class146.anInt1904 + 64;
                Class7.anInt2162 = Scenery.sceneryPositionHash(WorldListCountry.localPlane, 1, NPC.anInt3995, Class77.anInt1111) + -TextureOperation25.anInt3414;
            }

            // int var7 = 76 % ((-79 - var4) / 35);
            Class133.anInt1753 = 2;
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "lb.D(" + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    Class3_Sub20(int var1, int var2) {
        try {
            this.anInt2483 = var2;
            this.anInt2489 = var1;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "lb.<init>(" + var1 + ',' + var2 + ')');
        }
    }

    static void method392(CacheIndex skinsIndex, CacheIndex animationIndex, CacheIndex skeletonsIndex) {
        try {
            Class7.skinsReferenceIndex = skinsIndex;
            SequenceDefinition.animationReferenceIndex = animationIndex;
            Class131.skeletonsReferenceIndex = skeletonsIndex;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "lb.E(" + (skinsIndex != null ? "{...}" : "null") + ',' + (animationIndex != null ? "{...}" : "null") + ',' + -77 + ',' + (skeletonsIndex != null ? "{...}" : "null") + ')');
        }
    }

}
