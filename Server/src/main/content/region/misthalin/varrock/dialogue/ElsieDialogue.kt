package content.region.misthalin.varrock.dialogue

import core.api.inInventory
import core.api.removeItem
import core.api.sendItemDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.dialogue.Topic
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

@Initializable
class ElsieDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return ElsieDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (hasTea()) {
            npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Ooh - that looks like a lovely cup of tea, dearie. Is it for me?"
            ).also { stage = 10 }
        } else {
            npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Hello dearie! What can old Elsie do for you?"
            )
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> showTopics(
                Topic(core.game.dialogue.FacialExpression.ASKING, "What are you making?", 1),
                Topic(core.game.dialogue.FacialExpression.ASKING, "Can you tell me a story?", 3),
                Topic(core.game.dialogue.FacialExpression.ASKING, "Can you tell me how to get rich?", 5),
                Topic(core.game.dialogue.FacialExpression.NEUTRAL, "I've got to go.", END_DIALOGUE)
            )

            1 -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "I'm knitting a new stole for Father Lawrence downstairs."
            ).also { stage++ }

            2 -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "He could do with something to keep his neck warm, standing in "
                + "that draughty old church all day."
            ).also { stage = START_DIALOGUE }

            3 -> {
                if (hasTea()) {
                    npcl(
                        core.game.dialogue.FacialExpression.HALF_THINKING,
                        "Perhaps... Can I have that lovely cup of tea you have over there?"
                    ).also { stage = 10 }
                } else {
                    npcl(
                        core.game.dialogue.FacialExpression.NEUTRAL,
                        "Maybe I could tell you a story if you'd fetch me a nice cup of tea."
                    ).also { stage++ }
                }
            }
            
            4 -> playerl(
                core.game.dialogue.FacialExpression.ROLLING_EYES,
                "I'll think about it."
            ).also { stage = START_DIALOGUE }

            5 -> npcl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "Well, dearie, I am probably not the best person to ask about money, but I think the best thing "
                + "would be for you to get a good trade."
            ).also { stage++ }

            6 -> npcl(
                core.game.dialogue.FacialExpression.HALF_WORRIED,
                "If you've got a good trade you can earn your way. That's what my old father would tell me."
            ).also { stage++ }

            7 -> npcl(
                core.game.dialogue.FacialExpression.HALF_WORRIED,
                "Saradomin rest his soul. I hear people try to get rich by fighting in the Wilderness north of here "
                + "or the Duel Arena in the south... But that's no way for honest folk to earn a living!"
            ).also { stage++ }

            8 -> npcl(
                core.game.dialogue.FacialExpression.HALF_WORRIED,
                "So get yourself a good trade, and keep working at it. There's always folks wanting to buy ore and food around here."
            ).also { stage++ }

            9 -> playerl(
                core.game.dialogue.FacialExpression.ROLLING_EYES,
                "Thanks, old woman."
            ).also { stage = START_DIALOGUE }

            10 -> showTopics(
                Topic(core.game.dialogue.FacialExpression.HAPPY, "Yes, you can have it.", 12),
                Topic(core.game.dialogue.FacialExpression.ANNOYED, "No, keep your hands off my tea.", 11)
            )

            11 -> npcl(
                core.game.dialogue.FacialExpression.SAD,
                "Aww. Maybe another time, then... Anyway, what can old Elsie do for you?"
            ).also { stage = START_DIALOGUE }

            12 -> sendItemDialogue(player, Items.CUP_OF_TEA_712, "Elsie takes a sip from the cup of tea.")
                 .also { stage++; removeItem(player, Items.CUP_OF_TEA_712) }

            13 -> npcl(
                core.game.dialogue.FacialExpression.HAPPY,
                "Ahh, there's nothing like a nice cuppa tea. You know what, I'll tell you a story as a thank-you for that lovely tea..."
            ).also { stage++ }

            14 -> npcl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "A long time ago, when I was a little girl, there was a handsome young man living in Varrock..."
            ).also { stage++ }

            15 -> npcl(
                core.game.dialogue.FacialExpression.HALF_GUILTY,
                "I saw him here in the church quite often. Everyone said he was going to become a priest and "
                + "we girls were so sad about that..."
            ).also { stage++ }

            16 -> npcl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "But young Dissy - that was the young man's nickname - he was a wild young thing."
            ).also { stage++ }

            17 -> npcl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "One night he gathered some lads together, and after the evening prayer-meeting "
                + "they all put their masks on..."
            ).also { stage++ }

            18 -> npcl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "Then, they snuck down to the temple in the south of the city - the evil one. "
                + "The next day, there was quite a hubbub..."
            ).also { stage++ }

            19 -> npcl(
                core.game.dialogue.FacialExpression.LAUGH,
                "The guards told us that someone had painted 'Saradomin pwns' on the wall of Zamorakian temple!"
            ).also { stage++ }

            20 -> npcl(
                core.game.dialogue.FacialExpression.HALF_THINKING,
                "Now, we'd always been taught to keep well away from that dreadful place..."
            ).also { stage++ }

            21 -> npcl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "But it really did us all good to see someone wasn't afraid of the scum who live at that end of town."
            ).also { stage++ }

            22 -> npcl(
                core.game.dialogue.FacialExpression.NEUTRAL,
                "Old Father Packett was furious, but Dissy just laughed it off. "
                + "Dissy left town after that, saying he wanted to see the world."
            ).also { stage++ }

            23 -> npcl(
                core.game.dialogue.FacialExpression.HALF_GUILTY,
                "It was such a shame, he had the most handsome... Shoulders..."
            ).also { stage++ }

            24 -> npcl(
                core.game.dialogue.FacialExpression.HAPPY,
                "One day, a young man came here looking for stories about Dissy - of course, "
                + "that's not his proper name, but his friends called him Dissy - and I told "
                + "him that one. "
            ).also { stage++ }

            25 -> npcl(
                core.game.dialogue.FacialExpression.HALF_WORRIED,
                "He said Dissy had become a really famous man and there was going to be a book about him. "
                + "That's good and all, but I do wish Dissy had just come back to Varrock. I did miss him so much... "
            ).also { stage++ }

            26 -> npcl(
                core.game.dialogue.FacialExpression.HAPPY,
                "Well, until I met my Freddie and we got married. But that's another story."
            ).also { stage++ }

            27 -> playerl(
                core.game.dialogue.FacialExpression.FRIENDLY,
                "Thank you. I'll leave you to your knitting now."
            ).also { stage = END_DIALOGUE }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ELSIE_5915)

    private fun hasTea(): Boolean {
        return inInventory(player, Items.CUP_OF_TEA_712)
    }
}
