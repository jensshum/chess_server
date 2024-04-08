package userGameCommands;

import chess.ChessGame;

public class UserGameCommand {
    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private String username;

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

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public String getAuthToken() {
        return authToken;
    }

}
