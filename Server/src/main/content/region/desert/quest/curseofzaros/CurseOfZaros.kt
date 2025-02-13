package content.region.desert.quest.curseofzaros

import core.game.dialogue.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player

/**
 * Curse of Zaros is a miniquest with no quest points or final dialogue
 *
 * Players get the ghostly set for fashionscape.
 */
class CurseOfZaros {
    companion object {
        const val attributePathNumber = "/save:miniquest:curseofzaros-pathnumber" // 1 of 3.
        const val attributeValdezSpoke = "/save:miniquest:curseofzaros-valdezspoke"
        const val attributeRennardSpoke = "/save:miniquest:curseofzaros-rennardspoke"
        const val attributeKharrimSpoke = "/save:miniquest:curseofzaros-kharrimspoke"
        const val attributeLennissaSpoke = "/save:miniquest:curseofzaros-lennissaspoke"
        const val attributeDhalakSpoke = "/save:miniquest:curseofzaros-dhalakspoke"
        const val attributeViggoraSpoke = "/save:miniquest:curseofzaros-viggoraspoke"

        // Ghostly Robes lost: http://youtu.be/YcwYOqfG1Ys

        fun withoutGhostspeak(d: DialogueLabeller) {
            fun label(label: String) { d.label(label) }
            fun loadLabel(player: Player, label: String) { d.loadLabel(player, label) }
            fun player(vararg messages: String) { d.player(ChatAnim.NEUTRAL, *messages) }
            fun npc(vararg messages: String) { d.npc(ChatAnim.NEUTRAL, *messages) }
            fun exec(callback: (player: Player, npc: NPC) -> Unit) { d.exec(callback) }

            label("noghostspeak")
                exec { player, npc ->
                    loadLabel(player, "noghostspeak" + (1..8).random())
                }

            label("noghostspeak1")
                player("Hello there.")
                npc("Wooo? Woooo woo woooooo wooooo woooowooo wooo woooooo woo!")
                player("You don't say?")
                npc("Woo! WOO WOOOOO WOOWOOWOO WOO WOOOOO!")
                player("You don't say!")
                npc("Wooowoowoooooo.... Woowoowoo? Woooo, wooowoo wooowooowooo!")
                player("Well, I guess you didn't say.")

            label("noghostspeak2")
                player("Hello there.")
                npc("Wooo? Woooo woo woooooo wooooo woooowooo wooo woooooo woo!")
                player("Yeah, I don't want to brag, but seriously: I am SOOOO rich....")
                npc("Woo! WOO WOOOOO WOOWOOWOO WOO WOOOOO!")
                player("Yeah, I know they say money doesn't bring you happiness, but sometimes I just like to open my bank account and look at all of the stuff I own and just think to myself:")
                player("Wow. I am soooooo rich.")
                npc("Wooowoowoooooo.... Woowoowoo? Woooo, wooowoo wooowooowooo!")
                player("Well, us rich alive people don't want to waste all day spending time with you poor dead people, because I'm just sooooo rich I have to go now, and very possibly make myself even richer.")
                player("See ya around ghosty!")

            label("noghostspeak3")
                player("Hello there.")
                npc("Wooo? Woooo woo woooooo wooooo woooowooo wooo woooooo woo!")
                player("Yeah, I heard about that, ha-ha-ha!")
                npc("Woo! WOO WOOOOO WOOWOOWOO WOO WOOOOO!")
                player("With a MACKEREL? Ouch!")
                npc("Wooowoowoooooo.... Woowoowoo? Woooo, wooowoo wooowooowooo!")
                player("Well, it was fun. Let's do it again sometime.")

            label("noghostspeak4")
                player("Hello there.")
                npc("Wooo? Woooo woo woooooo wooooo woooowooo wooo woooooo woo!")
                player("Why, thank you very much!")
                npc("Woo! WOO WOOOOO WOOWOOWOO WOO WOOOOO!")
                player("I know, but what are you going to do?")
                npc("Wooowoowoooooo.... Woowoowoo? Woooo, wooowoo wooowooowooo!")
                player("Well, I guess that's always true in the long run. See you around, weird invisible dead person!")

            label("noghostspeak5")
                player("Hello there.")
                npc("Wooo? Woooo woo woooooo wooooo woooowooo wooo woooooo woo!")
                player("Yeah it's not bad, but I prefer cooked chicken.")
                npc("Woo! WOO WOOOOO WOOWOOWOO WOO WOOOOO!")
                player("Maybe, but nothing beats a home cooked pie!")
                player("Man, I love pie!")
                npc("Wooowoowoooooo.... Woowoowoo? Woooo, wooowoo wooowooowooo!")
                player("You don't say? I never knew that. Well, I must be going, see you around.")

            // This is peak 2009 memez
            label("noghostspeak6")
                player("Hello there.")
                npc("Wooo? Woooo woo woooooo wooooo woooowooo wooo woooooo woo!")
                player("We get signal!")
                npc("Woo! WOO WOOOOO WOOWOOWOO WOO WOOOOO!")
                player("Somebody set up us the bomb!")
                npc("Wooowoowoooooo.... Woowoowoo? Woooo, wooowoo wooowooowooo!")
                player("You have no chance to survive make your time.")
                npc("Woo?")
                player("All your base are belong to us.")

            label("noghostspeak7")
                player("Hey, don't think you can talk to me like that!")
                npc("Woo! WOO WOOOOO WOOWOOWOO WOO WOOOOO!")
                player("Are you threatening me?")
                npc("Wooowoowoooooo.... Woowoowoo? Woooo, wooowoo wooowooowooo!")
                player("Just because you're already dead, doesn't mean I can't find a way to hurt you ghosty!")

            label("noghostspeak8")
                player("No, I've never been there in my life and CERTAINLY didn't steal anything when I was!")
                npc("Woo! WOO WOOOOO WOOWOOWOO WOO WOOOOO!")
                player("Are you calling me a liar?!?! I have never stolen a thing in my life, and I resent the implication that I am the kind of morally depraved individual that would steal someone else's hard earned")
                player("money from their very pockets!")
                player("Or cakes. Or fur. Repeatedly. For long periods at a time.")
                npc("Wooowoowoooooo.... Woowoowoo? Woooo, wooowoo wooowooowooo!")
                player("Well if you are going to take that attitude, then I have nothing further to say on the matter, and bid you good day!")
        }


        fun wrongPath(d: DialogueLabeller) {
            fun label(label: String) { d.label(label) }
            fun loadLabel(player: Player, label: String) { d.loadLabel(player, label) }
            fun player(vararg messages: String) { d.player(ChatAnim.NEUTRAL, *messages) }
            fun npc(vararg messages: String) { d.npc(ChatAnim.NEUTRAL, *messages) }
            fun exec(callback: (player: Player, npc: NPC) -> Unit) { d.exec(callback) }

            label("wrongpath")
            exec { player, npc ->
                loadLabel(player, "wrongpath" + (1..7).random())
            }

            label("wrongpath1")
                player("Hello there.")
                npc("The endless tragedy of fate...", "Why must you torment me so?")
                player("Alright, alright, calm down, calm down. All I said was 'hello'!")

            label("wrongpath2")
                player("Hello there.")
                npc("You can see me?")
                player("Uh... yes?")
                npc("And you understand my words?")
                player("Well, most of them...")
                npc("This is incredible! How can such a thing have come to pass?")
                player("What can I say? I'm a professional.")

            label("wrongpath3")
                player("Hello there.")
                npc("Hello back at you.")
                player("So what's a nice ghost like you doing in a place like this?")
                npc("I suppose you think that's funny?")
                player("Well... Mildly amusing I guess.")
                npc("I don't think I want to talk to you anymore.")

            label("wrongpath4")
                player("Hello there.")
                npc("Hello stranger. It is rare indeed that I meet one who can see my presence, let alone one who can understand my words.")
                player("Soooo.... Is it fun being a ghost?")
                npc("Does it look like fun to you?")
                player("Erm... Well, yes actually.")
                npc("Then you are a fool, and I will waste no more words upon you.")
                player("What, not even 'goodbye'?")
                npc(" ")
                player("Sheesh, what a grouch. You'd think you'd have more of a sense of humour being dead and all.")

            label("wrongpath5")
                player("Hello there.")
                npc("Mortal... Take heed of my example, and waste not your life, lest you may suffer the same fate as myself...")
                player("Huh? You mean someone did this to you?")
                npc("You have ascertained the truth in my words...")
                player("So who did it to you? And why?")
                npc("Events of moons past, I remember not clearly...")
                player("Fine help you are then.")

            label("wrongpath6")
                player("Hello there.")
                npc("Hello stranger.")
                player("So.... Invisible ghost haunting the same place for thousands of years, huh?")
                npc("You have no idea...")
                player("Well, bad luck and all that. See ya around!")

            label("wrongpath7")
                player("Hello there.")
                npc("Hello.")
                player("So you're a ghost, huh?")
                npc("Apparently.")
                player("In which case I only have one thing to say to you:")
                npc("...what?")
                player("Guess you don't have the amulet of humanspeak, huh?")
                npc("...huh?")
                player("Yeah, that's right. WOO! WOOOWOOO WOO WOOOOWOO! Got no comeback for that, have you?")
                npc("You are very strange...")

        }
    }
}