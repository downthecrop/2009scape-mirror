package org.runite.client;

final class Class91 {

    static RSString[] aClass94Array1299 = new RSString[8];
    static int anInt1302 = 0;
    static Class33 aClass33_1305;
    static byte aByte1308;
    static short[] aShortArray1311;
    private final int anInt1300;
    private final int anInt1303;
    private final int anInt1306;
    private final int anInt1309;
    int[][] anIntArrayArray1304;


    Class91() {
        try {
            this.anInt1303 = 104;
            this.anInt1306 = 0;
            this.anInt1309 = 0;
            this.anInt1300 = 104;
            this.anIntArrayArray1304 = new int[this.anInt1300][this.anInt1303];
            this.method1496();
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "mj.<init>(" + 104 + ',' + 104 + ')');
        }
    }

    static boolean method1495(int var0, int var1, int var2, int var3, int var4) {
        int var5 = var3 * TextureOperation25.anInt3417 + var0 * Class145.anInt3153 >> 16;
        int var6 = var3 * Class145.anInt3153 - var0 * TextureOperation25.anInt3417 >> 16;
        int var7 = var1 * Class60.anInt936 + var6 * Unsorted.anInt1037 >> 16;
        int var8 = var1 * Unsorted.anInt1037 - var6 * Class60.anInt936 >> 16;
        if (var7 < 1) {
            var7 = 1;
        }

        int var9 = (var5 << 9) / var7;
        int var10 = (var8 << 9) / var7;
        int var11 = var2 * Class60.anInt936 + var6 * Unsorted.anInt1037 >> 16;
        int var12 = var2 * Unsorted.anInt1037 - var6 * Class60.anInt936 >> 16;
        if (var11 < 1) {
            var11 = 1;
        }

        int var13 = (var5 << 9) / var11;
        int var14 = (var12 << 9) / var11;
        return (var7 >= 50 || var11 >= 50) && ((var7 <= var4 || var11 <= var4) && ((var9 >= Class139.screenLowerX || var13 >= Class139.screenLowerX) && ((var9 <= Class145.screenUpperX || var13 <= Class145.screenUpperX) && ((var10 >= Class1.screenUpperY || var14 >= Class1.screenUpperY) && (var10 <= AtmosphereParser.screenLowerY || var14 <= AtmosphereParser.screenLowerY)))));
    }

    final void method1485(int var1, boolean var2, int var4, int var5, int var6) {
        try {
            var6 -= this.anInt1309;

            var4 -= this.anInt1306;
            if (0 == var5) {
                if (var1 == 0) {
                    this.method1501((byte) 114, var4, var6, 128);
                    this.method1501((byte) 122, var4, var6 + -1, 8);
                }

                if (var1 == 1) {
                    this.method1501((byte) 77, var4, var6, 2);
                    this.method1501((byte) 105, 1 + var4, var6, 32);
                }

                if (var1 == 2) {
                    this.method1501((byte) 75, var4, var6, 8);
                    this.method1501((byte) 38, var4, var6 - -1, 128);
                }

                if (3 == var1) {
                    this.method1501((byte) 110, var4, var6, 32);
                    this.method1501((byte) 26, var4 - 1, var6, 2);
                }
            }

            if (var5 == 1 || var5 == 3) {
                if (var1 == 0) {
                    this.method1501((byte) 99, var4, var6, 1);
                    this.method1501((byte) 23, 1 + var4, -1 + var6, 16);
                }

                if (var1 == 1) {
                    this.method1501((byte) 52, var4, var6, 4);
                    this.method1501((byte) 60, 1 + var4, 1 + var6, 64);
                }

                if (var1 == 2) {
                    this.method1501((byte) 126, var4, var6, 16);
                    this.method1501((byte) 103, var4 - 1, var6 - -1, 1);
                }

                if (var1 == 3) {
                    this.method1501((byte) 125, var4, var6, 64);
                    this.method1501((byte) 64, var4 - 1, -1 + var6, 4);
                }
            }

            if (var5 == 2) {
                if (var1 == 0) {
                    this.method1501((byte) 110, var4, var6, 130);
                    this.method1501((byte) 78, var4, -1 + var6, 8);
                    this.method1501((byte) 45, var4 + 1, var6, 32);
                }

                if (var1 == 1) {
                    this.method1501((byte) 93, var4, var6, 10);
                    this.method1501((byte) 72, var4 - -1, var6, 32);
                    this.method1501((byte) 23, var4, var6 + 1, 128);
                }

                if (2 == var1) {
                    this.method1501((byte) 33, var4, var6, 40);
                    this.method1501((byte) 102, var4, var6 + 1, 128);
                    this.method1501((byte) 102, var4 - 1, var6, 2);
                }

                if (var1 == 3) {
                    this.method1501((byte) 24, var4, var6, 160);
                    this.method1501((byte) 122, -1 + var4, var6, 2);
                    this.method1501((byte) 77, var4, var6 + -1, 8);
                }
            }

            if (var2) {
                if (0 == var5) {
                    if (var1 == 0) {
                        this.method1501((byte) 24, var4, var6, 65536);
                        this.method1501((byte) 115, var4, var6 - 1, 4096);
                    }

                    if (var1 == 1) {
                        this.method1501((byte) 120, var4, var6, 1024);
                        this.method1501((byte) 110, var4 - -1, var6, 16384);
                    }

                    if (var1 == 2) {
                        this.method1501((byte) 101, var4, var6, 4096);
                        this.method1501((byte) 38, var4, 1 + var6, 65536);
                    }

                    if (var1 == 3) {
                        this.method1501((byte) 31, var4, var6, 16384);
                        this.method1501((byte) 65, var4 + -1, var6, 1024);
                    }
                }

                if (1 == var5 || var5 == 3) {
                    if (var1 == 0) {
                        this.method1501((byte) 47, var4, var6, 512);
                        this.method1501((byte) 66, 1 + var4, var6 + -1, 8192);
                    }

                    if (var1 == 1) {
                        this.method1501((byte) 40, var4, var6, 2048);
                        this.method1501((byte) 88, 1 + var4, var6 - -1, 32768);
                    }

                    if (var1 == 2) {
                        this.method1501((byte) 96, var4, var6, 8192);
                        this.method1501((byte) 41, var4 - 1, 1 + var6, 512);
                    }

                    if (var1 == 3) {
                        this.method1501((byte) 90, var4, var6, 32768);
                        this.method1501((byte) 47, var4 + -1, var6 + -1, 2048);
                    }
                }

                if (var5 == 2) {
                    if (var1 == 0) {
                        this.method1501((byte) 51, var4, var6, 66560);
                        this.method1501((byte) 82, var4, var6 + -1, 4096);
                        this.method1501((byte) 68, var4 + 1, var6, 16384);
                    }

                    if (var1 == 1) {
                        this.method1501((byte) 112, var4, var6, 5120);
                        this.method1501((byte) 89, 1 + var4, var6, 16384);
                        this.method1501((byte) 48, var4, 1 + var6, 65536);
                    }

                    if (var1 == 2) {
                        this.method1501((byte) 126, var4, var6, 20480);
                        this.method1501((byte) 25, var4, var6 - -1, 65536);
                        this.method1501((byte) 46, -1 + var4, var6, 1024);
                    }

                    if (var1 == 3) {
                        this.method1501((byte) 39, var4, var6, 81920);
                        this.method1501((byte) 113, var4 - 1, var6, 1024);
                        this.method1501((byte) 86, var4, var6 + -1, 4096);
                    }
                }
            }

        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "mj.L(" + var1 + ',' + var2 + ',' + -104 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    final void method1486(int var1, int var3, boolean var4, int var5, int var6) {
        try {
            var6 -= this.anInt1309;
            var5 -= this.anInt1306;
            if (var3 == 0) {
                if (0 == var1) {
                    this.method1490(128, var6, var5);
                    this.method1490(8, -1 + var6, var5);
                }

                if (var1 == 1) {
                    this.method1490(2, var6, var5);
                    this.method1490(32, var6, 1 + var5);
                }

                if (var1 == 2) {
                    this.method1490(8, var6, var5);
                    this.method1490(128, 1 + var6, var5);
                }

                if (var1 == 3) {
                    this.method1490(32, var6, var5);
                    this.method1490(2, var6, -1 + var5);
                }
            }

            if (var3 == 1 || var3 == 3) {
                if (var1 == 0) {
                    this.method1490(1, var6, var5);
                    this.method1490(16, var6 + -1, 1 + var5);
                }

                if (var1 == 1) {
                    this.method1490(4, var6, var5);
                    this.method1490(64, var6 - -1, var5 + 1);
                }

                if (var1 == 2) {
                    this.method1490(16, var6, var5);
                    this.method1490(1, 1 + var6, -1 + var5);
                }

                if (var1 == 3) {
                    this.method1490(64, var6, var5);
                    this.method1490(4, -1 + var6, var5 + -1);
                }
            }

            if (var3 == 2) {
                if (var1 == 0) {
                    this.method1490(130, var6, var5);
                    this.method1490(8, -1 + var6, var5);
                    this.method1490(32, var6, var5 - -1);
                }

                if (var1 == 1) {
                    this.method1490(10, var6, var5);
                    this.method1490(32, var6, 1 + var5);
                    this.method1490(128, var6 - -1, var5);
                }

                if (var1 == 2) {
                    this.method1490(40, var6, var5);
                    this.method1490(128, 1 + var6, var5);
                    this.method1490(2, var6, var5 + -1);
                }

                if (var1 == 3) {
                    this.method1490(160, var6, var5);
                    this.method1490(2, var6, -1 + var5);
                    this.method1490(8, -1 + var6, var5);
                }
            }

            if (var4) {
                if (var3 == 0) {
                    if (0 == var1) {
                        this.method1490(65536, var6, var5);
                        this.method1490(4096, var6 - 1, var5);
                    }

                    if (var1 == 1) {
                        this.method1490(1024, var6, var5);
                        this.method1490(16384, var6, var5 + 1);
                    }

                    if (var1 == 2) {
                        this.method1490(4096, var6, var5);
                        this.method1490(65536, 1 + var6, var5);
                    }

                    if (var1 == 3) {
                        this.method1490(16384, var6, var5);
                        this.method1490(1024, var6, var5 - 1);
                    }
                }

                if (var3 == 1 || var3 == 3) {
                    if (0 == var1) {
                        this.method1490(512, var6, var5);
                        this.method1490(8192, -1 + var6, 1 + var5);
                    }

                    if (var1 == 1) {
                        this.method1490(2048, var6, var5);
                        this.method1490(32768, 1 + var6, var5 + 1);
                    }

                    if (var1 == 2) {
                        this.method1490(8192, var6, var5);
                        this.method1490(512, var6 - -1, var5 + -1);
                    }

                    if (3 == var1) {
                        this.method1490(32768, var6, var5);
                        this.method1490(2048, -1 + var6, var5 - 1);
                    }
                }

                if (2 == var3) {
                    if (var1 == 0) {
                        this.method1490(66560, var6, var5);
                        this.method1490(4096, -1 + var6, var5);
                        this.method1490(16384, var6, 1 + var5);
                    }

                    if (var1 == 1) {
                        this.method1490(5120, var6, var5);
                        this.method1490(16384, var6, var5 - -1);
                        this.method1490(65536, 1 + var6, var5);
                    }

                    if (var1 == 2) {
                        this.method1490(20480, var6, var5);
                        this.method1490(65536, var6 + 1, var5);
                        this.method1490(1024, var6, -1 + var5);
                    }

                    if (var1 == 3) {
                        this.method1490(81920, var6, var5);
                        this.method1490(1024, var6, var5 - 1);
                        this.method1490(4096, -1 + var6, var5);
                    }
                }
            }

        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "mj.N(" + var1 + ',' + 2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    private boolean method1487(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        try {
            if (var2 + var3 > var8 && var3 < var1 + var8) {

                return var5 < var4 + var6 && var7 + var5 > var6;
            } else {
                return false;
            }
        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "mj.M(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + (byte) -125 + ')');
        }
    }

    final boolean method1488(int var1, int var2, int var4, int var5, int var6, int var7, int var8) {
        try {
            if (1 != var7) {
                if (var2 <= var5 && var2 + var7 + -1 >= var5 && var7 + var1 + -1 >= var1) {
                    return true;
                }
            } else if (var2 == var5 && var4 == var1) {
                return true;
            }

            if (true) {
                var4 -= this.anInt1306;
                var5 -= this.anInt1309;
                var1 -= this.anInt1306;
                var2 -= this.anInt1309;
                if (1 == var7) {
                    if (0 == var6) {
                        if (var8 == 0) {
                            if (var5 - 1 == var2 && var4 == var1) {
                                return true;
                            }

                            if (var2 == var5 && var1 - -1 == var4 && (this.anIntArrayArray1304[var2][var4] & 19661088) == 0) {
                                return true;
                            }

                            if (var2 == var5 && -1 + var1 == var4 && 0 == (this.anIntArrayArray1304[var2][var4] & 19661058)) {
                                return true;
                            }
                        } else if (1 == var8) {
                            if (var5 == var2 && var4 == var1 - -1) {
                                return true;
                            }

                            if (var5 + -1 == var2 && var4 == var1 && 0 == (this.anIntArrayArray1304[var2][var4] & 19661064)) {
                                return true;
                            }

                            if (var2 == 1 + var5 && var4 == var1 && (this.anIntArrayArray1304[var2][var4] & 19661184) == 0) {
                                return true;
                            }
                        } else if (var8 == 2) {
                            if (1 + var5 == var2 && var1 == var4) {
                                return true;
                            }

                            if (var2 == var5 && var4 == 1 + var1 && (this.anIntArrayArray1304[var2][var4] & 19661088) == 0) {
                                return true;
                            }

                            if (var5 == var2 && var4 == var1 - 1 && (this.anIntArrayArray1304[var2][var4] & 19661058) == 0) {
                                return true;
                            }
                        } else if (var8 == 3) {
                            if (var2 == var5 && var4 == var1 + -1) {
                                return true;
                            }

                            if (-1 + var5 == var2 && var4 == var1 && (this.anIntArrayArray1304[var2][var4] & 19661064) == 0) {
                                return true;
                            }

                            if (var2 == var5 - -1 && var4 == var1 && (this.anIntArrayArray1304[var2][var4] & 19661184) == 0) {
                                return true;
                            }
                        }
                    }

                    if (2 == var6) {
                        if (var8 == 0) {
                            if (var2 == var5 - 1 && var1 == var4) {
                                return true;
                            }

                            if (var5 == var2 && var4 == 1 + var1) {
                                return true;
                            }

                            if (var2 == 1 + var5 && var1 == var4 && (19661184 & this.anIntArrayArray1304[var2][var4]) == 0) {
                                return true;
                            }

                            if (var5 == var2 && var4 == var1 + -1 && (this.anIntArrayArray1304[var2][var4] & 19661058) == 0) {
                                return true;
                            }
                        } else if (var8 == 1) {
                            if (var2 == var5 + -1 && var1 == var4 && 0 == (this.anIntArrayArray1304[var2][var4] & 19661064)) {
                                return true;
                            }

                            if (var5 == var2 && var4 == var1 + 1) {
                                return true;
                            }

                            if (var5 + 1 == var2 && var4 == var1) {
                                return true;
                            }

                            if (var2 == var5 && var4 == -1 + var1 && (19661058 & this.anIntArrayArray1304[var2][var4]) == 0) {
                                return true;
                            }
                        } else if (var8 == 2) {
                            if (var2 == -1 + var5 && var4 == var1 && (this.anIntArrayArray1304[var2][var4] & 19661064) == 0) {
                                return true;
                            }

                            if (var5 == var2 && 1 + var1 == var4 && (19661088 & this.anIntArrayArray1304[var2][var4]) == 0) {
                                return true;
                            }

                            if (var5 - -1 == var2 && var4 == var1) {
                                return true;
                            }

                            if (var5 == var2 && var1 + -1 == var4) {
                                return true;
                            }
                        } else if (var8 == 3) {
                            if (var5 - 1 == var2 && var1 == var4) {
                                return true;
                            }

                            if (var5 == var2 && var1 + 1 == var4 && 0 == (19661088 & this.anIntArrayArray1304[var2][var4])) {
                                return true;
                            }

                            if (var5 - -1 == var2 && var1 == var4 && (19661184 & this.anIntArrayArray1304[var2][var4]) == 0) {
                                return true;
                            }

                            if (var5 == var2 && -1 + var1 == var4) {
                                return true;
                            }
                        }
                    }

                    if (9 == var6) {
                        if (var5 == var2 && var4 == 1 + var1 && 0 == (this.anIntArrayArray1304[var2][var4] & 32)) {
                            return true;
                        }

                        if (var2 == var5 && -1 + var1 == var4 && (this.anIntArrayArray1304[var2][var4] & 2) == 0) {
                            return true;
                        }

                        if (var2 == -1 + var5 && var1 == var4 && (this.anIntArrayArray1304[var2][var4] & 8) == 0) {
                            return true;
                        }

                        return var5 - -1 == var2 && var4 == var1 && (this.anIntArrayArray1304[var2][var4] & 128) == 0;
                    }
                } else {
                    int var9 = -1 + var7 + var2;
                    int var10 = -1 + var4 - -var7;
                    if (var6 == 0) {
                        if (0 == var8) {
                            if (-var7 + var5 == var2 && var1 >= var4 && var10 >= var1) {
                                return true;
                            }

                            if (var2 <= var5 && var5 <= var9 && var4 == 1 + var1 && 0 == (this.anIntArrayArray1304[var5][var4] & 19661088)) {
                                return true;
                            }

                            if (var5 >= var2 && var9 >= var5 && var4 == -var7 + var1 && (this.anIntArrayArray1304[var5][var10] & 19661058) == 0) {
                                return true;
                            }
                        } else if (1 == var8) {
                            if (var2 <= var5 && var5 <= var9 && var4 == var1 - -1) {
                                return true;
                            }

                            if (var5 - var7 == var2 && var1 >= var4 && var1 <= var10 && (this.anIntArrayArray1304[var9][var1] & 19661064) == 0) {
                                return true;
                            }

                            if (var5 + 1 == var2 && var4 <= var1 && var10 >= var1 && 0 == (this.anIntArrayArray1304[var2][var1] & 19661184)) {
                                return true;
                            }
                        } else if (2 == var8) {
                            if (1 + var5 == var2 && var4 <= var1 && var10 >= var1) {
                                return true;
                            }

                            if (var2 <= var5 && var5 <= var9 && 1 + var1 == var4 && (this.anIntArrayArray1304[var5][var4] & 19661088) == 0) {
                                return true;
                            }

                            if (var2 <= var5 && var5 <= var9 && var4 == var1 - var7 && 0 == (19661058 & this.anIntArrayArray1304[var5][var10])) {
                                return true;
                            }
                        } else if (3 == var8) {
                            if (var2 <= var5 && var9 >= var5 && var1 + -var7 == var4) {
                                return true;
                            }

                            if (var2 == var5 - var7 && var1 >= var4 && var1 <= var10 && (19661064 & this.anIntArrayArray1304[var9][var1]) == 0) {
                                return true;
                            }

                            if (var2 == 1 + var5 && var4 <= var1 && var10 >= var1 && (this.anIntArrayArray1304[var2][var1] & 19661184) == 0) {
                                return true;
                            }
                        }
                    }

                    if (var6 == 2) {
                        if (var8 == 0) {
                            if (var2 == -var7 + var5 && var4 <= var1 && var1 <= var10) {
                                return true;
                            }

                            if (var2 <= var5 && var5 <= var9 && 1 + var1 == var4) {
                                return true;
                            }

                            if (var2 == 1 + var5 && var1 >= var4 && var1 <= var10 && 0 == (this.anIntArrayArray1304[var2][var1] & 19661184)) {
                                return true;
                            }

                            if (var2 <= var5 && var9 >= var5 && -var7 + var1 == var4 && (19661058 & this.anIntArrayArray1304[var5][var10]) == 0) {
                                return true;
                            }
                        } else if (var8 == 1) {
                            if (var2 == var5 - var7 && var1 >= var4 && var10 >= var1 && (19661064 & this.anIntArrayArray1304[var9][var1]) == 0) {
                                return true;
                            }

                            if (var5 >= var2 && var5 <= var9 && var4 == 1 + var1) {
                                return true;
                            }

                            if (var5 + 1 == var2 && var1 >= var4 && var10 >= var1) {
                                return true;
                            }

                            if (var2 <= var5 && var9 >= var5 && var4 == -var7 + var1 && (this.anIntArrayArray1304[var5][var10] & 19661058) == 0) {
                                return true;
                            }
                        } else if (var8 == 2) {
                            if (-var7 + var5 == var2 && var4 <= var1 && var10 >= var1 && (this.anIntArrayArray1304[var9][var1] & 19661064) == 0) {
                                return true;
                            }

                            if (var5 >= var2 && var5 <= var9 && var4 == var1 + 1 && (this.anIntArrayArray1304[var5][var4] & 19661088) == 0) {
                                return true;
                            }

                            if (1 + var5 == var2 && var1 >= var4 && var1 <= var10) {
                                return true;
                            }

                            if (var2 <= var5 && var5 <= var9 && var4 == -var7 + var1) {
                                return true;
                            }
                        } else if (var8 == 3) {
                            if (-var7 + var5 == var2 && var1 >= var4 && var1 <= var10) {
                                return true;
                            }

                            if (var5 >= var2 && var5 <= var9 && 1 + var1 == var4 && (this.anIntArrayArray1304[var5][var4] & 19661088) == 0) {
                                return true;
                            }

                            if (var2 == 1 + var5 && var4 <= var1 && var1 <= var10 && (this.anIntArrayArray1304[var2][var1] & 19661184) == 0) {
                                return true;
                            }

                            if (var2 <= var5 && var5 <= var9 && var1 + -var7 == var4) {
                                return true;
                            }
                        }
                    }

                    if (var6 == 9) {
                        if (var5 >= var2 && var9 >= var5 && var1 - -1 == var4 && (this.anIntArrayArray1304[var5][var4] & 19661088) == 0) {
                            return true;
                        }

                        if (var2 <= var5 && var9 >= var5 && var1 - var7 == var4 && (19661058 & this.anIntArrayArray1304[var5][var10]) == 0) {
                            return true;
                        }

                        if (var2 == -var7 + var5 && var1 >= var4 && var10 >= var1 && (19661064 & this.anIntArrayArray1304[var9][var1]) == 0) {
                            return true;
                        }

                        return 1 + var5 == var2 && var4 <= var1 && var10 >= var1 && (this.anIntArrayArray1304[var2][var1] & 19661184) == 0;
                    }
                }

            }
            return false;
        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "mj.D(" + var1 + ',' + var2 + ',' + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ')');
        }
    }

    final void method1489(int var1, boolean var2, byte var3, int var4, int var5, int var6) {
        try {
            var4 -= this.anInt1306;
            var1 -= this.anInt1309;
            int var7 = 256;
            if (var2) {
                var7 += 131072;
            }

            int var8 = var1;
            if (var3 > 57) {
                for (; var1 + var5 > var8; ++var8) {
                    if (var8 >= 0 && var8 < this.anInt1300) {
                        for (int var9 = var4; var9 < var6 + var4; ++var9) {
                            if (0 <= var9 && var9 < this.anInt1303) {
                                this.method1490(var7, var8, var9);
                            }
                        }
                    }
                }

            }
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "mj.B(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ')');
        }
    }

    private void method1490(int var1, int var3, int var4) {
        try {

            this.anIntArrayArray1304[var3][var4] = TextureOperation3.bitwiseOr(this.anIntArrayArray1304[var3][var4], var1);
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "mj.J(" + var1 + ',' + (byte) -80 + ',' + var3 + ',' + var4 + ')');
        }
    }

    final boolean method1492(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
        try {
            if (var5 == 1) {
                if (var3 == var7 && var4 == var1) {
                    return true;
                }
            } else if (var3 >= var7 && var3 <= -1 + var5 + var7 && -1 + var1 + var5 >= var1) {
                return true;
            }

            if (var8 <= 78) {
                TextCore.aClass94_1301 = null;
            }

            var7 -= this.anInt1309;
            var1 -= this.anInt1306;
            var3 -= this.anInt1309;
            var4 -= this.anInt1306;
            if (var5 == 1) {
                if (6 == var2 || var2 == 7) {
                    if (var2 == 7) {
                        var6 = var6 - -2 & 3;
                    }

                    if (var6 == 0) {
                        if (1 + var3 == var7 && var1 == var4 && (this.anIntArrayArray1304[var7][var4] & 128) == 0) {
                            return true;
                        }

                        if (var7 == var3 && var4 == -1 + var1 && (2 & this.anIntArrayArray1304[var7][var4]) == 0) {
                            return true;
                        }
                    } else if (var6 == 1) {
                        if (var7 == var3 + -1 && var4 == var1 && (this.anIntArrayArray1304[var7][var4] & 8) == 0) {
                            return true;
                        }

                        if (var3 == var7 && var4 == -1 + var1 && (2 & this.anIntArrayArray1304[var7][var4]) == 0) {
                            return true;
                        }
                    } else if (var6 == 2) {
                        if (var3 - 1 == var7 && var1 == var4 && 0 == (8 & this.anIntArrayArray1304[var7][var4])) {
                            return true;
                        }

                        if (var3 == var7 && var4 == 1 + var1 && (32 & this.anIntArrayArray1304[var7][var4]) == 0) {
                            return true;
                        }
                    } else if (3 == var6) {
                        if (var3 - -1 == var7 && var4 == var1 && (128 & this.anIntArrayArray1304[var7][var4]) == 0) {
                            return true;
                        }

                        if (var7 == var3 && var1 - -1 == var4 && (32 & this.anIntArrayArray1304[var7][var4]) == 0) {
                            return true;
                        }
                    }
                }

                if (var2 == 8) {
                    if (var7 == var3 && 1 + var1 == var4 && 0 == (32 & this.anIntArrayArray1304[var7][var4])) {
                        return true;
                    }

                    if (var3 == var7 && var1 + -1 == var4 && (this.anIntArrayArray1304[var7][var4] & 2) == 0) {
                        return true;
                    }

                    if (-1 + var3 == var7 && var4 == var1 && 0 == (8 & this.anIntArrayArray1304[var7][var4])) {
                        return true;
                    }

                    return var3 - -1 == var7 && var1 == var4 && (128 & this.anIntArrayArray1304[var7][var4]) == 0;
                }
            } else {
                int var9 = var7 + var5 + -1;
                int var10 = -1 + var4 + var5;
                if (6 == var2 || var2 == 7) {
                    if (var2 == 7) {
                        var6 = 2 + var6 & 3;
                    }

                    if (var6 == 0) {
                        if (var7 == 1 + var3 && var1 >= var4 && var1 <= var10 && (this.anIntArrayArray1304[var7][var1] & 128) == 0) {
                            return true;
                        }

                        if (var7 <= var3 && var9 >= var3 && var4 == var1 + -var5 && (2 & this.anIntArrayArray1304[var3][var10]) == 0) {
                            return true;
                        }
                    } else if (var6 == 1) {
                        if (var7 == -var5 + var3 && var1 >= var4 && var1 <= var10 && (8 & this.anIntArrayArray1304[var9][var1]) == 0) {
                            return true;
                        }

                        if (var7 <= var3 && var3 <= var9 && var1 + -var5 == var4 && 0 == (this.anIntArrayArray1304[var3][var10] & 2)) {
                            return true;
                        }
                    } else if (var6 == 2) {
                        if (-var5 + var3 == var7 && var1 >= var4 && var10 >= var1 && (8 & this.anIntArrayArray1304[var9][var1]) == 0) {
                            return true;
                        }

                        if (var7 <= var3 && var3 <= var9 && var1 + 1 == var4 && 0 == (32 & this.anIntArrayArray1304[var3][var4])) {
                            return true;
                        }
                    } else if (var6 == 3) {
                        if (var7 == 1 + var3 && var4 <= var1 && var10 >= var1 && (128 & this.anIntArrayArray1304[var7][var1]) == 0) {
                            return true;
                        }

                        if (var7 <= var3 && var9 >= var3 && var4 == var1 - -1 && 0 == (this.anIntArrayArray1304[var3][var4] & 32)) {
                            return true;
                        }
                    }
                }

                if (var2 == 8) {
                    if (var7 <= var3 && var9 >= var3 && var4 == 1 + var1 && (this.anIntArrayArray1304[var3][var4] & 32) == 0) {
                        return true;
                    }

                    if (var7 <= var3 && var3 <= var9 && var4 == var1 - var5 && (this.anIntArrayArray1304[var3][var10] & 2) == 0) {
                        return true;
                    }

                    if (var7 == -var5 + var3 && var4 <= var1 && var1 <= var10 && (this.anIntArrayArray1304[var9][var1] & 8) == 0) {
                        return true;
                    }

                    return 1 + var3 == var7 && var1 >= var4 && var1 <= var10 && 0 == (this.anIntArrayArray1304[var7][var1] & 128);
                }
            }

            return false;
        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "mj.Q(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ')');
        }
    }

    private boolean method1494(int var1, int var2, int var3, int var4, int var6, int var7, int var8, int var9, int var10) {
        try {
            int var11 = var7 + var9;
            int var12 = var8 + var10;
            int var13 = var3 + var1;
            int var14 = var2 - -var6;
            int var15;
            int var16;
            if (var7 >= var1 && var7 < var13) {
                if (var12 == var2 && (4 & var4) == 0) {
                    var15 = var7;

                    for (var16 = var13 >= var11 ? var11 : var13; var16 > var15; ++var15) {
                        if (0 == (this.anIntArrayArray1304[-this.anInt1309 + var15][var12 - (this.anInt1306 + 1)] & 2)) {
                            return true;
                        }
                    }
                } else if (var8 == var14 && (1 & var4) == 0) {
                    var15 = var7;

                    for (var16 = var13 < var11 ? var13 : var11; var15 < var16; ++var15) {
                        if ((32 & this.anIntArrayArray1304[var15 - this.anInt1309][var8 + -this.anInt1306]) == 0) {
                            return true;
                        }
                    }
                }
            } else if (var11 > var1 && var13 >= var11) {
                if (var12 == var2 && 0 == (4 & var4)) {
                    for (var15 = var1; var15 < var11; ++var15) {
                        if ((2 & this.anIntArrayArray1304[-this.anInt1309 + var15][var12 - (this.anInt1306 + 1)]) == 0) {
                            return true;
                        }
                    }
                } else if (var14 == var8 && (1 & var4) == 0) {
                    for (var15 = var1; var11 > var15; ++var15) {
                        if ((32 & this.anIntArrayArray1304[-this.anInt1309 + var15][-this.anInt1306 + var8]) == 0) {
                            return true;
                        }
                    }
                }
            } else if (var8 >= var2 && var14 > var8) {
                if (var11 == var1 && 0 == (8 & var4)) {
                    var15 = var8;

                    for (var16 = var12 <= var14 ? var12 : var14; var16 > var15; ++var15) {
                        if ((8 & this.anIntArrayArray1304[-1 + -this.anInt1309 + var11][var15 - this.anInt1306]) == 0) {
                            return true;
                        }
                    }
                } else if (var13 == var7 && (var4 & 2) == 0) {
                    var15 = var8;

                    for (var16 = var14 < var12 ? var14 : var12; var16 > var15; ++var15) {
                        if ((this.anIntArrayArray1304[-this.anInt1309 + var7][-this.anInt1306 + var15] & 128) == 0) {
                            return true;
                        }
                    }
                }
            } else if (var2 < var12 && var12 <= var14) {
                if (var1 == var11 && (var4 & 8) == 0) {
                    for (var15 = var2; var12 > var15; ++var15) {
                        if ((this.anIntArrayArray1304[-this.anInt1309 + var11 + -1][-this.anInt1306 + var15] & 8) == 0) {
                            return true;
                        }
                    }
                } else if (var7 == var13 && (var4 & 2) == 0) {
                    for (var15 = var2; var12 > var15; ++var15) {
                        if ((128 & this.anIntArrayArray1304[var7 + -this.anInt1309][-this.anInt1306 + var15]) == 0) {
                            return true;
                        }
                    }
                }
            }

            return false;
        } catch (RuntimeException var17) {
            throw ClientErrorException.clientError(var17, "mj.O(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + true + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var10 + ')');
        }
    }

    final void method1496() {
        try {
            for (int var2 = 0; var2 < this.anInt1300; ++var2) {
                for (int var3 = 0; var3 < this.anInt1303; ++var3) {
                    if (var2 != 0 && 0 != var3 && var2 < this.anInt1300 - 5 && this.anInt1303 - 5 > var3) {
                        this.anIntArrayArray1304[var2][var3] = 16777216;
                    } else {
                        this.anIntArrayArray1304[var2][var3] = 16777215;
                    }
                }
            }

        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "mj.P(" + 0 + ')');
        }
    }

    final void method1497(int var1, int var3) {
        try {
            var1 -= this.anInt1306;
            var3 -= this.anInt1309;
            this.anIntArrayArray1304[var3][var1] = TextureOperation3.bitwiseOr(this.anIntArrayArray1304[var3][var1], 2097152);
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "mj.E(" + var1 + ',' + 7605 + ',' + var3 + ')');
        }
    }

    final boolean method1498(int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
        try {
            if (var5 > 1) {
                return this.method1487(var5, var6, var2, var9, var3, var8, var5, var4) || this.method1494(var2, var8, var6, var7, var9, var4, var3, var5, var5);
            } else {
                int var10 = -1 + var6 + var2;
                int var11 = var8 + (var9 - 1);
                if (var4 >= var2 && var10 >= var4 && var8 <= var3 && var11 >= var3) {
                    return true;
                } else {
                    if (false) {
                        this.method1501((byte) -85, 4, 106, -39);
                    }

                    return var2 + -1 == var4 && var8 <= var3 && var3 <= var11 && (this.anIntArrayArray1304[-this.anInt1309 + var4][var3 - this.anInt1306] & 8) == 0 && (var7 & 8) == 0 || (var4 == 1 + var10 && var3 >= var8 && var3 <= var11 && 0 == (128 & this.anIntArrayArray1304[var4 - this.anInt1309][-this.anInt1306 + var3]) && (var7 & 2) == 0 || (var8 - 1 == var3 && var4 >= var2 && var10 >= var4 && (2 & this.anIntArrayArray1304[-this.anInt1309 + var4][-this.anInt1306 + var3]) == 0 && (4 & var7) == 0 || 1 + var11 == var3 && var4 >= var2 && var4 <= var10 && (32 & this.anIntArrayArray1304[var4 - this.anInt1309][var3 + -this.anInt1306]) == 0 && (var7 & 1) == 0));
                }
            }
        } catch (RuntimeException var12) {
            throw ClientErrorException.clientError(var12, "mj.S(" + true + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ')');
        }
    }

    final void method1499(int var1, int var3) {
        try {
            var3 -= this.anInt1309;
            var1 -= this.anInt1306;
            this.anIntArrayArray1304[var3][var1] = Unsorted.bitwiseAnd(this.anIntArrayArray1304[var3][var1], -262145);

        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "mj.I(" + var1 + ',' + (byte) -73 + ',' + var3 + ')');
        }
    }

    final boolean method1500(int var2, int var3, int var4, int var5) {
        try {
            if (var5 == var4 && var3 == var2) {
                return true;
            } else {
                var3 -= this.anInt1306;

                var4 -= this.anInt1309;
                if (0 <= var4 && this.anInt1300 > var4 && var3 >= 0 && this.anInt1303 > var3) {
                    var2 -= this.anInt1306;
                    var5 -= this.anInt1309;
                    int var6;
                    if (var5 < var4) {
                        var6 = var4 - var5;
                    } else {
                        var6 = -var4 + var5;
                    }

                    int var7;
                    if (var2 >= var3) {
                        var7 = -var3 + var2;
                    } else {
                        var7 = var3 - var2;
                    }

                    int var8;
                    int var9;
                    if (var7 >= var6) {
                        var9 = 32768;
                        var8 = 65536 * var6 / var7;

                        while (var3 != var2) {
                            if (var2 < var3) {
                                if (0 != (this.anIntArrayArray1304[var5][var2] & 19661058)) {
                                    return false;
                                }

                                ++var2;
                            } else {
                                if (0 != (19661088 & this.anIntArrayArray1304[var5][var2])) {
                                    return false;
                                }

                                --var2;
                            }

                            var9 += var8;
                            if (var9 >= 65536) {
                                var9 -= 65536;
                                if (var5 >= var4) {
                                    if (var5 > var4) {
                                        if ((19661184 & this.anIntArrayArray1304[var5][var2]) != 0) {
                                            return false;
                                        }

                                        --var5;
                                    }
                                } else {
                                    if ((19661064 & this.anIntArrayArray1304[var5][var2]) != 0) {
                                        return false;
                                    }

                                    ++var5;
                                }
                            }
                        }
                    } else {
                        var8 = 65536 * var7 / var6;
                        var9 = 32768;

                        while (var5 != var4) {
                            if (var4 <= var5) {
                                if ((this.anIntArrayArray1304[var5][var2] & 19661184) != 0) {
                                    return false;
                                }

                                --var5;
                            } else {
                                if (0 != (this.anIntArrayArray1304[var5][var2] & 19661064)) {
                                    return false;
                                }

                                ++var5;
                            }

                            var9 += var8;
                            if (var9 >= 65536) {
                                var9 -= 65536;
                                if (var3 > var2) {
                                    if (0 != (this.anIntArrayArray1304[var5][var2] & 19661058)) {
                                        return false;
                                    }

                                    ++var2;
                                } else if (var3 < var2) {
                                    if (0 != (19661088 & this.anIntArrayArray1304[var5][var2])) {
                                        return false;
                                    }

                                    --var2;
                                }
                            }
                        }
                    }

                    return 0 == (19136768 & this.anIntArrayArray1304[var4][var3]);
                } else {
                    return false;
                }
            }
        } catch (RuntimeException var10) {
            throw ClientErrorException.clientError(var10, "mj.G(" + -2 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ')');
        }
    }

    private void method1501(byte var1, int var2, int var3, int var4) {
        try {
            if (var1 >= 18) {
                this.anIntArrayArray1304[var3][var2] = Unsorted.bitwiseAnd(this.anIntArrayArray1304[var3][var2], ~var4);
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "mj.H(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
        }
    }

    final void method1502(int var2, int var3, boolean var4, int var5, int var6, int var7) {
        try {
            var2 -= this.anInt1309;
            var7 -= this.anInt1306;
            int var8 = 256;

            if (var4) {
                var8 += 131072;
            }

            int var9;
            if (1 == var5 || var5 == 3) {
                var9 = var3;
                var3 = var6;
                var6 = var9;
            }

            for (var9 = var2; var9 < var2 - -var3; ++var9) {
                if (0 <= var9 && var9 < this.anInt1300) {
                    for (int var10 = var7; var6 + var7 > var10; ++var10) {
                        if (var10 >= 0 && var10 < this.anInt1303) {
                            this.method1501((byte) 32, var10, var9, var8);
                        }
                    }
                }
            }

        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "mj.A(" + 20851 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ')');
        }
    }

    final void method1503(int var1, int var2) {
        try {
            var2 -= this.anInt1306;
            var1 -= this.anInt1309;
            this.anIntArrayArray1304[var1][var2] = TextureOperation3.bitwiseOr(this.anIntArrayArray1304[var1][var2], 262144);
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "mj.C(" + var1 + ',' + var2 + ',' + -5 + ')');
        }
    }

}
