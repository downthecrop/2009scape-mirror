package org.runite.client;

import org.rs09.client.net.Connection;

import java.awt.Component;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public final class Class163_Sub1_Sub1 extends Class163_Sub1 {

    static byte[] aByteArray4005 = new byte[]{(byte) 95, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57};
    static int anInt4006;
    public static boolean[] aBooleanArray4008 = new boolean[100];
    static int[] anIntArray4009 = new int[5];
    static int[][] anIntArrayArray4010 = new int[104][104];
    static int anInt2246 = 0;
    public static int adminLoginStage = 0;
    static int anInt1616 = 0;

    static void method2214() {
        try {

            if (NPC.method1986(90) || WorldListCountry.localPlane == Class140_Sub3.anInt2745) {
                if (Class58.anInt909 != WorldListCountry.localPlane && Class3_Sub19.method385(WorldListCountry.localPlane)) {
                    Class58.anInt909 = WorldListCountry.localPlane;
                    Unsorted.method792();
                }

            } else {
                Unsorted.method1301(WorldListCountry.localPlane, Class3_Sub7.anInt2294, Unsorted.anInt3606, Class102.player.anIntArray2755[0], false, Class102.player.anIntArray2767[0]);
            }
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "ch.E(" + 0 + ')');
        }
    }

    static void method2215(Component var0) {
        try {
            var0.removeKeyListener(TextureOperation33.aClass148_3049);
            var0.removeFocusListener(TextureOperation33.aClass148_3049);
            KeyboardListener.anInt2384 = -1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ch.G(" + (var0 != null ? "{...}" : "null") + ',' + -9320 + ')');
        }
    }

    static void method2216() {
        try {

            if (adminLoginStage != 0) {
                try {
                    if (1500 < ++anInt2246) {
                        if (null != Class3_Sub15.activeConnection) {
                            Class3_Sub15.activeConnection.close();
                            Class3_Sub15.activeConnection = null;
                        }

                        if (1 <= anInt1616) {
                            Client.messageToDisplay = -5;
                            adminLoginStage = 0;
                            return;
                        }

                        anInt2246 = 0;
                        ++anInt1616;
                        adminLoginStage = 1;
                        if (Class123.anInt1658 == Client.anInt3773) {
                            Class123.anInt1658 = Class53.anInt867;
                        } else {
                            Class123.anInt1658 = Client.anInt3773;
                        }
                    }

                    if (adminLoginStage == 1) {
                        Class3_Sub9.aClass64_2318 = Class38.gameSignlink.method1441((byte) 8, ClientErrorException.worldListHost, Class123.anInt1658);
                        adminLoginStage = 2;
                    }

                    int var1;
                    if (adminLoginStage == 2) {
                        if (Objects.requireNonNull(Class3_Sub9.aClass64_2318).anInt978 == 2) {
                            throw new IOException();
                        }

                        if (1 != Class3_Sub9.aClass64_2318.anInt978) {
                            return;
                        }

                        Class3_Sub15.activeConnection = new Connection((Socket) Class3_Sub9.aClass64_2318.anObject974, Class38.gameSignlink);
                        Class3_Sub9.aClass64_2318 = null;
                        Class3_Sub15.activeConnection.sendBytes(TextureOperation12.outgoingBuffer.buffer, TextureOperation12.outgoingBuffer.index);
                        if (WorldListEntry.aAudioChannel_2627 != null) {
                            WorldListEntry.aAudioChannel_2627.method2159();
                        }
                        if (null != Class3_Sub21.aAudioChannel_2491) {
                            Class3_Sub21.aAudioChannel_2491.method2159();
                        }

                        var1 = Class3_Sub15.activeConnection.readByte();
                        if (WorldListEntry.aAudioChannel_2627 != null) {
                            WorldListEntry.aAudioChannel_2627.method2159();
                        }

                        if (Class3_Sub21.aAudioChannel_2491 != null) {
                            Class3_Sub21.aAudioChannel_2491.method2159();
                        }

                        if (var1 != 101) {
                            Client.messageToDisplay = var1;
                            adminLoginStage = 0;
                            Class3_Sub15.activeConnection.close();
                            Class3_Sub15.activeConnection = null;
                            return;
                        }

                        adminLoginStage = 3;
                    }

                    if (adminLoginStage == 3) {
                        if (Class3_Sub15.activeConnection.availableBytes() < 2) {
                            return;
                        }

                        var1 = Class3_Sub15.activeConnection.readByte() << 8 | Class3_Sub15.activeConnection.readByte();
                        WaterfallShader.method1627(var1, (byte) -16);
                        if (CS2Script.userCurrentWorldID == -1) {
                            adminLoginStage = 0;
                            Client.messageToDisplay = 6;
                            Class3_Sub15.activeConnection.close();
                            Class3_Sub15.activeConnection = null;
                            return;
                        }

                        adminLoginStage = 0;
                        Class3_Sub15.activeConnection.close();
                        Class3_Sub15.activeConnection = null;
                        Class24.method951();
                        return;
                    }
                } catch (IOException var2) {
                    if (null != Class3_Sub15.activeConnection) {
                        Class3_Sub15.activeConnection.close();
                        Class3_Sub15.activeConnection = null;
                    }

                    if (anInt1616 < 1) {
                        if (Class123.anInt1658 == Client.anInt3773) {
                            Class123.anInt1658 = Class53.anInt867;
                        } else {
                            Class123.anInt1658 = Client.anInt3773;
                        }

                        adminLoginStage = 1;
                        anInt2246 = 0;
                        ++anInt1616;
                    } else {
                        Client.messageToDisplay = -4;
                        adminLoginStage = 0;
                    }
                }

            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ch.D(" + (byte) 81 + ')');
        }
    }

}
