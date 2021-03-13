package core.game.node.entity.combat.spell;

import core.game.node.Node;
import rs09.game.system.SystemLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpellBlocks {
    private static HashMap<Integer, List<Node>> blocks = new HashMap<>();

    public static void register(int spellId, Node toBlock){
        if(blocks.get(spellId) != null){
            blocks.get(spellId).add(toBlock);
        } else {
            List<Node> blockslist = new ArrayList<>(20);
            blockslist.add(toBlock);
            blocks.put(spellId,blockslist);
        }
    }

    public static boolean isBlocked(int spellId,Node node){
        AtomicBoolean blocked = new AtomicBoolean(false);
        if(blocks.get(spellId) == null){
            return false;
        }
        blocks.get(spellId).forEach(n -> {
            if(node.getName().equals(n.getName())){
                blocked.set(true);
            }
        });
        return blocked.get();
    }
}
