package content.region.kandarin.ardougne.quest.plaguecity.dialogue

import content.data.Quests
import content.region.kandarin.ardougne.quest.plaguecity.PlagueCityListeners
import content.region.kandarin.ardougne.quest.plaguecity.UndergroundCutscene
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class EdmondDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        val questStage = getQuestStage(player, Quests.PLAGUE_CITY)
        npc = args[0] as NPC
        if(inEquipmentOrInventory(player, Items.GAS_MASK_1506) && (questStage == 2)) {
            playerl(FacialExpression.FRIENDLY, "Hi Edmond, I've got the gas mask now.").also { stage++ }
        }
        else if (questStage == 4) {
            // npcl not formatted quite right
            npc(FacialExpression.FRIENDLY, "I think it's the pipe to the south that comes up in West", "Ardougne.").also { stage++ }

        }
        else if(questStage == 5){
            playerl( FacialExpression.NEUTRAL,
            "Edmond, I can't get through to West Ardougne! There's an iron grill blocking my way, I can't pull it off alone."
            ).also { stage++ }

        }
        else if(questStage == 6){
            // playerl not formatted quite right
            player(FacialExpression.NEUTRAL, "I've tied a rope to the grill over there, will you help me","pull it off?").also { stage++ }

        }
        else if(questStage > 2) {
            playerl(FacialExpression.FRIENDLY, "Hello Edmond.").also { stage++ }
        }
        else {
            playerl(FacialExpression.FRIENDLY, "Hello old man.").also { stage++ }
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {

            0 -> when (stage) {
                1 -> npcl(FacialExpression.NEUTRAL, "Sorry, I can't stop to talk...").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "Why, what's wrong?").also { stage++ }
                3 -> npcl(
                    FacialExpression.FRIENDLY,
                    "I've got to find my daughter. I pray that she is still alive..."
                ).also { stage++ }

                4 -> options("What's happened to her?", "Well, good luck finding her.").also { stage++ }
                5 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "What's happened to her?").also { stage = 6 }
                    2 -> playerl(FacialExpression.FRIENDLY, "Well, good luck finding her.").also {
                        stage = END_DIALOGUE
                    }
                }
                // npcl does not word wrap correctly
                6 -> npc(
                    FacialExpression.NEUTRAL,
                    "Elena's a missionary and a healer. Three weeks ago she",
                    "managed to cross the Ardougne wall... No-one's allowed",
                    "to cross the wall in case they spread the plague. But",
                    "after hearing the screams of suffering she felt she had"
                ).also { stage++ }

                7 -> npcl(
                    FacialExpression.NEUTRAL,
                    "to help. She said she'd be gone for a few days but we've heard nothing since."
                ).also { stage++ }

                8 -> options(
                    "Tell me more about the plague.",
                    "Can I help find her?",
                    "I'm sorry, I have to go."
                ).also { stage++ }

                9 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "Tell me more about the plague.").also { stage = 10 }
                    2 -> playerl(FacialExpression.FRIENDLY, "Can I help find her?").also { stage = 11 }
                    3 -> playerl(FacialExpression.FRIENDLY, "I'm sorry, I have to go.").also { stage = END_DIALOGUE }
                }

                10 -> npcl(
                    FacialExpression.FRIENDLY,
                    "The mourners can tell you more than me. They're the only ones allowed to cross the border. I do know the plague is a horrible way to go... That's why Elena felt she had to go help."
                ).also { stage = 8 }

                11 -> npcl(
                    FacialExpression.FRIENDLY,
                    "Really, would you? I've been working on a plan to get into West Ardougne, but I'm too old and tired to carry it through. If you're going into West Ardougne you'll need protection from the plague. My wife made a"
                ).also { stage++ }

                12 -> npcl(
                    FacialExpression.FRIENDLY,
                    "special gas mask for Elena with dwellberries rubbed into it. Dwellberries help repel the virus! We need some more though..."
                ).also { stage++ }

                13 -> playerl(FacialExpression.ASKING, "Where can I find these dwellberries?").also { stage++ }
                14 -> npcl(
                    FacialExpression.FRIENDLY,
                    "The only place I know of is McGrubor's Wood just north of the Rangers' Guild."
                ).also { stage++ }

                15 -> playerl(FacialExpression.FRIENDLY, "Ok, I'll go and get some.").also { stage++ }
                16 -> npcl(
                    FacialExpression.NEUTRAL,
                    "The foresters keep a close eye on it, but there is a back way in."
                ).also { stage++ }

                17 -> {
                    end()
                    setQuestStage(player!!, Quests.PLAGUE_CITY, 1)
                }
            }

            1 -> when (stage) {
                1 -> npcl(FacialExpression.NEUTRAL, "Have you got the dwellberries yet?").also { stage++ }
                2 -> if (!inInventory(player, Items.DWELLBERRIES_2126)) {
                    playerl(FacialExpression.FRIENDLY, "Sorry, I'm afraid not.").also { stage = 3 }
                } else {
                    playerl(FacialExpression.FRIENDLY, "Yes I've got some here.").also { stage = 6 }
                }

                3 -> npcl(
                    FacialExpression.NEUTRAL,
                    "You'll probably find them in McGrubor's Wood it's just west of Seers village."
                ).also { stage++ }

                4 -> playerl(FacialExpression.NEUTRAL, "Ok, I'll go and get some.").also { stage++ }
                5 -> npcl(
                    FacialExpression.NEUTRAL,
                    "The foresters keep a close eye on it, but there is a back way in."
                ).also { stage = END_DIALOGUE }

                6 -> npcl(FacialExpression.NEUTRAL, "Take them to my wife Alrena, she's inside.").also {
                    stage = END_DIALOGUE
                }
            }

            2 -> when (stage) {
                // npcl formats this wrong
                1 -> npc(
                    FacialExpression.NEUTRAL,
                    "Good stuff, now for the digging. Beneath us are the",
                    "Ardougne sewers, there you'll find",
                    "access to West Ardougne."
                ).also { stage++ }

                2 -> npc(
                    FacialExpression.NEUTRAL, "The problem is the soil is rock hard. You'll need to pour",
                    "on several buckets of water to soften it up. I'll keep an",
                    "eye out for the mourners."
                ).also { stage++ }

                3 -> {
                    end()
                    setQuestStage(player!!, Quests.PLAGUE_CITY, 3)
                }
            }

            3 -> when (stage) {
                1 -> if (player!!.getAttribute("/save:elena:dig", false) == true) {
                    playerl(FacialExpression.NEUTRAL, "I've soaked the soil with water.").also { stage = 3 }
                } else {
                    npcl(FacialExpression.FRIENDLY, "How's it going?").also { stage = 2 }
                }

                2 -> if (player.getAttribute(PlagueCityListeners.BUCKET_USES_ATTRIBUTE, 0) == 1) {
                    playerl(
                        FacialExpression.NEUTRAL,
                        "I still need to pour three more buckets of water on the soil."
                    ).also { stage = END_DIALOGUE }
                } else if (player.getAttribute(PlagueCityListeners.BUCKET_USES_ATTRIBUTE, 0) == 2) {
                    playerl(
                        FacialExpression.NEUTRAL,
                        "I still need to pour two more buckets of water on the soil."
                    ).also { stage = END_DIALOGUE }
                } else if (player.getAttribute(PlagueCityListeners.BUCKET_USES_ATTRIBUTE, 0) == 3) {
                    playerl(
                        FacialExpression.NEUTRAL,
                        "I still need to pour one more bucket of water on the soil."
                    ).also { stage = END_DIALOGUE }
                }

                3 -> npcl(
                    FacialExpression.FRIENDLY,
                    "That's great, it should be soft enough to dig through now."
                ).also { stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                1 -> playerl(FacialExpression.NEUTRAL, "Alright I'll check it out.").also { stage = END_DIALOGUE }
            }

            5 -> when (stage) {
                1 -> npcl(
                    FacialExpression.NEUTRAL,
                    "If you get some rope you could tie to the grill, then we could both pull it at the same time."
                ).also { stage = END_DIALOGUE }
            }

            6 -> when(stage){
                1 -> npcl(FacialExpression.NEUTRAL, "Alright, let's get to it...").also { stage++ }
                2 -> end().also{ UndergroundCutscene(player!!).start() }
            }
            7-> when (stage) {
                1 -> npcl(FacialExpression.NEUTRAL, "Have you found Elena yet?").also { stage++ }
                2 -> playerl(FacialExpression.NEUTRAL, "Not yet, it's a big city over there.").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "Don't forget to look for my friend Jethick. He may be able to help.").also { stage = END_DIALOGUE }
            }

            8 -> when (stage) {
                1 -> npcl(FacialExpression.NEUTRAL, "Have you found Elena yet?").also { stage++ }
                2 -> playerl(FacialExpression.NEUTRAL, "Not yet, it's a big city over there. Do you have a picture of Elena?").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "There should be a picture of Elena in the house. Please find her quickly, I hope it's not too late.").also { stage = END_DIALOGUE }
            }

            // No source for this branch
            in 9..98 -> when(stage) {
                1 -> npcl(FacialExpression.NEUTRAL, "Have you found Elena yet?").also { stage++ }
                2 -> playerl(FacialExpression.SAD, "Not yet.").also { stage++ }
                3 -> npcl(FacialExpression.WORRIED, "Please find her quickly, I hope it's not too late.").also { stage = END_DIALOGUE }
            }

            99 -> when (stage) {
                1 -> npcl(FacialExpression.NEUTRAL, "Thank you, thank you! Elena beat you back by minutes.").also { stage++ }
                2 -> npcl(FacialExpression.NEUTRAL, "Now I said I'd give you a reward. What can I give you as a reward I wonder?").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "Here take this magic scroll, I have little use for it but it may help you.").also { stage++ }
                4 -> {
                    end()
                    player!!.questRepository.getQuest(Quests.PLAGUE_CITY).finish(player)
                }
            }

            100 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Ah hello again, and thank you again for rescuing my daughter.").also {
                    stage = if (
                        getAttribute(player, PlagueCityListeners.ARDOUGNE_TELE_ATTRIBUTE, false)
                        || hasAnItem(player, Items.A_MAGIC_SCROLL_1505).exists()) 5 else 2
                }
                2 -> showTopics(
                    Topic("Do you have any more of those scrolls?", 3),
                    Topic("No problem.", END_DIALOGUE)
                )
                3 -> npcl(FacialExpression.FRIENDLY, "Here take this magic scroll, I have little use for it but it may help you.").also {
                    stage = END_DIALOGUE
                    addItemOrDrop(player!!, Items.A_MAGIC_SCROLL_1505)
                }
                5 -> playerl(FacialExpression.NEUTRAL, "No problem.").also { stage = END_DIALOGUE }
                }
            }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.EDMOND_3213, NPCs.TUNNEL_EDMOND_3214)
}
