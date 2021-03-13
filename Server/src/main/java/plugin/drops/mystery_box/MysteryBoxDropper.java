package plugin.drops.mystery_box;

import core.game.node.item.Item;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.plugin.PluginManifest;
import org.rs09.consts.Items;
import core.tools.RandomFunction;
import plugin.drops.DropPlugin;
import plugin.drops.DropPlugins;

import java.util.ArrayList;
import java.util.List;

@Initializable
@PluginManifest(name="MysteryBox")
public class MysteryBoxDropper extends DropPlugin {

    //percentage chance of it dropping, between 1 and 100.
    private int chance = 15;
    List<Item> drops = new ArrayList<>(20); //the list of dropped items

    //standard way to initialize a drop plugin
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        DropPlugins.register(this);
        return this;
    }

    //default constructor used, called when the plugin manager initializes this plugin.
    //super() for a drop plugin simply means all NPCs can use this drop
    public MysteryBoxDropper(){
        super();
    }

    @Override
    public List<Item> handle() {
        boolean willDrop = RandomFunction.random(chance) == RandomFunction.random(chance);
        drops.clear();
        if(willDrop){
            drops.add(new Item(Items.MYSTERY_BOX_6199));
        }
        return drops;
    }
}
