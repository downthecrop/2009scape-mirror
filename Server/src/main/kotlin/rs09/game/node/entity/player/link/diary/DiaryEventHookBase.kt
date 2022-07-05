package rs09.game.node.entity.player.link.diary

import api.LoginListener
import api.MapArea
import api.events.*
import api.getAttribute
import api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.world.map.zone.ZoneBorders
import rs09.game.Event

abstract class DiaryEventHookBase(val diaryType: DiaryType) : MapArea, LoginListener {
    protected companion object {
        private fun<T> forEligibleEntityDo(entity: Entity, event: T, handler: (Player, T) -> Unit) {
            if (entity !is Player) return
            if (entity.isArtificial) return

            handler(entity, event)
        }
    }

    class ResourceProductionEvents(val owner: DiaryEventHookBase) : EventHook<ResourceProducedEvent> {
        override fun process(entity: Entity, event: ResourceProducedEvent) {
            forEligibleEntityDo(entity, event, owner::onResourceProduced)
        }
    }

    class NPCKillEvents(val owner: DiaryEventHookBase) : EventHook<NPCKillEvent> {
        override fun process(entity: Entity, event: NPCKillEvent) {
            forEligibleEntityDo(entity, event, owner::onNpcKilled)
        }
    }

    class TeleportEvents(val owner: DiaryEventHookBase) : EventHook<TeleportEvent> {
        override fun process(entity: Entity, event: TeleportEvent) {
            forEligibleEntityDo(entity, event, owner::onTeleported)
        }
    }

    class FiremakingEvents(val owner: DiaryEventHookBase) : EventHook<LitFireEvent> {
        override fun process(entity: Entity, event: LitFireEvent) {
            forEligibleEntityDo(entity, event, owner::onFireLit)
        }
    }

    class InteractionEvents(val owner: DiaryEventHookBase) : EventHook<InteractionEvent> {
        override fun process(entity: Entity, event: InteractionEvent) {
            forEligibleEntityDo(entity, event, owner::onInteracted)
        }
    }

    class ButtonClickEvents(val owner: DiaryEventHookBase) : EventHook<ButtonClickEvent> {
        override fun process(entity: Entity, event: ButtonClickEvent) {
            forEligibleEntityDo(entity, event, owner::onButtonClicked)
        }
    }

    class DialogueOpenEvents(val owner: DiaryEventHookBase) : EventHook<DialogueOpenEvent> {
        override fun process(entity: Entity, event: DialogueOpenEvent) {
            forEligibleEntityDo(entity, event, owner::onDialogueOpened)
        }
    }

    class DialogueCloseEvents(val owner: DiaryEventHookBase) : EventHook<DialogueCloseEvent> {
        override fun process(entity: Entity, event: DialogueCloseEvent) {
            forEligibleEntityDo(entity, event, owner::onDialogueClosed)
        }
    }

    class DialogueOptionSelectionEvents(val owner: DiaryEventHookBase) : EventHook<DialogueOptionSelectionEvent> {
        override fun process(entity: Entity, event: DialogueOptionSelectionEvent) {
            forEligibleEntityDo(entity, event, owner::onDialogueOptionSelected)
        }
    }

    class UseWithEvents(val owner: DiaryEventHookBase) : EventHook<UseWithEvent> {
        override fun process(entity: Entity, event: UseWithEvent) {
            forEligibleEntityDo(entity, event, owner::onUsedWith)
        }
    }

    class PickUpEvents(val owner: DiaryEventHookBase) : EventHook<PickUpEvent> {
        override fun process(entity: Entity, event: PickUpEvent) {
            forEligibleEntityDo(entity, event, owner::onPickedUp)
        }
    }

    class InterfaceOpenEvents(val owner: DiaryEventHookBase) : EventHook<InterfaceOpenEvent> {
        override fun process(entity: Entity, event: InterfaceOpenEvent) {
            forEligibleEntityDo(entity, event, owner::onInterfaceOpened)
        }
    }

    class AttributeSetEvents(val owner: DiaryEventHookBase) : EventHook<AttributeSetEvent> {
        override fun process(entity: Entity, event: AttributeSetEvent) {
            forEligibleEntityDo(entity, event, owner::onAttributeSet)
        }
    }

