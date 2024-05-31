package content.global.skill.runecrafting;

import content.global.handlers.item.equipment.fistofguthixgloves.FOGGlovesManager;
import core.ServerConstants;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.tools.RandomFunction;

import static core.api.ContentAPIKt.*;
import static core.game.system.command.sets.StatAttributeKeysKt.STATS_BASE;
import static core.game.system.command.sets.StatAttributeKeysKt.STATS_RC;

import core.game.world.GameWorld;
import org.rs09.consts.Items;
import org.rs09.consts.Sounds;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A class used to craft runes.
 *
 * @author Vexia
 */
public final class RuneCraftPulse extends SkillPulse<Item> {

    /**
     * Represent the rune essence item.
     */
    private static final Item RUNE_ESSENCE = new Item(1436);

    /**
     * Represents the pure essence item.
     */
    private static final Item PURE_ESSENCE = new Item(7936);

    /**
     * Represents the binding necklace item.
     */
    private static final Item BINDING_NECKLACE = new Item(5521);

    /**
     * Represents the animation used for this pulse.
     */
    private static final Animation ANIMATION = new Animation(791, Priority.HIGH);

    /**
     * Represents the graphics used for this pulse.
     */
    private static final Graphics GRAPHICS = new Graphics(186, 100);

    /**
     * Represents the altar.
     */
    private final Altar altar;

    /**
     * Represents the rune we're crafting.
     */
    private final Rune rune;

    /**
     * Represents the combination rune(if any)
     */
    private final CombinationRune combo;

    /**
     * Represents if it's a combination pulse.
     */
    private final boolean combination;

    /**
     * Represents the talisman to remove.
     */
    private Talisman talisman;

    /**
     * Constructs a new {@code RuneCraftPulse} {@code Object}.
     *
     * @param player the player.
     * @param node   the node.
     * @param altar  the altar.
     */
    public RuneCraftPulse(Player player, Item node, final Altar altar, final boolean combination, final CombinationRune combo) {
        super(player, node);
        this.altar = altar;
        this.rune = altar.getRune();
        this.combination = combination;
        this.combo = combo;
        this.resetAnimation = false;
    }

    @Override
    public boolean checkRequirements() {
        if (altar == Altar.ASTRAL) {
            if (!hasRequirement(player, "Lunar Diplomacy"))
                return false;
        }
        if (altar == Altar.DEATH) {
            if (!hasRequirement(player, "Mourning's End Part II"))
                return false;
        }
        if (altar == Altar.BLOOD) {
            if (!hasRequirement(player, "Legacy of Seergaze"))
                return false;
        }
        if (!altar.isOurania() && player.getSkills().getLevel(Skills.RUNECRAFTING) < rune.getLevel()) {
            player.getPacketDispatch().sendMessage("You need a Runecrafting level of at least " + rune.getLevel() + " to craft this rune.");
            return false;
        }
        if (combination && !player.getInventory().containsItem(PURE_ESSENCE)) {
            player.getPacketDispatch().sendMessage("You need pure essence to craft this rune.");
            return false;
        }
        if (!altar.isOurania() && !rune.isNormal() && !player.getInventory().containsItem(PURE_ESSENCE)) {
            player.getPacketDispatch().sendMessage("You need pure essence to craft this rune.");
            return false;
        }
        if (!altar.isOurania() && rune.isNormal() && !player.getInventory().containsItem(PURE_ESSENCE) && !player.getInventory().containsItem(RUNE_ESSENCE)) {
            player.getPacketDispatch().sendMessage("You need rune essence or pure essence in order to craft this rune.");
            return false;
        }
        if (altar.isOurania() && !player.getInventory().containsItem(PURE_ESSENCE)) {
            player.getPacketDispatch().sendMessage("You need pure essence to craft this rune.");
            return false;
        }
        if (combination && player.getSkills().getLevel(Skills.RUNECRAFTING) < combo.getLevel()) {
            player.getPacketDispatch().sendMessage("You need a Runecrafting level of at least " + combo.getLevel() + " to combine this rune.");
            return false;
        }
        if (node != null) {
            if (node.getName().contains("rune") && !hasSpellImbue()) {
                final Rune r = Rune.forItem(node);
                final Talisman t = Talisman.forName(r.name());
                if (!player.getInventory().containsItem(t.getTalisman())) {
                    player.getPacketDispatch().sendMessage("You don't have the correct talisman to combine this rune.");
                    return false;
                }
                talisman = t;
            }
        }
        player.lock(4);
        return true;
    }

