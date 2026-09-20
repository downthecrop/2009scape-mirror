package content.minigame.greatorbproject

import content.minigame.greatorbproject.OrbProjUtils.guildStartLoc
import core.api.Container
import core.api.EquipmentSlot
import core.api.findLocalNPC
import core.api.replaceSlot
import core.game.bots.AIPBuilder
import core.game.bots.AIPlayer
import core.game.bots.GeneralBotCreator
import core.game.bots.Script
import core.game.bots.ScriptAPI
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import kotlin.math.abs

/**
 * A bot for the Great Orb Project.
 */
class OrbProjBot : Script() {

    private var state = State.LOBBY
    private var gameCompleted = false

    // API for bot
    private val api by lazy { ScriptAPI(bot) }

    // flags to set if bots can repel orbs or place barriers
    private val canRepel = true
    private val canBarrier = true

    private var chatDelay = RandomFunction.random(100, 200)

    // ID for bots' equipment, set to specific team values
    private var attractorId = -1
    private var repellerId = -1
    private var barrierGenId = -1

    // sometimes the clanker is a slacker lmao
    private var roundTracker = -1
    private var isAlchingThisRound = false
    private var alchActionDelay = 0

    // variables to keep bot on task and not distracted every tick
    private var currentTaskDuration = 0
    private var currentTarget: NPC? = null
    private var currentActionType = -1 // 0 = attract, 1 = repel

    // ---------------------------------------------------------------------------
    // bot lifecycle tick
    // ---------------------------------------------------------------------------

    override fun tick() {
        val session = OrbProjSession.active

        // removes bot if game is done
        // In the lobby, session is null and gameCompleted is false.
        // During the game session is not null and gameCompleted is true.
        // After the last round, gameCompleted is true and session is null.
        // The session gets set to null if all the players leave and only bots are remaining.
        if (session == null && (gameCompleted || gameTerminated)) {
            this.running = false
            return
        }

        // alch delay counter
        if (alchActionDelay > 0) {
            alchActionDelay--
            return
        }

        // bot should run
        bot.walkingQueue.isRunning = true

        // set the bot equipment IDs
        if (attractorId == -1) setItemIds()

        // send messages from time to time
        if (chatDelay-- <= 0) {
            sayRandomLine()
            chatDelay = RandomFunction.random(200, 500)
        }

        // find what state the game is in
        state = when {
            session == null -> State.LOBBY
            session.isWaitingForNextRound -> State.FIND_PORTAL
            else -> {
                gameCompleted = true
                State.PLAYING
            }
        }

        // roll for a 2% chance the bot is useless this round
        if (session != null && session.currentAltarIndex != roundTracker) {
            roundTracker = session.currentAltarIndex
            isAlchingThisRound = RandomFunction.random(100) < 2
        }

        // choose what to do
        when (state) {
            // loiter in lobby
            State.LOBBY -> { api.randomWalkTo(guildStartLoc, 4) }

            // go to next altar
            State.FIND_PORTAL -> {
                val portal = api.getNearestNode(NPCs.PORTAL_8019, false) ?: return
                api.interact(bot, portal, "enter")
            }

            // play game
            State.PLAYING -> {
                if (isAlchingThisRound) {
                    handleHighAlch()
                } else {
                    botAction(session!!)
                }
            }
        }
    }

    // ---------------------------------------------------------------------------
    // main bot logic for deciding what to do
    // ---------------------------------------------------------------------------

