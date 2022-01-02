package core.gui.tab;


import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.PlayerDetails;
import core.game.node.entity.player.info.Rights;
import core.game.node.item.Item;
import core.game.system.task.TaskExecutor;
import core.gui.ConsoleFrame;
import core.gui.ConsoleTab;
import core.tools.PlayerLoader;
import core.tools.StringUtils;
import kotlin.Unit;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the utility programs.
 *
 * @author Vexia
 */
public final class UtilityTab extends ConsoleTab {
    /**
     * The {@link Logger} instance.
     */
    private static final Logger logger = Logger.getLogger(UtilityTab.class.getName());
    /**
     * The utility tab.
     */
    private static final UtilityTab INSTANCE = new UtilityTab();

    /**
     * The serial UID.
     */
    private static final long serialVersionUID = -4962790192758757624L;

    /**
     * The list of players.
     */
    private final List<Player> players = new ArrayList<>(20);

    /**
     * The mapping of ips & their users.
     */
    private final Map<String, List<String>> IPS = new HashMap<>();

    /**
     * The mapping of macs & their users.
     */
    private final Map<String, List<String>> MACS = new HashMap<>();

    /**
     * The mapping of comp names & their users.
     */
    private final Map<String, List<String>> COMPS = new HashMap<>();

    /**
     * The players loaded list.
     */
    private final JLabel lblPlayersLoaded = new JLabel("Players loaded: ");

    /**
     * If the map is being populated.
     */
    private boolean populating;

    /**
     * Constructs a new {@Code UtilityTab} {@Code Object}
     */
    public UtilityTab() {
        super("Utilities");

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Resolvers & Trackers", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBackground(UIManager.getColor("Button.background"));
        panel.setBounds(18, 98, 152, 219);
        add(panel);
        panel.setLayout(null);
        JButton btnResolveIp = new JButton("Resolve Ip");
        btnResolveIp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resolve(IPS);
            }
        });
        btnResolveIp.setBounds(17, 28, 117, 29);
        panel.add(btnResolveIp);

        JButton btnResolveMac = new JButton("Resolve Mac");
        btnResolveMac.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resolve(MACS);
            }
        });
        btnResolveMac.setBounds(17, 69, 117, 29);
        panel.add(btnResolveMac);

        JButton btnResolveCompName = new JButton("Resolve Comp");
        btnResolveCompName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resolve(COMPS);
            }
        });
        btnResolveCompName.setBounds(17, 110, 117, 29);
        panel.add(btnResolveCompName);

        JButton btnPopulateMaps = new JButton("Populate Maps");
        btnPopulateMaps.addActionListener(e -> {
            populateList();
            JOptionPane.showMessageDialog(null, "Finished populating maps!");
        });
        btnPopulateMaps.setBounds(405, 32, 236, 29);
        add(btnPopulateMaps);

        JLabel lblMustBeDone = new JLabel("MUST BE DONE BEFORE RUNNING TOOLS");
        lblMustBeDone.setBounds(390, 6, 379, 16);
        add(lblMustBeDone);

        lblPlayersLoaded.setBounds(460, 61, 157, 16);
        add(lblPlayersLoaded);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Wealth & Items", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setBackground(UIManager.getColor("Button.background"));
        panel_1.setBounds(182, 98, 145, 219);
        add(panel_1);

        JButton btnWealthScanner = new JButton("Wealth Scanner");
        btnWealthScanner.addActionListener(e -> wealthScanner());
        btnWealthScanner.setBounds(6, 29, 129, 29);
        panel_1.add(btnWealthScanner);

        JButton btnItemScanner = new JButton("Item Scanner");
        btnItemScanner.addActionListener(e -> itemScanner());
        btnItemScanner.setBounds(6, 69, 129, 29);
        panel_1.add(btnItemScanner);

        JPanel panel_2 = new JPanel();
        panel_2.setLayout(null);
        panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Player Utils", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_2.setBackground(UIManager.getColor("Button.background"));
        panel_2.setBounds(345, 98, 145, 219);
        add(panel_2);

        JButton btnStaffChecker = new JButton("Staff Scanner");
        btnStaffChecker.addActionListener(e -> staffScanner());
        btnStaffChecker.setBounds(6, 29, 129, 29);
        panel_2.add(btnStaffChecker);

        JPanel panel_3 = new JPanel();
        panel_3.setLayout(null);
        panel_3.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Security", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_3.setBackground(UIManager.getColor("Button.background"));
        panel_3.setBounds(502, 98, 145, 219);
        add(panel_3);

        JButton btnMacViewer = new JButton("Mac Viewer");
        btnMacViewer.addActionListener(e -> viewMacs());
        btnMacViewer.setBounds(6, 29, 129, 29);
        panel_3.add(btnMacViewer);

        JButton btnMacRemover = new JButton("Mac Remover");
        btnMacRemover.addActionListener(e -> removeMac());
        btnMacRemover.setBounds(6, 64, 129, 29);
        panel_3.add(btnMacRemover);
    }

    /**
     * Removes a mac.
     */
    public void removeMac() {
        String mac = JOptionPane.showInputDialog("Enter address:");
        if (mac == null || mac.length() <= 1) {
            JOptionPane.showMessageDialog(null, "Error! Nothing entered.");
            return;
        }
//        if (SystemManager.getSecurity().isBanned(mac)) {
//            SystemManager.getSecurity().remove(mac);
//            JOptionPane.showMessageDialog(null, "Removed the mac - " + mac + "!");
//        } else {
//            JOptionPane.showMessageDialog(null, "Error! Couldn't find mac in system manager.");
//        }
    }

    /**
     * Views the banned macs.
     */
    public void viewMacs() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JScrollPane pane = new JScrollPane();
        DefaultListModel<String> model = new DefaultListModel<String>();
        JList<String> list = new JList<String>();
