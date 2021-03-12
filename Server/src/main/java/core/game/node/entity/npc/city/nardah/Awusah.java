package core.game.node.entity.npc.city.nardah;

import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;

/**
 * Handles Awusah pre-quest
 * @author ceik
 */
@Initializable
public class Awusah extends DialoguePlugin {
    //TODO: Add dialogue for after the quest Spirits of Elid
    public Awusah(){
        /**
         * Empty
         */
    }
    public Awusah(Player player){
        super(player);
    }

    @Override
    public DialoguePlugin newInstance(Player player){return new Awusah(player);}

    @Override
    public boolean open(Object... args){
        interpreter.sendDialogue("The mayor doesn't seem interested in talking to you right now.");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId){
        switch(stage){
            case 0:
                end();
                break;
        }
        return true;
    }
    public int[] getIds() {return new int[] {3040};}
}
