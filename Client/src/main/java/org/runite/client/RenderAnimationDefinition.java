package org.runite.client;

import org.rs09.client.data.ReferenceCache;

final class RenderAnimationDefinition {

    static int[] anIntArray356 = new int[]{1, 0, -1, 0};
    static ReferenceCache aReferenceCache_1955 = new ReferenceCache(64);
    int yaw_max_speed = 0;
    int[][] equipment_transforms;
    int movement_acceleration = -1;
    static volatile int anInt362 = 0;
    int walk_follow_cw_turn_anim = -1;
    int standing_ccw_turn = -1;
    int stand_animation = -1;
    int yaw_acceleration = 0;
    int roll_max_speed = 0;
    int pitch_target_angle = 0;
    int slow_walk_follow_full_turn_anim = -1;
    int run_follow_ccw_turn_anim = -1;
    int run_follow_cw_turn_anim = -1;
    static RSString aClass94_378 = null;
    int slow_walk_follow_cw_turn_anim = -1;
    int hill_height = 0;
    int walk_animation = -1;
    static byte[][][] aByteArrayArrayArray383;
    static int anInt384 = 0;
    int run_follow_full_turn_anim = -1;
    int roll_acceleration = 0;
    int walk_follow_full_turn_anim = -1;
    int walk_follow_ccw_turn_anim = -1;
    int run_anim = -1;
    int hill_width = 0;
    static int anInt396;
    int slow_walk_anim = -1;
    int pitch_max_speed = 0;
    int roll_target_angle = 0;
    static boolean aBoolean402 = false;
    int pitch_acceleration = 0;
    int slow_walk_follow_ccw_turn_anim = -1;
    int standing_cw_turn = -1;

