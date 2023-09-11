package content.region.misc.keldagrim.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Components
import org.rs09.consts.NPCs

@Initializable
class BlastFurnaceForemanDialogue (player: Player? = null) : DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> npcl(FacialExpression.OLD_ANGRY1, "You! Get to work!").also { stage++ }
            1 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "What?", 2),
                    Topic(FacialExpression.FRIENDLY, "Okay.", END_DIALOGUE),
            )
            2 -> npcl(FacialExpression.OLD_DEFAULT, "You are here to help work the blast furnace, aren't you?").also { stage++ }
            3 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "What's the blast furnace?", 5),
                    Topic(FacialExpression.FRIENDLY, "How can I help work the blast furnace?", 10),
                    Topic(FacialExpression.FRIENDLY, "Can I use the furnace to smelt ore?", 40),
                    Topic(FacialExpression.FRIENDLY, "Maybe some other time.", 102),
            )

            5 -> npcl(FacialExpression.OLD_DEFAULT, "The blast furnace is the pinnacle of dwarven metal- processing! Ore goes in the top and comes out as metal bars almost at once! And it's so efficient it only takes half as much coal to purify it as a regular furnace.").also { stage++ }
            6 -> npcl(FacialExpression.OLD_DEFAULT, "But we've got a bit of a labour shortage at the moment, which is why I have to rely on immigrant workers to keep the furnace going. So, are you here to start work?").also { stage++ }
            7 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "How can I help work the blast furnace?", 10),
                    Topic(FacialExpression.FRIENDLY, "Can I use the furnace to smelt ore?", 40),
                    Topic(FacialExpression.FRIENDLY, "Maybe some other time.", 102),
            )

            10 -> npcl(FacialExpression.OLD_DEFAULT, "The blast furnace will only work if there is a group of people keeping it going. Let me explain...").also { stage++ }
            11 -> {
                openInterface(player, Components.BLAST_FURNACE_PLAN_SCROLL_29)
                submitIndividualPulse(player, object : Pulse() {
                    var flash = true
                    override fun pulse(): Boolean {
                        setComponentVisibility(player, Components.BLAST_FURNACE_PLAN_SCROLL_29, 2, flash)
                        flash = !flash
                        return false
                    }
                })
                npcl(FacialExpression.OLD_DEFAULT, "Firstly, the stove needs to be kept filled with coal. If this runs out the furnace will not work.")
                stage++
            }
            12 -> {
                setComponentVisibility(player, Components.BLAST_FURNACE_PLAN_SCROLL_29, 2, false)
                submitIndividualPulse(player, object : Pulse() {
                    var flash = true
                    override fun pulse(): Boolean {
                        setComponentVisibility(player, Components.BLAST_FURNACE_PLAN_SCROLL_29, 14, flash)
                        flash = !flash
                        return false
                    }
                })
                npcl(FacialExpression.OLD_DEFAULT, "Secondly, someone needs to operate the pump that keeps the hot air blast circulating through the furnace.")
                stage++
            }
            13 -> {
                setComponentVisibility(player, Components.BLAST_FURNACE_PLAN_SCROLL_29, 14, false)
                submitIndividualPulse(player, object : Pulse() {
                    var flash = true
                    override fun pulse(): Boolean {
                        setComponentVisibility(player, Components.BLAST_FURNACE_PLAN_SCROLL_29, 15, flash)
                        flash = !flash
                        return false
                    }
                })
                npcl(FacialExpression.OLD_DEFAULT, "Thirdly, someone needs to keep an eye on the temperature gauge. They should tell the pumper to start or stop so that the temperature stays in the right range.")
                stage++
            }
            14 -> {
                setComponentVisibility(player, Components.BLAST_FURNACE_PLAN_SCROLL_29, 15, false)
                submitIndividualPulse(player, object : Pulse() {
                    var flash = true
                    override fun pulse(): Boolean {
                        setComponentVisibility(player, Components.BLAST_FURNACE_PLAN_SCROLL_29, 13, flash)
                        flash = !flash
                        return false
                    }
                })
                npcl(FacialExpression.OLD_DEFAULT, "Lastly, someone needs to be on the pedals to power the conveyor belt that puts ore into the furnace.")
                stage++
            }
            15 -> npcl(FacialExpression.OLD_DEFAULT, "Someone will also need to be on standby with a hammer in case the machine breaks!").also { stage++ }
            16 -> npcl(FacialExpression.OLD_DEFAULT, "While that's going, anyone else can put their ore on the conveyor belt to have it smelted.").also { stage++ }
            17 -> {
                setComponentVisibility(player, Components.BLAST_FURNACE_PLAN_SCROLL_29, 13, false)
                closeInterface(player)
                showTopics(
                    Topic(FacialExpression.FRIENDLY, "So what do I get out of it?", 20),
                    Topic(FacialExpression.FRIENDLY, "Okay I'll get to work.", 101),
                    Topic(FacialExpression.FRIENDLY, "Maybe some other time.", 102),
                )
            }

            20 -> npcl(FacialExpression.OLD_DEFAULT, "Keeping the furnace going is valuable experience in itself!").also { stage++ }
            21 -> npcl(FacialExpression.OLD_DEFAULT, "Keeping the stove alight will train your Firemaking, working the pump will train your Strength, pedalling the conveyor belt will train your Agility and repairing the furnace will train Crafting.").also { stage++ }
            22 -> npcl(FacialExpression.OLD_DEFAULT, "Plus, everyone working the furnace will get a bit of experience in Smithing whenever anyone smelts ore in it.").also { stage++ }
            23 -> showTopics(
                        Topic(FacialExpression.FRIENDLY, "So I don't actually get paid then?", 25),
                        Topic(FacialExpression.FRIENDLY, "Okay I'll get to work.", 101),
                        Topic(FacialExpression.FRIENDLY, "Maybe some other time.", 102),
                )

            25 -> npcl(FacialExpression.OLD_ANGRY1, "I offer you the privilege of working on the greatest example of dwarven engineering and you want to be PAID?").also { stage++ }
            26 -> npcl(FacialExpression.OLD_ANGRY2, "If you co-operate with a group of people you can all get your smelting done pretty quickly. Or perhaps some humans who need to do some smelting will pay you, since you humans are all so obsessed with money.").also { stage++ }
            27 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Okay I'll get to work.", 101),
                    Topic(FacialExpression.FRIENDLY, "Maybe some other time.", 102),
                    Topic(FacialExpression.FRIENDLY, "No wonder you have a labour shortage if you don't pay your workers!", 30, true),
            )

            30 -> playerl(FacialExpression.FRIENDLY, "No wonder you have a labour shortage if you don't pay your workers!").also { stage++ }
            31 -> npcl(FacialExpression.OLD_DEFAULT, "What would be the point of making money if I gave it all away to menial workers?").also { stage++ }
            32 -> npcl(FacialExpression.OLD_DEFAULT, "I mean... Money isn't important! What about the greater glory of dwarven engineering? What about the satisfaction of helping others? What about the XP?").also { stage++ }
            33 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Okay I'll get to work.", 101),
                    Topic(FacialExpression.FRIENDLY, "Maybe some other time.", 102),
            )

            40 -> npcl(FacialExpression.OLD_DEFAULT, "You can, although you'll need to find some people to work it for you as well. But the furnace is very delicate so I charge a fee for anyone who doesn't have level 60 smithing.").also { stage++ }
            41 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "I have level 60!", 45),
                    Topic(FacialExpression.FRIENDLY, "I don't have level 60.", 51),
            )

            45 -> npcl(FacialExpression.OLD_DEFAULT, "A human has level 60 smithing? How extraordinary!").also { stage++ }
            46 -> npcl(FacialExpression.OLD_DEFAULT, "Feel free to use the furnace. Remember, you only need half as much coal as with a regular furnace.").also {
                stage = END_DIALOGUE
            }
            // This is inauthentic. Authentic dialogue below.
            51 -> npcl(FacialExpression.OLD_DEFAULT, "Hmm, well, I can't let you use the furnace. You need to have level 60 smithing.").also {
                stage = END_DIALOGUE
            }

            // These are unreachable until Blast Furnace implements <60 Smithing timeshare.
            52 -> npcl(FacialExpression.OLD_DEFAULT, "Hmm, well, I'll let you use the furnace if you want, but you must pay a fee of 2,500 coins.").also { stage++ }
            53 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Okay.", 55, true),
                    Topic(FacialExpression.FRIENDLY, "Maybe some other time.", 102),
                    Topic(FacialExpression.FRIENDLY, "[Charm] How about I give you 1250 coins and I work extra hard?", 70, true), // Ring of Charos(a)
            )
            55 -> playerl(FacialExpression.FRIENDLY, "Okay, here you are.").also { stage++ }
            56 -> npcl(FacialExpression.OLD_DEFAULT, "Okay, you can use the furnace for ten minutes. Remember, you only need half as much coal as with a regular furnace.").also {
                stage = END_DIALOGUE
            }

            70 -> playerl(FacialExpression.FRIENDLY, "How about I give you 1250 coins and I work extra hard?").also { stage++ }
            71 -> npcl(FacialExpression.OLD_DEFAULT, "You'll have to work hard enough for two humans if I agree...").also { stage++ }
            72 -> playerl(FacialExpression.FRIENDLY, "Oh, three humans, easily!").also { stage++ }
            73 -> npcl(FacialExpression.OLD_DEFAULT, "Well, okay then - 1250GP it is.").also { stage++ }

            101 -> npcl(FacialExpression.OLD_DEFAULT, "Splendid!").also {
                stage = END_DIALOGUE
            }
            102 -> npcl(FacialExpression.OLD_ANGRY1, "Well if you're not here to work, go hang around somewhere else!").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BlastFurnaceForemanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLAST_FURNACE_FOREMAN_2553)
    }
}