package content.region.kandarin.quest.grandtree

import core.api.addItemOrDrop
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
class TheGrandTree: Quest("The Grand Tree", 71, 70, 5, 150, 0, 1, 160) {
    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.DACONIA_ROCK_793, 230, 277, 5)
        drawReward(player,"5 Quest Points", ln++)
        drawReward(player,"7900 Agility XP", ln++)
        drawReward(player,"18,400 Attack XP", ln++)
        drawReward(player,"2150 Magic XP", ln++)
        player.skills.addExperience(Skills.AGILITY, 7900.0)
        player.skills.addExperience(Skills.ATTACK, 18400.0)
        player.skills.addExperience(Skills.MAGIC, 2150.0)
        player.questRepository.syncronizeTab(player)
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        player?: return
        line(player, "I can start this quest at the !!Grand Tree?? in the !!Gnome??", line++, stage > 0)
        line(player, "!!Stronghold?? by speaking to !!King Narnode Shareen.??", line++, stage > 0)
        line++
        if(stage == 0) {
            line(player, "I must have:", line++)
            line(player, "!!Level 25 Agility.??", line++)
            line(player, "!!High enough combat to defeat a level 172 demon.??", line++)
        }
        if(stage >= 10){
            line(player, "King Narnode Shareen suspects sabotage on the Grand Tree and", line++, stage > 10)
            line(player, "wants to confirm it by consulting with Hazelmere.", line++, stage > 10)
            line++
            line(player, "Hazelmere's dwelling is located on a towering hill, on an island ", line++, stage > 10)
            line(player, "east of Yanille.", line++, stage > 10)
            line++
        }
        if(stage >= 20){
            line(player, "Report back to King Narnode Shareen.", line++, stage > 20)
            line++
        }
        if(stage >= 40){
            line(player, "King Narnode suspects that someone has forged his royal seal.", line++, stage > 40)
            line(player, "Find Glough and notify him about the situation.", line++, stage > 40)
            line++
        }
        if(stage >= 45){
            line(player, "Glough says the humans are going to invade! Report back to the king.", line++, stage > 45)
            line++
        }
        if(stage >= 46){
            line(player, "Talk to the prisoner at the top of the tree", line++, stage > 46)
            line++
        }
        if(stage >= 47){
            line(player, "Search Glough's home for clues.", line++, stage > 47)
            line++
        }
        if(stage >= 55){
            line(player, "Glough has placed guards on the front gate to stop you escaping!", line++, stage > 55)
            line(player, "Use King Narnode's glider pilot fly you away until things calm down.", line++, stage > 55)
            if(player.hasItem(Item(Items.LUMBER_ORDER_787))){
                line(player, "Bring the lumber order to Charlie", line++, stage > 55)
            }
            line++

        }
        if(stage >= 60){
            line(player, "Search Glough's girlfriend's house for his chest keys.", line++, stage > 60)
        }
        if(stage >= 60){
            if(player.hasItem(Item(Items.GLOUGHS_KEY_788))){
                line(player, "Find a use for Gloughs key and report back to the king.", line++, stage > 60)
            }
        }
        if(stage >= 70){
            line(player, "Find a use for the strange twigs from King Narnode Shareen.", line++, stage > 70)
        }
        if(stage == 100){
            line++
            line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
        }
    }

    companion object {
        const val questName = "The Grand Tree"
    }
}
