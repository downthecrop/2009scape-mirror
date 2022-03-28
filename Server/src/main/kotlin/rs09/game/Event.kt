package rs09.game

import api.events.*

object Event {
    val ResourceProduced = ResourceProducedEvent::class.java
    val NPCKilled = NPCKillEvent::class.java
    val Teleport = TeleportEvent::class.java
    val FireLit = LitFireEvent::class.java
    val Interaction = InteractionEvent::class.java
    val ButtonClicked = ButtonClickedEvent::class.java
    val UsedWith = UsedWithEvent::class.java
    val SelfDeath = SelfDeath::class.java
}