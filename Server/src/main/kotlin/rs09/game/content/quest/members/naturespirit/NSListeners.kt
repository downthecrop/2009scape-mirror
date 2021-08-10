package rs09.game.content.quest.members.naturespirit

import api.ContentAPI
import api.DialUtils
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.content.global.action.PickupHandler
import rs09.game.interaction.InteractionListener
import rs09.game.system.SystemLogger
import rs09.tools.END_DIALOGUE
import sun.audio.AudioPlayer.player

class NSListeners : InteractionListener() {

    val GROTTO_TREE = 3517
    val GROTTO_ENTRANCE = 3516
    val JOURNAL = Items.JOURNAL_2967
    val NATURE_STONE = 3527
    val FAITH_STONE = 3528
    val FREELY_GIVEN_STONE = 3529
    val WASHING_BOWL = Items.WASHING_BOWL_2964
    val MIRROR = Items.MIRROR_2966
    val MIRROR_TAKEN = "/save:ns:mirror_taken"
    val GROTTO_SEARCHED = "/save:ns:grotto_searched"

    override fun defineListeners() {
        on(GROTTO_TREE, SCENERY, "look-at"){player, _ ->
            ContentAPI.sendMessage(player, "It looks like a tree on a large rock with roots trailing down to the ground.")
            return@on true
        }

        on(GROTTO_TREE, SCENERY, "search"){player, _ ->
            if(!ContentAPI.getAttribute(player, GROTTO_SEARCHED, false) || !(ContentAPI.inInventory(player, JOURNAL) || ContentAPI.inBank(player, JOURNAL))){
                ContentAPI.sendItemDialogue(player, JOURNAL, "You search the strange rock. You find a knot and inside of it you discover a small tome. The words on the front are a bit vague, but you can make out the words 'Tarlock' and 'journal'.")
                ContentAPI.addItemOrDrop(player, JOURNAL, 1)
                ContentAPI.setAttribute(player, GROTTO_SEARCHED, true)
                return@on true
            }
            return@on false
        }

        on(GROTTO_ENTRANCE, SCENERY, "enter"){player, node ->
            val npc = core.game.node.entity.npc.NPC.create(NPCs.FILLIMAN_TARLOCK_1050, Location.create(3440, 3336, 0))
            npc.init()
            return@on true
        }

        on(NATURE_STONE, SCENERY, "search"){player, _ ->
            ContentAPI.sendDialogue(player, "You search the stone and find that it has some sort of nature symbol scratched into it.")
            return@on true
        }

        on(FAITH_STONE, SCENERY, "search"){player, _ ->
            ContentAPI.sendDialogue(player, "You search the stone and find that it has some sort of faith symbol scratched into it.")
            return@on true
        }

        on(FREELY_GIVEN_STONE, SCENERY, "search"){player, _ ->
            ContentAPI.sendDialogue(player, "You search the stone and find it has some sort of spirit symbol scratched into it.")
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
            if(ContentAPI.getAttribute(player, MIRROR_TAKEN, false) && (ContentAPI.inInventory(player, MIRROR) || ContentAPI.inBank(player, MIRROR))){
                ContentAPI.sendDialogue(player, "I don't need another one of these.")
                return@on true
            }
            ContentAPI.setAttribute(player, MIRROR_TAKEN, true)
            PickupHandler.take(player, node as GroundItem)
            return@on true
        }
    }
}

class NSJournalDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> dialogue(*DialUtils.splitLines("Most of the writing is pretty uninteresting, but something inside refers to a nature spirit. The requirements for which are,")).also { stage++ }
            1 -> dialogue(*DialUtils.splitLines("'Something from nature', 'something with faith' and 'something of the spirit-to-become freely given'. It's all pretty vague.")).also { stage = END_DIALOGUE }
        }
    }
}