package content.region.asgarnia.falador.dialogue

import content.region.asgarnia.falador.quest.recruitmentdrive.RecruitmentDrive
import content.region.asgarnia.falador.quest.recruitmentdrive.SirTiffyCashienDialogueFile
import core.ServerConstants
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class SirTiffyCashienDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {

        // Completed Recruitment Drive & Start Wanted!! Quest
        if (isQuestComplete(player!!, RecruitmentDrive.questName)) {
            openDialogue(player, SirTiffyCashienAfterRecruitmentDriveQuestDialogueFile(), npc)
            return true
        }

        // Recruitment Drive Quest
        if (isQuestInProgress(player!!, RecruitmentDrive.questName, 1, 99)) {
            openDialogue(player, SirTiffyCashienDialogueFile(), npc)
            return true
        }

        // Fallback to default.
        when (stage) {
            START_DIALOGUE -> player("Hello.").also { stage++ }
            1 -> npc(FacialExpression.FRIENDLY, "What ho, ${if (player.isMale) "sirrah" else "milady"}.", "Spiffing day for a walk in the park, what?").also { stage++ }
            2 -> player(FacialExpression.THINKING, "Spiffing?").also { stage++ }
            3 -> npc(FacialExpression.FRIENDLY, "Absolutely, top-hole!", "Well, can't stay and chat all day, dontchaknow!", "Ta-ta for now!").also { stage++ }
            4 -> player(FacialExpression.THINKING, "Erm...goodbye.").also { stage = END_DIALOGUE }
        }

        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return SirTiffyCashienDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_TIFFY_CASHIEN_2290)
    }
}

// Move this to Wanted!! Quest.
class SirTiffyCashienAfterRecruitmentDriveQuestDialogueFile : DialogueBuilderFile() {
    private fun dialogueChangeSpawnPoint(builder: DialogueBuilder, place: String, location: Location, tiffyLine1: String, tiffyLine2: String): DialogueBuilder {
        return builder.npcl("${tiffyLine1} Are you sure?")
            .options().let { optionBuilder ->
                optionBuilder.option("Yes, I want to respawn in $place.")
                    .playerl("Yes, I want to respawn in $place.")
                    .npcl(tiffyLine2)
                    .endWith { _, player ->
                        setAttribute(player, "/save:spawnLocation", location)
                        player.properties.spawnLocation = location
                    }
                optionBuilder.option("Actually, no thanks. I like my respawn point.")
                    .playerl("Actually, no thanks. I like my respawn point.")
                    .npcl("As you wish, what? Ta-ta for now.")
            }
    }

    override fun create(b: DialogueBuilder) {
        b.onPredicate { _ -> true }
                .npc(FacialExpression.HAPPY, "What ho, @g[sirrah,milady].", "Jolly good show on the old training grounds thingy,", "what?")
                .options().let { optionBuilder ->
                    optionBuilder.option_playerl("Do you have any jobs for me yet?")
                            .npcl("Sorry dear @g[boy,gal] but we are still in the process of organising.")
                            .npcl("I'm sure that we will have something for you soon, so please feel free to check back later.")
                            // Started of Wanted! quest
                            .end()
                    optionBuilder.option("Can you explain the Gaze of Saradomin to me?")
                            .playerl("I don't really understand this 'Gaze of Saradomin' thing... Do you think you could explain what it does for me?")
                            .npcl("Certainly @g[sirrah,milady]! As you know, we Temple Knights are personally favoured by Saradomin himself.")
                            .npcl("And when I say personally favoured, I don't mean that sometime off in the future he's going to buy us all a drink!")
                            .npcl("He watches over each of us, and when we die he catches us as we fall, and ensures we arrive back at Falador castle safe and sound.")
                            .npcl("We usually lose some equipment when he does so, but it's a small price to pay to be hale and hearty again, what?")
                            .npcl("Some lucky fellows have a similar system going already, but when they die they spawn in that squalid little swamp village Lumbridge.")
                            .playerl("Yeah, what kind of person would want to spawn there... Certainly not me, and I never have! Honest!")
                            .npcl("Well, you should be glad that we offer you a step up then! Falador is clearly a far superior town to spend your time in!")
                            .npcl("Was there something else you wanted to ask good old Tiffy, @g[sirrah,milady]?")
                            .end()
                    optionBuilder.option("Can I buy some armour?")
                            .playerl("Can I buy some armour?")
                            // Recruitment Drive -> Initiate level, Slug Menace -> Proselyte level
                            .npcl("Of course dear @g[boy,gal]. I can sell you up to Initiate level items only I'm afraid.")
                            .endWith { _, player ->
                                openNpcShop(player, npc!!.id)
                            }
                    optionBuilder.option("Can I switch respawns please?")
                            .branch { player -> if (player.properties.spawnLocation == ServerConstants.HOME_LOCATION) { 1 } else { 0 } }
                            .let { branch ->
                                dialogueChangeSpawnPoint(
                                    branch.onValue(1),
                                    "Falador", Location(2971, 3340, 0), //https://www.youtube.com/watch?v=Mm15dHuIaVg
                                    "Ah, so you'd like to respawn in Falador, the good old homestead!",
                                    "Top-hole, what? Good old Fally is definitely the hot-spot nowadays!"
                                )
                                dialogueChangeSpawnPoint(
                                    branch.onValue(0),
                                    "Lumbridge", ServerConstants.HOME_LOCATION ?: Location(3222, 3218, 0),
                                    "What? You're saying you want to respawn in Lumbridge?",
                                    "Why anyone would want to visit that smelly little swamp village of oiks is quite beyond me, I'm afraid, but the deed is done now."
                                )
                            }
                    optionBuilder.option("Goodbye.")
                            .playerl("Well, see you around Tiffy.")
                            .npcl(FacialExpression.HAPPY,"Ta-ta for now, old bean!")
                            .end()
                }
    }
}
