package rs09.game.content.tutorial

import api.*
import core.game.component.Component
import core.game.content.quest.tutorials.tutorialisland.CharacterDesign
import core.game.node.Node
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs09.consts.Components
import rs09.game.Event
import rs09.game.world.GameWorld.Pulser
import rs09.game.world.GameWorld.settings
import rs09.game.world.repository.Repository

/**
 * Loads stage-relevant tutorial data
 * @author Ceikry
 */
object TutorialStage {
    /**
     * Performs stage actions for the player
     * @param player the player to perform the actions on
     * @param stage the stage to load
     */
    fun load(player: Player, stage: Int, login: Boolean = false){
        if(login)
        {
            player.hook(Event.ButtonClicked, TutorialButtonReceiver)
            player.hook(Event.Interaction, TutorialInteractionReceiver)
            player.hook(Event.ResourceProduced, TutorialResourceReceiver)
            player.hook(Event.UsedWith, TutorialUseWithReceiver)
            player.hook(Event.FireLit, TutorialFireReceiver)
            player.hook(Event.NPCKilled, TutorialKillReceiver)
            openOverlay(player, Components.TUTORIAL_PROGRESS_371)
            player.packetDispatch.sendInterfaceConfig(371, 4, true)
        }

        updateProgressBar(player)

        when(stage)
        {
            0 -> {
                lock(player, 10)
                teleport(player, Location.create(3094, 3107, 0))
                hideTabs(player, login)
                CharacterDesign.open(player)
            }

            1 -> {
                hideTabs(player, login)
                player.interfaceManager.openTab(Component(Components.OPTIONS_261))
                setVarbit(player, 1021, 0, 12)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "",
                        "Player controls",
                        "Please click on the flashing spanner icon found at the bottom",
                        "right of your screen. This will display your player controls.",
                        ""
                    )
                )
            }

            2 -> {
                setVarbit(player, 1021, 0, 0)
                hideTabs(player, login)
                registerHintIcon(player, Repository.findNPC(945)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendScrollMessageWithBlueTitle(
                        "Player controls",
                        "On the side panel you can now see a variety of options from",
                        "adjusting graphics settings, to adjusting the volume of ",
                        "music, to selecting whether your player should accept help",
                        "from other players. Don't worry about these too much for now,",
                        "they will become clearer as you explore the game. Talk to the",
                        settings!!.name + " Guide to continue."
                    )
                )
            }

