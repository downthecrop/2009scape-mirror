package org.runite.client;

import org.rs09.client.config.GameConfig;

import org.rs09.client.net.Connection;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class LoginHandler {

    static CacheIndex aClass153_1680;
    static boolean dynamic;
    public static int loginStage = 0;
    static long isaacServerKey = 0L;

    static void handleLogin() {
        try {
            if (0 != loginStage && 5 != loginStage) {
                try {
                    if (++Class50.anInt820 > 2000) {
                        if (Class3_Sub15.activeConnection != null) {
                            Class3_Sub15.activeConnection.close();
                            Class3_Sub15.activeConnection = null;
                        }

                        if (Class166.anInt2079 >= 1) {
                            Client.messageToDisplay = -5;
                            loginStage = 0;
                            return;
                        }

                        Class50.anInt820 = 0;
                        if (Class140_Sub6.accRegistryPort == Class162.anInt2036) {
                            Class140_Sub6.accRegistryPort = Client.currentPort;
                        } else {
                            Class140_Sub6.accRegistryPort = Class162.anInt2036;
                        }

                        loginStage = 1;
                        ++Class166.anInt2079;
                    }
                    if (loginStage == 1) {
                        Class3_Sub9.aClass64_2318 = Class38.signlink.method1441((byte) 8, Class38_Sub1.accRegistryIp, GameConfig.SERVER_PORT + GameConfig.WORLD);//Class140_Sub6.accRegistryPort);
                        loginStage = 2;
                    }

                    if (loginStage == 2) {
                        if (Objects.requireNonNull(Class3_Sub9.aClass64_2318).anInt978 == 2) {
                            throw new IOException();
                        }

                        if (1 != Class3_Sub9.aClass64_2318.anInt978) {
                            return;
                        }

                        Class3_Sub15.activeConnection = new Connection((Socket) Class3_Sub9.aClass64_2318.anObject974, Class38.signlink);
                        Class3_Sub9.aClass64_2318 = null;
                        long var1 = PacketParser.aLong3202 = Class131.username.toLong();
                        Class3_Sub13_Sub1.outgoingBuffer.index = 0;
                        Class3_Sub13_Sub1.outgoingBuffer.writeByte(14);
                        int nameHash = (int) (var1 >> 16 & 31L);
                        Class3_Sub13_Sub1.outgoingBuffer.writeByte(nameHash);
                        Class3_Sub15.activeConnection.sendBytes(Class3_Sub13_Sub1.outgoingBuffer.buffer, 2);
                        if (WorldListEntry.aClass155_2627 != null) {
                            WorldListEntry.aClass155_2627.method2159(106);
                        }

                        if (Class3_Sub21.aClass155_2491 != null) {
                            Class3_Sub21.aClass155_2491.method2159(79);
                        }

                        int var4 = Class3_Sub15.activeConnection.readByte();
                        if (WorldListEntry.aClass155_2627 != null) {
                            WorldListEntry.aClass155_2627.method2159(68);
                        }

                        if (null != Class3_Sub21.aClass155_2491) {
                            Class3_Sub21.aClass155_2491.method2159(109);
                        }

                        if (var4 != 0) {
                            Client.messageToDisplay = var4;
                            loginStage = 0;
                            Class3_Sub15.activeConnection.close();
                            Class3_Sub15.activeConnection = null;
                            return;
                        }

                        loginStage = 3;
                    }

                    if (loginStage == 3) {
                        if (Class3_Sub15.activeConnection.availableBytes() < 8) {
                            return;
                        }

                        Class3_Sub15.activeConnection.readBytes(GraphicDefinition.incomingBuffer.buffer, 0, 8);
                        GraphicDefinition.incomingBuffer.index = 0;
                        isaacServerKey = GraphicDefinition.incomingBuffer.readLong();
                        int[] var9 = new int[4];
                        Class3_Sub13_Sub1.outgoingBuffer.index = 0;
                        var9[2] = (int) (isaacServerKey >> 32);
                        var9[3] = (int) isaacServerKey;
                        var9[1] = (int) (Math.random() * 9.9999999E7D);
                        var9[0] = (int) (Math.random() * 9.9999999E7D);
                        Class3_Sub13_Sub1.outgoingBuffer.writeByte(10);
                        Class3_Sub13_Sub1.outgoingBuffer.writeInt(var9[0]);
                        Class3_Sub13_Sub1.outgoingBuffer.writeInt(var9[1]);
                        Class3_Sub13_Sub1.outgoingBuffer.writeInt(var9[2]);
                        Class3_Sub13_Sub1.outgoingBuffer.writeInt(var9[3]);
                        Class3_Sub13_Sub1.outgoingBuffer.writeLong(Class131.username.toLong());
                        Class3_Sub13_Sub1.outgoingBuffer.writeString(Class131.password);
                        Class3_Sub13_Sub1.method229();
                        Class3_Sub13_Sub1.outgoingBuffer.rsaEncrypt(Class3_Sub13_Sub37.EXPONENT, Class3_Sub13_Sub14.MODULUS);
                        Unsorted.aClass3_Sub30_Sub1_2942.index = 0;
                        if (40 == Class143.gameStage) {
                            Unsorted.aClass3_Sub30_Sub1_2942.writeByte(18);
                        } else {
                            Unsorted.aClass3_Sub30_Sub1_2942.writeByte(16);
                        }

                        Unsorted.aClass3_Sub30_Sub1_2942.writeShort(Class3_Sub13_Sub1.outgoingBuffer.index + 163 - -Class3_Sub13_Sub33.method326((byte) 111, Class163_Sub2.paramSettings));
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(GameConfig.CLIENT_BUILD);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeByte(Class7.anInt2161);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeByte(!Client.paramAdvertisementSuppressed ? 0 : 1);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeByte(1);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeByte(Class83.getWindowType());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeShort(Class23.canvasWidth);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeShort(Class140_Sub7.canvasHeight);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeByte(Unsorted.anInt3671);
                        Class81.putRandomDataFile(Unsorted.aClass3_Sub30_Sub1_2942, true);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeString(Class163_Sub2.paramSettings);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(Class3_Sub26.paramAffid);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(Class84.method1421());
                        CS2Script.aBoolean2705 = true;
                        Unsorted.aClass3_Sub30_Sub1_2942.writeShort(Class113.interfacePacketCounter);
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.skeletonsIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.skinsIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.configurationsIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.interfacesIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.soundFXIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.landscapesIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.musicIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.modelsIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.spritesIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.texturesIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.huffmanEncodingIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.music2Index.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.interfaceScriptsIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.fontsIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.soundFX2Index.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.soundFX3Index.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.objectConfigIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.clientscriptMaskIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.npcConfigIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.itemConfigIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.animationIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.graphicFXIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.clientScriptConfigIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.worldmapIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.quickchatMessagesIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.quickchatMenusIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.materialsIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.particlesConfigIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.writeInt(CacheIndex.libIndex.getReferenceTableCrc());
                        Unsorted.aClass3_Sub30_Sub1_2942.putBytes(Class3_Sub13_Sub1.outgoingBuffer.buffer, Class3_Sub13_Sub1.outgoingBuffer.index);
                        Class3_Sub15.activeConnection.sendBytes(Unsorted.aClass3_Sub30_Sub1_2942.buffer, Unsorted.aClass3_Sub30_Sub1_2942.index);
                        Class3_Sub13_Sub1.outgoingBuffer.method814(var9);

                        for (int var2 = 0; var2 < 4; ++var2) {
                            var9[var2] += 50;
                        }

                        GraphicDefinition.incomingBuffer.method814(var9);
                        loginStage = 4;
                    }

                    if (loginStage == 4) {
                        if (Class3_Sub15.activeConnection.availableBytes() < 1) {
                            return;
                        }

                        int opcode = Class3_Sub15.activeConnection.readByte();
                        if (opcode == 21) {
                            loginStage = 7;
                        } else if (opcode == 29) {
                            loginStage = 10;
                        } else {
                            if (opcode == 1) {
                                loginStage = 5;
                                Client.messageToDisplay = opcode;
                                return;
                            }

                            if (2 != opcode) {
                                if (opcode != 15) {
                                    if (23 == opcode && Class166.anInt2079 < 1) {
                                        loginStage = 1;
                                        ++Class166.anInt2079;
                                        Class50.anInt820 = 0;
                                        Class3_Sub15.activeConnection.close();
                                        Class3_Sub15.activeConnection = null;
                                        return;
                                    }

                                    Client.messageToDisplay = opcode;
                                    loginStage = 0;
                                    Class3_Sub15.activeConnection.close();
                                    Class3_Sub15.activeConnection = null;
                                    return;
                                }

                                loginStage = 0;
                                Client.messageToDisplay = opcode;
                                return;
                            }

                            loginStage = 8;
                        }
                    }

                    if (6 == loginStage) {
                        Class3_Sub13_Sub1.outgoingBuffer.index = 0;
                        Class3_Sub13_Sub1.outgoingBuffer.putOpcode(17);
                        Class3_Sub15.activeConnection.sendBytes(Class3_Sub13_Sub1.outgoingBuffer.buffer, Class3_Sub13_Sub1.outgoingBuffer.index);
                        loginStage = 4;
                        return;
                    }

                    if (loginStage == 7) {
                        if (Class3_Sub15.activeConnection.availableBytes() >= 1) {
                            Class3_Sub13_Sub34.anInt3413 = 60 * (3 + Class3_Sub15.activeConnection.readByte());
                            loginStage = 0;
                            Client.messageToDisplay = 21;
                            Class3_Sub15.activeConnection.close();
                            Class3_Sub15.activeConnection = null;
                            return;
                        }

                        return;
                    }

                    if (loginStage == 10) {
                        if (1 <= Class3_Sub15.activeConnection.availableBytes()) {
                            Class3_Sub26.anInt2561 = Class3_Sub15.activeConnection.readByte();
                            loginStage = 0;
                            Client.messageToDisplay = 29;
                            Class3_Sub15.activeConnection.close();
                            Class3_Sub15.activeConnection = null;
                            return;
                        }

                        return;
                    }

                    if (loginStage == 8) {
                        if (Class3_Sub15.activeConnection.availableBytes() < 14) {
                            return;
                        }

                        Class3_Sub15.activeConnection.readBytes(GraphicDefinition.incomingBuffer.buffer, 0, 14);
                        GraphicDefinition.incomingBuffer.index = 0;
                        Player.rights = GraphicDefinition.incomingBuffer.readUnsignedByte();
                        if (Player.rights == 2) {
                            ClientLoader.setModPanelVisible(true);
                        } else {
                            ClientLoader.setModPanelVisible(false);
                        }
                        CS2Script.anInt3775 = GraphicDefinition.incomingBuffer.readUnsignedByte();
                        Class3_Sub15.aBoolean2433 = GraphicDefinition.incomingBuffer.readUnsignedByte() == 1;
                        Class121.aBoolean1641 = 1 == GraphicDefinition.incomingBuffer.readUnsignedByte();
                        Unsorted.aBoolean4063 = GraphicDefinition.incomingBuffer.readUnsignedByte() == 1;
                        Class3_Sub13_Sub14.aBoolean3166 = 1 == GraphicDefinition.incomingBuffer.readUnsignedByte();
                        Unsorted.aBoolean29 = GraphicDefinition.incomingBuffer.readUnsignedByte() == 1;
                        Class3_Sub1.localIndex = GraphicDefinition.incomingBuffer.readUnsignedShort();
                        Class3_Sub13_Sub29.disableGEBoxes = GraphicDefinition.incomingBuffer.readUnsignedByte() == 1;
                        Unsorted.isMember = GraphicDefinition.incomingBuffer.readUnsignedByte() == 1;
                        Class113.method1702(Unsorted.isMember);
                        Class8.method845(Unsorted.isMember);
                        if (!Client.paramAdvertisementSuppressed) {
                            if ((!Class3_Sub15.aBoolean2433 || Unsorted.aBoolean4063) && !Class3_Sub13_Sub29.disableGEBoxes) {
                                try {
                                    TextCore.aClass94_516.method1577(Class38.signlink.gameApplet);
                                } catch (Throwable var5) {
                                }
                            } else {
                                try {
                                    Class97.aClass94_1374.method1577(Class38.signlink.gameApplet);
                                } catch (Throwable var6) {
                                }
                            }
                        }

                        Unsorted.incomingOpcode = GraphicDefinition.incomingBuffer.getOpcode();
                        dynamic = Unsorted.incomingOpcode == 214;
                        Unsorted.incomingPacketLength = GraphicDefinition.incomingBuffer.readUnsignedShort();
                        loginStage = 9;
                    }

                    if (loginStage == 9) {
                        if (Unsorted.incomingPacketLength > Class3_Sub15.activeConnection.availableBytes()) {
                            return;
                        }

                        GraphicDefinition.incomingBuffer.index = 0;
                        Class3_Sub15.activeConnection.readBytes(GraphicDefinition.incomingBuffer.buffer, 0, Unsorted.incomingPacketLength);
                        Client.messageToDisplay = 2;
                        loginStage = 0;
                        SequenceDefinition.resetAll();
                        Unsorted.anInt3606 = -1;
                        Class39.updateSceneGraph(dynamic);
                        Unsorted.incomingOpcode = -1;
                        return;
                    }
                } catch (IOException var7) {
                    if (null != Class3_Sub15.activeConnection) {
                        Class3_Sub15.activeConnection.close();
                        Class3_Sub15.activeConnection = null;
                    }

                    if (Class166.anInt2079 >= 1) {
                        loginStage = 0;
                        Client.messageToDisplay = -4;
                    } else {
                        loginStage = 1;
                        Class50.anInt820 = 0;
                        ++Class166.anInt2079;
                        if (Class140_Sub6.accRegistryPort == Class162.anInt2036) {
                            Class140_Sub6.accRegistryPort = Client.currentPort;
                        } else {
                            Class140_Sub6.accRegistryPort = Class162.anInt2036;
                        }
                    }
                }

            }
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "ri.A(" + ')');
        }
    }

    static int method1753(int var0, int var1) {
        var1 = var1 * (var0 & 127) >> 7;
        if (var1 < 2) {
            var1 = 2;
        } else if (var1 > 126) {
            var1 = 126;
        }

        return (var0 & '\uff80') + var1;
    }

}
