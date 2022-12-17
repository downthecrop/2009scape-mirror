package core.game.node.entity.npc.drop;

import static api.ContentAPIKt.*;
import core.cache.def.impl.NPCDefinition;
import core.game.content.global.Bones;
import core.game.node.entity.Entity;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.GroundItem;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;
import core.game.world.map.Location;
import core.game.world.map.RegionManager;
import core.tools.RandomFunction;
import core.tools.StringUtils;
import rs09.game.ai.AIPlayer;
import rs09.game.ai.AIRepository;
import rs09.game.ai.general.GeneralBotCreator;
import rs09.game.content.global.NPCDropTable;
import rs09.game.ge.GrandExchange;
import rs09.game.world.repository.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds and handles the NPC drop tables.
 * @author Emperor
 */
public final class NPCDropTables {

	/**
	 * The drop rates (0=common, 1=uncommon, 2=rare, 3=very rare).
	 */
	public static final int[] DROP_RATES = { 750, 150, 15, 5 };
	
	/**
	 * The npcs that will display drop messages
	 */
	public static final int[] MESSAGE_NPCS = { 50, 7133, 7134, 2881, 2882, 2883, 3200, 3340, 6247, 6203, 6260, 6222, 2745, 1160, 8133, 8610, 8611, 8612, 8613, 8614, 6204, 6206, 6208, 6261, 6263, 6265, 6223, 6225, 6227 };

	public final NPCDropTable table = new NPCDropTable();

	/**
	 * The NPC definitions.
	 */
	private final NPCDefinition def;

	/**
	 * The main drop table size.
	 */
	private int mainTableSize;

	/**
	 * The mod rate used with this table.
	 */
	private double modRate;

	/**
	 * Constructs a new {@code NPCDropTables} {@code Object}.
	 * @param def The NPC definitions.
	 */
	public NPCDropTables(NPCDefinition def) {
		this.def = def;
	}

	/**
	 * Handles the dropping.
	 * @param npc The NPC dropping the loot.
	 * @param looter The entity gaining the loot.
	 */
	public void drop(NPC npc, Entity looter) {
		Player p = looter instanceof Player ? (Player) looter : null;
		table.roll().forEach(item -> createDrop(item,p,npc,npc.getDropLocation()));
	}

	/**
	 * Creates a dropped item.
	 * @param item The item to drop.
	 * @param player The player getting the loot (or null).
	 * @param npc the npc.
	 * @param l The location of the NPC dropping the loot.
	 */
	public void createDrop(Item item, Player player, NPC npc, Location l) {
		if (item == null || item.getId() == 0 || l == null || item.getName().equals("null") || player == null) {
			return;
		}
		if (handleBoneCrusher(player, item)) {
			return;
		}
		if (item.getId() == RareDropTable.SLOT_ITEM_ID){
			item = RareDropTable.retrieve();
		}
		if (item.getId() == UncommonSeedDropTable.SLOT_ITEM_ID){
			item = UncommonSeedDropTable.retrieve();
		}
		if (item.getId() == HerbDropTable.SLOT_ITEM_ID){
			item = HerbDropTable.retrieve();
		}
		if (item.getId() == GemDropTable.SLOT_ITEM_ID){
			item = GemDropTable.retrieve();
		}
		if (item.getId() == RareSeedDropTable.SLOT_ITEM_ID){
			item = RareSeedDropTable.retrieve();
		}
		if (item.getId() == AllotmentSeedDropTable.SLOT_ITEM_ID){
			item = AllotmentSeedDropTable.retrieve();
		}
		if (item.getId() == 995 && player.getBank().hasSpaceFor(item) && ( player.getGlobalData().isEnableCoinMachine() )) {
			item = new Item(995, (int) (item.getAmount() + (item.getAmount() * 0.25)));
			player.getBank().add(item);
			player.sendMessage("<col=3498db> " + item.getAmount() + " coins were sent to your bank.");
			return;
		}
		if (item.hasItemPlugin() && player != null) {
			if (!item.getPlugin().createDrop(item, player, npc, l)) {
				return;
			}
			item = item.getPlugin().getItem(item, npc);
		}
		if (!item.getDefinition().isStackable() && item.getAmount() > 1) {
			for (int i = 0; i < item.getAmount(); i++) {
				GroundItemManager.create(new Item(item.getId()), l, player);
			}
			return;
		}
        announceIfRare(player, item);
		if(item.getId() == 6199 && player instanceof Player){
		    player.sendMessage("<col=990000>A mystery box has fallen on the ground.</col>");
		}
		sendDropMessage(player, npc.getId(), item);
		if (player == null) {
			if (item != null) {
				GroundItemManager.create(item, l);
			}
		} else {
			GroundItem groundItem = GroundItemManager.create(item, l, getLooter(player, npc, item));
			if(player instanceof AIPlayer) {
				AIRepository.addItem(groundItem);
			}
			GeneralBotCreator.BotScriptPulse pulse = player.getAttribute("botting:script",null);
			if(pulse != null && pulse.isRunning()){
				List<GroundItem> items = player.getAttribute("botting:drops",new ArrayList<GroundItem>());
				items.add(groundItem);
				player.setAttribute("botting:drops",items);
			}
		}
	}

