package rs09.game

import api.events.*

object Event {
    @JvmStatic val ResourceProduced = ResourceProducedEvent::class.java
    @JvmStatic val NPCKilled = NPCKillEvent::class.java
    @JvmStatic val Teleport = TeleportEvent::class.java
    @JvmStatic val FireLit = LitFireEvent::class.java
    @JvmStatic val Interaction = InteractionEvent::class.java
    @JvmStatic val ButtonClicked = ButtonClickedEvent::class.java
    @JvmStatic val UsedWith = UsedWithEvent::class.java
    @JvmStatic val SelfDeath = SelfDeath::class.java
    @JvmStatic val Tick = TickEvent::class.java
    @JvmStatic val InterfaceOpened = InterfaceOpenEvent::class.java
    @JvmStatic val InterfaceClosed = InterfaceCloseEvent::class.java
}