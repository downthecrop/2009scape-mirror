package rs09.game.node.entity.skill.magic

import core.game.content.activity.mta.impl.GraveyardZone
import core.game.content.global.Bones
import core.game.node.entity.Entity
import core.game.node.entity.impl.Animator.Priority
import core.game.node.entity.impl.Projectile
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.entity.skill.smithing.smelting.Bar
import core.game.node.entity.skill.smithing.smelting.SmeltingPulse
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import rs09.ServerConstants
import rs09.game.node.entity.skill.magic.spellconsts.Modern

class ModernListeners : SpellListener("modern"){

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
    private val LOW_ANIMATION = Animation(716, Priority.HIGH)
    private val HIGH_ANIMATION = Animation(729, Priority.HIGH)
    private val LOW_ALCH_ANIM = Animation(712)
    private val LOW_ALCH_GFX = Graphics(112,5)
    private val HIGH_ALCH_ANIM = Animation(713)
    private val HIGH_ALCH_GFX = Graphics(113,5)
    private val BONE_CONVERT_GFX = Graphics(141, 96)
    private val BONE_CONVERT_ANIM = Animation(722)


    override fun defineListeners() {

        onCast(Modern.HOME_TELEPORT,NONE){player, _ ->
            requires(player)
            player.teleporter.send(ServerConstants.HOME_LOCATION,TeleportManager.TeleportType.HOME)
            setDelay(player,true)
        }

        onCast(Modern.VARROCK_TELEPORT,NONE){player, _->
            requires(player,25, arrayOf(Item(Items.FIRE_RUNE_554),Item(Items.AIR_RUNE_556,3),Item(Items.LAW_RUNE_563)))
            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK,1, 13)
            sendTeleport(player,35.0,Location.create(3213, 3424, 0))
        }

