package core.game.node.entity.player.link.appearance;

import core.cache.def.impl.ItemDefinition;
import core.cache.def.impl.NPCDefinition;
import core.game.container.impl.EquipmentContainer;
import core.game.node.entity.combat.equipment.WeaponInterface;
import core.game.node.entity.impl.Animator.Priority;
import core.game.node.entity.player.Player;
import core.game.node.item.Item;
import core.game.world.update.flag.context.Animation;
import core.game.world.update.flag.player.AppearanceFlag;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rs09.game.system.config.ItemConfigParser;

/**
 * Represents an appearance managing class of a player.
 * @author Emperor
 * @author Vexia
 */
public final class Appearance {

	/**
	 * Represents the player instance.
	 */
	private final Player player;

	/**
	 * Represents the cached animations.
	 */
	private final int[] animationCache = new int[] { AppearanceCache.STAND_ANIM, AppearanceCache.STAND_TURN_ANIM, AppearanceCache.WALK_ANIM, AppearanceCache.TURN_180_AIM, AppearanceCache.TURN_90_CW, AppearanceCache.TUNR_90_CWW, AppearanceCache.RUN_ANIM };
	
	/**
	 * Represents the cached icons.
	 */
	private final int[] iconCache = new int[] { -1, -1 };

	/**
	 * Represents the shown body parts.
	 */
	private final int[] bodyParts = new int[14];

	/**
	 * Represents the cached body parts (default male).
	 */
	private BodyPart[] appearanceCache = Gender.MALE.generateCache();

	/**
	 * Represents the gender of a player.
	 */
	private Gender gender = Gender.MALE;

	/**
	 * Represents the npc id to represent if not -1.
	 */
	private int npcId = -1;
	
	/**
	 * The render animation id.
	 */
	private int renderAnimationId = 1426;

	/**
	 * If the player is riding a mining cart.
	 */
	private boolean ridingMinecart;

	/**
	 * Constructs a new {@code Appearance} {@code Object}.
	 * @param player the player of this appearance.
	 */
	public Appearance(final Player player) {
		this.player = player;
	}

	public void parse(JSONObject appearance){
		gender = gender.asByte(Byte.parseByte(appearance.get("gender").toString()));
		JSONArray appCache = (JSONArray) appearance.get("appearance_cache");
		for(int i = 0; i < appearanceCache.length; i++){
			(appearanceCache[i]).parse((JSONObject) appCache.get(i));
		}
	}

	/**
	 * Transforms the player into an NPC.
	 * @param id The NPC id.
	 */
	public void transformNPC(int id) {
		this.npcId = id;
		setAnimations();
		if (id == -1) {
			player.setSize(1);
			Animation[] anims = WeaponInterface.DEFAULT_ANIMS;
			if (player.getEquipment().get(3) != null) {
				anims = player.getEquipment().get(3).getDefinition().getConfiguration(ItemConfigParser.ATTACK_ANIMS, anims);
			}
			int index = player.getSettings().getAttackStyleIndex();
			if (index < 0 || index >= anims.length) {
				index = 0;
			}
			player.getProperties().setAttackAnimation(anims[index]);
			player.getProperties().setDefenceAnimation(new Animation(404));
			player.getProperties().setDeathAnimation(new Animation(9055, Priority.HIGH));
		} else {
			NPCDefinition def = NPCDefinition.forId(id);
			player.setSize(def.getSize());
			if (def.getCombatAnimation(0) != null) {
				player.getProperties().setAttackAnimation(def.getCombatAnimation(0));
			}
			if (def.getCombatAnimation(3) != null) {
				player.getProperties().setDefenceAnimation(def.getCombatAnimation(3));
			}
			if (def.getCombatAnimation(4) != null) {
				player.getProperties().setDeathAnimation(def.getCombatAnimation(4));
			}
		}
		sync();
	}

