package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.mahjarratArc
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.attrCarnillean
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCultListeners.Companion.attrHazeel
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.*

@Initializable
class ClivetDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(componentID: Int, buttonID: Int, ): Boolean {

        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)

        when {
            // stage 0 - unstarted
            (questStage == 0) -> when (stage) {
                0 -> npcl(FacialExpression.ANNOYED, "What do you want traveller?").also { stage++ }
                1 -> playerl(FacialExpression.ANNOYED, "Just passing by.").also { stage++ }
                2 -> npcl(FacialExpression.ANNOYED, "You have no business here.").also { stage++ }
                3 -> npcl(FacialExpression.ANGRY, "Leave...now").also { stage = END_DIALOGUE }
            }

            // stage 1 - after talking to ceril
            (questStage == 1) -> when (stage) {
                0 -> playerl(FacialExpression.ANNOYED, "Do you know the Carnilleans?").also { stage++ }
                1 -> npcl(FacialExpression.ANNOYED, "You mind your business, I'll mind mine.").also { stage++ }
                2 -> playerl(FacialExpression.ANNOYED, "Look I KNOW you're hiding something. I've heard there's a cult hideout down here.").also { stage++ }
                3 -> npcl(FacialExpression.ANGRY, "If you want to stay healthy you'll leave now.").also { stage++ }
                4 -> playerl(FacialExpression.ANNOYED, "I have my orders.").also { stage++ }
                5 -> npcl(FacialExpression.HALF_ASKING, "So... that two faced cold hearted snob has made you fall for his propaganda eh?").also { stage++ }
                6 -> playerl(FacialExpression.FRIENDLY, "Sir Ceril Carnillean is a man of honour!").also { stage++ }
                7 -> npcl(FacialExpression.ANNOYED, "Is he now? Is he REALLY? There is a lot more to the Carnilleans than meets the eye you fool...").also { stage++ }
                8 -> npcl(FacialExpression.ANNOYED, "And none of it is honourable.").also { stage++ }
                9 -> options("What do you mean?", "I've heard enough of your rubbish.").also { stage++ }
                10 -> when (buttonID) {
                    1 -> playerl(FacialExpression.HALF_ASKING, "What do you mean?").also { stage++ }
                    2 -> playerl(FacialExpression.ANNOYED, "I've heard enough of your rubbish.").also { stage = 22 }
                }
                11 -> npcl(FacialExpression.ANNOYED, "The Carnillean home does not belong to them. The builder of the house was Lord Hazeel, one of the Mahjarrat followers of Zamorak. Many years ago there was a civil war").also { stage++ }
                12 -> npcl(FacialExpression.ANNOYED, "in this land, and the hateful Saradominists declared war upon all Zamorakians who lived here. Lord Hazeel nobly would not repent his beliefs, and the Carnilleans harassed").also { stage++ }
                13 -> npcl(FacialExpression.ANNOYED, "Hazeel and his followers for many decades. One fateful night, under cover of darkness, they stormed his home in an angry mob torturing and butchering all loyal").also { stage++ }
                14 -> npcl(FacialExpression.ANNOYED, "Zamorakians they encountered inside. The following morning, the Carnillean forefathers moved into the empty household and claimed it as their own.").also { stage++ }
                15 -> npcl(FacialExpression.ANNOYED, "They have grown fat on the hard work of Lord Hazeel ever since. Unluckily for them Lord Hazeel as a Mahjarrat had access to powers and enchantments they knew nothing").also { stage++ }
                16 -> npcl(FacialExpression.ANNOYED, "about, and made preparations for his return when first they began to storm his home. Soon the day will come when he will return to wreak his vengeance upon the thieves!").also { stage++ }
                17 -> playerl(FacialExpression.ANNOYED, "The politics and histories of Ardougne do not concern me. I have been given a job and intend to see it through to the end.").also { stage++ }
                18 -> npcl(FacialExpression.ANNOYED, "Well then friend, perhaps I can offer you a different job then? Sooner as later Our master WILL return to this land; those faithful to him will be well rewarded.").also { stage++ }
                19 -> npcl(FacialExpression.ANNOYED, "Join our cult and assist us in his restoration, and you will be well rewarded; try and prevent his return and suffer the wrath of Zamorak and the Mahjarrat.").also { stage++ }
                20 -> options("You're crazy, I'd never help you.", "So what would I have to do?").also { stage++ }
                21 -> when (buttonID) {
                    1 -> playerl(FacialExpression.DISGUSTED_HEAD_SHAKE, "You're crazy, I'd never help you.").also { stage++ }
                    2 -> playerl(FacialExpression.HALF_ASKING, "So what would I have to do?").also { stage = 40 }
                }

                // SET CARNILLEAN ARC
                22 -> npcl(FacialExpression.ANNOYED, "Then you are a fool. Go back to your small minded mundane little life; you will never know the glories you could have tasted as one of us!").also { stage++ }
                23 -> {
                    end()
                    setAttribute(player, attrCarnillean, true)
                    setQuestStage(player, Quests.HAZEEL_CULT, 2)
                    runTask(player, 1) {
                        npc.isInvisible = true
                        sendDialogue(player, "The man jumps onto the raft and pushes off down into the sewer system.")
                        sendMessage(player, "Clivet: You'll never find us...")
                        stage = END_DIALOGUE
                    }
                }

                // SET MAHJARRAT ARC
                40 -> npcl(FacialExpression.NEUTRAL, "You must prove your loyalty to our cause. Killing one of the Carnillean household should be sufficient proof of your dedication to resurrecting our master Lord Hazeel.").also { stage++ }
                41 -> npcl(FacialExpression.NEUTRAL, "So what say you adventurer? Join our side against the Carnillean thieves?").also { stage++ }
                42 -> options("No. I won't do it.", "Ok, count me in.").also { stage++ }
                43 -> when (buttonID) {
                    1 -> playerl(FacialExpression.DISGUSTED_HEAD_SHAKE, "No. I won't do it.").also { stage = 22 }
                    2 -> playerl(FacialExpression.NEUTRAL, "Ok, count me in.").also { stage++ }
                }
                44 -> npcl(FacialExpression.NEUTRAL, "Excellent. It takes a rare character to see through the cursed Carnillean lies. I can see you are a person of exactly the right character to join the followers of Hazeel.").also { stage++ }
                45 -> npcl(FacialExpression.NEUTRAL, "Here, take this poison. Pour it into their food, and once the deed is done, return here and speak to me once more.").also { stage++ }
                46 -> {
                    end()
                    setAttribute(player, attrHazeel, true)
                    setQuestStage(player, Quests.HAZEEL_CULT, 2)
                    addItemOrDrop(player, Items.POISON_273)
                }
            }

            // stage 2 - talk to clivet (set attr for carnillean)
            // stage 2 - talk to clivet (set attr for mahjarrat)
            (questStage == 2) -> when (stage) {
                0 -> {
                    if (mahjarratArc(player) && !inInventory(player, Items.POISON_273)) {
                        playerl(FacialExpression.FRIENDLY, "I need some more poison.").also { stage++ }
                    } else if (mahjarratArc(player) && inInventory(player, Items.POISON_273)){
                        npcl(FacialExpression.FRIENDLY, "You have a mission for us, adventurer. Go to the Carnilliean household and poison Ceril Carnillean's meal to prove your loyalty.").also { stage = END_DIALOGUE }
                    } else {
                        playerl(FacialExpression.HALF_ASKING, "WHERE is the cult hideout?").also { stage = 3 }
                    }
                }
                1 -> npcl(FacialExpression.ANNOYED, "Fool! Be more careful with it this time.").also { stage++ }
                2 -> {
                    end()
                    addItemOrDrop(player, Items.POISON_273)
                }
                3 -> npcl(FacialExpression.ANNOYED, "You're more of a fool than you look if you think you will ever find it. When Lord Hazeel is revived you will be the first to grovel for his mercy!").also { stage = END_DIALOGUE }
            }

            // stage 3 - poison poured in food (mahjarrat-only stage)
            (questStage == 3 && mahjarratArc(player)) -> when (stage) {
                0 -> {
                    if (!inEquipmentOrInventory(player, Items.HAZEELS_MARK_2406)) {
                        playerl(FacialExpression.FRIENDLY, "I poured the poison into the Carnillean's meal as requested. It didn't quite go to plan however.").also { stage++ }
                    } else {
                        playerl(FacialExpression.HALF_ASKING, "What was I supposed to do again?").also { stage = 6 }
                    }
                }
                1 -> npcl(FacialExpression.NEUTRAL, "Yes, we heard all about it from one of our sources. Hazeel has eyes everywhere! You have now proved your loyalty and will be rewarded when Hazeel is revived!").also { stage++ }
                2 -> playerl(FacialExpression.HALF_ASKING, "Ok. So what's next?").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "Here. Wear this amulet; it is called The Sign of Hazeel. Not only will it identify you as one of our brotherhood to other followers but it is also the key to finding our hideout.").also { addItemOrDrop(player, Items.HAZEELS_MARK_2406) }.also { stage++ }
                4 -> playerl(FacialExpression.NEUTRAL, "How does this amulet help do that then?").also { stage++ }
                5 -> npcl(FacialExpression.NEUTRAL, "Hazeel in his wisdom when he built this lair did so by making it inaccessible unless the flow of the sewers is controlled exactly by the sewer valves above ground.").also { stage++ }
                6 -> npcl(FacialExpression.NEUTRAL, "Each sewer valve must be correctly set before the water level is such that you may enter. The secret of the correct settings is contained in these amulets which honour him.").also { stage++ }
                7 -> npcl(FacialExpression.NEUTRAL, "Starting from left to right, follow the design of the amulet to locate each sewer valve, and then turn each so as to follow the amulet design.").also { stage++ }
                8 -> npcl(FacialExpression.NEUTRAL, "When you solve the sequence you may enter our hideout where our leader, Alomone, awaits you.").also { stage = END_DIALOGUE }
            }

            // stage 4 - alomone either fought or he tells you he needs scroll
            // stage 5 - either returning the armour, or finding the scroll
            // stage 100 - quest complete - either presenting proof that jones is a bad guy (duh) or resurrecting hazeel
            (questStage in  4..100) -> when (stage) {
                0 -> {
                    if (mahjarratArc(player)) {
                        playerl(FacialExpression.FRIENDLY, "Hello.").also { stage++ }
                    } else {
                        playerl(FacialExpression.FRIENDLY, "Hello.").also { stage = 4 }
                    }
                }
                1 -> npcl(FacialExpression.FRIENDLY, "It is good to see you once more, adventurer. Glory to Hazeel!").also { stage++ }
                2 -> {
                    // if player doesn't have hazeel's mark they can get a new copy.
                    if (hasAnItem(player, Items.HAZEELS_MARK_2406).container == null) {
                        playerl(FacialExpression.FRIENDLY, "I've lost my Mark of Hazeel.").also { stage++ }
                    } else {
                        playerl(FacialExpression.FRIENDLY, "Glory to Hazeel!").also { stage = END_DIALOGUE }
                    }
                }
                3 -> npcl(FacialExpression.ANNOYED, "Here, take this.").also { addItemOrDrop(player, Items.HAZEELS_MARK_2406) }.also { stage = END_DIALOGUE }

                4 -> npcl(FacialExpression.ANNOYED, "You may have won this battle meddler, but the war rages on. Go bother some goblins or something, for when Hazeel returns your destruction is assured.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CLIVET_893)
}
