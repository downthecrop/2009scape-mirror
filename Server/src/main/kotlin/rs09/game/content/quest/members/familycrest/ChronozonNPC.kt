package plugin.quest.members.familycrest

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location



class ChronozonNPC(id: Int, location: Location?) : AbstractNPC(667, Location(3086, 9936, 0)){

    lateinit var m_targetPlayer: Player

    var m_amountOfFireDamageTaken: Int = 0

    var m_amountOfAirDamageTaken: Int = 0

    var m_amountOfWaterDamageTaken: Int = 0

    var m_amountOfEarthDamageTaken: Int = 0

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return ChronozonNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(667)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (!m_targetPlayer.isActive || m_targetPlayer.getLocation().getDistance(getLocation()) > 15) {
            clear()
        }

    }

    override fun checkImpact(state: BattleState?) {
        if (state != null) {
            if(state.style != CombatStyle.MAGIC){
                state.neutralizeHits()
            }
            else{
                if(state.spell.spellId == 24 ||state.spell.spellId == 45) {
                    if (m_amountOfAirDamageTaken < 23) {
                        m_amountOfAirDamageTaken += state.estimatedHit
                    } else {
                        state.neutralizeHits()
                        m_targetPlayer.sendMessage("Air Blast seems to have done all it will to Chronozon")
                    }
                }

                if(state.spell.spellId == 33 ||state.spell.spellId == 52) {
                    if (m_amountOfEarthDamageTaken < 23) {
                        m_amountOfEarthDamageTaken += state.estimatedHit
                    }
                    else {
                        state.neutralizeHits()
                        m_targetPlayer.sendMessage("Earth Blast seems to have done all it will to Chronozon")
                    }
                }

                if(state.spell.spellId == 38 || state.spell.spellId == 55) {
                    if (m_amountOfFireDamageTaken < 23) {
                        m_amountOfFireDamageTaken += state.estimatedHit
                    } else {
                        state.neutralizeHits()
                        m_targetPlayer.sendMessage("Fire Blast seems to have done all it will to Chronozon")
                    }
                }

                if(state.spell.spellId == 27 ||state.spell.spellId == 48)
                    if(m_amountOfWaterDamageTaken < 23){
                        m_amountOfWaterDamageTaken += state.estimatedHit
                    }
                    else{
                        state.neutralizeHits()
                        m_targetPlayer.sendMessage("Water Blast seems to have done all it will to Chronozon")
                    }

            }

        }
    }

    override fun isAttackable(entity: Entity, style: CombatStyle?): Boolean {
        return if (entity != m_targetPlayer) {
            false
        }

        else super.isAttackable(entity, style)
    }

    override fun clear() {
        super.clear()
    }

    override fun finalizeDeath(killer: Entity?) {
        clear()
        super.finalizeDeath(killer)

    }

    fun setPlayer(player: Player){
        m_targetPlayer = player;
    }

}

//3086, 9936, 0