    private fun botAction(session: OrbProjSession) {

        // get team. if there is no team, return early
        val team = bot.getAttribute(OrbProjUtils.ATTR_GOP_TEAM, Team.NONE)
        if (team == Team.NONE || attractorId == -1) return

        // get locations
        val scoreZone = session.altarScoreZones[session.currentAltarIndex]
        val altarCenter = scoreZone?.let {
            Location.create((it.northEastX + it.southWestX) / 2, (it.southWestY + it.northEastY) / 2, 0)
        }

        // get nearest team and enemy orbs
        val teamOrb = api.getNearestNode(if (team == Team.GREEN) "Green orb" else "Yellow orb") as? NPC
        val enemyOrb = api.getNearestNode(if (team == Team.GREEN) "Yellow orb" else "Green orb") as? NPC

        // find the nearest barrier
        val barrier = api.getNearestNode("barrier")

        // continue current task
        if (currentTaskDuration > 0
            && currentTarget != null
            && currentTarget!!.isActive) {
            if (currentTarget!!.location == null
                || currentTarget!!.id !in OrbProjUtils.greenOrbs + OrbProjUtils.yellowOrbs) {
                currentTaskDuration = 0
            } else {
                currentTaskDuration--
                when (currentActionType) {
                    0 -> attractOrb(currentTarget!!, altarCenter)
                    1 -> if (canRepel) repelOrb(currentTarget!!, altarCenter)
                }
                return
            }
        }

        // roll for initiative.
        // 70% chance attract
        // 15% chance to repel
        // 5% chance move towards altar center
        // 5% chance to create barrier
        // 5% chance to destroy barrier
        val action = RandomFunction.random(100)

        // sets the bots task, target, and duration where applicable
        if (teamOrb != null && enemyOrb != null) {
            when (action) {
                in 0..69 -> { // attract
                    currentTarget = teamOrb
                    currentActionType = 0
                    currentTaskDuration = RandomFunction.random(6, 7)
                    attractOrb(teamOrb, altarCenter)
                }
                in 70..84 -> { // repel
                    if (canRepel) {
                        currentTarget = enemyOrb
                        currentActionType = 1
                        currentTaskDuration = RandomFunction.random(6, 7)
                        repelOrb(enemyOrb, altarCenter)
                    }
                }
                in 85..89 -> { // move to some random location
                    if (altarCenter != null) {
                        val diffX = RandomFunction.random(-4, 4)
                        val diffY = RandomFunction.random(-4, 4)
                        api.walkTo(altarCenter.transform(diffX, diffY, 0))
                    }
                    currentTaskDuration = 3
                }
                in 90..94 -> { // create barrier at current location
                    if (canBarrier) {
                        api.interact(bot, Item(barrierGenId), "make-barrier")
                    }
                }
                in 95..100 -> { // try to destroy nearest barrier
                    if (canBarrier) {
                        api.interact(bot, barrier, "destroy")
                    }
                }
            }
        }
    }

    // ---------------------------------------------------------------------------
    // attract an orb toward the altar
    // ---------------------------------------------------------------------------

    // tracks how many consecutive ticks bot has been locked onto this orb without it moving. used to trigger a reposition.
    private var attractTicksWithoutProgress = 0
    private var lastOrbLocation: Location? = null