	/**
	 * Sets the rendering animations.
	 */
	public void setAnimations() {
		if (npcId == -1) {
			Item weapon = player.getEquipment().get(3);
			if (isRidingMinecart()) {
				this.setRidingMinecart(false);
			}
			if (weapon == null || weapon.getDefinition().getConfiguration(ItemConfigParser.WALK_ANIM, null) == null) {
				setDefaultAnimations();
			} else {
				ItemDefinition def = weapon.getDefinition();
				setStandAnimation(def.getConfiguration(ItemConfigParser.STAND_ANIM, 0x328));
				setStandTurnAnimation(def.getConfiguration(ItemConfigParser.STAND_TURN_ANIM, 0x337));
				setWalkAnimation(def.getConfiguration(ItemConfigParser.WALK_ANIM, 0x333));
				setRunAnimation(def.getConfiguration(ItemConfigParser.RUN_ANIM, 0x338));
				setTurn180(def.getConfiguration(ItemConfigParser.TURN180_ANIM, 0x334));
				setTurn90cw(def.getConfiguration(ItemConfigParser.TURN90CW_ANIM, 0x335));
				setTurn90ccw(def.getConfiguration(ItemConfigParser.TURN90CCW_ANIM, 0x336));
				renderAnimationId = def.getRenderAnimationId();
			}
			if (weapon != null && weapon.getId() == 12842) {
				setStandAnimation(8964);
				setWalkAnimation(8961);
				setRunAnimation(8963);
				setTurn180(8963);
				setTurn90ccw(8963);
				setTurn90cw(8963);
				setStandTurnAnimation(8963);
			}
		} else {
			NPCDefinition def = NPCDefinition.forId(npcId);
			renderAnimationId = def.getRenderAnimationId();
			setStandAnimation(def.getStandAnimation());
			int turn = def.getTurnAnimation();
			if (turn < 1) {
				turn = def.getWalkAnimation();
			}
			setStandTurnAnimation(turn);
			setWalkAnimation(def.getWalkAnimation());
			setRunAnimation(def.getWalkAnimation());
			if (def.getTurn180Animation() > 0) {
				setTurn180(def.getTurn180Animation());
			} else {
				setTurn180(turn);
			}
			if (def.getTurnCWAnimation() > 0) {
				setTurn90cw(def.getTurnCWAnimation());
			} else {
				setTurn90cw(turn);
			}
			if (def.getTurnCCWAnimation() > 0) {
				setTurn90ccw(def.getTurnCCWAnimation());
			} else {
				setTurn90ccw(turn);
			}
		}
	}

	/**
	 * Sets the animations.
	 * @param anim the animation.
	 */
	public void setAnimations(Animation anim) {
		renderAnimationId = anim.getId();
		sync();
	}

	/**
	 * Method used to sync this appearance with the client.
	 */
	public void sync() {
		player.getUpdateMasks().register(new AppearanceFlag(player));
	}

	/**
	 * Copies the appearance.
	 * @param appearance The appearance to copy from.
	 */
	public void copy(Appearance appearance) {
		gender = appearance.gender;
		for (int i = 0; i < appearanceCache.length; i++) {
			appearanceCache[i] = appearance.appearanceCache[i];
		}
		for (int i = 0; i < animationCache.length; i++) {
			animationCache[i] = appearance.animationCache[i];
		}
		renderAnimationId = appearance.renderAnimationId;
	}

	/**
	 * Draws an item on a body part.
	 * @param part The body part.
	 * @param item The item to draw.
	 */
	public void drawItem(int part, Item item) {
		this.bodyParts[part] = item.getDefinition().getEquipId() + 0x8000;
	}

	/**
	 * Draws clothing on a body part.
	 * @param part The body part.
	 * @param clothesId The clothes id.
	 */
	public void drawClothes(int part, int clothesId) {
		this.bodyParts[part] = clothesId + 0x100;
	}

	/**
	 * Clears a body part.
	 * @param part The part to clear.
	 */
	public void clearBodyPart(int part) {
		this.bodyParts[part] = 0;
	}

	public void flagHatClipping() {
		boolean isBald = getHair().getLook() == (isMale() ? 0 : 45);
		int hairLook = isBald ? getHair().getLook() : (isMale() ? 5 : 51);
		this.bodyParts[8] = hairLook + 0x100;
	}

