package core.game.node.entity.player.link;

import core.game.content.global.GodBook;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.ByteBuffer;

/**
 * Represents the quest data to save.
 * @author 'Vexia
 */
public final class GlobalData {

	/**
	 * Represents the tutorial stage.
	 */
	private int tutorialStage;

	/**
	 * Represents the home teleport delay.
	 */
	private long homeTeleportDelay;

	/**
	 * Represents the assist time.
	 */
	private long assistTime;

	/**
	 * Represents the last chat ping.
	 */
	private long chatPing;

	/**
	 * Represents the time between tutor claims.
	 */
	private long tutorClaim;

	/**
	 * Represents if luthas has given us a task.
	 */
	private boolean luthasTask;

	/**
	 * Represents the stahsed karamja bnnanas.
	 */
	private int karamjaBananas;

	private int savedX;
	
	private int savedY;
	
	private int savedH;
	
	private int taskAmount;
	
	private int taskPoints;
	/**
	 * Represents the silk steal time.
	 */
	private long silkSteal;

	/**
	 * Represents the assist experience.
	 */
	private double[] assistExperience = new double[9];

	/**
	 * Represents the strong hold rewards.
	 */
	private boolean[] strongHoldRewards = new boolean[4];

	/**
	 * Represents if the lumbridge rope has been tied;
	 */
	private boolean lumbridgeRope;

	/**
	 * Represents if the player has spoken to the apprentice.
	 */
	private boolean apprentice;

	/**
	 * Represents if the player has spoken to the friz glass blower.
	 */
	private boolean fritzGlass;

	/**
	 * Represents the zaff npc staff amount.
	 */
	private int zafAmount = 8;

	/**
	 * Represents the time until you can buy from zaff.
	 */
	private long zafTime;

	/**
	 * Represents if you're a wydin employee.
	 */
	private boolean wydinEmployee;

	/**
	 * Represents if the draynor recording has been seen.
	 */
	private boolean draynorRecording;

	/**
	 * Represents if the ge tutorial has been done.
	 */
	private boolean geTutorial;

	/**
	 * Represents the npc id of the essence teleporter.
	 */
	private int essenceTeleporter;

	/**
	 * The amount of recoil damage left (ring of recoil).
	 */
	private int recoilDamage = 40;

	/**
	 * The delay between the next use of double exp.
	 */
	private long doubleExpDelay;

	/**
	 * The delay between the next use of increased drops.
	 */
	private long dropDelay;

	/**
	 * If joined the monastery.
	 */
	private boolean joinedMonastery;

	/**
	 * If the player has read each plague
	 */
	private boolean[] readPlaques = new boolean[7];

	/**
	 * The ring of forging uses.
	 */
	private int forgingUses;

	/**
	 * The ectofuntus charges.
	 */
	private int ectoCharges;

	/**
	 * The bracelet of clay uses.
	 */
	private int braceletClayUses;

	/**
	 * The abbyss data.
	 */
	private boolean[] abyssData = new boolean[4];

	/**
	 * The amount of decays for the rc type.
	 */
	private final int[] rcDecays = new int[3];

	/**
	 * If the death screen is disabled.
	 */
	private boolean disableDeathScreen;

	/**
	 * The stage of the player safety test.
	 */
	private int playerTestStage;

	/**
	 * The delay between charming usage.
	 */
	private long charmingDelay;

	/**
	 * The fairy ring travel log.
	 */
	private boolean[] travelLogs = new boolean[45];

	/**
	 * The completed god books.
	 */
	private boolean[] godBooks = new boolean[3];

	/**
	 * The god book you are currently filling.
	 */
	private int godBook = -1;

	/**
	 * If the player see's news or not.
	 */
	private boolean disableNews;

	/**
	 * The completed god pages.
	 */
	private boolean[] godPages = new boolean[4];

	/**
	 * The time until the overcharge delay can be used.
	 */
	private long overChargeDelay;

	/**
	 * The boss kill counter.
	 */
	private int[] bossCounters = new int[25];

	/**
	 * The barrows loot counter.
	 */
	private int barrowsLoots;

	/**
	 * The amount of lootshare points.
	 */
	private int lootSharePoints;

	/**
	 * The delay between the next decrease of lootshare.
	 */
	private long lootShareDelay;
	
	/**
	 * The double exp time.
	 */
	private long doubleExp;
	
