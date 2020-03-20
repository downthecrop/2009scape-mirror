package plugin.npc.city.nardah;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;

/**
 * Handles Ghaslor dialogue
 * @author ceik
 */

//TODO: Add post-quest dialogue
@InitializablePlugin
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