    private fun attractOrb(baseOrb: NPC, altarCenter: Location?) {
        // equip the attract wand
        equipWand(attractorId)

        // find the orb visible to the bot based on its equipped wand
        val team = bot.getAttribute(OrbProjUtils.ATTR_GOP_TEAM, Team.NONE)
        val wantedId = if (team == Team.GREEN) OrbProjUtils.greenOrbs[1] else OrbProjUtils.yellowOrbs[1]
        val orb = getVisibleOrb(baseOrb, wantedId)

        // prevent interacts with the inactive orbs
        if (orb.id == NPCs.GREEN_ORB_8026 || orb.id == NPCs.YELLOW_ORB_8022) {
            return
        }

        val orbLoc = orb.location
        val dist   = api.distance(bot, orb)
        val hasLOS = CombatSwingHandler.isProjectileClipped(bot, orb, false)

        // if bot has no LOS or too far, strafe tangentially to find LOS
        if (!hasLOS || dist > 10) {
            // build a vector from the bot toward the orb, then take the perpendicular
            val dx = orbLoc.x - bot.location.x
            val dy = orbLoc.y - bot.location.y
            // pick one of the perpendicular candidates (+ or -)
            val (perpX, perpY) = if (RandomFunction.nextBool()) Pair(dy, -dx) else Pair(-dy, dx)
            val norm = maxOf(abs(perpX), abs(perpY), 1)
            // strafe 2 tiles in the chosen direction
            val strafe = bot.location.transform((perpX / norm) * 2, (perpY / norm) * 2, 0)
            api.walkTo(strafe)
            attractTicksWithoutProgress = 0
            return
        }

        // if the orb is close, move towards altar and try to attract it again
        if (dist <= 1.5 && altarCenter != null) {
            val dx = (altarCenter.x - orbLoc.x).coerceIn(-1, 1)
            val dy = (altarCenter.y - orbLoc.y).coerceIn(-1, 1)
            // walk 3 tiles past the altar center so the bot drags the orb through the scoring zone
            val pullTarget = altarCenter.transform(dx * 3, dy * 3, 0)
            if (bot.location.getDistance(pullTarget) > 1) {
                api.walkTo(pullTarget)
                attractTicksWithoutProgress = 0
                return
            }
        }

        // reposition if the orb hasn't moved in ~6 ticks
        if (orbLoc == lastOrbLocation) {
            attractTicksWithoutProgress++
        } else {
            lastOrbLocation = orbLoc
            attractTicksWithoutProgress = 0
        }

        if (attractTicksWithoutProgress >= 6) {
            attractTicksWithoutProgress = 0
            // walk to a random offset around the orb to try again
            val diffX = RandomFunction.random(-3, 4)
            val diffY = RandomFunction.random(-3, 4)
            api.walkTo(orbLoc.transform(diffX, diffY, 0))
            return
        }

        // attract
        api.interact(bot, orb, "attract")
    }

    // ---------------------------------------------------------------------------
    // repel an enemy orb away from the altar
    // ---------------------------------------------------------------------------

    private fun repelOrb(baseOrb: NPC, altarCenter: Location?) {
        // equip the repel wand
        equipWand(repellerId)

        // find the orb visible to the bot based on its equipped wand
        val team = bot.getAttribute(OrbProjUtils.ATTR_GOP_TEAM, Team.NONE)
        val wantedId = if (team == Team.GREEN) OrbProjUtils.greenOrbs[2] else OrbProjUtils.yellowOrbs[2]
        val orb = getVisibleOrb(baseOrb, wantedId)

        // prevent interacts with the inactive orbs
        if (orb.id == NPCs.GREEN_ORB_8026 || orb.id == NPCs.YELLOW_ORB_8022) {
            return
        }

        // walk towards altar center
        val diffX = RandomFunction.random(-2, 3)
        val diffY = RandomFunction.random(-2, 3)
        if (altarCenter != null) {
            api.walkTo(altarCenter.transform(diffX, diffY, 0))
        }

        // repel
        api.interact(bot, orb, "repel")
    }

    // find the actual layer in the stack of orbs that is currently visible to this bot
    private fun getVisibleOrb(baseOrb: NPC, wantedOrbId: Int): NPC {
        return findLocalNPC(bot, wantedOrbId) ?: baseOrb
    }

    // ---------------------------------------------------------------------------
    // helper functions, etc.
    // ---------------------------------------------------------------------------

