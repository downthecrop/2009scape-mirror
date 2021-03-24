package org.runite.client;

import org.rs09.client.rendering.Toolkit;
import org.rs09.client.rendering.java.JavaToolkit;

// TODO LDToolkit
public final class Class74 {

    static int[] anIntArray1097;
    static int[] anIntArray1098;

    static JavaToolkit toolkit = Toolkit.JAVA_TOOLKIT;

    @Deprecated
    private static int[] getBuffer() {
        return Toolkit.JAVA_TOOLKIT.getBuffer();
    }

    static void method1310() {
        anIntArray1097 = null;
        anIntArray1098 = null;
    }

    private static void method1313(int var0, int var1) {
        if (var0 >= Toolkit.JAVA_TOOLKIT.clipLeft && var1 >= Toolkit.JAVA_TOOLKIT.clipTop && var0 < Toolkit.JAVA_TOOLKIT.clipRight && var1 < Toolkit.JAVA_TOOLKIT.clipBottom) {
            getBuffer()[var0 + var1 * Toolkit.JAVA_TOOLKIT.width] = 16776960;
        }
    }

    static void method1314(int[] var0, int[] var1) {
        if (var0.length == Toolkit.JAVA_TOOLKIT.clipBottom - Toolkit.JAVA_TOOLKIT.clipTop && var1.length == Toolkit.JAVA_TOOLKIT.clipBottom - Toolkit.JAVA_TOOLKIT.clipTop) {
            anIntArray1097 = var0;
            anIntArray1098 = var1;
        } else {
            throw new IllegalArgumentException();
        }
    }

    static void method1315(int var0, int var1, int var2, int var3, int var4, int var5) {
        method1321(var0, var1, var2, var4, var5);
        method1321(var0, var1 + var3 - 1, var2, var4, var5);
        if (var3 >= 3) {
            method1327(var0, var1 + 1, var3 - 2, var4, var5);
            method1327(var0 + var2 - 1, var1 + 1, var3 - 2, var4, var5);
        }

    }

    public static void setClipping(int[] var0) {
        Toolkit.JAVA_TOOLKIT.clipLeft = var0[0];
        Toolkit.JAVA_TOOLKIT.clipTop = var0[1];
        Toolkit.JAVA_TOOLKIT.clipRight = var0[2];
        Toolkit.JAVA_TOOLKIT.clipBottom = var0[3];
        method1310();
    }

    static void setBuffer(int[] buffer, int width, int height) {
        Toolkit.JAVA_TOOLKIT.setBuffer(buffer);
        Toolkit.JAVA_TOOLKIT.width = width;
        Toolkit.JAVA_TOOLKIT.height = height;
        setClipping(0, 0, width, height);
    }

    static void method1320() {
        int var0 = 0;

        int var1;
        for (var1 = Toolkit.JAVA_TOOLKIT.width * Toolkit.JAVA_TOOLKIT.height - 7; var0 < var1; getBuffer()[var0++] = 0) {
            getBuffer()[var0++] = 0;
            getBuffer()[var0++] = 0;
            getBuffer()[var0++] = 0;
            getBuffer()[var0++] = 0;
            getBuffer()[var0++] = 0;
            getBuffer()[var0++] = 0;
            getBuffer()[var0++] = 0;
        }

        for (var1 += 7; var0 < var1; getBuffer()[var0++] = 0) {
        }

    }

    private static void method1321(int var0, int var1, int var2, int var3, int var4) {
        if (var1 >= Toolkit.JAVA_TOOLKIT.clipTop && var1 < Toolkit.JAVA_TOOLKIT.clipBottom) {
            if (var0 < Toolkit.JAVA_TOOLKIT.clipLeft) {
                var2 -= Toolkit.JAVA_TOOLKIT.clipLeft - var0;
                var0 = Toolkit.JAVA_TOOLKIT.clipLeft;
            }

            if (var0 + var2 > Toolkit.JAVA_TOOLKIT.clipRight) {
                var2 = Toolkit.JAVA_TOOLKIT.clipRight - var0;
            }

            int var5 = 256 - var4;
            int var6 = (var3 >> 16 & 255) * var4;
            int var7 = (var3 >> 8 & 255) * var4;
            int var8 = (var3 & 255) * var4;
            int var12 = var0 + var1 * Toolkit.JAVA_TOOLKIT.width;

            for (int var13 = 0; var13 < var2; ++var13) {
                int var9 = (getBuffer()[var12] >> 16 & 255) * var5;
                int var10 = (getBuffer()[var12] >> 8 & 255) * var5;
                int var11 = (getBuffer()[var12] & 255) * var5;
                int var14 = (var6 + var9 >> 8 << 16) + (var7 + var10 >> 8 << 8) + (var8 + var11 >> 8);
                getBuffer()[var12++] = var14;
            }

        }
    }