	/**
	 * If Ava's device should randomly collect loot.
	 */
	private boolean avasDevice;
	
	/**
	 * The delay of the global teleporter system.
	 */
	private long globalTeleporterDelay;
	
	/**
	 * The delay until you can exchange the star sprite again.
	 */
	private long starSpriteDelay;
	
	/**
	 * The run replenish delay.
	 */
	private long runReplenishDelay;
	
	/**
	 * The run replenish charges.
	 */
	private int runReplenishCharges;
	
	/**
	 * The amount of low alchemy charges.
	 */
	private int lowAlchemyCharges;
	
	/**
	 * The low alchemy delay.
	 */
	private long lowAlchemyDelay;
	
	/**
	 * Is the bone crusher perk enabled.
	 */
	private boolean enableBoneCrusher = false;
	
	/**
	 * Is the coin machine perk enabled.
	 */
	private boolean enableCoinMachine = false;
	
	/**
	 * Is the charm collector perk enabled.
	 */
	private boolean enableCharmCollector = false;
	
	/**
	 * The magic skill cape delay.
	 */
	private long magicSkillCapeDelay;
	
	/**
	 * The hunter cape delay.
	 */
	private long hunterCapeDelay;
	
	/**
	 * The hunter cape charges.
	 */
	private int hunterCapeCharges;

	private long minigameTeleportDelay;

	/**
	 * Whether or not randoms are enabled
	 */
	private boolean macroDisabled = false;

	public void parse(JSONObject data){
		tutorialStage = Integer.parseInt( data.get("tutorialStage").toString());
		homeTeleportDelay = Long.parseLong(data.get("homeTeleportDelay").toString());
		lumbridgeRope = (boolean) data.get("lumbridgeRope");
		apprentice = (boolean) data.get("apprentice");
		assistTime = Long.parseLong(data.get("assistTime").toString());
		JSONArray ae = (JSONArray) data.get("assistExperience");
		for(int i = 0; i < ae.size(); i++){
			assistExperience[i] = Double.parseDouble(ae.get(i).toString());
		}
		JSONArray sr = (JSONArray) data.get("strongHoldRewards");
		for(int i = 0; i < sr.size(); i++){
			strongHoldRewards[i] = (boolean) sr.get(i);
		}
		chatPing =  Long.parseLong(data.get("chatPing").toString());
		tutorClaim = Long.parseLong(data.get("tutorClaim").toString());
		luthasTask = (boolean) data.get("luthasTask");
		karamjaBananas = Integer.parseInt( data.get("karamjaBananas").toString());
		silkSteal = Long.parseLong(data.get("silkSteal").toString());
		zafAmount = Integer.parseInt( data.get("zafAmount").toString());
		zafTime =  Long.parseLong(data.get("zafTime").toString());
		fritzGlass = (boolean) data.get("fritzGlass");
		wydinEmployee = (boolean) data.get("wydinEmployee");
		draynorRecording = (boolean) data.get("draynorRecording");
		geTutorial = (boolean) data.get("geTutorial");
		essenceTeleporter = Integer.parseInt( data.get("essenceTeleporter").toString());
		recoilDamage = Integer.parseInt( data.get("recoilDamage").toString());
		doubleExpDelay = Long.parseLong(data.get("doubleExpDelay").toString());
		joinedMonastery = (boolean) data.get("joinedMonastery");
		JSONArray rp = (JSONArray) data.get("readPlaques");
		for(int i = 0; i < rp.size(); i++){
			readPlaques[i] = (boolean) rp.get(i);
		}
		forgingUses = Integer.parseInt( data.get("forgingUses").toString());
		ectoCharges = Integer.parseInt( data.get("ectoCharges").toString());
		braceletClayUses = Integer.parseInt( data.get("braceletClayUses").toString());
		dropDelay = Long.parseLong(data.get("dropDelay").toString());
		JSONArray ad = (JSONArray) data.get("abyssData");
		for(int i = 0; i < ad.size(); i++){
			abyssData[i] = (boolean) ad.get(i);
		}
		JSONArray rd = (JSONArray) data.get("rcDecays");
		for(int i = 0; i < rd.size(); i++){
			rcDecays[i] = Integer.parseInt(rd.get(i).toString());
		}
		disableDeathScreen = (boolean) data.get("disableDeathScreen");
		playerTestStage = Integer.parseInt( data.get("playerTestStage").toString());
		charmingDelay = Long.parseLong(data.get("charmingDelay").toString());
		JSONArray tl = (JSONArray) data.get("travelLogs");
		for(int i = 0; i < tl.size(); i++){
			travelLogs[i] = (boolean) tl.get(i);
		}
		JSONArray gb = (JSONArray) data.get("godBooks");
		for(int i = 0 ; i < gb.size(); i++){
			godBooks[i] = (boolean) gb.get(i);
		}
		disableNews = (boolean) data.get("disableNews");
		JSONArray gp = (JSONArray) data.get("godPages");
		for(int i = 0 ; i < gp.size(); i++){
			godPages[i] = (boolean) gp.get(i);
		}
		overChargeDelay = Long.parseLong(data.get("overChargeDelay").toString());
		JSONArray bc = (JSONArray) data.get("bossCounters");
		for(int i = 0 ; i < bc.size(); i++){
			bossCounters[i] = Integer.parseInt(bc.get(i).toString());
		}
		barrowsLoots = Integer.parseInt( data.get("barrowsLoots").toString());
		lootShareDelay = Long.parseLong(data.get("lootShareDelay").toString());
		lootSharePoints = Integer.parseInt( data.get("lootSharePoints").toString());
		doubleExp = Long.parseLong(data.get("doubleExp").toString());
		avasDevice = (boolean) data.get("avasDevice");
		globalTeleporterDelay = Long.parseLong(data.get("globalTeleporterDelay").toString());
		starSpriteDelay = Long.parseLong(data.get("starSpriteDelay").toString());
		runReplenishDelay = Long.parseLong(data.get("runReplenishDelay").toString());
		runReplenishCharges = Integer.parseInt( data.get("runReplenishCharges").toString());
		lowAlchemyCharges = Integer.parseInt( data.get("lowAlchemyCharges").toString());
		lowAlchemyDelay = Long.parseLong(data.get("lowAlchemyDelay").toString());
		magicSkillCapeDelay = Long.parseLong(data.get("magicSkillCapeDelay").toString());
		hunterCapeDelay = Long.parseLong(data.get("hunterCapeDelay").toString());
		hunterCapeCharges = Integer.parseInt( data.get("hunterCapeCharges").toString());
		taskAmount = Integer.parseInt( data.get("taskAmount").toString());
		taskPoints = Integer.parseInt( data.get("taskPoints").toString());
		macroDisabled = (boolean) data.get("macroDisabled");
	}
	
