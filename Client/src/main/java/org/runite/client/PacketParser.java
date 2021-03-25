package org.runite.client;

import org.rs09.Discord;
import org.rs09.SlayerTracker;
import org.rs09.XPGainDraw;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class PacketParser {

    static int anInt80 = 0;
    static byte[][][] aByteArrayArrayArray81;
    static LinkedList aLinkedList_82 = new LinkedList();
    static short aShort83 = 32767;
    static RenderAnimationDefinition aClass16_84 = new RenderAnimationDefinition();
    static int anInt86 = 0;
    static int anInt87 = 0;
    static RSInterface aClass11_88 = null;
    static int inTutorialIsland = 0; // could be boolean
    public static Class3_Sub19[] aClass3_Sub19Array3694;
    static long aLong3202;
    static int anInt3213 = 1;


    static int method823(int var0, int var1, int var2, int var3) {
        try {
            if (var2 >= -76) {
                aShort83 = -91;
            }

            return (8 & Unsorted.aByteArrayArrayArray113[var3][var1][var0]) == 0 ? (var3 > 0 && (Unsorted.aByteArrayArrayArray113[1][var1][var0] & 2) != 0 ? -1 + var3 : var3) : 0;
        } catch (RuntimeException var5) {
            throw ClientErrorException.clientError(var5, "ac.G(" + var0 + ',' + var1 + ',' + var2 + ',' + var3 + ')');
        }
    }

    static void method824(long[] var0, Object[] var1, int var2) {
        try {
            Class134.method1809(var0.length - 1, var0, 122, 0, var1);
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ac.E(" + (var0 != null ? "{...}" : "null") + ',' + (var1 != null ? "{...}" : "null") + ',' + var2 + ')');
        }
    }

    static void method825(int var1) {
        try {
            InterfaceWidget var3 = InterfaceWidget.getWidget(1, var1);
            var3.a();
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ac.D(" + (byte) 92 + ',' + var1 + ')');
        }
    }

    static int method826(RSString var0, int var1) {
        try {
            if (var1 != -1) {
                method826(null, 65);
            }

            if (var0 != null) {
                for (int var2 = 0; Class8.anInt104 > var2; ++var2) {
                    if (var0.equalsStringIgnoreCase(Class70.aClass94Array1046[var2])) {
                        return var2;
                    }
                }

            }
            return -1;
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "ac.B(" + (var0 != null ? "{...}" : "null") + ',' + var1 + ')');
        }
    }

    public static boolean parseIncomingPackets() {
        int nodeModelId;
        if (60 == Unsorted.incomingOpcode) {
            nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
            byte var69 = BufferedDataStream.incomingBuffer.readSignedNegativeByte();
            TextureOperation39.method281(var69, nodeModelId);
            Unsorted.incomingOpcode = -1;
            return true;
        } else {
            int counter;
            RSString playerName;
            if (Unsorted.incomingOpcode == 115) {
                nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                playerName = BufferedDataStream.incomingBuffer.readString();
                Object[] var71 = new Object[playerName.length() - -1];
                for (counter = playerName.length() + -1; counter >= 0; --counter) {
                    if (115 == playerName.charAt(counter, (byte) -45)) {
                        var71[1 + counter] = BufferedDataStream.incomingBuffer.readString();
                    } else {
                        var71[1 + counter] = BufferedDataStream.incomingBuffer.readInt();
                    }
                }

                var71[0] = BufferedDataStream.incomingBuffer.readInt();
                Class146.updateInterfacePacketCounter(nodeModelId);
                CS2Script var66 = new CS2Script();
                var66.arguments = var71;
                Class43.method1065(var66);

                Unsorted.incomingOpcode = -1;
                return true;
            } else {
                long nameAsLong;
                boolean isIgnored;
                int var30;
                RSString var41;
                if (Unsorted.incomingOpcode == 70) {
                    RSString message = BufferedDataStream.incomingBuffer.readString();
                    if (message.endsWith(RSString.parse(":tradereq:"))) {
                        playerName = message.substring(0, message.indexOf(RSString.parse(":"), 65), 0);
                        nameAsLong = playerName.toLong();
                        isIgnored = false;

                        for (var30 = 0; Class3_Sub28_Sub5.anInt3591 > var30; ++var30) {
                            if (nameAsLong == Class114.ignores[var30]) {
                                isIgnored = true;
                                break;
                            }
                        }

                        if (!isIgnored && inTutorialIsland == 0) {
                            BufferedDataStream.addChatMessage(playerName, 4, TextCore.HasWishToTrade, (byte) -83 + 82);
                        }
                    } else if (message.endsWith(TextCore.cmdChalReq)) {
                        playerName = message.substring(0, message.indexOf(RSString.parse(":"), 75), 0);
                        nameAsLong = playerName.toLong();
                        isIgnored = false;

                        for (var30 = 0; var30 < Class3_Sub28_Sub5.anInt3591; ++var30) {
                            if (Class114.ignores[var30] == nameAsLong) {
                                isIgnored = true;
                                break;
                            }
                        }

                        if (!isIgnored && inTutorialIsland == 0) {
                            var41 = message.substring(1 + message.indexOf(RSString.parse(":"), 101), message.length() + -9, (byte) -83 ^ -83);
                            BufferedDataStream.addChatMessage(playerName, 8, var41, (byte) -83 ^ 82);
                        }
                    } else if (message.endsWith(TextCore.HasAssistRequest)) {
                        isIgnored = false;
                        playerName = message.substring(0, message.indexOf(RSString.parse(":"), 96), 0);
                        nameAsLong = playerName.toLong();

                        for (var30 = 0; var30 < Class3_Sub28_Sub5.anInt3591; ++var30) {
                            if (nameAsLong == Class114.ignores[var30]) {
                                isIgnored = true;
                                break;
                            }
                        }

                        if (!isIgnored && inTutorialIsland == 0) {
                            BufferedDataStream.addChatMessage(playerName, 10, RSString.parse(""), -1);
                        }
                    } else if (message.endsWith(TextCore.HasClan)) {
                        playerName = message.substring(0, message.indexOf(TextCore.HasClan, (byte) -83 ^ -50), 0);
                        BufferedDataStream.addChatMessage(RSString.parse(""), 11, playerName, -1);
                    } else if (message.endsWith(TextCore.HasTrade)) {
                        playerName = message.substring(0, message.indexOf(TextCore.HasTrade, 102), 0);
                        if (0 == inTutorialIsland) {
                            BufferedDataStream.addChatMessage(RSString.parse(""), 12, playerName, -1);
                        }
                    } else if (message.endsWith(TextCore.HasAssist)) {
                        playerName = message.substring(0, message.indexOf(TextCore.HasAssist, 121), 0);
                        if (inTutorialIsland == 0) {
                            BufferedDataStream.addChatMessage(RSString.parse(""), 13, playerName, -1);
                        }
                    } else if (message.endsWith(TextCore.HasDuelStake)) {
                        isIgnored = false;
                        playerName = message.substring(0, message.indexOf(RSString.parse(":"), 115), 0);
                        nameAsLong = playerName.toLong();

                        for (var30 = 0; Class3_Sub28_Sub5.anInt3591 > var30; ++var30) {
                            if (nameAsLong == Class114.ignores[var30]) {
                                isIgnored = true;
                                break;
                            }
                        }

                        if (!isIgnored && inTutorialIsland == 0) {
                            BufferedDataStream.addChatMessage(playerName, 14, RSString.parse(""), -1);
                        }
                    } else if (message.endsWith(TextCore.HasDuelFriend)) {
                        playerName = message.substring(0, message.indexOf(RSString.parse(":"), 118), 0);
                        isIgnored = false;
                        nameAsLong = playerName.toLong();

                        for (var30 = 0; var30 < Class3_Sub28_Sub5.anInt3591; ++var30) {
                            if (nameAsLong == Class114.ignores[var30]) {
                                isIgnored = true;
                                break;
                            }
                        }

                        if (!isIgnored && 0 == inTutorialIsland) {
                            BufferedDataStream.addChatMessage(playerName, 15, RSString.parse(""), -1);
                        }
                    } else if (message.endsWith(TextCore.HasClanRequest)) {
                        playerName = message.substring(0, message.indexOf(RSString.parse(":"), (byte) -83 + 138), 0);
                        nameAsLong = playerName.toLong();
                        isIgnored = false;

                        for (var30 = 0; var30 < Class3_Sub28_Sub5.anInt3591; ++var30) {
                            if (Class114.ignores[var30] == nameAsLong) {
                                isIgnored = true;
                                break;
                            }
                        }

                        if (!isIgnored && inTutorialIsland == 0) {
                            BufferedDataStream.addChatMessage(playerName, 16, RSString.parse(""), -1);
                        }
                    } else if (message.endsWith(TextCore.HasAllyReq)) {
                        playerName = message.substring(0, message.indexOf(RSString.parse(":"), (byte) -83 + 189), (byte) -83 + 83);
                        isIgnored = false;
                        nameAsLong = playerName.toLong();

                        for (var30 = 0; var30 < Class3_Sub28_Sub5.anInt3591; ++var30) {
                            if (nameAsLong == Class114.ignores[var30]) {
                                isIgnored = true;
                                break;
                            }
                        }

                        if (!isIgnored && inTutorialIsland == 0) {
                            var41 = message.substring(1 + message.indexOf(RSString.parse(":"), 92), message.length() - 9, (byte) -83 ^ -83);
                            BufferedDataStream.addChatMessage(playerName, 21, var41, -1);
                        }
                    } else {
                        BufferedDataStream.addChatMessage(RSString.parse(""), 0, message, (byte) -83 + 82);
                    }

                    Unsorted.incomingOpcode = -1;
                    return true;
                } else {
                    int var19;
                    RSString var58;
                    if (Unsorted.incomingOpcode == 123) {
                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                        var19 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                        var58 = BufferedDataStream.incomingBuffer.readString();
                        Class146.updateInterfacePacketCounter(var19);
                        InterfaceWidget.setWidgetText(var58, nodeModelId);


                        Unsorted.incomingOpcode = -1;
                        return true;
                    } else if (Unsorted.incomingOpcode == 230) {
                        Class39.currentChunkY = BufferedDataStream.incomingBuffer.readUnsignedByte128();
                        Class65.currentChunkX = BufferedDataStream.incomingBuffer.readUnsigned128Byte();

                        while (BufferedDataStream.incomingBuffer.index < Unsorted.incomingPacketLength) {
                            Unsorted.incomingOpcode = BufferedDataStream.incomingBuffer.readUnsignedByte();
                            Class39.parseChunkPacket((byte) -82);
                        }

                        Unsorted.incomingOpcode = -1;
                        return true;
                    } else if (153 == Unsorted.incomingOpcode) {
                        Unsorted.incomingOpcode = -1;
                        Class65.anInt987 = 0;
                        return true;
                    } else {
                        int modelId;
                        if (Unsorted.incomingOpcode == 220) {
                            nodeModelId = BufferedDataStream.incomingBuffer.readIntV2();
                            var19 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                            modelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                            Class146.updateInterfacePacketCounter(modelId);
                            TextureOperation29.method327(var19, nodeModelId);

                            Unsorted.incomingOpcode = -1;
                            return true;
                        } else {
                            long var2;
                            int clanChatIcon;
                            int var11;
                            long var29;
                            long var36;
                            if (81 == Unsorted.incomingOpcode) {
                                var2 = BufferedDataStream.incomingBuffer.readLong();
                                BufferedDataStream.incomingBuffer.readSignedByte();
                                nameAsLong = BufferedDataStream.incomingBuffer.readLong();
                                var29 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                var36 = BufferedDataStream.incomingBuffer.readMedium();
                                clanChatIcon = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                boolean var63 = false;
                                var11 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                long var55 = (var29 << 32) + var36;
                                int var54 = 0;

                                label1521:
                                while (true) {
                                    if (100 > var54) {
                                        if (Class163_Sub2_Sub1.aLongArray4017[var54] != var55) {
                                            ++var54;
                                            continue;
                                        }

                                        var63 = true;
                                        break;
                                    }

                                    if (1 >= clanChatIcon) {
                                        for (var54 = 0; var54 < Class3_Sub28_Sub5.anInt3591; ++var54) {
                                            if (var2 == Class114.ignores[var54]) {
                                                var63 = true;
                                                break label1521;
                                            }
                                        }
                                    }
                                    break;
                                }

                                if (!var63 && 0 == inTutorialIsland) {
                                    Class163_Sub2_Sub1.aLongArray4017[MouseListeningClass.anInt1921] = var55;
                                    MouseListeningClass.anInt1921 = (1 + MouseListeningClass.anInt1921) % 100;
                                    RSString var61 = QuickChat.method733(var11).method555(BufferedDataStream.incomingBuffer);
                                    if (clanChatIcon == 2 || 3 == clanChatIcon) {
                                        MessageManager.sendGameMessage(var11, 20, var61, Objects.requireNonNull(Unsorted.method1052(nameAsLong)).longToRSString(), RSString.stringCombiner(new RSString[]{RSString.parse("<img=1>"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}));
                                    } else if (clanChatIcon == 1) {
                                        MessageManager.sendGameMessage(var11, 20, var61, Objects.requireNonNull(Unsorted.method1052(nameAsLong)).longToRSString(), RSString.stringCombiner(new RSString[]{RSString.parse("<img=0>"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}));
                                    } else {
                                        MessageManager.sendGameMessage(var11, 20, var61, Objects.requireNonNull(Unsorted.method1052(nameAsLong)).longToRSString(), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString());
                                    }
                                }

                                Unsorted.incomingOpcode = -1;
                                return true;
                            } else {
                                int var6;
                                int chatIcon;
                                boolean var32;
                                if (Unsorted.incomingOpcode == 55) {
                                    Class167.anInt2087 = anInt3213;
                                    var2 = BufferedDataStream.incomingBuffer.readLong();
                                    if (var2 == 0) {
                                        Class161.aClass94_2035 = null;
                                        Unsorted.incomingOpcode = -1;
                                        RSInterface.aClass94_251 = null;
                                        aClass3_Sub19Array3694 = null;
                                        Unsorted.clanSize = 0;
                                        return true;
                                    } else {
                                        nameAsLong = BufferedDataStream.incomingBuffer.readLong();
                                        RSInterface.aClass94_251 = Unsorted.method1052(nameAsLong);
                                        Class161.aClass94_2035 = Unsorted.method1052(var2);
                                        Player.aByte3953 = BufferedDataStream.incomingBuffer.readSignedByte();
                                        var6 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                        if (255 != var6) {
                                            Unsorted.clanSize = var6;
                                            Class3_Sub19[] var7 = new Class3_Sub19[100];

                                            for (chatIcon = 0; chatIcon < Unsorted.clanSize; ++chatIcon) {
                                                var7[chatIcon] = new Class3_Sub19();
                                                var7[chatIcon].linkableKey = BufferedDataStream.incomingBuffer.readLong();
                                                var7[chatIcon].aClass94_2476 = Unsorted.method1052(var7[chatIcon].linkableKey);
                                                var7[chatIcon].anInt2478 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                var7[chatIcon].aByte2472 = BufferedDataStream.incomingBuffer.readSignedByte();
                                                var7[chatIcon].aClass94_2473 = BufferedDataStream.incomingBuffer.readString();
                                                if (var7[chatIcon].linkableKey == aLong3202) {
                                                    Class91.aByte1308 = var7[chatIcon].aByte2472;
                                                }
                                            }

                                            clanChatIcon = Unsorted.clanSize;

                                            while (clanChatIcon > 0) {
                                                var32 = true;
                                                --clanChatIcon;

                                                for (var11 = 0; var11 < clanChatIcon; ++var11) {
                                                    if (var7[var11].aClass94_2476.method1559(var7[var11 - -1].aClass94_2476) > 0) {
                                                        var32 = false;
                                                        Class3_Sub19 var9 = var7[var11];
                                                        var7[var11] = var7[1 + var11];
                                                        var7[var11 + 1] = var9;
                                                    }
                                                }

                                                if (var32) {
                                                    break;
                                                }
                                            }

                                            aClass3_Sub19Array3694 = var7;
                                        }
                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    }
                                } else if (Unsorted.incomingOpcode == 164) {
                                    nodeModelId = BufferedDataStream.incomingBuffer.readIntV1();
                                    Class136.aClass64_1778 = Class38.signlink.method1449((byte) -83 ^ -82, nodeModelId);
                                    Unsorted.incomingOpcode = -1;
                                    return true;
                                } else if (Unsorted.incomingOpcode == 225) {
                                    PlayerRendering.renderPlayers();
                                    Unsorted.incomingOpcode = -1;
                                    return true;
                                } else if (Unsorted.incomingOpcode == 48) { //UNUSED BY SERVER
                                    nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                    playerName = BufferedDataStream.incomingBuffer.readString();
                                    modelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                    Class146.updateInterfacePacketCounter(nodeModelId);
                                    InterfaceWidget.setWidgetText(playerName, modelId);

                                    Unsorted.incomingOpcode = -1;
                                    return true;
                                } else if (232 == Unsorted.incomingOpcode) {
                                    CS2Script.anInt3101 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                    Class24.anInt467 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                    Class45.anInt734 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                    Unsorted.incomingOpcode = -1;
                                    return true;
                                } else {
                                    RSString var56;
                                    if (Unsorted.incomingOpcode == 44) {
                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                        if (nodeModelId == 65535) {
                                            nodeModelId = -1;
                                        }

                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                        var56 = BufferedDataStream.incomingBuffer.readString();
                                        if (1 <= modelId && modelId <= 8) {
                                            if (var56.equalsStringIgnoreCase(TextCore.HasNull)) {
                                                var56 = null;
                                            }

                                            Class91.aClass94Array1299[-1 + modelId] = var56;
                                            TextureOperation35.anIntArray3328[modelId + -1] = nodeModelId;
                                            Class1.aBooleanArray54[modelId + -1] = var19 == 0;
                                        }

                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else if (Unsorted.incomingOpcode == 226) {
                                        nodeModelId = BufferedDataStream.incomingBuffer.readInt();
                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                        TextureOperation39.method281(nodeModelId, var19);
                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else if (Unsorted.incomingOpcode == 21) {
                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                        modelId = BufferedDataStream.incomingBuffer.readIntLE();
                                        Class146.updateInterfacePacketCounter(var19);
                                        TextureOperation4.method260(modelId, nodeModelId);

                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else if (Unsorted.incomingOpcode == 145) {
                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedByte128();
                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                        Class146.updateInterfacePacketCounter(modelId);
                                        if (var19 == 2) {
                                            method834();
                                        }
                                        ConfigInventoryDefinition.anInt3655 = nodeModelId;
                                        TextureOperation20.method232(nodeModelId);
                                        Class124.method1746(false, (byte) -64);
                                        TextureOperation24.method226(ConfigInventoryDefinition.anInt3655);

                                        for (counter = 0; counter < 100; ++counter) {
                                            Unsorted.aBooleanArray3674[counter] = true;
                                        }

                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else if (Unsorted.incomingOpcode == 69) { //UNUSED BY SERVER
                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                        var19 = BufferedDataStream.incomingBuffer.readInt();
                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                        Class146.updateInterfacePacketCounter(nodeModelId);
                                        TextureOperation19.method255(modelId, var19, 1);

                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else if (141 == Unsorted.incomingOpcode) { //UNUSED BY SERVER
                                        var2 = BufferedDataStream.incomingBuffer.readLong();
                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                        var56 = QuickChat.method733(modelId).method555(BufferedDataStream.incomingBuffer);
                                        MessageManager.sendGameMessage(modelId, 19, var56, null, Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString());
                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else if (Unsorted.incomingOpcode == 169) { //UNUSED BY SERVER
                                        Class162.method2204(BufferedDataStream.incomingBuffer);
                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else if (89 == Unsorted.incomingOpcode) { //UNUSED BY SERVER
                                        TextureOperation6.method176(-117);
                                        BufferedDataStream.method819();
                                        Class36.anInt641 += 32;
                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else if (Unsorted.incomingOpcode == 125) {//camera rotation
                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                        counter = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                        var6 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                        var30 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                        Class146.updateInterfacePacketCounter(nodeModelId);
                                        Class164_Sub1.method2238(counter, modelId, var6, var19, var30);

                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else if (Unsorted.incomingOpcode == 36) {
                                        nodeModelId = BufferedDataStream.incomingBuffer.readIntV2();
                                        var19 = BufferedDataStream.incomingBuffer.readSignedShortLE();
                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                        Class146.updateInterfacePacketCounter(modelId);
                                        Class131.method1790(nodeModelId, var19);

                                        Unsorted.incomingOpcode = -1;
                                        return true;
                                    } else {
                                        Class3_Sub1 var38;
                                        Class3_Sub1 var47;
                                        if (Unsorted.incomingOpcode == 9) {
                                            nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                            var19 = BufferedDataStream.incomingBuffer.readIntLE();
                                            modelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                            counter = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                            if (counter == 65535) {
                                                counter = -1;
                                            }

                                            var6 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                            if (var6 == 65535) {
                                                var6 = -1;
                                            }

                                            Class146.updateInterfacePacketCounter(modelId);
                                            for (var30 = var6; counter >= var30; ++var30) {
                                                var36 = (long) var30 + ((long) var19 << 32);
                                                var47 = (Class3_Sub1) Class124.aHashTable_1659.get(var36);
                                                if (null != var47) {
                                                    var38 = new Class3_Sub1(var47.anInt2205, nodeModelId);
                                                    var47.unlink();
                                                } else if (var30 == -1) {
                                                    var38 = new Class3_Sub1(Objects.requireNonNull(Unsorted.getRSInterface(var19)).aClass3_Sub1_257.anInt2205, nodeModelId);
                                                } else {
                                                    var38 = new Class3_Sub1(0, nodeModelId);
                                                }

                                                Class124.aHashTable_1659.put(var36, var38);
                                            }

                                            Unsorted.incomingOpcode = -1;
                                            return true;
                                        } else {
                                            int var33;
                                            if (Unsorted.incomingOpcode == 56) { //UNUSED
                                                nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                var19 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                modelId = BufferedDataStream.incomingBuffer.readIntV1();
                                                counter = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                if (modelId >> 30 == 0) {
                                                    SequenceDefinition var53;
                                                    if (modelId >> 29 != 0) {
                                                        var6 = 65535 & modelId;
                                                        NPC var62 = NPC.npcs[var6];
                                                        if (null != var62) {
                                                            if (counter == 65535) {
                                                                counter = -1;
                                                            }
                                                            var32 = counter == -1 || -1 == var62.anInt2842 || SequenceDefinition.getAnimationDefinition(GraphicDefinition.getGraphicDefinition((byte) 42, var62.anInt2842).anInt542).forcedPriority <= SequenceDefinition.getAnimationDefinition(GraphicDefinition.getGraphicDefinition((byte) 42, counter).anInt542).forcedPriority;

                                                            if (var32) {
                                                                var62.anInt2761 = 0;
                                                                var62.anInt2842 = counter;
                                                                var62.anInt2759 = Class44.anInt719 - -nodeModelId;
                                                                var62.anInt2805 = 0;
                                                                if (var62.anInt2759 > Class44.anInt719) {
                                                                    var62.anInt2805 = -1;
                                                                }

                                                                var62.anInt2799 = var19;
                                                                var62.anInt2826 = 1;
                                                                if (var62.anInt2842 != -1 && Class44.anInt719 == var62.anInt2759) {
                                                                    var33 = GraphicDefinition.getGraphicDefinition((byte) 42, var62.anInt2842).anInt542;
                                                                    if (var33 != -1) {
                                                                        var53 = SequenceDefinition.getAnimationDefinition(var33);
                                                                        if (null != var53.frames) {
                                                                            Unsorted.method1470(var62.anInt2829, var53, 183921384, var62.anInt2819, false, 0);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else if (modelId >> 28 != 0) {
                                                        var6 = modelId & 65535;
                                                        Player var60;
                                                        if (var6 == Class3_Sub1.localIndex) {
                                                            var60 = Class102.player;
                                                        } else {
                                                            var60 = Unsorted.players[var6];
                                                        }

                                                        if (null != var60) {
                                                            if (counter == 65535) {
                                                                counter = -1;
                                                            }
                                                            var32 = counter == -1 || var60.anInt2842 == -1 || SequenceDefinition.getAnimationDefinition(GraphicDefinition.getGraphicDefinition((byte) 42, var60.anInt2842).anInt542).forcedPriority <= SequenceDefinition.getAnimationDefinition(GraphicDefinition.getGraphicDefinition((byte) 42, counter).anInt542).forcedPriority;

                                                            if (var32) {
                                                                var60.anInt2759 = nodeModelId + Class44.anInt719;
                                                                var60.anInt2799 = var19;
                                                                var60.anInt2842 = counter;

                                                                var60.anInt2826 = 1;
                                                                var60.anInt2761 = 0;
                                                                var60.anInt2805 = 0;
                                                                if (var60.anInt2759 > Class44.anInt719) {
                                                                    var60.anInt2805 = -1;
                                                                }

                                                                if (var60.anInt2842 != -1 && Class44.anInt719 == var60.anInt2759) {
                                                                    var33 = GraphicDefinition.getGraphicDefinition((byte) 42, var60.anInt2842).anInt542;
                                                                    if (var33 != -1) {
                                                                        var53 = SequenceDefinition.getAnimationDefinition(var33);
                                                                        if (null != var53.frames) {
                                                                            Unsorted.method1470(var60.anInt2829, var53, 183921384, var60.anInt2819, var60 == Class102.player, 0);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    var6 = 3 & modelId >> 28;
                                                    var30 = ((modelId & 268434277) >> 14) + -Class131.anInt1716;
                                                    chatIcon = (modelId & 16383) + -Texture.anInt1152;
                                                    if (var30 >= 0 && chatIcon >= 0 && 104 > var30 && chatIcon < 104) {
                                                        chatIcon = chatIcon * 128 - -64;
                                                        var30 = 128 * var30 + 64;
                                                        Class140_Sub2 var50 = new Class140_Sub2(counter, var6, var30, chatIcon, -var19 + Class121.method1736(var6, 1, var30, chatIcon), nodeModelId, Class44.anInt719);
                                                        TextureOperation17.aLinkedList_3177.method1215(new Class3_Sub28_Sub2(var50));
                                                    }
                                                }

                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 207) { //UNUSED
                                                nodeModelId = BufferedDataStream.incomingBuffer.readIntV2();
                                                var19 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                modelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                counter = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                Class146.updateInterfacePacketCounter(var19);
                                                Class114.method1708(counter + (modelId << 16), nodeModelId);

                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 38) {
                                                BufferedDataStream.method819();
                                                nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedByte128();//Level
                                                var19 = BufferedDataStream.incomingBuffer.readIntV1();//Skillxp
                                                modelId = BufferedDataStream.incomingBuffer.readUnsignedByte();//Skill ID
                                                int gain = var19 - XPGainDraw.getLastXp()[modelId];
                                                XPGainDraw.addGain(gain,modelId);
                                                XPGainDraw.getLastXp()[modelId] = var19;
                                                Class133.anIntArray1743[modelId] = var19;//XP for Skill ID
                                                TextureOperation17.anIntArray3185[modelId] = nodeModelId;//Level for Skill ID
                                                Class3_Sub20.anIntArray2480[modelId] = 1;

                                                for (counter = 0; 98 > counter; ++counter) {
                                                    if (ItemDefinition.anIntArray781[counter] <= var19) { //Checks xp less than or equal to level 98 or 11805606 xp
                                                        Class3_Sub20.anIntArray2480[modelId] = counter + 2;
                                                    }
                                                }
                                                //Calculate xp till next level
                                                //System.out.println("xp Gained: " + (ItemDefinition.anIntArray781[nodeModelId - 1] - var19));


                                                Client.anIntArray3780[Unsorted.bitwiseAnd(31, Class49.anInt815++)] = modelId;
                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 104 ||
                                                    121 == Unsorted.incomingOpcode ||
                                                    Unsorted.incomingOpcode == 97 ||
                                                    Unsorted.incomingOpcode == 14 ||
                                                    Unsorted.incomingOpcode == 202 ||
                                                    Unsorted.incomingOpcode == 135 ||
                                                    Unsorted.incomingOpcode == 17 ||
                                                    Unsorted.incomingOpcode == 16 ||
                                                    Unsorted.incomingOpcode == 240 ||
                                                    Unsorted.incomingOpcode == 33 ||
                                                    Unsorted.incomingOpcode == 20 ||
                                                    195 == Unsorted.incomingOpcode ||
                                                    179 == Unsorted.incomingOpcode) {
                                                Class39.parseChunkPacket((byte) -99);
                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 149) {
                                                nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                var19 = BufferedDataStream.incomingBuffer.readInt();
                                                Class146.updateInterfacePacketCounter(nodeModelId);
                                                Class3_Sub31 var67 = TextureOperation23.aHashTable_3208.get(var19);
                                                if (null != var67) {
                                                    TextureOperation19.method254(true, var67);
                                                }

                                                if (null != TextureOperation27.aClass11_3087) {
                                                    Class20.method909(TextureOperation27.aClass11_3087);
                                                    TextureOperation27.aClass11_3087 = null;
                                                }

                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 187) {
                                                //set camera
                                                nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                var19 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                modelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                Class146.updateInterfacePacketCounter(var19);
                                                GraphicDefinition.CAMERA_DIRECTION = nodeModelId;
                                                Unsorted.anInt2309 = modelId;
                                                if (Class133.anInt1753 == 2) {
                                                    Class139.anInt1823 = Unsorted.anInt2309;
                                                    TextureOperation28.anInt3315 = GraphicDefinition.CAMERA_DIRECTION;
                                                }

                                                Unsorted.method1098((byte) -117);

                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 132) { //UNUSED
                                                nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                var19 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                modelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                counter = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                var6 = BufferedDataStream.incomingBuffer.readInt();
                                                Class146.updateInterfacePacketCounter(var19);
                                                Unsorted.method2143((byte) -124, modelId, var6, counter, nodeModelId);

                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (112 == Unsorted.incomingOpcode) {
                                                Class65.currentChunkX = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                Class39.currentChunkY = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();

                                                for (nodeModelId = Class65.currentChunkX; nodeModelId < 8 + Class65.currentChunkX; ++nodeModelId) {
                                                    for (var19 = Class39.currentChunkY; 8 + Class39.currentChunkY > var19; ++var19) {
                                                        if (null != Class39.aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][nodeModelId][var19]) {
                                                            Class39.aLinkedListArrayArrayArray3273[WorldListCountry.localPlane][nodeModelId][var19] = null;
                                                            Class128.method1760(var19, nodeModelId);
                                                        }
                                                    }
                                                }

                                                for (Class3_Sub4 var68 = (Class3_Sub4) TextureOperation26.aLinkedList_3075.method1222(); null != var68; var68 = (Class3_Sub4) TextureOperation26.aLinkedList_3075.method1221()) {
                                                    if (Class65.currentChunkX <= var68.anInt2264 && 8 + Class65.currentChunkX > var68.anInt2264 && var68.anInt2248 >= Class39.currentChunkY && 8 + Class39.currentChunkY > var68.anInt2248 && var68.anInt2250 == WorldListCountry.localPlane) {
                                                        var68.anInt2259 = 0;
                                                    }
                                                }

                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 144) {
                                                nodeModelId = BufferedDataStream.incomingBuffer.readIntV2();
                                                RSInterface var65 = Unsorted.getRSInterface(nodeModelId);

                                                for (modelId = 0; Objects.requireNonNull(var65).itemAmounts.length > modelId; ++modelId) {
                                                    var65.itemAmounts[modelId] = -1;
                                                    var65.itemAmounts[modelId] = 0;
                                                }

                                                Class20.method909(var65);
                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 130) {
                                                nodeModelId = BufferedDataStream.incomingBuffer.readIntLE();
                                                var19 = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                modelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                if (modelId == 65535) {
                                                    modelId = -1;
                                                }

                                                Class146.updateInterfacePacketCounter(var19);
                                                method256(-1, 1, nodeModelId, modelId);

                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 192) {
                                                Class161.anInt2028 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else if (Unsorted.incomingOpcode == 13) {
                                                nodeModelId = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                                                var19 = BufferedDataStream.incomingBuffer.readUnsignedByte128();
                                                modelId = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                WorldListCountry.localPlane = var19 >> 1;
                                                Class102.player.method1981(nodeModelId, (var19 & 1) == 1, modelId);
                                                Unsorted.incomingOpcode = -1;
                                                return true;
                                            } else {
                                                int var12;
                                                RSString var57;
                                                RSString var64;
                                                if (Unsorted.incomingOpcode == 62) {
                                                    var2 = BufferedDataStream.incomingBuffer.readLong();
                                                    modelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                    counter = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                    isIgnored = true;
                                                    if (var2 < 0L) {
                                                        var2 &= Long.MAX_VALUE;
                                                        isIgnored = false;
                                                    }

                                                    var41 = RSString.parse("");
                                                    if (modelId > 0) {
                                                        var41 = BufferedDataStream.incomingBuffer.readString();
                                                    }

                                                    RSString var46 = Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString();

                                                    for (var33 = 0; var33 < Class8.anInt104; ++var33) {
                                                        if (var2 == Class50.aLongArray826[var33]) {
                                                            if (Unsorted.anIntArray882[var33] != modelId) {
                                                                Unsorted.anIntArray882[var33] = modelId;
                                                                if (0 < modelId) {
                                                                    BufferedDataStream.addChatMessage(RSString.parse(""), 5, RSString.stringCombiner(new RSString[]{var46, TextCore.HasLoggedIn}), -1);
                                                                }

                                                                if (modelId == 0) {
                                                                    BufferedDataStream.addChatMessage(RSString.parse(""), 5, RSString.stringCombiner(new RSString[]{var46, TextCore.HasLoggedOut}), -1);
                                                                }
                                                            }

                                                            Unsorted.aClass94Array2566[var33] = var41;
                                                            Class57.anIntArray904[var33] = counter;
                                                            var46 = null;
                                                            Unsorted.aBooleanArray73[var33] = isIgnored;
                                                            break;
                                                        }
                                                    }

                                                    boolean var45;
                                                    if (null != var46 && 200 > Class8.anInt104) {
                                                        Class50.aLongArray826[Class8.anInt104] = var2;
                                                        Class70.aClass94Array1046[Class8.anInt104] = var46;
                                                        Unsorted.anIntArray882[Class8.anInt104] = modelId;
                                                        Unsorted.aClass94Array2566[Class8.anInt104] = var41;
                                                        Class57.anIntArray904[Class8.anInt104] = counter;
                                                        Unsorted.aBooleanArray73[Class8.anInt104] = isIgnored;
                                                        ++Class8.anInt104;
                                                    }

                                                    Class110.anInt1472 = anInt3213;
                                                    clanChatIcon = Class8.anInt104;

                                                    while (clanChatIcon > 0) {
                                                        --clanChatIcon;
                                                        var45 = true;

                                                        for (var11 = 0; var11 < clanChatIcon; ++var11) {
                                                            if (CS2Script.userCurrentWorldID != Unsorted.anIntArray882[var11] && Unsorted.anIntArray882[var11 - -1] == CS2Script.userCurrentWorldID || Unsorted.anIntArray882[var11] == 0 && Unsorted.anIntArray882[var11 - -1] != 0) {
                                                                var45 = false;
                                                                var12 = Unsorted.anIntArray882[var11];
                                                                Unsorted.anIntArray882[var11] = Unsorted.anIntArray882[var11 - -1];
                                                                Unsorted.anIntArray882[1 + var11] = var12;
                                                                var64 = Unsorted.aClass94Array2566[var11];
                                                                Unsorted.aClass94Array2566[var11] = Unsorted.aClass94Array2566[var11 + 1];
                                                                Unsorted.aClass94Array2566[var11 - -1] = var64;
                                                                var57 = Class70.aClass94Array1046[var11];
                                                                Class70.aClass94Array1046[var11] = Class70.aClass94Array1046[var11 + 1];
                                                                Class70.aClass94Array1046[var11 - -1] = var57;
                                                                long var15 = Class50.aLongArray826[var11];
                                                                Class50.aLongArray826[var11] = Class50.aLongArray826[var11 - -1];
                                                                Class50.aLongArray826[var11 - -1] = var15;
                                                                int var17 = Class57.anIntArray904[var11];
                                                                Class57.anIntArray904[var11] = Class57.anIntArray904[var11 - -1];
                                                                Class57.anIntArray904[1 + var11] = var17;
                                                                boolean var18 = Unsorted.aBooleanArray73[var11];
                                                                Unsorted.aBooleanArray73[var11] = Unsorted.aBooleanArray73[var11 - -1];
                                                                Unsorted.aBooleanArray73[var11 - -1] = var18;
                                                            }
                                                        }

                                                        if (var45) {
                                                            break;
                                                        }
                                                    }

                                                    Unsorted.incomingOpcode = -1;
                                                    return true;
                                                } else if (Unsorted.incomingOpcode == 160) {
                                                    if (0 == Unsorted.incomingPacketLength) {
                                                        TextureOperation32.aClass94_3353 = TextCore.HasWalkHere;
                                                    } else {
                                                        TextureOperation32.aClass94_3353 = BufferedDataStream.incomingBuffer.readString();
                                                    }

                                                    Unsorted.incomingOpcode = -1;
                                                    return true;
                                                } else if (128 == Unsorted.incomingOpcode) { //UNUSED
                                                    for (nodeModelId = 0; nodeModelId < ItemDefinition.ram.length; ++nodeModelId) {
                                                        if (ItemDefinition.ram[nodeModelId] != Class57.varpArray[nodeModelId]) {
                                                            ItemDefinition.ram[nodeModelId] = Class57.varpArray[nodeModelId];
                                                            Class46.method1087(98, nodeModelId);
                                                            Class44.anIntArray726[Unsorted.bitwiseAnd(Class36.anInt641++, 31)] = nodeModelId;
                                                        }
                                                    }

                                                    Unsorted.incomingOpcode = -1;
                                                    return true;
                                                } else if (Unsorted.incomingOpcode == 154) {//camera position
                                                    nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                    var19 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                    modelId = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                    counter = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                    var6 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                    var30 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                    Class146.updateInterfacePacketCounter(nodeModelId);
                                                    Class3_Sub20.method390(true, var6, counter, var30, (byte) -124, modelId, var19);

                                                    Unsorted.incomingOpcode = -1;
                                                    return true;
                                                } else if (247 == Unsorted.incomingOpcode) { //UNUSED
                                                    var2 = BufferedDataStream.incomingBuffer.readLong();
                                                    nameAsLong = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                    var29 = BufferedDataStream.incomingBuffer.readMedium();
                                                    chatIcon = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                    var33 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                    boolean var49 = false;
                                                    long var51 = (nameAsLong << 32) - -var29;
                                                    int var59 = 0;

                                                    label1603:
                                                    while (true) {
                                                        if (100 > var59) {
                                                            if (Class163_Sub2_Sub1.aLongArray4017[var59] != var51) {
                                                                ++var59;
                                                                continue;
                                                            }

                                                            var49 = true;
                                                            break;
                                                        }

                                                        if (chatIcon <= 1) {
                                                            for (var59 = 0; Class3_Sub28_Sub5.anInt3591 > var59; ++var59) {
                                                                if (var2 == Class114.ignores[var59]) {
                                                                    var49 = true;
                                                                    break label1603;
                                                                }
                                                            }
                                                        }
                                                        break;
                                                    }

                                                    if (!var49 && inTutorialIsland == 0) {
                                                        Class163_Sub2_Sub1.aLongArray4017[MouseListeningClass.anInt1921] = var51;
                                                        MouseListeningClass.anInt1921 = (1 + MouseListeningClass.anInt1921) % 100;
                                                        var64 = QuickChat.method733(var33).method555(BufferedDataStream.incomingBuffer);
                                                        if (chatIcon == 2) {
                                                            MessageManager.sendGameMessage(var33, 18, var64, null, RSString.stringCombiner(new RSString[]{RSString.parse("<img=1>"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}));
                                                        } else if (1 == chatIcon) {
                                                            MessageManager.sendGameMessage(var33, 18, var64, null, RSString.stringCombiner(new RSString[]{RSString.parse("<img=0>"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}));
                                                        } else {
                                                            MessageManager.sendGameMessage(var33, 18, var64, null, Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString());
                                                        }
                                                    }

                                                    Unsorted.incomingOpcode = -1;
                                                    return true;
                                                } else {
                                                    Class3_Sub31 var26;
                                                    if (Unsorted.incomingOpcode == 176) { //IMISED
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readIntV1();
                                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                        modelId = BufferedDataStream.incomingBuffer.readIntV1();
                                                        Class146.updateInterfacePacketCounter(var19);
                                                        Class3_Sub31 var23 = TextureOperation23.aHashTable_3208.get(nodeModelId);
                                                        var26 = TextureOperation23.aHashTable_3208.get(modelId);
                                                        if (null != var26) {
                                                            TextureOperation19.method254(null == var23 || var26.anInt2602 != var23.anInt2602, var26);
                                                        }

                                                        if (null != var23) {
                                                            var23.unlink();
                                                            TextureOperation23.aHashTable_3208.put(modelId, var23);
                                                        }

                                                        RSInterface var27 = Unsorted.getRSInterface(nodeModelId);
                                                        if (var27 != null) {
                                                            Class20.method909(var27);
                                                        }

                                                        var27 = Unsorted.getRSInterface(modelId);
                                                        if (null != var27) {
                                                            Class20.method909(var27);
                                                            Unsorted.method2104(var27, true, 48);
                                                        }

                                                        if (ConfigInventoryDefinition.anInt3655 != -1) {
                                                            Class3_Sub8.method124(28, 1, ConfigInventoryDefinition.anInt3655);
                                                        }

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 27) { //UNUSED
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        counter = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        var6 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        var30 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                        Class146.updateInterfacePacketCounter(nodeModelId);
                                                        WaterfallShader.aBooleanArray2169[var19] = true;
                                                        TextureOperation14.anIntArray3383[var19] = modelId;
                                                        Class166.anIntArray2073[var19] = counter;
                                                        TextureOperation3.anIntArray3359[var19] = var6;
                                                        Class163_Sub1_Sub1.anIntArray4009[var19] = var30;

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 2) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readIntV1();
                                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                        Class146.updateInterfacePacketCounter(var19);
                                                        Class79.method1385(modelId, nodeModelId);

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 85) {
                                                        Class38_Sub1.anInt2617 = BufferedDataStream.incomingBuffer.readUnsignedShort() * 30;
                                                        Unsorted.incomingOpcode = -1;
                                                        Class140_Sub6.anInt2905 = anInt3213;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 114) {
                                                        TextureOperation3.method305(Class38.signlink, BufferedDataStream.incomingBuffer, Unsorted.incomingPacketLength);
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (65 == Unsorted.incomingOpcode) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                        Class146.updateInterfacePacketCounter(nodeModelId);
                                                        TextureOperation19.method255(modelId, var19, (byte) -83 ^ -84);

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 234) {
                                                        BufferedDataStream.method819();
                                                        Unsorted.anInt136 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        Class140_Sub6.anInt2905 = anInt3213;
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 209) {
                                                        if (-1 != ConfigInventoryDefinition.anInt3655) {
                                                            Class3_Sub8.method124(48, 0, ConfigInventoryDefinition.anInt3655);
                                                        }

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 191) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                        Class3_Sub28_Sub1.method532(nodeModelId);
                                                        QuickChatDefinition.anIntArray3565[Unsorted.bitwiseAnd(31, Unsorted.anInt944++)] = Unsorted.bitwiseAnd(nodeModelId, 32767);
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 102) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                        var19 = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                        NPC var39 = NPC.npcs[nodeModelId];
                                                        if (null != var39) {
                                                            Unsorted.method1772(var19, modelId, 39, var39);
                                                        }

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 159) {
                                                        BufferedDataStream.method819();
                                                        MouseListeningClass.anInt1925 = BufferedDataStream.incomingBuffer.readSignedShort();
                                                        Class140_Sub6.anInt2905 = anInt3213;
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 71) {
                                                        var2 = BufferedDataStream.incomingBuffer.readLong();
                                                        var58 = Font.method686(Objects.requireNonNull(Class32.method992(BufferedDataStream.incomingBuffer).properlyCapitalize()));
                                                        BufferedDataStream.addChatMessage(Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString(), 6, var58, (byte) -83 ^ 82);
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 42) {
                                                        if (null != TextureOperation30.aFrame3121) {
                                                            GameObject.graphicsSettings(false, Unsorted.anInt2577, -1, -1);
                                                        }

                                                        byte[] var22 = new byte[Unsorted.incomingPacketLength];
                                                        BufferedDataStream.incomingBuffer.method811((byte) 30, 0, var22, Unsorted.incomingPacketLength);
                                                        playerName = TextureOperation33.bufferToString(var22, Unsorted.incomingPacketLength, 0);
                                                        if (null == GameShell.frame && (3 == Signlink.anInt1214 || !Signlink.osName.startsWith("win") || Class106.paramUserUsingInternetExplorer)) {
                                                            Class99.method1596(playerName, (byte) 127, true);
                                                        } else {
                                                            TextureOperation5.aClass94_3295 = playerName;
                                                            Unsorted.aBoolean2154 = true;
                                                            Class15.aClass64_351 = Class38.signlink.method1452(new String(playerName.method1568(), StandardCharsets.ISO_8859_1), true);
                                                        }

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 111) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                        var19 = BufferedDataStream.incomingBuffer.readIntV2();
                                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                        counter = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                        var6 = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                        Class146.updateInterfacePacketCounter(nodeModelId);
                                                        method256(modelId, 7, var19, counter << 16 | var6);

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (37 == Unsorted.incomingOpcode) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedByte128();
                                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                        Class163.method2209((byte) -122, nodeModelId, var19);
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 155) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        var19 = BufferedDataStream.incomingBuffer.readIntV2();
                                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                        counter = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                        Class146.updateInterfacePacketCounter(modelId);
                                                        var26 = TextureOperation23.aHashTable_3208.get(var19);
                                                        if (null != var26) {
                                                            TextureOperation19.method254(var26.anInt2602 != counter, var26);
                                                        }

                                                        Class21.method914(counter, var19, nodeModelId);

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 131) {
                                                        for (nodeModelId = 0; nodeModelId < Unsorted.players.length; ++nodeModelId) {
                                                            if (Unsorted.players[nodeModelId] != null) {
                                                                Unsorted.players[nodeModelId].anInt2771 = -1;
                                                            }
                                                        }

                                                        for (nodeModelId = 0; nodeModelId < NPC.npcs.length; ++nodeModelId) {
                                                            if (null != NPC.npcs[nodeModelId]) {
                                                                NPC.npcs[nodeModelId].anInt2771 = -1;
                                                            }
                                                        }

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 217) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        Class96 var48 = new Class96();
                                                        var19 = nodeModelId >> 6;
                                                        var48.anInt1360 = nodeModelId & 63;
                                                        var48.anInt1351 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        if (var48.anInt1351 >= 0 && Class166.aAbstractSpriteArray2072.length > var48.anInt1351) {
                                                            if (var48.anInt1360 == 1 || 10 == var48.anInt1360) {
                                                                var48.anInt1359 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                                BufferedDataStream.incomingBuffer.index += 3;
                                                            } else if (var48.anInt1360 >= 2 && 6 >= var48.anInt1360) {
                                                                if (var48.anInt1360 == 2) {
                                                                    var48.anInt1346 = 64;
                                                                    var48.anInt1350 = 64;
                                                                }

                                                                if (var48.anInt1360 == 3) {
                                                                    var48.anInt1346 = 0;
                                                                    var48.anInt1350 = 64;
                                                                }

                                                                if (4 == var48.anInt1360) {
                                                                    var48.anInt1346 = 128;
                                                                    var48.anInt1350 = 64;
                                                                }

                                                                if (5 == var48.anInt1360) {
                                                                    var48.anInt1346 = 64;
                                                                    var48.anInt1350 = 0;
                                                                }

                                                                if (var48.anInt1360 == 6) {
                                                                    var48.anInt1346 = 64;
                                                                    var48.anInt1350 = 128;
                                                                }

                                                                var48.anInt1360 = 2;
                                                                var48.anInt1356 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                                var48.anInt1347 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                                var48.anInt1353 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                            }

                                                            var48.anInt1355 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                            if (var48.anInt1355 == 65535) {
                                                                var48.anInt1355 = -1;
                                                            }

                                                            ClientErrorException.aClass96Array2114[var19] = var48;
                                                        }

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (126 == Unsorted.incomingOpcode) {
                                                        Class3_Sub28_Sub5.anInt3591 = Unsorted.incomingPacketLength / 8;

                                                        for (nodeModelId = 0; Class3_Sub28_Sub5.anInt3591 > nodeModelId; ++nodeModelId) {
                                                            Class114.ignores[nodeModelId] = BufferedDataStream.incomingBuffer.readLong();
                                                            TextureOperation7.aClass94Array3341[nodeModelId] = Unsorted.method1052(Class114.ignores[nodeModelId]);
                                                        }

                                                        Class110.anInt1472 = anInt3213;
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 32) {
                                                        NPCRendering.renderNPCs(8169);
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 119) {
                                                        //Reposition child?
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                        var19 = BufferedDataStream.incomingBuffer.readIntLE();
                                                        modelId = BufferedDataStream.incomingBuffer.readSignedShort();
                                                        counter = BufferedDataStream.incomingBuffer.readSignedShort128();
                                                        Class146.updateInterfacePacketCounter(nodeModelId);
                                                        Class168.method2271(modelId, var19, counter);

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 235) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                                                        var19 = nodeModelId >> 2;
                                                        modelId = 3 & nodeModelId;
                                                        counter = Class75.anIntArray1107[var19];
                                                        var6 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                        var30 = BufferedDataStream.incomingBuffer.readInt();
                                                        if (65535 == var6) {
                                                            var6 = -1;
                                                        }

                                                        clanChatIcon = 16383 & var30;
                                                        var33 = 16383 & var30 >> 14;
                                                        var33 -= Class131.anInt1716;
                                                        clanChatIcon -= Texture.anInt1152;
                                                        chatIcon = 3 & var30 >> 28;
                                                        Class50.method1131(chatIcon, 110, modelId, var19, clanChatIcon, counter, var33, var6);
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 0) {
                                                        var2 = BufferedDataStream.incomingBuffer.readLong();
                                                        nameAsLong = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                        var29 = BufferedDataStream.incomingBuffer.readMedium();
                                                        chatIcon = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        boolean var42 = false;
                                                        long var35 = var29 + (nameAsLong << 32);
                                                        var12 = 0;
                                                        label1651:
                                                        while (true) {
                                                            if (var12 >= 100) {
                                                                if (chatIcon <= 1) {
                                                                    if ((!Class3_Sub15.aBoolean2433 || Class121.aBoolean1641) && !TextureOperation31.aBoolean3166) {
                                                                        for (var12 = 0; var12 < Class3_Sub28_Sub5.anInt3591; ++var12) {
                                                                            if (Class114.ignores[var12] == var2) {
                                                                                var42 = true;
                                                                                break label1651;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        var42 = true;
                                                                    }
                                                                }
                                                                break;
                                                            }

                                                            if (Class163_Sub2_Sub1.aLongArray4017[var12] == var35) {
                                                                var42 = true;
                                                                break;
                                                            }

                                                            ++var12;
                                                        }

                                                        if (!var42 && inTutorialIsland == 0) {
                                                            Class163_Sub2_Sub1.aLongArray4017[MouseListeningClass.anInt1921] = var35;
                                                            MouseListeningClass.anInt1921 = (MouseListeningClass.anInt1921 - -1) % 100;
                                                            RSString var52 = Font.method686(Objects.requireNonNull(Class32.method992(BufferedDataStream.incomingBuffer).properlyCapitalize()));
                                                            if (chatIcon == 2 || chatIcon == 3) {
                                                                BufferedDataStream.addChatMessage(RSString.stringCombiner(new RSString[]{RSString.parse("<img=1>"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}), 7, var52, -1);
                                                            } else if (chatIcon == 1) {
                                                                BufferedDataStream.addChatMessage(RSString.stringCombiner(new RSString[]{RSString.parse("<img=" + (chatIcon - 1) + ">"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}), 7, var52, -1);
                                                            } else {
                                                                BufferedDataStream.addChatMessage(RSString.stringCombiner(new RSString[]{RSString.parse("<img=" + (chatIcon - 1) + ">"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}), 7, var52, -1);
                                                            }
                                                        }

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 54) {//cc messahe
                                                        var2 = BufferedDataStream.incomingBuffer.readLong();
                                                        BufferedDataStream.incomingBuffer.readSignedByte();
                                                        nameAsLong = BufferedDataStream.incomingBuffer.readLong();
                                                        var29 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                        var36 = BufferedDataStream.incomingBuffer.readMedium();
                                                        long var44 = (var29 << 32) + var36;
                                                        clanChatIcon = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        boolean var13 = false;
                                                        int var14 = 0;

                                                        label1774:
                                                        while (true) {
                                                            if (var14 >= 100) {
                                                                if (1 >= clanChatIcon) {
                                                                    if ((!Class3_Sub15.aBoolean2433 || Class121.aBoolean1641) && !TextureOperation31.aBoolean3166) {
                                                                        for (var14 = 0; Class3_Sub28_Sub5.anInt3591 > var14; ++var14) {
                                                                            if (Class114.ignores[var14] == var2) {
                                                                                var13 = true;
                                                                                break label1774;
                                                                            }
                                                                        }
                                                                    } else {
                                                                        var13 = true;
                                                                    }
                                                                }
                                                                break;
                                                            }

                                                            if (Class163_Sub2_Sub1.aLongArray4017[var14] == var44) {
                                                                var13 = true;
                                                                break;
                                                            }

                                                            ++var14;
                                                        }

                                                        if (!var13 && 0 == inTutorialIsland) {
                                                            Class163_Sub2_Sub1.aLongArray4017[MouseListeningClass.anInt1921] = var44;
                                                            MouseListeningClass.anInt1921 = (MouseListeningClass.anInt1921 + 1) % 100;
                                                            var57 = Font.method686(Objects.requireNonNull(Class32.method992(BufferedDataStream.incomingBuffer).properlyCapitalize()));
                                                            if (clanChatIcon == 2 || clanChatIcon == 3) {
                                                                TextureOperation1.method221(-1, var57, RSString.stringCombiner(new RSString[]{RSString.parse("<img=1>"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}), Objects.requireNonNull(Unsorted.method1052(nameAsLong)).longToRSString(), 9);
                                                            } else if (clanChatIcon == 1) {
                                                                TextureOperation1.method221(-1, var57, RSString.stringCombiner(new RSString[]{RSString.parse("<img=0>"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}), Objects.requireNonNull(Unsorted.method1052(nameAsLong)).longToRSString(), 9);
                                                            } else {
                                                                if (clanChatIcon == 0)
                                                                    TextureOperation1.method221(-1, var57, Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString(), Objects.requireNonNull(Unsorted.method1052(nameAsLong)).longToRSString(), 9);
                                                                else
                                                                    TextureOperation1.method221(-1, var57, RSString.stringCombiner(new RSString[]{RSString.parse("<img=" + (clanChatIcon - 1) + ">"), Objects.requireNonNull(Unsorted.method1052(var2)).longToRSString()}), Objects.requireNonNull(Unsorted.method1052(nameAsLong)).longToRSString(), 9);
                                                            }
                                                        }

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 214) {
                                                        Class39.updateSceneGraph(true);
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 172) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                        if (nodeModelId == 65535) {
                                                            nodeModelId = -1;
                                                        }

                                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                        AudioHandler.soundEffectHandler(var19, nodeModelId, modelId);
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 66) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                        var19 = BufferedDataStream.incomingBuffer.readIntV1();
                                                        Class146.updateInterfacePacketCounter(nodeModelId);
                                                        modelId = 0;
                                                        if (Class102.player.class52 != null) {
                                                            modelId = Class102.player.class52.method1163();
                                                        }

                                                        method256(-1, 3, var19, modelId);

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 171) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readIntV2();
                                                        playerName = BufferedDataStream.incomingBuffer.readString();
                                                        modelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                        Class146.updateInterfacePacketCounter(modelId);
                                                        Unsorted.method566(playerName, nodeModelId);

                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else if (Unsorted.incomingOpcode == 84) {
                                                        nodeModelId = BufferedDataStream.incomingBuffer.readIntLE();
                                                        var19 = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                        Class163.method2209((byte) -106, nodeModelId, var19);
                                                        Unsorted.incomingOpcode = -1;
                                                        return true;
                                                    } else {
                                                        RSInterface var25;
                                                        if (Unsorted.incomingOpcode == 22) {
                                                            nodeModelId = BufferedDataStream.incomingBuffer.readInt();
                                                            var19 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                            if (nodeModelId < -70000) {
                                                                var19 += 32768;
                                                            }

                                                            if (nodeModelId < 0) {
                                                                var25 = null;
                                                            } else {
                                                                var25 = Unsorted.getRSInterface(nodeModelId);
                                                            }

                                                            for (; Unsorted.incomingPacketLength > BufferedDataStream.incomingBuffer.index; Class168.method2277(var6 + -1, counter, var30, var19, (byte) 46)) {
                                                                counter = BufferedDataStream.incomingBuffer.getSmart();
                                                                var6 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                                var30 = 0;
                                                                if (var6 != 0) {
                                                                    var30 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                                    if (var30 == 255) {
                                                                        var30 = BufferedDataStream.incomingBuffer.readInt();
                                                                    }
                                                                }

                                                                if (var25 != null && counter >= 0 && counter < var25.itemAmounts.length) {
                                                                    var25.itemAmounts[counter] = var6;
                                                                    var25.itemIds[counter] = var30;
                                                                }
                                                            }

                                                            if (var25 != null) {
                                                                Class20.method909(var25);
                                                            }

                                                            BufferedDataStream.method819();
                                                            QuickChatDefinition.anIntArray3565[Unsorted.bitwiseAnd(Unsorted.anInt944++, 31)] = Unsorted.bitwiseAnd(32767, var19);
                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 24) {
                                                            nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                            Class146.updateInterfacePacketCounter(nodeModelId);
                                                            Class3_Sub28_Sub5.method560();

                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 86) {
                                                            XPGainDraw.reset();
                                                            SlayerTracker.reset();
                                                            Discord.updatePresence("At the login screen","","");
                                                            Class167.method2269((byte) 46);
                                                            Unsorted.incomingOpcode = -1;
                                                            return false;
                                                        } else if (116 == Unsorted.incomingOpcode) {
                                                            nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                            if (BufferedDataStream.incomingBuffer.readUnsignedByte() == 0) {
                                                                TextureOperation29.aClass133Array3393[nodeModelId] = new Class133();
                                                            } else {
                                                                --BufferedDataStream.incomingBuffer.index;
                                                                TextureOperation29.aClass133Array3393[nodeModelId] = new Class133(BufferedDataStream.incomingBuffer);
                                                            }

                                                            Unsorted.incomingOpcode = -1;
                                                            Class121.anInt1642 = anInt3213;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 73) {//npc model
                                                            nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                            var19 = BufferedDataStream.incomingBuffer.readIntLE();
                                                            if (nodeModelId == 65535) {
                                                                nodeModelId = -1;
                                                            }

                                                            modelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                            Class146.updateInterfacePacketCounter(modelId);
                                                            method256(-1, 2, var19, nodeModelId);

                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 162) {
                                                            Class39.updateSceneGraph(false);
                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (165 == Unsorted.incomingOpcode) {
                                                            nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                            var19 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                            if (var19 == 65535) {
                                                                var19 = -1;
                                                            }

                                                            modelId = BufferedDataStream.incomingBuffer.readInt();
                                                            counter = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                                                            var6 = BufferedDataStream.incomingBuffer.readIntV1();
                                                            if (counter == 65535) {
                                                                counter = -1;
                                                            }

                                                            Class146.updateInterfacePacketCounter(nodeModelId);
                                                            for (var30 = counter; var30 <= var19; ++var30) {
                                                                var36 = ((long) modelId << 32) - -((long) var30);
                                                                var47 = (Class3_Sub1) Class124.aHashTable_1659.get(var36);
                                                                if (var47 == null) {
                                                                    if (-1 == var30) {
                                                                        var38 = new Class3_Sub1(var6, Objects.requireNonNull(Unsorted.getRSInterface(modelId)).aClass3_Sub1_257.anInt2202);
                                                                    } else {
                                                                        var38 = new Class3_Sub1(var6, -1);
                                                                    }
                                                                } else {
                                                                    var38 = new Class3_Sub1(var6, var47.anInt2202);
                                                                    var47.unlink();
                                                                }

                                                                Class124.aHashTable_1659.put(var36, var38);
                                                            }

                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 197) {
                                                            CS2Script.anInt1357 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                            Class110.anInt1472 = anInt3213;
                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 196) {
                                                            var2 = BufferedDataStream.incomingBuffer.readLong();
                                                            modelId = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                            byte var28 = BufferedDataStream.incomingBuffer.readSignedByte();
                                                            isIgnored = (Long.MIN_VALUE & var2) != 0;

                                                            if (isIgnored) {
                                                                if (Unsorted.clanSize == 0) {
                                                                    Unsorted.incomingOpcode = -1;
                                                                    return true;
                                                                }

                                                                var2 &= Long.MAX_VALUE;

                                                                for (var30 = 0; var30 < Unsorted.clanSize && (var2 != aClass3_Sub19Array3694[var30].linkableKey || aClass3_Sub19Array3694[var30].anInt2478 != modelId); ++var30) {
                                                                }

                                                                if (var30 < Unsorted.clanSize) {
                                                                    while (var30 < -1 + Unsorted.clanSize) {
                                                                        aClass3_Sub19Array3694[var30] = aClass3_Sub19Array3694[1 + var30];
                                                                        ++var30;
                                                                    }

                                                                    --Unsorted.clanSize;
                                                                    aClass3_Sub19Array3694[Unsorted.clanSize] = null;
                                                                }
                                                            } else {
                                                                var41 = BufferedDataStream.incomingBuffer.readString();
                                                                Class3_Sub19 var40 = new Class3_Sub19();
                                                                var40.linkableKey = var2;
                                                                var40.aClass94_2476 = Unsorted.method1052(var40.linkableKey);
                                                                var40.aByte2472 = var28;
                                                                var40.aClass94_2473 = var41;
                                                                var40.anInt2478 = modelId;

                                                                for (var33 = -1 + Unsorted.clanSize; var33 >= 0; --var33) {
                                                                    clanChatIcon = aClass3_Sub19Array3694[var33].aClass94_2476.method1559(var40.aClass94_2476);
                                                                    if (clanChatIcon == 0) {
                                                                        aClass3_Sub19Array3694[var33].anInt2478 = modelId;
                                                                        aClass3_Sub19Array3694[var33].aByte2472 = var28;
                                                                        aClass3_Sub19Array3694[var33].aClass94_2473 = var41;
                                                                        if (aLong3202 == var2) {
                                                                            Class91.aByte1308 = var28;
                                                                        }

                                                                        Class167.anInt2087 = anInt3213;
                                                                        Unsorted.incomingOpcode = -1;
                                                                        return true;
                                                                    }

                                                                    if (clanChatIcon < 0) {
                                                                        break;
                                                                    }
                                                                }

                                                                if (aClass3_Sub19Array3694.length <= Unsorted.clanSize) {
                                                                    Unsorted.incomingOpcode = -1;
                                                                    return true;
                                                                }

                                                                for (clanChatIcon = Unsorted.clanSize + -1; clanChatIcon > var33; --clanChatIcon) {
                                                                    aClass3_Sub19Array3694[1 + clanChatIcon] = aClass3_Sub19Array3694[clanChatIcon];
                                                                }

                                                                if (Unsorted.clanSize == 0) {
                                                                    aClass3_Sub19Array3694 = new Class3_Sub19[100];
                                                                }

                                                                aClass3_Sub19Array3694[1 + var33] = var40;
                                                                if (aLong3202 == var2) {
                                                                    Class91.aByte1308 = var28;
                                                                }

                                                                ++Unsorted.clanSize;
                                                            }

                                                            Unsorted.incomingOpcode = -1;
                                                            Class167.anInt2087 = anInt3213;
                                                            return true;
                                                        } else if (50 == Unsorted.incomingOpcode) {
                                                            nodeModelId = BufferedDataStream.incomingBuffer.readInt();
                                                            var19 = BufferedDataStream.incomingBuffer.readIntV2();
                                                            modelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                            if (65535 == modelId) {
                                                                modelId = -1;
                                                            }

                                                            counter = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                            Class146.updateInterfacePacketCounter(counter);
                                                            RSInterface var34 = Unsorted.getRSInterface(var19);
                                                            ItemDefinition var43;
                                                            if (Objects.requireNonNull(var34).usingScripts) {
                                                                Class140_Sub6.method2026(var19, nodeModelId, modelId);
                                                                var43 = ItemDefinition.getItemDefinition(modelId);
                                                                Unsorted.method2143((byte) -128, var43.anInt810, var19, var43.anInt799, var43.anInt786);
                                                                Class84.method1420(var19, var43.anInt768, var43.anInt754, var43.anInt792);
                                                            } else {
                                                                if (-1 == modelId) {
                                                                    var34.modelType = 0;
                                                                    Unsorted.incomingOpcode = -1;
                                                                    return true;
                                                                }

                                                                var43 = ItemDefinition.getItemDefinition(modelId);
                                                                var34.anInt182 = var43.anInt786;
                                                                var34.anInt164 = 100 * var43.anInt810 / nodeModelId;
                                                                var34.modelType = 4;
                                                                var34.itemId = modelId;
                                                                var34.anInt308 = var43.anInt799;
                                                                Class20.method909(var34);
                                                            }

                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 105) {
                                                            nodeModelId = BufferedDataStream.incomingBuffer.readInt();
                                                            var19 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                            if (nodeModelId < -70000) {
                                                                var19 += 32768;
                                                            }

                                                            if (0 <= nodeModelId) {
                                                                var25 = Unsorted.getRSInterface(nodeModelId);
                                                            } else {
                                                                var25 = null;
                                                            }

                                                            if (var25 != null) {
                                                                for (counter = 0; var25.itemAmounts.length > counter; ++counter) {
                                                                    var25.itemAmounts[counter] = 0;
                                                                    var25.itemIds[counter] = 0;
                                                                }
                                                            }

                                                            CS2Methods.method852(var19);
                                                            counter = BufferedDataStream.incomingBuffer.readUnsignedShort();

                                                            for (var6 = 0; counter > var6; ++var6) {
                                                                var30 = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                                                                if (255 == var30) {
                                                                    var30 = BufferedDataStream.incomingBuffer.readInt();
                                                                }

                                                                chatIcon = BufferedDataStream.incomingBuffer.readUnsignedShort();
                                                                if (null != var25 && var25.itemAmounts.length > var6) {
                                                                    var25.itemAmounts[var6] = chatIcon;
                                                                    var25.itemIds[var6] = var30;
                                                                }

                                                                Class168.method2277(-1 + chatIcon, var6, var30, var19, (byte) 41);
                                                            }

                                                            if (var25 != null) {
                                                                Class20.method909(var25);
                                                            }

                                                            BufferedDataStream.method819();
                                                            QuickChatDefinition.anIntArray3565[Unsorted.bitwiseAnd(Unsorted.anInt944++, 31)] = Unsorted.bitwiseAnd(32767, var19);
                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 142) {
                                                            LinkableRSString.method734(BufferedDataStream.incomingBuffer.readString());
                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 26) {
                                                            Class65.currentChunkX = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                                                            Class39.currentChunkY = BufferedDataStream.incomingBuffer.readUnsignedByte();
                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (4 == Unsorted.incomingOpcode) {
                                                            nodeModelId = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
                                                            if (nodeModelId == 65535) {
                                                                nodeModelId = -1;
                                                            }

                                                            AudioHandler.musicHandler(nodeModelId);
                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else if (Unsorted.incomingOpcode == 208) {
                                                            nodeModelId = BufferedDataStream.incomingBuffer.getTriByte2();
                                                            var19 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                                                            if (var19 == 65535) {
                                                                var19 = -1;
                                                            }

                                                            Class167.musicEffectHandler(var19);
                                                            Unsorted.incomingOpcode = -1;
                                                            return true;
                                                        } else {
                                                            Class49.reportError("T1 - " + Unsorted.incomingOpcode + "," + Class7.anInt2166 + "," + Class24.anInt469 + " - " + Unsorted.incomingPacketLength, null, (byte) 117);
                                                            Class167.method2269((byte) 46);
                                                            return true;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    static void method829() {
        try {
            Class20.method909(Class56.aClass11_886);
            ++Class75_Sub3.anInt2658;
            if (Class21.aBoolean440 && Class85.aBoolean1167) {
                int var1 = Class126.anInt1676;
                var1 -= Unsorted.anInt1881;
                if (TextureOperation20.anInt3156 > var1) {
                    var1 = TextureOperation20.anInt3156;
                }

                int var2 = Unsorted.anInt1709;
                if (var1 - -Class56.aClass11_886.width > TextureOperation20.anInt3156 + aClass11_88.width) {
                    var1 = -Class56.aClass11_886.width + TextureOperation20.anInt3156 + aClass11_88.width;
                }

                var2 -= Class95.anInt1336;
                if (Class134.anInt1761 > var2) {
                    var2 = Class134.anInt1761;
                }

                if (Class134.anInt1761 - -aClass11_88.height < var2 - -Class56.aClass11_886.height) {
                    var2 = Class134.anInt1761 + aClass11_88.height + -Class56.aClass11_886.height;
                }

                int var4 = var2 - Class3_Sub2.anInt2218;
                int var3 = var1 + -Class3_Sub15.anInt2421;
                int var6 = var1 + -TextureOperation20.anInt3156 + aClass11_88.anInt247;
                int var7 = aClass11_88.anInt208 + -Class134.anInt1761 + var2;
                int var5 = Class56.aClass11_886.anInt214;
                if (Class56.aClass11_886.anInt179 < Class75_Sub3.anInt2658 && (var3 > var5 || var3 < -var5 || var4 > var5 || var4 < -var5)) {
                    NPC.aBoolean3975 = true;
                }

                CS2Script var8;
                if (Class56.aClass11_886.anObjectArray295 != null && NPC.aBoolean3975) {
                    var8 = new CS2Script();
                    var8.aClass11_2449 = Class56.aClass11_886;
                    var8.arguments = Class56.aClass11_886.anObjectArray295;
                    var8.worldSelectCursorPositionX = var6;
                    var8.scrollbarScrollAmount = var7;
                    Class43.method1065(var8);
                }

                if (0 == TextureOperation21.anInt3069) {
                    if (NPC.aBoolean3975) {
                        if (Class56.aClass11_886.anObjectArray229 != null) {
                            var8 = new CS2Script();
                            var8.scrollbarScrollAmount = var7;
                            var8.aClass11_2438 = Class27.aClass11_526;
                            var8.worldSelectCursorPositionX = var6;
                            var8.arguments = Class56.aClass11_886.anObjectArray229;
                            var8.aClass11_2449 = Class56.aClass11_886;
                            Class43.method1065(var8);
                        }
                        RSInterface withInter = Class27.aClass11_526;
                        if (withInter == null) {
                            withInter = Class56.aClass11_886;
                        }
                        TextureOperation12.outgoingBuffer.putOpcode(79);
                        TextureOperation12.outgoingBuffer.writeIntV2(Class56.aClass11_886.componentHash);
                        TextureOperation12.outgoingBuffer.writeShortLE(withInter.anInt191);
                        TextureOperation12.outgoingBuffer.writeInt(withInter.componentHash);
                        TextureOperation12.outgoingBuffer.writeShortLE(Class56.aClass11_886.anInt191);

                        // && client.method42(Class56.aClass11_886) != null) {
                        if (Class27.aClass11_526 == null) {
                            System.out.println("Could not send switch item packet, second interface is null!");
                        } else if (Client.method42(Class56.aClass11_886) != null) {
                            System.out.println("Shouldn't be sending packet, enabled to fix banking tabs though.");
                        }
                    } else if ((Unsorted.anInt998 == 1 || TextureOperation8.method353(-1 + Unsorted.menuOptionCount, ~-1)) && Unsorted.menuOptionCount > 2) {
                        Class132.method1801();
                    } else if (Unsorted.menuOptionCount > 0) {
                        TextureOperation9.method203(96);
                    }

                    Class56.aClass11_886 = null;
                }

            } else if (Class75_Sub3.anInt2658 > 1) {
                Class56.aClass11_886 = null;
            }
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "ac.F(" + -1 + ')');
        }
    }

    static void method256(int var0, int var1, int var2, int var4) {
       try {
          InterfaceWidget var5 = InterfaceWidget.getWidget(4, var2);
          var5.flagUpdate();
          var5.anInt3597 = var4;
          var5.anInt3596 = var0;
          var5.anInt3598 = var1;
       } catch (RuntimeException var6) {
          throw ClientErrorException.clientError(var6, "ke.E(" + var0 + ',' + var1 + ',' + var2 + ',' + var4 + ')');
       }
    }

    static void method1605(RSString var1, int var2) {
        try {
            TextureOperation12.outgoingBuffer.putOpcode(188);
            TextureOperation12.outgoingBuffer.writeByte128(var2);
            TextureOperation12.outgoingBuffer.writeLong(var1.toLong());
        } catch (RuntimeException var4) {
            throw ClientErrorException.clientError(var4, "ni.B(" + 255 + ',' + (var1 != null ? "{...}" : "null") + ',' + var2 + ')');
        }
    }

    static void method834() {
        try {
            Unsorted.method1250(43, false);
            System.gc();
            Class117.method1719(25);

        } catch (RuntimeException var2) {
            throw ClientErrorException.clientError(var2, "af.D(" + (byte) -86 + ')');
        }
    }
}
