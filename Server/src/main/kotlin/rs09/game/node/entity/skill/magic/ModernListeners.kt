package rs09.game.node.entity.skill.magic

import core.game.node.entity.player.Player
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
        player.interfaceManager.setViewedTab(6)
        player.pulseManager.run(SmeltingPulse(player, item, bar, 1, true))
        setDelay(player,false)
    }

    private fun alchemize(player: Player,item: Item,high:Boolean){
        if(item.name == "Coins") player.sendMessage("You can't alchemize something that's already gold!").also { return }
        if(!item.definition.isTradeable) player.sendMessage("You can't cast this spell on something like that.").also { return }

        val coins = Item(995, item.definition.getAlchemyValue(high))
        if (coins.amount > 0 && !player.inventory.hasSpaceFor(coins)) {
            player.sendMessage("Not enough space in your inventory!")
            return
        }

        player.lock(3)

        if(player.inventory.remove(Item(item.id,1))) {
            removeRunes(player)
            player.audioManager.send(if (high) 97 else 98)
            if (high) {
                player.visualize(Animation(713), Graphics(113, 5))
            } else {
                player.visualize(Animation(712), Graphics(112, 5))
            }

            if(coins.amount > 0)
                player.inventory.add(coins)

            if((item.id == Items.MAGIC_SHORTBOW_861 || item.id == Items.MAGIC_LONGBOW_859) && high && ZoneBorders(2721,3493,2730,3487).insideBorder(player)){
                player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 2, 6);
            }

            addXP(player,if(high) 65.0 else 31.0)
            player.interfaceManager.setViewedTab(6)
            setDelay(player,false)
        }
    }

    private fun sendTeleport(player: Player, xp: Double, location: Location){
        player.teleporter.send(location,TeleportManager.TeleportType.NORMAL)
        removeRunes(player)
        addXP(player,xp)
        setDelay(player,true)
    }
}