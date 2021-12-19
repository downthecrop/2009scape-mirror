package org.runite.client;

import java.util.Objects;
import java.util.Random;

public final class Class24 {

    public static int anInt469 = 0;
    static Random aRandom3088 = new Random();
    static int anInt467 = 0;
    static int anInt472 = 0;
    static CacheIndex modelsIndex_152;
    private final int[] anIntArray475 = new int[]{-1, -1, -1, -1, -1};
    int anInt466 = -1;
    boolean aBoolean476 = false;
    private short[] aShortArray460;
    private short[] aShortArray464;
    private short[] aShortArray470;
    private short[] aShortArray471;
    private int[] anIntArray474;

    static boolean isValidObjectMapping(byte var0, int var1, int var2, byte[] var3) {
        try {
            boolean var5 = true;
            int var7 = -1;
            DataBuffer buffer = new DataBuffer(var3);

            if (buffer.buffer.length == 0) {
//    		 System.out.println("No object mapping found!");
                return true;
            }
            while (true) {
                if (buffer.index == buffer.buffer.length) {
                    return true;
                }
                int var8 = buffer.method773();
                if (0 == var8) {
                    return var5;
                }

                int var9 = 0;
                var7 += var8;
                boolean var10 = false;

                while (true) {
                    int var11;
                    if (var10) {
                        var11 = buffer.getSmart();
                        if (var11 == 0) {
                            break;
                        }

                        buffer.readUnsignedByte();
                    } else {
                        if (buffer.index == buffer.buffer.length) {
                            break;
                        }
                        var11 = buffer.getSmart();
                        if (0 == var11) {
                            break;
                        }

                        var9 += var11 + -1;
                        int var12 = 63 & var9;
                        int var13 = (4088 & var9) >> 6;
                        int var16 = var2 + var12;
                        int var15 = var1 + var13;
                        int var14 = buffer.readUnsignedByte() >> 2;
                        if (var15 > 0 && var16 > 0 && 103 > var15 && 103 > var16) {
                            ObjectDefinition var17 = ObjectDefinition.getObjectDefinition(var7);
                            if (var14 != 22 || KeyboardListener.aBoolean1905 || 0 != var17.SecondInt || var17.ClipType == 1 || var17.aBoolean1483) {
                                var10 = true;
                                if (!var17.hasModels()) {
                                    var5 = false;
                                    ++Class162.anInt2038;
                                }
                            }
                        }
                    }
                }
            }
        } catch (RuntimeException var18) {
            throw ClientErrorException.clientError(var18, "dm.A(" + var0 + ',' + var1 + ',' + var2 + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

    static void method949(int var0, int var2, int var3, int var4) {
        try {
            int var6 = 0;
            TextureOperation18.method282(Class38.anIntArrayArray663[var4], -var2 + var0, 100, var0 - -var2, var3);
            int var8 = -var2;
            int var7 = var2;
            int var9 = -1;

            while (var7 > var6) {
                ++var6;
                var9 += 2;
                var8 += var9;
                if (var8 >= 0) {
                    --var7;
                    var8 -= var7 << 1;
                    int[] var10 = Class38.anIntArrayArray663[var4 - -var7];
                    int[] var11 = Class38.anIntArrayArray663[var4 - var7];
                    int var12 = var0 - -var6;
                    int var13 = -var6 + var0;
                    TextureOperation18.method282(var10, var13, 115, var12, var3);
                    TextureOperation18.method282(var11, var13, 114, var12, var3);
                }

                int var16 = var7 + var0;
                int var15 = -var7 + var0;
                int[] var17 = Class38.anIntArrayArray663[var4 - -var6];
                int[] var18 = Class38.anIntArrayArray663[-var6 + var4];
                TextureOperation18.method282(var17, var15, -61, var16, var3);
                TextureOperation18.method282(var18, var15, -93, var16, var3);
            }

        } catch (RuntimeException var14) {
            throw ClientErrorException.clientError(var14, "dm.I(" + var0 + ',' + (byte) 118 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
        }
    }

    static void method950(RSInterface var0, int var1, int var2, int var3) {
        try {
            if (2 <= Unsorted.menuOptionCount || Class164_Sub1.anInt3012 != 0 || GameObject.aBoolean1837) {
                RSString var4 = method531();
                if (var0 == null) {
                    int var5 = FontType.bold.method683(var4, 4 + var3, var2 - -15, aRandom3088, Class38_Sub1.anInt2618);
                    Class21.method1340(4 + var3, FontType.bold.method682(var4) + var5, var2, 15);
                } else {
                    Font var7 = var0.method868(Sprites.nameIconsSpriteArray);
                    if (null == var7) {
                        var7 = FontType.bold;
                    }

                    var7.method702(var4, var3, var2, var0.width, var0.height, var0.anInt218, var0.anInt287, var0.anInt194, var0.anInt225, aRandom3088, Class38_Sub1.anInt2618, Player.anIntArray3951);
                    Class21.method1340(Player.anIntArray3951[0], Player.anIntArray3951[2], Player.anIntArray3951[1], Player.anIntArray3951[3]);
                }

            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "dm.D(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ',' + var2 + ',' + var3 + ')');
        }
    }

    static void method951() {
        try {
            Class3_Sub28_Sub18.aBoolean3769 = false;
            TextureOperation25.anInt3413 = 0;
            Client.messageToDisplay = -3;
            Class50.anInt820 = 0;
            LoginHandler.loginStage = 1;
            Class166.anInt2079 = 0;
            Class3_Sub26.anInt2561 = -1;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "dm.G(" + 0 + ')');
        }
    }

    static RSString method531() {
        RSString var1;
        if (Class164_Sub1.anInt3012 == 1 && Unsorted.menuOptionCount < 2) {
            var1 = RSString.stringCombiner(new RSString[]{TextCore.HasUse, TextCore.Spacer, RenderAnimationDefinition.aString_378, TextCore.aString_1724});
        } else if (GameObject.aBoolean1837 && 2 > Unsorted.menuOptionCount) {
            var1 = RSString.stringCombiner(new RSString[]{Class3_Sub28_Sub9.aString_3621, TextCore.Spacer, TextCore.aString_676, TextCore.aString_1724});

        } else if (ClientCommands.shiftClickEnabled && ObjectDefinition.aBooleanArray1490[81] && Unsorted.menuOptionCount > 2 && !ObjectDefinition.aBooleanArray1490[82]) {
            for (Class3_Sub28_Sub1.counter = 2; Class3_Sub28_Sub1.counter < Unsorted.menuOptionCount; Class3_Sub28_Sub1.counter++) {
                RSString option = (Unsorted.method802(Unsorted.menuOptionCount - Class3_Sub28_Sub1.counter));
                if (option.toString().contains("Drop") || option.toString().contains("Release")) {
                    ClientCommands.canDrop = true;
                    Class3_Sub28_Sub1.dropAction = Class3_Sub28_Sub1.counter;
                    break;
                } else {
                    ClientCommands.canDrop = false;
                }
            }
            if (ClientCommands.canDrop) {
                var1 = Unsorted.method802(Unsorted.menuOptionCount - Class3_Sub28_Sub1.dropAction);
            } else {
                var1 = Unsorted.method802(Unsorted.menuOptionCount - 1);
            }
        } else {
            var1 = Unsorted.method802(Unsorted.menuOptionCount - 1);
        }

        if (Unsorted.menuOptionCount > 2) {
            var1 = RSString.stringCombiner(new RSString[]{var1, Class1.aString_58, RSString.stringAnimator(Unsorted.menuOptionCount - 2), TextCore.HasMoreOptions});
        }
        return var1;
    }

    final Model_Sub1 method941() {
        try {
            int var3 = 0;
            Model_Sub1[] var2 = new Model_Sub1[5];

            for (int var4 = 0; var4 < 5; ++var4) {
                if (this.anIntArray475[var4] != -1) {
                    var2[var3++] = Model_Sub1.method2015(modelsIndex_152, this.anIntArray475[var4]);
                }
            }

            Model_Sub1 var7 = new Model_Sub1(var2, var3);
            int var5;
            if (this.aShortArray464 != null) {
                for (var5 = 0; this.aShortArray464.length > var5; ++var5) {
                    var7.method2016(this.aShortArray464[var5], this.aShortArray460[var5]);
                }
            }

            if (null != this.aShortArray471) {
                for (var5 = 0; var5 < this.aShortArray471.length; ++var5) {
                    var7.method1998(this.aShortArray471[var5], this.aShortArray470[var5]);
                }
            }

            return var7;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "dm.F(" + true + ')');
        }
    }

    final boolean method942() {
        try {
            if (null == this.anIntArray474) {
                return true;
            } else {
                boolean var2 = true;

                for (int var3 = 0; this.anIntArray474.length > var3; ++var3) {
                    if (!modelsIndex_152.method2129((byte) -90, 0, this.anIntArray474[var3])) {
                        var2 = false;
                    }
                }

                return var2;
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "dm.J(" + 101 + ')');
        }
    }

    private void method946(DataBuffer var2, int var3) {
        try {

            if (var3 == 1) {
                this.anInt466 = var2.readUnsignedByte();
            } else {
                int var4;
                int var5;
                if (var3 == 2) {
                    var4 = var2.readUnsignedByte();
                    this.anIntArray474 = new int[var4];

                    for (var5 = 0; var4 > var5; ++var5) {
                        this.anIntArray474[var5] = var2.readUnsignedShort();
                    }
                } else if (var3 == 3) {
                    this.aBoolean476 = true;
                } else if (var3 == 40) {
                    var4 = var2.readUnsignedByte();
                    this.aShortArray460 = new short[var4];
                    this.aShortArray464 = new short[var4];

                    for (var5 = 0; var5 < var4; ++var5) {
                        this.aShortArray464[var5] = (short) var2.readUnsignedShort();
                        this.aShortArray460[var5] = (short) var2.readUnsignedShort();
                    }
                } else if (var3 == 41) {
                    var4 = var2.readUnsignedByte();
                    this.aShortArray471 = new short[var4];
                    this.aShortArray470 = new short[var4];

                    for (var5 = 0; var5 < var4; ++var5) {
                        this.aShortArray471[var5] = (short) var2.readUnsignedShort();
                        this.aShortArray470[var5] = (short) var2.readUnsignedShort();
                    }
                } else if (var3 >= 60 && var3 < 70) {
                    this.anIntArray475[-60 + var3] = var2.readUnsignedShort();
                }
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "dm.K(" + (byte) -84 + ',' + (var2 != null ? "{...}" : "null") + ',' + var3 + ')');
        }
    }

    final Model_Sub1 method947() {
        try {
            if (this.anIntArray474 == null) {
                return null;
            } else {
                Model_Sub1[] var2 = new Model_Sub1[this.anIntArray474.length];

                for (int var3 = 0; this.anIntArray474.length > var3; ++var3) {
                    var2[var3] = Model_Sub1.method2015(modelsIndex_152, this.anIntArray474[var3]);
                }
                Model_Sub1 var7;
                if (var2.length == 1) {
                    var7 = var2[0];
                } else {
                    var7 = new Model_Sub1(var2, var2.length);
                }

                int var5;
                if (null != this.aShortArray464) {
                    for (var5 = 0; var5 < this.aShortArray464.length; ++var5) {
                        Objects.requireNonNull(var7).method2016(this.aShortArray464[var5], this.aShortArray460[var5]);
                    }
                }

                if (this.aShortArray471 != null) {
                    for (var5 = 0; this.aShortArray471.length > var5; ++var5) {
                        Objects.requireNonNull(var7).method1998(this.aShortArray471[var5], this.aShortArray470[var5]);
                    }
                }

                return var7;
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "dm.H(" + (byte) -26 + ')');
        }
    }

    final boolean method948() {
        try {
            boolean var2 = true;

            for (int var3 = 0; var3 < 5; ++var3) {
                if (-1 != this.anIntArray475[var3] && !modelsIndex_152.method2129((byte) 95, 0, this.anIntArray475[var3])) {
                    var2 = false;
                }
            }

            return !var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "dm.B(" + 18991 + ')');
        }
    }

    final void method952(DataBuffer var2) {
        try {
            while (true) {
                int var3 = var2.readUnsignedByte();
                if (0 == var3) {
                    return;
                }

                this.method946(var2, var3);
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "dm.L(" + -31957 + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

}