        onCast(Modern.LUMBRIDGE_TELEPORT,NONE){player,_ ->
            requires(player,31, arrayOf(Item(Items.EARTH_RUNE_557),Item(Items.AIR_RUNE_556,3),Item(Items.LAW_RUNE_563)))
            player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 2, 2)
            sendTeleport(player,41.0,Location.create(3221, 3219, 0))
        }

        onCast(Modern.FALADOR_TELEPORT,NONE){player, _ ->
            requires(player,37, arrayOf(Item(Items.WATER_RUNE_555),Item(Items.AIR_RUNE_556,3),Item(Items.LAW_RUNE_563)))
            sendTeleport(player,47.0,Location.create(2965, 3378, 0))
        }

        onCast(Modern.CAMELOT_TELEPORT,NONE){player, _->
            requires(player,45, arrayOf(Item(Items.AIR_RUNE_556,5),Item(Items.LAW_RUNE_563)))
            player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 1, 5);
            sendTeleport(player,55.5, Location.create(2758, 3478, 0))
        }

        onCast(Modern.ARDOUGNE_TELEPORT,NONE){player,_ ->
            requires(player,51, arrayOf(Item(Items.WATER_RUNE_555,2),Item(Items.LAW_RUNE_563,2)))
            sendTeleport(player,61.0, Location.create(2662, 3307, 0))
        }

        onCast(Modern.WATCHTOWER_TELEPORT,NONE){player,_ ->
            requires(player,58, arrayOf(Item(Items.EARTH_RUNE_557,2),Item(Items.LAW_RUNE_563,2)))
            sendTeleport(player,68.0, Location.create(2549, 3112, 0))
        }

        onCast(Modern.TROLLHEIM_TELEPORT,NONE){player, _ ->
            requires(player,61, arrayOf(Item(Items.FIRE_RUNE_554,2),Item(Items.LAW_RUNE_563,2)))
            sendTeleport(player,68.0, Location.create(2891, 3678, 0))
        }

        onCast(Modern.APE_ATOLL_TELEPORT,NONE){player,_ ->
            requires(player,64, arrayOf(Item(Items.FIRE_RUNE_554,2),Item(Items.WATER_RUNE_555,2),Item(Items.LAW_RUNE_563,2),Item(Items.BANANA_1963)))
            sendTeleport(player,74.0, Location.create(2754, 2784, 0))
        }

        onCast(Modern.TELEPORT_TO_HOUSE,NONE){player, _ ->
            requires(player,40, arrayOf(Item(Items.LAW_RUNE_563), Item(Items.AIR_RUNE_556), Item(Items.EARTH_RUNE_557)))
            attemptHouseTeleport(player)
        }

        onCast(Modern.LOW_ALCHEMY,ITEM){player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player,21, arrayOf(Item(Items.FIRE_RUNE_554,3),Item(Items.NATURE_RUNE_561)))
            alchemize(player,item,high = false)
        }

        onCast(Modern.HIGH_ALCHEMY,ITEM){player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player,55, arrayOf(Item(Items.FIRE_RUNE_554,5),Item(Items.NATURE_RUNE_561,1)))
            alchemize(player,item,high = true)
        }

        onCast(Modern.SUPERHEAT,ITEM){player, node ->
            val item = node?.asItem() ?: return@onCast
            requires(player,43, arrayOf(Item(Items.FIRE_RUNE_554,4),Item(Items.NATURE_RUNE_561,1)))
            superheat(player,item)
        }

        onCast(Modern.BONES_TO_BANANAS,NONE){player, _ ->
            requires(player,15, arrayOf(Item(Items.EARTH_RUNE_557,2), Item(Items.WATER_RUNE_555,2), Item(Items.NATURE_RUNE_561,1)))
            boneConvert(player,true)
        }

        onCast(Modern.BONES_TO_PEACHES,NONE){player, _ ->
            requires(player,60, arrayOf(Item(Items.EARTH_RUNE_557,4), Item(Items.WATER_RUNE_555,4), Item(Items.NATURE_RUNE_561,2)))
            boneConvert(player,false)
        }
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
                    val amount = inInventory * (GraveyardZone.BoneType.forItem(Item(item.id)).ordinal + 1)
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
        player.audioManager.send(Audio(114))
        removeRunes(player)
        addXP(player,if(bananas) 25.0 else 65.0)
        setDelay(player,false)
    }

    private fun superheat(player: Player,item: Item){
        if(!item.name.contains("ore") && !item.name.toLowerCase().equals("coal")){
            player.sendMessage("You can only cast this spell on ore.")
            return
        }

        var bar = Bar.forOre(item.id) ?: return
        if(bar == Bar.IRON && player.inventory.getAmount(Items.COAL_453) >= 2 && player.skills.getLevel(Skills.SMITHING) >= Bar.STEEL.level && player.inventory.contains(Items.IRON_ORE_441,1)) bar = Bar.STEEL

        if(player.skills.getLevel(Skills.SMITHING) < bar.level){
            player.sendMessage("You need a smithing level of ${bar.level} to superheat that ore.")
            return
        }

        for (items in bar.ores) {
            if (!player.inventory.contains(items.id, items.amount)) {
                player.packetDispatch.sendMessage("You do not have the required ores to make this bar.")
                return
            }
        }

        player.lock(3)
        removeRunes(player)
        addXP(player,53.0)
        player.audioManager.send(117)
        showMagicTab(player)
        player.pulseManager.run(SmeltingPulse(player, item, bar, 1, true))
        setDelay(player,false)
    }

    private fun alchemize(player: Player,item: Item,high:Boolean){
        if(item.name == "Coins") player.sendMessage("You can't alchemize something that's already gold!").also { return }
        if(!item.definition.isTradeable) player.sendMessage("You can't cast this spell on something like that.").also { return }

        if(player.zoneMonitor.isInZone("Alchemists' Playground")){
            player.sendMessage("You can only alch items from the cupboards!")
            return
        }

        val coins = Item(995, item.definition.getAlchemyValue(high))
        if (coins.amount > 0 && !player.inventory.hasSpaceFor(coins)) {
            player.sendMessage("Not enough space in your inventory!")
            return
        }

        player.lock(3)

        if(player.inventory.remove(Item(item.id,1))) {
            removeRunes(player)
            if (high) {
                visualizeSpell(player,HIGH_ALCH_ANIM,HIGH_ALCH_GFX,97)
            } else {
                visualizeSpell(player,LOW_ALCH_ANIM,LOW_ALCH_GFX,98)
            }

            if(coins.amount > 0)
                player.inventory.add(coins)

            if((item.id == Items.MAGIC_SHORTBOW_861 || item.id == Items.MAGIC_LONGBOW_859) && high && ZoneBorders(2721,3493,2730,3487).insideBorder(player)){
                player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 2, 6);
            }

            addXP(player,if(high) 65.0 else 31.0)
            showMagicTab(player)
            setDelay(player,false)
        }
    }

    private fun sendTeleport(player: Player, xp: Double, location: Location){
        if(player.locks.isTeleportLocked){
            player.removeAttribute("spell:runes")
            player.sendMessage("A magical force prevents you from teleporting.")
            return
        }
        if(player.teleporter.send(location,TeleportManager.TeleportType.NORMAL)) {
            removeRunes(player)
            addXP(player, xp)
            setDelay(player, true)
        }
    }

    private fun attemptHouseTeleport(player: Player){
        if(player.locks.isTeleportLocked){
            player.removeAttribute("spell:runes")
            player.sendMessage("A magical force prevents you from teleporting.")
            return
        }
        val loc = player.houseManager.location.exitLocation
        if(loc == null){
            player.sendMessage("You do not have a house whose portal you can teleport to.")
            return
        }

        player.teleporter.send(loc,TeleportManager.TeleportType.NORMAL)
        removeRunes(player)
        addXP(player,30.0)
        setDelay(player,true)
    }
}
