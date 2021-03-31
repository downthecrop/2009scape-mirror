package org.runite.client;


import java.awt.*;

public final class Class58 {

    static int anInt909 = -1;
    static int[][][] anIntArrayArrayArray911 = new int[2][][];
    static int[][][] anIntArrayArrayArray914;
    static Interface4 anInterface4_915 = null;
    static int anInt916;
    static Js5Worker aJs5Worker_917;


    public static void method1194() {
        try {
            if (null != Class3_Sub21.aAudioChannel_2491) {
                Class3_Sub21.aAudioChannel_2491.method2153();
            }

            if (null != WorldListEntry.aAudioChannel_2627) {
                WorldListEntry.aAudioChannel_2627.method2153();
            }

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "id.A(" + -16385 + ')');
        }
    }

    static void method1196(int var3, int var4) {
        try {
            Class3_Sub28_Sub18.anInt3765 = var4;

            Class101.anInt1425 = 0;
            Class159.anInt2020 = 0;
            Class57.anInt902 = var3;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "id.B(" + 0 + ',' + 0 + ',' + (byte) 111 + ',' + var3 + ',' + var4 + ')');
        }
    }

    static void method1197(CacheIndex var0) {
        try {

            Class46.aClass153_737 = var0;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "id.E(" + (var0 != null ? "{...}" : "null") + ',' + (byte) 69 + ')');
        }
    }

}
