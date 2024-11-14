package content.region.asgarnia.falador.quest.recruitmentdrive

import content.region.asgarnia.burthorpe.quest.deathplateau.DeathPlateau
import core.ServerConstants
import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FacialExpression
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState
import org.rs09.consts.*

class RecruitmentDriveListeners : InteractionListener {
    companion object {

        enum class Stages(val npc: Int, val startLocation: Location, val startWalkLocation: Location, val quitPortal: Int, val successDoor: Int) {
            SIR_SPISHYUS(NPCs.SIR_SPISHYUS_2282, Location(2490, 4972), Location(2489, 4972), Scenery.PORTAL_7272, Scenery.DOOR_7274),
            LADY_TABLE(NPCs.LADY_TABLE_2283, Location(2460, 4979), Location(2459, 4979), Scenery.PORTAL_7288, Scenery.DOOR_7302),
            SIR_KUAM_FERENTSE(NPCs.SIR_KUAM_FERENTSE_2284, Location(2455, 4964), Location(2456, 4964), Scenery.PORTAL_7315, Scenery.DOOR_7317),
            SIR_TINLEY(NPCs.SIR_TINLEY_2286, Location(2471, 4956), Location(2472, 4956), Scenery.PORTAL_7318, Scenery.DOOR_7320),
            SIR_REN_ITCHOOD(NPCs.SIR_REN_ITCHOOD_2287, Location(2439, 4956), Location(2440, 4956), Scenery.PORTAL_7321, Scenery.DOOR_7323),
            MISS_CHEEVERS(NPCs.MISS_CHEEVERS_2288, Location(2467, 4940), Location(2468, 4940), Scenery.PORTAL_7324, Scenery.DOOR_7326),
            MS_HYNN_TERPRETT(NPCs.MS_HYNN_TERPRETT_2289, Location(2451, 4935), Location(2451, 4936), Scenery.PORTAL_7352, Scenery.DOOR_7354);

            companion object {
                @JvmField
                val indexMap = Stages.values().associateBy { it.ordinal }
                val indexArray = Stages.indexMap.keys.map { it }
                val quitPortalArray = Stages.indexMap.values.map { it.quitPortal }.toIntArray()
                val successDoorArray = Stages.indexMap.values.map { it.successDoor }.toIntArray()
            }
        }

        fun shuffleStages(player: Player) {
            // Obtain an array to shuffle. Must be at least [5] long.
            val stagesArrayToShuffle = intArrayOf(0,1,2,3,4,5,6) // Stages.indexArray.toIntArray()
            stagesArrayToShuffle.shuffle()
            setAttribute(player, RecruitmentDrive.attributeStage1, stagesArrayToShuffle[0])
            setAttribute(player, RecruitmentDrive.attributeStage2, stagesArrayToShuffle[1])
            setAttribute(player, RecruitmentDrive.attributeStage3, stagesArrayToShuffle[2])
            setAttribute(player, RecruitmentDrive.attributeStage4, stagesArrayToShuffle[3])
            setAttribute(player, RecruitmentDrive.attributeStage5, stagesArrayToShuffle[4])
            setAttribute(player, RecruitmentDrive.attributeCurrentStage, 0)
            removeAttribute(player, RecruitmentDrive.attributeStagePassFailState)
        }

        fun callStartingDialogues (player: Player, npc: Int) {
            when (npc) {
                NPCs.SIR_SPISHYUS_2282 -> openDialogue(player, SirSpishyusDialogueFile(1), NPC(npc))
                NPCs.LADY_TABLE_2283 -> openDialogue(player, LadyTableDialogueFile(1), NPC(npc))
                NPCs.SIR_KUAM_FERENTSE_2284 -> openDialogue(player, SirKuamFerentseDialogueFile(1), NPC(npc))
                NPCs.SIR_TINLEY_2286 -> openDialogue(player, SirTinleyDialogueFile(1), NPC(npc))
                NPCs.SIR_REN_ITCHOOD_2287 -> openDialogue(player, SirRenItchwoodDialogueFile(1), NPC(npc))
                NPCs.MISS_CHEEVERS_2288 -> openDialogue(player, MissCheeversDialogueFile(1), NPC(npc))
                NPCs.MS_HYNN_TERPRETT_2289 -> openDialogue(player, MsHynnTerprettDialogueFile(1), NPC(npc))
            }
        }
    }

