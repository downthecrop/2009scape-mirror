package rs09.game.ai.system;


import rs09.game.ai.AIPlayer;

import java.util.HashMap;
import java.util.Map;

public class GlobalAIManager {


    /**
     * The active Artificial intelligent players mapping.
     */
    private static final Map<Integer, AIPlayer> BOT_MAPPING = new HashMap<>();


    /**
     * Constructs a new {@code GlobalAIManager} {@code Object}.
     */
    public GlobalAIManager() {

    }

    public static void addBot(int uid, AIPlayer player) {
        BOT_MAPPING.put(uid, player);
    }

    public static AIPlayer getBot(int uid) {
        return BOT_MAPPING.get(uid);
    }
}
