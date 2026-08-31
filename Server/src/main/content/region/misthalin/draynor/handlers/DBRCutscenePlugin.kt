package content.region.misthalin.draynor.handlers

import core.api.*
import core.game.activity.Cutscene
import core.game.component.Component
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.context.CameraContext.CameraType
import core.net.packet.out.CameraViewPacket
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class DBRCutscene(player: Player) : Cutscene(player) {
    companion object {
        const val HAS_SEEN_RECORDING = "draynor-recording"

        // Animations
        private val STEAL_ANIMATION = Animation(832)
        private val CAST_ANIMATION = Animation(1167)
        private val TELEKINETIC_ANIMATION = Animation(723)
        private val DEATH_ANIMATION = Animation(2553)
        private val TELEPORT_ANIMATION = Animation(1816)
        private val PICK_UP_ANIMATION = Animation(827)
        private val HIT_ANIMATION = Animation(401)
        private val BLOCK_ANIMATION = Animation(425)
        private val THUNDER_ANIMATION = Animation(811)
        private val WISE_JUMP = Animation(2555)
        private val GUARD_JUMP = Animation(2556)
        private val STOMP_ANIM = Animation(1820)

        // Graphics
        private val TELEKINETIC_GRAPHIC = Graphics(142, 96)
        private val SHOCK_GRAPHIC = Graphics(432, 0, 0)
        private val SHOCK_CAST = Graphics(433)
        private val TELE_OTHER_CAST = Graphics(343)
        private val TELE_OTHER_TARGET = Graphics(342)
        private val PURPLE_GRAPHIC = Graphics(301, 100)
        private val THUNDER_GRAPHIC = Graphics(76)

        // Projectile ids
        private const val SHOCK_PROJECTILE_ID = 434

        // Components
        private val CRACKED = Component(385)
    }

    override fun setup() {
        setExit(player.location)
        loadRegion(8524)

        // Add all NPCs with their initial positions
        addNPC(NPCs.COOL_MOM227_2579, 8, 52, Direction.NORTH)
        addNPC(NPCs.OLIVIA_2572, 5, 49, Direction.WEST)
        addNPC(NPCs.MARKET_GUARD_2571, 13, 48, Direction.WEST)
        addNPC(NPCs.PUREPKER895_2575, 24, 50, Direction.WEST)
        addNPC(NPCs.BANKER_2568, 18, 45, Direction.EAST)
        addNPC(NPCs.BANKER_2569, 18, 43, Direction.NORTH)
        addNPC(NPCs.BANKER_2570, 18, 42, Direction.NORTH)
        addNPC(NPCs.ELFINLOCKS_2578, 21, 44, Direction.NORTH)
        addNPC(NPCs.SP34KR_1337_2577, 22, 42, Direction.WEST)
        addNPC(NPCs.QUTIEDOLL_2576, 21, 42, Direction.EAST)
        addNPC(NPCs.WISE_OLD_MAN_2566, 16, 51, Direction.SOUTH)

        // Set banker HP
        getNPC(NPCs.BANKER_2568)?.skills?.setStaticLevel(Skills.HITPOINTS, 15)
        getNPC(NPCs.BANKER_2568)?.skills?.setLevel(Skills.HITPOINTS, 15)
        getNPC(NPCs.BANKER_2569)?.skills?.setStaticLevel(Skills.HITPOINTS, 15)
        getNPC(NPCs.BANKER_2569)?.skills?.setLevel(Skills.HITPOINTS, 15)
        getNPC(NPCs.BANKER_2570)?.skills?.setStaticLevel(Skills.HITPOINTS, 15)
        getNPC(NPCs.BANKER_2570)?.skills?.setLevel(Skills.HITPOINTS, 15)
    }

    private fun camera(x: Int, y: Int, xRot: Int, yRot: Int, height: Int, speed: Int) {
        val loc = base.transform(x, y, 0)
        PacketRepository.send(
            CameraViewPacket::class.java,
            CameraContext(player, CameraType.POSITION, loc.x, loc.y, height, 1, speed)
        )
        PacketRepository.send(
            CameraViewPacket::class.java,
            CameraContext(player, CameraType.ROTATION, loc.x + xRot, loc.y + yRot, height, 1, speed)
        )
    }

    private fun castShock(target: Entity, wiseOldMan: Entity) {
        wiseOldMan.animate(CAST_ANIMATION)
        wiseOldMan.graphics(SHOCK_CAST)
        val projectile = Projectile.create(wiseOldMan, target, SHOCK_PROJECTILE_ID)
        projectile.send()
    }

    private fun castShock(loc: Location, wiseOldMan: Entity) {
        wiseOldMan.animate(CAST_ANIMATION)
        wiseOldMan.graphics(SHOCK_CAST)
        val projectile = Projectile.create(wiseOldMan, null, SHOCK_PROJECTILE_ID, 30, 30, 41, 140, 0, 0)
        projectile.endLocation = loc
        projectile.send()
    }

    private fun castTelekinetic() {
        val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
        wiseOldMan.animate(TELEKINETIC_ANIMATION)
        wiseOldMan.graphics(TELEKINETIC_GRAPHIC)
        wiseOldMan.skills.setStaticLevel(Skills.MAGIC, 99)
        wiseOldMan.skills.setLevel(Skills.MAGIC, 99)
        SpellBookManager.SpellBook.MODERN.getSpell(19).cast(wiseOldMan, GroundItemManager.get(Item(Items.BLUE_PARTYHAT_2422).id, base.transform(20, 44, 0), player))
    }

    private fun die(npc: NPC) {
        npc.animator.reset()
        npc.animate(DEATH_ANIMATION)
        npc.graphics(SHOCK_GRAPHIC)
        npc.impactHandler.manualHit(npc, npc.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                lock(player, 1000)
                sendDialogue(player, "You close your eyes and watch the recording...")
                fadeToBlack()
                timedUpdate(3)
            }
            1 -> {
                fadeFromBlack()
                player.isInvisible = true
                teleport(player, 17, 51)
                camera(27, 45, -14, 2, 700, 100)
                timedUpdate(3)
            }
            2 -> {
                val coolMom = getNPC(NPCs.COOL_MOM227_2579)!!
                val marketGuard = getNPC(NPCs.MARKET_GUARD_2571)!!
                coolMom.animate(STEAL_ANIMATION)
                marketGuard.skills.setStaticLevel(Skills.HITPOINTS, 99)
                marketGuard.skills.setLifepoints(99)
                sendChat(marketGuard, "Hey! Get your hands off there!")
                coolMom.locks.lockMovement(1000)
                marketGuard.properties.combatPulse.attack(coolMom)
                timedUpdate(7)
            }
            3 -> {
                move(getNPC(NPCs.PUREPKER895_2575)!!, 20, 45)
                timedUpdate(2)
            }
            4 -> {
                camera(27, 43, -37, -3, 600, 5)
                timedUpdate(2)
            }
            5 -> {
                sendChat(getNPC(NPCs.QUTIEDOLL_2576)!!, "Thx")
                timedUpdate(2)
            }
            6 -> {
                move(getNPC(NPCs.QUTIEDOLL_2576)!!, 20, 42)
                sendChat(getNPC(NPCs.SP34KR_1337_2577)!!, "Yw")
                getNPC(NPCs.PUREPKER895_2575)!!.faceLocation(base.transform(19, 45, 0))
                timedUpdate(2)
            }
            7 -> {
                sendChat(getNPC(NPCs.ELFINLOCKS_2578)!!, "Buying 2k nats - no noob offers")
                timedUpdate(4)
            }
            8 -> {
                sendChat(getNPC(NPCs.SP34KR_1337_2577)!!, "I h4ve n4ts")
                timedUpdate(2)
            }
            9 -> {
                sendChat(getNPC(NPCs.ELFINLOCKS_2578)!!, "You sell?")
                timedUpdate(3)
            }
            10 -> {
                sendChat(getNPC(NPCs.SP34KR_1337_2577)!!, "Tr4de")
                getNPC(NPCs.SP34KR_1337_2577)!!.faceTemporary(getNPC(NPCs.ELFINLOCKS_2578)!!, 5)
                move(getNPC(NPCs.SP34KR_1337_2577)!!, 21, 43)
                timedUpdate(1)
            }
            11 -> {
                camera(23, 49, -37, 0, 500, 3)
                timedUpdate(2)
            }
            12 -> {
                move(getNPC(NPCs.QUTIEDOLL_2576)!!, 20, 49)
                move(getNPC(NPCs.PUREPKER895_2575)!!, 16, 49)
                timedUpdate(1)
            }
            13 -> {
                move(getNPC(NPCs.OLIVIA_2572)!!, 12, 48)
                timedUpdate(7)
            }
            14 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                DoorActionHandler.handleAutowalkDoor(wiseOldMan, getObject(16, 51))
                timedUpdate(1)
            }
            15 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                val qutieDoll = getNPC(NPCs.QUTIEDOLL_2576)!!
                val purePker = getNPC(NPCs.PUREPKER895_2575)!!
                sendChat(qutieDoll, "Huh?")
                qutieDoll.faceTemporary(wiseOldMan, 2)
                sendChat(wiseOldMan, "Please don't block my line of fire!")
                castShock(purePker, wiseOldMan)
                timedUpdate(1)
            }
            16 -> {
                die(getNPC(NPCs.PUREPKER895_2575)!!)
                timedUpdate(1)
            }
            17 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                val olivia = getNPC(NPCs.OLIVIA_2572)!!
                sendChat(olivia, "Hey, what are you doing?")
                wiseOldMan.faceTemporary(olivia, 2)
                GroundItemManager.create(Item(Items.ASHES_592), base.transform(16, 49, 0), player)
                GroundItemManager.create(Item(Items.COINS_995, 60), base.transform(16, 49, 0), player)
                timedUpdate(3)
            }
            18 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                sendChat(wiseOldMan, "Olivia - please go away!")
                wiseOldMan.animate(CAST_ANIMATION)
                wiseOldMan.graphics(TELE_OTHER_CAST)
                timedUpdate(1)
            }
            19 -> {
                val olivia = getNPC(NPCs.OLIVIA_2572)!!
                olivia.animate(TELEPORT_ANIMATION)
                olivia.graphics(TELE_OTHER_TARGET)
                sendChat(olivia, "Eeeek!")
                GroundItemManager.destroy(GroundItemManager.get(Item(Items.ASHES_592).id, base.transform(16, 49, 0), player))
                timedUpdate(2)
            }
            20 -> {
                getNPC(NPCs.OLIVIA_2572)!!.isInvisible = true
                timedUpdate(3)
            }
            21 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                sendChat(wiseOldMan, "Ah, now I can get on with it...")
                wiseOldMan.faceLocation(base.transform(16, 49, 0))
                timedUpdate(1)
            }
            22 -> {
                getNPC(NPCs.WISE_OLD_MAN_2566)!!.animate(PICK_UP_ANIMATION)
                timedUpdate(1)
            }
            23 -> {
                GroundItemManager.destroy(GroundItemManager.get(Item(Items.COINS_995, 60).id, base.transform(16, 49, 0), player))
                timedUpdate(3)
            }
            24 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                castShock(base.transform(16, 46, 0), wiseOldMan)
                timedUpdate(2)
            }
            25 -> {
                val wall = getObject(16, 46)
                if (wall != null) {
                    SceneryBuilder.replace(wall, wall.transform(9151, 0, 10))
                }
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)
                if (wiseOldMan != null) {
                    wiseOldMan.walkingQueue.reset()
                    wiseOldMan.walkingQueue.addPath(base.x + 16, base.y + 46)
                    wiseOldMan.walkingQueue.addPath(base.x + 17, base.y + 46)
                }
                timedUpdate(3)
            }
            26 -> {
                camera(21, 38, -36, 43, 495, 99)
                timedUpdate(1)
            }
            27 -> {
                sendChat(getNPC(NPCs.WISE_OLD_MAN_2566)!!, "Could everyone please not move?")
                timedUpdate(3)
            }
            28 -> {
                val firstBanker = getNPC(NPCs.BANKER_2568)!!
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                sendChat(firstBanker, "Hey - you can't come in here!")
                move(firstBanker, 18, 46)
                firstBanker.faceTemporary(wiseOldMan, 2)
                wiseOldMan.faceTemporary(firstBanker, 2)
                timedUpdate(2)
            }
            29 -> {
                val firstBanker = getNPC(NPCs.BANKER_2568)!!
                val secondBanker = getNPC(NPCs.BANKER_2569)!!
                val lastBanker = getNPC(NPCs.BANKER_2570)!!
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                wiseOldMan.animate(HIT_ANIMATION)
                firstBanker.animate(BLOCK_ANIMATION)
                firstBanker.impactHandler.manualHit(wiseOldMan, firstBanker.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
                sendChat(secondBanker, "Oi!")
                sendChat(lastBanker, "Uh-oh!")
                secondBanker.walkingQueue.reset()
                secondBanker.walkingQueue.addPath(base.x + 17, base.y + 43)
                secondBanker.walkingQueue.addPath(base.x + 16, base.y + 43)
                secondBanker.walkingQueue.addPath(base.x + 16, base.y + 46)
                timedUpdate(6)
            }
            30 -> {
                move(getNPC(NPCs.BANKER_2570)!!, 18, 40)
                val secondBanker = getNPC(NPCs.BANKER_2569)!!
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                secondBanker.faceTemporary(wiseOldMan, 2)
                secondBanker.animate(Animation(422))
                wiseOldMan.animate(Animation(403))
                wiseOldMan.faceTemporary(secondBanker, 2)
                timedUpdate(3)
            }
            31 -> {
                val secondBanker = getNPC(NPCs.BANKER_2569)!!
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                wiseOldMan.animate(Animation(401))
                secondBanker.animate(Animation(435))
                secondBanker.impactHandler.manualHit(wiseOldMan, 11, ImpactHandler.HitsplatType.NORMAL)
                timedUpdate(2)
            }
            32 -> {
                val secondBanker = getNPC(NPCs.BANKER_2569)!!
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                secondBanker.animate(Animation(422))
                sendChat(wiseOldMan, "I do wish you'd just stop it!")
                wiseOldMan.animate(Animation(403))
                timedUpdate(3)
            }
            33 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                wiseOldMan.animate(Animation(401))
                move(getNPC(NPCs.BANKER_2570)!!, 16, 40)
                move(getNPC(NPCs.ELFINLOCKS_2578)!!, 20, 44)
                timedUpdate(2)
            }
            34 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                val lastBanker = getNPC(NPCs.BANKER_2570)!!
                val secondBanker = getNPC(NPCs.BANKER_2569)!!
                wiseOldMan.faceLocation(base.transform(17, 43, 0))
                lastBanker.faceLocation(wiseOldMan.location)
                secondBanker.impactHandler.manualHit(wiseOldMan, secondBanker.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
                timedUpdate(5)
            }
            35 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                wiseOldMan.faceLocation(base.transform(17, 43, 0))
                sendChat(getNPC(NPCs.ELFINLOCKS_2578)!!, "Hey - how'd he get in there?")
                timedUpdate(1)
            }
            36 -> {
                camera(26, 40, -35, 13, 545, 32)
                sendChat(getNPC(NPCs.BANKER_2570)!!, "Help! Help!")
                sendChat(getNPC(NPCs.SP34KR_1337_2577)!!, "Hax!")
                getNPC(NPCs.SP34KR_1337_2577)!!.faceLocation(base.transform(20, 43, 0))
                timedUpdate(4)
            }
            37 -> {
                sendChat(getNPC(NPCs.WISE_OLD_MAN_2566)!!, "My sincerest regrets, dear lady...")
                timedUpdate(2)
            }
            38 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                wiseOldMan.faceTemporary(getNPC(NPCs.ELFINLOCKS_2578)!!, 1)
                castShock(getNPC(NPCs.ELFINLOCKS_2578)!!, wiseOldMan)
                timedUpdate(2)
            }
            39 -> {
                die(getNPC(NPCs.ELFINLOCKS_2578)!!)
                timedUpdate(2)
            }
            40 -> {
                GroundItemManager.create(Item(Items.ASHES_592), base.transform(20, 44, 0), player)
                GroundItemManager.create(Item(Items.BLUE_PARTYHAT_2422), base.transform(20, 44, 0), player)
                timedUpdate(3)
            }
            41 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                sendChat(wiseOldMan, "And also you, sir...")
                GroundItemManager.destroy(GroundItemManager.get(Item(Items.ASHES_592).id, base.transform(20, 44, 0), player))
                timedUpdate(2)
            }
            42 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                castShock(getNPC(NPCs.SP34KR_1337_2577)!!, wiseOldMan)
                move(getNPC(NPCs.QUTIEDOLL_2576)!!, 21, 44)
                timedUpdate(2)
            }
            43 -> {
                getNPC(NPCs.QUTIEDOLL_2576)!!.faceTemporary(getNPC(NPCs.WISE_OLD_MAN_2566)!!, 1)
                timedUpdate(1)
            }
            44 -> {
                sendChat(getNPC(NPCs.QUTIEDOLL_2576)!!, "How you do that?")
                die(getNPC(NPCs.SP34KR_1337_2577)!!)
                timedUpdate(3)
            }
            45 -> {
                GroundItemManager.create(Item(Items.ASHES_592), base.transform(21, 43, 0), player)
                timedUpdate(2)
            }
            46 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                sendChat(wiseOldMan, "Oh dear - another one...")
                move(wiseOldMan, 18, 46)
                wiseOldMan.faceLocation(getNPC(NPCs.QUTIEDOLL_2576)!!.location)
                GroundItemManager.destroy(GroundItemManager.get(Item(Items.ASHES_592).id, base.transform(21, 43, 0), player))
                timedUpdate(1)
            }
            47 -> {
                GroundItemManager.destroy(GroundItemManager.get(Item(Items.ASHES_592).id, base.transform(21, 43, 0), player))
                timedUpdate(1)
            }
            48 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                castShock(getNPC(NPCs.QUTIEDOLL_2576)!!, wiseOldMan)
                timedUpdate(3)
            }
            49 -> {
                die(getNPC(NPCs.QUTIEDOLL_2576)!!)
                timedUpdate(1)
            }
            50 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                GroundItemManager.create(Item(Items.ASHES_592), base.transform(21, 44, 0), player)
                sendChat(wiseOldMan, "Ooh - a party hat?")
                wiseOldMan.faceLocation(base.transform(20, 44, 0))
                timedUpdate(1)
            }
            51 -> {
                GroundItemManager.destroy(GroundItemManager.get(Item(Items.ASHES_592).id, base.transform(21, 44, 0), player))
                castTelekinetic()
                timedUpdate(2)
            }
            52 -> {
                GroundItemManager.destroy(GroundItemManager.get(Item(Items.BLUE_PARTYHAT_2422).id, base.transform(20, 44, 0), player))
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                val olivia = getNPC(NPCs.OLIVIA_2572)!!
                val marketGuard = getNPC(NPCs.MARKET_GUARD_2571)!!
                wiseOldMan.transform(NPCs.WISE_OLD_MAN_2567)
                if (olivia.isActive) {
                    olivia.impactHandler.manualHit(marketGuard, olivia.skills.lifepoints, ImpactHandler.HitsplatType.NORMAL)
                }
                marketGuard.isInvisible = true
                timedUpdate(2)
            }
            53 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                sendChat(wiseOldMan, "Now, my dear...")
                move(wiseOldMan, 18, 42)
                timedUpdate(1)
            }
            54 -> {
                sendChat(getNPC(NPCs.BANKER_2570)!!, "Eeek!")
                timedUpdate(3)
            }
            55 -> {
                sendChat(getNPC(NPCs.WISE_OLD_MAN_2566)!!, "Just give me the money, please.")
                timedUpdate(2)
            }
            56 -> {
                val marketGuard = getNPC(NPCs.MARKET_GUARD_2571)!!
                marketGuard.isInvisible = false
                marketGuard.properties.teleportLocation = base.transform(18, 46, 0)
                timedUpdate(1)
            }
            57 -> {
                camera(25, 42, -20, 0, 400, 4)
                val marketGuard = getNPC(NPCs.MARKET_GUARD_2571)!!
                sendChat(marketGuard, "Old man, you're for da cage!")
                move(marketGuard, 18, 43)
                timedUpdate(1)
            }
            58 -> {
                getNPC(NPCs.WISE_OLD_MAN_2566)!!.faceTemporary(getNPC(NPCs.MARKET_GUARD_2571), 1)
                timedUpdate(1)
            }
            59 -> {
                sendChat(getNPC(NPCs.WISE_OLD_MAN_2566)!!, "Oh, am I?")
                timedUpdate(2)
            }
            60 -> {
                sendChat(getNPC(NPCs.WISE_OLD_MAN_2566)!!, "Ahh!")
                timedUpdate(2)
            }
            61 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                wiseOldMan.animate(THUNDER_ANIMATION)
                wiseOldMan.graphics(PURPLE_GRAPHIC)
                timedUpdate(3)
            }
            62 -> {
                val marketGuard = getNPC(NPCs.MARKET_GUARD_2571)!!
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                marketGuard.graphics(THUNDER_GRAPHIC)
                marketGuard.impactHandler.manualHit(wiseOldMan, 40, ImpactHandler.HitsplatType.NORMAL)
                sendChat(marketGuard, "Aaargh!")
                timedUpdate(2)
            }
            63 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                sendChat(wiseOldMan, "Yah!")
                wiseOldMan.animate(WISE_JUMP)
                timedUpdate(1)
            }
            64 -> {
                getNPC(NPCs.MARKET_GUARD_2571)!!.animate(GUARD_JUMP)
                timedUpdate(3)
            }
            65 -> {
                getNPC(NPCs.MARKET_GUARD_2571)!!.clear()
                camera(24, 42, -20, 0, 400, 4)
                timedUpdate(1)
            }
            66 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                sendChat(wiseOldMan, "And YOU can stop spying on me!")
                wiseOldMan.faceLocation(base.transform(24, 42, 0))
                timedUpdate(2)
            }
            67 -> {
                val wiseOldMan = getNPC(NPCs.WISE_OLD_MAN_2566)!!
                wiseOldMan.animate(STOMP_ANIM)
                val projectile = Projectile.create(wiseOldMan, null, 434, 50, 130, 41, 140, 0, 0)
                projectile.endLocation = base.transform(24, 42, 0)
                projectile.send()
                timedUpdate(3)
            }
            68 -> {
                player.interfaceManager.openOverlay(CRACKED)
                timedUpdate(2)
            }
            69 -> end {
                player.isInvisible = false
                setAttribute(player, "/save:$HAS_SEEN_RECORDING", true)
                sendDialogue(player, "End of recording.")
            }
        }
    }
}

class DBRCutsceneListeners : InteractionListener {
    override fun defineListeners() {
        on(Items.BLUE_PARTYHAT_2422, IntType.GROUNDITEM, "take") { player, _ ->
            sendMessage(player, "Nice try ;)")
            return@on true
        }
    }
}
