package content.region.desert.quest.curseofzaros

import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class MysteriousGhostRennardDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return MysteriousGhostRennardDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MysteriousGhostRennardDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(2382, 2383, 2384)
    }
}

class MysteriousGhostRennardDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, npc ->
            if (inEquipment(player, Items.GHOSTSPEAK_AMULET_552) || inEquipment(player, Items.GHOSTSPEAK_AMULET_4250)){
                if (2381 + getAttribute(player, CurseOfZaros.attributePathNumber, 0) == npc.id) {
                    if (getAttribute(player, CurseOfZaros.attributeRennardSpoke, false)) {
                        if (inInventory(player, Items.GHOSTLY_GLOVES_6110) || inEquipment(player, Items.GHOSTLY_GLOVES_6110)) {
                            loadLabel(player, "subsequenttime")
                        } else {
                            loadLabel(player, "lostghostlything")
                        }
                    } else {
                        loadLabel(player, "firsttime")
                    }
                } else {
                    loadLabel(player, "wrongpath")
                }
            } else {
                loadLabel(player, "noghostspeak")
            }
        }

        CurseOfZaros.withoutGhostspeak(this)

        CurseOfZaros.wrongPath(this)

        label("firsttime")
            player("Hello. You must be Rennard.")
            npc("What be this? You both see me and hear me, and also know my name?")
            npc("Tell me what devilry brings you here, and be quick about it afore I gut you like a fish!")
            player("Well, apart from the fact I ain't scared of no ghost, I am here because I have spoken to Valdez.")
            npc("Valdez? Who be that? Some foul necromancer?")
            player("No, he was a ghost I met near Glarial's tomb.")
            player("He seems convinced that the artefact you stole from him is responsible for him becoming cursed to be an invisible ghost.")
            player("Seems like he might be onto something too, given the state of you.")
            npc("A curse ye say... Aye, that makes sense...")
            npc("And there was I thinking my fate be the fault of the thieving and murdering I spent me life a-doing...")
            npc("So it all began the day I stole that staff, ye say? Aye, that be a story I have never told another soul...")
            options(
                DialogueOption("tellmeyourstory", "Tell me your story", skipPlayer = true),
                DialogueOption("goodbye", "Goodbye then", skipPlayer = true),
            )

        label("goodbye")
            player("Well, that's all fascinating, but I just don't particularly care. Bye-bye.")

        label("tellmeyourstory")
            player("Why don't you tell me what happened? I might be able to help...")
            npc("Well, I was making me merry way along, having just pulled off a glorious jewellery heist from a bunch of stinking dwarves...")
            player("Hey, that's no way to talk about dwarves! Some of my best friends are short!")
            npc("Ah, yer misunderstand me [lad/lass], I wasn't generalising about the whole dwarf species, I had just stolen a bundle of jewels from a very specific group of dwarves who happened to have an odious stench about them!")
            player("Oh. Well I guess that's okay then. Please continue.")
            npc("Well, as I headed on me merry way, hoping the foul odours that lingered in me nostrils would soon pass, I see in front of me this explorer fella, all decked out in in his fine clothing, and carrying some long package")
            npc("bundled in rags.")
            npc("So I says to meself, 'Rennard', I says, 'Rennard, why would some fella all dressed in his finery be carrying something wrapped in dirty rags?'.")
            npc("So I thinks to meself a little more, 'Rennard', I thinks, 'Rennard, maybe that fella has something valuable in there, and he covered it in dirty rags so it don't look so valuable'.")
            npc("So I coshed this fella round the back of his head with me bag of jewels, picked up his package and was on me merry way afore he comes to.")
            player("So what happened then?")
            npc("Well, I makes me way to the closest tavern I knew of that catered to my sort of people...")
            player("You mean thieves?")
            npc("Right ye are, so I makes me way to the nearest friendly tavern, and unwraps the bundle to see what it had inside.")
            player("The Staff of Armadyl?")
            npc("Was it? Ah, I never knew that...")
            npc("Anysways, I unwraps this staff, and sees it be a god- weapon; I may be just a common thief, but I recognises a weapon not made by mortal hands when I sees one.")
            player("So what did you do then?")
            npc("Well, I knew such a weapon would be of great value to...")
            npc("Now that's funny. Can't remember his name, now. The powerful god, lived in the North-east. Took the Mahjarrat away from under Icthlarin's")
            npc("control.")
            npc("Anyway, I hired me a messenger to go off and let him know I had something I was prepared to sell that I thought he'd be interested in...")
            npc("Now WHY can't I remember his name? Very odd that...")
            player("So you sold the staff to this god you can't remember?")
            npc("Well, that's the other funny thing... He never showed up, he sent some General or other instead.")
            npc("Hmmm... You know... Thinking back on that, I'm getting the feeling that messenger did a little doublecross of his own, and took")
            npc("me message to the wrong fella.")
            player("So what was this General's name?")
            npc("His name was Zamorak. I remember thinking at the time it was odd, because the fella was a mighty powerful warrior, but was never fully trusted by...")
            npc("WHY can't I remember his name???")
            player("So you suspect the messenger might have taken the message to the wrong person? So you think it was an accident or deliberate?")
            npc("Well that I can't tell ya, but if something happened to get me cursed, it's likely the messenger would know what more than me.")
            npc("His name was Kharrim, and if he caused me to be stuck like this, I'm gonna fillet him like a dog, ghost or no!")
            npc("I tell ye what, you've given me much to think about so I'd like to offer yer a gift; Here, take these, they were the gloves I stole me first cake with, they might bring yer some luck.")
            exec { player, npc ->
                setAttribute(player, CurseOfZaros.attributeRennardSpoke, true)
                addItemOrDrop(player, Items.GHOSTLY_GLOVES_6110)
            }
            player("Where can I find this Kharrim then?")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("subsequenttime")
            player("Hello again Rennard.")
            npc("Ah, it be you again! What can I do fer ya?")
            player("Can you tell me where I can find Kharrim again?")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("curseofzaros1")
            npc("Kharrim the messenger... Well, he was always a devoted follower of old General Zamorak, and if I remember rightly Zamorak set up a small base in an old temple near Dareeyak...")
            npc("You might want to check around there.")
            player("Okay, well I'll try and find him for you then.")

        label("curseofzaros2")
            npc("Kharrim the messenger... Last I'd heard of him, he'd headed off to Carrallagar to seek his fortune. Ya might want to check around there somewhere.")
            player("Okay, well I'll try and find him for you then.")

        label("curseofzaros3")
            npc("Kharrim the messenger... The last I'd heard of that weasel he was claiming he'd found some underground deposit of runite ore guarded by demons and dragons.")
            npc("I suspect he was pulling some scam or other, but if you know of such a place, that might be a good place to start checking.")
            player("Okay, well I'll try and find him for you then.")


        label("lostghostlything")
            player(ChatAnim.SAD, "I lost those gloves you gave me...", "Can I have some more please?")
            exec { player, npc ->
                addItemOrDrop(player, Items.GHOSTLY_GLOVES_6110)
            }
            npc(ChatAnim.SAD, "It seems as though the curse that keeps me here", "extends to my very clothing...")
            npc("Here, take them, some evil power returned them to", "me...")

    }
}