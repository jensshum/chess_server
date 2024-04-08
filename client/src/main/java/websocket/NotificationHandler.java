package websocket;

import chess.ChessGame;
import serverMessages.LoadGame;
import webSocketMessages.Notification;

public interface NotificationHandler {
    void notify(Notification notification);
    void updateGame(LoadGame game);

}