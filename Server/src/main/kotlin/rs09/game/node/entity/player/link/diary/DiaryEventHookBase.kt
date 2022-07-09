package rs09.game.node.entity.player.link.diary

import api.*
import api.events.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.zone.ZoneBorders
import rs09.game.Event

abstract class DiaryEventHookBase(private val diaryType: DiaryType) : MapArea, LoginListener {
    protected companion object {
        private fun <T> forEligibleEntityDo(entity: Entity, event: T, handler: (Player, T) -> Unit) {
            if (entity !is Player) return
            if (entity.isArtificial) return

            handler(entity, event)
        }
    }

    class EventHandler<T : api.events.Event>(
        private val owner: DiaryEventHookBase,
        private val handler: (Player, T) -> Unit
    ) : EventHook<T> {
        override fun process(entity: Entity, event: T) {
            forEligibleEntityDo(entity, event, handler)
        }
    }

    open val areaTasks get() = arrayOf<AreaDiaryTask>()

    final override fun defineAreaBorders(): Array<ZoneBorders>
        = areaTasks.map { task -> task.zoneBorders }.toTypedArray()

    final override fun areaEnter(entity: Entity) {
        if (entity !is Player) return
        if (entity.isArtificial) return

        onAreaVisited(entity)
    }

    final override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity !is Player) return
        if (entity.isArtificial) return

        onAreaLeft(entity)
    }

    final override fun login(player: Player) {
        player.hook(Event.ResourceProduced, EventHandler(this, ::onResourceProduced))
        player.hook(Event.NPCKilled, EventHandler(this, ::onNpcKilled))
        player.hook(Event.Teleported, EventHandler(this, ::onTeleported))
        player.hook(Event.FireLit, EventHandler(this, ::onFireLit))
        player.hook(Event.Interacted, EventHandler(this, ::onInteracted))
        player.hook(Event.ButtonClicked, EventHandler(this, ::onButtonClicked))
        player.hook(Event.DialogueOpened, EventHandler(this, ::onDialogueOpened))
        player.hook(Event.DialogueOptionSelected, EventHandler(this, ::onDialogueOptionSelected))
        player.hook(Event.DialogueClosed, EventHandler(this, ::onDialogueClosed))
        player.hook(Event.UsedWith, EventHandler(this, ::onUsedWith))
        player.hook(Event.PickedUp, EventHandler(this, ::onPickedUp))
        player.hook(Event.InterfaceOpened, EventHandler(this, ::onInterfaceOpened))
        player.hook(Event.AttributeSet, EventHandler(this, ::onAttributeSet))
        player.hook(Event.AttributeRemoved, EventHandler(this, ::onAttributeRemoved))
        player.hook(Event.SpellCast, EventHandler(this, ::onSpellCast))
        player.hook(Event.ItemAlchemized, EventHandler(this, ::onItemAlchemized))
        player.hook(Event.ItemEquipped, EventHandler(this, ::onItemEquipped))
        player.hook(Event.ItemUnequipped, EventHandler(this, ::onItemUnequipped))
        player.hook(Event.ItemPurchased, EventHandler(this, ::onItemPurchasedFromShop))
        player.hook(Event.ItemSold, EventHandler(this, ::onItemSoldToShop))
        player.hook(Event.JobAssigned, EventHandler(this, ::onJobAssigned))
        player.hook(Event.FairyRingDialed, EventHandler(this, ::onFairyRingDialed))
        player.hook(Event.SummoningPointsRecharged, EventHandler(this, ::onSummoningPointsRecharged))
        player.hook(Event.PrayerPointsRecharged, EventHandler(this, ::onPrayerPointsRecharged))
    }

    protected fun fulfillTaskRequirement(player: Player, level: DiaryLevel, task: Int, attribute: String) {
        if (getAttribute(player, attribute, false)) return

        player.achievementDiaryManager.updateTask(
            player,
            diaryType,
            findIndexFor(level),
            task,
            false
        )

        setAttribute(player, "/save:$attribute", true)
    }

    protected fun whenTaskRequirementFulfilled(player: Player, attribute: String, then: () -> Unit) {
        if (getAttribute(player, attribute, false)) {
            then()
            removeAttribute(player, attribute)
        }
    }

    protected fun progressIncrementalTask(
        player: Player,
        level: DiaryLevel,
        task: Int,
        attribute: String,
        maxProgress: Int
    ) {
        if (isTaskCompleted(player, level, task)) return

        val newValue = getAttribute(player, attribute, 0) + 1

        setAttribute(
            player,
            "/save:${attribute}",
            newValue
        )

        if (newValue < maxProgress) {
            player.achievementDiaryManager.updateTask(
                player,
                diaryType,
                findIndexFor(level),
                task,
                false
            )
        } else {
            finishTask(player, level, task)
            removeAttribute(player, attribute)
        }
    }

    protected fun progressFlaggedTask(
        player: Player,
        level: DiaryLevel,
        task: Int,
        attribute: String,
        bit: Int,
        targetValue: Int
    ) {
        if (isTaskCompleted(player, level, task)) {
            return
        }

        val oldValue = getAttribute(player, attribute, 0)
        val newValue = oldValue or bit

        if (newValue != targetValue) {
            if ((oldValue and bit) != 0) return

            setAttribute(
                player,
                "/save:${attribute}",
                newValue
            )

            player.achievementDiaryManager.updateTask(
                player,
                diaryType,
                findIndexFor(level),
                task,
                false
            )
        } else {
            finishTask(player, level, task)
            removeAttribute(player, attribute)
        }
    }

    protected fun finishTask(player: Player, level: DiaryLevel, task: Int) {
        player.achievementDiaryManager.finishTask(
            player,
            diaryType,
            findIndexFor(level),
            task
        )
    }

    private fun isTaskCompleted(player: Player, level: DiaryLevel, task: Int): Boolean {
        return player.achievementDiaryManager.hasCompletedTask(
            diaryType,
            findIndexFor(level),
            task
        )
    }

    private fun findIndexFor(level: DiaryLevel): Int {
        val levelName = level.name.lowercase().replaceFirstChar { c -> c.uppercase() }
        val levelIndex = diaryType.levelNames.indexOf(levelName)

        if (levelIndex < 0) {
            throw IllegalArgumentException("'$levelName' was not found in diary '$diaryType'.")
        }

        return levelIndex
    }

    protected open fun onAreaVisited(player: Player) {
        areaTasks.forEach {
            it.whenSatisfied(player) {
                finishTask(
                    player,
                    it.diaryLevel,
                    it.taskId
                )
            }
        }
    }

    protected open fun onAreaLeft(player: Player) {}
    protected open fun onResourceProduced(player: Player, event: ResourceProducedEvent) {}
    protected open fun onNpcKilled(player: Player, event: NPCKillEvent) {}
    protected open fun onTeleported(player: Player, event: TeleportEvent) {}
    protected open fun onFireLit(player: Player, event: LitFireEvent) {}
    protected open fun onInteracted(player: Player, event: InteractionEvent) {}
    protected open fun onButtonClicked(player: Player, event: ButtonClickEvent) {}
    protected open fun onDialogueOpened(player: Player, event: DialogueOpenEvent) {}
    protected open fun onDialogueClosed(player: Player, event: DialogueCloseEvent) {}
    protected open fun onDialogueOptionSelected(player: Player, event: DialogueOptionSelectionEvent) {}
    protected open fun onUsedWith(player: Player, event: UseWithEvent) {}
    protected open fun onPickedUp(player: Player, event: PickUpEvent) {}
    protected open fun onInterfaceOpened(player: Player, event: InterfaceOpenEvent) {}
    protected open fun onAttributeSet(player: Player, event: AttributeSetEvent) {}
    protected open fun onAttributeRemoved(player: Player, event: AttributeRemoveEvent) {}
    protected open fun onSpellCast(player: Player, event: SpellCastEvent) {}
    protected open fun onItemAlchemized(player: Player, event: ItemAlchemizationEvent) {}
    protected open fun onItemEquipped(player: Player, event: ItemEquipEvent) {}
    protected open fun onItemUnequipped(player: Player, event: ItemUnequipEvent) {}
    protected open fun onItemPurchasedFromShop(player: Player, event: ItemShopPurchaseEvent) {}
    protected open fun onItemSoldToShop(player: Player, event: ItemShopSellEvent) {}
    protected open fun onJobAssigned(player: Player, event: JobAssignmentEvent) {}
    protected open fun onFairyRingDialed(player: Player, event: FairyRingDialEvent) {}
    protected open fun onSummoningPointsRecharged(player: Player, event: SummoningPointsRechargeEvent) {}
    protected open fun onPrayerPointsRecharged(player: Player, event: PrayerPointsRechargeEvent) {}
}