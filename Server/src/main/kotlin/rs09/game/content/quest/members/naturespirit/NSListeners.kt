package rs09.game.content.quest.members.naturespirit

import api.Container
import api.*
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.global.action.PickupHandler
import rs09.game.interaction.InteractionListener
import rs09.game.node.entity.npc.other.MortMyreGhastNPC
import rs09.game.system.SystemLogger
import rs09.game.system.config.ShopParser
import rs09.tools.END_DIALOGUE

class NSListeners : InteractionListener() {

    val GROTTO_TREE = 3517
    val GROTTO_ENTRANCE = 3516
    val GROTTO_ALTAR = 3520
    val NATURE_ALTAR = 3521
    val JOURNAL = Items.JOURNAL_2967
    val NATURE_STONE = 3527
    val FAITH_STONE = 3528
    val FREELY_GIVEN_STONE = 3529
    val WASHING_BOWL = Items.WASHING_BOWL_2964
    val MIRROR = Items.MIRROR_2966
    val SPELLCARD = Items.DRUIDIC_SPELL_2968
    val USED_SPELLCARD = Items.A_USED_SPELL_2969
    val FUNGUS = Items.MORT_MYRE_FUNGUS_2970
    val STEM = Items.MORT_MYRE_STEM_2972
    val PEAR = Items.MORT_MYRE_PEAR_2974
    val MIRROR_TAKEN = "/save:ns:mirror_taken"
    val GROTTO_SEARCHED = "/save:ns:grotto_searched"
    val WISHING_WELL = 28715
    val DRUID_POUCH = Items.DRUID_POUCH_2958
    val DRUID_POUCH_EMPTY = Items.DRUID_POUCH_2957
    val stones = intArrayOf(NATURE_STONE, FAITH_STONE, FREELY_GIVEN_STONE)
    val items = intArrayOf(USED_SPELLCARD, FUNGUS)

