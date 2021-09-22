package rs09.game.node.entity.skill.slayer

import core.game.node.entity.combat.BattleState
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import org.rs09.consts.Items

class SlayerUtils(val player: Player) {

    fun hasBroadWeaponEquipped(state: BattleState): Boolean {
        return (state.weapon != null && state.weapon.id == Items.LEAF_BLADED_SPEAR_4158 ||
            state.weapon != null && state.weapon.id == Items.LEAF_BLADED_SWORD_13290 ||
            state.ammunition != null && (state.ammunition.itemId == Items.BROAD_ARROW_4160 || state.ammunition.itemId == Items.BROAD_TIPPED_BOLTS_13280) ||
            state.spell != null && state.spell.spellId == 31 && player.spellBookManager
                .spellBook == SpellBook.MODERN.interfaceId
        )
    }
}