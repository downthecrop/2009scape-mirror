# Developer contributing code

Before contributing code, review the [readme](https://gitlab.com/2009scape/2009scape/-/blob/master/README.md) for general policies, and the [server wiki](https://gitlab.com/2009scape/2009scape/-/wikis/home) for help with setting up and getting started.

It is the expectation that all new code contributions are written in Kotlin. Modifications to Java files are accepted, but these modifications should be limited to bugfixes.

## JSON editing

NPC/item spawns, NPC info, drop tables, item info, object examines, and more are all stored in JSON format. If you are editing supported JSON files, it is required that you use the [Zaros tool](https://gitlab.com/2009scape/tools/rs09-thanos-tool) to make any edits. Using this tool prevents typos and makes sure that the files are all edited in the same way.

## Kotlin tips

If you are adding content (item/object interactions, NPC dialogue, quests, etc.), nearly all the functions you will need are in [ContentAPI](https://gitlab.com/2009scape/2009scape/-/blob/master/Server/src/main/core/api/ContentAPI.kt). ContentAPI provides a single unified way to perform common tasks like sending messages, dialogue, adding/removing items, running scripts, and more. This API allows core code to remain independent of content code. 

All interactions must use [InteractionListener](https://gitlab.com/2009scape/2009scape/-/blob/master/Server/src/main/core/game/interaction/InteractionListener.kt). Interactions include a player performing an action on a piece of scenery (like climbing a ladder), using one item with another item, or using an item with an NPC.

There are multiple systems for writing dialogue, but the easiest and most human-readable way to write dialogue is using the [DialogueLabeller](https://gitlab.com/2009scape/2009scape/-/blob/master/Server/src/main/core/game/dialogue/DialogueLabeller.kt) system. The other methods of writing dialogue are much less user-friendly and prone to error.

## Java conversions

This codebase has a long history, and a lot of the Java code comes from the Arios project. This code is functional, but if you are converting Java to Kotlin, you should improve the code. As a general rule, the newest standards and best practices include:

1. Convert all plugins to listeners.
2. Replace Pulses with QueueScripts (if possible).
3. Replace "magic numbers" with defined [constants](https://gitlab.com/2009scape/tools/rs09-constants-library) (e.g. `2216` becomes `Scenery.BROKEN_CART_2216`).
4. Dialogue should be rewritten using DialogueLabeller (This makes it much easier to add on to for NPCs that speak in multiple quests or activities).
5. Function calls should be replaced with ContentAPI where possible (e.g. `player.sendMessage("Hi.")` becomes `sendMessage(player, "Hi.")`).
6. Check for sources on what you are converting. 2009Scape strives to be as authentic to 2009-era RuneScape as possible. Arios started as an OSRS-based project, and did not care as much about authenticity. The best sources are YouTube videos dated as close to 28 January 2009 as possible, or the RuneScape Wiki (RS3, not OSRS) back-dated to 28 January 2009.

# Developer opening a MR
If you are opening a MR you can help make the merge process easier.
In the MR template there is a section called `What has been done in this MR?`.
In order to be helpful, this must be written in past tense, use an action verb, and be clear to a non-technical user.

If this is done correctly then your MR can be added to a merge train quicker.

```
Implemented hangover recipe 
Fixed #1908 
Updated descriptions of damaged mourner equipment
Updated all children in W Ardougne to have normal sized heads in chat dialogues
```

If your MR implements multiple related items then the list should be formatted as the [changelog subsections](CONTRIBUTING.md#changelog-subsections) describes.
This should only be used rarely.
If you are unsure use the standard format.

When updating the commit message for the merge train we should generally be able to copy-paste this as-is and only need to replace the "fixed" line with a more verbose description.

## Sources
A cause of last-moment rejection of MRs is due to an unsourced implementation.
If you are referencing an issue then make sure that there is a source in that issue.
If you are doing something without an issue then you should either add a source to your MR or open a new issue.
If you are implementing an RFC it will be merged after the discussion has resolved in favor of your implementation.

As a developer you can make it easier for a reviewer if you clearly show your sources.

## Tips and tricks
Use the [test bot](https://gitlab.com/ryannathans/2009servertools/-/tree/master/devops%20scripts?ref_type=heads) to check if your MR will conflict with or crash the test server before hitting test server.


# Merge train building
In order to perform this process you must have the role of maintainer.

## Process

1) Complete a review of the code
2) Tag MR as ready to merge (if not already)
3) Check `Edit commit message` 
4) If required enter `/run_pipeline` in a comment to re-run the pipeline.
5) If required, approve the MR and close any open threads. Do not rebase it. The merge train should be able to handle it.
6) Add the MR to the train

Remember trains take time. 

## Review
First sort all ready to merge MRs from oldest to newest.
As each MR is opened check the following carefully:
- Are any server properties changing?
- Are any default arguments changing?
- Has content API been touched?
- Is there a new property? What happens if that property is not set?
- Has everything that has been changed been tested?
- Has anything been changed that has not been documented?

If something needs to be changed on live make sure it is documented in the minutes channel.
You must have Moderator to have access to that channel.

## Commit message
A commit message needs to be clear for a non technical user.
Words like "implemented" should be replaced with "added".
This is because it is used to build the changelog.
Also, all messages should start with a past tense verb and **not** have a period at the end.
There is no length limit as the changelog window will wordwrap.
For example:
```
Attempted fix for bank interface first load issue
Removed inauthentic granite maul special handler
Improved handing of wilderness combat rules
```
Each line will be listed as a single changelog item.


### Changelog subsections
Occasionally a MR has multiple features under an overarching category.
In that case the commit message should be written as:

```
Rewrote Plague City

Updated mourners in W. Ardougne 
Corrected Elena spawn logic
```

In the changelog this will generate a single bullet point with the first line and everything else will be indented without a bullet point.

### Confidential issues
If a confidential issue is being merged the changelog should describe the change vaguely.
For example:

A weapon is performing incorrectly in the wilderness.
The changelog states `Removed inauthentic granite maul special handler`.
This allows the average user to know what has changed without knowing what the initial exploit was.

Common phrases for these types of issues are:
- Updated XP for X
- Fixed drop table for Y
- Removed inauthentic Z

If the exploit involves item duplications or other economy-damaging exploits then great care should be taken with this description.


