package core.game.node.entity.skill.fishing;

import core.game.content.global.SkillingPets;
import core.game.content.quest.tutorials.tutorialisland.TutorialSession;
import core.game.content.quest.tutorials.tutorialisland.TutorialStage;
import core.game.node.entity.skill.SkillPulse;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.summoning.familiar.Forager;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.player.link.skillertasks.SkillTasks;
import core.game.node.item.Item;
import core.game.system.task.Pulse;
import core.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.path.Pathfinder;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import core.game.node.entity.skill.skillcapeperks.SkillcapePerks;

import static core.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_BASE;
import static core.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_FISH;
import static core.tools.stringtools.StringToolsKt.colorize;

/**
 * Handles a fishing pulse.
 *
 * @author Ceikry
 */
public final class FishingPulse extends SkillPulse<NPC> {

    /**
     * Represents the fishing option.
     */
    private final FishingOption option;

    /**
     * Represents the fish type.
     */
    private Fish fish;

    /**
     * Represents the base location the npc was at.
     */
    private final Location location;

    /**
     * Constructs a new {@code FishingPulse} {@code Object}.
     *
     * @param player the player.
     * @param npc    the fishing spot NPC.
     * @param option The fishing option.
     */
    public FishingPulse(final Player player, final NPC npc, final FishingOption option) {
        super(player, npc);
        this.option = option;
        this.location = npc.getLocation();
        if (option != null) {
            this.fish = option.getRandomFish(player);
        }
    }

    @Override
    public void start() {
        if (TutorialSession.getExtension(player).getStage() == 12) {
            TutorialStage.load(player, 13, false);
        }
        if (player.getFamiliarManager().hasFamiliar() && player.getFamiliarManager().getFamiliar() instanceof Forager) {
            final Forager forager = (Forager) player.getFamiliarManager().getFamiliar();
            Location dest = player.getLocation().transform(player.getDirection());
            Pathfinder.find(forager.getLocation(), dest).walk(forager);
        }
        super.start();
    }

    @Override
    public boolean checkRequirements() {
        if (option == null) {
            return false;
        }
        player.debug(String.valueOf(player.getInventory().containsItem(option.getTool())));
        if (!player.getInventory().containsItem(option.getTool()) && !hasBarbTail()) {
            //System.out.println(isBareHanded(player));
            player.getDialogueInterpreter().sendDialogue("You need a " + option.getTool().getName().toLowerCase() + " to catch these fish.");
            stop();
            return false;
        }
        if (option.getBait() != null && !player.getInventory().containsItem(option.getBait())) {
            player.getDialogueInterpreter().sendDialogue("You don't have any " + option.getBait().getName().toLowerCase() + "s left.");
            stop();
            return false;
        }
        if (player.getSkills().getLevel(Skills.FISHING) < fish.getLevel()) {
            player.getDialogueInterpreter().sendDialogue("You need a fishing level of " + fish.getLevel() + " to catch " + (fish == Fish.SHRIMP || fish == Fish.ANCHOVIE ? "" : "a") + " " + fish.getItem().getName().toLowerCase() + ".".trim());
            stop();
            return false;
        }
        if (player.getInventory().freeSlots() == 0) {
            player.getDialogueInterpreter().sendDialogue("You don't have enough space in your inventory.");
            stop();
            return false;
        }
        if (location != node.getLocation() || !node.isActive() || node.isInvisible()) {
            stop();
            return false;
        }
        return true;
    }

    @Override
    public void animate() {
        if (isBareHanded(player)) {
            player.animate(new Animation(6709));
            GameWorld.getPulser().submit(new Pulse(1) {
                int counter = 0;

                @Override
                public boolean pulse() {
                    switch (counter++) {
                        case 5:
                            getCatchAnimationAndLoot(player);
                            break;
                    }
                    return false;
                }
            });
        } else {
            player.animate(option.getAnimation());
        }
    }

