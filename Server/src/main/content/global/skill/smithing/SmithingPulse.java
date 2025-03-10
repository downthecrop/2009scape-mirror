package content.global.skill.smithing;

import content.global.skill.skillcapeperks.SkillcapePerks;
import core.game.event.ResourceProducedEvent;
import core.cache.def.impl.ItemDefinition;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.tools.StringUtils;

import static core.api.ContentAPIKt.hasRequirement;
import static core.api.ContentAPIKt.sendDialogue;
import content.data.Quests;

/**
 * Represents the pulse used to smith a bar.
 *
 * @author 'Vexia
 */
public class SmithingPulse extends SkillPulse<Item> {

    /**
     * Represents the animation to use.
     */
    private static final Animation ANIMATION = new Animation(898);

    /**
     * Represents the bar being made.
     */
    private final Bars bar;

    /**
     * Represents the amount to make.
     */
    private int amount;

    /**
     * Constructs a new {@code SmithingPulse} {@code Object}.
     *
     * @param player the player.
     * @param item   the item.
     */
    public SmithingPulse(Player player, Item item, Bars bar, int amount) {
        super(player, item);
        this.bar = bar;
        this.amount = amount;
    }

    @Override
    public boolean checkRequirements() {
        if (!player.getInventory().contains(bar.getBarType().getBarType(), bar.getSmithingType().getRequired() * amount)) {
            amount = player.getInventory().getAmount(new Item(bar.getBarType().getBarType()));
        }
        player.getInterfaceManager().close();
        if (player.getSkills().getLevel(Skills.SMITHING) < bar.getLevel()) {
            player.getDialogueInterpreter().sendDialogue("You need a Smithing level of " + bar.getLevel() + " to make a " + ItemDefinition.forId(bar.getProduct()).getName() + ".");
            return false;
        }
        if (!player.getInventory().contains(bar.getBarType().getBarType(), bar.getSmithingType().getRequired())) {
            player.getDialogueInterpreter().sendDialogue("You don't have enough " + ItemDefinition.forId(bar.getBarType().getBarType()).getName().toLowerCase() + "s to make a " + bar.getSmithingType().name().replace("TYPE_", "").replace("_", " ").toLowerCase() + ".");
            return false;
        }
        if (!player.getInventory().contains(2347, 1) && !SkillcapePerks.isActive(SkillcapePerks.BAREFISTED_SMITHING,player)) {
            player.getDialogueInterpreter().sendDialogue("You need a hammer to work the metal with.");
            return false;
        }
        if (!player.getQuestRepository().isComplete(Quests.THE_TOURIST_TRAP) && bar.getSmithingType() == SmithingType.TYPE_DART_TIP) {
            player.getDialogueInterpreter().sendDialogue("You need to complete Tourist Trap to smith dart tips.");
            return false;
        }
        if (!hasRequirement(player, Quests.DEATH_PLATEAU, false) && bar.getSmithingType() == SmithingType.TYPE_CLAWS) {
            sendDialogue(player, "You need to complete Death Plateau to smith claws.");
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
        if(SkillcapePerks.isActive(SkillcapePerks.BAREFISTED_SMITHING,player)){
            player.animate(new Animation(2068)); //Torag's Hammer animation lol
            return;
        }
        player.animate(ANIMATION);
    }

    @Override
    public boolean reward() {
        if (getDelay() == 1) {
            setDelay(4);
            return false;
        }
        player.getInventory().remove(new Item(bar.getBarType().getBarType(), bar.getSmithingType().getRequired()));
        final Item item = new Item(node.getId(), bar.getSmithingType().getProductAmount());
        player.getInventory().add(item);
        player.dispatch(new ResourceProducedEvent(item.getId(), 1, player, bar.getBarType().getBarType()));
        player.getSkills().addExperience(Skills.SMITHING, bar.getBarType().getExperience() * bar.getSmithingType().getRequired(), true);
        String message = StringUtils.isPlusN(ItemDefinition.forId(bar.getProduct()).getName().toLowerCase()) ? "an" : "a";
        player.getPacketDispatch().sendMessage("You hammer the " + bar.getBarType().getBarName().toLowerCase().replace("smithing", "") + "and make " + message + " " + ItemDefinition.forId(bar.getProduct()).getName().toLowerCase() + ".");

        if (bar == Bars.BLURITE_CROSSBOW_LIMBS
                && player.getLocation().withinDistance(new Location(3000, 3145, 0), 10)) { // near Thurgo's anvil
            player.getAchievementDiaryManager().finishTask(player, DiaryType.FALADOR, 1, 9);
        }

        // Smith a steel longsword on the anvil in the jailhouse<br><br>sewers
        if (bar == Bars.STEEL_LONGSWORD && player.getLocation().withinDistance(Location.create(3112, 9688, 0))) {
            player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 2, 0);
        }

        // Smith an adamantite medium helm on the south-east anvil in<br><br>Varrock, next to Aubury's Rune Shop
        if (bar == Bars.ADAMANT_MEDIUM_HELM && player.getLocation().withinDistance(Location.create(3247, 3404, 0))) {
            player.getAchievementDiaryManager().finishTask(player, DiaryType.VARROCK, 2, 3);
        }

        amount--;
        return amount < 1;
    }

    @Override
    public void message(int type) {

    }

}
