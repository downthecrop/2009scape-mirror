import api.ContentAPI
import core.game.component.Component
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBuilder
import rs09.game.interaction.InteractionListener

val AVACH_NIMPORTO_LOC = Location.create(1637, 4709)
val PORTAL = 29534
val SIGNS = intArrayOf(29461, 29462, 29463, 29464)
val HOLES = intArrayOf(29476, 29477, 29478)
val NUMBERS = intArrayOf(29447, 29448, 29449, 29450, 29451, 29452, 29453, 29454, 29455)
var i = 0

//val TUTORIALS = intArrayOf(684, 685, 687, 688, 690)

val TUTORIALS = hashMapOf(
    29463 to 684,
    29464 to 687,
    29462 to 688,
    29461 to 690
    )

val MRS_WINKIN = 7132

class VinesweeperListener : InteractionListener() {
    override fun defineListeners() {
        ZoneBuilder.configure(VinesweeperZone())
        on(PORTAL, SCENERY, "enter") { player, _ ->
            val loc = player.getAttribute("vinesweeper:return-tele", Location.create(3052, 3304))
            ContentAPI.teleport(player, loc)
            return@on true
        }
        on(SIGNS, SCENERY, "read") { player, node ->
            player.interfaceManager.open(Component(TUTORIALS[node.id]!!))
            return@on true
        }
        on(HOLES, SCENERY, "dig") { player, node ->
            player.sendMessage("hello from ${node.location}")
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(NUMBERS[i++ % NUMBERS.size]))
            return@on true
        }
        on(MRS_WINKIN, NPC, "trade") { player, node ->
            player.interfaceManager.open(Component(686))
            return@on true
        }
        on(MRS_WINKIN, NPC, "buy-flags") { player, node ->
            return@on true
        }
    }
}

class VinesweeperZone : MapZone("Vinesweeper", true) {

    override fun enter(e: Entity): Boolean {
        if(e is Player) {
            e.interfaceManager.openOverlay(Component(689))
            e.configManager.set(1195, 31337 shl 6);
        }

        return super.enter(e)
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if(e is Player) {
            e.interfaceManager.closeOverlay()
        }

        return super.leave(e, logout)
    }

    override fun configure() {
        super.registerRegion(6473)
    }
}

/*
https://www.youtube.com/watch?v=WkCVAOOR7Sw
buy-flags
Player: "Have you got any flags?"
NPC: "Let me check for you."
NPC: "Alright, you can have a total of 10 flags. To obtain a\nfull set of flags will cost you 5000 coins. Would you\nlike to buy these flags?"
"Yes, please." "No thanks."
NPC: "Here you are then, dear."

talk-to
NPC: "Oh hello there, dear. How can I help you?"
"Where are we?" "Have you got any flags?" "Do you have a spare spade?" "Do you have anything for trade?" "Nothing. I'm fine, thanks."

Player: "Do you have a spare spade?"
NPC: "Why, of course. I can sell you one for 5 gold pieces."
"Okay, thanks." "Actually, I've changed my mind."
NPC: "Here you are, then."

inspect:
chat log: "You examine the hole to see what might be in it."
chat dialog: "You notice a seed hidden in the dirt."

Farmer overhead:
"Ah, another flag to clear. Let's see what's there."
"Ah! A seed. Points for everyone near me!"

https://www.youtube.com/watch?v=UjxfJdHkJnM
inspect:
"Oh dear! It looks like you dug up a potato seed by mistake."
Farmer overhead:
"Hmm. Looks like there's a plant here."
"Gracious me! This one's dead"

flag:
"You add a flag to the patch."

digging ogleroot:
"You uncover a rather odd-looking root vegetable."

https://www.youtube.com/watch?v=fKyy0sgrBYM
Rabbit overhead:
"Squeak!"

https://www.youtube.com/watch?v=RnhNrwbUjjQ
Player: "Have you got any flags?"
NPC: "Let me check for you."
NPC: "Ah! First things first. One of the farmers dropped off\nsome flags for you. You can have them back. Here you\ngo."

NPC: "It looks like you have all the flags you need. You don't\nneed to buy any more."

inspect:
"You notice a seed hidden in the dirt."
"A slimy worm wriggles out of the mud, making you jump and lose\nconcentration. You're not sure if there is a seed here or not."
"You are certain there is no seed planted here."

farmer overhead:
"Hmm, no seeds planted here, I'm afraid."
"I'll have to keep this 'ere flag. Sorry."

*/
