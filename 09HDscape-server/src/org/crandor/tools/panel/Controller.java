package org.crandor.tools.panel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.crandor.tools.TimeStamp;
import org.crandor.ServerConstants;
import org.crandor.game.node.entity.player.Player;
import org.crandor.game.system.SystemLogger;
import org.crandor.game.system.SystemShutdownHook;
import org.crandor.game.system.mysql.SQLManager;
import org.crandor.game.world.GameWorld;
import org.crandor.net.NioReactor;
import org.crandor.net.amsc.WorldCommunicator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static javafx.scene.layout.VBoxBuilder.create;

public class Controller extends AnchorPane implements Initializable {

    private ObservableList<String> rankData = FXCollections.observableArrayList();
    private ObservableList<String> punishmentData = FXCollections.observableArrayList();
    private ObservableList<String> toolsData = FXCollections.observableArrayList();
    private ObservableList<String> playerListData = FXCollections.observableArrayList();

    @FXML
    private PasswordField validationKey;

    @FXML
    private AnchorPane vali;

    @FXML
    private AnchorPane mainFrame;

    @FXML
    private VBox legendFrame;

    @FXML
    private Button submitButton;

    @FXML
    private TextArea playersList;

    @FXML
    private TextArea systemLogger;

    @FXML
    private ComboBox<String> punishmentBox;

    @FXML
    private ComboBox<String> toolsBox;

    @FXML
    private ComboBox<String> ranksBox;

    public static long startTime;

    public static NioReactor reactor;

    private Player player;


    public Controller() {
        for (String options : BoxOptions.RANKS.getOptions()) {
            rankData.add(options);
        }
        for (String options : BoxOptions.PUNISHMENTS.getOptions()) {
            punishmentData.add(options);
        }
        for (String options : BoxOptions.TOOLS.getOptions()) {
            toolsData.add(options);
        }
    }

    public Controller(Player player) {
        this.player = player;
    }

    private EventHandler<ActionEvent> loginEventHandler = event -> {
        try {
            Scanner scanner = new Scanner(new File("key.cfg"));
            String key = scanner.next();
            String sha1Encryption = Encrypt.encryptSHA1(validationKey.getText());
            if (sha1Encryption.length() == key.length()) {
                if (sha1Encryption.equalsIgnoreCase(key)) {
                    Stage accessMessage = new Stage();
                    Button okayButton = new Button("Ok");
                    okayButton.setOnAction((closeErrorEvent) -> {
                        legendFrame.getChildren().remove(vali);
                        legendFrame.getChildren().add(mainFrame);
                        accessMessage.close();
                        try {
                            startTime = System.currentTimeMillis();
                            final TimeStamp t = new TimeStamp();
                            GameWorld.prompt(true);
                            SQLManager.init();
                            Runtime.getRuntime().addShutdownHook(new Thread(new SystemShutdownHook()));
                            SystemLogger.log("Starting NIO reactor...");
                            reactor = NioReactor.configure(43593 + GameWorld.getSettings().getWorldId());
                            WorldCommunicator.connect();
                            reactor.start();
                            SystemLogger.log(GameWorld.getName() + " flags " + GameWorld.getSettings().toString());
                            SystemLogger.log(GameWorld.getName() + " started in " + t.duration(false, "") + " milliseconds.");
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
                    accessMessage.initModality(Modality.NONE);
                    accessMessage.setScene(new Scene(create().children(new Text("[Success] - Access granted.\n"), okayButton).alignment(Pos.CENTER).padding(new Insets(5)).build()));
                    accessMessage.show();
                } else {
                    Stage errorMessage = new Stage();
                    Button okayButton = new Button("Ok");
                    okayButton.setOnAction((closeErrorEvent) -> errorMessage.close());
                    errorMessage.initModality(Modality.WINDOW_MODAL);
                    errorMessage.setScene(new Scene(create().children(new Text("[Error] - Incorrect validation key.\n"), okayButton).alignment(Pos.CENTER).padding(new Insets(5)).build()));
                    errorMessage.show();
                    ServerConstants.VALIDATED = false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    };

    private EventHandler<ActionEvent> comboBoxEventHandler = event -> {

        if (event.getSource() == ranksBox) {

        }

        if (event.getSource() == punishmentBox) {

        }

        if (event.getSource() == toolsBox) {

        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        legendFrame.getChildren().remove(mainFrame);

        submitButton.setOnAction(loginEventHandler);

        validationKey.setOnAction(loginEventHandler);

        ranksBox.setOnAction(comboBoxEventHandler);

        ranksBox.setItems(rankData);

        punishmentBox.setItems(punishmentData);

        toolsBox.setItems(toolsData);


        Console console = new Console(systemLogger);
        PrintStream ps = new PrintStream(console, true);
        System.setOut(ps);
        System.setErr(ps);

    }

    public TextArea getPlayersList() {
        return playersList;
    }

    public ObservableList<String> getPlayerListData() {
        return playerListData;
    }


}
