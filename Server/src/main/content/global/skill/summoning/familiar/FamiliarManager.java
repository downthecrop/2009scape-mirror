package content.global.skill.summoning.familiar;

import content.global.skill.summoning.pet.Pet;
import content.global.skill.summoning.pet.Pets;
import core.cache.def.impl.ItemDefinition;
import core.game.component.Component;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import core.game.node.entity.skill.Skills;
import content.global.skill.summoning.SummoningPouch;
import content.global.skill.summoning.pet.PetDetails;
import core.game.node.entity.combat.BattleState;
import core.game.node.entity.player.Player;

import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.map.zone.ZoneRestriction;
import core.game.world.update.flag.context.Animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static core.api.ContentAPIKt.*;

/**
 * Handles a player's familiar.
 * @author Emperor
 * @author Player Name
 */
public final class FamiliarManager {

	/**
	 * The familiars mapping.
	 */
	private static final Map<Integer, Familiar> FAMILIARS = new HashMap<>();

	/**
	 * The pet details mapping.
	 */
	private final Map<Integer, ArrayList<PetDetails>> petDetails = new HashMap<>();

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The familiar.
	 */
	private Familiar familiar;

	/**
	 * The combat level difference when using summoning.
	 */
	private int summoningCombatLevel;

	/**
	 * If the player has a summoning pouch.
	 */
	private boolean hasPouch;
	
	/**
	 * Constructs a new {@code FamiliarManager} {@code Object}.
	 * @param player The player.
	 */
	public FamiliarManager(Player player) {
		this.player = player;
	}

	public void parse(JSONObject familiarData) {
		for (Pets pet : Pets.values()) {
			for (int id : new int[]{pet.getBabyItemId(), pet.getGrownItemId(), pet.getOvergrownItemId()}) {
				if (id != -1) {
					petDetails.put(id, new ArrayList<PetDetails>());
				}
			}
		}

		int currentPet = -1;
		if (familiarData.containsKey("currentPet")) {
			currentPet = Integer.parseInt(familiarData.get("currentPet").toString());
		}
		if (player.version < 2) { //migrate the v1 format
			JSONArray petDetails = (JSONArray) familiarData.get("petDetails");
			for (Object petDetail : petDetails) {
				JSONObject detail = (JSONObject) petDetail;
				PetDetails details = new PetDetails(0);
				details.updateHunger(Double.parseDouble(detail.get("hunger").toString()));
				details.updateGrowth(Double.parseDouble(detail.get("growth").toString()));
				int itemId;
				int itemIdHash = Integer.parseInt(detail.get("petId").toString());
				// The below is for migrating the v0 format, which stored baby item IDs + growth stages
				if (detail.containsKey("stage")) {
					// The itemIdHash is actually the baby item ID. The "stage" gives the actual pet stage we want.
					int babyItemId = itemIdHash;
					itemId = babyItemId;
					int stage = Integer.parseInt(detail.get("stage").toString());
					if (stage > 0) {
						Pets pets = Pets.forId(babyItemId);
						itemId = pets.getNextStageItemId(itemId);
						if (stage > 1) {
							itemId = pets.getNextStageItemId(itemId);
						}
					}
				} else {
					itemId = itemIdHash >> 16 & 0xFFFF; //in the legacy v1 format, was hash rather than item id
				}
				this.petDetails.get(itemId).add(details);
			}
			if (currentPet > 65536) {
				currentPet = currentPet >> 16 & 0xFFFF; //in the legacy v1 format, was hash rather than item id
			}
        } else {
			JSONObject petDetails = (JSONObject) familiarData.get("petDetails");
			for (Object key : petDetails.keySet()) {
				int itemId = Integer.parseInt(key.toString());
				this.petDetails.put(itemId, new ArrayList<>());
				JSONArray values = (JSONArray) petDetails.get(key.toString());
				for (Object petDetail : values) {
					JSONObject detail = (JSONObject) petDetail;
					PetDetails details = new PetDetails(0);
					details.updateHunger(Double.parseDouble(detail.get("hunger").toString()));
					details.updateGrowth(Double.parseDouble(detail.get("growth").toString()));
					this.petDetails.get(itemId).add(details);
				}
			}
		}
		if (currentPet != -1) {
			int last = this.petDetails.get(currentPet).size() - 1;
			PetDetails details = this.petDetails.get(currentPet).get(last);
			Pets pets = Pets.forId(currentPet);
			familiar = new Pet(player, details, currentPet, pets.getNpcId(currentPet));
		} else if (familiarData.containsKey("familiar")) {
			JSONObject currentFamiliar = (JSONObject) familiarData.get("familiar");
			int familiarId = Integer.parseInt( currentFamiliar.get("originalId").toString());
			familiar = FAMILIARS.get(familiarId).construct(player,familiarId);
			familiar.ticks = Integer.parseInt( currentFamiliar.get("ticks").toString());
			familiar.specialPoints = Integer.parseInt( currentFamiliar.get("specialPoints").toString());
			JSONArray famInv = (JSONArray) currentFamiliar.get("inventory");
			if (famInv != null) {
				((BurdenBeast) familiar).container.parse(famInv);
			}
			familiar.setAttribute("hp",Integer.parseInt( currentFamiliar.get("lifepoints").toString()));
		}
	}

