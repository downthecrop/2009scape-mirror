package org.runite.client;

import org.rs09.client.rendering.Toolkit;

final class Class168 {

    int anInt2090 = 128;
    static RSInterface aClass11_2091 = null;
    boolean aBoolean2092 = false;
    boolean aBoolean2093 = true;
    int anInt2094 = 1190717;
    int anInt2095 = -1;
    static Class3_Sub28_Sub17 aClass3_Sub28_Sub17_2096;
    int anInt2098 = -1;
    static int anInt2099 = 0;
    int anInt2100 = 8;
    int anInt2101 = 16;
    boolean aBoolean2102 = true;
    int anInt2103 = 0;


    static void method2270(Class140_Sub4 var0) {
        try {
            if (Class44.anInt719 == var0.anInt2790 || var0.anInt2771 == -1 || var0.anInt2828 != 0 || SequenceDefinition.getAnimationDefinition(var0.anInt2771).duration[var0.anInt2832] < 1 + var0.anInt2760) {
                int var2 = var0.anInt2790 + -var0.anInt2800;
                int var3 = Class44.anInt719 + -var0.anInt2800;
                int var4 = var0.anInt2784 * 128 + 64 * var0.getSize();
                int var5 = var0.anInt2835 * 128 - -(var0.getSize() * 64);
                int var6 = 128 * var0.anInt2823 + var0.getSize() * 64;
                int var7 = 128 * var0.anInt2798 + var0.getSize() * 64;
                var0.anInt2819 = (var3 * var6 + var4 * (var2 - var3)) / var2;
                var0.anInt2829 = (var7 * var3 + var5 * (var2 - var3)) / var2;
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

    static void method2271(int var0, int var1, int var3) {
        try {

            InterfaceWidget var4 = InterfaceWidget.getWidget(11, var1);
            var4.flagUpdate();
            var4.anInt3597 = var3;
            var4.anInt3598 = var0;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "wl.F(" + var0 + ',' + var1 + ',' + 1 + ',' + var3 + ')');
        }
    }

    static void method2272(int[] var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9) {
        try {
            int var11 = var2;
            if (var2 < Toolkit.JAVA_TOOLKIT.clipRight) {
                if (Toolkit.JAVA_TOOLKIT.clipLeft > var2) {
                    var11 = Toolkit.JAVA_TOOLKIT.clipLeft;
                }

                int var12 = var7 + var2;
                if (Toolkit.JAVA_TOOLKIT.clipLeft < var12) {
                    if (Toolkit.JAVA_TOOLKIT.clipRight < var12) {
                        var12 = Toolkit.JAVA_TOOLKIT.clipRight;
                    }

                    int var13 = var8;
                    if (var8 < Toolkit.JAVA_TOOLKIT.clipBottom) {
                        int var14 = var8 + var6;
                        if (Toolkit.JAVA_TOOLKIT.clipTop > var8) {
                            var13 = Toolkit.JAVA_TOOLKIT.clipTop;
                        }

                        if (var14 > Toolkit.JAVA_TOOLKIT.clipTop) {
                            int var15 = var11 + Toolkit.JAVA_TOOLKIT.width * var13;
                            if (var5 == 9) {
                                var3 = 3 & var3 - -1;
                                var5 = 1;
                            }

                            int var16 = -var12 + var11 + Toolkit.JAVA_TOOLKIT.width;
                            var13 -= var8;
                            int var20 = var6 + -var13;
                            if (Toolkit.JAVA_TOOLKIT.clipBottom < var14) {
                                var14 = Toolkit.JAVA_TOOLKIT.clipBottom;
                            }

                            if (var5 == 10) {
                                var3 = var3 - -3 & 3;
                                var5 = 1;
                            }

                            var11 -= var2;
                            int var18 = -var11 + var7;
                            if (11 == var5) {
                                var3 = 3 & var3 + 3;
                                var5 = 8;
                            }

                            var12 -= var2;
                            int var17 = var7 + -var12;
                            var14 -= var8;
                            int var19 = var6 - var14;
                            int var21;
                            int var22;
                            if (var5 != 1) {
                                if (2 == var5) {
                                    if (var3 == 0) {
                                        for (var21 = var20 + -1; var19 <= var21; --var21) {
                                            for (var22 = var11; var12 > var22; ++var22) {
                                                if (var22 <= var21 >> 1) {
                                                    var0[var15] = var1;
                                                } else if (var9) {
                                                    var0[var15] = var4;
                                                }

                                                ++var15;
                                            }

                                            var15 += var16;
                                        }

                                    } else if (1 == var3) {
                                        for (var21 = var13; var14 > var21; ++var21) {
                                            for (var22 = var11; var22 < var12; ++var22) {
                                                if (0 <= var15 && var0.length > var15) {
                                                    if (var22 >= var21 << 1) {
                                                        var0[var15] = var1;
                                                    } else if (var9) {
                                                        var0[var15] = var4;
                                                    }

                                                    ++var15;
                                                } else {
                                                    ++var15;
                                                }
                                            }

                                            var15 += var16;
                                        }

                                    } else if (var3 == 2) {
                                        for (var21 = var13; var21 < var14; ++var21) {
                                            for (var22 = var18 + -1; var17 <= var22; --var22) {
                                                if (var21 >> 1 >= var22) {
                                                    var0[var15] = var1;
                                                } else if (var9) {
                                                    var0[var15] = var4;
                                                }

                                                ++var15;
                                            }

                                            var15 += var16;
                                        }

                                    } else if (3 == var3) {
                                        for (var21 = var20 + -1; var19 <= var21; --var21) {
                                            for (var22 = -1 + var18; var17 <= var22; --var22) {
                                                if (var21 << 1 > var22) {
                                                    if (var9) {
                                                        var0[var15] = var4;
                                                    }
                                                } else {
                                                    var0[var15] = var1;
                                                }

                                                ++var15;
                                            }

                                            var15 += var16;
                                        }

                                    }
                                } else if (var5 != 3) {
                                    if (var5 != 4) {
                                        if (var5 != 5) {
                                            if (var5 == 6) {
                                                if (var3 == 0) {
                                                    for (var21 = var13; var14 > var21; ++var21) {
                                                        for (var22 = var11; var12 > var22; ++var22) {
                                                            if (var22 > var7 / 2) {
                                                                if (var9) {
                                                                    var0[var15] = var4;
                                                                }
                                                            } else {
                                                                var0[var15] = var1;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }

                                                if (var3 == 1) {
                                                    for (var21 = var13; var14 > var21; ++var21) {
                                                        for (var22 = var11; var12 > var22; ++var22) {
                                                            if (var21 > var6 / 2) {
                                                                if (var9) {
                                                                    var0[var15] = var4;
                                                                }
                                                            } else {
                                                                var0[var15] = var1;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }

                                                if (2 == var3) {
                                                    for (var21 = var13; var14 > var21; ++var21) {
                                                        for (var22 = var11; var22 < var12; ++var22) {
                                                            if (var22 >= var7 / 2) {
                                                                var0[var15] = var1;
                                                            } else if (var9) {
                                                                var0[var15] = var4;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }

                                                if (var3 == 3) {
                                                    for (var21 = var13; var14 > var21; ++var21) {
                                                        for (var22 = var11; var12 > var22; ++var22) {
                                                            if (var6 / 2 > var21) {
                                                                if (var9) {
                                                                    var0[var15] = var4;
                                                                }
                                                            } else {
                                                                var0[var15] = var1;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }
                                            }

                                            if (7 == var5) {
                                                if (0 == var3) {
                                                    for (var21 = var13; var21 < var14; ++var21) {
                                                        for (var22 = var11; var22 < var12; ++var22) {
                                                            if (var22 <= var21 + -(var6 / 2)) {
                                                                var0[var15] = var1;
                                                            } else if (var9) {
                                                                var0[var15] = var4;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }

                                                if (var3 == 1) {
                                                    for (var21 = var20 + -1; var21 >= var19; --var21) {
                                                        for (var22 = var11; var12 > var22; ++var22) {
                                                            if (var22 > -(var6 / 2) + var21) {
                                                                if (var9) {
                                                                    var0[var15] = var4;
                                                                }
                                                            } else {
                                                                var0[var15] = var1;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }

                                                if (var3 == 2) {
                                                    for (var21 = var20 + -1; var21 >= var19; --var21) {
                                                        for (var22 = -1 + var18; var22 >= var17; --var22) {
                                                            if (var22 > var21 + -(var6 / 2)) {
                                                                if (var9) {
                                                                    var0[var15] = var4;
                                                                }
                                                            } else {
                                                                var0[var15] = var1;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }

                                                if (3 == var3) {
                                                    for (var21 = var13; var14 > var21; ++var21) {
                                                        for (var22 = -1 + var18; var17 <= var22; --var22) {
                                                            if (var21 + -(var6 / 2) >= var22) {
                                                                var0[var15] = var1;
                                                            } else if (var9) {
                                                                var0[var15] = var4;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }
                                            }

                                            if (var5 == 8) {
                                                if (0 == var3) {
                                                    for (var21 = var13; var14 > var21; ++var21) {
                                                        for (var22 = var11; var22 < var12; ++var22) {
                                                            if (-(var6 / 2) + var21 <= var22) {
                                                                var0[var15] = var1;
                                                            } else if (var9) {
                                                                var0[var15] = var4;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }

                                                if (var3 == 1) {
                                                    for (var21 = -1 + var20; var21 >= var19; --var21) {
                                                        for (var22 = var11; var22 < var12; ++var22) {
                                                            if (-(var6 / 2) + var21 > var22) {
                                                                if (var9) {
                                                                    var0[var15] = var4;
                                                                }
                                                            } else {
                                                                var0[var15] = var1;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }

                                                if (var3 == 2) {
                                                    for (var21 = var20 - 1; var19 <= var21; --var21) {
                                                        for (var22 = -1 + var18; var22 >= var17; --var22) {
                                                            if (var21 - var6 / 2 > var22) {
                                                                if (var9) {
                                                                    var0[var15] = var4;
                                                                }
                                                            } else {
                                                                var0[var15] = var1;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }

                                                if (var3 == 3) {
                                                    for (var21 = var13; var21 < var14; ++var21) {
                                                        for (var22 = -1 + var18; var17 <= var22; --var22) {
                                                            if (var21 + -(var6 / 2) > var22) {
                                                                if (var9) {
                                                                    var0[var15] = var4;
                                                                }
                                                            } else {
                                                                var0[var15] = var1;
                                                            }

                                                            ++var15;
                                                        }

                                                        var15 += var16;
                                                    }

                                                    return;
                                                }
                                            }

                                        } else if (0 == var3) {
                                            for (var21 = var20 + -1; var21 >= var19; --var21) {
                                                for (var22 = -1 + var18; var22 >= var17; --var22) {
                                                    if (var21 >> 1 <= var22) {
                                                        var0[var15] = var1;
                                                    } else if (var9) {
                                                        var0[var15] = var4;
                                                    }

                                                    ++var15;
                                                }

                                                var15 += var16;
                                            }

                                        } else if (1 == var3) {
                                            for (var21 = -1 + var20; var21 >= var19; --var21) {
                                                for (var22 = var11; var22 < var12; ++var22) {
                                                    if (var22 > var21 << 1) {
                                                        if (var9) {
                                                            var0[var15] = var4;
                                                        }
                                                    } else {
                                                        var0[var15] = var1;
                                                    }

                                                    ++var15;
                                                }

                                                var15 += var16;
                                            }

                                        } else if (var3 == 2) {
                                            for (var21 = var13; var14 > var21; ++var21) {
                                                for (var22 = var11; var22 < var12; ++var22) {
                                                    if (var21 >> 1 > var22) {
                                                        if (var9) {
                                                            var0[var15] = var4;
                                                        }
                                                    } else {
                                                        var0[var15] = var1;
                                                    }

                                                    ++var15;
                                                }

                                                var15 += var16;
                                            }

                                        } else if (var3 == 3) {
                                            for (var21 = var13; var21 < var14; ++var21) {
                                                for (var22 = var18 + -1; var17 <= var22; --var22) {
                                                    if (var21 << 1 >= var22) {
                                                        var0[var15] = var1;
                                                    } else if (var9) {
                                                        var0[var15] = var4;
                                                    }

                                                    ++var15;
                                                }

                                                var15 += var16;
                                            }

                                        }
                                    } else if (var3 == 0) {
                                        for (var21 = var20 + -1; var21 >= var19; --var21) {
                                            for (var22 = var11; var12 > var22; ++var22) {
                                                if (var22 < var21 >> 1) {
                                                    if (var9) {
                                                        var0[var15] = var4;
                                                    }
                                                } else {
                                                    var0[var15] = var1;
                                                }

                                                ++var15;
                                            }

                                            var15 += var16;
                                        }

                                    } else if (1 == var3) {
                                        for (var21 = var13; var21 < var14; ++var21) {
                                            for (var22 = var11; var22 < var12; ++var22) {
                                                if (var22 <= var21 << 1) {
                                                    var0[var15] = var1;
                                                } else if (var9) {
                                                    var0[var15] = var4;
                                                }

                                                ++var15;
                                            }

                                            var15 += var16;
                                        }

                                    } else if (2 == var3) {
                                        for (var21 = var13; var21 < var14; ++var21) {
                                            for (var22 = var18 + -1; var22 >= var17; --var22) {
                                                if (var21 >> 1 <= var22) {
                                                    var0[var15] = var1;
                                                } else if (var9) {
                                                    var0[var15] = var4;
                                                }

                                                ++var15;
                                            }

                                            var15 += var16;
                                        }

                                    } else if (var3 == 3) {
                                        for (var21 = -1 + var20; var21 >= var19; --var21) {
                                            for (var22 = -1 + var18; var22 >= var17; --var22) {
                                                if (var22 <= var21 << 1) {
                                                    var0[var15] = var1;
                                                } else if (var9) {
                                                    var0[var15] = var4;
                                                }

                                                ++var15;
                                            }

                                            var15 += var16;
                                        }

                                    }
                                } else if (var3 == 0) {
                                    for (var21 = var20 - 1; var21 >= var19; --var21) {
                                        for (var22 = -1 + var18; var17 <= var22; --var22) {
                                            if (var22 <= var21 >> 1) {
                                                var0[var15] = var1;
                                            } else if (var9) {
                                                var0[var15] = var4;
                                            }

                                            ++var15;
                                        }

                                        var15 += var16;
                                    }

                                } else if (var3 == 1) {
                                    for (var21 = -1 + var20; var19 <= var21; --var21) {
                                        for (var22 = var11; var12 > var22; ++var22) {
                                            if (var22 >= var21 << 1) {
                                                var0[var15] = var1;
                                            } else if (var9) {
                                                var0[var15] = var4;
                                            }

                                            ++var15;
                                        }

                                        var15 += var16;
                                    }

                                } else if (2 == var3) {
                                    for (var21 = var13; var21 < var14; ++var21) {
                                        for (var22 = var11; var12 > var22; ++var22) {
                                            if (var21 >> 1 >= var22) {
                                                var0[var15] = var1;
                                            } else if (var9) {
                                                var0[var15] = var4;
                                            }

                                            ++var15;
                                        }

                                        var15 += var16;
                                    }

                                } else if (3 == var3) {
                                    for (var21 = var13; var21 < var14; ++var21) {
                                        for (var22 = var18 - 1; var17 <= var22; --var22) {
                                            if (var22 < var21 << 1) {
                                                if (var9) {
                                                    var0[var15] = var4;
                                                }
                                            } else {
                                                var0[var15] = var1;
                                            }

                                            ++var15;
                                        }

                                        var15 += var16;
                                    }

                                }
                            } else if (var3 == 0) {
                                for (var21 = var13; var21 < var14; ++var21) {
                                    for (var22 = var11; var22 < var12; ++var22) {
                                        if (var21 >= var22) {
                                            var0[var15] = var1;
                                        } else if (var9) {
                                            var0[var15] = var4;
                                        }

                                        ++var15;
                                    }

                                    var15 += var16;
                                }

                            } else if (1 == var3) {
                                for (var21 = var20 + -1; var21 >= var19; --var21) {
                                    for (var22 = var11; var22 < var12; ++var22) {
                                        if (var21 >= var22) {
                                            var0[var15] = var1;
                                        } else if (var9) {
                                            var0[var15] = var4;
                                        }

                                        ++var15;
                                    }

                                    var15 += var16;
                                }

                            } else if (2 == var3) {
                                for (var21 = var13; var21 < var14; ++var21) {
                                    for (var22 = var11; var12 > var22; ++var22) {
                                        if (var22 >= var21) {
                                            var0[var15] = var1;
                                        } else if (var9) {
                                            var0[var15] = var4;
                                        }

                                        ++var15;
                                    }

                                    var15 += var16;
                                }

                            } else if (var3 == 3) {
                                for (var21 = var20 + -1; var19 <= var21; --var21) {
                                    for (var22 = var11; var12 > var22; ++var22) {
                                        if (var22 < var21) {
                                            if (var9) {
                                                var0[var15] = var4;
                                            }
                                        } else {
                                            var0[var15] = var1;
                                        }

                                        ++var15;
                                    }

                                    var15 += var16;
                                }

                            }
                        }
                    }
                }
            }
        } catch (RuntimeException var23) {
            throw ClientErrorException.clientError(var23, "wl.C(" + "null" + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + (byte) 21 + ')');
        }
    }

    static RSInterface method2273(RSInterface iface) {
        if (iface.parentId != -1) {
            return Class7.getRSInterface(iface.parentId);
        }

        int var3 = iface.componentHash >>> 16;
        Class80<Class3_Sub31> var4 = new Class80<>(Class3_Sub13_Sub17.aHashTable_3208);

        for (Class3_Sub31 var2 = var4.method1393(); null != var2; var2 = var4.method1392()) {
            if (var2.anInt2602 == var3) {
                return Class7.getRSInterface((int) var2.linkableKey);
            }
        }

        return null;
    }


    final void method2274(DataBuffer var2, int var3) {
        try {
            while (true) {
                int var4 = var2.readUnsignedByte();
                if (var4 == 0) {

                    return;
                }

                this.method2279(var4, var2, var3);
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "wl.H(" + 24559 + ',' + (var2 != null ? "{...}" : "null") + ',' + var3 + ')');
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
                Class3_Sub13_Sub23_Sub1.method282(var15, var16, 102, var18, var4);
                Class3_Sub13_Sub23_Sub1.method282(var15, var18, -44, var19, var0);
                Class3_Sub13_Sub23_Sub1.method282(var15, var19, -61, var17, var4);
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
                                Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var16], var18, -53, var17, var4);
                            }

                            if (var24 >= Class159.anInt2020) {
                                Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var24], var18, 96, var17, var4);
                            }
                        } else {
                            var17 = GameObject.anIntArray1838[var9];
                            var18 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var7 + var6, Class101.anInt1425);
                            var19 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var7 + var6, Class101.anInt1425);
                            var20 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, var6 - -var17, Class101.anInt1425);
                            var21 = Class40.method1040(Class3_Sub28_Sub18.anInt3765, -var17 + var6, Class101.anInt1425);
                            if (Class57.anInt902 >= var16) {
                                var22 = Class38.anIntArrayArray663[var16];
                                Class3_Sub13_Sub23_Sub1.method282(var22, var19, 116, var21, var4);
                                Class3_Sub13_Sub23_Sub1.method282(var22, var21, 125, var20, var0);
                                Class3_Sub13_Sub23_Sub1.method282(var22, var20, 87, var18, var4);
                            }

                            if (Class159.anInt2020 <= var24) {
                                var22 = Class38.anIntArrayArray663[var24];
                                Class3_Sub13_Sub23_Sub1.method282(var22, var19, 110, var21, var4);
                                Class3_Sub13_Sub23_Sub1.method282(var22, var21, -114, var20, var0);
                                Class3_Sub13_Sub23_Sub1.method282(var22, var20, -88, var18, var4);
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
                                Class3_Sub13_Sub23_Sub1.method282(var22, var18, 126, var21, var4);
                                Class3_Sub13_Sub23_Sub1.method282(var22, var21, 103, var20, var0);
                                Class3_Sub13_Sub23_Sub1.method282(var22, var20, -61, var17, var4);
                            }

                            if (var24 >= Class159.anInt2020) {
                                var22 = Class38.anIntArrayArray663[var24];
                                Class3_Sub13_Sub23_Sub1.method282(var22, var18, 102, var21, var4);
                                Class3_Sub13_Sub23_Sub1.method282(var22, var21, -94, var20, var0);
                                Class3_Sub13_Sub23_Sub1.method282(var22, var20, 99, var17, var4);
                            }
                        } else {
                            if (var16 <= Class57.anInt902) {
                                Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var16], var18, 94, var17, var4);
                            }

                            if (var24 >= Class159.anInt2020) {
                                Class3_Sub13_Sub23_Sub1.method282(Class38.anIntArrayArray663[var24], var18, 126, var17, var4);
                            }
                        }
                    }
                }
            }

        } catch (RuntimeException var23) {
            throw ClientErrorException.clientError(var23, "wl.I(" + var0 + ',' + (byte) 109 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    static void method2277(int var0, int var1, int var2, int var3, byte var4) {
        try {
            Class3_Sub25 var5 = (Class3_Sub25) Class3_Sub2.aHashTable_2220.get((long) var3);
            if (var5 == null) {
                var5 = new Class3_Sub25();
                Class3_Sub2.aHashTable_2220.put((long) var3, var5);
            }

            if (var4 > 16) {
                if (var1 >= var5.anIntArray2547.length) {
                    int[] var6 = new int[var1 - -1];
                    int[] var7 = new int[1 + var1];

                    int var8;
                    for (var8 = 0; var8 < var5.anIntArray2547.length; ++var8) {
                        var6[var8] = var5.anIntArray2547[var8];
                        var7[var8] = var5.anIntArray2551[var8];
                    }

                    for (var8 = var5.anIntArray2547.length; var1 > var8; ++var8) {
                        var6[var8] = -1;
                        var7[var8] = 0;
                    }

                    var5.anIntArray2547 = var6;
                    var5.anIntArray2551 = var7;
                }

                var5.anIntArray2547[var1] = var0;
                var5.anIntArray2551[var1] = var2;
            }
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "wl.A(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
        }
    }

    static void method2278(int var0) {
        try {
            boolean var1 = false;

            while (!var1) {
                var1 = true;

                for (int var2 = 0; -1 + Unsorted.menuOptionCount > var2; ++var2) {
                    if (Class3_Sub13_Sub7.aShortArray3095[var2] < 1000 && Class3_Sub13_Sub7.aShortArray3095[1 + var2] > 1000) {
                        RSString var3 = Class163_Sub2_Sub1.aClass94Array4016[var2];
                        var1 = false;
                        Class163_Sub2_Sub1.aClass94Array4016[var2] = Class163_Sub2_Sub1.aClass94Array4016[1 + var2];
                        Class163_Sub2_Sub1.aClass94Array4016[1 + var2] = var3;
                        RSString var4 = Class140_Sub7.aClass94Array2935[var2];
                        Class140_Sub7.aClass94Array2935[var2] = Class140_Sub7.aClass94Array2935[var2 + 1];
                        Class140_Sub7.aClass94Array2935[var2 - -1] = var4;
                        int var5 = Class117.anIntArray1613[var2];
                        Class117.anIntArray1613[var2] = Class117.anIntArray1613[1 + var2];
                        Class117.anIntArray1613[var2 + 1] = var5;
                        var5 = Class27.anIntArray512[var2];
                        Class27.anIntArray512[var2] = Class27.anIntArray512[var2 + 1];
                        Class27.anIntArray512[1 + var2] = var5;
                        var5 = Class114.anIntArray1578[var2];
                        Class114.anIntArray1578[var2] = Class114.anIntArray1578[1 + var2];
                        Class114.anIntArray1578[var2 - -1] = var5;
                        short var6 = Class3_Sub13_Sub7.aShortArray3095[var2];
                        Class3_Sub13_Sub7.aShortArray3095[var2] = Class3_Sub13_Sub7.aShortArray3095[1 + var2];
                        Class3_Sub13_Sub7.aShortArray3095[var2 + 1] = var6;
                        long var7 = Class3_Sub13_Sub22.aLongArray3271[var2];
                        Class3_Sub13_Sub22.aLongArray3271[var2] = Class3_Sub13_Sub22.aLongArray3271[var2 + 1];
                        Class3_Sub13_Sub22.aLongArray3271[var2 - -1] = var7;
                    }
                }
            }

        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "wl.D(" + var0 + ')');
        }
    }

    private void method2279(int var2, DataBuffer var3, int var4) {
        try {
            if (var2 == 1) {
                this.anInt2103 = RSInterface.method869(116, var3.readMedium());
            } else if (var2 == 2) {
                this.anInt2095 = var3.readUnsignedByte();
            } else if (3 == var2) {
                this.anInt2095 = var3.readUnsignedShort();
                if (this.anInt2095 == '\uffff') {
                    this.anInt2095 = -1;
                }
            } else if (5 == var2) {
                this.aBoolean2102 = false;
            } else if (var2 == 7) {
                this.anInt2098 = RSInterface.method869(116, var3.readMedium());
            } else if (var2 == 8) {
                Class3_Sub13_Sub6.anInt3081 = var4;
            } else if (var2 == 9) {
                this.anInt2090 = var3.readUnsignedShort();
            } else if (var2 == 10) {
                this.aBoolean2093 = false;
            } else if (var2 == 11) {
                this.anInt2100 = var3.readUnsignedByte();
            } else if (12 == var2) {
                this.aBoolean2092 = true;
            } else if (13 == var2) {
                this.anInt2094 = var3.readMedium();
            } else if (var2 == 14) {
                this.anInt2101 = var3.readUnsignedByte();
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "wl.E(" + 0 + ',' + var2 + ',' + (var3 != null ? "{...}" : "null") + ',' + var4 + ')');
        }
    }

    static void method2280(int var1) {
        try {

            InterfaceWidget var2 = InterfaceWidget.getWidget(11, var1);
            var2.a();
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "wl.B(" + 2714 + ',' + var1 + ')');
        }
    }

}
