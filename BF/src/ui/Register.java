package ui;


import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import rmi.RemoteHelper;

import java.rmi.RemoteException;

public class Register extends Stage {
    public Register(){
        GridPane gridPane = new GridPane();
        gridPane.setVgap(12);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setId("gridPane");
        Scene scene = new Scene(gridPane,300,200);

        Label registerLabel = new Label("Register");
        registerLabel.setId("register-label");

        TextField textField = new TextField();
        textField.setPromptText("User Name");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Confirm Password");

        Button registerButton = new Button("Sign up");
        registerButton.setMinWidth(200);
        registerButton.setOnMouseClicked(event -> {
            if (!passwordField.getText().equals(confirmPassword.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("The two passwords you enter is inconsistent, please enter again");
                alert.showAndWait();
            } else if (textField.getText().equals("") || passwordField.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("The username or password is empty");
                alert.showAndWait();
            } else {
                String username = textField.getText();
                String password = passwordField.getText();
                try {
                    if (RemoteHelper.getInstance().getUserService().register(username, password)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText("   Sign up successfully");
                        alert.getDialogPane().getStylesheets().add("mainUI.css");
                        alert.showAndWait();
                        this.close();
                        MainFrame mainFrame = new MainFrame(username);
                    }else{
                        Alert sameNameAlert = new Alert(Alert.AlertType.ERROR);
                        sameNameAlert.getDialogPane().getStylesheets().add("mainUI.css");
                        sameNameAlert.setTitle("Error");
                        sameNameAlert.setHeaderText("The name has been used!");
                        sameNameAlert.showAndWait();
                    }

                }catch (RemoteException e){
                    e.printStackTrace();
                }

            }
        });
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().add(column0);
        gridPane.add(registerLabel,0,0);
        gridPane.add(textField,0,1);
        gridPane.add(passwordField,0,2);
        gridPane.add(confirmPassword,0,3);
        gridPane.add(registerButton,0,4);

        this.setTitle("Sign Up");
        this.setResizable(false);
        scene.getStylesheets().add("mainUI.css");
        this.setScene(scene);
        this.show();
    }
}
