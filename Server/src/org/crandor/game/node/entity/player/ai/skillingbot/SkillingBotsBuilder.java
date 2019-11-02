package org.crandor.game.node.entity.player.ai.skillingbot;

import java.util.ArrayList;

import org.crandor.game.container.impl.EquipmentContainer;
import org.crandor.game.content.global.tutorial.CharacterDesign;
import org.crandor.game.content.skill.Skills;
import org.crandor.game.node.entity.player.ai.AIPlayer;
import org.crandor.game.node.entity.player.link.appearance.Gender;
import org.crandor.game.node.item.Item;
import org.crandor.game.world.map.Location;
import org.crandor.game.world.repository.Repository;
import org.crandor.tools.RandomFunction;

public final class SkillingBotsBuilder extends AIPlayer {

	public SkillingBotsBuilder(String name, Location l) {
		super(name, l);
		// TODO Auto-generated constructor stub
	}
	
	private static SkillingBot generateMiningBot(String name, Location loc, ArrayList<Integer> entrys)
	{
		SkillingBot bot = new SkillingBot(name, loc, Skills.MINING, entrys);
		bot.getAppearance().setGender(RandomFunction.random(3) == 1 ? Gender.FEMALE : Gender.MALE);
		Repository.getPlayers().add(bot);
		bot.init();
		bot.getEquipment().replace(new Item(1265), EquipmentContainer.SLOT_WEAPON);
		return bot;
	}
	
	private static SkillingBot generateWoodcuttingBot(String name, Location loc, ArrayList<Integer> entrys)
	{
		SkillingBot bot = new SkillingBot(name, loc, Skills.WOODCUTTING, entrys);
		bot.getAppearance().setGender(RandomFunction.random(3) == 1 ? Gender.FEMALE : Gender.MALE);
		Repository.getPlayers().add(bot);
		bot.init();
		bot.getEquipment().replace(new Item(1351), EquipmentContainer.SLOT_WEAPON);
		return bot;
	}
	
	private static SkillingBot generateFishingBot(String name, Location loc, ArrayList<Integer> entrys)
	{
		SkillingBot bot = new SkillingBot(name, loc, Skills.FISHING, entrys);
		bot.getAppearance().setGender(RandomFunction.random(3) == 1 ? Gender.FEMALE : Gender.MALE);
        CharacterDesign.randomize(bot, false);
		Repository.getPlayers().add(bot);
		bot.init();
		return bot;
	}
	
	public static void spawnClayBotVarrock(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(15503);
		entrys.add(15505);
		
		SkillingBot bot = generateMiningBot(name, loc, entrys);
		
		bot.getSkills().setLevel(Skills.MINING, 10);
	}
	
	public static void spawnIronBotVarrock(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(11954);
		entrys.add(11955);
		entrys.add(11956);
		
		SkillingBot bot = generateMiningBot(name, loc, entrys);
		
		bot.getSkills().setLevel(Skills.MINING, 25);
	}
	
	public static void spawnSilverBotVarrock(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(11948);
		entrys.add(11949);
		entrys.add(11950);
		
		SkillingBot bot = generateMiningBot(name, loc, entrys);
		
		bot.getSkills().setLevel(Skills.MINING, 30);
	}
	
	public static void spawnTinBotVarrock(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(11957);
		entrys.add(11959);
		entrys.add(11958);
		
		SkillingBot bot = generateMiningBot(name, loc, entrys);
		
		bot.getSkills().setLevel(Skills.MINING, 5);
	}
	
	public static void spawnTinBotLumbridge(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(11933);
		entrys.add(11934);
		entrys.add(11935);
		
		SkillingBot bot = generateMiningBot(name, loc, entrys);
		
		bot.getSkills().setLevel(Skills.MINING, 5);
	}
	
	public static void spawnCopperBotLumbridge(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(11936);
		entrys.add(11937);
		entrys.add(11938);
		
		SkillingBot bot = generateMiningBot(name, loc, entrys);
		
		bot.getSkills().setLevel(Skills.MINING, 5);
	}
	
	public static void spawnOakTreeBotLumbridge(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(1281);
		entrys.add(1278);
		entrys.add(1276);
		
		SkillingBot bot = generateWoodcuttingBot(name, loc, entrys);
		
		bot.getSkills().setLevel(Skills.WOODCUTTING, 25);
		bot.setInteractionRange(25);
	}
	
	public static void spawnDeadTreeBotLumbridge(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(1282);
		entrys.add(1286);
		
		SkillingBot bot = generateWoodcuttingBot(name, loc, entrys);
		
		bot.getSkills().setLevel(Skills.WOODCUTTING, 25);
		bot.setInteractionRange(15);
	}
	
	public static void spawnShrimpFisherLumbridge(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(323);
		entrys.add(326);
		
		SkillingBot bot = generateFishingBot(name, loc, entrys);
		
		bot.getInventory().add(new Item(303));
		// don't drop net
		bot.setFromWhereDoIdrop(1);
		
		bot.getSkills().setLevel(Skills.FISHING, 25);
		bot.setInteractionRange(25);
	}
	
	public static void spawnTroutLumbridge(String name, Location loc)
	{
		ArrayList<Integer> entrys = new ArrayList<Integer>();
		entrys.add(310);
		
		SkillingBot bot = generateFishingBot(name, loc, entrys);
		
		bot.getInventory().add(new Item(309));
		bot.getInventory().add(new Item(314, 20000));
		// don't drop net
		bot.setFromWhereDoIdrop(2);
		
		bot.getSkills().setLevel(Skills.FISHING, 25);
		bot.setInteractionRange(25);
	}
	
	public static void immersiveSpawnsSkillingBots()
	{
		// Varrock Mine
		SkillingBotsBuilder.spawnClayBotVarrock("clay", new Location(3181, 3368));
		SkillingBotsBuilder.spawnSilverBotVarrock("silver", new Location(3181, 3368));
		SkillingBotsBuilder.spawnIronBotVarrock("iron", new Location(3181, 3368));
		SkillingBotsBuilder.spawnTinBotVarrock("tin", new Location(3181, 3368));
		
		// Lumbridge woodcutting
		spawnOakTreeBotLumbridge("Bot", new Location(3227, 3243));
		spawnOakTreeBotLumbridge("Bot", new Location(3186, 3251));
		spawnOakTreeBotLumbridge("Bot", new Location(3188, 3223));
		spawnOakTreeBotLumbridge("Bot", new Location(3162, 3222));
		spawnOakTreeBotLumbridge("Bot", new Location(3162, 3219));
		spawnOakTreeBotLumbridge("Bot", new Location(3152, 3228));
		spawnOakTreeBotLumbridge("Bot", new Location(3146, 3244));
		spawnDeadTreeBotLumbridge("Bot", new Location(3247, 3240));
		
		// Lumbridge mining
		spawnTinBotLumbridge("Bot", new Location(3224, 3147));
		spawnCopperBotLumbridge("Bot", new Location(3224, 3147));
		
		// Lumbridge Fishing
		spawnShrimpFisherLumbridge("Bot", new Location(3242, 3151));
		spawnShrimpFisherLumbridge("Bot", new Location(3238, 3148));
		spawnShrimpFisherLumbridge("Bot", new Location(3245, 3161));
		spawnTroutLumbridge("Bot", new Location(3241, 3242));
	}
	
}
