package content.region.asgarnia.burthorpe.quest.deathplateau

import core.api.addItemOrDrop
import core.api.getAttribute
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * Death Plateau Quest
 * @author bushtail
 * @author ovenbread
 */
@Initializable
class DeathPlateau : Quest("Death Plateau",44, 43, 1, 314, 0, 1, 80) {
    companion object {
        const val questName = "Death Plateau"
    }
    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        var line = 12
        var stage = getStage(player)

        var started = player?.questRepository?.getStage(questName)!! > 0

        if(!started){
            line(player, "I can start this quest by speaking to !!Denulth?? who is in his", line++)
            line(player, "tent at the !!Imperial Guard camp?? in !!Burthorpe", line++)
        } else {
            if(stage >= 5) {
                when(stage) {
                    in 1..18 -> {
                        line(player,"I have offered to help !!Denulth?? by finding !!another way?? up",line++,)
                        line(player,"!!Death Plateau.?? I also need to find the !!combination?? to the",line++,)
                        line(player,"!!equipment room?? and !!unlock?? the door.",line++,)
                    }
                    // Technically this part is to be done in parallel with the ball mechanism part above.
                    in 19..100 -> {
                        line(player,"I have found another way up Death Plateau.",line++, stage>26)
                        line++
                        line(player,"I have found the combination to the equipment room and",line++,stage>18)
                        line(player,"unlocked the door.",line++,stage>18)
                        line++
                    }
                }
            }
            if(stage >= 10) {
                when(stage) {
                    10 -> {
                        line(player,"The equipment room guard is staying at the local inn, the",line++,stage>18)
                        line(player,"!!Toad and Chicken.??",line++,stage>18)
                    }
                    in 11 .. 12 -> {
                        line(player,"The equipment room guard is staying at the local inn, the",line++,stage>18)
                        line(player,"!!Toad and Chicken.?? the guard wouldn't talk to me!",line++,stage>18)
                    }
                    in 13 .. 14 -> {
                        line(player,"The equipment room guard is staying at the local inn, the",line++,stage>18)
                        line(player,"!!Toad and Chicken.?? the guard wouldn't talk to me! I bought",line++,stage>18)
                        line(player,"the guard a drink and he seemed more helpful.",line++,stage>18)
                    }
                    in 15..100 -> {
                        line(player,"The equipment room guard is staying at the local inn, the",line++,stage>18)
                        line(player,"!!Toad and Chicken.?? the guard wouldn't talk to me! I bought",line++,stage>18)
                        line(player,"the guard a drink and he seemed more helpful. I gambled",line++,stage>18)
                        line(player,"with the guard until he ran out of money, he wrote out an !!IOU.??",line++,stage>18)
                        line++
                    }
                }
            }
            if(stage >= 16) {
                when(stage) {
                    16 -> {
                        line(player,"It turned out the IOU was written on the back of the",line++,stage>18)
                        line(player,"combination!",line++,stage>18)
                    }
                    in 17..100 -> {
                        line(player,"It turned out the IOU was written on the back of the",line++,stage>18)
                        line(player,"combination! I put the stone balls in the right places on the",line++,stage>18)
                        line(player,"stone mechanism and unlocked the door!",line++,stage>18)
                        line++
                    }
                }
            }
            if(stage >= 20) {
                when(stage) {
                    20 -> {
                        line(player,"!!Saba?? says that there is a !!sherpa?? living !!nearby?? that may",line++,false)
                        line(player,"know another way up Death Plateau.",line++,false)
                    }
                    21 -> {
                        line(player, "I found the !!sherpa's?? house.", line++, true)
                        // Note: You must give him the 21 items in one go. The items will cross out when you have them in your inventory.
                        line(player, "!!Tenzing?? will show me a !!secret way?? up Death Plateau if I get", line++, false)
                        line(player, "him some items:", line++, false)
                        line(player, "Ten loaves of bread.", line++, getAttribute(player, "deathplateau:bread", false))
                        line(player, "Ten cooked trout.", line++, getAttribute(player, "deathplateau:trout", false))
                        line(player, "Spiked boots.", line++, getAttribute(player, "deathplateau:boots", false))
                        line++
                        line(player, "!!Tenzing?? gave me his !!climbing boots??, I need to take them to", line++, false)
                        line(player, "!!Dunstan?? in !!Burthorpe?? for the !!spikes??", line++, false)
                    }
                    24 -> {
                        line(player, "I found the !!sherpa's?? house. I gave Tenzing the ten loaves", line++, true)
                        line(player, "of bread, ten cooked trout and the Spiked boots. Tenzing", line++, true)
                        line(player, "has given me a map of the secret way.", line++, true)
                        line++
                        line(player,"I need to !!check?? that the !!secret way?? is !!safe?? for the !!Imperial??", line++, false)
                        line(player,"!!Guard?? to use.", line++, false)
                    }
                    25 -> {
                        line(player, "I found the !!sherpa's?? house. I gave Tenzing the ten loaves", line++, true)
                        line(player, "of bread, ten cooked trout and the Spiked boots. Tenzing", line++, true)
                        line(player, "has given me a map of the secret way.", line++, true)
                        line++
                        line(player,"I need to !!check?? that the !!secret way?? is !!safe?? for the !!Imperial??", line++, false)
                        line(player,"!!Guard?? to use.", line++, false)
                    }
                    in 26 .. 100 -> {
                        line(player, "I found the !!sherpa's?? house. I gave Tenzing the ten loaves", line++, true)
                        line(player, "of bread, ten cooked trout and the Spiked boots. Tenzing", line++, true)
                        line(player, "has given me a map of the secret way. I checked the", line++, true)
                        line(player, "secret way and it is safe for the Imperial Guard to use.", line++, true)
                        line++
                    }
                }
                when(stage) {
                    in 22 .. 23 -> {
                        line(player, "!!Dunstan?? will help me if I get his !!son signed up?? for the", line++, false)
                        line(player, "!!Imperial Guard??. I will need an !!Iron bar?? for the boots", line++, false)
                    }
                    in 24 .. 100 -> {
                        line(player,"I have given Dunstan the certificate to prove that his son", line++, true)
                        line(player,"has been signed up for the Imperial Guard. Dunstan made", line++, true)
                        line(player, "me the Spiked boots.", line++, true)
                        line++
                    }
                }
            }
            when(stage) {
                in 30 .. 40 -> {
                    line(player, "I should go and tell !!Denulth?? I've completed my mission.", line++, false)
                }
                in 41 .. 100 -> {
                    line(player, "I gave Denulth the secret way map and the combination.", line++, true)
                    line(player, "Denulth gave me some Steel claws and trained me in.", line++, true)
                    line(player, "attack. I'm now an honorary member of the Imperial Guard!", line++, true)
                }
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
        player.packetDispatch.sendString("You have completed the Death Plateau Quest!", 277, 4)
        player.packetDispatch.sendItemZoomOnInterface(Items.STEEL_CLAWS_3097, 240, 277, 5)

        drawReward(player,"1 Quest Point", ln++)
        drawReward(player,"3,000 Attack XP", ln++)
        drawReward(player,"Some Steel Claws", ln++)
        drawReward(player,"Ability to make Claws", ln++)

        addItemOrDrop(player, Items.STEEL_CLAWS_3097, 1)
        player.skills.addExperience(Skills.ATTACK, 3000.0)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}