    static void method1322(int var0, int var1, int var2, int var3, int var4, int var5) {
        int var6 = var2 - var0;
        int var7 = var3 - var1;
        int var8 = var6 >= 0 ? var6 : -var6;
        int var9 = var7 >= 0 ? var7 : -var7;
        int var10 = var8;
        if (var8 < var9) {
            var10 = var9;
        }

        if (var10 != 0) {
            int var11 = (var6 << 16) / var10;
            int var12 = (var7 << 16) / var10;
            if (var12 <= var11) {
                var11 = -var11;
            } else {
                var12 = -var12;
            }

            int var13 = var5 * var12 >> 17;
            int var14 = var5 * var12 + 1 >> 17;
            int var15 = var5 * var11 >> 17;
            int var16 = var5 * var11 + 1 >> 17;
            var0 -= Class51.method1139();
            var1 -= Class51.method1153();
            int var17 = var0 + var13;
            int var18 = var0 - var14;
            int var19 = var0 + var6 - var14;
            int var20 = var0 + var6 + var13;
            int var21 = var1 + var15;
            int var22 = var1 - var16;
            int var23 = var1 + var7 - var16;
            int var24 = var1 + var7 + var15;
            Class51.method1148(var17, var18, var19);
            Class51.method1144(var21, var22, var23, var17, var18, var19, var4);
            Class51.method1148(var17, var19, var20);
            Class51.method1144(var21, var23, var24, var17, var19, var20, var4);
        }
    }

    public static void setClipping(int left, int top, int right, int bottom) {
        if (left < 0) {
            left = 0;
        }

        if (top < 0) {
            top = 0;
        }

        if (right > Toolkit.JAVA_TOOLKIT.width) {
            right = Toolkit.JAVA_TOOLKIT.width;
        }

        if (bottom > Toolkit.JAVA_TOOLKIT.height) {
            bottom = Toolkit.JAVA_TOOLKIT.height;
        }

        Toolkit.JAVA_TOOLKIT.clipLeft = left;
        Toolkit.JAVA_TOOLKIT.clipTop = top;
        Toolkit.JAVA_TOOLKIT.clipRight = right;
        Toolkit.JAVA_TOOLKIT.clipBottom = bottom;
        method1310();
    }

    static void method1325(int[] var0) {
        var0[0] = Toolkit.JAVA_TOOLKIT.clipLeft;
        var0[1] = Toolkit.JAVA_TOOLKIT.clipTop;
        var0[2] = Toolkit.JAVA_TOOLKIT.clipRight;
        var0[3] = Toolkit.JAVA_TOOLKIT.clipBottom;
    }

    static void method1326(int var0, int var1, int var2, int var3) {
        if (Toolkit.JAVA_TOOLKIT.clipLeft < var0) {
            Toolkit.JAVA_TOOLKIT.clipLeft = var0;
        }

        if (Toolkit.JAVA_TOOLKIT.clipTop < var1) {
            Toolkit.JAVA_TOOLKIT.clipTop = var1;
        }

        if (Toolkit.JAVA_TOOLKIT.clipRight > var2) {
            Toolkit.JAVA_TOOLKIT.clipRight = var2;
        }

        if (Toolkit.JAVA_TOOLKIT.clipBottom > var3) {
            Toolkit.JAVA_TOOLKIT.clipBottom = var3;
        }

        method1310();
    }

