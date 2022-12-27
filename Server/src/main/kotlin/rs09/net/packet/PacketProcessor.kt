package rs09.net.packet

import api.events.ButtonClickEvent
import api.getAttribute
import api.sendMessage
import api.tryPop
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.container.Container
import core.game.container.impl.BankContainer
import core.game.content.global.report.AbuseReport
import core.game.content.global.report.Rule
import core.game.content.quest.PluginInteractionManager
import core.game.interaction.Interaction
import core.game.interaction.MovementPulse
import core.game.interaction.NodeUsageEvent
import core.game.interaction.Option
import core.game.interaction.UseWithHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.info.login.LoginConfiguration
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.skill.magic.MagicSpell
import core.game.node.entity.skill.summoning.familiar.FamiliarSpecial
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.communication.ClanRank
import core.game.system.communication.CommunicationInfo
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.ChatMessage
import core.game.world.update.flag.player.ChatFlag
import core.net.amsc.MSPacketRepository
import core.net.packet.PacketRepository
import core.net.packet.context.PlayerContext
import core.net.packet.out.ClearMinimapFlag
import discord.Discord
import org.rs09.consts.Components
import proto.management.ClanMessage
import proto.management.JoinClanRequest
import proto.management.LeaveClanRequest
import rs09.ServerConstants
import rs09.game.ge.GrandExchange.Companion.getOfferStats
import rs09.game.ge.GrandExchange.Companion.getRecommendedPrice
import rs09.game.ge.GrandExchangeOffer
import rs09.game.ge.PriceIndex
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListeners
import rs09.game.interaction.InterfaceListeners
import rs09.game.interaction.QCRepository
import rs09.game.interaction.inter.ge.StockMarket
import rs09.game.node.entity.player.info.PlayerMonitor
import rs09.game.node.entity.skill.magic.SpellListener
import rs09.game.node.entity.skill.magic.SpellListeners
import rs09.game.node.entity.skill.magic.SpellUtils
import rs09.game.system.SystemLogger
import rs09.game.system.command.CommandSystem
import rs09.game.world.GameWorld
import rs09.game.world.repository.Repository
import rs09.net.packet.`in`.Packet
import rs09.net.packet.`in`.RunScript
import rs09.worker.ManagementEvents
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

object PacketProcessor {
    val queue = LinkedList<Packet>()

    @JvmStatic fun enqueue(pkt: Packet) {
        queue.addLast(pkt)
    }

    @JvmStatic fun processQueue() {
        var countThisCycle = queue.size
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        var pkt: Packet
        while (countThisCycle-- > 0) {
            pkt = queue.tryPop(Packet.NoProcess())
            try {
                process(pkt)
            } catch (e: Exception) {
                e.printStackTrace(pw)
                SystemLogger.logErr(this::class.java, "Error Processing ${pkt::class.java.simpleName}: $sw")
            }
        }
    }

