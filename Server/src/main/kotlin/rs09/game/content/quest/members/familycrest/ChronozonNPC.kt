package rs09.game.content.quest.members.familycrest

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
            if(m_amountOfAirDamageTaken == 0 || m_amountOfWaterDamageTaken == 0 ||
                m_amountOfEarthDamageTaken == 0 || m_amountOfFireDamageTaken == 0) {
                if(state.style != CombatStyle.MAGIC || state.totalDamage >= skills.lifepoints) {
                    state.neutralizeHits()
                }
            }

            if(state.spell != null) {
                if(state.spell.spellId == 24) {
                    if(state.totalDamage > 0 && m_amountOfAirDamageTaken == 0) {
                        m_targetPlayer.sendMessage("Chronozon weakens...")
                    }
                    m_amountOfAirDamageTaken += state.totalDamage
                }

                if(state.spell.spellId == 27) {
                    if(state.totalDamage > 0 && m_amountOfWaterDamageTaken == 0) {
                        m_targetPlayer.sendMessage("Chronozon weakens...")
                    }
                    m_amountOfWaterDamageTaken += state.totalDamage
                }

                if(state.spell.spellId == 33) {
                    if(state.totalDamage > 0 && m_amountOfEarthDamageTaken == 0) {
                        m_targetPlayer.sendMessage("Chronozon weakens...")
                    }
                    m_amountOfEarthDamageTaken += state.totalDamage
                }

                if(state.spell.spellId == 38) {
                    if(state.totalDamage > 0 && m_amountOfFireDamageTaken == 0) {
                        m_targetPlayer.sendMessage("Chronozon weakens...")
                    }
                    m_amountOfFireDamageTaken += state.totalDamage
                }
            }
        }
    }

    override fun isAttackable(entity: Entity, style: CombatStyle?, message: Boolean): Boolean {
        return entity == m_targetPlayer &&
            m_targetPlayer.questRepository.getQuest("Family Crest").getStage(m_targetPlayer) == 19 &&
            super.isAttackable(entity, style, message)
    }

    override fun clear() {
        super.clear()
    }

    override fun finalizeDeath(killer: Entity?) {
        if(killer == m_targetPlayer) {
            m_targetPlayer.questRepository.getQuest("Family Crest").setStage(m_targetPlayer, 20)
        }
        clear()
        super.finalizeDeath(killer)

    }

    fun setPlayer(player: Player){
        m_targetPlayer = player;
    }

}

//3086, 9936, 0
