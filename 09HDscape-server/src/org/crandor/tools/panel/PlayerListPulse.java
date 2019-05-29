package org.crandor.tools.panel;

import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.task.Pulse;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.callback.CallBack;

/**
 * Package -> org.keldagrim.tools.panel
 * Created on -> 9/1/2016 @2:09 AM for Server
 *
 * @author Ethan Kyle Millard
 */
public final class PlayerListPulse extends Pulse implements CallBack {

    private Player player;

    public PlayerListPulse(Player player) {
        this.player = player;
    }

    @Override
    public boolean pulse() {
            System.out.println("new pulse");
            player.getController().getPlayersList().appendText(player.getName() + "\n");
            System.out.println(player.getName());
        return false;
    }

    @Override
    public boolean call() {
        GameWorld.submit(this);
        return true;
    }
}
