package rs09.game

import api.events.*

object Event {
    @JvmStatic val ResourceProduced = ResourceProducedEvent::class.java
    @JvmStatic val NPCKilled = NPCKillEvent::class.java
    @JvmStatic val Teleported = TeleportEvent::class.java
    @JvmStatic val FireLit = LitFireEvent::class.java
    @JvmStatic val Interacted = InteractionEvent::class.java
    @JvmStatic val ButtonClicked = ButtonClickEvent::class.java
    @JvmStatic val DialogueOpened = DialogueOpenEvent::class.java
    @JvmStatic val DialogueOptionSelected = DialogueOptionSelectionEvent::class.java
    @JvmStatic val DialogueClosed = DialogueCloseEvent::class.java
    @JvmStatic val UsedWith = UseWithEvent::class.java
    @JvmStatic val SelfDeath = SelfDeathEvent::class.java
    @JvmStatic val Tick = TickEvent::class.java
    @JvmStatic val PickedUp = PickUpEvent::class.java
    @JvmStatic val InterfaceOpened = InterfaceOpenEvent::class.java
    @JvmStatic val InterfaceClosed = InterfaceCloseEvent::class.java
    @JvmStatic val AttributeSet = AttributeSetEvent::class.java
    @JvmStatic val AttributeRemoved = AttributeRemoveEvent::class.java
    @JvmStatic val SpellCast = SpellCastEvent::class.java
    @JvmStatic val ItemAlchemized = ItemAlchemizationEvent::class.java
    @JvmStatic val ItemEquipped = ItemEquipEvent::class.java
    @JvmStatic val ItemUnequipped = ItemUnequipEvent::class.java
    @JvmStatic val ItemPurchased = ItemShopPurchaseEvent::class.java
    @JvmStatic val ItemSold = ItemShopSellEvent::class.java
    @JvmStatic val JobAssigned = JobAssignmentEvent::class.java
    @JvmStatic val FairyRingDialed = FairyRingDialEvent::class.java
    @JvmStatic val VarbitUpdated = VarbitUpdateEvent::class.java
    @JvmStatic val DynamicSkillLevelChanged = DynamicSkillLevelChangeEvent::class.java
    @JvmStatic val SummoningPointsRecharged = SummoningPointsRechargeEvent::class.java
    @JvmStatic val PrayerPointsRecharged = PrayerPointsRechargeEvent::class.java
    @JvmStatic val XpGained = XPGainEvent::class.java
}