package core.game.content.dialogue;

import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.link.IronmanMode;
import core.game.world.map.zone.ZoneBorders;
import core.plugin.Initializable;
import core.game.node.entity.player.Player;

import static rs09.tools.DialogueConstKt.END_DIALOGUE;


/**
 * Represents the dialogue plugin used for the hans npc.
 */
@Initializable
public final class HansDialoguePlugin extends DialoguePlugin {

	private int[] timePlayed = new int[3];
	private int joinDateDays;
	private boolean inStartDungeon;

	/**
	 * Constructs a new {@code HansDialoguePlugin} {@code Object}.
	 */
	public HansDialoguePlugin() {
		/**
		 * empty.
		 */
	}

	/**
	 * Constructs a new {@code HansDialoguePlugin} {@code Object}.
	 * @param player the player.
	 */
	public HansDialoguePlugin(Player player) {
		super(player);
	}

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new HansDialoguePlugin(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "Hello. What are you doing here?");
		stage = 0;
		if (new ZoneBorders(2528, 5004, 2520, 4997).insideBorder(player.getLocation())) {
			inStartDungeon = true;
		}
		return true;
	}

	@Override
	public boolean handle(int interfaceId, int buttonId) {

		if (inStartDungeon && stage == 0) {
			stage = 1;
			buttonId = 4;
		}

		switch (stage) {
			case 0:
				interpreter.sendOptions("Select an Option", "I'm looking for whoever is in charge of this place.", "I have come to kill everyone in this castle!", "I don't know. I'm lost. Where am I?", "Have you been here as long as me?");
				stage++;
				break;
			case 1:
				switch (buttonId) {
					case 1:
						interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "Who, the Duke? He's in his study, on the first floor.");
						stage = 50;
						break;
					case 2:
						end();
						//TODO:
						// Face the player and walk away from them (like moon walking?).
						// After a moment, return to normal pathing associated with HansNPC.java
						npc.sendChat("Help! Help!");
						break;
					case 3:
						interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "You are in Lumbridge Castle.");
						stage = 50;
						break;
					case 4:
						interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "I've been patrolling this castle for years!");
						stage = 41;
						break;
				}
				break;
			case 10:
				switch (buttonId) {
					case 1:
						//Have you been here as long as me?
						interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "I've been patrolling this castle for years!");
						stage = 41;
						break;
					case 2:
						npc("Your current XP rate is: " + player.getSkills().experienceMutiplier);
						stage = 11;
						break;
					case 3:
						//About Iron Man Mode...
						if (player.getIronmanManager().isIronman()) {
							npc("Your ironman mode is: " + player.getIronmanManager().getMode().name().toLowerCase());
							stage = 50;
						} else {
							interpreter.sendOptions("Select an Option", "I would like to be an Iron Man.", "What is an Iron Man?", "Go Back...");
							stage = 110;
						}
						break;
					case 4:
						interpreter.sendDialogue("Randoms are now permanently enabled. This option to be removed","at a later date.");
						stage = END_DIALOGUE;
						break;
					case 5: // Go back
						interpreter.sendOptions("Select an Option", "I'm looking for whoever is in charge of this place.", "I have come to kill everyone in this castle!", "I don't know. I'm lost. Where am I?", "More Options...");
						stage = 1;
						break;
				}
				break;

			case 11:
				if (player.getSkills().experienceMutiplier == 5.0) {
					player.newPlayer = player.getSkills().getTotalLevel() < 50;
					options("Change xp rate", "Nevermind.");
					stage++;
				} else {
					npc("Have a great day!");
					stage = 131;
				}
				break;
			case 12:
				switch(buttonId){
					case 1:
						if(player.getAttributes().containsKey("permadeath")){
							options("1.0x", "2.5x", "Stay 5.0x", "(HCIM Only) 10x");
						} else {
							options("1.0x", "2.5x", "Stay 5.0x");
						}
						stage++;
						break;
					case 2:
						npc(FacialExpression.LAUGH, "Haha, alright then!");
						stage = 50;
						break;
				}
				break;
			case 13:
				switch(buttonId){
					case 1:
						if(player.newPlayer) {
							player.getSkills().experienceMutiplier = 1.0;
							stage = 14;
						} else {
							stage = 15;
							break;
						}
						break;
					case 2:
						if(player.newPlayer){
							player.getSkills().experienceMutiplier = 2.5;
							stage = 14;
						} else {
							stage = 15;
						}
						break;
					case 3:
						playerl(FacialExpression.FRIENDLY, "I'd rather stay 5x, thank you.");
						stage = END_DIALOGUE;
						return true;
					case 4:
						if (player.newPlayer) {
							player.getSkills().experienceMutiplier = 10.0;
							stage = 14;
						} else {
							stage = 15;
						}
						break;					
				}
				npc("One moment, please...");
				break;
			case 14:
				npc("Tada, your xp rate is now " + player.getSkills().experienceMutiplier);
				stage = 131;
				break;
			case 15:
				npc("Sorry, only new accounts can select 2.5x.");
				stage = 131;
				break;
				//Have you been here as long as me?
			case 41:
				interpreter.sendDialogues(player, FacialExpression.THINKING, "You must be old then?");
				stage++;
				break;
			case 42:
				interpreter.sendDialogues(npc, FacialExpression.LAUGH, "Haha, you could say I'm quite the veteran of these lands.", "Yes, I've been here a fair while...");
				stage++;
				break;
			case 43: //mixing OSRS here
				interpreter.sendDialogues(player, FacialExpression.ASKING, "Can you tell me how long I've been here?");
				stage++;
				break;
			case 44:
				if (!inStartDungeon) {
					interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "Ahh, I see all the newcomers arriving in Lumbridge, ","fresh-faced and eager for adventure. I remember you...");
					player.sendMessage("Feature not currently available.");
				} else {
					interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "Ahh, I see all the newcomers arriving in Lumbridge, ","fresh-faced and eager for adventure.", "But this is the first time meeting you...");
				}
				stage = 50;
				break;
				//TODO:
			/*case 45:
				getTimePlayed();

				//The text:
				//NOTE: it splits the text in different spots if the hours/minutes/days are 0 (because 0 days sounds weird, so it doesn't show it).

				//You've spent [amount] days, [amount] hours, [amount] minutes in the world (NEXT LINE) since you arrived [amount] days ago.
				//You've spent [amount] (days/hours), [amount] (hours/minutes) in the world since (NEXT LINE) you arrived [amount] days ago.
				//You've spent [amount] (days/hours/minutes) in the world since you arrived (NEXT LINE) [amount] days ago.
			*/

			//Closing Chat
			case 50:
				end();
				break;

			//About Iron Man Mode...
			case 100:
				switch (buttonId) {
					case 1: //no longer want to be iron
						if (player.getSavedData().getActivityData().getHardcoreDeath() == true) {
							interpreter.sendDialogues(npc, FacialExpression.GUILTY, "Sorry, but you've fallen as a Hardcore Iron Man", "already. It would be unfair for those with other", " restrictions if your status were to be removed!");
							stage = 50;
							break;
						}
						if (player.getSkills().getTotalLevel() > 500 || player.getQuestRepository().getPoints() > 10){
							interpreter.sendDialogues(npc, FacialExpression.GUILTY, "Sorry, but you are too far along your journey.", "It would be unfair for those with other", " restrictions if your status were to be removed!");
							stage = 50;
							break;
						} else {
							interpreter.sendDialogues(npc, FacialExpression.NEUTRAL, "I have removed your Iron Man status.");
							player.getIronmanManager().setMode(IronmanMode.NONE);
							player.sendMessage("Your Iron Man status has been removed.");
							stage = 50;
							break;
						}
					case 2: //change ironman mode
						if (player.getSavedData().getActivityData().getHardcoreDeath() == true) {
							interpreter.sendDialogues(npc, FacialExpression.GUILTY, "Sorry, but you've fallen as a Hardcore Iron Man", "already. It would be unfair for those with other", " restrictions if your status were to be changed!");
							stage = 50;
							break;
						}
						if (player.getSkills().getTotalLevel() > 500 || player.getQuestRepository().getPoints() > 10){
							interpreter.sendDialogues(npc, FacialExpression.GUILTY, "Sorry, but you are too far along your journey.", "It would be unfair for those with other", " restrictions if your status were to be changed!");
							stage = 50;
							break;
						} else {
							interpreter.sendOptions("Select a Mode", "Standard", "<col=8A0808>Hardcore</col>", "<col=ECEBEB>Ultimate</col>", "Nevermind.");
							stage = 150;
							break;
						}
					case 3: // What is Iron Man Mode?
						interpreter.sendDialogues(player, FacialExpression.ASKING,"What is an Iron Man?");
						stage = 120;
						break;
					case 4: //Go back.
						interpreter.sendOptions("Select an Option", "Have you been here as long as me?", "I'd like to learn faster!", "About Iron Man mode...", "Go Back...");
						stage = 10;
						break;
				}
				break;
			case 110:
				switch (buttonId) {
					case 1: //I would like to be an Iron Man
						if (player.getSkills().getTotalLevel() > 50 || player.getQuestRepository().getPoints() > 10){
							interpreter.sendDialogues(npc, FacialExpression.GUILTY, "Sorry, but you are too far along your journey.", "It would be unfair for those with other", " restrictions if your status were to be changed!");
							stage = 50;
							break;
						} else {
							interpreter.sendOptions("Select a Mode", "Standard", "<col=8A0808>Hardcore</col>", "<col=ECEBEB>Ultimate</col>", "Nevermind.");
							stage = 150;
							break;
						}
					case 2: // What is Iron Man Mode?
						player("What is an Iron Man?");
						stage = 120;
						break;
					case 3: //Go back.
						interpreter.sendOptions("Select an Option", "Have you been here as long as me?", "I'd like to learn faster!", "About Iron Man mode...", "Go Back...");
						stage = 10;
						break;
				}
				break;

			//What is an Iron Man?
			case 120:
				interpreter.sendDialogues(npc, FacialExpression.NEUTRAL,"An Iron Man account is a style of playing where players", "are completely self-sufficient.");
				stage++;
				break;
			case 121:
				interpreter.sendDialogues(npc, FacialExpression.NEUTRAL,"A Standard Ironman does not receive items or", "assistance from other players. They cannot trade, stake,", "receive PK loot, scavenge dropped items, nor play", "certain minigames.");
				stage++;
				break;
			case 122:
				interpreter.sendDialogues(npc, FacialExpression.NEUTRAL,"In addition to Standard Ironman restrictions,", "<col=8A0808>Hardcore</col> Ironmen only have one life. In the event of","a dangerous death, a player will be downgraded to a", "Standard Ironman, and their stats frozen on the hiscores.");
				stage++;
				break;
			case 123:
				interpreter.sendDialogues(npc, FacialExpression.NEUTRAL,"For the ultimate challenge, players who choose the", "<col=ECEBEB>Ultimate</col> Ironman mode cannot use banks, nor", "retain any items on death in dangerous areas.");
				stage++;
				break;
			case 124:
				if (player.getIronmanManager().isIronman()) {
					interpreter.sendOptions("Select an Option", "I no longer want to be an Iron Man", "I'd like to change my Iron Man mode", "What is an Iron Man?", "Go Back.");
					stage = 100;
				} else {
					interpreter.sendOptions("Select an Option", "I would like to be an Iron Man.", "What is an Iron Man?", "Go Back...");
					stage = 110;
				}
				break;

			case 131:
				end();
				break;
			//Change Iron man mode dialogue/code
			case 150:
				switch(buttonId){
					case 1:
					case 2:
						interpreter.sendDialogues(npc, FacialExpression.NEUTRAL,"I have changed your Iron Man mode to: ","" + (buttonId == 1 ? "Standard" : "<col=8A0808>Hardcore</col>" + " Ironman mode."));
						player.getSettings().toggleAcceptAid();
						player.getIronmanManager().setMode(IronmanMode.values()[buttonId]);
						if(buttonId == 2) {
							player.setAttribute("/save:permadeath",true);
						}
						player.sendMessage("Your Iron Man status has been changed.");
						stage = 50;
						break;
					case 3: //ultimate ironman
						if (!player.getBank().isEmpty())
						{
							interpreter.sendDialogues(npc, FacialExpression.GUILTY, "Sorry, but your bank is has items in it.", "Please empty your bank and speak to me again.");
							stage = 50;
							break;
						} else {
							interpreter.sendDialogues(npc, FacialExpression.NEUTRAL,"I have changed your Iron Man mode to:","<col=ECEBEB>Ultimate</col> Ironman mode.");
							player.getIronmanManager().setMode(IronmanMode.ULTIMATE);
							player.sendMessage("Your Iron Man status has been changed.");
							stage = 50;
							break;
						}
					case 4:
						if (player.getIronmanManager().isIronman()) {
							interpreter.sendOptions("Select an Option", "I no longer want to be an Iron Man", "I'd like to change my Iron Man mode", "What is an Iron Man?", "Go Back...");
							stage = 100;
						} else {
							interpreter.sendOptions("Select an Option", "I would like to be an Iron Man.", "What is an Iron Man?", "Go Back...");
							stage = 110;
						}
						break;
				}
				break;


			//About XP Multiplier
			case 200:
				interpreter.sendOptions("Select an Option", "Set my experience rate to 10x", "Nevermind.");
				stage++;
				break;
			case 201:
				switch (buttonId) {
					case 1:
						interpreter.sendDialogues(npc, FacialExpression.FRIENDLY, "Tada! Your experience rate is now 10x.", "Happy Scaping!");
						player.getSkills().experienceMutiplier = 10.0;
						stage = 50;
						break;
					case 2:
					    end();
						break;
				}
				break;
		}

		return true;
	}

	/**
	 * Obtains the player's join date and time played.
	 */
	private void getPlayerTime() {

		//TODO:
		// Find the Date Joined and Time Played variables for the player WITHOUT directly connecting to the SQL database here
		// Split the Time Played variable into Days, Hours and Minutes
		// Insert each calculation into the timePlayed array ( 0 for Days, 1 for Hours and 2 for Minutes)
		// Calculate the Days Since registering by subtracting the Date Joined from the Current Server Date (ServerDate - Join_Date)
		// Insert the date difference into joinDateDays variable
		// return;???
	}

	@Override
	public int[] getIds() {
		return new int[] { 0 };
	}
}