            3 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3098,3107,0), 125)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendScrollMessageWithBlueTitle(
                        "Interacting with scenery",
                        "You can interact with many items of scenery by simply clicking",
                        "on them. Right clicking will also give more options. Feel free to",
                        "try it with the things in this room, then click on the door",
                        "indicated with the yellow arrow to go though to the next",
                        "instructor."
                    )
                )
            }

            4 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(943)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Moving around",
                        "Follow the path to find the next instructor. Clicking on the",
                        "ground will walk you to that point. Talk to the Survival Expert by",
                        "the pond to continue the tutorial. Remember you can rotate",
                        "the view by pressing the arrow keys."
                    )
                )
            }

            5 -> {
                hideTabs(player, login)
                player.interfaceManager.openTab(Component(Components.INVENTORY_149))
                setVarbit(player, 1021, 0, 4)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Viewing the items that you were given.",
                        "Click on the flashing backpack icon to the right hand side of",
                        "the main window to view your inventory. Your inventory is a list",
                        "of everything you have in your backpack.",
                        ""
                    )
                )
            }

            6 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 0)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Cut down a tree",
                        "You can click on the backpack icon at any time to view the",
                        "items that you currently have in your inventory. You will see",
                        "that you now have an axe in your inventory. Use this to get",
                        "some logs by clicking on one of the trees in the area."
                    )
                )
            }

            7 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Please wait.",
                        "",
                        "Your character is now attempting to cut down the tree. Sit back",
                        "for a moment while " + (if (player.appearance.isMale) "he" else "she") + " does all the hard work.",
                        ""
                    )
                )
            }

            8 -> {
                hideTabs(player, login)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Making a fire",
                        "Well done! You managed to cut some logs from the tree! Next,",
                        "use the tinderbox in your inventory to light the logs.",
                        "First click on the tinderbox to 'use' it.",
                        "Then click on the logs in your inventory to light them."
                    )
                )
            }

            9 -> {
                hideTabs(player, login)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Please wait.",
                        "",
                        "Your character is now attempting to light a fire.",
                        "This should only take a few seconds.",
                        ""
                    )
                )
            }

            10 -> {
                hideTabs(player, login)
                player.interfaceManager.openTab(Component(Components.STATS_320))
                setVarbit(player, 1021, 0, 2)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "You gained some experience.",
                        "",
                        "",
                        "Click on the flashing bar graph icon near the inventory button to see",
                        "your skill state."
                    )
                )
            }

            11 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 0)
                registerHintIcon(player, Repository.findNPC(943)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Your skill stats.",
                        "Here you will see how good your skills are. As you move your mouse",
                        "over any of the icons in this tab, the small yellow popup box will show",
                        "you the exact amount of experience you have and how much is",
                        "needed to get to the next level. Speak to the survival guide."
                    )
                )
            }

            12 -> {
                hideTabs(player, login)
                setVarbit(player,406, 0, 2)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(952)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Catch some shrimp",
                        "Click on the bubbling fishing spot indicated by the flashing arrow.",
                        "Remember, you can check your inventory by clicking the backpack",
                        "icon.",
                        ""
                    )
                )
            }

            13 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Please wait.",
                        "",
                        "This should only take a few seconds.",
                        "As you gain Fishing experience you'll find that there are many types",
                        "of fish and many ways to catch them."
                    )
                )
            }

            14 -> {
                hideTabs(player, login)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Cooking your shrimp",
                        "Now you have caught some shrimp, let's cook it. First light a fire: chop",
                        "down a tree and then use the tinderbox on the logs. If you've lost",
                        "your axe or tinderbox Brynna will give you another.",
                        "Once you have a fire, \"use\" your shrimp on the fire."
                    )
                )
            }

            15 -> {
                hideTabs(player, login)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Burning your shrimp",
                        "You have just burnt your first shrimp. This is normal. As you get",
                        "more experience in Cooking you will burn stuff less often. Let's try",
                        "cooking without burning it this time.",
                        ""
                    )
                )
            }

            16 -> {
                hideTabs(player, login)
                registerHintIcon(player, Location.create(3089, 3091, 0), 75)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Well done, you've just cooked your first " + settings!!.name + " meal.",
                        "",
                        "You can now move on to the next",
                        "instructor. Click on the gate shown and follow the path.",
                        "Remember, you can move the camera with the arrow keys."
                    )
                )
            }

            17 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3078, 3084, 0), 125)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Find your next instructor.",
                        "Follow the path until you get to the door with the yellow arrow",
                        "above it. Click on the door to open it. Notice the mini-map in",
                        "the top right, this shows a top down view of the area around",
                        "you. This can also be used for navigation."
                    )
                )
            }

            18 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(942)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Find your next instructor",
                        "Talk to the chef indicated. He will teach you the more advanced",
                        "aspects of Cooking such as combining ingredients. He will also",
                        "teach you about your music player menu as well.",
                        ""
                    )
                )
            }

            19 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Making dough",
                        "This is the base for many of the meals. To make dough we must mix",
                        "flour and water. First right click the bucket of water and select use.",
                        "then left click on the pot of flour.",
                        ""
                    )
                )
            }

            20 -> {
                hideTabs(player, login)
                registerHintIcon(player, Location.create(3076, 3081, 0), 75)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Cooking dough",
                        "Now you have made dough you can cook it. To cook the dough use",
                        "it with the range shown by the arrow. If you lose your dough talk to",
                        "Lev he will give you more ingredients.",
                        ""
                    )
                )
            }

            21 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(Components.MUSIC_V3_187))
                setVarbit(player, 1021, 0, 14)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Cooking dough",
                        "Well done! Your first loaf of bread. As you gain experience in",
                        "Cooking, you will be able to make other things like pies, cakes and even ",
                        "kebabs. Now you've got the hang of cooking, lets move on. Click on",
                        "the flashing icon in the bottom right to see the Music Player."
                    )
                )
            }

            22 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 0)
                registerHintIcon(player, Location.create(3072, 3090, 0), 125)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "The Music Player",
                        "From this interface you can control the music that is played. As you",
                        "explore the world and complete quests, more of the tunes will become",
                        "unlocked. Once you've examined this menu use the next door to",
                        "continue."
                    )
                )
            }

            23 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 13)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(Components.EMOTES_464))
                stopWalk(player)
                player.locks.lockMovement(100000)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Emotes",
                        "",
                        "Now how about showing some feelings? You will see a flashing icon in",
                        "the shape of a person. Click on that to access your emotes.",
                        ""
                    )
                )
            }

            24 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 0)
                player.locks.lockMovement(100000)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Emotes",
                        "For those situtations where words don't quite describe how you feel try",
                        "an emote. Go ahead try one out! You might notice that some of the",
                        "emotes are grey and cannot be used now. Don't worry! As you",
                        "progress further into the game you'll gain access to all sorts of things."
                    )
                )
            }

            25 -> {
                hideTabs(player, login)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Running",
                        "",
                        "It's only a short distance to the next guide.",
                        "Why not try running there? To do this click on the run icon in",
                        "your settings tab."
                    )
                )
            }

            26 -> {
                hideTabs(player, login)
                registerHintIcon(player, Repository.findNPC(949)!!)
                player.locks.unlockMovement()
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendScrollMessageWithBlueTitle(
                        "Run to the next guide",
                        "Now that you have the run button turned on, follow the path",
                        "until you come to the end. You may notice that the number on",
                        "the button goes down. This is your run energy. If your run",
                        "energy reaches zero, you'll stop running. Click on the door to",
                        "pass through it."
                    )
                )
            }

            27 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(949)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Talk with the Quest Guide.",
                        "",
                        "He will tell you all about quests.",
                        "",
                        ""
                    )
                )
            }

            28 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 0)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Your quest Journal",
                        "",
                        "This is your quest Journal, a list of all the quests in the game. Talk",
                        "to the Quest Guide again for an explanation.",
                        ""
                    )
                )
            }

            29 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                setVarbit(player, 1021, 0, 0)
                registerHintIcon(player, Location.create(3088, 3119, 0), 15)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Moving on.",
                        "",
                        "It's time to enter some caves. Click on the ladder to go down to the",
                        "next area.",
                        ""
                    )
                )
            }

            30 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                setVarbit(player, 1021, 0, 0)
                registerHintIcon(player, Repository.findNPC(948)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Mining and Smithing",
                        "",
                        "Next let's get you a weapon, or more to the point, you can make",
                        "your first weapon yourself. Don't panic, the Mining Instructor will",
                        "help you. Talk to him and he'll tell you all about it."
                    )
                )
            }

            31 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3076, 9504, 0), 50)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Prospecting",
                        "To prospect a mineable rock, just right click it and select the prospect",
                        "rock option. This will tell you the type of ore you can mine from it.",
                        "Try it now on one of the rocks indicated.",
                        ""
                    )
                )
            }

            32 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Please wait.",
                        "",
                        "Your character is now attempting to prospect the rock. This should",
                        "Only take a few seconds.",
                        ""
                    )
                )
                Pulser.submit(object : Pulse(3) {
                    override fun pulse(): Boolean {
                        setAttribute(player, "tutorial:stage", 33)
                        load(player, 33)
                        return true
                    }
                })
            }

            33 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3086, 9501, 0), 50)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "It's tin.",
                        "",
                        "So now you know there's tin in the grey rocks, try prospecting the",
                        "brown ones next.",
                        ""
                    )
                )
            }

            34 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(948)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendScrollMessageWithBlueTitle(
                        "It's copper.",
                        "Talk to the Mining Instructor to find out about these types of",
                        "ore and how you can mine them.",
                        "He'll even give you the required tools.",
                        ""
                    )
                )
            }

            35 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3076, 9504), 50)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Mining",
                        "It's quite simple really. All you need to do is right click on the rock",
                        "and select 'mine'. You can only mine when you have a pickaxe. So",
                        "give it a try: first mine one tin ore.",
                        ""
                    )
                )
            }

            36 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Please wait.",
                        "",
                        "Your character is now attempting to mine the rock.",
                        "This should only take a few seconds.",
                        ""
                    )
                )
            }

            37 -> {
                hideTabs(player, login)
                registerHintIcon(player, Location.create(3086, 9501), 50)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Mining",
                        "Now you have some tin ore you just need some copper ore, then",
                        "you'll have all you need to create a bronze bar. As you did before",
                        "right click on the copper rock and select 'mine'.",
                        ""
                    )
                )
            }

            38 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3079, 9496), 75)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Smelting",
                        "You should now have both some copper and tin ore. So let's smelt",
                        "them to make a bronze bar. To do this right click on either tin or",
                        "copper ore and select use then left click on the furnance. Try it now.",
                        ""
                    )
                )
            }

            //Yes I know I skipped one. Blegh.

            40 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(948)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "You've made a bronze bar!",
                        "",
                        "Speak to the Mining Instructor and he'll show you how to make",
                        "it into a weapon.",
                        ""
                    )
                )
            }

            41 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3083, 9499), 50)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Smithing a dagger",
                        "To smith you'll need a hammer - like the one you were given by",
                        "Dezzick - access to an anvil like the one with the arrow over it",
                        "and enough metal bars to make what you are trying to smith.",
                        "To start the process, use the bar on one of the anvils."
                    )
                )
            }

            42 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Smithing a dagger.",
                        "Now you have the Smithing menu open, you will see a list of all",
                        "the things you can make. Only the dagger can be made at this time; ",
                        "this is shown by the white text under it. You'll need",
                        "to select the dagger to continue."
                    )
                )
            }

            43 -> {
                hideTabs(player, login)
                registerHintIcon(player, Location.create(3094, 9502), 75)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "You've finished in this area.",
                        "So let's move on. Go through the gates shown by the arrow.",
                        "Remember, you may need to move the camera to see your",
                        "surroundings.",
                        ""
                    )
                )
            }

            44 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(944)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Combat.",
                        "In this area you will find out about combat, both melee and",
                        "ranged. Speak to the guide and he'll tell you about it.",
                        "",
                        ""
                    )
                )
            }

            45 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(Components.WORNITEMS_387))
                setVarbit(player, 1021, 0, 5)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Wielding weapons.",
                        "",
                        "You now have access to a new interface. Click on the flashing",
                        "icon of a man, the one to the right of your backpack icon.",
                        ""
                    )
                )
            }

            46 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 0)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "This is your worn inventory.",
                        "From here you can see what items you have equipped. Let's",
                        "get one of those slots filled, go back to your inventory and",
                        "right click your dagger, select wield from the menu.",
                        ""
                    )
                )
            }

            47 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendScrollMessageWithBlueTitle(
                        "You're now holding your dagger.",
                        "Clothes, armor, weapons and many other items are equipped",
                        "like this. You can unequip items by clicking on the item in the",
                        "worn inventory.",
                        "Speak to the Combat Instructor to continue."
                    )
                )
            }

            48 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendScrollMessageWithBlueTitle(
                        "Unequipping items.",
                        "In your worn inventory panel, right click on the dagger and",
                        "select the remove option from the drop down list. After you've",
                        "unequipped the dagger, wield the sword and shield. As you",
                        "pass the mouse over an item you will see its name appear at",
                        "the top left of the screen."
                    )
                )
            }

            49 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 1)
                var wepInter = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
                if(wepInter == null)
                {
                    wepInter = WeaponInterface(player)
                    player.addExtension(WeaponInterface::class.java, wepInter)
                }
                player.interfaceManager.openTab(wepInter)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Combat interface.",
                        "",
                        "Click on the flashing crossed swords icon to see the combat",
                        "interface.",
                        ""
                    )
                )
            }

            50 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 0)
                registerHintIcon(player, Location.create(3110,9518,0), 75)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendScrollMessageWithBlueTitle(
                        "This is your combat interface.",
                        "From this interface you can select the type of attack your",
                        "character will use. Different monsters have different",
                        "weaknesses. If you hover your mouse over the buttons, you",
                        "will see the type of XP you will receive when using each type of",
                        "attack. Now you have the tools needed for battle why not slay",
                        "some rats. Click on the gates indicated to continue."
                    )
                )
            }

            51 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Attacking",
                        "To attack the rat, click it and select the attack option. You",
                        "will then walk over to it and start hitting it.",
                        "",
                        ""
                    )
                )
            }

            52 -> {
                hideTabs(player, login)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Sit back and watch",
                        "While you are fighting you will see a bar over your head. The",
                        "bar shows how much health you have left. Your opponent will",
                        "have one too. You will continue to attack the rat until it's dead",
                        "or you do something else."
                    )
                )
            }

            53 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Well done, you've made your first kill!",
                        "Pass through the gate and talk to the Combat Instructor; he",
                        "will give you your next task.",
                        "",
                        ""
                    )
                )
            }

            54 -> {
                hideTabs(player, login)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Rat ranging.",
                        "Now you have a bow and some arrows. Before you can use",
                        "them you'll need to equip them. Once equipped with the",
                        "ranging gear try killing another rat. Remember, to attack: right",
                        "click on the monster and select attack."
                    )
                )
            }

            55 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3111,9526), 50)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Moving on.",
                        "You have completed the tasks here, to move on click on the",
                        "ladder shown.",
                        "",
                        ""
                    )
                )
            }

            56 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3122,3124), 50)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Banking.",
                        "Follow the path and you will come to the front of a building.",
                        "This is the 'Bank of " + settings!!.name + "' where you can store all your",
                        "most valued items. To open your bank box just right click on an",
                        "open booth indicated and select use."
                    )
                )
            }

            57 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3125, 3124), 75)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "This is your bank box.",
                        "You can store stuff here for safekeeping. If you die, anything",
                        "in your bank will be saved. To deposit something, right click it",
                        "and select 'store'. One you've had a good look, close the",
                        "window and move on through the door indicated."
                    )
                )
            }

            58 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(947)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Financial advice.",
                        "The guide here will tell you about making cash. Just click on",
                        "him to hear what he's got to say.",
                        "",
                        ""
                    )
                )
            }

            59 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3130, 3124, 0), 75)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "",
                        "Continue through the next door.",
                        "",
                        "",
                        ""
                    )
                )
            }

            60 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(954)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Prayer",
                        "Follow the path to the chapel and enter it.",
                        "Once inside talk to the monk. He'll tell you all about the prayer",
                        "skill.",
                        ""
                    )
                )
            }

            61 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(Components.PRAYER_271))
                setVarbit(player, 1021, 0, 6)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Your Prayer menu",
                        "Click on the flashing icon to open the Prayer menu.",
                        "",
                        "",
                        ""
                    )
                )
            }

            62 -> {
                hideTabs(player, login)
                registerHintIcon(player, Repository.findNPC(954)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "",
                        "Your Prayer menu.",
                        "Talk with Brother Brace and he'll tell you all about prayer.",
                        "",
                        ""
                    )
                )
            }

            63 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(Components.FRIENDS2_550))
                setVarbit(player, 1021, 0, 9)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "",
                        "Friends list.",
                        "You should now see another new icon. Click on the flashing",
                        "smiling face to open your friend list.",
                        ""
                    )
                )
            }

            64 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 10)
                player.interfaceManager.openTab(Component(Components.IGNORE2_551))
                player.interfaceManager.openTab(Component(Components.CLANJOIN_589))
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "This is your friends list.",
                        "This will be explained by Brother Brace shortly, but first click",
                        "the other flashing icon next to the friends list one.",
                        "",
                        ""
                    )
                )
            }

            65 -> {
                hideTabs(player, login)
                setVarbit(player, 1021, 0, 0)
                registerHintIcon(player, Repository.findNPC(945)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "This is your ignore list.",
                        "The two lists, friends and ignore, can be very helpful for",
                        "keeping track of when your friends are online or for blocking",
                        "messages from people you simply don't like. Speak with",
                        "Brother Brace and he will tell you more."
                    )
                )
            }

            66 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Location.create(3122,3102), 75)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Your final instructor!",
                        "You're almost finished on tutorial island. Pass through the",
                        "door to find the path leading to your last instructor.",
                        "",
                        ""
                    )
                )
            }

            67 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                registerHintIcon(player, Repository.findNPC(946)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Your final instructor!",
                        "Just follow the path to the wizards house, where you will be",
                        "shown how to cast spells. Just talk with the mage indicated to",
                        "find out more.",
                        ""
                    )
                )
            }

            68 -> {
                hideTabs(player, login)
                removeHintIcon(player)
                player.interfaceManager.openTab(Component(player.spellBookManager.spellBook))
                setVarbit(player, 1021, 0, 7)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "Open up your final menu.",
                        "Open up the Magic Spellbook tab by clicking on the flashing spellbook",
                        "icon next to the Prayer List tab you just learned about.",
                        "",
                        ""
                    )
                )
            }

            69 -> {
                hideTabs(player, login)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "",
                        "This is your spell list.",
                        "",
                        "Ask the mage about it.",
                        ""
                    )
                )
            }

            70 -> {
                hideTabs(player, login)
                registerHintIcon(player, Repository.findNPC(41)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendScrollMessageWithBlueTitle(
                        "Cast Wind Strike at a chicken.",
                        "Now you have the runes you should see the Wind Strike icon",
                        "at the top-left of your spellbook, second from the left. Walk over to",
                        "the caged chickens, click the Wind Strike icon and then  ",
                        "select one of the chickens to cast it on. It may take",
                        "several tries."
                    )
                )
            }

            71 -> {
                removeHintIcon(player)
                player.interfaceManager.restoreTabs()
                registerHintIcon(player, Repository.findNPC(946)!!)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlaneMessageWithBlueTitle(
                        "You have almost completed the tutorial!",
                        "All you need to do now is teleport to the mainland. Just speak with",
                        "Terrova and he'll tell you how to do that.",
                        "You have almost completed the tutorial!",
                        ""
                    )
                )
            }
        }
    }

    @JvmStatic
    public fun hideTabs(player: Player, login: Boolean)
    {
        val stage = getAttribute(player, "tutorial:stage", 0)
        if(login && player.interfaceManager.tabs.isNotEmpty())
            player.interfaceManager.removeTabs(*(0..13).toIntArray())

        if(stage > 2)
            player.interfaceManager.openTab(Component(Components.OPTIONS_261))
        if(stage > 5)
            player.interfaceManager.openTab(Component(Components.INVENTORY_149))
        if(stage > 10)
            player.interfaceManager.openTab(Component(Components.STATS_320))
        if(stage > 21)
            player.interfaceManager.openTab(Component(Components.MUSIC_V3_187))
        if(stage > 23)
            player.interfaceManager.openTab(Component(Components.EMOTES_464))
        if(stage > 28)
            player.interfaceManager.openTab(Component(Components.QUESTJOURNAL_V2_274))
        if(stage > 45)
            player.interfaceManager.openTab(Component(Components.WORNITEMS_387))
        if(stage > 49){
            var wepInter = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
            if(wepInter == null)
            {
                wepInter = WeaponInterface(player)
                player.addExtension(WeaponInterface::class.java, wepInter)
            }
        }
        if(stage > 61)
            player.interfaceManager.openTab(Component(Components.PRAYER_271))
        if(stage > 63)
            player.interfaceManager.openTab(Component(Components.FRIENDS2_550))
        if(stage > 64){
            player.interfaceManager.openTab(Component(Components.IGNORE2_551))
            player.interfaceManager.openTab(Component(Components.CLANJOIN_589))
        }
        if(stage > 68)
            player.interfaceManager.openTab(Component(player.spellBookManager.spellBook))
    }

    private fun updateProgressBar(player: Player)
    {
        val stage = getAttribute(player, "tutorial:stage", 0)
        val percent = if(stage == 0) 0 else ((stage.toDouble() / 71.0) * 100.0).toInt()
        val barPercent = if(stage == 0) 0 else (((percent.toDouble() / 100.0) * 20.0).toInt() + 1)
        setVarbit(player, 406, 0, barPercent)
        setInterfaceText(player, "$percent% Done", 371, 1)
    }

    fun removeHintIcon(player: Player) {
        val slot = player.getAttribute("tutorial:hinticon", -1)
        if (slot < 0 || slot >= HintIconManager.MAXIMUM_SIZE) {
            return
        }
        player.removeAttribute("tutorial:hinticon")
        HintIconManager.removeHintIcon(player, slot)
    }

    private fun registerHintIcon(player: Player, node: Node)
    {
        setAttribute(player, "tutorial:hinticon", HintIconManager.registerHintIcon(player, node))
    }

    private fun registerHintIcon(player: Player, location: Location, height: Int)
    {
        setAttribute(player, "tutorial:hinticon", HintIconManager.registerHintIcon(player, location, 1, -1, player.hintIconManager.freeSlot(), height, 3))
    }
}