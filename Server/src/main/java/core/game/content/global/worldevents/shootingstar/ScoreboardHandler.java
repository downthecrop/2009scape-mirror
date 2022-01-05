package core.game.content.global.worldevents.shootingstar;

import core.cache.def.impl.SceneryDefinition;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import rs09.game.world.GameWorld;
import core.plugin.Plugin;

public class ScoreboardHandler extends OptionHandler {
    int index = 0;
    int ifaceid = ShootingStarScoreboard.iface.getId();
    @Override
    public boolean handle(Player player, Node node, String option) {
        ScoreboardManager.getEntries().forEach(e -> {
            player.getPacketDispatch().sendString("" + Math.floor(((GameWorld.getTicks() - e.time) / 0.6) / 60) + " minutes ago",ifaceid,index + 6);
            player.getPacketDispatch().sendString(e.playerName,ifaceid,index + 11);
            index++;
        });
        index = 0;
        player.getInterfaceManager().open(ShootingStarScoreboard.iface);
        return true;
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(38669).getHandlers().put("option:read",this);

        return this;
    }
}
