package ui;


import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import rmi.RemoteHelper;
import service.ExecuteService;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Optional;

public class SimpleMenu {
    protected String name;
    protected Menu menu;
    protected ArrayList<MenuItem> menuItemsList = new ArrayList<>();
    protected String[] menuItemName = {};
    public SimpleMenu(){
    }

    public SimpleMenu(String menuName){
        menu = new Menu(menuName);
    }

    public MenuItem getMenuItem(String itemName){
        for (MenuItem mi:menuItemsList)
            if (mi.getText().equals(itemName))
                return mi;
        return null;
    }
    public void setMenuItem(){
        for (String itemName:menuItemName){
            MenuItem menuItem = new MenuItem(itemName);
            menuItemsList.add(menuItem);
        }
        menu.getItems().addAll(menuItemsList);
    }

    public Menu getMenu(){
        return menu;
    }
    public void setAction(MainFrame mainFrame){
    }
}
class FileMenu extends SimpleMenu{
    String[] menuItemName = {"Open","Save","Exit"};
    public FileMenu(){}
    public FileMenu(String menuName){
        menu = new Menu(menuName);
    }

    @Override
    public void setMenuItem() {
        Menu newMenuItem = new Menu("New");
        final ToggleGroup fileTypeGroup = new ToggleGroup();
        RadioMenuItem bfFile = new RadioMenuItem("BF File");
        RadioMenuItem ookFile = new RadioMenuItem("Ook File");
        bfFile.setToggleGroup(fileTypeGroup);
        ookFile.setToggleGroup(fileTypeGroup);
        newMenuItem.getItems().addAll(bfFile,ookFile);
        this.menuItemsList.add(newMenuItem);
        super.menuItemName = menuItemName;
        super.setMenuItem();
    }

    public void setAction(MainFrame mainFrame){
        ((Menu)getMenuItem("New")).getItems().get(0).setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("新建文件");
            dialog.setHeaderText("请输入新建文件的名称");
            dialog.setContentText("文件名：");
            Optional result = dialog.showAndWait();
            if (result.isPresent()) {
                mainFrame.fileName = (String) result.get();
                mainFrame.codeArea.clear();
                mainFrame.fileType = "bf";
            }
        });
        ((Menu)getMenuItem("New")).getItems().get(1).setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("新建文件");
            dialog.setHeaderText("请输入新建文件的名称");
            dialog.setContentText("文件名：");
            Optional result = dialog.showAndWait();
            if (result.isPresent()) {
                mainFrame.fileName = (String) result.get();
                mainFrame.codeArea.clear();
                mainFrame.fileType = "ook";
            }
        });
        getMenuItem("Open").setOnAction(event -> {
            Open open = new Open(mainFrame.userName);
            open.showAndWait();
            if (open.fileType.equals("")||open.fileName.equals("")||open.version.equals("")){
            }else {
                mainFrame.fileName = open.fileName;
                mainFrame.fileType = open.fileType;
                mainFrame.version = open.version;
                String code = "";
                try {
                    code = RemoteHelper.getInstance().getIOService().readFile(mainFrame.userName, mainFrame.fileName, mainFrame.version, mainFrame.fileType);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                mainFrame.originalCode = code;
                mainFrame.setCode(code);
            }
        });
        //保存
        getMenuItem("Save").setOnAction(event -> {
            String code = mainFrame.getCode();
            if (!mainFrame.originalCode.equals(code)){
                if (mainFrame.fileName.equals("") || mainFrame.fileType.equals("")) {
                    SaveDialog dialog = new SaveDialog(code, mainFrame.userName);
                    dialog.showAndWait();
                    mainFrame.fileName = dialog.fileName;
                    mainFrame.fileType = dialog.fileType;
                } else {
                    try {
                        RemoteHelper.getInstance().getIOService().writeFile(code, mainFrame.userName, mainFrame.fileName, mainFrame.fileType);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //退出
        getMenuItem("Exit").setOnAction(event ->  mainFrame.close());
    }
}
class EditMenu extends SimpleMenu{
    String[] menuItemName = {"Undo","Redo"};
    public EditMenu(){}
    public EditMenu(String menuName){
        menu = new Menu((menuName));
    }
    @Override
    public void setMenuItem() {
        super.menuItemName = menuItemName;
        super.setMenuItem();
    }

    @Override
    public void setAction(MainFrame mainFrame) {
        getMenuItem("Undo").setOnAction(event -> {
            UndoAndRedo.redoSave(mainFrame.getCode());
            String code = UndoAndRedo.undo();
            mainFrame.setCode(code);
            mainFrame.codeArea.positionCaret(code.length());
        });
        getMenuItem("Redo").setOnAction(event -> {
            String code = UndoAndRedo.redo();
            if (!code.equals("Empty")) {
                UndoAndRedo.undoSave(mainFrame.getCode());
                mainFrame.setCode(code);
                mainFrame.codeArea.positionCaret(code.length());
            }
        });
    }
}

class RunMenu extends SimpleMenu{
    String[] menuItemName = {"Execute"};
    public RunMenu(){}
    public RunMenu(String menuName){
        menu = new Menu(menuName);
    }

    @Override
    public void setMenuItem() {
        super.menuItemName = menuItemName;
        super.setMenuItem();
    }



    public void setAction(MainFrame mainFrame){
        getMenuItem("Execute").setOnAction(event -> {
            String fileType = mainFrame.fileType;
            String code = mainFrame.getCode();
            String param = mainFrame.getInput();
            String result;
            try {

                if (fileType.equals("bf")||!code.contains("o"))
                    result = RemoteHelper.getInstance().getExecuteService().bfExecute(code, param);
                else
                    result = RemoteHelper.getInstance().getExecuteService().ookExecute(code, param);
                mainFrame.setResult(result);
            } catch (RemoteException e){
                e.printStackTrace();
            }
        });
    }

}

class UserMenu extends SimpleMenu{
    String[] menuItemName = {"Log Out"};
    public UserMenu(){}
    public UserMenu(String menuName){
        menu = new Menu(menuName);
    }
    @Override
    public void setMenuItem() {
        super.menuItemName = menuItemName;
        super.setMenuItem();
    }

    public void setAction(MainFrame mainFrame){
        getMenuItem("Log Out").setOnAction(event -> {
            mainFrame.close();
            Login login = new Login();
        });
    }
}
