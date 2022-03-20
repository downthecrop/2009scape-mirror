package org.runite.client;

import org.rs09.SystemLogger;
import org.rs09.client.config.GameConfig;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import java.util.Hashtable;

public class ModPanel extends JPanel {
    ButtonListener listener = new ButtonListener();
    DebugMenu debugMenu = new DebugMenu();
    ItemMenu itemMenu = new ItemMenu();
    NPCMenu npcMenu = new NPCMenu();
    PlayerMenu playerMenu = new PlayerMenu();
    TeleMenu teleMenu = new TeleMenu();
    ModelMenu modelMenu = new ModelMenu();

    public ModPanel() {
        setVisible(false);
        JButton teleMenu = new JButton("Tele");
        teleMenu.setActionCommand("tele");
        teleMenu.addActionListener(listener);

        JButton playerMenu = new JButton("Player");
        playerMenu.setActionCommand("player");
        playerMenu.addActionListener(listener);

        JButton npcMenu = new JButton("NPC");
        npcMenu.setActionCommand("npc");
        npcMenu.addActionListener(listener);

        JButton itemMenu = new JButton("Item");
        itemMenu.setActionCommand("item");
        itemMenu.addActionListener(listener);

        JButton debugMenu = new JButton("Debug");
        debugMenu.setActionCommand("debug");
        debugMenu.addActionListener(listener);

        JButton modelMenu = new JButton("Model");
        modelMenu.setActionCommand("model");
        modelMenu.addActionListener(listener);

        JButton closeButton = new JButton("Close Panel");
        closeButton.setActionCommand("close");
        closeButton.addActionListener(listener);

        add(teleMenu);
        add(playerMenu);
        add(npcMenu);
        add(itemMenu);
        add(debugMenu);
        add(modelMenu);
        add(closeButton, BorderLayout.EAST);
    }

