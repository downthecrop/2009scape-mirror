package core.game.node.entity.skill.gather.woodcutting;

import api.events.ResourceProducedEvent;
import core.cache.def.impl.ItemDefinition;
import core.game.container.impl.EquipmentContainer;
import core.game.content.dialogue.FacialExpression;
import core.game.content.global.BirdNest;
import core.game.content.global.SkillingPets;
import core.game.node.entity.impl.Animator;
import core.game.node.entity.impl.Projectile;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.entity.skill.Skills;
import core.game.node.entity.skill.gather.SkillingTool;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import core.game.world.map.Location;
import core.game.world.update.flag.context.Animation;
import core.tools.RandomFunction;
import rs09.game.node.entity.skill.farming.FarmingPatch;
import rs09.game.node.entity.skill.farming.Patch;
import rs09.game.node.entity.skill.skillcapeperks.SkillcapePerks;

import static rs09.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_BASE;
import static rs09.game.node.entity.player.info.stats.StatAttributeKeysKt.STATS_LOGS;

/**
 * Woodcutting skill pulse
 *
 * @author ceik
 */
public class WoodcuttingSkillPulse extends Pulse {
    private WoodcuttingNode resource;
    private int ticks;
    private Player player;
    private Scenery node;
    protected boolean resetAnimation = true;


    public WoodcuttingSkillPulse(Player player, Scenery node) {
        super(1, player, node);
        this.player = player;
        this.node = node;
        super.stop();
    }

    public void message(int type) {
        if (type == 0) {
            player.getPacketDispatch().sendMessage("You swing your axe at the tree...");
        }
    }

    @Override
    public boolean pulse() {
        if (!checkRequirements()) {
            return true;
        }
        animate();
        return reward();
    }

    @Override
    public void stop() {
        if (resetAnimation) {
            player.animate(new Animation(-1, Animator.Priority.HIGH));
        }
        super.stop();
        message(1);
    }

    @Override
    public void start() {
        resource = WoodcuttingNode.forId(node.getId());
        if (resource == null) {
            return;
        }
        if (checkRequirements()) {
            super.start();
            message(0);
        }
    }

    public boolean checkRequirements() {
        if (player.getSkills().getLevel(Skills.WOODCUTTING) < resource.getLevel()) {
            player.getPacketDispatch().sendMessage("You need a woodcutting level of " + resource.getLevel() + " to chop this tree.");
            return false;
        }
        if (SkillingTool.getHatchet(player) == null) {
            player.getPacketDispatch().sendMessage("You do not have an axe to use.");
            return false;
        }
        if (player.getInventory().freeSlots() < 1) {
            player.getDialogueInterpreter().sendDialogue("Your inventory is too full to hold any more " + ItemDefinition.forId(resource.getReward()).getName().toLowerCase() + ".");
            return false;
        }
        return true;
    }

    public void animate() {
        if(!player.getAnimator().isAnimating()) player.animate(SkillingTool.getHatchet(player).getAnimation());
    }

