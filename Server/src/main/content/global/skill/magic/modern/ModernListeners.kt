package content.global.skill.magic.modern

import content.global.skill.magic.SpellListener
import content.global.skill.magic.SpellUtils.hasRune
import content.global.skill.magic.TeleportMethod
import content.global.skill.magic.spellconsts.Modern
import content.global.skill.prayer.Bones
import content.global.skill.smithing.smelting.Bar
import content.global.skill.smithing.smelting.SmeltingPulse
import core.ServerConstants
import core.api.*
import core.game.event.ItemAlchemizationEvent
import core.game.event.ResourceProducedEvent
import core.game.event.TeleportEvent
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.spell.MagicStaff
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import org.rs09.consts.Sounds

class ModernListeners : SpellListener("modern"){
    override fun defineListeners() {
        onCast(Modern.HOME_TELEPORT, NONE){ player, _ ->
            if (!getAttribute(player, "tutorial:complete", false)) {
                return@onCast
            }
            requires(player)
            player.teleporter.send(ServerConstants.HOME_LOCATION,TeleportManager.TeleportType.HOME)
            setDelay(player,true)
        }

        onCast(Modern.VARROCK_TELEPORT, NONE){ player, _->
            requires(player,25, arrayOf(Item(Items.FIRE_RUNE_554),Item(Items.AIR_RUNE_556,3),Item(Items.LAW_RUNE_563)))
            val alternateTeleport = getAttribute(player, "diaries:varrock:alttele", false)
            val dest = if(alternateTeleport) Location.create(3165, 3472, 0) else Location.create(3213, 3424, 0)
            sendTeleport(player,35.0, dest)
        }

        onCast(Modern.LUMBRIDGE_TELEPORT, NONE){ player, _ ->
            requires(player,31, arrayOf(Item(Items.EARTH_RUNE_557),Item(Items.AIR_RUNE_556,3),Item(Items.LAW_RUNE_563)))
            sendTeleport(player,41.0,Location.create(3221, 3219, 0))
        }

        onCast(Modern.FALADOR_TELEPORT, NONE){ player, _ ->
            requires(player,37, arrayOf(Item(Items.WATER_RUNE_555),Item(Items.AIR_RUNE_556,3),Item(Items.LAW_RUNE_563)))
            sendTeleport(player,47.0,Location.create(2965, 3378, 0))
        }

        onCast(Modern.CAMELOT_TELEPORT, NONE){ player, _->
            requires(player,45, arrayOf(Item(Items.AIR_RUNE_556,5),Item(Items.LAW_RUNE_563)))
            player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 1, 5)
            sendTeleport(player,55.5, Location.create(2758, 3478, 0))
        }

        onCast(Modern.ARDOUGNE_TELEPORT, NONE){ player, _ ->
            if (!hasRequirement(player, "Plague City"))
                return@onCast
            requires(player,51, arrayOf(Item(Items.WATER_RUNE_555,2),Item(Items.LAW_RUNE_563,2)))
            sendTeleport(player,61.0, Location.create(2662, 3307, 0))
        }

        onCast(Modern.WATCHTOWER_TELEPORT, NONE){ player, _ ->
            if (!hasRequirement(player, "Watchtower"))
                return@onCast
            requires(player,58, arrayOf(Item(Items.EARTH_RUNE_557,2),Item(Items.LAW_RUNE_563,2)))
            sendTeleport(player,68.0, Location.create(2549, 3112, 0))
        }

        onCast(Modern.TROLLHEIM_TELEPORT, NONE){ player, _ ->
            if (!hasRequirement(player, "Eadgar's Ruse"))
                return@onCast
            requires(player,61, arrayOf(Item(Items.FIRE_RUNE_554,2),Item(Items.LAW_RUNE_563,2)))
            sendTeleport(player,68.0, Location.create(2891, 3678, 0))
        }

        onCast(Modern.APE_ATOLL_TELEPORT, NONE){ player, _ ->
            if (!hasRequirement(player, "Monkey Madness"))
                return@onCast
            requires(player,64, arrayOf(Item(Items.FIRE_RUNE_554,2),Item(Items.WATER_RUNE_555,2),Item(Items.LAW_RUNE_563,2),Item(Items.BANANA_1963)))
            sendTeleport(player,74.0, Location.create(2754, 2784, 0))
        }

        onCast(Modern.TELEPORT_TO_HOUSE, NONE){ player, _ ->
            requires(player,40, arrayOf(Item(Items.LAW_RUNE_563), Item(Items.AIR_RUNE_556), Item(Items.EARTH_RUNE_557)))
            attemptHouseTeleport(player)
        }

