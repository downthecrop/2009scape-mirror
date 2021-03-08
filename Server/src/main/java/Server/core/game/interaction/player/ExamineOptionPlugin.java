package core.game.interaction.player;

import core.game.interaction.Option;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.interaction.inter.PlayerExamineInterfacePlugin;

/**
 * Package -> core.game.interaction.player
 * Created on -> 9/13/2016 @7:53 AM for 530
 *
 * @author Ethan Kyle Millard
 */
@Initializable
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
