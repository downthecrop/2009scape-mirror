package core.gui.component;


import core.game.container.Container;
import core.game.node.entity.player.Player;
import core.game.node.entity.player.info.PlayerDetails;
import core.game.node.item.Item;
import rs09.game.world.GameWorld;
import rs09.game.world.repository.Repository;
import core.gui.ConsoleFrame;
import core.tools.PlayerLoader;
import core.tools.StringUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Views a players details.
 *
 * @author Vexia
 */
public class PlayerViewer extends JPanel {

    /**
     * The serail UID.
     */
    private static final long serialVersionUID = 1589056461884384398L;

    /**
     * The name of the viewer.
     */
    private final String name;

    /**
     * The player details.
     */
    private PlayerDetails details;

    /**
     * The player file.
     */
    private Player player;

    /**
     * Constructs a new {@Code PlayerViewer} {@Code Object}
     *
     * @param name the name.
     */
    public PlayerViewer(String name) {
        super();
        this.name = name;
        this.details = PlayerLoader.getPlayerDetailFile(name);
        if (Repository.getPlayerByName(name) != null) {
            setPlayer(Repository.getPlayerByName(name));
        } else {
            if (GameWorld.getMajorUpdateWorker().getStarted()) {
                setPlayer(PlayerLoader.getPlayerFile(name));
            }
        }
        setBounds(new Rectangle(0, 0, 755, 539));
        setLayout(null);
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> ConsoleFrame.getInstance().getPlayerTab().closeViewer(PlayerViewer.this));
        btnClose.setBounds(6, 6, 117, 29);
        add(btnClose);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(6, 35, 286, 428);
        add(panel);
        panel.setLayout(null);

        JLabel lblName = new JLabel("Username: " + details.getUsername());
        lblName.setBounds(6, 16, 224, 16);
        panel.add(lblName);

        JLabel lblIp = new JLabel("IP: " + details.getInfo().getIp());
        lblIp.setBounds(6, 96, 190, 16);
        panel.add(lblIp);

        JLabel lblMac = new JLabel("Mac: " + details.getInfo().getMac());
        lblMac.setBounds(6, 124, 224, 16);
        panel.add(lblMac);

        JLabel lblCompName = new JLabel("Comp name: " + details.getInfo().getCompName());
        lblCompName.setBounds(6, 208, 236, 16);
        panel.add(lblCompName);

        JLabel lblLastLogin = new JLabel("Last login: " + new Date(details.getLastLogin()));
        lblLastLogin.setBounds(6, 152, 274, 16);
        panel.add(lblLastLogin);

        JLabel lblStatus = new JLabel("Status: " + getStatus());
        lblStatus.setBounds(6, 236, 149, 16);
        panel.add(lblStatus);

//		JLabel lblCredits = new JLabel("Credits: " + details.getShop().getCredits());
//		lblCredits.setBounds(6, 180, 139, 16);
//		panel.add(lblCredits);

        JLabel lblRights = new JLabel("Rights: " + StringUtils.formatDisplayName(details.getRights().toString()));
        lblRights.setBounds(6, 68, 176, 16);
        panel.add(lblRights);

//		JLabel lblDisplayName = new JLabel("Display name: " + details.getUsername());
//		lblDisplayName.setBounds(6, 40, 213, 16);
//		panel.add(lblDisplayName);

        JLabel lblNetworth = new JLabel("Networth:");
        lblNetworth.setBounds(6, 264, 165, 16);
        panel.add(lblNetworth);

        JLabel lblBanned = new JLabel("Banned: " + (details.getBanTime() > 0));
        lblBanned.setBounds(6, 292, 149, 16);
        panel.add(lblBanned);

        JLabel lblMuted = new JLabel("Muted: " + (details.getMuteTime() > 0));
        lblMuted.setBounds(6, 317, 149, 16);
        panel.add(lblMuted);

        JLabel lblActive = new JLabel("Active: " + checkActive());
        lblActive.setBounds(6, 342, 165, 16);
        panel.add(lblActive);
        if (player != null) {
            lblNetworth.setText("Networth: " + player.getMonitor().getNetworth() + "");
        }

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBounds(124, 6, 117, 29);
        add(btnRefresh);

        JButton btnViewContainer = new JButton("View Container");
        btnViewContainer.addActionListener(e -> {
            if (player == null) {
                return;
            }
            viewContainer();
        });
        btnViewContainer.setBounds(365, 6, 144, 29);
        add(btnViewContainer);

        JButton btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account!?");
            if (result == 0) {
                ConsoleFrame.getInstance().getUtilityTab().delete(details);
                ConsoleFrame.getInstance().getPlayerTab().closeViewer(PlayerViewer.this);
            }
        });
        btnDelete.setBounds(244, 6, 117, 29);
        add(btnDelete);
    }

    /**
     * Opens the view containermanager.
     */
    public void viewContainer() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        ActionListener listener = e -> {
            String name1 = e.getActionCommand();
            Container container = null;
            switch (name1) {
                case "Inventory":
                    container = player.getInventory();
                    break;
                case "Equipment":
                    container = player.getEquipment();
                    break;
                case "Bank":
                    container = player.getBank();
                    break;
            }
            if (container != null) {
                showContainer(container, name1);
            }
        };
        JButton invy = new JButton("Inventory");
        panel.add(invy);
        invy.addActionListener(listener);
        JButton equip = new JButton("Equipment");
        panel.add(equip);
        equip.addActionListener(listener);
        JButton bank = new JButton("Bank");
        bank.addActionListener(listener);
        panel.add(bank);
        JOptionPane.showConfirmDialog(null, panel, "View Container", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Shows a container.
     *
     * @param container the container.
     * @param name      the name.
     */
    public void showContainer(core.game.container.Container container, String name) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JScrollPane pane = new JScrollPane();
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>();
        for (Item i : container.toArray()) {
            if (i == null) {
                continue;
            }
            model.addElement(i.toString());
        }
        list.setModel(model);
        pane.setViewportView(list);
        panel.add(pane);
        JOptionPane.showConfirmDialog(null, panel, name, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Checks if the player is active.
     *
     * @return {@code True} if so.
     */
    public String checkActive() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date cutOff = null;
        long mili = 0;
        try {
            cutOff = sdf.parse("2014-6-10");
            mili = cutOff.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ((details.getLastLogin() < mili) ? "Not " : "") + "Active";
    }

    /**
     * Checks active status.
     *
     * @return the status.
     */
    public String getStatus() {
        return Repository.getPlayerByName(name) != null ? "Online" : "Offline";
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the baplayer.
     *
     * @param player the player to set.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
