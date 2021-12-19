package org.runite.client;

import org.rs09.client.rendering.Toolkit;

import java.util.Objects;
import java.util.zip.CRC32;

final class TextureOperation24 extends TextureOperation {

    static CRC32 CRC32 = new CRC32();
    static int anInt3377 = 7759444;//Very light Brown 7759444 // #766654
    static int anInt2243 = 3353893;//Dark Brown 2 3353893 // #332D25
    static int anInt2530 = 2301979;//Dark Brown 2301979 // #34301B
    static int anInt486 = 5063219;//Light Brown 5063219 // #4d4233


    static void method223(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        try {
            if (var7 == var4) {
                Unsorted.method1460(var1, var3, var6, var7, var2, var5);
            } else {

                if (Class101.anInt1425 <= var2 - var7 && var7 + var2 <= Class3_Sub28_Sub18.anInt3765 && var3 - var4 >= Class159.anInt2020 && Class57.anInt902 >= var3 + var4) {
                    Class161.method2200(var6, var2, var3, var5, var7, 95, var4, var1);
                } else {
                    TextureOperation25.method329(var7, var6, var5, var1, var3, var2, var4);
                }

            }
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "fn.C(" + true + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
        }
    }

    public TextureOperation24() {
        super(1, true);
    }

    static void method224(int var1, int var2, int var3, int var4, int var5) {
        try {
            GameObject.aClass109Array1831[0].method1667(var3, var4);
            GameObject.aClass109Array1831[1].method1667(var3, -16 + var5 + var4);
            int var6 = var5 * (var5 + -32) / var2;
            if (var6 < 8) {
                var6 = 8;
            }
            int var7 = var1 * (-var6 + -32 + var5) / (var2 + -var5);
            Toolkit.getActiveToolkit().method934(var3, 16 + var4, 16, -32 + var5, anInt2530);
            Toolkit.getActiveToolkit().method934(var3, 16 + var4 + var7, 16, var6, anInt486);
            Toolkit.getActiveToolkit().drawVerticalLine(var3, var7 + (var4 + 16), var6, anInt3377);
            Toolkit.getActiveToolkit().drawVerticalLine(var3 + 1, var7 + 16 + var4, var6, anInt3377);
            Toolkit.getActiveToolkit().drawHorizontalLine(var3, var7 + 16 + var4, 16, anInt3377);
            Toolkit.getActiveToolkit().drawHorizontalLine(var3, var7 + var4 + 17, 16, anInt3377);
            Toolkit.getActiveToolkit().drawVerticalLine(15 + var3, var4 + (16 + var7), var6, anInt2243);
            Toolkit.getActiveToolkit().drawVerticalLine(14 + var3, 17 + (var4 + var7), -1 + var6, anInt2243);
            Toolkit.getActiveToolkit().drawHorizontalLine(var3, var6 + 15 + var4 + var7, 16, anInt2243);
            Toolkit.getActiveToolkit().drawHorizontalLine(1 + var3, var6 + var4 + (14 + var7), 15, anInt2243);

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "fn.E(" + (byte) 120 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
        }
    }

    static void method226(int var0) {
        try {
            if (var0 != -1) {
                if (Unsorted.loadInterface(var0)) {
                    RSInterface[] var2 = GameObject.interfaces1834[var0];

                    for (int var3 = 0; var3 < var2.length; ++var3) {
                        RSInterface var4 = var2[var3];
                        if (null != var4.interfaceScript159) {
                            CS2Script var5 = new CS2Script();
                            System.out.printf("Interface (%d, %d) running script %d\n", var4.componentHash >> 16, var4.componentHash & 0xffff, var4.interfaceScript159[0]);
                            var5.arguments = var4.interfaceScript159;
                            var5.aClass11_2449 = var4;
                            DumpingTools.RunOnceAfterStartup();
                            CS2Script.runAssembledScript(2000000, var5);
                        }
                    }

                }
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "fn.F(" + var0 + ')');
        }
    }

    final int[] method154(int var1, byte var2) {
        try {
            int[] var10 = this.aClass114_2382.method1709(var1);
            if (this.aClass114_2382.aBoolean1580) {
                int[][] var4 = this.method162(var1, 0, (byte) -126);
                int[] var5 = Objects.requireNonNull(var4)[0];
                int[] var7 = var4[2];
                int[] var6 = var4[1];

                for (int var8 = 0; Class113.anInt1559 > var8; ++var8) {
                    var10[var8] = (var7[var8] + var5[var8] + var6[var8]) / 3;
                }
            }

            return var10;
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "fn.D(" + var1 + ',' + var2 + ')');
        }
    }

}