	public void setSavedLocation(int x, int y, int z) {
		setSavedX(x);
		setSavedY(y);
		setSavedH(z);
	}
	
	/**
	 * Sets the star sprite delay.
	 * @param value The value.
	 */
	public void setStarSpriteDelay(long value) {
		starSpriteDelay = value;
	}
	
	/**
	 * Gets the star sprite delay.
	 * @return The star sprite delay.
	 */
	public long getStarSpriteDelay() {
		return starSpriteDelay;
	}

	/**
	 * Gets the travel logs.
	 * @return the logs.
	 */
	public boolean[] getTravelLogs() {
		return travelLogs;
	}

	/**
	 * Removes a travel log.
	 * @param index the index.
	 */
	public void removeTravelLog(int index) {
		travelLogs[index] = false;
	}

	/**
	 * Checks if they have the travel log.
	 * @param index the index.
	 * @return {@code True} if so.
	 */
	public boolean hasTravelLog(int index) {
		return travelLogs[index];
	}

	/**
	 * Sets a travel log.
	 * @param index the index.
	 */
	public void setTravelLog(int index) {
		travelLogs[index] = true;
	}

	/**
	 * Sets the charming delay.
	 * @param delay the delay.
	 */
	public void setCharmingDelay(long delay) {
		this.charmingDelay = delay;
	}

	/**
	 * Gets the charming delay.
	 * @return the delay.
	 */
	public long getCharmingDelay() {
		return charmingDelay;
	}

	/**
	 * Gets the stage.
	 * @return the stage.
	 */
	public int getTestStage() {
		return playerTestStage;
	}

	/**
	 * Sets the test stage.
	 * @param stage the stage.
	 */
	public void setTestStage(int stage) {
		playerTestStage = stage;
	}

	/**
	 * Gets the tutorialStage.
	 * @return The tutorialStage.
	 */
	public int getTutorialStage() {
		return tutorialStage;
	}

