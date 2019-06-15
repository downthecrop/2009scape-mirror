package org.crandor.game.node.entity.player.ai.minigamebots;

import java.util.ArrayList;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.Node;
import org.crandor.game.node.entity.player.ai.pvmbots.PvMBots;
import org.crandor.game.node.entity.player.link.prayer.*;
import org.crandor.game.world.map.Location;
import org.crandor.net.packet.in.InteractionPacket;
import plugin.activity.pestcontrol.PCLanderZone;

import static plugin.activity.pestcontrol.PestControlHelper.*;

public class PestControlTestBot extends PvMBots {

	private int tick = 0;
	private int movetimer = 0;
	
	public PestControlTestBot(String name, Location l) {
		super(name, legitimizeLocation(l));
		// TODO Auto-generated constructor stub
	}

	private static Location legitimizeLocation(Location l) {
		return PCLanderZone.landerContainsLoc(l) ? new Location(2657, 2639, 0) : l;
	}

	@Override
	public void tick()
	{
		super.tick();
		
		Node test = getClosestNodeWithEntry(15, NOVICE_GANGPLANK);
		if (test != null) {
			int x = test.getLocation().getX();
			int y = test.getLocation().getY();
			if (!this.getLocation().equals(new Location(2660, 2638)))
				InteractionPacket.handleObjectInteraction(this, 0, x, y, test.getId());
		}
		
		ArrayList<Integer> doorEntrys = new ArrayList<Integer>();
		doorEntrys.add(14233);
		doorEntrys.add(14235);
		test = getClosestNodeWithEntry(5, doorEntrys);
		if (!this.inCombat() && test != null) {
			int x = test.getLocation().getX();
			int y = test.getLocation().getY();
			InteractionPacket.handleObjectInteraction(this, 0, x, y, test.getId());
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
	
}
