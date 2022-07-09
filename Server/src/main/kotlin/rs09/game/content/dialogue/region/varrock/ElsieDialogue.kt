package rs09.game.content.dialogue.region.varrock

import api.inInventory
import api.removeItem
import api.sendInputDialogue
import api.sendItemDialogue
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.Topic
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

@Initializable
class ElsieDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player?): DialoguePlugin {
        return ElsieDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (hasTea()) {
            npcl(
                FacialExpression.FRIENDLY,
                "Ooh - that looks like a lovely cup of tea, dearie. Is it for me?"
            ).also { stage = 10 }
        } else {
            npcl(
                FacialExpression.FRIENDLY,
                "Hello dearie! What can old Elsie do for you?"
            )
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> showTopics(
                Topic(FacialExpression.ASKING, "What are you making?", 1),
                Topic(FacialExpression.ASKING, "Can you tell me a story?", 3),
                Topic(FacialExpression.ASKING, "Can you tell me how to get rich?", 5),
                Topic(FacialExpression.NEUTRAL, "I've got to go.", END_DIALOGUE)
            )

            1 -> npcl(
                FacialExpression.FRIENDLY,
                "I'm knitting a new stole for Father Lawrence downstairs."
            ).also { stage++ }

            2 -> npcl(
                FacialExpression.FRIENDLY,
                "He could do with something to keep his neck warm, standing in "
                + "that draughty old church all day."
            ).also { stage = START_DIALOGUE }

            3 -> {
                if (hasTea()) {
                    npcl(
                        FacialExpression.HALF_THINKING,
                        "Perhaps... Can I have that lovely cup of tea you have over there?"
                    ).also { stage = 10 }
                } else {
                    npcl(
                        FacialExpression.NEUTRAL,
                        "Maybe I could tell you a story if you'd fetch me a nice cup of tea."
                    ).also { stage++ }
                }
            }
            
            4 -> playerl(
                FacialExpression.ROLLING_EYES,
                "I'll think about it."
            ).also { stage = START_DIALOGUE }

            5 -> npcl(
                FacialExpression.NEUTRAL,
                "Well, dearie, I am probably not the best person to ask about money, but I think the best thing "
                + "would be for you to get a good trade."
            ).also { stage++ }

            6 -> npcl(
                FacialExpression.HALF_WORRIED,
                "If you've got a good trade you can earn your way. That's what my old father would tell me."
            ).also { stage++ }

            7 -> npcl(
                FacialExpression.HALF_WORRIED,
                "Saradomin rest his soul. I hear people try to get rich by fighting in the Wilderness north of here "
                + "or the Duel Arena in the south... But that's no way for honest folk to earn a living!"
            ).also { stage++ }

            8 -> npcl(
                FacialExpression.HALF_WORRIED,
                "So get yourself a good trade, and keep working at it. There's always folks wanting to buy ore and food around here."
            ).also { stage++ }

            9 -> playerl(
                FacialExpression.ROLLING_EYES,
                "Thanks, old woman."
            ).also { stage = START_DIALOGUE }

            10 -> showTopics(
                Topic(FacialExpression.HAPPY, "Yes, you can have it.", 12),
                Topic(FacialExpression.ANNOYED, "No, keep your hands off my tea.", 11)
            )

            11 -> npcl(
                FacialExpression.SAD,
                "Aww. Maybe another time, then... Anyway, what can old Elsie do for you?"
            ).also { stage = START_DIALOGUE }

            12 -> sendItemDialogue(player, Items.CUP_OF_TEA_712, "Elsie takes a sip from the cup of tea.")
                 .also { stage++; removeItem(player, Items.CUP_OF_TEA_712) }

            13 -> npcl(
                FacialExpression.HAPPY,
                "Ahh, there's nothing like a nice cuppa tea. You know what, I'll tell you a story as a thank-you for that lovely tea..."
            ).also { stage++ }

            14 -> npcl(
                FacialExpression.NEUTRAL,
                "A long time ago, when I was a little girl, there was a handsome young man living in Varrock..."
            ).also { stage++ }

            15 -> npcl(
                FacialExpression.HALF_GUILTY,
                "I saw him here in the church quite often. Everyone said he was going to become a priest and "
                + "we girls were so sad about that..."
            ).also { stage++ }

            16 -> npcl(
                FacialExpression.NEUTRAL,
                "But young Dissy - that was the young man's nickname - he was a wild young thing."
            ).also { stage++ }

            17 -> npcl(
                FacialExpression.NEUTRAL,
                "One night he gathered some lads together, and after the evening prayer-meeting "
                + "they all put their masks on..."
            ).also { stage++ }

            18 -> npcl(
                FacialExpression.NEUTRAL,
                "Then, they snuck down to the temple in the south of the city - the evil one. "
                + "The next day, there was quite a hubbub..."
            ).also { stage++ }

            19 -> npcl(
                FacialExpression.LAUGH,
                "The guards told us that someone had painted 'Saradomin pwns' on the wall of Zamorakian temple!"
            ).also { stage++ }

            20 -> npcl(
                FacialExpression.HALF_THINKING,
                "Now, we'd always been taught to keep well away from that dreadful place..."
            ).also { stage++ }

            21 -> npcl(
                FacialExpression.FRIENDLY,
                "But it really did us all good to see someone wasn't afraid of the scum who live at that end of town."
            ).also { stage++ }

            22 -> npcl(
                FacialExpression.NEUTRAL,
                "Old Father Packett was furious, but Dissy just laughed it off. "
                + "Dissy left town after that, saying he wanted to see the world."
            ).also { stage++ }

            23 -> npcl(
                FacialExpression.HALF_GUILTY,
                "It was such a shame, he had the most handsome... Shoulders..."
            ).also { stage++ }

            24 -> npcl(
                FacialExpression.HAPPY,
                "One day, a young man came here looking for stories about Dissy - of course, "
                + "that's not his proper name, but his friends called him Dissy - and I told "
                + "him that one. "
            ).also { stage++ }

            25 -> npcl(
                FacialExpression.HALF_WORRIED,
                "He said Dissy had become a really famous man and there was going to be a book about him. "
                + "That's good and all, but I do wish Dissy had just come back to Varrock. I did miss him so much... "
            ).also { stage++ }

            26 -> npcl(
                FacialExpression.HAPPY,
                "Well, until I met my Freddie and we got married. But that's another story."
            ).also { stage++ }

            27 -> playerl(
                FacialExpression.FRIENDLY,
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