    private fun process(pkt: Packet) {
        when (pkt) {
            is Packet.ItemAction -> processItemAction(pkt)
            is Packet.NpcAction -> processNpcAction(pkt)
            is Packet.SceneryAction -> processSceneryAction(pkt)
            is Packet.PlayerAction -> processPlayerAction(pkt)
            is Packet.GroundItemAction -> processGroundItemAction(pkt)
            is Packet.UseWithNpc,
            is Packet.UseWithItem,
            is Packet.UseWithScenery,
            is Packet.UseWithGroundItem,
            is Packet.UseWithPlayer -> processUseWith(pkt)
            is Packet.IfAction -> processIfAction(pkt)
            is Packet.CloseIface -> processCloseIface(pkt)
            is Packet.WorldspaceWalk,
            is Packet.MinimapWalk,
            is Packet.InteractWalk -> processWalkPacket(pkt)
            is Packet.AddFriend -> CommunicationInfo.add(pkt.player, pkt.username)
            is Packet.RemoveFriend -> CommunicationInfo.remove(pkt.player, pkt.username, false)
            is Packet.AddIgnore -> CommunicationInfo.block(pkt.player, pkt.username)
            is Packet.RemoveIgnore -> CommunicationInfo.remove(pkt.player, pkt.username, true)
            is Packet.ComponentItemAction,
            is Packet.ComponentPlayerAction,
            is Packet.ComponentNpcAction,
            is Packet.ComponentSceneryAction,
            is Packet.ComponentGroundItemAction -> processComponentUseWith(pkt)
            is Packet.SlotSwitchSingleComponent,
            is Packet.SlotSwitchMultiComponent -> processSlotSwitch(pkt)
            is Packet.TrackingMouseClick,
            is Packet.TrackingAfkTimeout,
            is Packet.TrackingFocus,
            is Packet.TrackingCameraPos,
            is Packet.TrackingDisplayUpdate -> processTrackingPacket(pkt)
            is Packet.PlayerPrefsUpdate -> {/*TODO implement something that cares about this */}
            is Packet.Ping -> pkt.player.session.lastPing = System.currentTimeMillis()
            is Packet.JoinClan -> {
                if (pkt.clanName.isEmpty() && pkt.player.communication.currentClan.isNotEmpty()) {
                    val builder = LeaveClanRequest.newBuilder()
                    builder.clanName = pkt.player.communication.currentClan
                    builder.username = pkt.player.name
                    ManagementEvents.publish(builder.build())
                    return
                }
                sendMessage(pkt.player, "Attempting to join channel....:clan:")
                val builder = JoinClanRequest.newBuilder()
                builder.clanName = pkt.clanName
                builder.username = pkt.player.name
                ManagementEvents.publish(builder.build())
            }
            is Packet.SetClanRank -> {
                CommunicationInfo.updateClanRank(pkt.player, pkt.username, ClanRank.values()[pkt.rank + 1])
            }
            is Packet.KickFromClan -> {
                val clan = pkt.player.communication.clan ?: return
                val target = Repository.getPlayerByName(pkt.username) ?: return
                clan.kick(pkt.player, target)
            }
            is Packet.QuickChat -> QCRepository.sendQC(pkt.player, pkt.multiplier, pkt.offset, pkt.type, pkt.indexA, pkt.indexB, pkt.forClan)
            is Packet.InputPromptResponse -> {
                val script: ((Any) -> Boolean) = pkt.player.getAttribute<((Any) -> Boolean)?>("runscript", null) ?: return
                if (pkt.player.locks.isInteractionLocked)
                    return
                try {
                    RunScript.processInput(pkt.player, pkt.response, script)
                } finally {
                    pkt.player.removeAttribute("parseamount")
                    pkt.player.removeAttribute("runscript")
                }
            }
            is Packet.ReportAbuse -> {
                if (!GameWorld.accountStorage.checkUsernameTaken(pkt.target.lowercase())) {
                    sendMessage(pkt.player, "Invalid player name.")
                    return
                }
                if (pkt.target.equals(pkt.player.username, true)){
                    pkt.player.sendMessage("You can't report yourself!")
                }
                AbuseReport (
                    pkt.player.name,
                    pkt.target,
                    Rule.forId(pkt.rule)
                ).construct(pkt.player, pkt.modMute)
            }
            is Packet.TrackFinished -> {
                if (pkt.player.musicPlayer.isLooping)
                    pkt.player.musicPlayer.replay()
                else
                    pkt.player.musicPlayer.isPlaying = false
            }
            is Packet.GESetOfferItem -> {
                val offer = pkt.player.getAttribute("ge-temp", GrandExchangeOffer())
                val index = pkt.player.getAttribute("ge-index", -1)
                offer.itemID = pkt.itemId
                offer.sell = false
                if (!PriceIndex.canTrade(pkt.itemId)) {
                    sendMessage(pkt.player, "That item is blacklisted from the grand exchange.")
                    return
                }
                offer.player = pkt.player
                offer.amount = 1
                offer.offeredValue = getRecommendedPrice(pkt.itemId, false)
                offer.index = index
                StockMarket.updateVarbits(pkt.player, offer, index, false)
                pkt.player.setAttribute("ge-temp", offer)
                pkt.player.packetDispatch.sendString(getOfferStats(pkt.itemId, false), 105, 142)
                pkt.player.interfaceManager.closeChatbox()
            }
            is Packet.Command -> {
                PlayerMonitor.logMisc(pkt.player, "CommandUse", pkt.commandLine)
                CommandSystem.commandSystem.parse(pkt.player, pkt.commandLine)
            }
            is Packet.ChatMessage -> {
                if (pkt.player.details.isMuted)
                    pkt.player.sendMessage("You have been muted due to breaking a rule.")
                else {
                    if (pkt.message.startsWith("/") && pkt.player.communication.clan != null) {
                        val builder = ClanMessage.newBuilder()
                        builder.sender = pkt.player.name
                        builder.clanName = pkt.player.communication.clan.owner.lowercase().replace(" ", "_")
                        builder.message = pkt.message.substring(1)
                        builder.rank = pkt.player.rights.ordinal
                        ManagementEvents.publish(builder.build())
                        return
                    }
                    PlayerMonitor.logChat(pkt.player, "public", pkt.message)
                    val ctx = ChatMessage(pkt.player, pkt.message, pkt.effects, pkt.message.length)
                    pkt.player.updateMasks.register(ChatFlag(ctx))
                }
            }
            is Packet.ChatSetting -> {
                MSPacketRepository.sendChatSetting(pkt.player, pkt.public, pkt.private, pkt.trade)
                pkt.player.settings.updateChatSettings(pkt.public, pkt.private, pkt.trade)
            }
            is Packet.PrivateMessage -> {
                if (pkt.player.details.isMuted)
                    pkt.player.sendMessage("You have been muted due to breaking a rule.")
                else
                    CommunicationInfo.sendMessage(pkt.player, pkt.username, pkt.message)
                PlayerMonitor.logPrivateChat(pkt.player, pkt.username, pkt.message)
            }
            is Packet.PacketCountUpdate -> {
                val final = pkt.count - pkt.player.interfaceManager.getPacketCount(0)
                pkt.player.interfaceManager.getPacketCount(final)
            }
            is Packet.ContinueOption -> {
                val player = pkt.player
                player.debug("[CONTINUE OPT]----------")
                player.debug("Iface: ${pkt.iface}")
                player.debug("Child: ${pkt.child}")
                player.debug("Slot: ${pkt.slot}")
                player.debug("------------------------")
                if (player.dialogueInterpreter.dialogue == null) {
                    player.interfaceManager.closeChatbox()
                    player.dialogueInterpreter.actions.removeFirstOrNull()?.handle(player, pkt.child)
                    val component = player.interfaceManager.getComponent(pkt.iface) ?: return
                    if (!InterfaceListeners.run(player, component, pkt.opcode, pkt.child, pkt.slot, -1))
                        component.plugin?.handle(player, component, pkt.opcode, pkt.child, pkt.slot, -1)
                    return
                }
                player.dialogueInterpreter.handle(pkt.iface, pkt.child)
            }
            is Packet.ItemExamine -> {
                val def = ItemDefinition.forId(pkt.id) ?: return
                pkt.player.debug("[ITEM] ID: ${pkt.id} Value: ${def.value}")
                pkt.player.sendMessage(def.examine)
            }
            is Packet.SceneryExamine -> {
                val def = SceneryDefinition.forId(pkt.id) ?: return
                pkt.player.debug("[SCENERY]---------------------")
                pkt.player.debug("ID: ${pkt.id}")
                if (def.configFile != null)
                    pkt.player.debug("Varbit: ${def.configFile.id}")
                pkt.player.debug("------------------------------")
                pkt.player.sendMessage(def.examine)
            }
            is Packet.NpcExamine -> {
                val def = NPCDefinition.forId(pkt.id)
                pkt.player.debug("[NPC]-------------------------")
                pkt.player.debug("ID: ${pkt.id}")
                if (def.configFileId != -1)
                    pkt.player.debug("Varbit: ${def.configFileId}")
                pkt.player.debug("------------------------------")
                pkt.player.sendMessage(def.examine)
            }


            else -> SystemLogger.logWarn(this::class.java, "Unprocessed Packet: ${pkt::class.java.simpleName}")
        }
    }

