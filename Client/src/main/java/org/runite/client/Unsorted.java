package org.runite.client;

import org.rs09.SlayerTracker;
import org.rs09.XPGainDraw;
import org.rs09.client.config.GameConfig;
import org.rs09.client.console.DeveloperConsole;
import org.rs09.client.data.HashTable;
import org.rs09.client.data.ReferenceCache;
import org.rs09.client.net.Connection;
import org.rs09.client.rendering.Toolkit;
import org.rs09.client.util.ArrayUtils;
import org.runite.client.drawcalls.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Unsorted {
    public static boolean aBoolean2146 = false;
    public static int incomingOpcode = 0;
    public static int anInt2148 = 0;
    public static boolean aBoolean2154;
    public static int[] anIntArray882 = new int[200];
    public static int anInt144;
    public static int anInt136 = 0;
    public static byte[][][] aByteArrayArrayArray113 = new byte[4][104][104];
    public static int[] anIntArray39;
    public static int anInt40 = 0;
    public static Connection js5Connection;
    public static int anInt42 = 0;
    public static RSString[] aClass94Array45;
    public static short aShort46 = 256;
    public static boolean aBoolean47 = false;
    public static int anInt48 = 2;
    public static int[] anIntArray49;
    public static int anInt1234;
    public static Class33 aClass33_1238;
    public static LinkedList aLinkedList_1242 = new LinkedList();
    public static int anInt1244 = -1;
    public static RSInterface aClass11_1933;
    public static float[] aFloatArray1934 = new float[]{0.073F, 0.169F, 0.24F, 1.0F};
    public static Class8 aClass8_1936;
    public static int[] anIntArray2952 = new int[128];
    public static int anInt2958 = 0;
    public static LinkedList aLinkedList_78 = new LinkedList();
    public static int anInt72 = 0;
    public static boolean[] aBooleanArray73 = new boolean[200];
    public static boolean[] aBooleanArray1703;
    public static int incomingPacketLength = 0;
    public static int anInt1705 = 0;
    public static int anInt1709 = 0;
    public static int anInt1711 = -2;
    public static boolean[] aBooleanArray1712 = new boolean[100];
    public static ReferenceCache aReferenceCache_1131 = new ReferenceCache(5);
    public static ReferenceCache aReferenceCache_1135 = new ReferenceCache(4);
    public static AbstractSprite[] aAbstractSpriteArray1136;
    public static int anInt1137 = 2;
    public static int[] anIntArray1138;
    public static int anInt3625 = 3;
    public static int anInt3631;
    public static Class25[] aClass25Array4060;
    public static int anInt4062 = 0;
    public static boolean aBoolean4063 = false;
    public static boolean aBoolean4068 = true;
    public static Class3_Sub2[][][] aClass3_Sub2ArrayArrayArray4070;
    public static int anInt4073;
    public static CacheIndex aClass153_332;
    public static int[] anIntArray2591;
    public static RSString[] aClass94Array2596 = null;
    public static Class3_Sub28_Sub3 aClass3_Sub28_Sub3_2600;
    public static Class151_Sub1[] aClass151_Sub1Array2601 = new Class151_Sub1[29]; //TODO
    public static boolean aBoolean1080 = false;
    public static int anInt1081 = 0;
    public static int anInt1082;
    public static int[] anIntArray1083;
    public static boolean aBoolean1084 = false;
    public static int anInt1088 = 0;
    public static int anInt1950;
    public static boolean aBoolean1951 = false;
    public static int anInt1953;
    public static int anInt1881 = 0;
    public static int anInt1887;
    public static int anInt14 = 0;
    public static int anInt15 = 0;
    public static ReferenceCache aReferenceCache_21 = new ReferenceCache(64);
    public static int registryStage = 0;
    public static int anInt25 = 1;
    public static int anInt3657;
    public static int anInt3660 = 0;
    public static boolean aBoolean3665 = true;
    public static LinkedList aLinkedList_2468 = new LinkedList();
    public static int[] anIntArray2469;
    public static int[] anIntArray2470 = new int[]{0, 0, 2, 0, 0, 2, 1, 1, 0};
    public static boolean paramJavaScriptEnabled = false;
    public static int anInt3642 = 0;
    public static int anInt3644 = 0;
    public static int anInt59 = 0;
    public static int anInt65;
    public static boolean isMember = false;
    public static int anInt67;
    public static ShaderInterface[] anShaderInterfaceArray70;
    public static boolean aBoolean742 = false;
    public static ReferenceCache aReferenceCache_743 = new ReferenceCache(20);
    public static CacheIndex quickChatMessages;
    public static byte[][][] aByteArrayArrayArray1328;
    public static int anInt3602;
    public static int anInt3603;
    public static boolean aBoolean3604 = true;
    public static int[][][] anIntArrayArrayArray3605;
    public static int anInt3606;
    public static int[] anIntArray3607 = new int[]{0, 2, 2, 2, 1, 1, 2, 2, 1, 3, 1, 1};
    public static ReferenceCache aReferenceCache_4043 = new ReferenceCache(64);
    public static volatile int anInt4045 = 0;
    public static HashTable aHashTable_4046 = new HashTable(16);
    public static MouseListeningClass aClass149_4047 = new MouseListeningClass();
    public static CacheIndex aClass153_4048;
    public static int[] anIntArray4050 = new int[1000];
    public static ReferenceCache aReferenceCache_4051 = new ReferenceCache(30);
    public static int gameWindowWidth = 0;
    public static Player[] players = new Player[2048];
    static int anInt942;
    static int anInt944 = 0;
    static int anInt952;
    static int anInt963;
    static int anInt1037;
    static int anInt1038;
    static Class30 aClass30_1039;
    static int anInt3695;
    static int anInt1042;
    static CacheIndex aClass153_1043;
    static AbstractSprite[] aAbstractSpriteArray996;
    static int maskUpdateCount = 0;
    static int anInt998 = 0;
    static int anInt999 = -1;
    static int wlPacketSize = 0;
    static ReferenceCache aReferenceCache_684 = new ReferenceCache(64);
    static int[] anIntArray686 = new int[2];
    static int anInt688 = 0;
    static int anInt689;
    static RSString[] aClass94Array2566 = new RSString[200];
    static int anInt2567 = -1;
    public static int clanSize;
    static int[] anIntArray2574 = new int[14];
    static int anInt2577 = 0;
    static boolean aBoolean29 = false;
    static int anInt30;
    static int anInt31 = 0;
    static int anInt3671 = 0;
    static boolean[] aBooleanArray3674 = new boolean[100];
    static int anInt2281 = 0;
    static int anInt2309 = 128;
    static boolean aBoolean2311;
    static int frameWidth;
    static int anInt3071 = 0;
    static int anInt3070 = 0;
    static int[] anIntArray3076;
    static int anInt3155;
    static WorldListEntry[] aClass44_Sub1Array3201 = new WorldListEntry[0];
    static CacheIndex aClass153_3361;
    static Class52 aClass52_1112 = new Class52();
    static int anInt1165 = -1;
    static byte[][][] aByteArrayArrayArray1774;
    static int[][] anIntArrayArray2039 = new int[][]{{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3}, {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0}, {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}};
    public static int menuOptionCount = 0;
    static boolean aBoolean11 = false;
    public static Class158 aClass158_3009;
    static long[] aLongArray3271 = new long[500];
    static boolean aBoolean3275 = true;
    static int anInt154 = 0;
    static RSInterface aClass11_2091 = null;
    static int anInt2099 = 0;

    static void method2086() {
        try {
            int var1 = Class102.player.xAxis + TextureOperation20.anInt3216;
            int var2 = Class102.player.zAxis - -anInt42;
            if (-var1 + anInt3155 < -500 || -var1 + anInt3155 > 500 || anInt942 + -var2 < -500 || -var2 + anInt942 > 500) {
                anInt3155 = var1;
                anInt942 = var2;
            }

            if (var2 != anInt942) {
                anInt942 += (-anInt942 + var2) / 16;
            }

            if (var1 != anInt3155) {
                anInt3155 += (-anInt3155 + var1) / 16;
            }

            if (AudioThread.aBoolean346) {
                for (int var3 = 0; var3 < Class3_Sub23.anInt2537; ++var3) {
                    int var4 = Class133.inputTextCodeArray[var3];
                    if (98 == var4) {
                        anInt2309 = -16 & anInt2309 + 47;
                    } else if (var4 == 99) {
                        anInt2309 = -16 & anInt2309 - 17;
                    } else if (var4 == 96) {
                        GraphicDefinition.CAMERA_DIRECTION = GraphicDefinition.CAMERA_DIRECTION - 65 & -128;
                    } else if (var4 == 97) {
                        GraphicDefinition.CAMERA_DIRECTION = GraphicDefinition.CAMERA_DIRECTION + 191 & -128;
                    }
                }
            } else {
                if (ObjectDefinition.aBooleanArray1490[98]) {
                    Class27.anInt517 += (-Class27.anInt517 + 12) / 2;
                } else if (!ObjectDefinition.aBooleanArray1490[99]) {
                    Class27.anInt517 /= 2;
                } else {
                    Class27.anInt517 += (-Class27.anInt517 + -12) / 2;
                }

                if (!ObjectDefinition.aBooleanArray1490[96]) {
                    if (ObjectDefinition.aBooleanArray1490[97]) {
                        anInt2281 += (-anInt2281 + 24) / 2;
                    } else {
                        anInt2281 /= 2;
                    }
                } else {
                    anInt2281 += (-anInt2281 + -24) / 2;
                }

                anInt2309 += Class27.anInt517 / 2;
                GraphicDefinition.CAMERA_DIRECTION += anInt2281 / 2;
            }

            method1098((byte) -94);
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "uf.B(" + (byte) 68 + ')');
        }
    }

    static boolean method1529(int var0, int var1, int var2, int var3, int var4, int var6) {
        try {
            long var8 = Class157.method2174(var6, var1, var3 + var0);
            int var10;
            int var11;
            int var12;
            ObjectDefinition var13;
            int var14;
            int[] var15;
            int var16;
            if (var8 != 0) {
                var10 = 3 & (int) var8 >> 20;
                var11 = (508650 & (int) var8) >> 14;
                var12 = Integer.MAX_VALUE & (int) (var8 >>> 32);
                var13 = ObjectDefinition.getObjectDefinition(var12);
                if (var13.anInt1516 == -1) {
                    var14 = var2;
                    if (var8 > 0) {
                        var14 = var4;
                    }

                    var15 = Toolkit.JAVA_TOOLKIT.getBuffer();
                    var16 = 4 * (-(var3 * 512) + '\uce00') + var1 * 4 + 24624;
                    if (var11 == 0 || var11 == 2) {
                        if (var10 == 0) {
                            var15[var16] = var14;
                            var15[512 + var16] = var14;
                            var15[var16 - -1024] = var14;
                            var15[1536 + var16] = var14;
                        } else if (var10 == 1) {
                            var15[var16] = var14;
                            var15[1 + var16] = var14;
                            var15[var16 - -2] = var14;
                            var15[var16 - -3] = var14;
                        } else if (var10 == 2) {
                            var15[var16 - -3] = var14;
                            var15[var16 - -3 - -512] = var14;
                            var15[var16 - -3 + 1024] = var14;
                            var15[var16 + 3 + 1536] = var14;
                        } else {
                            var15[var16 + 1536] = var14;
                            var15[1536 + var16 - -1] = var14;
                            var15[var16 + 1538] = var14;
                            var15[3 + var16 + 1536] = var14;
                        }
                    }

                    if (var11 == 3) {
                        if (var10 == 0) {
                            var15[var16] = var14;
                        } else if (1 == var10) {
                            var15[var16 - -3] = var14;
                        } else if (var10 == 2) {
                            var15[var16 - -3 + 1536] = var14;
                        } else {
                            var15[var16 - -1536] = var14;
                        }
                    }

                    if (var11 == 2) {
                        if (var10 == 3) {
                            var15[var16] = var14;
                            var15[var16 - -512] = var14;
                            var15[var16 + 1024] = var14;
                            var15[1536 + var16] = var14;
                        } else if (var10 == 0) {
                            var15[var16] = var14;
                            var15[1 + var16] = var14;
                            var15[2 + var16] = var14;
                            var15[3 + var16] = var14;
                        } else if (var10 == 1) {
                            var15[var16 - -3] = var14;
                            var15[512 + 3 + var16] = var14;
                            var15[3 + (var16 - -1024)] = var14;
                            var15[1536 + var16 + 3] = var14;
                        } else {
                            var15[1536 + var16] = var14;
                            var15[var16 - -1536 + 1] = var14;
                            var15[1536 + var16 + 2] = var14;
                            var15[var16 + 1539] = var14;
                        }
                    }
                } else if (AudioThread.method888(var1, var13, var0, var3, var10)) {
                    return false;
                }
            }

            var8 = Class3_Sub28_Sub5.method557(var6, var1, var0 + var3);
            if (var8 != 0L) {
                var10 = (int) var8 >> 20 & 3;
                var11 = ((int) var8 & 520964) >> 14;
                var12 = (int) (var8 >>> 32) & Integer.MAX_VALUE;
                var13 = ObjectDefinition.getObjectDefinition(var12);
                if (var13.anInt1516 != -1) {
                    if (AudioThread.method888(var1, var13, var0, var3, var10)) {
                        return false;
                    }
                } else if (var11 == 9) {
                    var14 = 15658734;
                    if (var8 > 0) {
                        var14 = 15597568;
                    }

                    var16 = var1 * 4 + (24624 - -(2048 * (103 - var3)));
                    var15 = Toolkit.JAVA_TOOLKIT.getBuffer();
                    if (var10 == 0 || var10 == 2) {
                        var15[1536 + var16] = var14;
                        var15[var16 - -1025] = var14;
                        var15[var16 + 512 + 2] = var14;
                        var15[var16 - -3] = var14;
                    } else {
                        var15[var16] = var14;
                        var15[var16 - -512 - -1] = var14;
                        var15[var16 - -1024 - -2] = var14;
                        var15[1536 + var16 - -3] = var14;
                    }
                }
            }

            var8 = Class3_Sub2.method104(var6, var1, var3 + var0);
            if (var8 != 0L) {
                var10 = (int) var8 >> 20 & 3;
                var11 = (int) (var8 >>> 32) & Integer.MAX_VALUE;
                ObjectDefinition var18 = ObjectDefinition.getObjectDefinition(var11);
                return var18.anInt1516 == -1 || !AudioThread.method888(var1, var18, var0, var3, var10);
            }

            return true;
        } catch (RuntimeException var17) {
            throw ClientErrorException.clientError(var17, "na.N(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + 0 + ',' + var6 + ',' + true + ')');
        }
    }

    static int method1535(WorldListEntry var0, WorldListEntry var1, int var2, int var3, int var4, boolean var5, boolean var6) {
        try {
            int var7 = Class161.method2201(var1, var4, var2 + -5638, var0, var6);
            if (var7 == 0) {
                if (var2 != 5730) {
                    return -76;
                } else if (var3 == -1) {
                    return 0;
                } else {
                    int var8 = Class161.method2201(var1, var3, var2 ^ 5651, var0, var5);
                    return !var5 ? var8 : -var8;
                }
            } else {
                return !var6 ? var7 : -var7;
            }
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "na.D(" + (var0 != null ? "{...}" : "null") + ',' + (var1 != null ? "{...}" : "null") + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    static SoftwareSprite method1537(CacheIndex var0, int var1) {
        try {
            if (Class140_Sub7.method2029((byte) -118, var0, var1)) {

                return Class117.method1722(-93);
            } else {
                return null;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "na.GB(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ',' + false + ')');
        }
    }

    static LDIndexedSprite method1539(int var2, CacheIndex var3) {
        try {
            //  System.out.println("RSString " + var2);
            return Class75_Sub4.method1351(var3, 0, var2) ? Class77.method1364() : null;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "na.MA(" + 0 + ',' + true + ',' + var2 + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

    public static RSString method802(int var0) {
        try {

            return Class163_Sub2_Sub1.aClass94Array4016[var0].length() > 0 ? RSString.stringCombiner(new RSString[]{Class140_Sub7.aClass94Array2935[var0], TextCore.Spacer, Class163_Sub2_Sub1.aClass94Array4016[var0]}) : Class140_Sub7.aClass94Array2935[var0];
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "wa.RB(" + var0 + ',' + true + ')');
        }
    }

    public static void method83(byte var0) {
        try {
            ItemDefinition.stringsStack = null;
            if (var0 != 30) {
                method84(null, 89);
            }

            aLinkedList_78 = null;
            aBooleanArray73 = null;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "ab.H(" + var0 + ')');
        }
    }

    static void calculateInterfacePosition(RSInterface iface, int parentWidth, int parentHeight) {
        if (iface.verticalPos == 0) {
            iface.anInt210 = iface.defY;
        } else if (iface.verticalPos == 1) {
            iface.anInt210 = (parentHeight - iface.height) / 2 + iface.defY;
        } else if (iface.verticalPos == 2) {
            iface.anInt210 = parentHeight - iface.height - iface.defY;
        } else if (iface.verticalPos == 3) {
            iface.anInt210 = iface.defY * parentHeight >> 14;
        } else if (iface.verticalPos == 4) {
            iface.anInt210 = (parentHeight * iface.defY >> 14) + (-iface.height + parentHeight) / 2;
        } else {
            iface.anInt210 = -(parentHeight * iface.defY >> 14) + -iface.height + parentHeight;
        }

        if (0 == iface.horizontalPos) {
            iface.anInt306 = iface.defX;
        } else if (iface.horizontalPos == 1) {
            iface.anInt306 = iface.defX + (parentWidth - iface.width) / 2;
        } else if (iface.horizontalPos == 2) {
            iface.anInt306 = -iface.defX + -iface.width + parentWidth;
        } else if (3 == iface.horizontalPos) {
            iface.anInt306 = iface.defX * parentWidth >> 14;
        } else if (4 == iface.horizontalPos) {
            iface.anInt306 = (iface.defX * parentWidth >> 14) + (parentWidth - iface.width) / 2;
        } else {
            iface.anInt306 = -(parentWidth * iface.defX >> 14) + parentWidth + -iface.width;
        }

        if (ClientCommands.commandQaOpEnabled && (Client.method44(iface).anInt2205 != 0 || iface.type == 0)) {
            if (iface.anInt210 < 0) {
                iface.anInt210 = 0;
            } else if (iface.height + iface.anInt210 > parentHeight) {
                iface.anInt210 = parentHeight + -iface.height;
            }

            if (0 > iface.anInt306) {
                iface.anInt306 = 0;
            } else if (parentWidth < iface.anInt306 - -iface.width) {
                iface.anInt306 = parentWidth + -iface.width;
            }
        }
    }

    static void method1225() {
        try {
            MouseListeningClass var1 = aClass149_4047;
            synchronized (var1) {

                TextureOperation21.anInt3069 = GraphicDefinition.anInt549;
                Class126.anInt1676 = Class3_Sub21.anInt2493;
                anInt1709 = MouseListeningClass.anInt1340;
                anInt3644 = Class140_Sub3.anInt2743;
                Class163_Sub1.anInt2993 = RenderAnimationDefinition.anInt362;
                ++anInt4045;
                Class38_Sub1.anInt2614 = TextureOperation14.anInt3389;
                Class75.aLong1102 = Class140_Sub6.aLong2926;
                Class140_Sub3.anInt2743 = 0;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ii.D(" + 18074 + ')');
        }
    }

    static void method848() {
        try {
            if (Class44.aFloat727 < NPC.aFloat3979) {
                Class44.aFloat727 = (float) ((double) Class44.aFloat727 + (double) Class44.aFloat727 / 30.0D);
                if (NPC.aFloat3979 < Class44.aFloat727) {
                    Class44.aFloat727 = NPC.aFloat3979;
                }

                Class3_Sub5.method117();
            } else if (NPC.aFloat3979 < Class44.aFloat727) {
                Class44.aFloat727 = (float) ((double) Class44.aFloat727 - (double) Class44.aFloat727 / 30.0D);
                if (NPC.aFloat3979 > Class44.aFloat727) {
                    Class44.aFloat727 = NPC.aFloat3979;
                }

                Class3_Sub5.method117();
            }

            if (Texture.anInt1150 != -1 && -1 != TextureOperation13.anInt3362) {
                int var1 = -Class3_Sub28_Sub1.anInt3536 + Texture.anInt1150;
                if (2 > var1 || var1 > 2) {
                    var1 >>= 4;
                }

                int var2 = -Class3_Sub4.anInt2251 + TextureOperation13.anInt3362;
                if (var2 < 2 || var2 > 2) {
                    var2 >>= 4;
                }

                Class3_Sub4.anInt2251 -= -var2;
                Class3_Sub28_Sub1.anInt3536 += var1;
                if (0 == var1 && 0 == var2) {
                    Texture.anInt1150 = -1;
                    TextureOperation13.anInt3362 = -1;
                }

                Class3_Sub5.method117();
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "bb.A(" + 4 + ')');
        }
    }

    static int bitwiseAnd(int var0, int var1) {
        try {
            return var0 & var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "qc.C(" + var0 + ',' + var1 + ')');
        }
    }

    static LDIndexedSprite[] method1281() {
        try {
            LDIndexedSprite[] var1 = new LDIndexedSprite[Class95.anInt1338];

            for (int var2 = 0; Class95.anInt1338 > var2; ++var2) {
                var1[var2] = new LDIndexedSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[var2], anIntArray2591[var2], Class140_Sub7.anIntArray2931[var2], anIntArray3076[var2], Class163_Sub1.aByteArrayArray2987[var2], TextureOperation38.spritePalette);
            }

            Class39.method1035((byte) 116);
            return var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "jg.A(" + 0 + ')');
        }
    }

    public static void method1282(int var0, int var2, int var3, int var4) {
        try {
            for (int var5 = 0; var5 < Class3_Sub28_Sub3.anInt3557; ++var5) {
                if (var0 < AudioChannel.anIntArray1969[var5] - -Class3_Sub28_Sub18.anIntArray3768[var5] && var0 + var4 > AudioChannel.anIntArray1969[var5] && var2 < Player.anIntArray3954[var5] - -Class140_Sub4.anIntArray2794[var5] && var3 + var2 > Player.anIntArray3954[var5]) {
                    Class163_Sub1_Sub1.aBooleanArray4008[var5] = true;
                }
            }

        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "jg.C(" + var0 + ',' + (byte) 122 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
        }
    }

    static Class3_Sub15 method1245(CacheIndex var1, int var2) {
        try {

            byte[] var3 = var1.method2138(var2);
            return var3 != null ? new Class3_Sub15(var3) : null;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "jb.F(" + 117 + ',' + (var1 != null ? "{...}" : "null") + ',' + var2 + ')');
        }
    }

    static void method1250(int var0, boolean var1) {
        try {
            Class3_Sub10.aByteArrayArrayArray2339 = null;
            Class44.anIntArrayArrayArray720 = null;
            if (var0 < 14) {
                method1250(10, true);
            }

            Class3_Sub28_Sub3.aClass11_3551 = null;
            RenderAnimationDefinition.aByteArrayArrayArray383 = null;
            Class83.anIntArray1161 = null;
            CS2Script.aByteArrayArrayArray2452 = null;
            if (var1 && null != aClass3_Sub28_Sub3_2600) {
                Class3_Sub20.aClass94_3220 = aClass3_Sub28_Sub3_2600.aClass94_3561;
            } else {
                Class3_Sub20.aClass94_3220 = null;
            }

            Class36.aByteArrayArrayArray640 = null;
            TextureOperation29.aByteArrayArrayArray3390 = null;
            Class29.anIntArrayArrayArray558 = null;
            Class146.anIntArrayArrayArray1903 = null;
            WorldMap.anInt2737 = 0;
            aClass3_Sub28_Sub3_2600 = null;
            Class84.aLinkedList_1162.method1211(-108);
            Class119.aClass131_1624 = null;
            TextureOperation13.anInt3362 = -1;
            Class75_Sub2.aClass33_2648 = null;
            Class91.aClass33_1305 = null;
            aClass33_1238 = null;
            Class161.aClass33_2034 = null;
            Class164_Sub2.aClass33_3019 = null;
            Class99.aClass33_1399 = null;
            Class75_Sub2.aClass33_2637 = null;
            Class119.aClass33_1626 = null;
            Class36.aAbstractSprite_637 = null;
            Texture.anInt1150 = -1;
            WorldMap.aSoftwareSprite_3221 = null;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "jb.E(" + var0 + ',' + var1 + ')');
        }
    }

    public static int[] method62() {
        try {

            int[] var8 = new int[2048];
            TextureOperation34 var9 = new TextureOperation34();
            var9.anInt3060 = 8;
            var9.anInt3058 = 4;
            var9.anInt3067 = 35;
            var9.anInt3056 = 8;
            var9.anInt3062 = (int) ((float) 0.4 * 4096.0F);
            var9.aBoolean3065 = true;
            var9.postDecode();
            TextureOperation33.method180(-106, 1, 2048);
            var9.method186(0, var8);
            return var8;
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "qk.A(" + true + ',' + 14585 + ',' + 8 + ',' + 2048 + ',' + 4 + ',' + (float) 0.4 + ',' + 8 + ',' + 35 + ')');
        }
    }

    public static int method64(int var1) {
        try {
            return var1 >>> 8;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "qk.D(" + true + ',' + var1 + ')');
        }
    }

    public static short[] method65(short[] var1) {
        try {
            if (null == var1) {
                return null;
            } else {
                short[] var2 = new short[var1.length];
                ArrayUtils.arraycopy(var1, 0, var2, 0, var1.length);
                return var2;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "vg.A(" + 23032 + ',' + "{...}" + ')');
        }
    }

    public static void method66(RSString var0, int var1, int var2, byte var3, int var4) {
        try {
            RSInterface var5 = AbstractSprite.method638(var4, var1);
            if (null != var5) {
                if (var5.anObjectArray314 != null) {
                    CS2Script var6 = new CS2Script();
                    var6.arguments = var5.anObjectArray314;
                    var6.aClass11_2449 = var5;
                    var6.aClass94_2439 = var0;
                    var6.interfaceButtons = var2; // Set to 1 for jukebox/friends/all/game chat. set to non 1 for all other chat settings
                    Class43.method1065(var6);
                }

                boolean var8 = true;
                if (0 < var5.anInt189) {
                    var8 = BufferedDataStream.method715(var5);
                }

                if (var8) {
                    if (Client.method44(var5).method92(var2 - 1, (byte) -108)) {
                        if (1 == var2) {
                            TextureOperation12.outgoingBuffer.putOpcode(155);
                            TextureOperation12.outgoingBuffer.writeInt(var4);
                            TextureOperation12.outgoingBuffer.writeShort(var1);
                        }

                        if (var3 < -7) {
                            if (var2 == 2) {
                                TextureOperation12.outgoingBuffer.putOpcode(196);
                                TextureOperation12.outgoingBuffer.writeInt(var4);
                                TextureOperation12.outgoingBuffer.writeShort(var1);
                            }

                            if (var2 == 3) {
                                TextureOperation12.outgoingBuffer.putOpcode(124);
                                TextureOperation12.outgoingBuffer.writeInt(var4);
                                TextureOperation12.outgoingBuffer.writeShort(var1);
                            }

                            if (var2 == 4) {
                                TextureOperation12.outgoingBuffer.putOpcode(199);
                                TextureOperation12.outgoingBuffer.writeInt(var4);
                                TextureOperation12.outgoingBuffer.writeShort(var1);
                            }

                            if (var2 == 5) {
                                TextureOperation12.outgoingBuffer.putOpcode(234);
                                TextureOperation12.outgoingBuffer.writeInt(var4);
                                TextureOperation12.outgoingBuffer.writeShort(var1);
                            }

                            if (6 == var2) {
                                TextureOperation12.outgoingBuffer.putOpcode(168);
                                TextureOperation12.outgoingBuffer.writeInt(var4);
                                TextureOperation12.outgoingBuffer.writeShort(var1);
                            }

                            if (var2 == 7) {
                                TextureOperation12.outgoingBuffer.putOpcode(166);
                                TextureOperation12.outgoingBuffer.writeInt(var4);
                                TextureOperation12.outgoingBuffer.writeShort(var1);
                            }

                            if (var2 == 8) {
                                TextureOperation12.outgoingBuffer.putOpcode(64);
                                TextureOperation12.outgoingBuffer.writeInt(var4);
                                TextureOperation12.outgoingBuffer.writeShort(var1);
                            }

                            if (var2 == 9) {
                                TextureOperation12.outgoingBuffer.putOpcode(53);
                                TextureOperation12.outgoingBuffer.writeInt(var4);
                                TextureOperation12.outgoingBuffer.writeShort(var1);
                            }

                            if (var2 == 10) {
                                TextureOperation12.outgoingBuffer.putOpcode(9);
                                TextureOperation12.outgoingBuffer.writeInt(var4);
                                TextureOperation12.outgoingBuffer.writeShort(var1);
                            }

                        }
                    }
                }
            }
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "vg.D(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
        }
    }

    public static void method68(int var0, Class140_Sub4 var2) {
        try {
            if (var2.anInt2800 <= Class44.anInt719) {
                if (var2.anInt2790 >= Class44.anInt719) {
                    method2270(var2);
                } else {
                    method1180((byte) -22, var2);
                }
            } else {
                Class140_Sub2.method1950(var2);
            }

            if (var2.xAxis < 128 || var2.zAxis < 128 || var2.xAxis >= 13184 || var2.zAxis >= 13184) {
                var2.anInt2771 = -1;
                var2.anInt2842 = -1;
                var2.anInt2800 = 0;
                var2.anInt2790 = 0;
                var2.xAxis = 128 * var2.anIntArray2767[0] - -(64 * var2.getSize());
                var2.zAxis = var2.anIntArray2755[0] * 128 + var2.getSize() * 64;
                var2.method1973(2279 + -2395);
            }

            if (var2 == Class102.player && (var2.xAxis < 1536 || var2.zAxis < 1536 || var2.xAxis >= 11776 || var2.zAxis >= 11776)) {
                var2.anInt2842 = -1;
                var2.anInt2800 = 0;
                var2.anInt2790 = 0;
                var2.anInt2771 = -1;
                var2.xAxis = var2.anIntArray2767[0] * 128 + var2.getSize() * 64;
                var2.zAxis = 128 * var2.anIntArray2755[0] + 64 * var2.getSize();
                var2.method1973(-98);
            }

            Class17.method904(65536, var2);
            RenderAnimationDefinition.method900(var2);
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "vg.C(" + var0 + ',' + 2279 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    public static void method1470(int var0, SequenceDefinition var1, int var3, boolean var4, int var5) {
        try {
            if (AudioHandler.currentSoundEffectCount < 50) {
                if (var1.sounds != null && var1.sounds.length > var5 && null != var1.sounds[var5]) {
                    int var6 = var1.sounds[var5][0];
                    int var7 = var6 >> 8;
                    int var10;
                    if (1 < var1.sounds[var5].length) {
                        var10 = (int) ((double) var1.sounds[var5].length * Math.random());
                        if (0 < var10) {
                            var7 = var1.sounds[var5][var10];
                        }
                    }

                    int var8 = var6 >> 5 & 7;
                    int var9 = var6 & 31;
                    if (var9 == 0) {
                        if (var4) {
                            AudioHandler.soundEffectHandler(var8, var7, 0);
                        }

                    } else if (0 != Sprites.ambientVolume) {
                        AudioHandler.soundEffectIDs[AudioHandler.currentSoundEffectCount] = var7;
                        AudioHandler.soundEffectVolumeArray[AudioHandler.currentSoundEffectCount] = var8;
                        int var11 = (-64 + var0) / 128;
                        var10 = (var3 + -64) / 128;
                        AudioHandler.soundEffectDelayArray[AudioHandler.currentSoundEffectCount] = 0;
                        AudioHandler.soundEffects[AudioHandler.currentSoundEffectCount] = null;
                        AudioHandler.soundEffectCoordinates[AudioHandler.currentSoundEffectCount] = var9 + (var10 << 16) + (var11 << 8);

                        ++AudioHandler.currentSoundEffectCount;
                    }
                }
            }
        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "ma.C(" + var0 + ',' + (var1 != null ? "{...}" : "null") + ',' + var3 + ',' + var4 + ',' + var5 + ')');
        }
    }

    public static void method1460(int var0, int var1, int var3, int var4, int var5, int var6) {
        try {
            if (Class101.anInt1425 <= var5 - var4 && Class3_Sub28_Sub18.anInt3765 >= var5 - -var4 && Class159.anInt2020 <= -var4 + var1 && Class57.anInt902 >= var4 + var1) {
                TextureOperation6.method175(var6, var0, var1, var3, var4, var5);
            } else {
                method2275(var3, var1, var4, var6, var0, var5);
            }

        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "ma.A(" + var0 + ',' + var1 + ',' + (byte) -113 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    public static void method1469(int[] var0, int var1, int var2, int var3, int var4, int var5) {
        Class3_Sub2 var6 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var3][var4][var5];
        if (var6 != null) {
            Class126 var7 = var6.aClass126_2240;
            int var9;
            if (var7 == null) {
                Class35 var8 = var6.aClass35_2226;
                if (var8 != null) {
                    var9 = var8.anInt611;
                    int var10 = var8.anInt612;
                    int var11 = var8.anInt626;
                    int var12 = var8.anInt621;
                    int[] var13 = TextureOperation19.anIntArrayArray3215[var9];
                    int[] var14 = anIntArrayArray2039[var10];
                    int var15 = 0;
                    int var16;
                    if (var11 == 0) {
                        for (var16 = 0; var16 < 4; ++var16) {
                            if (var13[var14[var15++]] != 0) {
                                var0[var1] = var12;
                            }

                            if (var13[var14[var15++]] != 0) {
                                var0[var1 + 1] = var12;
                            }

                            if (var13[var14[var15++]] != 0) {
                                var0[var1 + 2] = var12;
                            }

                            if (var13[var14[var15++]] != 0) {
                                var0[var1 + 3] = var12;
                            }

                            var1 += var2;
                        }
                    } else {
                        for (var16 = 0; var16 < 4; ++var16) {
                            var0[var1] = var13[var14[var15++]] == 0 ? var11 : var12;
                            var0[var1 + 1] = var13[var14[var15++]] == 0 ? var11 : var12;
                            var0[var1 + 2] = var13[var14[var15++]] == 0 ? var11 : var12;
                            var0[var1 + 3] = var13[var14[var15++]] == 0 ? var11 : var12;
                            var1 += var2;
                        }
                    }

                }
            } else {
                int var17 = var7.anInt1673;
                if (var17 != 0) {
                    for (var9 = 0; var9 < 4; ++var9) {
                        var0[var1] = var17;
                        var0[var1 + 1] = var17;
                        var0[var1 + 2] = var17;
                        var0[var1 + 3] = var17;
                        var1 += var2;
                    }

                }
            }
        }
    }

    public static boolean method2096(int var0, int var1, int var2, long var3) {
        Class3_Sub2 var5 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
        if (var5 == null) {
            return false;
        } else if (var5.aClass70_2234 != null && var5.aClass70_2234.aLong1048 == var3) {
            return true;
        } else if (var5.aClass19_2233 != null && var5.aClass19_2233.aLong428 == var3) {
            return true;
        } else if (var5.aClass12_2230 != null && var5.aClass12_2230.aLong328 == var3) {
            return true;
        } else {
            for (int var6 = 0; var6 < var5.anInt2223; ++var6) {
                if (var5.aClass25Array2221[var6].aLong498 == var3) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean method2103(int var0, int var1) {
        try {
            return var1 >= -78 || (var0 == 198 || 230 == var0 || var0 == 156 || var0 == 140 || 223 == var0);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "bg.P(" + var0 + ',' + var1 + ')');
        }
    }

    public static void method2104(RSInterface var0, boolean var1, int var2) {
        try {
            int var4 = var0.anInt240 != 0 ? var0.anInt240 : var0.width;
            int var5 = var0.anInt252 != 0 ? var0.anInt252 : var0.height;
            Class158.method2183(var0.componentHash, var1, var4, var5, GameObject.aClass11ArrayArray1834[var0.componentHash >> 16]);
            if (var0.aClass11Array262 != null) {
                Class158.method2183(var0.componentHash, var1, var4, var5, var0.aClass11Array262);
            }

            Class3_Sub31 var6 = TextureOperation23.aHashTable_3208.get(var0.componentHash);
            if (var6 != null) {
                Class75_Sub4.method1352(var5, var1, var6.anInt2602, var4);
            }

        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "bg.N(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ',' + var2 + ')');
        }
    }

    public static void method84(RSString var0, int var1) {
        try {
            int var2 = method1602(var0);
            if (var2 != -1) {
                method565(Class119.aClass131_1624.aShortArray1727[var2], Class119.aClass131_1624.aShortArray1718[var2]);
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ab.N(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ')');
        }
    }

    public static void languageSetter(int var1) {
        try {
            if (0 != var1) {
                if (var1 == 1) {
                    Translation.englishToGerman();
                } else {
                    if (2 != var1) {
                        throw new RuntimeException();
                    }

                    Translation.englishToFrench();
                }

            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ab.K(" + ',' + var1 + ')');
        }
    }

    public static Class106[] method88() {
        try {

            if (Class56.aClass106Array890 == null) {
                Class106[] var1 = method596(Class38.gameSignlink);
                Class106[] var2 = new Class106[var1.length];
                int var3 = 0;

                label58:
                for (Class106 var5 : var1) {
                    if ((0 >= var5.anInt1450 || var5.anInt1450 >= 24) && var5.anInt1447 >= 800 && 600 <= var5.anInt1449) {
                        for (int var6 = 0; var3 > var6; ++var6) {
                            Class106 var7 = var2[var6];
                            if (var5.anInt1447 == var7.anInt1447 && var5.anInt1449 == var7.anInt1449) {
                                if (var7.anInt1450 < var5.anInt1450) {
                                    var2[var6] = var5;
                                }
                                continue label58;
                            }
                        }

                        var2[var3] = var5;
                        ++var3;
                    }
                }

                Class56.aClass106Array890 = new Class106[var3];
                ArrayUtils.arraycopy(var2, 0, Class56.aClass106Array890, 0, var3);
                int[] var9 = new int[Class56.aClass106Array890.length];

                for (int var10 = 0; Class56.aClass106Array890.length > var10; ++var10) {
                    Class106 var11 = Class56.aClass106Array890[var10];
                    var9[var10] = var11.anInt1449 * var11.anInt1447;
                }

                Class108.method1658(var9, Class56.aClass106Array890);
            }

            return Class56.aClass106Array890;
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "ab.M(" + (byte) 28 + ')');
        }
    }

    public static void method89(CacheIndex var1, CacheIndex var2, CacheIndex var3, CacheIndex var4) {
        try {
            Class12.aClass153_323 = var2;
            Class97.aClass153_1378 = var1;
            aClass153_3361 = var3;
            Class119.aClass153_1628 = var4;

            GameObject.aClass11ArrayArray1834 = new RSInterface[aClass153_3361.method2121()][];
            aBooleanArray1703 = new boolean[aClass153_3361.method2121()];
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "ab.J(" + true + ',' + (var1 != null ? "{...}" : "null") + ',' + (var2 != null ? "{...}" : "null") + ',' + (var3 != null ? "{...}" : "null") + ',' + (var4 != null ? "{...}" : "null") + ')');
        }
    }

    public static void method1772(int plane, int animId, int dummy, NPC var3) {
        try {
            if (var3.anInt2771 == animId && -1 != animId) {
                SequenceDefinition var4 = SequenceDefinition.getAnimationDefinition(animId);
                int var5 = var4.delayType;
                if (var5 == 1) {
                    var3.anInt2776 = 1;
                    var3.anInt2832 = 0;
                    var3.anInt2760 = 0;
                    var3.anInt2773 = 0;
                    var3.anInt2828 = plane;
                    method1470(var3.zAxis, var4, var3.xAxis, false, var3.anInt2832);
                }

                if (var5 == 2) {
                    var3.anInt2773 = 0;
                }
            } else if (animId == -1 || -1 == var3.anInt2771 || SequenceDefinition.getAnimationDefinition(animId).forcedPriority >= SequenceDefinition.getAnimationDefinition(var3.anInt2771).forcedPriority) {
                var3.anInt2760 = 0;
                var3.anInt2771 = animId;
                var3.anInt2776 = 1;
                var3.anInt2773 = 0;
                var3.anInt2828 = plane;
                var3.anInt2811 = var3.anInt2816;
                var3.anInt2832 = 0;
                if (var3.anInt2771 != -1) {
                    method1470(var3.zAxis, SequenceDefinition.getAnimationDefinition(var3.anInt2771), var3.xAxis, false, var3.anInt2832);
                }
            }

            if (dummy != 39) {
                anInt1711 = 41;
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "sc.J(" + plane + ',' + animId + ',' + dummy + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

    public static void method1775() {
        for (int var0 = 0; var0 < anInt3070; ++var0) {
            Class25 var1 = SequenceDefinition.aClass25Array1868[var0];
            Class158.method2186(var1);
            SequenceDefinition.aClass25Array1868[var0] = null;
        }

        anInt3070 = 0;
    }

    public static void method1783(Component var1) {
        try {
            var1.removeMouseListener(aClass149_4047);
            var1.removeMouseMotionListener(aClass149_4047);
            var1.removeFocusListener(aClass149_4047);
            GraphicDefinition.anInt549 = 0;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sc.M(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    public static long method1395(int var0, int var1, int var2) {
        Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
        return var3 != null && var3.aClass19_2233 != null ? var3.aClass19_2233.aLong428 : 0L;
    }

    public static void method1396(int var0) {
        try {
            int var2 = Class106.rightMargin;
            int var1 = Class84.leftMargin;
            int var4 = -Class140_Sub7.canvasHeight + (Class70.frameHeight - var2);
            int var3 = -var1 + frameWidth - Class23.canvasWidth;
            if (~var1 < var0 || var3 > 0 || var2 > 0 || var4 > 0) {
                try {
                    Object var5;
                    if (null != TextureOperation30.fullScreenFrame) {
                        var5 = TextureOperation30.fullScreenFrame;
                    } else if (GameShell.frame == null) {
                        var5 = Class38.gameSignlink.gameApplet;
                    } else {
                        var5 = GameShell.frame;
                    }

                    int var7 = 0;
                    int var6 = 0;
                    if (GameShell.frame == var5) {
                        Insets var8 = GameShell.frame.getInsets();
                        var6 = var8.left;
                        var7 = var8.top;
                    }

                    Graphics var11 = ((Container) var5).getGraphics();
                    var11.setColor(Color.black);
                    if (var1 > 0) {
                        var11.fillRect(var6, var7, var1, Class70.frameHeight);
                    }

                    if (0 < var2) {
                        var11.fillRect(var6, var7, frameWidth, var2);
                    }

                    if (var3 > 0) {
                        var11.fillRect(-var3 + var6 + frameWidth, var7, var3, Class70.frameHeight);
                    }

                    if (var4 > 0) {
                        var11.fillRect(var6, -var4 + var7 + Class70.frameHeight, frameWidth, var4);
                    }
                } catch (Exception var9) {
                }
            }

        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "l.A(" + var0 + ')');
        }
    }

    public static void method589(int var0, int var1, int var2) {
        TextureOperation37.aBoolean3261 = true;
        Class91.anInt1302 = var0;
        Class49.anInt819 = var1;
        TextureOperation18.anInt4039 = var2;
        Class27.anInt515 = -1;
        anInt999 = -1;
    }

    public static boolean method590(byte var0, int var1, int var2) {
        try {
            if (11 == var2) {
                var2 = 10;
            }

            if (var2 >= 5 && var2 <= 8) {
                var2 = 4;
            }

            ObjectDefinition var4 = ObjectDefinition.getObjectDefinition(var1);
            return var4.method1684(var2);
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "il.D(" + var0 + ',' + var1 + ',' + var2 + ')');
        }
    }

    public static boolean method591(int var0) {
        try {
            KeyboardListener var1 = TextureOperation33.aClass148_3049;
            synchronized (var1) {
                if (Class3_Sub28_Sub9.anInt3620 == Class134.anInt1762) {
                    return false;
                } else {
                    Class3_Sub28_Sub9.anInt3624 = Class129.anIntArray1693[Class3_Sub28_Sub9.anInt3620];
                    TextureOperation7.anInt3342 = KeyboardListener.anIntArray1978[Class3_Sub28_Sub9.anInt3620];
                    Class3_Sub28_Sub9.anInt3620 = 1 + Class3_Sub28_Sub9.anInt3620 & 127;
                    if (var0 < 58) {
                        method591(68);
                    }

                    return true;
                }
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "c.F(" + var0 + ')');
        }
    }

    public static void method592(byte var0) {
        try {
            aClass25Array4060 = null;
            TextCore.aClass94_4057 = null;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "c.P(" + var0 + ')');
        }
    }

    public static void method593(Frame var0, Signlink var2) {
        try {

            while (true) {
                Class64 var3 = var2.method1436(var0, 86);

                while (var3.anInt978 == 0) {
                    TimeUtils.sleep(10L);
                }

                if (1 == var3.anInt978) {
                    var0.setVisible(false);
                    var0.dispose();
                    return;
                }

                TimeUtils.sleep(100L);
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "c.R(" + (var0 != null ? "{...}" : "null") + ',' + true + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    public static Class106[] method596(Signlink var1) {
        try {
            if (var1.method1432(false)) {
                Class64 var2 = var1.method1453((byte) 8);

                while (0 == var2.anInt978) {
                    TimeUtils.sleep(10L);
                }

                if (2 == var2.anInt978) {
                    return new Class106[0];
                } else {
                    int[] var3 = (int[]) var2.anObject974;
                    Class106[] var4 = new Class106[var3.length >> 2];

                    for (int var5 = 0; var5 < var4.length; ++var5) {
                        Class106 var6 = new Class106();
                        var4[var5] = var6;
                        var6.anInt1447 = var3[var5 << 2];
                        var6.anInt1449 = var3[(var5 << 2) + 1];
                        var6.anInt1450 = var3[(var5 << 2) - -2];
                    }

                    return var4;
                }
            } else {
                return new Class106[0];
            }
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "pm.P(" + 10 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    public static void method598(boolean var0, int var1, boolean var2, int var3, boolean var4, int var5, int var6) {
        try {
            if (var2) {
                HDToolKit.method1842();
            }

            if (null != TextureOperation30.fullScreenFrame && (3 != var1 || TextureOperation.anInt2378 != var5 || anInt3071 != var6)) {
                method593(TextureOperation30.fullScreenFrame, Class38.gameSignlink);
                TextureOperation30.fullScreenFrame = null;
            }

            if (3 == var1 && null == TextureOperation30.fullScreenFrame) {
                TextureOperation30.fullScreenFrame = Class99.method1597(0, var6, var5, Class38.gameSignlink);
                if (null != TextureOperation30.fullScreenFrame) {
                    anInt3071 = var6;
                    TextureOperation.anInt2378 = var5;
                    Class119.method1730(Class38.gameSignlink);
                }
            }

            if (var1 == 3 && TextureOperation30.fullScreenFrame == null) {
                method598(true, anInt2577, true, var3, var4, -1, -1);
            } else {
                Container var7;
                if (null == TextureOperation30.fullScreenFrame) {
                    if (null == GameShell.frame) {
                        var7 = Class38.gameSignlink.gameApplet;
                    } else {
                        var7 = GameShell.frame;
                    }
                } else {
                    var7 = TextureOperation30.fullScreenFrame;
                }

                frameWidth = var7.getSize().width;
                Class70.frameHeight = var7.getSize().height;
                Insets var8;
                if (GameShell.frame == var7) {
                    var8 = GameShell.frame.getInsets();
                    frameWidth -= var8.right + var8.left;
                    Class70.frameHeight -= var8.bottom + var8.top;
                }

                if (var1 >= 2) {
                    Class23.canvasWidth = frameWidth;
                    Class140_Sub7.canvasHeight = Class70.frameHeight;
                    Class84.leftMargin = 0;
                    Class106.rightMargin = 0;
                } else {
                    Class106.rightMargin = 0;
                    Class84.leftMargin = (frameWidth + -765) / 2;
                    Class23.canvasWidth = 765;
                    Class140_Sub7.canvasHeight = 503;
                }

                if (var0) {
                    Class163_Sub1_Sub1.method2215(GameShell.canvas);
                    method1783(GameShell.canvas);
                    if (null != Class38.aClass146_668) {
                        Class38.aClass146_668.method2082(GameShell.canvas);
                    }

                    Client.clientInstance.addCanvas();
                    TextureOperation34.method193((byte) 97, GameShell.canvas);
                    ItemDefinition.method1119(GameShell.canvas, var4);
                    if (Class38.aClass146_668 != null) {
                        Class38.aClass146_668.method2084(GameShell.canvas, -103);
                    }
                } else {
                    if (HDToolKit.highDetail) {
                        HDToolKit.method1854(Class23.canvasWidth, Class140_Sub7.canvasHeight);
                    }

                    GameShell.canvas.setSize(Class23.canvasWidth, Class140_Sub7.canvasHeight);
                    if (GameShell.frame == var7) {
                        var8 = GameShell.frame.getInsets();
                        GameShell.canvas.setLocation(var8.left - -Class84.leftMargin, var8.top + Class106.rightMargin);
                    } else {
                        GameShell.canvas.setLocation(Class84.leftMargin, Class106.rightMargin);
                    }
                }

                if (0 == var1 && var3 > 0) {
                    System.out.println("Trying to run method1834 for HD");
                    HDToolKit.method1834(GameShell.canvas);
                }

                if (var2 && var1 > 0) {
                    GameShell.canvas.setIgnoreRepaint(true);
                    if (!aBoolean11) {
                        Class32.method995();
                        aClass158_3009 = null;
                        aClass158_3009 = TextureOperation18.method285(Class140_Sub7.canvasHeight, Class23.canvasWidth, GameShell.canvas);
                        Class74.method1320();
                        if (5 == Class143.gameStage) {
                            StartupLoadingBar.draw(true, FontType.bold);
                        } else {
                            LoadingBox.draw(false, TextCore.LoadingPleaseWait2);
                        }

                        try {
                            Graphics var11 = GameShell.canvas.getGraphics();
                            aClass158_3009.method2179(var11);
                        } catch (Exception var9) {
                        }

                        method1396(-1);
                        if (var3 == 0) {
                            aClass158_3009 = TextureOperation18.method285(503, 765, GameShell.canvas);
                        } else {
                            aClass158_3009 = null;
                        }

                        Class64 var13 = Class38.gameSignlink.method1444(-43, Client.clientInstance.getClass());

                        while (var13.anInt978 == 0) {
                            TimeUtils.sleep(100L);
                        }

                        if (1 == var13.anInt978) {
                            aBoolean11 = true;
                        }
                    }

                    if (aBoolean11) {
                        HDToolKit.method1853(GameShell.canvas, 2 * anInt3671);
                    }
                }

                if (!HDToolKit.highDetail && 0 < var1) {
                    method598(true, 0, true, var3, false, -1, -1);
                } else {
                    if (var1 > 0 && var3 == 0) {
                        GameShell.aThread409.setPriority(5);
                        aClass158_3009 = null;
                        Class140_Sub1_Sub2.method1935();
                        ((Class102) Class51.anInterface2_838).method1619(200);
                        if (Class106.aBoolean1441) {
                            Class51.method1137(0.7F);
                        }

                        InvalidateData.method165();
                    } else if (0 == var1 && var3 > 0) {
                        GameShell.aThread409.setPriority(1);
                        aClass158_3009 = TextureOperation18.method285(503, 765, GameShell.canvas);
                        Class140_Sub1_Sub2.method1938();
                        ((Class102) Class51.anInterface2_838).method1619(20);
                        if (Class106.aBoolean1441) {
                            if (1 == anInt3625) {
                                Class51.method1137(0.9F);
                            }

                            if (anInt3625 == 2) {
                                Class51.method1137(0.8F);
                            }

                            if (3 == anInt3625) {
                                Class51.method1137(0.7F);
                            }

                            if (anInt3625 == 4) {
                                Class51.method1137(0.6F);
                            }
                        }

                        Class3_Sub11.method144();
                        InvalidateData.method165();
                    }

                    aBoolean742 = !NPC.method1986(89);
                    if (var2) {
                        Class3_Sub20.method389();
                    }

                    Class3_Sub15.aBoolean2427 = var1 >= 2;

                    if (-1 != ConfigInventoryDefinition.anInt3655) {
                        Class124.method1746(true, (byte) -107);
                    }

                    if (null != Class3_Sub15.activeConnection && (Class143.gameStage == 30 || Class143.gameStage == 25)) {
                        TextureOperation9.method204();
                    }

                    for (int var12 = 0; var12 < 100; ++var12) {
                        aBooleanArray3674[var12] = true;
                    }

                    TextureOperation30.fullRedraw = true;
                }
            }
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "pm.F(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    static void method1047(int var0, int var1, int var2, boolean var3, int var4, boolean var5) {
        try {
            if (var2 > var4) {
                int var7 = (var2 + var4) / 2;
                int var8 = var4;
                WorldListEntry var9 = aClass44_Sub1Array3201[var7];
                aClass44_Sub1Array3201[var7] = aClass44_Sub1Array3201[var2];
                aClass44_Sub1Array3201[var2] = var9;

                for (int var10 = var4; var10 < var2; ++var10) {
                    if (method1535(var9, aClass44_Sub1Array3201[var10], 5730, var0, var1, var3, var5) <= 0) {
                        WorldListEntry var11 = aClass44_Sub1Array3201[var10];
                        aClass44_Sub1Array3201[var10] = aClass44_Sub1Array3201[var8];
                        aClass44_Sub1Array3201[var8++] = var11;
                    }
                }

                aClass44_Sub1Array3201[var2] = aClass44_Sub1Array3201[var8];
                aClass44_Sub1Array3201[var8] = var9;
                method1047(var0, var1, -1 + var8, var3, var4, var5);
                method1047(var0, var1, var2, var3, var8 - -1, var5);
            }

        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "ge.A(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + false + ')');
        }
    }

    static void method1048(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        try {
            if (var5 > -15) {
                anInt688 = -64;
            }

            if (var1 >= 1 && var4 >= 1 && 102 >= var1 && var4 <= 102) {
                int var8;
                if (!NPC.method1986(41) && 0 == (2 & aByteArrayArrayArray113[0][var1][var4])) {
                    var8 = var2;
                    if ((8 & aByteArrayArrayArray113[var2][var1][var4]) != 0) {
                        var8 = 0;
                    }

                    if (var8 != Class140_Sub3.anInt2745) {
                        return;
                    }
                }

                var8 = var2;
                if (var2 < 3 && (2 & aByteArrayArrayArray113[1][var1][var4]) == 2) {
                    var8 = var2 + 1;
                }

                Class20.method910(var4, var1, var2, var7, var8, AtmosphereParser.aClass91Array1182[var2]);
                if (0 <= var0) {
                    boolean var9 = KeyboardListener.aBoolean1905;
                    KeyboardListener.aBoolean1905 = true;
                    Class110.method1683(var8, false, var2, false, AtmosphereParser.aClass91Array1182[var2], var0, var6, var1, var4, var3);
                    KeyboardListener.aBoolean1905 = var9;
                }
            }

        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "ge.H(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
        }
    }

    static RSString method1052(long var1) {
        try {
            if (var1 > 0 && var1 < 6582952005840035281L) {
                if (var1 % 37L == 0) {
                    return null;
                } else {
                    int var3 = 0;

                    for (long var4 = var1; var4 != 0L; ++var3) {
                        var4 /= 37L;
                    }

                    byte[] var6 = new byte[var3];

                    while (0L != var1) {
                        long var7 = var1;
                        var1 /= 37L;
                        --var3;
                        var6[var3] = Class163_Sub1_Sub1.aByteArray4005[(int) (-(var1 * 37L) + var7)];
                    }

                    RSString var10 = new RSString();
                    var10.buffer = var6;
                    var10.length = var6.length;
                    return var10;
                }
            } else {
                return null;
            }
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "ge.B(" + -29664 + ',' + var1 + ')');
        }
    }

    static void method1053(CacheIndex var1) {
        try {
            Class97.aClass153_1372 = var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ge.F(" + (byte) -117 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    public static Font method880(byte[] var1) {
        try {
            if (null == var1) {
                return null;
            } else {
                Object var2;
                if (HDToolKit.highDetail) {
                    var2 = new Class3_Sub28_Sub17_Sub2(var1, Class164.anIntArray2048, anIntArray2591, Class140_Sub7.anIntArray2931, anIntArray3076, Class163_Sub1.aByteArrayArray2987);
                } else {
                    var2 = new Class3_Sub28_Sub17_Sub1(var1, Class164.anIntArray2048, anIntArray2591, Class140_Sub7.anIntArray2931, anIntArray3076, Class163_Sub1.aByteArrayArray2987);
                }

                Class39.method1035((byte) 106);
                return (Font) var2;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ce.G(" + -22376 + ',' + "{...}" + ')');
        }
    }

    public static void method881(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
        try {
            if (var2 <= -65) {
                Class3_Sub4 var10 = null;

                for (Class3_Sub4 var11 = (Class3_Sub4) TextureOperation26.aLinkedList_3075.method1222(); var11 != null; var11 = (Class3_Sub4) TextureOperation26.aLinkedList_3075.method1221()) {
                    if (var11.anInt2250 == var0 && var11.anInt2264 == var4 && var11.anInt2248 == var1 && var11.anInt2263 == var7) {
                        var10 = var11;
                        break;
                    }
                }

                if (null == var10) {
                    var10 = new Class3_Sub4();
                    var10.anInt2264 = var4;
                    var10.anInt2248 = var1;
                    var10.anInt2250 = var0;
                    var10.anInt2263 = var7;
                    Class132.method1798(72, var10);
                    TextureOperation26.aLinkedList_3075.method1215(var10);
                }

                var10.anInt2262 = var8;
                var10.anInt2261 = var9;
                var10.anInt2259 = var5;
                var10.anInt2265 = var6;
                var10.anInt2256 = var3;
            }
        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "ce.J(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ')');
        }
    }

    public static void method882() {
        try {
            TextureOperation36.anInt3423 = 0;
            Class132.anInt1741 = -1;
            anInt154 = 1;
            GraphicDefinition.anInt546 = 2;
            aBoolean2311 = false;
            Class101.aClass153_1423 = null;
            TextureOperation8.anInt3463 = -1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ce.F(" + -1 + ',' + 2 + ')');
        }
    }

    public static Class3_Sub28_Sub3 method884(int var0, byte var1, int var2) {
        try {
            Class3_Sub28_Sub3 var3 = (Class3_Sub28_Sub3) Class134.aLinkedList_1758.method1222();

            for (; var3 != null; var3 = (Class3_Sub28_Sub3) Class134.aLinkedList_1758.method1221()) {
                if (var3.aBoolean3553 && var3.method537(var2, var0)) {
                    return var3;
                }
            }

            return null;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ce.D(" + var0 + ',' + var1 + ',' + var2 + ')');
        }
    }

    static void method518(Player var0) {
        try {
            Class3_Sub9 var2 = (Class3_Sub9) aHashTable_4046.get(var0.displayName.toLong());

            if (var2 == null) {
                Class70.method1286(var0.anIntArray2755[0], null, 0, null, var0.anIntArray2767[0], WorldListCountry.localPlane, var0);
            } else {
                var2.method134();
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "rg.UA(" + (var0 != null ? "{...}" : "null") + ',' + -110 + ')');
        }
    }

    static int method519(int var0, int var2, int var3) {
        try {
            var0 &= 3;

            return 0 != var0 ? (var0 != 1 ? (var0 == 2 ? -var3 + 7 : -var2 + 7) : var2) : var3;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "rg.RA(" + var0 + ',' + true + ',' + var2 + ',' + var3 + ')');
        }
    }

    static Class3_Sub28_Sub3 method520(byte var0) {
        try {
            return aClass3_Sub28_Sub3_2600;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "rg.OA(" + var0 + ')');
        }
    }

    static void method523(int var0, int var1, int var3, int var4, int var5, int var6, int var7, int var8) {
        try {
            int var9 = var3 - var8;
            int var11 = (-var5 + var0 << 16) / var9;
            int var10 = -var4 + var6;
            int var12 = (var7 + -var1 << 16) / var10;
            Class83.method1410(var1, var6, var4, var3, var5, var8, var12, var11);
        } catch (RuntimeException var13) {
            throw ClientErrorException.clientError(var13, "rg.SA(" + var0 + ',' + var1 + ',' + 0 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ')');
        }
    }

    public static void method744() {
        try {

            ++KeyboardListener.anInt1908;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "wa.HA(" + true + ')');
        }
    }

    public static void method746(byte var0) {
        try {
            Class67.aReferenceCache_1013.clear();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "wa.VC(" + var0 + ')');
        }
    }

    public static void method777(Class91[] var0, boolean var1, int var2, int var3, int var5, int var6, byte[] var7) {
        try {
            int var10;
            int var11;
            if (!var1) {
                for (int var9 = 0; 4 > var9; ++var9) {
                    for (var10 = 0; var10 < 64; ++var10) {
                        for (var11 = 0; var11 < 64; ++var11) {
                            if (var5 - -var10 > 0 && var10 + var5 < 103 && var3 + var11 > 0 && var11 + var3 < 103) {
                                var0[var9].anIntArrayArray1304[var10 + var5][var3 - -var11] = bitwiseAnd(var0[var9].anIntArrayArray1304[var10 + var5][var3 - -var11], -16777217);
                            }
                        }
                    }
                }
            }

            DataBuffer var20 = new DataBuffer(var7);
            byte var8;
            if (var1) {
                var8 = 1;
            } else {
                var8 = 4;
            }

            int var12;
            for (var10 = 0; var8 > var10; ++var10) {
                for (var11 = 0; var11 < 64; ++var11) {
                    for (var12 = 0; 64 > var12; ++var12) {
                        Class167.method2267(var2, var6, var1, var20, var12 - -var3, var5 + var11, (byte) 91, 0, var10);
                    }
                }
            }

            int var14;
            int var15;
            int var17;
            boolean var21;
            int var24;
            for (var21 = false; var20.index < var20.buffer.length; var21 = true) {
                var11 = var20.readUnsignedByte();
                if (var11 != 129) {
                    --var20.index;
                    break;
                }

                for (var12 = 0; var12 < 4; ++var12) {
                    byte var13 = var20.readSignedByte();
                    if (0 == var13) {
                        var14 = var5;
                        if (var5 >= 0) {
                            if (var5 >= 104) {
                                var14 = 104;
                            }
                        } else {
                            var14 = 0;
                        }

                        var24 = var3;
                        if (var3 < 0) {
                            var24 = 0;
                        } else if (var3 >= 104) {
                            var24 = 104;
                        }

                        var15 = 64 + var5;
                        var17 = var3 + 64;
                        if (var17 >= 0) {
                            if (var17 >= 104) {
                                var17 = 104;
                            }
                        } else {
                            var17 = 0;
                        }

                        if (var15 < 0) {
                            var15 = 0;
                        } else if (var15 >= 104) {
                            var15 = 104;
                        }

                        while (var15 > var14) {
                            while (var24 < var17) {
                                aByteArrayArrayArray1774[var12][var14][var24] = 0;
                                ++var24;
                            }

                            ++var14;
                        }
                    } else if (1 == var13) {
                        for (var14 = 0; var14 < 64; var14 += 4) {
                            for (var15 = 0; var15 < 64; var15 += 4) {
                                byte var16 = var20.readSignedByte();

                                for (var17 = var14 + var5; 4 + var5 + var14 > var17; ++var17) {
                                    for (int var18 = var3 + var15; 4 + var3 + var15 > var18; ++var18) {
                                        if (var17 >= 0 && var17 < 104 && 0 <= var18 && var18 < 104) {
                                            aByteArrayArrayArray1774[var12][var17][var18] = var16;
                                        }
                                    }
                                }
                            }
                        }
                    } else if (var13 == 2 && var12 > 0) {
                        var15 = var5 + 64;
                        var24 = var3;
                        var17 = var3 + 64;
                        if (var15 < 0) {
                            var15 = 0;
                        } else if (104 <= var15) {
                            var15 = 104;
                        }

                        if (var3 >= 0) {
                            if (var3 >= 104) {
                                var24 = 104;
                            }
                        } else {
                            var24 = 0;
                        }

                        if (var17 >= 0) {
                            if (var17 >= 104) {
                                var17 = 104;
                            }
                        } else {
                            var17 = 0;
                        }

                        var14 = var5;
                        if (var5 >= 0) {
                            if (104 <= var5) {
                                var14 = 104;
                            }
                        } else {
                            var14 = 0;
                        }

                        while (var15 > var14) {
                            while (var17 > var24) {
                                aByteArrayArrayArray1774[var12][var14][var24] = aByteArrayArrayArray1774[var12 + -1][var14][var24];
                                ++var24;
                            }

                            ++var14;
                        }
                    }
                }
            }

            int var23;
            if (HDToolKit.highDetail && !var1) {
                AtmosphereParser var22 = null;

                while (var20.buffer.length > var20.index) {
                    var12 = var20.readUnsignedByte();
                    if (var12 == 0) {
                        var22 = new AtmosphereParser(var20);
                    } else {
                        if (var12 != 1) {
//								throw
                            new IllegalStateException().printStackTrace();
                            return;//
                        }

                        var23 = var20.readUnsignedByte();
                        if (0 < var23) {
                            for (var14 = 0; var23 > var14; ++var14) {
                                Class43 var25 = new Class43(var20);
                                if (var25.anInt705 == 31) {
                                    Class57 var26 = Class81.method1401(var20.readUnsignedShort());
                                    var25.method1060((byte) -67, var26.anInt896, var26.anInt908, var26.anInt899, var26.anInt907);
                                }

                                var25.anInt708 += var3 << 7;
                                var25.anInt703 += var5 << 7;
                                var17 = var25.anInt708 >> 7;
                                var24 = var25.anInt703 >> 7;
                                if (var24 >= 0 && 0 <= var17 && var24 < 104 && var17 < 104) {
                                    var25.aBoolean696 = 0 != (aByteArrayArrayArray113[1][var24][var17] & 2);
                                    var25.anInt697 = Class44.anIntArrayArrayArray723[var25.anInt704][var24][var17] + -var25.anInt697;
                                    Class68.method1264(var25);
                                }
                            }
                        }
                    }
                }

                if (var22 == null) {
                    var22 = new AtmosphereParser();
                }

                for (var12 = 0; var12 < 8; ++var12) {
                    for (var23 = 0; var23 < 8; ++var23) {
                        var14 = var12 + (var5 >> 3);
                        var15 = (var3 >> 3) + var23;
                        if (0 <= var14 && var14 < 13 && var15 >= 0 && var15 < 13) {
                            AtmosphereParser.aAtmosphereParserArrayArray1581[var14][var15] = var22;
                        }
                    }
                }
            }

            if (!var21) {
                for (var11 = 0; var11 < 4; ++var11) {
                    for (var12 = 0; 16 > var12; ++var12) {
                        for (var23 = 0; var23 < 16; ++var23) {
                            var14 = (var5 >> 2) - -var12;
                            var15 = var23 + (var3 >> 2);
                            if (0 <= var14 && 26 > var14 && 0 <= var15 && var15 < 26) {
                                aByteArrayArrayArray1774[var11][var14][var15] = 0;
                            }
                        }
                    }
                }
            }

        } catch (RuntimeException var19) {
            throw ClientErrorException.clientError(var19, "wa.OA(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ',' + var2 + ',' + var3 + ',' + 4 + ',' + var5 + ',' + var6 + ',' + (var7 != null ? "{...}" : "null") + ')');
        }
    }

    public static Class12 method784(int var0, int var1, int var2) {
        Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
        return var3 != null && var3.aClass12_2230 != null ? var3.aClass12_2230 : null;
    }

    public static void method792() {
        try {
            int var1 = Class137.method1817();
            if (0 == var1) {
                Class158.aByteArrayArrayArray2008 = null;
                Class136.method1816(0, -7);
            } else if (var1 == 1) {
                Class3_Sub5.method112((byte) 0);
                Class136.method1816(512, -7);
                TextureOperation19.method257();
            } else {
                Class3_Sub5.method112((byte) (-4 + Class79.anInt1127 & 0xFF));
                Class136.method1816(2, -7);
            }

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "wa.AA(" + 9179409 + ')');
        }
    }

    public static Font method1300(int var1, CacheIndex var3, CacheIndex var4) {
        //System.out.println("Class 73 " + var1);
        return !Class75_Sub4.method1351(var3, 0, var1) ? null : method880(var4.getFile(var1, 0));
    }

    public static void method1301(int plane, int regionY, int regionX, int sceneY, boolean var4, int sceneX) {
        try {
            if (anInt3606 != regionX || Class3_Sub7.anInt2294 != regionY || plane != Class140_Sub3.anInt2745 && !NPC.method1986(45)) {
                anInt3606 = regionX;
                Class3_Sub7.anInt2294 = regionY;
                Class140_Sub3.anInt2745 = plane;
                if (NPC.method1986(105)) {
                    Class140_Sub3.anInt2745 = 0;
                }

                if (var4) {
                    Class117.method1719(28);
                } else {
                    Class117.method1719(25);
                }

                LoadingBox.draw(true, TextCore.LoadingPleaseWait2);
                int var8 = Texture.anInt1152;
                int var7 = Class131.anInt1716;
                Texture.anInt1152 = regionY * 8 - 48;
                Class131.anInt1716 = 8 * (-6 + regionX);
                TextureOperation37.aClass3_Sub28_Sub3_3264 = method884(8 * anInt3606, (byte) 88, 8 * Class3_Sub7.anInt2294);
                int var10 = -var8 + Texture.anInt1152;
                int var9 = Class131.anInt1716 + -var7;
                TextureOperation22.aClass131_3421 = null;
                int var11;
                NPC var12;
                int var13;
                if (var4) {
                    Class163.localNPCCount = 0;

                    for (var11 = 0; var11 < 32768; ++var11) {
                        var12 = NPC.npcs[var11];
                        if (null != var12) {
                            var12.xAxis -= 128 * var9;
                            var12.zAxis -= 128 * var10;
                            if (var12.xAxis >= 0 && var12.xAxis <= 13184 && var12.zAxis >= 0 && var12.zAxis <= 13184) {
                                for (var13 = 0; 10 > var13; ++var13) {
                                    var12.anIntArray2767[var13] -= var9;
                                    var12.anIntArray2755[var13] -= var10;
                                }

                                AudioThread.localNPCIndexes[Class163.localNPCCount++] = var11;
                            } else {
                                NPC.npcs[var11].setDefinitions(null);
                                NPC.npcs[var11] = null;
                            }
                        }
                    }
                } else {
                    for (var11 = 0; var11 < 32768; ++var11) {
                        var12 = NPC.npcs[var11];
                        if (null != var12) {
                            for (var13 = 0; var13 < 10; ++var13) {
                                var12.anIntArray2767[var13] -= var9;
                                var12.anIntArray2755[var13] -= var10;
                            }

                            var12.xAxis -= 128 * var9;
                            var12.zAxis -= var10 * 128;
                        }
                    }
                }

                for (var11 = 0; var11 < 2048; ++var11) {
                    Player var23 = players[var11];
                    if (null != var23) {
                        for (var13 = 0; 10 > var13; ++var13) {
                            var23.anIntArray2767[var13] -= var9;
                            var23.anIntArray2755[var13] -= var10;
                        }

                        var23.xAxis -= 128 * var9;
                        var23.zAxis -= 128 * var10;
                    }
                }

                WorldListCountry.localPlane = plane;
                Class102.player.method1981(sceneX, false, sceneY);
                byte var25 = 104;
                byte var24 = 0;
                byte var14 = 0;
                byte var16 = 1;
                byte var15 = 104;
                byte var26 = 1;
                if (var10 < 0) {
                    var16 = -1;
                    var15 = -1;
                    var14 = 103;
                }

                if (var9 < 0) {
                    var26 = -1;
                    var24 = 103;
                    var25 = -1;
                }

                for (int var17 = var24; var25 != var17; var17 += var26) {
                    for (int var18 = var14; var18 != var15; var18 += var16) {
                        int var19 = var9 + var17;
                        int var20 = var18 + var10;

                        for (int var21 = 0; 4 > var21; ++var21) {
                            if (var19 >= 0 && var20 >= 0 && var19 < 104 && var20 < 104) {
                                Class39.aLinkedListArrayArrayArray3273[var21][var17][var18] = Class39.aLinkedListArrayArrayArray3273[var21][var19][var20];
                            } else {
                                Class39.aLinkedListArrayArrayArray3273[var21][var17][var18] = null;
                            }
                        }
                    }
                }

                for (Class3_Sub4 var27 = (Class3_Sub4) TextureOperation26.aLinkedList_3075.method1222(); var27 != null; var27 = (Class3_Sub4) TextureOperation26.aLinkedList_3075.method1221()) {
                    var27.anInt2248 -= var10;
                    var27.anInt2264 -= var9;
                    if (0 > var27.anInt2264 || var27.anInt2248 < 0 || var27.anInt2264 >= 104 || var27.anInt2248 >= 104) {
                        var27.unlink();
                    }
                }

                if (var4) {
                    NPC.anInt3995 -= 128 * var9;
                    Class77.anInt1111 -= var10 * 128;
                    Class146.anInt1904 -= var10;
                    MouseListeningClass.anInt1923 -= var9;
                    Class157.anInt1996 -= var10;
                    anInt30 -= var9;
                } else {
                    Class133.anInt1753 = 1;
                }

                AudioHandler.currentSoundEffectCount = 0;
                if (Class65.anInt987 != 0) {
                    Class45.anInt733 -= var10;
                    Class65.anInt987 -= var9;
                }

                if (HDToolKit.highDetail && var4 && (Math.abs(var9) > 104 || 104 < Math.abs(var10))) {
                    TextureOperation31.method236();
                }

                Class58.anInt909 = -1;
                TextureOperation17.aLinkedList_3177.method1211(-122);
                TextureOperation13.aLinkedList_3364.method1211(-87);
            }
        } catch (RuntimeException var22) {
            throw ClientErrorException.clientError(var22, "k.D(" + plane + ',' + regionY + ',' + regionX + ',' + sceneY + ',' + var4 + ',' + sceneX + ',' + true + ')');
        }
    }

    public static InterfaceWidget method1302() {
        try {
            InterfaceWidget var1 = (InterfaceWidget) Class126.aClass13_1666.getFront();
            if (var1 == null) {
                do {
                    var1 = (InterfaceWidget) Class81.aClass13_1139.getFront();
                    if (var1 == null) {
                        return null;
                    }

                    if (var1.b() > TimeUtils.time()) {
                        return null;
                    }

                    var1.unlink();
                    var1.unlinkNode();
                } while ((Long.MIN_VALUE & var1.nodeKey) == 0L);

                return var1;
            } else {
                var1.unlink();
                var1.unlinkNode();
                return var1;
            }
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "k.J(" + (byte) -36 + ')');
        }
    }

    public static RSString method1303(RSInterface var0, RSString var1) {
        try {
            if (var1.indexOf(RSString.parse("(U"), 102) == -1) {
                return var1;
            } else {
                while (true) {
                    int var3 = var1.indexOf(TextCore.aClass94_331, 55);
                    if (var3 == -1) {
                        while (true) {
                            var3 = var1.indexOf(TextCore.aClass94_2080, 106);
                            if (var3 == -1) {
                                while (true) {
                                    var3 = var1.indexOf(TextCore.aClass94_1301, 95);
                                    if (var3 == -1) {
                                        while (true) {
                                            var3 = var1.indexOf(TextCore.aClass94_852, 57);
                                            if (var3 == -1) {
                                                while (true) {
                                                    var3 = var1.indexOf(TextCore.aClass94_3418, 113);
                                                    if (var3 == -1) {
                                                        while (true) {
                                                            var3 = var1.indexOf(TextCore.aClass94_1051, 50);
                                                            if (var3 == -1) {
                                                                return var1;
                                                            }

                                                            RSString var4 = RSString.parse("");
                                                            if (null != Class136.aClass64_1778) {
                                                                var4 = Class108.method1653(Class136.aClass64_1778.anInt979);

                                                                if (null != Class136.aClass64_1778.anObject974) {
                                                                    byte[] var5 = ((String) Class136.aClass64_1778.anObject974).getBytes(StandardCharsets.ISO_8859_1);
                                                                    var4 = TextureOperation33.bufferToString(var5, var5.length, 0);
                                                                }
                                                            }

                                                            var1 = RSString.stringCombiner(new RSString[]{var1.substring(0, var3, 0), var4, var1.substring(4 + var3)});
                                                        }
                                                    }

                                                    var1 = RSString.stringCombiner(new RSString[]{var1.substring(0, var3, 0), Class154.method2148(Class164_Sub2.method2247((byte) -4, 4, var0)), var1.substring(var3 - -2)});
                                                }
                                            }

                                            var1 = RSString.stringCombiner(new RSString[]{var1.substring(0, var3, 0), Class154.method2148(Class164_Sub2.method2247((byte) -109, 3, var0)), var1.substring(2 + var3)});
                                        }
                                    }

                                    var1 = RSString.stringCombiner(new RSString[]{var1.substring(0, var3, 0), Class154.method2148(Class164_Sub2.method2247((byte) 111, 2, var0)), var1.substring(2 + var3)});
                                }
                            }

                            var1 = RSString.stringCombiner(new RSString[]{var1.substring(0, var3, 0), Class154.method2148(Class164_Sub2.method2247((byte) 23, 1, var0)), var1.substring(var3 + 2)});
                        }
                    }

                    var1 = RSString.stringCombiner(new RSString[]{var1.substring(0, var3, 0), Class154.method2148(Class164_Sub2.method2247((byte) 107, 0, var0)), var1.substring(2 + var3)});
                }
            }
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "k.K(" + (var0 != null ? "{...}" : "null") + ',' + (var1 != null ? "{...}" : "null") + ',' + 0 + ')');
        }
    }

    public static void method2143(byte var0, int var1, int var2, int var3, int var4) {
        try {
            InterfaceWidget var5 = InterfaceWidget.getWidget(8, var2);
            var5.flagUpdate();
            var5.anInt3596 = var1;
            if (var0 >= -120) {
                anInt1950 = -14;
            }

            var5.anInt3598 = var4;
            var5.anInt3597 = var3;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "ve.U(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
        }
    }

    public static void method2065(CacheIndex var1, CacheIndex var2) {
        try {
            TextureOperation4.aClass153_3227 = var1;
            SequenceDefinition.aClass153_1852 = var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "u.D(" + (byte) -125 + ',' + (var1 != null ? "{...}" : "null") + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    public static void method2067() {
        try {
            for (Class3_Sub28_Sub2 var1 = (Class3_Sub28_Sub2) TextureOperation17.aLinkedList_3177.method1222(); null != var1; var1 = (Class3_Sub28_Sub2) TextureOperation17.aLinkedList_3177.method1221()) {
                Class140_Sub2 var2 = var1.aClass140_Sub2_3545;
                if (WorldListCountry.localPlane == var2.anInt2717 && !var2.aBoolean2718) {
                    if (Class44.anInt719 >= var2.anInt2703) {
                        var2.method1955(Class106.anInt1446);
                        if (var2.aBoolean2718) {
                            var1.unlink();
                        } else {
                            Class20.method907(var2.anInt2717, var2.anInt2716, var2.anInt2710, var2.anInt2712, 60, var2, 0, -1L, false);
                        }
                    }
                } else {
                    var1.unlink();
                }
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "u.G(" + false + ')');
        }
    }

    // static final void method2068(NPCDefinition var0, int var1, int var2, int var3, int var4) {
    // @Splinter
    public static void drawNpcRightClickOptions(NPCDefinition var0, int var1, int var2, int var3, int var4) {
        try {
            if (menuOptionCount < 400) {
                if (var0.childNPCs != null) {
                    var0 = var0.method1471((byte) 66);
                }

                if (null != var0) {
                    if (var0.aBoolean1270) {
                        RSString var5 = var0.NPCName;
                        if (0 != var0.anInt1260) {
                            RSString var6 = Class158.paramGameTypeID != 1 ? TextCore.HasLevel : TextCore.HasRating;
                            var5 = RSString.stringCombiner(new RSString[]{var5, Player.combatLevelColor(var0.anInt1260, (byte) -122, Class102.player.COMBAT_LEVEL), TextCore.LEFT_PARENTHESES, var6, RSString.stringAnimator(var0.anInt1260), TextCore.RIGHT_PARENTHESES});
                        }

                        if (Class164_Sub1.anInt3012 == 1) {
                            Class3_Sub24_Sub4.method1177(Class99.anInt1403, var3, (byte) -125, RSString.stringCombiner(new RSString[]{RenderAnimationDefinition.aClass94_378, ColorCore.TextColor, var5}), var1, (short) 26, TextCore.HasUse, var4);
                        } else if (GameObject.aBoolean1837) {
                            Class3_Sub28_Sub9 var12 = -1 == anInt1038 ? null : LinkedList.method1210(anInt1038);
                            if ((2 & Class164.anInt2051) != 0 && (var12 == null || var0.method1475(anInt1038, var12.anInt3614) != var12.anInt3614)) {
                                Class3_Sub24_Sub4.method1177(anInt1887, var3, (byte) -93, RSString.stringCombiner(new RSString[]{TextCore.aClass94_676, ColorCore.TextColor, var5}), var1, (short) 45, Class3_Sub28_Sub9.aClass94_3621, var4);
                            }
                        } else {
                            RSString[] var11 = var0.options;
                            if (Class123.aBoolean1656) {
                                var11 = Class3_Sub31.optionsArrayStringConstructor(var11);
                            }

                            int var7;
                            if (var11 != null) {
                                for (var7 = 4; var7 >= 0; --var7) {
                                    if (var11[var7] != null && (Class158.paramGameTypeID != 0 || !var11[var7].equalsStringIgnoreCase(TextCore.HasAttack))) {
                                        byte var8 = 0;
                                        if (var7 == 0) {
                                            var8 = 17;
                                        }

                                        if (var7 == 1) {
                                            var8 = 16;
                                        }

                                        int var9 = -1;
                                        if (var7 == 2) {
                                            var8 = 4;
                                        }

                                        if (var7 == 3) {
                                            var8 = 19;
                                        }

                                        if (var0.anInt1296 == var7) {
                                            var9 = var0.anInt1253;
                                        }

                                        if (var0.anInt1289 == var7) {
                                            var9 = var0.anInt1278;
                                        }

                                        if (var7 == 4) {
                                            var8 = 2;
                                        }

                                        Class3_Sub24_Sub4.method1177(var9, var3, (byte) -103, RSString.stringCombiner(new RSString[]{ColorCore.NPCRightClickColor, var5}), var1, var8, var11[var7], var4);
                                    }
                                }
                            }

                            if (0 == Class158.paramGameTypeID && var11 != null) {
                                for (var7 = 4; var7 >= 0; --var7) {
                                    if (null != var11[var7] && var11[var7].equalsStringIgnoreCase(TextCore.HasAttack)) {
                                        short var14 = 0;
                                        if (var0.anInt1260 > Class102.player.COMBAT_LEVEL) {
                                            var14 = 2000;
                                            //This var sets "attack" as a right click attack option for higher level npcs, let's make it a single click!
                                        }

                                        short var13 = 0;
                                        if (var7 == 0) {
                                            var13 = 17;
                                        }

                                        if (var7 == 1) {
                                            var13 = 16;
                                        }

                                        if (2 == var7) {
                                            var13 = 4;
                                        }

                                        if (3 == var7) {
                                            var13 = 19;
                                        }

                                        if (var7 == 4) {
                                            var13 = 2;
                                        }

                                        if (0 != var13) {
                                            var13 += var14;
                                        }

                                        Class3_Sub24_Sub4.method1177(var0.anInt1298, var3, (byte) -128, RSString.stringCombiner(new RSString[]{ColorCore.NPCRightClickColor, var5}), var1, var13, var11[var7], var4);
                                    }
                                }
                            }

                            if (GameConfig.NPC_DEBUG_ENABLED) {
                                Class3_Sub24_Sub4.method1177(Class131.anInt1719, var3, (byte) -73, RSString.stringCombiner(new RSString[]{ColorCore.NPCRightClickColor, var5}), var1, (short) 1007, RSString.parse("Examine" + "<br>" + " ID: (X" + var0.npcId + "(Y"), var4);
                            } else {
                                Class3_Sub24_Sub4.method1177(Class131.anInt1719, var3, (byte) -73, RSString.stringCombiner(new RSString[]{ColorCore.NPCRightClickColor, var5}), var1, (short) 1007, TextCore.HasExamine, var4);
                            }
                        }

                    }
                }
            }
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "u.A(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
        }
    }

    static void method56(int var0) {
        InterfaceWidget var2 = InterfaceWidget.getWidget(6, var0);
        var2.a();
    }

    static boolean loadInterface(int archive) {
        if (!aBooleanArray1703[archive]) {
            if (aClass153_3361.method2117(archive)) {
                int fileLength = aClass153_3361.getFileAmount(archive);
                if (0 == fileLength) {
                    aBooleanArray1703[archive] = true;
                } else {
                    if (null == GameObject.aClass11ArrayArray1834[archive]) {
                        GameObject.aClass11ArrayArray1834[archive] = new RSInterface[fileLength];
                    }

                    for (int fileId = 0; fileId < fileLength; ++fileId) {
                        if (null == GameObject.aClass11ArrayArray1834[archive][fileId]) {
                            byte[] var4 = aClass153_3361.getFile(archive, fileId);
                            if (var4 != null) {
                                RSInterface iface = GameObject.aClass11ArrayArray1834[archive][fileId] = new RSInterface();
                                iface.componentHash = fileId + (archive << 16);
                                if (var4[0] == -1) {
                                    iface.decodeScriptFormat(new DataBuffer(var4));
                                } else {
                                    iface.decodeNoScripts(new DataBuffer(var4));
                                }
                            }
                        }
                    }

                    aBooleanArray1703[archive] = true;

                }
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    static void method60(int var0, int var1, int var2, Class91[] var3, int var4, byte var5, byte[] var6, int var7, int var8, int var9, boolean var10) {
        int var13;
        if (!var10) {
            for (int var12 = 0; var12 < 8; ++var12) {
                for (var13 = 0; 8 > var13; ++var13) {
                    if (0 < var1 - -var12 && var12 + var1 < 103 && var13 + var4 > 0 && var4 + var13 < 103) {
                        var3[var2].anIntArrayArray1304[var12 + var1][var13 + var4] = bitwiseAnd(var3[var2].anIntArrayArray1304[var12 + var1][var13 + var4], -16777217);
                    }
                }
            }
        }
        byte var11;
        if (var10) {
            var11 = 1;
        } else {
            var11 = 4;
        }

        DataBuffer var25 = new DataBuffer(var6);

        int var14;
        int var15;
        for (var13 = 0; var13 < var11; ++var13) {
            for (var14 = 0; var14 < 64; ++var14) {
                for (var15 = 0; var15 < 64; ++var15) {
                    if (var13 == var7 && var9 <= var14 && 8 + var9 > var14 && var8 <= var15 && var15 < 8 + var8) {
                        Class167.method2267(0, 0, var10, var25, TextureOperation3.method310(var0, (byte) -117, 7 & var14, 7 & var15) + var4, method519(var0, var15 & 7, var14 & 7) + var1, (byte) 63, var0, var2);
                    } else {
                        Class167.method2267(0, 0, var10, var25, -1, -1, (byte) 123, 0, 0);
                    }
                }
            }
        }

        int var17;
        int var21;
        int var20;
        int var22;
        int var29;
        while (var25.index < var25.buffer.length) {
            var14 = var25.readUnsignedByte();
            if (var14 != 129) {
                --var25.index;
                break;
            }

            for (var15 = 0; var15 < 4; ++var15) {
                byte var16 = var25.readSignedByte();
                int var18;
                if (var16 != 0) {
                    if (var16 == 1) {
                        for (var17 = 0; 64 > var17; var17 += 4) {
                            for (var18 = 0; var18 < 64; var18 += 4) {
                                byte var19 = var25.readSignedByte();
                                if (var7 >= var15) {
                                    for (var20 = var17; var17 + 4 > var20; ++var20) {
                                        for (var21 = var18; var21 < 4 + var18; ++var21) {
                                            if (var9 <= var20 && 8 + var9 > var20 && var8 <= var21) {
                                                var22 = var1 - -method519(var0, var21 & 7, var20 & 7);
                                                int var23 = TextureOperation3.method310(var0, (byte) -97, 7 & var20, var21 & 7) + var4;
                                                if (0 <= var22 && 104 > var22 && var23 >= 0 && var23 < 104) {
                                                    aByteArrayArrayArray1774[var2][var22][var23] = var19;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (var15 <= var7) {
                    var18 = 7 + var1;
                    var17 = var1;
                    var20 = var4 - -7;
                    if (var20 < 0) {
                        var20 = 0;
                    } else if (104 <= var20) {
                        var20 = 104;
                    }

                    if (0 <= var18) {
                        if (var18 >= 104) {
                            var18 = 104;
                        }
                    } else {
                        var18 = 0;
                    }

                    var29 = var4;
                    if (var4 < 0) {
                        var29 = 0;
                    } else if (104 <= var4) {
                        var29 = 104;
                    }

                    if (var1 < 0) {
                        var17 = 0;
                    } else if (104 <= var1) {
                        var17 = 104;
                    }

                    while (var18 > var17) {
                        while (var29 < var20) {
                            aByteArrayArrayArray1774[var2][var17][var29] = 0;
                            ++var29;
                        }

                        ++var17;
                    }
                }
            }
        }

        int var28;
        if (HDToolKit.highDetail && !var10) {
            AtmosphereParser var26 = null;

            while (var25.buffer.length > var25.index) {
                var15 = var25.readUnsignedByte();
                if (var15 == 0) {
                    var26 = new AtmosphereParser(var25);
                } else {
                    if (var15 != 1) {
                        throw new IllegalStateException();
                    }

                    var28 = var25.readUnsignedByte();
                    if (var28 > 0) {
                        for (var17 = 0; var28 > var17; ++var17) {
                            Class43 var30 = new Class43(var25);
                            if (var30.anInt705 == 31) {
                                Class57 var31 = Class81.method1401(var25.readUnsignedShort());
                                var30.method1060((byte) 123, var31.anInt896, var31.anInt908, var31.anInt899, var31.anInt907);
                            }

                            var29 = var30.anInt703 >> 7;
                            var20 = var30.anInt708 >> 7;
                            if (var30.anInt704 == var7 && var9 <= var29 && var9 - -8 > var29 && var20 >= var8 && var20 < var8 - -8) {
                                var21 = Class3_Sub26.method514(var0, var30.anInt703 & 1023, 1023 & var30.anInt708) + (var1 << 7);
                                var22 = TextureOperation28.method293(var30.anInt703 & 1023, var0, 1023 & var30.anInt708) + (var4 << 7);
                                var30.anInt703 = var21;
                                var30.anInt708 = var22;
                                var29 = var30.anInt703 >> 7;
                                var20 = var30.anInt708 >> 7;
                                if (var29 >= 0 && var20 >= 0 && 104 > var29 && var20 < 104) {
                                    var30.aBoolean696 = (2 & aByteArrayArrayArray113[1][var29][var20]) != 0;
                                    var30.anInt697 = Class44.anIntArrayArrayArray723[var30.anInt704][var29][var20] - var30.anInt697;
                                    Class68.method1264(var30);
                                }
                            }
                        }
                    }
                }
            }

            if (null == var26) {
                var26 = new AtmosphereParser();
            }

            AtmosphereParser.aAtmosphereParserArrayArray1581[var1 >> 3][var4 >> 3] = var26;
        }

        var14 = 7 + var1;
        var15 = var4 - -7;

        for (var28 = var1; var14 > var28; ++var28) {
            for (var17 = var4; var15 > var17; ++var17) {
                aByteArrayArrayArray1774[var2][var28][var17] = 0;
            }
        }
    }

    public static int method54(int var0) {
        try {
            return var0 & 127;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "oe.A(" + var0 + ',' + false + ')');
        }
    }

    public static LDIndexedSprite[] method619(byte var0, int var1, CacheIndex var2) {
        try {
            return Class140_Sub7.method2029((byte) -119, var2, var1) ? (var0 <= 52 ? null : method1281()) : null;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ml.A(" + var0 + ',' + var1 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

    public static void method383(int var0, int var1) {
        try {
            if (var1 == Class23.anInt453 && var1 != 0) {
                ShaderInterface var2 = anShaderInterfaceArray70[var1];
                var2.method23(TextureOperation37.anInt3263);
            }

            if (var0 != -32584) {
                method383(60, 23);
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ka.A(" + var0 + ',' + var1 + ')');
        }
    }

    public static int method599(CacheIndex var1) {
        try {
            int var2 = 0;
            if (var1.retrieveSpriteFile(Class154.anInt1966)) {
                ++var2;
            }

            if (var1.retrieveSpriteFile(Class79.anInt1124)) {
                ++var2;
            }

            return var2;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "lk.F(" + -20916 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    public static AbstractSprite method602(int var0, CacheIndex var1) {
        try {
            //  System.out.println("Class3_sub28_Sub16 " + var1);
            if (Class75_Sub4.method1351(var1, 0, var0)) {
                return Class43.method1062((byte) -18 + 103);
            } else {
                return null;
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "lk.R(" + 0 + ',' + var0 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    /**
     * @param notifyScripts Whether if a listening CS2 script should be invoked if this interface is resized
     */
    public static void calculateInterfaceSize(RSInterface iface, int parentWidth, int parentHeight, boolean notifyScripts) {
        int oldWidth = iface.width;
        int oldHeight = iface.height;

        if (iface.horizontalResize == 0) {
            iface.width = iface.defWidth;
        } else if (iface.horizontalResize == 1) {
            iface.width = parentWidth + -iface.defWidth;
        } else if (iface.horizontalResize == 2) {
            iface.width = iface.defWidth * parentWidth >> 14;
        } else if (iface.horizontalResize == 3) {
            if (iface.type == 2) {
                iface.width = iface.defWidth * 32 - -((iface.defWidth - 1) * iface.anInt285);
            } else if (iface.type == 7) {
                iface.width = 115 * iface.defWidth + iface.anInt285 * (-1 + iface.defWidth);
            }
        }

        if (iface.verticalResize == 0) {
            iface.height = iface.defHeight;
        } else if (iface.verticalResize == 1) {
            iface.height = -iface.defHeight + parentHeight;
        } else if (iface.verticalResize == 2) {
            iface.height = parentHeight * iface.defHeight >> 14;
        } else if (iface.verticalResize == 3) {
            if (iface.type == 2) {
                iface.height = (iface.defHeight + -1) * iface.anInt290 + iface.defHeight * 32;
            } else if (iface.type == 7) {
                iface.height = iface.defHeight * 12 + (-1 + iface.defHeight) * iface.anInt290;
            }
        }

        if (iface.horizontalResize == 4) {
            iface.width = iface.anInt216 * iface.height / iface.anInt160;
        }

        if (iface.verticalResize == 4) {
            iface.height = iface.anInt160 * iface.width / iface.anInt216;
        }

        if (ClientCommands.commandQaOpEnabled && (Client.method44(iface).anInt2205 != 0 || iface.type == 0)) {
            if (iface.height < 5 && 5 > iface.width) {
                iface.height = 5;
                iface.width = 5;
            } else {
                if (iface.width <= 0) {
                    iface.width = 5;
                }

                if (0 >= iface.height) {
                    iface.height = 5;
                }
            }
        }

        if (iface.anInt189 == 1337) {
            aClass11_2091 = iface;
        }

        if (notifyScripts && null != iface.anObjectArray235 && (iface.width != oldWidth || iface.height != oldHeight)) {
            CS2Script script = new CS2Script();
            script.arguments = iface.anObjectArray235;
            script.aClass11_2449 = iface;
            Client.aLinkedList_1471.method1215(script);
        }
    }

    public static void method606(int var0, Class3_Sub9 var1, int var2, int var3, int var4, int var5) {
        try {
            if (var5 > 44) {
                if (var1.anInt2332 != -1 || var1.anIntArray2333 != null) {
                    int var6 = 0;
                    if (var1.anInt2321 < var0) {
                        var6 += -var1.anInt2321 + var0;
                    } else if (var1.anInt2326 > var0) {
                        var6 += var1.anInt2326 - var0;
                    }

                    if (var1.anInt2307 >= var4) {
                        if (var4 < var1.anInt2308) {
                            var6 += -var4 + var1.anInt2308;
                        }
                    } else {
                        var6 += -var1.anInt2307 + var4;
                    }

                    if (0 != var1.anInt2328 && var6 - 64 <= var1.anInt2328 && 0 != Sprites.ambientVolume && var2 == var1.anInt2314) {
                        var6 -= 64;
                        if (var6 < 0) {
                            var6 = 0;
                        }

                        int var7 = (-var6 + var1.anInt2328) * Sprites.ambientVolume / var1.anInt2328;
                        if (var1.aClass3_Sub24_Sub1_2312 == null) {
                            if (var1.anInt2332 >= 0) {
                                SynthSound var8 = SynthSound.create(CacheIndex.soundFXIndex, var1.anInt2332, 0);
                                if (null != var8) {
                                    PcmSound var9 = var8.toPCMSound().method151(Class27.resampler);
                                    Class3_Sub24_Sub1 var10 = Class3_Sub24_Sub1.method437(var9, var7);
                                    Objects.requireNonNull(var10).method429(-1);
                                    Class3_Sub26.aClass3_Sub24_Sub2_2563.method457(var10);
                                    var1.aClass3_Sub24_Sub1_2312 = var10;
                                }
                            }
                        } else {
                            var1.aClass3_Sub24_Sub1_2312.method419(var7);
                        }

                        if (null != var1.aClass3_Sub24_Sub1_2315) {
                            var1.aClass3_Sub24_Sub1_2315.method419(var7);
                            if (!var1.aClass3_Sub24_Sub1_2315.isLinked()) {
                                var1.aClass3_Sub24_Sub1_2315 = null;
                            }
                        } else if (var1.anIntArray2333 != null && ((var1.anInt2316 -= var3) <= 0)) {
                            int var13 = (int) ((double) var1.anIntArray2333.length * Math.random());
                            SynthSound var14 = SynthSound.create(CacheIndex.soundFXIndex, var1.anIntArray2333[var13], 0);
                            if (null != var14) {
                                PcmSound var15 = var14.toPCMSound().method151(Class27.resampler);
                                Class3_Sub24_Sub1 var11 = Class3_Sub24_Sub1.method437(var15, var7);
                                Objects.requireNonNull(var11).method429(0);
                                Class3_Sub26.aClass3_Sub24_Sub2_2563.method457(var11);
                                var1.anInt2316 = (int) ((double) (-var1.anInt2310 + var1.anInt2325) * Math.random()) + var1.anInt2310;
                                var1.aClass3_Sub24_Sub1_2315 = var11;
                            }
                        }

                    } else {
                        if (null != var1.aClass3_Sub24_Sub1_2312) {
                            Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var1.aClass3_Sub24_Sub1_2312);
                            var1.aClass3_Sub24_Sub1_2312 = null;
                        }

                        if (var1.aClass3_Sub24_Sub1_2315 != null) {
                            Class3_Sub26.aClass3_Sub24_Sub2_2563.method461(var1.aClass3_Sub24_Sub1_2315);
                            var1.aClass3_Sub24_Sub1_2315 = null;
                        }

                    }
                }
            }
        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "lk.O(" + var0 + ',' + (var1 != null ? "{...}" : "null") + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
        }
    }

    static void method1180(byte var0, Class140_Sub4 var1) {
        try {
            RenderAnimationDefinition var2 = var1.getRenderAnimationType();
            var1.anInt2764 = var2.stand_animation;
            if (var0 >= -5) {
                anIntArray882 = null;
            }

            if (var1.anInt2816 == 0) {
                var1.anInt2824 = 0;
            } else {
                if (var1.anInt2771 != -1 && 0 == var1.anInt2828) {
                    SequenceDefinition var3 = SequenceDefinition.getAnimationDefinition(var1.anInt2771);
                    if (var1.anInt2811 > 0 && var3.resetWhenWalk == 0) {
                        ++var1.anInt2824;
                        return;
                    }

                    if (var1.anInt2811 <= 0 && var3.priority == 0) {
                        ++var1.anInt2824;
                        return;
                    }
                }

                int var18 = var1.xAxis;
                int var4 = var1.zAxis;
                int var5 = var1.anIntArray2767[-1 + var1.anInt2816] * 128 - -(var1.getSize() * 64);
                int var6 = 128 * var1.anIntArray2755[-1 + var1.anInt2816] - -(var1.getSize() * 64);
                if (var5 + -var18 > 256 || -var18 + var5 < -256 || var6 - var4 > 256 || -256 > var6 - var4) {
                    var1.xAxis = var5;
                    var1.zAxis = var6;
                    return;
                }

                if (var5 <= var18) {
                    if (var18 <= var5) {
                        if (var6 <= var4) {
                            if (var6 < var4) {
                                var1.anInt2806 = 0;
                            }
                        } else {
                            var1.anInt2806 = 1024;
                        }
                    } else if (var6 > var4) {
                        var1.anInt2806 = 768;
                    } else if (var6 < var4) {
                        var1.anInt2806 = 256;
                    } else {
                        var1.anInt2806 = 512;
                    }
                } else if (var6 > var4) {
                    var1.anInt2806 = 1280;
                } else if (var4 > var6) {
                    var1.anInt2806 = 1792;
                } else {
                    var1.anInt2806 = 1536;
                }

                int var7 = 2047 & -var1.anInt2785 + var1.anInt2806;
                int var8 = var2.walk_follow_full_turn_anim;
                if (1024 < var7) {
                    var7 -= 2048;
                }

                boolean var10 = true;
                byte var11 = 1;
                if (var7 >= -256 && var7 <= 256) {
                    var8 = var2.walk_animation;
                } else if (var7 >= 256 && 768 > var7) {
                    var8 = var2.walk_follow_cw_turn_anim;
                } else if (var7 >= -768 && -256 >= var7) {
                    var8 = var2.walk_follow_ccw_turn_anim;
                }

                int var9 = 4;
                if (var8 == -1) {
                    var8 = var2.walk_animation;
                }

                var1.anInt2764 = var8;
                if (var1 instanceof NPC) {
                    var10 = ((NPC) var1).definition.aBoolean1255;
                }

                if (var10) {
                    if (var1.anInt2806 != var1.anInt2785 && var1.anInt2772 == -1 && var1.anInt2779 != 0) {
                        var9 = 2;
                    }

                    if (2 < var1.anInt2816) {
                        var9 = 6;
                    }

                    if (3 < var1.anInt2816) {
                        var9 = 8;
                    }

                    if (var1.anInt2824 > 0 && var1.anInt2816 > 1) {
                        var9 = 8;
                        --var1.anInt2824;
                    }
                } else {
                    if (1 < var1.anInt2816) {
                        var9 = 6;
                    }

                    if (var1.anInt2816 > 2) {
                        var9 = 8;
                    }

                    if (var1.anInt2824 > 0 && var1.anInt2816 > 1) {
                        --var1.anInt2824;
                        var9 = 8;
                    }
                }

                if (2 == var1.aByteArray2795[var1.anInt2816 - 1]) {
                    var9 <<= 1;
                    var11 = 2;
                } else if (var1.aByteArray2795[-1 + var1.anInt2816] == 0) {
                    var11 = 0;
                    var9 >>= 1;
                }

                if (var9 >= 8 && -1 != var2.run_anim) {
                    if (var1.anInt2764 == var2.walk_follow_full_turn_anim && -1 != var2.run_follow_full_turn_anim) {
                        var1.anInt2764 = var2.run_follow_full_turn_anim;
                    } else if (var1.anInt2764 == var2.walk_follow_ccw_turn_anim && var2.run_follow_ccw_turn_anim != -1) {
                        var1.anInt2764 = var2.run_follow_ccw_turn_anim;
                    } else if (var2.walk_follow_cw_turn_anim == var1.anInt2764 && var2.run_follow_cw_turn_anim != -1) {
                        var1.anInt2764 = var2.run_follow_cw_turn_anim;
                    } else {
                        var1.anInt2764 = var2.run_anim;
                    }
                } else if (var2.slow_walk_anim != -1 && var11 == 0) {
                    if (var1.anInt2764 == var2.walk_follow_full_turn_anim && var2.slow_walk_follow_full_turn_anim != -1) {
                        var1.anInt2764 = var2.slow_walk_follow_full_turn_anim;
                    } else if (var1.anInt2764 == var2.walk_follow_ccw_turn_anim && -1 != var2.slow_walk_follow_ccw_turn_anim) {
                        var1.anInt2764 = var2.slow_walk_follow_ccw_turn_anim;
                    } else if (var2.walk_follow_cw_turn_anim == var1.anInt2764 && -1 != var2.slow_walk_follow_cw_turn_anim) {
                        var1.anInt2764 = var2.slow_walk_follow_cw_turn_anim;
                    } else {
                        var1.anInt2764 = var2.slow_walk_anim;
                    }
                }

                if (var2.movement_acceleration != -1) {
                    var9 <<= 7;
                    if (var1.anInt2816 == 1) {
                        int var13 = (var1.xAxis <= var5 ? var5 - var1.xAxis : -var5 + var1.xAxis) << 7;
                        int var12 = var1.anInt2758 * var1.anInt2758;
                        int var14 = (var1.zAxis > var6 ? -var6 + var1.zAxis : -var1.zAxis + var6) << 7;
                        int var15 = var13 > var14 ? var13 : var14;
                        int var16 = var2.movement_acceleration * 2 * var15;
                        if (var12 <= var16) {
                            if (var12 / 2 > var15) {
                                var1.anInt2758 -= var2.movement_acceleration;
                                if (var1.anInt2758 < 0) {
                                    var1.anInt2758 = 0;
                                }
                            } else if (var1.anInt2758 < var9) {
                                var1.anInt2758 += var2.movement_acceleration;
                                if (var1.anInt2758 > var9) {
                                    var1.anInt2758 = var9;
                                }
                            }
                        } else {
                            var1.anInt2758 /= 2;
                        }
                    } else if (var1.anInt2758 < var9) {
                        var1.anInt2758 += var2.movement_acceleration;
                        if (var1.anInt2758 > var9) {
                            var1.anInt2758 = var9;
                        }
                    } else {
                        var1.anInt2758 -= var2.movement_acceleration;
                        if (0 > var1.anInt2758) {
                            var1.anInt2758 = 0;
                        }
                    }

                    var9 = var1.anInt2758 >> 7;
                    if (var9 < 1) {
                        var9 = 1;
                    }
                }

                if (var5 > var18) {
                    var1.xAxis += var9;
                    if (var1.xAxis > var5) {
                        var1.xAxis = var5;
                    }
                } else if (var18 > var5) {
                    var1.xAxis -= var9;
                    if (var1.xAxis < var5) {
                        var1.xAxis = var5;
                    }
                }

                if (var4 >= var6) {
                    if (var6 < var4) {
                        var1.zAxis -= var9;
                        if (var6 > var1.zAxis) {
                            var1.zAxis = var6;
                        }
                    }
                } else {
                    var1.zAxis += var9;
                    if (var6 < var1.zAxis) {
                        var1.zAxis = var6;
                    }
                }

                if (var5 == var1.xAxis && var6 == var1.zAxis) {
                    --var1.anInt2816;
                    if (0 < var1.anInt2811) {
                        --var1.anInt2811;
                    }
                }
            }

        } catch (RuntimeException var17) {
            throw ClientErrorException.clientError(var17, "ia.C(" + var0 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static int method1184(int var0, byte var1) {
        try {
            int var3 = var0 & 63;
            int var4 = (var0 & 217) >> 6;
            if (var3 == 18) {
                if (0 == var4) {
                    return 1;
                }

                if (var4 == 1) {
                    return 2;
                }

                if (var4 == 2) {
                    return 4;
                }

                return 8;
            } else if (var3 == 19 || var3 == 21) {
                if (var4 == 0) {
                    return 16;
                }

                if (1 == var4) {
                    return 32;
                }

                if (var4 == 2) {
                    return 64;
                }

                return 128;
            }

            return 0;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ia.A(" + var0 + ',' + var1 + ')');
        }
    }

    public static boolean method73(short var0) {
        try {
            if (var0 != 47 && var0 != 5 && var0 != 43 && var0 != 35 && var0 != 58 && var0 != 22 && var0 != 40 && var0 != 3) {
                if (var0 != 9 && var0 != 12 && var0 != 1006 && var0 != 1003) {

                    return var0 == 25 || var0 == 23 || 48 == var0 || var0 == 7 || var0 == 13 || (var0 == 8 || var0 == 32 || var0 == 28 || var0 == 59 || var0 == 51 || var0 == 41);
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "aa.C(" + var0 + ',' + 121 + ')');
        }
    }

    public static void method75(RSInterface[] var0, int var2) {
        try {
            for (int var3 = 0; var0.length > var3; ++var3) {
                RSInterface var4 = var0[var3];
                if (null != var4) {
                    if (var4.type == 0) {
                        if (null != var4.aClass11Array262) {
                            method75(var4.aClass11Array262, var2);
                        }

                        Class3_Sub31 var5 = TextureOperation23.aHashTable_3208.get(var4.componentHash);
                        if (null != var5) {
                            Class3_Sub8.method124(49, var2, var5.anInt2602);
                        }
                    }

                    CS2Script var7;
                    if (var2 == 0 && null != var4.anObjectArray206) {
                        var7 = new CS2Script();
                        var7.arguments = var4.anObjectArray206;
                        var7.aClass11_2449 = var4;
                        Class43.method1065(var7);
                    }

                    if (var2 == 1 && var4.anObjectArray176 != null) {
                        if (var4.anInt191 >= 0) {
                            RSInterface var8 = getRSInterface(var4.componentHash);
                            if (null == var8 || null == var8.aClass11Array262 || var8.aClass11Array262.length <= var4.anInt191 || var8.aClass11Array262[var4.anInt191] != var4) {
                                continue;
                            }
                        }

                        var7 = new CS2Script();
                        var7.arguments = var4.anObjectArray176;
                        var7.aClass11_2449 = var4;
                        Class43.method1065(var7);
                    }
                }
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "aa.I(" + (var0 != null ? "{...}" : "null") + ',' + true + ',' + var2 + ')');
        }
    }

    public static boolean method76(int var0, int var1, int var2, int var3, int var4, boolean var5, int var6, int var7, int var8, int var9, int var11) {
        try {
            int var12;
            int var13;
            for (var12 = 0; 104 > var12; ++var12) {
                for (var13 = 0; var13 < 104; ++var13) {
                    Class84.anIntArrayArray1160[var12][var13] = 0;
                    Class97.anIntArrayArray1373[var12][var13] = 99999999;
                }
            }

            Class84.anIntArrayArray1160[var11][var3] = 99;
            Class97.anIntArrayArray1373[var11][var3] = 0;
            var13 = var3;
            var12 = var11;
            byte var14 = 0;
            TextureOperation38.anIntArray3456[var14] = var11;
            boolean var16 = false;
            int var15 = 0;
            int var27 = var14 + 1;
            Class45.anIntArray729[var14] = var3;
            int[][] var17 = AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].anIntArrayArray1304;

            int var18;
            while (var15 != var27) {
                var12 = TextureOperation38.anIntArray3456[var15];
                var13 = Class45.anIntArray729[var15];
                var15 = 4095 & var15 - -1;
                if (var8 == var12 && var13 == var4) {
                    var16 = true;
                    break;
                }

                if (var1 != 0) {
                    if ((5 > var1 || var1 == 10) && AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].method1488(var4, var12, var13, var8, -1 + var1, 2, var7)) {
                        var16 = true;
                        break;
                    }

                    if (var1 < 10 && AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].method1492(var4, var1 + -1, var8, var13, 2, var7, var12, 88)) {
                        var16 = true;
                        break;
                    }
                }

                if (0 != var0 && var6 != 0 && AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].method1498(var8, var13, var12, 2, var0, var2, var4, var6)) {
                    var16 = true;
                    break;
                }

                var18 = Class97.anIntArrayArray1373[var12][var13] - -1;
                if (var12 > 0 && Class84.anIntArrayArray1160[var12 + -1][var13] == 0 && 0 == (var17[-1 + var12][var13] & 19661070) && (var17[-1 + var12][var13 + 1] & 19661112) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12 + -1;
                    Class45.anIntArray729[var27] = var13;
                    var27 = 1 + var27 & 4095;
                    Class84.anIntArrayArray1160[var12 - 1][var13] = 2;
                    Class97.anIntArrayArray1373[-1 + var12][var13] = var18;
                }

                if (102 > var12 && Class84.anIntArrayArray1160[1 + var12][var13] == 0 && 0 == (var17[2 + var12][var13] & 19661187) && 0 == (var17[var12 + 2][1 + var13] & 19661280)) {
                    TextureOperation38.anIntArray3456[var27] = 1 + var12;
                    Class45.anIntArray729[var27] = var13;
                    var27 = 4095 & var27 + 1;
                    Class84.anIntArrayArray1160[var12 - -1][var13] = 8;
                    Class97.anIntArrayArray1373[var12 - -1][var13] = var18;
                }

                if (var13 > 0 && Class84.anIntArrayArray1160[var12][var13 + -1] == 0 && (19661070 & var17[var12][-1 + var13]) == 0 && (var17[var12 + 1][var13 - 1] & 19661187) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12;
                    Class45.anIntArray729[var27] = -1 + var13;
                    Class84.anIntArrayArray1160[var12][-1 + var13] = 1;
                    Class97.anIntArrayArray1373[var12][-1 + var13] = var18;
                    var27 = 4095 & 1 + var27;
                }

                if (var13 < 102 && Class84.anIntArrayArray1160[var12][var13 + 1] == 0 && (var17[var12][2 + var13] & 19661112) == 0 && (var17[1 + var12][var13 + 2] & 19661280) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12;
                    Class45.anIntArray729[var27] = var13 + 1;
                    Class84.anIntArrayArray1160[var12][1 + var13] = 4;
                    var27 = 1 + var27 & 4095;
                    Class97.anIntArrayArray1373[var12][var13 - -1] = var18;
                }

                if (var12 > 0 && var13 > 0 && Class84.anIntArrayArray1160[-1 + var12][-1 + var13] == 0 && (19661112 & var17[-1 + var12][var13]) == 0 && 0 == (var17[-1 + var12][-1 + var13] & 19661070) && 0 == (19661187 & var17[var12][-1 + var13])) {
                    TextureOperation38.anIntArray3456[var27] = -1 + var12;
                    Class45.anIntArray729[var27] = -1 + var13;
                    Class84.anIntArrayArray1160[-1 + var12][-1 + var13] = 3;
                    Class97.anIntArrayArray1373[-1 + var12][-1 + var13] = var18;
                    var27 = 4095 & var27 + 1;
                }

                if (var12 < 102 && 0 < var13 && Class84.anIntArrayArray1160[1 + var12][var13 + -1] == 0 && (var17[var12 - -1][var13 - 1] & 19661070) == 0 && (19661187 & var17[2 + var12][var13 + -1]) == 0 && (19661280 & var17[var12 + 2][var13]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12 - -1;
                    Class45.anIntArray729[var27] = var13 - 1;
                    var27 = 4095 & 1 + var27;
                    Class84.anIntArrayArray1160[var12 - -1][-1 + var13] = 9;
                    Class97.anIntArrayArray1373[1 + var12][var13 + -1] = var18;
                }

                if (var12 > 0 && var13 < 102 && 0 == Class84.anIntArrayArray1160[var12 - 1][var13 + 1] && (var17[-1 + var12][var13 - -1] & 19661070) == 0 && 0 == (19661112 & var17[-1 + var12][var13 - -2]) && (19661280 & var17[var12][var13 + 2]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12 - 1;
                    Class45.anIntArray729[var27] = 1 + var13;
                    Class84.anIntArrayArray1160[var12 - 1][1 + var13] = 6;
                    Class97.anIntArrayArray1373[-1 + var12][1 + var13] = var18;
                    var27 = 1 + var27 & 4095;
                }

                if (var12 < 102 && var13 < 102 && Class84.anIntArrayArray1160[1 + var12][1 + var13] == 0 && (19661112 & var17[var12 - -1][2 + var13]) == 0 && (19661280 & var17[var12 + 2][var13 - -2]) == 0 && (19661187 & var17[var12 - -2][var13 + 1]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = 1 + var12;
                    Class45.anIntArray729[var27] = var13 + 1;
                    var27 = var27 - -1 & 4095;
                    Class84.anIntArrayArray1160[var12 - -1][var13 + 1] = 12;
                    Class97.anIntArrayArray1373[1 + var12][1 + var13] = var18;
                }
            }

            Class129.anInt1692 = 0;
            int var19;
            if (!var16) {
                if (!var5) {
                    return false;
                }

                var18 = 1000;
                var19 = 100;
                byte var20 = 10;

                for (int var21 = -var20 + var8; var20 + var8 >= var21; ++var21) {
                    for (int var22 = var4 + -var20; var20 + var4 >= var22; ++var22) {
                        if (var21 >= 0 && var22 >= 0 && var21 < 104 && 104 > var22 && Class97.anIntArrayArray1373[var21][var22] < 100) {
                            int var23 = 0;
                            int var24 = 0;
                            if (var8 <= var21) {
                                if (-1 + var0 + var8 < var21) {
                                    var23 = var21 + 1 + -var0 + -var8;
                                }
                            } else {
                                var23 = var8 - var21;
                            }

                            if (var22 >= var4) {
                                if (-1 + var4 + var6 < var22) {
                                    var24 = -var4 + -var6 + 1 + var22;
                                }
                            } else {
                                var24 = -var22 + var4;
                            }

                            int var25 = var23 * var23 + var24 * var24;
                            if (var25 < var18 || var25 == var18 && Class97.anIntArrayArray1373[var21][var22] < var19) {
                                var13 = var22;
                                var19 = Class97.anIntArrayArray1373[var21][var22];
                                var18 = var25;
                                var12 = var21;
                            }
                        }
                    }
                }

                if (var18 == 1000) {
                    return false;
                }

                if (var11 == var12 && var3 == var13) {
                    return false;
                }

                Class129.anInt1692 = 1;
            }

            byte var28 = 0;
            TextureOperation38.anIntArray3456[var28] = var12;
            var15 = var28 + 1;
            Class45.anIntArray729[var28] = var13;
            var18 = var19 = Class84.anIntArrayArray1160[var12][var13];

            for (; var12 != var11 || var13 != var3; var18 = Class84.anIntArrayArray1160[var12][var13]) {
                if (var19 != var18) {
                    TextureOperation38.anIntArray3456[var15] = var12;
                    Class45.anIntArray729[var15++] = var13;
                    var19 = var18;
                }

                if ((var18 & 2) != 0) {
                    ++var12;
                } else if ((8 & var18) != 0) {
                    --var12;
                }

                if ((var18 & 1) == 0) {
                    if ((var18 & 4) != 0) {
                        --var13;
                    }
                } else {
                    ++var13;
                }
            }

            if (var15 <= 0) {
                return 1 != var9;
            } else {
                TextureOperation7.method299(113, var15, var9);
                return true;
            }
        } catch (RuntimeException var26) {
            throw ClientErrorException.clientError(var26, "aa.A(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + 127 + ',' + var11 + ')');
        }
    }

    public static void method78(int var0, int var2) {
        try {
            TextureOperation12.outgoingBuffer.putOpcode(132);
            TextureOperation12.outgoingBuffer.writeIntV1(var2);
            TextureOperation12.outgoingBuffer.writeShortLE(var0);
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "aa.E(" + var0 + ',' + false + ',' + var2 + ')');
        }
    }

    public static void method81(RSInterface var1) {
        try {

            int var2 = var1.anInt189;
            if (324 == var2) {
                if (-1 == TextureOperation37.anInt3260) {
                    TextureOperation37.anInt3260 = var1.spriteArchiveId;
                    anInt1165 = var1.anInt296;
                }

                if (aClass52_1112.aBoolean864) {
                    var1.spriteArchiveId = TextureOperation37.anInt3260;
                } else {
                    var1.spriteArchiveId = anInt1165;
                }

            } else if (var2 == 325) {
                if (-1 == TextureOperation37.anInt3260) {
                    anInt1165 = var1.anInt296;
                    TextureOperation37.anInt3260 = var1.spriteArchiveId;
                }

                if (aClass52_1112.aBoolean864) {
                    var1.spriteArchiveId = anInt1165;
                } else {
                    var1.spriteArchiveId = TextureOperation37.anInt3260;
                }

            } else if (var2 == 327) {
                var1.anInt182 = 150;
                var1.anInt308 = 2047 & (int) (Math.sin((double) Class44.anInt719 / 40.0D) * 256.0D);
                var1.modelType = 5;
                var1.itemId = -1;
            } else if (var2 == 328) {
                if (null == Class102.player.displayName) {
                    var1.itemId = 0;
                } else {
                    var1.anInt182 = 150;
                    var1.anInt308 = 2047 & (int) (256.0D * Math.sin((double) Class44.anInt719 / 40.0D));
                    var1.modelType = 5;
                    var1.itemId = 2047 + ((int) Class102.player.displayName.toLong() << 11);
                    var1.anInt260 = Class102.player.anInt2793;
                    var1.anInt267 = 0;
                    var1.animationId = Class102.player.anInt2764;
                    var1.anInt283 = Class102.player.anInt2813;
                }
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "aa.H(" + (byte) -128 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    public static boolean method1088(boolean var0) {
        try {
            if (paramJavaScriptEnabled) {
                try {
                    TextCore.aClass94_106.method1577(Class38.gameSignlink.gameApplet);
                    return true;
                } catch (Throwable var2) {
                }
            }

            if (var0) {
                aReferenceCache_743 = null;
            }

            return false;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "gn.K(" + var0 + ')');
        }
    }

    public static Class3_Sub28_Sub5 method1089(CacheIndex cacheIndex0, CacheIndex var2, int frameId) {
        try {
            boolean var5 = true;
            int[] var6 = cacheIndex0.getFileIds(frameId);

            for (int var7 = 0; var7 < Objects.requireNonNull(var6).length; ++var7) {
                byte[] var8 = cacheIndex0.method2140(var6[var7], frameId);
                if (var8 == null) {
                    var5 = false;
                } else {
                    int var9 = (255 & var8[0]) << 8 | var8[1] & 0xFF;
                    byte[] var10;
                    var10 = var2.method2140(0, var9);

                    if (null == var10) {
                        // System.out.println("Roar , " + var9);
                        var5 = false;
                    }
                }
            }
            if (var5) {
                try {
                    return new Class3_Sub28_Sub5(cacheIndex0, var2, frameId);
                } catch (Exception var11) {
                    var11.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "gn.J(" + (cacheIndex0 != null ? "{...}" : "null") + ',' + false + ',' + (var2 != null ? "{...}" : "null") + ',' + (byte) 118 + ',' + frameId + ')');
        }
    }

    public static RSString emptyString(int capacit) {
        RSString str = new RSString();
        str.length = 0;
        str.buffer = new byte[capacit];
        return str;
    }

    public static void method1091(boolean var0, int var1) {
        try {
            byte var2;
            byte[][] var3;
            if (HDToolKit.highDetail && var0) {
                var2 = 1;
                var3 = Class40.aByteArrayArray3669;
            } else {
                var2 = 4;
                var3 = Class164_Sub2.aByteArrayArray3027;
            }

            int var4 = var3.length;

            int var5;
            int var6;
            int var7;
            byte[] var8;
            for (var5 = 0; var5 < var4; ++var5) {
                var6 = -Class131.anInt1716 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var5] >> 8);
                var7 = -Texture.anInt1152 + 64 * (255 & Class3_Sub24_Sub3.anIntArray3494[var5]);
                var8 = var3[var5];
                if (null != var8) {
                    Class58.method1194();
                    method777(AtmosphereParser.aClass91Array1182, var0, -48 + 8 * anInt3606, var7, var6, (Class3_Sub7.anInt2294 + -6) * 8, var8);
                }
            }

            var5 = 0;
            if (var1 > -66) {
                method1088(true);
            }

            for (; var5 < var4; ++var5) {
                var6 = -Class131.anInt1716 + 64 * (Class3_Sub24_Sub3.anIntArray3494[var5] >> 8);
                var7 = -Texture.anInt1152 + 64 * (255 & Class3_Sub24_Sub3.anIntArray3494[var5]);
                var8 = var3[var5];
                if (var8 == null && Class3_Sub7.anInt2294 < 800) {
                    Class58.method1194();

                    for (int var9 = 0; var9 < var2; ++var9) {
                        Class12.method870(var9, (byte) 102, var7, var6, 64, 64);
                    }
                }
            }

        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "gn.I(" + var0 + ',' + var1 + ')');
        }
    }

    public static void method1093(boolean var0) {
        try {
            if (var0) {
                method1093(true);
            }

            for (int var1 = 0; var1 < 100; ++var1) {
                aBooleanArray3674[var1] = true;
            }

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "gn.H(" + var0 + ')');
        }
    }

    public static void method1095(int var0, int var1, int var2, RSInterface[] var3, int var4, int var5, int var6, int var7, byte var8, int var9) {
        try {
            if (HDToolKit.highDetail) {
                Class22.setClipping(var0, var6, var4, var7);
            } else {
                Class74.setClipping(var0, var6, var4, var7);
                Class51.method1134();
            }

            for (RSInterface var11 : var3) {
                if (var11 != null && (var5 == var11.parentId || var5 == -1412584499 && var11 == Class56.aClass11_886)) {
                    int var12;
                    if (var9 == -1) {
                        AudioChannel.anIntArray1969[Class3_Sub28_Sub3.anInt3557] = var2 + var11.anInt306;
                        Player.anIntArray3954[Class3_Sub28_Sub3.anInt3557] = var11.anInt210 - -var1;
                        Class3_Sub28_Sub18.anIntArray3768[Class3_Sub28_Sub3.anInt3557] = var11.width;
                        Class140_Sub4.anIntArray2794[Class3_Sub28_Sub3.anInt3557] = var11.height;
                        var12 = Class3_Sub28_Sub3.anInt3557++;
                    } else {
                        var12 = var9;
                    }

                    var11.anInt204 = Class44.anInt719;
                    var11.anInt292 = var12;
                    if (!var11.usingScripts || !Client.method51(var11)) {
                        if (0 < var11.anInt189) {
                            method81(var11);
                        }

                        int var14 = var1 + var11.anInt210;
                        int alpha = var11.anInt223;
                        int var13 = var11.anInt306 + var2;
                        if (ClientCommands.commandQaOpEnabled && (0 != Client.method44(var11).anInt2205 || var11.type == 0) && alpha > 127) {
                            alpha = 127;
                        }

                        int var17;
                        int var16;
                        if (var11 == Class56.aClass11_886) {
                            if (var5 != -1412584499 && !var11.aBoolean200) {
                                anInt3602 = var2;
                                anInt1082 = var1;
                                Class8.aClass11Array1836 = var3;
                                continue;
                            }

                            if (NPC.aBoolean3975 && Class85.aBoolean1167) {
                                var17 = anInt1709;
                                var16 = Class126.anInt1676;
                                var17 -= Class95.anInt1336;
                                if (var17 < Class134.anInt1761) {
                                    var17 = Class134.anInt1761;
                                }

                                if (PacketParser.aClass11_88.height + Class134.anInt1761 < var17 + var11.height) {
                                    var17 = -var11.height + PacketParser.aClass11_88.height + Class134.anInt1761;
                                }

                                var14 = var17;
                                var16 -= anInt1881;
                                if (TextureOperation20.anInt3156 > var16) {
                                    var16 = TextureOperation20.anInt3156;
                                }

                                if (var11.width + var16 > PacketParser.aClass11_88.width + TextureOperation20.anInt3156) {
                                    var16 = -var11.width + PacketParser.aClass11_88.width + TextureOperation20.anInt3156;
                                }

                                var13 = var16;
                            }

                            if (!var11.aBoolean200) {
                                alpha = 128;
                            }
                        }

                        int var19;
                        int var18;
                        int var21;
                        int var20;
                        if (2 == var11.type) {
                            var19 = var7;
                            var18 = var4;
                            var17 = var6;
                            var16 = var0;
                        } else {
                            var17 = var6 < var14 ? var14 : var6;
                            var16 = var13 > var0 ? var13 : var0;
                            var20 = var11.width + var13;
                            var21 = var14 - -var11.height;
                            if (var11.type == 9) {
                                ++var21;
                                ++var20;
                            }

                            var19 = var7 <= var21 ? var7 : var21;
                            var18 = var4 <= var20 ? var4 : var20;
                        }

                        if (!var11.usingScripts || var16 < var18 && var19 > var17) {
                            int var23;
                            int var22;
                            int var25;
                            int var24;
                            int var26;
                            int var29;
                            int var28;
                            int var47;
                            if (var11.anInt189 != 0) {
                                if (var11.anInt189 == 1337 || var11.anInt189 == 1403 && HDToolKit.highDetail) {
                                    aClass11_2091 = var11;
                                    anInt2567 = var14;
                                    Class53.anInt865 = var13;
                                    method338(var11.height, var11.anInt189 == 1403, var13, var11.width, var14);
                                    if (HDToolKit.highDetail) {
                                        Class22.setClipping(var0, var6, var4, var7);
                                    } else {
                                        Class74.setClipping(var0, var6, var4, var7);
                                    }
                                    continue;
                                }

                                if (var11.anInt189 == 1338) {
                                    if (!var11.method855()) {
                                        continue;
                                    }

                                    Minimap.displayMinimap(var12, (byte) 59, var14, var13, var11);
                                    if (HDToolKit.highDetail) {
                                        Class22.setClipping(var0, var6, var4, var7);
                                    } else {
                                        Class74.setClipping(var0, var6, var4, var7);
                                    }

                                    if (0 != Class161.anInt2028 && 3 != Class161.anInt2028 || Class38_Sub1.aBoolean2615 || var16 > NPCDefinition.anInt1297 || var17 > Class38_Sub1.anInt2612 || var18 <= NPCDefinition.anInt1297 || Class38_Sub1.anInt2612 >= var19) {
                                        continue;
                                    }

                                    var20 = NPCDefinition.anInt1297 - var13;
                                    var21 = -var14 + Class38_Sub1.anInt2612;
                                    var22 = var11.anIntArray207[var21];
                                    if (var22 > var20 || var22 + var11.anIntArray291[var21] < var20) {
                                        continue;
                                    }

                                    var21 -= var11.height / 2;
                                    var23 = 2047 & GraphicDefinition.CAMERA_DIRECTION - -TextureOperation9.anInt3102;
                                    var20 -= var11.width / 2;
                                    var24 = Class51.anIntArray840[var23];
                                    var25 = Class51.anIntArray851[var23];
                                    var24 = (Class164_Sub2.anInt3020 + 256) * var24 >> 8;
                                    var25 = (Class164_Sub2.anInt3020 - -256) * var25 >> 8;
                                    var47 = -(var24 * var20) + var25 * var21 >> 11;
                                    var26 = var21 * var24 - -(var20 * var25) >> 11;
                                    var28 = Class102.player.xAxis + var26 >> 7;
                                    var29 = -var47 + Class102.player.zAxis >> 7;
                                    if (GameObject.aBoolean1837 && 0 != (Class164.anInt2051 & 64)) {
                                        RSInterface var53 = AbstractSprite.method638(BufferedDataStream.anInt872, RSInterface.anInt278);
                                        if (null == var53) {
                                            Class25.method958((byte) -91);
                                        } else {
                                            Class3_Sub24_Sub4.method1177(anInt1887, 1L, (byte) -49, TextCore.aClass94_1724, var28, (short) 11, Class3_Sub28_Sub9.aClass94_3621, var29);
                                        }
                                        continue;
                                    }

                                    if (Class158.paramGameTypeID == 1) {
                                        Class3_Sub24_Sub4.method1177(-1, 1L, (byte) -41, RSString.parse(""), var28, (short) 36, TextCore.HasFaceHere, var29);
                                    }

                                    Class3_Sub24_Sub4.method1177(-1, 1L, (byte) -125, RSString.parse(""), var28, (short) 60, TextureOperation32.aClass94_3353, var29);
                                    continue;
                                }

                                if (var11.anInt189 == 1339) {
                                    if (var11.method855()) {
                                        Compass.drawCompass(var13, var14, var11, var12);
                                        if (HDToolKit.highDetail) {
                                            Class22.setClipping(var0, var6, var4, var7);
                                        } else {
                                            Class74.setClipping(var0, var6, var4, var7);
                                        }
                                    }
                                    continue;
                                }

                                if (var11.anInt189 == 1400) {
                                    WorldMap.drawWorldMap(var13, var14, var11.height, var11.width);
                                    aBooleanArray3674[var12] = true;
                                    Class163_Sub1_Sub1.aBooleanArray4008[var12] = true;
                                    if (HDToolKit.highDetail) {
                                        Class22.setClipping(var0, var6, var4, var7);
                                    } else {
                                        Class74.setClipping(var0, var6, var4, var7);
                                    }
                                    continue;
                                }

                                if (var11.anInt189 == 1401) {
                                    Class1.worldMapMinimap(var13, var11.height, var11.width, var14);
                                    aBooleanArray3674[var12] = true;
                                    Class163_Sub1_Sub1.aBooleanArray4008[var12] = true;
                                    if (HDToolKit.highDetail) {
                                        Class22.setClipping(var0, var6, var4, var7);
                                    } else {
                                        Class74.setClipping(var0, var6, var4, var7);
                                    }
                                    continue;
                                }

                                if (1402 == var11.anInt189) {
                                    if (!HDToolKit.highDetail) {
                                        Class129.method1768(var13, var14);
                                        aBooleanArray3674[var12] = true;
                                        Class163_Sub1_Sub1.aBooleanArray4008[var12] = true;
                                    }
                                    continue;
                                }

                                if (var11.anInt189 == 1405) {
                                    if (DeveloperConsole.INSTANCE.getOpen()) {
                                        gameWindowWidth = var11.width + var13;
                                    }

                                    if (ClientCommands.fpsOverlayEnabled) {
                                        var20 = var11.width + var13;
                                        var21 = 15 + var14;
                                        FontType.plainFont.drawStringRightAnchor(RSString.stringCombiner(new RSString[]{RSString.parse("Fps:"), RSString.stringAnimator(SequenceDefinition.anInt1862)}), var20, var21, 16776960, -1);//Class72.stringAnimator(SequenceDefinition.anInt1862)}), var20, var21, 16776960, -1);
                                        var21 += 15;
                                        Runtime var57 = Runtime.getRuntime();
                                        var23 = (int) ((var57.totalMemory() + -var57.freeMemory()) / 1024L);
                                        var24 = 16776960;
                                        if (var23 > 65536) {
                                            var24 = 16711680;
                                        }

                                        FontType.plainFont.drawStringRightAnchor(RSString.stringCombiner(new RSString[]{TextCore.aClass94_4057, RSString.stringAnimator(var23), TextCore.Memoryk}), var20, var21, var24, -1);
                                        var21 += 15;
                                        if (HDToolKit.highDetail) {
                                            var24 = 16776960;
                                            var25 = (Class31.anInt580 + Class31.anInt585 + Class31.memory2D) / 1024;
                                            if (65536 < var25) {
                                                var24 = 16711680;
                                            }

                                            FontType.plainFont.drawStringRightAnchor(RSString.stringCombiner(new RSString[]{TextCore.aClass94_1622, RSString.stringAnimator(var25), TextCore.Memoryk}), var20, var21, var24, -1);
                                            var21 += 15;
                                        }

                                        var24 = 16776960;
                                        var25 = 0;
                                        var47 = 0;
                                        var26 = 0;

                                        for (var28 = 0; var28 < 29; ++var28) { //TODO:
                                            var25 += aClass151_Sub1Array2601[var28].method2108();
                                            var26 += aClass151_Sub1Array2601[var28].method2102();
                                            var47 += aClass151_Sub1Array2601[var28].method2106();
                                        }

                                        var29 = 10000 * var26 / var25;
                                        var28 = var47 * 100 / var25;
                                        RSString var55 = RSString.stringCombiner(new RSString[]{TextCore.aClass94_436, Class3_Sub23.method407(0, true, 2, var29), TextCore.aClass94_2498, RSString.stringAnimator(var28), RSString.parse("(U(Y")});
                                        FontType.smallFont.drawStringRightAnchor(var55, var20, var21, var24, -1);
                                        aBooleanArray3674[var12] = true;
                                        Class163_Sub1_Sub1.aBooleanArray4008[var12] = true;
                                        continue;
                                    }
                                    if (ClientCommands.renderInfoOverlayEnabled) {
                                        if (HDToolKit.highDetail) {
                                            int vendorColor;
                                            String vendor = HDToolKit.gl.glGetString(7936);
                                            if (vendor.contains("NVIDIA")) {
                                                vendorColor = 7780608;
                                            } else if (vendor.contains("AMD")) {
                                                vendorColor = 15539236;
                                            } else if (vendor.contains("INTEL")) {
                                                vendorColor = 29125;
                                            } else {
                                                vendorColor = 16776960;
                                            }
                                            var20 = var11.width + var13;
                                            var21 = 15 + var14;
                                            FontType.plainFont.drawStringRightAnchor(RSString.parse(HDToolKit.gl.glGetString(7938)), var20, var21, 16776960, 7);
                                            var21 += 15;
                                            FontType.plainFont.drawStringRightAnchor(RSString.parse(vendor), var20, var21, vendorColor, 4);
                                            var21 += 15;
                                            FontType.plainFont.drawStringRightAnchor(RSString.parse(HDToolKit.gl.glGetString(7937)), var20, var21, 16776960, 7);
                                            var21 += 15;
                                            FontType.plainFont.drawStringRightAnchor(RSString.parse("Oncard Geometry: " + Class31.anInt585), var20, var21, 16776960, 7);
                                            var21 += 15;
                                            FontType.plainFont.drawStringRightAnchor(RSString.parse("Oncard 2D: " + Class31.memory2D), var20, var21, 16776960, 7);
                                            var21 += 15;
                                            FontType.plainFont.drawStringRightAnchor(RSString.parse("Oncard Texture: " + Class31.anInt580), var20, var21, 16776960, 7);
                                        } else {
                                            var20 = var11.width + var13;
                                            var21 = 15 + var14;
                                            FontType.plainFont.drawStringRightAnchor(RSString.parse("Java Toolkit"), var20, var21, 16776960, 7);
                                            var21 += 15;
                                            FontType.plainFont.drawStringRightAnchor(RSString.parse(System.getProperty("java.vendor")), var20, var21, 16776960, 7);
                                            var21 += 15;
                                            FontType.plainFont.drawStringRightAnchor(RSString.parse(System.getProperty("java.version")), var20, var21, 16776960, 7);
                                        }
                                        continue;
                                    }
                                    XPGainDraw.drawGains();
                                    SlayerTracker.draw();
                                    continue;
                                }

                                if (var11.anInt189 == 1406) {
                                    ClientErrorException.anInt2115 = var14;
                                    AbstractSprite.aClass11_3708 = var11;
                                    TextureOperation18.anInt4041 = var13;
                                    continue;
                                }
                            }

                            if (!Class38_Sub1.aBoolean2615) {
                                if (var11.type == 0 && var11.aBoolean219 && NPCDefinition.anInt1297 >= var16 && var17 <= Class38_Sub1.anInt2612 && NPCDefinition.anInt1297 < var18 && Class38_Sub1.anInt2612 < var19 && !ClientCommands.commandQaOpEnabled) {
                                    menuOptionCount = 1;
                                    Class114.anIntArray1578[0] = Class3_Sub28_Sub5.anInt3590;
                                    Class140_Sub7.aClass94Array2935[0] = TextCore.HasCancel;
                                    Class163_Sub2_Sub1.aClass94Array4016[0] = RSString.parse("");
                                    TextureOperation27.aShortArray3095[0] = 1005;
                                }

                                if (var16 <= NPCDefinition.anInt1297 && Class38_Sub1.anInt2612 >= var17 && var18 > NPCDefinition.anInt1297 && Class38_Sub1.anInt2612 < var19) {
                                    Class3_Sub24_Sub4.method477(Class38_Sub1.anInt2612 + -var14, -var13 + NPCDefinition.anInt1297, var11);
                                }
                            }

                            if (var11.type == 0) {
                                if (!var11.usingScripts && Client.method51(var11) && Class107.aClass11_1453 != var11) {
                                    continue;
                                }

                                if (!var11.usingScripts) {
                                    if (var11.anInt208 > -var11.height + var11.anInt252) {
                                        var11.anInt208 = -var11.height + var11.anInt252;
                                    }

                                    if (0 > var11.anInt208) {
                                        var11.anInt208 = 0;
                                    }
                                }

                                method1095(var16, -var11.anInt208 + var14, -var11.anInt247 + var13, var3, var18, var11.componentHash, var17, var19, (byte) 87, var12);
                                if (null != var11.aClass11Array262) {
                                    method1095(var16, -var11.anInt208 + var14, -var11.anInt247 + var13, var11.aClass11Array262, var18, var11.componentHash, var17, var19, (byte) 52, var12);
                                }

                                Class3_Sub31 var36 = TextureOperation23.aHashTable_3208.get(var11.componentHash);
                                if (var36 != null) {
                                    if (var36.anInt2603 == 0 && !Class38_Sub1.aBoolean2615 && NPCDefinition.anInt1297 >= var16 && Class38_Sub1.anInt2612 >= var17 && NPCDefinition.anInt1297 < var18 && Class38_Sub1.anInt2612 < var19 && !ClientCommands.commandQaOpEnabled) {
                                        Class140_Sub7.aClass94Array2935[0] = TextCore.HasCancel;
                                        menuOptionCount = 1;
                                        Class114.anIntArray1578[0] = Class3_Sub28_Sub5.anInt3590;
                                        TextureOperation27.aShortArray3095[0] = 1005;
                                        Class163_Sub2_Sub1.aClass94Array4016[0] = RSString.parse("");
                                    }

                                    TextureOperation12.method171(-101, var36.anInt2602, var16, var18, var13, var12, var19, var17, var14);
                                }

                                if (HDToolKit.highDetail) {
                                    Class22.setClipping(var0, var6, var4, var7);
                                } else {
                                    Class74.setClipping(var0, var6, var4, var7);
                                    Class51.method1134();
                                }
                            }

                            if (aBooleanArray1712[var12] || Client.rectDebugInt > 1) {
                                if (var11.type == 0 && !var11.usingScripts && var11.anInt252 > var11.height) {
                                    TextureOperation24.method224(var11.anInt208, var11.anInt252, var11.width + var13, var14, var11.height);
                                }

                                if (var11.type != 1) {
                                    boolean var39;
                                    boolean var46;
                                    if (var11.type == 2) {
                                        var20 = 0;

                                        for (var21 = 0; var11.defHeight > var21; ++var21) {
                                            for (var22 = 0; var11.defWidth > var22; ++var22) {
                                                var24 = var14 + var21 * (32 - -var11.anInt290);
                                                var23 = (var11.anInt285 + 32) * var22 + var13;
                                                if (var20 < 20) {
                                                    var24 += var11.anIntArray300[var20];
                                                    var23 += var11.anIntArray272[var20];
                                                }

                                                if (var11.itemAmounts[var20] <= 0) {
                                                    if (null != var11.anIntArray197 && var20 < 20) {
                                                        AbstractSprite var58 = var11.method859(var20);
                                                        if (null == var58) {
                                                            if (RSInterface.aBoolean6) {
                                                                Class20.method909(var11);
                                                            }
                                                        } else {
                                                            var58.drawAt(var23, var24);
                                                        }
                                                    }
                                                } else {
                                                    var47 = var11.itemAmounts[var20] + -1;
                                                    if (var0 < 32 + var23 && var4 > var23 && var24 - -32 > var6 && var7 > var24 || var11 == Class67.aClass11_1017 && var20 == PacketParser.anInt86) {
                                                        AbstractSprite var54;
                                                        if (Class164_Sub1.anInt3012 == 1 && BufferedDataStream.anInt1473 == var20 && Class3_Sub28_Sub18.anInt3764 == var11.componentHash) {
                                                            var54 = Class114.method1707(2, var47, var11.aBoolean227, var11.itemIds[var20], 0);
                                                        } else {
                                                            var54 = Class114.method1707(1, var47, var11.aBoolean227, var11.itemIds[var20], 3153952);
                                                        }

                                                        if (Class51.aBoolean837) {
                                                            aBooleanArray3674[var12] = true;
                                                        }

                                                        if (null == var54) {
                                                            Class20.method909(var11);
                                                        } else if (Class67.aClass11_1017 == var11 && var20 == PacketParser.anInt86) {
                                                            var25 = Class126.anInt1676 - Class129_Sub1.anInt2693;
                                                            var26 = -anInt40 + anInt1709;
                                                            if (var26 < 5 && var26 > -5) {
                                                                var26 = 0;
                                                            }

                                                            if (var25 < 5 && var25 > -5) {
                                                                var25 = 0;
                                                            }

                                                            if (5 > Class40.anInt677) {
                                                                var25 = 0;
                                                                var26 = 0;
                                                            }

                                                            var54.method637(var23 + var25, var24 - -var26, 128);
                                                            if (var5 != -1) {
                                                                RSInterface var51 = var3[var5 & 65535];
                                                                int var31;
                                                                int var30;
                                                                if (HDToolKit.highDetail) {
                                                                    var31 = Class22.anInt451;
                                                                    var30 = Class22.anInt448;
                                                                } else {
                                                                    var30 = Toolkit.JAVA_TOOLKIT.clipTop;
                                                                    var31 = Toolkit.JAVA_TOOLKIT.clipBottom;
                                                                }

                                                                int var32;
                                                                if (var26 + var24 < var30 && var51.anInt208 > 0) {
                                                                    var32 = Class106.anInt1446 * (-var26 + var30 + -var24) / 3;
                                                                    if (Class106.anInt1446 * 10 < var32) {
                                                                        var32 = 10 * Class106.anInt1446;
                                                                    }

                                                                    if (var32 > var51.anInt208) {
                                                                        var32 = var51.anInt208;
                                                                    }

                                                                    var51.anInt208 -= var32;
                                                                    anInt40 += var32;
                                                                    Class20.method909(var51);
                                                                }

                                                                if (var31 < 32 + var26 + var24 && var51.anInt208 < -var51.height + var51.anInt252) {
                                                                    var32 = (-var31 + 32 + (var24 - -var26)) * Class106.anInt1446 / 3;
                                                                    if (var32 > Class106.anInt1446 * 10) {
                                                                        var32 = 10 * Class106.anInt1446;
                                                                    }

                                                                    if (-var51.anInt208 + var51.anInt252 + -var51.height < var32) {
                                                                        var32 = var51.anInt252 + -var51.height + -var51.anInt208;
                                                                    }

                                                                    var51.anInt208 += var32;
                                                                    anInt40 -= var32;
                                                                    Class20.method909(var51);
                                                                }
                                                            }
                                                        } else if (var11 == aClass11_1933 && var20 == KeyboardListener.anInt1918) {
                                                            var54.method637(var23, var24, 128);
                                                        } else {
                                                            var54.drawAt(var23, var24);
                                                        }
                                                    }
                                                }

                                                ++var20;
                                            }
                                        }
                                    } else if (3 == var11.type) {
                                        if (method609(var11, 26)) {
                                            var20 = var11.anInt253;
                                            if (Class107.aClass11_1453 == var11 && 0 != var11.anInt222) {
                                                var20 = var11.anInt222;
                                            }
                                        } else {
                                            var20 = var11.anInt218;
                                            if (var11 == Class107.aClass11_1453 && 0 != var11.anInt228) {
                                                var20 = var11.anInt228;
                                            }
                                        }

                                        /* INTERFACE DRAWS ALPHA/NON-ALPHA BOXES/BACKGROUNDS */
                                        if (alpha != 0) {
                                            if (var11.aBoolean226) {
                                                if (HDToolKit.highDetail) {
                                                    Toolkit.OPENGL_TOOLKIT.fillRect(var13, var14, var11.width, var11.height, var20, 256 + -(255 & alpha));//Magic book spell tool-tip hover background draw
                                                } else {
                                                    Toolkit.JAVA_TOOLKIT.fillRect(var13, var14, var11.width, var11.height, var20, 256 + -(255 & alpha));
                                                }
                                            } else if (HDToolKit.highDetail) {
                                                Class22.method928(var13, var14, var11.width, var11.height, var20, 256 + -(alpha & 0xFF));//Interface backgrounds (includes dark areas)
                                            } else {
                                                Class74.method1315(var13, var14, var11.width, var11.height, var20, -(alpha & 0xFF) + 256);//Interface backgrounds (includes dark areas)
                                            }
                                        } else if (var11.aBoolean226) {
                                            if (HDToolKit.highDetail) {
                                                Toolkit.OPENGL_TOOLKIT.method934(var13, var14, var11.width, var11.height, var20);//Skill tool-tip hover background draw
                                            } else {
                                                Toolkit.JAVA_TOOLKIT.fillRect(var13, var14, var11.width, var11.height, var20, 255);//Skill tool-tip pop up background
                                            }
                                        } else if (HDToolKit.highDetail) {
                                            Toolkit.OPENGL_TOOLKIT.drawRect(var13, var14, var11.width, var11.height, var20, 255);//Boxed border background draw
                                        } else {
                                            Toolkit.JAVA_TOOLKIT.drawRect(var13, var14, var11.width, var11.height, var20, 255);//Boxed border background draw
                                        }
                                        // TODO DRAWRECT
//                                        if (Class3_Sub28_Sub12.method609(var11, 26)) {
//                                            var20 = var11.anInt253;
//                                            if (Class107.aClass11_1453 == var11 && 0 != var11.anInt222) {
//                                                var20 = var11.anInt222;
//                                            }
//                                        } else {
//                                            var20 = var11.anInt218;
//                                            if (var11 == Class107.aClass11_1453 && 0 != var11.anInt228) {
//                                                var20 = var11.anInt228;
//                                            }
//                                        }
//
//                                        if (var15 != 0) {
//                                            if (var11.aBoolean226) {
//                                                Toolkit.getActiveToolkit().fillRect(var13, var14, var11.width, var11.height, var20, 256 - (var15 & 0xFF));//Background for mage book
//                                            } else if (HDToolKit.highDetail) {
//                                                Class22.method928(var13, var14, var11.width, var11.height, var20, 256 + -(var15 & 0xFF));
//                                            } else {
//                                                Class74.method1315(var13, var14, var11.width, var11.height, var20, -(var15 & 0xFF) + 256);
//                                            }
//                                        } else if (var11.aBoolean226) {
//                                            Toolkit.getActiveToolkit().method934(var13, var14, var11.width, var11.height, var20);//Fill Rectangle for skill hover
//                                        } else {
//                                            Toolkit.getActiveToolkit().drawRect(var13, var14, var11.width, var11.height, var20, 255);//Background border for mage book
//                                        }

                                    } else {
                                        Font var34;
                                        if (var11.type == 4) {
                                            // TODO RSInterfaceRenderText
                                            var34 = var11.method868(Sprites.nameIconsSpriteArray);
                                            if (var34 != null) {
                                                RSString var45 = var11.text;
                                                if (method609(var11, 97)) {
                                                    var21 = var11.anInt253;
                                                    if (Class107.aClass11_1453 == var11 && var11.anInt222 != 0) {
                                                        var21 = var11.anInt222;
                                                    }

                                                    if (var11.aClass94_172.length() > 0) {
                                                        var45 = var11.aClass94_172;
                                                    }
                                                } else {
                                                    var21 = var11.anInt218;
                                                    if (Class107.aClass11_1453 == var11 && var11.anInt228 != 0) {
                                                        var21 = var11.anInt228;
                                                    }
                                                }

                                                if (var11.usingScripts && var11.anInt192 != -1) {
                                                    ItemDefinition var50 = ItemDefinition.getItemDefinition(var11.anInt192);
                                                    var45 = var50.name;
                                                    if (var45 == null) {
                                                        var45 = TextCore.HasNull;
                                                    }

                                                    if ((var50.stackingType == 1 || var11.anInt271 != 1) && var11.anInt271 != -1) {
                                                        var45 = RSString.stringCombiner(new RSString[]{ColorCore.ItemInterfaceColor, var45, TextCore.aClass94_2306, Class36.method1013((byte) -125, var11.anInt271)});
                                                    }
                                                }

                                                if (TextureOperation27.aClass11_3087 == var11) {
                                                    var21 = var11.anInt218;
                                                    var45 = TextCore.LoadingPleaseWait;
                                                }

                                                if (!var11.usingScripts) {
                                                    var45 = method1303(var11, var45);
                                                }

                                                var34.method676(var45, var13, var14, var11.width, var11.height, var21, !var11.aBoolean215 ? -1 : 0, var11.anInt194, var11.anInt225, var11.anInt205);
                                            } else if (RSInterface.aBoolean6) {
                                                Class20.method909(var11);
                                            }
                                        } else if (5 == var11.type) {
                                            AbstractSprite var37;
                                            if (var11.usingScripts) {
                                                if (var11.anInt192 == -1) {
                                                    var37 = var11.method866(false);
                                                } else {
                                                    var37 = Class114.method1707(var11.anInt288, var11.anInt192, var11.aBoolean227, var11.anInt271, var11.anInt287);
                                                }

                                                if (var37 == null) {
                                                    if (RSInterface.aBoolean6) {
                                                        Class20.method909(var11);
                                                    }
                                                } else {
                                                    var21 = var37.anInt3697;
                                                    var22 = var37.anInt3706;
                                                    if (var11.aBoolean186) {
                                                        var23 = (var21 + -1 + var11.width) / var21;
                                                        var24 = (var11.height - 1 - -var22) / var22;
                                                        if (HDToolKit.highDetail) {
                                                            Class22.method931(var13, var14, var11.width + var13, var11.height + var14);
                                                            var39 = Class140_Sub6.method2021((byte) -94, var37.width);
                                                            var46 = Class140_Sub6.method2021((byte) -113, var37.height);
                                                            HDSprite var27 = (HDSprite) var37;
                                                            if (var39 && var46) {
                                                                if (alpha == 0) {
                                                                    var27.method649(var13, var14, var23, var24);
                                                                } else {
                                                                    var27.method646(var13, var14, -(255 & alpha) + 256, var23, var24);
                                                                }
                                                            } else if (var39) {
                                                                for (var28 = 0; var28 < var24; ++var28) {
                                                                    if (alpha == 0) {
                                                                        var27.method649(var13, var28 * var22 + var14, var23, 1);
                                                                    } else {
                                                                        var27.method646(var13, var14 + var28 * var22, 256 + -(alpha & 0xFF), var23, 1);
                                                                    }
                                                                }
                                                            } else if (var46) {
                                                                for (var28 = 0; var28 < var23; ++var28) {
                                                                    if (alpha == 0) {
                                                                        var27.method649(var21 * var28 + var13, var14, 1, var24);
                                                                    } else {
                                                                        var27.method646(var21 * var28 + var13, var14, -(alpha & 0xFF) + 256, 1, var24);
                                                                    }
                                                                }
                                                            } else {
                                                                for (var28 = 0; var23 > var28; ++var28) {
                                                                    for (var29 = 0; var29 < var24; ++var29) {
                                                                        if (alpha == 0) {
                                                                            var37.drawAt(var13 - -(var21 * var28), var22 * var29 + var14);
                                                                        } else {
                                                                            var37.method637(var28 * var21 + var13, var22 * var29 + var14, -(255 & alpha) + 256);
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            Class22.setClipping(var0, var6, var4, var7);
                                                        } else {
                                                            Class74.method1326(var13, var14, var13 - -var11.width, var14 - -var11.height);

                                                            for (var25 = 0; var25 < var23; ++var25) {
                                                                for (var26 = 0; var24 > var26; ++var26) {
                                                                    if (var11.anInt301 == 0) {
                                                                        if (0 == alpha) {
                                                                            var37.drawAt(var25 * var21 + var13, var22 * var26 + var14);
                                                                        } else {
                                                                            var37.method637(var25 * var21 + var13, var14 + var26 * var22, 256 - (255 & alpha));
                                                                        }
                                                                    } else {
                                                                        var37.drawScaledOrRotated(var14 - -(var22 * var26) + var22 / 2, var11.anInt301, 4096, var25 * var21 + var13 + var21 / 2);
                                                                    }
                                                                }
                                                            }

                                                            Class74.setClipping(var0, var6, var4, var7);
                                                        }
                                                    } else {
                                                        var23 = var11.width * 4096 / var21;
                                                        if (var11.anInt301 == 0) {
                                                            if (0 != alpha) {
                                                                var37.method642(var13, var14, var11.width, var11.height, -(255 & alpha) + 256);
                                                            } else if (var11.width == var21 && var11.height == var22) {
                                                                var37.drawAt(var13, var14);
                                                            } else {
                                                                var37.method639(var13, var14, var11.width, var11.height);
                                                            }
                                                        } else {
                                                            var37.drawScaledOrRotated(var14 + var11.height / 2, var11.anInt301, var23, var13 + var11.width / 2);
                                                        }
                                                    }
                                                }
                                            } else {
                                                var37 = var11.method866(method609(var11, 69));
                                                if (null != var37) {
                                                    var37.drawAt(var13, var14);
                                                } else if (RSInterface.aBoolean6) {
                                                    Class20.method909(var11);
                                                }
                                            }
                                        } else {
                                            ItemDefinition var42;
                                            if (var11.type == 6) {
                                                boolean var41 = method609(var11, 110);
                                                Model var38 = null;
                                                if (var41) {
                                                    var21 = var11.secondAnimationId;
                                                } else {
                                                    var21 = var11.animationId;
                                                }

                                                var23 = 0;
                                                if (var11.anInt192 != -1) {
                                                    var42 = ItemDefinition.getItemDefinition(var11.anInt192);
                                                    var42 = var42.method1106(var11.anInt271);
                                                    SequenceDefinition var52 = var21 == -1 ? null : SequenceDefinition.getAnimationDefinition(var21);
                                                    var38 = var42.method1110(var11.anInt260, var11.anInt267, var52, 1, var11.anInt283);
                                                    if (var38 == null) {
                                                        Class20.method909(var11);
                                                    } else {
                                                        var23 = -var38.method1871() / 2;
                                                    }
                                                } else if (5 != var11.modelType) {
                                                    if (var21 == -1) {
                                                        var38 = var11.method865(-1, null, -1, 126, 0, var41, Class102.player.class52);
                                                        if (null == var38 && RSInterface.aBoolean6) {
                                                            Class20.method909(var11);
                                                        }
                                                    } else {
                                                        SequenceDefinition var48 = SequenceDefinition.getAnimationDefinition(var21);
                                                        var38 = var11.method865(var11.anInt260, var48, var11.anInt283, 127, var11.anInt267, var41, Class102.player.class52);
                                                        if (null == var38 && RSInterface.aBoolean6) {
                                                            Class20.method909(var11);
                                                        }
                                                    }
                                                } else if (-1 == var11.itemId) {
                                                    var38 = aClass52_1112.method1165(null, -1, null, null, 0, -1, 100, 0, -1, -1);
                                                } else {
                                                    var24 = 2047 & var11.itemId;
                                                    if (Class3_Sub1.localIndex == var24) {
                                                        var24 = 2047;
                                                    }

                                                    Player var49 = players[var24];
                                                    SequenceDefinition var56 = var21 == -1 ? null : SequenceDefinition.getAnimationDefinition(var21);
                                                    if (null != var49 && (-2048 & var11.itemId) == (int) var49.displayName.toLong() << 11) {
                                                        var38 = var49.class52.method1165(null, -1, null, var56, 0, -1, -126, 0, var11.anInt283, 0);
                                                    }
                                                }

                                                if (var38 != null) {
                                                    if (var11.anInt184 > 0) {
                                                        var24 = (var11.width << 8) / var11.anInt184;
                                                    } else {
                                                        var24 = 256;
                                                    }

                                                    if (var11.anInt312 <= 0) {
                                                        var25 = 256;
                                                    } else {
                                                        var25 = (var11.height << 8) / var11.anInt312;
                                                    }

                                                    var26 = var13 - -(var11.width / 2) - -(var24 * var11.anInt259 >> 8);
                                                    var47 = var11.height / 2 + var14 + (var25 * var11.anInt230 >> 8);
                                                    if (HDToolKit.highDetail) {
                                                        if (var11.aBoolean181) {
                                                            HDToolKit.method1855(var26, var47, var11.anInt164, var11.aShort293, var24, var25);
                                                        } else {
                                                            HDToolKit.method1821(var26, var47, var24, var25);
                                                            HDToolKit.method1825(var11.aShort169, 1.5F * (float) var11.aShort293);
                                                        }

                                                        HDToolKit.method1846();
                                                        HDToolKit.method1831(true);
                                                        HDToolKit.method1827(false);
                                                        TextureOperation29.method324(anInt3625);
                                                        if (aBoolean47) {
                                                            Class22.resetClipping();
                                                            HDToolKit.method1841();
                                                            Class22.setClipping(var0, var6, var4, var7);
                                                            aBoolean47 = false;
                                                        }

                                                        if (var11.aBoolean309) {
                                                            HDToolKit.depthBufferWritingDisabled();
                                                        }

                                                        var28 = Class51.anIntArray840[var11.anInt182] * var11.anInt164 >> 16;
                                                        var29 = var11.anInt164 * Class51.anIntArray851[var11.anInt182] >> 16;
                                                        if (var11.usingScripts) {
                                                            var38.method1893(var11.anInt308, var11.anInt280, var11.anInt182, var11.anInt258, var11.anInt264 + var28 + var23, var11.anInt264 + var29);
                                                        } else {
                                                            var38.method1893(var11.anInt308, 0, var11.anInt182, 0, var28, var29);
                                                        }

                                                        if (var11.aBoolean309) {
                                                            HDToolKit.method1830();
                                                        }
                                                    } else {
                                                        Class51.method1145(var26, var47);
                                                        var28 = Class51.anIntArray840[var11.anInt182] * var11.anInt164 >> 16;
                                                        var29 = var11.anInt164 * Class51.anIntArray851[var11.anInt182] >> 16;
                                                        if (!var11.usingScripts) {
                                                            var38.method1893(var11.anInt308, 0, var11.anInt182, 0, var28, var29);
                                                        } else if (var11.aBoolean181) {
                                                            ((Class140_Sub1_Sub2) var38).method1946(var11.anInt308, var11.anInt280, var11.anInt182, var11.anInt258, var11.anInt264 + var23 + var28, var29 + var11.anInt264, var11.anInt164);
                                                        } else {
                                                            var38.method1893(var11.anInt308, var11.anInt280, var11.anInt182, var11.anInt258, var11.anInt264 + (var28 - -var23), var29 + var11.anInt264);
                                                        }

                                                        Class51.method1141();
                                                    }
                                                }
                                            } else {
                                                if (var11.type == 7) {
                                                    var34 = var11.method868(Sprites.nameIconsSpriteArray);
                                                    if (var34 == null) {
                                                        if (RSInterface.aBoolean6) {
                                                            Class20.method909(var11);
                                                        }
                                                        continue;
                                                    }

                                                    var21 = 0;

                                                    for (var22 = 0; var22 < var11.defHeight; ++var22) {
                                                        for (var23 = 0; var23 < var11.defWidth; ++var23) {
                                                            if (0 < var11.itemAmounts[var21]) {
                                                                var42 = ItemDefinition.getItemDefinition(var11.itemAmounts[var21] + -1);
                                                                RSString var40;
                                                                if (1 != var42.stackingType && 1 == var11.itemIds[var21]) {
                                                                    var40 = RSString.stringCombiner(new RSString[]{
                                                                            ColorCore.ItemInterfaceColor, var42.name, TextCore.aClass94_2584
                                                                    });
                                                                } else {
                                                                    var40 = RSString.stringCombiner(new RSString[]{
                                                                            ColorCore.ItemInterfaceColor, var42.name, TextCore.aClass94_2306, Class36.method1013((byte) -100, var11.itemIds[var21])
                                                                    });
                                                                }

                                                                var26 = var13 + var23 * (var11.anInt285 + 115);
                                                                var47 = (var11.anInt290 + 12) * var22 + var14;
                                                                if (var11.anInt194 == 0) {
                                                                    var34.method681(var40, var26, var47, var11.anInt218, var11.aBoolean215 ? 0 : -1);
                                                                } else if (var11.anInt194 == 1) {
                                                                    var34.method699(var40, 57 + var26, var47, var11.anInt218, !var11.aBoolean215 ? -1 : 0);
                                                                } else {
                                                                    var34.drawStringRightAnchor(var40, -1 + var26 + 115, var47, var11.anInt218, !var11.aBoolean215 ? -1 : 0);
                                                                }
                                                            }

                                                            ++var21;
                                                        }
                                                    }
                                                }

                                                if (var11.type == 8 && Class20.aClass11_439 == var11 && TextureOperation35.anInt3323 == Class75.anInt1109) {
                                                    var21 = 0;
                                                    var20 = 0;
                                                    RSString var43 = var11.text;
                                                    Font var35 = FontType.plainFont;
                                                    var43 = method1303(var11, var43);

                                                    RSString var44;
                                                    while (var43.length() > 0) {
                                                        var25 = var43.indexOf(TextCore.aClass94_2598, 62);
                                                        if (var25 == -1) {
                                                            var44 = var43;
                                                            var43 = RSString.parse("");
                                                        } else {
                                                            var44 = var43.substring(0, var25, 0);
                                                            var43 = var43.substring(var25 + 4);
                                                        }

                                                        var26 = var35.method682(var44);
                                                        var21 += var35.anInt3727 - -1;
                                                        if (var26 > var20) {
                                                            var20 = var26;
                                                        }
                                                    }

                                                    var26 = var14 - -var11.height - -5;
                                                    var20 += 6;
                                                    var21 += 7;
                                                    if (var7 < var26 - -var21) {
                                                        var26 = -var21 + var7;
                                                    }

                                                    var25 = -var20 + -5 + var13 - -var11.width;
                                                    if (var25 < 5 + var13) {
                                                        var25 = 5 + var13;
                                                    }

                                                    if (var4 < var20 + var25) {
                                                        var25 = -var20 + var4;
                                                    }

                                                    Toolkit.getActiveToolkit().method934(var25, var26, var20, var21, 16777120);
                                                    Toolkit.getActiveToolkit().drawRect(var25, var26, var20, var21, 0, 255);

                                                    var43 = var11.text;
                                                    var47 = 2 + (var26 - -var35.anInt3727);

                                                    for (var43 = method1303(var11, var43); var43.length() > 0; var47 += var35.anInt3727 + 1) {
                                                        var28 = var43.indexOf(TextCore.aClass94_2598, 86);
                                                        if (var28 == -1) {
                                                            var44 = var43;
                                                            var43 = RSString.parse("");
                                                        } else {
                                                            var44 = var43.substring(0, var28, 0);
                                                            var43 = var43.substring(4 + var28);
                                                        }

                                                        var35.method681(var44, 3 + var25, var47, 0, -1);
                                                    }
                                                }

                                                if (var11.type == 9) {
                                                    if (var11.aBoolean167) {
                                                        var20 = var13;
                                                        var22 = var13 - -var11.width;
                                                        var21 = var14 - -var11.height;
                                                        var23 = var14;
                                                    } else {
                                                        var20 = var13;
                                                        var21 = var14;
                                                        var23 = var14 - -var11.height;
                                                        var22 = var13 + var11.width;
                                                    }

                                                    if (var11.anInt250 == 1) {
                                                        if (HDToolKit.highDetail) {
                                                            Class22.method933(var20, var21, var22, var23, var11.anInt218);
                                                        } else {
                                                            Class74.method1328(var20, var21, var22, var23, var11.anInt218);
                                                        }
                                                    } else if (HDToolKit.highDetail) {
                                                        Class22.method929(var20, var21, var22, var23, var11.anInt218, var11.anInt250);
                                                    } else {
                                                        Class74.method1322(var20, var21, var22, var23, var11.anInt218, var11.anInt250);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (var8 <= 31) {
                quickChatMessages = null;
            }

        } catch (RuntimeException var33) {
            throw ClientErrorException.clientError(var33, "gn.B(" + var0 + ',' + var1 + ',' + var2 + ',' + (var3 != null ? "{...}" : "null") + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ')');
        }
    }

    public static void method1098(byte var0) {
        try {
            if (anInt2309 < 128) {
                anInt2309 = 128;
            }

            if (anInt2309 > 383) {
                anInt2309 = 383;
            }

            GraphicDefinition.CAMERA_DIRECTION &= 2047;
            if (var0 >= -31) {
                quickChatMessages = null;
            }

            int var1 = anInt3155 >> 7;
            int var2 = anInt942 >> 7;
            int var3 = Class121.method1736(WorldListCountry.localPlane, 1, anInt3155, anInt942);
            int var4 = 0;
            int var5;
            if (var1 > 3 && 3 < var2 && 100 > var1 && var2 < 100) {
                for (var5 = -4 + var1; var1 - -4 >= var5; ++var5) {
                    for (int var6 = -4 + var2; var6 <= 4 + var2; ++var6) {
                        int var7 = WorldListCountry.localPlane;
                        if (3 > var7 && 2 == (2 & aByteArrayArrayArray113[1][var5][var6])) {
                            ++var7;
                        }

                        int var8 = (255 & aByteArrayArrayArray1774[var7][var5][var6]) * 8 - Class44.anIntArrayArrayArray723[var7][var5][var6] + var3;
                        if (var8 > var4) {
                            var4 = var8;
                        }
                    }
                }
            }

            var5 = 192 * var4;
            if (var5 > 98048) {
                var5 = 98048;
            }

            if (var5 < 32768) {
                var5 = 32768;
            }

            if (var5 <= Class75_Sub4.anInt2670) {
                if (Class75_Sub4.anInt2670 > var5) {
                    Class75_Sub4.anInt2670 += (var5 - Class75_Sub4.anInt2670) / 80;
                }
            } else {
                Class75_Sub4.anInt2670 += (-Class75_Sub4.anInt2670 + var5) / 24;
            }

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "gn.D(" + var0 + ')');
        }
    }

    public static int method1100(int var0, int var2) {
        try {
            if (var2 == -1) {
                return 12345678;
            } else {

                var0 = var0 * (127 & var2) >> 7;
                if (2 <= var0) {
                    if (126 < var0) {
                        var0 = 126;
                    }
                } else {
                    var0 = 2;
                }

                return var0 + ('\uff80' & var2);
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "gn.C(" + var0 + ',' + true + ',' + var2 + ')');
        }
    }

    public static void method1516(int var0, int var1) {
        try {
            InterfaceWidget var2 = InterfaceWidget.getWidget(3, var0);
            var2.a();
            if (var1 < 87) {
                TextCore.aClass94_1326 = null;
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "n.L(" + var0 + ',' + var1 + ')');
        }
    }

    /**
     * static final void method1517(byte var0) {
     * try {
     * Class139.aFontMetrics1822 = null;
     * TextCore.Helvetica = null;
     * if(var0 != -118) {
     * method1516(64, 82);
     * }
     * <p>
     * Class129_Sub1.anImage2695 = null;
     * } catch (RuntimeException var2) {
     * throw Class44.method1067(var2, "n.J(" + var0 + ')');
     * }
     * }
     **/

    public static void method1517() {
        try {
            StartupLoadingBarInitial.aFontMetrics1822 = null;

            //MillisTimer.anImage2695 = null;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "n.J(" + (byte) -118 + ')');
        }
    }

    public static int bitwiseXOR(int var0, int var1) {
        try {
            return var0 ^ var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "n.E(" + var0 + ',' + var1 + ')');
        }
    }

    public static void method1525(int var1, int var2, int var3, int var4, int var5) {
        try {
            int var6 = Class40.method1040(Class57.anInt902, var3, Class159.anInt2020);
            int var7 = Class40.method1040(Class57.anInt902, var5, Class159.anInt2020);
            int var8 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var4, Class101.anInt1425);

            int var9 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var2, Class101.anInt1425);

            for (int var10 = var6; var10 <= var7; ++var10) {
                TextureOperation18.method282(Class38.anIntArrayArray663[var10], var8, 3074 + -2974, var9, var1);
            }

        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "n.I(" + 3074 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
        }
    }

    public static SoftwareSprite method562(CacheIndex var0, int var2) {
        try {
            //  System.out.println("Class 3_Sub28_Sub16_Sub2 " + var2);
            return Class75_Sub4.method1351(var0, 0, var2) ? Class3_Sub28_Sub9.method578() : null;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "gf.O(" + (var0 != null ? "{...}" : "null") + ',' + 0 + ',' + var2 + ',' + (byte) 39 + ')');
        }
    }

    public static void parsePreferences(Signlink var0) {
        try {
            anInt3625 = 3;
            Class25.method957(true);
            aBoolean3604 = true;
            TextureOperation17.stereoSound = true;
            Class128.aBoolean1685 = true;
            Class3_Sub28_Sub9.anInt3622 = 0;
            anInt3071 = 0;
            KeyboardListener.aBoolean1905 = true;
            WorldListEntry.aBoolean2623 = true;
            RSInterface.aBoolean236 = true;
            Sprites.ambientVolume = 127;
            Class38.aBoolean661 = true;
            Class140_Sub6.aBoolean2910 = true;
            TextureOperation.anInt2378 = 0;
            anInt1137 = 2;
            aBoolean3275 = true;
            Class106.aBoolean1441 = true;
            AudioHandler.musicVolume = 255;
            Class25.aBoolean488 = true;
            anInt3671 = 0;
            RandomAccessFileWrapper var2 = null;
            AudioHandler.soundEffectVolume = 127;
            if (Class3_Sub24_Sub3.maxClientMemory >= 96) {
                Class127_Sub1.method1758(2);
            } else {
                Class127_Sub1.method1758(0);
            }

            anInt2148 = 0;
            Class3_Sub20.anInt2488 = 0;
            AudioThread.aBoolean346 = false;
            Class163_Sub3.aBoolean3004 = true;
            aBoolean2146 = false;
            aBoolean1080 = false;
            anInt2577 = 0;

            try {
                Class64 var3 = var0.method1433("runescape", 12);

                while (0 == var3.anInt978) {
                    TimeUtils.sleep(1L);
                }

                if (var3.anInt978 == 1) {
                    var2 = (RandomAccessFileWrapper) var3.anObject974;
                    byte[] var4 = new byte[(int) var2.getLength()];

                    int var6;
                    for (int var5 = 0; var4.length > var5; var5 += var6) {
                        var6 = var2.read(var4, var5, var4.length - var5, 0);
                        if (var6 == -1) {
                            throw new IOException("EOF");
                        }
                    }

                    Class79.method1390(new DataBuffer(var4));
                }
            } catch (Exception var8) {
            }

            try {
                if (var2 != null) {
                    var2.close();
                }
            } catch (Exception var7) {
            }

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "gf.F(" + (var0 != null ? "{...}" : "null") + ',' + 0 + ')');
        }
    }

    public static void method565(int var1, int var2) {
        try {
            Texture.anInt1150 = -TextureOperation37.anInt3256 + var1;

            int var3 = -((int) ((float) Class3_Sub28_Sub3.aClass11_3551.width / Class44.aFloat727)) + Texture.anInt1150;
            int var4 = Texture.anInt1150 + (int) ((float) Class3_Sub28_Sub3.aClass11_3551.width / Class44.aFloat727);
            if (var3 < 0) {
                Texture.anInt1150 = (int) ((float) Class3_Sub28_Sub3.aClass11_3551.width / Class44.aFloat727);
            }

            TextureOperation13.anInt3362 = Class108.anInt1460 + -1 + anInt65 + -var2;
            int var6 = (int) ((float) Class3_Sub28_Sub3.aClass11_3551.height / Class44.aFloat727) + TextureOperation13.anInt3362;
            int var5 = TextureOperation13.anInt3362 - (int) ((float) Class3_Sub28_Sub3.aClass11_3551.height / Class44.aFloat727);
            if (Class23.anInt455 < var4) {
                Texture.anInt1150 = Class23.anInt455 + -((int) ((float) Class3_Sub28_Sub3.aClass11_3551.width / Class44.aFloat727));
            }

            if (var5 < 0) {
                TextureOperation13.anInt3362 = (int) ((float) Class3_Sub28_Sub3.aClass11_3551.height / Class44.aFloat727);
            }

            if (var6 > Class108.anInt1460) {
                TextureOperation13.anInt3362 = -((int) ((float) Class3_Sub28_Sub3.aClass11_3551.height / Class44.aFloat727)) + Class108.anInt1460;
            }

        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "gf.E(" + (byte) 86 + ',' + var1 + ',' + var2 + ')');
        }
    }

    public static void method566(RSString var0, int var2) {
        try {
            InterfaceWidget var3 = InterfaceWidget.getWidget(3, var2);
            var3.flagUpdate();
            var3.text = var0;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "gf.P(" + (var0 != null ? "{...}" : "null") + ',' + 0 + ',' + var2 + ')');
        }
    }

    public static void method569(int var1) {
        try {
            InterfaceWidget var2 = InterfaceWidget.getWidget(7, var1);
            var2.a();
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "he.C(" + -82 + ',' + var1 + ')');
        }
    }

    public static RSString method1723(byte var0, boolean var1, int var3) {
        try {
            int var4 = 1;

            for (int var5 = var3 / 10; 0 != var5; ++var4) {
                var5 /= 10;
            }

            int var6 = var4;
            if (var3 < 0 || var1) {
                var6 = var4 + 1;
            }

            byte[] var7 = new byte[var6];
            if (var3 >= 0) {
                if (var1) {
                    var7[0] = 43;
                }
            } else {
                var7[0] = 45;
            }

            for (int var8 = 0; var4 > var8; ++var8) {
                int var9 = var3 % 10;
                if (var9 < 0) {
                    var9 = -var9;
                }

                var7[var6 + -var8 + -1] = (byte) (48 + var9);
                var3 /= 10;
            }

            if (var0 >= -111) {
                method1727((byte) 53);
            }

            RSString var11 = new RSString();
            var11.buffer = var7;
            var11.length = var6;
            return var11;
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "q.A(" + var0 + ',' + var1 + ',' + 10 + ',' + var3 + ')');
        }
    }

    public static void method1724(int var0, int var1, int var2, int var3, int var4, int var5, byte var6, int var7) {
        try {
            if (var5 >= 128 && var2 >= 128 && var5 <= 13056 && var2 <= 13056) {
                int var8 = Class121.method1736(WorldListCountry.localPlane, 1, var5, var2) + -var3;
                var2 -= Class77.anInt1111;
                var8 -= Class7.anInt2162;
                var5 -= NPC.anInt3995;
                int var9 = Class51.anIntArray840[Class139.anInt1823];
                int var10 = Class51.anIntArray851[Class139.anInt1823];
                int var11 = Class51.anIntArray840[TextureOperation28.anInt3315];
                int var12 = Class51.anIntArray851[TextureOperation28.anInt3315];
                int var13 = var5 * var12 + var11 * var2 >> 16;
                var2 = var2 * var12 + -(var11 * var5) >> 16;
                var5 = var13;
                var13 = var10 * var8 - var2 * var9 >> 16;
                if (var6 <= -47) {
                    var2 = var2 * var10 + var8 * var9 >> 16;
                    if (50 > var2) {
                        Texture.anInt2208 = -1;
                        Class32.anInt590 = -1;
                    } else if (HDToolKit.highDetail) {
                        int var14 = var1 * 512 >> 8;
                        Class32.anInt590 = var14 * var5 / var2 + var0;
                        int var15 = 512 * var7 >> 8;
                        Texture.anInt2208 = var15 * var13 / var2 + var4;
                    } else {
                        Class32.anInt590 = (var5 << 9) / var2 + var0;
                        Texture.anInt2208 = (var13 << 9) / var2 + var4;
                    }

                }
            } else {
                Texture.anInt2208 = -1;
                Class32.anInt590 = -1;
            }
        } catch (RuntimeException var16) {
            throw ClientErrorException.clientError(var16, "q.C(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
        }
    }

    public static void method1726(int var0) {
        try {
            TextCore.aClass94_1622 = null;
            TextCore.aClass94_1617 = null;

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "q.B(" + var0 + ')');
        }
    }

    public static int method1727(byte var0) {
        try {
            if (var0 != 123) {
                method1726(-123);
            }

            return KeyboardListener.aReferenceCache_1911.hardCount();
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "q.D(" + var0 + ')');
        }
    }

    static int method1602(RSString var1) {
        try {
            if (Class119.aClass131_1624 == null || var1.length() == 0) {
                return -1;
            } else {
                for (int var2 = 0; var2 < Class119.aClass131_1624.anInt1720; ++var2) {
                    if (Class119.aClass131_1624.aClass94Array1721[var2].method1560(RSString.parse(" "), TextCore.aClass94_4066).method1562((byte) -32, var1)) {
                        return var2;
                    }
                }

                return -1;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ni.G(" + 0 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static void method338(int var1, boolean var2, int var3, int var4, int var5) {
       try {
          ++Class79.anInt1127;
          Class124.method1745();
          if(!var2) {
             Class3_Sub5.method116(true);
             Class102.method1611(71, true);
             Class3_Sub5.method116(false);
          }

          Class102.method1611(100, false);
          if(!var2) {
             TextureOperation32.method302(2);
          }

          method2067();
          if(HDToolKit.highDetail) {
             Class65.method1239(var4, 125, var5, var1, var3, true);
             var3 = Class163_Sub1.anInt2989;
             var5 = Class3_Sub28_Sub3.anInt3564;
             var4 = Class96.anInt1358;
             var1 = anInt31;
          }

          int var6;
          int var7;
          if(1 == Class133.anInt1753) {
             var7 = 2047 & LinkableRSString.anInt2589 + GraphicDefinition.CAMERA_DIRECTION;
             var6 = anInt2309;
             if(Class75_Sub4.anInt2670 / 256 > var6) {
                var6 = Class75_Sub4.anInt2670 / 256;
             }

             if(WaterfallShader.aBooleanArray2169[4] && Class166.anIntArray2073[4] + 128 > var6) {
                var6 = 128 + Class166.anIntArray2073[4];
             }

             Class140_Sub2.method1952(anInt3155, var1, Class121.method1736(WorldListCountry.localPlane, 1, Class102.player.xAxis, Class102.player.zAxis) + -50, Client.ZOOM - -(var6 * 3), var7, anInt942, var6);
          }

          var7 = Class7.anInt2162;
          var6 = NPC.anInt3995;
          int var8 = Class77.anInt1111;
          int var9 = Class139.anInt1823;
          int var10 = TextureOperation28.anInt3315;

          int var11;
          int var12;
          for(var11 = 0; 5 > var11; ++var11) {
             if(WaterfallShader.aBooleanArray2169[var11]) {
                var12 = (int)((double)(-TextureOperation14.anIntArray3383[var11]) + (double)(TextureOperation14.anIntArray3383[var11] * 2 + 1) * Math.random() + Math.sin((double)Class163_Sub1_Sub1.anIntArray4009[var11] * ((double) TextureOperation3.anIntArray3359[var11] / 100.0D)) * (double)Class166.anIntArray2073[var11]);
                if(var11 == 3) {
                   TextureOperation28.anInt3315 = var12 + TextureOperation28.anInt3315 & 0x7FF;
                }

                if(var11 == 4) {
                   Class139.anInt1823 += var12;
                   if(128 > Class139.anInt1823) {
                      Class139.anInt1823 = 128;
                   }

                   if(Class139.anInt1823 > 383) {
                      Class139.anInt1823 = 383;
                   }
                }

                if(var11 == 2) {
                   Class77.anInt1111 += var12;
                }

                if(var11 == 1) {
                   Class7.anInt2162 += var12;
                }

                if(var11 == 0) {
                   NPC.anInt3995 += var12;
                }
             }
          }

          Class3_Sub28_Sub20.method725();
          if(HDToolKit.highDetail) {
             Class22.setClipping(var3, var5, var3 + var4, var5 - -var1);
             float var17 = (float)Class139.anInt1823 * 0.17578125F;
             float var16 = 0.17578125F * (float) TextureOperation28.anInt3315;
             if(Class133.anInt1753 == 3) {
                var17 = 360.0F * InterfaceWidget.aFloat1169 / 6.2831855F;
                var16 = Class45.aFloat730 * 360.0F / 6.2831855F;
             }

             HDToolKit.viewport(var3, var5, var4, var1, var4 / 2 + var3, var5 - -(var1 / 2), var17, var16, anInt1705, anInt1705);
          } else {
             Class74.setClipping(var3, var5, var4 + var3, var1 + var5);
             Class51.method1134();
          }

          if(!Class38_Sub1.aBoolean2615 && var3 <= NPCDefinition.anInt1297 && var4 + var3 > NPCDefinition.anInt1297 && var5 <= Class38_Sub1.anInt2612 && Class38_Sub1.anInt2612 < var1 + var5) {
             TextureOperation36.aBoolean3094 = true;
             anInt59 = 0;
             var12 = Class145.screenUpperX;
             int var13 = Class1.screenUpperY;
             var11 = Class139.screenLowerX;
             anInt3642 = var11 + (var12 - var11) * (-var3 + NPCDefinition.anInt1297) / var4;
             int var14 = AtmosphereParser.screenLowerY;
             RenderAnimationDefinition.anInt384 = (-var13 + var14) * (Class38_Sub1.anInt2612 - var5) / var1 + var13;
          } else {
             TextureOperation36.aBoolean3094 = false;
             anInt59 = 0;
          }

          Class58.method1194();
          byte var19 = Class137.method1817() != 2 ?1:(byte)Class79.anInt1127;
          if(HDToolKit.highDetail) {
             HDToolKit.method1846();
             HDToolKit.method1831(true);
             HDToolKit.method1827(true);
             if(Class143.gameStage == 10) {
                var12 = BufferedDataStream.method809(Class106.anInt1446, Class77.anInt1111 >> 10, anInt3625, NPC.anInt3995 >> 10);
             } else {
                var12 = BufferedDataStream.method809(Class106.anInt1446, Class102.player.anIntArray2755[0] >> 3, anInt3625, Class102.player.anIntArray2767[0] >> 3);
             }

             Class68.method1269(Class44.anInt719, !WorldListEntry.aBoolean2623);
             HDToolKit.clearScreen(var12);
             ClientErrorException.method2285(Class139.anInt1823, Class77.anInt1111, Class7.anInt2162, NPC.anInt3995, TextureOperation28.anInt3315);
             HDToolKit.anInt1791 = Class44.anInt719;
             Class3_Sub22.method398(NPC.anInt3995, Class7.anInt2162, Class77.anInt1111, Class139.anInt1823, TextureOperation28.anInt3315, Class158.aByteArrayArrayArray2008, anIntArray686, Class129_Sub1.anIntArray2696, Class159.anIntArray2021, Player.anIntArray3959, SequenceDefinition.anIntArray1871, WorldListCountry.localPlane + 1, var19, Class102.player.xAxis >> 7, Class102.player.zAxis >> 7);
             aBoolean47 = true;
             Class68.method1265();
             ClientErrorException.method2285(0, 0, 0, 0, 0);
             Class58.method1194();
             method1775();
             Texture.method1405(var5, var4, var3, anInt1705, var1, anInt1705, -7397);
             Class163_Sub2_Sub1.method2221(var4, var3, var1, anInt1705, anInt1705, var5);
          } else {
             Toolkit.JAVA_TOOLKIT.method934(var3, var5, var4, var1, 0);
             Class3_Sub22.method398(NPC.anInt3995, Class7.anInt2162, Class77.anInt1111, Class139.anInt1823, TextureOperation28.anInt3315, Class158.aByteArrayArrayArray2008, anIntArray686, Class129_Sub1.anIntArray2696, Class159.anIntArray2021, Player.anIntArray3959, SequenceDefinition.anIntArray1871, WorldListCountry.localPlane - -1, var19, Class102.player.xAxis >> 7, Class102.player.zAxis >> 7);
             Class58.method1194();
             method1775();
             Texture.method1405(var5, var4, var3, 256, var1, 256, -6403 + -994);
             Class163_Sub2_Sub1.method2221(var4, var3, var1, 256, 256, var5);
          }

          ((Class102)Class51.anInterface2_838).method1610(Class106.anInt1446);
          Class65.method1235(var4, var5, var1, var3);
          Class139.anInt1823 = var9;
          Class77.anInt1111 = var8;
          Class7.anInt2162 = var7;
          NPC.anInt3995 = var6;
          TextureOperation28.anInt3315 = var10;
          if(TextureOperation34.aBoolean3064 && Class58.aJs5Worker_917.countPriorityRequests() == 0) {
             TextureOperation34.aBoolean3064 = false;
          }

          if(TextureOperation34.aBoolean3064) {
             Toolkit.getActiveToolkit().method934(var3, var5, var4, var1, 0);
             LoadingBox.draw(false, TextCore.LoadingPleaseWait2);
          }

          if(!var2 && !TextureOperation34.aBoolean3064 && !Class38_Sub1.aBoolean2615 && var3 <= NPCDefinition.anInt1297 && var4 + var3 > NPCDefinition.anInt1297 && Class38_Sub1.anInt2612 >= var5 && var1 + var5 > Class38_Sub1.anInt2612) {
             TextureOperation36.method1628(var5, var4, var1, var3, Class38_Sub1.anInt2612, NPCDefinition.anInt1297);
          }

       } catch (RuntimeException var15) {
          throw ClientErrorException.clientError(var15, "ui.OA(" + -6403 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
       }
    }

    static boolean method609(RSInterface var0, int var1) {
        if (null == var0.anIntArray275) {
            return false;
        } else {
            int var2 = 0;
            if (var1 <= 20) {
                TextCore.COMMAND_MEMORY_MANAGEMENT = null;
            }

            for (; var0.anIntArray275.length > var2; ++var2) {
                int var3 = Class164_Sub2.method2247((byte) 119, var2, var0);
                int var4 = var0.anIntArray307[var2];
                if (var0.anIntArray275[var2] != 2) {
                    if (var0.anIntArray275[var2] != 3) {
                        if (4 == var0.anIntArray275[var2]) {
                            if (var4 == var3) {
                                return false;
                            }
                        } else if (var3 != var4) {
                            return false;
                        }
                    } else if (var3 <= var4) {
                        return false;
                    }
                } else if (var3 >= var4) {
                    return false;
                }
            }

            return true;
        }
    }

    public static RSInterface getRSInterface(int interfaceHash) {
        try {
            int windowId = interfaceHash >> 16;

            int componentId = 65535 & interfaceHash;
            if (GameObject.aClass11ArrayArray1834.length <= windowId || windowId < 0) {
                return null;
            }
            if (GameObject.aClass11ArrayArray1834[windowId] == null || GameObject.aClass11ArrayArray1834[windowId].length <= componentId || null == GameObject.aClass11ArrayArray1834[windowId][componentId]) {
                boolean var4 = loadInterface(windowId);
                if (!var4) {
                    return null;
                }
            }
            if (GameObject.aClass11ArrayArray1834[windowId].length <= componentId) {
                return null;
            }
            return GameObject.aClass11ArrayArray1834[windowId][componentId];
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "af.F(" + interfaceHash + ')');
        }
    }

    static void method551(int var1, int var2) {
       try {
          if(4 == var2 && !Class128.aBoolean1685) {
             var2 = 2;
             var1 = 2;
          }

          if(var2 == Class23.anInt453) {
             if(0 != var2 && var1 != TextureOperation37.anInt3263) {
                anShaderInterfaceArray70[var2].method23(var1);
                TextureOperation37.anInt3263 = var1;
             }
          } else {
             if(TextureOperation23.aBoolean3207) {
                return;
             }

             if(Class23.anInt453 != 0) {
                anShaderInterfaceArray70[Class23.anInt453].method21();
             }

             if(var2 != 0) {
                ShaderInterface var3 = anShaderInterfaceArray70[var2];
                var3.method22();
                var3.method23(var1);
             }

             Class23.anInt453 = var2;
             TextureOperation37.anInt3263 = var1;
          }

       } catch (RuntimeException var4) {
          throw ClientErrorException.clientError(var4, "cb.A(" + 0 + ',' + var1 + ',' + var2 + ')');
       }
    }

    static void method2275(int var0, int var2, int var3, int var4, int var5, int var6) {
        try {

            int var8 = -var5 + var3;
            MouseListeningClass.method2091(var3);
            int var7 = 0;
            if (var8 < 0) {
                var8 = 0;
            }

            int var9 = var3;
            int var10 = -var3;
            int var12 = -var8;
            int var11 = var8;
            int var13 = -1;
            int var17;
            int var16;
            int var19;
            int var18;
            if (var2 >= Class159.anInt2020 && Class57.anInt902 >= var2) {
                int[] var15 = Class38.anIntArrayArray663[var2];
                var16 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var3 + var6, Class101.anInt1425);
                var17 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var3 + var6, Class101.anInt1425);
                var18 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var6 + -var8, Class101.anInt1425);
                var19 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var6 - -var8, Class101.anInt1425);
                TextureOperation18.method282(var15, var16, 102, var18, var4);
                TextureOperation18.method282(var15, var18, -44, var19, var0);
                TextureOperation18.method282(var15, var19, -61, var17, var4);
            }

            int var14 = -1;

            while (var9 > var7) {
                var13 += 2;
                var14 += 2;
                var12 += var14;
                var10 += var13;
                if (var12 >= 0 && var11 >= 1) {
                    --var11;
                    GameObject.anIntArray1838[var11] = var7;
                    var12 -= var11 << 1;
                }

                ++var7;
                int var21;
                int var20;
                int[] var22;
                int var24;
                if (0 <= var10) {
                    --var9;
                    var10 -= var9 << 1;
                    var24 = var2 + -var9;
                    var16 = var2 + var9;
                    if (var16 >= Class159.anInt2020 && var24 <= Class57.anInt902) {
                        if (var8 <= var9) {
                            var17 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var7 + var6, Class101.anInt1425);
                            var18 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var7 + var6, Class101.anInt1425);
                            if (var16 <= Class57.anInt902) {
                                TextureOperation18.method282(Class38.anIntArrayArray663[var16], var18, -53, var17, var4);
                            }

                            if (var24 >= Class159.anInt2020) {
                                TextureOperation18.method282(Class38.anIntArrayArray663[var24], var18, 96, var17, var4);
                            }
                        } else {
                            var17 = GameObject.anIntArray1838[var9];
                            var18 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var7 + var6, Class101.anInt1425);
                            var19 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var7 + var6, Class101.anInt1425);
                            var20 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var6 - -var17, Class101.anInt1425);
                            var21 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var17 + var6, Class101.anInt1425);
                            if (Class57.anInt902 >= var16) {
                                var22 = Class38.anIntArrayArray663[var16];
                                TextureOperation18.method282(var22, var19, 116, var21, var4);
                                TextureOperation18.method282(var22, var21, 125, var20, var0);
                                TextureOperation18.method282(var22, var20, 87, var18, var4);
                            }

                            if (Class159.anInt2020 <= var24) {
                                var22 = Class38.anIntArrayArray663[var24];
                                TextureOperation18.method282(var22, var19, 110, var21, var4);
                                TextureOperation18.method282(var22, var21, -114, var20, var0);
                                TextureOperation18.method282(var22, var20, -88, var18, var4);
                            }
                        }
                    }
                }

                var24 = -var7 + var2;
                var16 = var2 - -var7;
                if (var16 >= Class159.anInt2020 && var24 <= Class57.anInt902) {
                    var17 = var6 + var9;
                    var18 = var6 + -var9;
                    if (var17 >= Class101.anInt1425 && var18 <= Class3_Sub28_Sub18.anInt3765) {
                        var17 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var17, Class101.anInt1425);
                        var18 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var18, Class101.anInt1425);
                        if (var7 < var8) {
                            var19 = var11 >= var7 ? var11 : GameObject.anIntArray1838[var7];
                            var20 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var19 + var6, Class101.anInt1425);
                            var21 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var6 - var19, Class101.anInt1425);
                            if (Class57.anInt902 >= var16) {
                                var22 = Class38.anIntArrayArray663[var16];
                                TextureOperation18.method282(var22, var18, 126, var21, var4);
                                TextureOperation18.method282(var22, var21, 103, var20, var0);
                                TextureOperation18.method282(var22, var20, -61, var17, var4);
                            }

                            if (var24 >= Class159.anInt2020) {
                                var22 = Class38.anIntArrayArray663[var24];
                                TextureOperation18.method282(var22, var18, 102, var21, var4);
                                TextureOperation18.method282(var22, var21, -94, var20, var0);
                                TextureOperation18.method282(var22, var20, 99, var17, var4);
                            }
                        } else {
                            if (var16 <= Class57.anInt902) {
                                TextureOperation18.method282(Class38.anIntArrayArray663[var16], var18, 94, var17, var4);
                            }

                            if (var24 >= Class159.anInt2020) {
                                TextureOperation18.method282(Class38.anIntArrayArray663[var24], var18, 126, var17, var4);
                            }
                        }
                    }
                }
            }

        } catch (RuntimeException var23) {
            throw ClientErrorException.clientError(var23, "wl.I(" + var0 + ',' + (byte) 109 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    static void method2270(Class140_Sub4 var0) {
        try {
            if (Class44.anInt719 == var0.anInt2790 || var0.anInt2771 == -1 || var0.anInt2828 != 0 || SequenceDefinition.getAnimationDefinition(var0.anInt2771).duration[var0.anInt2832] < 1 + var0.anInt2760) {
                int var2 = var0.anInt2790 + -var0.anInt2800;
                int var3 = Class44.anInt719 + -var0.anInt2800;
                int var4 = var0.anInt2784 * 128 + 64 * var0.getSize();
                int var5 = var0.anInt2835 * 128 - -(var0.getSize() * 64);
                int var6 = 128 * var0.anInt2823 + var0.getSize() * 64;
                int var7 = 128 * var0.anInt2798 + var0.getSize() * 64;
                var0.xAxis = (var3 * var6 + var4 * (var2 - var3)) / var2;
                var0.zAxis = (var7 * var3 + var5 * (var2 - var3)) / var2;
            }

            var0.anInt2824 = 0;
            if (var0.anInt2840 == 0) {
                var0.anInt2806 = 1024;
            }

            if (1 == var0.anInt2840) {
                var0.anInt2806 = 1536;
            }

            if (var0.anInt2840 == 2) {
                var0.anInt2806 = 0;
            }

            if (var0.anInt2840 == 3) {
                var0.anInt2806 = 512;
            }

            var0.anInt2785 = var0.anInt2806;
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "wl.K(" + (var0 != null ? "{...}" : "null") + ',' + (byte) -56 + ')');
        }
    }
}