    @Override
    public void animate() {
        player.animate(ANIMATION);
        player.graphics(GRAPHICS);
        playAudio(player, Sounds.BIND_RUNES_2710);
    }

    @Override
    public boolean reward() {
        if (!combination) {
            craft();
        } else {
            combine();
        }
        return true;
    }

    /**
     * Method used to craft runes.
     */
    private void craft() {
        final Item item = new Item(getEssence().getId(), getEssenceAmount());
        int amount = player.getInventory().getAmount(item);
        if (!altar.isOurania()) {
            int total = 0;
            for(int j = 0; j < amount; j++) {
                // since getMultiplier is stochastic, roll `amount` independent copies
                total += getMultiplier();
            }
            Item i = new Item(rune.getRune().getId(), total);

            if (player.getInventory().remove(item) && player.getInventory().hasSpaceFor(i)) {
                player.getPacketDispatch().sendMessage("You bind the temple's power into " + (combination ? combo.getRune().getName().toLowerCase() : rune.getRune().getName().toLowerCase()) + "s.");
                player.getInventory().add(i);
                player.incrementAttribute("/save:" + STATS_BASE + ":" + STATS_RC, amount);
                
                // Fist of guthix gloves
                double xp = rune.getExperience() * amount;
                if ((altar == Altar.AIR && inEquipment(player, Items.AIR_RUNECRAFTING_GLOVES_12863, 1))
                        || (altar == Altar.WATER && inEquipment(player, Items.WATER_RUNECRAFTING_GLOVES_12864, 1))
                        || (altar == Altar.EARTH && inEquipment(player, Items.EARTH_RUNECRAFTING_GLOVES_12865, 1))) {
                    xp += xp * FOGGlovesManager.updateCharges(player, amount) / amount;
                }
                player.getSkills().addExperience(Skills.RUNECRAFTING, xp, true);

                // Achievement Diary handling
                // Craft some nature runes
                if (altar == Altar.NATURE) {
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.KARAMJA, 2, 3);
                }
                // Craft 196 or more air runes simultaneously
                if (altar == Altar.AIR && i.getAmount() >= 196) {
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.FALADOR, 2, 2);
                }
                // Craft a water rune at the Water Altar
                if (altar == Altar.WATER && rune == Rune.WATER) {
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 1, 11);
                }

            }
        } else {
            if (player.getInventory().remove(item)) {
                player.getPacketDispatch().sendMessage("You bind the temple's power into runes.");
                player.incrementAttribute("/save:" + STATS_BASE + ":" + STATS_RC, amount);
                for (int i = 0; i < amount; i++) {
                    Rune rune = null;
                    while (rune == null) {
                        final Rune temp = Rune.values()[RandomFunction.random(Rune.values().length)];
                        if (player.getSkills().getLevel(Skills.RUNECRAFTING) >= temp.getLevel()) {
                            rune = temp;
                        } else {
                            if (RandomFunction.random(3) == 1) {
                                rune = temp;
                            }
                        }
                    }
                    player.getSkills().addExperience(Skills.RUNECRAFTING, rune.getExperience() * 2, true);
                    Item runeItem = rune.getRune();
                    player.getInventory().add(runeItem);
                }
            }
        }
    }

    /**
     * Method used to combine runes.
     */
    private final void combine() {
        final Item remove = node.getName().contains("talisman") ? node : talisman != null ? talisman.getTalisman() : Talisman.forName(Rune.forItem(node).name()).getTalisman();
        boolean imbued = hasSpellImbue();
        if (!imbued ? player.getInventory().remove(remove) : imbued) {
            int amount = 0;
            int essenceAmt = player.getInventory().getAmount(PURE_ESSENCE);
            final Item rune = node.getName().contains("rune") ? Rune.forItem(node).getRune() : Rune.forName(Talisman.forItem(node).name()).getRune();
            int runeAmt = player.getInventory().getAmount(rune);
            if (essenceAmt > runeAmt) {
                amount = runeAmt;
            } else {
                amount = essenceAmt;
            }
            if (player.getInventory().remove(new Item(PURE_ESSENCE.getId(), amount)) && player.getInventory().remove(new Item(rune.getId(), amount))) {
                for (int i = 0; i < amount; i++) {
                    if (RandomFunction.random(1, 3) == 1 || hasBindingNecklace()) {
                        player.getInventory().add(new Item(combo.getRune().getId(), 1));
                        player.getSkills().addExperience(Skills.RUNECRAFTING, combo.getExperience(), true);
                    }
                }
                if (hasBindingNecklace()) {
                    player.getEquipment().get(EquipmentContainer.SLOT_AMULET).setCharge(player.getEquipment().get(EquipmentContainer.SLOT_AMULET).getCharge() - 1);
                    if (1000 - player.getEquipment().get(EquipmentContainer.SLOT_AMULET).getCharge() > 14) {
                        player.getEquipment().remove(BINDING_NECKLACE, true);
                        player.getPacketDispatch().sendMessage("Your binding necklace crumbles into dust.");
                    }
                }
            }
        }
    }

    /**
     * Checks if the player has the spell imbue.
     *
     * @return {@code True} if so.
     */
    private boolean hasSpellImbue() {
        return player.getAttribute("spell:imbue", 0) > GameWorld.getTicks();
    }

    /**
     * Gets the essence amount.
     *
     * @return the amount of essence.
     */
    private int getEssenceAmount() {
        if (altar.isOurania() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return player.getInventory().getAmount(PURE_ESSENCE);
        }
        if (!rune.isNormal() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return player.getInventory().getAmount(PURE_ESSENCE);
        } else if (rune.isNormal() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return player.getInventory().getAmount(PURE_ESSENCE);
        } else {
            return player.getInventory().getAmount(RUNE_ESSENCE);
        }
    }

    /**
     * Gets the rune essence that needs to be defined.
     *
     * @return the item.
     */
    private Item getEssence() {
        if (altar.isOurania() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return PURE_ESSENCE;
        }
        if (!rune.isNormal() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return PURE_ESSENCE;
        } else if (rune.isNormal() && player.getInventory().containsItem(PURE_ESSENCE)) {
            return PURE_ESSENCE;
        } else {
            return RUNE_ESSENCE;
        }
    }

    /**
     * Gets the multiplied amount of runes to make.
     *
     * @return the amount.
     */
    public int getMultiplier() {
        if (altar.isOurania()) {
            return 1;
        }
        int rcLevel = player.getSkills().getLevel(Skills.RUNECRAFTING);
        int runecraftingFormulaRevision = ServerConstants.RUNECRAFTING_FORMULA_REVISION;
        boolean lumbridgeDiary = player.getAchievementDiaryManager().getDiary(DiaryType.LUMBRIDGE).isComplete(1);
        return RuneCraftPulse.getMultiplier(rcLevel, rune, runecraftingFormulaRevision, lumbridgeDiary);
    }

    public static int getMultiplier(int rcLevel, Rune rune, int runecraftingFormulaRevision, boolean lumbridgeDiary) {
        int[] multipleLevels = rune.getMultiple();
        int i = 0;
        for (int level : multipleLevels) {
            if (rcLevel >= level) {
                i++;
            }
        }

        if(multipleLevels.length > i && runecraftingFormulaRevision >= 573) {
            int a = Math.max(multipleLevels[i-1], rune.getLevel());
            int b = multipleLevels[i];
            if(b <= 99 || runecraftingFormulaRevision >= 581) {
                double chance = ((double)rcLevel - (double)a) / ((double)b - (double)a);
                if(RandomFunction.random(0.0, 1.0) < chance) {
                    i += 1;
                }
            }
        }

        if (lumbridgeDiary
                && new ArrayList<>(Arrays.asList(Rune.AIR, Rune.WATER, Rune.FIRE, Rune.EARTH)).contains(rune)
                && RandomFunction.getRandom(10) == 0) { //approximately 10% chance
            i += 1;
        }

        return i != 0 ? i : 1;
    }

    /**
     * Method used to check if the player has a binding necklace.
     *
     * @return <code>True</code> if so.
     */
    public boolean hasBindingNecklace() {
        return player.getEquipment().containsItem(BINDING_NECKLACE);
    }

    /**
     * Gets the altar.
     *
     * @return The altar.
     */
    public Altar getAltar() {
        return altar;
    }

}
