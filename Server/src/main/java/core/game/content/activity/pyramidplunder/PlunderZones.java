package core.game.content.activity.pyramidplunder;

import core.game.content.global.action.ClimbActionHandler;
import core.game.interaction.Option;
import core.game.node.Node;
import core.game.node.entity.Entity;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.npc.NPC;
import core.game.node.entity.player.Player;
import core.game.node.entity.skill.Skills;
import core.game.node.item.Item;
import core.game.node.scenery.Scenery;
import core.game.node.scenery.SceneryBuilder;
import core.game.world.map.Location;
import core.game.world.map.zone.MapZone;
import core.game.world.map.zone.ZoneBorders;
import core.game.world.map.zone.ZoneBuilder;
import core.game.world.update.flag.context.Animation;
import core.plugin.Initializable;
import core.plugin.Plugin;
import core.tools.RandomFunction;
import kotlin.Unit;
import rs09.game.Varp;
import rs09.game.content.activity.pyramidplunder.PlunderSession;

/**
 * Defines the zones for the pyramid plunder rooms and their interactions
 * @author ceik
 */

@Initializable
public class PlunderZones implements Plugin<Object> {
    private final PlunderZone[] ROOMS = {
            new PlunderZone("plunder:room1", 1,1923, 4464, 1932, 4474),
            new PlunderZone("plunder:room2", 2,1925, 4449, 1941, 4458),
            new PlunderZone("plunder:room3", 3,1941, 4421, 1954, 4432),
            new PlunderZone("plunder:room4", 4,1949, 4464, 1959, 4477),
            new PlunderZone("plunder:room5", 5,1968, 4420, 1978, 4436),
            new PlunderZone("plunder:room6", 6,1969, 4452, 1980, 4473),
            new PlunderZone("plunder:room7", 7,1923, 4424, 1931, 4439),
            new PlunderZone("plunder:room8", 8, 1950, 4442, 1969, 4455)
    };

    @Override
    public Plugin<Object> newInstance(Object arg) throws Throwable {
        for(PlunderZone room : ROOMS){
            ZoneBuilder.configure(room);
        }
        return this;
    }
    @Override
    public Object fireEvent(String identifier, Object... args) {
        return null;
    }

    public class PlunderZone extends MapZone {
        PyramidPlunderRoom room;
        Location entrance;
        private final Animation[] animations = new Animation[] { new Animation(2247), new Animation(2248), new Animation(1113), new Animation(2244) };
        final int swx, swy, nex, ney;
        final String name;
        final int roomnum;

        public PlunderZone(final String name, final int roomnum, final int swx, final int swy, final int nex, final int ney) {
            super(name, true);
            this.name = name;
            this.swx = swx;
            this.swy = swy;
            this.nex = nex;
            this.ney = ney;
            this.roomnum = roomnum;
            switch(roomnum){
                case 1:
                    room = PyramidPlunderRoom.ROOM_1;
                    break;
                case 2:
                    room = PyramidPlunderRoom.ROOM_2;
                    break;
                case 3:
                    room = PyramidPlunderRoom.ROOM_3;
                    break;
                case 4:
                    room = PyramidPlunderRoom.ROOM_4;
                    break;
                case 5:
                    room = PyramidPlunderRoom.ROOM_5;
                    break;
                case 6:
                    room = PyramidPlunderRoom.ROOM_6;
                    break;
                case 7:
                    room = PyramidPlunderRoom.ROOM_7;
                    break;
                case 8:
                    room = PyramidPlunderRoom.ROOM_8;
            }
        }

        @Override
        public void configure() {
            ZoneBorders borders = new ZoneBorders(swx, swy, nex, ney,0);
            register(borders);
        }

        @Override
        public boolean enter(Entity e){
            if(e instanceof Player && ((Player) e).getLocation().getZ() == 0) {
                e.asPlayer().getPacketDispatch().sendMessage("<col=7f03ff>Room: " + (roomnum) + " Level required: " + (21 + ((roomnum - 1) * 10)));
                e.asPlayer().getPlunderObjectManager().resetObjectsFor(e.asPlayer());
                PlunderSession session = e.asPlayer().getAttribute("plunder-session",null);
                if(session != null){
                    session.resetVars();
                }
                e.asPlayer().logoutListeners.put("plunder-logout", player -> {
                    player.setLocation(Location.create(3288, 2801, 0));
                    return Unit.INSTANCE;
                });
                updateRoomVarp(e.asPlayer());
            }
            return true;
        }

        @Override
        public boolean leave(Entity e, boolean logout) {
            e.asPlayer().logoutListeners.remove("plunder-logout");
            return super.leave(e, logout);
        }

        public void updateRoomVarp(Player player){
            Varp varp = player.varpManager.get(822);
            varp.setVarbit(0,room.reqLevel);
            varp.setVarbit(9,roomnum);
            player.getPacketDispatch().sendVarp(varp);
        }

        public boolean checkRequirements(Player player, PyramidPlunderRoom room){
            int requiredLevel = room.reqLevel;
            int playerLevel = player.getSkills().getLevel(Skills.THIEVING);
            return playerLevel >= requiredLevel;
        }

