package rs09.game.interaction.region.dungeons.brimhaven

import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.agility.AgilityHandler
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class BrimhavenDungeonListeners : InteractionListener() {

    val EXIT = 5084
    val SANIBOCH = NPCs.SANIBOCH_1595
    val VINES = intArrayOf(5103,5104,5105,5106,5107)
    val STEPPING_STONES = intArrayOf(5110,5111)
    val STAIRS = intArrayOf(5094,5096,5097,5098)
    val LOGS = intArrayOf(5088,5090)

    override fun defineListeners() {
        on(EXIT,SCENERY,"leave"){ player, _ ->
            player.properties.teleportLocation = Location.create(2745, 3152, 0)
            return@on true
        }

        on(STAIRS,SCENERY,"walk-up","walk-down"){ player, node ->
            BrimhavenUtils.handleStairs(node.asScenery(),player)
            return@on true
        }

        on(STEPPING_STONES,SCENERY,"jump-from"){ player, node ->
            BrimhavenUtils.handleSteppingStones(player,node.asScenery())
            return@on true
        }

        on(VINES,SCENERY,"chop-down"){ player, node ->
            BrimhavenUtils.handleVines(player,node.asScenery())
            return@on true
        }

        on(SANIBOCH,NPC,"pay"){player, node ->
            player.dialogueInterpreter.open(SANIBOCH,node.asNpc(),10)
            return@on true
        }

        on(LOGS,SCENERY,"walk-across"){ player, node ->

            if (player.skills.getLevel(Skills.AGILITY) < 30) {
                player.packetDispatch.sendMessage("You need an agility level of 30 to cross this.")
                return@on true
            }

            if(node.id == 5088){
                AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    Location.create(2687, 9506, 0),
                    Animation.create(155),
                    0.0,
                    null
                )
            } else {
                AgilityHandler.walk(
                    player,
                    -1,
                    player.location,
                    Location.create(2682, 9506, 0),
                    Animation.create(155),
                    0.0,
                    null
                )
            }

            return@on true
        }
    }
}