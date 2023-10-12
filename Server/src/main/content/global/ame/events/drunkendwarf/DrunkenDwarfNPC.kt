package content.global.ame.events.drunkendwarf

import content.global.ame.RandomEventNPC
import core.api.*
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class DrunkenDwarfNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.DRUNKEN_DWARF_956) {
    private val phrases = arrayOf("Oi, are you der @name!","Dunt ignore your matey!","Aww comeon, talk to ikle me @name!")
    private var attackPhrase = false
    private var attackDelay = 0
    private var lastPhraseTime = 0

    private fun sendPhrases() {
        if (getWorldTicks() > lastPhraseTime + 5) {
            playGlobalAudio(this.location, Sounds.DWARF_WHISTLE_2297)
            sendChat(this, phrases.random().replace("@name",player.username.capitalize()))
            this.face(player)
            lastPhraseTime = getWorldTicks()
        }
    }

    override fun init() {
        super.init()
        playGlobalAudio(this.location, Sounds.DWARF_WHISTLE_2297)
        sendChat(this, "'Ello der ${player.username.capitalize()}! *hic*")
    }

    override fun tick() {
        if (RandomFunction.roll(20) && !attackPhrase)
            sendPhrases()
        if (ticksLeft <= 10) {
            ticksLeft = 10
            if (!attackPhrase)
                sendChat("I hates you, ${player.username.capitalize()}!").also { attackPhrase = true }
            if (attackDelay <= getWorldTicks())
                this.attack(player)
        }
        super.tick()
    }

    override fun talkTo(npc: NPC) {
        attackDelay = getWorldTicks() + 10
        this.pulseManager.clear()
        openDialogue(player, DrunkenDwarfDialogue(), this.asNpc())
    }
}