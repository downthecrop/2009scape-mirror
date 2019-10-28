package org.crandor.game.node.entity.player.ai.pvmbots;

import org.crandor.game.node.entity.player.ai.AIPlayer;
import org.crandor.game.world.map.Location;

public class LowestBot extends PvMBots{

	public LowestBot(String name, Location l) {
		super(name, l);
		// TODO Auto-generated constructor stub
	}
	
	private int tick = 0;
	
	@Override
	public void tick()
	{
		super.tick();
		
		//Despawn
		if (this.getSkills().getLifepoints() == 0)
			//this.teleport(new Location(500, 500));
			//Despawning not being delayed causes 3 errors in the console
			AIPlayer.deregister(this.getUid());
		
		//Npc Combat
		if (tick == 0)
		{
			if (!this.inCombat())
				AttackNpcsInRadius(this, 5);
			this.tick = 10;
		}
		else 
			this.tick--;
		
		this.eat(329);
		//this.getPrayer().toggle()
	}

}
