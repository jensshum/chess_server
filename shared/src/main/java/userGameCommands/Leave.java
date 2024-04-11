package userGameCommands;

public class Leave extends UserGameCommand{

    private int gameID;

    public Leave(int gameID, String username, String authToken) {
        this.gameID = gameID;
        this.commandType = CommandType.LEAVE;
        setUsername(username);
        setAuthToken(authToken);

    }

    public int getGameID() {
        return gameID;
    }
}
