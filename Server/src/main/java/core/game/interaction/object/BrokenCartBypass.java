package core.game.interaction.object;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;


/**
 * Temporarily fix to allow people into shilo village by foot until the Shilo Village quest is added
 * addresses #92
 * @author ceik
 */
@Initializable
public class BrokenCartBypass extends OptionHandler {
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(2216).getHandlers().put("option:look-at",this);
        return this;
    }

    public final void jumpOver(Player player, String options, final Location location){
        player.lock();
        player.animate(new Animation(839));
        player.getImpactHandler().setDisabledTicks(4);
        GameWorld.getPulser().submit(new Pulse(1, player) {
            @Override
            public boolean pulse() {
                player.unlock();
                player.getProperties().setTeleportLocation(location);
                player.getAnimator().reset();
                return true;
            }
        });
    }
    public final boolean handle(Player player, Node node, String options){
        Location location = new Location(0,0);
        Location playerloc = new Location(player.getLocation().getX(),player.getLocation().getY());
        if(options.equals("look-at")) {
            if (playerloc.getX() > 2878) {
                location = new Location(2876, 2952);
            } else if (playerloc.getX() < 2879) {
                location = new Location(2880, 2952);
            }
        }
        jumpOver(player,options,location);
        return true;
    }
}