    private fun processTrackingPacket(pkt: Packet) {
        when(pkt) {
            is Packet.TrackingFocus -> {}
            is Packet.TrackingDisplayUpdate -> {
                pkt.player.session.clientInfo.screenWidth = pkt.screenWidth
                pkt.player.session.clientInfo.screenHeight = pkt.screenHeight
                pkt.player.session.clientInfo.displayMode = pkt.displayMode
                pkt.player.interfaceManager.switchWindowMode(pkt.windowMode)
            }
            is Packet.TrackingAfkTimeout -> {
                if (pkt.player.details.rights != Rights.ADMINISTRATOR)
                    pkt.player.packetDispatch.sendLogout()
            }
            is Packet.TrackingCameraPos -> {
                //TODO Refactor the player monitor to be actually useful and log this
            }
            is Packet.TrackingMouseClick -> {
                //TODO see above todo

            }
        }
    }

    private fun processSlotSwitch(pkt: Packet) {
        //TODO refactor this to function as callbacks to InterfaceListeners, e.g. onSlotSwitch
        //TODO See above TODO, I hate Arios. This was hardcoded in the packet decoder.
        if (pkt is Packet.SlotSwitchMultiComponent) {
            if (pkt.destIface == 762) {
                if (pkt.destChild == 73) {
                    val container = pkt.player.bank
                    switchItem(pkt.sourceSlot, pkt.destSlot, container, pkt.player.bank.isInsertItems, pkt.player)
                } else {
                    val tabIndex = BankContainer.getArrayIndex(pkt.destChild)
                    if (tabIndex > -1) {
                        val secondSlot =
                            if (tabIndex == 10) pkt.player.bank.freeSlot() else pkt.player.bank.tabStartSlot[tabIndex] + pkt.player.bank.getItemsInTab(tabIndex)
                        val inSlot: Item = pkt.player.bank.get(pkt.sourceSlot)
                        if (secondSlot == -1 && pkt.player.bank.remove(inSlot)) {
                            pkt.player.bank.add(inSlot)
                            return
                        }
                        val childId = pkt.player.bank.getTabByItemSlot(pkt.sourceSlot)
                        if (secondSlot > pkt.sourceSlot) {
                            pkt.player.bank.insert(pkt.sourceSlot, secondSlot - 1)
                        } else if (pkt.sourceSlot > secondSlot) {
                            pkt.player.bank.insert(pkt.sourceSlot, secondSlot)
                        }
                        pkt.player.bank.increaseTabStartSlots(tabIndex)
                        pkt.player.bank.decreaseTabStartSlots(childId)
                        pkt.player.bank.setTabConfigurations()
                        return
                    }
                }
                return
            } else {
                switchItem(pkt.sourceSlot, pkt.destSlot, pkt.player.inventory, false, pkt.player)
            }
        }
        else if (pkt is Packet.SlotSwitchSingleComponent) {
            val container = if (pkt.iface == 762) pkt.player.bank else pkt.player.inventory
            switchItem(pkt.sourceSlot, pkt.destSlot, container, pkt.isInsert, pkt.player)
        }
    }