    override fun defineListeners() {
        on(GROTTO_TREE, SCENERY, "look-at"){player, _ ->
            sendMessage(player, "It looks like a tree on a large rock with roots trailing down to the ground.")
            return@on true
        }

        on(GROTTO_TREE, SCENERY, "search"){player, _ ->
            if(!getAttribute(player, GROTTO_SEARCHED, false) || !(inInventory(player, JOURNAL) || inBank(player, JOURNAL))){
                sendItemDialogue(player, JOURNAL, "You search the strange rock. You find a knot and inside of it you discover a small tome. The words on the front are a bit vague, but you can make out the words 'Tarlock' and 'journal'.")
                addItemOrDrop(player, JOURNAL, 1)
                setAttribute(player, GROTTO_SEARCHED, true)
                return@on true
            }
            return@on false
        }

        on(GROTTO_ENTRANCE, SCENERY, "enter"){player, node ->
            val questStage = player.questRepository.getQuest("Nature Spirit").getStage(player)
            if(questStage < 55) {
                val npc = core.game.node.entity.npc.NPC.create(NPCs.FILLIMAN_TARLOCK_1050, Location.create(3440, 3336, 0))
                npc.init()
            } else if(questStage < 60) {
                player.teleport(Location.create(3442, 9734, 0))
            } else if (questStage >= 60){
                player.teleport(Location.create(3442, 9734, 1))
            }
            return@on true
        }

        on(GROTTO_ALTAR, SCENERY,"search"){player, node ->
            val stage = player.questRepository.getStage("Nature Spirit")
            if(stage == 55){
                openDialogue(player, FillimanCompletionDialogue(), NPC(NPCs.FILLIMAN_TARLOCK_1050))
                return@on true
            }

            return@on false
        }

        on(NATURE_STONE, SCENERY, "search"){player, _ ->
            sendDialogue(player, "You search the stone and find that it has some sort of nature symbol scratched into it.")
            return@on true
        }

        on(FAITH_STONE, SCENERY, "search"){player, _ ->
            sendDialogue(player, "You search the stone and find that it has some sort of faith symbol scratched into it.")
            return@on true
        }

        on(FREELY_GIVEN_STONE, SCENERY, "search"){player, _ ->
            sendDialogue(player, "You search the stone and find it has some sort of spirit symbol scratched into it.")
            return@on true
        }

        on(WISHING_WELL, SCENERY, "make-wish"){player, node ->
            if(player.questRepository.isComplete("Nature Spirit") && player.questRepository.isComplete("Wolf Whistle"))
                ShopParser.openUid(player, 241)
            else
                sendDialogue(player, "You can't do that yet.")

            return@on true
        }

        on(JOURNAL, ITEM, "read"){player, _ ->
            player.dialogueInterpreter.open(NSJournalDialogue())
            return@on true
        }

        on(WASHING_BOWL, GROUNDITEM, "take"){player, node ->
            SystemLogger.logInfo("Running listener")
            GroundItemManager.create(Item(MIRROR), node.location, player)
            PickupHandler.take(player, node as GroundItem)
            return@on true
        }

        on(MIRROR, GROUNDITEM, "take"){player, node ->
            if(getAttribute(player, MIRROR_TAKEN, false) && (inInventory(player, MIRROR) || inBank(player, MIRROR))){
                sendDialogue(player, "I don't need another one of these.")
                return@on true
            }
            setAttribute(player, MIRROR_TAKEN, true)
            PickupHandler.take(player, node as GroundItem)
            return@on true
        }

        on(SPELLCARD, ITEM, "cast"){player, node ->
            if(NSUtils.castBloom(player)){
                removeItem(player, node.asItem(), Container.INVENTORY)
                addItem(player, Items.A_USED_SPELL_2969)
            }
            return@on true
        }

        on(intArrayOf(DRUID_POUCH, DRUID_POUCH_EMPTY), ITEM, "fill"){player, node ->

            if(player.questRepository.getStage("Nature Spirit") >= 75) {
                if (amountInInventory(player, PEAR) >= 3) {
                    if (node.id != Items.DRUID_POUCH_2958) {
                        removeItem(player, node, Container.INVENTORY)
                    }
                    removeItem(player, Item(PEAR, 3), Container.INVENTORY)
                    addItem(player, Items.DRUID_POUCH_2958, 9 )
                } else if (amountInInventory(player, STEM) >= 3) {
                    if (node.id != Items.DRUID_POUCH_2958) {
                        removeItem(player, node, Container.INVENTORY)
                    }
                    removeItem(player, Item(STEM, 3), Container.INVENTORY)
                    addItem(player, Items.DRUID_POUCH_2958, 6)
                } else if (amountInInventory(player, FUNGUS) >= 3) {
                    if (node.id != Items.DRUID_POUCH_2958) {
                        removeItem(player, node, Container.INVENTORY)
                    }
                    removeItem(player, Item(FUNGUS, 3), Container.INVENTORY)
                    addItem(player, Items.DRUID_POUCH_2958, 3)
                } else {
                    sendDialogue(player, "You need 3 fungus before you can do that.")
                }
            } else {
                sendDialogue(player, "I don't know how to use that yet.")
            }

            return@on true
        }

        onUseWith(SCENERY, Items.SILVER_SICKLE_2961, NATURE_ALTAR){player, used, with ->
            sendItemDialogue(player, Items.SILVER_SICKLEB_2963, "You dump the sickle into the waters.")
            if(removeItem(player, Items.SILVER_SICKLE_2961, Container.INVENTORY)){
                addItem(player, Items.SILVER_SICKLEB_2963, 1)
            }
            return@onUseWith true
        }

        onUseWith(NPC, DRUID_POUCH, NPCs.GHAST_1052){player, used, with ->
            NSUtils.activatePouch(player, with as MortMyreGhastNPC)
        }

        onUseWith(SCENERY, items, *stones) { player, used, with ->
            when (used.id) {
                USED_SPELLCARD -> {
                    if (with.id == FREELY_GIVEN_STONE) {
                        if(removeItem(player, used, Container.INVENTORY)){
                            sendNPCDialogue(player, NPCs.FILLIMAN_TARLOCK_1050, "Aha, yes, that seems right well done!")
                            sendMessage(player, "The stone seems to absorb the used spell scroll.")
                            NSUtils.flagCardPlaced(player)
                        }
                    } else sendMessage(player, "You try to put the item on the stone, but it just moves off.")
                }

                FUNGUS -> {
                    if (with.id == NATURE_STONE) {
                        if(removeItem(player, used, Container.INVENTORY)){
                            sendNPCDialogue(player, NPCs.FILLIMAN_TARLOCK_1050, "Aha, yes, that seems right well done!")
                            sendMessage(player, "The stone seems to absorb the used fungus.")
                            NSUtils.flagFungusPlaced(player)
                        }
                    } else sendMessage(player, "You try to put the item on the stone, but it just moves off.")
                }
            }
            return@onUseWith true
        }
    }
}

class NSJournalDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> dialogue(*splitLines("Most of the writing is pretty uninteresting, but something inside refers to a nature spirit. The requirements for which are,")).also { stage++ }
            1 -> dialogue(*splitLines("'Something from nature', 'something with faith' and 'something of the spirit-to-become freely given'. It's all pretty vague.")).also { stage = END_DIALOGUE }
        }
    }
}

class FillimanCompletionDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> npcl(FacialExpression.NEUTRAL, "Well, hello there again. I was just enjoying the grotto. Many thanks for your help, I couldn't have become a Spirit of nature without you.").also { stage++ }
            1 -> npcl(FacialExpression.NEUTRAL, "I must complete the transformation now. Just stand there and watch the show, apparently it's quite good!").also { stage++ }
            2 -> {
                end()
                lock(player!!, 10)
                submitWorldPulse(CompleteSpellPulse(player!!))
            }
        }
    }
}

class CompleteSpellPulse(val player: Player) : Pulse(2){
    var counter = 0
    val locations = arrayOf(Location.create(3444, 9740, 0), Location.create(3439, 9740, 0), Location.create(3439, 9737, 0), Location.create(3444, 9737, 0), Location.create(3444, 9735, 0), Location.create(3438, 9735, 0))
    val dest = Location.create(3441, 9738, 0)
    override fun pulse(): Boolean {
        when(counter++){
            0 -> repeat(6) { spawnProjectile(locations[it], dest, 268, 0, 1000, 0, 40, 20) }
            1 -> player.questRepository.getQuest("Nature Spirit").setStage(player, 60)
            2 -> player.teleport(player.location.transform(0,0,1))
            3 -> openDialogue(player, NPCs.NATURE_SPIRIT_1051, findLocalNPC(player, NPCs.NATURE_SPIRIT_1051) as NPC).also { unlock(player); return true }
        }
        return false
    }
}
