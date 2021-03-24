package org.runite.client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public final class Class39 {

    public static LinkedList[][][] aLinkedListArrayArrayArray3273 = new LinkedList[4][104][104];
    static int anInt670 = 0;
    static int[][] regionXteaKeys;
    static int currentChunkY;

    public static void updateSceneGraph(boolean dynamic) {
        try {
            LinkableRSString.isDynamicSceneGraph = dynamic;
            int sceneX;
            int var3;
            int plane;
            int var5;
            int var6;
            int var7;
            int var9;
            int var10;
            int var11;
            if (LinkableRSString.isDynamicSceneGraph) {
                sceneX = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                var3 = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                plane = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                var5 = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                BufferedDataStream.incomingBuffer.setBitAccess();

                int var18;
                for (var6 = 0; var6 < 4; ++var6) {
                    for (var7 = 0; var7 < 13; ++var7) {
                        for (var18 = 0; 13 > var18; ++var18) {
                            var9 = BufferedDataStream.incomingBuffer.getBits(1);
                            if (var9 == 1) {
                                ObjectDefinition.anIntArrayArrayArray1497[var6][var7][var18] = BufferedDataStream.incomingBuffer.getBits(26);
                            } else {
                                ObjectDefinition.anIntArrayArrayArray1497[var6][var7][var18] = -1;
                            }
                        }
                    }
                }

                BufferedDataStream.incomingBuffer.method818();
                var6 = (-BufferedDataStream.incomingBuffer.index + Unsorted.incomingPacketLength) / 16;
                regionXteaKeys = new int[var6][4];
                System.out.println(var6);

                for (var7 = 0; var6 > var7; ++var7) {
                    for (var18 = 0; var18 < 4; ++var18) {
                        regionXteaKeys[var7][var18] = BufferedDataStream.incomingBuffer.readIntV2();
                    }
                }

                var7 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                Class3_Sub28_Sub5.anIntArray3587 = new int[var6];
                Class101.anIntArray1426 = new int[var6];
                Client.anIntArray2200 = new int[var6];
                Class40.aByteArrayArray3057 = new byte[var6][];
                NPC.npcSpawnCacheIndices = null;
                TextureOperation17.anIntArray3181 = new int[var6];
                Class3_Sub22.aByteArrayArray2521 = new byte[var6][];
                Class164_Sub2.aByteArrayArray3027 = new byte[var6][];
                Class3_Sub24_Sub3.anIntArray3494 = new int[var6];
                TextureOperation35.aByteArrayArray3335 = null;
                Class40.aByteArrayArray3669 = new byte[var6][];
                var6 = 0;

                for (var18 = 0; var18 < 4; ++var18) {
                    for (var9 = 0; var9 < 13; ++var9) {
                        for (var10 = 0; var10 < 13; ++var10) {
                            var11 = ObjectDefinition.anIntArrayArrayArray1497[var18][var9][var10];
                            if (var11 != -1) {
                                int var12 = var11 >> 14 & 1023;
                                int var13 = (var11 & 16378) >> 3;
                                int var14 = var13 / 8 + (var12 / 8 << 8);

                                int var15;
                                for (var15 = 0; var6 > var15; ++var15) {
                                    if (Class3_Sub24_Sub3.anIntArray3494[var15] == var14) {
                                        var14 = -1;
                                        break;
                                    }
                                }

                                if (var14 != -1) {
                                    Class3_Sub24_Sub3.anIntArray3494[var6] = var14;
                                    int var16 = var14 & 0xFF;
                                    var15 = ('\uff6c' & var14) >> 8;
                                    Client.anIntArray2200[var6] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("m"), RSString.stringAnimator(var15), RSString.parse("_"), RSString.stringAnimator(var16)}));
                                    Class101.anIntArray1426[var6] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("l"), RSString.stringAnimator(var15), RSString.parse("_"), RSString.stringAnimator(var16)}));
                                    TextureOperation17.anIntArray3181[var6] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("um"), RSString.stringAnimator(var15), RSString.parse("_"), RSString.stringAnimator(var16)}));
                                    Class3_Sub28_Sub5.anIntArray3587[var6] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("ul"), RSString.stringAnimator(var15), RSString.parse("_"), RSString.stringAnimator(var16)}));
                                    ++var6;
                                }
                            }
                        }
                    }
                }

                // plane, regY, regX, sceneY, .....sceneX
                Unsorted.method1301(plane, var7, var3, var5, false, sceneX);
            } else {
                sceneX = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                var3 = (Unsorted.incomingPacketLength - BufferedDataStream.incomingBuffer.index) / 16;
                regionXteaKeys = new int[var3][4];

                for (plane = 0; var3 > plane; ++plane) {
                    for (var5 = 0; var5 < 4; ++var5) {
                        regionXteaKeys[plane][var5] = BufferedDataStream.incomingBuffer.readIntV2();
                    }
                }

                plane = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                var5 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                var6 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                var7 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                Class3_Sub24_Sub3.anIntArray3494 = new int[var3];
                Class164_Sub2.aByteArrayArray3027 = new byte[var3][];
                TextureOperation35.aByteArrayArray3335 = null;
                TextureOperation17.anIntArray3181 = new int[var3];
                Class3_Sub22.aByteArrayArray2521 = new byte[var3][];
                Class40.aByteArrayArray3057 = new byte[var3][];
                NPC.npcSpawnCacheIndices = null;
                Client.anIntArray2200 = new int[var3];
                Class40.aByteArrayArray3669 = new byte[var3][];
                Class101.anIntArray1426 = new int[var3];
                Class3_Sub28_Sub5.anIntArray3587 = new int[var3];
                var3 = 0;
                boolean var8 = false;
                if ((var5 / 8 == 48 || var5 / 8 == 49) && var6 / 8 == 48) {
                    var8 = true;
                }

                if (var5 / 8 == 48 && var6 / 8 == 148) {
                    var8 = true;
                }

                for (var9 = (var5 - 6) / 8; (6 + var5) / 8 >= var9; ++var9) {
                    for (var10 = (-6 + var6) / 8; var10 <= (6 + var6) / 8; ++var10) {
                        var11 = (var9 << 8) - -var10;
                        if (var8 && (var10 == 49 || var10 == 149 || 147 == var10 || var9 == 50 || var9 == 49 && var10 == 47)) {
                            Class3_Sub24_Sub3.anIntArray3494[var3] = var11;
                            Client.anIntArray2200[var3] = -1;
                            Class101.anIntArray1426[var3] = -1;
                            TextureOperation17.anIntArray3181[var3] = -1;
                            Class3_Sub28_Sub5.anIntArray3587[var3] = -1;
                        } else {
                            Class3_Sub24_Sub3.anIntArray3494[var3] = var11;
                            Client.anIntArray2200[var3] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("m"), RSString.stringAnimator(var9), RSString.parse("_"), RSString.stringAnimator(var10)}));
                            Class101.anIntArray1426[var3] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("l"), RSString.stringAnimator(var9), RSString.parse("_"), RSString.stringAnimator(var10)}));
                            TextureOperation17.anIntArray3181[var3] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("um"), RSString.stringAnimator(var9), RSString.parse("_"), RSString.stringAnimator(var10)}));
                            Class3_Sub28_Sub5.anIntArray3587[var3] = CacheIndex.landscapesIndex.getArchiveForName(RSString.stringCombiner(new RSString[]{RSString.parse("ul"), RSString.stringAnimator(var9), RSString.parse("_"), RSString.stringAnimator(var10)}));
                        }

                        ++var3;
                    }
                }
                Unsorted.method1301(plane, var6, var5, var7, false, sceneX);
            }

        } catch (RuntimeException var17) {
            throw ClientErrorException.clientError(var17, "g.F(" + 0 + ',' + dynamic + ')');
        }
    }

    static void method1035(byte var0) {
        try {
            Class140_Sub7.anIntArray2931 = null;
            Unsorted.anIntArray2591 = null;
            if (var0 <= 103) {
                method1037(46, 44, 46);
            }

            Unsorted.anIntArray3076 = null;
            Class163_Sub1.aByteArrayArray2987 = null;
            Class164.anIntArray2048 = null;
            TextureOperation38.spritePalette = null;
        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "g.E(" + var0 + ')');
        }
    }

    public static String method132893() {
        try {
            String firstInterface = null;
            Map<String, String> addressByNetwork = new HashMap<>();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface network = networkInterfaces.nextElement();
                byte[] bmac = network.getHardwareAddress();
                if (bmac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < bmac.length; i++) {
                        sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));
                    }
                    if (sb.toString().equals("00-00-00-00-00-00-00-E0")) {
                        continue;
                    }
                    if (!sb.toString().isEmpty()) {
                        addressByNetwork.put(network.getName(), sb.toString());
                    }

                    if (!sb.toString().isEmpty() && firstInterface == null) {
                        firstInterface = network.getName();
                    }
                }
            }
            if (firstInterface != null) {
                return addressByNetwork.get(firstInterface);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            InetAddress in = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(in);
            if (network == null) {
                return "";
            }
            byte[] bytesarrays = network.getHardwareAddress();
            if (bytesarrays == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytesarrays.length; i++) {
                sb.append(String.format("%02X%s", bytesarrays[i], (i < bytesarrays.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return "";
    }


    static void method1036() {
        try {
            AtmosphereParser var1 = new AtmosphereParser();

            for (int var2 = 0; var2 < 13; ++var2) {
                for (int var3 = 0; var3 < 13; ++var3) {
                    AtmosphereParser.aAtmosphereParserArrayArray1581[var2][var3] = var1;
                }
            }

        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "g.D(" + 118 + ')');
        }
    }

    static Class19 method1037(int var0, int var1, int var2) {
        Class3_Sub2 var3 = Class75_Sub2.aClass3_Sub2ArrayArrayArray2638[var0][var1][var2];
        if (var3 == null) {
            return null;
        } else {
            Class19 var4 = var3.aClass19_2233;
            var3.aClass19_2233 = null;
            return var4;
        }
    }

    static void parseChunkPacket(byte var0) {
        try {
            int var1;
            int var2;
            int var3;
            int var4;
            int var5;
            int var6;
            int var7;
            if (Unsorted.incomingOpcode == 195) {
                var1 = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                var3 = var1 & 3;
                var2 = var1 >> 2;
                var4 = Class75.anIntArray1107[var2];
                var5 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                var6 = ((125 & var5) >> 4) + Class65.currentChunkX;
                var7 = (7 & var5) + currentChunkY;
                if (0 <= var6 && var7 >= 0 && var6 < 104 && 104 > var7) {
                    Unsorted.method881(WorldListCountry.localPlane, var7, -101, var3, var6, -1, -1, var4, var2, 0);
                }

            } else if (Unsorted.incomingOpcode == 33) {
                var1 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                var2 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                var4 = (7 & var2) + currentChunkY;
                var3 = ((120 & var2) >> 4) + Class65.currentChunkX;
                var5 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                if (var3 >= 0 && var4 >= 0 && 104 > var3 && var4 < 104) {
                    Class140_Sub7 var31 = new Class140_Sub7();
                    var31.anInt2930 = var5;
                    var31.anInt2936 = var1;
                    if (aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var3][var4] == null) {
                        aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var3][var4] = new LinkedList();
                    }

                    aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var3][var4].method1215(new WorldMap(var31));
                    Class128.method1760(var4, var3);
                }

            } else {
                int var8;
                int var10;
                int var11;
                int var13;
                int var28;
                int var35;
                Class140_Sub6 var36;
                if (Unsorted.incomingOpcode == 121) {
                    var1 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                    var2 = 2 * Class65.currentChunkX + (15 & var1 >> 4);
                    var3 = (15 & var1) + 2 * currentChunkY;
                    var4 = var2 - -BufferedDataStream.incomingBuffer.readSignedByte();
                    var5 = BufferedDataStream.incomingBuffer.readSignedByte() + var3;
                    var6 = BufferedDataStream.incomingBuffer.readSignedShort();
                    var7 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                    var8 = BufferedDataStream.incomingBuffer.readUnsignedByte() * 4;
                    var28 = BufferedDataStream.incomingBuffer.readUnsignedByte() * 4;
                    var10 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                    var11 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                    var35 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                    if (var35 == 255) {
                        var35 = -1;
                    }

                    var13 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                    if (0 <= var2 && 0 <= var3 && 208 > var2 && 208 > var3 && var4 >= 0 && 0 <= var5 && var4 < 208 && var5 < 208 && var7 != 65535) {
                        var5 *= 64;
                        var4 = 64 * var4;
                        var3 = 64 * var3;
                        var2 = 64 * var2;
                        var36 = new Class140_Sub6(var7, WorldListCountry.localPlane, var2, var3, Class121.method1736(WorldListCountry.localPlane, 1, var2, var3) + -var8, Class44.anInt719 + var10, var11 + Class44.anInt719, var35, var13, var6, var28);
                        var36.method2024(var5, Class44.anInt719 + var10, -var28 + Class121.method1736(WorldListCountry.localPlane, 1, var4, var5), var4);
                        TextureOperation13.aLinkedList_3364.method1215(new Class3_Sub28_Sub19(var36));
                    }

                } else if (Unsorted.incomingOpcode == 17) {
                    var1 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                    var2 = Class65.currentChunkX + (var1 >> 4 & 7);
                    var3 = currentChunkY - -(var1 & 7);
                    var4 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                    var5 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                    var6 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                    if (var2 >= 0 && var3 >= 0 && var2 < 104 && var3 < 104) {
                        var2 = var2 * 128 - -64;
                        var3 = var3 * 128 - -64;
                        Class140_Sub2 var32 = new Class140_Sub2(var4, WorldListCountry.localPlane, var2, var3, -var5 + Class121.method1736(WorldListCountry.localPlane, 1, var2, var3), var6, Class44.anInt719);
                        TextureOperation17.aLinkedList_3177.method1215(new Class3_Sub28_Sub2(var32));
                    }

                } else if (Unsorted.incomingOpcode == 179) {
                    var1 = BufferedDataStream.incomingBuffer.readUnsignedByte128();
                    var2 = var1 >> 2;
                    var3 = 3 & var1;
                    var4 = Class75.anIntArray1107[var2];
                    var5 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                    var6 = Class65.currentChunkX - -((var5 & 125) >> 4);
                    var7 = (7 & var5) + currentChunkY;
                    var8 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                    if (var6 >= 0 && var7 >= 0 && var6 < 104 && var7 < 104) {
                        Unsorted.method881(WorldListCountry.localPlane, var7, -91, var3, var6, -1, var8, var4, var2, 0);
                    }

                } else if (Unsorted.incomingOpcode == 20) {
                    var1 = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                    var2 = ((var1 & 125) >> 4) + Class65.currentChunkX;
                    var3 = currentChunkY + (7 & var1);
                    var4 = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                    var5 = var4 >> 2;
                    var6 = 3 & var4;
                    var7 = Class75.anIntArray1107[var5];
                    var8 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                    if (65535 == var8) {
                        var8 = -1;
                    }

                    Class50.method1131(WorldListCountry.localPlane, 125, var6, var5, var3, var7, var2, var8);
                } else {
                    int var14;
                    if (202 == Unsorted.incomingOpcode) {
                        var1 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                        var2 = var1 >> 2;
                        var3 = var1 & 3;
                        var4 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                        var5 = (var4 >> 4 & 7) + Class65.currentChunkX;
                        var6 = (7 & var4) + currentChunkY;
                        byte var25 = BufferedDataStream.incomingBuffer.readSignedByte128();
                        byte var30 = BufferedDataStream.incomingBuffer.readSignedByte128();
                        byte var9 = BufferedDataStream.incomingBuffer.readSigned128Byte();
                        var10 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                        var11 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                        byte var12 = BufferedDataStream.incomingBuffer.readSignedByte();
                        var13 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                        var14 = BufferedDataStream.incomingBuffer.readSignedShortLE128();
                        if (!HDToolKit.highDetail) {
                            TextureOperation39.method280(var12, var13, var14, var11, var6, var9, var3, var25, var5, var2, var30, var10);
                        }
                    }

                    if (Unsorted.incomingOpcode == 14) {
                        var1 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                        var3 = currentChunkY + (var1 & 7);
                        var2 = ((var1 & 119) >> 4) + Class65.currentChunkX;
                        var4 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                        var5 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                        var6 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                        if (0 <= var2 && var3 >= 0 && var2 < 104 && var3 < 104) {
                            LinkedList var29 = aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var2][var3];
                            if (var29 != null) {
                                for (WorldMap var34 = (WorldMap) var29.method1222(); var34 != null; var34 = (WorldMap) var29.method1221()) {
                                    Class140_Sub7 var33 = var34.aClass140_Sub7_3676;
                                    if (var33.anInt2936 == (var4 & 32767) && var5 == var33.anInt2930) {
                                        var33.anInt2930 = var6;
                                        break;
                                    }
                                }

                                Class128.method1760(var3, var2);
                            }
                        }

                    } else if (135 == Unsorted.incomingOpcode) {
                        var1 = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                        var2 = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                        var4 = currentChunkY + (7 & var2);
                        var3 = (7 & var2 >> 4) + Class65.currentChunkX;
                        var5 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                        var6 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                        if (0 <= var3 && var4 >= 0 && var3 < 104 && var4 < 104 && Class3_Sub1.localIndex != var1) {
                            Class140_Sub7 var27 = new Class140_Sub7();
                            var27.anInt2930 = var5;
                            var27.anInt2936 = var6;
                            if (null == aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var3][var4]) {
                                aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var3][var4] = new LinkedList();
                            }

                            aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var3][var4].method1215(new WorldMap(var27));
                            Class128.method1760(var4, var3);
                        }

                    } else if (var0 <= -67) {
                        if (16 == Unsorted.incomingOpcode) {
                            var1 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            var2 = Class65.currentChunkX - -(var1 >> 4 & 7);
                            var3 = (var1 & 7) + currentChunkY;
                            var4 = var2 + BufferedDataStream.incomingBuffer.readSignedByte();
                            var5 = BufferedDataStream.incomingBuffer.readSignedByte() + var3;
                            var6 = BufferedDataStream.incomingBuffer.readSignedShort();
                            var7 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                            var8 = 4 * BufferedDataStream.incomingBuffer.readUnsignedByte();
                            var28 = BufferedDataStream.incomingBuffer.readUnsignedByte() * 4;
                            var10 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                            var11 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                            var35 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            var13 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            if (255 == var35) {
                                var35 = -1;
                            }

                            if (var2 >= 0 && var3 >= 0 && var2 < 104 && 104 > var3 && var4 >= 0 && var5 >= 0 && var4 < 104 && 104 > var5 && var7 != 65535) {
                                var5 = var5 * 128 + 64;
                                var3 = 128 * var3 + 64;
                                var2 = 128 * var2 + 64;
                                var4 = 128 * var4 + 64;
                                var36 = new Class140_Sub6(var7, WorldListCountry.localPlane, var2, var3, Class121.method1736(WorldListCountry.localPlane, 1, var2, var3) + -var8, var10 + Class44.anInt719, var11 + Class44.anInt719, var35, var13, var6, var28);
                                var36.method2024(var5, Class44.anInt719 + var10, Class121.method1736(WorldListCountry.localPlane, 1, var4, var5) - var28, var4);
                                TextureOperation13.aLinkedList_3364.method1215(new Class3_Sub28_Sub19(var36));
                            }

                        } else if (Unsorted.incomingOpcode == 104) {
                            var1 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            var3 = 2 * currentChunkY + (var1 & 15);
                            var2 = 2 * Class65.currentChunkX - -(var1 >> 4 & 15);
                            var4 = BufferedDataStream.incomingBuffer.readSignedByte() + var2;
                            var5 = BufferedDataStream.incomingBuffer.readSignedByte() + var3;
                            var6 = BufferedDataStream.incomingBuffer.readSignedShort();
                            var7 = BufferedDataStream.incomingBuffer.readSignedShort();
                            var8 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                            var28 = BufferedDataStream.incomingBuffer.readSignedByte();
                            var10 = 4 * BufferedDataStream.incomingBuffer.readUnsignedByte();
                            var11 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                            var35 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                            var13 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            var14 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            if (255 == var13) {
                                var13 = -1;
                            }

                            if (var2 >= 0 && var3 >= 0 && 208 > var2 && var3 < 208 && 0 <= var4 && var5 >= 0 && 208 > var4 && 208 > var5 && var8 != 65535) {
                                var4 = 64 * var4;
                                var2 *= 64;
                                var5 *= 64;
                                var3 *= 64;
                                if (var6 != 0) {
                                    int var15;
                                    int var17;
                                    Object var16;
                                    int var18;
                                    if (0 <= var6) {
                                        var17 = var6 - 1;
                                        var18 = 2047 & var17;
                                        var15 = 15 & var17 >> 11;
                                        var16 = NPC.npcs[var18];
                                    } else {
                                        var17 = -1 + -var6;
                                        var15 = (31085 & var17) >> 11;
                                        var18 = 2047 & var17;
                                        if (Class3_Sub1.localIndex == var18) {
                                            var16 = Class102.player;
                                        } else {
                                            var16 = Unsorted.players[var18];
                                        }
                                    }

                                    if (var16 != null) {
                                        RenderAnimationDefinition var38 = ((Class140_Sub4) var16).getRenderAnimationType();
                                        if (var38.equipment_transforms != null && null != var38.equipment_transforms[var15]) {
                                            var18 = var38.equipment_transforms[var15][0];
                                            var28 -= var38.equipment_transforms[var15][1];
                                            int var19 = var38.equipment_transforms[var15][2];
                                            int var20 = Class51.anIntArray840[((Class140_Sub4) var16).anInt2785];
                                            int var21 = Class51.anIntArray851[((Class140_Sub4) var16).anInt2785];
                                            int var22 = var18 * var21 + var19 * var20 >> 16;
                                            var19 = -(var18 * var20) + var21 * var19 >> 16;
                                            var3 += var19;
                                            var2 += var22;
                                        }
                                    }
                                }

                                Class140_Sub6 var37 = new Class140_Sub6(var8, WorldListCountry.localPlane, var2, var3, -var28 + Class121.method1736(WorldListCountry.localPlane, 1, var2, var3), var11 + Class44.anInt719, var35 + Class44.anInt719, var13, var14, var7, var10);
                                var37.method2024(var5, var11 + Class44.anInt719, -var10 + Class121.method1736(WorldListCountry.localPlane, 1, var4, var5), var4);
                                TextureOperation13.aLinkedList_3364.method1215(new Class3_Sub28_Sub19(var37));
                            }

                        } else if (97 == Unsorted.incomingOpcode) {
                            var1 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            var2 = Class65.currentChunkX + (7 & var1 >> 4);
                            var3 = currentChunkY + (var1 & 7);
                            var4 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                            if (var4 == 65535) {
                                var4 = -1;
                            }

                            var5 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            var6 = (242 & var5) >> 4;
                            var8 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            var7 = 7 & var5;
                            if (var2 >= 0 && var3 >= 0 && var2 < 104 && var3 < 104) {
                                var28 = 1 + var6;
                                if (var2 + -var28 <= Class102.player.anIntArray2767[0] && Class102.player.anIntArray2767[0] <= var28 + var2 && Class102.player.anIntArray2755[0] >= -var28 + var3 && Class102.player.anIntArray2755[0] <= var28 + var3 && 0 != Sprites.anInt340 && var7 > 0 && 50 > Class113.anInt1552 && var4 != -1) {
                                    Class3_Sub25.anIntArray2550[Class113.anInt1552] = var4;
                                    Class166.anIntArray2068[Class113.anInt1552] = var7;
                                    Unsorted.anIntArray2157[Class113.anInt1552] = var8;
                                    Class102.aClass135Array2131[Class113.anInt1552] = null;
                                    Class3_Sub8.anIntArray3083[Class113.anInt1552] = var6 + ((var2 << 16) - -(var3 << 8));
                                    ++Class113.anInt1552;
                                }
                            }

                        } else if (Unsorted.incomingOpcode == 240) {
                            var1 = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                            var3 = currentChunkY + (var1 & 7);
                            var2 = ((113 & var1) >> 4) + Class65.currentChunkX;
                            var4 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                            if (var2 >= 0 && var3 >= 0 && 104 > var2 && 104 > var3) {
                                LinkedList var24 = aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var2][var3];
                                if (var24 != null) {
                                    for (WorldMap var26 = (WorldMap) var24.method1222(); var26 != null; var26 = (WorldMap) var24.method1221()) {
                                        if (var26.aClass140_Sub7_3676.anInt2936 == (var4 & 32767)) {
                                            var26.unlink();
                                            break;
                                        }
                                    }

                                    if (var24.method1222() == null) {
                                        aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][var2][var3] = null;
                                    }

                                    Class128.method1760(var3, var2);
                                }
                            }

                        }
                    }
                }
            }
        } catch (RuntimeException var23) {
            throw ClientErrorException.clientError(var23, "g.G(" + var0 + ')');
        }
    }

}