    @Override
    public boolean reward() {
        if (getDelay() == 1) {
            super.setDelay(5);
            return false;
        }
        if (player.getFamiliarManager().hasFamiliar() && player.getFamiliarManager().getFamiliar() instanceof Forager) {
            final Forager forager = (Forager) player.getFamiliarManager().getFamiliar();
            forager.handlePassiveAction();
        }
        if (success()) {
            if ((player.getInventory().hasSpaceFor(fish.getItem()) && option.getBait() != null) ? player.getInventory().remove(option.getBait()) : true) {

                if (player.getSkillTasks().hasTask()) {
                    updateSkillTask();
                }
                updateDiary();

                SkillingPets.checkPetDrop(player, SkillingPets.HERON);
                final Item item = fish.getItem();
                if(SkillcapePerks.isActive(SkillcapePerks.GREAT_AIM,player) && RandomFunction.random(100) <= 5){
                    player.getInventory().add(item);
                    player.sendMessage(colorize("%RYour expert aim catches you a second fish."));
                }
                player.getInventory().add(item);
                int fishCaught = player.getAttribute(STATS_BASE + ":" + STATS_FISH,0);
                player.setAttribute("/save:" + STATS_BASE + ":" + STATS_FISH,++fishCaught);
                player.getSkills().addExperience(Skills.FISHING, fish.getExperience(), true);
                message(2);
                if (TutorialSession.getExtension(player).getStage() == 13) {
                    TutorialStage.load(player, 14, false);
                    stop();
                    return true;
                }
                fish = option.getRandomFish(player);
            }
        }
        return player.getInventory().freeSlots() == 0;
    }

