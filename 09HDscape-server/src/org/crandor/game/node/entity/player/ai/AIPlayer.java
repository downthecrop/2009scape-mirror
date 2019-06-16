package org.crandor.game.node.entity.player.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crandor.game.content.dialogue.DialoguePlugin;
import org.crandor.game.content.global.tutorial.CharacterDesign;
import org.crandor.game.interaction.DestinationFlag;
import org.crandor.game.interaction.MovementPulse;
import org.crandor.game.interaction.Option;
import org.crandor.game.interaction.OptionHandler;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.npc.NPC;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.entity.player.info.PlayerDetails;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Direction;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.RegionManager;
import org.crandor.game.world.map.path.Pathfinder;
import org.crandor.net.packet.in.InteractionPacket;
import org.crandor.plugin.Plugin;
import org.crandor.tools.RandomFunction;
import org.crandor.tools.StringUtils;

/**
 * Represents an <b>A</b>rtificial <b>I</b>ntelligent <b>P</b>layer.
 * @author Emperor
 */
public class AIPlayer extends Player {

	/**
	 * The current UID.
	 */
	private static int currentUID = 0x1;

	/**
	 * The active Artificial intelligent players mapping.
	 */
	private static final Map<Integer, AIPlayer> botMapping = new HashMap<>();

	/**
	 * The aip control dialogue.
	 */
	private static final AIPControlDialogue CONTROL_DIAL = new AIPControlDialogue();

	/**
	 * The AIP's UID.
	 */
	private final int uid;

	/**
	 * The start location of the AIP.
	 */
	private final Location startLocation;

	/**
	 * The username.
	 */
	private String username;

	/**
	 * The player controlling this AIP.
	 */
	private Player controler;
	


	/**
	 * Constructs a new {@code AIPlayer} {@code Object}.
	 * @param name The name of the AIP.
	 * @param l The location.
	 */
	@SuppressWarnings("deprecation")
	public AIPlayer(String name, Location l) {
		super(new PlayerDetails("/aip" + (currentUID + 1) + ":" + name));
		super.setLocation(startLocation = l);
		super.artificial = true;
		super.getDetails().setSession(ArtificialSession.getSingleton());
		this.username = StringUtils.formatDisplayName(name + (currentUID + 1));
		this.uid = currentUID++;
	}

	@Override
	public void init() {
		getProperties().setSpawnLocation(startLocation);
		getInterfaceManager().openDefaultTabs();
		getSession().setObject(this);
		botMapping.put(uid, this);
		super.init();
		getSettings().setRunToggled(true);
        CharacterDesign.randomize(this, false);
		getInteraction().set(new Option("Control", 7).setHandler(new OptionHandler() {
			@Override
			public Plugin<Object> newInstance(Object arg) throws Throwable {
				return null;
			}

			@Override
			public boolean handle(Player p, Node node, String option) {
				DialoguePlugin dial = CONTROL_DIAL.newInstance(p);
				if (dial != null && dial.open(AIPlayer.this)) {
					p.getDialogueInterpreter().setDialogue(dial);
				}
				return true;
			}

			@Override
			public boolean isWalk() {
				return false;
			}

		}));

	}

	/**
	 * Handles the following.
	 * @param e The entity to follow.
	 */
	public void follow(final Entity e) {
		getPulseManager().run(new MovementPulse(this, e, DestinationFlag.FOLLOW_ENTITY) {
			@Override
			public boolean pulse() {
				face(e);
				return false;
			}
		}, "movement");
	}

	public void randomWalkAroundPoint(Location point, int radius)
	{
		Pathfinder.find(this, point.transform(RandomFunction.random(radius, (radius * -1)), RandomFunction.random(radius, (radius * -1)), 0), true, Pathfinder.SMART).walk(this);
	}

	public void randomWalk(int radiusX, int radiusY)
	{
		Pathfinder.find(this, this.getLocation().transform(RandomFunction.random(radiusX, (radiusX * -1)), RandomFunction.random(radiusY, (radiusY * -1)), 0), false, Pathfinder.SMART).walk(this);
	}

	public void walkToPosSmart(int x, int y)
	{
		walkToPosSmart(new Location(x, y));
	}

