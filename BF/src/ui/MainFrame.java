package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Optional;

import rmi.RemoteHelper;

public class MainFrame extends Stage{
    String fileType = "";
    String userName = "";
    String fileName = "";
    String version = "";
    String originalCode = "";

    TextArea codeArea;
    TextArea inputArea;
    TextArea resultArea;
    public MainFrame(String username){
        this.userName = username;
        this.setTitle("BF Client");
        VBox root = new VBox();
        Scene scene = new Scene(root,800,600);

        SimpleMenu[] simpleMenus = {
                new FileMenu("File"),
                new RunMenu("Run"),
                new UserMenu("User"),
                new EditMenu("Edit")
        };

        MenuBar menuBar = new MenuBar();

        for (SimpleMenu simpleMenu:simpleMenus) {
            simpleMenu.setMenuItem();
            menuBar.getMenus().add(simpleMenu.getMenu());
            simpleMenu.setAction(this);
        }

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15,0,0,0));
        gridPane.setHgap(25);
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(25,25,25,25));

        codeArea = new TextArea();
        codeArea.setPrefRowCount(30);
        codeArea.setScrollLeft(50);
        codeArea.setWrapText(true);
        codeArea.setId("codeArea");
        inputArea = new TextArea();
        resultArea = new TextArea();

        Label inputLabel = new Label("Input:");
        Label resultLabel = new Label("Result:");
        inputLabel.setPrefWidth(400);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(inputLabel,0,0);
        gridPane.add(resultLabel,1,0);
        gridPane.add(inputArea,0,1);
        gridPane.add(resultArea,1,1);

        borderPane.setCenter(codeArea);
        borderPane.setBottom(gridPane);


        root.getChildren().add(menuBar);
        root.getChildren().add(borderPane);
        this.setScene(scene);
        this.setResizable(false);
        this.show();

        codeArea.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.Z){
                UndoAndRedo.redoSave(getCode());
                String code = UndoAndRedo.undo();
                setCode(code);
                codeArea.positionCaret(code.length());
            } else if (event.isControlDown() && event.getCode() == KeyCode.C || event.getCode() == KeyCode.BACK_SPACE ||
                    event.isControlDown() && event.getCode() == KeyCode.V){
                UndoAndRedo.undoSave(getCode());
            } else if (event.isControlDown() && event.getCode() == KeyCode.Y){
                String code = UndoAndRedo.redo();
                if (!code.equals("Empty")) {
                    UndoAndRedo.undoSave(getCode());
                    setCode(code);
                    codeArea.positionCaret(code.length());
                }
            }
        });
    }

    public String getCode(){
        return codeArea.getText();
    }
    public String getInput(){
        return inputArea.getText();
    }
    public void setResult(String result){
        resultArea.setText(result);
    }
    public void setCode(String code){
        codeArea.setText(code);
    }
}
