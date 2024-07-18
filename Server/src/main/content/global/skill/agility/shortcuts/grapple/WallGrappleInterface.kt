package content.global.skill.agility.shortcuts.grapple

import core.api.*
import core.game.component.Component
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState

interface WallGrappleInterface {
    var tab: Component?
    fun jump(player: Player, destination: Location): Boolean
    fun fadeToBlack(player: Player): Component
    fun showGame(player: Player): Boolean
}


class WallGrappleInterfaceImpl: WallGrappleInterface{
    override var tab: Component? = null

    override fun jump(player: Player, destination: Location): Boolean {
        // todo this doesn't look great compared to what it used to look like
        closeAllInterfaces(player)
        forceWalk(player, destination,"smart" )
        face(player, destination)
        // We're teleporting if we are animating so make the strength SOFT
        queueScript(player, strength = QueueStrength.SOFT){ stage: Int ->
            when (stage){
                1 -> animate(player, Animation(7268))
                2 ->{
                    teleport(player, destination)
                    return@queueScript stopExecuting(player)
                }
            }
            return@queueScript  delayScript(player, 1)

        }
        return true
    }


    override fun fadeToBlack(player: Player): Component {
        // todo make this work. Right now the tab is always null
        tab = player.interfaceManager.singleTab
        player.interfaceManager.openOverlay(Component(115))
        PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
        player.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 11, 12)
        if (tab == null){
            println("Panic")
            return Component(1)
        }
        return tab!!
    }

    override fun showGame(player: Player): Boolean {
        player.interfaceManager.restoreTabs()
        if (tab != null) {
            player.interfaceManager.openTab(tab)
        }
        PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
        closeOverlay(player)
        closeInterface(player)
        return true
    }

}