        public void rollSceptre(Player player){
            if(RandomFunction.random(1000) == 451){
                if(!player.getInventory().isFull()) {
                    player.getInventory().add(new Item(9044));
                    player.getPacketDispatch().sendMessage("<col=7f03ff>You find a strange object.");
                } else {
                    player.getBank().add(new Item(9044));
                    player.getPacketDispatch().sendMessage("<col=7f03ff>You sense that something has appeared in your bank.");
                }
            }
        }

        public final boolean success(final Player player, final int skill) {
            double level = player.getSkills().getLevel(skill);
            double successChance = Math.ceil((level * 50 - room.reqLevel) / room.reqLevel / 3 * 4);
            int roll = RandomFunction.random(99);
            if (successChance >= roll) {
                return true;
            }
            return false;
        }

        public void reward(Player player, int objid){
            Item[][] ARTIFACTS = { {new Item(9032),new Item(9036), new Item(9026)}, {new Item(9042), new Item(9030), new Item(9038)}, {new Item(9040), new Item(9028), new Item(9034)} };
            rollSceptre(player);
            switch(objid){
                case 16517: // Spear
                    player.getSkills().addExperience(Skills.THIEVING, (30 + (roomnum * 20)), true);
                    break;
                case 16503:
                case 16502:
                case 16501: // Urns
                    player.getSkills().addExperience(Skills.THIEVING, (25 + (roomnum * 20)), true);
                    player.getInventory().add(ARTIFACTS[((int)Math.floor((double) roomnum / 3))][RandomFunction.random(3)]);
                    break;
                case 16473: // Chest
                    player.getInventory().add(ARTIFACTS[RandomFunction.random(1, 3)][RandomFunction.random(3)]);
                    player.getPacketDispatch().sendMessage("And you find an artifact!");
                    player.getSkills().addExperience(Skills.THIEVING, (40 + (roomnum * 20)));
                    break;
                case 16495: // Sarcophagus
                    player.getPacketDispatch().sendMessage("You find some loot inside.");
                    player.getInventory().add(ARTIFACTS[RandomFunction.random(0,3)][RandomFunction.random(3)]);
                    player.getSkills().addExperience(Skills.STRENGTH,50 + (roomnum * 20));
                    break;

            }
        }


