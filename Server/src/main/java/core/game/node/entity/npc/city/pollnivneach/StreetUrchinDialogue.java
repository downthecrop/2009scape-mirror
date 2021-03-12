package core.game.node.entity.npc.city.pollnivneach;

import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles the street urchin npc TODO: Add more dialogue after/during the quest when it is implemented.
 * @author ceik
 */
@Initializable
public class StreetUrchinDialogue extends DialoguePlugin {
    public StreetUrchinDialogue(){
        /**
         * Empty
         */
    }

    public StreetUrchinDialogue(Player player){super(player);}

    @Override
    public DialoguePlugin newInstance(Player player){return new StreetUrchinDialogue(player);}

    public boolean open(Object... args){
        player.getDialogueInterpreter().sendDialogue("This child doesn't seem interested in you.");
        return true;
    }

    public boolean handle(int interfaceId, int buttonId){
        switch(stage){
            case 0:
                end();
                break;
        }
        return true;
    }
    @Override
    public int[] getIds(){return new int[] {6357};}
}
