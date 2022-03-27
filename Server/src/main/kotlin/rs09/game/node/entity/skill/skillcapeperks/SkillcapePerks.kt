package rs09.game.node.entity.skill.skillcapeperks

import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.runecrafting.Altar
import core.game.world.map.zone.impl.DarkZone
import core.plugin.Initializable
import rs09.game.world.GameWorld

enum class SkillcapePerks(val attribute: String, val effect: ((Player) -> Unit)? = null) {
    BAREFISTED_SMITHING("cape_perks:barefisted-smithing"),
    DIVINE_FAVOR("cape_perks:divine-favor"),
    CONSTANT_GLOW("cape_perks:eternal-glow"),
    PRECISION_MINER("cape_perks:precision-miner"),
    GREAT_AIM("cape_perks:great-aim"),
    NEST_HUNTER("cape_perks:nest-hunter"),
    PRECISION_STRIKES("cape_perks:precision-strikes"),
    FINE_ATTUNEMENT("cape_perks:fine-attunement"),
    GRAND_BULLWARK("cape_perks:grand-bullwark"),
    ACCURATE_MARKSMAN("cape_perks:accurate-marksman"),
    DAMAGE_SPONG("cape_perks:damage-sponge"),
    MARATHON_RUNNER("cape_perks:marathon-runner"),
    LIBRARIAN_MAGUS("cape_perks:librarian-magus",{player ->
       val time = player.getAttribute("cape_perks:librarian-magus-timer",0L)
        if(player.getAttribute("cape_perks:librarian-magus-charges",2) > 0 || System.currentTimeMillis() > time){
            if(System.currentTimeMillis() > time) player.setAttribute("/save:cape_perks:librarian-magus-charges",3)
            player.dialogueInterpreter.open(509871234)
            if(System.currentTimeMillis() > time)
                player.setAttribute("/save:cape_perks:librarian-magus-timer",System.currentTimeMillis() + java.util.concurrent.TimeUnit.DAYS.toMillis(1))
        }
    }),
    ABYSS_WARPING("cape_perks:abyss_warp",{ player ->
        val time = player.getAttribute("cape_perks:abyssal_warp_timer",0L)
        if(player.getAttribute("cape_perks:abyssal_warp",3) > 0 || System.currentTimeMillis() > time){
            if(System.currentTimeMillis() > time) player.setAttribute("/save:cape_perks:abyssal_warp",3)
            player.dialogueInterpreter.open(509871233)
            if(System.currentTimeMillis() > time)
                player.setAttribute("/save:cape_perks:abyssal_warp_timer",System.currentTimeMillis() + java.util.concurrent.TimeUnit.DAYS.toMillis(1))
        } else {
            player.dialogueInterpreter.sendDialogue("Your cape is still on cooldown.","Ready in " + java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(time - System.currentTimeMillis()) + " minutes.")
        }
    }),
    SEED_ATTRACTION("cape_perks:seed_attract",{player ->
        val time = player.getAttribute("cape_perks:seed_attract_timer",0L)
        if(System.currentTimeMillis() > time){
            val possibleSeeds = rs09.game.node.entity.skill.farming.Plantable.values()
            for(i in 0 until 10){
                var seed = possibleSeeds.random()
                while(seed == rs09.game.node.entity.skill.farming.Plantable.SCARECROW || seed.applicablePatch == rs09.game.node.entity.skill.farming.PatchType.FRUIT_TREE || seed.applicablePatch == rs09.game.node.entity.skill.farming.PatchType.TREE || seed.applicablePatch == rs09.game.node.entity.skill.farming.PatchType.SPIRIT_TREE){
                    seed = possibleSeeds.random()
                }
                val reward = core.game.node.item.Item(seed.itemID)
                if(!player.inventory.add(reward)){
                    core.game.node.item.GroundItemManager.create(reward,player)
                }
            }
            player.dialogueInterpreter.sendDialogue("You pluck off the seeds that were stuck to your cape.")
            player.setAttribute("/save:cape_perks:seed_attract_timer",System.currentTimeMillis() + java.util.concurrent.TimeUnit.DAYS.toMillis(1))
        } else {
            player.dialogueInterpreter.sendDialogue("Your cape is still on cooldown.","Ready in " + java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(time - System.currentTimeMillis()) + " minutes.")
        }
    }),
    TRICKS_OF_THE_TRADE("cape_perks:tott",{player ->
        val hasHelmetBonus = api.getAttribute(player, "cape_perks:tott:helmet-stored", false)
        if(hasHelmetBonus){
            api.sendDialogue(player, "Your cape's pockets are lined with all the utilities you need for slayer.")
        } else {
            api.sendDialogue(player, "Your cape is lined with empty pockets shaped like various utilities needed for slayer.")
        }
    }),
    HASTY_COOKING("cape_perks:hasty-cooking"),
    NONE("cape_perks:none")
    ;

