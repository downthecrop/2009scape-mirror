package plugin.interaction.player;

import org.crandor.game.interaction.Option;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;
import org.crandor.plugin.Plugin;
import plugin.interaction.inter.PlayerExamineInterfacePlugin;

/**
 * Package -> plugin.interaction.player
 * Created on -> 9/13/2016 @7:53 AM for 530
 *
 * @author Ethan Kyle Millard
 */
@InitializablePlugin
public class ExamineOptionPlugin extends OptionHandler {
    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        Option._P_EXAMINE.setHandler(this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        PlayerExamineInterfacePlugin component = new PlayerExamineInterfacePlugin();
        component.prepareInterface(player, (Player) node);
        return false;
    }

}
