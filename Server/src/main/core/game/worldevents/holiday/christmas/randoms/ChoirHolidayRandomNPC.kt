package core.game.worldevents.holiday.christmas.randoms

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.tools.RandomFunction
import org.rs09.consts.NPCs

class ChoirHolidayRandomNPC() : HolidayRandomEventNPC(NPCs.ZANARIS_CHOIR_3312) {
    override fun init() {
        super.init()
        queueScript(this, 4, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    face(player)
                    when (RandomFunction.getRandom(3)) {
                        0 -> {
                            sendChat(this, "Jingle bells, jingle bells!")
                            sendChat(this, "Jingle all the way!", 4)
                            sendChat(this, "Oh what fun it is to ride", 8)
                            sendChat(this, "On a one unicorn open sleigh", 12)
                            sendChat(this, "HEY!", 15)
                            return@queueScript delayScript(this, 16)
                        }
                        1 -> {
                            sendChat(this, "Silver bells")
                            sendChat(this, "Silver bells", 5)
                            sendChat(this, "It's Christmas time in the city", 10)
                            sendChat(this, "Ring a ling", 15)
                            sendChat(this, "Hear them ring", 20)
                            sendChat(this, "Soon it will be Christmas day", 25)
                            return@queueScript delayScript(this, 26)
                        }
                        2 -> {
                            sendChat(this, "Deck the halls with boughs of ranarr")
                            sendChat(this, "Fa la la la la la la la!", 5)
                            sendChat(this, "Tis the season to be jolly", 10)
                            sendChat(this, "Fa la la la la la la la!", 15)
                            return@queueScript delayScript(this, 16)
                        }
                        3 -> {
                            sendChat(this, "O Wintumber Tree, O Wintumber tree,")
                            sendChat(this, "How lovely are your branches!", 5)
                            sendChat(this, "O Wintumber Tree, O Wintumber tree,", 10)
                            sendChat(this, "Of all the trees most lovely", 15)
                            return@queueScript delayScript(this, 16)
                        }
                        else -> return@queueScript keepRunning(this)
                    }
                }
                1 -> {
                    terminate()
                    return@queueScript stopExecuting(this)
                }
                else -> return@queueScript stopExecuting(this)
            }
        }
    }

    override fun talkTo(npc: NPC) {
    }
}