package content.region.asgarnia.burthorpe.quest.trollstronghold

import content.region.asgarnia.burthorpe.quest.deathplateau.DeathPlateau
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Troll Stronghold Quest
 * @author ovenbread
 */
@Initializable
class TrollStronghold : Quest("Troll Stronghold",128, 127, 1, 317, 0, 1, 50) {

    /**
     *  1 - Talked to Denulth to start the quest
     *  3 - Enter the Arena with Dad
     *  4 - Start fighting Dad
     *  5 - Dad surrenders or gets killed, allowed to exit the Arena
     *  8 - Unlocks Prison Gate
     *  9 - Unlocked Mad Eadgar's cell
     *  10 - Unlocked Godric's cell
     *  11 - Unlocked both Mad Eadgar's and Godric's cell
     *  100 - Finish at Dunstan
     */
    companion object {
        const val questName = "Troll Stronghold"
    }
    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = getQuestStage(player!!, questName) > 0

        if(!started){
            line(player, "I can start this quest by speaking to !!Denulth?? in his tent at", line++)
            line(player, "the !!Imperial Guard camp?? in !!Burthorpe?? after completing the", line++)
            line(player, "!!Death Plateau Quest??", line++, isQuestComplete(player, DeathPlateau.questName))
            line++
            line(player, "To complete this quest I need:", line++)
            line(player, "Level 15 Agility.", line++, hasLevelStat(player, Skills.AGILITY, 15))
            line(player, "I also need to be able to defeat a !!level 113 Troll??.", line++)
            line(player, "Level 30 Thieving might be useful.", line++, hasLevelStat(player, Skills.THIEVING, 30))
            if (isQuestComplete(player, DeathPlateau.questName) && hasLevelStat(player, Skills.AGILITY, 15) && hasLevelStat(player, Skills.THIEVING, 30)) {
                line(player, "I have all the requirements to start this quest.", line++)
            }
        } else {
            line(player, "I promised !!Denulth?? that I would rescue !!Godric?? from the !!Troll??", line++, stage == 100)
            line(player, "!!Stronghold??", line++, stage == 100)
            line++
            if (stage >= 5 || inBank(player, Items.CLIMBING_BOOTS_3105) || inInventory(player, Items.CLIMBING_BOOTS_3105) || inEquipment(player, Items.CLIMBING_BOOTS_3105)) {
                line(player, "I got some !!climbing boots?? from !!Tenzing??", line++, true)
            } else {
                line(player, "I have to get some !!climbing boots?? from !!Tenzing??", line++)
            }
            line++
            if (stage >= 5) {
                line(player, "I have defeated the !!Troll Champion??", line++, true)
            } else if (stage >= 3) {
                line(player, "I have accepted the !!Troll Champion's?? challenge.", line++)
            }
            line++
            if (stage >= 7) {
                line(player, "I found my way into the Troll Stronghold", line++, true)
            } else if (stage >= 5) {
                line(player, "I have to find a way to get into the !!Troll Stronghold??", line++)
            }
            line++
            if (stage >= 8) {
                line(player, "I found my way into the !!Prison??", line++, true)
            } else if (stage >= 5) {
                line(player, "I have to find a way to get into the !!prison??", line++)
            }
            line++
            if (stage >= 11) {
                line(player, "I've rescued !!Godric?? and !!Mad Eadgar??", line++, true)
            } else if (stage >= 8) {
                line(player, "I have to rescue !!Godric?? and !!Mad Eadgar??", line++)
            }
            if (stage >= 100) {
                line++
                line(player, "I talked to Dunstan and he rewarded me.", line++, true)
            } else if (stage >= 11) {
                line(player, "I should return and tell !!Dunstan?? his son is safe.", line++)
            }
            if (stage >= 100) {
                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed the Troll Stronghold quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.MYSTERIOUS_LAMP_13227, 240, 277, 5)

        drawReward(player,"1 Quest Point", ln++)
        drawReward(player,"2 Reward lamps giving 10,000", ln++)
        drawReward(player,"XP each", ln++)

        addItemOrDrop(player, Items.MYSTERIOUS_LAMP_13227, 2)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}