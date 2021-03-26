package org.runite.client;

import org.rs09.client.data.ReferenceCache;

import java.awt.*;
import java.awt.image.*;

final class Class158_Sub1 extends Class158 implements ImageProducer, ImageObserver {

    static RSString[] aClass94Array2977 = new RSString[5];
    static int anInt3158 = -8 + (int) (17.0D * Math.random());
    static int anInt1463 = -16 + (int) (Math.random() * 33.0D);
    static byte[][][] aByteArrayArrayArray1828;
    private ImageConsumer anImageConsumer2978;
    private ColorModel aColorModel2979;
    static Class3_Sub1 aClass3_Sub1_2980 = new Class3_Sub1(0, -1);
    static boolean aBoolean2981 = false;
    static ReferenceCache aReferenceCache_2982 = new ReferenceCache(32);


    public final synchronized void addConsumer(ImageConsumer var1) {
        try {
            this.anImageConsumer2978 = var1;
            var1.setDimensions(this.anInt2012, this.anInt2011);
            var1.setProperties(null);
            var1.setColorModel(this.aColorModel2979);
            var1.setHints(14);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "di.addConsumer(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    private synchronized void method2188(int var1, int var2, int var3, int var5) {
        try {
            if (null != this.anImageConsumer2978) {
                this.anImageConsumer2978.setPixels(var3, var5, var1, var2, this.aColorModel2979, this.anIntArray2007, var5 * this.anInt2012 + var3, this.anInt2012);
                this.anImageConsumer2978.imageComplete(2);

            }
        } catch (RuntimeException var7) {
            throw ClientErrorException.clientError(var7, "di.N(" + var1 + ',' + var2 + ',' + var3 + ',' + (byte) -124 + ',' + var5 + ')');
        }
    }

    public final synchronized void removeConsumer(ImageConsumer var1) {
        try {
            if (this.anImageConsumer2978 == var1) {
                this.anImageConsumer2978 = null;
            }

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "di.removeConsumer(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    final void drawGraphics(int var1, int var2, int var4, Graphics var5, int var6) {
        try {
            this.method2188(var1, var4, var2, var6);
            Shape var7 = var5.getClip();
            var5.clipRect(var2, var6, var1, var4);
            var5.drawImage(this.anImage2009, 0, 0, this);
            var5.setClip(var7);
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "di.E(" + var1 + ',' + var2 + ',' + 6260 + ',' + var4 + ',' + (var5 != null ? "{...}" : "null") + ',' + var6 + ')');
        }
    }

    public final void startProduction(ImageConsumer var1) {
        try {
            this.addConsumer(var1);
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "di.startProduction(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    private synchronized void method2190() {
        try {
            if (this.anImageConsumer2978 != null) {
                this.anImageConsumer2978.setPixels(0, 0, this.anInt2012, this.anInt2011, this.aColorModel2979, this.anIntArray2007, 0, this.anInt2012);
                this.anImageConsumer2978.imageComplete(2);
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "di.L(" + 19661184 + ')');
        }
    }

    public final synchronized boolean isConsumer(ImageConsumer var1) {
        try {
            return this.anImageConsumer2978 == var1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "di.isConsumer(" + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static boolean method2191(int var0, int var1, int var2, int var4, int var5, int var6, int var7, boolean var8, int var9, int var10, int var11) {
        try {
            int var12;
            int var13;
            for (var12 = 0; var12 < 104; ++var12) {
                for (var13 = 0; var13 < 104; ++var13) {
                    Class84.anIntArrayArray1160[var12][var13] = 0;
                    Class97.anIntArrayArray1373[var12][var13] = 99999999;
                }
            }

            var12 = var2;
            Class84.anIntArrayArray1160[var2][var10] = 99;
            var13 = var10;
            Class97.anIntArrayArray1373[var2][var10] = 0;
            byte var14 = 0;
            boolean var16 = false;
            int var15 = 0;
            TextureOperation38.anIntArray3456[var14] = var2;
            int var27 = var14 + 1;
            Class45.anIntArray729[var14] = var10;
            int[][] var17 = AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].anIntArrayArray1304;

            int var18;
            while (var15 != var27) {
                var13 = Class45.anIntArray729[var15];
                var12 = TextureOperation38.anIntArray3456[var15];
                var15 = 4095 & var15 + 1;
                if (var12 == var0 && var13 == var4) {
                    var16 = true;
                    break;
                }

                if (var9 != 0) {
                    if ((var9 < 5 || 10 == var9) && AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].method1488(var4, var12, var13, var0, var9 + -1, 1, var7)) {
                        var16 = true;
                        break;
                    }

                    if (var9 < 10 && AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].method1492(var4, -1 + var9, var0, var13, 1, var7, var12, 95)) {
                        var16 = true;
                        break;
                    }
                }

                if (var11 != 0 && 0 != var6 && AtmosphereParser.aClass91Array1182[WorldListCountry.localPlane].method1498(var0, var13, var12, 1, var11, var1, var4, var6)) {
                    var16 = true;
                    break;
                }

                var18 = 1 + Class97.anIntArrayArray1373[var12][var13];
                if (0 < var12 && Class84.anIntArrayArray1160[var12 + -1][var13] == 0 && (19661064 & var17[var12 + -1][var13]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = -1 + var12;
                    Class45.anIntArray729[var27] = var13;
                    var27 = var27 - -1 & 4095;
                    Class84.anIntArrayArray1160[-1 + var12][var13] = 2;
                    Class97.anIntArrayArray1373[-1 + var12][var13] = var18;
                }

                if (103 > var12 && Class84.anIntArrayArray1160[var12 + 1][var13] == 0 && (var17[var12 + 1][var13] & 19661184) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12 - -1;
                    Class45.anIntArray729[var27] = var13;
                    var27 = 1 + var27 & 4095;
                    Class84.anIntArrayArray1160[var12 - -1][var13] = 8;
                    Class97.anIntArrayArray1373[1 + var12][var13] = var18;
                }

                if (var13 > 0 && Class84.anIntArrayArray1160[var12][var13 - 1] == 0 && (19661058 & var17[var12][-1 + var13]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12;
                    Class45.anIntArray729[var27] = -1 + var13;
                    Class84.anIntArrayArray1160[var12][var13 - 1] = 1;
                    var27 = var27 + 1 & 4095;
                    Class97.anIntArrayArray1373[var12][-1 + var13] = var18;
                }

                if (103 > var13 && Class84.anIntArrayArray1160[var12][1 + var13] == 0 && (19661088 & var17[var12][var13 + 1]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12;
                    Class45.anIntArray729[var27] = var13 - -1;
                    var27 = 1 + var27 & 4095;
                    Class84.anIntArrayArray1160[var12][1 + var13] = 4;
                    Class97.anIntArrayArray1373[var12][var13 - -1] = var18;
                }

                if (var12 > 0 && var13 > 0 && Class84.anIntArrayArray1160[-1 + var12][var13 - 1] == 0 && (var17[var12 - 1][-1 + var13] & 19661070) == 0 && (19661064 & var17[var12 - 1][var13]) == 0 && (19661058 & var17[var12][-1 + var13]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = -1 + var12;
                    Class45.anIntArray729[var27] = var13 + -1;
                    var27 = 1 + var27 & 4095;
                    Class84.anIntArrayArray1160[-1 + var12][-1 + var13] = 3;
                    Class97.anIntArrayArray1373[var12 - 1][var13 + -1] = var18;
                }

                if (var12 < 103 && 0 < var13 && Class84.anIntArrayArray1160[var12 - -1][var13 - 1] == 0 && 0 == (19661187 & var17[var12 - -1][-1 + var13]) && (19661184 & var17[var12 - -1][var13]) == 0 && (19661058 & var17[var12][-1 + var13]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12 + 1;
                    Class45.anIntArray729[var27] = -1 + var13;
                    var27 = 4095 & var27 + 1;
                    Class84.anIntArrayArray1160[1 + var12][var13 + -1] = 9;
                    Class97.anIntArrayArray1373[var12 - -1][-1 + var13] = var18;
                }

                if (0 < var12 && var13 < 103 && 0 == Class84.anIntArrayArray1160[var12 + -1][var13 + 1] && 0 == (19661112 & var17[var12 + -1][1 + var13]) && 0 == (var17[var12 + -1][var13] & 19661064) && (19661088 & var17[var12][1 + var13]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12 - 1;
                    Class45.anIntArray729[var27] = 1 + var13;
                    Class84.anIntArrayArray1160[-1 + var12][var13 - -1] = 6;
                    var27 = 4095 & 1 + var27;
                    Class97.anIntArrayArray1373[-1 + var12][1 + var13] = var18;
                }

                if (var12 < 103 && var13 < 103 && Class84.anIntArrayArray1160[var12 - -1][1 + var13] == 0 && (19661280 & var17[1 + var12][var13 + 1]) == 0 && (var17[1 + var12][var13] & 19661184) == 0 && (19661088 & var17[var12][var13 - -1]) == 0) {
                    TextureOperation38.anIntArray3456[var27] = var12 + 1;
                    Class45.anIntArray729[var27] = var13 - -1;
                    Class84.anIntArrayArray1160[var12 + 1][1 + var13] = 12;
                    var27 = var27 - -1 & 4095;
                    Class97.anIntArrayArray1373[1 + var12][var13 - -1] = var18;
                }
            }

            Class129.anInt1692 = 0;
            int var19;
            if (!var16) {
                if (!var8) {
                    return false;
                }

                var18 = 1000;
                var19 = 100;
                byte var20 = 10;

                for (int var21 = var0 + -var20; var20 + var0 >= var21; ++var21) {
                    for (int var22 = var4 + -var20; var4 - -var20 >= var22; ++var22) {
                        if (var21 >= 0 && var22 >= 0 && 104 > var21 && var22 < 104 && 100 > Class97.anIntArrayArray1373[var21][var22]) {
                            int var24 = 0;
                            if (var4 > var22) {
                                var24 = var4 + -var22;
                            } else if (var6 + var4 - 1 < var22) {
                                var24 = 1 + (-var4 - var6) + var22;
                            }

                            int var23 = 0;
                            if (var0 <= var21) {
                                if (-1 + var11 + var0 < var21) {
                                    var23 = 1 - var11 - (var0 - var21);
                                }
                            } else {
                                var23 = var0 + -var21;
                            }

                            int var25 = var24 * var24 + var23 * var23;
                            if (var18 > var25 || var18 == var25 && Class97.anIntArrayArray1373[var21][var22] < var19) {
                                var13 = var22;
                                var18 = var25;
                                var12 = var21;
                                var19 = Class97.anIntArrayArray1373[var21][var22];
                            }
                        }
                    }
                }

                if (var18 == 1000) {
                    return false;
                }

                if (var2 == var12 && var10 == var13) {
                    return false;
                }

                Class129.anInt1692 = 1;
            }

            byte var28 = 0;
            TextureOperation38.anIntArray3456[var28] = var12;
            var15 = var28 + 1;
            Class45.anIntArray729[var28] = var13;

            for (var18 = var19 = Class84.anIntArrayArray1160[var12][var13]; var2 != var12 || var13 != var10; var18 = Class84.anIntArrayArray1160[var12][var13]) {
                if (var19 != var18) {
                    var19 = var18;
                    TextureOperation38.anIntArray3456[var15] = var12;
                    Class45.anIntArray729[var15++] = var13;
                }

                if ((var18 & 2) == 0) {
                    if (0 != (8 & var18)) {
                        --var12;
                    }
                } else {
                    ++var12;
                }

                if ((1 & var18) == 0) {
                    if (0 != (4 & var18)) {
                        --var13;
                    }
                } else {
                    ++var13;
                }
            }

            if (var15 > 0) {
                TextureOperation7.method299(100, var15, var5);
                return true;
            } else return var5 != 1;
        } catch (RuntimeException var26) {
            throw ClientErrorException.clientError(var26, "di.J(" + var0 + ',' + var1 + ',' + var2 + ',' + -1001 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var10 + ',' + var11 + ')');
        }
    }

    public final void method2179(Graphics var3) {
        try {
            this.method2190();
            var3.drawImage(this.anImage2009, 0, 0, this);
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "di.C(" + 0 + ',' + 0 + ',' + (var3 != null ? "{...}" : "null") + ',' + 0 + ')');
        }
    }

    final void method2185(int var1, int var3, Component var4) {
        try {
            this.anInt2011 = var1;
            this.anIntArray2007 = new int[var3 * var1 + 1];
            this.anInt2012 = var3;
            this.aColorModel2979 = new DirectColorModel(32, 16711680, 65280, 255);
            this.anImage2009 = var4.createImage(this);
            this.method2190();
            var4.prepareImage(this.anImage2009, this);
            this.method2190();
            var4.prepareImage(this.anImage2009, this);
            this.method2190();
            var4.prepareImage(this.anImage2009, this);
            this.method2182();

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "di.F(" + var1 + ',' + false + ',' + var3 + ',' + (var4 != null ? "{...}" : "null") + ')');
        }
    }

    public final boolean imageUpdate(Image var1, int var2, int var3, int var4, int var5, int var6) {
        try {
            return true;
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "di.imageUpdate(" + (var1 != null ? "{...}" : "null") + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    public final void requestTopDownLeftRightResend(ImageConsumer var1) {
    }

}
