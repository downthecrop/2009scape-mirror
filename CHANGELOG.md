"So be it! Let my life fuel the spell that ends his!"

 redwings Update Log  


The latest changes are at the top, and the less latest changes are at the bottom.

08 February -> March 10 2022

- ***The beginning of the Expansion Disk Content*** 
Some of the upcoming items for the Expansion Disk have been successfully ported and packed into the cache.

- New Items:
- Dragon Defender
- Dragon Pickaxe
- Blade of Saeldor and inactive Blade
- Neitiznot Faceguard
- Ring of the Gods
- Treasonous Ring
- Tyrranical Ring
- Corrupt Armour set
- Bronze (g) Armour set
- Iron (g) Armour set
- Steel (g) Armour set
- Mithril (g) Armour set
- Bronze-Mithril (g) Med helms and Square shields

- Dragon pickaxe added to Chaos Elemental and King Black Dragon's droptables at 1/256 and 1/1500 respectively

- Dragon pickaxe added with correct mining power

- As a result of the inclusion of the Dragon Pickaxe, Inferno Adze's mining strength has been reduced back to its original mining strength level, equal to Rune

- Dragon Pickaxe special attack added
- Dragon axe (OSRS) special attack corrected

- Blade of Saeldor has a new, heavily WIP special attack based on the failed poll implementation: Saeldor's Strike, consuming 100% spec energy to stun target

- Bronze, Iron, and Steel (g) equipment now obtainable from Easy Clue reward caskets

- Mithril (g) equipment now obtainable from Medium Clue reward caskets

- Currently unobtainable by player: Neitiznot Faceguard, Blade of Saeldor, Corrupt armour set, Wilderness Rings

- ***Monk's Friend Quest!!**  Should be fully complete now

- Sara brews properly on the linear formula (Meaning they will correctly scale, and correctly overheal to the right amounts)

- Make-All fixed for using Pestle and Mortar on Herblore secondaries

- Dark bow and Crystal bow now have their correct Attack Range of 10 tiles

- Overhauled Entrana Item Restriction checks

-  Allow using a hunter potion to catch implings, you can now Boost to catch them

- Implement Make-X for potatoes, pizzas, and breads

- Improve the behavior of trying to interact with moving entities (such as tool leprechauns)


27 January -> 08 February 2022
- **Fremennik Trials!!** Don your horned helmets and board your longboat, gaining the trust of the standoffish Fremennik tribe won't be easy, but the Adventurer never shies away from a good adventure! The Quest has been fully completed and is now in-game.

- **Tribal Totem!!** Help Kangali Mau recover an important tribal artefact from the clutches of the postal system. The Tribal Totem Quest is now fully complete and in-game.

- **HOSTILE RANDOM EVENTS ARE BACK** Tree Spirit, Rock Golem, River Troll, Shade, and Zombie join evil chicken in his AFK-anihilation schemes. Be careful when training Woodcutting, Mining, and Fishing, as you will have a chance to meet these nasties!

- **A sleeping bot has been awoken!** GenericSlayerBot can only currently mosey his way on down to the Fremennik Slayer Dungeon and kill Cave Crawlers, but he's now awake and online! Go say hi if you see him on a Slayer assigment!

- **GLOBAL SHOP RESTOCKING** Before, shops would only restock if a minute had passed, and a player was in render distance/region distance of the shop. Now, it functions more correctly, restocking shops every 60 seconds globally. Rejoice, ShopScapers!

- **POSITIONAL AUDIO SUPPORT!** Sounds will now fall off after a certain distance. The cacophonous cry of 20+ whips at Pest Control will now be no longer. Monster combat, area sounds, object sounds, and more now correctly fall off depending on how far away from their origin the player is.

- **SFX UPDATES** Due to the inclusion of positional audio, many many SFX have been added. Jewellery teleports(incl. Mounted), Standard Spellbook Teleports, slashing of webs, entering the Mole Lair, digging with a Spade, charging orbs, charging God Spells, Bones to Bananas, Dwarven Multicannon and more are now playing their sounds! Volume UP!

