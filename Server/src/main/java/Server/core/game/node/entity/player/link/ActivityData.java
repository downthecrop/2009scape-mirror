package core.game.node.entity.player.link;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.nio.ByteBuffer;

/**
 * Represents a managing class of activity related information.
 * @author 'Vexia
 */
public final class ActivityData {

	/**
	 * Represents the pest points gained from pest control.
	 */
	private int pestPoints;

	/**
	 * The amount of warrior's guild tokens.
	 */
	private int warriorGuildTokens;

	/**
	 * The bounty hunter rating.
	 */
	private int bountyHunterRate;

	/**
	 * The bounty rogue rating.
	 */
	private int bountyRogueRate;

	/**
	 * The barrow brothers currently killed.
	 */
	private boolean[] barrowBrothers = new boolean[6];

	/**
	 * The barrow kill count.
	 */
	private int barrowKills;

	/**
	 * The barrow tunnel crypt index.
	 */
	private int barrowTunnelIndex;

	/**
	 * The kolodion stage.
	 */
	private int kolodionStage;

	/**
	 * The god casts.
	 */
	private int[] godCasts = new int[3];

	/**
	 * The kolodion boss.
	 */
	private int kolodionBoss;

	/**
	 * If received the elnock supplies.
	 */
	private boolean elnockSupplies;

	/**
	 * The time stamp of the last battle with Bork.
	 */
	private long lastBorkBattle;
	
	/**
	 * The amount of temp bork kills.
	 */
	private byte borkKills;

	/**
	 * If the player has lost his DMC.
	 */
	private boolean lostCannon;

	/**
	 * If we started the mta.
	 */
	private boolean startedMta;

	/**
	 * The pizazz points.
	 */
	private int[] pizazzPoints = new int[4];

	/**
	 * If the player has unlocked bones to peaches.
	 */
	private boolean bonesToPeaches;

	/**
	 * The amount of solved telekinetic mazes.
	 */
	private int solvedMazes;

	/**
	 * The fog rating.
	 */
	private int fogRating;

	/**
	 * The death status of a Hardcore Iron Man
	 */
	private boolean hardcoreDeath;

	/**
	 * Pyramid plunder top (is it grabbed?)
	 */
	boolean topGrabbed;

	/**
	 * Constructs a new {@code ActivityInfo} {@code Object}.
	 */
	public ActivityData() {
		/*
		 * empty.
		 */
	}

	public void parse(JSONObject data){
		pestPoints = Integer.parseInt( data.get("pestPoints").toString());
		warriorGuildTokens = Integer.parseInt( data.get("warriorGuildTokens").toString());
		bountyHunterRate = Integer.parseInt( data.get("bountyHunterRate").toString());
		bountyRogueRate = Integer.parseInt( data.get("bountyRogueRate").toString());
		barrowKills = Integer.parseInt( data.get("barrowKills").toString());
		JSONArray bb = (JSONArray) data.get("barrowBrothers");
		for(int i = 0; i < bb.size(); i++){
			barrowBrothers[i] = (boolean) bb.get(i);
		}
		barrowTunnelIndex = Integer.parseInt( data.get("barrowTunnelIndex").toString());
		kolodionStage = Integer.parseInt( data.get("kolodionStage").toString());
		JSONArray gc = (JSONArray) data.get("godCasts");
		for(int i = 0; i < gc.size(); i++){
			godCasts[i] = Integer.parseInt(gc.get(i).toString());
		}
		kolodionBoss = Integer.parseInt( data.get("kolodionBoss").toString());
		elnockSupplies = (boolean) data.get("elnockSupplies");
		lastBorkBattle =  Long.parseLong(data.get("lastBorkBattle").toString());
		startedMta = (boolean) data.get("startedMta");
		lostCannon = (boolean) data.get("lostCannon");
		JSONArray pp = (JSONArray) data.get("pizazzPoints");
		for(int i = 0 ; i < pp.size(); i++){
			pizazzPoints[i] = Integer.parseInt(pp.get(i).toString());
		}
		bonesToPeaches = (boolean) data.get("bonesToPeaches");
		solvedMazes = Integer.parseInt( data.get("solvedMazes").toString());
		fogRating = Integer.parseInt( data.get("fogRating").toString());
		borkKills = Byte.parseByte(data.get("borkKills").toString());
		hardcoreDeath = (boolean) data.get("hardcoreDeath");
		topGrabbed = (boolean) data.get("topGrabbed");
	}

	/**
	 * Gets the elnockSupplies.
	 * @return The elnockSupplies.
	 */
	public boolean isElnockSupplies() {
		return elnockSupplies;
	}

	/**
	 * Sets the elnockSupplies.
	 * @param elnockSupplies The elnockSupplies to set.
	 */
	public void setElnockSupplies(boolean elnockSupplies) {
		this.elnockSupplies = elnockSupplies;
	}

