package content.global.skill.construction.decoration.chapel

import core.api.playAudio
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items
import org.rs09.consts.Sounds

const val HOLY_ELIXER = 13754
const val SPIRIT_SHIELD = 13734
@Initializable
/**
 * Blesses shields
 * @author Ceikry
 */
class ShieldBlessingPlugin : UseWithHandler(HOLY_ELIXER, SPIRIT_SHIELD) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for(i in arrayOf(13185, 13188, 13191, 13194, 13197)){
            addHandler(i, OBJECT_TYPE,this)
        }
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        val player = event?.player
        player ?: return false
        if (player.ironmanManager.isIronman && !player.houseManager.isInHouse(player)) {
            player.sendMessage("You cannot do this on someone else's altar.")
            return true
        }
        if(player.skills.getLevel(Skills.PRAYER) < 85){
            player.sendMessage("You need 85 prayer to do this.")
            return true
        }

        player.animator.animate(Animation(896))
        playAudio(player, Sounds.POH_OFFER_BONES_958)

        if(player.inventory.remove(Item(HOLY_ELIXER)) && player.inventory.remove(Item(SPIRIT_SHIELD)))
            player.inventory.add(Item(Items.BLESSED_SPIRIT_SHIELD_13736))
        return true
    }
}