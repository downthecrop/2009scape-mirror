package content.region.kandarin.yanille.dialogue

import content.region.kandarin.feldip.quest.zogreflesheaters.ZogreFleshEaters
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class ZavisticRarveDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return ZavisticRarveDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, ZavisticRarveDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ZAVISTIC_RARVE_2059)
    }
}
class ZavisticRarveDialogueFile : DialogueBuilderFile() {

    companion object {
        fun dialogueInitialTalk(builder: DialogueBuilder) : DialogueBuilder {
            return builder
                    .branch { player ->
                        return@branch if (getQuestStage(player, ZogreFleshEaters.questName) > 3 /* || hand in the sand quest */) {
                            1
                        } else {
                            0
                        }
                    }.let { branch2 ->
                        val returnJoin = builder.placeholder()
                        branch2.onValue(1)
                                .npcl("What are you doing...Oh, it's you...sorry...didn't realise...what can I do for you?")
                                // There is a fork here if you are doing hand in the sand.
                                .goto(returnJoin)
                        branch2.onValue(0)
                                .npcl("What are you doing bothering me? Don't you think some of us have work to do?")
                                .playerl("I thought you were here to help?")
                                .npcl("Well... I am, I suppose, anyway... we're very busy here, hurry up, what do you want?")
                                .goto(returnJoin)
                        return@let returnJoin.builder()
                    }
        }

        fun dialogueInitialTalkViaBell(builder: DialogueBuilder) : DialogueBuilder {
            return builder
                    .branch { player ->
                        return@branch if (getQuestStage(player, ZogreFleshEaters.questName) > 3 /* || hand in the sand quest */) {
                            1
                        } else {
                            0
                        }
                    }.let { branch2 ->
                        val returnJoin = builder.placeholder()
                        branch2.onValue(1)
                                .npcl("What are you doing...Oh, it's you...sorry...didn't realise...what can I do for you?")
                                .goto(returnJoin)
                        branch2.onValue(0)
                                .npcl("What are you doing ringing that bell?! Don't you think some of us have work to do?")
                                .playerl("But I was told to ring the bell if I wanted some attention.")
                                .npcl("Well...anyway...we're very busy here, hurry up what do you want?")
                                .goto(returnJoin)
                        return@let returnJoin.builder()
                    }
        }

        fun defaultTalk(continueBuilder: DialogueBuilder) {
            continueBuilder.let { builder ->
                val returnJoin = builder.placeholder()
                returnJoin.builder()
                        .options()
                        .let { optionBuilder ->
                            optionBuilder.option_playerl("What is there to do in the Wizards' Guild?")
                                    .npcl("This is the finest wizards' establishment in the land.")
                                    .npcl("We have magic portals to the other towers of wizardry around Gielinor.")
                                    .npcl("We have a particularly wide collection of runes in our rune shop.")
                                    .npcl("We sell some of the finest mage robes in the land and we have a training area full of zombies for you to practice your magic on.")
                                    .goto(returnJoin)
                            optionBuilder.option_playerl("What are the requirements to get in the Wizards' Guild?")
                                    .npcl("You need a magic level of 66, the high magic energy level is too dangerous for anyone below that level.")
                                    .goto(returnJoin)
                            optionBuilder.option_playerl("What do you do in the Guild?")
                                    .npcl("I'm the Grand Secretary for the Wizards' Guild, I have lots of correspondence to keep up with, as well as attending to the discipline of the more problematic guild members.")
                                    .goto(returnJoin)
                            optionBuilder.option_playerl("Ok, thanks.")
                                    .end()
                        }
                builder.goto(returnJoin)
            }
        }
    }


    override fun create(b: DialogueBuilder) {

        b.onPredicate { player -> isQuestComplete(player, ZogreFleshEaters.questName) }
                .let { dialogueInitialTalk(it) }
                .let { defaultTalk(it) }

        // This is during the ZogreFleshEaters quest
        b.onPredicate { player -> isQuestInProgress(player, ZogreFleshEaters.questName, 2, 99) }
                .manualStage() { df, player, _, _ ->
                    openDialogue(player, content.region.kandarin.feldip.quest.zogreflesheaters.ZavisticRarveDialogueFile(), npc!!)
                }
                .end()

        b.onPredicate { _ -> true }
                .let { dialogueInitialTalk(it) }
                .let { defaultTalk(it) }

    }
}