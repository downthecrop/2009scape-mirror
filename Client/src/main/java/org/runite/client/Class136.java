package org.runite.client;

import org.rs09.client.data.ReferenceCache;

final class Class136 {

    static LinkedList aLinkedList_1424 = new LinkedList();
    static int anInt1771;
    static ReferenceCache aReferenceCache_1772 = new ReferenceCache(64);
    static Class64 aClass64_1778;
    static short[] aShortArray1779 = new short[256];
    LDIndexedSprite aClass109_Sub1_1770;
    GameObject aClass140_1777;

    static void method1814(int var0, int var1, int var2, int var3, int var4, int var8, int var9, int var10) {
        try {
            int var11 = var2 - var4;
            int var13 = -1;
            if (AbstractSprite.anInt3704 > 0) {
                if (Class3_Sub28_Sub8.anInt3611 <= 10) {
                    var13 = 5 * Class3_Sub28_Sub8.anInt3611;
                } else {
                    var13 = -((-10 + Class3_Sub28_Sub8.anInt3611) * 5) + 50;
                }
            }

            int var12 = -var9 + var1;
            int var15 = 983040 / var8;
            int var16 = 983040 / var3;

            for (int var17 = -var15; var17 < var11 - -var15; ++var17) {
                int var18 = - -(var17 * var8) >> 16;
                int var19 = var8 * (var17 + 1) >> 16;
                int var20 = -var18 + var19;
                if (var20 > 0) {
                    int var21 = var4 + var17 >> 6;
                    var18 += var0;
                    if (var21 >= 0 && var21 <= -1 + Class44.anIntArrayArrayArray720.length) {
                        int[][] var22 = Class44.anIntArrayArrayArray720[var21];

                        for (int var23 = -var16; var23 < var12 - -var16; ++var23) {
                            int var25 = - -(var3 * (var23 - -1)) >> 16;
                            int var24 = var23 * var3 >> 16;
                            int var26 = var25 + -var24;
                            if (0 < var26) {
                                var24 += var10;
                                int var27 = var9 + var23 >> 6;
                                if (var27 >= 0 && -1 + var22.length >= var27 && null != var22[var27]) {
                                    int var28 = (63 & var17 + var4) + (4032 & var9 + var23 << 6);
                                    int var29 = var22[var27][var28];
                                    if (var29 != 0) {
                                        ObjectDefinition var14 = ObjectDefinition.getObjectDefinition(-1 + var29);
                                        if (!Class3_Sub24_Sub4.aBooleanArray3503[var14.MapIcon]) {
                                            if (var13 != -1 && Class8.anInt101 == var14.MapIcon) {
                                                Class3_Sub23 var30 = new Class3_Sub23();
                                                var30.anInt2531 = var18;
                                                var30.anInt2539 = var24;
                                                var30.anInt2532 = var14.MapIcon;
                                                aLinkedList_1424.method1215(var30);
                                            } else {
                                                GameObject.aSoftwareSpriteArray1839[var14.MapIcon].drawAt(var18 + -7, -7 + var24);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            for (Class3_Sub23 var32 = (Class3_Sub23) aLinkedList_1424.method1222(); null != var32; var32 = (Class3_Sub23) aLinkedList_1424.method1221()) {
                Class74.method1330(var32.anInt2531, var32.anInt2539, 15, var13);
                Class74.method1330(var32.anInt2531, var32.anInt2539, 13, var13);
                Class74.method1330(var32.anInt2531, var32.anInt2539, 11, var13);
                Class74.method1330(var32.anInt2531, var32.anInt2539, 9, var13);
                GameObject.aSoftwareSpriteArray1839[var32.anInt2532].drawAt(-7 + var32.anInt2531, -7 + var32.anInt2539);
            }

            aLinkedList_1424.method1211(-76);
        } catch (RuntimeException var31) {
            throw ClientErrorException.clientError(var31, "sm.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + 0 + ',' + 0 + ',' + 127 + ',' + var8 + ',' + var9 + ',' + var10 + ')');
        }
    }

    static void method1816(int var0, int var1) {
        try {
            if (var1 == -7) {
                Class129_Sub1.anIntArray2696 = new int[var0];
                Class159.anIntArray2021 = new int[var0];
                SequenceDefinition.anIntArray1871 = new int[var0];
                Player.anIntArray3959 = new int[var0];
                Unsorted.anIntArray686 = new int[var0];
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "sm.C(" + var0 + ',' + var1 + ')');
        }
    }

}
