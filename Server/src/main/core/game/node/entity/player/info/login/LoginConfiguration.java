package core.game.node.entity.player.info.login;

import core.game.component.CloseEvent;
import core.game.component.Component;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.link.emote.Emotes;
import core.game.node.item.Item;
import core.game.world.map.RegionManager;
import core.net.packet.PacketRepository;
import core.net.packet.context.InterfaceContext;
import core.net.packet.out.Interface;
import core.plugin.Plugin;
import core.ServerConstants;
import core.game.interaction.InteractionListeners;
import content.global.handlers.iface.RulesAndInfo;
import core.tools.Log;
import core.game.world.GameWorld;
import core.game.world.repository.Repository;
import core.game.world.update.UpdateSequence;
import core.game.node.entity.player.link.SpellBookManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static core.api.ContentAPIKt.*;
import static core.tools.GlobalsKt.colorize;
import content.data.Quests;


/**
 * Sends the login configuration packets.
 *
 * @author Emperor
 */
public final class LoginConfiguration {

    /**
     * The login plugins.
     */
    private static final List<Plugin<Object>> LOGIN_PLUGINS = new ArrayList<>(20);

    /**
     * The lobby pane component.
     */
    private static final Component LOBBY_PANE = new Component(549);

    /**
     * The lobby message of the week models & constant to be set for auto selecting the models
     */
    private static final int[] MESSAGE_MODEL = {15, 16, 17, 18, 19, 20, 21, 22, 23, 405, 447, 622, 623, 679, 715, 800};
    private static int messModel;

    /**
     * The lobby interface close event.
     */
    private static final Component LOBBY_INTERFACE = new Component(378).setCloseEvent(new CloseEvent() {
        @Override
        public boolean close(Player player, Component c) {
            return player.getLocks().isLocked("login");
        }
    });

    /**
     * Constructs a new {@Code LoginConfiguration} {@Code Object}
     */
    public LoginConfiguration() {
        /*
         * empty.
         */
    }

    /**
     * Configures the lobby login.
     *
     * @param player The player.
     */
    public static void configureLobby(Player player) {
        player.updateSceneGraph(true);
        if (!player.isArtificial() && player.getAttribute("tutorial:complete",false) && player.getAttribute("login_type", LoginType.NORMAL_LOGIN) != LoginType.RECONNECT_TYPE) {
            sendLobbyScreen(player);
        } else {
            configureGameWorld(player);
        }
    }

    /**
     * Sends the lobby interface-related packets.
     *
     * @param player The player.
     */
    public static void sendLobbyScreen(Player player) {
        messModel = autoSelect();
        for(Player p : Repository.getLobbyPlayers()){
            if(p.getName().equals(player.getName())){
                p.clear();
                Repository.getLobbyPlayers().remove(p);
                break;
            }
        }
        Repository.getLobbyPlayers().add(player);
        player.getPacketDispatch().sendString(getLastLogin(player), 378, 116);
        player.getPacketDispatch().sendString("Welcome to " + GameWorld.getSettings().getName(), 378, 115);
        player.getPacketDispatch().sendString(" ", 378, 37);
        player.getPacketDispatch().sendString("Want to stay up to date with the latest news and updates? Join our <br>discord by using the link below!", 378, 38);
        player.getPacketDispatch().sendString(" ", 378, 39);
        player.getPacketDispatch().sendString("Discord Invite", 378, 14);
        player.getPacketDispatch().sendString("Discord Invite", 378, 129);
        player.getPacketDispatch().sendString("Credits", 378, 94);
        player.getPacketDispatch().sendString(player.getDetails().getCredits() + "", 378, 96);
        player.getPacketDispatch().sendString(" ", 378, 229);
        player.getPacketDispatch().sendString("Want to contribute to " + ServerConstants.SERVER_NAME + "? <br>Visit the GitLab using the link below!", 378, 230);
        player.getPacketDispatch().sendString(" ", 378, 231);
        player.getPacketDispatch().sendString("Github", 378, 240);
        player.getPacketDispatch().sendString(GameWorld.getSettings().getMessage_string(), messModel, getMessageChild(messModel));
        player.getPacketDispatch().sendString("You can gain more credits by voting, reporting bugs and various other methods of contribution.", 378, 93);
        player.getInterfaceManager().openWindowsPane(LOBBY_PANE);
        player.getInterfaceManager().setOpened(LOBBY_INTERFACE);
        PacketRepository.send(Interface.class, new InterfaceContext(player, 549, 2, 378, true));
        PacketRepository.send(Interface.class, new InterfaceContext(player, 549, 3, messModel, true));//UPDATE `configs` SET `value`=FLOOR(RAND()*(25-10)+10) WHERE key_="messageInterface"
        player.getDetails().setLastLogin(System.currentTimeMillis());
    }

