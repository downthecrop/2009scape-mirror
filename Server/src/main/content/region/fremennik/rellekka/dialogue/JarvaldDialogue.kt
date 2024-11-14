package content.region.fremennik.rellekka.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs


/**
 * @author Player Name
 */

@Initializable
class JarvaldDialogue(player: Player? = null) : DialoguePlugin(player) {
	override fun open(vararg args: Any?): Boolean {
		npc = args[0] as NPC
		val travelOption = args.size > 1
		val fremname = player.getAttribute("fremennikname","lebron james")
		if (npc.id == NPCs.JARVALD_2438) {
			// We're on Waterbirth Island
			if (isQuestComplete(player, "Fremennik Trials")) {
				if (travelOption) {
					npc("So what say you, stay here for the hunt,","or return home to sweet Rellekka to feast","and drink with your tribe?").also { stage = 201 }
				} else {
					npc("Ah, ${fremname}! Such glorious battle","makes you feel glad of life, does it not?").also { stage = 220 }
				}
			} else {
				if (travelOption) {
					npc("Have you had your fill of the hunt and wish to return,", "or are you still feeling the joy of the cull?").also { stage = 201 }
				} else {
					npc("Ah, you live yet, outerlander!").also { stage = 200 }
				}
			}
		} else {
			// We're in Rellekka
			if (isQuestComplete(player, "Fremennik Trials")) {
				if (travelOption) {
					npc("Of course, ${fremname}! Your presence is more than welcome","on this cull! You wish to leave now?").also { stage = 131 }
				} else {
					npc("Greetings, ${fremname}!").also { stage = if (fremname == "Jarvald") 100 else 102 }
				}
			} else {
				if (travelOption) {
					npc("So do you have the 1,000 coins for my service, and are", "you ready to leave?").also { stage = 41 }
				} else if (isQuestInProgress(player, "Fremennik Trials", 1, 100)) {
					player("Hi, I don't suppose you are a member of", "the council of elders are you?").also { stage = 0 }
				} else {
					npc("What do you want from me outerlander?", "It is our policy not to associate with those not of our", "tribe.").also { stage = 3 }
				}
			}
		}
		return true
	}

	fun sail(to: Boolean) {
		lock(player, 5)
		closeAllInterfaces(player)
		openInterface(player, 224)
		queueScript(player, 5, QueueStrength.SOFT) {
			closeInterface(player)
			unlock(player)
			if (to) {
				player.teleport(Location.create(2544, 3759, 0))
				sendDialogue("The ship arrives at Waterbirth Island.")
			} else {
				player.teleport(Location.create(2620, 3685, 0))
				sendDialogue("The ship arrives at Rellekka.")
			}
			return@queueScript stopExecuting(player)
		}
	}

