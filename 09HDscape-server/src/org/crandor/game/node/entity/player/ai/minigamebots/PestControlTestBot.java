package org.crandor.game.node.entity.player.ai.minigamebots;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.ai.pvmbots.PvMBots;
import org.crandor.game.node.entity.player.link.prayer.*;
import org.crandor.game.world.map.Location;
import org.crandor.net.packet.in.InteractionPacket;

import static plugin.activity.pestcontrol.PestControlHelper.*;

public class PestControlTestBot extends PvMBots {

	private int tick = 0;
	private int movetimer = 0;

	enum State {
		OUTSIDE_GANGPLANK,
		WAITING_IN_BOAT,
		NPC_COMBAT,
		GET_TO_PC
	}

	public PestControlTestBot(String name, Location l) {
		super(name, legitimizeLocation(l));
		// TODO Auto-generated constructor stub
	}

	private static Location legitimizeLocation(Location l) {
		return landerContainsLoc(l) ? new Location(2657, 2639, 0) : l;
	}

	@Override
	public void tick()
	{
		super.tick();

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
			if (movetimer == 0)
			{
				if (this.FindTargets(this, 5) == null)
					this.randomWalk(5, 5);
				this.movetimer = 5;
			}
			else
				movetimer --;
		}
	}

	private void idleInBoat() {
	}

	private void enterBoat() {
		Node test = getClosestNodeWithEntry(15, NOVICE_GANGPLANK);
		if (test != null) {
			if (!this.getLocation().equals(new Location(2660, 2638)))
				InteractionPacket.handleObjectInteraction(this, 0, test.getLocation(), test.getId());
		} else {
			System.out.println("Warning: " + this.getName() + " can't find the gangplank!");
		}
	}

	private void getToPC() {
		Node test = getClosestNodeWithEntry(15, NOVICE_GANGPLANK);
		if (test == null)
		{
			this.teleport(PestControlIslandLocation);
		} else {
			InteractionPacket.handleObjectInteraction(this, test);
		}
	}

}
