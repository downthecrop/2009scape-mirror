package org.crandor.game.node.entity.player.ai.pvmbots;

import java.util.ArrayList;
import java.util.List;

import org.crandor.game.content.global.consumable.Consumable;
import org.crandor.game.content.global.consumable.ConsumableProperties;
import org.crandor.game.content.global.consumable.Consumables;
import org.crandor.game.content.global.consumable.Food;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.ai.AIPlayer;
import org.crandor.game.node.entity.player.link.prayer.PrayerType;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.update.flag.context.Animation;
import org.crandor.tools.RandomFunction;

public class PvMBots extends AIPlayer {

    //Used so the bot doesn't spam actions
    private int tick = 0;

    public PvMBots(String name, Location l) {
        super(name, l);
        // TODO Auto-generated constructor stub
    }


    public List<Entity> FindTargets(Entity entity, int radius) {
        List<Entity> targets = new ArrayList<>();
        for (NPC npc : RegionManager.getLocalNpcs(entity, radius)) {
            {
                if (checkValidTargets(npc))
                    targets.add(npc);
            }
        }
        if (targets.size() == 0)
            return null;
        return targets;
    }

    public boolean checkValidTargets(NPC target) {
        if (!target.isActive()) {
            return false;
        }
        if (!target.getProperties().isMultiZone() && target.inCombat()) {
            return false;
        }
        if (!target.getDefinition().hasAction("attack")) {
            return false;
        }
        return true;
    }

    public void AttackNpcsInRadius(Player bot, int radius) {
        if (bot.inCombat())
            return;
        List<Entity> creatures = FindTargets(bot, radius);
        if (creatures == null) {
            return;
        }
        if (!(creatures.isEmpty())) {
            bot.attack(creatures.get(RandomFunction.getRandom((creatures.size() - 1))));
            return;
        } else {
            creatures = FindTargets(bot, radius);
            if (!creatures.isEmpty())
                bot.attack(creatures.get(RandomFunction.getRandom((creatures.size() - 1))));
            return;
        }
    }

    @Override
    public void tick() {
        super.tick();

        //Despawn
        if (this.getSkills().getLifepoints() == 0)
            //this.teleport(new Location(500, 500));
            //Despawning not being delayed causes 3 errors in the console
            AIPlayer.deregister(this.getUid());

        //Npc Combat
        if (tick == 0) {
            if (!this.inCombat())
                AttackNpcsInRadius(this, 5);
            this.tick = 10;
        } else
            this.tick--;

        //this.eat();
        //this.getPrayer().toggle()
    }

    public void CheckPrayer(PrayerType type[]) {
        for (int i = 0; i < type.length; i++)
            this.getPrayer().toggle(type[i]);
    }

    public void eat(int foodId) {
        Item foodItem = new Item(foodId);

        if ((this.getSkills().getStaticLevel(Skills.HITPOINTS) >= this.getSkills().getLifepoints() * 3) && this.getInventory().containsItem(foodItem)) {
            this.lock(3);
            //this.animate(new Animation(829));
            Item food = this.getInventory().getItem(foodItem);

            Consumable consumable = Consumables.forFood(food);

            if (consumable == null) {
                consumable = new Food(food.getId(), new ConsumableProperties(1));
            }

            consumable.consume(food, this);
            this.getProperties().getCombatPulse().delayNextAttack(3);
        }
        if (this.checkVictimIsPlayer() == false)
            if (!(this.getInventory().contains(foodId, 1)))
                this.getInventory().add(new Item(foodId, 5)); //Add Food to Inventory
    }
}