	public void walkToPosSmart(Location loc) {
		Pathfinder.find(this, loc, true, Pathfinder.SMART).walk(this);
	}

	public void walkPos(int x, int y)
	{
		Pathfinder.find(this, new Location(x, y));
	}

	public boolean checkVictimIsPlayer()
	{
		if (this.getProperties().getCombatPulse().getVictim() != null)
			if (this.getProperties().getCombatPulse().getVictim().isPlayer())
				return true;
		return false;
	}

	public Item getItemById(int id)
	{
		for (int i = 0; i < 28; i++)
		{
			Item item = this.getInventory().get(i);
			if (item != null)
			{
				if (item.getId() == id)
					return item;
			}
		}
		return null;
	}

	private ArrayList<Node> getNodeInRange(int range, int entry)
	{
		int meX = this.getLocation().getX();
		int meY = this.getLocation().getY();
		//int meX2 = this.getLocation().getX();
		//System.out.println("local " + meX + " real x? " + meX2 );
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (NPC npc : RegionManager.getLocalNpcs(this, range)) {
			if (npc.getId() == entry)
				nodes.add(npc);
		}
		for (int x = 0; x < range; x++)
		{
			for (int y = 0; y < range - x; y++)
			{
				Node node = RegionManager.getObject(0, meX + x, meY + y);
				if (node != null)
					if (node.getId() == entry)
						nodes.add(node);
				Node node2 = RegionManager.getObject(0, meX + x, meY - y);
				if (node2 != null)
					if (node2.getId() == entry)
						nodes.add(node2);
				Node node3 = RegionManager.getObject(0, meX - x, meY + y);
				if (node3 != null)
					if (node3.getId() == entry)
						nodes.add(node3);
				Node node4 = RegionManager.getObject(0, meX - x, meY - y);
				if (node4 != null)
					if (node4.getId() == entry)
						nodes.add(node4);
			}
		}
		return nodes;
	}

	private ArrayList<Node> getNodeInRange(int range, List<Integer> entrys)
	{
		int meX = this.getLocation().getX();
		int meY = this.getLocation().getY();
		//int meX2 = this.getLocation().getX();
		//System.out.println("local " + meX + " real x? " + meX2 );
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (NPC npc : RegionManager.getLocalNpcs(this, range)) {
		    if (entrys.contains(npc.getId()))
		    	nodes.add(npc);
		}
		for (int x = 0; x < range; x++)
		{
			for (int y = 0; y < range - x; y++)
			{
				Node node = RegionManager.getObject(0, meX + x, meY + y);
				if (node != null)
					if (entrys.contains(node.getId()))
						nodes.add(node);
				Node node2 = RegionManager.getObject(0, meX + x, meY - y);
				if (node2 != null)
					if (entrys.contains(node2.getId()))
						nodes.add(node2);
				Node node3 = RegionManager.getObject(0, meX - x, meY + y);
				if (node3 != null)
					if (entrys.contains(node3.getId()))
						nodes.add(node3);
				Node node4 = RegionManager.getObject(0, meX - x, meY - y);
				if (node4 != null)
					if (entrys.contains(node4.getId()))
						nodes.add(node4);
			}
		}
		return nodes;
	}

	public Node getClosestNodeWithEntryAndDirection(int range, int entry, Direction direction)
	{
		ArrayList<Node> nodeList = getNodeInRange(range, entry);
		if (nodeList.isEmpty())
		{
			//System.out.println("nodelist empty");
			return null;
		}
		Node node = getClosestNodeinNodeListWithDirection(nodeList, direction);
		return node;
	}

	public Node getClosestNodeWithEntry(int range, int entry)
	{
		ArrayList<Node> nodeList = getNodeInRange(range, entry);
		if (nodeList.isEmpty())
		{
			//System.out.println("nodelist empty");
			return null;
		}
		Node node = getClosestNodeinNodeList(nodeList);
		return node;
	}

	public Node getClosestNodeWithEntry(int range, List<Integer> entrys)
	{
		ArrayList<Node> nodeList = getNodeInRange(range, entrys);
		if (nodeList.isEmpty())
		{
			//System.out.println("nodelist empty");
			return null;
		}
		Node node = getClosestNodeinNodeList(nodeList);
		return node;
	}

