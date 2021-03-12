package core.game.node.entity.npc.city.sophanem;

import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;
import core.game.node.entity.player.Player;

/**
 * Handles the Klenter NPC, which should be invisible until a quest stage has been reached. Will do that when quest is added.
 * @author ceik
 */
@Initializable
public class KlenterNPC extends DialoguePlugin {
    public KlenterNPC(){
        /**
         * Empty
         */
    }
    public KlenterNPC(Player player){super(player);}

    @Override
    public DialoguePlugin newInstance(Player player){return new KlenterNPC(player);}

    @Override
    public boolean open(Object... args){
        npc("OOOOoOOOoOO");
        return true;
    }

    @Override
    public boolean handle(int interfaceId,int buttonId){
        return true;
    }
    @Override
    public int[] getIds(){return new int[] {2014};}
}
