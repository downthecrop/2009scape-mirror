package core.net.packet.context;

import core.game.node.entity.player.Player;
import core.net.packet.Context;

public class VarbitContext implements Context {

    Player player;
    public int varbitId;
    public int value;

    public VarbitContext(Player player, int varbitId, int value){
        this.player = player;
        this.varbitId = varbitId;
        this.value = value;
    }

    @Override
    public Player getPlayer() {
        return player;
    }
}