	/**
	 * Sets the tutorialStage.
	 * @param tutorialStage The tutorialStage to set.
	 */
	public void setTutorialStage(int tutorialStage) {
		this.tutorialStage = tutorialStage;
	}

	/**
	 * Gets the homeTeleportDelay.
	 * @return The homeTeleportDelay.
	 */
	public long getHomeTeleportDelay() {
		return homeTeleportDelay;
	}

	/**
	 * Sets the homeTeleportDelay.
	 * @param homeTeleportDelay The homeTeleportDelay to set.
	 */
	public void setHomeTeleportDelay(long homeTeleportDelay) {
		this.homeTeleportDelay = homeTeleportDelay;
	}

	/**
	 * Gets the lumbridgeRope.
	 * @return The lumbridgeRope.
	 */
	public boolean hasTiedLumbridgeRope() {
		return lumbridgeRope;
	}

	/**
	 * Sets the lumbridgeRope.
	 * @param lumbridgeRope The lumbridgeRope to set.
	 */
	public void setLumbridgeRope(boolean lumbridgeRope) {
		this.lumbridgeRope = lumbridgeRope;
	}

	/**
	 * Gets the apprentice.
	 * @return The apprentice.
	 */
	public boolean hasSpokenToApprentice() {
		return apprentice;
	}

	/**
	 * Sets the apprentice.
	 * @param apprentice The apprentice to set.
	 */
	public void setApprentice(boolean apprentice) {
		this.apprentice = apprentice;
	}

	/**
	 * Gets the assistTime.
	 * @return The assistTime.
	 */
	public long getAssistTime() {
		return assistTime;
	}

	/**
	 * Sets the assistTime.
	 * @param assistTime The assistTime to set.
	 */
	public void setAssistTime(long assistTime) {
		this.assistTime = assistTime;
	}

	/**
	 * Gets the assistExperience.
	 * @return The assistExperience.
	 */
	public double[] getAssistExperience() {
		return assistExperience;
	}

	/**
	 * Sets the assistExperience.
	 * @param assistExperience The assistExperience to set.
	 */
	public void setAssistExperience(double[] assistExperience) {
		this.assistExperience = assistExperience;
	}

	/**
	 * Gets the strongHoldRewards.
	 * @return The strongHoldRewards.
	 */
	public boolean[] getStrongHoldRewards() {
		return strongHoldRewards;
	}

	/**
	 * Gets the strong hold reward value.
	 * @param reward the reward.
	 * @return {@code True} if so.
	 */
	public boolean hasStrongholdReward(int reward) {
		return strongHoldRewards[reward - 1];
	}

	/**
	 * Gets the chatPing.
	 * @return The chatPing.
	 */
	public long getChatPing() {
		return chatPing;
	}

	/**
	 * Sets the chatPing.
	 * @param chatPing The chatPing to set.
	 */
	public void setChatPing(long chatPing) {
		this.chatPing = chatPing;
	}

	/**
	 * Gets the tutorClaim.
	 * @return The tutorClaim.
	 */
	public long getTutorClaim() {
		return tutorClaim;
	}

	/**
	 * Sets the tutorClaim.
	 * @param tutorClaim The tutorClaim to set.
	 */
	public void setTutorClaim(long tutorClaim) {
		this.tutorClaim = tutorClaim;
	}

	/**
	 * Gets the luthasTask.
	 * @return The luthasTask.
	 */
	public boolean isLuthasTask() {
		return luthasTask;
	}

	/**
	 * Sets the luthasTask.
	 * @param luthasTask The luthasTask to set.
	 */
	public void setLuthasTask(boolean luthasTask) {
		this.luthasTask = luthasTask;
	}

	/**
	 * Gets the karamjaBannanas.
	 * @return The karamjaBannanas.
	 */
	public int getKaramjaBananas() {
		return karamjaBananas;
	}

	/**
	 * Sets the karamjaBannanas.
	 * @param karamjaBannanas The karamjaBannanas to set.
	 */
	public void setKaramjaBannanas(int karamjaBannanas) {
		this.karamjaBananas = karamjaBannanas;
	}

	/**
	 * Gets the silkSteal.
	 * @return The silkSteal.
	 */
	public long getSilkSteal() {
		return silkSteal;
	}

	/**
	 * Sets the silkSteal.
	 * @param silkSteal The silkSteal to set.
	 */
	public void setSilkSteal(long silkSteal) {
		this.silkSteal = silkSteal;
	}