- **MONSTER SFX UPDATES** Due to finding a debug sfx dump, some work has been done to adding monster combat SFX. The list is quite long, so here's one good example: All the Pest Control monsters now have their entire suite of combat SFX!

- **BIRD'S NEST LOOT POOL CORRECTIONS** Seed Nests, Ring Nests, and Wyson nests have been corrected in loot pool, and weights to be more accurate to the original game instead of... a complete mess.

- **CHAOS ELEMENTAL OVERHAUL!** Chaos Elemental has been overhauled with Crash's special Accuracy Polish. He has all SFX added(sans Spellcast SFX), and his drop table has been accurately repaired. He now has his minor drop table, which always rolls alongside his "main" table. No longer will you be only getting 5 bones, you'll get 5 bones *and something possibly decent*. Or 10 Strange Fruit. Who knows?

- **FELLER CELLAR UPDATES!!** The Cellar now shows you what track is currently playing in the chatbox. When a song is about to end, the Cellar will now shuffle, and tell the players currently inside what the next track will be. 2 new tracks have been added to accompany the Cellar Jukebox polish: Fight On! (FF7) and Fun Naming! :cd: 

- Barlak (Long Bone Merchant) now temporarily spawned in the Dorgeshuun Mine, next to the Bone Weapon merchant.

- Falador Mole Lair Music track "The Mad Mole" now correctly plays across the entirety of the Mole Lair.

- Added 8 spawns for Tortured souls, 6 spawns north of the Ectofuntus, and 2 spawns west of Port Phasmatys

- All God Kiteshields now have a +1 Prayer bonus, in line with the previous Equipment Rebalance changes

- All Baby Mole NPCs now correctly wander, and their three unique examine texts have been added.

- Ryan: Failing to enter the GWD Boss rooms now no longer reduces your KC. (Example: attempting to enter the same time as another player is entering)

- Ryan: Plank Make spell rewritten to be authentic and polished

- Ryan: Player is now notified that Turael Slayer tasks won't give them Slayer points

- aweinstock: Add InteractionListener.onUseWithWildcard. Important for quests and content that requires checking of using *any* item, not just small specific handfuls of them.

- aweinstock: Allow Dark Mage to be contacted via NPC Contact Spell, allowing players to contact the mage and repair pouches.

- aweinstock: Make multiple runes continuous (via linear interpolation) between levels required for multiple runes.


18 -> 26 January 2022
- Chaos Druid Warrior drop table corrections, removing copious noted secondary drops (Arios loved these for some reason...)

- Fixed some hats clipping with hair (Q)

- All Ancient Warriors Equipment (Vesta, Zuriel, etc) can now be correctly traded in its un-degraded form

- Kermit: Fixed issues with Adventure bot assembly and initial spawning. You'll notice increased GE activity, as less bots are now capable of becoming inactive null bots, and increased stability on launch, as the adventure bots will no longer all be forced to spawn in the same timeframe.

- Ryan: Fix Gertrude's Cat quest step not appropriately setting, allowing players to once again complete the Quest.

- More Zanaris dialogue and spawn work by Q! 

- **FIRST REDWINGS CACHE CHANGES** - Mounted Glory Right-Click Options, Obelisk in POH
Mounted glories now have right-click options for each teleport, similar to later incarnations, and a brand-new Construction object has been added, built in the Centrepiece slot of the Garden room: The Summoning Obelisk! Requiring 41 Summoning, and similar materials to its later RS2 counterpart, you now have the opportunity to recharge your Summoning points right inside your POH!

- Ryan: Random events now actually random. Before, Randoms would roll every 60 seconds, for everyone, no deviation. Now, there's a larger window (Between 30 and 90 minutes), and is appropriately random so we all don't chimp out at the sight of the first Genie/Old Man random spawn.

- Ryan: Added a bunch of debugging to RSA block and player init

- Ryan: fix ruby bolt(e) calc and fix general bolt spec audio. Rejoice! The Ruby bolt special attack will now also forfeit your eardrums as well as your HP!

