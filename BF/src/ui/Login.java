package ui;

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

public class Login extends Stage {
    public Login(){
        GridPane gridPane = new GridPane();
        gridPane.setVgap(12);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setId("gridPane");

        Scene scene = new Scene(gridPane,300,200);

        Label loginLabel = new Label("Login");
        loginLabel.setId("login-label");

        TextField textField = new TextField();
        textField.setPromptText("User Name");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button registerButton = new Button("Sign up");
        Button loginButton = new Button("Login");
        registerButton.setMinWidth(200);
        loginButton.setMinWidth(200);

        loginButton.setOnMouseClicked(event -> {
            if (textField.getText().equals("") || passwordField.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("The username or password is empty");
                alert.showAndWait();
            } else {
                String username = textField.getText();
                String password = passwordField.getText();
                try {
                    String result = RemoteHelper.getInstance().getUserService().login(username, password);
                    if (result.equals("success")) {
                        this.close();
                        MainFrame mainFrame = new MainFrame(username);
                    } else if (result.equals("password Wrong")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Wrong Password");
                        alert.setContentText(null);
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Login Failed");
                        alert.setContentText("Please sign up first");
                        alert.showAndWait();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        registerButton.setOnMouseClicked(event -> {
            this.close();
            Register register = new Register();
        });

        ColumnConstraints column0 = new ColumnConstraints();
        column0.setHalignment(HPos.CENTER);
        gridPane.getColumnConstraints().add(column0);
        gridPane.add(loginLabel,0,0);
        gridPane.add(textField,0,1);
        gridPane.add(passwordField,0,2);
        gridPane.add(loginButton,0,3);
        gridPane.add(registerButton,0,4);

        scene.getStylesheets().add("mainUI.css");
        this.setTitle("Login");
        this.setResizable(false);
        this.setScene(scene);
        this.show();
    }
}
