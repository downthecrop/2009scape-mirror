package rs09.game.node.entity.skill.magic

import core.cache.def.impl.ItemDefinition
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import rs09.game.interaction.Listener
import rs09.game.world.GameWorld

abstract class SpellListener(val bookName: String) : Listener {
    companion object {
        @JvmField
        val NPC = -1
        @JvmField
        val OBJECT = -2
        @JvmField
        val ITEM = -3
        @JvmField
        val PLAYER = -4
        @JvmField
        val NONE = -5
        @JvmField
        val GROUND_ITEM = -6
    }
    fun onCast(spellID: Int,type: Int,method: (player: Player, node: Node?) -> Unit){
        SpellListeners.add(spellID,type,bookName,method)
    }

    fun onCast(spellID: Int, type: Int, vararg ids: Int, method: (player: Player, node: Node?) -> Unit){
        SpellListeners.add(spellID,type,ids,bookName,method)
    }

    fun requires(player: Player, magicLevel: Int = 0, runes: Array<Item> = arrayOf<Item>(), specialEquipment: IntArray = intArrayOf()){
        if(player.getAttribute("magic-delay",0) > GameWorld.ticks){
            throw IllegalStateException()
        }
        if(player.getAttribute("tablet-spell",false)){
            return
        }
        if(player.skills.getLevel(Skills.MAGIC) < magicLevel){
            player.sendMessage("You need a magic level of $magicLevel to cast this spell.")
            throw IllegalStateException()
        }
        for(rune in runes){
            if(!SpellUtils.hasRune(player,rune)){
                player.sendMessage("You don't have enough ${rune.definition.name.toLowerCase()}s to cast this spell.")
                throw IllegalStateException()
            }
        }
        for(item in specialEquipment){
            if(!player.equipment.contains(item,1)){
                player.sendMessage("You need a ${ItemDefinition.forId(item).name} to cast this.")
                throw IllegalStateException()
            }
        }
    }

    fun removeRunes(player: Player,removeAttr: Boolean = true){
        player.inventory.remove(*player.getAttribute("spell:runes",ArrayList<Item>()).toTypedArray())
        if(removeAttr) {
            player.removeAttribute("spell:runes")
            player.removeAttribute("tablet-spell")
        }
    }

    fun addXP(player: Player,amount:Double){
        if(player.getAttribute("tablet-spell",false)) return
        player.skills.addExperience(Skills.MAGIC,amount)
    }

    fun visualizeSpell(player: Player,anim:Animation,gfx: Graphics,soundID: Int = -1){
        if(player.getAttribute("tablet-spell",false)) return
        player.visualize(anim,gfx)
        if(soundID != -1){
            player.audioManager.send(soundID)
        }
    }

    fun setDelay(player: Player,isTeleport: Boolean = false){
        if(!isTeleport) player.setAttribute("magic-delay",GameWorld.ticks + 3) else player.setAttribute("magic-delay",GameWorld.ticks + 5)
    }

    fun interrupt(player: Player){
        player.pulseManager.clear()
    }

    fun showMagicTab(player: Player){
        player.interfaceManager.setViewedTab(6)
    }
}