package rs09.game.content.quest.members.elementalworkshop

import api.Commands
import api.addItem
import api.setAttribute
import api.setVarbit
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.Vars
import rs09.game.system.command.Privilege

/**
 * Elemental Workshop I
 *
 *  Original Development Team:
 *      Developer: Dylan C
 *      Graphics: Tom W
 *      Quality Assurance: Andrew C
 *      Audio: Ian T
 *
 *  RS2 Rework Team:
 *      Developer: Dylan C
 *      Graphics: Tom W
 *      Quality Assurance: Andrew C
 *      QuestHelp: Rob M
 *
 * 2009scape adaptation:
 *   @author Woah, with love
 */
@Initializable
class ElementalWorkshopQuest : Quest("Elemental Workshop I", 52, 51, 1), Commands {

    override fun newInstance(`object`: Any?): Quest {
        return this
    }

    override fun drawJournal(player: Player?, stage: Int) {
        super.drawJournal(player, stage)
        player ?: return
        var line = 11
        if (stage == 0) {
            line(player, "I can start this quest by reading a", line++)
            line(player, "!!book?? found in !!Seers village??.", line++)
            line++
            line(player, "Minimum requirements:", line++)
            line(player, if (player.skills.getStaticLevel(Skills.MINING) >= 20) "---Level 20 Mining/--" else "!!Level 20 Mining??", line++)
            line(player, if (player.skills.getStaticLevel(Skills.SMITHING) >= 20) "---Level 20 Smithing/--" else "!!Level 20 Smithing??", line++)
            line(player, if (player.skills.getStaticLevel(Skills.CRAFTING) >= 20) "---Level 20 Crafting/--" else "!!Level 20 Crafting??", line++)
        } else {
            // Player read through book on bookshelf
            if (stage < 100) {
                if (stage >= 1) {
                    line(player, "---I have found a battered book in a house in Seers Village./--", line++)
                    line(player, "---It tells of magic ore and a workshop created to fashion it./--", line++)
                    line++
                    if (stage <= 2) {
                        line(player, "Where is the workshop and how do I get in?", line++)
                    }
                }

                if (stage >= 3) {
                    line(player, "---Cutting open the spine of the book with a knife,/--", line++)
                    line(player, "---I found a key hidden under the leather binding./--", line++)
                    line++
                    if (stage <= 4) {
                        line(player, "Where is the workshop and how do I get in?", line++)
                    }
                }

                if (stage >= 5) {
                    line(player, "---I have found a secret door in the Seers Village smithy/--", line++)
                    line++
                    line(player, "---Where is the workshop and how do I get in?/--", line++)
                    line++
                }

                // Player climbed down staircase
                if (stage == 7) {
                    line(player, "There is obviously lots to do here.", line++)
                }
            } else {
                line(player, "---I have found a battered book in a house in Seers Village./--", line++)
                line(player, "---It tells of magic ore and a workshop created to fashion it./--", line++)
                line++
                line(player, "---After fixing up the old workshop machinery, collecting ore", line++)
                line(player, "---and smelting it I was able to create an Elemental Shield./--", line++)
                line++
                line(player, "!!QUEST COMPLETE!??", line++)
            }
        }
    }

    override fun finish(player: Player?) {
        super.finish(player)
        player ?: return
        player.packetDispatch.sendString("1 Quest Point,", 277, 8 + 2)
        player.packetDispatch.sendString("5,000 Crafting XP", 277, 9 + 2)
        player.packetDispatch.sendString("5,000 Smithing XP", 277, 10 + 2)
        player.packetDispatch.sendString("The ability to make", 277, 11 + 2)
        player.packetDispatch.sendString("elemental shields.", 277, 12 + 2)
        player.packetDispatch.sendItemZoomOnInterface(Items.ELEMENTAL_SHIELD_2890, 235, 277, 3 + 2)
        player.skills.addExperience(Skills.CRAFTING, 5000.0)
        player.skills.addExperience(Skills.SMITHING, 5000.0)
        player.questRepository.syncronizeTab(player)
    }

    override fun getConfig(player: Player?, stage: Int): IntArray {
        if (stage >= 100) return intArrayOf(Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, 1048576)
        if (stage > 0) return intArrayOf(Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, 3)
        else return intArrayOf(Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, 0)
    }

    override fun defineCommands() {
        define("resetew", Privilege.ADMIN) { player, _ ->
            setAttribute(player, "/save:ew1:got_needle", false)
            setAttribute(player, "/save:ew1:got_leather", false)
            setAttribute(player, "/save:ew1:bellows_fixed", false)
            player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(52), 0)
            player.varpManager.get(Vars.VARP_QUEST_ELEMENTAL_WORKSHOP).clearBitRange(0, 31)
            player.varpManager.get(Vars.VARP_QUEST_ELEMENTAL_WORKSHOP).send(player)
            player.teleport(Location.create(2715, 3481, 0))
            player.inventory.clear()
            addItem(player, Items.KNIFE_946)
            addItem(player, Items.BRONZE_PICKAXE_1265)
            addItem(player, Items.NEEDLE_1733)
            addItem(player, Items.THREAD_1734)
            addItem(player, Items.LEATHER_1741)
            addItem(player, Items.HAMMER_2347)
            addItem(player, Items.COAL_453, 4)
        }
        define("readyew", Privilege.ADMIN) { player, _ ->
            val enabled = 1
            setAttribute(player, "/save:ew1:got_needle", true)
            setAttribute(player, "/save:ew1:got_leather", true)
            setAttribute(player, "/save:ew1:bellows_fixed", true)
            player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(52), 95)
            setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, EWUtils.BELLOWS_STATE, enabled, true)
            setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, EWUtils.FURNACE_STATE, enabled, true)
            setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, EWUtils.WATER_WHEEL_STATE, enabled, true)
            setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, EWUtils.RIGHT_WATER_CONTROL_STATE, enabled, true)
            setVarbit(player, Vars.VARP_QUEST_ELEMENTAL_WORKSHOP, EWUtils.LEFT_WATER_CONTROL_STATE, enabled, true)
        }
    }
}