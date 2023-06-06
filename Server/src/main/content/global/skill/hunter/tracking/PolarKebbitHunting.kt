package content.global.skill.hunter.tracking

import core.cache.def.impl.SceneryDefinition
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

@Initializable
class PolarKebbitHunting : HunterTracking() {

    init {
        KEBBIT_ANIM = Animation(5256)
        trailLimit = 3
        attribute = "hunter:tracking:polartrail"
        indexAttribute = "hunter:tracking:polarindex"
        rewards = arrayOf(Item(Items.RAW_BEAST_MEAT_9986),Item(Items.POLAR_KEBBIT_FUR_10117),Item(Items.BONES_526))
        tunnelEntrances = arrayOf(
                Location.create(2711, 3819, 1),
                Location.create(2714, 3821, 1),
                Location.create(2718, 3829, 1),
                Location.create(2721, 3827, 1),
                Location.create(2718, 3832, 1),
                Location.create(2715, 3820, 1)
        )
        initialMap = hashMapOf(
                19640 to arrayListOf(
                        TrailDefinition(3061,TrailType.TUNNEL,false,Location.create(2712, 3831, 1),Location.create(2718, 3832, 1)),
                        TrailDefinition(3060,TrailType.LINKING,true,Location.create(2712, 3831, 1),Location.create(2716, 3827, 1),Location.create(2713,3827,1)),
                        TrailDefinition(3057,TrailType.LINKING,false,Location.create(2712, 3831, 1),Location.create(2708, 3819, 1),Location.create(2708,3825,1))
                ),
                19641 to arrayListOf(
                        TrailDefinition(3053,TrailType.LINKING,true,Location.create(2718, 3820, 1),Location.create(2708, 3819, 1),Location.create(2712,3815,1)),
                        TrailDefinition(3055,TrailType.TUNNEL,false, Location.create(2718, 3820, 1),Location.create(2715, 3820, 1)),
                        TrailDefinition(3056,TrailType.TUNNEL,false,Location.create(2718, 3820, 1),Location.create(2721, 3827, 1))
                )
        )
        linkingTrails = arrayListOf(
                TrailDefinition(3058,TrailType.LINKING,true,Location.create(2714,3821,1),Location.create(2716, 3827, 1)),
                TrailDefinition(3059,TrailType.TUNNEL,true,Location.create(2716, 3827, 1),Location.create(2718,3829,1)),
                TrailDefinition(3054,TrailType.TUNNEL,false,Location.create(2708, 3819, 1),Location.create(2711, 3819, 1))
        )
        experience = 30.0
        varp = 926
        requiredLevel = 1
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        addExtraTrails()
        SceneryDefinition.forId(19640).handlers["option:inspect"] = this
        SceneryDefinition.forId(19641).handlers["option:inspect"] = this
        SceneryDefinition.forId(19435).handlers["option:inspect"] = this
        SceneryDefinition.forId(36689).handlers["option:inspect"] = this
        SceneryDefinition.forId(36690).handlers["option:inspect"] = this
        SceneryDefinition.forId(19421).handlers["option:inspect"] = this
        SceneryDefinition.forId(19424).handlers["option:inspect"] = this
        SceneryDefinition.forId(19426).handlers["option:inspect"] = this
        SceneryDefinition.forId(19419).handlers["option:inspect"] = this
        SceneryDefinition.forId(19420).handlers["option:inspect"] = this
        SceneryDefinition.forId(19423).handlers["option:inspect"] = this
        SceneryDefinition.forId(36688).handlers["option:inspect"] = this
        SceneryDefinition.forId(19435).handlers["option:search"] = this
        SceneryDefinition.forId(19435).handlers["option:attack"] = this
        return this
    }
}
