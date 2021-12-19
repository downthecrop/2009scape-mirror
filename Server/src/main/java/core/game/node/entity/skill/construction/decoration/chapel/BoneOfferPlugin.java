package core.game.node.entity.skill.construction.decoration.chapel;


import static api.ContentAPIKt.*;
import core.cache.def.impl.SceneryDefinition;
import core.game.content.global.Bones;
import core.game.node.entity.skill.Skills;
import core.game.interaction.NodeUsageEvent;
import core.game.interaction.UseWithHandler;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.audio.Audio;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.system.task.Pulse;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.context.Graphics;
import core.plugin.Initializable;
import core.plugin.Plugin;

/**
 * Handles the offering of bones on an altar.
 * @author Splinter
 */
@Initializable
public class BoneOfferPlugin extends UseWithHandler {
	
	/**
	 * The offer GFX.
	 */
	private final Graphics GFX = new Graphics(624);
	
	/**
	 * The offer Animation.
	 */
	private final Animation ANIM = new Animation(896);
	
	/**
	 * Constructor.
	 */
	public BoneOfferPlugin() {
		super(526, 2859, 528, 3183, 3179, 530, 532, 3125, 4812, 3123, 534, 6812, 536, 4830, 4832, 6729, 4834, 14693);
	}

	@Override
	public Plugin<Object> newInstance(Object arg) throws Throwable {
		addHandler(13185, OBJECT_TYPE, this);
		addHandler(13188, OBJECT_TYPE, this); 
		addHandler(13191, OBJECT_TYPE, this); 
		addHandler(13194, OBJECT_TYPE, this); 
		addHandler(13197, OBJECT_TYPE, this); 
		return this;
	}

	@Override
	public boolean handle(NodeUsageEvent event) {
		Player player = event.getPlayer();
		Scenery left = null;
		Scenery right = null;
		if (event.getUsedWith().asScenery().getRotation() % 2 == 0) {
			left = RegionManager.getObject(event.getUsedWith().getLocation().getZ(), event.getUsedWith().getLocation().getX() + 3, event.getUsedWith().getLocation().getY());
			right = RegionManager.getObject(event.getUsedWith().getLocation().getZ(), event.getUsedWith().getLocation().getX() - 2, event.getUsedWith().getLocation().getY());
		} else {
			left = RegionManager.getObject(event.getUsedWith().getLocation().getZ(), event.getUsedWith().getLocation().getX(), event.getUsedWith().getLocation().getY() + 3);
			right = RegionManager.getObject(event.getUsedWith().getLocation().getZ(), event.getUsedWith().getLocation().getX(), event.getUsedWith().getLocation().getY() - 2);
		}
		Bones b = Bones.forId(event.getUsedItem().getId());
		if (b != null) {
			worship(player, event.getUsedWith().asScenery(), left, right, b);
		}
		return true;
	}
	
	/**
	 * Worships the altar.
	 * @param player the player
	 * @param altar the altar object
	 * @param left the left brazier
	 * @param right the right brazier
	 * @param b the bone used
	 */
	private void worship(final Player player, final Scenery altar, final Scenery left, final Scenery right, final Bones b) {
		if (player.getIronmanManager().isIronman() && !player.getHouseManager().isInHouse(player)) {
			player.sendMessage("You cannot do this on someone else's altar.");
			return;
		}
		final Location start = player.getLocation();

		Location gfxLoc = player.getLocation().transform(player.getDirection(), 1);

		submitIndividualPulse(player, new Pulse(1) {
			int counter = 0;

			@Override
			public boolean pulse() {
				counter++;
				if (counter == 1 || counter % 5 == 0) {
					if (player.getInventory().remove(new Item(b.getItemId()))) {
						player.animate(ANIM);
						player.getAudioManager().send(new Audio(958));
						player.getPacketDispatch().sendPositionedGraphics(GFX, gfxLoc);
						player.sendMessage(getMessage(isLit(left), isLit(right)));
						player.getSkills().addExperience(Skills.PRAYER, b.getExperience() * getMod(altar, isLit(left), isLit(right)));
					}
				}
				return !player.getLocation().equals(start) || !player.getInventory().containsItem(new Item(b.getItemId()));
				
			}
			
		});
		
	}
	
	/**
	 * Checks if the burner is lit.
	 * @param obj the object.
	 */
	private boolean isLit(Scenery obj) {
		return obj != null && obj.getId() != 15271 && SceneryDefinition.forId(obj.getId()).getOptions() != null && !SceneryDefinition.forId(obj.getId()).hasAction("Light");
	}
	
	/**
	 * Gets the base modifier of the altar.
	 * @param altar the altar object
	 * @return the base bonus.
	 */
	private double getBase(Scenery altar) {
		double base = 150.0;
		if (altar == null) {
			return base;
		}
		switch (altar.getId()) {
		case 13182:
			base = 110.0;
			break;
		case 13185:
			base = 125.0;
			break;
		case 13188:
			base = 150.0;
			break;
		case 13191:
			base = 175.0;
			break;
		case 13194:
			base = 200.0;
			break;
		case 13197:
			base = 250.0;
			break;
		}
		return base;
	}
	
	/**
	 * Gets the total experience modifier.
	 * @param isLeft if the left is lit.
	 * @param isRight if the right is lit.
	 * @return the mod.
	 */
	private double getMod(Scenery altar, boolean isLeft, boolean isRight) {
		double total = getBase(altar);
		if (isLeft) {
			total += 50.0;
		}
		if (isRight) {
			total += 50.0;
		}
		return (total / 100);
	}
	
	/**
	 * Gets the proper message.
	 * @return the message.
	 */
	private String getMessage(boolean isLeft, boolean isRight) {
		return isLeft && isRight ? "The gods are very pleased with your offering." : isLeft || isRight ? "The gods are pleased with your offering." : "The gods accept your offering.";
	}
	
}