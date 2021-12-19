package rs09.game.interaction.inter

import api.*
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.plugin.Plugin
import rs09.game.system.SystemLogger

/**
 * Represents the experience interface.
 * @author Ceikry
 */
@Initializable
class ExperienceInterface() : ComponentPlugin() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any?> {
        ComponentDefinition.put(COMPONENT_ID, this)
        return this
    }

    override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        if(button == 2){
            val confirmedSkill = player.getAttribute("exp_interface:skill",-1)
            if(confirmedSkill == -1){
                player.sendMessage("You must first select a skill.")
            } else {
               player.removeAttribute("exp_interface:skill")
                when(confirmedSkill){
                    Skills.HERBLORE -> if(!checkHerblore(player)) player.sendMessage("You need to have completed Druidic Ritual for this.").also { return true }
                    Skills.RUNECRAFTING -> if(!checkRunecrafting(player)) player.sendMessage("You need to have completed Rune Mysteries for this.").also { return true }
                    Skills.SUMMONING -> if(!checkSummoning(player)) player.sendMessage("You need to have completed Wolf Whistle for this.").also { return true }
                }
                val caller = player.attributes["caller"]
                caller ?: return true
                if(caller is Plugin<*>)
                    caller.handleSelectionCallback(confirmedSkill, player)
                else (caller as (Int,Player) -> Unit).invoke(confirmedSkill,player)
                playAudio(player, SOUND)
                closeInterface(player)
            }
        } else {
            val skill = when (button) {
                29 -> Skills.ATTACK
                30 -> Skills.STRENGTH
                31 -> Skills.DEFENCE
                32 -> Skills.RANGE
                35 -> Skills.MAGIC
                39 -> Skills.CRAFTING
                34 -> Skills.HITPOINTS
                33 -> Skills.PRAYER
                36 -> Skills.AGILITY
                37 -> Skills.HERBLORE
                38 -> Skills.THIEVING
                43 -> Skills.FISHING
                47 -> Skills.RUNECRAFTING
                48 -> Skills.SLAYER
                50 -> Skills.FARMING
                41 -> Skills.MINING
                42 -> Skills.SMITHING
                49 -> Skills.HUNTER
                52 -> Skills.SUMMONING
                45 -> Skills.COOKING
                44 -> Skills.FIREMAKING
                46 -> Skills.WOODCUTTING
                40 -> Skills.FLETCHING
                51 -> Skills.CONSTRUCTION
                else -> Skills.SLAYER.also { SystemLogger.logWarn("EXP_INTERFACE: Invalid SKILL CHOICE BUTTON: $button") }
            }
            player.setAttribute("exp_interface:skill",skill)
        }
        return true
    }

    private fun checkHerblore(player: Player): Boolean{
        return (player.questRepository.isComplete("Druidic Ritual"))
    }

    private fun checkSummoning(player: Player): Boolean{
        return player.questRepository.isComplete("Wolf Whistle")
    }

    private fun checkRunecrafting(player: Player): Boolean{
        return player.questRepository.isComplete("Rune Mysteries")
    }

    companion object {
        /**
         * Represents the sound to send.
         */
        private val SOUND = Audio(1270, 12, 1)
        @JvmField
        public val COMPONENT_ID = 134
    }
}