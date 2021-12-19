package rs09.game.ai.general.scriptrepository

import api.*
import core.game.world.map.zone.ZoneBorders
import rs09.game.ai.general.ScriptAPI
import rs09.game.ai.skillingbot.SkillingBotAssembler
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InteractionListeners


@PlayerCompatible
@ScriptName("Gnome Agility Course Script")
@ScriptDescription("Start the script while standing front of the balancing log")
@ScriptIdentifier("gnome_agility")
class GnomeAgility : Script() {

    var state = State.INIT
    val startzone = ZoneBorders(2473,3436,2475,3437,0)
    val afterlog = ZoneBorders(2474,3429,2474,3429,0)
    val beforefirstnet = ZoneBorders(2473,3426,2474,3427,0)
    val afterfirstnet = ZoneBorders(2471,3424,2476,3424,1)
    val beforebranchup = ZoneBorders(2473,3423,2473,3423,1)
    val afterbranchup = ZoneBorders(2473,3420,2473,3420,2)
    val beforerope = ZoneBorders(2477,3420,2477,3420,2)
    val afterrope = ZoneBorders(2483,3420,2483,3420,2)
    val beforebranchdown = ZoneBorders(2485,3419,2486,3420,2)
    val afterbranchdown = ZoneBorders(2487,3420,2487,3420,0)
    val beforesecondnet = ZoneBorders(2483,3425,2488,3425,0)
    val aftersecondnet = ZoneBorders(2483,3427,2488,3427,0)
    val beforepipe = ZoneBorders(2484,3430,2487,3430,0)
    val afterpipe = ZoneBorders(2484,3437,2487,3437,0)
    var overlay: ScriptAPI.BottingOverlay? = null
    var totalLaps = 0

    override fun tick() {

        when (state) {

            State.INIT -> {
                overlay = scriptAPI.getOverlay()
                overlay!!.init()
                overlay!!.setTitle("Agility Course")
                overlay!!.setTaskLabel("Laps Run")
                overlay!!.setAmount(0)

                state = State.START
            }

            State.START -> {
                if(!startzone.insideBorder(bot)){
                    scriptAPI.walkTo(startzone.randomLoc)
                }
                if(startzone.insideBorder(bot)){
                    state = State.LOG
                }
            }

            State.LOG -> {
                val log = scriptAPI.getNearestNode("log balance",true)
                if(startzone.insideBorder(bot)) {
                    scriptAPI.interact(bot, log, "walk-across")
                }
                if(afterlog.insideBorder(bot)){
                    state = State.FIRSTNET
                }
            }

            State.FIRSTNET -> {
                val firstnet = scriptAPI.getNearestNode("obstacle net",true)
                if(!beforefirstnet.insideBorder(bot)){
                    scriptAPI.walkTo(beforefirstnet.randomLoc)
                }
                if(beforefirstnet.insideBorder(bot)) {
                    scriptAPI.interact(bot, firstnet, "climb-over")
                }
                if(afterfirstnet.insideBorder(bot)){
                    state = State.BRANCHUP
                }
            }

            State.BRANCHUP -> {
                val branchup = scriptAPI.getNearestNode("tree branch", true)
                if(!beforebranchup.insideBorder(bot) && !afterbranchup.insideBorder(bot)){
                    scriptAPI.walkTo(beforebranchup.randomLoc)
                }
                else if(beforebranchup.insideBorder(bot)){
                    scriptAPI.interact(bot,branchup,"climb")
                }
                else if(afterbranchup.insideBorder(bot)){
                    state = State.ROPE
                }
            }

            State.ROPE -> {
                val rope = scriptAPI.getNearestNode("balancing rope",true)
                if(!beforerope.insideBorder(bot) && !afterrope.insideBorder(bot)){
                    scriptAPI.walkTo(beforerope.randomLoc)
                }
                else if(beforerope.insideBorder(bot)){
                    scriptAPI.interact(bot,rope,"walk-on")
                }
                else if(afterrope.insideBorder(bot)){
                    state = State.BRANCHDOWN
                }
            }

            State.BRANCHDOWN -> {
                val branchdown = scriptAPI.getNearestNode("tree branch",true)
                if(!beforebranchdown.insideBorder(bot) && !afterbranchdown.insideBorder(bot)){
                    scriptAPI.walkTo(beforebranchdown.randomLoc)
                }
                else if(beforebranchdown.insideBorder(bot)){
                    scriptAPI.interact(bot,branchdown,"climb-down")
                }
                else if(afterbranchdown.insideBorder(bot)){
                    state = State.SECONDNET
                }
            }

            State.SECONDNET -> {
                val secondnet = scriptAPI.getNearestNode("obstacle net",true)
                if(!beforesecondnet.insideBorder(bot) && !aftersecondnet.insideBorder(bot)){
                    scriptAPI.walkTo(beforesecondnet.randomLoc)
                }
                else if(beforesecondnet.insideBorder(bot)){
                    scriptAPI.interact(bot,secondnet,"climb-over")
                }
                else if(aftersecondnet.insideBorder(bot)){
                    state = State.PIPE
                }
            }

            State.PIPE -> {
                val pipe = scriptAPI.getNearestNode("obstacle pipe",true)
                if(!beforepipe.insideBorder(bot) && !afterpipe.insideBorder(bot)){
                    scriptAPI.walkTo((beforepipe.randomLoc))
                }
                if(beforepipe.insideBorder(bot)){
                    scriptAPI.interact(bot,pipe,"squeeze-through")
                }
                if(afterpipe.insideBorder(bot)){
                    totalLaps += 1
                    overlay!!.setAmount(totalLaps)
                    state = State.START
                }
            }
        }
    }

    override fun newInstance(): Script {
        val script = GnomeAgility()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR,bot.startLocation)
        return script
    }

    enum class State {
        START,
        LOG,
        FIRSTNET,
        BRANCHUP,
        ROPE,
        BRANCHDOWN,
        SECONDNET,
        PIPE,
        INIT
    }
}