    private fun processComponentUseWith(pkt: Packet) {
        val iface: Int
        val child: Int
        val target: Node
        val targetId: Int
        val player: Player
        val type: Int
        if (pkt is Packet.ComponentGroundItemAction) {
            player = pkt.player
            child = pkt.child
            iface = pkt.iface
            target = GroundItemManager.get(pkt.itemId, Location.create(pkt.x, pkt.y, player.location.z), player) ?: return sendClearMinimap(player).also { sendMessage(player, "Too late!") }
            targetId = pkt.itemId
            type = SpellListener.GROUND_ITEM
        }
        else if (pkt is Packet.ComponentPlayerAction) {
            player = pkt.player
            child = pkt.child
            iface = pkt.iface
            target = Repository.players[pkt.otherIndex] ?: return sendClearMinimap(player)
            targetId = target.id
            type = SpellListener.PLAYER
        }
        else if (pkt is Packet.ComponentSceneryAction) {
            player = pkt.player
            child = pkt.child
            iface = pkt.iface
            target = RegionManager.getObject(player.location.z, pkt.x, pkt.y) ?: return sendClearMinimap(player)
            targetId = pkt.sceneryId
            type = SpellListener.OBJECT
        }
        else if (pkt is Packet.ComponentNpcAction) {
            if (pkt.npcIndex !in 1 until ServerConstants.MAX_NPCS)
                return sendClearMinimap(pkt.player)
            player = pkt.player
            child = pkt.child
            iface = pkt.iface
            target = Repository.npcs[pkt.npcIndex] ?: return sendClearMinimap(player)
            targetId = target.id
            type = SpellListener.NPC
        }
        else {
            if (pkt !is Packet.ComponentItemAction) return
            player = pkt.player
            child = pkt.child
            iface = pkt.iface
            target = player.inventory[pkt.slot] ?: return
            targetId = pkt.itemId
            type = SpellListener.ITEM
        }

        if (targetId != target.id)
            return
        if (player.getAttribute("magic:delay", -1) > GameWorld.ticks)
            return
        val book = SpellUtils.getBookFromInterface(iface)
        if (book != "none")
            SpellListeners.run(child, type, book, player, target)
        when (iface) {
            430,192 -> MagicSpell.castSpell(player, SpellBookManager.SpellBook.forInterface(iface), child, target)
            662 -> {
                if (player.familiarManager.hasFamiliar())
                    player.familiarManager.familiar.executeSpecialMove(FamiliarSpecial(target, iface, child, target as? Item))
                else
                    player.sendMessage("You don't have a familiar.")
            }
        }
    }

