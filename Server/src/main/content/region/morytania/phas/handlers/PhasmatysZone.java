package content.region.morytania.phas.handlers;

import content.global.skill.agility.AgilityHandler;
import content.global.skill.prayer.Bones;
import content.region.morytania.phas.dialogue.NecrovarusDialogue;
import core.game.global.action.ClimbActionHandler;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Direction;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.update.flag.context.Animation;
import core.plugin.ClassScanner;
import core.plugin.Initializable;
import core.plugin.Plugin;
import org.rs09.consts.NPCs;
import org.rs09.consts.Sounds;

import static core.api.ContentAPIKt.playAudio;
import static core.api.ContentAPIKt.sendMessage;

/**
 * Handles the phasmatys zone area.
 *
 * @author Vexia
 */
@Initializable
public final class PhasmatysZone extends MapZone implements Plugin<Object> {
    int b;
    Bones[] bones;
    Player player;
    Bones bone;

    /**
     * Constructs a new {@code PhasmatysZone} {@code Object}.
     */
    public PhasmatysZone() {
        super("Port phasmatys", true);
    }

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        ZoneBuilder.configure(this);
        ClassScanner.definePlugin(new GravingasNPC());
        ClassScanner.definePlugin(new NecrovarusDialogue());
        ClassScanner.definePlugin(new EctoplasmFillPlugin());
        return this;
    }

    @Override
    public boolean interact(Entity e, Node target, Option option) {
        if (e.isPlayer()) {
            player = e.asPlayer();
            if (target instanceof NPC) {
                NPC npc = (NPC) target;

                if (npc.getId() == NPCs.GHOST_BANKER_1702) {
                    // Kludge to prevent this module from overriding NPC
                    // handling until I rip this piece of Vexia shit out.
                    return false;
                }

                if ((npc.getName().toLowerCase().contains("ghost") || npc.getName().equalsIgnoreCase("velorina") || npc.getName().contains("husband")) && !hasAmulet(player)) {
                    //player.getDialogueInterpreter().open("ghosty dialogue", target); // Commented for ghost npcs to work from their individual plugin files
                    player.getDialogueInterpreter().open(target.getId() , target);
                    return true;
                }
            }
            switch (target.getId()) {
                case 5259:
                    handleEnergyBarrier(player, (Scenery) target);
                    return true;
                case 5267:
                    player.animate(Animation.create(536));
                    sendMessage(player, "The trapdoor opens...");
                    SceneryBuilder.replace((Scenery) target, ((Scenery) target).transform(5268));
                    return true;
                case 5268:
                    if (option.getName().equals("Close")) {
                        player.animate(Animation.create(535));
                        sendMessage(player, "You close the trapdoor.");
                        SceneryBuilder.replace((Scenery) target, ((Scenery) target).transform(5267));
                    } else {
                        sendMessage(player, "You climb down through the trapdoor...");
                        player.getProperties().setTeleportLocation(Location.create(3669, 9888, 3));
                    }
                    return true;
                case 7434: // Trapdoors in bar
                    if (option.getName().equalsIgnoreCase("open")) {
                        SceneryBuilder.replace(target.asScenery(), target.asScenery().transform(7435));
                    }
                    break;
                case 7435: // open trapdoor in bar
                    if (option.getName().equalsIgnoreCase("close")) {
                        SceneryBuilder.replace(target.asScenery(), target.asScenery().transform(7434));
                    }
                    break;
                case 9308:
                    if (player.getSkills().getStaticLevel(Skills.AGILITY) < 58) {
                        player.sendMessage("You need an agility level of at least 58 to climb down this wall.");
                        return true;
                    }
                    player.getProperties().setTeleportLocation(Location.create(3671, 9888, 2));
                    return true;
                case 9307:
                    if (player.getSkills().getStaticLevel(Skills.AGILITY) < 58) {
                        player.sendMessage("You need an agility level of at least 58 to climb up this wall.");
                        return true;
                    }
                    player.getProperties().setTeleportLocation(Location.create(3670, 9888, 3));
                    return true;
                case 5264:
                    ClimbActionHandler.climb(player, Animation.create(828), Location.create(3654, 3519, 0));
                    return true;
                case 5282:
                    worship(player);
                    return true;
            }
        }
        return super.interact(e, target, option);
    }

    /**
     * Checks if the player has the amulet equipped.
     *
     * @param player the player.
     * @return {@code True} if so.
     */
    public static boolean hasAmulet(Player player) {
        return player.getEquipment().contains(552, 1);
    }

    /**
     * Worships the ectofuntus.
     *
     * @param player the player.
     */
    private void worship(Player player) {
        Bones bone = null;
        for (Item i : player.getInventory().toArray()) {
            if (i == null) {
                continue;
            }
            Bones b = Bones.forBoneMeal(i.getId());
            if (b != null) {
                bone = b;
                break;
            }
        }
        if (!player.getInventory().contains(4286, 1) && bone == null) {
            player.getDialogueInterpreter().sendDialogue("You don't have any ectoplasm or crushed bones to put into the", "Ectofuntus.");
            return;
        }
        if (bone == null) {
            player.getDialogueInterpreter().sendDialogue("You need a pot of crushed bones to put into the Ectofuntus.");
            return;
        }
        if (!player.getInventory().contains(4286, 1)) {
            player.getDialogueInterpreter().sendDialogue("You need ectoplasm to put into the Ectofuntus.");
            return;
        }
        if (player.getInventory().remove(new Item(bone.getBonemealId()), new Item(4286, 1))) {
            player.lock(1);
            player.animate(Animation.create(1651));
            playAudio(player, Sounds.PRAYER_BOOST_2671);
            player.getInventory().add(new Item(1925), new Item(1931));
            player.getSkills().addExperience(Skills.PRAYER, bone.getExperience() * 4, true);
            player.sendMessage("You put some ectoplasm and bonemeal into the Ectofuntus, and worship it.");
            player.getSavedData().getGlobalData().setEctoCharges(player.getSavedData().getGlobalData().getEctoCharges() + 1);
        }
    }

    /**
     * Handles the passing of the energy barrier.
     *
     * @param player the player.
     * @param object the object.
     */
    private void handleEnergyBarrier(Player player, Scenery object) {
        boolean force = object.getLocation().equals(3659, 3508, 0) && player.getLocation().getY() < 3508 || object.getLocation().equals(3652, 3485, 0) && player.getLocation().getX() > 3652;
        if (!force && !player.getInventory().contains(4278, 2)) {
            player.getDialogueInterpreter().open(1706);
            return;
        }
        if (force || player.getInventory().remove(new Item(4278, 2))) {
            final Direction direction = Direction.getLogicalDirection(player.getLocation(), object.getLocation());
            Location end = player.getLocation().transform(direction, 2);
            if (player.getLocation().getY() >= 3508) {
                end = object.getLocation().transform(0, -2, 0);
            }
            if (object.getLocation().equals(new Location(3652, 3485, 0))) {
                end = object.getLocation().transform(player.getLocation().getX() >= 3653 ? -1 : 2, 0, 0);
            }
            AgilityHandler.walk(player, -1, player.getLocation(), end, null, 0.0, null);
        }
    }

    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    @Override
    public void configure() {
        register(new ZoneBorders(3583, 3456, 3701, 3552));
        register(new ZoneBorders(3667, 9873, 3699, 9914));
    }

}
