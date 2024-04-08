package userGameCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
    private int gameID;
    private ChessGame.TeamColor playerColor;

    public JoinPlayer(int gameID, String authToken, String username, ChessGame.TeamColor color) {
        this.commandType = CommandType.JOIN_PLAYER;
        playerColor = color;
        this.gameID = gameID;
        setUsername(username);
        setAuthToken(authToken);
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }
}
