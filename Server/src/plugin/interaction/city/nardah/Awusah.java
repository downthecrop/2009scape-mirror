package plugin.interaction.city.nardah;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;

/**
 * Handles Awusah pre-quest
 * @author ceik
 */
@InitializablePlugin
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
