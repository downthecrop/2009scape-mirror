package content.region.fremennik.rellekka.quest.thefremenniktrials

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.world.GameWorld.Pulser
import core.game.world.repository.Repository

/**
 * Handles the Koschei npc.
 * @author NixWigton
 */
class KoscheiNPC constructor(id: Int = 0, location: Location? = null, session: KoscheiSession? = null) :
    AbstractNPC(id, location) {

    /**
     * The session.
     */
    val session: KoscheiSession?

    /**
     * The Koschei type.
     */
    var type: KoscheiType?

    /**
     * If the fight has commenced.
     */
    var isCommenced = false

    /**
     * Constructs a new `KoscheiNPC` `Object`.
     */
    init {
        this.isWalks = true
        this.session = session
        this.isRespawn = false
        type = KoscheiType.forId(id)
    }

    override fun init() {
        super.init()

        /**
         * Ensure the player is in the arena then spawn.
         */
        if (session?.player?.location?.regionId == 10653)
            Pulser.submit(KoscheiSpawnPulse(session.player, this))
        else session?.close()
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (session == null) {
            return
        }
        if (!session.player.isActive) {
            clear()
            return
        }
        if (isCommenced && !properties.combatPulse.isAttacking) {
            properties.combatPulse.attack(session.player)
        }
    }

    override fun startDeath(killer: Entity) {
        if (killer === session!!.player) {
            if (type !== KoscheiType.FOURTH_FORM) {
                type!!.transform(this, session!!.player)
            } else {
                session?.player?.sendMessage("Congratulations! You have completed the warriors trial!")
                session?.player?.setAttribute("/save:fremtrials:thorvald-vote",true)
                session?.player?.setAttribute("/save:fremtrials:votes", session.player.getAttribute("fremtrials:votes", 0) + 1)
                session?.player?.removeAttribute("fremtrials:warrior-accepted")
                addItemOrDrop(session?.player!!, Items.FREMENNIK_BLADE_3757, 1)
                session.close()
            }
            return
        }
        super.startDeath(killer)
    }

    override fun sendImpact(state: BattleState?) {
        if (type == KoscheiType.FOURTH_FORM) {
            if (session?.player?.skills?.lifepoints!! < 2) {
                session.player.fullRestore()
                properties.combatPulse.stop()
                Pulser.submit(FightEndPulse(session.player, this))
                return
            } else {
                session.player.skills?.decrementPrayerPoints(session.player.skills?.prayerPoints!!)
            }
        }
        super.sendImpact(state)
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return KoscheiNPC(id, location, null)
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        if (session == null) {
            return false
        }
        return session.player == entity
    }

    override fun canSelectTarget(target: Entity): Boolean {
        if (target is Player) {
            if (target != session!!.player) {
                return false
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KOSCHEI_THE_DEATHLESS_1290, NPCs.KOSCHEI_THE_DEATHLESS_1291, NPCs.KOSCHEI_THE_DEATHLESS_1292, NPCs.KOSCHEI_THE_DEATHLESS_1293)
    }

    /**
     * Represents a Koschei type.
     * @author NixWigton
     */
    enum class KoscheiType(var npcId: Int, var appearMessage: String?, vararg var appearDialogues: String?) {
        FIRST_FORM(NPCs.KOSCHEI_THE_DEATHLESS_1290, "You must prove yourself... now!"),
        SECOND_FORM(NPCs.KOSCHEI_THE_DEATHLESS_1291, "This is only the beginning; you can't beat me!", "It seems you have some idea of combat after all,", "outerlander! I will not hold back so much this time!"),
        THIRD_FORM(NPCs.KOSCHEI_THE_DEATHLESS_1292, "Foolish mortal; I am unstoppable.", "Impressive start... But now we fight for real!"),
        FOURTH_FORM(NPCs.KOSCHEI_THE_DEATHLESS_1293, "Aaaaaaaarrgghhhh! The power!", "You show some skill at combat... I will hold back no", "longer! This time you lose your prayer however, and", "fight like a warrior!");

        /**
         * Transforms the new npc.
         */
        fun transform(koschei: KoscheiNPC, player: Player) {
            val newType = next()
            koschei.lock()
            player.properties.combatPulse.stop()
            koschei.properties.combatPulse.stop()
            koschei.walkingQueue.queue.clear()
            koschei.animate(Animation(1057))
            koschei.type = newType
            koschei.transform(newType.npcId)
            Pulser.submit(KoscheiSpawnPulse(player, koschei))
        }

        /**
         * Gets the next type.
         * @return the type.
         */
        operator fun next(): KoscheiType {
            return values()[ordinal + 1]
        }

        companion object {
            /**
             * Gets the Koschei type for the id.
             * @param id the id.
             * @return the Koschei type.
             */
            fun forId(id: Int): KoscheiType? {
                for (type in values()) {
                    if (type.npcId == id) {
                        return type
                    }
                }
                return null
            }
        }
    }

    class KoscheiSpawnPulse(val player: Player?, val koschei: KoscheiNPC) : Pulse() {
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++) {
                0 -> koschei.face(player).also { koschei.unlock(); player?.face(koschei) }
                1 -> if (koschei.type?.appearDialogues?.size!! > 0) {
                        player?.dialogueInterpreter?.sendDialogues(NPCs.KOSCHEI_THE_DEATHLESS_1291, core.game.dialogue.FacialExpression.NEUTRAL, *koschei.type!!.appearDialogues)
                    } else { counter = 4 }
                4 -> koschei.attack(player).also { if (koschei.type?.appearMessage?.isNotEmpty() == true) { koschei.sendChat(koschei.type?.appearMessage) }}
            }
            return false
        }
    }

    class FightEndPulse(val player: Player?, val koschei: KoscheiNPC) : Pulse() {
        var counter = 0
        override fun pulse(): Boolean {
            when(counter++) {
                0 -> player?.lock().also { player?.animate(Animation(1332)).also { player?.sendMessage("Oh dear you are...") }}
                1 -> player?.setAttribute("/save:fremtrials:thorvald-vote",true).also {
                    player?.setAttribute("/save:fremtrials:votesplayer", player.getAttribute("fremtrials:votesplayer", 0) + 1)
                    player?.removeAttribute("fremtrials:warrior-accepted")
                }
                3 -> player?.teleport(Location.create(2666,3694,1)).also { koschei.session?.close() }
                4 -> player?.sendMessage("...still alive somehow?")
                6 -> player?.dialogueInterpreter?.open(NPCs.THORVALD_THE_WARRIOR_1289, Repository.findNPC(NPCs.THORVALD_THE_WARRIOR_1289), this)
                7 -> player?.unlock().also { player?.sendMessage("Congratulations! You have passed the warrior's trial!") }
            }
            return false
        }
    }
}