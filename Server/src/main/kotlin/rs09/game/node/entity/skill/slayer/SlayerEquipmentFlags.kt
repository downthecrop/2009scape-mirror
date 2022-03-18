package rs09.game.node.entity.skill.slayer

import api.EquipmentSlot
import api.getAttribute
import api.getItemFromEquipment
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import rs09.game.node.entity.skill.skillcapeperks.SkillcapePerks

/**
 * Represents a slayer equipment.
 * @author Ceikry
 */
object SlayerEquipmentFlags {

    val blackMasks = (Items.BLACK_MASK_10_8901..Items.BLACK_MASK_8921).map { it }.toIntArray()
    val slayerItems = intArrayOf(Items.NOSE_PEG_4168, Items.EARMUFFS_4166, Items.FACE_MASK_4164, *blackMasks, Items.SPINY_HELMET_4551, Items.SLAYER_CAPET_9787, Items.SLAYER_CAPE_9786, Items.SLAYER_HELMET_13263, Items.WITCHWOOD_ICON_8923, Items.MIRROR_SHIELD_4156)

    @JvmStatic
    fun updateFlags(player: Player){
        var flags = 0
        if(SkillcapePerks.isActive(SkillcapePerks.TRICKS_OF_THE_TRADE, player) && getAttribute(player, "cape_perks:tott:helmet-stored", false)) flags = 0x3F
        else if(hasItem(player, Items.SLAYER_HELMET_13263)) flags = 0x1F
        else if(hasItem(player, Items.NOSE_PEG_4168)) flags = 1
        else if(hasItem(player, Items.EARMUFFS_4166)) flags = flags or (1 shl 1)
        else if(hasItem(player, Items.FACE_MASK_4164)) flags = flags or (1 shl 2)
        else if((getItemFromEquipment(player, EquipmentSlot.HAT)?.id ?: 0) in blackMasks) flags = flags or (1 shl 3)
        else if(hasItem(player, Items.SPINY_HELMET_4551)) flags = flags or (1 shl 4)

        if((getItemFromEquipment(player, EquipmentSlot.AMULET)?.id ?: 0) == Items.WITCHWOOD_ICON_8923) flags = flags or (1 shl 7)
        if((getItemFromEquipment(player, EquipmentSlot.SHIELD)?.id ?: 0) == Items.MIRROR_SHIELD_4156) flags = flags or (1 shl 8)
        player.slayer.flags.equipmentFlags = flags
    }

    @JvmStatic
    fun hasNosePeg(player: Player): Boolean{
        return player.slayer.flags.equipmentFlags and 1 == 1
    }

    @JvmStatic
    fun hasEarmuffs(player: Player): Boolean {
        return (player.slayer.flags.equipmentFlags shr 1) and 1 == 1
    }

    @JvmStatic
    fun hasFaceMask(player: Player): Boolean {
        return (player.slayer.flags.equipmentFlags shr 2) and 1 == 1
    }

    @JvmStatic
    fun hasBlackMask(player: Player): Boolean {
        return (player.slayer.flags.equipmentFlags shr 3) and 1 == 1
    }

    @JvmStatic
    fun hasSpinyHelmet(player: Player): Boolean {
        return (player.slayer.flags.equipmentFlags shr 4) and 1 == 1
    }

    @JvmStatic
    fun hasWitchwoodIcon(player: Player): Boolean {
        return (player.slayer.flags.equipmentFlags shr 7) and 1 == 1
    }

    @JvmStatic
    fun hasMirrorShield(player: Player): Boolean {
        return (player.slayer.flags.equipmentFlags shr 8) and 1 == 1
    }

    @JvmStatic
    fun getDamAccBonus(player: Player): Double {
        val isCape = player.slayer.flags.equipmentFlags == 0x3F
        val hasMask = hasBlackMask(player)

        return if(isCape) 1.075
        else if(hasMask) 1.15
        else 1.0
    }

    private fun hasItem(player: Player, id: Int): Boolean{
        return (getItemFromEquipment(player, EquipmentSlot.HAT)?.id ?: 0) == id
    }

    fun isSlayerEq(item: Int): Boolean{
        return item in slayerItems
    }

}