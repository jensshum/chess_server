package userGameCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{

    private int gameID;
    private ChessMove move;


    public MakeMove(int gameID, String authToken, String username, ChessMove move) {
        this.gameID = gameID;
        this.commandType = CommandType.MAKE_MOVE;
        this.move = move;
        setAuthToken(authToken);
        setUsername(username);

    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
