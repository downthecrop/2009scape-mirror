package rs09.game.interaction.item

import api.*
import core.game.node.entity.player.link.quest.QuestRepository
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.InteractionListeners.run

class ItemQuestRequirementListener : InteractionListener() {

    private val fremennikIslesEquipment = intArrayOf(Items.HELM_OF_NEITIZNOT_10828, Items.HELM_OF_NEITIZNOT_E_12680, Items.HELM_OF_NEITIZNOT_CHARGED_12681)
    private val fremennikTrialsEquipment = intArrayOf(Items.BERSERKER_HELM_3751, Items.BERSERKER_HELM_13408, Items.BERSERKER_HELM_E_12674, Items.BERSERKER_HELM_CHARGED_12675,
        Items.FREMENNIK_HELM_3748, Items.WARRIOR_HELM_3753, Items.WARRIOR_HELM_13409, Items.WARRIOR_HELM_CHARGED_12677, Items.WARRIOR_HELM_E_12676,
        Items.ARCHER_HELM_3749, Items.ARCHER_HELM_13407, Items.ARCHER_HELM_CHARGED_12673, Items.ARCHER_HELM_E_12672,
        Items.ROCK_SHELL_HELM_6128, Items.ROCK_SHELL_HELM_10613, Items.ROCK_SHELL_HELM_13411,
        Items.ROCK_SHELL_LEGS_6130, Items.ROCK_SHELL_LEGS_13413,
        Items.ROCK_SHELL_PLATE_6129, Items.ROCK_SHELL_PLATE_13412,
        Items.ROCK_SHELL_BOOTS_6145, Items.ROCK_SHELL_BOOTS_13421,
        Items.ROCK_SHELL_GLOVES_6151, Items.ROCK_SHELL_GLOVES_13424,
        Items.SPINED_HELM_6131, Items.SPINED_HELM_10614, Items.SPINED_HELM_13414,
        Items.SPINED_BODY_6133, Items.SPINED_BODY_13415,
        Items.SPINED_CHAPS_6135, Items.SPINED_CHAPS_13416,
        Items.SPINED_GLOVES_6149, Items.SPINED_GLOVES_13423,
        Items.SPINED_BOOTS_6143, Items.SPINED_BOOTS_13420,
        Items.SKELETAL_HELM_6137, Items.SKELETAL_HELM_10604, Items.SKELETAL_HELM_13417,
        Items.SKELETAL_TOP_6139, Items.SKELETAL_TOP_13418,
        Items.SKELETAL_BOTTOMS_6141, Items.SKELETAL_BOTTOMS_13419,
        Items.SKELETAL_GLOVES_6153, Items.SKELETAL_GLOVES_13425,
        Items.SKELETAL_BOOTS_6147, Items.SKELETAL_BOOTS_13422,
        Items.FREMENNIK_HAT_3798, Items.FREMENNIK_ROBE_3793, Items.FREMENNIK_SKIRT_3795, Items.GLOVES_3799, Items.FREMENNIK_BOOTS_3791,
        Items.FREMENNIK_CLOAK_3759, Items.FREMENNIK_CLOAK_3761, Items.FREMENNIK_CLOAK_3763, Items.FREMENNIK_CLOAK_3765, Items.FREMENNIK_CLOAK_3777, Items.FREMENNIK_CLOAK_3779,
        Items.FREMENNIK_CLOAK_3781, Items.FREMENNIK_CLOAK_3783, Items.FREMENNIK_CLOAK_3785, Items.FREMENNIK_CLOAK_3787, Items.FREMENNIK_CLOAK_3789,
        Items.FREMENNIK_SHIRT_3767, Items.FREMENNIK_SHIRT_3769, Items.FREMENNIK_SHIRT_3771, Items.FREMENNIK_SHIRT_3773, Items.FREMENNIK_SHIRT_3775,
        Items.FREMENNIK_BLADE_3757, Items.FREMENNIK_SHIELD_3758)

    private val fremennikIslesDuringQuestEquipment = intArrayOf(Items.YAK_HIDE_ARMOUR_10822, Items.YAK_HIDE_ARMOUR_10824, Items.FREMENNIK_ROUND_SHIELD_10826)

    private val avasBackpacks = intArrayOf(Items.AVAS_ACCUMULATOR_10499, Items.AVAS_ATTRACTOR_10498)

    private val questCapes = intArrayOf(9813,9814)

    override fun defineListeners() {
        onEquip(fremennikIslesEquipment) { player, _ ->
            if (!isQuestComplete(player, "Fremennik Isles")) {
                sendMessage(player, "You must have completed The Fremennik Isles to equip this.")
                return@onEquip false
            }
            return@onEquip true
        }

        onEquip(fremennikTrialsEquipment) { player, _ ->
            if (!isQuestComplete(player, "Fremennik Trials")) {
                sendMessage(player, "You must have completed The Fremennik Trials to equip this.")
                return@onEquip false
            }
            return@onEquip true
        }

        onEquip(fremennikIslesDuringQuestEquipment){ player, _ ->
            if (questStage(player, "Fremennik Isles") > 0) {
                sendMessage(player, "You must have started The Fremennik Isles to equip this.")
                return@onEquip false
            }
            return@onEquip true
        }

        onEquip(avasBackpacks){ player, _ ->
            if (!isQuestComplete(player, "Animal Magnetism")) {
                sendMessage(player, "You must have completed Animal Magnetism to equip this.")
                return@onEquip false
            }
            return@onEquip true
        }

        onEquip(Items.CAPE_OF_LEGENDS_1052) { player, _ ->
            if(!isQuestComplete(player, "Legends' Quest")) {
                player.packetDispatch.sendMessage("You must have completed Legends' Quest to equip this.")
                return@onEquip false
            }
            return@onEquip true
        }

        onEquip(questCapes) { player, _ ->
            val maxQP = QuestRepository.getQuests().values.sumBy { it.questPoints }
            if (player.questRepository.points < maxQP) {
                player.packetDispatch.sendMessage("You cannot equip the Quest cape without completing all available quests.")
                return@onEquip false
            }
            return@onEquip true
        }

        onEquip(Items.WOLFBANE_2952){ player, _ ->
            if(!isQuestComplete(player, "Priest in Peril")) {
                sendMessage(player, "You must have completed Priest in Peril to equip this.")
                return@onEquip false
            }
            return@onEquip true
        }

        onEquip(Items.ANCIENT_MACE_11061){ player, _ ->
            if(!isQuestComplete(player, "Another Slice of H.A.M.")) {
                sendMessage(player, "You must have completed Another Slice of H.A.M. to equip this.")
                return@onEquip false
            }
            return@onEquip true
        }

        onEquip(Items.ANCIENT_STAFF_4675){ player, _ ->
            if(!isQuestComplete(player, "Desert Treasure")) {
                sendMessage(player, "You must have completed Desert Treasure to equip this.")
                return@onEquip false
            }
            return@onEquip true
        }


    }
}