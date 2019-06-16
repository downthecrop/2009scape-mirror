package org.crandor.game.node.entity.player.ai.minigamebots;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.ai.pvmbots.PvMBots;
import org.crandor.game.node.entity.player.link.prayer.*;
import org.crandor.game.world.map.Location;
import org.crandor.net.packet.in.InteractionPacket;
import org.crandor.tools.RandomFunction;

import java.util.Random;

import static plugin.activity.pestcontrol.PestControlHelper.*;

public class PestControlTestBot extends PvMBots {

	private int tick = 0;
	private int combatMoveTimer = 0;
	private int movetimer = 0;

	private int randomType;
	private BoatInfo myBoat = BoatInfo.NOVICE;

	enum State {
		OUTSIDE_GANGPLANK,
		WAITING_IN_BOAT,
		NPC_COMBAT,
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
			this.setCustomState(String.valueOf(state));

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
				case NPC_COMBAT:
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
			return State.NPC_COMBAT;
		}
		if (outsideGangplankContainsLoc(this.getLocation()))
		{
			return State.OUTSIDE_GANGPLANK;
		}
		return State.GET_TO_PC;
	}

	private void attackNPCs() {
		Node test = getClosestNodeWithEntry(5, GATE_ENTRIES);
		if (!this.inCombat() && test != null) {
			InteractionPacket.handleObjectInteraction(this, 0, test.getLocation(), test.getId());
		}

		//Npc Combat
		if (tick == 0)
		{
			if (!this.inCombat())
				AttackNpcsInRadius(this, 15);
			this.tick = 10;
		}
		else
			this.tick--;

		this.eat(379);
		this.getSkills().setLevel(Skills.PRAYER, 99);
		this.getSkills().setStaticLevel(Skills.PRAYER, 99);
		if (!(this.getPrayer().getActive().contains(PrayerType.PROTECT_FROM_MELEE)))
			this.getPrayer().toggle(PrayerType.PROTECT_FROM_MELEE);

		if (!this.inCombat())
		{
			if (combatMoveTimer <= 0)
			{
				if (this.FindTargets(this, 5) == null)
					this.randomWalk(5, 5);
				this.combatMoveTimer = 5;
			}
		}
	}

	private int insideBoatWalks = 3;
	private void idleInBoat() {
		if (randomType < 15) //He's the type of guy to walk around the boat
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
				//System.out.println("Rare movetimer set on " + this.getName());
			}
			movetimer = RandomFunction.normalPlusWeightRandDist(100, 50);
			//System.out.println("Set movetimer to " + movetimer + " on " + this.getName());
			return;
		}
		if (randomType < 60 && new Random().nextInt(6) == 1) //Missclick the ladder
		{
			movetimer = new Random().nextInt(2);
			this.walkToPosSmart(myBoat.outsideBoatBorder.getWeightedRandomLoc(3));
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