    public void updateDiary() {
        switch (fish) {
            case MACKEREL:
                if (player.getViewport().getRegion().getId() == 11317) {
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 0, 11);
                }
            case BASS:
                if (player.getViewport().getRegion().getId() == 11317
                        && !player.getAchievementDiaryManager().hasCompletedTask(DiaryType.SEERS_VILLAGE, 1, 11)) {
                    player.setAttribute("/save:diary:seers:bass-caught", true);
                }
            case SHARK:
                if (player.getViewport().getRegion().getId() == 11317
                        && !player.getAchievementDiaryManager().hasCompletedTask(DiaryType.SEERS_VILLAGE, 2, 7)) {
                    player.setAttribute("/save:diary:seers:caught-shark", 1 + player.getAttribute("diary:seers:caught-shark", 0));
                    if (player.getAttribute("diary:seers:caught-shark", 0) >= 5) {
                        player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 2, 7);
                    }
                }
            case SHRIMP:
            	// Catch some shrimp in the Fishing spot to the east of<br><br>Lumbridge Swamp
                if (player.getLocation().withinDistance(Location.create(3241, 3149, 0))) {
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 0, 13);
                }
			case PIKE:
				// Catch a pike in the river to the east of Lumbridge Castle
				if (player.getLocation().withinDistance(Location.create(3240, 3247, 0))) {
					player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 1, 4);
				}
			case SALMON:
				// Catch a salmon in the river to the east of Lumbridge Castle
				if (player.getLocation().withinDistance(Location.create(3240, 3247, 0))) {
					player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 2, 9);
				}
            case TROUT:
                // Catch a trout in the river to the east of Barbarian Village
                if (player.getLocation().withinDistance(Location.create(3105, 3431, 0))) {
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.VARROCK, 0, 16);
                }
        }
        // Use the Fishing spots north of the banana plantation
        if (node.getId() == 333 && player.getZoneMonitor().isInZone("karamja")
                && player.getLocation().withinDistance(new Location(2924, 3178, 0), 10)) {
            player.getAchievementDiaryManager().finishTask(player, DiaryType.KARAMJA, 0, 6);
        }
    }

    public void updateSkillTask() {
        switch (fish) {
            case ANCHOVIE:
                player.getSkillTasks().decreaseTask(player, SkillTasks.FANCHOVIES1);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FANCHOVIES2);
                break;
            case HERRING:
                player.getSkillTasks().decreaseTask(player, SkillTasks.FHERRING1);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FHERRING2);
                break;
            case LOBSTER:
                player.getSkillTasks().decreaseTask(player, SkillTasks.FLOBSTER1);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FLOBSTER2);
                break;
            case SALMON:
                player.getSkillTasks().decreaseTask(player, SkillTasks.FSALMON1);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FSALMON2);
                break;
            case SHARK:
                player.getSkillTasks().decreaseTask(player, SkillTasks.FSHARK1);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FSHARK2);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FSHARK3);
                break;
            case SHRIMP:
                player.getSkillTasks().decreaseTask(player, SkillTasks.FSHRIMP1);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FSHRIMP2);
                break;
            case SWORDFISH:
                player.getSkillTasks().decreaseTask(player, SkillTasks.FSWORD1);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FSWORD2);
                break;
            case TROUT:
                player.getSkillTasks().decreaseTask(player, SkillTasks.FTROUT1);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FTROUT2);
                break;
            case TUNA:
                player.getSkillTasks().decreaseTask(player, SkillTasks.FTUNA1);
                player.getSkillTasks().decreaseTask(player, SkillTasks.FTUNA2);
                break;
        }
    }

    private boolean isBareHanded(Player p) {
        if (option == FishingOption.HARPOON) {
            if (checkFish(p) > 0 && !(player.getInventory().containsItem(option.getTool()) || player.getEquipment().containsItem(option.getTool()))) {
                return true;
            }
            if (checkFish(p) > 2 && !(player.getInventory().containsItem(option.getTool()) || player.getEquipment().containsItem(option.getTool()))) {
                return true;
            }
        }
        return false;
    }

    private int getCatchAnimationAndLoot(Player p) {
        int fishingFor = checkFish(p);
        switch (node.getId()) {
            case 324:
                switch (fishingFor) {
                    case 1:
                        p.animate(new Animation(6710));
                        p.getSkills().addExperience(Skills.FISHING, 80);
                        p.getSkills().addExperience(Skills.STRENGTH, 8);
                        p.getInventory().add(new Item(359));
                        break;
                    case 2:
                    case 3:
                        if (RandomFunction.random(1) == 1) {
                            p.animate(new Animation(6710));
                            p.getSkills().addExperience(Skills.FISHING, 80);
                            p.getSkills().addExperience(Skills.STRENGTH, 8);
                            p.getInventory().add(new Item(359));
                        } else {
                            p.animate(new Animation(6707));
                            p.getSkills().addExperience(Skills.FISHING, 100);
                            p.getSkills().addExperience(Skills.STRENGTH, 10);
                            p.getInventory().add(new Item(371));
                        }
                }
                break;
            case 313:
                p.animate(new Animation(6705));
                p.getSkills().addExperience(Skills.FISHING, 110);
                p.getSkills().addExperience(Skills.STRENGTH, 11);
                p.getInventory().add(new Item(383));
                break;
        }
        return 0;
    }

    public static int checkFish(Player p) {
        if (p.getSkills().getLevel(Skills.FISHING) >= 55 && p.getSkills().getLevel(Skills.STRENGTH) >= 35) {
            if (p.getSkills().getLevel(Skills.FISHING) >= 70 && p.getSkills().getLevel(Skills.STRENGTH) >= 50) {
                if (p.getSkills().getLevel(Skills.FISHING) >= 96 && p.getSkills().getLevel(Skills.STRENGTH) >= 76) {
                    return 3;
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }

    /**
     * Checks if they have the barb tail harpoon.
     *
     * @return {@code True} if so.
     */
    private boolean hasBarbTail() {
        if (option == FishingOption.HARPOON) {
            if (player.getInventory().containsItem(FishingOption.BARB_HARPOON.getTool()) || player.getEquipment().containsItem(FishingOption.BARB_HARPOON.getTool())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void message(int type) {
        switch (type) {
            case 0:
                player.getPacketDispatch().sendMessage(option.getStartMessage());
                break;
            case 2:
                player.getPacketDispatch().sendMessage(fish == Fish.ANCHOVIE || fish == Fish.SHRIMP ? "You catch some " + fish.getItem().getName().toLowerCase().replace("raw", "").trim() + "." : "You catch a " + fish.getItem().getName().toLowerCase().replace("raw", "").trim() + ".");
                if (player.getInventory().freeSlots() == 0) {
                    player.getDialogueInterpreter().sendDialogue("You don't have enough space in your inventory.");
                    stop();
                }
                break;
        }
    }

    /**
     * Method used to check if the catch was a success.
     *
     * @return <code>True</code> if so.
     */
    private boolean success() {
        if (getDelay() == 1) {
            return false;
        }
        int level = 1 + player.getSkills().getLevel(Skills.FISHING) + player.getFamiliarManager().getBoost(Skills.FISHING);
        double hostRatio = Math.random() * fish.getLevel();
        double clientRatio = Math.random() * ((level * 1.25 - fish.getLevel()));
        return hostRatio < clientRatio;
    }
}