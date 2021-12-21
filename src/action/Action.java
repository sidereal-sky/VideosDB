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
    private Database database;
    private Writer output;
    private JSONArray arrayResult;
    private String message;

    public Action(final ActionInputData action, final Database database,
                   final Writer output, final JSONArray arrayResult) {
        this.actionId = action.getActionId();
        this.actionType = action.getActionType();
        this.type = action.getType();
        this.database = database;
        this.output = output;
        this.arrayResult = arrayResult;
    }

    public Action(final Database database, final Writer output,
                  final JSONArray arrayResult) {
        this.database = database;
        this.output = output;
        this.arrayResult = arrayResult;
    }

    /**
     * @return action operation
     */
    public String getType() {
        return type;
    }

    /**
     * @param type: operation type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return database: users, videos, actors
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * @return String: output message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message: new output message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * perform operation: command/query/recommendation
     */
    public void execute() {

    }

    /**
     * writes messages to the output json files
     */
    public void writeOutput() {
        try {
            arrayResult.add(output.writeFile(actionId, "", message));
        } catch (IOException e) {
            System.out.println("Couldn't write to file");
        }

    }
}