    private void sendCommand(String command) {
        TextureOperation12.outgoingBuffer.putOpcode(44);
        TextureOperation12.outgoingBuffer.writeByte(command.length() + -1);
        TextureOperation12.outgoingBuffer.writeString(command.substring(2));
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            switch (actionEvent.getActionCommand()) {
                case "close":
                    ClientLoader.setModPanelVisible(false);
                    break;
                case "debug":
                    debugMenu.setVisible(true);
                    break;
                case "item":
                    itemMenu.open();
                    break;
                case "npc":
                    npcMenu.open();
                    break;
                case "player":
                    playerMenu.open();
                    break;
                case "tele":
                    teleMenu.open();
                    break;
                case "model":
                    modelMenu.open();
                    break;
            }
        }
    }

    class TeleMenu extends JFrame {
        Object[][] rowData = {{"2974,4383,2", "corporeal beast"}, {"2659,2649,0", "pest control"}, {"3293,3184,0", "al kharid"}, {"3222,3217,0", "lumbridge"}, {"3110,3168,0", "wizard's tower"}, {"3083,3249,0", "draynor village"}, {"3019,3244,0", "port sarim"}, {"2956,3209,0", "rimmington"}, {"2965,3380,0", "falador"}, {"2895,3436,0", "taverly"}, {"3080,3423,0", "barbarian village"}, {"3213,3428,0", "varrock"}, {"3164,3485,0", "grand exchange"}, {"2917,3175,0", "karamja"}, {"2450,5165,0", "tzhaar"}, {"2795,3177,0", "brimhaven"}, {"2849,2961,0", "shilo village"}, {"2605,3093,0", "yanille"}, {"2663,3305,0", "ardougne"}, {"2450,3422,0", "tree gnome stronghold"}, {"2730,3485,0", "camelot"}, {"2730,3485,0", "seer's village"}, {"2805,3435,0", "catherby"}, {"2658,3657,0", "relleka"}, {"2890,3676,0", "trollheim"}, {"2914,3746,0", "god wars dungeon"}, {"3180,3684,0", "bounty hunter"}, {"3272,3687,0", "clan wars"}, {"3090,3957,0", "mage arena"}, {"3069,10257,0", "king black dragon"}, {"3359,3416,0", "digsite"}, {"3488,3489,0", "canifis"}, {"3428,3526,0", "slayer tower"}, {"3502,9483,0", "kalphite queen"}, {"3233,2913,0", "pyramid"}, {"3419,2917,0", "nardah"}, {"3482,3090,0", "uzer"}, {"3358,2970,0", "pollnivneach"}, {"3305,2788,0", "sophanem"}, {"2898,3544,0", "burthorpe"}, {"3088,3491,0", "edgeville"}, {"3169,3034,0", "bedabin"}, {"3565,3289,0", "barrows"}, {"3016,3513,0", "black knight's fortress"}, {"3052,3481,0", "monastery"}};
        JTextField playerNameField = new JTextField();
        JTextField coordsField = new JTextField();
        JCheckBox toMeToggle = new JCheckBox("To Me");
        JTable table = new JTable(rowData, new Object[]{"Coords", "Name"}) {
            @Override
            public boolean editCellAt(int i, int i1, EventObject eventObject) {
                return false;
            }

            @Override
            public Object getValueAt(int i, int i1) {
                return rowData[i][i1];
            }

            @Override
            public int getRowCount() {
                return rowData.length;
            }
        };
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();

        ActionListener buttonListener = actionEvent -> {
            String command = actionEvent.getActionCommand();
            switch (command) {
                case "telePlayer":
                    if (toMeToggle.isSelected()) {
                        sendCommand("::teletome " + playerNameField.getText());
                    } else {
                        sendCommand("::teleto " + playerNameField.getText());
                    }
                    break;
                case "teleCoords":
                    String[] coordTokens = coordsField.getText().split(",");
                    if (coordTokens.length < 2) {
                        coordsField.setText("");
                        break;
                    }
                    int x, y, z = 0;
                    try {
                        x = Integer.parseInt(coordTokens[0]);
                        y = Integer.parseInt(coordTokens[1]);
                        if (coordTokens.length == 3)
                            z = Integer.parseInt(coordTokens[2]);
                    } catch (NumberFormatException e) {
                        SystemLogger.logErr("(ModPanel.java:118) Invalid value passed to coords tele.");
                        coordsField.setText("");
                        break;
                    }
                    sendCoordTele(x, y, z);
                    break;
            }
        };

        TeleMenu() {
            super("Teleport Menu");
            setLayout(new BorderLayout());

            JPanel playerTelePanel = new JPanel();
            playerTelePanel.setLayout(new FlowLayout());
            JLabel playerNameLabel = new JLabel("Player Name:");
            JButton teleButton = new JButton("Tele");
            teleButton.setActionCommand("telePlayer");
            teleButton.addActionListener(buttonListener);
            playerNameField.setPreferredSize(new Dimension(100, 20));
            playerTelePanel.add(playerNameLabel);
            playerTelePanel.add(playerNameField);
            playerTelePanel.add(toMeToggle);
            playerTelePanel.add(teleButton);

            JPanel coordTelePanel = new JPanel();
            JLabel coordsLabel = new JLabel("Coordinates:");
            teleButton = new JButton("Tele");
            teleButton.setActionCommand("teleCoords");
            teleButton.addActionListener(buttonListener);
            coordsField.setPreferredSize(new Dimension(60, 20));
            coordTelePanel.add(coordsLabel);
            coordTelePanel.add(coordsField);
            coordTelePanel.add(teleButton);

            table.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        JTable table = (JTable)mouseEvent.getSource();
                        int row = table.getSelectedRow();
                        String coordTokens = table.getValueAt(row, 0).toString();
                        sendCommand("::tele " + coordTokens.replace(",", " "));
                        SystemLogger.logInfo("Sending teleport command to " + coordTokens);
                    }
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                }
            });
            cellRenderer.setToolTipText("Double-click to teleport.");
            table.getColumnModel().getColumn(0).setMaxWidth(250);
            table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
            table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);

            JPanel fullTele = new JPanel();
            fullTele.setLayout(new BoxLayout(fullTele, BoxLayout.PAGE_AXIS));
            JScrollPane scrollPane = new JScrollPane(table);
            fullTele.add(scrollPane);

            add(playerTelePanel, BorderLayout.NORTH);
            add(coordTelePanel, BorderLayout.CENTER);
            add(fullTele, BorderLayout.SOUTH);
            pack();
        }

        private void open() {
            setVisible(true);
        }

        private void sendCoordTele(int x, int y, int z) {
            sendCommand("::tele " + x + " " + y + " " + z);
        }

        private void sendCoordTele(int x, int y) {
            sendCoordTele(x, y, 0);
        }
    }

    class PlayerMenu extends JFrame {
        JTextField nameField = new JTextField();
        JTextField durationField = new JTextField();
        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                String title = ((JCheckBox)itemEvent.getItem()).getText();
                switch (title) {
                    case "God Mode":
                        sendCommand("::god");
                        break;
                    case "Instakill":
                        sendCommand("::1hit");
                        break;
                }
            }
        };
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String actionName = actionEvent.getActionCommand();
                switch (actionName) {
                    case "jail":
                        sendCommand("::jail " + durationField.getText() + " " + nameField.getText());
                        break;
                    case "empty":
                        sendCommand("::empty");
                        break;
                }
            }
        };

        PlayerMenu() {
            super("Player Options");
            setLayout(new BorderLayout());

            JPanel jailPanel = new JPanel();
            jailPanel.setLayout(new FlowLayout());
            JLabel nameLabel = new JLabel("Name:");
            JLabel durationLabel = new JLabel("Duration(seconds):");
            JButton jailButton = new JButton("Jail");

            jailPanel.add(nameLabel);
            jailPanel.add(nameField);
            nameField.setPreferredSize(new Dimension(100, 20));
            jailPanel.add(durationLabel);
            jailPanel.add(durationField);
            durationField.setPreferredSize(new Dimension(55, 20));
            jailPanel.add(jailButton);
            jailButton.setActionCommand("jail");
            jailButton.addActionListener(listener);

            add(jailPanel, BorderLayout.NORTH);

            JPanel selfPanel = new JPanel();
            selfPanel.setLayout(new FlowLayout());
            JLabel selfLabel = new JLabel("Self: ");
            JCheckBox godCheck = new JCheckBox("God Mode");
            JCheckBox instakillCheck = new JCheckBox("Instakill");
            godCheck.addItemListener(itemListener);
            instakillCheck.addItemListener(itemListener);
            JButton emptyButton = new JButton("Empty Inv");
            emptyButton.setActionCommand("empty");
            emptyButton.addActionListener(listener);
            selfPanel.add(selfLabel);
            selfPanel.add(godCheck);
            selfPanel.add(instakillCheck);
            selfPanel.add(emptyButton);

            add(selfPanel, BorderLayout.SOUTH);

            pack();

            setVisible(false);
        }

        public void open() {
            setVisible(true);
        }
    }

    class NPCMenu extends JFrame {
        DefaultTableModel model = new DefaultTableModel();
        JTextField searchField = new JTextField();
        JTextField amountField = new JTextField();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        boolean populated = false;

        NPCMenu() {
            super("NPC Spawning Menu");
            setLayout(new BorderLayout());

            cellRenderer.setToolTipText("Double-Click to spawn.");
            JPanel searchPanel = new JPanel();
            JLabel searchLabel = new JLabel("Search for NPC:");
            searchField.setPreferredSize(new Dimension(100, 20));
            searchPanel.add(searchLabel);
            searchPanel.add(searchField);
            add(searchPanel, BorderLayout.NORTH);
            setLocationRelativeTo(null);

            JTable npcTable = new JTable(model) {
                @Override
                public boolean editCellAt(int i, int i1, EventObject eventObject) {
                    return false;
                }
            };
            model.addColumn("ID");
            model.addColumn("Name");
            npcTable.setRowSorter(sorter);
            npcTable.getColumnModel().getColumn(0).setMaxWidth(55);
            npcTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
            npcTable.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);


            npcTable.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        JTable table = (JTable)mouseEvent.getSource();
                        int row = table.getSelectedRow();
                        sendCommand("::npc " + table.getValueAt(row, 0) + (amountField.getText().isEmpty() ? "" : (" " + amountField.getText())));
                        SystemLogger.logInfo("Sending spawn command for NPC: " + table.getValueAt(row, 1) + " " + amountField.getText());
                    }
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                }
            });

            searchField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                    filter();
                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {
                }
            });


            JScrollPane scrollPane = new JScrollPane(npcTable);
            add(scrollPane, BorderLayout.SOUTH);

            pack();
            setVisible(false);
        }

        private void filter() {
            RowFilter<DefaultTableModel, Object> rf = null;
            //If current expression doesn't parse, don't update.
            try {
                rf = RowFilter.regexFilter(searchField.getText(), 1);
            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }
            sorter.setRowFilter(rf);
        }

        public void open() {
            if (!populated) {
                SwingUtilities.invokeLater(() -> {
                    for (int i = 0; i < 15000; i++) {
                        NPCDefinition def = NPCDefinition.getNPCDefinition(i);
                        if (def == null)
                            continue;
                        model.addRow(new Object[]{i, def.NPCName.toLowercase()});
                    }
                    setVisible(true);
                    populated = true;
                });
            } else {
                setVisible(true);
            }
        }
    }

    class ItemMenu extends JFrame {
        DefaultTableModel model = new DefaultTableModel();
        JTextField searchField = new JTextField();
        JTextField amountField = new JTextField();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        boolean populated = false;

        ItemMenu() {
            super("Item Spawning Menu");
            setLayout(new BorderLayout());
            cellRenderer.setToolTipText("Double-Click to spawn.");

            JPanel searchPanel = new JPanel();
            JLabel searchLabel = new JLabel("Search for Item:");
            JLabel amountLabel = new JLabel("Amount: ");
            amountField.setPreferredSize(new Dimension(45, 20));
            searchField.setPreferredSize(new Dimension(100, 20));
            searchPanel.add(searchLabel);
            searchPanel.add(searchField);
            searchPanel.add(amountLabel);
            searchPanel.add(amountField);
            add(searchPanel, BorderLayout.NORTH);
            setLocationRelativeTo(null);

            JTable itemTable = new JTable(model) {
                @Override
                public boolean editCellAt(int i, int i1, EventObject eventObject) {
                    return false;
                }
            };
            model.addColumn("ID");
            model.addColumn("Name");
            itemTable.setRowSorter(sorter);
            itemTable.getColumnModel().getColumn(0).setMaxWidth(55);
            itemTable.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
            itemTable.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);

            itemTable.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        JTable table = (JTable)mouseEvent.getSource();
                        int row = table.getSelectedRow();
                        sendCommand("::item " + table.getValueAt(row, 0) + (amountField.getText().isEmpty() ? "" : (" " + amountField.getText())));
                        SystemLogger.logInfo("Sending spawn command for item: " + table.getValueAt(row, 1) + " " + amountField.getText());
                    }
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {
                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {
                }
            });

            searchField.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                    filter();
                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {
                }
            });


            JScrollPane scrollPane = new JScrollPane(itemTable);
            add(scrollPane, BorderLayout.SOUTH);

            pack();
            setVisible(false);
        }

        private void filter() {
            RowFilter<DefaultTableModel, Object> rf = null;
            //If current expression doesn't parse, don't update.
            try {
                rf = RowFilter.regexFilter(searchField.getText(), 1);
            } catch (java.util.regex.PatternSyntaxException e) {
                return;
            }
            sorter.setRowFilter(rf);
        }

        public void open() {
            if (!populated) {
                SwingUtilities.invokeLater(() -> {
                    for (int i = 0; i < 15000; i++) {
                        ItemDefinition def = ItemDefinition.getItemDefinition(i);
                        if (def == null)
                            continue;
                        model.addRow(new Object[]{i, def.name.toLowercase()});
                    }
                    setVisible(true);
                    populated = true;
                });
            } else {
                setVisible(true);
            }
        }
    }

    class DebugMenu extends JFrame {
        DebugMenu() {
            super("Debug Options");
            ItemListener listener = new CheckBoxListener();
            setResizable(false);
            setLayout(new FlowLayout());
            setLocationRelativeTo(null);
            setDefaultCloseOperation(HIDE_ON_CLOSE);
            JCheckBox itemDebug = new JCheckBox("Item IDs");
            JCheckBox npcDebug = new JCheckBox("NPC IDs");
            JCheckBox objectDebug = new JCheckBox("Object IDs");
            JCheckBox debugMode = new JCheckBox("Debug Output");
            itemDebug.addItemListener(listener);
            npcDebug.addItemListener(listener);
            objectDebug.addItemListener(listener);
            debugMode.addItemListener(listener);
            add(itemDebug);
            add(npcDebug);
            add(objectDebug);
            add(debugMode);
            pack();
            setVisible(false);
        }

        class CheckBoxListener implements ItemListener {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                String title = ((JCheckBox)itemEvent.getItem()).getText();
                switch (title) {
                    case "Item IDs":
                        GameConfig.ITEM_DEBUG_ENABLED = itemEvent.getStateChange() == ItemEvent.SELECTED;
                        break;
                    case "NPC IDs":
                        GameConfig.NPC_DEBUG_ENABLED = itemEvent.getStateChange() == ItemEvent.SELECTED;
                        break;
                    case "Object IDs":
                        GameConfig.OBJECT_DEBUG_ENABLED = itemEvent.getStateChange() == ItemEvent.SELECTED;
                        break;
                    case "Debug Output":
                        sendCommand("::debug");
                        break;
                }
            }
        }
    }

    // TODO: Needs re-write
    class ModelMenu extends JFrame {
        JTextField itemSearchField = new JTextField();
        JLabel offsetMaleXStatusText = new JLabel("X Offset:", SwingConstants.LEFT);
        JLabel offsetMaleYStatusText = new JLabel("Y Offset:", SwingConstants.LEFT);
        JLabel offsetMaleZStatusText = new JLabel("Z Offset:", SwingConstants.LEFT);
        JLabel offsetFemaleXStatusText = new JLabel("X Offset:", SwingConstants.LEFT);
        JLabel offsetFemaleYStatusText = new JLabel("Y Offset:", SwingConstants.LEFT);
        JLabel offsetFemaleZStatusText = new JLabel("Z Offset:", SwingConstants.LEFT);
        int offsetXMale = 0;
        int offsetYMale = 0;
        int offsetZMale = 0;
        int offsetXFemale = 0;
        int offsetYFemale = 0;
        int offsetZFemale = 0;
        int defaultOffsetXMale = 0;
        int defaultOffsetYMale = 0;
        int defaultOffsetZMale = 0;
        int defaultOffsetXFemale = 0;
        int defaultOffsetYFemale = 0;
        int defaultOffsetZFemale = 0;
        JLabel defaultMaleXStatusText = new JLabel("Default X Offset: ", JLabel.LEFT);
        JLabel defaultMaleYStatusText = new JLabel("Default Y Offset: ", JLabel.LEFT);
        JLabel defaultMaleZStatusText = new JLabel("Default Z Offset: ", JLabel.LEFT);
        JLabel defaultFemaleXStatusText = new JLabel("Default X Offset: ", JLabel.LEFT);
        JLabel defaultFemaleYStatusText = new JLabel("Default Y Offset: ", JLabel.LEFT);
        JLabel defaultFemaleZStatusText = new JLabel("Default Z Offset: ", JLabel.LEFT);
        JTabbedPane itemOffsetTabPane = new JTabbedPane();
        JSlider sliderMaleXOffset = new JSlider(Byte.MIN_VALUE, Byte.MAX_VALUE);
        JSlider sliderMaleYOffset = new JSlider(Byte.MIN_VALUE, Byte.MAX_VALUE);
        JSlider sliderMaleZOffset = new JSlider(Byte.MIN_VALUE, Byte.MAX_VALUE);
        JSlider sliderFemaleXOffset = new JSlider(Byte.MIN_VALUE, Byte.MAX_VALUE);
        JSlider sliderFemaleYOffset = new JSlider(Byte.MIN_VALUE, Byte.MAX_VALUE);
        JSlider sliderFemaleZOffset = new JSlider(Byte.MIN_VALUE, Byte.MAX_VALUE);
        ChangeListener listenerMaleXOffset = new SliderMaleOffsetXListener();
        ChangeListener listenerMaleYOffset = new SliderMaleOffsetYListener();
        ChangeListener listenerMaleZOffset = new SliderMaleOffsetZListener();
        ChangeListener listenerFemaleXOffset = new SliderFemaleOffsetXListener();
        ChangeListener listenerFemaleYOffset = new SliderFemaleOffsetYListener();
        ChangeListener listenerFemaleZOffset = new SliderFemaleOffsetZListener();

        ModelMenu() {
            super("Model Helper");
            setResizable(false);
            setAlwaysOnTop(true);
            setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
            setLocationRelativeTo(null);
            setDefaultCloseOperation(HIDE_ON_CLOSE);

            JPanel searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
            searchPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            JLabel searchLabel = new JLabel("Item:");
            itemSearchField.setPreferredSize(new Dimension(65, 20));

            JButton resetValues = new JButton("Reset");
            resetValues.addActionListener(e -> resetOffsetValues());

            searchPanel.add(searchLabel);
            searchPanel.add(itemSearchField);
            searchPanel.add(resetValues);

            PlainDocument doc = (PlainDocument) itemSearchField.getDocument();
            doc.setDocumentFilter(new MyIntFilter());

            itemOffsetTabPane.add("Male offset", prepareMaleSliders());
            itemOffsetTabPane.add("Female offset", prepareFemaleSliders());

            add(searchPanel);
            add(itemOffsetTabPane);

            pack();
            setVisible(false);
        }

        private void updateOffsetValues() {
            if (itemSearchField.getText() != null) {
                if (!itemSearchField.getText().contentEquals("")) {
                    ItemDefinition def = ItemDefinition.getItemDefinition(Integer.parseInt(itemSearchField.getText()));
                    if (def != null) {
                        // Clears model reference cache
                        KeyboardListener.aReferenceCache_1911.clear();

                        def.maleWieldX = offsetXMale;
                        def.maleWieldY = offsetYMale;
                        def.maleWieldZ = offsetZMale;
                        def.femaleWieldX = offsetXFemale;
                        def.femaleWieldY = offsetYFemale;
                        def.femaleWieldZ = offsetZFemale;
                    }
                }
            }
        }

        private void resetOffsetValues() {
            // Clear item def cache
            QuickChatDefinition.aReferenceCache_3572.clear();
            updateOffsetValues();

            offsetXMale = defaultOffsetXMale;
            offsetYMale = defaultOffsetYMale;
            offsetZMale = defaultOffsetZMale;
            offsetXFemale = defaultOffsetXFemale;
            offsetYFemale = defaultOffsetYFemale;
            offsetZFemale = defaultOffsetZFemale;

            sliderMaleXOffset.setValue(offsetXMale);
            sliderMaleYOffset.setValue(offsetYMale);
            sliderMaleZOffset.setValue(offsetZMale);
            sliderFemaleXOffset.setValue(offsetXFemale);
            sliderFemaleYOffset.setValue(offsetYFemale);
            sliderFemaleZOffset.setValue(offsetZFemale);
        }

        void updateDefaultOffsetValues(int id) {
            ItemDefinition def = ItemDefinition.getItemDefinition(id);
            if (def == null) {
                defaultMaleXStatusText.setText("Default X Offset: NULL");
                defaultMaleYStatusText.setText("Default Y Offset: NULL");
                defaultMaleZStatusText.setText("Default Z Offset: NULL");
                defaultFemaleXStatusText.setText("Default X Offset: NULL");
                defaultFemaleYStatusText.setText("Default Y Offset: NULL");
                defaultFemaleZStatusText.setText("Default Z Offset: NULL");
            } else {
                defaultOffsetXMale = def.maleWieldX;
                defaultOffsetYMale = def.maleWieldY;
                defaultOffsetZMale = def.maleWieldZ;
                defaultOffsetXFemale = def.femaleWieldX;
                defaultOffsetYFemale = def.femaleWieldY;
                defaultOffsetZFemale = def.femaleWieldZ;
                offsetXMale = def.maleWieldX;
                offsetYMale = def.maleWieldY;
                offsetZMale = def.maleWieldZ;
                offsetXFemale = def.femaleWieldX;
                offsetYFemale = def.femaleWieldY;
                offsetZFemale = def.femaleWieldZ;

                sliderMaleXOffset.setValue(defaultOffsetXMale);
                sliderMaleYOffset.setValue(defaultOffsetYMale);
                sliderMaleZOffset.setValue(defaultOffsetZMale);
                sliderFemaleXOffset.setValue(defaultOffsetXFemale);
                sliderFemaleYOffset.setValue(defaultOffsetYFemale);
                sliderFemaleZOffset.setValue(defaultOffsetZFemale);

                defaultMaleXStatusText.setText("Default X Offset: " + def.maleWieldX);
                defaultMaleYStatusText.setText("Default Y Offset: " + def.maleWieldY);
                defaultMaleZStatusText.setText("Default Z Offset: " + def.maleWieldZ);
                defaultFemaleXStatusText.setText("Default X Offset: " + def.femaleWieldX);
                defaultFemaleYStatusText.setText("Default Y Offset: " + def.femaleWieldY);
                defaultFemaleZStatusText.setText("Default Z Offset: " + def.femaleWieldZ);
            }
        }

        private void open() {
            setVisible(true);
        }

        private Component leftJustify(JPanel panel)  {
            Box  b = Box.createHorizontalBox();
            b.add(panel);
            b.add(Box.createHorizontalGlue());
            // (Note that you could throw a lot more components
            // and struts and glue in here.)
            return b;
        }

        public JPanel prepareMaleSliders() {
            // Add positions label in the slider
            Hashtable<Integer, JLabel> position = new Hashtable<>();
            position.put((int)Byte.MIN_VALUE, new JLabel("" + Byte.MIN_VALUE));
            position.put(0, new JLabel("0"));
            position.put((int)Byte.MAX_VALUE, new JLabel("" + Byte.MAX_VALUE));

            // Setup the sliders
            sliderMaleXOffset.setName("X offset");
            sliderMaleXOffset.setMinorTickSpacing(10);
            sliderMaleXOffset.setPaintTicks(true);
            sliderMaleXOffset.setPaintLabels(true);
            sliderMaleXOffset.setLabelTable(position);
            sliderMaleXOffset.setSnapToTicks(true);
            sliderMaleXOffset.addChangeListener(listenerMaleXOffset);

            sliderMaleYOffset.setName("Y offset");
            sliderMaleYOffset.setMinorTickSpacing(10);
            sliderMaleYOffset.setPaintTicks(true);
            sliderMaleYOffset.setPaintLabels(true);
            sliderMaleYOffset.setLabelTable(position);
            sliderMaleYOffset.setSnapToTicks(true);
            sliderMaleYOffset.addChangeListener(listenerMaleYOffset);

            sliderMaleZOffset.setName("Z offset");
            sliderMaleZOffset.setMinorTickSpacing(10);
            sliderMaleZOffset.setPaintTicks(true);
            sliderMaleZOffset.setPaintLabels(true);
            sliderMaleZOffset.setLabelTable(position);
            sliderMaleZOffset.setSnapToTicks(true);
            sliderMaleZOffset.addChangeListener(listenerMaleZOffset);

            JPanel sliderPanel = new JPanel();
            sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));

            JPanel offXPanel = new JPanel();
            offXPanel.setLayout(new BoxLayout(offXPanel, BoxLayout.Y_AXIS));
            offXPanel.add(offsetMaleXStatusText);
            offXPanel.add(sliderMaleXOffset);

            JPanel offYPanel = new JPanel();
            offYPanel.setLayout(new BoxLayout(offYPanel, BoxLayout.Y_AXIS));
            offYPanel.add(offsetMaleYStatusText);
            offYPanel.add(sliderMaleYOffset);

            JPanel offZPanel = new JPanel();
            offZPanel.setLayout(new BoxLayout(offZPanel, BoxLayout.Y_AXIS));
            offZPanel.add(offsetMaleZStatusText);
            offZPanel.add(sliderMaleZOffset);

            sliderPanel.add(offXPanel);
            sliderPanel.add(offYPanel);
            sliderPanel.add(offZPanel);

            JPanel infoPane = new JPanel();
            infoPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.Y_AXIS));
            JLabel defaultsText = new JLabel(" ");

            infoPane.add(defaultsText);
            infoPane.add(defaultMaleXStatusText);
            infoPane.add(defaultMaleYStatusText);
            infoPane.add(defaultMaleZStatusText);

            sliderPanel.add(leftJustify(infoPane));
            return sliderPanel;
        }

        public JPanel prepareFemaleSliders() {
            // Add positions label in the slider
            Hashtable<Integer, JLabel> position = new Hashtable<>();
            position.put((int)Byte.MIN_VALUE, new JLabel("" + Byte.MIN_VALUE));
            position.put(0, new JLabel("0"));
            position.put((int)Byte.MAX_VALUE, new JLabel("" + Byte.MAX_VALUE));

            // Setup the sliders
            sliderFemaleXOffset.setPaintTicks(true);
            sliderFemaleXOffset.setPaintLabels(true);
            sliderFemaleXOffset.setLabelTable(position);
            sliderFemaleXOffset.setSnapToTicks(true);
            sliderFemaleXOffset.addChangeListener(listenerFemaleXOffset);

            sliderFemaleYOffset.setPaintTicks(true);
            sliderFemaleYOffset.setPaintLabels(true);
            sliderFemaleYOffset.setLabelTable(position);
            sliderFemaleYOffset.setSnapToTicks(true);
            sliderFemaleYOffset.addChangeListener(listenerFemaleYOffset);

            sliderFemaleZOffset.setPaintTicks(true);
            sliderFemaleZOffset.setPaintLabels(true);
            sliderFemaleZOffset.setLabelTable(position);
            sliderFemaleZOffset.setSnapToTicks(true);
            sliderFemaleZOffset.addChangeListener(listenerFemaleZOffset);

            JPanel sliderPanel = new JPanel();
            sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));

            JPanel offXPanel = new JPanel();
            offXPanel.setLayout(new BoxLayout(offXPanel, BoxLayout.Y_AXIS));
            offXPanel.add(offsetFemaleXStatusText);
            offXPanel.add(sliderFemaleXOffset);

            JPanel offYPanel = new JPanel();
            offYPanel.setLayout(new BoxLayout(offYPanel, BoxLayout.Y_AXIS));
            offYPanel.add(offsetFemaleYStatusText);
            offYPanel.add(sliderFemaleYOffset);

            JPanel offZPanel = new JPanel();
            offZPanel.setLayout(new BoxLayout(offZPanel, BoxLayout.Y_AXIS));
            offZPanel.add(offsetFemaleZStatusText);
            offZPanel.add(sliderFemaleZOffset);

            sliderPanel.add(offXPanel);
            sliderPanel.add(offYPanel);
            sliderPanel.add(offZPanel);

            JPanel infoPane = new JPanel();
            infoPane.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.Y_AXIS));
            JLabel defaultsText = new JLabel(" ");

            infoPane.add(defaultsText);
            infoPane.add(defaultFemaleXStatusText);
            infoPane.add(defaultFemaleYStatusText);
            infoPane.add(defaultFemaleZStatusText);

            sliderPanel.add(leftJustify(infoPane));
            return sliderPanel;
        }

        class SliderMaleOffsetXListener implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
                int offset = ((JSlider)e.getSource()).getValue();
                offsetXMale = offset;
                offsetMaleXStatusText.setText("X Offset: " + offset);
                updateOffsetValues();
            }
        }

        class SliderMaleOffsetYListener implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
                int offset = ((JSlider)e.getSource()).getValue();
                offsetYMale = offset;
                offsetMaleYStatusText.setText("Y Offset: " + offset);
                updateOffsetValues();
            }
        }

        class SliderMaleOffsetZListener implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
                int offset = ((JSlider)e.getSource()).getValue();
                offsetZMale = offset;
                offsetMaleZStatusText.setText("Z Offset: " + offset);
                updateOffsetValues();
            }
        }

        class SliderFemaleOffsetXListener implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
                int offset = ((JSlider)e.getSource()).getValue();
                offsetXFemale = offset;
                offsetFemaleXStatusText.setText("X Offset: " + offset);
                updateOffsetValues();
            }
        }

        class SliderFemaleOffsetYListener implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
                int offset = ((JSlider)e.getSource()).getValue();
                offsetYFemale = offset;
                offsetFemaleYStatusText.setText("Y Offset: " + offset);
                updateOffsetValues();
            }
        }

        class SliderFemaleOffsetZListener implements ChangeListener {
            @Override
            public void stateChanged(ChangeEvent e) {
                int offset = ((JSlider)e.getSource()).getValue();
                offsetZFemale = offset;
                offsetFemaleZStatusText.setText("Z Offset: " + offset);
                updateOffsetValues();
            }
        }

        class MyIntFilter extends DocumentFilter {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                Document doc = fb.getDocument();
                StringBuilder sb = new StringBuilder();
                sb.append(doc.getText(0, doc.getLength()));
                sb.insert(offset, string);

                if (test(sb.toString())) {
                    super.insertString(fb, offset, string, attr);
                    updateDefaultOffsetValues(Integer.parseInt(sb.toString()));
                } else {
                    // warn the user and don't allow the insert
                }
            }

            private boolean test(String text) {
                try {
                    Integer.parseInt(text);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                Document doc = fb.getDocument();
                StringBuilder sb = new StringBuilder();
                sb.append(doc.getText(0, doc.getLength()));
                sb.replace(offset, offset + length, text);

                if (test(sb.toString())) {
                    super.replace(fb, offset, length, text, attrs);
                    updateDefaultOffsetValues(Integer.parseInt(sb.toString()));
                } else {
                    // warn the user and don't allow the insert
                }

            }

            @Override
            public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                Document doc = fb.getDocument();
                StringBuilder sb = new StringBuilder();
                sb.append(doc.getText(0, doc.getLength()));
                sb.delete(offset, offset + length);

                if (test(sb.toString())) {
                    super.remove(fb, offset, length);
                    updateDefaultOffsetValues(Integer.parseInt(sb.toString()));
                } else {
                    // warn the user and don't allow the insert
                }

            }
        }
    }
}
