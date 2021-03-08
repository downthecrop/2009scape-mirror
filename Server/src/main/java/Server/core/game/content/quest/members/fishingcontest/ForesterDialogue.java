package core.game.content.quest.members.fishingcontest;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.plugin.Initializable;
import core.game.content.dialogue.DialoguePlugin;


/**
 * Handles the SinisterStrangerDialogue dialogue.
 * @author Woah
 */
@Initializable
public class ForesterDialogue extends DialoguePlugin {

    public ForesterDialogue() {
        //empty
    }

    public ForesterDialogue(Player player){super(player);}

    @Override
    public DialoguePlugin newInstance(Player player) {
        return new ForesterDialogue(player);
    }

    @Override
    public boolean open(Object... args) {
        npc = (NPC) args[0];
        npc("Yeah? What do you want?");
        return true;
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        return true;
    }

    @Override
    public int[] getIds() {
        return new int[] {231};
    }
}