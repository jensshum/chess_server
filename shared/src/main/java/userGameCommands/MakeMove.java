package userGameCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{

    private int gameID;
    private ChessMove move;

    CommandType commandType = CommandType.MAKE_MOVE;
}