    private static void method1327(int var0, int var1, int var2, int var3, int var4) {
        if (var0 >= Toolkit.JAVA_TOOLKIT.clipLeft && var0 < Toolkit.JAVA_TOOLKIT.clipRight) {
            if (var1 < Toolkit.JAVA_TOOLKIT.clipTop) {
                var2 -= Toolkit.JAVA_TOOLKIT.clipTop - var1;
                var1 = Toolkit.JAVA_TOOLKIT.clipTop;
            }

            if (var1 + var2 > Toolkit.JAVA_TOOLKIT.clipBottom) {
                var2 = Toolkit.JAVA_TOOLKIT.clipBottom - var1;
            }

            int var5 = 256 - var4;
            int var6 = (var3 >> 16 & 255) * var4;
            int var7 = (var3 >> 8 & 255) * var4;
            int var8 = (var3 & 255) * var4;
            int var12 = var0 + var1 * Toolkit.JAVA_TOOLKIT.width;

            for (int var13 = 0; var13 < var2; ++var13) {
                int var9 = (getBuffer()[var12] >> 16 & 255) * var5;
                int var10 = (getBuffer()[var12] >> 8 & 255) * var5;
                int var11 = (getBuffer()[var12] & 255) * var5;
                int var14 = (var6 + var9 >> 8 << 16) + (var7 + var10 >> 8 << 8) + (var8 + var11 >> 8);
                getBuffer()[var12] = var14;
                var12 += Toolkit.JAVA_TOOLKIT.width;
            }

        }
    }

    static void method1328(int var0, int var1, int var2, int var3, int var4) {
        var2 -= var0;
        var3 -= var1;
        if (var3 == 0) {
            if (var2 >= 0) {
                Toolkit.JAVA_TOOLKIT.drawHorizontalLine(var0, var1, var2 + 1, var4);
            } else {
                Toolkit.JAVA_TOOLKIT.drawHorizontalLine(var0 + var2, var1, -var2 + 1, var4);
            }

        } else if (var2 == 0) {
            if (var3 >= 0) {
                Toolkit.JAVA_TOOLKIT.drawVerticalLine(var0, var1, var3 + 1, var4);
            } else {
                Toolkit.JAVA_TOOLKIT.drawVerticalLine(var0, var1 + var3, -var3 + 1, var4);
            }

        } else {
            if (var2 + var3 < 0) {
                var0 += var2;
                var2 = -var2;
                var1 += var3;
                var3 = -var3;
            }

            int var5;
            int var6;
            if (var2 > var3) {
                var1 <<= 16;
                var1 += '\u8000';
                var3 <<= 16;
                var5 = (int) Math.floor((double) var3 / (double) var2 + 0.5D);
                var2 += var0;
                if (var0 < Toolkit.JAVA_TOOLKIT.clipLeft) {
                    var1 += var5 * (Toolkit.JAVA_TOOLKIT.clipLeft - var0);
                    var0 = Toolkit.JAVA_TOOLKIT.clipLeft;
                }

                if (var2 >= Toolkit.JAVA_TOOLKIT.clipRight) {
                    var2 = Toolkit.JAVA_TOOLKIT.clipRight - 1;
                }

                while (var0 <= var2) {
                    var6 = var1 >> 16;
                    if (var6 >= Toolkit.JAVA_TOOLKIT.clipTop && var6 < Toolkit.JAVA_TOOLKIT.clipBottom) {
                        getBuffer()[var0 + var6 * Toolkit.JAVA_TOOLKIT.width] = var4;
                    }

                    var1 += var5;
                    ++var0;
                }
            } else {
                var0 <<= 16;
                var0 += '\u8000';
                var2 <<= 16;
                var5 = (int) Math.floor((double) var2 / (double) var3 + 0.5D);
                var3 += var1;
                if (var1 < Toolkit.JAVA_TOOLKIT.clipTop) {
                    var0 += var5 * (Toolkit.JAVA_TOOLKIT.clipTop - var1);
                    var1 = Toolkit.JAVA_TOOLKIT.clipTop;
                }

                if (var3 >= Toolkit.JAVA_TOOLKIT.clipBottom) {
                    var3 = Toolkit.JAVA_TOOLKIT.clipBottom - 1;
                }

                while (var1 <= var3) {
                    var6 = var0 >> 16;
                    if (var6 >= Toolkit.JAVA_TOOLKIT.clipLeft && var6 < Toolkit.JAVA_TOOLKIT.clipRight) {
                        getBuffer()[var6 + var1 * Toolkit.JAVA_TOOLKIT.width] = var4;
                    }

                    var0 += var5;
                    ++var1;
                }
            }

        }
    }

