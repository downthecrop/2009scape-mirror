package content.region.misthalin.varrock.quest.allfiredup

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import content.minigame.allfiredup.AFUBeacon
import core.api.*

/**
 * Represents the "All Fired Up" quest.
 * @author Ceikry
 */
@Initializable
class AllFiredUp : Quest("All Fired Up", 157, 156, 1){
    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        player ?: return
        if (stage == 0) {
            line(player, "I can start this quest by speaking to !!King Roald?? in !!Varrock??", line++)
            line(player, "!!Palace??.", line++)
            line++
            line(player, "To start this quest, I require:", line++)
            line(player, "!!43 Firemaking??", line++, player.skills.getLevel(Skills.FIREMAKING) >= 43)
            line(player, "!!Completion of Priest in Peril??", line++, player.questRepository.isComplete("Priest in Peril"))
        } else {
            line(player, "I have agreed to help King Roald test the beacon network", line++, true)
            line(player, "that he hopes will serve as an early warning system,", line++, true)
            line(player, "should Misthalin and Asgarnia come under attack from", line++, true)
            line(player, "Morytania or the Wilderness.", line++, true)

            if (stage > 10) {
                line(player, "I've spoken with the head fire-tender, Blaze Sharpeye", line++, true)
                line(player, "who is stationed near the Temple of Paterdomus, at the", line++, true)
                line(player, "source of the River Salve.", line++, true)
            } else if (stage == 10) {
                line(player, "!!King Roald?? asked me to talk to the head fire-tender !!Blaze??", line++, false)
                line(player, "!!Sharpeye?? who is stationed near the !!Temple of??", line++, false)
                line(player, "!!Paterdomus??, at the source of the !!River Salve??.", line++, false)
            }

            if (stage > 20) {
                // Replaced with stage 30 text.
            } else if (stage == 20) {
                line(player, "!!Blaze?? has asked me to light the nearby !!beacon??.", line++, false)
                line(player, "To light the !!beacon??, I need to add !!twenty logs?? of the same", line++, false)
                line(player, "type and then light the fire with a !!tinderbox??.", line++, false)
            }

            if (stage > 30) {
                line(player, "I've lit the beacon near Blaze by loading it with twenty logs", line++, true)
                line(player, "and lighting them with a tinderbox.", line++, true)
            } else if (stage == 30) {
                line(player, "!!Blaze?? has asked me to light the nearby !!beacon??.", line++, false)
                line(player, "I've placed !!twenty logs?? on the !!beacon?? and lit the fire with", line++, false)
                line(player, "my !!tinderbox??. Now that it's blazing brightly, perhaps I", line++, false)
                line(player, "should speak with !!Blaze??.", line++, false)
            }

            if (stage > 40) {
                // Replaced with stage 50 text.
            } else if (stage == 40) {
                line(player, "!!Blaze?? has now asked me to light the !!beacon?? tended by", line++, false)
                line(player, "!!Squire Fyre??, which is located just west of the !!Rag and??", line++, false)
                line(player, "!!Bone Man??'s hovel.", line++, false)
                line(player, "I need permission from !!Squire Fyre?? to light the !!beacon??", line++, false)
            }

            if (stage > 50) {
                // Replaced with stage 60 text.
            } else if (stage == 50) {
                line(player, "!!Blaze?? has now asked me to light the !!beacon?? tended by", line++, false)
                line(player, "!!Squire Fyre??, which is located just west of the !!Rag and??", line++, false)
                line(player, "!!Bone Man??'s hovel.", line++, false)
                line(player, "To light the !!beacon??, I need to add !!twenty logs?? of the same", line++, false)
                line(player, "type and then light the fire with a !!tinderbox??.", line++, false)
            }

            if (stage > 60) {
                line(player, "Blaze has now asked me to light the beacon tended by", line++, true)
                line(player, "Squire Fyre, near the Rag and Bone Man's hovel. I've", line++, true)
                line(player, "loaded it with logs and set it alight; it's now blazing", line++, true)
                line(player, "brightly.", line++, true)
            } else if (stage == 60) {
                line(player, "!!Blaze?? has now asked me to light the !!beacon?? tended by", line++, false)
                line(player, "!!Squire Fyre??, which is located just west of the !!Rag and??", line++, false)
                line(player, "!!Bone Man??'s hovel.", line++, false)
                line(player, "I've placed twenty logs on the !!beacon?? and lit the fire with", line++, false)
                line(player, "my tinderbox. Now that it's blazing brightly, perhaps I", line++, false)
                line(player, "should speak with Blaze.", line++, false)
            }

            if (stage > 70) {
                // Replaced with stage 80 text.
            } else if (stage == 70) {
                line(player, "!!Blaze?? has now asked me to maintain the nearby !!beacon??.", line++, false)
                line(player, "To maintain the !!beacon??, I need to add !!five logs?? of the same", line++, false)
                line(player, "type.", line++, false)
            }

            if (stage > 80) {
                line(player, "Blaze has explained how to maintain a beacon. When the", line++, true)
                line(player, "fire begins to die out, five more logs can be added to", line++, true)
                line(player, "restore a beacon to its blazing state. I've tended the", line++, true)
                line(player, "beacon near Blaze and have reported back to him.", line++, true)
            } else if (stage == 80) {
                line(player, "!!Blaze?? has explained how to maintain a beacon. When the", line++, false)
                line(player, "fire begins to die out, !!five more logs?? can be added to", line++, false)
                line(player, "restore a beacon to its blazing state.", line++, false)
                line(player, "!!Blaze?? has asked me to maintain the !!beacon?? nearest him.", line++, false)
            }

            if (stage > 90) {
                // Replaced with stage 100 complete text.
            } else if (stage == 90){
                line(player, "I need to talk to !!King Roald?? in !!Varrock Palace?? to claim my", line++, false)
                line(player, "reward.", line++, false)
            }

            if (stage == 100) {
                line(player, "I spoke to King Roald who thanked me for helping and", line++, true)
                line(player, "rewarded me with 20,000gp and 5,500 Firemaking XP. He", line++, true)
                line(player, "told me that if I'm able to light six, ten and all fourteen", line++, true)
                line(player, "fires simultaneously, he'll have further rewards for me.", line++, true)
                line++
                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        player.packetDispatch.sendString("20,000gp,", 277, 8 + 2)
        player.packetDispatch.sendString("5,500 Firemaking XP", 277, 9 + 2)
        player.packetDispatch.sendString("Full access to the beacon network", 277, 10 + 2)
        player.packetDispatch.sendString("1 Quest Point", 277, 11 + 2)
        player.packetDispatch.sendItemZoomOnInterface(Items.TINDERBOX_590, 235, 277, 3 + 2)
        player.skills.addExperience(Skills.FIREMAKING, 5500.0)
        player.inventory.add(Item(995,20000))
        AFUBeacon.resetAllBeacons(player)
        setVarbit(player, 1283, 0)
        player.questRepository.syncronizeTab(player)
    }

    override fun getConfig(player: Player?, stage: Int): IntArray {
        if(stage == 100) return intArrayOf(1282, 90)
        if(stage > 0) return intArrayOf(1282, 1)
        else return intArrayOf(1282, 0)
    }
}