- **BOSS SLAYER** Boss Slayer tasks have now been added to the game! You can only receive these Boss tasks from Duradel, and can only get them assigned at 75/90 Slayer depending on Boss. Now you can also get some Slayer EXP while hunting for drops!
75: G.Mole, C.Ele, KBD
90: GWD

- aweinstock: MTA fixes to Graveyard, Telekinetic Maze, and the Shop interface

- aweinstock: All GWD Boss minion secondary drops that should be noted, are now noted.

- aweinstock: TDs unique items have been correctly re-weighted (1/256 for claws, 1/1024 for ruined armour pieces), erroneous highly-weighted Godsword Shard drop on Balfrug Kreeyath corrected

- aweinstock: Add server-wide announcements for Godsword shards, Ruined armour pieces, steam battlestaves and Seercull.

- aweinstock: Re-run charm scraper to fix monster charm drops missed the first time and any regressions since

- **REDWINGS OST DISK 1** Due to new, experimental tools from the sorceror, we can now pack new midi files inside the cache. Ten new songs have been added to the game.



14 -> 18 January 2022
- Long and curved bones are now real content! You can trade these in to Barlak after completion of The Lost Tribe quest, for Construction XP. Huge thanks to Q!

- Many other NPC dialogues by Q in the Dorgeshuun and Ape Atoll areas of the game as well as expansion to Crate code.

- All currently functional Summoning familiars with invisible skills boosts are now correctly added to the game! Pyrelord, Forge Regent, Arctic Bear, Lava Titan, Magpie, Spirit Graahk, Spirit Larupia, Spirit Kyatt, Void Ravager and Wolpertinger now boost skills alongside their other helpful abilities.

- The Mining Guild can now be entered under 60 Mining via the use of skill boosts.

- Black Knight and Water Elemental drop fixes

- The Burnt Stew Incident has been resolved. All burnt bowl foods can now be properly emptied.

- Barrows prayer drain timer has been fixed, and now correctly drains every 18 seconds.

- The Lady of the Lake will now **finally** enhance Excalibur for you if you have the prerequisite Seers Hard Diary and Merlin's Crystal completed, and the headband and sword equipped when talking to her.

- Giant Mole's Burrow ability has now been corrected, no longer being entirely random down to it's last Hitpoint. 