	/**
	 * Prepares the data used for the appearance update mask.
	 * @param player The player.
	 */
	public void prepareBodyData(Player player) {
		if (player.getRenderInfo().preparedAppearance()) {
			return;
		}
		player.getRenderInfo().setPreparedAppearance(true);
		Item chest = player.getEquipment().get(EquipmentContainer.SLOT_CHEST);
		Item shield = player.getEquipment().get(EquipmentContainer.SLOT_SHIELD);
		Item legs = player.getEquipment().get(EquipmentContainer.SLOT_LEGS);
		Item hat = player.getEquipment().get(EquipmentContainer.SLOT_HAT);
		Item hands = player.getEquipment().get(EquipmentContainer.SLOT_HANDS);
		Item feet = player.getEquipment().get(EquipmentContainer.SLOT_FEET);
		Item cape = player.getEquipment().get(EquipmentContainer.SLOT_CAPE);
		Item amulet = player.getEquipment().get(EquipmentContainer.SLOT_AMULET);
		Item weapon = player.getEquipment().get(EquipmentContainer.SLOT_WEAPON);
		boolean castleWarsHood = cape != null && (cape.getId() == 4041 || cape.getId() == 4042); // Item
		if (hat != null) {
			drawItem(0, hat);
		} else {
			clearBodyPart(0);
		}
		if (cape != null) {
			drawItem(1, cape);
		} else {
			clearBodyPart(1);
		}
		if (amulet != null) {
			drawItem(2, amulet);
		} else {
			clearBodyPart(2);
		}
		if (!ridingMinecart) {
			if (weapon != null) {
				drawItem(3, weapon);
			} else {
				clearBodyPart(3);
			}
			if (shield != null) {
				drawItem(5, shield);
			} else {
				clearBodyPart(5);
			}
		} else {
			clearBodyPart(5);
			drawClothes(3, 82);
		}
		if (chest != null) {
			drawItem(4, chest);
		} else {
			drawClothes(4, getTorso().getLook());
		}
		if (chest != null && chest.getDefinition().getConfiguration(ItemConfigParser.REMOVE_SLEEVES, false)) {
			clearBodyPart(6);
		} else {
			drawClothes(6, getArms().getLook());
		}
		if (legs != null) {
			drawItem(7, legs);
		} else {
			drawClothes(7, getLegs().getLook());
		}
		if ((hat != null && hat.getDefinition().getConfiguration(ItemConfigParser.REMOVE_HEAD, false)) || castleWarsHood) {
			clearBodyPart(8);
		} else {
			drawClothes(8, getHair().getLook());
		}
		if((hat != null && hat.getDefinition().getConfiguration(ItemConfigParser.IS_HAT,false))){
			flagHatClipping();
		}
		if (hands != null) {
			drawItem(9, hands);
		} else {
			drawClothes(9, getWrists().getLook());
		}
		if (feet != null) {
			drawItem(10, feet);
		} else {
			drawClothes(10, getFeet().getLook());
		}
		if (hat != null && hat.getDefinition().getConfiguration(ItemConfigParser.REMOVE_BEARD, false)) {
			clearBodyPart(11);
		} else {
			drawClothes(11, getBeard().getLook());
		}

	}

	/**
	 * Rides a mine cart.
	 */
	public void rideCart(boolean ride) {
		if (!ride) {
			setAnimations();
		} else {
			player.getAppearance().setAnimations(Animation.create(211));
		}
		player.getAppearance().setRidingMinecart(ride);
		player.getAppearance().sync();
	}

	/**
	 * Flys a gnome copter.
	 */
	public void flyCopter() {
		player.animate(Animation.create(8956));
		player.getEquipment().replace(new Item(12842), 3);
		player.getAppearance().setStandAnimation(8964);
		player.getAppearance().setWalkAnimation(8961);
		player.getAppearance().setRunAnimation(8963);
		player.getAppearance().setTurn180(8963);
		player.getAppearance().setTurn90ccw(8963);
		player.getAppearance().setTurn90cw(8963);
		player.getAppearance().setStandTurnAnimation(8963);
	}

	/**
	 * Gets the player.
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the hair body part.
	 * @return the hair.
	 */
	public BodyPart getHair() {
		return appearanceCache[AppearanceCache.HAIR];
	}

	/**
	 * Gets the beard body part.
	 * @return the beard.
	 */
	public BodyPart getBeard() {
		return appearanceCache[AppearanceCache.FACIAL_HAIR];
	}

