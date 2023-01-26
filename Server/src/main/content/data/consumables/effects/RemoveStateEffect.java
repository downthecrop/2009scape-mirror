package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.player.Player;
import core.game.node.entity.state.EntityState;

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
