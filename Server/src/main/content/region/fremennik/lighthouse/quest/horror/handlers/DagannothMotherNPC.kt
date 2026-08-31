package content.region.fremennik.lighthouse.quest.horror.handlers

import content.data.Quests
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep
import content.region.fremennik.lighthouse.quest.horror.JossikLighthouseDialogueFile
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.repository.Repository.getPlayerByName
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class DagannothMotherNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    // elemental attack spells
    private val airSpells = intArrayOf(1, 10, 24, 45)
    private val waterSpells = intArrayOf(4, 14, 27, 48)
    private val earthSpells = intArrayOf(6, 17, 33, 52)
    private val fireSpells = intArrayOf(8, 20, 38, 55)

    var type: DagannothType?

    var isSpawned = false

    init {
        this.isWalks = true
        this.isRespawn = false
        type = DagannothType.forId(id)
    }

    override fun init() {
        super.init()
    }

    override fun handleTickActions() {
        super.handleTickActions()

        // retrieve the target player
        val targetName = getAttribute<String?>(this, HorrorFromTheDeep.HFTD_TARGET, null)
        val target = getPlayerByName(targetName)

        // poofclear if target runs too far or leaves
        if (target == null
            || !target.isActive
            || !this.location.withinDistance(target.location, 25)
        ) {
            // clear the player's inCombat attribute
            if (target != null) {
                removeAttribute(target, HorrorFromTheDeep.HFTD_COMBAT)
                clearHintIcon(target)
            }

            // clear the NPC
            this.walkingQueue.reset()
            this.properties.combatPulse.stop()
            poofClear(this)
        } else {
            // keep attacking target
            if (isSpawned && !properties.combatPulse.isAttacking) {
                properties.combatPulse.attack(target)
                this.getSkills().isLifepointsUpdate = false
            }

            // transform every 18 seconds
            if(getWorldTicks() % 30 == 0){
                type!!.transform(this, target)
                playAudio(target, 1617)
            }
        }

        return
    }

    // you need to attack with the correct style, otherwise hits are neutralized
    override fun checkImpact(state: BattleState) {

        if (state.attacker !is Player || state.victim !is NPC) return

        // get the attack style
        var style = state.style
        if (style == null) {
            style = state.attacker.properties.combatPulse.style
        }

        // get spell
        val spellId = state.spell?.spellId

        // check current attack against weakness
        val isWeakness = when (type?.npcId) {
            // white: damaged by air spells
            NPCs.DAGANNOTH_MOTHER_1351 -> style == CombatStyle.MAGIC && spellId != null && spellId in airSpells
            // blue: damaged by water spells
            NPCs.DAGANNOTH_MOTHER_1352 -> style == CombatStyle.MAGIC && spellId != null && spellId in waterSpells
            // red: damaged by fire spells
            NPCs.DAGANNOTH_MOTHER_1353 -> style == CombatStyle.MAGIC && spellId != null && spellId in fireSpells
            // brown: damaged by earth spells
            NPCs.DAGANNOTH_MOTHER_1354 -> style == CombatStyle.MAGIC && spellId != null && spellId in earthSpells
            // green: damamged by ranged
            NPCs.DAGANNOTH_MOTHER_1355 -> style == CombatStyle.RANGE
            // orange: damaged by melee
            NPCs.DAGANNOTH_MOTHER_1356 -> style == CombatStyle.MELEE
            else -> false
        }

        // either apply the attack, or neutralize if it's not the weakness
        if (isWeakness) {
            state.estimatedHit = state.maximumHit
        } else {
            state.neutralizeHits()
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            clearHintIcon(killer)
            removeAttribute(killer, HorrorFromTheDeep.HFTD_COMBAT)

            // teleport player and finish the quest
            teleport(killer, Location.create(2515, 4625, 1))
            finishQuest(killer, Quests.HORROR_FROM_THE_DEEP)

            lock(killer, 10)
            lockInteractions(killer, 10)

            // add casket or send to Jossik. Since the game teleports you out at the finish, and the arena isn't accessible after, you shouldn't addItemOrDrop the casket
            if (freeSlots(killer) == 0) {
                setAttribute(killer, HorrorFromTheDeep.HFTD_NEEDS_CASKET, true)
            } else {
                addItem(killer, Items.RUSTY_CASKET_3849)
            }

            // display 'let's go' dialogue after a moment
            queueScript(killer, 5) {
                closeInterface(killer)
                openDialogue(killer, JossikLighthouseDialogueFile(), NPC(NPCs.JOSSIK_1335))
                return@queueScript true
            }
        }

        clear()
        super.finalizeDeath(killer)
    }

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return DagannothMotherNPC(id, location)
    }

    // checks if player can attack this NPC
    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {

        // retrieve the target player
        val targetName = getAttribute<String?>(this, HorrorFromTheDeep.HFTD_TARGET, null)
        val target = getPlayerByName(targetName)

        if (entity is Player) {
            if (entity == target) {
                return true
            } else {
                sendMessage(entity, "It's not after you...")
            }
        }
        return false
    }

    // checks if this NPC can attack a target
    override fun canSelectTarget(target: Entity): Boolean {

        // retrieve the target player
        val targetName = getAttribute<String?>(this, HorrorFromTheDeep.HFTD_TARGET, null)
        val yourTarget = getPlayerByName(targetName)

        // returns false if the target is not who the NPC is supposed to attack
        return target == yourTarget
    }

    // the different forms
    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.DAGANNOTH_MOTHER_1351,
            NPCs.DAGANNOTH_MOTHER_1352,
            NPCs.DAGANNOTH_MOTHER_1353,
            NPCs.DAGANNOTH_MOTHER_1354,
            NPCs.DAGANNOTH_MOTHER_1355,
            NPCs.DAGANNOTH_MOTHER_1356,
        )
    }

    enum class DagannothType(var npcId: Int, var sendChat: String?, var sendMessage: String?) {
        WHITE(NPCs.DAGANNOTH_MOTHER_1351, "Tktktktktktkt", null),
        BLUE(NPCs.DAGANNOTH_MOTHER_1352, "Krrrrrrk", "the dagannoth changes to blue..."),
        RED(NPCs.DAGANNOTH_MOTHER_1353, "Sssssrrrkkkkk", "the dagannoth changes to red..."),
        BROWN(NPCs.DAGANNOTH_MOTHER_1354, "Krrrrrrssssssss", "the dagannoth changes to brown..."),
        GREEN(NPCs.DAGANNOTH_MOTHER_1355, "Krkrkrkrkrkrkrkr", "the dagannoth changes to green..."),
        ORANGE(NPCs.DAGANNOTH_MOTHER_1356, "Chkhkhkhkhk", "the dagannoth changes to orange..."),
        ;

        fun transform(dagannoth: DagannothMotherNPC, player: Player) {
            val newType = next()
            val oldHp = dagannoth.getSkills().lifepoints
            dagannoth.type = newType
            dagannoth.transform(newType.npcId)
            dagannoth.skills.isLifepointsUpdate = false

            registerHintIcon(player, dagannoth)
            if (dagannoth.type?.sendMessage != null) sendMessage(player, dagannoth.type!!.sendMessage!!)
            dagannoth.attack(player)
            dagannoth.sendChat(dagannoth.type?.sendChat)

            dagannoth.getSkills().setLifepoints(oldHp)
        }

        operator fun next(): DagannothType {
            return values().random()
        }

        companion object {
            fun forId(id: Int): DagannothType? {
                for (type in values()) {
                    if (type.npcId == id) {
                        return type
                    }
                }
                return null
            }
        }
    }
}