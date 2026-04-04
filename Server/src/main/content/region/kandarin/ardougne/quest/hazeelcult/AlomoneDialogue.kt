package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.mahjarratArc
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.HAZEEL_SCROLL
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.*

@Initializable
class AlomoneDialogue(player: Player? = null) : DialoguePlugin(player) {

    val hazeel = HazeelNPC(NPCs.HAZEEL_892, Location.create(2607, 9669, 0))

    override fun handle(componentID: Int, buttonID: Int, ): Boolean {

        val questStage = getQuestStage(player, Quests.HAZEEL_CULT)

        when {
            // you can't access Alomone before stage 2
            // stage 2 - talk to clivet (set attr for carnillean)
            // stage 2 - talk to clivet (set attr for mahjarrat)
            // stage 3 - poison poured in food (mahjarrat-only stage)
            (questStage in 2..3) -> when (stage) {
                0 -> {
                    if (mahjarratArc(player)) {
                        playerl(FacialExpression.FRIENDLY, "Hi there.").also { stage = 20 }
                    } else {
                        npcl(FacialExpression.ANGRY, "How did YOU get in here?").also { stage ++}
                    }
                }
                // carnillean arc
                1 -> playerl(FacialExpression.NEUTRAL, "I've come for the Carnillean family armour. Hand it over, or face the consequences.").also { stage++ }
                2 -> npcl(FacialExpression.ANNOYED, "I thought I made it clear to the butler you could not be allowed to interfere with our mission. The incompetent fool must be going soft.").also { stage++ }
                3 -> playerl(FacialExpression.NEUTRAL, "So, the butler's part of your sordid little cult, huh? Why is it ALWAYS the butler? I should have known...").also { stage++ }
                4 -> npcl(FacialExpression.ANNOYED, "Well, you won't live long enough to tell anyone! DIE!!!").also { stage++ }
                5 -> {
                    end()
                    setQuestStage(player, Quests.HAZEEL_CULT, 4)
                    npc!!.attack(player!!)
                }

                // mahjarrat arc
                20 -> npcl(FacialExpression.NEUTRAL, "Well well well... So we have a new recruit. Clivet told me of your desire to join us in our glorious task to resurrect the mighty Hazeel from his slumber.").also { stage++ }
                21 -> npcl(FacialExpression.NEUTRAL, "To accomplish this, we require the ancient words of summoning which will restore his shattered body, so that he can bring vengeance upon his enemies.").also { stage++ }
                22 -> npcl(FacialExpression.NEUTRAL, "Hazeel in his mighty cunning anticipated that he might be defeated and secured within his home somewhere a powerful magical scroll that could restore him.").also { stage++ }
                23 -> npcl(FacialExpression.NEUTRAL, "The words to this powerful enchantment are hidden within the Carnillean mansion, right under the nose of his unsuspecting foes. We already have an agent in place there").also { stage++}
                24 -> npcl(FacialExpression.NEUTRAL, "in their Butler Jones, a faithful follower of Hazeel. His loyalty to Hazeel is beyond questioning, yet he has been unsuccessful in locating the scroll containing the spell so far.").also { stage++ }
                25 -> npcl(FacialExpression.NEUTRAL, "Go back to the mansion, and assist us in finding this enchantment, so that together we can restore Hazeel to his true power and glory!").also { stage = END_DIALOGUE}.also { setQuestStage(player, Quests.HAZEEL_CULT, 4) }
            }

            // stage 4 - alomone either fought or he tells you he needs scroll
            // stage 5 - either returning the armour, or finding the scroll
            (questStage in 4..5 && mahjarratArc(player)) -> when (stage) {
                0 -> if(npc.id == NPCs.ALOMONE_891 && !getAttribute(player, "hazeelSpawned", false)) {
                    playerl(FacialExpression.NEUTRAL, "Hi.").also { stage++ }
                } else {
                    playerl(FacialExpression.NEUTRAL, "Hi.").also { stage = 12 }
                }
                1 -> npcl(FacialExpression.NEUTRAL, "Have you brought me the scroll of restoration to complete the ritual?").also { stage++ }
                2 -> if(inInventory(player, HAZEEL_SCROLL)){
                    playerl(FacialExpression.NEUTRAL, "Yep. Got it right here.").also { stage++ }
                } else {
                    playerl(FacialExpression.NEUTRAL, "No, not yet.").also { stage = END_DIALOGUE }
                }
                3 -> npcl(FacialExpression.NEUTRAL, "FINALLY! Quickly, give it to me and I will begin the ritual!").also { stage++ }
                4 -> sendDialogue(player, "You hand Alomone the scroll of Hazeel.").also { stage++ }
                5 -> npcl(FacialExpression.NEUTRAL, "Yes... YES! With the words contained within this scroll our Lord shall return to this realm and bring destruction to our enemies!").also { stage++ }
                6 -> npcl(FacialExpression.NEUTRAL, "Watch adventurer, and witness the glorious rebirth of Hazeel!").also { stage++ }
                7 -> npcl(FacialExpression.NEUTRAL, "Lord Hazeel... Lord Zamorak... I call upon the powers of evil... The forces of chaos... The strength of hatred...").also { stage++ }
                8 -> npcl(FacialExpression.NEUTRAL, "Sentente sillaberi junque dithmento! Ia! Ia! dextrimon encanto! termando... imcando... solly enty rando... sentente! Ia! Ia! Indenti zaggarati g'thxa!").also { stage++ }
                9 -> sendDialogue(player, "Alomone continues to recite the scroll. It is in a language you have never heard and do not understand at all.").also { stage++ }
                10 -> npcl(FacialExpression.NEUTRAL, "Dintenta! Sententa! Retenta! Q'exjta! Ia! Sottottott! Ia! Dysmenta junque fammatio svelken! Sottey! Sentey! SOLOMENT!").also { stage++ }
                11 -> {
                    sendDialogue(player, "As Alomone finishes reading the scroll you hear a shrill scream emanate from Hazeel's coffin.").also { stage++ }
                    hazeel.init()
                    setAttribute(player,"hazeelSpawned", true)
                    player.face(hazeel)
                    hazeel.isWalks = false
                }
                12 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "My loyal followers. I have pride in you all. Never did I expect to return to this land and I see I have much to attend to.").also { stage++ }
                13 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "Soon this world will cower once more at the name Hazeel, and my fury will blaze across mine enemies just as my loyal followers gain their rewards.").also { stage++ }
                14 -> sendDialogue(player, "Hazeel turns to look at you directly.").also{ hazeel.face(player) }.also { stage++ }
                15 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "Adventurer. I know that thy efforts were principal in my return to this plain of existence. I offer you my thanks directly.").also { stage++ }
                16 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "You may not be a follower of Zamorak directly but your cunning and treachery mark you as a friend to the Mahjarrat. Rest assured; I will call on your assistance again.").also { stage++ }
                17 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "Weak as I am now, my strength shall return, and my followers will be greatly rewarded when my powers peak once more. Join me in my cause as my servant, and all").also { stage++ }
                18 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "the riches of this plane shall be yours to share, in domination over all who oppose my kind. You would be wise to pledge yourself now while you still may.").also { stage++ }
                19 -> playerl(FacialExpression.ANGRY, "I serve nobody but myself.").also { stage++ }
                20 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "Your insolence shall be overlooked this time for I am in truth in your debt. Come close: I shall reward you with wealth such as you deserve.").also { stage++ }
                21 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "Although your true reward will be later, when Zamorak and I, together, lay waste to this miserable planet, and you are spared as an ally to us.").also { stage++ }
                22 -> sendDialogue(player, "Hazeel gives you some coins. They seem to be extremely ancient.").also { stage++ }
                23 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "And now I must leave you my loyal subjects for time is short, and I have many pressing matters to address before I make my return. I must leave now, and head Northwards").also { stage++ }
                24 -> sendNPCDialogue(player, NPCs.HAZEEL_892, "to join my fellow mahjarrat and regain my strength and powers. I shall return when I am ready to fight my enemies and much blood shall flow across these lands!").also { stage++ }
                25 -> sendDialogue(player, "The cultists let out a mighty cheer.").also { stage++ }
                26 -> {
                    end()
                    removeItem(player, HAZEEL_SCROLL)
                    val hazeel = player.findNpcInViewport(NPCs.HAZEEL_892)
                    hazeel?.clear()
                    removeAttribute(player,"hazeelSpawned")
                    finishQuest(player, Quests.HAZEEL_CULT)
                }
            }

            // stage 100 - quest complete - either presenting proof that jones is a bad guy (duh) or resurrecting hazeel
            // also valid for stages 4 and 5 in the carnillean arc.
            (questStage in 4..100) -> when (stage) {
                0 -> {
                    if (mahjarratArc(player)) {
                        playerl(FacialExpression.FRIENDLY, "Hi there.").also { stage++ }
                    } else {
                        npcl(FacialExpression.ANNOYED, "You have crossed my path too many times intruder. Leave or face my wrath.").also { stage = 2 }
                    }
                }
                1 -> npcl(FacialExpression.NEUTRAL, "Welcome, adventurer. Know that as a friend to Hazeel, you are always welcome here.").also { stage = END_DIALOGUE }

                2 -> playerl(FacialExpression.FRIENDLY, "Yeah, whatever.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    // function used to find any hazeels and remove them at the end of the quest.
    // this has the potential to remove other players' hazeels, but the player could still complete the quest by either continuing their
    // active dialogue or speaking to alomone, and just dealing with the fact that their hazeel is not on the map. relogging and restarting
    // the dialogue would get these players a new hazeel.
    private fun Player.findNpcInViewport(npcId: Int): NPC? {
        val viewport = viewport ?: return null
        for (plane in viewport.viewingPlanes) {
            for (npc in plane.npcs) {
                if (npc.id == npcId) {
                    return npc
                }
            }
        }
        return null
    }

    // Hazeel is in this array because in the event the player quits the dialogue halfway through in the final stage, it can be restarted by speaking to either alomone or hazeel.
    override fun getIds(): IntArray = intArrayOf(NPCs.ALOMONE_891, NPCs.HAZEEL_892)
}