	/**
	 * Increases the pest points.
	 * @param pestPoints the pest points to increase with.
	 */
	public void increasePestPoints(int pestPoints) {
		if (pestPoints + this.pestPoints > 500) {
			this.pestPoints = 500;
		} else {
			this.pestPoints += pestPoints;
		}
	}

	/**
	 * Decreases the pest points.
	 * @param pestPoints the pest points to increase with.
	 */
	public void decreasePestPoints(int pestPoints) {
		this.pestPoints -= pestPoints;
	}

	/**
	 * Gets the pest points.
	 * @return the pest points.
	 */
	public int getPestPoints() {
		return pestPoints;
	}

	/**
	 * Sets the pest points.
	 * @param pestPoints the pest points.
	 */
	public void setPestPoints(int pestPoints) {
		this.pestPoints = pestPoints;
	}

	/**
	 * Gets the warriorGuildTokens.
	 * @return The warriorGuildTokens.
	 */
	public int getWarriorGuildTokens() {
		return warriorGuildTokens;
	}

	/**
	 * Sets the warriorGuildTokens.
	 * @param warriorGuildTokens The warriorGuildTokens to set.
	 */
	public void setWarriorGuildTokens(int warriorGuildTokens) {
		this.warriorGuildTokens = warriorGuildTokens;
	}

	/**
	 * Updates the warrior guild tokens.
	 * @param amount The amount to increase with.
	 */
	public void updateWarriorTokens(int amount) {
		this.warriorGuildTokens += amount;
	}

	/**
	 * Gets the bountyHunterRate.
	 * @return The bountyHunterRate.
	 */
	public int getBountyHunterRate() {
		return bountyHunterRate;
	}

	/**
	 * Increments the bountyHunterRate.
	 * @param rate The rate to set.
	 */
	public void updateBountyHunterRate(int rate) {
		this.bountyHunterRate += rate;
	}

	/**
	 * Gets the bountyRogueRate.
	 * @return The bountyRogueRate.
	 */
	public int getBountyRogueRate() {
		return bountyRogueRate;
	}

	/**
	 * Increments the bountyRogueRate.
	 * @param rate The rate to set.
	 */
	public void updateBountyRogueRate(int rate) {
		this.bountyRogueRate += rate;
	}

	/**
	 * Gets the barrowBrothers.
	 * @return The barrowBrothers.
	 */
	public boolean[] getBarrowBrothers() {
		return barrowBrothers;
	}

	/**
	 * Sets the barrowBrothers.
	 * @param barrowBrothers The barrowBrothers to set.
	 */
	public void setBarrowBrothers(boolean[] barrowBrothers) {
		this.barrowBrothers = barrowBrothers;
	}

	/**
	 * Gets the barrowKills.
	 * @return The barrowKills.
	 */
	public int getBarrowKills() {
		return barrowKills;
	}

	/**
	 * Sets the barrowKills.
	 * @param barrowKills The barrowKills to set.
	 */
	public void setBarrowKills(int barrowKills) {
		if (barrowKills > 10000) {
			barrowKills = 10000;
		}
		this.barrowKills = barrowKills;
	}

	/**
	 * Gets the barrowTunnelIndex.
	 * @return The barrowTunnelIndex.
	 */
	public int getBarrowTunnelIndex() {
		return barrowTunnelIndex;
	}

	/**
	 * Sets the barrowTunnelIndex.
	 * @param barrowTunnelIndex The barrowTunnelIndex to set.
	 */
	public void setBarrowTunnelIndex(int barrowTunnelIndex) {
		this.barrowTunnelIndex = barrowTunnelIndex;
	}

	/**
	 * Sets the kolodion stage.
	 * @param stage the stage.
	 */
	public void setKolodionStage(int stage) {
		this.kolodionStage = stage;
	}

	/**
	 * Checks if they have started kolodion.
	 * @return {@code True} if so.
	 */
	public boolean hasStartedKolodion() {
		return kolodionStage == 1;
	}

	/**
	 * Checks if the stage is killed.
	 * @return {@code True} if so.
	 */
	public boolean hasKilledKolodion() {
		return kolodionStage >= 2;
	}

	/**
	 * Checks if they have recieved the reward.
	 * @return {@code True if so.}
	 */
	public boolean hasRecievedKolodionReward() {
		return kolodionStage == 3;
	}

	/**
	 * Gets the godCasts.
	 * @return The godCasts.
	 */
	public int[] getGodCasts() {
		return godCasts;
	}

	/**
	 * Gets the kolodionBoss.
	 * @return The kolodionBoss.
	 */
	public int getKolodionBoss() {
		return kolodionBoss;
	}

	/**
	 * Sets the kolodionBoss.
	 * @param kolodionBoss The kolodionBoss to set.
	 */
	public void setKolodionBoss(int kolodionBoss) {
		this.kolodionBoss = kolodionBoss;
	}

