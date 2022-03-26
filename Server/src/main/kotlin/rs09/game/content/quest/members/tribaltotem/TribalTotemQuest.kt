package rs09.game.content.quest.members.tribaltotem

import api.rewardXP
import core.game.content.quest.fremtrials.FremennikTrials
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class TribalTotem : Quest("Tribal Totem",126,125,1,200,0,1,5){
    class SkillRequirement(val skill: Int?, val level: Int?)

    val requirements = arrayListOf<TribalTotem.SkillRequirement>()

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        val started = player?.questRepository?.getStage("Tribal Totem")!! > 0

        if(!started){
            line(player,"I can start this quest by speaking to !!Kangai Mau?? in",line++)
            line(player,"!!Shrimp & Parrot?? restaurant in Brimhaven.",line++)
            line += 1
            line(player,"To complete this quest I need:",line++)
            line(player,"!!Level 21 Theiving??",line++, player?.skills?.getStaticLevel(Skills.THIEVING)!! >= 21)
        }
        else if(started && stage != 100){
            if(stage >= 10){
                line(player,"I agreed to help !!Kangai Mau?? on Brimhaven recover",line++,stage>15)
                line(player,"the tribal totem stolen from his village by",line++,stage>15)
                line(player,"!!Lord Handelmort??.",line++,stage>10)
            }
            if(stage >= 20){
                line(player,"I found a package due for delivery to !!Lord Handelmort??",line++,stage>25)
                line(player,"at the !!G.P.D.T. Depot??, and swapped the label for the",line++,stage>25)
                line(player,"!!Wizard Cromperty's?? experimental teleport block.",line++,stage>25)
            }
            if(stage >= 30){
                line(player,"I got the !!G.P.D.T.?? men to deliver the teleport block to",line++,stage>35)
                line(player,"!!Lord Handelmort?? and teleported myself inside.",line++,stage>35)
            }
        }
        else if(stage == 100){
            if(stage == 100){
                line(player,"After bypassing the traps and security inside the mansion I was able",line++)
                line(player,"to reclaim the totem, and take it back to !!Kangai Mau??, who rewarded",line++)
                line(player,"me for all of my help.",line++)
                line += 1
                line(player,"<col=FF0000>QUEST COMPLETE!</col>",line++ +1)
            }
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.TOTEM_1857,230,277,5)
        drawReward(player,"1 Quest point",ln++)
        drawReward(player,"1,775 Thieving XP",ln++)
        drawReward(player,"5 Swordfish",ln++)
        rewardXP(player,Skills.THIEVING,1775.0)
        if(!player.inventory.add(Item(Items.SWORDFISH_373,5))){
            GroundItemManager.create(Item(Items.SWORDFISH_373,5),player)
        }
        cleanTTAttributes(player)
    }

    override fun newInstance(`object`: Any?): Quest {
        requirements.add(TribalTotem.SkillRequirement(Skills.THIEVING, 21))
        return this
    }

    private fun cleanTTAttributes(player: Player){
        player.removeAttribute("TT:StairsChecked")
        player.removeAttribute("TT:DoorUnlocked")
    }

}
