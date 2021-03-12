package core.game.node.entity.npc.city.nardah;

import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles Ghaslor dialogue
 * @author ceik
 */

//TODO: Add post-quest dialogue
@Initializable
public class Ghaslor extends DialoguePlugin {
    public Ghaslor(){
        /**
         * empty
         */
    }
    public Ghaslor(Player player){
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player){return new Ghaslor(player);}

    @Override
    public boolean open(Object... args){
        String gender = player.isMale() ? "gentleman" : "lady";
        npc("Good day young " + gender);
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId){
        switch(stage) {
            case 0:
                end();
                break;
        }
        return true;
    }
    public int[] getIds() {return new int[] {3029};}
}
