package rs09.net.packet.`in`

import core.game.node.entity.player.Player
import core.net.packet.IoBuffer
import core.tools.StringUtils
import rs09.game.system.SystemLogger
import java.io.PrintWriter
import java.io.StringWriter

enum class Decoders530(val opcode: Int) {
    /******************************************
     * ITEM INTERACTIONS
     ******************************************/
    ITEM_ACTION_1(156) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val slot = buffer.leShortA
            val itemId = buffer.shortA
            val ifHash = buffer.leInt
            val (iface, button) = deHash(ifHash)
            return Packet.ItemAction(player, 0, itemId, slot, iface, button)
        }
    },
    ITEM_ACTION_2(55) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val itemId = buffer.leShort
            val slot = buffer.shortA
            val ifHash = buffer.intA
            val (iface, button) = deHash(ifHash)
            return Packet.ItemAction(player, 1, itemId, slot, iface, button)
        }
    },
    ITEM_ACTION_3(153) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.leInt
            val slot = buffer.leShort
            val itemId = buffer.leShort
            val (iface, button) = deHash(ifHash)
            return Packet.ItemAction(player, 2, itemId, slot, iface, button)
        }
    },
    ITEM_ACTION_4(161) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.leInt
            val itemId = buffer.leShortA
            val slot = buffer.leShortA
            val (iface, button) = deHash(ifHash)
            return Packet.ItemAction(player, 3, itemId, slot, iface, button)
        }
    },
    ITEM_ACTION_5(135) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val itemId = buffer.shortA
            val slot = buffer.shortA
            val ifHash = buffer.intB
            val (iface, button) = deHash(ifHash)
            return Packet.ItemAction(player, 4, itemId, slot, iface, button)
        }
    },



    /******************************************
     * NPC INTERACTIONS
     ******************************************/
    NPC_ACTION_1(78) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val npcIndex = buffer.leShort
            return Packet.NpcAction(player, 0, npcIndex)
        }
    },
    NPC_ACTION_2(3) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val npcIndex = buffer.leShortA
            return Packet.NpcAction(player, 1, npcIndex)
        }
    },
    NPC_ACTION_3(148) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val npcIndex = buffer.shortA
            return Packet.NpcAction(player, 2, npcIndex)
        }
    },
    NPC_ACTION_4(30) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val npcIndex = buffer.short
            return Packet.NpcAction(player, 3, npcIndex)
        }
    },
    NPC_ACTION_5(218) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val npcIndex = buffer.leShort
            return Packet.NpcAction(player, 4, npcIndex)
        }
    },

    /******************************************
     * SCENERY INTERACTIONS
     ******************************************/
    SCENERY_ACTION_1(254) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val x = buffer.leShort
            val objId = buffer.shortA and 0xFFFF
            val y = buffer.short
            return Packet.SceneryAction(player, 0, objId, x, y)
        }
    },
    SCENERY_ACTION_2(194) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val y = buffer.leShortA
            val x = buffer.leShort
            val objId = buffer.short and 0xFFFF
            return Packet.SceneryAction(player, 1, objId, x, y)
        }
    },
    SCENERY_ACTION_3(84) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val objId = buffer.leShortA and 0xFFFF
            val y = buffer.leShortA
            val x = buffer.leShort
            return Packet.SceneryAction(player, 2, objId, x, y)
        }
    },
    SCENERY_ACTION_4(247) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val y = buffer.leShort and 0xFFFF
            val x = buffer.leShortA
            val objId = buffer.short and 0xFFFF
            return Packet.SceneryAction(player, 3, objId, x, y)
        }
    },
    SCENERY_ACTION_5(170) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val objId = buffer.leShortA and 0xFFFF
            val x = buffer.leShortA
            val y = buffer.leShortA
            return Packet.SceneryAction(player, 4, objId, x, y)
        }
    },

    /******************************************
     * PLAYER INTERACTIONS
     ******************************************/
    PLAYER_ACTION_1(68) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val pIndex = buffer.leShortA
            return Packet.PlayerAction(player, 0, pIndex)
        }
    },
    PLAYER_ACTION_3(71) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val pIndex = buffer.leShortA
            return Packet.PlayerAction(player, 2, pIndex)
        }
    },
    PLAYER_ACTION_4(180) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val pIndex = buffer.leShortA
            return Packet.PlayerAction(player, 3, pIndex)
        }
    },
    PLAYER_ACTION_7(114) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val index = buffer.leShortA
            return Packet.PlayerAction(player, 6, index)
        }
    },
    PLAYER_ACTION_8(175) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val pIndex = buffer.shortA
            return Packet.PlayerAction(player, 7, pIndex)
        }
    },

    /******************************************
     * OBJSTACK (Ground Item) INTERACTIONS
     ******************************************/
    OBJSTACK_ACTION_1(66) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val x = buffer.leShort
            val itemId = buffer.short
            val y = buffer.leShortA
            return Packet.GroundItemAction(player, 2, itemId, x, y)
        }
    },
    OBJSTACK_ACTION_2(33) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val itemId = buffer.short
            val x = buffer.short
            val y = buffer.leShort
            return Packet.GroundItemAction(player, 3, itemId, x, y)
        }
    },

    /******************************************
     * USEWITH INTERACTIONS
     ******************************************/
    USEWITH_NPC(115) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.intB
            val slot = buffer.leShort
            val npcIndex = buffer.leShort
            val itemId = buffer.leShortA
            val (iface, _) = deHash(ifHash)
            return Packet.UseWithNpc(player, itemId, npcIndex, iface, slot)
        }
    },
    USEWIH_PLAYER(248) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val otherIndex = buffer.leShortA
            val itemId = buffer.short
            val slot = buffer.short
            val ifHash = buffer.intB
            val (iface, _) = deHash(ifHash)
            return Packet.UseWithPlayer(player, itemId, otherIndex, iface, slot)
        }
    },
    USEWITH_ITEM(27) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val usedSlot = buffer.short
            val usedIfHash = buffer.leInt
            val usedWithSlot = buffer.leShort
            val usedWithIfHash = buffer.leInt
            val usedId = buffer.leShortA
            val usedWithId = buffer.leShortA

            val (usedIface, usedChild) = deHash(usedIfHash)
            val (usedWithIface, usedWithChild) = deHash(usedWithIfHash)
            return Packet.UseWithItem(
                player,
                usedId, usedWithId,
                usedSlot, usedWithSlot,
                usedIface, usedWithIface,
                usedChild, usedWithChild
            )
        }
    },
    USEWITH_SCENERY(134) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val x = buffer.shortA
            val id = buffer.short
            val y = buffer.leShort
            val slot = buffer.short
            buffer.leShort //Suspicious noops? TODO: FIND OUT WHAT THESE ARE
            buffer.short
            val sceneryId = buffer.shortA
            return Packet.UseWithScenery(player, id, slot, sceneryId, x, y)
        }
    },
    USEWITH_GROUNDITEM(101) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val x = buffer.leShortA
            val slot = buffer.leShort
            val usedId = buffer.leShort
            val usedWithId = buffer.leShort
            val y = buffer.leShortA
            val ifHash = buffer.intB
            val (iface,child) = deHash(ifHash)
            return Packet.UseWithGroundItem(player, usedId, usedWithId, iface, child, slot, x, y)
        }
    },

    /******************************************
     * INTERFACE INTERACTIONS
     ******************************************/
    IF_ACTION_1(155) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 0, iface, button, slot)
        }
    },
    IF_ACTION_2(196) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 1, iface, button, slot)
        }
    },
    IF_ACTION_3(124) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 2, iface, button, slot)
        }
    },
    IF_ACTION_4(199) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 3, iface, button, slot)
        }
    },
    IF_ACTION_5(234) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 4, iface, button, slot)
        }
    },
    IF_ACTION_6(168) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 5, iface, button, slot)
        }
    },
    IF_ACTION_7(166) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 6, iface, button, slot)
        }
    },
    IF_ACTION_8(64) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 7, iface, button, slot)
        }
    },
    IF_ACTION_9(53) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 8, iface, button, slot)
        }
    },
    IF_ACTION_10(9) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val slot = buffer.short
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 9, iface, button, slot)
        }
    },
    IF_ACTION_CS(10) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.int
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, -1, iface, button, -1, -1)
        }
    },
    CONTINUE_OPT(132) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.intA
            val slot = buffer.leShort
            val (iface, button) = deHash(ifHash)
            return Packet.ContinueOption(player, iface, button, slot, 132)
        }
    },
    CLOSE_IFACE(184) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.CloseIface(player)
        }
    },
    IF_GROUNDITEM_ACTION(73) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val ifHash = buffer.intA
            val (iface, child) = deHash(ifHash)
            val y = buffer.short
            val itemId = buffer.leShortA
            val x = buffer.leShortA
            val slot = buffer.leShort
            return Packet.ComponentGroundItemAction(player, iface, child, slot, itemId, x, y)
        }
    },
    IF_PLAYER_ACTION(195) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            buffer.shortA //Arios ignoring more data... nice
            val child = buffer.leShort
            val iface = buffer.leShort
            val otherIndex = buffer.leShortA
            return Packet.ComponentPlayerAction(player, otherIndex, iface, child)
        }
    },
    IF_SCENERY_ACTION(233) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val y = buffer.leShortA
            val x = buffer.shortA
            val itemId = buffer.leShortA //probably misnamed/mislabeled by Arios.
            val ifHash = buffer.intA
            val (iface, child) = deHash(ifHash)
            val objId = buffer.shortA
            return Packet.ComponentSceneryAction(player, iface, child, objId, x, y)
        }
    },
    IF_NPC_ACTION(239) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val child = buffer.leShort
            val iface = buffer.leShort
            buffer.shortA //more ignored data....
            val index = buffer.leShortA
            return Packet.ComponentNpcAction(player, iface, child, index)
        }
    },
    IF_ITEM_ACTION(253) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val child = buffer.leShort
            val iface = buffer.leShort
            val itemSlot = buffer.leShortA
            val unused = buffer.leInt //unused???
            val itemId = buffer.shortA
            buffer.shortA //more ignored data....
            return Packet.ComponentItemAction(player, iface, child, itemId, itemSlot)
        }
    },
    SLOTSWITCH_MULTI(79) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val srcHash = buffer.intB
            val destSlot = buffer.leShort
            val destHash = buffer.int
            val srcSlot = buffer.leShort
            val (srcIface, srcChild) = deHash(srcHash)
            val (destIface, destChild) = deHash(destHash)
            return Packet.SlotSwitchMultiComponent(player, srcIface, srcChild, srcSlot, destIface, destChild, destSlot)
        }
    },
    SLOTSWITCH_SINGLE(231) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val srcSlot = buffer.short
            val ifHash = buffer.leInt
            val destSlot = buffer.shortA
            val isInsert = buffer.get() == 1
            val (iface, child) = deHash(ifHash)
            return Packet.SlotSwitchSingleComponent(player, iface, child, srcSlot, destSlot, isInsert)
        }
    },
    IF_ITEM_OPT_1(81) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val slot = buffer.shortA
            val itemId = buffer.short
            val child = buffer.short
            val iface = buffer.short
            return Packet.IfAction(player, opcode, 0, iface, child, slot, itemId)
        }
    },
    IF_ITEM_OPT_2(206) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val itemId = buffer.shortA
            val slot = buffer.leShort
            val ifHash = buffer.leInt
            val (iface, button) = deHash(ifHash)
            return Packet.IfAction(player, opcode, 1, iface, button, slot, itemId)
        }
    },

    /******************************************
     * EXAMINE
     ******************************************/
    EXAMINE_SCENERY(94) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.SceneryExamine(player, buffer.leShortA)
        }
    },
    EXAMINE_ITEM(92) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.ItemExamine(player, buffer.leShortA)
        }
    },
    EXAMINE_NPC(72) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.NpcExamine(player, buffer.short)
        }
    },

    /******************************************
     * CLANS
     ******************************************/
    CLAN_JOIN(104) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val clanName = StringUtils.longToString(buffer.long)
            return Packet.JoinClan(player, clanName)
        }
    },
    CLAN_SETRANK(188) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val rank = buffer.a
            val name = StringUtils.longToString(buffer.long)
            return Packet.SetClanRank(player, name, rank)
        }
    },
    CLAN_KICK(162) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val name = StringUtils.longToString(buffer.long)
            return Packet.KickFromClan(player, name)
        }
    },

    /******************************************
     * FRIENDS/IGNORES
     ******************************************/
    ADD_FRIEND(120) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.AddFriend(player, StringUtils.longToString(buffer.long))
        }
    },
    REMOVE_FRIEND(57) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.RemoveFriend(player, StringUtils.longToString(buffer.long))
        }
    },
    ADD_IGNORE(34) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.AddIgnore(player, StringUtils.longToString(buffer.long))
        }
    },
    REMOVE_IGNORE(213) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.RemoveIgnore(player, StringUtils.longToString(buffer.long))
        }
    },
    PRIVATE_MESSAGE(201) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val name = StringUtils.longToString(buffer.long)
            val message = StringUtils.decryptPlayerChat(buffer, buffer.get() and 0xFF)
            return Packet.PrivateMessage(player, name, message)
        }
    },

    /******************************************
     * INPUT TRACKING
     ******************************************/
    FOCUS_CHANGE(22) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.TrackingFocus(player, buffer.get() == 1)
        }
    },
    CAMERA_MOVEMENT(21) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val x = buffer.shortA
            val y = buffer.leShort
            return Packet.TrackingCameraPos(player, x, y)
        }
    },
    DISPLAY_UPDATE(243) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val windowMode = buffer.get()
            val screenWidth = buffer.short
            val screenHeight = buffer.short
            val displayMode = buffer.get()
            return Packet.TrackingDisplayUpdate(player, windowMode, screenWidth, screenHeight, displayMode)
        }
    },
    AFK_TIMEOUT(245) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.TrackingAfkTimeout(player)
        }
    },
    MOUSE_CLICKED(75) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val data = buffer.leShortA
            val pos = buffer.intB
            val rightClick = ((data shr 15) and 0x1) == 1
            val delay = data and 0x7FF
            val x = pos shr 16
            val y = pos and 0xFFFF
            return Packet.TrackingMouseClick(player, x, y, rightClick, delay)
        }
    },

    /******************************************
     * WALKING
     ******************************************/
    WORLDSPACE_WALK(215) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val (running, x, y) = decodeWalkInformation(buffer, false)
            return Packet.WorldspaceWalk(player, x, y, running)
        }
    },
    MINIMAP_WALK(39) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val (running, x, y) = decodeWalkInformation(buffer, true)
            val clickedX = buffer.get()
            val clickedY = buffer.get()
            val rotation = buffer.short
            //Unlabeled data ignored by arios
            buffer.get()
            buffer.get()
            buffer.get()
            buffer.get()
            buffer.short
            buffer.short
            buffer.get()
            buffer.get()
            return Packet.MinimapWalk(player, x, y, clickedX, clickedY, rotation, running)
        }
    },
    INTERACT_WALK(77) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val (running, x, y) = decodeWalkInformation(buffer, false)
            return Packet.InteractWalk(player, x, y, running)
        }
    },

    /******************************************
     * INPUT PROMPT RESPONSE
     ******************************************/
    INPUT_SHORT_STRING_RESPONSE(244) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.InputPromptResponse(player, StringUtils.longToString(buffer.long))
        }
    },
    INPUT_LONG_STRING_RESPONSE(65) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.InputPromptResponse(player, buffer.string)
        }
    },
    INPUT_INT_RESPONSE(23) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.InputPromptResponse(player, buffer.int)
        }
    },

    /******************************************
     * ASSORTED
     ******************************************/
    GE_SET_OFFER_ITEM(111) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val itemId = buffer.short
            return Packet.GESetOfferItem(player, itemId)
        }
    },
    COMMAND(44) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            if (buffer.toByteBuffer().remaining() > 1) {
                val message = buffer.string.lowercase()
                return Packet.Command(player, message)
            }
            return Packet.NoProcess()
        }
    },
    CHAT_SETTINGS(157) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.ChatSetting(
                player,
                buffer.get(),
                buffer.get(),
                buffer.get()
            )
        }
    },
    CHAT_MESSAGE(237) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val effects = buffer.short
            val numChars = buffer.smart
            val message = StringUtils.decryptPlayerChat(buffer, numChars)
            return Packet.ChatMessage(player, effects, message)
        }
    },
    MUSIC_TRACK_FINISHED(137) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val trackId = buffer.leShortA
            return Packet.TrackFinished(player, trackId)
        }
    },
    REPORT_ABUSE(99) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val target = StringUtils.longToString(buffer.long)
            val ruleId = buffer.get()
            val modMute = buffer.get() == 1
            return Packet.ReportAbuse(player, target, ruleId, modMute)
        }
    },
    UPDATE_PACKET_COUNT(177) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val count = buffer.short
            return Packet.PacketCountUpdate(player, count)
        }
    },
    PING(93) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.Ping(player)
        }
    },
    QUICKCHAT(167) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val x = buffer.toByteBuffer()

            val packetType = when(x.array().size){
                3,4 -> QCPacketType.STANDARD
                5 -> QCPacketType.SINGLE
                7 -> QCPacketType.DOUBLE
                else -> QCPacketType.UNHANDLED.also { SystemLogger.logWarn(this::class.java, "UNHANDLED QC PACKET TYPE Size ${x.array().size}") }
            }

            val forClan = (buffer.get() and 0xFF) == 1
            val multiplier: Int = buffer.get()
            val offset: Int = buffer.get()
            var selection_a_index = -1
            var selection_b_index = -1

            when(packetType){
                QCPacketType.SINGLE -> {
                    selection_a_index = buffer.short
                }
                QCPacketType.DOUBLE -> {
                    buffer.get() //discard
                    selection_a_index = buffer.get()
                    buffer.get() //discard
                    selection_b_index = buffer.get()
                }
                QCPacketType.UNHANDLED -> SystemLogger.logWarn(this::class.java, "Unhandled packet type, skipping remaining buffer contents.")
            }
            return Packet.QuickChat(player, selection_a_index, selection_b_index, forClan, multiplier, offset, packetType)
        }
    },
    MAP_REBUILD_STARTED(20) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.NoProcess()
        }
    },
    MAP_REBUILD_FINISHED(110) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            return Packet.NoProcess()
        }
    },
    PLAYER_PREFS_UPDATE(98) {
        override fun decode(player: Player, buffer: IoBuffer): Packet {
            val prefs = buffer.int
            val particleSetting =       prefs shr 23
            val ambienceEnabled =       (prefs shr 22) and 1 == 1
            val musicEnabled =          (prefs shr 21) and 1 == 1
            val soundEnabled =          (prefs shr 20) and 1 == 1
            val stereo =                (prefs shr 19) and 1 == 1
            val fog =                   (prefs shr 16) and 1 == 1
            val hdWater =               (prefs shr 15) and 1 == 1
            val hdLighting =            (prefs shr 13) and 1 == 1
            val shadowType =            (prefs shr 11) and 0x3
            val charShadows =           (prefs shr 10) and 1 == 1
            val manyGroundTextures =    (prefs shr 9) and 1 == 1
            val flickeringEffects =     (prefs shr 8) and 1 == 1
            val manyIdleAnims =         (prefs shr 7) and 1 == 1
            val hdTextures =            (prefs shr 6) and 1 == 1
            val showGroundDeco =        (prefs shr 5) and 1 == 1
            val selectiveRoofs =        (prefs shr 4) and 1 == 1
            val allLayersVisible =      (prefs shr 3) and 1 == 1
            val brightness =            prefs and 0x7;
            //added in the above so that we don't have to figure it out later, not doing more
            //because we currently have genuinely no use for this information.
            return Packet.PlayerPrefsUpdate(player, prefs)
        }
    }
    ;
    abstract fun decode(player: Player, buffer: IoBuffer): Packet

    fun deHash(ifHash: Int) : Pair<Int,Int> {
        return Pair(ifHash shr 16, ifHash and 0xFFFF)
    }

    fun decodeWalkInformation(buffer: IoBuffer, isMinimap: Boolean) : Triple<Boolean,Int,Int> {
        val isRunning = buffer.a == 1
        var x = buffer.short
        var y = buffer.shortA
        val steps = (buffer.toByteBuffer().remaining() - if (isMinimap) 14 else 0) shr 1
        //derive the final destination by poking the last set of values in the packet (client does its own pathfinding and reports it)
        for (i in 0 until steps) {
            val offsetX = buffer.a
            val offsetY = buffer.s
            if (i == steps - 1){
                x += offsetX
                y += offsetY
            }
        }
        return Triple(isRunning, x, y)
    }

    companion object {
        private val opcodeMap = values().associateBy { it.opcode }

        @JvmStatic fun process(player: Player, opcode: Int, buffer: IoBuffer) : Packet {
            val decoder = opcodeMap[opcode] ?: return Packet.UnhandledOp()

            return try {
                decoder.decode(player, buffer)
            } catch (e: Exception) {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                e.printStackTrace(pw)
                Packet.DecodingError("Error decoding opcode $opcode/${decoder.name}: $sw")
            }
        }
    }
}