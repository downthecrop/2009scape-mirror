package content.region.kandarin.quest.observatoryquest

import content.data.Quests
import content.region.kandarin.quest.observatoryquest.ObservatoryQuest.Companion.attributeReceivedMould
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class SpiritOfScorpiusDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return SpiritOfScorpiusDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, SpiritOfScorpiusDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_OF_SCORPIUS_492)
    }
}

class SpiritOfScorpiusDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.SPIRIT_OF_SCORPIUS_492)

        exec { player, npc ->
            if (getQuestStage(player, Quests.OBSERVATORY_QUEST) == 100) {
                if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) loadLabel(player, "noghostspeak")
                else loadLabel(player, "observatoryqueststage100")
            } else loadLabel(player, "observatoryquestincomplete")
        }

        label("observatoryquestincomplete")
            line("They seem to be ignoring you.")

        label("noghostspeak")
            line("This powerful spirit seems capable of speaking to you", "even though you are not wearing an Amulet of Ghostspeak.")
            exec { player, _ ->
                loadLabel(player, "observatoryqueststage100")
            }

        label("observatoryqueststage100")
            npc("Who treads upon my grave?")
            options(
                DialogueOption("wisdom", "I seek your wisdom.") { player, _ ->
                    return@DialogueOption !getAttribute(player, attributeReceivedMould, false)
                },
                DialogueOption("anothermould", "I need another unholy symbol mould.", "I need another mould for the unholy symbol.") { player, _ ->
                    return@DialogueOption getAttribute(player, attributeReceivedMould, false)
                },
                DialogueOption("blessing", "I have come to seek your blessing."),
                DialogueOption("killyou", "I have come to kill you.")
            )

        label("wisdom")
            npc("Indeed, I feel you have beheld the far places in the heavens. My Lord instructs me to help you.")
            item(Item(Items.UNHOLY_MOULD_1594), "An unholy mould appears in your inventory.")
            exec { player, _ ->
                addItemOrDrop(player, Items.UNHOLY_MOULD_1594)
                setAttribute(player, attributeReceivedMould, true)
            }
            npc("Here is a mould to make a token for our Lord; a mould for the unholy symbol of Zamorak. Return to me when you desire my blessing.")

        label("anothermould")
            exec { player, _ ->
                if (inInventory(player, Items.UNHOLY_MOULD_1594)) {
                    loadLabel(player, "hasmould")
                }
                else {
                    addItemOrDrop(player, Items.UNHOLY_MOULD_1594)
                    loadLabel(player, "lostmould")
                }
            }

        label("hasmould")
            npc("One you already have, another is not needed. Leave me be.")

        label("lostmould")
            npc("A lost object is easy to replace. The loss of the affections of our Lord is impossible to forgive.")

        label("blessing")
            exec { player, _ ->
                if (inInventory(player, Items.UNPOWERED_SYMBOL_1722)) {
                    loadLabel(player, "canbless")
                }
                else loadLabel(player, "cannotbless")
            }

        label("canbless")
            npc("I see you have the unholy symbol of our Lord. I will bless it for you.")
            line("The ghost mutters in a strange voice.", "The unholy symbol throbs with power.")
            exec { player, _ ->
                removeItem(player, Items.UNPOWERED_SYMBOL_1722)
                addItemOrDrop(player, Items.UNHOLY_SYMBOL_1724)
            }
            npc("The symbol of our Lord has been blessed with power! My master calls.")


        label("cannotbless")
            npc("No blessing will be given to those who have no symbol of our Lord's love.")

        label("killyou")
            npc("The might of mortals, to me, is as the dust to the sea.")
    }
}