        @Override
        public boolean interact(Entity e, Node target, Option option) {
            final Player player = e instanceof Player ? e.asPlayer() : null;
            String optionName = option.getName().toLowerCase();
            if(target instanceof NPC){
                if(target.getName().contains("Guardian")) {
                    if (optionName.equals("talk-to")) {
                        player.getDialogueInterpreter().open(target.getId(), target.asNpc(), 0);
                    } else {
                        player.getDialogueInterpreter().open(target.getId(), target.asNpc(), 1);
                    }
                }
                if(optionName.equals("attack")){
                        player.getProperties().getCombatPulse().attack(target); 
                }
               return true; 
            }
            PlunderObject object = target instanceof Scenery ? new PlunderObject(target.asScenery()) : null;
            PlunderObjectManager manager = player.getPlunderObjectManager();
            if(object == null || manager == null) return super.interact(e, target, option);
            boolean alreadyOpened = manager.openedMap.getOrDefault(object.getLocation(),false);
            boolean charmed = manager.charmedMap.getOrDefault(object.getLocation(),false);
            boolean success = success(player, Skills.THIEVING);
            PlunderSession session = player.getAttribute("plunder-session",null);
            if(session == null){
                return false;
            }
            switch (object.getId()) {
                case 16517: //Spear trap
                    if(!checkRequirements(player,room)){
                        player.getPacketDispatch().sendMessage("You need to be at least level " + room.reqLevel + " thieving.");
                        return true;
                    }
                    player.getLocks().lockInteractions(2);
                    player.animate(animations[success ? 1 : 0]);
                    if (success) {
                        player.getPacketDispatch().sendMessage("You successfully pass the spears.");
                        int moveX = player.getLocation().getX();
                        int moveY = player.getLocation().getY();
                        Location moveto = new Location(moveX + room.spearX, moveY + room.spearY);
                        player.getProperties().setTeleportLocation(moveto);
                        //player.moveStep();
                    } else {
                        player.getPacketDispatch().sendMessage("You fail to pass the spears.");
                    }
                    return true;
                case 16503:
                case 16502:
                case 16501: // Urns
                    if (optionName.equals("search")) {
                        if (!checkRequirements(player,room)){
                            player.getPacketDispatch().sendMessage("You need to be at least level " + room.reqLevel + " thieving.");
                            return true;
                        }
                        if (alreadyOpened){
                            player.getPacketDispatch().sendMessage("You've already looted this.");
                            return true;
                        }

                        player.animate(animations[success ? 1 : 0]);
                        player.getLocks().lockInteractions(2);

                        if (!alreadyOpened && (success || charmed)) {
                            player.getPacketDispatch().sendMessage("You successfully search the urn...");
                            SceneryBuilder.replace(target.asScenery(), target.asScenery().transform(object.openId), 5);
                            manager.registerOpened(object);
                            reward(player,object.getId());
                        } else {
                            player.getPacketDispatch().sendMessage("You failed and got bit by a snake.");
                            player.getImpactHandler().manualHit(player,RandomFunction.random(2,8), ImpactHandler.HitsplatType.NORMAL);
                        }
                    } else if(optionName.equals("check for snakes")){
                        if(charmed){
                            player.getPacketDispatch().sendMessage("You already checked for snakes.");
                        } else {
                            player.getPacketDispatch().sendMessage("You check the urn for snakes...");
                            SceneryBuilder.replace(target.asScenery(), target.asScenery().transform(object.snakeId), 5);
                        }
                    }
                    return true;
                case 16509:
                case 16510:
                case 16511: // Snake urns
                    if(optionName.equals("search") && player.getInventory().containsItem(new Item(4605))){
                        player.animate(new Animation(1877));
                        player.getPacketDispatch().sendMessage("You charm the snake into docility.");
                        manager.registerCharmed(object);
                    } else {
                        player.getImpactHandler().manualHit(player, RandomFunction.random(2, 8), ImpactHandler.HitsplatType.NORMAL);
                        player.getPacketDispatch().sendMessage("The snake bites you.");
                    }
                    return true;
                case 16473: // Chest
                    if(optionName.equals("search")) {
                        boolean willSpawnSwarm = (RandomFunction.random(1,20) == 10);
                        if(!checkRequirements(player,room)){
                            player.getPacketDispatch().sendMessage("You need at least level " + room.reqLevel + " thieving to loot this.");
                            return true;
                        }
                        if (alreadyOpened){
                            player.getPacketDispatch().sendMessage("You've already looted this.");
                            return true;
                        }
                        player.getPacketDispatch().sendMessage("You search the chest...");
                        player.animate(animations[1]);
                        player.getLocks().lockInteractions(2);
                        if(willSpawnSwarm) {
                            player.getPacketDispatch().sendMessage("A swarm flies out!");
                            PyramidPlunderSwarmNPC swarm = new PyramidPlunderSwarmNPC(2001,player.getLocation(),player);
                            swarm.setRespawn(false);
                            swarm.setAggressive(true);
                            swarm.init();
                        } else {
                            reward(player,object.getId());
                        }
                        session.setChestOpen(true);
                        session.updateOverlay();
                        manager.registerOpened(object);
                        //ObjectBuilder.replace(target.asObject(), target.asObject().transform(object.openId), 5);
                    }
                    return true;
                case 16495: //Sarcophagus
                    if(optionName.equals("open")) {
                        if (!checkRequirements(player,room)){
                            player.getPacketDispatch().sendMessage("You need to be at least level " + room.reqLevel + " thieving.");
                            return true;
                        }
                        if (alreadyOpened){
                            player.getPacketDispatch().sendMessage("You've already looted this.");
                            return true;
                        }
                        boolean willSpawnMummy = (RandomFunction.random(1,5) == 3);
                        player.animate(animations[1]);
                        player.getPacketDispatch().sendMessage("You open the sarcophagus and....");
                        if(willSpawnMummy) {
                            player.getPacketDispatch().sendMessage("A mummy crawls out!");
                            PyramidPlunderMummyNPC mummy = new PyramidPlunderMummyNPC(1958, player.getLocation(),player);
                            mummy.setRespawn(false);
                            mummy.setAggressive(true);
                            mummy.init();
                        } else {
                            reward(player,object.getId());
                        }
                        session.setCoffinOpen(true);
                        session.updateOverlay();
                        manager.registerOpened(object);
                        //ObjectBuilder.replace(target.asObject(), target.asObject().transform(object.openId), 5);
                    }
                    return true;
                case 16475: //doors
                    if(optionName.equals("pick-lock") && roomnum < 8) {
                        if (!checkRequirements(player,room)){
                            player.getPacketDispatch().sendMessage("You need to be at least level " + room.reqLevel + " thieving.");
                            return true;
                        }
                        player.animate(animations[1]);
                        player.getLocks().lockInteractions(2);
                        boolean doesOpen = success(player, Skills.THIEVING);
                        if (doesOpen) {
                            PyramidPlunderRoom nextRoom = PyramidPlunderRoom.forRoomNum(roomnum + 1);
                            player.getPacketDispatch().sendMessage("The door opens!");
                            int index = room.doorLocations.indexOf(object.getLocation());
                            if(session.getCorrectDoorIndex() == index){
                                player.getProperties().setTeleportLocation(nextRoom.entrance);
                            } else {
                                session.setDoorOpen(index);
                                session.updateOverlay();
                            }
                        } else {
                            player.getPacketDispatch().sendMessage("You fail to unlock the door.");
                        }
                    } else if(roomnum == 8) {
                        ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location.create(3288, 2801, 0));
                    }
                    return true;
                case 16476:
                    player.getDialogueInterpreter().sendDialogue("This door doesn't seem to lead","anywhere.");
                    return true;
                default:
                    return super.interact(e, target, option);
            }
        }
    }
}
