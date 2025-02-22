package content.global.skill.smithing.smelting;

import static core.api.ContentAPIKt.*;

import core.api.Container;
import core.game.event.ResourceProducedEvent;
import core.game.container.impl.EquipmentContainer;
import core.tools.Log;
import org.rs09.consts.Items;
import core.game.world.map.Location;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;
import core.tools.StringUtils;
import org.rs09.consts.Sounds;
import content.data.Quests;

/**
 * Represents the pulse used to smelt.
 *
 * @author 'Vexia
 */
public class SmeltingPulse extends SkillPulse<Item> {

    /**
     * The ring of forging item.
     */
    private static final Item RING_OF_FORGING = new Item(2568);

    /**
     * Represents the bar to make.
     */
    private final Bar bar;

    /**
     * Represents if using the super heat spell.
     */
    private final boolean superHeat;

    /**
     * The ticks passed.
     */
    private int ticks;

    /**
     * Represents the amount to produce.
     */
    private int amount;

    /**
     * Constructs a new {@code SmeltingPulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     * @param bar    the bar.
     * @param amount the amount.
     */
    public SmeltingPulse(Player player, Item node, Bar bar, int amount) {
        super(player, node);
        this.bar = bar;
        this.amount = amount;
        this.superHeat = false;
    }

    /**
     * Constructs a new {@code SmeltingPulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     * @param bar    the bar.
     * @param amount the amount.
     * @param heat   the heat.
     */
    public SmeltingPulse(Player player, Item node, Bar bar, int amount, boolean heat) {
        super(player, node);
        this.bar = bar;
        this.amount = amount;
        this.superHeat = heat;
        this.resetAnimation = false;
    }

    @Override
    public boolean checkRequirements() {
        player.getInterfaceManager().closeChatbox();
        if (bar == null || player == null) {
            return false;
        }
        if (bar == Bar.BLURITE && !player.getQuestRepository().isComplete(Quests.THE_KNIGHTS_SWORD)) {
            return false;
        }
        if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevel()) {
            player.getPacketDispatch().sendMessage("You need a Smithing level of at least " + bar.getLevel() + " in order to smelt " + bar.getProduct().getName().toLowerCase().replace("bar", "") + ".");
            player.getInterfaceManager().closeChatbox();
            return false;
        }
        for (Item item : bar.getOres()) {
            if (!player.getInventory().contains(item.getId(), item.getAmount())) {
                player.getPacketDispatch().sendMessage("You do not have the required ores to make this bar.");
                return false;
            }
        }
        return true;
    }

    @Override
    public void animate() {
        if (ticks == 0 || ticks % 5 == 0) {
            if (superHeat) {
                player.visualize(Animation.create(725), new Graphics(148, 96));
            } else {
                player.animate(Animation.create(3243)); // Used to be 899 but that looked wonky and broken
                playAudio(player, Sounds.FURNACE_2725);
            }
        }
    }

    @Override
    public boolean reward() {
        if (!superHeat && ++ticks % 5 != 0) {
            return false;
        }
        if (!superHeat) {
            player.getPacketDispatch().sendMessage("You place the required ores and attempt to create a bar of " + StringUtils.formatDisplayName(bar.toString().toLowerCase()) + ".");
        }
        for (Item i : bar.getOres()) {
            if (!player.getInventory().remove(i)) {
                return true;
            }
        }
        if (success(player)) {
            // Varrock Armour secondary reward
            int amt = (player.getInventory().freeSlots() != 0 && !superHeat
                    && player.getLocation().withinDistance(Location.create(3107, 3500, 0)) // edgeville furnace
                    && player.getInventory().containsItems(bar.getOres())
                    && player.getAchievementDiaryManager().getDiary(DiaryType.VARROCK).getLevel() != -1
                    && player.getAchievementDiaryManager().checkSmithReward(bar)
                    && RandomFunction.random(100) <= 10) ? 2 : 1;
            if (amt != 1) {
                if (!player.getInventory().remove(bar.getOres())) {
                    amt = 1;
                } else {
                    player.sendMessage("The magic of the Varrock armour enables you to smelt 2 bars at the same time.");
                }
            }
            player.getInventory().add(new Item(bar.getProduct().getId(), amt));
            player.dispatch(new ResourceProducedEvent(bar.getProduct().getId(), 1, player, -1));
            double xp = bar.getExperience() * amt;
            // Goldsmith gauntlets
            if (((player.getEquipment().get(EquipmentContainer.SLOT_HANDS) != null
                    && player.getEquipment().get(EquipmentContainer.SLOT_HANDS).getId() == Items.GOLDSMITH_GAUNTLETS_776))
                    && bar.getProduct().getId() == 2357) {
                xp = 56.2 * amt;
            }
            player.getSkills().addExperience(Skills.SMITHING, xp, true);
            if (!superHeat) {
                player.getPacketDispatch().sendMessage("You retrieve a bar of " + bar.getProduct().getName().toLowerCase().replace(" bar", "") + ".");
            }

            // Smelt a steel bar in the Lumbridge furnace
            if (bar == Bar.STEEL && player.getLocation().withinDistance(Location.create(3226, 3254, 0))) {
                player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 1, 5);
            }
            // Smelt a silver bar in the Lumbridge furnace
            if (bar == Bar.SILVER && player.getLocation().withinDistance(Location.create(3226, 3254, 0))) {
                player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 2, 7);
            }

        } else {
            player.getPacketDispatch().sendMessage("The ore is too impure and you fail to refine it.");
        }
        amount--;
        return amount < 1;
    }

    /**
     * Checks if the forging is a succes.
     *
     * @param player the player.
     * @return {@code True} if success.
     */
    public boolean success(Player player) {
        if (bar == Bar.IRON && !superHeat) {
            if (inEquipment(player, Items.RING_OF_FORGING_2568, 1)) {
                int charges = getAttribute(player, "ringOfForgingCharges", 140) - 1;
                if (charges <= 0) {
                    if (removeItem(player, Items.RING_OF_FORGING_2568, Container.EQUIPMENT)) {
                        charges = 140;
                        sendMessage(player, "Your ring of forging uses up its last charge and disintegrates.");
                    } else {
                        log(this.getClass(), Log.ERR, "Failed to delete empty ring of forging for player " + player.getName());
                        return false; //unfair but prevents exploit if the impossible happens
                    }
                }
                setAttribute(player, "/save:ringOfForgingCharges", charges);
                return true;
            } else {
                return RandomFunction.nextBool();
            }
        }
        return true;
    }

}