    /**
     * Configures the game world.
     *
     * @param player The player.
     */
    public static void configureGameWorld(final Player player) {
        sendGameConfiguration(player);
        Repository.getLobbyPlayers().remove(player);
        player.setPlaying(true);
        UpdateSequence.getRenderablePlayers().add(player);
        RegionManager.move(player);
        player.getMusicPlayer().init();
        player.updateAppearance();
        player.getPlayerFlags().setUpdateSceneGraph(true);
        player.getPacketDispatch().sendInterfaceConfig(226, 1, true);

        if(player.getGlobalData().getTestStage() == 3 && !player.getEmoteManager().isUnlocked(Emotes.SAFETY_FIRST)){
            player.getEmoteManager().unlock(Emotes.SAFETY_FIRST);
        }

        for (Item item : player.getEquipment().toArray()) {
            //Run equip hooks for all items equipped on login.
            //We should have already been doing this.
            //Frankly, I don't even want to imagine the number of bugs us *not* doing this has caused.
            if (item == null) continue;
            player.getEquipment().remove(item);
            if (!InteractionListeners.run(item.getId(), player, item, true) || !player.getEquipment().add(item, true, false)) {
                player.sendMessage(colorize("%RAs you can no longer wear " + item.getName() + ", it has been unequipped."));
                addItemOrBank(player, item.getId(), item.getAmount());
            }
        }

        SpellBookManager.SpellBook currentSpellBook = SpellBookManager.SpellBook.forInterface(player.getSpellBookManager().getSpellBook());
        if (currentSpellBook == SpellBookManager.SpellBook.ANCIENT && !hasRequirement(player, Quests.DESERT_TREASURE)) {
            player.sendMessage(colorize("%RAs you can no longer use Ancient Magic, you have been set back to Modern."));
            player.getSpellBookManager().setSpellBook(SpellBookManager.SpellBook.MODERN);
        } else if (currentSpellBook == SpellBookManager.SpellBook.LUNAR && !hasRequirement(player, Quests.LUNAR_DIPLOMACY)) {
            player.sendMessage(colorize("%RAs you can no longer use Lunar Magic, you have been set back to Modern."));
            player.getSpellBookManager().setSpellBook(SpellBookManager.SpellBook.MODERN);
        }
        player.getSpellBookManager().update(player);

        // 1050 is checked client-side for making piety/chivalry disallowed sfx, likely due to the minigame requirement.
        // Set it here unconditionally until the minigame is implemented.
        setVarbit(player, 3909, 8, false);
        if(ServerConstants.RULES_AND_INFO_ENABLED)
            RulesAndInfo.openFor(player);
		/*if (GameWorld.getSettings().isPvp()) {
			player.getPacketDispatch().sendString("", 226, 1);
		}*/
        /*if (TutorialSession.getExtension(player).getStage() != 73) {
            TutorialStage.load(player, TutorialSession.getExtension(player).getStage(), true);
        }*/
    }