	/**
	 * Gets the lastBorkBattle.
	 * @return the lastBorkBattle
	 */
	public long getLastBorkBattle() {
		return lastBorkBattle;
	}

	/**
	 * Sets the balastBorkBattle.
	 * @param lastBorkBattle the lastBorkBattle to set.
	 */
	public void setLastBorkBattle(long lastBorkBattle) {
		this.lastBorkBattle = lastBorkBattle;
	}

	/**
	 * Checks if the player has killed bork.
	 * @return {@code True if so.}
	 */
	public boolean hasKilledBork() {
		return lastBorkBattle > 0;
	}

	/**
	 * Gets the lostCannon.
	 * @return the lostCannon
	 */
	public boolean isLostCannon() {
		return lostCannon;
	}

	/**
	 * Sets the balostCannon.
	 * @param lostCannon the lostCannon to set.
	 */
	public void setLostCannon(boolean lostCannon) {
		this.lostCannon = lostCannon;
	}

	/**
	 * Gets the startedMta.
	 * @return the startedMta
	 */
	public boolean isStartedMta() {
		return startedMta;
	}

	/**
	 * Sets the bastartedMta.
	 * @param startedMta the startedMta to set.
	 */
	public void setStartedMta(boolean startedMta) {
		this.startedMta = startedMta;
	}

	/**
	 * Increments the pizazz points.
	 * @param index the index.
	 */
	public void incrementPizazz(int index) {
		pizazzPoints[index] += 1;
	}

	/**
	 * Increments the pizzaz points.
	 * @param index the index.
	 * @param amount the amount.
	 */
	public void incrementPizazz(int index, int amount) {
		pizazzPoints[index] += amount;
	}

	/**
	 * Decrements the pizzaz points.
	 * @param index the index.
	 * @param amount the amount.
	 */
	public void decrementPizazz(int index, int amount) {
		pizazzPoints[index] -= amount;
	}

	/**
	 * Increments the pizazz points.
	 * @param index the index.
	 */
	public void decrementPizazz(int index) {
		pizazzPoints[index] -= 1;
	}

	/**
	 * Gets the pizzaz points in the index. tele=0, alchemist=1, 2=enchant,
	 * 3=grave
	 * @param index the index.
	 * @return the value.
	 */
	public int getPizazzPoints(int index) {
		return pizazzPoints[index];
	}

	/**
	 * Gets the pizazzPoints.
	 * @return the pizazzPoints
	 */
	public int[] getPizazzPoints() {
		return pizazzPoints;
	}

	/**
	 * Sets the bapizazzPoints.
	 * @param pizazzPoints the pizazzPoints to set.
	 */
	public void setPizazzPoints(int[] pizazzPoints) {
		this.pizazzPoints = pizazzPoints;
	}

	/**
	 * Gets the bonesToPeaches.
	 * @return the bonesToPeaches
	 */
	public boolean isBonesToPeaches() {
		return bonesToPeaches;
	}

	/**
	 * Sets the babonesToPeaches.
	 * @param bonesToPeaches the bonesToPeaches to set.
	 */
	public void setBonesToPeaches(boolean bonesToPeaches) {
		this.bonesToPeaches = bonesToPeaches;
	}

	/**
	 * Gets the solvedMazes.
	 * @return the solvedMazes
	 */
	public int getSolvedMazes() {
		return solvedMazes;
	}

	/**
	 * Sets the solvedMazes.
	 * @param solvedMazes the solvedMazes to set.
	 */
	public void setSolvedMazes(int solvedMazes) {
		this.solvedMazes = solvedMazes;
	}

	/**
	 * Gets the fogRating.
	 * @return the fogRating
	 */
	public int getFogRating() {
		return fogRating;
	}

	/**
	 * Sets the fogRating.
	 * @param fogRating the fogRating to set.
	 */
	public void setFogRating(int fogRating) {
		this.fogRating = fogRating;
	}

	/**
	 * Gets the borkKills.
	 * @return the borkKills.
	 */
	public byte getBorkKills() {
		return borkKills;
	}

	/**
	 * Sets the borkKills.
	 * @param borkKills the borkKills to set
	 */
	public void setBorkKills(byte borkKills) {
		this.borkKills = borkKills;
	}

	/**
	 * gets the current value of an Hardcore Iron Man's death status
	 * @return the value of a Hardcore Iron Man's death status
	 */
	public boolean getHardcoreDeath() {
		return hardcoreDeath;
	}

	public void setHardcoreDeath(boolean hardcoreDeath) {
		this.hardcoreDeath = hardcoreDeath;
	}
	public void setTopGrabbed(boolean topGrabbed){
		this.topGrabbed = topGrabbed;
	}
	public boolean isTopGrabbed(){return topGrabbed;}

	public int getKolodionStage() {
		return kolodionStage;
	}
}