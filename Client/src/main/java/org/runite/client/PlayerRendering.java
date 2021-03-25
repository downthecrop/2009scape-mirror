package org.runite.client;

import java.util.Objects;

public final class PlayerRendering {


    static final int[] PLAYER_RENDER_LOG = new int[4];
    static DataBuffer playerRenderBuffer = new DataBuffer(new byte[5000]);

    static void renderPlayers() {
        try {
            Unsorted.maskUpdateCount = 0;
            Class139.anInt1829 = 0;

            updateLocalPosition();
            PLAYER_RENDER_LOG[0] = BufferedDataStream.incomingBuffer.index;

            renderLocalPlayers();
            PLAYER_RENDER_LOG[1] = BufferedDataStream.incomingBuffer.index;

            addLocalPlayers();
            PLAYER_RENDER_LOG[2] = BufferedDataStream.incomingBuffer.index;

            parsePlayerMasks();
            PLAYER_RENDER_LOG[3] = BufferedDataStream.incomingBuffer.index;

            int var1;
            for (var1 = 0; Class139.anInt1829 > var1; ++var1) {
                int var2 = Class3_Sub7.anIntArray2292[var1];
                if (Class44.anInt719 != Unsorted.players[var2].anInt2838) {
                    if (0 < Unsorted.players[var2].anInt3969) {
                        Class162.method2203(Unsorted.players[var2]);
                    }

                    Unsorted.players[var2] = null;
                }
            }

            if (BufferedDataStream.incomingBuffer.index == Unsorted.incomingPacketLength) {
                for (var1 = 0; var1 < Class159.localPlayerCount; ++var1) {
                    if (null == Unsorted.players[Class56.localPlayerIndexes[var1]]) {
                        //                     throw new RuntimeException("gpp2 pos:" + var1 + " size:" + Class159.anInt2022);
                        //                     System.err.println("gpp2 pos:" + var1 + " size:" + Class159.anInt2022);
                        System.err.println("Local player was null - index: " + Class56.localPlayerIndexes[var1] + ", list index: " + var1 + ", list size: " + Class159.localPlayerCount);
                    }
                }

            } else {
                System.err.println("Player rendering packet size mismatch - size log: self=" + PLAYER_RENDER_LOG[0] + ", local=" + PLAYER_RENDER_LOG[1] + ", add global=" + PLAYER_RENDER_LOG[2] + ", masks=" + PLAYER_RENDER_LOG[3] + ".");
                //               System.err.println("gpp1 pos:" + GraphicDefinition.incomingBuffer.index + " psize:" + Class130.incomingPacketLength);
                //                throw new RuntimeException("gpp1 pos:" + Class28.incomingBuffer.index + " psize:" + Class130.incomingPacketLength);
            }
        } catch (RuntimeException var3) {
            throw ClientErrorException.clientError(var3, "fb.B(" + (byte) -122 + ')');
        }
    }