	/**
	 * Called when the player logs in.
	 */
	public void login() {
		if (hasFamiliar()) {
			familiar.init();
		}
		player.getFamiliarManager().setConfig(243269632);
	}

	/**
	 * Summons a familiar.
	 * @param item The item.
	 * @param pet If the familiar is a pet.
	 * @param deleteItem we should delete the item.
	 */
	public void summon(Item item, boolean pet, boolean deleteItem) {
		boolean renew = false;
		if (hasFamiliar()) {
			if (familiar.getPouchId() == item.getId()) {
				renew = true;
			} else {
				player.getPacketDispatch().sendMessage("You already have a follower.");
				return;
			}
		}
		if (player.getZoneMonitor().isRestricted(ZoneRestriction.FOLLOWERS) && !player.getLocks().isLocked("enable_summoning")) {
			player.getPacketDispatch().sendMessage("This is a Summoning-free area.");
			return;
		}
		if (pet) {
			summonPet(item, deleteItem);
			return;
		}
		final SummoningPouch pouch = SummoningPouch.get(item.getId());
		if (pouch == null) {
			return;
		}
		if (player.getSkills().getStaticLevel(Skills.SUMMONING) < pouch.getLevelRequired()) {
			player.getPacketDispatch().sendMessage("You need a Summoning level of " + pouch.getLevelRequired() + " to summon this familiar.");
			return;
		}
		if (player.getSkills().getLevel(Skills.SUMMONING) < pouch.getSummonCost()) {
			player.getPacketDispatch().sendMessage("You need at least " + pouch.getSummonCost() + " Summoning points to summon this familiar.");
			return;
		}
		final int npcId = pouch.getNpcId();
		Familiar fam = !renew ? FAMILIARS.get(npcId) : familiar;
		if (fam == null) {
			player.getPacketDispatch().sendMessage("Invalid familiar " + npcId + " - report on 2009Scape GitLab");
			return;
		}
		if (!renew) {
			fam = fam.construct(player, npcId);
			if (fam.getSpawnLocation() == null) {
				player.getPacketDispatch().sendMessage("The spirit in this pouch is too big to summon here. You will need to move to a larger");
				player.getPacketDispatch().sendMessage("area.");
				return;
			}
		}
		if (!player.getInventory().remove(item)) {
			return;
		}
		player.getSkills().updateLevel(Skills.SUMMONING, -pouch.getSummonCost(), 0);
		player.getSkills().addExperience(Skills.SUMMONING, pouch.getSummonExperience());
		if (!renew) {
			familiar = fam;
			spawnFamiliar();
		} else {
			familiar.refreshTimer();
		}
		player.getAppearance().sync();
	}

