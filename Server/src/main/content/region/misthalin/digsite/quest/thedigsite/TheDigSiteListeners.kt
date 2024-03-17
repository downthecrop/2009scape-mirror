package content.region.misthalin.digsite.quest.thedigsite

import content.global.skill.thieving.ThievingListeners
import core.api.*
import core.api.utils.PlayerCamera
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class TheDigSiteListeners : InteractionListener {
    companion object {
        val BENDING_DOWN_ANIMATION = Animation(827)
        val TROWEL_ANIMATION = Animation(2272)
        val PANNING_ANIMATION = Animation(4593)


        val trainingDigTable = WeightBasedTable.create(
                WeightedItem(0, 0, 0, 8.0, false),
                WeightedItem(Items.COINS_995, 1, 1, 1.0, false),
                WeightedItem(Items.CHARCOAL_973, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARROW_687, 1, 1, 1.0, false),
                WeightedItem(Items.CRACKED_SAMPLE_674, 1, 1, 1.0, false),
                WeightedItem(Items.VASE_710, 1, 1, 1.0, false),
        )
        val level1DigTable = WeightBasedTable.create(
                WeightedItem(0, 0, 0, 2.0, false),
                WeightedItem(Items.BUTTONS_688, 1, 1, 1.0, false),
                WeightedItem(Items.VASE_710, 1, 1, 1.0, false),
                WeightedItem(Items.COPPER_ORE_436, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_BOOTS_1061, 1, 1, 1.0, false),
                WeightedItem(Items.OPAL_1609, 1, 1, 1.0, false),
                WeightedItem(Items.OLD_TOOTH_695, 1, 1, 1.0, false),
                WeightedItem(Items.ROTTEN_APPLE_1984, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_GLASS_1469, 1, 1, 1.0, false),
                WeightedItem(Items.RUSTY_SWORD_686, 1, 1, 1.0, false),
                WeightedItem(Items.BONES_526, 1, 1, 1.0, false),
        )
        val level2DigTable = WeightBasedTable.create(
                WeightedItem(0, 0, 0, 3.0, false),
                WeightedItem(Items.BONES_526, 1, 1, 2.0, false),
                WeightedItem(Items.DAMAGED_ARMOUR_697, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_BOOTS_1061, 1, 1, 1.0, false),
                WeightedItem(Items.BOWL_1923, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_STAFF_689, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARMOUR_698, 1, 1, 1.0, false),
                WeightedItem(Items.UNCUT_JADE_1627, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_GLASS_1469, 1, 1, 1.0, false),
                WeightedItem(Items.JUG_1935, 1, 1, 1.0, false),
                WeightedItem(Items.EMPTY_POT_1931, 1, 1, 1.0, false),
                WeightedItem(Items.CLAY_434, 1, 1, 1.0, false),
                WeightedItem(Items.UNCUT_OPAL_1625, 1, 1, 1.0, false),
        )
        val level3DigTable = WeightBasedTable.create(
                WeightedItem(0, 0, 0, 1.0, false),
                WeightedItem(Items.COINS_995, 10, 10, 2.0, false),
                WeightedItem(Items.ANCIENT_TALISMAN_681, 1, 1, 1.0, false), // sendItemDialogue "You find a strange talisman."
                WeightedItem(Items.BELT_BUCKLE_684, 1, 1, 2.0, false),
                WeightedItem(Items.BLACK_MED_HELM_1151, 1, 1, 1.0, false),
                WeightedItem(Items.BONES_526, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARMOUR_698, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARROW_687, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_STAFF_689, 1, 1, 1.0, false),
                WeightedItem(Items.BRONZE_SPEAR_1237, 1, 1, 1.0, false),
                WeightedItem(Items.BUTTONS_688, 1, 1, 1.0, false),
                WeightedItem(Items.CERAMIC_REMAINS_694, 1, 1, 1.0, false),
                WeightedItem(Items.CLAY_434, 1, 1, 1.0, false),
                WeightedItem(Items.DAMAGED_ARMOUR_697, 1, 1, 1.0, false),
                WeightedItem(Items.IRON_KNIFE_863, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_BOOTS_1061, 1, 1, 1.0, false),
                WeightedItem(Items.NEEDLE_1733, 1, 1, 1.0, false),
                WeightedItem(Items.OLD_TOOTH_695, 1, 1, 1.0, false),
                WeightedItem(Items.PIE_DISH_2313, 1, 1, 1.0, false),
        )
        val specimenTrayTable = WeightBasedTable.create(
                WeightedItem(0, 0, 0, 2.0, false),
                WeightedItem(Items.BONES_526, 1, 1, 2.0, false),
                WeightedItem(Items.COINS_995, 1, 1, 1.0, false),
                WeightedItem(Items.IRON_DAGGER_1203, 1, 1, 1.0, false),
                WeightedItem(Items.CHARCOAL_973, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_ARROW_687, 1, 1, 1.0, false),
                WeightedItem(Items.BROKEN_GLASS_1469, 1, 1, 1.0, false),
                WeightedItem(Items.CERAMIC_REMAINS_694, 1, 1, 1.0, false),
                WeightedItem(Items.CRACKED_SAMPLE_674, 1, 1, 1.0, false),
        )

        val workmanPickpocketingTable = WeightBasedTable.create(
                WeightedItem(Items.SPECIMEN_BRUSH_670, 1, 1, 3.0, false),
                WeightedItem(Items.ANIMAL_SKULL_671, 1, 1, 3.0, false),
                WeightedItem(Items.COINS_995, 10, 10, 1.0, false),
                WeightedItem(Items.ROPE_954, 1, 1, 1.0, false),
                WeightedItem(Items.BUCKET_1925, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_GLOVES_1059, 1, 1, 1.0, false),
                WeightedItem(Items.SPADE_952, 1, 1, 1.0, false),
        )

        val workmanPostQuestPickpocketingTable = WeightBasedTable.create(
                WeightedItem(Items.SPECIMEN_BRUSH_670, 1, 1, 3.0, false),
                WeightedItem(Items.COINS_995, 10, 10, 4.0, false),
                WeightedItem(Items.ROPE_954, 1, 1, 1.0, false),
                WeightedItem(Items.BUCKET_1925, 1, 1, 1.0, false),
                WeightedItem(Items.LEATHER_GLOVES_1059, 1, 1, 1.0, false),
                WeightedItem(Items.SPADE_952, 1, 1, 1.0, false),
        )

        val panningTable = WeightBasedTable.create(
                WeightedItem(0, 0, 0, 20.0, false),
                WeightedItem(Items.COINS_995, 1, 1, 4.0, false),
                WeightedItem(Items.NUGGETS_680, 1, 1, 4.0, false),
                WeightedItem(Items.OYSTER_407, 1, 1, 3.0, false),
                WeightedItem(Items.UNCUT_OPAL_1625, 1, 1, 3.0, false),
                WeightedItem(Items.UNCUT_JADE_1627, 1, 1, 3.0, false),
                WeightedItem(Items.SPECIAL_CUP_672, 1, 1, 3.0, false),
        )
    }

    override fun defineListeners() {

        // 3: Certificate Level 1
        on(Items.LEVEL_1_CERTIFICATE_691, ITEM, "look-at") { player, _ ->
            openInterface(player, 440)
            setInterfaceText(player, player.username, 440, 5)
            return@on true
        }
        // 3: Certificate Level 2
        on(Items.LEVEL_2_CERTIFICATE_692, ITEM, "look-at") { player, _ ->
            openInterface(player, 441)
            setInterfaceText(player, player.username, 441, 5)
            return@on true
        }
        // 3: Certificate Level 3
        on(Items.LEVEL_3_CERTIFICATE_693, ITEM, "look-at") { player, _ ->
            openInterface(player, 444)
            setInterfaceText(player, player.username, 444, 5)
            return@on true
        }

        // 3 Green: Pickpocket Animal Skull from Digsite Workman
        on(intArrayOf(NPCs.DIGSITE_WORKMAN_613, NPCs.DIGSITE_WORKMAN_4564, NPCs.DIGSITE_WORKMAN_4565), NPC, "steal-from") { player, node ->

            if(getStatLevel(player, Skills.THIEVING) < 25){
                player.sendMessage("You need a Thieving level of 25 to do that.")
                return@on true
            }
            if(!workmanPickpocketingTable.canRoll(player)){
                player.sendMessage("You don't have enough inventory space to do that.")
                return@on true
            }
            sendMessage(player, "You attempt to pick the workman's pocket...")
            if (getQuestStage(player, TheDigSite.questName) == 3) {
                player.animator.animate(ThievingListeners.PICKPOCKET_ANIM)
                val rollOutcome = ThievingListeners.pickpocketRoll(player, 84.0, 240.0, workmanPickpocketingTable)
                if (rollOutcome != null) {
                    queueScript(player, ThievingListeners.PICKPOCKET_ANIM.duration, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                if (rollOutcome.size > 0) {
                                    addItemOrDrop(player, rollOutcome[0].id)
                                    when (rollOutcome[0].id){
                                        Items.ANIMAL_SKULL_671 -> sendItemDialogue(player, Items.ANIMAL_SKULL_671, "You steal an animal skull.")
                                        else -> sendMessage(player, "You steal something.")
                                    }
                                } else {
                                    sendMessage(player, "You couldn't steal anything.")
                                }
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else {
                    node.asNpc().face(player)
                    node.asNpc().animator.animate(ThievingListeners.NPC_ANIM)
                    sendMessage(player, "You fail to pick the workman's pocket.")
                    sendChat(node.asNpc(), "What do you think you're doing???")
                    sendMessage(player, "You have been stunned.") // -1 HP
                    playHurtAudio(player, 20)
                    stun(player, 3)
                    player.impactHandler.manualHit(node.asNpc(), 1, ImpactHandler.HitsplatType.NORMAL)
                    node.asNpc().face(null)
                }
            } else {
                // When not during quest
                player.animator.animate(ThievingListeners.PICKPOCKET_ANIM)
                val rollOutcome = ThievingListeners.pickpocketRoll(player, 84.0, 240.0, workmanPostQuestPickpocketingTable)
                if (rollOutcome != null) {
                    queueScript(player, ThievingListeners.PICKPOCKET_ANIM.duration, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                if (rollOutcome.size > 0) {
                                    addItemOrDrop(player, rollOutcome[0].id)
                                    when (rollOutcome[0].id){
                                        Items.ANIMAL_SKULL_671 -> sendItemDialogue(player, Items.ANIMAL_SKULL_671, "You steal an animal skull.")
                                        else -> sendMessage(player, "You steal something.")
                                    }
                                } else {
                                    sendMessage(player, "You couldn't steal anything.")
                                }
                                return@queueScript stopExecuting(player)
                            }
                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else {
                    node.asNpc().face(player)
                    node.asNpc().animator.animate(ThievingListeners.NPC_ANIM)
                    sendMessage(player, "You fail to pick the workman's pocket.")
                    sendChat(player, "What do you think you're doing???")
                    sendMessage(player, "You have been stunned.") // -1 HP
                    playHurtAudio(player, 20)
                    stun(player, 3)
                    player.impactHandler.manualHit(node.asNpc(), 1, ImpactHandler.HitsplatType.NORMAL)
                    node.asNpc().face(null)
                }
            }
            return@on true
        }

        // 3 Purple: Pickpocket student.
        on(NPCs.STUDENT_617, NPC, "pickpocket") { player, _ ->
            sendDialogue(player, "I don't think I should try to steal from this poor student.")
            // Technically you can steal the teddy back from her. But why?
            return@on true
        }

        // 3 Purple: Search Bush for Teddy Bear (but wrong bushes)
        on(Scenery.BUSH_2357, SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the bush... You find nothing of interest.")
            return@on true
        }
        // 3 Purple: Search Bush for Teddy Bear (the correct bush)
        on(Scenery.BUSH_2358, SCENERY, "search") { player, _ ->
            openDialogue(player, object : DialogueFile(){
                override fun handle(componentID: Int, buttonID: Int) {
                    when(stage){
                        0 -> playerl("Hey, something has been dropped here...").also {
                            addItemOrDrop(player, Items.TEDDY_673)
                            stage++
                        }
                        1 -> sendItemDialogue(player, Items.TEDDY_673, "You find... something.").also { stage = END_DIALOGUE }
                    }
                }
            })
            return@on true
        }


        // 8/9: Pouring CHEMICAL_COMPOUND_707 on brick. Transitions to stage 11.
        onUseWith(NPC, Items.CUP_OF_TEA_712, NPCs.PANNING_GUIDE_620) { player, used, with ->
            if(removeItem(player, used)) {
                sendNPCDialogue(player, with.id, "Ah! Lovely! You can't beat a good cuppa! You're free to pan all you want.")
                setAttribute(player, TheDigSite.attributePanningGuideTea, true)
            }
            return@onUseWith true
        }

        onUseWith(SCENERY, Items.PANNING_TRAY_677, Scenery.PANNING_POINT_2363) { player, used, with ->
            if (getAttribute(player, TheDigSite.attributePanningGuideTea, false)) {
                queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                    when (stage) {
                        0 -> {
                            animate(player, PANNING_ANIMATION)
                            lock(player, PANNING_ANIMATION.duration)
                            return@queueScript delayScript(player, PANNING_ANIMATION.duration)
                        }

                        1 -> {
                            sendItemDialogue(player, Items.PANNING_TRAY_679, "You lift the full tray from the water")
                            if (removeItem(player, used)) {
                                addItemOrDrop(player, Items.PANNING_TRAY_679)
                            }
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }

                }
            } else {
                openDialogue(player, PanningGuideCannotPanDialogueFile(), findNPC(NPCs.PANNING_GUIDE_620)!!)
            }
            return@onUseWith true
        }
        // 3 Brown: Search Panning Point for Special Cup
        on(Scenery.PANNING_POINT_2363, SCENERY, "pan") { player, _ ->
            //
            if (getAttribute(player, TheDigSite.attributePanningGuideTea, false)) {
                if (inInventory(player, Items.PANNING_TRAY_677)) {
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                animate(player, PANNING_ANIMATION)
                                return@queueScript delayScript(player, PANNING_ANIMATION.duration)
                            }

                            1 -> {
                                sendItemDialogue(player, Items.PANNING_TRAY_679, "You lift the full tray from the water.")
                                if (removeItem(player, Items.PANNING_TRAY_677)) {
                                    addItemOrDrop(player, Items.PANNING_TRAY_679)
                                }
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else if (inInventory(player, Items.PANNING_TRAY_679)) {
                    sendPlayerDialogue(player, "I already have a full panning tray; perhaps I should search it first.")
                } else {
                    sendMessage(player, "I need a panning tray to pan the panning point.")
                }
            } else {
                openDialogue(player, PanningGuideCannotPanDialogueFile(), findNPC(NPCs.PANNING_GUIDE_620)!!)
            }
            return@on true
        }
        // 3 Brown: Empty tray
        on(Items.PANNING_TRAY_677, ITEM, "search") { player, used ->
            sendMessage(player, "The panning tray is empty.")
            return@on true
        }
        // 3 Brown: Search Panning Point for Special Cup
        on(Items.PANNING_TRAY_679, ITEM, "search") { player, used ->
            sendMessage(player, "You search the contents of the tray.")
            if(removeItem(player, used)) {
                addItemOrDrop(player, Items.PANNING_TRAY_677)
                val tableRoll = panningTable.roll()
                if (tableRoll.size > 0) {
                    addItemOrDrop(player, tableRoll[0].id)
                    when (tableRoll[0].id){
                        Items.COINS_995 -> sendItemDialogue(player, Items.COINS_995, "You find some coins within the mud.")
                        Items.NUGGETS_680 -> sendItemDialogue(player, Items.PANNING_TRAY_678, "You find some gold nuggets within the mud.")
                        Items.OYSTER_407 -> sendItemDialogue(player, Items.OYSTER_407, "You find an oyster within the mud.")
                        Items.UNCUT_OPAL_1625 -> sendItemDialogue(player, Items.UNCUT_OPAL_1625, "You find a gem within the mud!")
                        Items.UNCUT_JADE_1627 -> sendItemDialogue(player, Items.UNCUT_JADE_1627, "You find a gem within the mud!")
                        Items.SPECIAL_CUP_672 -> sendItemDialogue(player, Items.SPECIAL_CUP_672, "You find a shiny cup covered in mud.")
                    }
                } else {
                    sendItemDialogue(player, Items.PANNING_TRAY_679, "The tray contains only plain mud.")
                }
            }
            return@on true
        }

        // 3-End: Soil trowel digging. Very complex.
        onUseWith(SCENERY, Items.TROWEL_676, Scenery.SOIL_2376, Scenery.SOIL_2377, Scenery.SOIL_2378) { player, used, with ->

            val level3DigRight = ZoneBorders(3370, 3437, 3377, 3442)
            val level3DigLeft = ZoneBorders(3350, 3404, 3357, 3412)
            if (level3DigRight.insideBorder(player.location) || level3DigLeft.insideBorder(player.location)) {
                if (getQuestStage(player, TheDigSite.questName) >= 6) {
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                sendMessage(player, "You dig through the earth.")
                                animate(player, TROWEL_ANIMATION)
                                lock(player, TROWEL_ANIMATION.duration)
                                return@queueScript delayScript(player, TROWEL_ANIMATION.duration)
                            }

                            1 -> {
                                val tableRoll = level3DigTable.roll()
                                if (tableRoll.size > 0) {
                                    addItemOrDrop(player, tableRoll[0].id)
                                }
                                sendMessage(player, "You carefully clean your find with the specimen brush.")
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else {
                    sendNPCDialogue(player, NPCs.DIGSITE_WORKMAN_613, "Oi! What do you think you're doing? There's fragile specimens around here!", FacialExpression.ANGRY)
                }
            }

            val level2Dig = ZoneBorders(3350, 3424, 3363, 3430)
            if (level2Dig.insideBorder(player.location)) {
                if (getQuestStage(player, TheDigSite.questName) >= 5) {
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                sendMessage(player, "You dig through the earth.")
                                animate(player, TROWEL_ANIMATION)
                                lock(player, TROWEL_ANIMATION.duration)
                                return@queueScript delayScript(player, TROWEL_ANIMATION.duration)
                            }

                            1 -> {
                                val tableRoll = level2DigTable.roll()
                                if (tableRoll.size > 0) {
                                    addItemOrDrop(player, tableRoll[0].id)
                                }
                                sendMessage(player, "You carefully clean your find with the specimen brush.")
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else {
                    sendNPCDialogue(player, NPCs.DIGSITE_WORKMAN_613, "Oi! What do you think you're doing? There's fragile specimens around here!", FacialExpression.ANGRY)
                }
            }

            val level1DigCentre = ZoneBorders(3360, 3402, 3363, 3414)
            val level1DigRight = ZoneBorders(3367, 3403, 3372, 3414)
            if (level1DigCentre.insideBorder(player.location) || level1DigRight.insideBorder(player.location)) {
                if (getQuestStage(player, TheDigSite.questName) >= 4) {
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                sendMessage(player, "You dig through the earth.")
                                animate(player, TROWEL_ANIMATION)
                                lock(player, TROWEL_ANIMATION.duration)
                                return@queueScript delayScript(player, TROWEL_ANIMATION.duration)
                            }

                            1 -> {
                                val tableRoll = level1DigTable.roll()
                                if (tableRoll.size > 0) {
                                    addItemOrDrop(player, tableRoll[0].id)
                                }
                                sendMessage(player, "You carefully clean your find with the specimen brush.")
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                } else {
                    sendNPCDialogue(player, NPCs.DIGSITE_WORKMAN_613, "Oi! What do you think you're doing? There's fragile specimens around here!", FacialExpression.ANGRY)
                }
            }

            val trainingDigLeft = ZoneBorders(3352, 3396, 3357, 3400)
            val trainingDigRight = ZoneBorders(3367, 3397, 3372, 3400)
            if (trainingDigLeft.insideBorder(player.location) || trainingDigRight.insideBorder(player.location)) {
                if (getQuestStage(player, TheDigSite.questName) >= 3) {
                    queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                        when (stage) {
                            0 -> {
                                sendMessage(player, "You dig through the earth.")
                                animate(player, TROWEL_ANIMATION)
                                lock(player, TROWEL_ANIMATION.duration)
                                return@queueScript delayScript(player, TROWEL_ANIMATION.duration)
                            }

                            1 -> {
                                val tableRoll = trainingDigTable.roll()
                                if (tableRoll.size > 0) {
                                    addItemOrDrop(player, tableRoll[0].id)
                                    // You find a/an ...
                                    sendMessage(player, "You carefully clean your find with the specimen brush.")
                                }
                                return@queueScript stopExecuting(player)
                            }

                            else -> return@queueScript stopExecuting(player)
                        }
                    }
                }
            }

            return@onUseWith true
        }

        // 8: North East Winch goes to Doug Deeping
        on(Scenery.WINCH_2350, SCENERY, "operate") { player, _ ->
            if (getQuestStage(player, TheDigSite.questName) >= 11) {
                sendMessage(player, "You try to climb down the rope...")
                sendMessage(player, "You lower yourself into the shaft...")
                teleport(player, Location(3369, 9763))
                sendMessage(player, "You find yourself in a cavern...")
            } else if (getQuestStage(player, TheDigSite.questName) >= 8) {
                if (getAttribute(player, TheDigSite.attributeRopeNorthEastWinch, false)) {
                    sendMessage(player, "You try to climb down the rope...")
                    sendMessage(player, "You lower yourself into the shaft...")
                    teleport(player, Location(3369, 9827))
                    sendMessage(player, "You find yourself in a cavern...")
                } else {
                    sendMessage(player, "You operate the winch...")
                    queueScript(player, 2, QueueStrength.NORMAL) { stage: Int ->
                        sendPlayerDialogue(player, "Hey, I think I could fit down here. I need something to help me get all the way down.")
                        sendMessage(player, "The bucket descends, but does not reach the bottom.")
                        return@queueScript stopExecuting(player)
                    }
                }
            } else {
                openDialogue(player, object : DialogueFile(){
                    override fun handle(componentID: Int, buttonID: Int) {
                        when(stage){
                            0 -> npc(NPCs.DIGSITE_WORKMAN_613, "Sorry; this area is private. The only way you'll get to", "use these is by impressing the archaeological expert up", "at the center.").also {stage++ }
                            1 -> npc(NPCs.DIGSITE_WORKMAN_613,"Find something worthwhile and he might let you use the", "winches. Until then, get lost!").also { stage = END_DIALOGUE }
                        }
                    }
                })
            }
            // sendMessage(player, "There is a sign on the winch.")
            // sendMessage(player, "Private area - invitation only.")
            return@on true
        }

        // 8: Tie rope to winch
        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.WINCH_2350) { player, used, with ->
            if (removeItem(player, used)) {
                if (getQuestStage(player, TheDigSite.questName) >= 8) {
                    setAttribute(player, TheDigSite.attributeRopeNorthEastWinch, true)
                    sendMessage(player, "You tie the rope to the bucket.")
                } else {
                    openDialogue(player, object : DialogueFile(){
                        override fun handle(componentID: Int, buttonID: Int) {
                            when(stage){
                                0 -> npc(NPCs.DIGSITE_WORKMAN_613, "Sorry; this area is private. The only way you'll get to", "use these is by impressing the archaeological expert up", "at the center.").also {stage++ }
                                1 -> npc(NPCs.DIGSITE_WORKMAN_613,"Find something worthwhile and he might let you use the", "winches. Until then, get lost!").also { stage = END_DIALOGUE }
                            }
                        }
                    })
                }
            }
            return@onUseWith true
        }

        // 8: Climb back out
        on(Scenery.ROPE_2352, SCENERY, "climb-up") { player, _ ->
            teleport(player, Location(3370, 3427))
            return@on true
        }

        // 8: West Winch goes to Skeletons, Explosion and Stone Tablet
        on(Scenery.WINCH_2351, SCENERY, "operate") { player, _ ->
            if (getQuestStage(player, TheDigSite.questName) >= 11) {
                sendMessage(player, "You try to climb down the rope...")
                sendMessage(player, "You lower yourself into the shaft...")
                teleport(player, Location(3352, 9753))
                sendMessage(player, "You find yourself in a cavern...")
            } else if (getQuestStage(player, TheDigSite.questName) >= 8) {
                if (getAttribute(player, TheDigSite.attributeRopeWestWinch, false)) {
                    sendMessage(player, "You try to climb down the rope...")
                    sendMessage(player, "You lower yourself into the shaft...")
                    teleport(player, Location(3352, 9818))
                    sendMessage(player, "You find yourself in a cavern...")
                } else {
                    sendMessage(player, "You operate the winch...")
                    queueScript(player, 2, QueueStrength.NORMAL) { stage: Int ->
                        sendPlayerDialogue(player, "Hey, I think I could fit down here. I need something to help me get all the way down.")
                        sendMessage(player, "The bucket descends, but does not reach the bottom.")
                        return@queueScript stopExecuting(player)
                    }
                }
            } else {
                openDialogue(player, object : DialogueFile(){
                    override fun handle(componentID: Int, buttonID: Int) {
                        when(stage){
                            0 -> npc(NPCs.DIGSITE_WORKMAN_613, "Sorry; this area is private. The only way you'll get to", "use these is by impressing the archaeological expert up", "at the center.").also {stage++ }
                            1 -> npc(NPCs.DIGSITE_WORKMAN_613,"Find something worthwhile and he might let you use the", "winches. Until then, get lost!").also { stage = END_DIALOGUE }
                        }
                    }
                })
            }
            return@on true
        }


        // 8: Tie rope to winch
        onUseWith(IntType.SCENERY, Items.ROPE_954, Scenery.WINCH_2351) { player, used, with ->
            if (removeItem(player, used)) {
                if (getQuestStage(player, TheDigSite.questName) >= 8) {
                    setAttribute(player, TheDigSite.attributeRopeWestWinch, true)
                    sendMessage(player, "You tie the rope to the bucket.")
                } else {
                    openDialogue(player, object : DialogueFile(){
                        override fun handle(componentID: Int, buttonID: Int) {
                            when(stage){
                                0 -> npc(NPCs.DIGSITE_WORKMAN_613, "Sorry; this area is private. The only way you'll get to", "use these is by impressing the archaeological expert up", "at the center.").also {stage++ }
                                1 -> npc(NPCs.DIGSITE_WORKMAN_613,"Find something worthwhile and he might let you use the", "winches. Until then, get lost!").also { stage = END_DIALOGUE }
                            }
                        }
                    })
                }
            }
            return@onUseWith true
        }

        // 8: Climb back out
        on(Scenery.ROPE_2353, SCENERY, "climb-up") { player, _ ->
            teleport(player, Location(3354, 3417))
            return@on true
        }


        on(Items.INVITATION_LETTER_696, ITEM, "read") { player, _ ->
            sendPlayerDialogue(player, "It says, 'I give permission for the bearer... to use the mine shafts on site. - signed Terrance Balando, Archaeological Expert, City of Varrock.")
            // LINE SEPARATED : ("It says, 'I give permission for the bearer... to use the", "mine shafts on site. - signed Terrance Balando,", "Archaeological Expert, City of Varrock.'")
            return@on true
        }


        // 8: Investigating brick. Transitions to stage 9.
        on(Scenery.BRICK_2362, SCENERY, "search") { player, _ ->
            if(getQuestStage(player, TheDigSite.questName) == 8) {
                sendPlayerDialogue(player, "Hmmm, there's a room past these bricks. If I could move them out of the way then I could find out what's inside. Maybe there's someone around here who can help...", FacialExpression.THINKING)
                setQuestStage(player, TheDigSite.questName, 9)
            }
            if(getQuestStage(player, TheDigSite.questName) == 9) {
                sendPlayerDialogue(player, "Hmmm, there's a room past these bricks. If I could move them out of the way then I could find out what's inside. Maybe there's someone around here who can help...", FacialExpression.THINKING)
            }
            if(getQuestStage(player, TheDigSite.questName) == 10) {
                sendPlayerDialogue(player, "The brick is covered with the chemicals I made.", FacialExpression.THINKING)
            }
            return@on true
        }


        // 8/9: Chest open
        on(Scenery.CHEST_2361, SCENERY, "search") { player, _ ->
            sendMessage(player, "The chest is locked.")
            return@on true
        }

        // 8/9: Chest open with key
        onUseWith(IntType.SCENERY, Items.CHEST_KEY_709, Scenery.CHEST_2361) { player, used, with ->
            if (!removeItem(player, used)) {
                return@onUseWith false
            }
            sendMessage(player, "You use the key in the chest.")
            animate(player, 536)
            replaceScenery(with as core.game.node.scenery.Scenery, Scenery.CHEST_2360, 100)
            return@onUseWith true
        }


        // 8/9: Specimen tray mainly to find charcoal. No quest stage limit as long as you have a specimen jar.
        on(Scenery.SPECIMEN_TRAY_2375, SCENERY, "search") { player, _ ->
            if (inInventory(player, Items.SPECIMEN_JAR_669)) {
                sendMessage(player, "You sift through the earth in the tray.")
                animate(player, BENDING_DOWN_ANIMATION)
                val tableRoll = specimenTrayTable.roll()
                if (tableRoll.size > 0) {
                    addItemOrDrop(player, tableRoll[0].id)
                }
            } else {
                sendMessage(player, "You need to have a specimen jar when you are searching the tray.")
                // sendMessage(player, "There must be a workman present when you are searching the tray.") <- authentic, but what does this mean...
            }
            return@on true
        }

        on(Scenery.CHEST_2360, SCENERY, "search") { player, _ ->
            addItemOrDrop(player, Items.CHEMICAL_POWDER_700)
            sendItemDialogue(player, Items.CHEMICAL_POWDER_700, "You find some unusual powder inside...")
            return@on true
        }

        // 8/9 Getting unidentified liquid (Do not shift this code down. This somehow gets overwritten.)
        onUseWith(SCENERY, Items.VIAL_229, Scenery.BARREL_17297) { player, used, with ->
            if(removeItem(player, used)) {
                addItemOrDrop(player, Items.UNIDENTIFIED_LIQUID_702)
                openDialogue(player, object : DialogueBuilderFile() {
                    override fun create(b: DialogueBuilder) {
                        b.onPredicate { _ -> true }
                                .item(Items.UNIDENTIFIED_LIQUID_702, "You fill the vial with the liquid.")
                                .player("I'm not sure what this stuff is. I had better be VERY", "careful with it; I had better not drop it either...")
                    }
                })
                sendMessage(player, "You put the lid back on the barrel just in case it's dangerous.")
                setVarbit(player, TheDigSite.barrelVarbit, 0)
            }
            return@onUseWith true
        }
        
        // 8/9 Barrel open
        on(Scenery.BARREL_17296, SCENERY, "search", "open") { player, _ ->
            sendPlayerDialogue(player, "Mmmm... The lid is shut tight; I'll have to find something to lever it off.", FacialExpression.THINKING)
            return@on true
        }

        // 8/9 Barrel opened with trowel
        onUseWith(SCENERY, Items.TROWEL_676, Scenery.BARREL_17296) { player, used, with ->
            sendPlayerDialogue(player, "Great! It's opened it.")
            setVarbit(player, TheDigSite.barrelVarbit, 1)
            return@onUseWith true
        }

        // 8/9 Open barrel search
        on(Scenery.BARREL_17297, SCENERY, "search") { player, _ ->
            sendPlayerDialogue(player, "I can't pick this up with my bare hands! I'll need something to put it in. It looks and smells rather dangerous though, so it'll need to be something small and capable of containing dangerous chemicals.", FacialExpression.THINKING)
            return@on true
        }

        // 8/9 Mixed chem 1
        onUseWith(ITEM, Items.AMMONIUM_NITRATE_701, Items.NITROGLYCERIN_703) { player, used, with ->
            if (getStatLevel(player, Skills.HERBLORE) < 10) {
                sendMessage(player, "You need level 10 Herblore to combine the chemicals.")
                return@onUseWith true
            }
            sendMessage(player, "You mix the nitrate powder into the liquid.")
            sendMessage(player, "It has produced a foul mixture.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.MIXED_CHEMICALS_705)
            }
            return@onUseWith true
        }

        // 8/9 Mixed chem 2
        onUseWith(ITEM, Items.MIXED_CHEMICALS_705, Items.GROUND_CHARCOAL_704) { player, used, with ->
            if (getStatLevel(player, Skills.HERBLORE) < 10) {
                sendMessage(player, "You need level 10 Herblore to combine the chemicals.")
                return@onUseWith true
            }
            sendMessage(player, "You mix the charcoal into the liquid.")
            sendMessage(player, "It has produced an even fouler mixture.")
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.MIXED_CHEMICALS_706)
            }
            return@onUseWith true
        }

        // 8/9 Mixed chem 3
        onUseWith(ITEM, Items.MIXED_CHEMICALS_706, Items.ARCENIA_ROOT_708) { player, used, with ->
            if (getStatLevel(player, Skills.HERBLORE) < 10) {
                sendMessage(player, "You need level 10 Herblore to combine the chemicals.")
                return@onUseWith true
            }
            sendMessage(player, "You mix the root into the mixture.")
            sendMessage(player, "You produce a potentially explosive compound.")
            sendPlayerDialogue(player, "Excellent! This looks just right!", FacialExpression.HAPPY)
            if(removeItem(player, used) && removeItem(player, with)) {
                addItemOrDrop(player, Items.CHEMICAL_COMPOUND_707)
            }
            return@onUseWith true
        }

        // 8/9: Pouring CHEMICAL_COMPOUND_707 on brick. Transitions to stage 10.
        onUseWith(SCENERY, Items.CHEMICAL_COMPOUND_707, Scenery.BRICK_2362) { player, used, with ->
            if (getQuestStage(player, TheDigSite.questName) == 9) {
                if(removeItem(player, used)) {
                    addItemOrDrop(player, Items.VIAL_229)
                    sendMessage(player, "You pour the compound over the bricks...")
                    sendPlayerDialogue(player, "Ok, the mixture is all over the bricks. I need some way to ignite this compound.", FacialExpression.THINKING)
                    setQuestStage(player, TheDigSite.questName, 10)
                }
            }
            return@onUseWith true
        }

        // 10: Lighting brick. Transitions to stage 11.
        onUseWith(SCENERY, Items.TINDERBOX_590, Scenery.BRICK_2362) { player, used, with ->
            if(getQuestStage(player, TheDigSite.questName) == 10) {
                setQuestStage(player, TheDigSite.questName, 11)
                lock(player, 15)
                queueScript(player, 0, QueueStrength.NORMAL) { stage: Int ->
                    when (stage) {
                        0 -> {
                            animate(player, BENDING_DOWN_ANIMATION)
                            sendMessage(player, "You strike the tinderbox...")
                            return@queueScript delayScript(player, 2)
                        }
                        1 -> {
                            sendMessage(player, "Fizz..")
                            sendPlayerDialogue(player, "Woah! This is going to blow! I'd better run!", FacialExpression.EXTREMELY_SHOCKED)
                            return@queueScript delayScript(player, BENDING_DOWN_ANIMATION.duration)
                        }
                        2 -> {
                            // forceWalk(player, Location(3366, 9830, 0), "smart") doesn't work when player is locked.
                            player.walkingQueue.reset()
                            player.walkingQueue.addPath(3366, 9830)

                            return@queueScript delayScript(player, 8)
                        }
                        3 -> {
                            PlayerCamera(player).shake(0, 20, 8, 128, 40)
                            return@queueScript delayScript(player, 4)
                        }
                        4 -> {
                            PlayerCamera(player).reset()
                            teleport(player, Location(3366, 9766))
                            unlock(player)
                            sendPlayerDialogue(player, "Wow, that was a big explosion! What's that noise I can hear? Sounds like bones moving or something...", FacialExpression.EXTREMELY_SHOCKED)
                            return@queueScript stopExecuting(player)
                        }
                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@onUseWith true
        }

        // 12: Stone tablet get.
        on(Scenery.STONE_TABLET_17367, SCENERY, "take") { player, _ ->
            setVarbit(player, TheDigSite.tabletVarbit, 1)
            addItemOrDrop(player,Items.STONE_TABLET_699)
            sendMessage(player, "You pick the stone tablet up.")
            return@on true
        }

        on(Items.STONE_TABLET_699, ITEM, "read") { player, _ ->
            sendPlayerDialogue(player, "It says: Tremble mortal, before the altar of our dread lord Zaros.")
            return@on true
        }

        // Dropping Liquids
        on(Items.UNIDENTIFIED_LIQUID_702, ITEM, "empty") { player, node ->
            if(removeItem(player, node)) {
                addItemOrDrop(player, Items.VIAL_229)
            }
            sendChat(player, "You very carefully empty out the liquid.")
            return@on true
        }

        on(Items.UNIDENTIFIED_LIQUID_702, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 25)
            sendChat(player, "Ow! The liquid exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        on(Items.NITROGLYCERIN_703, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 35)
            sendChat(player, "Ow! The nitroglycerin exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        on(Items.MIXED_CHEMICALS_705, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 45)
            sendChat(player, "Ow! The liquid exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        on(Items.MIXED_CHEMICALS_706, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 55)
            sendChat(player, "Ow! The liquid exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        on(Items.CHEMICAL_COMPOUND_707, ITEM, "drop") { player, node ->
            removeItem(player, node)
            impact(player, 65)
            sendChat(player, "Ow! The liquid exploded!")
            sendMessage(player, "You were injured by the burning liquid.")
            return@on true
        }

        // Scenery not tied to quest

        on(intArrayOf(Scenery.GATE_24560, Scenery.GATE_24561), IntType.SCENERY, "open") { player, _ ->
            // This gate is only openable when you have completed Digsite and have 153 kudos from the museum.
            // We can assume, no one is going to reach that until the museum is fully fleshed out.
            // So this gate will stay locked.
            sendMessage(player, "You can't go through there, it's for Dig Site workmen only.")
            sendChat(findNPC(NPCs.MUSEUM_GUARD_5942) as Entity, "Sorry - workman's gate only.")
            return@on true
        }

        on(Scenery.CUPBOARD_17303, SCENERY, "search") { player, _ ->
            sendItemDialogue(player, Items.SPECIMEN_JAR_669, "You find a specimen jar.")
            addItemOrDrop(player, Items.SPECIMEN_JAR_669)
            return@on true
        }

        on(Scenery.CUPBOARD_35223, SCENERY, "search") { player, _ ->
            sendItemDialogue(player, Items.ROCK_PICK_675, "You find a rock pick.")
            addItemOrDrop(player, Items.ROCK_PICK_675)
            return@on true
        }

        on(intArrayOf(Scenery.SACKS_2354, Scenery.SACKS_2355, Scenery.SACKS_2356), SCENERY, "search") { player, _ ->
            sendItemDialogue(player, Items.SPECIMEN_JAR_669, "You find a specimen jar.")
            addItemOrDrop(player, Items.SPECIMEN_JAR_669)
            return@on true
        }

        on(Scenery.BOOKCASE_35224, SCENERY, "search") { player, _ ->
            sendMessage(player, "You search through the bookcase...")
            sendItemDialogue(player, Items.BOOK_ON_CHEMICALS_711, "You find a book on chemicals.")
            addItemOrDrop(player, Items.BOOK_ON_CHEMICALS_711)
            return@on true
        }

        on(Scenery.SIGNPOST_2366, SCENERY, "read") { player, _ ->
            sendMessage(player, "This site is for training purposes only.")
            return@on true
        }

        on(Scenery.SIGNPOST_2367, SCENERY, "read") { player, _ ->
            sendMessage(player, "Level 1 digs only.")
            return@on true
        }

        on(Scenery.SIGNPOST_2368, SCENERY, "read") { player, _ ->
            sendMessage(player, "Level 2 digs only.")
            return@on true
        }

        on(Scenery.SIGNPOST_2369, SCENERY, "read") { player, _ ->
            sendMessage(player, "Level 3 digs only.")
            return@on true
        }

        on(Scenery.SIGNPOST_2370, SCENERY, "read") { player, _ ->
            sendMessage(player, "Private dig.")
            return@on true
        }

        on(Scenery.SIGNPOST_2371, SCENERY, "read") { player, _ ->
            sendMessage(player, "Digsite educational centre.")
            return@on true
        }

    }
}