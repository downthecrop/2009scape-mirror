package rs09.game.node.entity.skill.hunter.tracking

import core.cache.def.impl.SceneryDefinition
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

@Initializable
class CommonKebbitEast : HunterTracking() {

    init {
        initialMap = hashMapOf(
                19439 to arrayListOf(
                        TrailDefinition(0,TrailType.LINKING, false,Location.create(2354, 3595, 0),Location.create(2360, 3602, 0)),
                        TrailDefinition(3,TrailType.LINKING,false,Location.create(2354, 3595, 0),Location.create(2355, 3601, 0)),
                        TrailDefinition(6,TrailType.LINKING,false,Location.create(2354, 3594, 0),Location.create(2349, 3604, 0))
                ),
                19440 to arrayListOf(
                        TrailDefinition(18,TrailType.LINKING,true,Location.create(2361, 3611, 0),Location.create(2360, 3602, 0)),
                        TrailDefinition(21,TrailType.LINKING,true,Location.create(2360, 3612, 0),Location.create(2357, 3607, 0))
                )
        )

        linkingTrails = arrayListOf(
                TrailDefinition(24,TrailType.LINKING,false,Location.create(2357, 3607, 0),Location.create(2354, 3609, 0),Location.create(2355, 3608, 0)),
                TrailDefinition(27,TrailType.LINKING,false,Location.create(2354, 3609, 0),Location.create(2349, 3604, 0),Location.create(2351, 3608, 0)),
                TrailDefinition(9,TrailType.LINKING,false ,Location.create(2360, 3602, 0),Location.create(2355, 3601, 0),Location.create(2358, 3599, 0)),
                TrailDefinition(12,TrailType.LINKING,false,Location.create(2355, 3601, 0),Location.create(2349, 3604, 0),Location.create(2352, 3603, 0)),
                TrailDefinition(15,TrailType.LINKING,false,Location.create(2360, 3602, 0),Location.create(2357, 3607, 0),Location.create(2358, 3603, 0))
        )
        experience = 36.0
        varp = 919
        trailLimit = 3
        attribute = "hunter:tracking:commontrail"
        indexAttribute = "hunter:tracking:commonIndex"
        rewards = arrayOf(Item(Items.COMMON_KEBBIT_FUR_10121), Item(Items.BONES_526),Item(Items.RAW_BEAST_MEAT_9986))
        KEBBIT_ANIM = Animation(5259)
    }


    override fun newInstance(arg: Any?): Plugin<Any> {
        if(!linkingTrails.contains(initialMap.values.random()[0])){
            addExtraTrails()
        }
        SceneryDefinition.forId(19439).handlers["option:inspect"] = this
        SceneryDefinition.forId(19440).handlers["option:inspect"] = this
        SceneryDefinition.forId(19360).handlers["option:inspect"] = this
        SceneryDefinition.forId(19361).handlers["option:inspect"] = this
        SceneryDefinition.forId(19362).handlers["option:inspect"] = this
        SceneryDefinition.forId(19363).handlers["option:inspect"] = this
        SceneryDefinition.forId(19364).handlers["option:inspect"] = this
        SceneryDefinition.forId(19365).handlers["option:inspect"] = this
        SceneryDefinition.forId(19356).handlers["option:inspect"] = this
        SceneryDefinition.forId(19357).handlers["option:inspect"] = this
        SceneryDefinition.forId(19358).handlers["option:inspect"] = this
        SceneryDefinition.forId(19359).handlers["option:inspect"] = this
        SceneryDefinition.forId(19375).handlers["option:inspect"] = this
        SceneryDefinition.forId(19376).handlers["option:inspect"] = this
        SceneryDefinition.forId(19377).handlers["option:inspect"] = this
        SceneryDefinition.forId(19378).handlers["option:inspect"] = this
        SceneryDefinition.forId(19379).handlers["option:inspect"] = this
        SceneryDefinition.forId(19372).handlers["option:inspect"] = this
        SceneryDefinition.forId(19380).handlers["option:inspect"] = this
        SceneryDefinition.forId(19374).handlers["option:inspect"] = this
        SceneryDefinition.forId(19373).handlers["option:inspect"] = this
        SceneryDefinition.forId(19428).handlers["option:search"] = this
        SceneryDefinition.forId(19428).handlers["option:attack"] = this

        return this
    }
}