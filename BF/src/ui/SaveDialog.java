package ui;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rmi.RemoteHelper;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SaveDialog extends Stage {
    private final ChoiceBox choiceBox;
    String fileName = "";
    String fileType = "";

    public SaveDialog(String code, String username) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(12);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setId("gridPane");

        Scene scene = new Scene(gridPane, 300, 200);

        Label saveLabel = new Label("Save");
        saveLabel.setId("save-label");

        TextField textField = new TextField();
        textField.setMinWidth(200);
        textField.setPromptText("File Name");
        choiceBox = new ChoiceBox();
        choiceBox.getItems().addAll("bf","ook");
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.setMinWidth(200);

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        saveButton.setMinWidth(200);
        cancelButton.setMinWidth(200);
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().add(column0);
        gridPane.add(saveLabel, 0, 0);
        gridPane.add(textField, 0, 1);
        gridPane.add(choiceBox, 0, 2);
        gridPane.add(saveButton, 0, 3);
        gridPane.add(cancelButton, 0, 4);
        Platform.runLater(() -> textField.requestFocus());
        scene.getStylesheets().add("mainUI.css");
        this.setTitle("Save File");
        this.setResizable(false);
        this.setScene(scene);
        saveButton.setOnMouseClicked(event -> {
            fileName = textField.getText();
            fileType = choiceBox.getSelectionModel().getSelectedItem().toString();

            try {
                RemoteHelper.getInstance().getIOService().writeFile(code,username,fileName,fileType);
                this.close();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        cancelButton.setOnAction(event -> {
            this.close();
        });

    }

}
