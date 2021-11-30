package action;

import database.Database;
import fileio.ActionInputData;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.IOException;

public abstract class Action {
    private int actionId;
    private String actionType;
    private String type;
//    private String username;
//    private String objectType;
//    private String sortType;
//    private String criteria;
//    private String title;
//    private String genre;
//    private int number;
//    private double grade;
//    private int seasonNumber;
//    private List<List<String>> filters = new ArrayList<>();
    private Database database;
    private Writer output;
    private JSONArray arrayResult;
    String message;

    public Action(ActionInputData action, Database database, Writer output, JSONArray arrayResult) {
        this.actionId = action.getActionId();
        this.actionType = action.getActionType();
        this.type = action.getType();
        this.database = database;
        this.output = output;
        this.arrayResult = arrayResult;
    }

    public void execute(String type) {

    }

    public void writeOutput() {
        try {
            arrayResult.add(output.writeFile(getActionId(), "", message));
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }

    }

    public int getActionId() {
        return actionId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Database getDatabase() {
        return database;
    }

    public Writer getOutput() {
        return output;
    }
}
