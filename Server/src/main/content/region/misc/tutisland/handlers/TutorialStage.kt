package content.region.misc.tutisland.handlers

import core.api.*
import core.game.component.Component
import content.region.misc.tutisland.handlers.iface.CharacterDesign
import core.game.node.Node
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.Components
import core.api.Event
import core.game.world.GameWorld.Pulser
import core.game.world.repository.Repository
import org.rs09.consts.NPCs

/**
 * Loads stage-relevant tutorial data
 * @author Ceikry
 * @author Player Name
 */
object TutorialStage {
    /**
     * Performs stage actions for the player
     * @param player the player to perform the actions on
     * @param stage the stage to load
     */
    fun load(player: Player, stage: Int, login: Boolean = false) {
        if (login) {
            player.hook(Event.ButtonClicked, TutorialButtonReceiver)
            player.hook(Event.Interacted, TutorialInteractionReceiver)
            player.hook(Event.ResourceProduced, TutorialResourceReceiver)
            player.hook(Event.UsedWith, TutorialUseWithReceiver)
            player.hook(Event.FireLit, TutorialFireReceiver)
            player.hook(Event.NPCKilled, TutorialKillReceiver)
            player.hook(Event.DialogueClosed, TutorialDialogPreserver)
            openOverlay(player, Components.TUTORIAL_PROGRESS_371)
            player.packetDispatch.sendInterfaceConfig(371, 4, true)
        }
        updateProgressBar(player)

        when(stage) {
            0 -> {
                lock(player, 10)
                teleport(player, Location.create(3094, 3107, 0))
                hideTabs(player, login)
                CharacterDesign.open(player)
                // We have two dialogs in this stage. This is awkward, but not a problem.
                // The first dialog is impossible to close in any way, so we can send it here manually.
                // The second dialog could be lost by e.g. talking to an npc, so this dialog gets implemented in
                // TutorialDialogs.kt, which has the hook for restoring it if it does get lost.
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "",
                        "",
                        "Getting started",
                        "Please take a moment to design your character.",
                        ""
                    )
                ).also {
                    runTask(player, 10) {
                        sendStageDialog(player, stage)
                    }
                }
            }
            1 -> {
                hideTabs(player, login)
                player.interfaceManager.openTab(Component(Components.OPTIONS_261))
                setVarbit(player, 3756, 12)
                removeHintIcon(player)
                sendStageDialog(player, stage)
            }
            2 -> {
                setVarbit(player, 3756, 0)
                hideTabs(player, login)
                registerHintIcon(player, Repository.findNPC(NPCs.RUNESCAPE_GUIDE_945)!!)
                sendStageDialog(player, stage)
            }
            3 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3098, 3107, 0), 125)
                sendStageDialog(player, stage)
            }
            4 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.SURVIVAL_EXPERT_943)!!)
                sendStageDialog(player, stage)
            }
            5 -> {
                hideTabs(player, login)
                player.interfaceManager.openTab(Component(Components.INVENTORY_149))
                setVarbit(player, 3756, 4)
                removeHintIcon(player)
                sendStageDialog(player, stage)
            }
            6 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 4)
                sendStageDialog(player, stage)
            }
            7 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                sendStageDialog(player, stage)
            }
            8 -> {
                hideTabs(player, login)
                sendStageDialog(player, stage)
            }
            9 -> {
                hideTabs(player, login)
                sendStageDialog(player, stage)
            }
            10 -> {
                hideTabs(player, login)
                player.interfaceManager.openTab(Component(Components.STATS_320))
                setVarbit(player, 3756, 2)
                sendStageDialog(player, stage)
            }
            11 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 2)
                registerHintIcon(player, Repository.findNPC(NPCs.SURVIVAL_EXPERT_943)!!)
                sendStageDialog(player, stage)
            }
            12 -> {
                hideTabs(player, login)
                setVarp(player, 406, 2)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.FISHING_SPOT_952)!!)
                sendStageDialog(player, stage)
            }
            13 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                sendStageDialog(player, stage)
            }
            14 -> {
                hideTabs(player, login)
                sendStageDialog(player, stage)
            }
            15 -> {
                hideTabs(player, login)
                sendStageDialog(player, stage)
            }
            16 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3089, 3091, 0), 75)
                sendStageDialog(player, stage)
            }
            17 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3079, 3084, 0), 125)
                sendStageDialog(player, stage)
            }
            18 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.MASTER_CHEF_942)!!)
                sendStageDialog(player, stage)
            }
            19 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                sendStageDialog(player, stage)
            }
            20 -> {
                hideTabs(player, login)
                registerHintIcon(player, Location.create(3076, 3081, 0), 75)
                sendStageDialog(player, stage)
            }
            21 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(Components.MUSIC_V3_187))
                setVarbit(player, 3756, 14)
                sendStageDialog(player, stage)
            }
            22 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 0)
                registerHintIcon(player, Location.create(3073, 3090, 0), 125)
                sendStageDialog(player, stage)
            }
            23 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 13)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(Components.EMOTES_464))
                stopWalk(player)
                player.locks.lockMovement(100000)
                sendStageDialog(player, stage)
            }
            24 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 0)
                player.locks.lockMovement(100000)
                sendStageDialog(player, stage)
            }
            25 -> {
                hideTabs(player, login)
                sendStageDialog(player, stage)
            }
            26 -> {
                hideTabs(player, login)
                registerHintIcon(player, Repository.findNPC(NPCs.QUEST_GUIDE_949)!!)
                player.locks.unlockMovement()
                sendStageDialog(player, stage)
            }
            27 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.QUEST_GUIDE_949)!!)
                sendStageDialog(player, stage)
            }
            28 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 0)
                sendStageDialog(player, stage)
            }
            29 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                setVarbit(player, 3756, 0)
                registerHintIcon(player, Location.create(3088, 3119, 0), 15)
                sendStageDialog(player, stage)
            }
            30 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                setVarbit(player, 3756, 0)
                registerHintIcon(player, Repository.findNPC(NPCs.MINING_INSTRUCTOR_948)!!)
                sendStageDialog(player, stage)
            }
            31 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3076, 9504, 0), 50)
                sendStageDialog(player, stage)
            }
            32 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                sendStageDialog(player, stage)
                Pulser.submit(object : Pulse(3) {
                    override fun pulse(): Boolean {
                        setAttribute(player, "tutorial:stage", 33)
                        load(player, 33)
                        return true
                    }
                })
            }
            33 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3086, 9501, 0), 50)
                sendStageDialog(player, stage)
            }
            34 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.MINING_INSTRUCTOR_948)!!)
                sendStageDialog(player, stage)
            }
            35 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3076, 9504), 50)
                sendStageDialog(player, stage)
            }
            36 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                sendStageDialog(player, stage)
            }
            37 -> {
                hideTabs(player, login)
                registerHintIcon(player, Location.create(3086, 9501), 50)
                sendStageDialog(player, stage)
            }
            38 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3079, 9496), 75)
                sendStageDialog(player, stage)
            }
            39 -> {
                sendStageDialog(player, stage)
            }
            40 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.MINING_INSTRUCTOR_948)!!)
                sendStageDialog(player, stage)
            }
            41 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3083, 9499), 50)
                sendStageDialog(player, stage)
            }
            42 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                sendStageDialog(player, stage)
            }
            43 -> {
                hideTabs(player, login)
                registerHintIcon(player, Location.create(3095, 9502), 75)
                sendStageDialog(player, stage)
            }
            44 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.COMBAT_INSTRUCTOR_944)!!)
                sendStageDialog(player, stage)
            }
            45 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                runTask(player, 10) {
                    // this part needs sendStageDialog because you could just be logging in here
                    sendStageDialog(player, stage)
                }.also {
                    // for this part, you are locked into the interface so we don't need sendStageDialog here
                    hideTabs(player, login)
                    removeHintIcon(player)
                    player.interfaceManager.openTab(Component(Components.WORNITEMS_387))
                    setVarbit(player, 3756, 5)
                    Component.setUnclosable(
                        player,
                        player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                            "Worn interface",
                            "You can see what items you are wearing in the worn equipment",
                            "to the left of the screen, with their combined statistics on the",
                            "right. Let's add something. Left click your dagger to 'wield' it.",
                            ""
                        )
                    )
                }
            }
            46 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 0)
                sendStageDialog(player, stage)
            }
            47 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.COMBAT_INSTRUCTOR_944)!!)
                sendStageDialog(player, stage)
            }
            48 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                sendStageDialog(player, stage)
            }
            49 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 1)
                var wepInter = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
                if (wepInter == null) {
                    wepInter = WeaponInterface(player)
                    player.addExtension(WeaponInterface::class.java, wepInter)
                }
                player.interfaceManager.openTab(wepInter)
                sendStageDialog(player, stage)
            }
            50 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 0)
                registerHintIcon(player, Location.create(3111,9518,0), 75)
                sendStageDialog(player, stage)
            }
            51 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                //FIXME: add a hint arrow over the rat closest to you that is not in combat with somebody else. https://www.youtube.com/watch?v=FGQ2BZrJIug. The below should work but doesn't.
                registerHintIcon(player, Repository.findNPC(NPCs.GIANT_RAT_86)!!)
                sendStageDialog(player, stage)
            }
            52 -> {
                hideTabs(player, login)
                //FIXME: add a hint arrow over the rat you're in combat with (also in the ranging part btw). https://www.youtube.com/watch?v=FGQ2BZrJIug
                sendStageDialog(player, stage)
            }
            53 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.COMBAT_INSTRUCTOR_944)!!)
                sendStageDialog(player, stage)
            }
            54 -> {
                hideTabs(player, login)
                sendStageDialog(player, stage)
            }
            55 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3111,9526), 50)
                sendStageDialog(player, stage)
            }
            56 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3122,3124), 50)
                sendStageDialog(player, stage)
            }
            57 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3125, 3124), 75)
                sendStageDialog(player, stage)
            }
            58 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.FINANCIAL_ADVISOR_947)!!)
                sendStageDialog(player, stage)
            }
            59 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3130, 3124, 0), 75)
                sendStageDialog(player, stage)
            }
            60 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.BROTHER_BRACE_954)!!)
                sendStageDialog(player, stage)
            }
            61 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(Components.PRAYER_271))
                setVarbit(player, 3756, 6)
                sendStageDialog(player, stage)
            }
            62 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.BROTHER_BRACE_954)!!)
                sendStageDialog(player, stage)
            }
            63 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(Components.FRIENDS2_550))
                setVarbit(player, 3756, 9)
                sendStageDialog(player, stage)
            }
            64 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 10)
                player.interfaceManager.openTab(Component(Components.IGNORE2_551))
                player.interfaceManager.openTab(Component(Components.CLANJOIN_589))
                sendStageDialog(player, stage)
            }
            65 -> {
                hideTabs(player, login)
                setVarbit(player, 3756, 0)
                registerHintIcon(player, Repository.findNPC(NPCs.BROTHER_BRACE_954)!!)
                sendStageDialog(player, stage)
            }
            66 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3122,3102), 75)
                sendStageDialog(player, stage)
            }
            67 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(NPCs.MAGIC_INSTRUCTOR_946)!!)
                sendStageDialog(player, stage)
            }
            68 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(player.spellBookManager.spellBook))
                setVarbit(player, 3756, 7)
                sendStageDialog(player, stage)
            }
            69 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                setVarbit(player, 3756, 0)
                registerHintIcon(player, Repository.findNPC(NPCs.MAGIC_INSTRUCTOR_946)!!)
                sendStageDialog(player, stage)
            }
            70 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                //FIXME: as with the rats, the below should work, but doesn't
                registerHintIcon(player, Repository.findNPC(NPCs.CHICKEN_41)!!)
                sendStageDialog(player, stage)
            }
            71 -> {
                removeHintIcon(player)
                player.interfaceManager.restoreTabs()
                registerHintIcon(player, Repository.findNPC(NPCs.MAGIC_INSTRUCTOR_946)!!)
                sendStageDialog(player, stage)
            }
        }
    }

    @JvmStatic
    fun hideTabs(player: Player, login: Boolean)
    {
        val stage = getAttribute(player, "tutorial:stage", 0)
        if(login && player.interfaceManager.tabs.isNotEmpty())
            player.interfaceManager.removeTabs(*(0..13).toIntArray())

        if(stage > 2)
            player.interfaceManager.openTab(Component(Components.OPTIONS_261))
        if(stage > 5)
            player.interfaceManager.openTab(Component(Components.INVENTORY_149))
        if(stage > 10)
            player.interfaceManager.openTab(Component(Components.STATS_320))
        if(stage > 21)
            player.interfaceManager.openTab(Component(Components.MUSIC_V3_187))
        if(stage > 23)
            player.interfaceManager.openTab(Component(Components.EMOTES_464))
        if(stage > 28)
            player.interfaceManager.openTab(Component(Components.QUESTJOURNAL_V2_274))
        if(stage > 45)
            player.interfaceManager.openTab(Component(Components.WORNITEMS_387))
        if(stage > 46){
            var wepInter = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
            if (wepInter == null) {
                wepInter = WeaponInterface(player)
                player.addExtension(WeaponInterface::class.java, wepInter)
            }
        }
        if(stage > 61)
            player.interfaceManager.openTab(Component(Components.PRAYER_271))
        if(stage > 63)
            player.interfaceManager.openTab(Component(Components.FRIENDS2_550))
        if(stage > 64){
            player.interfaceManager.openTab(Component(Components.IGNORE2_551))
            player.interfaceManager.openTab(Component(Components.CLANJOIN_589))
        }
        if(stage > 68)
            player.interfaceManager.openTab(Component(player.spellBookManager.spellBook))
    }

    private fun updateProgressBar(player: Player)
    {
        val stage = getAttribute(player, "tutorial:stage", 0)
        val percent = if(stage == 0) 0 else ((stage.toDouble() / 71.0) * 100.0).toInt()
        val barPercent = if(stage == 0) 0 else (((percent.toDouble() / 100.0) * 20.0).toInt() + 1)
        setVarp(player, 406, barPercent)
        setInterfaceText(player, "$percent% Done", 371, 1)
    }

    fun removeHintIcon(player: Player) {
        val slot = player.getAttribute("tutorial:hinticon", -1)
        if (slot < 0 || slot >= HintIconManager.MAXIMUM_SIZE) {
            return
        }
        player.removeAttribute("tutorial:hinticon")
        HintIconManager.removeHintIcon(player, slot)
    }

    private fun registerHintIcon(player: Player, node: Node)
    {
        setAttribute(player, "tutorial:hinticon", HintIconManager.registerHintIcon(player, node))
    }

    private fun registerHintIcon(player: Player, location: Location, height: Int)
    {
        setAttribute(player, "tutorial:hinticon", HintIconManager.registerHintIcon(player, location, 1, -1, player.hintIconManager.freeSlot(), height, 3))
    }
}