	/**
	 * Gets the looting player.
	 * @param player the player.
	 * @param item the item.
	 * @return the player.
	 */
	public Player getLooter(Player player, NPC npc, Item item) {
		int itemId = item.getDefinition().isUnnoted() ? item.getId() : item.getNoteChange();
		if (player != null && npc.getProperties().isMultiZone() && (item.getDefinition().isTradeable() || item.getName().endsWith("charm")) && player.getCommunication().getClan() != null && player.getCommunication().isLootShare() && player.getCommunication().getLootRequirement().ordinal() >= player.getCommunication().getClan().getLootRequirement().ordinal() && !player.getIronmanManager().isIronman()) {
			Player looter = player;
			List<Player> players = RegionManager.getLocalPlayers(npc, 16);
			List<Player> looters = new ArrayList<>(20);
			for (Player p : players) {
				if (p != null && p.getCommunication().getClan() != null && p.getCommunication().getClan() == player.getCommunication().getClan() && p.getCommunication().isLootShare() && p.getCommunication().getLootRequirement().ordinal() >= p.getCommunication().getClan().getLootRequirement().ordinal() && npc.getImpactHandler().getImpactLog().containsKey(p)) {
					looters.add(p);
				}
			}
			if (looters.size() > 1) {
				int highestLsp = 0;
				for (Player p : looters) {
					if (p.getGlobalData().getLootSharePoints() > highestLsp && RandomFunction.random(10) >= 2) {
						highestLsp = p.getGlobalData().getLootSharePoints();
						looter = p;
					}
				}
				player.sendMessage(player.getInterfaceManager().isResizable()+"");
				int price = item.getName().endsWith("charm") ? 100 : GrandExchange.getRecommendedPrice(itemId, false);
				looter.getGlobalData().setLootSharePoints(looter.getGlobalData().getLootSharePoints() - (price) + ((price / looters.size())));
				looter.sendMessage((player.getInterfaceManager().isResizable() ? "<col=32CD32>" : "<col=009900>") + "You received: " + item.getAmount() + " " + item.getName());
				for (Player p : looters) {
					if (p != looter) {
						p.sendMessage((player.getInterfaceManager().isResizable() ? "<col=32CD32>" : "<col=009900>") + looter.getUsername() + " received: " + item.getAmount() + " " + item.getName());
						p.getGlobalData().setLootSharePoints(p.getGlobalData().getLootSharePoints() + price / looters.size());
					}
				}
			}
			return looter;
		}
		return player;
	}

	/**
	 * Sends the drop to players within the area.
	 * @param killer the killer.
	 * @param npcId the npcId.
	 * @param item the item.
	 */
	private void sendDropMessage(Player killer, int npcId, Item item) {
		if (!item.getName().toLowerCase().contains("bone") && !item.getName().toLowerCase().contains("ashes")) {
			for (int id : MESSAGE_NPCS) {
				if (npcId == id) {
					for (Player player : Repository.getPlayers()) {
						if (player.getLocation().getDistance(killer.getLocation()) <= 10) {
							player.sendMessage((player.getInterfaceManager().isResizable() ? "<col=32CD32>" : "<col=005F00>") + (player == killer ? "You received: " : killer.getUsername() + " received a drop: ") + (item.getAmount() > 1 ? StringUtils.getFormattedNumber(item.getAmount()) + " x " + item.getName() : item.getName()) + "</col>");
						}
					}
				}
			}
		}
	}
	
	/**
	 * Handles the bone crusher perk.
	 * @param player The player
	 * @param item The item
	 * @return true if successfully added experience.
	 */
	private boolean handleBoneCrusher(Player player, Item item) {
		Bones bone = Bones.forId(item.getId());
		if (bone == null) {
			return false;
		}
		if (!player.getGlobalData().isEnableBoneCrusher()) {
			return false;
		}
		player.getSkills().addExperience(Skills.PRAYER, item.getAmount() * bone.getExperience());
		return true;		
	}
	
	/**
	 * Gets the ratio for stabilizing NPC combat difficulty & drop rates.
	 * @return The ratio.
	 */
	public double getStabilizerRatio() {
		return (1 / (1 + def.getCombatLevel())) * 10;
	}

	/**
	 * Gets the modRate.
	 * @return The modRate.
	 */
	public double getModRate() {
		return modRate;
	}

	/**
	 * Sets the modRate.
	 * @param modRate The modRate to set.
	 */
	public void setModRate(double modRate) {
		this.modRate = modRate;
	}

}