    private static void method1329(int var0, int var1, int var2) {
        if (var2 == 0) {
            method1313(var0, var1);
        } else {
            if (var2 < 0) {
                var2 = -var2;
            }

            int var4 = var1 - var2;
            if (var4 < Toolkit.JAVA_TOOLKIT.clipTop) {
                var4 = Toolkit.JAVA_TOOLKIT.clipTop;
            }

            int var5 = var1 + var2 + 1;
            if (var5 > Toolkit.JAVA_TOOLKIT.clipBottom) {
                var5 = Toolkit.JAVA_TOOLKIT.clipBottom;
            }

            int var6 = var4;
            int var7 = var2 * var2;
            int var8 = 0;
            int var9 = var1 - var4;
            int var10 = var9 * var9;
            int var11 = var10 - var9;
            if (var1 > var5) {
                var1 = var5;
            }

            int var12;
            int var13;
            int var14;
            int var15;
            while (var6 < var1) {
                while (var11 <= var7 || var10 <= var7) {
                    var10 += var8 + var8;
                    var11 += var8++ + var8;
                }

                var12 = var0 - var8 + 1;
                if (var12 < Toolkit.JAVA_TOOLKIT.clipLeft) {
                    var12 = Toolkit.JAVA_TOOLKIT.clipLeft;
                }

                var13 = var0 + var8;
                if (var13 > Toolkit.JAVA_TOOLKIT.clipRight) {
                    var13 = Toolkit.JAVA_TOOLKIT.clipRight;
                }

                var14 = var12 + var6 * Toolkit.JAVA_TOOLKIT.width;

                for (var15 = var12; var15 < var13; ++var15) {
                    getBuffer()[var14++] = 16776960;
                }

                ++var6;
                var10 -= var9-- + var9;
                var11 -= var9 + var9;
            }

            var8 = var2;
            var9 = var6 - var1;
            var11 = var9 * var9 + var7;
            var10 = var11 - var2;

            for (var11 -= var9; var6 < var5; var10 += var9++ + var9) {
                while (var11 > var7 && var10 > var7) {
                    var11 -= var8-- + var8;
                    var10 -= var8 + var8;
                }

                var12 = var0 - var8;
                if (var12 < Toolkit.JAVA_TOOLKIT.clipLeft) {
                    var12 = Toolkit.JAVA_TOOLKIT.clipLeft;
                }

                var13 = var0 + var8;
                if (var13 > Toolkit.JAVA_TOOLKIT.clipRight - 1) {
                    var13 = Toolkit.JAVA_TOOLKIT.clipRight - 1;
                }

                var14 = var12 + var6 * Toolkit.JAVA_TOOLKIT.width;

                for (var15 = var12; var15 <= var13; ++var15) {
                    getBuffer()[var14++] = 16776960;
                }

                ++var6;
                var11 += var9 + var9;
            }

        }
    }