	override fun handle(interfaceId: Int, buttonID: Int): Boolean {
		val fremname = player.getAttribute("fremennikname","lebron james")
		when (stage) {
			0 -> npc("No, outerlander, I have never had the honour to be asked.").also { stage++ }
			1 -> npc("I am but a lowly warrior, fighting for my people","wherever threats may appear against us...").also { stage++ }
			2 -> npc("So what do you want from me, outerlander?").also { stage++ }
			3 -> options("Where is your chieftain?", "What Jarvald is doing.", "Nothing").also { stage++ }
			4 -> when (buttonID) {
				1 -> player("Where is your chieftain?", "I find it highly discriminatory to refuse to talk to", "someone on the grounds that they are not part of your", "tribe.").also { stage++ }
				2 -> player("So what are you doing here?").also { stage = if (getVarp(player, 520) == 0) 10 else 40 }
				3 -> player("Actually, I don't think I have","anything to speak to you about...").also { stage = END_DIALOGUE }
			}
			5 -> npc("I don't rightly understand your speech outerlander, but", "my loyality is with Chieftain Brundt.").also { stage++ }
			6 -> npc("He resides in our longhall; it is the large building over", "there, you should speak to him for he speaks for us all.").also { stage++ }
			7 -> end().also { stage = END_DIALOGUE }
			10 -> npc("This should not concern you, outerlander.", "I am awaiting other Fremenniks to join me on an", "expedition to Waterbirth Island.").also { stage++ }
			11 -> options("Waterbirth Island?", "Can I come?", "Nice hat!", "Ok, 'bye.").also { stage++ }
			12 -> when (buttonID) {
				1 -> npc("It is a small crescent-shaped island","just north-west of here, outerlander.").also { stage++ }
				2 -> player("Can I come?").also { stage = 20 }
				3 -> player("Hey, I have to say, that's a fine looking","hat you are wearing there.").also { stage = 30 }
				4 -> player("Wow, you Fremenniks sure know how to party.","Well, see ya around.").also { stage = END_DIALOGUE }
			}
			13 -> npc("We have many legends about it,","such as the tale of the broken sky,","and the day of the green seas.").also { stage++ }
			14 -> npc("The reason I am travelling there is more serious","Fremennik business, however. I doubt an outerlander","would be interested.").also { stage++ }
			15 -> options("Can I come?", "Nice hat!", "Ok, 'bye.").also { stage++ }
			16 -> when (buttonID) {
				1 -> player("Can I come?").also { stage = 20 }
				2 -> player("Hey, I have to say, that's a fine looking","hat you are wearing there.").also { stage = 30 }
				3 -> player("Wow, you Fremenniks sure know how to party.","Well, see ya around.").also { stage = END_DIALOGUE }
			}
			20 -> npc("An outerlander join us on an honoured hunt???").also { stage++ }
			21 -> npc("Well....", "I guess...", "I might be able to allow you to join us, although it is a", "breach of many of our customs...").also { stage++ }
			22 -> player("Oh, pleeeeease?", "I really LOVE killing stuff!").also { stage++ }
			23 -> npc("Well...", "I remain unconvinced that it would be wise to allow an", "outerlander to join us in such dangerous battle, but", "your ethusiasm seems genuine enough...").also { stage++ }
			24 -> npc("I will allow you to escort us, but you must pay me a", "sum of money first.").also { stage++ }
			25 -> player("What?", "That's outrageous, why charge me money?", "And, uh, how much does it cost me?").also { stage++ }
			26 -> npc("Ah, the outerlanders have stolen from my people for", "many years, in this way you can help my community", "with a small amount of money...").also { stage++ }
			27 -> npc("Let us say...", "1,000 coins.", "Payable in advance, of course.").also { stage++ }
			28 -> npc("For this I will take you to Waterbirth Island on my", "boat, and will bring you back here when you have had", "your fill of the hunt.", "Assuming you are still alive to wish to leave, of course.").also { setVarp(player, 520, 1 shl 13, true) }.also{ stage = END_DIALOGUE }
			29 -> npc("Wow, you Fremenniks sure know how to party.","Well, see ya around.").also { stage = END_DIALOGUE }
			30 -> npc("It is actually a helm, outerlander,","but the sentiment is appreciated nonetheless.").also { stage = END_DIALOGUE }
			40 -> npc("So do you have the 1,000 coins for my service, and are", "you ready to leave?").also { stage++ }
			41 -> options("YES", "NO").also { stage++ }
			42 -> when (buttonID) {
				1 -> if (player.inventory.contains(Items.COINS_995, 1000)) {
					if (player.inventory.remove(Item(Items.COINS_995, 1000))) {
						sail(true)
					}
				} else {
					player("Sorry, I don't have enough coins.").also { stage = END_DIALOGUE }
				}
				2 -> player("No, actually I have some stuff to do here first.").also { stage++ }
			}
			43 -> npc("As you wish.", "Come and see me when your bloodlust needs sating.").also { stage = END_DIALOGUE }
			100 -> npc("Ah, and what a glorious name that is! Worthy of only the finest warriors!").also { stage++ }
			101 -> player("Heh. Yup, you're right there.").also { stage++ }

			102 -> npc("So what brings you back to fair Rellekka?","It has been too long since you have drunk in the longhall","with us and sang of your battles!").also { stage++ }
			103 -> options("What Jarvald is doing.", "Nothing").also { stage++ }
			104 -> when (buttonID) {
				1 -> player("So what are you doing here?").also { stage = if (getVarp(player, 520) and (1 shl 13) == 0) 105 else 130 }
				2 -> player("Actually, I don't think I have","anything to speak to you about...").also { stage = END_DIALOGUE }
			}
			105 -> npc("You have not heard, ${fremname}?","I am leading an expedition to Waterbirth Island!").also { stage++ }
			106 -> options("Waterbirth Island?", "Can I come?", "Nice hat!", "Ok, 'bye.").also { stage++ }
			107 -> when (buttonID) {
				1 -> npc("You have not ever travelled to Waterbirth Island, ${fremname}?","I am surprised, it is a place of outstanding natural beauty.").also { stage++ }
				2 -> player("Can I come?").also { stage = 130 }
				3 -> player("Hey, I have to say, that's a fine looking","hat you are wearing there.").also { stage = 140 }
				4 -> player("Wow, you Fremenniks sure know how to party. Well, see ya around.").also { stage = END_DIALOGUE }
			}
			108 -> npc("Or at least it used to be! But things have now changed...").also { stage++ }
			109 -> player("Changed? How do you mean, changed?").also { stage++ }
			110 -> npc("It seems as though the sea-beasts known to us as","the dagger-mouths have begun their hatching once again...","And there may be others of their ilk there too.").also { stage++ }
			111 -> player("Dagger-mouths?").also { stage++ }
			112 -> npc("Aye, the dagger-mouths! The vile creatures lived","near here once, but we had thought them all driven back","to the ocean depths many moons past.").also { stage++ }
			113 -> npc("I can only imagine a dagger-mouth queen has nested","somewhere nearby and spawned her foul brood","under the sea once more, and some of them have","migrated to fair Waterbirth Island.").also { stage++ }
			114 -> player("So you're scared they might attack Rellekka?").also { stage++ }
			115 -> npc("Scared? Ha ha ha!").also { stage++ }
			116 -> npc("You wound us with your questioning, ${fremname}!").also { stage++ }
			117 -> npc("We are glad the dagger-mouths have returned to","these shores, for it means we will get the chance","to hunt them once again as our ancestors did!").also { stage++ }
			118 -> npc("When treated in the correct manner, the creatures'","remains can be used to make fine battleworthy armour!").also { setVarp(player, 520, 1 shl 13, true) }.also{ stage++ }
			119 -> options("Can I come?", "Nice hat!", "Ok, 'bye.").also { stage++ }
			120 -> when (buttonID) {
				1 -> player("Can I come?").also { stage = 130 }
				2 -> player("Hey, I have to say, that's a fine looking","hat you are wearing there.").also { stage = 140 }
				3 -> player("Wow, you Fremenniks sure know how to party. Well, see ya around.").also { stage = END_DIALOGUE }
			}
			130 -> npc("Of course, ${fremname}! Your presence is more than welcome","on this cull! You wish to leave now?").also { stage++ }
			131 -> options("YES", "NO").also { stage++ }
			132 -> when (buttonID) {
				1 -> sail(true)
				2 -> player("No, actually I have some stuff to do here first.").also { stage = 43 }
			}
			140 -> player("Hey, I have to say, that's a fine looking","hat you are wearing there.").also { stage++ }
			141 -> npc("Aye, that it is ${fremname}!","Skulgrimen fashioned it for me from the carcass of","one of the monsters on Waterbirth Island after our last hunt!").also { stage++ }
			142 -> npc("I hope to kill enough creatures to fashion","some fine armour as well","when next we leave!").also { stage++ }
			143 -> options("Waterbirth Island?", "Can I come?", "Ok, 'bye.").also { stage++ }
			144 -> when (buttonID) {
				1 -> npc("You have not ever travelled to Waterbirth Island, ${fremname}?","I am surprised, it is a place of outstanding natural beauty.").also { stage = 108 }
				2 -> player("Can I come?").also { stage = 130 }
				3 -> player("Wow, you Fremenniks sure know how to party.","Well, see ya around.").also { stage = END_DIALOGUE }
			}
			200 -> npc("Have you had your fill of the hunt and wish to return,", "or are you still feeling the joy of the cull?").also { stage++ }
			201 -> options("I wish to return to Rellekka.", "I want to stay here.").also { stage++ }
			202 -> when (buttonID) {
				1 -> player("I wish to return to Rellekka.").also { stage++ }
				2 -> player("I want to stay here.").also { stage = 210 }
			}
			203 -> npc("Then let us away;", "There will be death to bring here another day!").also{ sail(false) }
			210 -> npc("Ha Ha Ha! A true huntsman at heart!").also { stage++ }
			211 -> npc("I myself have killed over a hundred of the dagger-","mouths, and did not think it too many!").also { stage = END_DIALOGUE }
			220 -> npc("So what say you, stay here for the hunt,","or return home to sweet Rellekka to feast","and drink with your tribe?").also { stage = 201 }
		}
		return true
	}

	override fun newInstance(player: Player?): DialoguePlugin {
		return JarvaldDialogue(player)
	}

	override fun getIds(): IntArray {
		return intArrayOf(NPCs.JARVALD_2435, NPCs.JARVALD_2436, NPCs.JARVALD_2437, NPCs.JARVALD_2438)
	}
}
