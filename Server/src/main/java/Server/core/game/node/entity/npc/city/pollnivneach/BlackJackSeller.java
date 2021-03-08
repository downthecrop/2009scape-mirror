package core.game.node.entity.npc.city.pollnivneach;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * Stand-in dialogue for Black Jack Seller til blackjacking/the quest is implemented
 * @author ceik
 */
@Initializable
public class BlackJackSeller extends DialoguePlugin {
    public BlackJackSeller(){
        /**
         * empty.
         */
    }
    public BlackJackSeller(Player player){super(player);}

    @Override
    public DialoguePlugin newInstance(Player player){return new BlackJackSeller(player);}

    @Override
    public boolean open(Object... args){
        npc("I'm not interested in selling to you. Not yet...");
        stage = 0;
        return true;
    }

    @Override
    public boolean handle(int intefaceId, int objectId){
        switch(stage){
            case 0:
                end();
                break;
        }
        return true;
    }

    @Override
    public int[] getIds() {return new int[] {2548};}
}