    static void method1330(int var0, int var1, int var2, int var4) {
        if (var4 != 0) {
            if (var4 == 256) {
                method1329(var0, var1, var2);
            } else {
                if (var2 < 0) {
                    var2 = -var2;
                }

                int var5 = 256 - var4;
                int var6 = (16776960 >> 16 & 255) * var4;
                int var7 = (16776960 >> 8 & 255) * var4;
                int var8 = 0;
                int var12 = var1 - var2;
                if (var12 < Toolkit.JAVA_TOOLKIT.clipTop) {
                    var12 = Toolkit.JAVA_TOOLKIT.clipTop;
                }

                int var13 = var1 + var2 + 1;
                if (var13 > Toolkit.JAVA_TOOLKIT.clipBottom) {
                    var13 = Toolkit.JAVA_TOOLKIT.clipBottom;
                }

                int var14 = var12;
                int var15 = var2 * var2;
                int var16 = 0;
                int var17 = var1 - var12;
                int var18 = var17 * var17;
                int var19 = var18 - var17;
                if (var1 > var13) {
                    var1 = var13;
                }

                int var9;
                int var10;
                int var11;
                int var21;
                int var20;
                int var23;
                int var22;
                int var24;
                while (var14 < var1) {
                    while (var19 <= var15 || var18 <= var15) {
                        var18 += var16 + var16;
                        var19 += var16++ + var16;
                    }

                    var20 = var0 - var16 + 1;
                    if (var20 < Toolkit.JAVA_TOOLKIT.clipLeft) {
                        var20 = Toolkit.JAVA_TOOLKIT.clipLeft;
                    }

                    var21 = var0 + var16;
                    if (var21 > Toolkit.JAVA_TOOLKIT.clipRight) {
                        var21 = Toolkit.JAVA_TOOLKIT.clipRight;
                    }

                    var22 = var20 + var14 * Toolkit.JAVA_TOOLKIT.width;

                    for (var23 = var20; var23 < var21; ++var23) {
                        var9 = (getBuffer()[var22] >> 16 & 255) * var5;
                        var10 = (getBuffer()[var22] >> 8 & 255) * var5;
                        var11 = (getBuffer()[var22] & 255) * var5;
                        var24 = (var6 + var9 >> 8 << 16) + (var7 + var10 >> 8 << 8) + (var8 + var11 >> 8);
                        getBuffer()[var22++] = var24;
                    }

                    ++var14;
                    var18 -= var17-- + var17;
                    var19 -= var17 + var17;
                }

                var16 = var2;
                var17 = -var17;
                var19 = var17 * var17 + var15;
                var18 = var19 - var2;

                for (var19 -= var17; var14 < var13; var18 += var17++ + var17) {
                    while (var19 > var15 && var18 > var15) {
                        var19 -= var16-- + var16;
                        var18 -= var16 + var16;
                    }

                    var20 = var0 - var16;
                    if (var20 < Toolkit.JAVA_TOOLKIT.clipLeft) {
                        var20 = Toolkit.JAVA_TOOLKIT.clipLeft;
                    }

                    var21 = var0 + var16;
                    if (var21 > Toolkit.JAVA_TOOLKIT.clipRight - 1) {
                        var21 = Toolkit.JAVA_TOOLKIT.clipRight - 1;
                    }

                    var22 = var20 + var14 * Toolkit.JAVA_TOOLKIT.width;

                    for (var23 = var20; var23 <= var21; ++var23) {
                        var9 = (getBuffer()[var22] >> 16 & 255) * var5;
                        var10 = (getBuffer()[var22] >> 8 & 255) * var5;
                        var11 = (getBuffer()[var22] & 255) * var5;
                        var24 = (var6 + var9 >> 8 << 16) + (var7 + var10 >> 8 << 8) + (var8 + var11 >> 8);
                        getBuffer()[var22++] = var24;
                    }

                    ++var14;
                    var19 += var17 + var17;
                }

            }
        }
    }

    public static void resetClipping() {
        Toolkit.JAVA_TOOLKIT.clipLeft = 0;
        Toolkit.JAVA_TOOLKIT.clipTop = 0;
        Toolkit.JAVA_TOOLKIT.clipRight = Toolkit.JAVA_TOOLKIT.width;
        Toolkit.JAVA_TOOLKIT.clipBottom = Toolkit.JAVA_TOOLKIT.height;
        method1310();
    }

    static void method1332(int var0, int var1, int[] var3, int[] var4) {
        int var5 = var0 + var1 * Toolkit.JAVA_TOOLKIT.width;

        for (var1 = 0; var1 < var3.length; ++var1) {
            int var6 = var5 + var3[var1];

            for (var0 = -var4[var1]; var0 < 0; ++var0) {
                getBuffer()[var6++] = 0;
            }

            var5 += Toolkit.JAVA_TOOLKIT.width;
        }

    }

}
