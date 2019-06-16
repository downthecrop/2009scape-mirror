package org.crandor.game.node.entity.player.ai.minigamebots.pestcontrol;

import org.crandor.game.node.Node;
import org.crandor.game.node.entity.Entity;
import org.crandor.game.node.entity.player.ai.pvmbots.PvMBots;
import org.crandor.game.world.map.Location;
import org.crandor.net.packet.in.InteractionPacket;
import org.crandor.tools.RandomFunction;

import java.util.List;
import java.util.Random;

import static plugin.activity.pestcontrol.PestControlHelper.*;

public class PestControlTestBot extends PvMBots {

	public int tick = 0;
	public int combatMoveTimer = 0;
	public boolean justStartedGame = true;
	public int movetimer = 0;

	public int randomType;
	public boolean openedGate;
	private BoatInfo myBoat = BoatInfo.NOVICE;

	private CombatState combathandler = new CombatState(this);

	enum State {
		OUTSIDE_GANGPLANK,
		WAITING_IN_BOAT,
		PLAY_GAME,
		GET_TO_PC
	}

	public PestControlTestBot(String name, Location l) {
		super(name, legitimizeLocation(l));
		randomType = new Random().nextInt(100);
	}

	private static Location legitimizeLocation(Location l) {
		return landerContainsLoc(l) ? new Location(2657, 2639, 0) : l;
	}

	@Override
	public void tick()
	{
		super.tick();
		movetimer --;

		if (movetimer <= 0)
		{
		    movetimer = 0;
			State state = getState();
			this.setCustomState(String.valueOf(state) + movetimer);

			switch (state)
			{
				case GET_TO_PC:
					getToPC();
					break;
				case OUTSIDE_GANGPLANK:
					enterBoat();
					break;
				case WAITING_IN_BOAT:
					idleInBoat();
					break;
				case PLAY_GAME:
					attackNPCs();
					break;
			}
		}
	}

	private State getState() {
		if (landerContainsLoc(this.getLocation()))
		{
			return State.WAITING_IN_BOAT;
		}
		if (isInPestControlInstance(this))
		{
			return State.PLAY_GAME;
		}
		if (outsideGangplankContainsLoc(this.getLocation()))
		{
			return State.OUTSIDE_GANGPLANK;
		}
		return State.GET_TO_PC;
	}

	private void attackNPCs() {
		List<Entity> creatures = FindTargets(this, 15);
		if (creatures == null || creatures.isEmpty())
		{
			this.setCustomState("Going to portals");
			combathandler.goToPortals();
		} else {
			this.setCustomState("Fighting NPCs");
			combathandler.fightNPCs();
		}

		/*
		if (randomType < 20)
		{
			this.getUpdateMasks().register(new ChatFlag(new ChatMessage(this, "Meee", 0, 0)));
		}
		Node test = getClosestNodeWithEntry(5, GATE_ENTRIES);
		if (!this.inCombat() && test != null) {
			InteractionPacket.handleObjectInteraction(this, 0, test.getLocation(), test.getId());
		}

		*/
	}

	private int insideBoatWalks = 3;
	private void idleInBoat() {
		justStartedGame = true;
		openedGate = false;
		if (randomType < 35) //He's the type of guy to walk around the boat
		{
			if (new Random().nextInt(insideBoatWalks) <= 1)
			{
				insideBoatWalks *= 1.5;
				if (new Random().nextInt(7) == 1)
				{
					this.walkToPosSmart(new Location(2660, 2638));
				} else {
					this.walkToPosSmart(myBoat.boatBorder.getRandomLoc());
				}
			}
			if (new Random().nextInt(3) == 1)
			{
				insideBoatWalks += 2;
			}
		}
	}

	private void enterBoat() {
		if (new Random().nextInt(3) <= 1) //Don't join instantly
		{
			return;
		}
		if (randomType > 20 && new Random().nextInt(4) == 0) //Idle outside ladder
		{
			if (new Random().nextInt(16) == 0)
			{
				this.walkToPosSmart(myBoat.outsideBoatBorder.getRandomLoc());
				movetimer += RandomFunction.normalPlusWeightRandDist(400, 200);
			}
			movetimer = RandomFunction.normalPlusWeightRandDist(100, 50);
			return;
		}
		if (randomType < 60 && new Random().nextInt(6) == 1) //Missclick the ladder
		{
			movetimer = new Random().nextInt(2);
			this.walkToPosSmart(myBoat.outsideBoatBorder.getWeightedRandomLoc(2));
			return;
		}
		Node test = getClosestNodeWithEntry(15, myBoat.ladderId);
		InteractionPacket.handleObjectInteraction(this, 0, test.getLocation(), test.getId());
		insideBoatWalks = 3;
	}

	private void getToPC() {
		Node test = getClosestNodeWithEntry(25, myBoat.ladderId);
		if (test == null)
		{
			this.teleport(PestControlIslandLocation);
		} else {
			InteractionPacket.handleObjectInteraction(this, test);
		}
	}

}
