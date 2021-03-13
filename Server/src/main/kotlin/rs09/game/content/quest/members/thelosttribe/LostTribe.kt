package rs09.game.content.quest.members.thelosttribe

import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items

@Initializable
/**
 * Represents the lost tribe quest and quest journal
 * @author Ceikry
 */
class LostTribe : Quest("Lost Tribe",84,83,1) {
    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        player ?: return
        var line = 11
        if(stage == 0){
            line(player,"I can start this quest by speaking to !!Sigmund?? in !!Lumbridge??",line++)
            line(player,"!!Castle.??",line++)
            line(player,"I must have completed:",line++)
            line(player,"Rune Mysteries",line++,player?.questRepository?.isComplete("Rune Mysteries") == true)
            line(player,"Goblin Diplomacy",line++,player?.questRepository?.isComplete("Goblin Diplomacy") == true)
            line(player,"and have:",line++)
            line(player,"Level 17 mining",line++,player.skills.getLevel(Skills.MINING) >= 17)
            line(player,"Level 13 agility",line++,player.skills.getLevel(Skills.AGILITY) >= 13)
            line(player,"Level 13 thieving",line++,player.skills.getLevel(Skills.THIEVING) >= 13)
        } else {
            if(stage >= 10) {
                line(player, "!!Sigmund?? said I should ask around town and see", line++, stage >= 20)
                line(player, "if anyone had seen what happened in the !!cellar??.", line++, stage >= 20)
            }
            if(stage >= 20){
                line(player, "I found out that someone had seen a !!goblin-like?? creature in the cellar.",line++,stage >= 30)
                line(player,"I should go speak to !!The Duke?? about this.",line++,stage >= 30)
            }
            if(stage >= 30){
                line(player,"The Duke gave me permission to investigate !!the tunnel??.",line++,stage >= 40)
                line(player,"I should get down into the !!cellar?? and try to unblock the !!tunnel??.",line++,stage >= 40)
            }
            if(stage >= 40){
                line(player,"The Duke asked me to speak with the !!librarian?? in !!Varrock??",line++,stage >= 43)
                line(player,"and see if he can identify the brooch.",line++,stage >= 43)
            }
            if(stage >= 43){
                line(player,"I should go speak with some !!goblins?? who might be able",line++,stage >= 44)
                line(player,"to teach me more about the !!Dorgeshuun??. Perhaps the",line++,stage >= 44)
                line(player,"!!generals in Goblin Village?? can help.",line++,stage >= 44)
            }
            if(stage >= 44){
                line(player,"I should return to !!Duke Horacio?? with my findings.",line++,stage >= 45)
            }
            if(stage >= 45){
                line(player,"I should try to make contact with the !!Dorgeshuun??.",line++,stage >= 46)
            }
            if(stage >= 46){
                line(player,"I need to return to !!Duke Horacio?? and ask him to stop",line++,stage >= 47)
                line(player,"the war!",line++,stage >= 47)
            }
            if(stage >= 47){
                line(player,"Duke Horacio said the goblins stole some !!silverware??.",line++,stage >= 49)
                line(player,"I need to find it if I want to stop the war.",line++,stage >= 49)
            }
            if(stage >= 48){
                line(player,"I found !!H.A.M. robes?? in !!Sigmund??'s chest. Perhaps",line++,stage >= 49)
                line(player,"I should check the !!H.A.M. hideout?? behind the castle",line++,stage >= 49)
                line(player,"for the missing !!silverware??.",line++,stage >= 49)
            }
            if(stage >= 49){
                line(player,"I found the !!silverware?? in the !!H.A.M. hideout??.",line++,stage >= 50)
                line(player,"I should go inform !!Duke Horacio?? immediately!",line++,stage >= 50)
            }
            if(stage >= 50){
                line(player,"!!The Duke?? gave me a !!peace treaty?? to take to",line++,stage >= 51)
                line(player,"the cave goblins. I should do so at once!",line++,stage >= 51)
            }
            if(stage >= 100){
                line(player,"!!QUEST COMPLETE!??",line++)
            }
        }

    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        var ln = 10
        player.packetDispatch.sendItemZoomOnInterface(Items.BROOCH_5008,230,277,5)
        drawReward(player,"1 Quest Point",ln++)
        drawReward(player,"3000 Mining XP",ln++)
        drawReward(player,"A Ring of Life",ln++)
        drawReward(player,"Freedom of the",ln++)
        drawReward(player,"Dorgeshuun mines.",ln++)
        player.skills.addExperience(Skills.MINING,3000.0)
        if(!player.inventory.add(Item(Items.RING_OF_LIFE_2570))){
            GroundItemManager.create(Item(Items.RING_OF_LIFE_2570),player)
        }
        player.varpManager.get(465).setVarbit(0,11).send(player)
    }

    override fun getConfig(player: Player?, stage: Int): IntArray {
        player ?: return intArrayOf(465,0)
        if(stage in 50..99){
            return intArrayOf(465,9)
        }
        if(stage in 46..49){
            return intArrayOf(465,8)
        }
        if(stage in 44..45){
            return intArrayOf(465,7)
        }
        if(stage in 30..43 && player.getAttribute("tlt-hole-cleared",false)){
            return intArrayOf(465,4)
        }
        if(stage >= 100) return intArrayOf(465,11)
        if(stage > 0) return intArrayOf(465,1)
        return intArrayOf(465,0)
    }
}