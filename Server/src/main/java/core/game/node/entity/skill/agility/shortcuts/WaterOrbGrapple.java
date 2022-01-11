package core.game.node.entity.skill.agility.shortcuts;

import core.cache.def.impl.SceneryDefinition;
import core.game.component.Component;
import org.rs09.consts.Items;
import core.game.interaction.OptionHandler;
import core.game.node.Node;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.diary.DiaryType;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.system.task.Pulse;
import rs09.game.world.GameWorld;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.game.node.entity.skill.Skills;

import java.util.HashMap;
import java.util.Map;

@Initializable
public class WaterOrbGrapple extends OptionHandler {
    private static final HashMap<Integer, Integer> REQUIREMENTS = new HashMap<>();
    private static final String requirementsString;

    static {
        REQUIREMENTS.putIfAbsent(Skills.AGILITY, 36);
        REQUIREMENTS.putIfAbsent(Skills.RANGE, 39);
        REQUIREMENTS.putIfAbsent(Skills.STRENGTH, 22);

        requirementsString = "You need at least "
                + REQUIREMENTS.get(Skills.AGILITY) + " " + Skills.SKILL_NAME[Skills.AGILITY] + ", "
                + REQUIREMENTS.get(Skills.RANGE) + " " + Skills.SKILL_NAME[Skills.RANGE] + ", and "
                + REQUIREMENTS.get(Skills.STRENGTH) + " " + Skills.SKILL_NAME[Skills.STRENGTH]
                + " to use this shortcut.";
    }

    private static final int[] CBOWS = new int[]{
            Items.MITH_CROSSBOW_9181,
            Items.ADAMANT_CROSSBOW_9183,
            Items.RUNE_CROSSBOW_9185,
            Items.DORGESHUUN_CBOW_8880
    };
    private static final Item MITH_GRAPPLE = new Item(9419);

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        SceneryDefinition.forId(17062).getHandlers().put("option:grapple", this);
        return this;
    }

    @Override
    public boolean handle(Player player, Node node, String option) {
        Location destination;
        Location current = player.getLocation();
        Scenery rock = RegionManager.getObject(Location.create(2841, 3426, 0));
        Scenery tree = RegionManager.getObject(Location.create(2841, 3434, 0));

        switch (option) {
            case "grapple":
                destination = Location.create(2841, 3433, 0);

                for (Map.Entry<Integer, Integer> e : REQUIREMENTS.entrySet()) {
                    if (player.getSkills().getLevel(e.getKey()) < e.getValue()) {
                        player.getDialogueInterpreter().sendDialogue(requirementsString);
                        return true;
                    }
                }

                if (!player.getEquipment().containsAtLeastOneItem(CBOWS) || !player.getEquipment().containsItem(MITH_GRAPPLE)) {
                    player.getDialogueInterpreter().sendDialogue("You need a Mithril crossbow and a Mithril grapple in order to do this.");
                    return true;
                }

                player.lock();
                GameWorld.getPulser().submit(new Pulse(1, player) {
                    int counter = 1;
                    Component tab;

                    @Override
                    public boolean pulse() {
                        switch (counter++) {
                            // TODO this animation sequence is wrong. sendPositionedGraphic doesn't work correctly, and rest of water crossing not well implemented
                            // See 4:24 in https://www.youtube.com/watch?v=O90y-N_vwTc
                            // rope gfx is 67
                            // splash gfx are 68 and 69, not sure why there are two
                            // not sure what swimming animation is, could be 4464 thru 4468
                            case 1:
                                player.faceLocation(destination);
                                player.animate(new Animation(4230));
                                break;
                            case 3:
                                player.getPacketDispatch().sendPositionedGraphic(67, 10, 0, Location.create(2840,3427,0)); //
                                break;
                            case 4:
                                SceneryBuilder.replace(rock, rock.transform(rock.getId() + 1), 10);
                                SceneryBuilder.replace(tree, tree.transform(tree.getId() + 1), 10);
                                break;
                            case 5:
                                break;
                            case 13:
                                player.getProperties().setTeleportLocation(destination);
                                break;
                            case 14:
                                player.unlock();
                                player.getAchievementDiaryManager().finishTask(player, DiaryType.SEERS_VILLAGE, 2, 10);
                                return true;
                        }
                        return false;
                    }
                });
                break;
        }
        return true;
    }

    @Override
    public Location getDestination(final Node moving, final Node destination) {
        // Run between rock and stream before firing grapple
        return Location.create(2841, 3427, 0);
    }
}
