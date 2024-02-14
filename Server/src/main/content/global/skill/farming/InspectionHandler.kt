package content.global.skill.farming

import core.api.*
import core.cache.def.impl.SceneryDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.prependArticle

@Initializable
class InspectionHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.setOptionHandler("inspect", this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        node ?: return false
        player ?: return false
        val patch = FarmingPatch.forObject(node.asScenery())
        if (patch == null) {
            sendMessage(player, "This is an improperly handled inspect option. Report this please.")
            return true
        }

        val p = patch.getPatchFor(player)
        val patchName = p.patch.type.displayName()

        val statusPatchType = if (patch == FarmingPatch.TROLL_STRONGHOLD_HERB) "This is a very special herb patch."
            else "This is ${prependArticle(patchName)}."

        val statusCompost = if (p.compost == CompostType.NONE) "The soil has not been treated."
            else "The soil has been treated with ${p.compost.name.lowercase()}."

        val statusStage = if (p.plantable == Plantable.SCARECROW) ""
            else if (p.isWeedy()) "The patch needs weeding."
            else if (p.isEmptyAndWeeded()) "The patch is empty and weeded."
            else if (p.isDiseased && !p.isDead) "The patch is diseased and needs attending to before it dies."
            else if (p.isDead) "The patch has become infected by disease and has died."
            else if (p.isGrown()) "The patch is fully grown."
            else "The patch has something growing in it."

        val statusGardener = if (patch == FarmingPatch.TROLL_STRONGHOLD_HERB) "My Arm will look after this patch for you."
            else if (p.protectionPaid) "A nearby gardener is looking after this patch for you."
            else ""

        sendMessage(player, "$statusPatchType $statusCompost $statusStage".trim())
        if (statusGardener != "") sendMessage(player, statusGardener)

        return true
    }

}
