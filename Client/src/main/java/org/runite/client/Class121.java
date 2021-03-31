package org.runite.client;

import org.rs09.client.config.GameConfig;
import org.rs09.client.net.Connection;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

final class Class121 {

    static boolean aBoolean1641 = false;
    static int anInt1642 = 0;
    int anInt1639 = 0;
    boolean aBoolean1640 = false;
    Class156 aClass156_1643;
    ByteBuffer aByteBuffer1644;
    int anInt1648 = 0;


    static int method1734(int var0, float var1, int var2, int var3, int[][] var4, int[][] var5, int var6, float[][] var7, int var8, byte var9, int var10, boolean var11, Class3_Sub11 var12, float[][] var13, int var14, int var15, float[][] var16, int var17) {
        try {
            int var18;
            if (var10 == 1) {
                var18 = var15;
                var15 = var17;
                var17 = -var18 + 128;
            } else if (var10 == 2) {
                var17 = -var17 + 128;
                var15 = -var15 + 128;
            } else if (var10 == 3) {
                var18 = var15;
                var15 = 128 + -var17;
                var17 = var18;
            }

            float var19;
            int var21;
            float var20;
            float var30;
            if (var15 == 0 && var17 == 0) {
                var19 = var16[var6][var14];
                var20 = var7[var6][var14];
                var30 = var13[var6][var14];
                var21 = var2;
            } else if (var15 == 128 && var17 == 0) {
                var21 = var3;
                var19 = var16[var6 + 1][var14];
                var20 = var7[1 + var6][var14];
                var30 = var13[var6 + 1][var14];
            } else if (var15 == 128 && var17 == 128) {
                var20 = var7[1 + var6][var14 - -1];
                var30 = var13[var6 + 1][1 + var14];
                var19 = var16[1 + var6][var14 - -1];
                var21 = var8;
            } else if (var15 == 0 && var17 == 128) {
                var20 = var7[var6][1 + var14];
                var19 = var16[var6][1 + var14];
                var30 = var13[var6][var14 - -1];
                var21 = var0;
            } else {
                var30 = var13[var6][var14];
                var19 = var16[var6][var14];
                float var23 = (float) var17 / 128.0F;
                float var22 = (float) var15 / 128.0F;
                var30 += (var13[var6 - -1][var14] - var30) * var22;
                var19 += (-var19 + var16[var6 + 1][var14]) * var22;
                float var24 = var13[var6][var14 + 1];
                float var25 = var16[var6][var14 - -1];
                var25 += (-var25 + var16[var6 + 1][var14 - -1]) * var22;
                var20 = var7[var6][var14];
                var19 += var23 * (-var19 + var25);
                float var26 = var7[var6][1 + var14];
                var24 += (-var24 + var13[var6 - -1][var14 + 1]) * var22;
                var30 += var23 * (-var30 + var24);
                var26 += (-var26 + var7[1 + var6][var14 - -1]) * var22;
                var20 += (-var20 + var7[var6 - -1][var14]) * var22;
                var20 += (-var20 + var26) * var23;
                int var27 = TextureOperation30.method210(var15, var2, var3);
                int var28 = TextureOperation30.method210(var15, var0, var8);
                var21 = TextureOperation30.method210(var17, var27, var28);
            }

            if (var9 > -111) {
                return -54;
            } else {
                int var32 = var17 + (var14 << 7);
                int var33 = Class3_Sub23.method408(var15, (byte) -51, var14, var5, var6, var17);
                int var31 = (var6 << 7) - -var15;
                return var12.method146(var31, var33, var32, var30, var19, var20, !var11 ? var21 : -256 & var21, var4 != null ? (float) (var33 + -Class3_Sub23.method408(var15, (byte) 103, var14, var4, var6, var17)) / var1 : 0.0F);
            }
        } catch (RuntimeException var29) {
            throw ClientErrorException.clientError(var29, "ql.A(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ',' + (var4 != null ? "{...}" : "null") + ',' + (var5 != null ? "{...}" : "null") + ',' + var6 + ',' + (var7 != null ? "{...}" : "null") + ',' + var8 + ',' + var9 + ',' + var10 + ',' + var11 + ',' + (var12 != null ? "{...}" : "null") + ',' + (var13 != null ? "{...}" : "null") + ',' + var14 + ',' + var15 + ',' + (var16 != null ? "{...}" : "null") + ',' + var17 + ')');
        }
    }

    static int method1735() {
        try {
            try {
                if (Class43.worldListStage == 0) {
                    if (TextureOperation25.aLong3411 > TimeUtils.time() + -5000L) {
                        return 0;
                    }

                    Class3_Sub9.aClass64_2318 = Class38.gameSignlink.method1441((byte) 8, GameConfig.IP_MANAGEMENT, GameConfig.WL_PORT);//, Class123.anInt1658);
                    RSInterface.aLong261 = TimeUtils.time();
                    Class43.worldListStage = 1;
                }

                if (30000L + RSInterface.aLong261 < TimeUtils.time()) {
                    return method179((byte) 92, 1000);
                }

                int wlUpdateStamp;
                int var2;
                if (1 == Class43.worldListStage) {
                    if (Class3_Sub9.aClass64_2318.anInt978 == 2) {
                        return method179((byte) 92, 1001);
                    }

                    if (1 != Class3_Sub9.aClass64_2318.anInt978) {
                        return -1;
                    }

                    Class3_Sub15.activeConnection = new Connection((Socket) Class3_Sub9.aClass64_2318.anObject974, Class38.gameSignlink);
                    TextureOperation12.outgoingBuffer.index = 0;
                    Class3_Sub9.aClass64_2318 = null;
                    wlUpdateStamp = 0;
                    if (Class30.loadedWorldList) {
                        wlUpdateStamp = WorldListEntry.updateStamp;
                    }

                    TextureOperation12.outgoingBuffer.writeByte(255);
                    TextureOperation12.outgoingBuffer.writeInt(wlUpdateStamp);
                    Class3_Sub15.activeConnection.sendBytes(TextureOperation12.outgoingBuffer.buffer, TextureOperation12.outgoingBuffer.index);
                    if (null != WorldListEntry.aAudioChannel_2627) {
                        WorldListEntry.aAudioChannel_2627.method2159();
                    }

                    if (null != Class3_Sub21.aAudioChannel_2491) {
                        Class3_Sub21.aAudioChannel_2491.method2159();
                    }

                    var2 = Class3_Sub15.activeConnection.readByte();
                    //System.out.println(var2); //Not sure what this was debugging
                    if (WorldListEntry.aAudioChannel_2627 != null) {
                        WorldListEntry.aAudioChannel_2627.method2159();
                    }

                    if (null != Class3_Sub21.aAudioChannel_2491) {
                        Class3_Sub21.aAudioChannel_2491.method2159();
                    }

                    if (var2 != 0) {
                        return method179((byte) 92, var2);
                    }

                    Class43.worldListStage = 2;
                }

                if (Class43.worldListStage == 2) {
                    if (2 > Class3_Sub15.activeConnection.availableBytes()) {
                        return -1;
                    }

                    Unsorted.wlPacketSize = Class3_Sub15.activeConnection.readByte();
                    Unsorted.wlPacketSize <<= 8;
                    Unsorted.wlPacketSize += Class3_Sub15.activeConnection.readByte();
                    Class43.worldListStage = 3;
                    Class3_Sub20.wlPacketIndex = 0;
                    TextureOperation29.aByteArray3396 = new byte[Unsorted.wlPacketSize];
                }

                if (Class43.worldListStage == 3) {
                    wlUpdateStamp = Class3_Sub15.activeConnection.availableBytes();
                    if (1 > wlUpdateStamp) {
                        return -1;
                    }

                    if (wlUpdateStamp > -Class3_Sub20.wlPacketIndex + Unsorted.wlPacketSize) {
                        wlUpdateStamp = Unsorted.wlPacketSize + -Class3_Sub20.wlPacketIndex;
                    }

                    Class3_Sub15.activeConnection.readBytes(TextureOperation29.aByteArray3396, Class3_Sub20.wlPacketIndex, wlUpdateStamp);
                    Class3_Sub20.wlPacketIndex += wlUpdateStamp;
                    if (Class3_Sub20.wlPacketIndex >= Unsorted.wlPacketSize) {
                        if (TextureOperation39.handleWorldListUpdate(TextureOperation29.aByteArray3396)) {
                            Unsorted.aClass44_Sub1Array3201 = new WorldListEntry[WorldListEntry.activeWorldListSize];
                            var2 = 0;

                            for (int var3 = Class53.worldListOffset; var3 <= WorldListEntry.worldListArraySize; ++var3) {
                                WorldListEntry var4 = Class3_Sub8.getWorld(91, var3);
                                if (null != var4) {
                                    Unsorted.aClass44_Sub1Array3201[var2++] = var4;
                                }
                                //System.out.println("world = " +var4.worldId);
                            }

                            Class3_Sub15.activeConnection.close();
                            Class3_Sub15.activeConnection = null;
                            Unsorted.anInt1088 = 0;
                            Class43.worldListStage = 0;
                            TextureOperation29.aByteArray3396 = null;
                            TextureOperation25.aLong3411 = TimeUtils.time();
                            return 0;
                        }

                        return method179((byte) 92, 1002);
                    }

                    return -1;
                }
            } catch (IOException var5) {
                return method179((byte) 92, 1003);
            }

            return -1;
        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "ql.D(" + 29984 + ')');
        }
    }

