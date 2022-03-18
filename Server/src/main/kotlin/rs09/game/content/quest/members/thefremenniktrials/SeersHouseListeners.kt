package rs09.game.content.quest.members.thefremenniktrials

import api.*
import core.game.content.global.action.ClimbActionHandler
import core.game.content.global.action.DoorActionHandler
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.audio.Audio
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener
import rs09.tools.stringtools.RED

class SeersHouseListeners : InteractionListener() {

    val WESTDOOR = 4165
    val EASTDOOR = 4166
    val WESTLADDER = 4163
    val EASTLADDER = 4164
    val WESTTRAPDOOR = getScenery(2631, 3663, 2)!!
    val EASTTRAPDOOR = getScenery(2636, 3663, 2)!!
    val TAP = 4176
    val COOKINGRANGE = 4172
    val DRAIN = 4175
    val CUPBOARD = 4178
    val BALANCECHEST = 4170
    val UNICORNHEAD = 4181
    val SOUTHBOXES = 4183
    val CHEST = intArrayOf (4167,4168)
    val SOUTHCRATES = 4186
    val EASTCRATES = 4185
    val EASTBOXES = 4184
    val FROZENTABLE = 4169
    val BOOKCASE = 4171
    val BULLSHEAD = 4182
    val MURAL = 4179

    val OLDREDDISK = Items.OLD_RED_DISK_9947
    val WOODENDISK = Items.WOODEN_DISK_3744
    val REDHERRING = Items.RED_HERRING_3742
    val BLUETHREAD = Items.THREAD_3719
    val PICK = Items.PICK_3720
    val SHIPTOY = Items.SHIP_TOY_3721
    val MAGNET = Items.MAGNET_3718
    val REDGOOP = Items.STICKY_RED_GOOP_3746
    val REDDISK = Items.RED_DISK_3743
    val VASELID = Items.VASE_LID_3737
    val VASE = Items.VASE_3734
    val FULLVASE = Items.VASE_OF_WATER_3735
    val FROZENVASE = Items.FROZEN_VASE_3736
    val SEALEDEMPTYVASE = Items.SEALED_VASE_3738
    val SEALEDFULLVASE = Items.SEALED_VASE_3739
    val FROZENKEY = Items.FROZEN_KEY_3741
    val SEERSKEY = Items.SEERS_KEY_3745
    val EMPTYBUCKET = Items.EMPTY_BUCKET_3727
    val ONEFIFTHBUCKET = Items.ONE_5THS_FULL_BUCKET_3726
    val TWOFIFTHBUCKET = Items.TWO_5THS_FULL_BUCKET_3725
    val THREEFIFTHBUCKET = Items.THREE_5THS_FULL_BUCKET_3724
    val FOURFIFTHBUCKET = Items.FOUR_5THS_FULL_BUCKET_3723
    val FULLBUCKET = Items.FULL_BUCKET_3722
    val FROZENBUCKET = Items.FROZEN_BUCKET_3728
    val EMPTYJUG = Items.EMPTY_JUG_3732
    val ONETHIRDJUG = Items.ONE_THIRDRDS_FULL_JUG_3731
    val TWOTHIRDJUG = Items.TWO_THIRDSRDS_FULL_JUG_3730
    val FULLJUG = Items.FULL_JUG_3729
    val FROZENJUG = Items.FROZEN_JUG_3733

    val JUGS = intArrayOf(
        Items.EMPTY_JUG_3732,
        Items.ONE_THIRDRDS_FULL_JUG_3731,
        Items.TWO_THIRDSRDS_FULL_JUG_3730,
        Items.FULL_JUG_3729
    )

    val BUCKETS = intArrayOf(
        Items.EMPTY_BUCKET_3727,
        Items.ONE_5THS_FULL_BUCKET_3726,
        Items.TWO_5THS_FULL_BUCKET_3725,
        Items.THREE_5THS_FULL_BUCKET_3724,
        Items.FOUR_5THS_FULL_BUCKET_3723,
        Items.FULL_BUCKET_3722
    )

    val DISKS = intArrayOf(
        Items.OLD_RED_DISK_9947,
        Items.RED_DISK_3743
    )

    val EASTZONE = ZoneBorders(2635, 3662, 2637, 3664, 2)

    val WESTZONE = ZoneBorders(2630, 3662, 2632, 3664, 2)

