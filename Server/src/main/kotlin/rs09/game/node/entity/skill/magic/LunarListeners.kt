package rs09.game.node.entity.skill.magic

import api.*
import core.game.component.Component
import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.cooking.CookableItems
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Components
import org.rs09.consts.Items
import rs09.game.node.entity.skill.farming.FarmingPatch
import rs09.game.node.entity.skill.magic.spellconsts.Lunar
import rs09.game.system.config.NPCConfigParser

class LunarListeners : SpellListener("lunar") {
    private val BAKE_PIE_ANIM = Animation(4413)
    private val BAKE_PIE_GFX = Graphics(746,75)
    private val STATSPY_ANIM = Animation(6293)
    private val STATSPY_GFX = Graphics(1059)
    private val CURE_PLANT_ANIM = Animation(4409)
    private val CURE_PLANT_GFX = Graphics(742,100)
    private val NPC_CONTACT_ANIM = Animation(4413)
    private val NPC_CONTACT_GFX = Graphics(730,130)
    private val PLANK_MAKE_ANIM = Animation(6298)
    private val PLANK_MAKE_GFX = Graphics(1063, 120)

    override fun defineListeners() {

        onCast(Lunar.HOME_TELEPORT, NONE){player, _ ->
            requires(player)
            sendTeleport(player,0.0, Location.create(2100, 3914, 0))
        }

        onCast(Lunar.MOONCLAN_TELEPORT, NONE){player, _ ->
            requires(player,69, arrayOf(Item(Items.ASTRAL_RUNE_9075,2), Item(Items.LAW_RUNE_563,1), Item(Items.EARTH_RUNE_557,2)))
            sendTeleport(player,66.0, Location.create(2111, 3916, 0))
        }

        onCast(Lunar.MOONCLAN_GR_TELEPORT, NONE){player, _ ->
            requires(player,70, arrayOf(Item(Items.ASTRAL_RUNE_9075,2), Item(Items.LAW_RUNE_563,1), Item(Items.EARTH_RUNE_557,4)))
            sendGroupTeleport(player,67.0,"Moonclan Island",Location.create(2111, 3916, 0))
        }

        onCast(Lunar.OURANIA_TELEPORT, NONE){player,_ ->
            requires(player,71, arrayOf(Item(Items.ASTRAL_RUNE_9075,2), Item(Items.LAW_RUNE_563,1), Item(Items.EARTH_RUNE_557,6)))
            sendTeleport(player,69.0, Location.create(2469, 3247, 0))
        }

        onCast(Lunar.WATERBIRTH_TELEPORT, NONE){player, _ ->
            requires(player,72, arrayOf(Item(Items.ASTRAL_RUNE_9075,2), Item(Items.LAW_RUNE_563), Item(Items.WATER_RUNE_555)))
            sendTeleport(player,71.0, Location.create(2527, 3739, 0))
        }

        onCast(Lunar.WATERBIRTH_GR_TELEPORT, NONE){player,_ ->
            requires(player,73, arrayOf(Item(Items.ASTRAL_RUNE_9075,2), Item(Items.LAW_RUNE_563), Item(Items.WATER_RUNE_555,5)))
            sendGroupTeleport(player,72.0,"Waterbirth Island", Location.create(2527, 3739, 0))
        }

        onCast(Lunar.BARBARIAN_TELEPORT, NONE){player,_ ->
            requires(player,75, arrayOf(Item(Items.ASTRAL_RUNE_9075,2), Item(Items.LAW_RUNE_563,2), Item(Items.FIRE_RUNE_554,3)))
            sendTeleport(player,76.0, Location.create(2544, 3572, 0))
        }

        onCast(Lunar.BARBARIAN_GR_TELEPORT, NONE){player,_ ->
            requires(player,77, arrayOf(Item(Items.ASTRAL_RUNE_9075,2), Item(Items.LAW_RUNE_563,2), Item(Items.FIRE_RUNE_554,6)))
            sendGroupTeleport(player,77.0,"Barbarian Outpost", Location.create(2544, 3572, 0))
        }

        onCast(Lunar.KHAZARD_TELEPORT, NONE){player,_ ->
            requires(player,78, arrayOf(Item(Items.ASTRAL_RUNE_9075,2), Item(Items.LAW_RUNE_563,2), Item(Items.WATER_RUNE_555,4)))
            sendTeleport(player,80.0, Location.create(2656, 3157, 0))
        }

        onCast(Lunar.KHAZARD_GR_TELEPORT, NONE){player,_ ->
            requires(player,79, arrayOf(Item(Items.ASTRAL_RUNE_9075,2), Item(Items.LAW_RUNE_563,2), Item(Items.WATER_RUNE_555,8)))
            sendGroupTeleport(player,81.0, "Port Khazard", Location.create(2656, 3157, 0))
        }

        onCast(Lunar.FISHING_GUILD_TELEPORT, NONE){player,_ ->
            requires(player,85, arrayOf(Item(Items.ASTRAL_RUNE_9075,3), Item(Items.LAW_RUNE_563,3), Item(Items.WATER_RUNE_555,10)))
            sendTeleport(player,89.0, Location.create(2611, 3393, 0))
        }

        onCast(Lunar.FISHING_GUILD_GR_TELEPORT, NONE){player,_ ->
            requires(player,86, arrayOf(Item(Items.ASTRAL_RUNE_9075,3), Item(Items.LAW_RUNE_563,3), Item(Items.WATER_RUNE_555,14)))
            sendGroupTeleport(player,90.0,"Fishing Guild", Location.create(2611, 3393, 0))
        }

        onCast(Lunar.CATHERBY_TELEPORT, NONE){player,_ ->
            requires(player,87, arrayOf(Item(Items.ASTRAL_RUNE_9075,3), Item(Items.LAW_RUNE_563,3), Item(Items.WATER_RUNE_555,10)))
            sendTeleport(player,92.0, Location.create(2804, 3433, 0))
        }

        onCast(Lunar.CATHERBY_GR_TELEPORT, NONE){player,_ ->
            requires(player,88, arrayOf(Item(Items.ASTRAL_RUNE_9075,3), Item(Items.LAW_RUNE_563,3), Item(Items.WATER_RUNE_555,15)))
            sendGroupTeleport(player,93.0,"Catherby", Location.create(2804, 3433, 0))
        }

        onCast(Lunar.ICE_PLATEAU_TELEPORT, NONE){player,_ ->
            requires(player,89, arrayOf(Item(Items.ASTRAL_RUNE_9075,3), Item(Items.LAW_RUNE_563,3), Item(Items.WATER_RUNE_555,8)))
            sendTeleport(player,96.0, Location.create(2972, 3873, 0))
        }

        onCast(Lunar.ICE_PLATEAU_GR_TELEPORT, NONE){player,_ ->
            requires(player,90, arrayOf(Item(Items.ASTRAL_RUNE_9075,3), Item(Items.LAW_RUNE_563,3), Item(Items.WATER_RUNE_555,16)))
            sendGroupTeleport(player,99.0, "Ice Plateau", Location.create(2972, 3873, 0))
        }

        onCast(Lunar.BAKE_PIE, NONE){player,_ ->
            requires(player,65, arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.FIRE_RUNE_554,5), Item(Items.WATER_RUNE_555,4)))
            bakePie(player)
        }

        onCast(Lunar.MONSTER_EXAMINE, NPC){player,node ->
            requires(player,66, arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.MIND_RUNE_558), Item(Items.COSMIC_RUNE_564)))
            examineMonster(player,node!!.asNpc())
        }

        onCast(Lunar.CURE_PLANT, OBJECT){player,node ->
            requires(player,66, arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.EARTH_RUNE_557,8)))
            curePlant(player,node!!.asScenery())
        }

        onCast(Lunar.NPC_CONTACT, NONE){ player, _ ->
            requires(player,67, arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.COSMIC_RUNE_564), Item(Items.AIR_RUNE_556,2)))
            player.interfaceManager.open(Component(429))
            player.setAttribute("contact-caller"){
                removeRunes(player)
                addXP(player,63.0)
                setDelay(player,false)
                visualizeSpell(player,NPC_CONTACT_ANIM,NPC_CONTACT_GFX)
            }
        }

        onCast(Lunar.PLANK_MAKE, ITEM) { player, node ->
            requires(player, 86, arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.NATURE_RUNE_561, 1), Item(Items.EARTH_RUNE_557, 15)))
            plankMake(player, node!!.asItem())
        }
    }

    private fun plankMake(player: Player, item: Item) {

        val plankType = PlankType.getForLog(item)
        if (plankType == null) {
            sendMessage(player, "You need to use this spell on logs.")
            return
        }
        if (!removeItem(player, Item(Items.COINS_995, plankType.price))) {
            sendMessage(player, "You need ${plankType.price} coins to convert that log into a plank.")
            return
        }
        lock(player, 3)
        setDelay(player, false)
        visualizeSpell(player, PLANK_MAKE_ANIM, PLANK_MAKE_GFX, 3617)
        removeRunes(player)
        replaceSlot(player, item.slot, plankType.plank)
        addXP(player, 90.0)
        showMagicTab(player)
    }

    enum class PlankType (val log: Item, val plank: Item, val price: Int) {
        WOOD(Item(1511), Item(960), 70),
        OAK(Item(1521), Item(8778), 175),
        TEAK(Item(6333), Item(8780), 350),
        MAHOGANY(Item(6332), Item(8782), 1050);
        companion object {
            fun getForLog(item: Item): PlankType? {
                for (plankType in values()) {
                    if (plankType.log.id == item.id) {
                        return plankType
                    }
                }
                return null
            }
        }
    }

    fun curePlant(player: Player, obj: Scenery){
        val fPatch = FarmingPatch.forObject(obj)
        if(fPatch == null){
            player.sendMessage("You attempt to cast Cure Plant on ${obj.definition.name}!")
            player.sendMessage("It's not very effective....")
            return
        }
        val patch = fPatch.getPatchFor(player)
        if(!patch.isDiseased && !patch.isWeedy()){
            player.sendMessage("This patch is already growing fine.")
            return
        }
        if(patch.isWeedy()){
            player.sendMessage("Trust me lad, the weeds are healthy enough as is.")
            return
        }
        if(patch.isDead){
            player.sendMessage("It says 'Cure' not 'Resurrect'. Although death may arise from disease, it is not in itself a disease and hence cannot be cured. So there.")
            return
        }

        patch.cureDisease()
        removeRunes(player)
        addXP(player,60.0)
        visualizeSpell(player,CURE_PLANT_ANIM,CURE_PLANT_GFX)
        setDelay(player,false)
    }

    private fun examineMonster(player: Player, npc: NPC){
        if(!npc.location.withinDistance(player.location)){
            player.sendMessage("You must get closer to use this spell.")
            return
        }

        player.faceLocation(npc.location)
        visualizeSpell(player,STATSPY_ANIM,STATSPY_GFX)
        removeRunes(player)
        addXP(player,66.0)
        setDelay(player,false)

        player.interfaceManager.openSingleTab(Component(Components.DREAM_MONSTER_STAT_522))
        player.packetDispatch.sendString("Monster name : " + npc.definition.name,Components.DREAM_MONSTER_STAT_522,0)
        player.packetDispatch.sendString("Combat Level : ${npc.definition.combatLevel}",Components.DREAM_MONSTER_STAT_522,1)
        player.packetDispatch.sendString("Hitpoints : ${npc.definition.handlers.get(NPCConfigParser.LIFEPOINTS) ?: 0}",Components.DREAM_MONSTER_STAT_522,2)
        player.packetDispatch.sendString("Max hit : ${npc.getSwingHandler(false).calculateHit(npc,player,1.0)}",Components.DREAM_MONSTER_STAT_522,3)

        val poisonStatus = if(npc.definition.handlers.getOrDefault(NPCConfigParser.POISON_IMMUNE,false) == true){
            "This creature is immune to poison."
        } else "This creature is not immune to poison."

        player.packetDispatch.sendString(poisonStatus,Components.DREAM_MONSTER_STAT_522,4)
    }

    private fun bakePie(player: Player){
        val playerPies = ArrayList<Item>()

        for(item in player.inventory.toArray()){
            if(item == null) continue
            val pie = CookableItems.forId(item.id) ?: continue
            if(!pie.name.toLowerCase().contains("pie")) continue
            if(player.skills.getLevel(Skills.COOKING) < pie.level) continue
            playerPies.add(item)
        }

        if(playerPies.isEmpty()){
            player.sendMessage("You have no pies which you have the level to cook.")
            return
        }

        player.pulseManager.run(object : Pulse(){
            var counter = 0
            override fun pulse(): Boolean {
                if(playerPies.isEmpty()) return true
                if(counter == 0) delay = BAKE_PIE_ANIM.definition.durationTicks + 1
                val item = playerPies.get(0)
                val pie = CookableItems.forId(item.id)
                player.visualize(BAKE_PIE_ANIM,BAKE_PIE_GFX)
                addXP(player,60.0)
                player.skills.addExperience(Skills.COOKING,pie.experience)
                setDelay(player,false)
                player.inventory.remove(item)
                player.inventory.add(Item(pie.cooked))
                playerPies.remove(item)
                if(playerPies.isNotEmpty()) removeRunes(player,false) else removeRunes(player,true)
                return false
            }
        })
    }

    private fun sendTeleport(player: Player, xp: Double, loc: Location){
        if(player.locks.isTeleportLocked){
            player.sendMessage("A magical force prevents you from teleporting.")
            return
        }
        if(player.teleporter.send(loc,TeleportManager.TeleportType.LUNAR)) {
            addXP(player, xp)
            removeRunes(player)
            setDelay(player, true)
        }
    }

    private fun sendGroupTeleport(player: Player, xp: Double, destName: String, loc: Location){
        RegionManager.getLocalPlayers(player,1).forEach {
            if(it == player) return@forEach
            if(it.locks.isTeleportLocked) return@forEach
            if(!it.isActive) return@forEach
            if(!it.settings.isAcceptAid) return@forEach
            if(it.ironmanManager.isIronman) return@forEach
            it.setAttribute("t-o_location",loc)
            it.interfaceManager.open(Component(Components.TELEPORT_OTHER_326))
            it.packetDispatch.sendString(player.username,Components.TELEPORT_OTHER_326,1)
            it.packetDispatch.sendString(destName,Components.TELEPORT_OTHER_326,3)
        }
        sendTeleport(player,xp,loc)
    }
}