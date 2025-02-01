package content.region.asgarnia.burthorpe.quest.heroesquest

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.item.GroundItem
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class HeroesQuestListener: InteractionListener {

    override fun defineListeners() {
        // Black arm gang office door.
        on(Scenery.DOOR_2626, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, HeroesQuest.questName) >= 2 &&
                    getAttribute(player, HeroesQuest.attributeGruborLetsYouIn, false) &&
                    HeroesQuest.isBlackArm(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, GruborDialogueFile(), NPC(NPCs.GRUBOR_789))
            }
            return@on true
        }

        // Kitchen entrance
        on(Scenery.DOOR_2628, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, HeroesQuest.questName) >= 3 && HeroesQuest.isPhoenix(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendDialogue(player, "This door is locked.")
            }
            return@on true
        }

        // Kitchen wall
        on(Scenery.WALL_2629, IntType.SCENERY, "push") { player, node ->
            if (getQuestStage(player, HeroesQuest.questName) >= 4 && HeroesQuest.isPhoenix(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, CharlieTheCookDialogueFile(), NPC(NPCs.CHARLIE_THE_COOK_794))
            }
            return@on true
        }

        // Mansion frontdoor
        on(Scenery.DOOR_2627, IntType.SCENERY, "open") { player, node ->
            if (getQuestStage(player, HeroesQuest.questName) >= 4 && HeroesQuest.isBlackArm(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                openDialogue(player, GarvDialogueFile(), NPC(NPCs.GARV_788))
            }
            return@on true
        }

        // Cupboard

        on(Scenery.CUPBOARD_2636, IntType.SCENERY, "search") { player, node ->
            openDialogue(player, object : DialogueFile() {
                override fun handle(componentID: Int, buttonID: Int) {
                    when (stage) {
                        START_DIALOGUE -> sendNPCDialogue(player, NPCs.PIRATE_GUARD_799, "I don't think Mr Grip will like you opening that. That's his private drinks cabinet.") .also { stage++ }
                        1 -> showTopics(
                                Topic(FacialExpression.NEUTRAL, "He won't notice me having a quick look.", 2),
                                Topic(FacialExpression.NEUTRAL, "Ok, I'll leave it.", END_DIALOGUE)
                        )
                        2 -> end().also {
                            val gripNpc = findNPC(NPCs.GRIP_792)
                            sendChat(gripNpc!!, "Stay out of my drinks cabinet!")
                            forceWalk(gripNpc, Location(2777, 3198, 0), "smart")
                        }
                    }
                }
            })
            return@on true
        }

        // Mansion backdoor
        on(Scenery.DOOR_2622, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, HeroesQuest.attributeHasOpenedBackdoor, false)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendDialogue(player, "This door is locked.")
            }
            return@on true
        }
        onUseWith(IntType.SCENERY, Items.MISCELLANEOUS_KEY_1586, Scenery.DOOR_2622) { player, used, with ->
            setAttribute(player, HeroesQuest.attributeHasOpenedBackdoor, true)
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            return@onUseWith true
        }

        // Chest door
        on(Scenery.DOOR_2621, IntType.SCENERY, "open") { player, node ->
            if (getAttribute(player, HeroesQuest.attributeHasOpenedChestDoor, false)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendDialogue(player, "This door is locked.")
            }
            return@on true
        }
        onUseWith(IntType.SCENERY, Items.GRIPS_KEY_RING_1588, Scenery.DOOR_2621) { player, used, with ->
            setAttribute(player, HeroesQuest.attributeHasOpenedChestDoor, true)
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            return@onUseWith true
        }

        // Chest
        on(Scenery.CHEST_2632, IntType.SCENERY, "open"){ player, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CHEST_2633, -1)
            return@on true
        }
        on(Scenery.CHEST_2633, IntType.SCENERY, "close"){ player, node ->
            replaceScenery(node as core.game.node.scenery.Scenery, Scenery.CHEST_2632, -1)
            return@on true
        }
        on(Scenery.CHEST_2633, IntType.SCENERY, "search"){ player, node ->
            if (inInventory(player, Items.PETES_CANDLESTICK_1577)) {
                sendMessage(player, "You search the chest but find nothing.")
            } else {
                if (getQuestStage(player, HeroesQuest.questName) == 4) {
                    setQuestStage(player, HeroesQuest.questName, 5)
                }
                sendDialogue(player, "You find two candlesticks in the chest. So that will be one for you, and one for the person who killed Grip for you.")
                addItemOrDrop(player, Items.PETES_CANDLESTICK_1577, 2)
            }
            return@on true
        }

        //
        on(Items.FIRE_FEATHER_1583, IntType.GROUNDITEM, "take") { player, groundItem ->
            if (inEquipment(player, Items.ICE_GLOVES_1580)) {
                addItem(player, Items.FIRE_FEATHER_1583)
                removeGroundItem(groundItem as GroundItem)
            } else {
                sendChat(player, "Ouch!")
                player.impactHandler.manualHit(player, 9, ImpactHandler.HitsplatType.NORMAL)
                sendMessage(player, "It is too hot to take. You need something cold to pick it up with.")
            }
            return@on true
        }

        // OilFishingRodListener.kt + DrinkBlamishOilListener.kt + FinishedPotion.java
    }
}