	/**
	 * Summons a familiar.
	 * @param item the item.
	 * @param pet the pet.
	 */
	public void summon(final Item item, boolean pet) {
		summon(item, pet, true);
	}

	/**
	 * Morphs a pet.
	 * @param item the item.
	 * @param deleteItem the item.
	 * @param location the location.
	 */
	public void morphPet(final Item item, boolean deleteItem, Location location, double hunger, double growth) {
		int hasWarned = ((Pet) familiar).getHasWarned();
		familiar.dismiss();
		summonPet(item, deleteItem, true, location, hasWarned, hunger, growth);
	}
	
	/**
	 * Summons a pet.
	 * @param item the item.
	 * @param deleteItem the item.
	 */
	private boolean summonPet(final Item item, boolean deleteItem) {
		return summonPet(item, deleteItem, false, null, 0, -1, -1);
	}
	
	/**
	 * Summons a pet.
	 * @param item the item.
	 * @param morph the pet.
	 */
	private boolean summonPet(final Item item, boolean deleteItem, boolean morph, Location location, int hasWarned, double hunger, double growth) {
		final int itemId = item.getId();
		if (itemId > 8850 && itemId < 8900) {
			return false;
		}
		Pets pets = Pets.forId(itemId);
		if (pets == null) {
			return false;
		}
		if (player.getSkills().getStaticLevel(Skills.SUMMONING) < pets.getSummoningLevel()) {
			player.getDialogueInterpreter().sendDialogue("You need a summoning level of " + pets.getSummoningLevel() + " to summon this.");
			return false;
		}
		int last = this.petDetails.get(itemId).size() - 1;
		if (last < 0) { //new pet
			last = 0;
			PetDetails details = new PetDetails(pets.getGrowthRate() == 0.0 ? 100.0 : 0.0);
			this.petDetails.get(itemId).add(details);
		}
		PetDetails details = this.petDetails.get(itemId).get(last);
		int npcId = pets.getNpcId(itemId);
		if (npcId > 0) {
			familiar = new Pet(player, details, itemId, npcId);
			((Pet) familiar).setHasWarned(hasWarned);
			if (hunger != -1) ((Pet) familiar).getDetails().setHunger(hunger);
			if (growth != -1) ((Pet) familiar).getDetails().setGrowth(growth);
			if (deleteItem) {
				player.animate(new Animation(827));
				if (!player.getInventory().remove(item, true)) {
					return false;
				}
			}
			if (morph) {
				morphFamiliar(location);
			} else {
				spawnFamiliar();
			}
			return true;
		}
		return true;
	}

	/**
	 * Morphs the current familiar.
	 * @param location the location.
	 */
	public void morphFamiliar(Location location) {
		familiar.init(location, false);
		player.getInterfaceManager().openTab(new Component(662));
		player.getInterfaceManager().setViewedTab(7);
	}
	
	/**
	 * Spawns the current familiar.
	 */
	public void spawnFamiliar() {
		familiar.init();
		player.getInterfaceManager().openTab(new Component(662));
		player.getInterfaceManager().setViewedTab(7);
	}

	/**
	 * Makes the pet eat.
	 * @param foodId The food item id.
	 * @param npc The pet NPC.
	 */
	public void eat(int foodId, Pet npc) {
		if (npc != familiar) {
			player.getPacketDispatch().sendMessage("This isn't your pet!");
			return;
		}
		Pet pet = (Pet) familiar;
		Pets pets = Pets.forId(pet.getItemId());
		if (pets == null) {
			return;
		}
		for (int food : pets.getFood()) {
			if (food == foodId) {
				player.getInventory().remove(new Item(foodId));
				player.getPacketDispatch().sendMessage("Your pet happily eats the " + ItemDefinition.forId(food).getName() + ".");
				player.animate(new Animation(827));
				npc.getDetails().updateHunger(-15.0);
				return;
			}
		}
		player.getPacketDispatch().sendMessage("Nothing interesting happens.");
	}

