package content.region.misthalin.varrock.dialogue

import content.data.Quests
import content.global.skill.summoning.pet.Pet
import content.region.misthalin.varrock.dialogue.GertrudeKittenChooseInterface.Companion.kittens
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Represents the Gertrude dialogue plugin.
 * @author Vexia
 * @author Ported to Kotlin by oftheshire
 */

// TODO: This was ported to Kotlin during Garden of Tranquility quest writing, but it should probably be audited for authenticity. It's still the old Arios dialogue.

class GertrudeDialogue : InteractionListener {
	override fun defineListeners() {
		on(intArrayOf(NPCs.GERTRUDE_780), IntType.NPC, "talk-to") { player, node ->
			DialogueLabeller.open(player, GertrudeDialogueFile(), node.asNpc())
			return@on true
		}
	}
}

class GertrudeDialogueFile : DialogueLabeller() {

	override fun addConversation() {
		exec { player, _ ->
			when (getQuestStage(player, Quests.GERTRUDES_CAT)) {
				0 -> loadLabel(player, "stage_0")
				10 -> loadLabel(player, "stage_10")
				20 -> loadLabel(player, "stage_20")
				30 -> loadLabel(player, "stage_30")
				40 -> loadLabel(player, "stage_40")
				50 -> loadLabel(player, "stage_50")
				60 -> loadLabel(player, "stage_60")
				100 -> loadLabel(player, "stage_100")
				else -> loadLabel(player, "end")
			}
		}

		label("stage_0")
		player("Hello, are you OK?")
		npc("Do I look OK? Those kids drive me crazy.")
		npc("I'm sorry. It's just that I've lost her.")
		player("Lost who?")
		npc("Fluffs, poor Fluffs. She never hurt anyone.")
		player("Who's Fluffs?")
		npc("My beloved feline friends Fluffs. She's been purring by", "my side for almost a decade. Please, could you go", "search for her while I look over the kids?")
		options(
			DialogueOption("stage_0_accept", "Well, I suppose I could."),
			DialogueOption("end", "Sorry, I'm too busy to play pet rescue.")
		)

		label("stage_0_accept")
		npc("Really? Thank you so much! I really have no idea", "where she could be!")
		npc("I think my sons, Shilop and Wilough, saw the cat last.", "They'll be out in the market place.")
		player("Alright then, I'll see what I can do.")
		exec { player, _ ->
			startQuest(player, Quests.GERTRUDES_CAT)
		}
		goto("end")

		label("stage_10")
		player("Hello, Gertrude.")
		npc("Have you seen my poor Fluffs?")
		player("I'm afraid not.")
		npc(ChatAnim.HALF_ASKING, "What about Shilop?")
		player("No sign of him either.")
		npc("Hmmm...strange, he should be at the market.")
		goto("end")

		label("stage_20")
		player("Hello, Gertrude.")
		npc("Hello again, did you manage to find Shilop? I can't keep", "an eye on him for the life of me.")
		player("He does seem quite a handful.")
		npc("You have no idea! Did he help at all?")
		player(ChatAnim.OLD_NORMAL, "I think so, I'm just going to look now.")
		npc(ChatAnim.HAPPY, "Thanks again adventurer!")
		goto("end")

		label("stage_30")
		player("Hello again.")
		npc(ChatAnim.HALF_ASKING, "Hello. How's it going? Any luck?")
		player(ChatAnim.NEUTRAL, "Yes, I've found Fluffs!")
		npc(ChatAnim.HALF_ASKING, "Well well, you are clever!", "Did you bring her back?")
		player(ChatAnim.HALF_WORRIED, "Well, that's the thing, she refuses to leave.")
		npc("Oh dear, oh dear! Maybe she's just hungry.", "She loves doogle sardines but I'm all out.")
		player(ChatAnim.HALF_ASKING, "Doogle sardines?")
		npc(ChatAnim.HALF_ASKING, "Yes, raw sardines seasoned with doogle leaves.", "Unfortunately I've used all my doogle leaves,", "but you may find some in the woods out back.")
		goto("end")

		label("stage_40")
		player("Hello Gertrude.")
		npc("Hi! Did you find fluffs?")
		player("Yes! But she won't follow me.")
		npc("You should try tempting her with a", "seasoned sardine! Those are her favourite snacks.")
		player("Thanks for the advice!")
		goto("end")

		label("stage_50")
		npc(ChatAnim.HALF_WORRIED, "Please bring me my cat back!")
		goto("end")

		label("stage_60")
		player("Hello Gertrude. Fluffs ran off with her kitten.")
		npc("You're back! Thank you! Thank you! Fluffs just came", "back! I think she was just upset as she couldn't find her", "kitten.")
		line("Gertrude gives you a hug.")
		exec { player, npc -> face(npc, player) }
		npc("If you hadn't found her kitten it would have died out", "there!")
		player("That's OK, I like to do my bit.")
		npc("I don't know how to thank you. I have no real material", "possesions. I do have kittens! I can only really look", "after one.")
		player("Well, if it needs a home.")
		npc("I would sell it to my cousin in West Ardougne. I hear", "there's a rat epidemic there. But it's too far.")
		exec { player, _ ->
			if (freeSlots(player) == 0) {
				loadLabel(player, "stage_60_no_space")
			} else {
				loadLabel(player, "stage_60_finish")
			}
		}

		label("stage_60_no_space")
		player("I don't seem to have enough inventory space.")
		npc("Good good, See you again.")
		goto("end")

		label("stage_60_finish")
		npc("Here you go, look after her and thank you again!")
		npc("Oh by the way, the kitten can live in your backpack,", "but to make it grow you must take it out and feed and", "stroke it often.")
		line("Gertrude gives you a kitten.")
		exec { player, npc ->
			end()
			resetFace(npc) // Some weird bug where Gertrude stays stuck facing you if you don't resetFace() her. Maybe a race condition between end() and finishQuest()?
			sendMessage(player, "...and some food!")
			finishQuest(player, Quests.GERTRUDES_CAT)
		}
		goto("end")

		// buying kittens after the quest
		label("stage_100")
		player("Hello again.")
		npc(ChatAnim.HALF_ASKING, "Hello my dear. How's things?")

		options(
			DialogueOption("stage_100_fine", "I'm fine, thanks."),
			DialogueOption("stage_100_kittens", "Do you have any more kittens?")
		)

		label("stage_100_fine")
		npc("Good good, See you again.")
		goto("end")

		label("stage_100_kittens")
		exec { player, _ ->
			var hasKitten = false

			// kitten as familiar
			if (player.familiarManager.hasFamiliar() && player.familiarManager.hasPet()) {
				val pet = player.familiarManager.familiar as Pet
				if (pet.itemId in kittens) {
					hasKitten = true
				}
			}

			// kitten in bank
			if (hasAnItem(player, kittens.toTypedArray(), false).exists()) {
				hasKitten = true
			}

			if (hasKitten) {
				loadLabel(player, "has_kitten_reject")
			} else {
				loadLabel(player, "offer_kitten")
			}
		}

		label("has_kitten_reject")
		npc("Aren't you still raising that other kitten? Only once it's", "fully grown and it no longer needs your attention will", "I let you have another kitten.")
		goto("end")

		label("offer_kitten")
		npc("Indeed I have. They are 100 coins each, do you want", "one?")
		options(
			DialogueOption("adoption_check", "Yes please.", skipPlayer = true),
			DialogueOption("end", "No thanks."),
			DialogueOption("adoption_check_charos", "[Charm] Ask to choose your own kitten.",
				spokenText = "Could I choose my own kitten?", ChatAnim.ASKING) { player, _ ->
				inEquipment(player, Items.RING_OF_CHAROSA_6465)
			}
		)

		label("adoption_check")
		exec { player, _ ->
			val hasMoney = inInventory(player, Items.COINS_995, 100)
			if (!hasMoney) {
				sendMessage(player, "You need 100 coins to buy a kitten.")
				loadLabel(player, "end")
			} else {
				val hasExtraMoney = inInventory(player, Items.COINS_995, 101)
				val hasSpace = freeSlots(player) > 0 || !hasExtraMoney
				if (player.familiarManager.hasFamiliar() && !hasSpace) {
					sendMessage(player, "You don't have enough inventory space.")
					loadLabel(player, "end")
				} else {
					loadLabel(player, "buy_kitten")
				}
			}
		}

		label("adoption_check_charos")
		exec { player, _ ->
			val hasMoney = inInventory(player, Items.COINS_995, 100)
			if (!hasMoney) {
				sendMessage(player, "You need 100 coins to buy a kitten.")
				loadLabel(player, "end")
			} else {
				val hasExtraMoney = inInventory(player, Items.COINS_995, 101)
				val hasSpace = freeSlots(player) > 0 || !hasExtraMoney
				if (player.familiarManager.hasFamiliar() && !hasSpace) {
					sendMessage(player, "You don't have enough inventory space.")
					loadLabel(player, "end")
				} else {
					loadLabel(player, "buy_kitten_choose")
				}
			}
		}

		label("buy_kitten")
		player("Yes please.")
		npc("OK then, here you go.")
		player("Thanks.")
		exec { player, _ ->
			if (removeItem(player, Item(Items.COINS_995, 100))) {
				loadLabel(player, "receive_kitten")
			} else {
				loadLabel(player, "end")
			}
		}

		label("receive_kitten")
		line("Gertrude gives you another kitten.")
		exec { player, _ ->
			val kitten = Item(RandomFunction.random(Items.PET_KITTEN_1555, Items.PET_KITTEN_1560 + 1))
			if (player.familiarManager.hasFamiliar()) {
				addItem(player, kitten.id)
			} else {
				player.familiarManager.summon(kitten, true, false)
			}
		}
		goto("end")

		label("buy_kitten_choose")
		npc("Well okay, but they're all just as cuddly as each other.")
		exec { player, _ ->
			end()
			openInterface(player, Components.KITTEN_PICK_737)
		}
	}
}