package org.crandor.game.node.entity.player.ai.pvmbots;

import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.ai.AIPlayer;
import org.crandor.game.node.entity.player.link.TeleportManager.TeleportType;
import org.crandor.game.node.entity.player.link.prayer.*;
import org.crandor.game.world.GameWorld;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.map.zone.impl.WildernessZone;
import org.crandor.tools.RandomFunction;

public class DragonKiller extends PvMBots{

	private int tick = 0;
	
	public DragonKiller(String name, Location l) {
		super(name, l);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void tick()
	{
		super.tick();
		
		//Despawn
		if (this.getSkills().getLifepoints() == 0 || this.getLocation().equals(new Location(0, 0)))
			//this.teleport(new Location(500, 500));
			//Despawning not being delayed causes 3 errors in the console
			AIPlayer.deregister(this.getUid());
		
		if (this.checkVictimIsPlayer())
		{
			tryEscape();
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
		//this.getPrayer().toggle()
	}
	
	public void tryEscape()
	{
		if (this.isTeleBlocked() == false)
		{
			if (WildernessZone.getWilderness(this) <= 20)
			{
				System.out.println("not Teleblocked");
				this.teleport(new Location(0, 0));
			}
		}
		else 
			return;
		//run away
	}
}
