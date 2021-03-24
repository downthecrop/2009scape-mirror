package org.runite.client;

import org.rs09.client.data.Queue;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

final class Class81 {

    static Queue aClass13_1139 = new Queue();
    static int[][][] anIntArrayArrayArray1142;


    static void putRandomDataFile(DataBuffer var0, boolean var1) {
        try {
            if (!var1) {
                anIntArrayArrayArray1142 = null;
            }

            byte[] var2 = new byte[24];
            if (null != Unsorted.aClass30_1039) {
                try {
                    Unsorted.aClass30_1039.method984(-41, 0L);
                    Unsorted.aClass30_1039.method982(var2);

                    int var3;
                    for (var3 = 0; var3 < 24 && var2[var3] == 0; ++var3) {
                    }

                    if (var3 >= 24) {
                        throw new IOException();
                    }
                } catch (Exception var5) {
                    for (int var4 = 0; 24 > var4; ++var4) {
                        var2[var4] = -1;
                    }
                }
            }

            var0.putBytes(var2, 24);
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "la.G(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ')');
        }
    }

    static int method1398(Player var1) {
        try {

            int var2 = var1.anInt3963;
            RenderAnimationDefinition var3 = var1.getRenderAnimationType();
            if (var1.anInt2764 == var3.stand_animation) {
                var2 = var1.anInt3952;
            } else if (var3.run_anim != var1.anInt2764 && var1.anInt2764 != var3.run_follow_full_turn_anim && var1.anInt2764 != var3.run_follow_cw_turn_anim && var3.run_follow_ccw_turn_anim != var1.anInt2764) {
                if (var3.slow_walk_anim == var1.anInt2764 || var3.slow_walk_follow_full_turn_anim == var1.anInt2764 || var1.anInt2764 == var3.slow_walk_follow_cw_turn_anim || var3.slow_walk_follow_ccw_turn_anim == var1.anInt2764) {
                    var2 = var1.anInt3966;
                }
            } else {
                var2 = var1.anInt3973;
            }

            return var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "la.A(" + 0 + ',' + (var1 != null ? "{...}" : "null") + ')');
        }
    }

    static void friendsIgnoreListAlerts(long var1) {
        try {
            if (var1 != 0L) {
                if (Class3_Sub28_Sub5.anInt3591 < 100) {
                    RSString var4 = Objects.requireNonNull(Unsorted.method1052(var1)).longToRSString();

                    int var5;
                    for (var5 = 0; var5 < Class3_Sub28_Sub5.anInt3591; ++var5) {
                        if (Class114.ignores[var5] == var1) {
                            BufferedDataStream.addChatMessage(TextCore.emptyJagexString, 0, RSString.stringCombiner(new RSString[]{var4, TextCore.HasIgnoreAlready}), -1);
                            return;
                        }
                    }

                    for (var5 = 0; Class8.anInt104 > var5; ++var5) {
                        if (var1 == Class50.aLongArray826[var5]) {
                            BufferedDataStream.addChatMessage(TextCore.emptyJagexString, 0, RSString.stringCombiner(new RSString[]{TextCore.HasPleaseRemove, var4, TextCore.HasFriendsToIgnore}), -1);
                            return;
                        }
                    }

                    if (var4.equalsString(Class102.player.displayName)) {
                        BufferedDataStream.addChatMessage(TextCore.emptyJagexString, 0, TextCore.HasOnOwnIgnoreList, -1);
                    } else {
                        Class114.ignores[Class3_Sub28_Sub5.anInt3591] = var1;
                        TextureOperation7.aClass94Array3341[Class3_Sub28_Sub5.anInt3591++] = Unsorted.method1052(var1);
                        Class110.anInt1472 = PacketParser.anInt3213;
                        TextureOperation12.outgoingBuffer.putOpcode(34);
                        TextureOperation12.outgoingBuffer.writeLong(var1);
                    }
                } else {
                    BufferedDataStream.addChatMessage(TextCore.emptyJagexString, 0, TextCore.HasIgnoreListFull, -1);
                }
            }
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "la.B(" + 32 + ',' + var1 + ')');
        }
    }

    static void method1400(Signlink var0, Object var1, int var2) {
        try {
            if (var2 >= -29) {
                anIntArrayArrayArray1142 = null;
            }

            if (null != var0.systemEventQueue) {
                for (int var3 = 0; var3 < 50 && null != var0.systemEventQueue.peekEvent(); ++var3) {
                    TimeUtils.sleep(1L);
                }

                if (var1 != null) {
                    var0.systemEventQueue.postEvent(new ActionEvent(var1, 1001, "dummy"));
                }

            }
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "la.E(" + (var0 != null ? "{...}" : "null") + ',' + (var1 != null ? "{...}" : "null") + ',' + var2 + ')');
        }
    }

    static Class57 method1401(int var1) {
        try {
            Class57 var2 = (Class57) Class128.aReferenceCache_1683.get(var1);
            if (var2 == null) {
                byte[] var3 = Class46.aClass153_737.getFile(31, var1);
                var2 = new Class57();
                if (var3 != null) {
                    var2.method1190(new DataBuffer(var3), var1);
                }

                Class128.aReferenceCache_1683.put(var2, var1);
            }
            return var2;
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "la.D(" + 1001 + ',' + var1 + ')');
        }
    }

    static Class131 getWorldMapArchive(RSString var1, CacheIndex var2) {
        try {
            int var3 = var2.getArchiveForName(var1);
            if (var3 == -1) {
                return new Class131(0);
            } else {
                int[] var4 = var2.getFileIds(var3);
                Class131 var5 = new Class131(Objects.requireNonNull(var4).length);

                for (int var6 = 0; var5.anInt1720 > var6; ++var6) {
                    DataBuffer var7 = new DataBuffer(var2.getFile(var3, var4[var6]));
                    var5.aClass94Array1721[var6] = var7.readString();
                    var5.aByteArray1730[var6] = var7.readSignedByte();
                    var5.aShortArray1727[var6] = (short) var7.readUnsignedShort();
                    var5.aShortArray1718[var6] = (short) var7.readUnsignedShort();
                    var5.anIntArray1725[var6] = var7.readInt();
                }

                return var5;
            }
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "la.C(" + (var1 != null ? "{...}" : "null") + ',' + (var2 != null ? "{...}" : "null") + ')');
        }
    }

}
