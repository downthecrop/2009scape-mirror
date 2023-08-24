package content.minigame.mta

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spellconsts.Modern
import core.api.playAudio
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import org.rs09.consts.Sounds

class MTAListeners : InteractionListener {
    override fun defineListeners() {
        on(NPCs.MAZE_GUARDIAN_3102, IntType.NPC, "talk-to"){ player, node ->
            player.dialogueInterpreter.open(node.id, node)
            return@on true
        }
        setDest(IntType.NPC, intArrayOf(NPCs.MAZE_GUARDIAN_3102), "talk-to") { player, node ->
			return@setDest node.location.transform(Direction.getDirection(player.location, node.location), -1)
        }
    }
}

class MTASpellListeners : SpellListener("modern"){
    private val LOW_ALCH_ANIM = Animation(712)
    private val LOW_ALCH_GFX = Graphics(112,5)
    private val HIGH_ALCH_ANIM = Animation(713)
    private val HIGH_ALCH_GFX = Graphics(113,5)
    private val MTA_ALCH_ITEMS = content.minigame.mta.impl.AlchemistZone.AlchemistItem.values().map{it.item.id}.toIntArray()

    override fun defineListeners() {

        onCast(Modern.HIGH_ALCHEMY,ITEM,*MTA_ALCH_ITEMS){ p, node ->
            val item = node?.asItem() ?: return@onCast
            requires(p,55)
            val alchItem = content.minigame.mta.impl.AlchemistZone.AlchemistItem.forItem(item.id) ?: return@onCast
            val coins = Item(content.minigame.mta.impl.AlchemistZone.COINS.id, alchItem.cost)
            val freeAlch = alchItem == content.minigame.mta.impl.AlchemistZone.freeConvert

            if (p.inventory.getAmount(content.minigame.mta.impl.AlchemistZone.COINS.id) + alchItem.cost > 10000) {
                p.dialogueInterpreter.sendDialogue("Warning: You can't deposit more than 12000 coins at a time.")
            }

            if (coins.amount > 1 && !p.inventory.hasSpaceFor(coins)) {
                p.packetDispatch.sendMessage("Not enough space in your inventory!")
                return@onCast
            }

            if(!freeAlch){
                requires(p,55,arrayOf(Item(Items.FIRE_RUNE_554,5),Item(Items.NATURE_RUNE_561,1)))
            }

            p.lock(3)
            p.visualize(HIGH_ALCH_ANIM,HIGH_ALCH_GFX)
            if(p.inventory.remove(Item(item.id,1))){
                playAudio(p, Sounds.HIGH_ALCHEMY_97)
                if (coins.amount != 0) {
                    p.inventory.add(coins)
                }
            }

            showMagicTab(p)
            addXP(p,65.0)
            setDelay(p,false)
            removeRunes(p)
        }

        onCast(Modern.LOW_ALCHEMY,ITEM,*MTA_ALCH_ITEMS){p, node ->
            val item = node?.asItem() ?: return@onCast
            requires(p,21)
            val alchItem = content.minigame.mta.impl.AlchemistZone.AlchemistItem.forItem(item.id) ?: return@onCast
            val coins = Item(content.minigame.mta.impl.AlchemistZone.COINS.id, alchItem.cost)
            val freeAlch = alchItem == content.minigame.mta.impl.AlchemistZone.freeConvert

            if (p.inventory.getAmount(content.minigame.mta.impl.AlchemistZone.COINS.id) + alchItem.cost > 10000) {
                p.dialogueInterpreter.sendDialogue("Warning: You can't deposit more than 12000 coins at a time.")
            }

            if (coins.amount > 1 && !p.inventory.hasSpaceFor(coins)) {
                p.packetDispatch.sendMessage("Not enough space in your inventory!")
                return@onCast
            }

            if(!freeAlch){
                requires(p,21,arrayOf(Item(Items.FIRE_RUNE_554,3),Item(Items.NATURE_RUNE_561,1)))
            }

            p.lock(3)
            p.visualize(LOW_ALCH_ANIM,LOW_ALCH_GFX)
            if(p.inventory.remove(Item(item.id,1))){
                playAudio(p, Sounds.HIGH_ALCHEMY_97)
                if (coins.amount != 0) {
                    p.inventory.add(coins)
                }
            }

            showMagicTab(p)
            addXP(p,31.0)
            setDelay(p,false)
            removeRunes(p)
        }

    }
}
