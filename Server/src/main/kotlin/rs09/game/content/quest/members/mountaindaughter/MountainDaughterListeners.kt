package rs09.game.content.quest.members.mountaindaughter

import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class MountainDaughterListeners : InteractionListener(){

    val rockSlide = 5847
    val boulder = 5842
    val tallTree = 5848
    val rockCluster = 5849
    val flatStoneOne = 5850
    val flatStoneTwo = 5851
    val shiningPool = 5897


    val plank = Items.PLANK_960
    val pole = Items.POLE_4494
    val rope = Items.ROPE_954
    val mud = Items.MUD_4490


    override fun defineListeners() {

        on(rockSlide, SCENERY,"Climb-over"){player, node ->

            return@on true
        }

        on(tallTree, SCENERY,"Climb"){player, node ->

            return@on true
        }

        on(shiningPool, SCENERY,"Listen-to"){ player, node ->

            return@on true
        }

        on(flatStoneOne, SCENERY,"Jump-across"){ player, node ->

            return@on true
        }

        on(flatStoneTwo, SCENERY,"Jump-across"){ player, node ->

            return@on true
        }

        onUseWith(SCENERY,boulder,rope){player, used, with ->

            return@onUseWith true
        }

        onUseWith(SCENERY,tallTree,mud){player, used, with ->

            return@onUseWith true
        }

        onUseWith(SCENERY,flatStoneOne,plank){player, used, with ->

            return@onUseWith true
        }

        onUseWith(SCENERY,flatStoneTwo,plank){player, used, with ->

            return@onUseWith true
        }

        onUseWith(SCENERY,rockCluster,pole){player, used, with ->

            return@onUseWith true
        }


    }
}