	/**
	 * Gets the torso body part.
	 * @return the torso.
	 */
	public BodyPart getTorso() {
		return appearanceCache[AppearanceCache.TORSO];
	}

	/**
	 * Gets the arms body part.
	 * @return the body part.
	 */
	public BodyPart getArms() {
		return appearanceCache[AppearanceCache.ARMS];
	}

	/**
	 * Gets the wrist part part.
	 * @return the body part.
	 */
	public BodyPart getWrists() {
		return appearanceCache[AppearanceCache.WRISTS];
	}

	/**
	 * Gets the skin part.
	 * @return the part.
	 */
	public BodyPart getSkin() {
		return appearanceCache[AppearanceCache.WRISTS];
	}

	/**
	 * Getsthe leg body parts.
	 * @return the legs.
	 */
	public BodyPart getLegs() {
		return appearanceCache[AppearanceCache.LEGS];
	}

	/**
	 * Gets the feet body parts.
	 * @return the feet.
	 */
	public BodyPart getFeet() {
		return appearanceCache[AppearanceCache.FEET];
	}

	/**
	 * Gets the stand animation.
	 * @return the animation.
	 */
	public int getStandAnimation() {
		return animationCache[0];
	}

	/**
	 * Method used to set the stand animation.
	 * @param animation
	 */
	public void setStandAnimation(int animation) {
		animationCache[0] = animation;
	}

	/**
	 * Gets the standing turn animation.
	 * @return the animation.
	 */
	public int getStandTurnAnimation() {
		return animationCache[1];
	}

	/**
	 * Sets the standing turn animation.
	 */
	public void setStandTurnAnimation(int animation) {
		animationCache[1] = animation;
	}

	/**
	 * Gets the walk animation.
	 * @return the animation.
	 */
	public int getWalkAnimation() {
		return animationCache[2];
	}

	/**
	 * Sets the walk animation.
	 * @param animation the animation.
	 */
	public void setWalkAnimation(int animation) {
		animationCache[2] = animation;
	}

	/**
	 * Gets the turning 180 animation.
	 * @return the animation.
	 */
	public int getTurn180() {
		return animationCache[3];
	}

	/**
	 * Sets the tunring 180 animation.
	 * @param animation the animation.
	 */
	public void setTurn180(int animation) {
		animationCache[3] = animation;
	}

	/**
	 * Gets the turn90cw animation.
	 * @return the animation.
	 */
	public int getTurn90cw() {
		return animationCache[4];
	}

	/**
	 * Sets the turn90cw animation.
	 * @param animation the animation.
	 */
	public void setTurn90cw(int animation) {
		animationCache[4] = animation;
	}

	/**
	 * Gets the turn90ccw animation.
	 * @return the animation.
	 */
	public int getTurn90ccw() {
		return animationCache[5];
	}

	/**
	 * Sets thr turn90cww animation.
	 * @param animation
	 */
	public void setTurn90ccw(int animation) {
		animationCache[5] = animation;
	}

	/**
	 * Gets the run animation.
	 * @return the animation.
	 */
	public int getRunAnimation() {
		return animationCache[6];
	}

	/**
	 * Sets the running animation.
	 * @param animation the animation.
	 */
	public void setRunAnimation(int animation) {
		animationCache[6] = animation;
	}

	/**
	 * Gets the render animation id.
	 * @return The render animation id.
	 */
	public int getRenderAnimation() {
		return renderAnimationId;
	}

	/**
	 * Method used to set the default animations.
	 */
	public void setDefaultAnimations() {
		for (int i = 0; i < animationCache.length; i++) {
			animationCache[i] = AppearanceCache.ANIMATIONS[i];
		}
		renderAnimationId = 1426;
	}

	/**
	 * Gets the skull icon.
	 * @return the icon.
	 */
	public int getSkullIcon() {
		return iconCache[0];
	}

	/**
	 * Sets the skull icon.
	 * @param icon the icon.
	 */
	public void setSkullIcon(int icon) {
		iconCache[0] = icon;
	}

	/**
	 * Gets the head icon.
	 * @return the icon.
	 */
	public int getHeadIcon() {
		return iconCache[1];
	}

	/**
	 * Sets the head icon.
	 * @param icon the icon.
	 */
	public void setHeadIcon(int icon) {
		this.iconCache[1] = icon;
	}

