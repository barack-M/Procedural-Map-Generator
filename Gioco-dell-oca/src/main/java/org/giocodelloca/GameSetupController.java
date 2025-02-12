package org.giocodelloca;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.giocodelloca.effects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSetupController {
    private final List<Player> players = new ArrayList<>();
    @FXML
    private GridPane playerGrid;
    @FXML
    private Slider waitOneSlider;
    @FXML
    private Label waitOneLabel;
    @FXML
    private Slider backToOneSlider;
    @FXML
    private Label backToOneLabel;
    @FXML
    private Slider swapSlider;
    @FXML
    private Label swapLabel;
    @FXML
    private Slider randomSlider;
    @FXML
    private Label randomLabel;

    private int playerIndex = 0;

    public void initialize() {
        for (int i = 0; i < 2; i++) {
            createP();
            playerIndex++;
        }
        playerIndex--;

        waitOneSlider.valueProperty().addListener((obs, oldVal, newVal) -> waitOneLabel.setText(String.valueOf(newVal.intValue())));
        backToOneSlider.valueProperty().addListener((obs, oldVal, newVal) -> backToOneLabel.setText(String.valueOf(newVal.intValue())));
        swapSlider.valueProperty().addListener((obs, oldVal, newVal) -> swapLabel.setText(String.valueOf(newVal.intValue())));
        randomSlider.valueProperty().addListener((obs, oldVal, newVal) -> randomLabel.setText(String.valueOf(newVal.intValue())));

    }

    public void createP() {
        TextField nameField = new TextField();
        nameField.setPromptText("Nome Giocatore " + (playerIndex + 1));
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(Color.hsb(playerIndex * 100, 0.7, 0.7));
        colorPicker.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 30;");
        Label gridLabel = new Label();
        gridLabel.setText("Giocatore " + (playerIndex + 1));
        gridLabel.setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-opacity: 1");
        playerGrid.add(gridLabel, 0, playerIndex);
        playerGrid.add(nameField, 1, playerIndex);
        playerGrid.add(colorPicker, 2, playerIndex);
    }

    @FXML
    private void setupPlayers() {
        playerIndex++;
        createP();
    }

    @FXML
    private void startGame() {
        players.clear();
        int playerNumber = playerGrid.getRowCount();

        for (int i = 0; i < playerNumber; i++) {
            TextField nameField = (TextField) getNodeFromGridPane(playerGrid, 1, i);
            ColorPicker colorPicker = (ColorPicker) getNodeFromGridPane(playerGrid, 2, i);
            assert colorPicker != null;
            if (nameField == null || nameField.getText().isEmpty()) {
                players.add(new Player("P" + i, 0, colorPicker.getValue()));
            } else {
                players.add(new Player(nameField.getText(), 0, colorPicker.getValue()));
            }
        }

        Map<CellEffect, Integer> effectSettings = new HashMap<>();
        effectSettings.put(new WaitOneEffect(), (int) waitOneSlider.getValue());
        effectSettings.put(new BackToOneEffect(), (int) backToOneSlider.getValue());
        effectSettings.put(new SwapEffect(), (int) swapSlider.getValue());
        effectSettings.put(new RandomEffect(), (int) randomSlider.getValue());

        Main.setGame(players, effectSettings);
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }
}
