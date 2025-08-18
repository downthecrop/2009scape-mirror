package content.region.kandarin.quest.murdermystery

import content.data.Quests
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeNoiseClue
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs09.consts.NPCs

@Initializable
class GossipDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GossipDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return GossipDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GOSSIP_813)
    }
}

class GossipDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> npcl("There's some kind of commotion up at the Sinclair place I hear. Not surprising all things considered.").also { stage = END_DIALOGUE}
            1 -> when (stage) {
                0 -> playerl("I'm investigating the murder up at the Sinclair place.").also { stage++ }
                1 -> npcl("Murder is it? Well, I'm not really surprised...").also { stage++ }
                2 -> options("What can you tell me about the Sinclairs?", "Who do you think was responsible?", "I think the butler did it.", "I am so confused about who did it.").also { stage++ }
                3 -> when(buttonID) {
                    1 -> playerl("What can you tell me about the Sinclairs?").also { stage = 4 }
                    2 -> playerl("Who do you think was responsible?").also { stage = 40 }
                    3 -> playerl("I think the butler did it.").also { stage = 42 }
                    4 -> playerl("I am so confused about who did it.").also { stage = 43 }
                }
                4 -> npcl("Well, what do you want to know?").also { stage++ }
                5 -> options("Tell me about Lord Sinclair.", "Why do the Sinclairs live so far from town?", "What can you tell me about his sons?", "What can you tell me about his daughters?").also { stage++ }
                6 -> when(buttonID) {
                    1 -> playerl("Tell me about Lord Sinclair.").also { stage = 7 }
                    2 -> playerl("Why do the Sinclairs live so far from town?").also { stage = 10 }
                    3 -> playerl("What can you tell me about his sons?").also { stage = 12 }
                    4 -> playerl("What can you tell me about his daughters?").also { stage = 15 }
                }
                7 -> npcl("Old Lord Sinclair was a great man with a lot of respect around these parts. More than his worthless children have anyway.").also { stage++ }
                8 -> playerl("His children? They have something to gain by his death?").also { stage++ }
                9 -> npcl("Yes. You could say that. Not that I am one to gossip.").also { stage = END_DIALOGUE }

                10 -> npcl("Well, they used to live in the big castle, but old Lord Sinclair gave it up so that those strange knights could live there instead. So the king built him a new house to the North.").also { stage++ }
                11 -> npcl("It's more cramped than his old place, but he seemed to like it. His children were furious at him for doing it though!").also { stage = END_DIALOGUE }

                12 -> npcl("His sons eh? They all have their own skeletons in their cupboards. You'll have to be more specific. Who are you interested in exactly?").also { stage++ }
                13 -> options("Tell me about Bob.", "Tell me about David.", "Tell me about Frank.").also { stage++ }
                14 -> when(buttonID) {
                    1 -> playerl("Tell me about Bob.").also { stage = 18 }
                    2 -> playerl("Tell me about David.").also { stage = 23 }
                    3 -> playerl("Tell me about Frank.").also { stage = 27 }
                }

                15 -> npcl("His daughters eh? They're all nasty pieces of work, which of them specifically did you want to know about?").also { stage++ }
                16 -> options("Tell me about Anna.", "Tell me about Carol.", "Tell me about Elizabeth.").also { stage++ }
                17 -> when(buttonID) {
                    1 -> playerl("Tell me about Anna.").also { stage = 30 }
                    2 -> playerl("Tell me about Carol.").also { stage = 33 }
                    3 -> playerl("Tell me about Elizabeth.").also { stage = 36 }
                }

                18 -> npcl("Bob is an odd character indeed... I'm not one to gossip, but I heard Bob is addicted to Tea. He can't make").also { stage++ }
                19 -> npcl("it through the day without having at least 20 cups!").also { stage++ }
                20 -> npcl("You might not think that's such a big thing, but he has spent thousands of gold to feed his habit!").also { stage++ }
                21 -> npcl("At one point he stole a lot of silverware from the kitchen and pawned it just so he could afford to buy his daily tea allowance.").also { stage++ }
                22 -> npcl("If his father ever found out, he would be in so much trouble... he might even get disowned!").also { stage = END_DIALOGUE }

                23 -> npcl("David... oh David... not many people know this, but David really has an anger problem. He's always screaming and shouting").also { stage++ }
                24 -> npcl("at the household servants when he's angry, and they live in a state of fear, always walking on eggshells around him, but none of them have the courage").also { stage++ }
                25 -> npcl("to talk to his father about his behaviour. If they did, Lord Sinclair would almost certainly").also { stage++ }
                26 -> npcl("kick him out of the house, as some of the servants have been there longer than he has, and he definitely has no right to treat them like he does... but I'm not one to gossip about people.").also { stage = END_DIALOGUE }

                27 -> npcl("I'm not one to talk ill of people behind their back, but Frank is a real piece of work. He is an absolutely terrible gambler...he can't pass 2 dogs in the street without putting a bet on which one will bark first!").also { stage++ }
                28 -> npcl("He has already squandered all of his allowance, and I heard he had stolen a number of paintings of his fathers to sell to try and cover his debts, but he still owes a lot of people a lot of").also { stage++ }
                29 -> npcl("money. If his father ever found out, he would stop his income, and then he would be in serious trouble!").also { stage = END_DIALOGUE }

                30 -> npcl("Anna... ah yes... Anna has 2 great loves:").also { stage++ }
                31 -> npcl("Sewing and gardening. But one thing she has kept secret is that she once had an affair with Stanford the gardener, and tried to get him fired when they broke up,").also { stage++ }
                32 -> npcl("by killing all of the flowers in the garden. If her father ever found out she had done that he would be so furious he would probably disown her.").also { stage = END_DIALOGUE }

                33 -> npcl("Oh Carol... she is such a fool. You didn't hear this from me, but I heard a while ago she was conned out of a lot of money by a travelling salesman who sold her a box full").also { stage++ }
                34 -> npcl("of beans by telling her they were magic. But they weren't. She sold some rare books from the library to cover her debts, but").also { stage++ }
                35 -> npcl("her father would be incredibly annoyed if he ever found out - he might even throw her out of the house!").also { stage = END_DIALOGUE }

                36 -> npcl("Elizabeth? Elizabeth has a strange problem... She cannot help herself, but is always stealing small objects - it's pretty sad that she is rich enough to afford to buy things, but would rather steal them instead.").also { stage++ }
                37 -> npcl("Now, I don't want to spread stories, but I heard she even stole a silver needle from her father that had great sentimental value for him.").also { stage++ }
                38 -> npcl("He was devastated when it was lost, and cried for a week thinking he had lost it!").also { stage++ }
                39 -> npcl("If he ever found out that it was her who had stolen it he would go absolutely mental, maybe even disowning her!").also { stage = END_DIALOGUE }

                40 -> npcl("Well, I guess it could have been an intruder, but with that big guard dog of theirs I seriously doubt it. I suspect it was someone closer to home...").also { stage ++ }
                41 -> npcl("Especially as I heard that the poison salesman in the Seers' village made a big sale to one of the family the other day.")
                    .also { setAttribute(player!!, attributeNoiseClue, true)}
                    .also { stage = END_DIALOGUE }

                42 -> npcl("And I think you've been reading too many cheap detective novels. Hobbes is kind of uptight, but his loyalty to old Lord Sinclair is beyond question.").also { stage = END_DIALOGUE }

                43 -> playerl("Think you could give me any hints?")
                    .also { when (RandomFunction.random(4)) { //RS3 wiki seems to indicate this is fully random, not based off of progression
                        0 -> stage = 44
                        1 -> stage = 46
                        2 -> stage = 47
                        3 -> stage = 48
                        4 -> stage = 51
                        }
                    }
                44 -> npcl("Well, I don't know if it's related, but I heard from that Poison Salesman in town that he sold some poison to one of the Sinclair family").also { stage++ }
                45 -> npcl("the other day. I don't think he has any stock left now though...").also { stage = END_DIALOGUE }

                46 -> npcl("Well, I don't know how much help this is, but I heard their guard dog will bark loudly at anyone it doesn't recognise. Maybe you should find out if anyone heard anything suspicious?")
                    .also { stage = END_DIALOGUE }

                47 -> npcl("Well, this might be of some help to you. My father was in the guards when he was younger and he always said that there isn't a crime that can't be solved through careful examination of the crime scene and all surrounding areas.").also { stage = END_DIALOGUE }

                48 -> npcl("I don't know how much help this is to you, but my dad was in the guard once and he told me the marks on your hands are totally unique. He calls them 'finger prints'.").also { stage++ }
                49 -> npcl("He said you can find them easily on any shiny metallic surface, by using a fine powder to mark out where the marks are and then using some sticky paper to lift the print from the object.").also { stage++ }
                50 -> npcl("I bet if you could find a way to get everyone's 'finger prints' you could solve the crime pretty easily!").also { stage = END_DIALOGUE }

                51 -> npcl("My father used to be in the guard, he always wrote himself notes on a piece of paper so he could keep track of information easily.").also { stage++ }
                52 -> npcl("Maybe you should try that? Don't forget to thank me if I help you solve the case!").also { stage = END_DIALOGUE }
            }
        }
    }
}