        onCast(Modern.LOW_ALCHEMY, ITEM){ player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player,21, arrayOf(Item(Items.FIRE_RUNE_554,3),Item(Items.NATURE_RUNE_561)))
            alchemize(player,item,high = false)
        }

        onCast(Modern.HIGH_ALCHEMY, ITEM){ player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player,55, arrayOf(Item(Items.FIRE_RUNE_554,5),Item(Items.NATURE_RUNE_561,1)))
            alchemize(player,item,high = true)
        }

        onCast(Modern.SUPERHEAT, ITEM){ player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player,43, arrayOf(Item(Items.FIRE_RUNE_554,4),Item(Items.NATURE_RUNE_561,1)))
            superheat(player,item)
        }

        onCast(Modern.BONES_TO_BANANAS, NONE){ player, _ ->
            requires(player,15, arrayOf(Item(Items.EARTH_RUNE_557,2), Item(Items.WATER_RUNE_555,2), Item(Items.NATURE_RUNE_561,1)))
            boneConvert(player,true)
        }

        onCast(Modern.BONES_TO_PEACHES, NONE){ player, _ ->
            requires(player,60, arrayOf(Item(Items.EARTH_RUNE_557,4), Item(Items.WATER_RUNE_555,4), Item(Items.NATURE_RUNE_561,2)))
            boneConvert(player,false)
        }

        onCast(Modern.CHARGE_WATER_ORB, OBJECT, Scenery.OBELISK_OF_WATER_2151, 3, method = ::chargeOrb)
        onCast(Modern.CHARGE_EARTH_ORB, OBJECT, Scenery.OBELISK_OF_EARTH_29415, 3, method = ::chargeOrb)
        onCast(Modern.CHARGE_FIRE_ORB, OBJECT, Scenery.OBELISK_OF_FIRE_2153, 3, method = ::chargeOrb)
        onCast(Modern.CHARGE_AIR_ORB, OBJECT, Scenery.OBELISK_OF_AIR_2152, 3, method = ::chargeOrb)
    }

    private fun boneConvert(player: Player,bananas: Boolean){
        val isInMTA = player.zoneMonitor.isInZone("Creature Graveyard")
        if(isInMTA && player.getAttribute("tablet-spell",false)){
            player.sendMessage("You can not use this tablet in the Mage Training Arena.")
            return
        }

        if(!bananas && !player.savedData.activityData.isBonesToPeaches && !player.getAttribute("tablet-spell",false)){
            player.sendMessage("You can only learn this spell from the Mage Training Arena.")
            return
        }

        val bones = if(isInMTA) intArrayOf(6904,6905,6906,6907) else Bones.values().map { it.itemId }.toIntArray()

        for(item in player.inventory.toArray()){
            item ?: continue
            if(isInMTA){
                if(bones.contains(item.id)){
                    val inInventory = player.inventory.getAmount(item.id)
                    val amount = inInventory * (content.minigame.mta.impl.GraveyardZone.BoneType.forItem(Item(item.id)).ordinal + 1)
                    if(amount > 0){
                        player.inventory.remove(Item(item.id,inInventory))
                        player.inventory.add(Item(if(bananas) Items.BANANA_1963 else Items.PEACH_6883,amount))
                    }
                }
            } else {
                if(bones.contains(item.id)){
                    val inInventory = player.inventory.getAmount(item.id)
                    player.inventory.remove(Item(item.id,inInventory))
                    player.inventory.add(Item(if(bananas) Items.BANANA_1963 else Items.PEACH_6883,inInventory))
                }
            }
        }
        visualizeSpell(player,BONE_CONVERT_ANIM, BONE_CONVERT_GFX)
        playAudio(player, Sounds.BONES_TO_BANANAS_ALL_114)
        removeRunes(player)
        addXP(player,if(bananas) 25.0 else 65.0)
        setDelay(player,false)
    }

    private fun superheat(player: Player,item: Item){
        if(!item.name.contains("ore") && !item.name.equals("coal", true)){
            player.sendMessage("You can only cast this spell on ore.")
            return
        }

        // Elemental Workshop I special interaction
        if(item.id == Items.ELEMENTAL_ORE_2892) {
            sendMessage(player, "Even this spell is not hot enough to heat this item.")
            return
        }

        fun returnBar(player: Player,item: Item): Bar? {
            // Loop through all metal bars starting with the highest tier
            for (potentialBar in Bar.values().reversed()) {
                // Check if the ore being cast on is needed for the current bar being considered
                val inputOreInBar = potentialBar.ores.map{it.id}.contains(item.id)
                // Check the player has all the required ores (and corresponding quantities) to make the current bar being considered
                val playerHasNecessaryOres = potentialBar.ores.all{ore -> inInventory(player, ore.id, ore.amount)}
                // If both tests pass return the current bar being considered as the one the spell should try to make
                if (inputOreInBar && playerHasNecessaryOres) return potentialBar
            }
            // If none of the bars passed both tests the player must be missing a required ore
            player.packetDispatch.sendMessage("You do not have the required ores to make this bar.")
            return null
        }
        var bar = returnBar(player,item)?: return

        if(player.skills.getLevel(Skills.SMITHING) < bar.level){
            player.sendMessage("You need a smithing level of ${bar.level} to superheat that ore.")
            return
        }

        player.lock(3)
        removeRunes(player)
        addXP(player,53.0)
        playAudio(player, Sounds.SUPERHEAT_ALL_190)
        showMagicTab(player)
        player.pulseManager.run(SmeltingPulse(player, item, bar, 1, true))
        setDelay(player,false)
    }

    fun alchemize(player: Player, item: Item, high: Boolean, explorersRing: Boolean = false): Boolean {
        if(item.name == "Coins") player.sendMessage("You can't alchemize something that's already gold!").also { return false }
        if((!item.definition.isTradeable) && (!item.definition.isAlchemizable)) player.sendMessage("You can't cast this spell on something like that.").also { return false }

        if(player.zoneMonitor.isInZone("Alchemists' Playground")){
            player.sendMessage("You can only alch items from the cupboards!")
            return false
        }

        val coins = Item(995, item.definition.getAlchemyValue(high))
        if (item.amount > 1 && coins.amount > 0 && !player.inventory.hasSpaceFor(coins)) {
            player.sendMessage("Not enough space in your inventory!")
            return false
        }

        if (player.pulseManager.current !is MovementPulse) {
            player.pulseManager.clear()
        }

        if (explorersRing) {
            visualize(player, LOW_ALCH_ANIM, EXPLORERS_RING_GFX)
        } else {
            val weapon = getItemFromEquipment(player, EquipmentSlot.WEAPON)
            if (weapon != null && weapon.id in MagicStaff.FIRE_RUNE.staves) {
                visualize(player, if (high) HIGH_ALCH_STAFF_ANIM else LOW_ALCH_STAFF_ANIM, if (high) HIGH_ALCH_STAFF_GFX else LOW_ALCH_STAFF_GFX)
            } else {
                visualize(player, if (high) HIGH_ALCH_ANIM else LOW_ALCH_ANIM, if (high) HIGH_ALCH_GFX else LOW_ALCH_GFX)
            }
        }
        playAudio(player, if (high) Sounds.HIGH_ALCHEMY_97 else Sounds.LOW_ALCHEMY_98)
        player.dispatch(ItemAlchemizationEvent(item.id, high))
        if (player.inventory.remove(Item(item.id, 1)) && coins.amount > 0) {
            player.inventory.add(coins)
        }
        removeRunes(player)
        addXP(player, if (high) 65.0 else 31.0)
        showMagicTab(player)
        setDelay(player, 5)
        return true
    }

    private fun sendTeleport(player: Player, xp: Double, location: Location){
        if(player.isTeleBlocked){
            player.removeAttribute("spell:runes")
            player.sendMessage("A magical force prevents you from teleporting.")
            return
        }

        val teleType = TeleportManager.TeleportType.NORMAL

        if (player.teleporter.send(location, teleType)) {
            player.dispatch(TeleportEvent(teleType, TeleportMethod.SPELL, -1, location))

            removeRunes(player)
            addXP(player, xp)
            setDelay(player, true)
        }
    }

    private fun attemptHouseTeleport(player: Player){
        if(player.isTeleBlocked){
            player.removeAttribute("spell:runes")
            player.sendMessage("A magical force prevents you from teleporting.")
            return
        }
        val hasHouse = player.houseManager.location.exitLocation != null
        if(!hasHouse){
            player.sendMessage("You do not have a house you can teleport to.")
            return
        }

        player.houseManager.preEnter(player, false)
        val teleType = TeleportManager.TeleportType.NORMAL
        val loc = player.houseManager.getEnterLocation()
        player.teleporter.send(loc, teleType)
        player.houseManager.postEnter(player, false) //this actually runs when the teleport is SUBMITTED rather than EXECUTED, but this is fine
        removeRunes(player)
        addXP(player,30.0)
        setDelay(player,true)
    }

    private fun chargeOrb(player: Player, node: Node?) {
        if (node == null) return
        val spell = ChargeOrbData.spellMap[node.id] ?: return
        requires(player, spell.level, spell.requiredRunes)
        removeAttribute(player, "spell:runes")
        face(player, node)
        sendSkillDialogue(player) {
            withItems(spell.chargedOrb)
            calculateMaxAmount { return@calculateMaxAmount amountInInventory(player, Items.UNPOWERED_ORB_567) }
            create { _, amount ->
                var crafted = 0
                queueScript(player, 0) {
                    if (!hasLevelDyn(player, Skills.CRAFTING, spell.level)) {
                        sendMessage(player, "You need a magic level of ${spell.level} to cast this spell.")
                        return@queueScript stopExecuting(player)
                    }
                    for (rune in spell.requiredRunes) {
                        if(!hasRune(player,rune)){
                            sendMessage(player, "You don't have enough ${rune.name.lowercase()}s to cast this spell.")
                            return@queueScript stopExecuting(player)
                        }
                    }
                    visualizeSpell(player, CHARGE_ORB_ANIM, spell.graphics, spell.sound)
                    removeRunes(player)
                    addItem(player, spell.chargedOrb)
                    addXP(player, spell.experience)
                    setDelay(player, 3)
                    crafted++

                    if (crafted == 5 && spell.chargedOrb == Items.WATER_ORB_571) {
                        player.dispatch(ResourceProducedEvent(spell.chargedOrb, crafted, node))
                    }
                    if (amount == crafted) { return@queueScript stopExecuting(player) }
                    setCurrentScriptState(player, 0)
                    return@queueScript delayScript(player, 6)
                }
            }
        }
        return
    }
    companion object {
        private val CONFUSE_START = Graphics(102, 96)
        private val CONFUSE_PROJECTILE = Projectile.create(null as Entity?, null, 103, 40, 36, 52, 75, 15, 11)
        private val CONFUSE_END = Graphics(104, 96)
        private val WEAKEN_START = Graphics(105, 96)
        private val WEAKEN_PROJECTILE = Projectile.create(null as Entity?, null, 106, 40, 36, 52, 75, 15, 11)
        private val WEAKEN_END = Graphics(107, 96)
        private val CURSE_START = Graphics(108, 96)
        private val CURSE_PROJECTILE = Projectile.create(null as Entity?, null, 109, 40, 36, 52, 75, 15, 11)
        private val CURSE_END = Graphics(110, 96)
        private val VULNER_START = Graphics(167, 96)
        private val VULNER_PROJECTILE = Projectile.create(null as Entity?, null, 168, 40, 36, 52, 75, 15, 11)
        private val VULNER_END = Graphics(169, 96)
        private val ENFEEBLE_START = Graphics(170, 96)
        private val ENFEEBLE_PROJECTILE = Projectile.create(null as Entity?, null, 171, 40, 36, 52, 75, 15, 11)
        private val ENFEEBLE_END = Graphics(172, 96)
        private val STUN_START = Graphics(173, 96)
        private val STUN_PROJECTILE = Projectile.create(null as Entity?, null, 174, 40, 36, 52, 75, 15, 11)
        private val STUN_END = Graphics(107, 96)
        private val LOW_ANIMATION = Animation(716, Animator.Priority.HIGH)
        private val HIGH_ANIMATION = Animation(729, Animator.Priority.HIGH)
        private val LOW_ALCH_ANIM = Animation(9623)
        private val LOW_ALCH_STAFF_ANIM = Animation(9625)
        private val HIGH_ALCH_ANIM = Animation(9631)
        private val HIGH_ALCH_STAFF_ANIM = Animation(9633)
        private val LOW_ALCH_GFX = Graphics(763)
        private val HIGH_ALCH_GFX = Graphics(1691)
        private val LOW_ALCH_STAFF_GFX = Graphics(1692)
        private val HIGH_ALCH_STAFF_GFX = Graphics(1693)
        private val EXPLORERS_RING_GFX = Graphics(1698)
        private val BONE_CONVERT_GFX = Graphics(141, 96)
        private val BONE_CONVERT_ANIM = Animation(722)
        private val CHARGE_ORB_ANIM = Animation(726)
    }
}