package userGameCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
    private int gameID;
    private ChessGame.TeamColor playerColor;

    CommandType commandType = CommandType.JOIN_PLAYER;
}
