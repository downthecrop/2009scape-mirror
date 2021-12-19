package rs09.game.ai.lumbridge;

import rs09.game.world.World;
import core.tools.RandomFunction;

import java.util.concurrent.Executors;

/**
 * Creates a few random bots around Lumbridge area.
 * Code by Red Bracket
 */
public class LumbridgeBotHandler {
    public static void immersiveLumbridge()
    {
        //Generate a few random bots here and there
        System.out.println("[" + World.getSettings().getName() + "]: LumbridgeBotHandler: Initialized dead idlers.");
        generateDeadIdlers();
    }

    private static void generateDeadIdlers() {
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true) //Would probably be better if this could be "while game is running"
            {
                new DeadIdler();
                try {
                    Thread.sleep(RandomFunction.random(300_000));
                } catch (InterruptedException e) {
                    System.out.println("LumbridgeBotHandler can't sleep!!?");
                }
            }
        });
    }
}
