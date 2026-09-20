package content.global.skill.crafting

import content.global.skill.construction.CrestType
import org.rs09.consts.Items

/**
 * Enumerates the heraldic items players can craft with easels in their POHs.
 * @author Bishop
 */

enum class HeraldicProduct(
    val primaryMaterialId: Int,
    val secondaryMaterialId: Int?,
    val baseProductId: Int,
    val craftingReq: Int,
    val craftingXp: Double,
    val constructionReq: Int = 16) {

    STEEL_HELM(Items.STEEL_FULL_HELM_1157, null,
        Items.STEEL_HERALDIC_HELM_8682, 38, 37.0),
    RUNE_HELM(Items.RUNE_FULL_HELM_1163, null,
        Items.RUNE_HERALDIC_HELM_8464, STEEL_HELM.craftingReq, STEEL_HELM.craftingXp),
    STEEL_SHIELD(Items.STEEL_KITESHIELD_1193, null,
        Items.STEEL_KITESHIELD_8746, 43, 40.0),
    RUNE_SHIELD(Items.RUNE_KITESHIELD_1201, null,
        Items.RUNE_KITESHIELD_8714, STEEL_SHIELD.craftingReq, STEEL_SHIELD.craftingXp),
    BANNER(Items.BOLT_OF_CLOTH_8790, Items.PLANK_960,
        Items.BANNER_8650, 48, 42.5);

    fun productForCrest(crest: CrestType): Int = baseProductId + (crest.ordinal * 2)
}