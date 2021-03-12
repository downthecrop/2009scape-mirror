package core.plugin.CorePluginTypes;

import rs09.game.system.SystemLogger;

import java.util.ArrayList;
import java.util.List;

public class Managers {

    private static List<ManagerPlugin> plugins = new ArrayList<>();
    public static void register(ManagerPlugin plugin){
        if(plugin != null){
            plugins.add(plugin);
        }
    }

    public static void tick(){
        for(ManagerPlugin p : plugins){
            p.tick();
        }
    }
}