    private fun processWalkPacket(pkt: Packet) {
        val player: Player
        val x: Int
        val y: Int
        val isRunning: Boolean
        if (pkt is Packet.WorldspaceWalk) {
            player = pkt.player
            x = pkt.destX
            y = pkt.destY
            isRunning = pkt.isRun
        }
        else if (pkt is Packet.InteractWalk) {
            player = pkt.player
            x = pkt.destX
            y = pkt.destY
            isRunning = pkt.isRun
        }
        else {
            if (pkt !is Packet.MinimapWalk) return
            player = pkt.player
            x = pkt.destX
            y = pkt.destY
            isRunning = pkt.isRun
            //there's more data in this packet, we're just not using it
        }

        var canWalk = !player.locks.isMovementLocked

        if (canWalk && player.interfaceManager.isOpened && !player.interfaceManager.opened.definition.isWalkable)
            canWalk = canWalk && player.interfaceManager.close()
        if (canWalk && player.interfaceManager.hasChatbox() && !player.interfaceManager.chatbox.definition.isWalkable)
            player.interfaceManager.closeChatbox()

        if (!canWalk || !player.dialogueInterpreter.close()) {
            player.debug("[WALK ACTION]-- NO HANDLE: PLAYER LOCKED OR INTERFACES SAY NO")
            return sendClearMinimap(player)
        }

        if (pkt is Packet.InteractWalk) {
            //Arios did this, so we're replicating it for now. Probably wrong.
            player.walkingQueue.isRunning = isRunning
            return
        }

        player.face(null)
        player.faceLocation(null)

        player.pulseManager.run(object : MovementPulse(player, Location.create(x,y,player.location.z), isRunning) {
            override fun pulse(): Boolean {
                if (isRunning)
                    player.walkingQueue.isRunning = false
                return true
            }
        })
    }

