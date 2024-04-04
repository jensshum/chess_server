package userGameCommands;

public class UserGameCommand {
    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private String authToken;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public CommandType type() {
        return commandType;
    }

    public String typeString() {
        switch(commandType) {
            case JOIN_OBSERVER:
                return "Observer";
            case JOIN_PLAYER:
                return "Player";
            default:
                return "";
        }
    }
}
