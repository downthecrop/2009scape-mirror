package content.region.misc.tutisland.dialogue

import content.global.handlers.iface.RulesAndInfo
import content.region.misc.tutisland.handlers.*
import core.ServerConstants
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.worker.ManagementEvents
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import proto.management.JoinClanRequest

/**
 * Handles the magic tutor's dialogue
 * @author Ceikry
 */
@Initializable
class TutorialMagicTutorDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    private val STARTER_PACK = arrayOf(
        Item(1351, 1),
        Item(590, 1),
        Item(303, 1),
        Item(315, 1),
        Item(1925, 1),
        Item(1931, 1),
        Item(2309, 1),
        Item(1265, 1),
        Item(1205, 1),
        Item(1277, 1),
        Item(1171, 1),
        Item(841, 1),
        Item(882, 25),
        Item(556, 25),
        Item(558, 15),
        Item(555, 6),
        Item(557, 4),
        Item(559, 2)
    )
    private val STARTER_BANK = arrayOf(Item(995, 25))

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return TutorialMagicTutorDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when(getAttribute(player, "tutorial:stage", 0))
        {
            67 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Hello.")
            69 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Good. This is a list of your spells. Currently you can only cast one offensive spell called Wind Strike. Let's try it out on one of those chickens.")
            70 -> if(!inInventory(player, Items.AIR_RUNE_556) && !inInventory(player, Items.MIND_RUNE_558))
            {
                player.dialogueInterpreter.sendDoubleItemMessage(Items.AIR_RUNE_556, Items.MIND_RUNE_558, "You receive some spare runes.")
                addItemOrDrop(player, Items.AIR_RUNE_556, 15)
                addItemOrDrop(player, Items.MIND_RUNE_558, 15)
                return false
            }
            71 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Alright, last thing. Are you interested in being an ironman or changing your experience rate?")
            else -> return false
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(getAttribute(player, "tutorial:stage", 0))
        {
            67 -> when(stage++){
                0 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Good day, newcomer. My name is Terrova. I'm here to tell you about Magic. Let's start by opening your spell list.")
                1 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 68)
                    TutorialStage.load(player, 68)
                }
            }
            69 -> when(stage++){
                0 -> {
                    sendDoubleItemDialogue(player, Items.AIR_RUNE_556, Items.MIND_RUNE_558, "Terrova gives you 15 air runes and 15 mind runes!")
                    addItemOrDrop(player, Items.AIR_RUNE_556, 5)
                    addItemOrDrop(player, Items.MIND_RUNE_558, 5)
                }
                1 -> {
                    end()
                    setAttribute(player, "tutorial:stage", 70)
                    TutorialStage.load(player, 70)
                }
            }
            71 -> when(stage){
                0 -> options("Set Ironman Mode (current: ${player.ironmanManager.mode.name})", "Change XP Rate (current: ${player.skills.experienceMultiplier}x)", "I'm ready now.").also { stage++ }
                1 -> when(buttonId){
                    1 -> options("None","Standard","Ultimate","Nevermind.").also { stage = 10 }
                    2 -> options("1.0x","2.5x","5.0x").also { stage = 20 }
                    3 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Well, you're all finished here now. I'll give you a reasonable number of starting items when you leave.").also { stage = 30 }
                }

                10 -> {
                    stage = 0
                    if(buttonId < 5)
                    {
                        val mode = when (buttonId - 1)
                        {
                            0 -> IronmanMode.NONE
                            1 -> IronmanMode.STANDARD
                            2 -> IronmanMode.ULTIMATE
                            else -> IronmanMode.NONE
                        }
                        if (mode != IronmanMode.NONE) stage = 11
                        player.dialogueInterpreter.sendDialogue("You set your ironman mode to: ${mode.name}.")
                        player.ironmanManager.mode = mode
                        if (player.skills.experienceMultiplier == 10.0) player.skills.experienceMultiplier = 5.0
                    }
                    else
                    {
                        handle(interfaceId, 0)
                    }
                }
                11 -> player.dialogueInterpreter.sendPlainMessage(false, *splitLines("WARNING: You have selected an ironman mode. This is an uncompromising mode that WILL completely restrict your ability to trade. This MAY leave you unable to complete certain content, including quests.")).also { stage = 0 }

                20 -> {
                    val rates = arrayOf(1.0,2.5,5.0)
                    val rate = rates[buttonId - 1]
                    if(rate == 10.0) {
                        player.dialogueInterpreter.sendDialogue("10.0x is no longer available!")
                        player.skills.experienceMultiplier = 5.0
                        stage = 0
                        return true
                    }
                    player.dialogueInterpreter.sendDialogue("You set your XP rate to: ${rate}x.")
                    player.skills.experienceMultiplier = rate
                    stage = 0
                }

                30 -> player.dialogueInterpreter.sendOptions("Leave Tutorial Island?", "Yes, I'm ready.", "No, not yet.").also { stage++ }
                31 -> when(buttonId)
                {
                    1 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "I'm ready to go now, thank you.").also { stage = 40 }
                    2 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "I'm not quite ready to go yet, thank you.").also { stage = END_DIALOGUE }
                }

                40 -> {
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

                    if(player.skills.experienceMultiplier == 10.0)
                    {
                        player.skills.experienceMultiplier = 5.0
                    }

                    //This overwrites the stuck dialogue after teleporting to Lumbridge for some reason
                    //Dialogue from 2007 or thereabouts
                    //Original is five lines, but if the same is done here it will break. Need to find another way of showing all this information.
                    interpreter.sendDialogue(
                        "Welcome to Lumbridge! To get more help, simply click on the",
                        "Lumbridge Guide or one of the Tutors - these can be found by looking",
                        "for the question mark icon on your mini-map. If you find you are lost",
                        "at any time, look for a signpost or use the Lumbridge Home Port Spell."
                    )
                    stage = 12
                    TutorialStage.removeHintIcon(player)

                    player.unhook(TutorialKillReceiver)
                    player.unhook(TutorialFireReceiver)
                    player.unhook(TutorialResourceReceiver)
                    player.unhook(TutorialUseWithReceiver)
                    player.unhook(TutorialInteractionReceiver)
                    player.unhook(TutorialButtonReceiver)
                    RulesAndInfo.openFor(player)

                    if (GameWorld.settings!!.enable_default_clan) {
                        player.communication.currentClan = ServerConstants.SERVER_NAME.toLowerCase()

                        val clanJoin = JoinClanRequest.newBuilder()
                        clanJoin.clanName = ServerConstants.SERVER_NAME.toLowerCase()
                        clanJoin.username = player.name

                        ManagementEvents.publish(clanJoin.build())
                    }
                }

                12 -> {
                    player.setAttribute("close_c_", true)
                    end()
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAGIC_INSTRUCTOR_946)
    }

}
