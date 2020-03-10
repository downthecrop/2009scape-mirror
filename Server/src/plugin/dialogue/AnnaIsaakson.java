package plugin.dialogue;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.node.entity.player.Player;
import org.crandor.plugin.InitializablePlugin;

/**
 * Replacement for the Anna Isaakson .asc file
 * @author ceik
 */

@InitializablePlugin
public class AnnaIsaakson extends DialoguePlugin {
    public AnnaIsaakson(){
        /**
         * Empty on purpose
         */
    }
    public AnnaIsaakson(Player player){super(player);}

    @Override
    public DialoguePlugin newInstance(Player player){return new AnnaIsaakson(player);}

    @Override
    public boolean open(Object... args) {
        npc("Hello visitor, how are you?");
        stage = 1;
        return true;
    }

    @Override
    public final boolean handle(int interfaceId, int buttonId){
        switch(stage){
            case 1:
                player("Better than expected. Its a lot...nicer...here than I was", "expecting. Everyone seems pretty happy.");
                stage = 2;
                break;
            case 2:
                npc("Of course, the Burgher is strong and wise, and looks", "after us well");
                stage = 3;
                break;
            case 3:
                player("I think some of those Jatizso citizens have got", "the wrong idea about this place.");
                stage = 10;
                break;
            case 10:
                end();
                break;
        }
        return true;
    }
    @Override
    public int[] getIds() {
        return new int[] { 5512 };
    }
}