Burrow: When the mole's health is between 5-50%, there is a 25% chance for any incoming attack to cause it to burrow away into another location. There is also a chance that the dirt she digs while burrowing to escape can extinguish the player's light source (shown with dirt splatters on the player's screen), though covered light sources will be unaffected by this.

- Potion decanting note bug should now be fixed and not eat up all your notes when attempting to decant. Another certified Q moment (fixed in !28)

- Fixed incorrect arrows being returned when used with poison++ (Q)
 




13 January 2022
- The GE should now properly save and load, meaning our newly-resurrected bot boys will actually be able to stock up their gathered goods! And, we'll actually be able to put in buy/sell offers that won't get voided on server restart!

- More Vexia-tier dialogue cleanups by Nuggles 😍 

- More dialogue by Q for some Digsite, Isafdar, and Tyras Camp NPCs 🗣️ 

-  aweinstock: Waterbirth Dungeon is cannonable, Spinolyps only drain prayer if they hit, and always target ranged defence. 

- Some nice Waterbirth Dungeon tweaks by Avi, making it more consistent, opening up a new Dag Cannon spot, and Spinolyps draining Prayer more in-line with how it should work on the original games

- aweinstock: - Increase drop rate of Sinister Key to 1/12, make strike spells always max on Salarin, and port Yanille Agility Dungeon to kotlin + InteractionListener.

- Yanille Agility Dungeon's unique content (Salarin and his herb-filled, poison-trapped Chest) has been properly overhauled, and functions correctly to how it does in the RS2 game. Thanks Avi!

11 January 2022
- Core Reversion has now been completed The unstable changes the alien made to the core have been carefully singled out and reverted. Stability should be back to pre-new-core levels of the usual occasional server vomit. 🌍 

- The bots are back in town! A few changes singled out and diff'd by Ryan were found to be the cause of the bots becoming completely nonfunctional. The bots have been returned to their former glory. 🤖  Huge content BIS upgrade!!

- This makes the Adventure bots work similarly to how they did on 09-live pre-core changes, where they will actually adventure and sell shit until the entropy eventually slows them down. Can you find sniffbot?
- This makes the simple gatherer (logs etc) bots work again
- This makes PC bots work again
- This ((probably)) makes the GD bots work again
- This stops most of the bots from pooling at the Lumbridge spawnpoint after they die/void out/other shit

- Playerscripts are functional again! Bit cringe, but they're back for those who want to make use of them

- A staggering amount of new NPC dialogue work done by Q, for places like Falador, Miscellania, and many other areas of the game! 🗣️ 

4 January 2022
- The Ring of Wealth now has **all** of its unique teleports, akin to OSRS. You can now use it to teleport to: Miscellania, Grand Exchange(as usual), Falador Park and Dondakan's Rock. :ring: 

Equipment Rebalancing: Part I
Similar to OSRS's small, oft necessary tweaks to improve health of the combat triangle, and open up some more horizontal or supplementary gearing options, after careful analysis, some of the Rebalance changes have now been included into `redwings`. Read below for all currently included changes. Possibly more to come in the future.

- **ALL** God plate armour (helmets, platebody, platelegs) (Saradomin, Zamorak, Guthix) now receives a `+1` `Prayer bonus`, up from `+0`

- **ALL** God dragonhide (coif, body, chaps, bracers) now receives a `+1` `Prayer bonus`, up from `+0`

- **ALL** God dragonhide chaps `Defence` level requirement has been ***removed***. Def pures weep with reckless abandon.

- Mystic Staves and Battlestaves across the board have had their `Magic attack` and `Magic defence` increased.

- **ALL Battlestaves** have an increased MagicATK/MagicDEF of `+12`, up from `+10`

- **ALL Mystic staves** have an increased MagicATK/MagicDEF of `+14`, up from `+10`

The above staff changes still keep power in-line, as `Ancient Staff` and beyond is at `+15` and above, but opens up more options for staves, making some of the combo staves more useful for combat.

3 January 2022
- Fixes to Family Crest
- GlobalKillCounter fixes
- Ship Charters that cost -1GP now corrected to cost 0GP
- Magic Dart EXP correction
- Can now correctly pickpocket ID 25 Woman
- Enchanting Emerald or Diamond Bracelets no longer turns them into enchanted (noted) bracelets but the correct unnoted item
- A whole bunch of funny backend shit that may or may not work haha (such as disabling GUI server monitor, and reverting Fractals dynamic region changes)
- more fixes to blast furnace (so it doesnt accidentally eat coal when it shouldnt)

31 December 2021
- Renewable Familiars You can now renew your Summoning familiar by Summon-ing an extra pouch of the same Familiar, refreshing their duration to its maximum. Extend Beasts of Burden for longer trips, or your Golem to keep the Mining Boost up!

- Beasts of Burden will now drop their currently held items on dismissal, even if the player is an Iron. 

- Battlestaff price correction to 7k ea 

- Slimy eel fishing spots have been added

- the cope boxes have been disabled we will see him next time

- Clue Scroll Overhaul All monsters that can drop Clue Scrolls have now been corrected to drop the right "clue" drop. No longer will everything always force an Easy Clue. 

27 December 2021

- Secondary banks are now accessible at a hefty fee of 5 million GP, doubling your bank space from 496 to 992. You can switch between bank accounts at a bank teller.

- A new chinning/bursting spot has been made accessible. The Skeleton Monkeys inside Ape Atoll Dungeon can now be fought, and the area is now a multicombat area

- Giant Mole's drop table has been cleaned up, with the Thanos Drop Editor back online.

- The Oo'glog Fresh Meat store is now accessible, and has an increased stock of 100 raw bird meat.

- The damage modifier (A thing mostly used in special attacks to modify damage) is now correctly factored in, for all 3 combat styles.   

- You can now obtain Mort Myre Pears and stems from casting Bloom, and Bloom's sfx has been added.

- The Fishing Guild can now be entered via skill boosts. 

- Edgeville Canoe -> WIlderness Pond travel option now fixed

26 December 2021

- Ruby bolts(e) fix!(kinda)!! Ruby bolt(e) special attack, Blood Forfeit, maximum current Hitpoint damage cap has been increased from 35 to a kiiinda close enough cap of 150 Hitpoints. (The reason why the cap can't be removed yet, is that the Corporeal Beast-specific damage cap is not implemented) 

- The Cider, needed to complete a step in Seers Village Diary, can now be purchased from our bandaid-fix Digsite Exam Center shop. Make sure to take 5, and try not to left-click Drink them

25 December 2021
- Seer's Achievement Diaries should now be fully completable.
- Enhanced Excalibur now has all of it's special attack effects implemented 
- Blast Furnace should now reward 1 smithing XP to everyone in the Blast Furnace area for each bar that is smithed.
- The AFK timer has been removed, may Evil Chicken have mercy on your soul.

- Mobile Client Update
- The Android Client has been updated.
- App strings updated to match the rebrand, new app icon replacement still pending.
- To use redwings-mobile to connect to other servers (such as local dev instances or the main 2009scape game), download and edit the Android Client Config, load the Android Client, tap the rightmost corner in-app to bring up the Settings menu, and tap LOAD CONFIG. 

24 December 2021
- THE FUNNY FURNACE UPDATE(and friends)
- What's new?
- Blast Furnace is now complete enough to be considered complete™️
- Ordan and Jorzik's dialogue has been implemented.
- Jorzik's armour store is in as well so now you have a place to dump all of your blast furnace creations
- Some of the funny furnace funny logic has been polished up and the kinks worked out
- The steps to the blast furnace in Keldagrim will now gatekeep players with under 60 smithing as intended
- If you're under 60 smithing you can pay 2500gp for ten minutes of access to the blast furnace
- If you have the Ring of Charos(a) equipped the fee is lowered to 1250gp
- Green Dragon bots can hop over the wildy ditch again but bots in general are still fucked
- The correct item spawns have been added to the Blast Furnace area
- The Blast Furnace will now remember what you have stored into it
- You now have to pedal your ores into the ore pot before you can smelt them

- Explorer's Ring now has correct sfx for recharging your run energy, as well as the Falador Farm Cabbage-port. Leaf-bladed sword now has correct sfx.

- Family Crest Update! Fractals polished the last bits of the quest up. The bossfight, Chronozon, now works as it should, and the perfect gold ore puzzle has been added.

- Client Update!
1. december_snow is now accessible, allowing you to toggle on/off the game-wide snow effect. 
2. left_click_attack This allows you, no matter what, to have any monster's main left-click option be Attack. This is important for bossing, as bosses usually have insanely high Combat levels, thus usually only being right-clickable.  

23 December 2021 
- The Xmas Gift Limit has been increased! To compensate for missing most of December, the daily gift limit has been increased to 28.

- Permadeath Ironmen should now be able to access the 20x rates from Hans in Tutorial Cellar/Lumbridge Castle. Take on the challenge, and rebuild faster than ever.  

- sniffbot is back The Adventure Bot dialogue has been restored

- At the Digsite Exam Centre, south of the Digsite, there is a Researcher you can talk to to access a shop "Forgotten Relix" which contains until now inacessible quest-unlocked items such as the Magic Secateurs, Salve Amulet, and Iban's Staff.  

- Bill Teach, Once Lost Uncle of the Fellers, has returned to take us on adventures near and far. At the Port Phasmatys pub, you can talk to him and he will take you where you want to go.  

- Barrows gloves return from the crypt! The Culinaromancer's Chest, the RFD gloves, and the bankchest in Lumbridge Basement are now accessible to all accounts.

- Blast Furnace Exclusive Beta The Blast Furnace is now accessible deep within Keldagrim. Smithing skill trainers are weeping, overjoyed. 