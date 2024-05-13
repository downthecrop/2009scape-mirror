package content.region.misthalin.varrock.quest.familycrest

import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs09.consts.NPCs


class ChronozonNPC(id: Int, location: Location?) : AbstractNPC(NPCs.CHRONOZON_667, Location(3086, 9936, 0)){

    private lateinit var targetplayer: Player

    private var amountOfFireDamageTaken: Int = 0

    private var amountOfAirDamageTaken: Int = 0

    private var amountOfWaterDamageTaken: Int = 0

    private var amountOfEarthDamageTaken: Int = 0

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return ChronozonNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CHRONOZON_667)
    }

    override fun checkImpact(state: BattleState?) {
        if (state != null) {
            if(amountOfAirDamageTaken == 0 || amountOfWaterDamageTaken == 0 ||
                amountOfEarthDamageTaken == 0 || amountOfFireDamageTaken == 0) {
                if(state.style != CombatStyle.MAGIC || state.totalDamage >= skills.lifepoints) {
                    state.neutralizeHits()
                }
            }

            if(state.spell != null) {
                if(state.spell.spellId == 24) {
                    if(state.totalDamage > 0 && amountOfAirDamageTaken == 0) {
                        targetplayer.sendMessage("Chronozon weakens...")
                    }
                    amountOfAirDamageTaken += state.totalDamage
                }

                if(state.spell.spellId == 27) {
                    if(state.totalDamage > 0 && amountOfWaterDamageTaken == 0) {
                        targetplayer.sendMessage("Chronozon weakens...")
                    }
                    amountOfWaterDamageTaken += state.totalDamage
                }

                if(state.spell.spellId == 33) {
                    if(state.totalDamage > 0 && amountOfEarthDamageTaken == 0) {
                        targetplayer.sendMessage("Chronozon weakens...")
                    }
                    amountOfEarthDamageTaken += state.totalDamage
                }

                if(state.spell.spellId == 38) {
                    if(state.totalDamage > 0 && amountOfFireDamageTaken == 0) {
                        targetplayer.sendMessage("Chronozon weakens...")
                    }
                    amountOfFireDamageTaken += state.totalDamage
                }
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if(killer == targetplayer) {
            if (targetplayer.questRepository.getStage("Family Crest") != 20){
                targetplayer.questRepository.getQuest("Family Crest").setStage(targetplayer, 20)
                // Make sure to despawn Chronozon
                this.clear()
            }
        }
        clear()
        super.finalizeDeath(killer)
    }


    fun setPlayer(player: Player){
        targetplayer = player
    }

    override fun init() {
        // Make sure to reset damage taken when spawning in
        amountOfFireDamageTaken = 0
        amountOfAirDamageTaken= 0
        amountOfWaterDamageTaken= 0
        amountOfEarthDamageTaken= 0
        super.init()

    }

}

