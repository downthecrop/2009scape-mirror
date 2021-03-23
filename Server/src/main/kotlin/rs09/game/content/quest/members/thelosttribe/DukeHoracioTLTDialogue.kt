package rs09.game.content.quest.members.thelosttribe

import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE

class DukeHoracioTLTDialogue(val questStage: Int) : DialogueFile() {
    private val Sigmund = NPC(2082)


    override fun handle(componentID: Int, buttonID: Int) {

        if(questStage == 20){
            when(stage){
                START_DIALOGUE -> player("I know what happened in the cellar.").also { stage++ }
                1 -> npc("Oh?").also { stage++ }
                2 -> player("The cook says he saw something in the cellar.","Like a goblin with big eyes.").also { stage++ }
                3 -> npc(
                    "Yes, he mentioned that to me. But I think he was",
                    "imagining things. Goblins live in natural caves but",
                    "everyone knows they don't have the wit to make their",
                    "own tunnels."
                ).also { stage++ }

                4 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.ANGRY,
                    "Yes your grace, but if there is any possibility that this",
                    "is a goblin incursion then we should take that possibility",
                    "very seriously!"
                ).also { stage++ }

                5 -> player("I think we should at least investigate.").also { stage++ }
                6 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.WORRIED,
                    "Your grace, I think you should listen to " + (if (player!!.isMale) "him" else "her") + "."
                ).also { stage++ }

                7 -> {
                    npc(
                        "Hmm, very well. I give you permission to investigate",
                        "this mystery. If there is a blocked tunnel then perhaps",
                        "you should try to un-block it."
                    )
                    player!!.questRepository.getQuest("Lost Tribe").setStage(player, 30)
                    stage = END_DIALOGUE
                }
            }
        }

        else if(questStage == 30 && player!!.inventory.containsItem(Item(Items.BROOCH_5008))){
            when(stage){
                START_DIALOGUE -> player("On the ground I found this brooch.").also { stage++ }
                1 -> npc(
                    "I've never seen anything like that before. It doesn't",
                    "come from Lumbridge. What do you think, Sigmund?"
                ).also { stage++ }

                2 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.WORRIED,
                    "It is unknown to me, your grace. But the fact it is",
                    "there is enough to prove the Cook's story. It must have",
                    "been dropped by a goblin as it fled."
                ).also { stage++ }

                3 -> npc("I've never heard of a goblin wearing something so well-", "crafted.").also { stage++ }
                4 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.ANGRY,
                    "Then it must have been stolen!"
                ).also { stage++ }

                5 -> npc("But it wasn't stolen from us. Where could it be from?").also { stage++ }
                6 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.ANGRY,
                    "That doesn't matter! You said yourself that goblins",
                    "couldn't have made that, so they must have stolen it",
                    "from somewhere."
                ).also { stage++ }

                7 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.ANGRY,
                    "Horrible, thieving goblins have broken into our cellar!",
                    "We must retaliate immediately!"
                ).also { stage++ }

                8 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.ANGRY,
                    "First we should wipe out the goblins east of the river,",
                    "then we can march on the goblin village to the north-",
                    "west..."
                ).also { stage++ }

                9 -> npc("I will not commit troops until I have proof that goblins", "are behind this.").also { stage++ }
                10 -> {
                    npc(
                        player!!.name + ", please find out what you can about this",
                        "brooch. The librarian in Varrock might be able to help",
                        "identify the symbol."
                    )
                    player!!.questRepository.getQuest("Lost Tribe").setStage(player, 40)
                    stage = END_DIALOGUE
                }
            }
        }

        else if(questStage == 44){
            when(stage){
                START_DIALOGUE -> player(
                    "I spoke to the goblin generals in the goblin village. They",
                    "told me about an ancient goblin tribe that went to live",
                    "underground."
                ).also { stage++ }

                1 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.ANGRY,
                    "What more proof do we need? Nasty, smelly goblins",
                    "have been living under our feet all this time! We must",
                    "crush them at once!"
                ).also { stage++ }

                2 -> npc(
                    "Hmm, perhaps you are right. I will send word to the",
                    "army to prepare for an underground assault."
                ).also { stage++ }

                3 -> {
                    npc(
                        player!!.name.capitalize() + ", I would still like you to find out more",
                        "about this tribe. It cannot hurt to know one's enemy."
                    )
                    player!!.questRepository.getQuest("Lost Tribe").setStage(player, 45)
                    stage = END_DIALOGUE
                }
            }
        }

        else if(questStage == 46){
            when(stage){
                START_DIALOGUE -> player(
                    "I've made contact with the cave goblins. They say they",
                    "were following a seam and broke into the cellar by",
                    "mistake."
                ).also { stage++ }

                1 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.ANGRY,
                    "And I suppose you believe them, goblin lover?"
                ).also { stage++ }

                2 -> player("Well, they seemed friendlier than most goblins, and", "nothing was taken from the cellar.").also { stage++ }
                3 -> npc(
                    "Actually, something was taken. Sigmund has informed",
                    "me that some of the castle silverware is missing from",
                    "the cellar."
                ).also { stage++ }

                4 -> {
                    npc("Unless it is returned, I am afraid I will have no option", "but war.")
                    player!!.questRepository.getQuest("Lost Tribe").setStage(player, 47)
                    stage = END_DIALOGUE
                }
            }
        }

        else if(questStage == 49 && player!!.inventory.contains(Items.SILVERWARE_5011,1)){
            when(stage){
                START_DIALOGUE -> player("I found the missing silverware in the HAM cave!").also { stage++ }
                1 -> npc("Sigmund! Is this your doing?").also { stage++ }
                2 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.WORRIED,
                    "Of...of course not! The goblins must have, um, dropped",
                    "the silverware as they ran away."
                ).also { stage++ }

                3 -> npc(
                    "Don't lie to me! I knew you were a HAM member but",
                    "I didn't think you would stoop to this. You are",
                    "dismissed from my service."
                ).also { stage++ }

                4 -> sendNormalDialogue(
                    Sigmund,
                    FacialExpression.THINKING,
                    "But don't you see it was for the best? For goblins to be",
                    "living under our feet like this... ugh. It doesn't matter",
                    "how civilised they are: all sub-human species must be",
                    "wiped out!"
                ).also { stage++ }

                5 -> npc("That's enough! Get out of my castle now!").also { stage++ }
                6 -> npc(
                    "I see I was ill-advised. Unless there is an act of",
                    "aggression by the cave goblins there is no need for war."
                ).also { stage++ }

                7 -> interpreter!!.sendItemMessage(Items.PEACE_TREATY_5012, "The Duke writes a document and signs it.").also { stage++ }
                8 -> {
                    npc(
                        "This peace treaty specifies the border between",
                        "Lumbridge and the Cave Goblin realm. Please take it to",
                        "the cave goblins and tell them I would like to meet with",
                        "their leader to sign it."
                    )
                    player!!.inventory.add(Item(Items.PEACE_TREATY_5012))
                    player!!.questRepository.getQuest("Lost Tribe").setStage(player, 50)
                    player!!.varpManager.get(465).setVarbit(0, 9).send(player!!)
                    stage = END_DIALOGUE
                }
            }
        }

        else {
            abandonFile()
        }
    }
}