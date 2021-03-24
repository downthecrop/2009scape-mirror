package org.runite.client;

final class Class17 {

    static Interface2 anInterface2_408;
    static Thread aThread409;
    static int anInt410;
    static Class64 aClass64_413;

    static void method904(int var0, Class140_Sub4 var1) {
        try {
            if (var1.anInt2779 != 0) {
                RenderAnimationDefinition var2 = var1.getRenderAnimationType();
                int var4;
                int var5;
                if (var1.anInt2772 != -1 && 32768 > var1.anInt2772) {
                    NPC var3 = NPC.npcs[var1.anInt2772];
                    if (var3 != null) {
                        var5 = -var3.anInt2829 + var1.anInt2829;
                        var4 = -var3.anInt2819 + var1.anInt2819;
                        if (0 != var4 || 0 != var5) {
                            var1.anInt2806 = (int) (Math.atan2(var4, var5) * 325.949D) & 0x7FF;
                        }
                    }
                }

                int var6;
                int var9;
                if (var1.anInt2772 >= 32768) {
                    var9 = -32768 + var1.anInt2772;
                    if (Class3_Sub1.localIndex == var9) {
                        var9 = 2047;
                    }

                    Player var10 = Unsorted.players[var9];
                    if (null != var10) {
                        var6 = -var10.anInt2829 + var1.anInt2829;
                        var5 = -var10.anInt2819 + var1.anInt2819;
                        if (var5 != 0 || var6 != 0) {
                            var1.anInt2806 = (int) (Math.atan2(var5, var6) * 325.949D) & 0x7FF;
                        }
                    }
                }

                if ((0 != var1.anInt2786 || 0 != var1.anInt2762) && (var1.anInt2816 == 0 || var1.anInt2824 > 0)) {
                    var9 = var1.anInt2819 + -((-Class131.anInt1716 + (var1.anInt2786 - Class131.anInt1716)) * 64);
                    var4 = -((-Texture.anInt1152 + (var1.anInt2762 - Texture.anInt1152)) * 64) + var1.anInt2829;
                    if (var9 != 0 || var4 != 0) {
                        var1.anInt2806 = (int) (Math.atan2(var9, var4) * 325.949D) & 0x7FF;
                    }

                    var1.anInt2762 = 0;
                    var1.anInt2786 = 0;
                }

                var9 = var1.anInt2806 - var1.anInt2785 & 0x7FF;
                if (var9 == 0) {
                    var1.anInt2789 = 0;
                    var1.anInt2821 = 0;
                } else if (var2.yaw_acceleration == 0) {
                    ++var1.anInt2789;
                    boolean var11;
                    if (var9 > 1024) {
                        var1.anInt2785 -= var1.anInt2779;
                        var11 = true;
                        if (var1.anInt2779 > var9 || var9 > -var1.anInt2779 + 2048) {
                            var1.anInt2785 = var1.anInt2806;
                            var11 = false;
                        }

                        if (var1.anInt2764 == var2.stand_animation && (25 < var1.anInt2789 || var11)) {
                            if (var2.standing_ccw_turn == -1) {
                                var1.anInt2764 = var2.walk_animation;
                            } else {
                                var1.anInt2764 = var2.standing_ccw_turn;
                            }
                        }
                    } else {
                        var11 = true;
                        var1.anInt2785 += var1.anInt2779;
                        if (var1.anInt2779 > var9 || 2048 - var1.anInt2779 < var9) {
                            var11 = false;
                            var1.anInt2785 = var1.anInt2806;
                        }

                        if (var2.stand_animation == var1.anInt2764 && (25 < var1.anInt2789 || var11)) {
                            if (-1 == var2.standing_cw_turn) {
                                var1.anInt2764 = var2.walk_animation;
                            } else {
                                var1.anInt2764 = var2.standing_cw_turn;
                            }
                        }
                    }

                    var1.anInt2785 &= 2047;
                } else {
                    if (var1.anInt2764 == var2.stand_animation && 25 < var1.anInt2789) {
                        if (var2.standing_cw_turn == -1) {
                            var1.anInt2764 = var2.walk_animation;
                        } else {
                            var1.anInt2764 = var2.standing_cw_turn;
                        }
                    }

                    var4 = var1.anInt2806 << 5;
                    if (var1.anInt2808 != var4) {
                        var1.anInt2791 = 0;
                        var1.anInt2808 = var4;
                        var5 = -var1.anInt2780 + var4 & 65535;
                        var6 = var1.anInt2821 * var1.anInt2821 / (var2.yaw_acceleration * 2);
                        int var7;
                        if (var1.anInt2821 > 0 && var6 <= var5 && -var6 + var5 < 32768) {
                            var1.anInt2803 = var5 / 2;
                            var1.aBoolean2769 = true;
                            var7 = var2.yaw_max_speed * var2.yaw_max_speed / (var2.yaw_acceleration * 2);
                            if (32767 < var7) {
                                var7 = 32767;
                            }

                            if (var7 < var1.anInt2803) {
                                var1.anInt2803 = -var7 + var5;
                            }
                        } else if (0 > var1.anInt2821 && var6 <= -var5 + 65536 && 65536 + -var5 + -var6 < 32768) {
                            var1.anInt2803 = (-var5 + 65536) / 2;
                            var1.aBoolean2769 = true;
                            var7 = var2.yaw_max_speed * var2.yaw_max_speed / (var2.yaw_acceleration * 2);
                            if (var7 > 32767) {
                                var7 = 32767;
                            }

                            if (var7 < var1.anInt2803) {
                                var1.anInt2803 = 65536 - (var5 + var7);
                            }
                        } else {
                            var1.aBoolean2769 = false;
                        }
                    }

                    if (var1.anInt2821 == 0) {
                        var5 = -var1.anInt2780 + var1.anInt2808 & 65535;
                        if (var5 < var2.yaw_acceleration) {
                            var1.anInt2780 = var1.anInt2808;
                        } else {
                            var1.anInt2791 = 0;
                            var6 = var2.yaw_max_speed * var2.yaw_max_speed / (2 * var2.yaw_acceleration);
                            var1.aBoolean2769 = true;
                            if (32767 < var6) {
                                var6 = 32767;
                            }

                            if (var5 >= 32768) {
                                var1.anInt2821 = -var2.yaw_acceleration;
                                var1.anInt2803 = (65536 - var5) / 2;
                                if (var1.anInt2803 > var6) {
                                    var1.anInt2803 = 65536 - (var5 + var6);
                                }
                            } else {
                                var1.anInt2821 = var2.yaw_acceleration;
                                var1.anInt2803 = var5 / 2;
                                if (var1.anInt2803 > var6) {
                                    var1.anInt2803 = -var6 + var5;
                                }
                            }
                        }
                    } else if (var1.anInt2821 <= 0) {
                        if (var1.anInt2803 <= var1.anInt2791) {
                            var1.aBoolean2769 = false;
                        }

                        if (!var1.aBoolean2769) {
                            var1.anInt2821 += var2.yaw_acceleration;
                            if (0 < var1.anInt2821) {
                                var1.anInt2821 = 0;
                            }
                        } else if (var1.anInt2821 > -var2.yaw_max_speed) {
                            var1.anInt2821 -= var2.yaw_acceleration;
                        }
                    } else {
                        if (var1.anInt2791 >= var1.anInt2803) {
                            var1.aBoolean2769 = false;
                        }

                        if (!var1.aBoolean2769) {
                            var1.anInt2821 -= var2.yaw_acceleration;
                            if (var1.anInt2821 < 0) {
                                var1.anInt2821 = 0;
                            }
                        } else if (var1.anInt2821 < var2.yaw_max_speed) {
                            var1.anInt2821 += var2.yaw_acceleration;
                        }
                    }

                    var1.anInt2780 += var1.anInt2821;
                    var1.anInt2780 &= 65535;
                    if (0 >= var1.anInt2821) {
                        var1.anInt2791 -= var1.anInt2821;
                    } else {
                        var1.anInt2791 += var1.anInt2821;
                    }

                    var1.anInt2785 = var1.anInt2780 >> 5;
                }

                if (var0 != 65536) {
                    method904(-93, null);
                }

            }
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "cm.A(" + var0 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

}