//        for (String i : SystemManager.getSecurity().getBannedAddresses()) {
//            if (i == null) {
//                continue;
//            }
//            model.addElement(i.toString());
//        }
        list.setModel(model);
        pane.setViewportView(list);
        panel.add(pane);
        JOptionPane.showConfirmDialog(null, panel, "Banned Addresses", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Wealth scanner.
     */
    public void wealthScanner() {
        String args = JOptionPane.showInputDialog("Enter item id:");
        int id = -1;
        if (args != null && args.length() > 0) {
            try {
                id = Integer.parseInt(args);
            } catch (NumberFormatException e) {
                id = -1;
                logger.log(Level.WARNING, "Error in wealth scanner, number exception!");
            }
        }
        List<String> data = wealthScanner(id);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JScrollPane pane = new JScrollPane();
        DefaultListModel<String> model = new DefaultListModel<String>();
        JList<String> list = new JList<String>();
        for (String i : data) {
            if (i == null) {
                continue;
            }
            model.addElement(i.toString());
        }
        list.setModel(model);
        pane.setViewportView(list);
        panel.add(pane);
        JOptionPane.showConfirmDialog(null, panel, "Wealth Scanner", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    }

    /**
     * Wealth scanner.
     */
    public List<String> wealthScanner(int itemId) {
        Item item = null;
        if (itemId != -1) {
            item = new Item(itemId);
        }
        Object[][] mostWealthy = new Object[50][2];
        int player = 0;
        long count = 0;
        for (Player p : players) {
            if (p.getDetails().getRights() == Rights.PLAYER_MODERATOR) {
                continue;
            }
            if (item != null) {
                long old = count;
                count += p.getInventory().getAmount(item);
                count += p.getEquipment().getAmount(item);
                count += p.getBank().getAmount(item);
                if (old != count) {
                    player++;
                }
            }
            for (int i = 0; i < mostWealthy.length; i++) {
                Long value = (Long) mostWealthy[i][0];
                if (value == null || value < p.getMonitor().getNetworth()) {
                    for (int j = mostWealthy.length - 1; j > i; j--) {
                        mostWealthy[j] = mostWealthy[j - 1];
                    }
                    mostWealthy[i] = new Object[2];
                    mostWealthy[i][0] = p.getMonitor().getNetworth();
                    mostWealthy[i][1] = p.getName();
                    break;
                }
            }
        }
        List<String> formats = new ArrayList<>(20);
        for (int i = mostWealthy.length - 1; i >= 0; i--) {
            Object[] info = mostWealthy[i];
            if (info != null && info[0] != null) {
                formats.add(i + ": " + info[1] + " - " + NumberFormat.getNumberInstance(Locale.US).format((Long) info[0]) + " coins.");
            }
        }
        if (item != null) {
            formats.add(NumberFormat.getNumberInstance(Locale.US).format(count) + " occurrences of item " + item.getName() + " (id=" + item.getId() + ") by " + player + " players!");
        }
        return formats;
    }

    /**
     * The item scanner.
     */
    public void itemScanner() {
        String args = JOptionPane.showInputDialog("Enter item id:");
        Item item = new Item(11694);
        if (args != null && args.length() > 0) {
            try {
                item = new Item(Integer.parseInt(args));
            } catch (NumberFormatException e) {
                item = new Item(11694);
                logger.log(Level.WARNING, "Error in wealth scanner, number exception!");
            }
        }
        List<String> formats = itemScanner(item);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JScrollPane pane = new JScrollPane();
        DefaultListModel<String> model = new DefaultListModel<String>();
        JList<String> list = new JList<String>();
        for (String i : formats) {
            if (i == null) {
                continue;
            }
            model.addElement(i.toString());
        }
        list.setModel(model);
        pane.setViewportView(list);
        panel.add(pane);
        JOptionPane.showConfirmDialog(null, panel, "Item Scanner", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Scans for items in the eco.
     */
    public List<String> itemScanner(Item item) {
        Map<String, Integer> map = new HashMap<>();
        for (Player p : players) {
            List<core.game.container.Container> containers = new ArrayList<>(20);
            containers.add(p.getInventory());
            containers.add(p.getBank());
            containers.add(p.getEquipment());
            int amount = 0;
            for (core.game.container.Container container : containers) {
                Item i = container.getItem(item);
                if (i != null) {
                    amount += i.getAmount();
                }
            }
            if (amount > 0) {
                map.put(p.getName(), amount);
            }
        }
        List<String> formats = new ArrayList<>(20);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            formats.add("Player name=" + entry.getKey() + ", contained a total of " + entry.getValue() + " " + item.getName() + ".");
        }
        if (formats.size() == 0) {
            formats.add("There were no occurrences of " + item + ".");
        }
        return formats;
    }

    /**
     * Resolves the address.
     *
     * @param map the map.
     */
    public void resolve(Map<String, List<String>> map) {
        String address = JOptionPane.showInputDialog("Enter the address.");
        List<String> names = resolve(map, address, false);
        String string = "No names found registered with that address!";
        if (names == null) {
            JOptionPane.showMessageDialog(null, string);
        } else {
            string = "Names:" + Arrays.toString(names.toArray());
            JOptionPane.showMessageDialog(null, string);
        }
    }

    /**
     * Resolves & tracks an adddress.
     *
     * @param map   the map.
     * @param value the value.
     * @param name  if the name or not.
     * @return the names resolved.
     */
    public List<String> resolve(Map<String, List<String>> map, String value, boolean name) {
        List<String> names = null;
        String address = !name ? value : "";
        if (name) {
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                for (String s : entry.getValue()) {
                    if (s.equals(value)) {
                        address = entry.getKey();
                        break;
                    }
                }
            }
        }
        names = map.get(address);
        return names;
    }

    /**
     * Gets the mapping.
     *
     * @param id the id.
     * @return the map.
     */
    public Map<String, List<String>> getMap(int id) {
        return id == 1 ? IPS : id == 2 ? MACS : COMPS;
    }

    /**
     * Populates the lists.
     */
    public void populateList() {
        if (populating) {
            return;
        }
        populating = true;
        players.clear();
        TaskExecutor.execute(() -> {
            Thread.currentThread().setName("Utility Tab");
            ConsoleFrame.getInstance().getPlayerTab().getPlayerNames().clear();
            ConsoleFrame.getInstance().getPlayerTab().populatePlayerSearch();
            for (String name : ConsoleFrame.getInstance().getPlayerTab().getPlayerNames()) {
                Player player = PlayerLoader.getPlayerFile(name);
                if (player == null) {
                    continue;
                }
                PlayerDetails details = player.getDetails();
                if (details != null) {
                    addAddressToMap(name, details.getIpAddress(), IPS);
                    addAddressToMap(name, details.getMacAddress(), MACS);
                    addAddressToMap(name, details.getCompName(), COMPS);
                    players.add(player);
                }
            }
            populating = false;
            System.gc();
            lblPlayersLoaded.setText("Players loaded: " + players.size());
            return Unit.INSTANCE;
        });
    }

    /**
     * Checks the staff.
     */
    public void staffScanner() {
        List<String> staff = new ArrayList<>(20);
        for (Player player : players) {
            if (player.getDetails().getRights() != Rights.REGULAR_PLAYER) {
                staff.add(player.getName() + " - " + StringUtils.formatDisplayName(player.getDetails().getRights().toString()));
            }
        }
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JScrollPane pane = new JScrollPane();
        DefaultListModel<String> model = new DefaultListModel<String>();
        JList<String> list = new JList<String>();
        for (String i : staff) {
            if (i == null) {
                continue;
            }
            model.addElement(i.toString());
        }
        list.setModel(model);
        pane.setViewportView(list);
        panel.add(pane);
        JOptionPane.showConfirmDialog(null, panel, "Staff Scanner", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Adds an address with its names to the map.
     *
     * @param name    the name.
     * @param address the address.
     * @param map     the map.
     */
    public void addAddressToMap(String name, String address, Map<String, List<String>> map) {
        if (address == null) {
            return;
        }
        List<String> names = map.get(address);
        if (names == null) {
            names = new ArrayList<String>();
        }
        names.add(name);
        map.put(address, names);
    }

    /**
     * Deletes a player.
     *
     * @param details the details.
     */
    public void delete(PlayerDetails details) {
        if (details.getRights() == Rights.PLAYER_MODERATOR) {
            return;
        }
        // TODO
//        Connection connection = SQLManager.getConnection();
//        PreparedStatement statement;
//        try {
//            statement = connection.prepareStatement("DELETE FROM " + "playerdata" + " WHERE " + "" + "username" + "='" + details.getUsername().toLowerCase() + "'");
//            statement.execute();
//            statement = connection.prepareStatement("DELETE FROM " + "highscores" + " WHERE " + "" + "username" + "='" + details.getUsername().toLowerCase() + "'");
//            statement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        File detailFile = new File(GameConstants.getPlayerDetailsPath() + "/" + details.getUsername() + ".store");
//        File playerFile = new File(GameConstants.getPlayerSavePath() + "/" + details.getUsername() + ".store");
//        detailFile.delete();
//        playerFile.delete();
//        SQLManager.close(connection);
//        ConsoleFrame.getInstance().getPlayerTab().getPlayerNames().remove(details.getUsername());

    }

    /**
     * Gets the player.
     *
     * @param name the name.
     * @return the player.
     */
    public Player getPlayer(String name) {
        if (players.size() == 0) {
            populateList();
            return null;
        }
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Gets the ips.
     *
     * @return the ips.
     */
    public Map<String, List<String>> getIps() {
        return IPS;
    }

    /**
     * Gets the macs.
     *
     * @return the macs.
     */
    public Map<String, List<String>> getMacs() {
        return MACS;
    }

    /**
     * Gets the comps.
     *
     * @return the comps.
     */
    public Map<String, List<String>> getComps() {
        return COMPS;
    }

    /**
     * Gets the players.
     *
     * @return the players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the populating.
     *
     * @return the populating
     */
    public boolean isPopulating() {
        return populating;
    }

    /**
     * Sets the bapopulating.
     *
     * @param populating the populating to set.
     */
    public void setPopulating(boolean populating) {
        this.populating = populating;
    }

    /**
     * Gets the instance.
     *
     * @return the instance
     */
    public static UtilityTab getInstance() {
        return INSTANCE;
    }
}
