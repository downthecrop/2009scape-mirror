package rs09.game.node.entity.player.link.diary

import api.LoginListener
import api.MapArea
import api.events.*
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import rs09.game.Event

abstract class DiaryEventHookBase : MapArea, LoginListener {
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
        player.hook(Event.UsedWith, UseWithEvents(this))
        player.hook(Event.PickedUp, PickUpEvents(this))
        player.hook(Event.InterfaceOpened, InterfaceOpenEvents(this))
        player.hook(Event.AttributeSet, AttributeSetEvents(this))
        player.hook(Event.AttributeRemoved, AttributeRemoveEvents(this))
        player.hook(Event.SpellCast, SpellCastEvents(this))
        player.hook(Event.ItemAlchemized, ItemAlchemizationEvents(this))
    }

    protected fun finishTask(entity: Player, diary: DiaryType, level: DiaryLevel, task: Int) {
        val levelName = level.name.lowercase().replaceFirstChar { c -> c.uppercase() }
        val levelIndex = diary.levelNames.indexOf(levelName)

        if (levelIndex < 0) {
            throw IllegalArgumentException("'$levelName' was not found in diary '$diary'.")
        }

        entity.achievementDiaryManager.finishTask(entity, diary, levelIndex, task)
    }

    protected fun isTaskCompleted(entity: Player, diary: DiaryType, level: DiaryLevel, task: Int): Boolean {
        val levelName = level.name.lowercase().replaceFirstChar { c -> c.uppercase() }
        val levelIndex = diary.levelNames.indexOf(levelName)

        if (levelIndex < 0) {
            throw IllegalArgumentException("'$levelName' was not found in diary '$diary'.")
        }

        return entity.achievementDiaryManager.hasCompletedTask(diary, levelIndex, task)
    }

    protected open fun onAreaVisited(player: Player) {}
    protected open fun onAreaLeft(player: Player) {}
    protected open fun onResourceProduced(player: Player, event: ResourceProducedEvent) {}
    protected open fun onNpcKilled(player: Player, event: NPCKillEvent) {}
    protected open fun onTeleported(player: Player, event: TeleportEvent) {}
    protected open fun onFireLit(player: Player, event: LitFireEvent) {}
    protected open fun onInteracted(player: Player, event: InteractionEvent) {}
    protected open fun onButtonClicked(player: Player, event: ButtonClickEvent) {}
    protected open fun onUsedWith(player: Player, event: UseWithEvent) {}
    protected open fun onPickedUp(player: Player, event: PickUpEvent) {}
    protected open fun onInterfaceOpened(player: Player, event: InterfaceOpenEvent) {}
    protected open fun onAttributeSet(player: Player, event: AttributeSetEvent) {}
    protected open fun onAttributeRemoved(player: Player, event: AttributeRemoveEvent) {}
    protected open fun onSpellCast(player: Player, event: SpellCastEvent) {}
    protected open fun onItemAlchemized(player: Player, event: ItemAlchemizationEvent) {}
}