	/**
	 * Gets the zaffAmount.
	 * @return The zaffAmount.
	 */
	public int getZaffAmount() {
		return zafAmount;
	}

	/**
	 * Sets the zaffAmount.
	 * @param zaffAmount The zaffAmount to set.
	 */
	public void setZaffAmount(int zaffAmount) {
		this.zafAmount = zaffAmount;
	}

	/**
	 * Gets the zaffTime.
	 * @return The zaffTime.
	 */
	public long getZafTime() {
		return zafTime;
	}

	/**
	 * Gets the draynorRecording.
	 * @return The draynorRecording.
	 */
	public boolean isDraynorRecording() {
		return draynorRecording;
	}

	/**
	 * Sets the draynorRecording.
	 * @param draynorRecording The draynorRecording to set.
	 */
	public void setDraynorRecording(boolean draynorRecording) {
		this.draynorRecording = draynorRecording;
	}

	/**
	 * Gets the wydinEmployee.
	 * @return The wydinEmployee.
	 */
	public boolean isWydinEmployee() {
		return wydinEmployee;
	}

	/**
	 * Sets the wydinEmployee.
	 * @param wydinEmployee The wydinEmployee to set.
	 */
	public void setWydinEmployee(boolean wydinEmployee) {
		this.wydinEmployee = wydinEmployee;
	}

	/**
	 * Sets the zaffTime.
	 * @param zaffTime The zaffTime to set.
	 */
	public void setZafTime(long zaffTime) {
		this.zafTime = zaffTime;
	}

	/**
	 * Gets the frizGlass.
	 * @return The frizGlass.
	 */
	public boolean isFritzGlass() {
		return fritzGlass;
	}

	/**
	 * Sets the frizGlass.
	 * @param frizGlass The frizGlass to set.
	 */
	public void setFritzGlass(boolean frizGlass) {
		this.fritzGlass = frizGlass;
	}

	/**
	 * Gets the geTutorial.
	 * @return The geTutorial.
	 */
	public boolean isGeTutorial() {
		return geTutorial;
	}

	/**
	 * Sets the geTutorial.
	 * @param geTutorial The geTutorial to set.
	 */
	public void setGeTutorial(boolean geTutorial) {
		this.geTutorial = geTutorial;
	}

	/**
	 * Gets the essenceTeleporter.
	 * @return The essenceTeleporter.
	 */
	public int getEssenceTeleporter() {
		return essenceTeleporter;
	}

	/**
	 * Sets the essenceTeleporter.
	 * @param essenceTeleporter The essenceTeleporter to set.
	 */
	public void setEssenceTeleporter(int essenceTeleporter) {
		this.essenceTeleporter = essenceTeleporter;
	}

	/**
	 * Gets the recoilDamage.
	 * @return The recoilDamage.
	 */
	public int getRecoilDamage() {
		return recoilDamage;
	}

	/**
	 * Sets the recoilDamage.
	 * @param recoilDamage The recoilDamage to set.
	 */
	public void setRecoilDamage(int recoilDamage) {
		this.recoilDamage = recoilDamage;
	}

	/**
	 * Gets the doubleExpDelay.
	 * @return The doubleExpDelay.
	 */
	public long getDoubleExpDelay() {
		return doubleExpDelay;
	}

	/**
	 * Sets the doubleExpDelay.
	 * @param doubleExpDelay The doubleExpDelay to set.
	 */
	public void setDoubleExpDelay(long doubleExpDelay) {
		this.doubleExpDelay = doubleExpDelay;
	}

	/**
	 * Gets the joinedMonastery.
	 * @return The joinedMonastery.
	 */
	public boolean isJoinedMonastery() {
		return joinedMonastery;
	}

	/**
	 * Sets the joinedMonastery.
	 * @param joinedMonastery The joinedMonastery to set.
	 */
	public void setJoinedMonastery(boolean joinedMonastery) {
		this.joinedMonastery = joinedMonastery;
	}

	/**
	 * Gets the read plagues.
	 * @return The plague
	 */
	public boolean[] getReadPlaques() {
		return readPlaques;
	}

