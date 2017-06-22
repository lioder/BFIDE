package ui;

import java.util.Stack;
public class UndoAndRedo {
    static private Stack<String> undoStack = new Stack<>();
    static private Stack<String> redoStack = new Stack<>();

    public static void undoSave(String code){
        undoStack.push(code);
    }
    public static void redoSave(String code){
        redoStack.push(code);
    }
    public static String undo(){
        if (undoStack.isEmpty())
            return "";
        else
            return undoStack.pop();
    }
    public static String redo(){
        if (redoStack.isEmpty())
            return "Empty";
        else
            return redoStack.pop();
    }
}