    override fun defineListeners() {

        on(WESTDOOR,SCENERY,"open"){player,node ->
            if(!player.getAttribute("PeerStarted",false)){
                sendDialogue(player,"You should probably talk to the owner of this home.")
            }
            if(getAttribute(player, "fremtrials:peer-vote", false))
            {
                sendDialogue(player, "I don't need to go through that again.")
                return@on true
            }
            else if(player.getAttribute("PeerRiddle",5) < 5){
                player.dialogueInterpreter.open(DoorRiddleDialogue(player), Scenery(WESTDOOR,node.location))
            }
            else if(player.getAttribute("riddlesolved",false)){
                if(player.location.x == 2631){
                    player.inventory.clear()
                    DoorActionHandler.handleAutowalkDoor(player,node.asScenery())
                }else{
                    DoorActionHandler.handleAutowalkDoor(player,node.asScenery())
                }
                DoorActionHandler.handleAutowalkDoor(player,node.asScenery())
            }
            return@on true
        }

        on(WESTLADDER, SCENERY,"Climb-up") { player, node ->
            ClimbActionHandler.climb(player, Animation(828), Location.create(2631, 3664, 2))
            return@on true
        }

        on(WESTTRAPDOOR.id, SCENERY,"Climb-down") { player, node ->
            if(player.location.x < 2634){
                ClimbActionHandler.climb(player, Animation(828), Location.create(2631, 3664, 0))
            }
            else if(player.location.x > 2634){
                ClimbActionHandler.climb(player, Animation(828), Location.create(2636, 3664, 0))
            }
            return@on true
        }

        on(WESTTRAPDOOR.id, SCENERY,"Close") { player, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4174))
            return@on true
        }

        on(WESTTRAPDOOR.id, SCENERY,"Open") { player, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4173))
            return@on true
        }

        on(EASTTRAPDOOR.id, SCENERY,"Open") { player, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4173))
            return@on true
        }

        on(EASTTRAPDOOR.id, SCENERY,"Close") { player, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4174))
            return@on true
        }

        on(EASTTRAPDOOR.id, SCENERY,"Climb-down") { player, node ->
            if(player.location.x > 2634){
                ClimbActionHandler.climb(player, Animation(828), Location.create(2636, 3664, 0))
            }
            return@on true
        }

        on(EASTLADDER, SCENERY,"Climb-Up") { player, node ->
            ClimbActionHandler.climb(player, Animation(828), Location.create(2636, 3664, 2))
            return@on true
        }

        on(CUPBOARD,SCENERY,"Search") { player, node ->
            sendMessage(player,"You search the cupboard...")
            if(player.inventory.contains(EMPTYBUCKET,1)){
                sendMessage(player,"You find nothing of interest.")
            }
            else{
                addItem(player,EMPTYBUCKET)
                sendMessage(player,"You find a bucket with a number five painted on it.")
            }
            return@on true
        }

        on(BALANCECHEST, SCENERY,"Open") { player, node ->
            sendDialogue(player,"This chest is securely locked shut. There is some kind of balance attached to the lock, and a number four is painted just above it.")
            return@on true
        }

        on(BOOKCASE, SCENERY,"Search") { player, node ->
            sendMessage(player,"You search the bookcase...")
            if(player.inventory.contains(REDHERRING,1)){
                sendMessage(player,"You find nothing of interest.")
            }
            else{
                addItem(player,REDHERRING)
                sendMessage(player,"Hidden behind some old books, you find a red herring.")
            }
            return@on true
        }

        on(SOUTHBOXES, SCENERY,"Search"){ player, node ->
            sendMessage(player,"You search the boxes...")
            if(player.inventory.contains(BLUETHREAD,1)){
                sendMessage(player,"You find nothing of interest.")
            }
            else{
                addItem(player,BLUETHREAD)
                sendMessage(player,"You find some thread hidden inside.")
            }
            return@on true
        }

        on(CHEST, SCENERY,"Open"){ player, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4168))
            return@on true
        }

        on(CHEST, SCENERY,"Close"){ player, node ->
            SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(4167))
            return@on true
        }

        on(CHEST, SCENERY,"Search"){ player, node ->
            sendMessage(player,"You search the chest...")
            if(player.inventory.contains(EMPTYJUG,1)){
                sendMessage(player,"You find nothing of interest.")
            }
            else{
                addItem(player,EMPTYJUG)
                sendMessage(player,"You find a jug with a number three painted on it")
            }
            return@on true
        }

        on(EASTCRATES, SCENERY,"Search") { player, node ->
            sendMessage(player,"You search the crates...")
            if(player.inventory.contains(PICK,1)){
                sendMessage(player,"You find nothing of interest.")
            }
            else{
                addItem(player,PICK)
                sendMessage(player,"You find a small pick hidden inside.")
            }
            return@on true
        }

        on(SOUTHCRATES, SCENERY,"Search") { player, node ->
            sendMessage(player,"You search the crates...")
            if(player.inventory.contains(SHIPTOY,1)){
                sendMessage(player,"You find nothing of interest.")
            }
            else{
                addItem(player,SHIPTOY)
                sendMessage(player,"You findd a toy ship hidden inside")
            }
            return@on true
        }

        on(SOUTHBOXES, SCENERY,"Search") { player, node ->
            sendMessage(player,"You search the boxes...")
            if(player.inventory.contains(MAGNET,1)){
                sendMessage(player,"You find nothing of interest.")
            }
            else{
                addItem(player,MAGNET)
                sendMessage(player,"You find a magnet hidden inside.")
            }
            return@on true
        }

        on(SOUTHBOXES, SCENERY,"Search") { player, node ->
            sendMessage(player,"You search the boxes...")
            if(player.inventory.contains(MAGNET,1)){
                sendMessage(player,"You find nothing of interest.")
            }
            else{
                addItem(player,MAGNET)
                sendMessage(player,"You find a magnet hidden inside.")
            }
            return@on true
        }

        on(BULLSHEAD, SCENERY,"Study") { player, node ->
            player.dialogueInterpreter.open(BullHeadDialogue(player))
            return@on true
        }

        on(UNICORNHEAD, SCENERY,"Study") { player, node ->
            player.dialogueInterpreter.open(UnicornHeadDialogue(player))
            return@on true
        }

        onUseWith(SCENERY,REDHERRING,COOKINGRANGE) { player, used, with ->
            player.animate(Animation(883,Animator.Priority.HIGH))
            player.audioManager.send(Audio(2577, 1, 1))
            removeItem(player,REDHERRING)
            addItem(player,REDGOOP)
            sendDialogue(player,"As you cook the herring on the stove, the colouring on it peels off separately as a red sticky goop...")
            return@onUseWith true
        }

        onUseWith(ITEM,REDGOOP,WOODENDISK) { player, used, with ->
            removeItem(player,WOODENDISK)
            addItem(player,REDDISK)
            sendMessage(player,"You coat the wooden coin with the sticky red goop.")
            return@onUseWith true
        }

        on(MURAL, SCENERY,"Study") { player, node ->
            sendMessage(player,"The mural feels like something is missing.")
            return@on true
        }

        onUseWith(SCENERY,TAP,*BUCKETS) { player, _, bucket ->
            when(bucket.id){
                3727 ->{
                    removeItem(player,EMPTYBUCKET)
                    addItem(player,FULLBUCKET)
                    sendMessage(player,"You fill the bucket from the tap.")
                    return@onUseWith true
                }
                ONEFIFTHBUCKET ->{
                    removeItem(player,ONEFIFTHBUCKET)
                    addItem(player,FULLBUCKET)
                    sendMessage(player,"You fill the bucket from the tap.")
                }
                TWOFIFTHBUCKET ->{
                    removeItem(player,TWOFIFTHBUCKET)
                    addItem(player,FULLBUCKET)
                    sendMessage(player,"You fill the bucket from the tap.")
                }
                THREEFIFTHBUCKET ->{
                    removeItem(player,THREEFIFTHBUCKET)
                    addItem(player,FULLBUCKET)
                    sendMessage(player,"You fill the bucket from the tap.")
                }
                FOURFIFTHBUCKET ->{
                    removeItem(player,FOURFIFTHBUCKET)
                    addItem(player,FULLBUCKET)
                    sendMessage(player,"You fill the bucket from the tap.")
                }
                FULLBUCKET ->{
                    sendMessage(player,"The bucket is already full!")
                }
                else -> sendMessage(player,"FUCKFUCKFUCKFUCK")
            }
            return@onUseWith true
        }

        onUseWith(SCENERY,TAP,*JUGS) { player, used, with ->
            when(with.id){
                EMPTYJUG ->{
                    removeItem(player,EMPTYJUG)
                    addItem(player,FULLJUG)
                    sendMessage(player,"You fill the jug from the tap.")
                }
                ONETHIRDJUG ->{
                    removeItem(player,ONETHIRDJUG)
                    addItem(player,FULLJUG)
                    sendMessage(player,"You fill the jug from the tap.")
                }
                TWOTHIRDJUG ->{
                    removeItem(player,TWOTHIRDJUG)
                    addItem(player,FULLJUG)
                    sendMessage(player,"You fill the jug from the tap.")
                }
                FULLJUG ->{
                    sendMessage(player,"The jug is already full!")
                }
            }
            return@onUseWith true
        }

        onUseWith(SCENERY,DRAIN,*BUCKETS) { player, used, with ->
            when(with.id){
                EMPTYBUCKET ->{
                    sendMessage(player,"The bucket is already empty!")
                }
                ONEFIFTHBUCKET ->{
                    removeItem(player,ONEFIFTHBUCKET)
                    addItem(player,EMPTYBUCKET)
                    sendMessage(player,"You empty the bucket down the drain.")
                }
                TWOFIFTHBUCKET ->{
                    removeItem(player,TWOFIFTHBUCKET)
                    addItem(player,EMPTYBUCKET)
                    sendMessage(player,"You empty the bucket down the drain.")
                }
                THREEFIFTHBUCKET ->{
                    removeItem(player,THREEFIFTHBUCKET)
                    addItem(player,EMPTYBUCKET)
                    sendMessage(player,"You empty the bucket down the drain.")
                }
                FOURFIFTHBUCKET ->{
                    removeItem(player,FOURFIFTHBUCKET)
                    addItem(player,EMPTYBUCKET)
                    sendMessage(player,"You empty the bucket down the drain.")
                }
                FULLBUCKET ->{
                    removeItem(player,FULLBUCKET)
                    addItem(player,EMPTYBUCKET)
                    sendMessage(player,"You empty the bucket down the drain.")
                }
            }
            return@onUseWith true
        }

        onUseWith(SCENERY,DRAIN,*JUGS) { player, used, with ->
            when(with.id){
                EMPTYJUG ->{
                    sendMessage(player,"The jug is already empty!")
                }
                ONETHIRDJUG ->{
                    removeItem(player,ONETHIRDJUG)
                    addItem(player,EMPTYJUG)
                    sendMessage(player,"You empty the jug down the drain.")
                }
                TWOTHIRDJUG ->{
                    removeItem(player,TWOTHIRDJUG)
                    addItem(player,EMPTYJUG)
                    sendMessage(player,"You empty the jug down the drain.")
                }
                FULLJUG ->{
                    removeItem(player,FULLJUG)
                    addItem(player,EMPTYJUG)
                    sendMessage(player,"You empty the jug down the drain.")
                }
            }
            return@onUseWith true
        }

        onUseWith(ITEM,REDGOOP,WOODENDISK) { player, _, _ ->
            removeItem(player,REDGOOP)
            removeItem(player,WOODENDISK)
            addItem(player,REDDISK)
            return@onUseWith true
        }

        onUseWith(SCENERY,BALANCECHEST, FOURFIFTHBUCKET) { player, used, with ->
            removeItem(player,FOURFIFTHBUCKET)
            addItem(player,VASE)
            sendMessage(player,"You place the bucket on the scale.")
            sendMessage(player,"It is a perfect counterweight and balances precisely.")
            sendMessage(player,"You take a strange looking vase out of the chest.")
            return@onUseWith true
        }

        onUseWith(SCENERY,MURAL,*DISKS) { player, used, with ->
            when(with.id){
                REDDISK ->{
                    if(player.getAttribute("olddiskplaced",false)){
                        removeItem(player,REDDISK)
                        addItem(player,VASELID)
                        sendDialogue(player,"You put the red disk into the empty hole on the mural. It is a perfect fit! The centre of the mural appears to have become loose.")
                    }
                    else if(player.getAttribute("reddiskplaced",false)){
                        sendMessage(player,"You already have a disk in that spot")
                    }
                    else{
                        removeItem(player, REDDISK)
                        sendMessage(player,"You put the red disk into the empty hold on the mural.")
                        sendMessage(player, "It's a perfect fit!")
                        player.setAttribute("reddiskplaced",true)
                    }
                }
                OLDREDDISK -> {
                    if(player.getAttribute("reddiskplaced", false)) {
                        removeItem(player, OLDREDDISK)
                        addItem(player, VASELID)
                        sendDialogue(player, "You put the red disk into the empty hole on the mural. It is a perfect fit! The centre of the mural appears to have become loose.")
                    }
                    else if(player.getAttribute("olddiskplaced", false)) {
                        sendMessage(player, "You already have a disk in that spot")
                    }
                    else{
                        removeItem(player, OLDREDDISK)
                        sendMessage(player, "You put the red disk into the empty hold on the mural.")
                        sendMessage(player, "It's a perfect fit!")
                        player.setAttribute("olddiskplaced", true)
                    }
                }
            }
            return@onUseWith true
        }

        onUseWith(SCENERY,BALANCECHEST,*BUCKETS) { player, used, with ->
            when(with.id){
                EMPTYBUCKET ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the bucket on the scale")
                    sendMessage(player,"It is too light to balance it properly")
                }
                ONEFIFTHBUCKET ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the bucket on the scale")
                    sendMessage(player,"It is too light to balance it properly")
                }
                TWOFIFTHBUCKET ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the bucket on the scale")
                    sendMessage(player,"It is too light to balance it properly")
                }
                THREEFIFTHBUCKET ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the bucket on the scale")
                    sendMessage(player,"It is too light to balance it properly")
                }
                FOURFIFTHBUCKET ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the bucket on the scale")
                    sendMessage(player,"It is a perfect counterweight and balances precisely.")
                    sendMessage(player,"You take a strange looking vase out of the chest.")
                    addItem(player,VASE)
                }
                FULLBUCKET ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the bucket on the scale")
                    sendMessage(player,"It is too heavy to balance it properly")
                }
            }
            return@onUseWith true
        }

        onUseWith(SCENERY,BALANCECHEST,*JUGS) { player, used, with ->
            when(with.id){
                EMPTYJUG ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the jug on the scale")
                    sendMessage(player,"It is too light to balance it properly")
                }
                ONETHIRDJUG ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the jug on the scale")
                    sendMessage(player,"It is too light to balance it properly")
                }
                TWOTHIRDJUG ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the jug on the scale")
                    sendMessage(player,"It is too light to balance it properly")
                }
                FULLJUG ->{
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    sendMessage(player,"You place the jug on the scale")
                    sendMessage(player,"It is too light to balance it properly")
                }
            }
            return@onUseWith true
        }

        onUseWith(SCENERY,FROZENTABLE,*BUCKETS) { player, used, with ->
            when(with.id){
                EMPTYBUCKET -> sendMessage(player,"Your empty bucket gets very cold on the icy table.")
                FULLBUCKET -> {
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    removeItem(player,FULLBUCKET)
                    addItem(player,FROZENBUCKET)
                    sendMessage(player,"They icy table immediately freezes the water in your bucket.")
                }
            }
            return@onUseWith true
        }

        onUseWith(SCENERY,FROZENTABLE, *JUGS) { player, used, with ->
            when(with.id){
                EMPTYJUG -> sendMessage(player,"Your empty jug gets very cold on the icy table.")
                FULLJUG -> {
                    player.animate(Animation(883,Animator.Priority.HIGH))
                    removeItem(player,FULLJUG)
                    addItem(player,FROZENJUG)
                    sendMessage(player,"The icy table immediately freezes the water in your jug.")
                }
            }
            return@onUseWith true
        }

        onUseWith(ITEM,BUCKETS,*JUGS) { player, used, with ->
            when(used.id){
                ONEFIFTHBUCKET -> {
                    when(with.id){
                        EMPTYJUG ->{
                            removeItem(player,ONEFIFTHBUCKET)
                            removeItem(player,EMPTYJUG)
                            addItem(player,EMPTYBUCKET)
                            addItem(player,ONETHIRDJUG)
                            sendMessage(player,"You empty the bucket into the jug")
                        }
                        ONETHIRDJUG ->{
                            removeItem(player,ONEFIFTHBUCKET)
                            removeItem(player,ONETHIRDJUG)
                            addItem(player,EMPTYBUCKET)
                            addItem(player,TWOTHIRDJUG)
                            sendMessage(player,"You empty the bucket into the jug")
                        }
                        TWOTHIRDJUG ->{
                            removeItem(player,ONEFIFTHBUCKET)
                            removeItem(player,TWOTHIRDJUG)
                            addItem(player,EMPTYBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        FULLJUG -> sendMessage(player,"The jug is already full!")
                    }
                }
                TWOFIFTHBUCKET -> {
                    when(with.id){
                        EMPTYJUG ->{
                            removeItem(player,TWOFIFTHBUCKET)
                            removeItem(player,EMPTYJUG)
                            addItem(player,EMPTYBUCKET)
                            addItem(player,TWOTHIRDJUG)
                            sendMessage(player,"You empty the bucket into the jug")
                        }
                        ONETHIRDJUG ->{
                            removeItem(player,TWOFIFTHBUCKET)
                            removeItem(player,ONETHIRDJUG)
                            addItem(player,EMPTYBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        TWOTHIRDJUG ->{
                            removeItem(player,TWOFIFTHBUCKET)
                            removeItem(player,TWOTHIRDJUG)
                            addItem(player,ONEFIFTHBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        FULLJUG ->sendMessage(player,"The jug is already full!")
                    }
                }
                THREEFIFTHBUCKET -> {
                    when(with.id){
                        EMPTYJUG ->{
                            removeItem(player,THREEFIFTHBUCKET)
                            removeItem(player,EMPTYJUG)
                            addItem(player,EMPTYBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        ONETHIRDJUG ->{
                            removeItem(player,THREEFIFTHBUCKET)
                            removeItem(player,ONETHIRDJUG)
                            addItem(player,ONEFIFTHBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        TWOTHIRDJUG ->{
                            removeItem(player,THREEFIFTHBUCKET)
                            removeItem(player,TWOTHIRDJUG)
                            addItem(player,TWOFIFTHBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        FULLJUG ->sendMessage(player,"The jug is already full!")
                    }
                }
                FOURFIFTHBUCKET -> {
                    when(with.id){
                        EMPTYJUG ->{
                            removeItem(player,FOURFIFTHBUCKET)
                            removeItem(player,EMPTYJUG)
                            addItem(player,ONEFIFTHBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        ONETHIRDJUG ->{
                            removeItem(player,FOURFIFTHBUCKET)
                            removeItem(player,ONETHIRDJUG)
                            addItem(player,TWOFIFTHBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        TWOTHIRDJUG ->{
                            removeItem(player,FOURFIFTHBUCKET)
                            removeItem(player,TWOTHIRDJUG)
                            addItem(player,THREEFIFTHBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        FULLJUG ->sendMessage(player,"The jug is already full!")
                    }
                }
                FULLBUCKET -> {
                    when(with.id){
                        EMPTYJUG ->{
                            removeItem(player,FULLBUCKET)
                            removeItem(player,EMPTYJUG)
                            addItem(player,TWOFIFTHBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        ONETHIRDJUG ->{
                            removeItem(player,FULLBUCKET)
                            removeItem(player,ONETHIRDJUG)
                            addItem(player,THREEFIFTHBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        TWOTHIRDJUG ->{
                            removeItem(player,FULLBUCKET)
                            removeItem(player,TWOTHIRDJUG)
                            addItem(player,FOURFIFTHBUCKET)
                            addItem(player,FULLJUG)
                            sendMessage(player,"You fill the jug to the brim.")
                        }
                        FULLJUG ->sendMessage(player,"The jug is already full!")
                    }
                }
            }
            return@onUseWith true
        }

        onUseWith(ITEM,JUGS,*BUCKETS) { player, used, with ->
            when(used.id){
                ONETHIRDJUG->{
                    when(with.id){
                        EMPTYBUCKET ->{
                            removeItem(player,ONETHIRDJUG)
                            removeItem(player,EMPTYBUCKET)
                            addItem(player,EMPTYJUG)
                            addItem(player,ONEFIFTHBUCKET)
                            sendMessage(player,"You empty the jug into the bucket.")
                        }
                        ONEFIFTHBUCKET ->{
                            removeItem(player,ONETHIRDJUG)
                            removeItem(player,ONEFIFTHBUCKET)
                            addItem(player,EMPTYJUG)
                            addItem(player,TWOFIFTHBUCKET)
                            sendMessage(player,"You empty the jug into the bucket.")
                        }
                        TWOFIFTHBUCKET ->{
                            removeItem(player,ONETHIRDJUG)
                            removeItem(player,TWOFIFTHBUCKET)
                            addItem(player,EMPTYJUG)
                            addItem(player,THREEFIFTHBUCKET)
                            sendMessage(player,"You empty the jug into the bucket.")
                        }
                        THREEFIFTHBUCKET ->{
                            removeItem(player,ONETHIRDJUG)
                            removeItem(player,THREEFIFTHBUCKET)
                            addItem(player,EMPTYJUG)
                            addItem(player,FOURFIFTHBUCKET)
                            sendMessage(player,"You empty the jug into the bucket.")
                        }
                        FOURFIFTHBUCKET ->{
                            removeItem(player,ONETHIRDJUG)
                            removeItem(player,FOURFIFTHBUCKET)
                            addItem(player,EMPTYJUG)
                            addItem(player,FULLBUCKET)
                            sendMessage(player,"You fill the bucket to the brim.")
                        }
                        FULLBUCKET -> sendMessage(player,"The bucket is already full!")
                    }
                }
                TWOTHIRDJUG->{
                    when(with.id){
                        EMPTYBUCKET ->{
                            removeItem(player,TWOTHIRDJUG)
                            removeItem(player,EMPTYBUCKET)
                            addItem(player,TWOFIFTHBUCKET)
                            addItem(player,EMPTYJUG)
                            sendMessage(player,"You empty the jug into the bucket.")
                        }
                        ONEFIFTHBUCKET ->{
                            removeItem(player,TWOTHIRDJUG)
                            removeItem(player,ONEFIFTHBUCKET)
                            addItem(player,THREEFIFTHBUCKET)
                            addItem(player,EMPTYJUG)
                            sendMessage(player,"You empty the jug into the bucket.")
                        }
                        TWOFIFTHBUCKET ->{
                            removeItem(player,TWOTHIRDJUG)
                            removeItem(player,TWOFIFTHBUCKET)
                            addItem(player,FOURFIFTHBUCKET)
                            addItem(player,EMPTYJUG)
                            sendMessage(player,"You empty the jug into the bucket.")
                        }
                        THREEFIFTHBUCKET ->{
                            removeItem(player,TWOTHIRDJUG)
                            removeItem(player,THREEFIFTHBUCKET)
                            addItem(player,FULLBUCKET)
                            addItem(player,EMPTYJUG )
                            sendMessage(player,"You fill the bucket to the brim.")
                        }
                        FOURFIFTHBUCKET ->{
                            removeItem(player,TWOTHIRDJUG)
                            removeItem(player,FOURFIFTHBUCKET)
                            addItem(player,FULLBUCKET)
                            addItem(player,ONETHIRDJUG)
                            sendMessage(player,"You fill the bucket to the brim.")
                        }
                        FULLBUCKET -> sendMessage(player,"The bucket is already full!")
                    }
                }
                FULLJUG->{
                    when(with.id){
                        EMPTYBUCKET ->{
                            removeItem(player,FULLJUG)
                            removeItem(player,EMPTYBUCKET)
                            addItem(player,THREEFIFTHBUCKET)
                            addItem(player,EMPTYJUG)
                            sendMessage(player,"You empty the jug into the bucket.")
                        }
                        ONEFIFTHBUCKET ->{
                            removeItem(player,FULLJUG)
                            removeItem(player,ONEFIFTHBUCKET)
                            addItem(player,FOURFIFTHBUCKET)
                            addItem(player,EMPTYJUG)
                            sendMessage(player,"You empty the jug into the bucket.")
                        }
                        TWOFIFTHBUCKET ->{
                            removeItem(player,FULLJUG)
                            removeItem(player,TWOFIFTHBUCKET)
                            addItem(player,FULLBUCKET)
                            addItem(player,EMPTYJUG)
                            sendMessage(player,"You fill the bucket to the brim.")
                        }
                        THREEFIFTHBUCKET ->{
                            removeItem(player,FULLJUG)
                            removeItem(player,THREEFIFTHBUCKET)
                            addItem(player,FULLBUCKET)
                            addItem(player,ONETHIRDJUG)
                            sendMessage(player,"You fill the bucket to the brim.")
                        }
                        FOURFIFTHBUCKET ->{
                            removeItem(player,FULLJUG)
                            removeItem(player,FOURFIFTHBUCKET)
                            addItem(player,FULLBUCKET)
                            addItem(player,TWOTHIRDJUG)
                            sendMessage(player,"You fill the bucket to the brim.")
                        }
                        FULLBUCKET -> sendMessage(player,"The bucket is already full!")
                    }
                }
            }
            return@onUseWith true
        }

        on(VASE,ITEM,"Shake") { player, node ->
            sendDialogue(player,"You shake the strangely shaped Vase. From the sound of it there is something metallic inside, but the neck of th vase is too narrow for it to come out.")
            return@on true
        }

        onUseWith(ITEM,PICK,VASE) { player, used, with ->
            sendMessage(player,"The pick wouldn't be strong enough to break the vase open.")
            return@onUseWith true
        }

        onUseWith(ITEM,MAGNET,VASE) { player, used, with ->
            sendMessage(player,"You use the magnet on the vase. The metallic object inside moves.")
            sendMessage(player,"The neck of the vase is too thin for thee object to come out of the vase.")
            return@onUseWith true
        }

        onUseWith(SCENERY,TAP,VASE) { player, used, with ->
            removeItem(player,VASE)
            addItem(player,FULLVASE)
            sendMessage(player,"You fill the strange looking vase with water.")
            return@onUseWith true
        }

        onUseWith(ITEM,FULLJUG,VASE) { player, used, with ->
            removeItem(player,FULLJUG)
            removeItem(player,VASE)
            addItem(player,EMPTYJUG)
            addItem(player,FULLVASE)
            sendMessage(player,"You fill the vase with water.")
            return@onUseWith true
        }

        on(FULLVASE,ITEM,"Shake") { player, node ->
            sendDialogue(player,"You shake the strangely shaped vase. The water inside it sloshes a little. Some spills out of the neck of the vase.")
            return@on true
        }

        onUseWith(ITEM,VASE,VASELID) { player, used, with ->
            removeItem(player,VASE)
            removeItem(player,VASELID)
            addItem(player,SEALEDEMPTYVASE)
            sendMessage(player,"You screw the lid on tightly.")
            return@onUseWith true
        }

        onUseWith(ITEM,FULLVASE,VASELID) { player, used, with ->
            removeItem(player,FULLVASE)
            removeItem(player,VASELID)
            addItem(player,SEALEDFULLVASE)
            sendMessage(player,"You screw the lid on tightly.")
            return@onUseWith true
        }

        on(SEALEDEMPTYVASE,ITEM,"Remove-lid") { player, node ->
            removeItem(player,SEALEDEMPTYVASE)
            addItem(player,VASE)
            addItem(player,VASELID)
            sendMessage(player,"You unscrew the lid from the vase.")
            return@on true
        }

        on(SEALEDFULLVASE,ITEM,"Remove-lid") { player, node ->
            removeItem(player,SEALEDFULLVASE)
            addItem(player,VASE)
            addItem(player,VASELID)
            sendMessage(player,"You unscrew the lid from the vase.")
            return@on true
        }

        onUseWith(SCENERY,FROZENTABLE, FULLVASE) { player, used, with ->
            player.animate(Animation(883,Animator.Priority.HIGH))
            removeItem(player,VASE)
            addItem(player,FROZENVASE)
            sendMessage(player,"The icy table immediately freezes the water in your vase.")
            return@onUseWith true
        }

        onUseWith(SCENERY,FROZENTABLE, SEALEDFULLVASE) { player, used, with ->
            player.animate(Animation(883,Animator.Priority.HIGH))
            removeItem(player,SEALEDFULLVASE)
            addItem(player,FROZENKEY)
            sendMessage(player,"The water expands as it freezes, and shatters the vase.")
            sendMessage(player,"You are left with a key encased in ice.")
            return@onUseWith true
        }

        onUseWith(SCENERY,COOKINGRANGE, FROZENBUCKET) { player, used, with ->
            player.animate(Animation(883,Animator.Priority.HIGH))
            player.audioManager.send(Audio(2577, 1, 1))
            removeItem(player,FROZENBUCKET)
            addItem(player,EMPTYBUCKET)
            sendMessage(player,"You place the frozen bucket on the range. The ice turns to steam.")
            return@onUseWith true
        }

        onUseWith(SCENERY,COOKINGRANGE, FROZENJUG) { player, used, with ->
            player.animate(Animation(883,Animator.Priority.HIGH))
            player.audioManager.send(Audio(2577, 1, 1))
            removeItem(player,FROZENJUG)
            addItem(player,EMPTYJUG)
            sendMessage(player,"You place the frozen jug on the range. The ice turns to steam.")
            return@onUseWith true
        }

        onUseWith(SCENERY,COOKINGRANGE, FROZENVASE) { player, used, with ->
            player.animate(Animation(883,Animator.Priority.HIGH))
            player.audioManager.send(Audio(2577, 1, 1))
            removeItem(player,FROZENVASE)
            addItem(player,VASE)
            sendMessage(player,"You place the frozen vase on the range. The ice turns into steam.")
            return@onUseWith true
        }

        onUseWith(SCENERY,COOKINGRANGE, FROZENKEY) { player, used, with ->
            player.animate(Animation(883,Animator.Priority.HIGH))
            player.audioManager.send(Audio(2577, 1, 1))
            removeItem(player,FROZENKEY)
            addItem(player,SEERSKEY)
            sendMessage(player,"The heat of the range melts the ice around the key.")
            return@onUseWith true
        }

        on(EASTDOOR, SCENERY,"Open") { player, node ->
            if(player.inventory.contains(SEERSKEY,1)){
                player.setAttribute("/save:housepuzzlesolved",true)
                player.inventory.clear()
                DoorActionHandler.handleAutowalkDoor(player,node.asScenery())
                player.setAttribute("/save:fremtrials:peer-vote",true)
                player.setAttribute("/save:fremtrials:votes",player.getAttribute("fremtrials:votes",0) + 1)
                sendNPCDialogue(player,1288,"Incredible! To have solved my puzzle so quickly! I have no choice but to vote in your favour!")
            }
            else sendMessage(player,"This door is locked tightly shut.")
            return@on true
        }

    }
}

class BullHeadDialogue(player:Player) : DialogueFile() {

    private val WOODENDISK = Items.WOODEN_DISK_3744

    val p = player

    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> sendDialogue(p,"You notice there is something unusual about the right eye of this bulls' head...").also {
                if(p.inventory.contains(WOODENDISK,1)){
                    stage = 10
                }else stage = 1
            }
            1 -> sendDialogue(p,"It is not an eye at all, but some kind of disk made of wood. You take it from the head").also {
                addItem(p,WOODENDISK)
                stage = 1000
            }
            10 -> sendMessage(p,"It's unusual because you've already taken it!").also { stage = 1000 }
            1000 -> end()
        }
    }
}

class UnicornHeadDialogue(player:Player) : DialogueFile() {

    private val OLDREDDISK = Items.OLD_RED_DISK_9947

    val p = player

    override fun handle(componentID: Int, buttonID: Int) {
        when(stage){
            0 -> sendDialogue(p,"You notice there is something unusual about the left eye of this unicorn head...").also {
                if(p.inventory.contains(OLDREDDISK,1)){
                    stage = 10
                }else stage = 1
            }
            1 -> sendDialogue(p,"It is not an eye at all, but some kind of redd coloured disk. You take it from the head").also {
                addItem(p,OLDREDDISK)
                stage = 1000
            }
            10 -> sendMessage(p,"It's unusual because you've already taken it!").also { stage = 1000 }
            1000 -> end()
        }
    }
}

class DoorRiddleDialogue(player: Player) : DialogueFile() {

    private val RIDDLEONE = arrayOf(
        "My first is in the well, but not at sea.",
        "My second in 'I', but not in 'me'.",
        "My third is in flies, but insects not found.",
        "My last is in earth, but not in the ground.",
        "My whole when stolen from you, caused you death.",
        "What am I?"
    )

    private val RIDDLETWO = arrayOf(
        "My first is in mage, but not in wizard.",
        "My second in goblin, and also in lizard.",
        "My third is in night, but not in the day.",
        "My last is in fields, but not in the hay.",
        "My whole is the most powerful tool you will ever possess.",
        "What am I?"
        )

    private val RIDDLETHREE = arrayOf(
        "My first is in water, and also in tea.",
        "My second in fish, but not in the sea.",
        "My third in mountains, but not underground.",
        "My last is in strike, but not in pound.",
        "My whole crushes mountains, drains rivers, and destroys civilisations.",
        "All that live fear my passing.",
        "What am I?"
    )

    private val RIDDLEFOUR = arrayOf(
        "My first is in wizard, but not in a mage.",
        "My second in jail, but not in a cage.",
        "My third is in anger, but not in a rage.",
        "My last in a drawing, but not on a page.",
        "My whole helps to make bread, let birds fly and boats sail.",
        "What am I?"
    )

    private val RIDDLEANSWER = arrayOf("LIFE","MIND","TIME","WIND")

    val p = player

    val riddle = when(p.getAttribute("PeerRiddle",0)){
        0 -> RIDDLEONE
        1 -> RIDDLETWO
        2 -> RIDDLETHREE
        3 -> RIDDLEFOUR
        else -> RIDDLEONE
    }

    var init = true

    override fun handle(componentID: Int, buttonID: Int) {
        if(init){
            stage = 1
            init = false
        }
        when(stage){
            1 -> sendDialogue(player!!,"There is a combination lock on this door. Above the lock you can see that there is a metal plaque with a riddle on it.").also { stage = 5 }
            5 -> options("Read the riddle","Solve the riddle","Forget it").also { stage = 10 }
            10 -> when(buttonID){
                1 -> dialogue(riddle[0],riddle[1],riddle[2],riddle[3]).also { stage = 20 }
                2 -> {
                    openInterface(p,298)
                    end()
                }
                3 -> end()
            }
            15 ->{
                    dialogue(riddle[0],riddle[1],riddle[2],riddle[3])
                    stage = 20
            }
            20 -> if(riddle.contentEquals(RIDDLETHREE)){
                dialogue(riddle[4],riddle[5],riddle[6]).also { stage = 1000 }
            }else{
                dialogue(riddle[4],riddle[5]).also { stage = 1000 }
            }
            1000 -> end()
        }
    }
}
