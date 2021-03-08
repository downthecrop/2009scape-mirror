package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.game.node.entity.state.EntityState;
import core.game.content.consumable.ConsumableEffect;

public class RemoveStateEffect extends ConsumableEffect {
    int state = -1;
    String statekey;
    @Deprecated
    public RemoveStateEffect(int state){
        this.state = state;
    }
    public RemoveStateEffect(String key){
        statekey = key;
    }
    @Override
    public void activate(Player p) {
        if(state == -1){
            p.clearState(statekey);
            return;
        }
        p.getStateManager().remove(EntityState.values()[state]);
    }
}