    companion object{
        @JvmStatic
        fun isActive(perk: SkillcapePerks, player: Player): Boolean{
            return player.getAttribute(perk.attribute,false)
        }

        fun forSkillcape(skillcape: Skillcape): SkillcapePerks{
            return when(skillcape){
                Skillcape.ATTACK -> PRECISION_STRIKES
                Skillcape.STRENGTH -> FINE_ATTUNEMENT
                Skillcape.DEFENCE -> GRAND_BULLWARK
                Skillcape.RANGING -> ACCURATE_MARKSMAN
                Skillcape.PRAYER -> DIVINE_FAVOR
                Skillcape.MAGIC -> LIBRARIAN_MAGUS
                Skillcape.RUNECRAFTING -> ABYSS_WARPING
                Skillcape.HITPOINTS -> DAMAGE_SPONG
                Skillcape.AGILITY -> MARATHON_RUNNER
                Skillcape.HERBLORE -> NONE
                Skillcape.THIEVING -> NONE
                Skillcape.CRAFTING -> NONE
                Skillcape.FLETCHING -> NONE
                Skillcape.SLAYER -> TRICKS_OF_THE_TRADE
                Skillcape.CONSTRUCTION -> NONE
                Skillcape.MINING -> PRECISION_MINER
                Skillcape.SMITHING -> BAREFISTED_SMITHING
                Skillcape.FISHING -> GREAT_AIM
                Skillcape.COOKING -> HASTY_COOKING
                Skillcape.FIREMAKING -> CONSTANT_GLOW
                Skillcape.WOODCUTTING -> NONE
                Skillcape.FARMING -> SEED_ATTRACTION
                Skillcape.HUNTING -> NONE
                Skillcape.SUMMONING -> NONE
                else -> NONE
            }
        }
    }

    fun activate(player: Player){
        if(GameWorld.settings?.skillcape_perks != true){
            return
        }
        if(!isActive(this,player)){
            player.setAttribute("/save:$attribute",true)
        }
        player.debug("Activated ${this.name}")
        if(this == CONSTANT_GLOW)
            DarkZone.checkDarkArea(player)
    }

    fun operate(player: Player){
        if(GameWorld.settings?.skillcape_perks != true){
            player.sendMessage("This item can not be operated.")
            return
        }
        effect?.invoke(player)
    }

    fun deactivate(player: Player){
        player.removeAttribute(attribute)
        if(this == CONSTANT_GLOW)
            DarkZone.checkDarkArea(player)
    }

    @Initializable
    class MagicCapeDialogue(player: Player? = null): DialoguePlugin(player){
        override fun newInstance(player: Player?): DialoguePlugin {
            return MagicCapeDialogue(player)
        }

        override fun open(vararg args: Any?): Boolean {
            when(player.spellBookManager.spellBook){
                SpellBookManager.SpellBook.ANCIENT.interfaceId -> options("Modern","Lunar")
                SpellBookManager.SpellBook.MODERN.interfaceId -> options("Ancient","Lunar")
                SpellBookManager.SpellBook.LUNAR.interfaceId -> options ("Modern","Ancient")
            }
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            val spellbook = when(player.spellBookManager.spellBook){
                SpellBookManager.SpellBook.ANCIENT.interfaceId -> {
                    when(buttonId){
                        1 -> SpellBookManager.SpellBook.MODERN
                        2 -> SpellBookManager.SpellBook.LUNAR
                        else -> null
                    }
                }

                SpellBookManager.SpellBook.MODERN.interfaceId -> {
                    when(buttonId){
                        1 -> SpellBookManager.SpellBook.ANCIENT
                        2 -> SpellBookManager.SpellBook.LUNAR
                        else -> null
                    }
                }

                SpellBookManager.SpellBook.LUNAR.interfaceId -> {
                    when(buttonId){
                        1 -> SpellBookManager.SpellBook.MODERN
                        2 -> SpellBookManager.SpellBook.ANCIENT
                        else -> null
                    }
                }

                else -> null
            }

            if(spellbook != null){
                player.spellBookManager.setSpellBook(spellbook)
                player.interfaceManager.openTab(Component(spellbook.interfaceId))
                player.incrementAttribute("/save:cape_perks:librarian-magus-charges",-1)
            }
            end()
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(509871234)
        }

    }

    @Initializable
    class RCCapeDialogue(player: Player? = null) : DialoguePlugin(player){
        override fun newInstance(player: Player?): DialoguePlugin {
            return RCCapeDialogue(player)
        }

        override fun open(vararg args: Any?): Boolean {
            options("Air","Mind","Water","Earth","More...")
            stage = 0;
            return true
        }

        override fun handle(interfaceId: Int, buttonId: Int): Boolean {
            when(stage){
                0 -> when(buttonId){
                    1 -> sendAltar(player,Altar.AIR)
                    2 -> sendAltar(player,Altar.MIND)
                    3 -> sendAltar(player,Altar.WATER)
                    4 -> sendAltar(player,Altar.EARTH)
                    5 -> options("Fire","Body","Cosmic","Chaos","More...").also { stage++ }
                }
                1 -> when(buttonId){
                    1 -> sendAltar(player,Altar.FIRE)
                    2 -> sendAltar(player,Altar.BODY)
                    3 -> sendAltar(player,Altar.COSMIC)
                    4 -> sendAltar(player,Altar.CHAOS)
                    5 -> options("Astral","Nature","Law","Death","More...").also { stage++ }
                }
                2 -> when(buttonId){
                    1 -> sendAltar(player,Altar.ASTRAL)
                    2 -> sendAltar(player,Altar.NATURE)
                    3 -> sendAltar(player,Altar.LAW)
                    4 -> sendAltar(player,Altar.DEATH)
                    5 -> options("Blood","Nevermind").also { stage++ }
                }
                3 -> when(buttonId){
                    1 -> sendAltar(player,Altar.BLOOD)
                    2 -> end()
                }
            }
            return true
        }

        fun sendAltar(player: Player,altar: Altar){
            end()
            player.teleporter.send(altar.ruin.end,TeleportManager.TeleportType.TELE_OTHER)
            player.incrementAttribute("/save:cape_perks:abyssal_warp",-1)
        }

        override fun getIds(): IntArray {
            return intArrayOf(509871233)
        }

    }
}