    final void method899() {
        try {

        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ck.B(" + 96 + ')');
        }
    }

    static void method900(Class140_Sub4 var0) {
        try {
            var0.aBoolean2810 = false;
            SequenceDefinition var2;
            if (-1 != var0.anInt2764) {
                var2 = SequenceDefinition.getAnimationDefinition(var0.anInt2764);
                if (null == var2.frames) {
                    var0.anInt2764 = -1;
                } else {
                    ++var0.anInt2802;
                    if (var0.anInt2813 < var2.frames.length && var0.anInt2802 > var2.duration[var0.anInt2813]) {
                        var0.anInt2802 = 1;
                        ++var0.anInt2813;
                        ++var0.anInt2793;
                        Unsorted.method1470(var0.zAxis, var2, var0.xAxis, var0 == Class102.player, var0.anInt2813);
                    }

                    if (var2.frames.length <= var0.anInt2813) {
                        var0.anInt2813 = 0;
                        var0.anInt2802 = 0;
                        Unsorted.method1470(var0.zAxis, var2, var0.xAxis, Class102.player == var0, var0.anInt2813);
                    }

                    var0.anInt2793 = var0.anInt2813 - -1;
                    if (var2.frames.length <= var0.anInt2793) {
                        var0.anInt2793 = 0;
                    }
                }
            }

            int var6;
            if (var0.anInt2842 != -1 && var0.anInt2759 <= Class44.anInt719) {
                var6 = GraphicDefinition.getGraphicDefinition((byte) 42, var0.anInt2842).anInt542;
                if (var6 == -1) {
                    var0.anInt2842 = -1;
                } else {
                    SequenceDefinition var3 = SequenceDefinition.getAnimationDefinition(var6);
                    if (var3.frames == null) {
                        var0.anInt2842 = -1;
                    } else {
                        if (0 > var0.anInt2805) {
                            var0.anInt2805 = 0;
                            Unsorted.method1470(var0.zAxis, var3, var0.xAxis, Class102.player == var0, 0);
                        }

                        ++var0.anInt2761;
                        if (var0.anInt2805 < var3.frames.length && var0.anInt2761 > var3.duration[var0.anInt2805]) {
                            ++var0.anInt2805;
                            var0.anInt2761 = 1;
                            Unsorted.method1470(var0.zAxis, var3, var0.xAxis, Class102.player == var0, var0.anInt2805);
                        }

                        if (var0.anInt2805 >= var3.frames.length) {
                            var0.anInt2842 = -1;
                        }

                        var0.anInt2826 = var0.anInt2805 - -1;
                        if (var0.anInt2826 >= var3.frames.length) {
                            var0.anInt2826 = -1;
                        }
                    }
                }
            }

            if (var0.anInt2771 != -1 && var0.anInt2828 <= 1) {
                var2 = SequenceDefinition.getAnimationDefinition(var0.anInt2771);
                if (var2.resetWhenWalk == 1 && var0.anInt2811 > 0 && var0.anInt2800 <= Class44.anInt719 && Class44.anInt719 > var0.anInt2790) {
                    var0.anInt2828 = 1;
                    return;
                }
            }

            if (var0.anInt2771 != -1 && var0.anInt2828 == 0) {
                var2 = SequenceDefinition.getAnimationDefinition(var0.anInt2771);
                if (var2.frames == null) {
                    var0.anInt2771 = -1;
                } else {
                    ++var0.anInt2760;
                    if (var2.frames.length > var0.anInt2832 && var0.anInt2760 > var2.duration[var0.anInt2832]) {
                        var0.anInt2760 = 1;
                        ++var0.anInt2832;
                        Unsorted.method1470(var0.zAxis, var2, var0.xAxis, var0 == Class102.player, var0.anInt2832);
                    }

                    if (var2.frames.length <= var0.anInt2832) {
                        var0.anInt2832 -= var2.anInt1865;
                        ++var0.anInt2773;
                        if (var2.maxLoops > var0.anInt2773) {
                            if (var0.anInt2832 >= 0 && var0.anInt2832 < var2.frames.length) {
                                Unsorted.method1470(var0.zAxis, var2, var0.xAxis, Class102.player == var0, var0.anInt2832);
                            } else {
                                var0.anInt2771 = -1;
                            }
                        } else {
                            var0.anInt2771 = -1;
                        }
                    }

                    var0.anInt2776 = var0.anInt2832 + 1;
                    if (var0.anInt2776 >= var2.frames.length) {
                        var0.anInt2776 -= var2.anInt1865;
                        if (var2.maxLoops > var0.anInt2773 + 1) {
                            if (0 > var0.anInt2776 || var0.anInt2776 >= var2.frames.length) {
                                var0.anInt2776 = -1;
                            }
                        } else {
                            var0.anInt2776 = -1;
                        }
                    }

                    var0.aBoolean2810 = var2.aBoolean1859;
                }
            }

            if (0 < var0.anInt2828) {
                --var0.anInt2828;
            }

            for (var6 = 0; var0.aClass145Array2809.length > var6; ++var6) {
                Class145 var7 = var0.aClass145Array2809[var6];
                if (null != var7) {
                    if (var7.anInt1900 <= 0) {
                        SequenceDefinition var4 = SequenceDefinition.getAnimationDefinition(var7.animationId);
                        if (var4.frames == null) {
                            var0.aClass145Array2809[var6] = null;
                        } else {
                            ++var7.anInt1897;
                            if (var7.anInt1893 < var4.frames.length && var7.anInt1897 > var4.duration[var7.anInt1893]) {
                                ++var7.anInt1893;
                                var7.anInt1897 = 1;
                                Unsorted.method1470(var0.zAxis, var4, var0.xAxis, var0 == Class102.player, var7.anInt1893);
                            }

                            if (var7.anInt1893 >= var4.frames.length) {
                                ++var7.anInt1894;
                                var7.anInt1893 -= var4.anInt1865;
                                if (var4.maxLoops > var7.anInt1894) {
                                    if (var7.anInt1893 >= 0 && var4.frames.length > var7.anInt1893) {
                                        Unsorted.method1470(var0.zAxis, var4, var0.xAxis, Class102.player == var0, var7.anInt1893);
                                    } else {
                                        var0.aClass145Array2809[var6] = null;
                                    }
                                } else {
                                    var0.aClass145Array2809[var6] = null;
                                }
                            }

                            var7.anInt1891 = 1 + var7.anInt1893;
                            if (var4.frames.length <= var7.anInt1891) {
                                var7.anInt1891 -= var4.anInt1865;
                                if (1 + var7.anInt1894 < var4.maxLoops) {
                                    if (var7.anInt1891 < 0 || var4.frames.length <= var7.anInt1891) {
                                        var7.anInt1891 = -1;
                                    }
                                } else {
                                    var7.anInt1891 = -1;
                                }
                            }
                        }
                    } else {
                        --var7.anInt1900;
                    }
                }
            }

        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ck.F(" + (var0 != null ? "{...}" : "null") + ',' + -11973 + ')');
        }
    }

    final void parse(DataBuffer buffer) {
        try {

            while (true) {
                int opcode = buffer.readUnsignedByte();
                if (opcode == 0) {
                    return;
                }

                this.parseOpcode(opcode, buffer);
            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ck.H(" + -1 + ',' + (buffer != null ? "{...}" : "null") + ')');
        }
    }

    private void parseOpcode(int opcode, DataBuffer buffer) {
        try {
            if (opcode == 1) {
                this.stand_animation = buffer.readUnsignedShort();
                this.walk_animation = buffer.readUnsignedShort();
                if (this.walk_animation == 65535) {
                    this.walk_animation = -1;
                }

                if (65535 == this.stand_animation) {
                    this.stand_animation = -1;
                }
            } else if (opcode == 2) {
                this.slow_walk_anim = buffer.readUnsignedShort();
            } else if (opcode == 3) {
                this.slow_walk_follow_full_turn_anim = buffer.readUnsignedShort();
            } else if (4 == opcode) {
                this.slow_walk_follow_ccw_turn_anim = buffer.readUnsignedShort();
            } else if (opcode == 5) {
                this.slow_walk_follow_cw_turn_anim = buffer.readUnsignedShort();
            } else if (6 == opcode) {
                this.run_anim = buffer.readUnsignedShort();
            } else if (7 == opcode) {
                this.run_follow_full_turn_anim = buffer.readUnsignedShort();
            } else if (opcode == 8) {
                this.run_follow_ccw_turn_anim = buffer.readUnsignedShort();
            } else if (opcode == 9) {
                this.run_follow_cw_turn_anim = buffer.readUnsignedShort();
            } else if (opcode == 26) {
                this.hill_width = (short) (4 * buffer.readUnsignedByte());
                this.hill_height = (short) (4 * buffer.readUnsignedByte());
            } else if (opcode == 27) {
                if (this.equipment_transforms == null) {
                    this.equipment_transforms = new int[12][];
                }

                int bodyID = buffer.readUnsignedByte();
                this.equipment_transforms[bodyID] = new int[6];

                for (int type = 0; type < 6; ++type) {

                    /*
                     * 0 -Rotate X
                     * 1 - Rotate Y
                     * 2 - Rotate Z
                     * 3 - Translate X
                     * 4 - Translate Y
                     * 5 - Translate Z
                     */

                    this.equipment_transforms[bodyID][type] = buffer.readSignedShort();
                }
            } else if (opcode == 29) {
                this.yaw_acceleration = buffer.readUnsignedByte();
            } else if (opcode == 30) {
                this.yaw_max_speed = buffer.readUnsignedShort();
            } else if (opcode == 31) {
                this.roll_acceleration = buffer.readUnsignedByte();
            } else if (32 == opcode) {
                this.roll_max_speed = buffer.readUnsignedShort();
            } else if (33 == opcode) {
                this.roll_target_angle = buffer.readSignedShort();
            } else if (34 == opcode) {
                this.pitch_acceleration = buffer.readUnsignedByte();
            } else if (opcode == 35) {
                this.pitch_max_speed = buffer.readUnsignedShort();
            } else if (opcode == 36) {
                this.pitch_target_angle = buffer.readSignedShort();
            } else if (opcode == 37) {
                this.movement_acceleration = buffer.readUnsignedByte();
            } else if (opcode == 38) {
                this.standing_ccw_turn = buffer.readUnsignedShort();
            } else if (39 == opcode) {
                this.standing_cw_turn = buffer.readUnsignedShort();
            } else if (opcode == 40) {
                this.walk_follow_full_turn_anim = buffer.readUnsignedShort();
            } else if (41 == opcode) {
                this.walk_follow_ccw_turn_anim = buffer.readUnsignedShort();
            } else if (opcode == 42) {
                this.walk_follow_cw_turn_anim = buffer.readUnsignedShort();
            } else if (opcode == 43) {
                buffer.readUnsignedShort();
            } else if (opcode == 44) {
                buffer.readUnsignedShort();
            } else if (opcode == 45) {
                buffer.readUnsignedShort();
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "ck.E(" + opcode + ',' + (byte) -106 + ',' + (buffer != null ? "{...}" : "null") + ')');
        }
    }

}
