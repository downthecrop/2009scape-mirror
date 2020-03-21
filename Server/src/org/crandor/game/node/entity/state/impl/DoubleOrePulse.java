package org.crandor.game.node.entity.state.impl;

import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.state.StatePulse;
import org.crandor.game.world.GameWorld;

import java.nio.ByteBuffer;

/**
 * Pulse for checking remaining time on a shooting star mining bonus and informing the player
 * @author ceik
 */

public class DoubleOrePulse extends StatePulse {
    Player player;
    Entity entity;
    int ticks,currentTick;
    public DoubleOrePulse(Entity entity, int ticks, int currentTick){
        super(entity,1);
        this.ticks = ticks;
        this.currentTick = currentTick;
        this.player = entity.asPlayer();
        this.entity = entity;
    }
    @Override
    public boolean isSaveRequired(){return currentTick < ticks;}

    @Override
    public void save(ByteBuffer buffer){
        buffer.putInt(ticks);
        buffer.putInt(currentTick);
    }

    @Override
    public StatePulse parse(Entity entity, ByteBuffer buffer){
        return new DoubleOrePulse(entity, buffer.getInt(), buffer.getInt());
    }

    @Override
    public void start() {
        if (currentTick == GameWorld.getTicks()) {
            if (entity instanceof Player) {
                if((int) entity.asPlayer().getAttribute("SS Mining Bonus") > GameWorld.getTicks()){
                    entity.asPlayer().sendMessage("<col=006600>Your mining bonus is now active!</col>");
                }
            }
        }
        super.start();
    }

    @Override
    public boolean pulse() {
        int ticksLeft = (int)player.getAttribute("SS Mining Bonus") - GameWorld.getTicks();
        int minutes = (int)Math.ceil((ticksLeft * .6) / 60);
        if(ticksLeft % 500 == 0L){
            player.sendMessage("<col=f0f095>You have " + minutes + " minutes of your mining bonus left</col>");
        }
        if(ticksLeft == 0){
            if((int)ticks <= GameWorld.getTicks()){
                player.sendMessage("<col=FF0000>Your mining bonus has run out!</col>");
            }
        }
        return !(ticks >= GameWorld.getTicks());
    }
    @Override
    public StatePulse create(Entity entity, Object... args) {
        return new DoubleOrePulse(entity, entity.asPlayer().getAttribute("SS Mining Bonus"), GameWorld.getTicks());
    }
}