	public Node getClosesCreature(int radius) {
		int distance = radius + 1;
		Node npcReturn = null;
		for (NPC npc : RegionManager.getLocalNpcs(this, radius)) {
			double distanceToNpc = npc.getLocation().getDistance(this.getLocation());
			if ((distanceToNpc) < distance) {
				distance = (int) distanceToNpc;
				npcReturn = npc;
			}
		}
		return npcReturn;
	}

	public Node getClosesCreature(int radius, int entry) {
		int distance = radius + 1;
		Node npcReturn = null;
		for (NPC npc : RegionManager.getLocalNpcs(this, radius)) {
			double distanceToNpc = npc.getLocation().getDistance(this.getLocation());
			if (npc.getId() == entry) {
				if ((distanceToNpc) < distance) {
					distance = (int) distanceToNpc;
					npcReturn = npc;
				}
			}
		}
		return npcReturn;
	}

	public Node getClosesCreature(int radius, ArrayList<Integer> entrys) {
		int distance = radius + 1;
		Node npcReturn = null;
		for (NPC npc : RegionManager.getLocalNpcs(this, radius)) {
			double distanceToNpc = npc.getLocation().getDistance(this.getLocation());
			if (entrys.contains(npc.getId())) {
				if ((distanceToNpc) < distance) {
					distance = (int) distanceToNpc;
					npcReturn = npc;
				}
			}
		}
		return npcReturn;
	}

	private Node getClosestNodeinNodeListWithDirection(ArrayList<Node> nodes, Direction direction)
	{
		if (nodes.isEmpty())
		{
			//System.out.println("nodelist empty");
			return null;
		}

		double distance = 0;
		Node nodeReturn = null;
		for (Node node : nodes)
		{
			double nodeDistance = this.getLocation().getDistance(node.getLocation());
			if ((nodeReturn == null || nodeDistance < distance) && node.getDirection() == direction)
			{
				distance = nodeDistance;
				nodeReturn = node;
			}
		}
		return nodeReturn;
	}

	private Node getClosestNodeinNodeList(ArrayList<Node> nodes)
	{
		if (nodes.isEmpty())
		{
			//System.out.println("nodelist empty");
			return null;
		}

		double distance = 0;
		Node nodeReturn = null;
		for (Node node : nodes)
		{
			double nodeDistance = this.getLocation().getDistance(node.getLocation());
			if (nodeReturn == null || nodeDistance < distance)
			{
				distance = nodeDistance;
				nodeReturn = node;
			}
		}
		return nodeReturn;
	}

	@Override
	public void clear() {
		botMapping.remove(uid);
		super.clear();
	}

	@Override
	public void reset() {
		if (getPlayerFlags().isUpdateSceneGraph()) {
			getPlayerFlags().setLastSceneGraph(getLocation());
		}
		super.reset();
	}

	/**
	 * Gets the UID.
	 * @return the UID.
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * Deregisters an AIP.
	 * @param uid The player's UID.
	 */
	public static void deregister(int uid) {
		if (!botMapping.containsKey(uid)) {
			System.err.println("Could not deregister AIP#" + uid + ": UID not added to the mapping!");
			return;
		}
		AIPlayer player = botMapping.get(uid);
		if (player != null) {
			player.clear();
		}
	}

	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the AIP for the given UID.
	 * @param uid The UID.
	 * @return The AIPlayer.
	 */
	public static AIPlayer get(int uid) {
		return botMapping.get(uid);
	}

	/**
	 * @return the startLocation.
	 */
	public Location getStartLocation() {
		return startLocation;
	}

	/**
	 * Gets the controler.
	 * @return The controler.
	 */
	public Player getControler() {
		return controler;
	}

	/**
	 * Sets the controler.
	 * @param controler The controler to set.
	 */
	public void setControler(Player controler) {
		this.controler = controler;
	}


	public void interact(Node n)
	{
		InteractionPacket.handleObjectInteraction(this, 0, n.getLocation(), n.getId());
	}
}