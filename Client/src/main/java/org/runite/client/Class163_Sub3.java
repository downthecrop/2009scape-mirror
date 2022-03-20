package org.runite.client;

import java.util.Objects;

final class Class163_Sub3 extends Class163 {

    static int[] anIntArray2999;
    static RSString[] aStringArray3003 = new RSString[100];
    static boolean aBoolean3004 = true;
    static byte[][] aByteArrayArray3005;
    static int[] anIntArray3007 = new int[]{-1, -1, 1, 1};


    static void method2229(long var0) {
        try {
            if (var0 != 0) {
                if ((100 > Class8.anInt104 || TextureOperation3.disableGEBoxes) && Class8.anInt104 < 200) {
                    RSString var3 = Objects.requireNonNull(Unsorted.method1052(var0)).longToRSString();

                    int var4;
                    for (var4 = 0; Class8.anInt104 > var4; ++var4) {
                        if (var0 == Class50.aLongArray826[var4]) {
                            BufferedDataStream.addChatMessage(RSString.parse(""), 0, RSString.stringCombiner(new RSString[]{var3, TextCore.HasFriendsAlready}), -1);
                            return;
                        }
                    }

                    for (var4 = 0; var4 < Class3_Sub28_Sub5.anInt3591; ++var4) {
                        if (Class114.ignores[var4] == var0) {
                            BufferedDataStream.addChatMessage(RSString.parse(""), 0, RSString.stringCombiner(new RSString[]{TextCore.HasPleaseRemove, var3, TextCore.HasIgnoreToFriends}), -1);
                            return;
                        }
                    }

                    if (var3.equalsString(Class102.player.displayName)) {
                        BufferedDataStream.addChatMessage(RSString.parse(""), 0, TextCore.HasOnOwnFriendsList, -1);
                    } else {
                        Class70.aStringArray1046[Class8.anInt104] = var3;
                        Class50.aLongArray826[Class8.anInt104] = var0;
                        Unsorted.anIntArray882[Class8.anInt104] = 0;
                        Unsorted.aStringArray2566[Class8.anInt104] = RSString.parse("");
                        Class57.anIntArray904[Class8.anInt104] = 0;
                        Unsorted.aBooleanArray73[Class8.anInt104] = false;
                        ++Class8.anInt104;
                        Class110.anInt1472 = PacketParser.anInt3213;
                        TextureOperation12.outgoingBuffer.putOpcode(120);
                        TextureOperation12.outgoingBuffer.writeLong(var0);
                    }
                } else {
                    BufferedDataStream.addChatMessage(RSString.parse(""), 0, TextCore.HasFriendsListFull, -1);
                }
            }
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "fb.C(" + var0 + ',' + (byte) -91 + ')');
        }
    }

}
