package org.runite.client;

import java.util.Calendar;
import java.util.Date;

public final class AudioThread implements Runnable {

    static short[][] aShortArrayArray344;
    static int anInt4034 = -8 + (int) (17.0D * Math.random());
    static boolean aBoolean346;
    public static int[] localNPCIndexes = new int[32768];
    static Class64 aClass64_351;
    volatile boolean aBoolean345 = false;
    Signlink aClass87_350;
    volatile AudioChannel[] channels = new AudioChannel[2];
    volatile boolean aBoolean353 = false;


    static boolean method888(int var0, ObjectDefinition var1, int var3, int var5, int var6) {
        try {
            MapSceneDefinition var7 = InterfaceWidget.c(var1.anInt1516);
            if (var7.sprite == -1) {
                return false;
            } else {
                if (var1.aBoolean1537) {
                    var6 += var1.anInt1478;
                    var6 &= 3;
                } else {
                    var6 = 0;
                }

                LDIndexedSprite var8 = var7.getSprite(var6);
                if (var8 == null) {
                    return true;
                } else {
                    int var9 = var1.SizeX;
                    int var10 = var1.SizeY;
                    if (1 == (1 & var6)) {
                        var9 = var1.SizeY;
                        var10 = var1.SizeX;
                    }

                    int var11 = var8.anInt1469;
                    int var12 = var8.anInt1467;
                    if (var7.aBoolean69) {
                        var12 = 4 * var10;
                        var11 = 4 * var9;
                    }

                    if (var7.color == 0) {
                        var8.method1677(var0 * 4 + 48, 48 + 4 * (-var10 + -var5 + 104), var11, var12);
                    } else {
                        var8.method1669(48 + 4 * var0, 4 * (-var10 + -var5 + 104) + 48, var11, var12, var7.color);
                    }

                    return false;
                }
            }
        } catch (RuntimeException var13) {
            throw ClientErrorException.clientError(var13, "cj.D(" + var0 + ',' + (var1 != null ? "{...}" : "null") + ',' + false + ',' + var3 + ',' + 0 + ',' + var5 + ',' + var6 + ')');
        }
    }

    static AbstractSprite[] method891(int var0) {
        try {
            AbstractSprite[] var1 = new AbstractSprite[Class95.anInt1338];
            if (var0 != -5) {
                method894(113L);
            }

            for (int var2 = 0; var2 < Class95.anInt1338; ++var2) {
                int var3 = Class140_Sub7.anIntArray2931[var2] * Unsorted.anIntArray3076[var2];
                byte[] var4 = Class163_Sub1.aByteArrayArray2987[var2];
                int[] var5 = new int[var3];

                for (int var6 = 0; var6 < var3; ++var6) {
                    var5[var6] = TextureOperation38.spritePalette[Unsorted.bitwiseAnd(255, var4[var6])];
                }

                if (HDToolKit.highDetail) {
                    var1[var2] = new HDSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[var2], Unsorted.anIntArray2591[var2], Class140_Sub7.anIntArray2931[var2], Unsorted.anIntArray3076[var2], var5);
                } else {
                    var1[var2] = new SoftwareSprite(Class3_Sub15.anInt2426, Class133.anInt1748, Class164.anIntArray2048[var2], Unsorted.anIntArray2591[var2], Class140_Sub7.anIntArray2931[var2], Unsorted.anIntArray3076[var2], var5);
                }
            }

            Class39.method1035((byte) 116);
            return var1;
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "cj.C(" + var0 + ')');
        }
    }

    static RSString method894(long var0) {
        try {
            Class3_Sub28_Sub5.aCalendar3581.setTime(new Date(var0));
            int var3 = Class3_Sub28_Sub5.aCalendar3581.get(Calendar.DAY_OF_WEEK);//Day of the week
            int var4 = Class3_Sub28_Sub5.aCalendar3581.get(Calendar.DATE);
            int var5 = Class3_Sub28_Sub5.aCalendar3581.get(Calendar.MONTH);
            int var6 = Class3_Sub28_Sub5.aCalendar3581.get(Calendar.YEAR);
            int var7 = Class3_Sub28_Sub5.aCalendar3581.get(Calendar.HOUR_OF_DAY);
            int var8 = Class3_Sub28_Sub5.aCalendar3581.get(Calendar.MINUTE);
            int var9 = Class3_Sub28_Sub5.aCalendar3581.get(Calendar.SECOND);
            return RSString.stringCombiner(new RSString[]{TextCore.DaysOfTheWeek[var3 + -1], RSString.parse(")1 "), RSString.stringAnimator(var4 / 10), RSString.stringAnimator(var4 % 10), TextCore.aClass94_2025, TextCore.MonthsOfTheYear[var5], TextCore.aClass94_2025, RSString.stringAnimator(var6), RSString.parse(" "), RSString.stringAnimator(var7 / 10), RSString.stringAnimator(var7 % 10), RSString.parse(":"), RSString.stringAnimator(var8 / 10), RSString.stringAnimator(var8 % 10), RSString.parse(":"), RSString.stringAnimator(var9 / 10), RSString.stringAnimator(var9 % 10), RSString.parse(" GMT")});
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "cj.F(" + var0 + ')');
        }
    }

    static int[][] method895(int var4) {
        try {
            int[][] var9 = new int[256][64];
            TextureOperation34 var10 = new TextureOperation34();
            var10.anInt3062 = (int) ((float) 0.4 * 4096.0F);
            var10.anInt3058 = 3;
            var10.anInt3056 = 4;
            var10.aBoolean3065 = false;
            var10.anInt3060 = 8;
            var10.postDecode();
            TextureOperation33.method180(122, 256, 64);

            for (int var11 = 0; var11 < 256; ++var11) {
                var10.method186(var11, var9[var11]);
            }

            return var9;
        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "cj.B(" + false + ',' + 3 + ',' + 64 + ',' + 256 + ',' + var4 + ',' + 4 + ',' + 8 + ',' + (float) 0.4 + ')');
        }
    }

    public final void run() {
        try {
            this.aBoolean353 = true;

            try {
                while (!this.aBoolean345) {
                    for (int var1 = 0; var1 < 2; ++var1) {
                        AudioChannel var2 = this.channels[var1];
                        if (var2 != null) {
                            var2.method2153();
                        }
                    }

                    TimeUtils.sleep(10L);
                    Class81.method1400(this.aClass87_350, null, -71);
                }
            } catch (Exception var7) {
                Class49.reportError(null, var7);
            } finally {
                this.aBoolean353 = false;
            }

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "cj.run()");
        }
    }

}