    class AttributeRemoveEvents(val owner: DiaryEventHookBase) : EventHook<AttributeRemoveEvent> {
        override fun process(entity: Entity, event: AttributeRemoveEvent) {
            forEligibleEntityDo(entity, event, owner::onAttributeRemoved)
        }
    }

    class SpellCastEvents(val owner: DiaryEventHookBase) : EventHook<SpellCastEvent> {
        override fun process(entity: Entity, event: SpellCastEvent) {
            forEligibleEntityDo(entity, event, owner::onSpellCast)
        }
    }

    class ItemAlchemizationEvents(val owner: DiaryEventHookBase) : EventHook<ItemAlchemizationEvent> {
        override fun process(entity: Entity, event: ItemAlchemizationEvent) {
            forEligibleEntityDo(entity, event, owner::onItemAlchemized)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf()
    }

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
        player.hook(Event.ResourceProduced, ResourceProductionEvents(this))
        player.hook(Event.NPCKilled, NPCKillEvents(this))
        player.hook(Event.Teleported, TeleportEvents(this))
        player.hook(Event.FireLit, FiremakingEvents(this))
        player.hook(Event.Interacted, InteractionEvents(this))
        player.hook(Event.ButtonClicked, ButtonClickEvents(this))
        player.hook(Event.DialogueOpened, DialogueOpenEvents(this))
        player.hook(Event.DialogueOptionSelected, DialogueOptionSelectionEvents(this))
        player.hook(Event.DialogueClosed, DialogueCloseEvents(this))
        player.hook(Event.UsedWith, UseWithEvents(this))
        player.hook(Event.PickedUp, PickUpEvents(this))
        player.hook(Event.InterfaceOpened, InterfaceOpenEvents(this))
        player.hook(Event.AttributeSet, AttributeSetEvents(this))
        player.hook(Event.AttributeRemoved, AttributeRemoveEvents(this))
        player.hook(Event.SpellCast, SpellCastEvents(this))
        player.hook(Event.ItemAlchemized, ItemAlchemizationEvents(this))
    }

    protected fun progressIncrementalTask(player: Player, level: DiaryLevel, task: Int, attribute: String, maxProgress: Int) {
        if (isTaskCompleted(player, level, task)) return

        val levelName = level.name.lowercase().replaceFirstChar { c -> c.uppercase() }
        val levelIndex = diaryType.levelNames.indexOf(levelName)

        val newValue = getAttribute(player, attribute, 0) + 1

        setAttribute(
            player,
            "/save:${attribute}",
            newValue
        )

        if (newValue < maxProgress) {
            player.achievementDiaryManager.updateTask(player, diaryType, levelIndex, task, false)
        } else {
            finishTask(player, level, task)
        }
    }

    protected fun progressFlaggedTask(player: Player, level: DiaryLevel, task: Int, attribute: String, bit: Int, targetValue: Int) {
        if (isTaskCompleted(player, level, task)) return

        val levelName = level.name.lowercase().replaceFirstChar { c -> c.uppercase() }
        val levelIndex = diaryType.levelNames.indexOf(levelName)

        val newValue = getAttribute(player, attribute, 0) + 1

        setAttribute(
            player,
            "/save:${attribute}",
            newValue or bit
        )

        if (newValue != targetValue) {
            player.achievementDiaryManager.updateTask(player, diaryType, levelIndex, task, false)
        } else {
            finishTask(player, level, task)
        }
    }

    protected fun finishTask(player: Player, level: DiaryLevel, task: Int) {
        val levelName = level.name.lowercase().replaceFirstChar { c -> c.uppercase() }
        val levelIndex = diaryType.levelNames.indexOf(levelName)

        if (levelIndex < 0) {
            throw IllegalArgumentException("'$levelName' was not found in diary '$diaryType'.")
        }

        player.achievementDiaryManager.finishTask(player, diaryType, levelIndex, task)
    }

    protected fun isTaskCompleted(player: Player, level: DiaryLevel, task: Int): Boolean {
        val levelName = level.name.lowercase().replaceFirstChar { c -> c.uppercase() }
        val levelIndex = diaryType.levelNames.indexOf(levelName)

        if (levelIndex < 0) {
            throw IllegalArgumentException("'$levelName' was not found in diary '$diaryType'.")
        }

        return player.achievementDiaryManager.hasCompletedTask(diaryType, levelIndex, task)
    }

    protected open fun onAreaVisited(player: Player) {}
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
}