    private fun processCloseIface(pkt: Packet.CloseIface) {
        val player = pkt.player
        player.interfaceManager.close()
        if (player.getAttribute<Boolean>("logging_in") != null) {
            GameWorld.Pulser.submit(object : Pulse() {
                override fun pulse(): Boolean {
                    player.removeAttribute("logging_in")
                    LoginConfiguration.configureGameWorld(player)
                    return true
                }
            })
        }
        if (player.getAttribute<Any?>("worldMap:viewing") != null) {
            player.removeAttribute("worldMap:viewing")
            player.packetDispatch.sendWindowsPane(if (player.interfaceManager.isResizable) 746 else 548, 2)
            player.unlock()
        }
    }

    private fun processIfAction(pkt: Packet.IfAction) {
        val player = pkt.player
        player.debug("[IF ACTION]--------------------------")
        player.debug("Iface: ${pkt.iface}, Button: ${pkt.child}")
        player.debug("Slot: ${pkt.slot}, ItemID: ${pkt.itemId}")
        player.debug("RCM Index: ${pkt.optIndex}, Op: ${pkt.opcode}")
        player.debug("-------------------------------------")
        if (player.dialogueInterpreter.dialogue != null && pkt.opcode != 132 && pkt.iface != 64)
            player.dialogueInterpreter.close()
        if (player.locks.isComponentLocked)
            return
        if (player.zoneMonitor.clickButton(pkt.iface, pkt.child, pkt.slot, pkt.itemId, pkt.opcode))
            return
        val c = player.interfaceManager.getComponent(pkt.iface) ?: return
        if (c.isHidden)
            return
        val plugin = c.plugin
        player.dispatch(ButtonClickEvent(c.id, pkt.child))
        if (!InterfaceListeners.run(player, c, pkt.opcode, pkt.child, pkt.slot, pkt.itemId))
            plugin?.handle(player, c, pkt.opcode, pkt.child, pkt.slot, pkt.itemId)
    }

    private fun processUseWith(pkt: Packet) {
        val node: Node
        var childNode: Node? = null
        val item: Item
        val itemId: Int
        val nodeId: Int
        val type: IntType
        val player: Player
        if (pkt is Packet.UseWithNpc) {
            val container = getLikelyContainerForIface(pkt.player, pkt.iface) ?: return sendClearMinimap(pkt.player)
            itemId = pkt.itemId
            val itemSlot = pkt.slot
            item = container[itemSlot] ?: return sendClearMinimap(pkt.player)
            node = Repository.npcs[pkt.npcIndex] ?: return sendClearMinimap(pkt.player)
            childNode = node.getShownNPC(pkt.player)
            nodeId = node.id
            type = IntType.NPC
            player = pkt.player
        }
        else if (pkt is Packet.UseWithScenery) {
            item = pkt.player.inventory[pkt.slot] ?: return sendClearMinimap(pkt.player)
            node = RegionManager.getObject(pkt.player.location.z, pkt.x, pkt.y) ?: return sendClearMinimap(pkt.player)
            childNode = node.asScenery().getChild(pkt.player)
            itemId = pkt.itemId
            nodeId = node.id
            type = IntType.SCENERY
            player = pkt.player
        }
        else if (pkt is Packet.UseWithItem) {
            val containerUsed = getLikelyContainerForIface(pkt.player, pkt.usedIface) ?: return
            val containerWith = getLikelyContainerForIface(pkt.player, pkt.usedWithIface) ?: return
            item = containerUsed[pkt.usedSlot] ?: return
            node = containerWith[pkt.usedWithSlot] ?: return
            itemId = pkt.usedId
            nodeId = pkt.usedWithId
            type = IntType.ITEM
            player = pkt.player
        } else if (pkt is Packet.UseWithGroundItem) {
            val container = getLikelyContainerForIface(pkt.player, pkt.iface) ?: return
            item = container[pkt.slot]
            itemId = pkt.usedId
            node = GroundItemManager.get(pkt.withId, Location.create(pkt.x, pkt.y, pkt.player.location.z), pkt.player)
            nodeId = pkt.withId
            type = IntType.GROUNDITEM
            player = pkt.player
        } else {
            if (pkt !is Packet.UseWithPlayer) return
            val container = getLikelyContainerForIface(pkt.player, pkt.iface) ?: return sendClearMinimap(pkt.player)
            item = container[pkt.slot] ?: return sendClearMinimap(pkt.player)
            itemId = pkt.itemId
            node = Repository.players[pkt.otherIndex] ?: return sendClearMinimap(pkt.player)
            type = IntType.PLAYER
            nodeId = node.id
            player = pkt.player
        }

        if (item.id != itemId)
            return sendClearMinimap(player)
        if (node.id != nodeId)
            return sendClearMinimap(player)

        if (player.zoneMonitor.useWith(item, node))
            return
        if (InteractionListeners.run(item, node, type, player))
            return
        if (childNode != null && childNode.id != node.id) {
            if (InteractionListeners.run(item, childNode, type, player))
                return
        }
        val flipped = type == IntType.ITEM && item.id < node.id
        val event = if (flipped)
            NodeUsageEvent(player, 0, node, item)
        else
            NodeUsageEvent(player, 0, item, childNode ?: node)
        if (PluginInteractionManager.handle(player, event))
            return
        UseWithHandler.run(event)
    }