    // send a chat message. 100 lines of bespoke artisanal text!
    private fun sayRandomLine() {
        val lines = listOf(
            "Go green team!",
            "Yellow FTW!",
            "Vief is my daddy.",
            "C'mon, work harder everyone.",
            "Don't let them score!",
            "Just grinding out tokens ig.",
            "O R B   I S   L O V E",
            "Can't wait for the nat altar",
            "Are we blind?! Deploy the barriers!",
            "I use arch btw",
            "Shit I just spilled cool ranch doritos all over my desk.",
            "I am recording this for youtube. Say hi!",
            "Do you even know how to play this game?",
            "Runecrafting is my favourite skill.",
            "Someone tell Thrumbo to join my team pls.",
            "Ugh I have so much homework to do.",
            "I can't believe I'm doing this bad.",
            "Gonna run some laws later.",
            "Guys I can't see my cat just hopped up.",
            "Gonna play some Halo 3 later.",
            "You just lost the game.",
            "Split this game 50:50 pls, I need the tokens.",
            "I just love the little rc sparkle sound.",
            "I died in the abyss last week :(",
            "Pwned",
            "My mom is gonna drive me to the mall later.",
            "What is your favourite runecrafting altar?",
            "Help I am trapped in this game.",
            "Can't wait until they add dungeoneering :)",
            "Wtfffff get out of my way noob",
            "My cousin's acc got hacked last week ._.",
            "I'm going to sell these runes to buy more spirit shards.",
            "A q p",
            "     W",
            "Yo I'm selling some ess in the ge, please buy it.",
            "Why does the rune ess mine look like ice cream?",
            "When is next star?",
            "Anyone know where last star dropped?",
            "Remember to get your penguins for the week, guys!",
            "Fishing in Catherby is such nostalgia.",
            "Man, New Year's Eve 2008 party was wild.",
            "You think they'll ever make a Halo 4?",
            "Go check the gitlab and do some testing!",
            "Hit me up on myspace.",
            "Just set up a geocities weblog last week",
            "So how long you all been playing this game?",
            "Buying gf",
            "So random xd",
            "Green team just stooooop a moment.",
            "It's killing me how bad you are at this game.",
            "Go back to Slayer, bruh. You plainly aren't smart enough for this game.",
            "Oops wc",
            "Join my clan chat!",
            "I have a picture of Andrew Gower hung up in my living room.",
            "I can't believe it's 2009 already",
            "Who wants to go gwd later?",
            "Nothing interesting happens.",
            "I've only got a few more minutes here so let's pick up the pace.",
            "I'm playing on the library computers!",
            "Anyone got a spare fish, my kitten needs feeding",
            "B sale",
            "Stop touching my orb!",
            "Just score the points, how hard can it be?",
            "Can't believe it takes so much ess to get 99 on this skill.",
            "Gonna go questing later.",
            "Brb the pizza rolls just warmed up",
            "I've never lost a game of orb proj!",
            "I am undefeated at this!",
            "69. Nice.",
            "Can't believe my grandma just friended me on facebook.",
            "The best summoning familiar is the Spirit Guthatrice. Any other opinion is wrong.",
            "What y'all drinking today. Baja Blast mtdew ftw.",
            ":D",
            "God save the queen.",
            "What monster would trade a cat for death runes????",
            "1v1 me deep wildy, bro.",
            "Gonna take my dog on a walk later.",
            "Reported",
            "You think they'll ever get wgs added to this game?",
            "Have you seen kpop demon hunters that shit is insane.",
            "Chop chop!",
            "Yum!",
            "New season of Game of Thrones coming out soon!",
            "What is even up with these orbs?",
            "When this game ends I'll just be banished back to the void :(",
            "Lets go play Castle Wars!",
            "Join my cc, it's where the cool kids hang out.",
            "Y2k was actually only 9 years ago.",
            "I swear if you put up another barrier there...",
            "Git gud",
            "Ugh",
            "Acantha could really just lighten up, you know.",
            "Press F to pay respects.",
            "I spend all my tokens on mind talismans. I'm hoping it makes me smarter.",
            "That is some sick drip you got on.",
            "Wake me up when this round is over. I just want those tokens.",
            "Someone pls lend me sgs.",
            "Abyssal Whip is overrated.",
            "Dark Mystic? More like Dank Mystic.",
            "So how many cats do you all have?",
            "I'm alive! I'm aliiiiive!!!!!!!!11",
        )
        api.sendChat(lines.random())
    }

