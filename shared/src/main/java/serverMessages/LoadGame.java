package serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{
    private ChessGame game;

    public LoadGame(ChessGame game) {
        this.game = game;
        setType(ServerMessageType.LOAD_GAME);
    }

    public ChessGame getGame() {
        return game;
    }
}
