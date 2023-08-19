package core.api.utils

import core.game.node.entity.player.Player
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket

/**
 * Player camera
 *
 * @property player
 * @constructor Create empty Player camera
 * **See Also:** [This forum post](https://forum.2009scape.org/viewtopic.php?t=173-in-game-camera-movement-documentation-server-sided)
 *
 * WARNING: Playing around with camera values may potentially trigger seizures for people with photosensitive epilepsy.
 * Please use care when using camera values.
 */
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
    fun shake(cameraType: Int, jitter: Int, amplitude: Int, frequency: Int, speed: Int){
        player ?: return
        ctx = CameraContext(player,CameraContext.CameraType.SHAKE,cameraType,jitter,amplitude,frequency,speed)
        PacketRepository.send(CameraViewPacket::class.java,ctx)
    }
    fun reset(){
        player ?: return
        ctx = CameraContext(player,CameraContext.CameraType.RESET, -1, -1, -1, -1, -1)
        PacketRepository.send(CameraViewPacket::class.java,ctx)
    }
}