    static int method1736(int var0, int var1, int var2, int var3) {
        try {
            if (null == Class44.anIntArrayArrayArray723) {
                return 0;
            } else {
                int var4 = var2 >> 7;
                int var5 = var3 >> 7;
                if (var4 >= 0 && 0 <= var5 && var4 <= 103 && 103 >= var5) {
                    int var7 = 127 & var2;
                    int var8 = var3 & 127;
                    int var6 = var0;
                    if (3 > var0 && (2 & Unsorted.aByteArrayArrayArray113[1][var4][var5]) == 2) {
                        var6 = var0 + 1;
                    }

                    int var10 = var7 * Class44.anIntArrayArrayArray723[var6][var1 + var4][1 + var5] + Class44.anIntArrayArrayArray723[var6][var4][var5 + 1] * (-var7 + 128) >> 7;
                    int var9 = var7 * Class44.anIntArrayArrayArray723[var6][var4 + 1][var5] + (-var7 + 128) * Class44.anIntArrayArrayArray723[var6][var4][var5] >> 7;
                    return var8 * var10 + (128 - var8) * var9 >> 7;
                } else {
                    return 0;
                }
            }
        } catch (RuntimeException var11) {
            throw ClientErrorException.clientError(var11, "ql.B(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
        }
    }

    static int method179(byte var0, int var1) {
       try {
          if (var0 == 92) {
             if (null != Class3_Sub15.activeConnection) {
                Class3_Sub15.activeConnection.close();
                Class3_Sub15.activeConnection = null;
             }

             ++Unsorted.anInt1088;
             if (Unsorted.anInt1088 > 4) {
                Class43.worldListStage = 0;
                Unsorted.anInt1088 = 0;
                return var1;
             } else {
                Class43.worldListStage = 0;
                if (Class123.anInt1658 == Client.anInt3773) {
                   Class123.anInt1658 = Class53.anInt867;
                } else {
                   Class123.anInt1658 = Client.anInt3773;
                }

                return -1;
             }
          } else {
             return 122;
          }
       } catch (RuntimeException var3) {
          throw ClientErrorException.clientError(var3, "an.F(" + var0 + ',' + var1 + ')');
       }
    }
}
