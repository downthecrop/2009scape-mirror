package org.runite.client;

import java.util.Objects;

public final class Player extends Class140_Sub4 {

    static int[] anIntArray3951 = new int[4];
    static int rights = 0;
    static byte aByte3953;
    static int[] anIntArray3954 = new int[100];
    static int[] anIntArray3959 = new int[2];
    int anInt3952 = -1;
    int headIcon = -1;
    public int teamId = 0;
    int COMBAT_LEVEL = 0;
    Class52 class52;
    int anInt3963 = -1;
    int combatLevel = 0;
    int anInt3966 = -1;
    public RSString displayName;
    boolean aBoolean3968 = false;
    int anInt3969 = 0;
    int anInt3970 = -1;
    int skullIcon = -1;
    int anInt3973 = -1;
    int anInt3974 = 0;
    private int anInt3958 = 0;

    static RSString combatLevelColor(int otherPlayer, byte levelByte, int yourPlayer) {
        try {
            int playerLevelDiff = -otherPlayer + yourPlayer;
            if (levelByte > -52)
                return null;
            if (playerLevelDiff < -9)
                return ColorCore.LvlDiffN9;//Solid Red
            if (playerLevelDiff < -6)
                return ColorCore.LvlDiffN6;//Dark Orange
            if (playerLevelDiff < -3)
                return ColorCore.LvlDiffN3;//Orange
            if (playerLevelDiff < 0)
                return ColorCore.LvlDiffN0;//Yellow-Orange
            if (playerLevelDiff > 9)
                return ColorCore.LvlDiffP9;//Bright Green
            if (playerLevelDiff > 6)
                return ColorCore.LvlDiffP6;//Green
            if (playerLevelDiff > 3)
                return ColorCore.LvlDiffP3;//Yellow-Green
            if (playerLevelDiff > 0)
                return ColorCore.LvlDiffP0;//Yellow

            return ColorCore.LvlDiffDefault;//Yellow

        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "jj.E(" + otherPlayer + ',' + levelByte + ',' + yourPlayer + ')');
        }
    }

    /**
     * Gets the properly colored string for the combat level difference.
     *
     * @param otherPlayer Their combat level
     * @param yourPlayer  Your combat level
     * @returns the RSString color
     */

    static RSString getCombatLevelDifferenceColor(int otherPlayer, int yourPlayer) {
        int playerLevelDiff = -otherPlayer + yourPlayer;
        if (playerLevelDiff < -9)
            return ColorCore.LvlDiffN9;//Solid Red
        if (playerLevelDiff < -6)
            return ColorCore.LvlDiffN6;//Dark Orange
        if (playerLevelDiff < -3)
            return ColorCore.LvlDiffN3;//Orange
        if (playerLevelDiff < 0)
            return ColorCore.LvlDiffN0;//Yellow-Orange
        if (playerLevelDiff > 9)
            return ColorCore.LvlDiffP9;//Bright Green
        if (playerLevelDiff > 6)
            return ColorCore.LvlDiffP6;//Green
        if (playerLevelDiff > 3)
            return ColorCore.LvlDiffP3;//Yellow-Green
        if (playerLevelDiff > 0)
            return ColorCore.LvlDiffP0;//Yellow
        return ColorCore.LvlDiffDefault;//Yellow
    }


    final int getSize() {
        try {
            if (null == this.class52 || this.class52.pnpcId == -1) {

                return super.getSize();
            } else {
                return NPCDefinition.getNPCDefinition(this.class52.pnpcId).size;
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "e.H(" + (byte) 114 + ')');
        }
    }

    final int getRenderAnimationId() {
        try {

            return this.renderAnimationId;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "e.B(" + -1 + ')');
        }
    }

    final void parseAppearance(int var1, DataBuffer buffer) {
        try {
            buffer.index = 0;
            int var3 = buffer.readUnsignedByte();
            int npcId = -1;
            int var4 = 1 & var3;
            boolean var6 = (var3 & 4) != 0;
            int var7 = super.getSize();
            int[] look = new int[12];
            this.setSize(1 + (var3 >> 3 & 7), 2);
            this.anInt3958 = 3 & var3 >> 6;
            this.xAxis += (-var7 + this.getSize()) * 64;
            this.zAxis += 64 * (this.getSize() + -var7);
            this.skullIcon = buffer.readSignedByte();
            this.headIcon = buffer.readSignedByte();
            this.teamId = 0;

            int var11;
            int var12;
            int outfit;
            int var14;
            for (int var10 = 0; var10 < 12; ++var10) {
                var11 = buffer.readUnsignedByte();
                if (var11 == 0) {
                    look[var10] = 0;
                } else {
                    var12 = buffer.readUnsignedByte();
                    outfit = (var11 << 8) + var12;
                    if (var10 == 0 && outfit == 65535) {
                        npcId = buffer.readUnsignedShort();
                        this.teamId = buffer.readUnsignedByte();
                        break;
                    }

                    if (32768 <= outfit) {
                        int equipId = outfit - 32768;
                        if (equipId > Class75_Sub4.anIntArray2664.length) {
                            System.err.println("Player->parseAppearance()-> Array length = " + Class75_Sub4.anIntArray2664.length + ", equipId=" + equipId + ", item def size=" + TextureOperation39.itemDefinitionSize);
                            continue;
                        }
                        outfit = Class75_Sub4.anIntArray2664[equipId];
                        look[var10] = TextureOperation3.bitwiseOr(1073741824, outfit);
                        var14 = ItemDefinition.getItemDefinition(outfit).teamId;
                        if (var14 != 0) {
                            this.teamId = var14;
                        }
                    } else {
                        look[var10] = TextureOperation3.bitwiseOr(-256 + outfit, Integer.MIN_VALUE);
                    }
                }
            }

            int[] colors = new int[5];

            for (var11 = 0; var11 < 5; ++var11) {
                var12 = buffer.readUnsignedByte();
                if (var12 < 0 || var12 >= AudioThread.aShortArrayArray344[var11].length) {
                    var12 = 0;
                }

                colors[var11] = var12;
            }

            this.renderAnimationId = buffer.readUnsignedShort();
            long var20 = buffer.readLong();
            this.displayName = Objects.requireNonNull(Unsorted.method1052(var20)).longToRSString();
            this.COMBAT_LEVEL = buffer.readUnsignedByte();
            if (var6) {
                this.anInt3974 = buffer.readUnsignedShort();
                this.combatLevel = this.COMBAT_LEVEL;
                this.anInt3970 = -1;
            } else {
                this.anInt3974 = 0;
                this.combatLevel = buffer.readUnsignedByte();
                this.anInt3970 = buffer.readUnsignedByte();
                if (this.anInt3970 == 255) {
                    this.anInt3970 = -1;
                }
            }

            outfit = this.anInt3969;
            this.anInt3969 = buffer.readUnsignedByte();
            if (this.anInt3969 == 0) {
                Class162.method2203(this);
            } else {
                int var15 = this.anInt3966;
                int var16 = this.anInt3963;
                int var17 = this.anInt3973;
                var14 = this.anInt3952;
                this.anInt3952 = buffer.readUnsignedShort();
                this.anInt3966 = buffer.readUnsignedShort();
                this.anInt3963 = buffer.readUnsignedShort();
                this.anInt3973 = buffer.readUnsignedShort();
                if (this.anInt3969 != outfit || var14 != this.anInt3952 || var15 != this.anInt3966 || var16 != this.anInt3963 || var17 != this.anInt3973) {
                    Unsorted.method518(this);
                }
            }

            if (null == this.class52) {
                this.class52 = new Class52();
            }

            var14 = this.class52.pnpcId;
            this.class52.method1161(colors, npcId, var4 == 1, look, this.renderAnimationId);
            if (npcId != var14) {
                this.xAxis = 128 * this.anIntArray2767[0] + this.getSize() * 64;
                this.zAxis = 128 * this.anIntArray2755[0] - -(64 * this.getSize());
            }
        } catch (RuntimeException var18) {
            throw ClientErrorException.clientError(var18, "e.P(" + var1 + ',' + (buffer != null ? "{...}" : "null") + ')');
        }
    }

    final void animate(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9, int var11, Class127_Sub1 var12) {
        try {
            if (this.class52 != null) {
                SequenceDefinition var13 = this.anInt2771 != -1 && 0 == this.anInt2828 ? SequenceDefinition.getAnimationDefinition(this.anInt2771) : null;
                SequenceDefinition var14 = this.anInt2764 != -1 && !this.aBoolean3968 && (this.getRenderAnimationType().stand_animation != this.anInt2764 || var13 == null) ? SequenceDefinition.getAnimationDefinition(this.anInt2764) : null;
                Model var15 = this.class52.method1165(this.aClass145Array2809, this.anInt2776, var14, var13, this.anInt2802, this.anInt2793, -120, this.anInt2760, this.anInt2832, this.anInt2813);
                int var16 = Unsorted.method1727((byte) 123);
                if (HDToolKit.highDetail && Class3_Sub24_Sub3.maxClientMemory < 96 && var16 > 50) {
                    Class3_Sub1.method90(1);
                }

                int var17;
                if (0 != TextureOperation20.paramModeWhat && var16 < 50) {
                    for (var17 = 50 - var16; Class56.anInt893 < var17; ++Class56.anInt893) {
                        Class3_Sub6.softReferenceTestArray[Class56.anInt893] = new byte[102400];
                    }

                    while (Class56.anInt893 > var17) {
                        --Class56.anInt893;
                        Class3_Sub6.softReferenceTestArray[Class56.anInt893] = null;
                    }
                }

                if (var15 != null) {
                    this.anInt2820 = var15.method1871();
                    Model var23;
                    if (Class140_Sub6.aBoolean2910 && (-1 == this.class52.pnpcId || NPCDefinition.getNPCDefinition(this.class52.pnpcId).aBoolean1249)) {
                        var23 = Class140_Sub3.method1957(160, this.aBoolean2810, var14 == null ? var13 : var14, this.xAxis, 0, this.zAxis, 0, 1, var15, var1, null != var14 ? this.anInt2813 : this.anInt2832, this.anInt2831, 240);
                        if (HDToolKit.highDetail) {
                            float var18 = HDToolKit.method1852();
                            float var19 = HDToolKit.method1839();
                            HDToolKit.depthBufferWritingDisabled();
                            HDToolKit.method1825(var18, -150.0F + var19);
                            var23.animate(0, var2, var3, var4, var5, var6, var7, var8, -1L, var11, null);
                            HDToolKit.method1830();
                            HDToolKit.method1825(var18, var19);
                        } else {
                            var23.animate(0, var2, var3, var4, var5, var6, var7, var8, -1L, var11, null);
                        }
                    }

                    if (Class102.player == this) {
                        for (var17 = ClientErrorException.aClass96Array2114.length + -1; var17 >= 0; --var17) {
                            Class96 var27 = ClientErrorException.aClass96Array2114[var17];
                            if (var27 != null && var27.anInt1355 != -1) {
                                int var21;
                                int var20;
                                if (var27.anInt1360 == 1 && 0 <= var27.anInt1359 && var27.anInt1359 < NPC.npcs.length) {
                                    NPC var24 = NPC.npcs[var27.anInt1359];
                                    if (null != var24) {
                                        var20 = var24.xAxis / 32 - Class102.player.xAxis / 32;
                                        var21 = -(Class102.player.zAxis / 32) + var24.zAxis / 32;
                                        this.method1979(null, var21, var15, var20, var6, var11, var1, var8, var5, var4, var2, var27.anInt1355, var3, var7);
                                    }
                                }

                                if (var27.anInt1360 == 2) {
                                    int var29 = 4 * (-Class131.anInt1716 + var27.anInt1356) + 2 + -(Class102.player.xAxis / 32);
                                    var20 = 2 + (4 * (var27.anInt1347 - Texture.anInt1152) - Class102.player.zAxis / 32);
                                    this.method1979(null, var20, var15, var29, var6, var11, var1, var8, var5, var4, var2, var27.anInt1355, var3, var7);
                                }

                                if (var27.anInt1360 == 10 && var27.anInt1359 >= 0 && var27.anInt1359 < Unsorted.players.length) {
                                    Player var28 = Unsorted.players[var27.anInt1359];
                                    if (null != var28) {
                                        var20 = -(Class102.player.xAxis / 32) + var28.xAxis / 32;
                                        var21 = var28.zAxis / 32 + -(Class102.player.zAxis / 32);
                                        this.method1979(null, var21, var15, var20, var6, var11, var1, var8, var5, var4, var2, var27.anInt1355, var3, var7);
                                    }
                                }
                            }
                        }
                    }

                    this.method1971(var15, (byte) -103);
                    this.method1969((byte) 110, var15, var1);
                    var23 = null;
                    if (!this.aBoolean3968 && this.anInt2842 != -1 && this.anInt2805 != -1) {
                        GraphicDefinition var26 = GraphicDefinition.getGraphicDefinition((byte) 42, this.anInt2842);
                        var23 = var26.method966(this.anInt2826, this.anInt2805, this.anInt2761);
                        if (var23 != null) {
                            var23.method1897(0, -this.anInt2799, 0);
                            if (var26.aBoolean536) {
                                if (TextureOperation15.anInt3198 != 0) {
                                    var23.method1896(TextureOperation15.anInt3198);
                                }

                                if (0 != Class3_Sub28_Sub9.anInt3623) {
                                    var23.method1886(Class3_Sub28_Sub9.anInt3623);
                                }

                                if (TextureOperation16.anInt3111 != 0) {
                                    var23.method1897(0, TextureOperation16.anInt3111, 0);
                                }
                            }
                        }
                    }

                    Model var25 = null;
                    if (!this.aBoolean3968 && this.anObject2796 != null) {
                        if (Class44.anInt719 >= this.anInt2778) {
                            this.anObject2796 = null;
                        }

                        if (Class44.anInt719 >= this.anInt2797 && this.anInt2778 > Class44.anInt719) {
                            if (this.anObject2796 instanceof Class140_Sub3) {
                                var25 = (Model) ((Class140_Sub3) this.anObject2796).method1963();
                            } else {
                                var25 = (Model) this.anObject2796;
                            }

                            Objects.requireNonNull(var25).method1897(this.anInt2782 + -this.xAxis, this.anInt2812 + -this.anInt2831, this.anInt2833 + -this.zAxis);
                            if (this.anInt2806 == 512) {
                                var25.method1900();
                            } else if (this.anInt2806 == 1024) {
                                var25.method1874();
                            } else if (this.anInt2806 == 1536) {
                                var25.method1885();
                            }
                        }
                    }

                    if (HDToolKit.highDetail) {
                        var15.aBoolean2699 = true;
                        var15.animate(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, this.aClass127_Sub1_2801);
                        if (var23 != null) {
                            var23.aBoolean2699 = true;
                            var23.animate(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, this.aClass127_Sub1_2801);
                        }
                    } else {
                        if (null != var23) {
                            var15 = ((Class140_Sub1_Sub2) var15).method1943(var23);
                        }

                        if (var25 != null) {
                            var15 = ((Class140_Sub1_Sub2) var15).method1943(var25);
                        }

                        var15.aBoolean2699 = true;
                        var15.animate(var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, this.aClass127_Sub1_2801);
                    }

                    if (null != var25) {
                        if (this.anInt2806 == 512) {
                            var25.method1885();
                        } else if (1024 == this.anInt2806) {
                            var25.method1874();
                        } else if (1536 == this.anInt2806) {
                            var25.method1900();
                        }

                        var25.method1897(-this.anInt2782 + this.xAxis, -this.anInt2812 + this.anInt2831, -this.anInt2833 + this.zAxis);
                    }

                }
            }
        } catch (RuntimeException var22) {
            throw ClientErrorException.clientError(var22, "e.IA(" + var1 + ',' + var2 + ',' + var3 + ',' + var4 + ',' + var5 + ',' + var6 + ',' + var7 + ',' + var8 + ',' + var9 + ',' + var11 + ',' + (var12 != null ? "{...}" : "null") + ')');
        }
    }

    private void method1979(Class127_Sub1 var1, int var2, Model var3, int var4, int var5, int var6, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15) {
        try {
            int var16 = var4 * var4 - -(var2 * var2);
            if (var16 >= 16 && var16 <= 360000) {
                int var17 = (int) (325.949D * Math.atan2(var4, var2)) & 0x7FF;
                Model var18 = Class128.method1763(var17, this.zAxis, var13, this.xAxis, var3, this.anInt2831);
                if (var18 != null) {
                    if (HDToolKit.highDetail) {
                        float var19 = HDToolKit.method1852();
                        float var20 = HDToolKit.method1839();
                        HDToolKit.depthBufferWritingDisabled();
                        HDToolKit.method1825(var19, var20 - 150.0F);
                        var18.animate(0, var12, var14, var11, var10, var5, var15, var9, -1L, var6, var1);
                        HDToolKit.method1830();
                        HDToolKit.method1825(var19, var20);
                    } else {
                        var18.animate(0, var12, var14, var11, var10, var5, var15, var9, -1L, var6, var1);
                    }
                }

            }
        } catch (RuntimeException var21) {
            throw ClientErrorException.clientError(var21, "e.N(" + (var1 != null ? "{...}" : "null") + ',' + var2 + ',' + (var3 != null ? "{...}" : "null") + ',' + var4 + ',' + var5 + ',' + var6 + ',' + 2047 + ',' + var8 + ',' + var9 + ',' + var10 + ',' + var11 + ',' + var12 + ',' + var13 + ',' + var14 + ',' + var15 + ')');
        }
    }

    public final boolean hasDefinitions() {
        try {

            return this.class52 != null;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "e.L(" + (byte) 17 + ')');
        }
    }

    final RSString getName() {
        try {
            RSString var2 = this.displayName;

            if (BufferedDataStream.aClass94Array3802 != null) {
                var2 = RSString.stringCombiner(new RSString[]{BufferedDataStream.aClass94Array3802[this.anInt3958], var2});
            }

            if (null != Unsorted.aClass94Array45) {
                var2 = RSString.stringCombiner(new RSString[]{var2, Unsorted.aClass94Array45[this.anInt3958]});
            }

            return var2;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "e.Q(" + 0 + ')');
        }
    }

    final void method1867(int var1, int var2, int var3, int var4, int var5) {
    }

    final void method1981(int var2, boolean var3, int var4) {
        try {
            super.method1967(this.getSize(), var2, var4, var3);

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "e.O(" + (byte) 126 + ',' + var2 + ',' + var3 + ',' + var4 + ')');
        }
    }

    protected final void finalize() {
    }

    final int method1871() {
        try {
            return this.anInt2820;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "e.MA()");
        }
    }

}
