package rs09.game.camerautils

import core.game.node.entity.player.Player
import rs09.game.system.SystemLogger
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket

class PlayerCamera(val player: Player?) {
    var ctx: CameraContext? = null

    fun setPosition(x: Int, y: Int, height: Int){
        player ?: return
        ctx = CameraContext(player,CameraContext.CameraType.SET,x,y,height,0,0)
        PacketRepository.send(CameraViewPacket::class.java,ctx)
    }
    fun rotateTo(x: Int, y: Int, height: Int, speed: Int){
        player ?: return
        ctx = CameraContext(player,CameraContext.CameraType.ROTATION,x,y,height,speed,1)
        PacketRepository.send(CameraViewPacket::class.java,ctx)
    }
    fun rotateBy(diffX: Int, diffY: Int, diffHeight: Int, speed: Int){
        player ?: return
        ctx ?: return
        ctx = CameraContext(player,CameraContext.CameraType.ROTATION, ctx!!.x + diffX, ctx!!.y + diffY, ctx!!.height + diffHeight,speed,1)
        PacketRepository.send(CameraViewPacket::class.java,ctx)
    }
    fun panTo(x: Int, y: Int, height: Int, speed: Int){
        player ?: return
        ctx = CameraContext(player,CameraContext.CameraType.POSITION,x,y,height,speed,1)
        PacketRepository.send(CameraViewPacket::class.java,ctx)
    }
}