    override fun defineListeners() {

        on(Stages.quitPortalArray, IntType.SCENERY, "use") { player, node ->
            FailTestCutscene(player).start()
            return@on true
        }

        on(Stages.successDoorArray, IntType.SCENERY, "open") { player, node ->
            // This is specially for Miss Cheevers
            if (inInventory(player, Items.BRONZE_KEY_5585)) {
                sendMessage(player, "You use the duplicate key you made to unlock the door.")
                setAttribute(player, RecruitmentDrive.attributeStagePassFailState, 1)
            }
            // Success Door
            if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 1) {
                removeAttribute(player, RecruitmentDrive.attributeStagePassFailState)
                setAttribute(player, RecruitmentDrive.attributeCurrentStage, getAttribute(player, RecruitmentDrive.attributeCurrentStage, 0) + 1)
                DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
                val currentLevel = getAttribute(player, RecruitmentDrive.attributeCurrentStage, 0)
                if (currentLevel >= 5) {
                    CompleteTestCutscene(player).start()
                    return@on true
                }
                val currentStage = getAttribute(player, RecruitmentDrive.attributeStageArray[currentLevel], 0)
                val currentStageEnum = Stages.indexMap[currentStage]!!
                closeDialogue(player)

                // This is specifically for Sir Spishyus to reset the fox, chicken, grain
                SirSpishyusRoomListeners.resetStage(player)

                queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            player.inventory.clear()
                            player.equipment.clear()
                            openOverlay(player, Components.FADE_TO_BLACK_120)
                            return@queueScript delayScript(player, 6)
                        }
                        1 -> {
                            teleport(player, currentStageEnum.startLocation)
                            return@queueScript delayScript(player, 2)
                        }
                        2 -> {
                            openOverlay(player, Components.FADE_FROM_BLACK_170)
                            return@queueScript delayScript(player, 2)
                        }
                        3 -> {
                            forceWalk(player, currentStageEnum.startWalkLocation, "dumb")
                            return@queueScript delayScript(player, 2)
                        }
                        4 -> {
                            callStartingDialogues(player, currentStageEnum.npc)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            } else {
                if(node.id == Scenery.DOOR_7323) {
                    // This is specifically for SirRenItchwood
                    openInterface(player, Components.RD_COMBOLOCK_285)
                } else {
                    sendMessage(player, "You have not completed this room's puzzle yet.")
                }
            }
            return@on true
        }
    }

    /** Starting Recruitment Drive test cutscene */
    class StartTestCutscene(player: Player) : Cutscene(player) {
        override fun setup() {
            loadRegion(9805)
            val currentStage = getAttribute(player, RecruitmentDrive.attributeStageArray[0], 0)
            setExit(Stages.indexMap[currentStage]!!.startLocation)
        }

        override fun runStage(stage: Int) {
            when (stage) {
                0 -> {
                    fadeToBlack()
                    PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                    timedUpdate(6)
                }
                1 -> {
                    dialogueLinesUpdate(NPCs.SIR_TIFFY_CASHIEN_2290, FacialExpression.HAPPY, "Here we go!", "Mind your head!")
                    timedUpdate(3)
                }
                2 -> {
                    dialogueLinesUpdate(NPCs.SIR_TIFFY_CASHIEN_2290, FacialExpression.HAPPY, "Oops. Ignore the smell!", "Nearly there!")
                    timedUpdate(3)
                }
                3 -> {
                    dialogueLinesUpdate(NPCs.SIR_TIFFY_CASHIEN_2290, FacialExpression.HAPPY, "And...", "Here we are!", "Best of luck!")
                    timedUpdate(3)
                }
                4 -> {
                    player.inventory.clear()
                    player.equipment.clear()
                    PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                    dialogueClose()
                    endWithoutFade {
                        val currentStage = getAttribute(player, RecruitmentDrive.attributeStageArray[0], 0)
                        val firstStage = Stages.indexMap[currentStage]!!

                        // This is specifically for Sir Spishyus to reset the fox, chicken, grain
                        SirSpishyusRoomListeners.resetStage(player)

                        queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                            when (stage) {
                                0 -> {
                                    fadeFromBlack()
                                    return@queueScript delayScript(player, 2)
                                }
                                1 -> {
                                    forceWalk(player, firstStage.startWalkLocation, "dumb")
                                    return@queueScript delayScript(player, 2)
                                }
                                2 -> {
                                    callStartingDialogues(player, firstStage.npc)
                                    return@queueScript stopExecuting(player)
                                }
                                else -> return@queueScript stopExecuting(player)
                            }
                        }

                    }
                }
            }
        }
    }

    /** Failed Recruitment Drive test cutscene */
    class FailTestCutscene(player: Player) : Cutscene(player) {
        override fun setup() {
            loadRegion(9805)
            setExit(Location(2997, 3374))
        }

        override fun runStage(stage: Int) {
            when (stage) {
                0 -> {
                    closeDialogue(player)
                    fadeToBlack()
                    PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                    timedUpdate(6)
                }
                1 -> {
                    var clearBoss = getAttribute(player, SirKuamFerentseDialogueFile.attributeGeneratedSirLeye, NPC(0))
                    if (clearBoss.id != 0) {
                        clearBoss.clear()
                    }
                    player.inventory.clear()
                    player.equipment.clear()
                    queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                        when (stage) {
                            0 -> {
                                fadeFromBlack()
                                return@queueScript delayScript(player, 2)
                            }
                            1 -> {
                                PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                                openDialogue(player, SirTiffyCashienFailedDialogueFile(), NPC(NPCs.SIR_TIFFY_CASHIEN_2290))
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                    endWithoutFade {
                        face(player, Location(2997, 3373))
                        fadeFromBlack()
                    }
                }
            }
        }
    }

    /** Complete Recruitment Drive test cutscene */
    class CompleteTestCutscene(player: Player) : Cutscene(player) {
        override fun setup() {
            loadRegion(9805)
            setExit(Location(2996, 3375))
        }

        override fun runStage(stage: Int) {
            when (stage) {
                0 -> {
                    if (getQuestStage(player, RecruitmentDrive.questName) == 2) {
                        setQuestStage(player, RecruitmentDrive.questName, 3)
                    }
                    closeDialogue(player)
                    fadeToBlack()
                    PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                    timedUpdate(6)
                }
                1 -> {
                    player.inventory.clear()
                    player.equipment.clear()
                    PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                    queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
                        when (stage) {
                            0 -> {
                                fadeFromBlack()
                                return@queueScript delayScript(player, 2)
                            }
                            1 -> {
                                openDialogue(player, SirTiffyCashienDialogueFile(), NPC(NPCs.SIR_TIFFY_CASHIEN_2290))
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                    endWithoutFade {
                        face(player, Location(2997, 3373))
                        fadeFromBlack()
                    }
                }
            }
        }
    }

    class LogoutRecruitmentDrive : MapArea {
        override fun defineAreaBorders(): Array<ZoneBorders> {
            return arrayOf(getRegionBorders(9805))
        }

        override fun getRestrictions(): Array<ZoneRestriction> {
            return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS)
        }

        override fun areaLeave(entity: Entity, logout: Boolean) {
            if (entity is Player) {
                // This is specifically for Sir Spishyus to reset the fox, chicken, grain
                SirSpishyusRoomListeners.resetStage(entity)
                // Clear inventory whenever you leave the recruitment drive area
                entity.inventory.clear()
                entity.equipment.clear()
                // Restore player normal tabs on leave
                entity.interfaceManager.openDefaultTabs()
                // Teleport you out if you log out. You should do this in one sitting.
                if (logout) {
                    PacketRepository.send(MinimapState::class.java, MinimapStateContext(entity, 0))
                    teleport(entity, Location(2996, 3375))
                }
            }
        }
    }
}