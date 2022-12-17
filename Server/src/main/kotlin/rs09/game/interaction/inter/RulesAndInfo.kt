package rs09.game.interaction.inter

import api.*
import core.game.node.entity.player.Player
import core.tools.RandomFunction
import rs09.game.interaction.InterfaceListener
import rs09.game.system.SystemLogger

object RulesAndInfo {
    val RULES = arrayOf(
        "<col=ffffff>1. Be respectful to your fellow players.</col>",
        "   -No harassment, etc.",
        "   -Keep arguments private.",
        "<col=ffffff>2. Do not exploit bugs.</col>",
        "   -Zero tolerance. Can result in account deletion.",
        "   -If you discover a bug, report it on our Gitlab.",
        "<col=ffffff>3. Do not discuss or advertise other servers.</col>",
        "   -Discussion of the live jagex games is fine.",
        "   -Discussion of open source projects is fine.",
        "<col=ffffff>4. No unfair advantages.</col>",
        "   -This includes autoclickers, autohotkey, etc.",
        "   -Exception: 1-to-1 inputs, such as mousekeys.",
        "   -Includes training methods that bypass AFK limits."
    )
    val SEPARATOR = "<str>                                             </str>"
    val INFO = arrayOf(
        "<col=6bcdfa>Join the forums! forum.2009scape.org </col>"
    )

    @JvmStatic
    fun openFor(player: Player)
    {
        if(getAttribute(player, "rules:confirmed", false) || !getAttribute(player, "tutorial:complete", false))
            return
        val pin = getAttribute(player, "rules:pin", RandomFunction.random(1000,9999))
        setAttribute(player, "/save:rules:pin", pin)

        var ln = setBaseRulesAndInfo(player);

        setInterfaceText(player, "<col=ffffff>If you agree to the above, type ::confirmrules $pin", 384, ln++)
        setInterfaceText(player, "", 384, ln)
        player.packetDispatch.sendInterfaceConfig(384, 17, true)
        openInterface(player, 384)
        player.lock()
    }

    @JvmStatic
    fun setBaseRulesAndInfo(player: Player): Int {
        var ln = 1
        for(line in INFO) setInterfaceText(player, INFO[ln - 1], 384, ln++)
        setInterfaceText(player, SEPARATOR, 384, ln++)
        val newIndex = ln
        for(line in RULES) setInterfaceText(player, RULES[ln - newIndex], 384, ln++)
        setInterfaceText(player, "", 384, ln)
        return ln
    }
}

class RulesListener : InterfaceListener
{
    override fun defineInterfaceListeners() {
        onClose(384){player, _ ->
            if(!getAttribute(player, "rules:confirmed", false))
                runTask(player, 1) { RulesAndInfo.openFor(player); sendDialogue(player, "Please read the rules.") }
            return@onClose true
        }
    }
}