    // sets equipment IDs based on the team. used to swap wands and create barriers
    private fun setItemIds() {
        val team = bot.getAttribute(OrbProjUtils.ATTR_GOP_TEAM, Team.NONE)
        if (team == Team.GREEN) {
            attractorId  = Items.GREEN_ATTRACTOR_13645
            repellerId   = Items.GREEN_REPELLER_13646
            barrierGenId = Items.GREEN_BARRIER_GENERATOR_13647
        } else if (team == Team.YELLOW) {
            attractorId  = Items.YELLOW_ATTRACTOR_13643
            repellerId   = Items.YELLOW_REPELLER_13644
            barrierGenId = Items.YELLOW_BARRIER_GENERATOR_13648
        }
    }

    // makes sure the correct wand is equipped for the action
    private fun equipWand(wantedId: Int) {
        replaceSlot(bot, EquipmentSlot.WEAPON.ordinal, Item(wantedId), container = Container.EQUIPMENT)
    }

    // sometimes the bot just wants to afk the game and alch
    private fun handleHighAlch() {
        bot.visualize(Animation(ALCH_ANIM), Graphics(ALCH_GFX))
        // random excuse
        if (RandomFunction.random(15) == 0) {
            val excuses = listOf(
                "Hold on I gotta alch these bows.",
                "Lag",
                "One sec mom is calling me.",
                "Brb",
                "You guys got this!",
                "Hold on",
                "Y'all got this round for me."
            )
            api.sendChat(excuses.random())
        }
        alchActionDelay = 6
    }

    override fun newInstance(): Script = OrbProjBot()

    // states the bot can have
    internal enum class State {
        LOBBY,
        FIND_PORTAL,
        PLAYING
    }

