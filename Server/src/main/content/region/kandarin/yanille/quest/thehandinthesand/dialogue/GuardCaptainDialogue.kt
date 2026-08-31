package content.region.kandarin.yanille.quest.thehandinthesand.dialogue

import content.data.Quests
import content.region.kandarin.yanille.quest.thehandinthesand.quest.TheHandintheSand
import core.api.addItem
import core.api.freeSlots
import core.api.getQuestStage
import core.api.hasAnItem
import core.api.inInventory
import core.api.openDialogue
import core.api.playAudio
import core.api.removeItem
import core.api.sendChat
import core.api.setQuestStage
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

/**
 * Dialogue between the Guard Captain and the player for The Hand in the Sand quest.
 * @author Edith
 */

@Initializable
class GuardCaptainDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return GuardCaptainDialogue(player)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GuardCaptainDialogueFile(), npc)
        return false
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_CAPTAIN_3109)
    }
}

class GuardCaptainDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, _ ->
            when (getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)) {
                TheHandintheSand.STAGE_QUEST_STARTED_5 -> loadLabel(player, "quest_stage_5")
                TheHandintheSand.STAGE_SEE_WIZARD_10 -> loadLabel(player, "quest_stage_10")
                else -> loadLabel(player, "standard_dialogue")
            }
        }

        label("standard_dialogue")
            player("Excuse me...")
            npc("I don' need a hand drinkin me beer, go 'way!")
            player("But...")
            npc("Talk to tha' hand coz thish face ain't lishtnin'!")

        label("quest_stage_5")
            exec { player, _ ->
                if (!inInventory(player, Items.SANDY_HAND_6945)) {
                    loadLabel(player, "no_sandy_hand")
                } else if (!inInventory(player, Items.BEER_1917)) {
                    loadLabel(player, "need_more_beer")
                } else {
                    loadLabel(player, "give_captain_beer")
                }
            }

        label("no_sandy_hand")
            line("Perhaps you should be carrying the hand that Bert gave you", "as evidence of the crime.")

        label("need_more_beer")
            npc("Need more beer...")

        label("give_captain_beer")
            player("Sir? I have some more beer for you...")
            item(Item(Items.BEER_1917), "You give the beer to the Guard Captain who takes a large gulp.")
            exec { player, _ ->
                removeItem(player, Items.BEER_1917)
                playAudio(player, Sounds.HANDSAND_GULP_1589)
            }
            npc(FacialExpression.DRUNK, "Ahh... jus' wha' I need, now, wha' did you wanna know?")
            player(FacialExpression.SAD_TALKING, "I've come to report that Bert, the sandman, found a hand in the sand pit.")
            npc("Lucky for him, means he can get even more work done.")
            player(FacialExpression.AMAZED_TALKING, "But aren't you going to find out who it ... belonged to?")
            exec { player, _ ->
                removeItem(player, Items.SANDY_HAND_6945)
                addItem(player, Items.BEER_SOAKED_HAND_6946)
                setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_SEE_WIZARD_10)
                playAudio(player, Sounds.HANDSAND_DROP_HAND_1588)
            }
            item(Item(Items.BEER_SOAKED_HAND_6946), "You hand the... hand... to the Guard Captain, he fumbles with it, drops it in his beer, fishes it out and hands it back.")
            npc(FacialExpression.DRUNK, "Oops, No 'arm done. S'prob'ly a wizard, i's always the wizards fault, go ask them, jus' ring the bell outshide the guild and talk to the first pointy hatted ninny you shee!")
            player("Err... ok, I'll go ring the bell and talk to a wizard then.")

        label("quest_stage_10")
            player("Hello Sir!")
            npc("Go 'way! This pint'sh nearly finished! Unnlessh you got more that ish....? Wizards, s'all the wizard's fault...prob'ly that Zavistic one, he'sh the worsht!")
            exec { player, _ ->
                if (hasAnItem(player, Items.BEER_SOAKED_HAND_6946).exists()) {
                    loadLabel(player, "has_beersoaked_hand")
                } else {
                    loadLabel(player, "lost_beersoaked_hand")
                }
            }

        label("has_beersoaked_hand")
            player("I think I should go talk to the wizards in the guild before he makes me buy him more beer!")

        label("lost_beersoaked_hand")
            npc("E're, you left this 'and in me beer!")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_beersoakedhand")
                }
            }
            item(Item(Items.BEER_SOAKED_HAND_6946), "The Guard Captain fishes the hand out of his beer and hands it to you.")
            exec { player, _ ->
                addItem(player, Items.BEER_SOAKED_HAND_6946)
            }

        label("no_space_beersoakedhand")
            npc("No good if you don' have space fer it in yer invent'ry, come back when you do.")

    }

}

@Initializable
class GuardCaptainNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return GuardCaptainNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_CAPTAIN_3109)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (RandomFunction.roll(25)) {
            sendChat(this, "*HIC*")
        }
    }

}