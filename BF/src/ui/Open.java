
package ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import rmi.RemoteHelper;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Open extends Stage {
    private final ChoiceBox versionChoiceBox;
    private final ComboBox fileComboBox;
    String fileName = "";
    String fileType = "";
    String version = "";
    public Open(String username) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(12);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setId("gridPane");

        Scene scene = new Scene(gridPane, 300, 200);

        Label openLabel = new Label("Open");
        openLabel.setId("save-label");
        String[] nameArr = null;//文件名+版本号
        try {
            String nameStr = RemoteHelper.getInstance().getIOService().readFileList(username);
            nameArr = nameStr.split(",");
        }catch (RemoteException e){
            e.printStackTrace();
        }
        //文件名列表（不包含版本号）
        ArrayList<String> filenameList = new ArrayList<>();
        ArrayList<String> versionList = new ArrayList<>();
        for (int i = 0;i < nameArr.length; i++){
            filenameList.add(nameArr[i].split("@")[0]);
        }

        fileComboBox = new ComboBox();
        fileComboBox.getItems().addAll(filenameList.stream().distinct().toArray());
        fileComboBox.setMinWidth(200);
        fileComboBox.getSelectionModel().selectFirst();
        for (String str: nameArr){
            if (str.split("@")[0].equals(fileComboBox.getSelectionModel().getSelectedItem().toString())) {
                versionList.add(str.split("@")[1].substring(0,19));
            }
        }
        versionChoiceBox = new ChoiceBox();
        versionChoiceBox.getItems().addAll(versionList);
        versionChoiceBox.getSelectionModel().selectFirst();
        versionChoiceBox.setMinWidth(200);
        //动态加载版本号
        String[] finalNameArr = nameArr;
        fileComboBox.setOnAction(event ->  {
            versionList.clear();
            for (String str : finalNameArr) {
                if (str.split("@")[0].equals(fileComboBox.getSelectionModel().getSelectedItem().toString())) {
                    versionList.add(str.split("@")[1].substring(0, 19));
                }
            }
            versionChoiceBox.getItems().clear();
            versionChoiceBox.getItems().addAll(versionList);
            versionChoiceBox.getSelectionModel().selectFirst();
        });


        Button openButton = new Button("Open");
        Button cancelButton = new Button("Cancel");
        openButton.setMinWidth(200);
        cancelButton.setMinWidth(200);
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().add(column0);
        gridPane.add(openLabel, 0, 0);
        gridPane.add(fileComboBox, 0, 1);
        gridPane.add(versionChoiceBox, 0, 2);
        gridPane.add(openButton, 0, 3);
        gridPane.add(cancelButton, 0, 4);
        Platform.runLater(fileComboBox::requestFocus);
        scene.getStylesheets().add("mainUI.css");
        this.setTitle("Open File");
        this.setResizable(false);
        this.setScene(scene);
        openButton.setOnAction(event -> {
            fileName = fileComboBox.getSelectionModel().getSelectedItem().toString();
            version = versionChoiceBox.getSelectionModel().getSelectedItem().toString();
            fileType = "bf";
            this.close();
        });
        cancelButton.setOnAction(event -> {
            this.close();
        });

    }

}
