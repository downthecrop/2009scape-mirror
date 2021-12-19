package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

public class VarcUpdateContext implements Context {

    Player player;
    public int varcId;
    public int value;

    public VarcUpdateContext(Player player, int varcId, int value){
        this.player = player;
        this.varcId = varcId;
        this.value = value;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}