    private fun processGroundItemAction(pkt: Packet.GroundItemAction) {
        val item = GroundItemManager.get(pkt.id, Location.create(pkt.x, pkt.y, pkt.player.location.z), pkt.player)
        val player = pkt.player

        if (item == null) {
            return sendClearMinimap(player)
        }
        val option = item.interaction[pkt.optIndex]
        if (option == null) {
            Interaction.handleInvalidInteraction(player, item, Option.NULL)
            return sendClearMinimap(player)
        }
        if (PluginInteractionManager.handle(player, item, option))
            return
        if (InteractionListeners.run(item.id, IntType.GROUNDITEM, option.name, player, item))
            return
        if (InteractionListeners.run(item.id, IntType.ITEM, option.name, player, item))
            return
        item.interaction.handle(player, option)
    }

    private fun processPlayerAction(pkt: Packet.PlayerAction) {
        val player = pkt.player
        if (pkt.otherIndex !in 1 until ServerConstants.MAX_PLAYERS) {
            return sendClearMinimap(player)
        }
        val other = Repository.players[pkt.otherIndex]
        if (other == null || !other.isActive)
            return sendClearMinimap(player)
        val option = other.interaction[pkt.optIndex] ?: return sendClearMinimap(player)
        if (!InteractionListeners.run(-1, IntType.PLAYER, option.name.lowercase(), player, other)) {
            other.interaction.handle(player, option)
        }
    }

    private fun processSceneryAction(pkt: Packet.SceneryAction) {
        val player = pkt.player
        var scenery = RegionManager.getObject(player.location.z, pkt.x, pkt.y, pkt.id)
        var objId = pkt.id

        //what follows is a series of hardcoded crimes against humanity
        if (pkt.id == 6898)
            scenery = Scenery(6898, Location(3219, 9618))
        if (pkt.id == 6899)
            scenery = Scenery(6899, Location(3221, 9618))

        // Family crest levers don't have varps associated with them, so their state is validated with attributes
        // instead, and they always appear as their down/odd variant in the server's map
        if (objId in 2421..2426 && objId % 2 == 0) {
            scenery = Scenery(objId - 1, Location(pkt.x, pkt.y, player.location.z))
            objId -= 1
        }

        if (scenery == null || scenery.id != objId || !scenery.isActive) {
            player.debug("[SCENERY INTERACT] NULL OR MISMATCH OR INACTIVE")
            Interaction.handleInvalidInteraction(player, scenery, Option.NULL)
            return sendClearMinimap(player)
        }

        val wrapperChild = scenery.getChild(player)
        val option = wrapperChild.interaction[pkt.optIndex]

        if (option == null) {
            player.debug("[SCENERY INTERACT] NULL OPTION")
            Interaction.handleInvalidInteraction(player, scenery, Option.NULL)
            return sendClearMinimap(player)
        }

        val hasWrapper = wrapperChild.id != scenery.id

        player.debug("[SCENERY INTERACT]------------------------------")
        player.debug("ID: ${wrapperChild.id}, Option: ${option.name}[${option.index}]")
        player.debug("Loc: ${scenery.location}, Dir: ${scenery.direction}")
        if (hasWrapper) {
            player.debug("WrapperID: ${scenery.id}, Varbit: ${scenery.definition.configFile.id}")
        }
        player.debug("------------------------------------------------")

        if (InteractionListeners.run(wrapperChild.id, IntType.SCENERY, option.name, player, wrapperChild))
            return
        if (PluginInteractionManager.handle(player, wrapperChild))
            return
        wrapperChild.interaction.handle(player, option)
    }