    /**
     * Sends the game configuration packets.
     *
     * @param player The player to send to.
     */
    public static void sendGameConfiguration(final Player player) {
        player.getInterfaceManager().openWindowsPane(new Component(player.getInterfaceManager().isResizable() ? 746 : 548));
        player.getInterfaceManager().openChatbox(137);
        player.getInterfaceManager().openDefaultTabs();
        welcome(player);
        config(player);
        for (Plugin<Object> plugin : LOGIN_PLUGINS) {
            try {
                plugin.newInstance(player);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        player.getAppearance().sync();
    }

    /**
     * Method used to welcome the player.
     *
     * @param player the player. Fullscreen mode Object id:
     */
    public static final void welcome(final Player player) {
        if (GameWorld.getSettings().isPvp()) {
            player.getPacketDispatch().sendString("", 226, 0);
        }
        if (player.isArtificial()) {

            return;
        }
        player.getPacketDispatch().sendMessage("Welcome to " + GameWorld.getSettings().getName() + ".");
        //player.getPacketDispatch().sendMessage("You are currently playing in beta version 1.2");
        if (player.getDetails().isMuted()) {
            player.getPacketDispatch().sendMessage("You are muted.");
            player.getPacketDispatch().sendMessage("To prevent further mutes please read the rules.");
        }
//		ResourceAIPManager.get().load(player);
//		ResourceAIPManager.get().save(player);
    }

    /**
     * Method used to configure all possible settings for the player.
     *
     * @param player the player.
     */
    public static final void config(final Player player) {
        if(!player.isArtificial())
            log(LoginConfiguration.class, Log.INFO, "configuring player " + player.getUsername());
        player.getInventory().refresh();
        player.getEquipment().refresh();
        player.getSkills().refresh();
        player.getSkills().configure();
        player.getSettings().update();
        player.getInteraction().setDefault();
        player.getPacketDispatch().sendRunEnergy();
        player.getFamiliarManager().login();
        player.getInterfaceManager().openDefaultTabs();
        player.getPacketDispatch().sendString("Friends List - World " + GameWorld.getSettings().getWorldId(), 550, 3);
        player.getQuestRepository().syncronizeTab(player);
        player.getInterfaceManager().close();
        player.getEmoteManager().refresh();
        player.getInterfaceManager().openInfoBars();
        if(!player.isArtificial())
            log(LoginConfiguration.class, Log.INFO, "finished configuring player " + player.getUsername());
    }

    /**
     * Gets the message child for the inter id.
     * @notice GameSettings.kt contains the list of what these are
     * @param interfaceId The interface id.
     * @return The child id.
     */
    public static int getMessageChild(int interfaceId) {
        if (interfaceId == 622) {
            return 8;
        } else if (interfaceId == 16) {
            return 6;
        } else if (interfaceId == 17 || interfaceId == 15 || interfaceId == 18 || interfaceId == 19 || interfaceId == 21 || interfaceId == 22 || interfaceId == 447 || interfaceId == 405) {
            return 4;
        } else if (interfaceId == 20 || interfaceId == 623) {
            return 5;
        } else if (interfaceId == 23 || interfaceId == 800) {
            return 3;
        } else if (interfaceId == 715) {
            return 2;
        } else if (interfaceId == 679) {
            return 1;
        }
        return 0;
    }

    /**
     * Sets a random interface id for the "message of the week" models
     */
    private final static int autoSelect() {
        boolean contains = IntStream.of(MESSAGE_MODEL).anyMatch(x -> x == GameWorld.getSettings().getMessage_model());
        return contains ? GameWorld.getSettings().getMessage_model():MESSAGE_MODEL[new Random().nextInt(MESSAGE_MODEL.length)];
    }

    /**
     * Gets the last login string for the lobby.
     *
     * @param player the player.
     * @return the last login.
     */
    public static String getLastLogin(Player player) {
        String lastIp = player.getDetails().accountInfo.getLastUsedIp();
        if (lastIp.equals("")) {
            lastIp = player.getDetails().getIpAddress();
        }
        player.getDetails().accountInfo.setLastUsedIp(player.getDetails().getIpAddress());
        String string = "You last logged in @timeAgo from: " + lastIp;
        long time = player.getDetails().getLastLogin();
        Date lastLogin = new Date(time);
        Date current = new Date();
        long diff = current.getTime() - lastLogin.getTime();
        int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if (days < 1) {
            string = string.replace("@timeAgo", "earlier today");
        } else if (days == 1) {
            string = string.replace("@timeAgo", "yesterday");
        } else {
            string = string.replace("@timeAgo", days + " days ago");
        }
        return string;
    }


    /**
     * Gets the loginPlugins.
     *
     * @return The loginPlugins.
     */
    public static List<Plugin<Object>> getLoginPlugins() {
        return LOGIN_PLUGINS;
    }

}