	/**
	 * Gets the gender.
	 * @return The gender.
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * Method used to change the gender of a player.
	 * @param gender the gender.
	 */
	public void changeGender(Gender gender) {
		setGender(gender);
		sync();
	}

	/**
	 * Method used to set the gender.
	 * @param gender the gender.
	 */
	public void setGender(final Gender gender) {
		this.gender = gender;
		this.appearanceCache = gender.generateCache();
	}

	/**
	 * Gets the value {@code True} if male.
	 * @return {@code True} if so.
	 */
	public boolean isMale() {
		return gender == Gender.MALE;
	}

	/**
	 * Gets the npc id.
	 * @return the npc id.
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * If the player's appearance is a NPC.
	 * @return If the player's appearance is a NPC {@code true}.
	 */
	public boolean isNpc() {
		return npcId != -1;
	}

	/**
	 * Gets the appearanceCache.
	 * @return The appearanceCache.
	 */
	public BodyPart[] getAppearanceCache() {
		return appearanceCache;
	}

	/**
	 * Gets the animationCache.
	 * @return The animationCache.
	 */
	public int[] getAnimationCache() {
		return animationCache;
	}

	/**
	 * Gets the bodyParts.
	 * @return The bodyParts.
	 */
	public int[] getBodyParts() {
		return bodyParts;
	}

	/**
	 * Gets the ridingMinecart.
	 * @return The ridingMinecart.
	 */
	public boolean isRidingMinecart() {
		return ridingMinecart;
	}

	/**
	 * Sets the ridingMinecart.
	 * @param ridingMinecart The ridingMinecart to set.
	 */
	public void setRidingMinecart(boolean ridingMinecart) {
		this.ridingMinecart = ridingMinecart;
	}

	/**
	 * Represents a class of cached appearance related information.
	 * @author 'Vexia
	 */
	public static class AppearanceCache {

		/**
		 * Represents the appearance animations.
		 */
		public static final int[] ANIMATIONS = new int[] { 0x328, 0x337, 0x333, 0x334, 0x335, 0x336, 0x338 };

		/**
		 * Represents the index hair apperance is cached at.
		 */
		public static final int HAIR = 0;

		/**
		 * Represents the index facial hair appearance is cached at.
		 */
		public static final int FACIAL_HAIR = 1;

		/**
		 * Represents the index torso appearance is cached at.
		 */
		public static final int TORSO = 2;

		/**
		 * Represents the index arm appearance is cached at.
		 */
		public static final int ARMS = 3;

		/**
		 * Represents the index wrist appearance is cached at.
		 */
		public static final int WRISTS = 4;

		/**
		 * Represents the index leg appearance is cached at.
		 */
		public static final int LEGS = 5;

		/**
		 * Represents the index feet appearance is cached at.
		 */
		public static final int FEET = 6;

		/**
		 * Represents the index hair color is cached at.
		 */
		public static final int HAIR_COLOR = 0;

		/**
		 * Represents the index torso color is cached at.
		 */
		public static final int TORSO_COLOR = 1;

		/**
		 * Represents the index leg color is cached at.
		 */
		public static final int LEG_COLOR = 2;

		/**
		 * Represents the index feet color is cached at.
		 */
		public static final int FEET_COLOR = 3;

		/**
		 * Represents the index skin color is cached at.
		 */
		public static final int SKIN_COLOR = 4;

		/**
		 * The player's stand animation.
		 */
		private static final int STAND_ANIM = 0x328;

		/**
		 * The player's turn animation for standing.
		 */
		private static final int STAND_TURN_ANIM = 0x337;

		/**
		 * The player's walk animation.
		 */
		public static final int WALK_ANIM = 0x333;

		/**
		 * The player's turn 180 degrees animation.
		 */
		private static final int TURN_180_AIM = 0x334;

		/**
		 * The player's turn 90 degrees animation.
		 */
		private static final int TURN_90_CW = 0x335;

		/**
		 * The player's turn 90 degrees animation.
		 */
		private static final int TUNR_90_CWW = 0x336;

		/**
		 * The player's run animation.
		 */
		private static final int RUN_ANIM = 0x338;

	}

}
