package core.game.content.quest.free.dragonslayer;

import core.game.node.entity.player.link.diary.DiaryType;
import core.tools.Items;
import core.game.content.dialogue.DialoguePlugin;
import core.game.content.dialogue.FacialExpression;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.quest.Quest;
import core.game.node.item.GroundItemManager;
import core.game.node.item.Item;

/**
 * Represents the dialogue plugin used for the duke horacio
 * @author 'Vexia
 * @version 1.0
 */
public final class DukeHoracioDialogue extends DialoguePlugin {

    /**
     * Represents the air talisman item.
     */
    private static final Item TALISMAN = new Item(1438);

    private static final NPC Sigmund = new NPC(2082);

    /**
     * Constructs a new {@code DukeHoracioDialogue} {@code Object}.
     */
    public DukeHoracioDialogue() {
        /**
         * empty.
         */
    }

    /**
     * Constructs a new {@code DukeHoracioDialogue} {@code Object}.
     *
     * @param player the player.
     */
    public DukeHoracioDialogue(Player player) {
        super(player);
    }

    @Override
    public int[] getIds() {
        return new int[]{741};
    }

    @Override
    public boolean handle(int interfaceId, int buttonId) {
        if (player.getQuestRepository().getQuest("Dragon Slayer").getStage(player) >= 20
                && !player.getInventory().containsItem(DragonSlayer.SHIELD)
                && !player.getBank().containsItem(DragonSlayer.SHIELD) && !player.getEquipment().containsItem(DragonSlayer.SHIELD)) {
            switch (stage) {
                case 0:
                    player("I seek a shield that will protect me from dragonbreath.");
                    stage = 400;
                    break;
                case 400:
                    npc("A knight going on a dragon quest, hmm? What", "dragon do you intend to slay?");
                    stage = 401;
                    break;
                case 401:
                    player("Elvarg, the dragon of Crandor island!");
                    stage = 402;
                    break;
                case 402:
                    npc("Elvarg? Are you sure?");
                    stage = 403;
                    break;
                case 403:
                    player("Yes!");
                    stage = 404;
                    break;
                case 404:
                    npc("Well, you're a braver man than I!");
                    stage = 405;
                    break;
                case 405:
                    player("Why is everyone scared of this dragon?");
                    stage = 406;
                    break;
                case 406:
                    npc("Back in my father's day, Crandor was an important", "city-state. Politically, it was important as Falador or", "Varrock and its shipes traded with every port.");
                    stage = 407;
                    break;
                case 407:
                    npc("But, one day when I was little, all contact was lost. The", "trading ships and diplomatic envoys just stopped", "coming.");
                    stage = 408;
                    break;
                case 408:
                    npc("I remember my father being very scared. He posted", "lookouts on the roof to warn if the dragon was", "approaching. All the city rulers worried that", "Elvarg would devastate the whole continent.");
                    stage = 409;
                    break;
                case 409:
                    player("So, are you going to give me the shield or not?");
                    stage = 410;
                    break;
                case 410:
                    npc("If you really think you're up to it then perhaphs you", "are the one who can kill this dragon.");
                    stage = 411;
                    break;
                case 411:
                    if (!player.getInventory().add(DragonSlayer.SHIELD)) {
                        GroundItemManager.create(DragonSlayer.SHIELD, player);
                    }
                    interpreter.sendItemMessage(DragonSlayer.SHIELD, "The Duke hands you a heavy orange shield.");
                    // Obtain an Anti-dragonbreath shield from Duke Horacio
                    player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 2, 5);
                    stage = 412;
                    break;
            }
            return true;
        }
        if (player.getQuestRepository().getQuest("Dragon Slayer").getStage(player) == 20) {
            switch (stage) {
                case 412:
                    npc("Take care out there. If you kill it...");
                    stage = 413;
                    return true;
                case 413:
                    npc("If you kill it, for Saradomin's sake make sure it's really", "dead!");
                    stage = 414;
                    return true;
                case 414:
                    end();
                    return true;
            }
        }
        final Quest quest = player.getQuestRepository().getQuest("Rune Mysteries");
        switch (stage) {
            case 0:
                if (player.getQuestRepository().getQuest("Dragon Slayer").getStage(player) == 100 && !player.getInventory().containsItem(DragonSlayer.SHIELD) && !player.getBank().containsItem(DragonSlayer.SHIELD)) {
                    interpreter.sendOptions("Select an Option", "I seek a shield that will protect me from dragonbreath.", "Have you any quests for me?", "Where can I find money?");
                    stage = -5;
                    return true;
                } else if(player.getQuestRepository().getQuest("Lost Tribe").getStage(player) == 20){
                    interpreter.sendOptions("Select an Option","Have you any quests for me?","Where can I find money?","I know what happened in the cellar.");
                    stage = -10;
                    return true;
                } else if(player.getQuestRepository().getQuest("Lost Tribe").getStage(player) == 30 && player.getInventory().containsItem(new Item(Items.BROOCH_5008))){
                    options("Have you any quests for me?","Where can I find money?","I found something in the rubble.");
                    stage = -15;
                    return true;
                } else if(player.getQuestRepository().getQuest("Lost Tribe").getStage(player) == 44){
                    options("Have you any quests for me?","Where can I find money?","I spoke with the goblin generals.");
                    stage = -20;
                    return true;
                } else if(player.getQuestRepository().getQuest("Lost Tribe").getStage(player) == 46){
                    options("Have you any quests for me?","Where can I find money?","I made contact with the Dorgeshuun.");
                    stage = -25;
                    return true;
                } else if(player.getQuestRepository().getQuest("Lost Tribe").getStage(player) == 49 && player.getInventory().contains(Items.SILVERWARE_5011,1)){
                    options("Have you any quests for me?","Where can I find money?","I found the silverware.");
                    stage = -30;
                    return true;
                }

                else {
                    interpreter.sendOptions("Select an Option", "Have you any quests for me?", "Where can I find money?");
                }
                stage = 1;
                break;
            case -5:
                switch (buttonId) {
                    case 1:
                        stage = 800;
                        handleShield(buttonId);
                        break;
                    case 2:
                        interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Have any quests for me?");
                        stage = 20;
                        break;
                    case 3:
                        interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I hear many of the local people earn money by", "learning a skill. Many people get by in life by becoming", "accomplished smiths, cooks, miners and woodcutters.");
                        stage = 30;
                        break;
                }
                break;
            case -10:
                switch(buttonId){
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Have any quests for me?");
                        stage = 20;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I hear many of the local people earn money by", "learning a skill. Many people get by in life by becoming", "accomplished smiths, cooks, miners and woodcutters.");
                        stage = 30;
                        break;
                    case 3:
                        NPC witness = new NPC(player.getAttribute("tlt-witness",0));
                        player(witness.getName() + " says he saw something in the cellar.","Like a goblin with big eyes.");
                        stage = 500;
                        break;
                }
                break;
            case -15:
                switch(buttonId){
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Have any quests for me?");
                        stage = 20;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I hear many of the local people earn money by", "learning a skill. Many people get by in life by becoming", "accomplished smiths, cooks, miners and woodcutters.");
                        stage = 30;
                        break;
                    case 3:
                        player("I dug through the rubble in the cellar and found a","tunnel!");
                        stage = 520;
                        break;
                }
                break;
            case -20:
                switch(buttonId){
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Have any quests for me?");
                        stage = 20;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I hear many of the local people earn money by", "learning a skill. Many people get by in life by becoming", "accomplished smiths, cooks, miners and woodcutters.");
                        stage = 30;
                        break;
                    case 3:
                        player("I spoke to the goblin generals in the goblin village. They","told me about an ancient goblin tribe that went to live","underground.");
                        stage = 540;
                        break;
                }
                break;
            case -25:
                switch(buttonId){
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Have any quests for me?");
                        stage = 20;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I hear many of the local people earn money by", "learning a skill. Many people get by in life by becoming", "accomplished smiths, cooks, miners and woodcutters.");
                        stage = 30;
                        break;
                    case 3:
                        player("I've made contact with the cave goblins. They say they","were following a seam and broke into the cellar by","mistake.");
                        stage = 550;
                        break;
                }
                break;
            case -30:
                switch(buttonId){
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Have any quests for me?");
                        stage = 20;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I hear many of the local people earn money by", "learning a skill. Many people get by in life by becoming", "accomplished smiths, cooks, miners and woodcutters.");
                        stage = 30;
                        break;
                    case 3:
                        player("I found the missing silverware in the HAM cave!");
                        stage = 560;
                        break;
                }
                break;
            case 1:
                switch (buttonId) {
                    case 1:
                        interpreter.sendDialogues(player, FacialExpression.HALF_GUILTY, "Have any quests for me?");
                        stage = 20;
                        break;
                    case 2:
                        interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "I hear many of the local people earn money by learning a", "skill. Many people get by in life by becoming accomplished", "smiths, cooks, miners and woodcutters.");
                        stage = 30;
                        break;
                }
                break;
            case 10:
                interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "You haven't seem to have slain Elvarg yet!", "Once you have slain Elvarg come back and talk to me.");
                stage = 11;
                break;
            case 20:
                if (quest.getStage(player) == 10) {
                    interpreter.sendDialogues(npc, null, "The only task remotely approaching a quest is the", "delivery of the talisman I gave you to the head wizard", "of the Wizards' Tower,");
                    stage = 1000;
                    break;
                }
                if (quest.getStage(player) > 10) {
                    interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Nope, I've got everything under control", "in the castle at the moment.");
                    stage = 69;
                    return true;
                }
                interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Well, it's not really a quest but I recently discovered", "this strange talisman.");
                stage = 21;
                break;
            case 69:
                end();
                break;
            case 1000:
                interpreter.sendDialogues(npc, null, "south-west of here. I suggest you deliver it to him as", "soon as possible. I have the oddest feeling that is", "important...");
                stage = 1001;
                break;
            case 1001:
                end();
                break;
            case 21:
                interpreter.sendDialogues(npc, null, "It seems to be mystical and I have never seen anything", "like it before. Would you take it to the head wizard at");
                stage = 22;
                break;
            case 22:
                interpreter.sendDialogues(npc, null, "the Wizards' Tower for me? It's just south-west of here", "and should not take you very long at all. I would be", "awfully grateful.");
                stage = 23;
                break;
            case 23:
                interpreter.sendOptions("Select an Option", "Sure, no problem.", "Not right now.");
                stage = 24;
                break;
            case 24:
                switch (buttonId) {
                    case 1:
                        interpreter.sendDialogues(player, null, "Sure, no problem.");
                        stage = 100;
                        break;
                    case 2:
                        interpreter.sendDialogues(player, null, "Not right now.");
                        stage = 26;
                        break;
                }
                break;
            case 100:
                interpreter.sendDialogues(npc, null, "Thank you very much, stranger. I am sure the head", "wizard will reward you for such an interesting find.");
                stage = 101;
                break;
            case 101:
                interpreter.sendDialogue("The Duke hands you an " + Quest.BLUE + "air talisman</col>.");
                stage = 102;
                break;
            case 102:
                quest.start(player);
                player.getQuestRepository().syncronizeTab(player);
                if (!player.getInventory().add(TALISMAN)) {
                    GroundItemManager.create(TALISMAN, player.getLocation(), player);
                }
                end();
                break;
            case 26:
                interpreter.sendDialogues(npc, null, "As you wish, stranger, although I have this strange", "feeling that it is important. Unfortunately, I cannot", "leave my castle unattended.");
                stage = 27;
                break;
            case 27:
                end();
                break;
            case 30:
                end();
                break;
            case 500:
                npc("Yes, he mentioned that to me. But I think he was","imagining things. Goblins live in natural caves but","everyone knows they don't have the wit to make their","own tunnels.");
                stage++;
                break;
            case 501:
                sendNormalDialogue(Sigmund,FacialExpression.ANGRY,"Yes your grace, but if there is any possibility that this","is a goblin incursion then we should take that possibility","very seriously!");
                stage++;
                break;
            case 502:
                player("I think we should at least investigate.");
                stage++;
                break;
            case 503:
                sendNormalDialogue(Sigmund,FacialExpression.WORRIED,"Your grace, I think you should listen to " + (player.isMale() ? "him" : "her") + ".");
                stage++;
                break;
            case 504:
                npc("Hmm, very well. I give you permission to investigate","this mystery. If there is a blocked tunnel then perhaps","you should try to un-block it.");
                player.getQuestRepository().getQuest("Lost Tribe").setStage(player,30);
                stage++;
                break;
            case 505:
                end();
                break;
            case 520:
                player("On the ground I found this brooch.");
                stage++;
                break;
            case 521:
                npc("I've never seen anything like that before. It doesn't","come from Lumbridge. What do you think, Sigmund?");
                stage++;
                break;
            case 522:
                sendNormalDialogue(Sigmund,FacialExpression.WORRIED,"It is unknown to me, your grace. But the fact it is","there is enough to prove the Cook's story. It must have","been dropped by a goblin as it fled.");
                stage++;
                break;
            case 523:
                npc("I've never heard of a goblin wearing something so well-","crafted.");
                stage++;
                break;
            case 524:
                sendNormalDialogue(Sigmund,FacialExpression.ANGRY,"Then it must have been stolen!");
                stage++;
                break;
            case 525:
                npc("But it wasn't stolen from us. Where could it be from?");
                stage++;
                break;
            case 526:
                sendNormalDialogue(Sigmund,FacialExpression.ANGRY,"That doesn't matter! You said yourself that goblins","couldn't have made that, so they must have stolen it","from somewhere.");
                stage++;
                break;
            case 527:
                sendNormalDialogue(Sigmund,FacialExpression.ANGRY,"Horrible, thieving goblins have broken into our cellar!","We must retaliate immediately!");
                stage++;
                break;
            case 528:
                sendNormalDialogue(Sigmund,FacialExpression.ANGRY,"First we should wipe out the goblins east of the river,","then we can march on the goblin village to the north-","west...");
                stage++;
                break;
            case 529:
                npc("I will not commit troops until I have proof that goblins","are behind this.");
                stage++;
                break;
            case 530:
                npc(player.getName() + ", please find out what you can about this","brooch. The librarian in Varrock might be able to help","identify the symbol.");
                player.getQuestRepository().getQuest("Lost Tribe").setStage(player,40);
                stage++;
                break;
            case 531:
                end();
                break;
            case 540:
                sendNormalDialogue(Sigmund,FacialExpression.ANGRY,"What more proof do we need? Nasty, smelly goblins","have been living under our feet all this time! We must","crush them at once!");
                stage++;
                break;
            case 541:
                npc("Hmm, perhaps you are right. I will send word to the","army to prepare for an underground assault.");
                stage++;
                break;
            case 542:
                npc(player.getName() + ", I would still like you to find out more","about this tribe. It cannot hurt to know one's enemy.");
                player.getQuestRepository().getQuest("Lost Tribe").setStage(player,45);
                stage++;
                break;
            case 543:
                end();
                break;
            case 550:
                sendNormalDialogue(Sigmund,FacialExpression.ANGRY,"And I suppose you believe them, goblin lover?");
                stage++;
                break;
            case 551:
                player("Well, they seemed friendlier than most goblins, and","nothing was taken from the cellar.");
                stage++;
                break;
            case 552:
                npc("Actually, something was taken. Sigmund has informed","me that some of the castle silverware is missing from","the cellar.");
                stage++;
                break;
            case 553:
                npc("Unless it is returned, I am afraid I will have no option","but war.");
                player.getQuestRepository().getQuest("Lost Tribe").setStage(player,47);
                stage++;
                break;
            case 554:
                end();
                break;
            case 560:
                npc("Sigmund! Is this your doing?");
                stage++;
                break;
            case 561:
                sendNormalDialogue(Sigmund,FacialExpression.WORRIED,"Of...of course not! The goblins must have, um, dropped","the silverware as they ran away.");
                stage++;
                break;
            case 562:
                npc("Don't lie to me! I knew you were a HAM member but","I didn't think you would stoop to this. You are","dismissed from my service.");
                stage++;
                break;
            case 563:
                sendNormalDialogue(Sigmund,FacialExpression.THINKING,"But don't you see it was for the best? For goblins to be","living under our feet like this... ugh. It doesn't matter","how civilised they are: all sub-human species must be","wiped out!");
                stage++;
                break;
            case 564:
                npc("That's enough! Get out of my castle now!");
                stage++;
                break;
            case 565:
                npc("I see I was ill-advised. Unless there is an act of","aggression by the cave goblins there is no need for war.");
                stage++;
                break;
            case 566:
                interpreter.sendItemMessage(Items.PEACE_TREATY_5012,"The Duke writes a document and signs it.");
                stage++;
                break;
            case 567:
                npc("This peace treaty specifies the border between","Lumbridge and the Cave Goblin realm. Please take it to","the cave goblins and tell them I would like to meet with","their leader to sign it.");
                player.getInventory().add(new Item(Items.PEACE_TREATY_5012));
                player.getQuestRepository().getQuest("Lost Tribe").setStage(player,50);
                player.varpManager.get(465).setVarbit(0,9).send(player);
                stage++;
                break;
            case 568:
                end();
                break;
            default:
                handleShield(buttonId);
                break;
        }
        return true;
    }

	@Override
	public DialoguePlugin newInstance(Player player) {
		return new DukeHoracioDialogue(player);
	}

	@Override
	public boolean open(Object... args) {
		npc = (NPC) args[0];
		interpreter.sendDialogues(npc, FacialExpression.HALF_GUILTY, "Greetings. Welcome to my castle.");
		stage = 0;
        // Speak to the Duke of Lumbridge
        player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 0, 2);
		return true;
	}

	public void handleShield(int buttonId) {
		switch (stage) {
		case 800:
			player("I seek a shield that will protect me from dragonbreath.");
			stage = 801;
			break;
		case 801:
			npc("A knight going on a dragon quest, hmm? What", "dragon do you intend to slay?");
			stage = 802;
			break;
		case 802:
			player("Oh, no dragon in particular. I just feel like killing a", "dragon.");
			stage = 803;
			break;
		case 803:
			npc("Of course. Now you've slain Elvarg, you've earned", "the right to call the shield your own!");
			stage = 804;
			break;
		case 804:
			if (!player.getInventory().add(DragonSlayer.SHIELD)) {
				GroundItemManager.create(DragonSlayer.SHIELD, player);
			}
			interpreter.sendItemMessage(DragonSlayer.SHIELD, "The Duke hands you the shield.");
            // Obtain an Anti-dragonbreath shield from Duke Horacio
            player.getAchievementDiaryManager().finishTask(player, DiaryType.LUMBRIDGE, 2, 5);
			stage = 805;
			break;
		case 805:
			end();
			break;
		}
	}
}
