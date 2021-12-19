package rs09.game.interaction.item

import api.*
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class ToyListeners : InteractionListener() {
    companion object {
        val MARIONETTES = intArrayOf(Items.RED_MARIONETTE_6867, Items.GREEN_MARIONETTE_6866, Items.BLUE_MARIONETTE_6865)
        private val MARIONETTE_JUMP = Animation(3003)
        private val MARIONETTE_WALK = Animation(3004)
        private val MARIONETTE_BOW = Animation(3005)
        private val MARIONETTE_DANCE = Animation(3006)
        private val MARIONETTE_GFX = arrayOf(intArrayOf(507, 508, 509, 510), intArrayOf(511, 512, 513, 514), intArrayOf(515, 516, 517, 518))
        private val SNOWGLOBE_SHAKE = Animation(7535) //Initial Shake
        private val SNOWGLOBE_HOLDFACE = Animation(7536) //Immediately after shake, player holds the snow globe to face
        private val SNOWGLOBE_INTERFACE = 659 //After HOLDFACE this interface is displayed, player either clicks 'continue' for inv of snowballs, or 'close' for no snowballs
        private val SNOWGLOBE_DOWNFAST = Animation(7537) //Used when player hit 'close' on the interface
        private val SNOWGLOBE_DOWNSLOW = Animation(7538) //Used when the player hit 'continue' on the interface
        private val SNOWGLOBE_STOMP = Animation(7528) //When player hits continue this animation plays
        private val SNOWGLOBE_SNOW = Graphics(1284) //When Animation STOMP is playing this gfx also plays
        val YOYO_PLAY = Animation(1457)
        val YOYO_LOOP = Animation(1458)
        val YOYO_WALK = Animation(1459)
        val YOYO_CRAZY = Animation(1460)
        val ZOMBIE_HEAD_TALK_AT = Animation(2840)
        val ZOMBIE_HEAD_DISPLAY = Animation(2844)
    }

    override fun defineListeners() {
        on(Items.CHOCATRICE_CAPE_12634, ITEM, "operate"){player, _ ->
            lockInteractions(player, 2)
            visualize(player, 8903, 1566)
            return@on true
        }

        on(MARIONETTES, ITEM, "jump", "walk", "bow", "dance") {player, marionette ->
            val index = MARIONETTES.indexOf(marionette.id)

            lockInteractions(player, 2)
            when(getUsedOption(player)) {
                "jump" -> visualize(player, MARIONETTE_JUMP, MARIONETTE_GFX[index][0])
                "walk" -> visualize(player, MARIONETTE_WALK, MARIONETTE_GFX[index][1])
                "bow" ->  visualize(player, MARIONETTE_BOW, MARIONETTE_GFX[index][2])
                "dance" -> visualize(player, MARIONETTE_DANCE, MARIONETTE_GFX[index][3])
            }
            return@on true
        }

        on(Items.REINDEER_HAT_10507, ITEM, "operate"){player, _ ->
            lockInteractions(player, 2)
            animate(player, 5059)
            return@on true
        }

        on(Items.SNOW_GLOBE_11949, ITEM, "shake") {player, _ ->
            lockInteractions(player, 2)
            animate(player, SNOWGLOBE_SHAKE)
            runTask(player, 3) {
                animate(player, SNOWGLOBE_HOLDFACE)
                runTask(player){
                    openInterface(player, SNOWGLOBE_INTERFACE)
                }
            }
            return@on true
        }

        on(Items.YO_YO_4079, ITEM, "play", "loop", "walk", "crazy"){player, _ ->
            val option = getUsedOption(player)

            lockInteractions(player, 2)
            when(option) {
                "play" -> animate(player, YOYO_PLAY)
                "loop" -> animate(player, YOYO_LOOP)
                "walk" -> animate(player, YOYO_WALK)
                "crazy" -> animate(player, YOYO_CRAZY)
            }
            return@on true
        }

        on(Items.ZOMBIE_HEAD_6722, ITEM, "talk-at", "display", "question"){player, _ ->
            val option = getUsedOption(player)

            lockInteractions(player, 2)
            when(option) {
                "talk-at" -> {
                    animate(player, ZOMBIE_HEAD_TALK_AT)
                    sendChat(player, "Alas!")
                }

                "display" -> {
                    animate(player, ZOMBIE_HEAD_DISPLAY)
                    sendChat(player, "MWAHAHAHAHAHAHAH")
                }
            }
            return@on true
        }
    }
}