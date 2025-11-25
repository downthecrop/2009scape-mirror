package content.region.misc.tutisland.dialogue

import content.global.handlers.iface.RulesAndInfo
import content.region.misc.tutisland.handlers.*
import core.ServerConstants
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import core.worker.ManagementEvents
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import proto.management.JoinClanRequest

@Initializable
class TutorialMagicTutorDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.MAGIC_INSTRUCTOR_946, NPC, "talk-to") { player, _ ->
            val stage = getAttribute(player, "tutorial:stage", 0)
            if (stage == 70 && inInventory(player, Items.AIR_RUNE_556) && inInventory(player, Items.MIND_RUNE_558)) {
                // Player should be killing chickens instead, and could be. Instead of opening the dialogue and doing nothing (which will make you lose the tutorial island dialog), do nothing at all
                return@on true
            }
            openDialogue(player, TutorialMagicTutorDialogueFile(), NPC(NPCs.MAGIC_INSTRUCTOR_946))
            return@on true
        }
    }
}

class TutorialMagicTutorDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        val STARTER_PACK = arrayOf(
            Item(Items.BRONZE_AXE_1351),
            Item(Items.TINDERBOX_590),
            Item(Items.SMALL_FISHING_NET_303),
            Item(Items.SHRIMPS_315),
            Item(Items.BUCKET_1925),
            Item(Items.EMPTY_POT_1931),
            Item(Items.BREAD_2309),
            Item(Items.BRONZE_PICKAXE_1265),
            Item(Items.BRONZE_DAGGER_1205),
            Item(Items.BRONZE_SWORD_1277),
            Item(Items.WOODEN_SHIELD_1171),
            Item(Items.SHORTBOW_841),
            Item(Items.BRONZE_ARROW_882, 25),
            Item(Items.AIR_RUNE_556, 25),
            Item(Items.MIND_RUNE_558, 15),
            Item(Items.WATER_RUNE_555, 6),
            Item(Items.EARTH_RUNE_557, 4),
            Item(Items.BODY_RUNE_559, 2)
        )
        val STARTER_BANK = arrayOf(Item(Items.COINS_995, 25))

        exec { player, _ ->
            when (getAttribute(player, "tutorial:stage", 0)) {
                67 -> loadLabel(player, "hello")
                69 -> loadLabel(player, "spelllist")
                70 -> {
                    if (!inInventory(player, Items.AIR_RUNE_556, 1) || !inInventory(player, Items.MIND_RUNE_558, 1)) {
                        loadLabel(player, "givemorerunes")
                    } else {
                        goto("nowhere")
                    }
                }
                71 -> loadLabel(player, "finishedtutorial")
                else -> goto("nowhere")
            }
        }

        label("hello")
            player(ChatAnim.FRIENDLY, "Hello.", unclosable = true)
            npc(ChatAnim.FRIENDLY, "Good day, newcomer. My name is Terrova. I'm here to tell you about Magic. Let's start by opening your spell list.", unclosable = true)
            exec { player, _ ->
                setAttribute(player, "tutorial:stage", 68)
                TutorialStage.load(player, 68)
            }

        label("spelllist")
            npc(ChatAnim.FRIENDLY, "Good. This is a list of your spells. Currently you can only cast one offensive spell called Wind Strike. Let's try it out on one of those chickens.", unclosable = true)
            exec { player, _ ->
                setAttribute(player, "tutorial:stage", 70)
                addItemOrDrop(player, Items.AIR_RUNE_556, 15)
                addItemOrDrop(player, Items.MIND_RUNE_558, 15)
            }
            item(Item(Items.AIR_RUNE_556), Item(Items.MIND_RUNE_558), "Terrova gives you 15 air runes and 15 mind runes!", unclosable = true)
            exec { player, _ -> TutorialStage.load(player, 70) }

        label("givemorerunes")
            exec { player, _ ->
                addItemOrDrop(player, Items.AIR_RUNE_556, 5)
                addItemOrDrop(player, Items.MIND_RUNE_558, 5)
            }
            item(Item(Items.AIR_RUNE_556), Item(Items.MIND_RUNE_558), "You receive some spare runes.", unclosable = true)
            exec { player, _ -> TutorialStage.load(player, 70) }

        label("finishedtutorial")
            exec { player, _ ->
                if (ServerConstants.XP_RATES || ServerConstants.IRONMAN) {
                    loadLabel(player, "talk about inauthentic")
                } else {
                    loadLabel(player, "leave")
                }
            }

        label("talk about inauthentic")
            npc(ChatAnim.FRIENDLY, "Alright, last thing. Are you interested in our inauthentic ${ServerConstants.SERVER_NAME} features?", unclosable = true)
            goto("inauthentic")

        label("inauthentic")
            options(
                DialogueOption("xprate","Change XP rate (current: ${player?.skills?.experienceMultiplier}x)", skipPlayer = true) { _, _ ->
                    return@DialogueOption ServerConstants.XP_RATES
                },
                DialogueOption("ironman","Set ironman mode (current: ${player?.ironmanManager?.mode?.name?.toLowerCase()})", skipPlayer = true) { _, _ ->
                    return@DialogueOption ServerConstants.IRONMAN
                },
                DialogueOption("leave","I'm ready now."),
                unclosable = true)

        label("xprate")
            options(
                DialogueOption("1.0x","1.0x (default)", skipPlayer = true),
                DialogueOption("2.5x","2.5x", skipPlayer = true),
                DialogueOption("5.0x","5.0x", skipPlayer = true),
                title = "Change XP rate (current: ${player?.skills?.experienceMultiplier}x)",
                unclosable = true
            )
            for (rate in doubleArrayOf(1.0, 2.5, 5.0)) {
                label("${rate}x")
                exec { player, _ -> player.skills.experienceMultiplier = rate }
                manual(unclosable = true) { player, _ -> player.dialogueInterpreter.sendDialogue("You set your XP rate to: ${rate}x.") }
                goto("inauthentic")
            }

        label("ironman")
            options(
                DialogueOption("NONE","None (default)", skipPlayer = true),
                DialogueOption("STANDARD","Standard", skipPlayer = true),
                DialogueOption("ULTIMATE","Ultimate (no bank)", skipPlayer = true),
                title = "Change ironman mode (current: ${player?.ironmanManager?.mode?.name?.toLowerCase()}x)"
            )
            for (mode in arrayOf(IronmanMode.NONE, IronmanMode.STANDARD, IronmanMode.ULTIMATE)) {
                label(mode.name)
                exec { player, _ -> player.ironmanManager.mode = mode }
                manual(unclosable = true){ player, _ -> return@manual player.dialogueInterpreter.sendDialogue("You set your ironman mode to: ${mode.name.toLowerCase()}.") }
                exec { player, _ -> loadLabel(player, if (player.ironmanManager.mode == IronmanMode.NONE) "inauthentic" else "ironwarning") }
            }

        label("ironwarning")
            manual(unclosable = true) { player, _ -> player.dialogueInterpreter.sendDialogue(*splitLines("WARNING: You have selected an ironman mode. This is an uncompromising mode that WILL completely restrict your ability to trade. This MAY leave you unable to complete certain content, including quests.")) }
            goto("inauthentic")

        label("leave")
            npc(ChatAnim.FRIENDLY, "Well, you're all finished here now. I'll give you a reasonable number of starting items when you leave.", unclosable = true)
            options(
                DialogueOption("leave:yes","Yes, I'm ready.","I'm ready to go now, thank you.", ChatAnim.FRIENDLY),
                DialogueOption("nowhere","No, not yet.","I'm not quite ready to go yet, thank you.", ChatAnim.FRIENDLY),
                title = "Leave Tutorial Island?",
                unclosable = true
            )

        label("leave:yes")
            manual { player, _ ->
                setAttribute(player, "/save:tutorial:complete", true)
                setVarbit(player, 3756, 0)
                setVarp(player, 281, 1000, true)
                teleport(player, Location.create(3233, 3230), TeleportManager.TeleportType.NORMAL)
                closeOverlay(player)

                player.inventory.clear()
                player.bank.clear()
                player.equipment.clear()
                player.interfaceManager.restoreTabs()
                player.interfaceManager.setViewedTab(3)
                player.inventory.add(*STARTER_PACK)
                player.bank.add(*STARTER_BANK)

                TutorialStage.removeHintIcon(player)
                player.unhook(TutorialKillReceiver)
                player.unhook(TutorialFireReceiver)
                player.unhook(TutorialResourceReceiver)
                player.unhook(TutorialUseWithReceiver)
                player.unhook(TutorialInteractionReceiver)
                player.unhook(TutorialButtonReceiver)
                player.unhook(TutorialDialogPreserver)

                if (GameWorld.settings!!.enable_default_clan) {
                    player.communication.currentClan = ServerConstants.SERVER_NAME.toLowerCase()

                    val clanJoin = JoinClanRequest.newBuilder()
                    clanJoin.clanName = ServerConstants.SERVER_NAME.toLowerCase()
                    clanJoin.username = player.name

                    ManagementEvents.publish(clanJoin.build())
                }

                // This shows the actual dialog, which is what this manual stage is for.
                // Dialog is from 2007 or thereabouts.
                // Original is five lines, but if the same is done here it will break. Need to find another way of showing all this information.
                player.dialogueInterpreter.sendDialogue(
                    "Welcome to Lumbridge! To get more help, simply click on the",
                    "Lumbridge Guide or one of the Tutors - these can be found by looking",
                    "for the question mark icon on your mini-map. If you find you are lost",
                    "at any time, look for a signpost or use the Lumbridge Home Port Spell."
                )
                if (ServerConstants.RULES_AND_INFO_ENABLED) {
                    RulesAndInfo.openFor(player)
                    // The teleport finishing will release the player, so we need to relock them here
                    queueScript(player, 4, QueueStrength.SOFT) { _ ->
                        player.lock()
                        return@queueScript stopExecuting(player)
                    }
                }
                return@manual null
            }

        label("nowhere")
            exec { player, _ -> sendStageDialog(player) }
    }
}