	/**
	 * Picks up a pet.
	 */
	public void pickup() {
		if (player.getInventory().freeSlots() == 0) {
			player.getPacketDispatch().sendMessage("You don't have enough room in your inventory.");
			return;
		}
		Pet pet = ((Pet) familiar);
		if (player.getInventory().add(new Item(pet.getItemId()))) {
			player.animate(Animation.create(827));
			player.getFamiliarManager().dismiss();
		}
	}

	/**
	 * Adjusts the battle state.
	 * @param state the state.
	 */
	public void adjustBattleState(final BattleState state) {
		if (!hasFamiliar()) {
			return;
		}
		familiar.adjustPlayerBattle(state);
	}

	/**
	 * Gets a boost from a familiar.
	 * @param skill the skill.
	 * @return the boosted level.
	 */
	public int getBoost(int skill) {
		if (!hasFamiliar()) {
			return 0;
		}
		return familiar.getBoost(skill);
	}

	/**
	 * Checks if the player has an active familiar.
	 * @return {@code True} if so.
	 */
	public boolean hasFamiliar() {
		return familiar != null;
	}

	/**
	 * Checks if the player has an active familiar and is a pet.
	 * @return {@code True} if so.
	 */
	public boolean hasPet() {
		return hasFamiliar() && familiar instanceof Pet;
	}

	/**
	 * Dismisses the familiar.
	 */
	public void dismiss() {
		if (hasFamiliar()) {
			familiar.dismiss();
		}
	}

	/**
	 * Adds pet details for a new pet to that pet's stack.
	 * @param itemId The item id of the pet.
	 * @param details The new pet details.
	 */
	public void addDetails(int itemId, PetDetails details) {
		petDetails.get(itemId).add(details);
	}

	/**
	 * Removes the details for this pet.
	 * @param itemId The item id of the pet.
	 */
	public void removeDetails(int itemId) {
		int last = petDetails.get(itemId).size() - 1;
		if (last >= 0) {
			petDetails.get(itemId).remove(last);
		}
	}

	/**
	 * Checks if it's the owner of a familiar.
	 * @param familiar the familiar
	 * @return {@code True} if so.
	 */
	public boolean isOwner(Familiar familiar) {
		if (!hasFamiliar()) {
			return false;
		}
		if (this.familiar != familiar) {
			player.getPacketDispatch().sendMessage("This is not your familiar.");
			return false;
		}
		return true;
	}

	/**
	 * Sets a config value.
	 * @param value the value.
	 */
	public void setConfig(int value) {
		int current = getVarp(player, 1160);
		int newVal = current + value;
		setVarp(player, 1160, newVal);
	}

	/**
	 * Gets the familiar.
	 * @return The familiar.
	 */
	public Familiar getFamiliar() {
		return familiar;
	}

	/**
	 * Sets the familiar.
	 * @param familiar The familiar to set.
	 */
	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	/**
	 * Gets the familiars.
	 * @return The familiars.
	 */
	public static Map<Integer, Familiar> getFamiliars() {
		return FAMILIARS;
	}

	/**
	 * Gets the usingSummoning.
	 * @return The usingSummoning.
	 */
	public boolean isUsingSummoning() {
		return hasPouch || (hasFamiliar() && !hasPet());
	}

	/**
	 * Gets the hasPouch.
	 * @return The hasPouch.
	 */
	public boolean isHasPouch() {
		return hasPouch;
	}

	/**
	 * Sets the hasPouch.
	 * @param hasPouch The hasPouch to set.
	 */
	public void setHasPouch(boolean hasPouch) {
		this.hasPouch = hasPouch;
	}

	/**
	 * Gets the summoningCombatLevel.
	 * @return The summoningCombatLevel.
	 */
	public int getSummoningCombatLevel() {
		return summoningCombatLevel;
	}

	/**
	 * Sets the summoningCombatLevel.
	 * @param summoningCombatLevel The summoningCombatLevel to set.
	 */
	public void setSummoningCombatLevel(int summoningCombatLevel) {
		this.summoningCombatLevel = summoningCombatLevel;
	}


	public Map<Integer, ArrayList<PetDetails>> getPetDetails() {
		return petDetails;
	}
}
