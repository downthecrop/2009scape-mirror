package content.region.morytania.quest.creatureoffenkenstrain

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class LordRologarthDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return LordRologarthDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, LordRologarthDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        // THE IDs ARE WRONG. 1671 and 1672 are varp controlled Lord Rologarth
        return intArrayOf(NPCs.DR_FENKENSTRAIN_1671, NPCs.DR_FENKENSTRAIN_1672, NPCs.FENKENSTRAINS_MONSTER_1673)
    }
}

class LordRologarthDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b.onQuestStages(CreatureOfFenkenstrain.questName, 6)
                .playerl("I am commanded to destroy you, creature!")
                .npcl("Oh that's *hic* not very *hic* nice ...")
                .playerl("Are you feeling ok?")
                .npcl("Abso *hic* lutely. Never *buuurrp* better.")
                .playerl("You don't look very dangerous.")
                .npcl("How *hic* do I look?")
                .playerl("You really don't know, do you? Have a look for yourself.")
                .line("The creature stumbles over towards the mirror, focuses upon his", "reflection and...")
                .npcl("AAAAARRGGGGHHHH!")
                .line("The creature becomes instantly sober, horror all too evident in his", "undead eyes.")
                .playerl("I'm sorry. I suppose I'm partly to blame for this.")
                .npcl("No - it was him I wager - Fenkenstrain - wasn't it? He's brought me back to life!")
                .playerl("Who are - were - you?")
                .npcl("I was Rologarth, Lord of the North Coast - this castle was once mine. Fenkenstrain was the castle doctor.")
                .playerl("So the castle wasn't really abandoned when he found it?")
                .npcl("Is that what he told you? No, no, this castle was once full of people and life. Fenkenstrain advised me to sell them to the vampyres, which - I am sad to say - I did.")
                .playerl("I found your brain in a jar in Canifis, so he must have sold you too.")
                .npcl("Of that I will not speak. There lie memories that should rest with the dead, the living unable to bear them.")
                .playerl("That's it - I'm leaving this dreadful place, whether I get paid or not. Is there anything I can do for you before I leave?")
                .npcl("Only one - please stop Fenkenstrain from carrying on his experiments, once and for all, so that no other poor soul has to endure suffering such as that of my people and I.")
                .endWith() { df, player ->
                    if(getQuestStage(player, CreatureOfFenkenstrain.questName) == 6) {
                        setQuestStage(player, CreatureOfFenkenstrain.questName, 7)
                    }
                }
        b.onQuestStages(CreatureOfFenkenstrain.questName, 7)
                .playerl(FacialExpression.THINKING, "Do you know how I can stop Fenkenstrain's experiments?")
                .npcl("Take the Ring of Charos from him.")
                .playerl(FacialExpression.THINKING, "What is this ring?")
                .npcl("It was my birthright, passed down to me through the ages, its origin forgotten.")
                .npcl("The Ring of Charos has many powers, but Fenkenstrain has bent them to his down evil purposes. Without the power of the ring, he will not be able to raise the dead from their sleep.")
                .npcl("It has one other, extremely important use - it confuses the werewolves' senses, making them believe that they smell one of their own kind. Without the ring, Fenkenstrain will be at their mercy.")
                .end()
        b.onQuestStages(CreatureOfFenkenstrain.questName, 8, 100)
                .npcl("How goes it, friend?")
                .playerl("I stole the Ring of Charos from Fenkenstrain.")
                .npcl("I saw him climb up into the Tower to hide. It doesn't matter - soon the werewolves will come for him, and his experiments will be forever ceased.")
                .playerl(FacialExpression.THINKING, "Do you want the ring back? It is yours after all.")
                .npcl("No, you keep it, my friend. Werewolves hunger for the scent of live flesh - I have no need for the ring. I have my castle back, if not my soul.")
                .end()
    }
}