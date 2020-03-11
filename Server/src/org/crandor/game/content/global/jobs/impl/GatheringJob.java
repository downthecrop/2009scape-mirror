/**
 * 
 */
package org.crandor.game.content.global.jobs.impl;

import org.crandor.cache.def.impl.ItemDefinition;
import org.crandor.game.content.global.jobs.Job;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.node.item.Item;
import org.crandor.tools.RandomFunction;

/**
 * Represents a gathering job, eg fishing woodcutting and such.
 * @author jamix77
 *
 */
public class GatheringJob extends Job {
	
	private int itemId;
	private int amount;
	private int originalAmount;

	/**
	 * Constructs a new @{Code GatheringJob} object.
	 */
	public GatheringJob(int employer, Player player,int amount, int itemId) {
		super(employer,player);
		this.itemId = itemId;
		this.setAmount(amount);
		originalAmount = amount;
	}

	@Override
	public
	boolean reward() {
		if (getPlayer().getInventory().getAmount(itemId) >= getAmount()) {
			getPlayer().getInventory().remove(new Item(itemId,getAmount()));
			setAmount(0);
			getPlayer().getInventory().add(new Item(995,originalAmount * 25 * ( ItemDefinition.forId(itemId).getValue() + 1)));
			return true;
		} else {
			setAmount(getAmount() - getPlayer().getInventory().getAmount(itemId));
			getPlayer().getInventory().remove(new Item(itemId,getPlayer().getInventory().getAmount(itemId)));
			getPlayer().sendMessage("You still have " + getAmount() + " " + new Item(itemId).getName() + "left to gather.");
		}
		return false;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getItemId() {
		return itemId;
	}

	
	/**
	 * Represents the jobs that require gathering
	 * @author jamix77
	 *
	 */
	public enum GatheringJobs {
		LOG(20,28, 1, 1511, Skills.WOODCUTTING),
		COWHIDES(20,28,1, 1739,0),
		OAK(22,28,15, 1521,Skills.WOODCUTTING),
		WATER_RUNE(20,28,5, 555,Skills.RUNECRAFTING),
		EARTH_RUNE(20,28,9, 557,Skills.RUNECRAFTING),
		FIRE_RUNE(20,28,14, 554,Skills.RUNECRAFTING),
		AIR_RUNE(20,28,1, 556,Skills.RUNECRAFTING),
		RUNE_ESS(20,28,1, 1436,Skills.MINING),
		BALL_OF_WOOL(20,28,1, 1759,Skills.CRAFTING),
		LEATHER_GLOVE(20,28,1, 1059,Skills.CRAFTING),
		WILLOW(22,28,30, 1519,Skills.WOODCUTTING),
		LEATHER_BOOT(24,24,1, 1061,Skills.CRAFTING),
		BRONZE_DAGGER(24,24,1,1205,Skills.SMITHING),
		BRONZE_HELMS(22,22,1,1139, Skills.SMITHING),
		BRONZE_FULL_HELM(10,10,7,1155,Skills.SMITHING),
		BRONZE_CHAINBODY(10,10,11,1103,Skills.SMITHING),
		BRONZE_2H(10,10,14,1307,Skills.SMITHING),
		BRONZE_SCIM(10,10,5,1321,Skills.SMITHING),
		BRONZE_PLATEBODY(9,10,18,1117,Skills.SMITHING),
		BRONZE_PLATELEGS(9,10,16,1075,Skills.SMITHING),
		BRONZE_PLATESKIRTS(9,10,16,1087,Skills.SMITHING),
		BRONZE_WARHAMMER(9,9,9,1337,Skills.SMITHING),
		IRON_DAGGER(13,13,15,1203,Skills.SMITHING),
		IRON_CHAINBODY(10,10,26,1101,Skills.SMITHING),
		IRON_2H(9,9,29,1309, Skills.SMITHING),
		STEEL_BATTLEAXE(9,9, 40,1365,Skills.SMITHING ),
		STEEL_SCIMITAR(9,9,35,1325, Skills.SMITHING),
		STEEL_PLATEBODY(9,10,48,1119, Skills.SMITHING),
		STEEL_WARHAMMER(9,10,39,1339,Skills.SMITHING),
		LEATHER_CHAPS(22,28,18,1095,Skills.CRAFTING),
		LEATHER_BODY(22,28,14, 1129, Skills.CRAFTING),
		LEATHER_COWL(22,28,9,1167,Skills.CRAFTING),
		LEATHER_COIF(22,28,38,1169,Skills.CRAFTING),
		RAW_SHRIMP(22,28,1,317,Skills.FISHING),
		COOKED_SHRIMP(22,28,1,315,Skills.COOKING),
		RAW_CRAYFISH(22,28,1,13435,Skills.FISHING),
		COOKED_CRAYFISH(22,28,1,13433,Skills.COOKING),
		RAW_TROUT(22,28,20,335,Skills.FISHING),
		COOKED_TROUT(22,28,15,333,Skills.COOKING),
		RAW_SALMON(22,28,30,331,Skills.FISHING),
		COOKED_SALMON(22,28,25,329,Skills.COOKING),
		CAKE(12,16,40,1891,Skills.COOKING),
		MEAT_PIE(12,16,20,2327,Skills.COOKING),
		PLAIN_PIZZA(12,16,35,2289,Skills.COOKING),
		MEAT_PIZZA(12,16,45,2293,Skills.COOKING),
		ANCHOVY_PIZZA(12,16,55,2297,Skills.COOKING),
		REDBERRY_PIE(12,16,10,2325,Skills.COOKING),
		COPPER_ORE(22,28,1,436,Skills.MINING),
		TIN_ORE(23,26,1,438,Skills.MINING),
		IRON_ORE(24,24,15,440,Skills.MINING),
		SILVER_ORE(22,28,20,442, Skills.MINING),
		COAL(22,26,30,453,Skills.MINING),
		GOLD_ORE(22,24,40,444, Skills.MINING),
		BRONZE_BAR(10,12,1,2349,Skills.SMITHING),
		IRON_BAR(22,28,15,2351,Skills.SMITHING),
		STEEL_BAR(22,28,30,2353,Skills.SMITHING),
		ASHES(26,26,1,592,0);
		
		
		
		
		
		
		
		private final int id;
		private final int upperBound;
		private final int lowerBound;
		private final int lvlReq;
		private final int skillId;
		
		private GatheringJobs(int lowerBound,int upperBound, int lvlReq, int id, int skillId) {
			this.lowerBound = lowerBound;
			this.upperBound = upperBound;
			this.id = id;
			this.lvlReq = lvlReq;
			this.skillId = skillId;
		}

		public int getLvlReq() {
			return lvlReq;
		}

		public int getid() {
			return id;
		}
		
		public int getAmount() {
			return RandomFunction.random(lowerBound, upperBound);
		}


		public int getSkillId() {
			return skillId;
		}
		
		
	}


	public void setItemId(int int1) {
		itemId = int1;
	}
	
	
		
		

}