	/**
	 * If the player has read all of the jail-plaques in the jail.
	 * @return If the player has read them all.
	 */
	public boolean hasReadPlaques() {
		for (int i = 0; i < getReadPlaques().length; i++) {
			if (!getReadPlaques()[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets a god book to being completed.
	 * @param book the book.
	 */
	public void setGodBook(GodBook book) {
		godBooks[book.ordinal()] = true;
	}

	/**
	 * Checks if a god book has been completed.
	 * @param book the book.
	 * @return {@code True} if so.
	 */
	public boolean hasCompletedGodBook(GodBook book) {
		return godBooks[book.ordinal()];
	}

	/**
	 * Gets the forgingUses.
	 * @return The forgingUses.
	 */
	public int getForgingUses() {
		return forgingUses;
	}

	/**
	 * Sets the forgingUses.
	 * @param forgingUses The forgingUses to set.
	 */
	public void setForgingUses(int forgingUses) {
		this.forgingUses = forgingUses;
	}

	/**
	 * Gets the ectoCharges.
	 * @return The ectoCharges.
	 */
	public int getEctoCharges() {
		return ectoCharges;
	}

	/**
	 * Sets the ectoCharges.
	 * @param ectoCharges The ectoCharges to set.
	 */
	public void setEctoCharges(int ectoCharges) {
		this.ectoCharges = ectoCharges;
	}

	/**
	 * Gets the braceletClayUses.
	 * @return The braceletClayUses.
	 */
	public int getBraceletClayUses() {
		return braceletClayUses;
	}

	/**
	 * Increments the bracelet of clay uses.
	 */
	public void incrementBraceletOfClay() {
		this.braceletClayUses += 1;
	}

	/**
	 * Resets the abyss data.
	 */
	public void resetAbyss() {
		for (int i = 0; i < abyssData.length; i++) {
			abyssData[i] = false;
		}
	}

	/**
	 * Sets the charge.
	 * @param ordinal the ordinal.
	 */
	public void setAbyssCharge(int ordinal) {
		abyssData[ordinal] = true;
	}

	/**
	 * Has an anyss charge.
	 * @param ordinal the ordinal.
	 * @return {@code True} if so.
	 */
	public boolean hasAbyssCharge(int ordinal) {
		return abyssData[ordinal];
	}

	/**
	 * Sets the braceletClayUses.
	 * @param braceletClayUses The braceletClayUses to set.
	 */
	public void setBraceletClayUses(int braceletClayUses) {
		this.braceletClayUses = braceletClayUses;
	}

	/**
	 * Gets the dropDelay.
	 * @return The dropDelay.
	 */
	public long getDropDelay() {
		return dropDelay;
	}

	/**
	 * Sets the dropDelay.
	 * @param dropDelay The dropDelay to set.
	 */
	public void setDropDelay(long dropDelay) {
		this.dropDelay = dropDelay;
	}

	/**
	 * Gets the rc decay.
	 * @param ordinal the ordinal.
	 * @return the decay.
	 */
	public int getRcDecay(int ordinal) {
		if (ordinal < 0) {
			return 0;
		}
		return rcDecays[ordinal];
	}

	/**
	 * Gets the rc decays.
	 * @return the decays.
	 */
	public int[] getRcDecays() {
		return rcDecays;
	}

	/**
	 * Checks if the death screen is disabled.
	 * @return {@code True} if so.
	 */
	public boolean isDeathScreenDisabled() {
		return disableDeathScreen;
	}

	/**
	 * Sets if the death screen is disabled.
	 * @param b the boolean.
	 */
	public void setDisableDeathScreen(boolean b) {
		this.disableDeathScreen = b;
	}

	/**
	 * Gets the godBooks.
	 * @return the godBooks
	 */
	public boolean[] getGodBooks() {
		return godBooks;
	}

	/**
	 * Sets the godBooks.
	 * @param godBooks the godBooks to set.
	 */
	public void setGodBooks(boolean[] godBooks) {
		this.godBooks = godBooks;
	}

	/**
	 * Gets the godBook.
	 * @return the godBook
	 */
	public int getGodBook() {
		return godBook;
	}

	/**
	 * Sets the godBook.
	 * @param godBook the godBook to set.
	 */
	public void setGodBook(int godBook) {
		this.godBook = godBook;
	}

	/**
	 * Gets the disableNews.
	 * @return the disableNews
	 */
	public boolean isDisableNews() {
		return disableNews;
	}

	/**
	 * Sets the disableNews.
	 * @param disableNews the disableNews to set.
	 */
	public void setDisableNews(boolean disableNews) {
		this.disableNews = disableNews;
	}

	/**
	 * Gets the godPages.
	 * @return the godPages
	 */
	public boolean[] getGodPages() {
		return godPages;
	}

	/**
	 * Sets the godPages.
	 * @param godPages the godPages to set.
	 */
	public void setGodPages(boolean[] godPages) {
		this.godPages = godPages;
	}

	/**
	 * Gets the overChargeDelay.
	 * @return the overChargeDelay.
	 */
	public long getOverChargeDelay() {
		return overChargeDelay;
	}

	/**
	 * Sets the overChargeDelay.
	 * @param overChargeDelay the overChargeDelay to set
	 */
	public void setOverChargeDelay(long overChargeDelay) {
		this.overChargeDelay = overChargeDelay;
	}

	/**
	 * Gets the bossCounters.
	 * @return the bossCounters.
	 */
	public int[] getBossCounters() {
		return bossCounters;
	}

	/**
	 * Sets the bossCounters.
	 * @param bossCounters the bossCounters to set
	 */
	public void setBossCounters(int[] bossCounters) {
		this.bossCounters = bossCounters;
	}
	
	/**
	 * Gets the barrowsLoots.
	 * @return the barrowsLoots.
	 */
	public int getBarrowsLoots() {
		return barrowsLoots;
	}

	/**
	 * Sets the barrowsLoots.
	 * @param barrowsLoots the barrowsLoots to set
	 */
	public void setBarrowsLoots(int barrowsLoots) {
		this.barrowsLoots = barrowsLoots;
	}

	/**
	 * Gets the lootSharePoints.
	 * @return the lootSharePoints.
	 */
	public int getLootSharePoints() {
		return lootSharePoints;
	}

	/**
	 * Sets the lootSharePoints.
	 * @param lootSharePoints the lootSharePoints to set
	 */
	public void setLootSharePoints(int lootSharePoints) {
		this.lootSharePoints = lootSharePoints;
	}

	/**
	 * Gets the lootShareDelay.
	 * @return the lootShareDelay.
	 */
	public long getLootShareDelay() {
		return lootShareDelay;
	}

	/**
	 * Sets the lootShareDelay.
	 * @param lootShareDelay the lootShareDelay to set
	 */
	public void setLootShareDelay(long lootShareDelay) {
		this.lootShareDelay = lootShareDelay;
	}

	/**
	 * Gets the doubleExp.
	 * @return the doubleExp.
	 */
	public long getDoubleExp() {
		return doubleExp;
	}

	/**
	 * Sets the doubleExp.
	 * @param doubleExp the doubleExp to set
	 */
	public void setDoubleExp(long doubleExp) {
		this.doubleExp = doubleExp;
	}
	
	/**
	 * Checks if double exp is active.
	 * @return {@code True} if so.
	 */
	public boolean hasDoubleExp() {
		return doubleExp > System.currentTimeMillis();
	}
	
	/**
	 * Gets the avasDevice
	 * @return the avasDevice
	 */
	public boolean isAvasDisabled() {
		return avasDevice;
	}

	/**
	 * Sets the avasDevice
	 * @param avasDevice
	 */
	public void setAvasDisabled(boolean avasDevice) {
		this.avasDevice = avasDevice;
	}

	/**
	 * Gets the globalTeleporterDelay.
	 * @return the globalTeleporterDelay.
	 */
	public long getGlobalTeleporterDelay() {
		return globalTeleporterDelay;
	}

	/**
	 * Sets the globalTeleporterDelay.
	 * @param globalTeleporterDelay the globalTeleporterDelay to set
	 */
	public void setGlobalTeleporterDelay(long globalTeleporterDelay) {
		this.globalTeleporterDelay = globalTeleporterDelay;
	}

	/**
	 * @return the runReplenishDelay
	 */
	public long getRunReplenishDelay() {
		return runReplenishDelay;
	}

	/**
	 * @param runReplenishDelay the runReplenishDelay to set
	 */
	public void setRunReplenishDelay(long runReplenishDelay) {
		this.runReplenishDelay = runReplenishDelay;
	}

	/**
	 * @return the runReplenishCharges
	 */
	public int getRunReplenishCharges() {
		return runReplenishCharges;
	}

	/**
	 * @param runReplenishCharges the runReplenishCharges to set
	 */
	public void setRunReplenishCharges(int runReplenishCharges) {
		this.runReplenishCharges = runReplenishCharges;
	}

	/**
	 * @return the lowAlchemyCharges
	 */
	public int getLowAlchemyCharges() {
		return lowAlchemyCharges;
	}

	/**
	 * @param lowAlchemyCharges the lowAlchemyCharges to set
	 */
	public void setLowAlchemyCharges(int lowAlchemyCharges) {
		this.lowAlchemyCharges = lowAlchemyCharges;
	}

	/**
	 * @return the lowAlchemyDelay
	 */
	public long getLowAlchemyDelay() {
		return lowAlchemyDelay;
	}

	/**
	 * @param lowAlchemyDelay the lowAlchemyDelay to set
	 */
	public void setLowAlchemyDelay(long lowAlchemyDelay) {
		this.lowAlchemyDelay = lowAlchemyDelay;
	}

	/**
	 * @return the enableBoneCrusher
	 */
	public boolean isEnableBoneCrusher() {
		return enableBoneCrusher;
	}

	/**
	 * @param enableBoneCrusher the enableBoneCrusher to set
	 */
	public void setEnableBoneCrusher(boolean enableBoneCrusher) {
		this.enableBoneCrusher = enableBoneCrusher;
	}

	public boolean isEnableCoinMachine() {
		return enableCoinMachine;
	}

	public void setEnableCoinMachine(boolean enableCoinMachine) {
		this.enableCoinMachine = enableCoinMachine;
	}

	/**
	 * @return the magicSkillCapeDelay
	 */
	public long getMagicSkillCapeDelay() {
		return magicSkillCapeDelay;
	}

	/**
	 * @param magicSkillCapeDelay the magicSkillCapeDelay to set
	 */
	public void setMagicSkillCapeDelay(long magicSkillCapeDelay) {
		this.magicSkillCapeDelay = magicSkillCapeDelay;
	}

	/**
	 * @return the hunterCapeDelay
	 */
	public long getHunterCapeDelay() {
		return hunterCapeDelay;
	}

	/**
	 * @param hunterCapeDelay the hunterCapeDelay to set
	 */
	public void setHunterCapeDelay(long hunterCapeDelay) {
		this.hunterCapeDelay = hunterCapeDelay;
	}

	/**
	 * @return the hunterCapeCharges
	 */
	public int getHunterCapeCharges() {
		return hunterCapeCharges;
	}

	/**
	 * @param hunterCapeCharges the hunterCapeCharges to set
	 */
	public void setHunterCapeCharges(int hunterCapeCharges) {
		this.hunterCapeCharges = hunterCapeCharges;
	}

	/**
	 * @return the enableCharmCollector
	 */
	public boolean isEnableCharmCollector() {
		return enableCharmCollector;
	}

	/**
	 * @param enableCharmCollector the enableCharmCollector to set
	 */
	public void setEnableCharmCollector(boolean enableCharmCollector) {
		this.enableCharmCollector = enableCharmCollector;
	}
	
	/**
	 * Gets the Minigame Group Finder teleport delay.
	 * @return
	 */
	public long getMinigameTeleportDelay() {
		return minigameTeleportDelay;
	}

	/**
	 * Sets the Minigame Group Finder teleport delay.
	 * @param delay The delay to set.
	 */
	public void setMinigameTeleportDelay(long delay) {
		this.minigameTeleportDelay = delay;
	}

	public int getSavedH() {
		return savedH;
	}

	public void setSavedH(int savedH) {
		this.savedH = savedH;
	}

	public int getSavedY() {
		return savedY;
	}

	public void setSavedY(int savedY) {
		this.savedY = savedY;
	}

	public int getSavedX() {
		return savedX;
	}

	public void setSavedX(int savedX) {
		this.savedX = savedX;
	}

	public int getTaskAmount() {
		return taskAmount;
	}

	public void setTaskAmount(int taskAmount) {
		this.taskAmount = taskAmount;
	}

	public int getTaskPoints() {
		return taskPoints;
	}

	public void setTaskPoints(int taskPoints) {
		this.taskPoints = taskPoints;
	}

	public void setMacroDisabled(boolean disabled){this.macroDisabled = disabled;}

	public boolean getMacroDisabled() {return this.macroDisabled;}

	public boolean[] getAbyssData() {
		return abyssData;
	}

	public int getPlayerTestStage() {
		return playerTestStage;
	}
}
