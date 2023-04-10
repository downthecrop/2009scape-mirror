package rs09.game.content.activity.castlewars

import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

object CastleWars {
    // Scenery IDs
    const val joinSaradominTeamPortal: Int = Scenery.SARADOMIN_PORTAL_4387
    const val joinZamorakTeamPortal: Int = Scenery.ZAMORAK_PORTAL_4388
    const val saradominLeaveLobbyPortal: Int = Scenery.PORTAL_4389
    const val zamorakLeaveLobbyPortal: Int = Scenery.PORTAL_4390
    const val joinGuthixTeamPortal: Int = Scenery.GUTHIX_PORTAL_4408
    const val cwCastleClimbingRope: Int = Scenery.CLIMBING_ROPE_36312
    const val castleWaterTap: Int = Scenery.TAP_4482
    const val cwSteppingStones: Int = Scenery.STEPPING_STONE_4411

    // Map of the item table scenery id to the item id they give
    val cwTableItemRewardMap = mapOf(
        Scenery.TABLE_36573 to Items.TOOLKIT_4051,            // Saradomin Toolkit Table
        Scenery.TABLE_36580 to Items.TOOLKIT_4051,            // Zamorak Toolkit Table
        Scenery.TABLE_36574 to Items.ROCK_4043,               // Saradomin Rock Table
        Scenery.TABLE_36581 to Items.ROCK_4043,               // Zamorak Rock Table
        Scenery.TABLE_36575 to Items.BARRICADE_4053,          // Saradomin Barricade Table
        Scenery.TABLE_36582 to Items.BARRICADE_4053,          // Zamorak Barricade Table
        Scenery.TABLE_36576 to Items.CLIMBING_ROPE_4047,      // Saradomin Climbing Rope Table
        Scenery.TABLE_36583 to Items.CLIMBING_ROPE_4047,      // Zamorak Climbing Rope Table
        Scenery.TABLE_36577 to Items.EXPLOSIVE_POTION_4045,   // Saradomin Explosive Potion Table
        Scenery.TABLE_36584 to Items.EXPLOSIVE_POTION_4045,   // Zamorak Explosive Potion Table
        Scenery.TABLE_36578 to Items.BRONZE_PICKAXE_1265,     // Saradomin Pickaxe Table
        Scenery.TABLE_36585 to Items.BRONZE_PICKAXE_1265,     // Zamorak Pickaxe Table
        Scenery.TABLE_36579 to Items.BANDAGES_4049,           // Saradomin Bandages Table
        Scenery.TABLE_36586 to Items.BANDAGES_4049            // Zamorak Bandages Table
    )

    val cwCastleBattlementsMap = mapOf(
        Scenery.BATTLEMENTS_4446 to Scenery.BATTLEMENTS_36313,
        Scenery.BATTLEMENTS_4447 to Scenery.BATTLEMENTS_36314
    )

    // Item IDs
    const val saradominTeamHoodedCloak: Int = Items.HOODED_CLOAK_4041
    const val zamorakTeamHoodedCloak: Int = Items.HOODED_CLOAK_4042
    const val saradominFlag: Int = Items.SARADOMIN_BANNER_4037 // Might be 4038
    const val zamorakFlag: Int = Items.ZAMORAK_BANNER_4039 // Might be 4040
    const val cwPickaxe: Int = Items.BRONZE_PICKAXE_1265
    const val cwRock: Int = Items.ROCK_4043
    const val cwExplosivePotion: Int = Items.EXPLOSIVE_POTION_4045
    const val cwClimbingRope: Int = Items.CLIMBING_ROPE_4047
    const val cwBandages: Int = Items.BANDAGES_4049
    const val cwToolkit: Int = Items.TOOLKIT_4051
    const val cwBarricade: Int = Items.BARRICADE_4053
    const val cwManualBook: Int = Items.CASTLEWARS_MANUAL_4055
    const val cwTicketRewardCurrency: Int = Items.CASTLE_WARS_TICKET_4067

    // NPC IDs
    const val sheep = NPCs.SHEEP_1529
    const val imp = NPCs.IMP_1531
    const val rabbit = NPCs.RABBIT_1530
    const val unknownCwarsBarricade1 = NPCs.BARRICADE_1532
    const val unknownCwarsBarricadeOnFire = NPCs.BARRICADE_1533
    const val unknownCwarsBarricade2 = NPCs.BARRICADE_1534
    const val unknownCwarsBarricade3 = NPCs.BARRICADE_1535

    // Locations
    val lobbyBankArea: ZoneBorders = ZoneBorders(2440, 3092, 2444, 3086, 0)

    // Strings
    const val saradominName = "Saradomin"
    const val zamorakName = "Zamorak"
    const val guthixName = "Guthix"
    const val portalAttribute = "castlewars_portal"
    const val invFullMessage = "Your inventory is too full to hold any more "

    // Ints
    const val gameTimeMinutes = 20
    const val gameCooldownMinutes = 5
    const val ropeAliveTicks = -1 // TODO: Find amount of time until wall reverts to non rope state

}