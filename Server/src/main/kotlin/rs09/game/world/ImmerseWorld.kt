package rs09.game.world

import api.StartupListener
import core.game.node.entity.combat.CombatStyle
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import rs09.game.ai.general.GeneralBotCreator
import rs09.game.ai.general.scriptrepository.*
import rs09.game.ai.pvmbots.CombatBotAssembler
import rs09.game.ai.skillingbot.SkillingBotAssembler
import java.util.Timer
import java.util.concurrent.Executors
import kotlin.concurrent.schedule

class ImmerseWorld : StartupListener {

    override fun startup() {
        spawnBots()
    }

    companion object {
        var assembler = CombatBotAssembler()
        var skillingBotAssembler = SkillingBotAssembler()

        fun spawnBots()
        {
            if(GameWorld.settings!!.enable_bots)
            {
                Executors.newSingleThreadExecutor().execute {
                    Thread.currentThread().name = "BotSpawner"
                    immerseSeersAndCatherby()
                    immerseLumbridgeDraynor()
                    immerseVarrock()
                    immerseWilderness()
                    immerseFishingGuild()
                    immerseAdventurer()
                    // immerseSlayer()
                    immerseGE()
                }
            }
        }

        fun immerseAdventurer() {
            for (i in 0..(GameWorld.settings?.max_adv_bots ?: 50)) {
                var random: Long = (10000..300000).random().toLong()
                Timer().schedule(random) {
                    spawn_adventurers()
                }
            }
        }

        fun spawn_adventurers() {
            val lumbridge = Location.create(3221, 3219, 0)
            val tiers = listOf<CombatBotAssembler.Tier>(CombatBotAssembler.Tier.LOW, CombatBotAssembler.Tier.MED)
            GeneralBotCreator(
                Adventurer(CombatStyle.MELEE),
                assembler.MeleeAdventurer(tiers.random(), lumbridge)
            )
            GeneralBotCreator(
                Adventurer(CombatStyle.RANGE),
                assembler.RangeAdventurer(tiers.random(), lumbridge)
            )
        }

        fun immerseFishingGuild() {
            val fishingGuild = Location.create(2604, 3421, 0)
            for (i in (0..4)) {
                GeneralBotCreator(SharkCatcher(), fishingGuild)
            }
        }

        fun immerseSeersAndCatherby() {
            GeneralBotCreator(
                SeersMagicTrees(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.AVERAGE, Location.create(2702, 3397, 0))
            )
            GeneralBotCreator(
                SeersFlax(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(2738, 3444, 0))
            )
            GeneralBotCreator(
                FletchingBankstander(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.AVERAGE, Location.create(2722, 3493, 0))
            )
            GeneralBotCreator(
                GlassBlowingBankstander(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.AVERAGE, Location.create(2807, 3441, 0))
            )
            GeneralBotCreator(LobsterCatcher(), Location.create(2805, 3435, 0))
        }

        fun immerseLumbridgeDraynor() {
            GeneralBotCreator(
                CowKiller(),
                assembler.produce(
                    CombatBotAssembler.Type.RANGE,
                    CombatBotAssembler.Tier.MED,
                    Location.create(3261, 3269, 0)
                )
            )
            GeneralBotCreator(
                CowKiller(),
                assembler.produce(
                    CombatBotAssembler.Type.MELEE,
                    CombatBotAssembler.Tier.LOW,
                    Location.create(3261, 3269, 0)
                )
            )
            GeneralBotCreator(
                CowKiller(),
                assembler.produce(
                    CombatBotAssembler.Type.MELEE,
                    CombatBotAssembler.Tier.MED,
                    Location.create(3257, 3267, 0)
                )
            )
            GeneralBotCreator(
                ManThiever(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3235, 3213, 0))
            )
            GeneralBotCreator(
                FarmerThiever(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3094, 3243, 0))
            )
            GeneralBotCreator(
                FarmerThiever(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3094, 3243, 0))
            )
            GeneralBotCreator(
                DraynorWillows(),
                skillingBotAssembler.produce(
                    SkillingBotAssembler.Wealth.values().random(),
                    Location.create(3094, 3245, 0)
                )
            )
            GeneralBotCreator(
                DraynorWillows(),
                skillingBotAssembler.produce(
                    SkillingBotAssembler.Wealth.values().random(),
                    Location.create(3094, 3245, 0)
                )
            )
            GeneralBotCreator(
                DraynorWillows(),
                skillingBotAssembler.produce(
                    SkillingBotAssembler.Wealth.values().random(),
                    Location.create(3094, 3245, 0)
                )
            )
            GeneralBotCreator(
                DraynorFisher(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3095, 3246, 0))
            )
            GeneralBotCreator(
                DraynorFisher(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3095, 3246, 0))
            )
            GeneralBotCreator(
                DraynorFisher(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3095, 3246, 0))
            )
        }

        fun immerseVarrock() {
            val WestBankIdlerBorders = ZoneBorders(3184, 3435, 3187, 3444)
            GeneralBotCreator(
                GlassBlowingBankstander(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.RICH, Location.create(3189, 3435, 0))
            )
            GeneralBotCreator(
                FletchingBankstander(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.AVERAGE, Location.create(3189, 3439, 0))
            )
            GeneralBotCreator(
                Idler(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.RICH, WestBankIdlerBorders.randomLoc)
            )
            GeneralBotCreator(
                GlassBlowingBankstander(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3256, 3420, 0))
            )
            GeneralBotCreator(
                VarrockEssenceMiner(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3253, 3420, 0))
            )
            GeneralBotCreator(
                VarrockSmither(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.RICH, Location.create(3189, 3436, 0))
            )
            GeneralBotCreator(
                NonBankingMiner(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3182, 3374, 0))
            )
        }

        fun immerseWilderness() {
            val wilderness = Location.create(3092, 3493, 0)

            repeat(6) {
                GeneralBotCreator (
                    GreenDragonKiller(CombatStyle.MELEE),
                    assembler.assembleMeleeDragonBot(CombatBotAssembler.Tier.MED, wilderness)
                )
            }
        }

        fun immerseFalador() {
            GeneralBotCreator(
                CoalMiner(),
                skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, Location.create(3037, 9737, 0))
            )
        }

        fun immerseSlayer() {
            GeneralBotCreator(
                GenericSlayerBot(),
                assembler.produce(
                    CombatBotAssembler.Type.MELEE,
                    CombatBotAssembler.Tier.HIGH,
                    Location.create(2673, 3635, 0)
                )
            )
        }

        private fun immerseGE() {
            spawnDoubleMoneyBot(false)
        }

        fun spawnDoubleMoneyBot(delay: Boolean) {
            if (GameWorld.settings?.enable_doubling_money_scammers != true) return
            val random: Long = (10_000..7_200_000).random().toLong()
            Timer().schedule(if (delay) random else 0) {
                GeneralBotCreator (
                    DoublingMoney(),
                    skillingBotAssembler.produce(SkillingBotAssembler.Wealth.POOR, DoublingMoney.startingLocs.random())
                )
            }
        }
    }
}