    public boolean reward() {
        if (++ticks % 4 != 0) {
            return false;
        }
        if (node.getId() == 10041) {
            player.getDialogueInterpreter().sendDialogues(2574, FacialExpression.FURIOUS, RandomFunction.random(2) == 1 ? "You'll blow my cover! I'm meant to be hidden!" : "Will you stop that?");
            return true;
        }
        if (!checkReward(SkillingTool.getHatchet(player))) {
            return false;
        }


        // If player is in donator zone
       /* if (player.getLocation().getRegionId() == 12102) {
            player.getAntiMacroHandler().fireEvent("tree spirit");
            return true;
        }
*/
        // 20% chance to auto burn logs when using "inferno adze" item
        if (SkillingTool.getHatchet(player).getId() == 13661 && RandomFunction.random(100) < 25) {
            player.sendMessage("You chop some logs. The heat of the inferno adze incinerates them.");
            Projectile.create(player, null, 1776, 35, 30, 20, 25).transform(player, new Location(player.getLocation().getX() + 2, player.getLocation().getY()), true, 25, 25).send();
            player.getSkills().addExperience(Skills.WOODCUTTING, resource.getExperience());
            player.getSkills().addExperience(Skills.FIREMAKING, resource.getExperience());
            return false;
        }

        //actual reward calculations
        int reward = resource.getReward();
        int rewardAmount = 0;
        if (reward > 0) {
            reward = calculateReward(reward); // calculate rewards
            rewardAmount = calculateRewardAmount(reward); // calculate amount
            SkillingPets.checkPetDrop(player, SkillingPets.BEAVER); // roll for pet

            //add experience
            double experience = calculateExperience(resource.reward, rewardAmount);

            player.getSkills().addExperience(Skills.WOODCUTTING, experience, true);

            //send the message for the resource reward
            if (resource == WoodcuttingNode.DRAMEN_TREE) {
                player.getPacketDispatch().sendMessage("You cut a branch from the Dramen tree.");
            } else {
                player.getPacketDispatch().sendMessage("You get some " + ItemDefinition.forId(reward).getName().toLowerCase() + ".");
            }
            //give the reward
            player.getInventory().add(new Item(reward, rewardAmount));
            player.dispatch(new ResourceProducedEvent(reward, rewardAmount, node, -1));
            int cutLogs = player.getAttribute(STATS_BASE + ":" + STATS_LOGS,0);
            player.setAttribute("/save:" + STATS_BASE + ":" + STATS_LOGS,++cutLogs);

            //calculate bonus bird nest for mining
            int chance = 282;
            if (RandomFunction.random(chance) == chance / 2) {
                if(SkillcapePerks.isActive(SkillcapePerks.NEST_HUNTER,player)){
                    if(!player.getInventory().add(BirdNest.getRandomNest(false).getNest())){
                        BirdNest.drop(player);
                    }
                } else {
                    BirdNest.drop(player);
                }
            }

        }
        //transform to depleted version
        //OSRS and RS3 Wikis both agree: All trees present in 2009 are a 1/8 fell chance, aside from normal trees/dead trees which are 100%
        //OSRS: https://oldschool.runescape.wiki/w/Woodcutting scroll down to the mechanics section
        //RS3 : https://runescape.wiki/w/Woodcutting scroll down to the mechanics section, and expand the tree felling chances table
        if (resource.getRespawnRate() != 0) {
            if (RandomFunction.roll(8) || resource.identifier == 1 || resource.identifier == 2) {
                if (resource.isFarming()) {
                    FarmingPatch fPatch = FarmingPatch.forObject(node.asScenery());
                    if(fPatch != null) {
                        Patch patch = fPatch.getPatchFor(player);
                        patch.setCurrentState(patch.getCurrentState() + 1);
                    }
                    return true;
                }
                if (resource.getEmptyId() > -1) {
                    SceneryBuilder.replace(node, node.transform(resource.getEmptyId()), resource.getRespawnDuration());
                } else {
                    SceneryBuilder.replace(node, node.transform(0), resource.getRespawnDuration());
                }
                node.setActive(false);
                return true;
            }
        }
        return false;
    }

    private int calculateRewardAmount(int reward) {
        int amount = 1;

        // 3239: Hollow tree (bark) 10% chance of obtaining
        if (reward == 3239 && RandomFunction.random(100) >= 10) {
            amount = 0;
        }

        // Seers village medium reward - extra normal log while in seer's village
        if (reward == 1511
                && player.getAchievementDiaryManager().getDiary(DiaryType.SEERS_VILLAGE).isComplete(1)
                && player.getViewport().getRegion().getId() == 10806) {
            amount = 2;
        }

        return amount;
    }

    private double calculateExperience(int reward, int amount) {
        double experience = resource.getExperience();

        if(player.getLocation().getRegionId() == 10300){
            return 1.0;
        }

        // Bark
        if (reward == 3239) {
            // If we receive the item, give the full experience points otherwise give the base amount
            if (amount >= 1) {
                experience = 275.2;
            } else {
                amount = 1;
            }
        }

        // Seers village medium reward - extra 10% xp from maples while wearing headband
        if (reward == 1517
                && player.getAchievementDiaryManager().getDiary(DiaryType.SEERS_VILLAGE).isComplete(1)
                && player.getEquipment().get(EquipmentContainer.SLOT_HAT) != null
                && player.getEquipment().get(EquipmentContainer.SLOT_HAT).getId() == 14631) {
            experience *= 1.10;
        }

        return experience * amount;
    }

    private int calculateReward(int reward) {
        return reward;
    }

    /**
     * Checks if the player gets rewarded.
     *
     * @return {@code True} if so.
     */
    private boolean checkReward(SkillingTool tool) {
        int skill = Skills.WOODCUTTING;
        int level = player.getSkills().getLevel(skill) + player.getFamiliarManager().getBoost(skill);
        double hostRatio = RandomFunction.randomDouble(100.0);
        double lowMod = tool == SkillingTool.BLACK_AXE ? resource.tierModLow / 2 : resource.tierModLow;
        double low = resource.baseLow + (tool.ordinal() * lowMod);
        double highMod = tool == SkillingTool.BLACK_AXE ? resource.tierModHigh / 2 : resource.tierModHigh;
        double high = resource.baseHigh + (tool.ordinal() * highMod);
        double clientRatio = RandomFunction.getSkillSuccessChance(low,high,level);
        return hostRatio < clientRatio;
    }
}
