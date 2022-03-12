package core.game.content.quest.fremtrials

import core.game.node.entity.player.Player
import api.*
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import rs09.game.content.activity.allfiredup.AFUBeacon

@Initializable
class FremennikTrials : Quest("Fremennik Trials",64,63,3,347,0,1,10){

    class SkillRequirement(val skill: Int?, val level: Int?)

    val requirements = arrayListOf<SkillRequirement>()

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        val started = player?.questRepository?.getStage("Fremennik Trials")!! > 0

        if(!started){
            line(player,"Requirements to complete quest:",line++)
            line += 1
            line(player,"Level 40 Woodcutting",line++, player.skills.getStaticLevel(Skills.WOODCUTTING) >= 40)
            line(player,"Level 40 Crafting",line++,player.skills.getStaticLevel(Skills.CRAFTING) >= 40)
            line(player,"Level 25 Fletching",line++,player.skills.getStaticLevel(Skills.FLETCHING) >= 25)
            line(player,"I must also be able to defeat a !!level 69 enemy?? and must",line++)
            line(player,"not be afraid of !!combat without any weapons or armour.",line++)
            line += 1
            line(player,"I can start this quest by speaking to !!Chieftan Brundt?? on",line++)
            line(player,"the !!Fremennik Longhall,?? which is in the town of !!Rellekka?? to",line++)
            line(player,"the north of !!Sinclair Mansion??.",line)
        }
        else if(started && stage != 100){
            line(player,"In order to join the Fremenniks, I need to",line++)
            line(player,"!!earn the approval?? of !!7 members?? of the elder council.",line++)
            line(player,"I've written down the members who I can try to help:",line++)
            line(player,"Manni the Reveller",line++,player.getAttribute("fremtrials:manni-vote",false))
            line(player,"Swensen the Navigator",line++,player.getAttribute("fremtrials:swensen-vote",false))
            line(player,"Sigli the Huntsman",line++,player.getAttribute("fremtrials:sigli-vote",false))
            line(player,"Olaf the Bard",line++,player.getAttribute("fremtrials:olaf-vote",false))
            line(player,"Thorvald the Warrior",line++,player.getAttribute("fremtrials:thorvald-vote",false))
            line(player,"Sigmund the Merchant",line++,player.getAttribute("fremtrials:sigmund-vote",false))
            line(player,"Peer the Seer",line++,player.getAttribute("fremtrials:peer-vote",false))
            line(player,"So far I have gotten ${player.getAttribute("fremtrials:votes",0)} votes.",line++)
        }
        else if(stage == 100){
            line(player,"I made my way to the far north of !!Kandarin?? and found",line++)
            line(player,"the Barbarian hometown of !!Rellekka??. The tribe that live",line++)
            line(player,"there call themselves the !!Fremennik??, and offerred me the",line++)
            line(player,"chance to join them if I could pass their trials.",line++)
            line += 1
            line(player,"I managed to persuade !!seven?? of the !!twelve?? council of",line++)
            line(player,"elders to vote for me at their next meeting. and become an",line++)
            line(player,"honorary member of the !!Fremennik??.",line++)
            line += 1
            line(player,"---QUEST COMPLETE---",line++)
            line(player,"They also gave me a new name:",line++)
            line(player,player.getAttribute("fremennikname","hingerdinger lmao"),line++)
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.FREMENNIK_HELM_3748, 235, 277, 5)
        drawReward(player, "3 Quest points, 2.8k XP in:", ln++)
        drawReward(player, "Strength, Defence, Attack,",ln++)
        drawReward(player, "Hitpoints, Fishing, Thieving,", ln++)
        drawReward(player, "Agility,Crafting, Fletching,", ln++)
        drawReward(player, "Woodcutting",ln++)
        player.skills.addExperience(Skills.STRENGTH, 2812.4)
        player.skills.addExperience(Skills.DEFENCE, 2812.4)
        player.skills.addExperience(Skills.ATTACK, 2812.4)
        player.skills.addExperience(Skills.HITPOINTS, 2812.4)
        player.skills.addExperience(Skills.FISHING, 2812.4)
        player.skills.addExperience(Skills.THIEVING, 2812.4)
        player.skills.addExperience(Skills.AGILITY, 2812.4)
        player.skills.addExperience(Skills.CRAFTING, 2812.4)
        player.skills.addExperience(Skills.FLETCHING, 2812.4)
        player.skills.addExperience(Skills.WOODCUTTING, 2812.4)
        player.questRepository.syncronizeTab(player)
    }

    override fun newInstance(`object`: Any?): Quest {
        requirements.add(SkillRequirement(Skills.FLETCHING,25))
        requirements.add(SkillRequirement(Skills.CRAFTING,40))
        requirements.add(SkillRequirement(Skills.WOODCUTTING,40))
        return this
    }

}
