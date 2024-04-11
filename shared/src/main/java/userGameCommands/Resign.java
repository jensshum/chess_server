package userGameCommands;

public class Resign extends UserGameCommand{
    private int gameID;

    public Resign(int gameID, String username) {
        this.gameID = gameID;
        CommandType commandType = CommandType.RESIGN;
        setUsername(username);

    }

}