    companion object {
        var gameTerminated = false
        const val ALCH_ANIM = 713
        const val ALCH_GFX = 113

        // makes a new bot
        fun newBot(team: Team){
            val bot = spawn(guildStartLoc)
            OrbProjLobby.joinLobby(bot, team)
        }

        // various outfits. possible that some bots might not roll the stats to use them all
        private val botOutfits = listOf(
            intArrayOf(Items.MYSTIC_ROBE_BOTTOM_4104, Items.MYSTIC_ROBE_TOP_4101, Items.MYSTIC_BOOTS_4107, Items.MYSTIC_GLOVES_4105, Items.MAGIC_CAPE_9762), // dark mystic robes
            intArrayOf(Items.MYSTIC_ROBE_BOTTOM_4093, Items.MYSTIC_ROBE_TOP_4091, Items.MYSTIC_BOOTS_4097, Items.MYSTIC_GLOVES_4095, Items.MAGIC_CAPET_9763), // light mystic robes
            intArrayOf(Items.MYSTIC_ROBE_BOTTOM_4113, Items.MYSTIC_ROBE_TOP_4111, Items.MYSTIC_BOOTS_4117, Items.MYSTIC_GLOVES_4115, Items.MAGIC_CAPE_9762), // white mystic robes
            intArrayOf(Items.MONKS_ROBE_542, Items.MONKS_ROBE_544, Items.HOLY_SYMBOL_1718), // monk
            intArrayOf(Items.WIZARD_ROBE_577, Items.BLUE_SKIRT_1011, Items.CAPE_1007, Items.WIZARD_BOOTS_2579, Items.CHAOS_GAUNTLETS_777), // blue wizard
            intArrayOf(Items.PRIEST_GOWN_426, Items.PRIEST_GOWN_428, Items.LEATHER_GLOVES_1059, Items.LEATHER_BOOTS_1061, Items.CAPE_1019),
            intArrayOf(Items.RUNECRAFTER_ROBE_13614, Items.RUNECRAFTER_SKIRT_13617, Items.RUNECRAFTER_GLOVES_13618, Items.RUNECRAFT_CAPE_9765), // yellow runecrafter robes
            intArrayOf(Items.RUNECRAFTER_ROBE_13619, Items.RUNECRAFTER_SKIRT_13622, Items.RUNECRAFTER_GLOVES_13623, Items.RUNECRAFT_CAPET_9766), // yellow runecrafter robes
            intArrayOf(Items.GHOSTLY_ROBE_6107, Items.GHOSTLY_ROBE_6108, Items.GHOSTLY_BOOTS_6106, Items.GHOSTLY_GLOVES_6110, Items.GHOSTLY_CLOAK_6111), // ghostly robes
            intArrayOf(Items.SPLITBARK_BODY_3387, Items.SPLITBARK_LEGS_3389, Items.SPLITBARK_BOOTS_3393, Items.SPLITBARK_GAUNTLETS_3391, Items.TEAM_15_CAPE_4343), // splitbark
            intArrayOf(Items.LUNAR_AMULET_9102, Items.LUNAR_BOOTS_9100, Items.LUNAR_CAPE_9101, Items.LUNAR_GLOVES_9099, Items.LUNAR_LEGS_9098, Items.LUNAR_TORSO_9097), // lunar
            intArrayOf(Items.WIZARD_ROBE_G_7390, Items.BLUE_SKIRT_G_7386, Items.BOOTS_OF_LIGHTNESS_88, Items.CAPE_OF_LEGENDS_1052), // wizard g
            intArrayOf(Items.ZURIELS_ROBE_BOTTOM_13861, Items.ZURIELS_ROBE_TOP_13858, Items.AMULET_OF_FURY_6585), // zuriel
            intArrayOf(Items.BRONZE_PLATEBODY_1117, Items.BRONZE_PLATELEGS_1075, Items.BRONZE_BOOTS_4119, Items.CAPE_1027), // bronze armor lol
            intArrayOf(Items.THIRD_AGE_AMULET_10344, Items.THIRD_AGE_ROBE_10340, Items.THIRD_AGE_ROBE_TOP_10338, Items.DRAGON_BOOTS_11732, Items.ADAMANT_GAUNTLETS_13000, Items.SLAYER_CAPE_9786), // 3a mage
            intArrayOf(Items.ENCHANTED_ROBE_7398, Items.ENCHANTED_TOP_7399, Items.ICE_GLOVES_1580, Items.AMULET_OF_MAGIC_1727), // enchanted
            intArrayOf(Items.AHRIMS_ROBESKIRT_4714, Items.AHRIMS_ROBETOP_4712, Items.AMULET_OF_ACCURACY_1478), // ahrim
            intArrayOf(Items.SHADE_ROBE_546, Items.SHADE_ROBE_548, Items.AMULET_OF_MAGICT_10366), // shade robes
        )

        fun spawn(loc: Location) : AIPlayer {
            // create the bot
            val bot = AIPBuilder.create(loc)
            bot.isHighPriority = true

            // create the script
            val script = OrbProjBot()

            // set script skills
            script.skills[Skills.ATTACK] = RandomFunction.random(10, 99)
            script.skills[Skills.STRENGTH] = RandomFunction.random(10, 99)
            script.skills[Skills.DEFENCE] = RandomFunction.random(10, 99)
            script.skills[Skills.HITPOINTS] = RandomFunction.random(10, 99)
            script.skills[Skills.MAGIC] = 99
            script.skills[Skills.RUNECRAFTING] = 99
            script.skills[Skills.SLAYER] = 99

            // set script equipment
            val outfit = botOutfits.random()
            for (itemId in outfit) {
                script.equipment.add(Item(itemId))
            }

            // put the bot in the bot constructor
            GeneralBotCreator(script, bot)

            // return the bot so we can record all its personal info and sell ads based on the data (actually just to de-register the bot later)
            return bot
        }
    }
}