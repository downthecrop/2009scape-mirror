package content.region.kandarin.ardougne.quest.plaguecity.dialogue

import content.data.Quests
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class BravekDialogue(player: Player? = null) : DialoguePlugin(player) {

    companion object{
        const val SPEAK_ANOTHER_DAY = 10
        const val WAITING_FOR_CURE = 20
        const val CURED = 30
        const val IMPORTANT = 50
        const val DRINK_TOO_MUCH = 60
        const val CURE = 70
        const val WHAT_HELP = 80
        const val ALL_PEOPLE_SAY = 90
        const val NOT_LISTEN = 100
        const val INTEREST = 110
        const val WEAK_LEADER = 120
    }

    override fun open(vararg args: Any?): Boolean {
        npc = if (args[0] is Int) NPC(args[0] as Int) else args[0] as NPC
        when(getQuestStage(player, Quests.PLAGUE_CITY)){
            in 0..13 -> npcl(FacialExpression.ANNOYED, "My head hurts! I'll speak to you another day...").also { stage = SPEAK_ANOTHER_DAY }
            14 -> npcl(FacialExpression.ANNOYED, " Uurgh! My head still hurts too much to think straight. " +
                    "Oh for one of Trudi's hangover cures!").also { stage = if(hasAnItem(player, Items.HANGOVER_CURE_1504).exists()) WAITING_FOR_CURE else END_DIALOGUE }
            15 -> npcl(FacialExpression.FRIENDLY, "Ah now, what was it you wanted me to do for you?").also { stage = WHAT_HELP }
            else -> npcl(FacialExpression.FRIENDLY, "Thanks again for the hangover cure.").also { stage = if(hasAnItem(player, Items.WARRANT_1503).exists() || getQuestStage(player, Quests.PLAGUE_CITY) >= 99) CURED else WHAT_HELP }

        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (stage){

            SPEAK_ANOTHER_DAY -> showTopics(
                Topic("This is really important though!", IMPORTANT),
                Topic("Ok, goodbye.", END_DIALOGUE)
            )

            IMPORTANT -> npc(FacialExpression.ANNOYED,
                "I can't possibly speak to you with my head spinning like",
                "this... I went a bit heavy on the drink again last night.",
                "Curse my herbalist, she made the best hang over cures.",
                "Darn inconvenient of her catching the plague.").also { stage++ }
            IMPORTANT + 1 -> showTopics(
                Topic("Ok, goodbye.", END_DIALOGUE),
                Topic(" You shouldn't drink so much then!", DRINK_TOO_MUCH),
                Topic("Do you know what's in the cure?", CURE)
            )

            DRINK_TOO_MUCH -> npcl(FacialExpression.ANNOYED, "Well positions of responsibility are hard, " +
                    "I need something to take my mind off things... Especially with the problems this place has.").also { stage++ }
            DRINK_TOO_MUCH + 1 -> showTopics(
                Topic("Ok, goodbye.", END_DIALOGUE),
                Topic("Do you know what's in the cure?", CURE),
                Topic("I don't think drink is the solution.", DRINK_TOO_MUCH+2)
            )
            DRINK_TOO_MUCH + 2 -> npcl(FacialExpression.ANNOYED, "Uurgh! My head still hurts too much to think straight. Oh for one of Trudi's hangover cures!").also { stage = END_DIALOGUE }

            CURE -> npc(FacialExpression.NEUTRAL,
                "Hmmm let me think... Ouch! Thinking isn't clever. Ah",
                "here, she did scribble it down for me.").also { stage++ }
            CURE + 1 -> {
                if(hasSpaceFor(player!!, Item(Items.A_SCRUFFY_NOTE_1508))) {
                    sendItemDialogue(
                        player!!,
                        Items.A_SCRUFFY_NOTE_1508,
                        "Bravek hands you a tatty piece of paper."
                    ).also { stage++ }
                }
                else{
                    sendItemDialogue(player!!, Items.A_SCRUFFY_NOTE_1508, "Bravek waves a note in front of you but you do not have space for it").also { stage = END_DIALOGUE }
                }
            }
            CURE + 2 ->{
                addItem(player!!, Items.A_SCRUFFY_NOTE_1508)
                setQuestStage(player!!, Quests.PLAGUE_CITY, 14)
                end()
            }

            WAITING_FOR_CURE -> playerl(FacialExpression.NEUTRAL, "Try this.").also { stage++ }
            WAITING_FOR_CURE + 1 -> {
                animate(npc, 1330) // Drink hangover cure.
                findLocalNPC(player!!, NPCs.BRAVEK_711)!!.sendChat("Grruurgh!")
                sendItemDialogue(player!!, Items.HANGOVER_CURE_1504, "You give Bravek the hangover cure. Bravek gulps down the foul-looking liquid.").also { stage++ }
                setQuestStage(player, Quests.PLAGUE_CITY, 15)
                removeItem(player, Items.HANGOVER_CURE_1504)
            }
            WAITING_FOR_CURE + 2 -> npcl(FacialExpression.HAPPY, "Ooh that's much better! Thanks, that's the clearest my head has felt in a month." +
                    "Ah now, what was it you wanted me to do for you?"
            ).also { stage = WHAT_HELP }

            WHAT_HELP -> playerl(FacialExpression.ASKING, "I need to rescue a kidnap victim called Elena. She's being held in a plague house, I need permission to enter.").also { stage++ }
            WHAT_HELP + 1 -> npcl(FacialExpression.HALF_GUILTY, "Well the mourners deal with that sort of thing...").also { stage++ }
            WHAT_HELP + 2 -> showTopics(
                Topic("Ok, I'll go speak to them.", END_DIALOGUE),
                Topic("Is that all anyone says around here?", ALL_PEOPLE_SAY),
                Topic("They won't listen to me!", NOT_LISTEN, skipPlayer = true)
            )

            ALL_PEOPLE_SAY -> npcl(FacialExpression.HALF_GUILTY, "Well, they know best about plague issues.").also { stage++ }
            ALL_PEOPLE_SAY + 1 -> showTopics(
                Topic("Don't you want to take an interest in it at all?", INTEREST),
                Topic("They won't listen to me!", NOT_LISTEN, skipPlayer = true)
            )

            INTEREST -> npcl(FacialExpression.HALF_GUILTY, "Nope, I don't wish to take a deep interest in plagues. That stuff is too scary for me!").also { stage++ }
            INTEREST + 1 -> showTopics(
                Topic("I see why people say you're a weak leader.", WEAK_LEADER),
                Topic("Ok, I'll talk to the mourners.", END_DIALOGUE),
                Topic("They won't listen to me!", NOT_LISTEN, skipPlayer = true)
            )

            WEAK_LEADER -> npcl(FacialExpression.ANNOYED, "Bah, people always criticise their leaders but delegating is the only way to lead. I delegate all plague issues to the mourners.").also { stage++ }
            WEAK_LEADER + 1 -> playerl(FacialExpression.ANNOYED, "This whole city is a plague issue!").also { stage = END_DIALOGUE }

            NOT_LISTEN -> playerl(FacialExpression.ANNOYED, "They won't listen to me! They say I'm not properly equipped to go in the house, though I do have a very effective gas mask.").also { stage++ }
            // npcl does not work
            NOT_LISTEN + 1 -> npc(FacialExpression.ANNOYED,
                "Hmmm, well I guess they're not taking the issue of a",
                "kidnapping seriously enough. They do go a bit far",
                "sometimes. I've heard of Elena, she has helped us a lot...",
                "Ok, I'll give you this warrant to enter the house."
            ).also { stage++ }
            NOT_LISTEN + 2 -> {
                if (freeSlots(player!!) == 0) {
                    sendItemDialogue(player!!, Items.WARRANT_1503, "Bravek waves a warrant at you, but you don't have room to take it.").also { stage = END_DIALOGUE }
                } else {
                    sendItemDialogue(player!!, Items.WARRANT_1503, "Bravek hands you a warrant.").also { stage = END_DIALOGUE }
                    addItem(player!!, Items.WARRANT_1503)
                    setQuestStage(player!!, Quests.PLAGUE_CITY, 16)
                }
            }

            CURED -> playerl(FacialExpression.FRIENDLY, "Not a problem, happy to help out.").also { stage++ }
            CURED + 1 -> npcl(FacialExpression.FRIENDLY, " I'm just having a little drop of whisky, then I'll feel really good.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BRAVEK_711)
}