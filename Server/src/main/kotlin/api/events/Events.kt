package api.events

import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.TeleportManager.TeleportType

data class ResourceGatheredEvent(val itemId: Int, val amount: Int, val source: Node) : Event
data class NPCKillEvent(val npc: NPC) : Event
data class TeleportEvent(val type: TeleportType, val source: Int = -1) : Event
data class LitFireEvent(val logId: Int) : Event
data class InteractionEvent(val target: Node, val option: String) : Event