    private fun processNpcAction(pkt: Packet.NpcAction) {
        if (pkt.npcIndex !in 1 until ServerConstants.MAX_NPCS)
            return sendClearMinimap(pkt.player)
        val npc = Repository.npcs[pkt.npcIndex] ?: return sendClearMinimap(pkt.player)

        val wrapperChild = npc.getShownNPC(pkt.player)
        val option = wrapperChild.interaction[pkt.optIndex]

        if (option == null) {
            Interaction.handleInvalidInteraction(pkt.player, npc, Option.NULL)
            return sendClearMinimap(pkt.player)
        }

        val hasWrapper = wrapperChild.id != npc.id

        pkt.player.debug("[NPC INTERACT]-------------------")
        pkt.player.debug("ID: ${wrapperChild.id}, Index: ${pkt.npcIndex}")
        pkt.player.debug("Option: ${option.name}[${option.index}]")
        pkt.player.debug("SpawnLoc: ${npc.properties.spawnLocation}")
        if (hasWrapper) {
            pkt.player.debug("WrapperID: ${npc.id}, Varbit: ${npc.definition.configFileId}")
        }
        pkt.player.debug("---------------------------------")

        if (InteractionListeners.run(wrapperChild.id, IntType.NPC,option.name,pkt.player,npc))
            return
        if (PluginInteractionManager.handle(pkt.player, wrapperChild, option))
            return
        npc.interaction.handle(pkt.player, option)
    }

    private fun processItemAction(pkt: Packet.ItemAction) {
        val container = getLikelyContainerForIface(pkt.player, pkt.iface) ?: return
        if (pkt.slot !in 0 until container.capacity())
            return
        val item = container.get(pkt.slot) ?: return
        if (item.id != pkt.itemId)
            return
        val option = item.interaction[pkt.optIndex] ?: return
        if (pkt.player.locks.isInteractionLocked)
            return
        item.interaction.handleItemOption(pkt.player, option, container)
        pkt.player.debug("[ITEM INTERACT] ID: ${item.id}, Slot: ${pkt.slot}, Opt: ${option.name}")
    }

    private fun getLikelyContainerForIface(player: Player, iface: Int) : Container? {
        return when (iface) {
            Components.INVENTORY_149 -> player.inventory
            Components.BANK_V2_MAIN_762 -> player.bank
            Components.EQUIP_SCREEN2_667 -> player.equipment
            else -> null
        }
    }

    private fun sendClearMinimap(player: Player) {
        PacketRepository.send(ClearMinimapFlag::class.java, PlayerContext(player))
    }

    fun switchItem(slot: Int, secondSlot: Int, container: Container?, insert: Boolean, player: Player) {
        if (container == null || slot < 0 || slot >= container.toArray().size || secondSlot < 0 || secondSlot >= container.toArray().size) {
            return
        }
        val item = container[slot]
        val second = container[secondSlot]
        if (player.interfaceManager.hasChatbox() && !getAttribute(player, "close_c_", false)) {
            player.interfaceManager.closeChatbox()
            switchItem(secondSlot, slot, container, insert, player)
            container.refresh()
            return
        }
        if (item == null) {
            return
        }
        if (!insert) {
            container.replace(second, slot, false)
            container.replace(item, secondSlot, false)
            item.index = secondSlot
            if (second != null) {
                second.index = slot
            }
        } else {
            container.insert(slot, secondSlot, false)
        }
        container.refresh()
    } 
}