    static void renderLocalPlayers() {
        try {
            int localPlayerAmount = BufferedDataStream.incomingBuffer.getBits(8);
            int var2;
            if (localPlayerAmount < Class159.localPlayerCount) {
                for (var2 = localPlayerAmount; Class159.localPlayerCount > var2; ++var2) {
                    Class3_Sub7.anIntArray2292[Class139.anInt1829++] = Class56.localPlayerIndexes[var2];
                }
            }

            if (Class159.localPlayerCount >= localPlayerAmount) {
                Class159.localPlayerCount = 0;
                var2 = 0;

                for (; localPlayerAmount > var2; ++var2) {
                    int var3 = Class56.localPlayerIndexes[var2];
                    Player var4 = Unsorted.players[var3];
                    int update = BufferedDataStream.incomingBuffer.getBits(1);
                    if (update == 0) {
                        Class56.localPlayerIndexes[Class159.localPlayerCount++] = var3;
                        var4.anInt2838 = Class44.anInt719;
                    } else {
                        int type = BufferedDataStream.incomingBuffer.getBits(2);
                        if (type == 0) {
                            Class56.localPlayerIndexes[Class159.localPlayerCount++] = var3;
                            var4.anInt2838 = Class44.anInt719;
                            Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = var3;
                        } else {
                            int var7;
                            int var8;
                            if (type == 1) {
                                Class56.localPlayerIndexes[Class159.localPlayerCount++] = var3;
                                var4.anInt2838 = Class44.anInt719;
                                var7 = BufferedDataStream.incomingBuffer.getBits(3);
                                var4.walkStep(1, (byte) 46, var7);
                                var8 = BufferedDataStream.incomingBuffer.getBits(1);
                                if (var8 == 1) {
                                    Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = var3;
                                }
                            } else if (type == 2) {
                                Class56.localPlayerIndexes[Class159.localPlayerCount++] = var3;
                                var4.anInt2838 = Class44.anInt719;
                                if (BufferedDataStream.incomingBuffer.getBits(1) == 1) {
                                    var7 = BufferedDataStream.incomingBuffer.getBits(3);
                                    var4.walkStep(2, (byte) -92, var7);
                                    var8 = BufferedDataStream.incomingBuffer.getBits(3);
                                    var4.walkStep(2, (byte) 88, var8);
                                } else {
                                    var7 = BufferedDataStream.incomingBuffer.getBits(3);
                                    var4.walkStep(0, (byte) 113, var7);
                                }

                                var7 = BufferedDataStream.incomingBuffer.getBits(1);
                                if (1 == var7) {
                                    Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = var3;
                                }
                            } else if (type == 3) {
                                Class3_Sub7.anIntArray2292[Class139.anInt1829++] = var3;
                            }
                        }
                    }
                }

            } else {
                throw new RuntimeException("gppov1");
            }
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "dc.B(" + false + ')');
        }
    }

    static void addLocalPlayers() {
        try {
            while (true) {
                if (BufferedDataStream.incomingBuffer.method815(Unsorted.incomingPacketLength) >= 11) {
                    int index = BufferedDataStream.incomingBuffer.getBits(11);
                    if (index != 2047) {
                        boolean var2 = false;
                        if (null == Unsorted.players[index]) {
                            Unsorted.players[index] = new Player();
                            var2 = true;
                            if (null != Class65.aClass3_Sub30Array986[index]) {
                                Unsorted.players[index].parseAppearance(-54, Class65.aClass3_Sub30Array986[index]);
                            }
                        }

                        Class56.localPlayerIndexes[Class159.localPlayerCount++] = index;
                        Player var3 = Unsorted.players[index];
                        var3.anInt2838 = Class44.anInt719;
                        int var4 = BufferedDataStream.incomingBuffer.getBits(1);
                        if (var4 == 1) {
                            Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = index;
                        }

                        int var5 = BufferedDataStream.incomingBuffer.getBits(5);
                        int var6 = Class27.anIntArray510[BufferedDataStream.incomingBuffer.getBits(3)];
                        if (var5 > 15) {
                            var5 -= 32;
                        }

                        if (var2) {
                            var3.anInt2806 = var3.anInt2785 = var6;
                        }

                        int var7 = BufferedDataStream.incomingBuffer.getBits(1);
                        int var8 = BufferedDataStream.incomingBuffer.getBits(5);
                        if (var8 > 15) {
                            var8 -= 32;
                        }

                        var3.method1981(var5 + Class102.player.anIntArray2767[0], var7 == 1, Class102.player.anIntArray2755[0] + var8);
                        continue;
                    }
                }

                BufferedDataStream.incomingBuffer.method818();
                return;
            }
        } catch (RuntimeException var9) {
            throw ClientErrorException.clientError(var9, "se.D(" + -59 + ')');
        }
    }

    static void parsePlayerMasks() {
        int var1 = 0;

        while (var1 < Unsorted.maskUpdateCount) {
            int var2 = Class21.maskUpdateIndexes[var1];
            Player var3 = Unsorted.players[var2];
            int var4 = BufferedDataStream.incomingBuffer.readUnsignedByte();
            if ((16 & var4) != 0) {
                var4 += BufferedDataStream.incomingBuffer.readUnsignedByte() << 8;
            }

            parsePlayerMask(var4, var2, var3);
            ++var1;
        }
    }

    static void parsePlayerMask(int var0, int var1, Player var3) {
        try {
            int var4;
            int chatIcon;
            int var7;
            //Ordinal: 0 Chat
            if (0 != (var0 & 128)) {
                var4 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                chatIcon = BufferedDataStream.incomingBuffer.readUnsignedByte();
                int var6 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                var7 = BufferedDataStream.incomingBuffer.index;
                boolean var8 = (32768 & var4) != 0;
                if (null != var3.displayName && var3.class52 != null) {
                    long var9 = var3.displayName.toLong();
                    boolean var11 = false;
                    if (chatIcon <= 1) {
                        if (!var8 && (Class3_Sub15.aBoolean2433 && !Class121.aBoolean1641 || TextureOperation31.aBoolean3166)) {
                            var11 = true;
                        } else {
                            for (int var12 = 0; var12 < Class3_Sub28_Sub5.anInt3591; ++var12) {
                                if (Class114.ignores[var12] == var9) {
                                    var11 = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (!var11 && 0 == PacketParser.inTutorialIsland) {
                        playerRenderBuffer.index = 0;
                        BufferedDataStream.incomingBuffer.readBytesReverse(var6, playerRenderBuffer.buffer);
                        playerRenderBuffer.index = 0;
                        int var13 = -1;
                        RSString message;
                        if (var8) {
                            QuickChat var14 = QuickChat.method2156(playerRenderBuffer);
                            var4 &= 32767;
                            var13 = var14.anInt149;
                            message = var14.aQuickChatDefinition_151.method555(playerRenderBuffer);
                        } else {
                            message = Font.method686(Objects.requireNonNull(Class32.method992(playerRenderBuffer).properlyCapitalize()));
                        }

                        var3.textSpoken = message.trim(1);
                        var3.textEffect = var4 & 0xFF;
                        var3.textCycle = 150;
                        var3.textColor = var4 >> 8;
                        if (chatIcon >= 2) {
                            MessageManager.sendGameMessage(var13, var8 ? 17 : 1, message, null, RSString.stringCombiner(new RSString[]{RSString.parse("<img=" + (chatIcon - 1) + ">"), var3.getName()}));
                        } else if (chatIcon == 1) {
                            MessageManager.sendGameMessage(var13, var8 ? 17 : 1, message, null, RSString.stringCombiner(new RSString[]{RSString.parse("<img=0>"), var3.getName()}));
                        } else {
                            MessageManager.sendGameMessage(var13, var8 ? 17 : 2, message, null, var3.getName());
                        }
                    }
                }
                BufferedDataStream.incomingBuffer.index = var7 + var6;
            }

            //Ordinal: 1 Hit
            if ((var0 & 1) != 0) {
                var4 = BufferedDataStream.incomingBuffer.getSmart();
                chatIcon = BufferedDataStream.incomingBuffer.readUnsignedByte128();
                var3.addHit(chatIcon, Class44.anInt719, var4);
                var3.anInt2781 = 300 + Class44.anInt719;
                var3.anInt2775 = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
            }

            //Ordinal: 2 Animation
            if ((var0 & 8) != 0) {
                var4 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                if (var4 == 65535) {
                    var4 = -1;
                }

                chatIcon = BufferedDataStream.incomingBuffer.readUnsignedByte();
                method628(chatIcon, var4, var3);
            }

            //Ordinal: 3 Appearance
            if (0 != (4 & var0)) {
                var4 = BufferedDataStream.incomingBuffer.readUnsignedByte128();
                byte[] var16 = new byte[var4];
                DataBuffer var19 = new DataBuffer(var16);
                BufferedDataStream.incomingBuffer.readBytes(var16, var4);
                Class65.aClass3_Sub30Array986[var1] = var19;
                var3.parseAppearance(-15, var19);
            }

            //Ordinal: 4 Face entity
            if ((2 & var0) != 0) {
                var3.anInt2772 = BufferedDataStream.incomingBuffer.readUnsignedShort128();
                if (var3.anInt2772 == 65535) {
                    var3.anInt2772 = -1;
                }
            }

            //Ordinal: 5 Force movement
            if ((1024 & var0) != 0) {
                var3.anInt2784 = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                var3.anInt2835 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                var3.anInt2823 = BufferedDataStream.incomingBuffer.readUnsignedByte128();
                var3.anInt2798 = BufferedDataStream.incomingBuffer.readUnsignedByte();
                var3.anInt2800 = BufferedDataStream.incomingBuffer.readUnsignedShortLE() + Class44.anInt719;
                var3.anInt2790 = BufferedDataStream.incomingBuffer.readUnsignedShortLE() - -Class44.anInt719;
                var3.anInt2840 = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                var3.anInt2816 = 1;
                var3.anInt2811 = 0;
            }

            //Ordinal: 6 Force chat
            if ((var0 & 32) != 0) {
                var3.textSpoken = BufferedDataStream.incomingBuffer.readString();
                if (var3.textSpoken.charAt(0, (byte) -45) == 126) {
                    var3.textSpoken = var3.textSpoken.substring(1);
                    BufferedDataStream.addChatMessage(var3.getName(), 2, var3.textSpoken, (byte) -79 ^ 78);
                } else if (var3 == Class102.player) {
                    BufferedDataStream.addChatMessage(var3.getName(), 2, var3.textSpoken, (byte) -79 + 78);
                }

                var3.textEffect = 0;
                var3.textColor = 0;
                var3.textCycle = 150;
            }

            //Ordinal: 7 Hit 2
            if ((var0 & 512) != 0) {
                var4 = BufferedDataStream.incomingBuffer.getSmart();
                chatIcon = BufferedDataStream.incomingBuffer.readUnsigned128Byte();
                var3.addHit(chatIcon, Class44.anInt719, var4);
            }

            //Ordinal: 8
            if ((2048 & var0) != 0) {
                var4 = BufferedDataStream.incomingBuffer.readUnsignedNegativeByte();
                int[] var18 = new int[var4];
                int[] var17 = new int[var4];
                int[] var20 = new int[var4];

                for (int var22 = 0; var22 < var4; ++var22) {
                    int var23 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                    if (65535 == var23) {
                        var23 = -1;
                    }

                    var18[var22] = var23;
                    var17[var22] = BufferedDataStream.incomingBuffer.readUnsignedByte128();
                    var20[var22] = BufferedDataStream.incomingBuffer.readUnsignedShort();
                }

                Class75_Sub1.method1342(var17, var18, var3, var20);
            }

            //Ordinal: 9 Graphic
            if ((256 & var0) != 0) {
                var4 = BufferedDataStream.incomingBuffer.readUnsignedShortLE();
                if (var4 == 65535) {
                    var4 = -1;
                }

                chatIcon = BufferedDataStream.incomingBuffer.readIntV2();
                boolean var21 = true;
                if (var4 != -1 && var3.anInt2842 != -1 && SequenceDefinition.getAnimationDefinition(GraphicDefinition.getGraphicDefinition((byte) 42, var4).anInt542).forcedPriority < SequenceDefinition.getAnimationDefinition(GraphicDefinition.getGraphicDefinition((byte) 42, var3.anInt2842).anInt542).forcedPriority) {
                    var21 = false;
                }

                if (var21) {
                    var3.anInt2759 = (chatIcon & 65535) + Class44.anInt719;
                    var3.anInt2761 = 0;
                    var3.anInt2805 = 0;
                    var3.anInt2842 = var4;
                    if (Class44.anInt719 < var3.anInt2759) {
                        var3.anInt2805 = -1;
                    }

                    var3.anInt2799 = chatIcon >> 16;
                    var3.anInt2826 = 1;
                    if (var3.anInt2842 != -1 && Class44.anInt719 == var3.anInt2759) {
                        var7 = GraphicDefinition.getGraphicDefinition((byte) 42, var3.anInt2842).anInt542;
                        if (var7 != -1) {
                            SequenceDefinition var24 = SequenceDefinition.getAnimationDefinition(var7);
                            if (var24.frames != null) {
                                Unsorted.method1470(var3.anInt2829, var24, 183921384, var3.anInt2819, var3 == Class102.player, 0);
                            }
                        }
                    }
                }
            }

            //Ordinal: 10 Face location
            if ((var0 & 64) != 0) {
                var3.anInt2786 = BufferedDataStream.incomingBuffer.readUnsignedShort();
                var3.anInt2762 = BufferedDataStream.incomingBuffer.readUnsignedShortLE128();
            }

        } catch (RuntimeException var15) {
            throw ClientErrorException.clientError(var15, "gk.A(" + var0 + ',' + var1 + ',' + (byte) -79 + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

    static void method628(int var1, int var2, Player var3) {
        try {

            if (var3.anInt2771 == var2 && var2 != -1) {
                SequenceDefinition var4 = SequenceDefinition.getAnimationDefinition(var2);
                int var5 = var4.delayType;
                if (1 == var5) {
                    var3.anInt2828 = var1;
                    var3.anInt2760 = 0;
                    var3.anInt2776 = 1;
                    var3.anInt2832 = 0;
                    var3.anInt2773 = 0;
                    Unsorted.method1470(var3.anInt2829, var4, 183921384, var3.anInt2819, Class102.player == var3, var3.anInt2832);
                }

                if (var5 == 2) {
                    var3.anInt2773 = 0;
                }
            } else if (-1 == var2 || var3.anInt2771 == -1 || SequenceDefinition.getAnimationDefinition(var2).forcedPriority >= SequenceDefinition.getAnimationDefinition(var3.anInt2771).forcedPriority) {
                var3.anInt2776 = 1;
                var3.anInt2832 = 0;
                var3.anInt2828 = var1;
                var3.anInt2811 = var3.anInt2816;
                var3.anInt2773 = 0;
                var3.anInt2760 = 0;
                var3.anInt2771 = var2;
                if (var3.anInt2771 != -1) {
                    Unsorted.method1470(var3.anInt2829, SequenceDefinition.getAnimationDefinition(var3.anInt2771), 183921384, var3.anInt2819, var3 == Class102.player, var3.anInt2832);
                }
            }

        } catch (RuntimeException var6) {
            throw ClientErrorException.clientError(var6, "pa.C(" + 0 + ',' + var1 + ',' + var2 + ',' + (var3 != null ? "{...}" : "null") + ')');
        }
    }

    public static void updateLocalPosition() {
        try {
            BufferedDataStream.incomingBuffer.setBitAccess();
            int opcode = BufferedDataStream.incomingBuffer.getBits(1);
            if (opcode != 0) {
                int type = BufferedDataStream.incomingBuffer.getBits(2);
                if (type == 0) {
                    Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = 2047;
                } else {
                    int var4;
                    int var5;
                    if (type == 1) { //Walk
                        var4 = BufferedDataStream.incomingBuffer.getBits(3);
                        Class102.player.walkStep(1, (byte) -128, var4);
                        var5 = BufferedDataStream.incomingBuffer.getBits(1);
                        if (var5 == 1) {
                            Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = 2047;
                        }

                    } else if (type == 2) {
                        if (BufferedDataStream.incomingBuffer.getBits(1) == 1) {
                            var4 = BufferedDataStream.incomingBuffer.getBits(3);
                            Class102.player.walkStep(2, (byte) -104, var4);
                            var5 = BufferedDataStream.incomingBuffer.getBits(3);
                            Class102.player.walkStep(2, (byte) -126, var5);
                        } else {
                            var4 = BufferedDataStream.incomingBuffer.getBits(3);
                            Class102.player.walkStep(0, (byte) -109, var4);
                        }

                        var4 = BufferedDataStream.incomingBuffer.getBits(1);
                        if (var4 == 1) {
                            Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = 2047;
                        }

                    } else if (type == 3) {
                        var4 = BufferedDataStream.incomingBuffer.getBits(7);
                        var5 = BufferedDataStream.incomingBuffer.getBits(1);
                        WorldListCountry.localPlane = BufferedDataStream.incomingBuffer.getBits(2);
                        int var6 = BufferedDataStream.incomingBuffer.getBits(1);
                        if (var6 == 1) {
                            Class21.maskUpdateIndexes[Unsorted.maskUpdateCount++] = 2047;
                        }

                        int var7 = BufferedDataStream.incomingBuffer.getBits(7);
                        Class102.player.method1981(var7, var5 == 1, var4);
                    }
                }
            }
        } catch (RuntimeException var8) {
            throw ClientErrorException.clientError(var8, "bg.G(" + (byte) 81 + ')');
        }
    }
}
