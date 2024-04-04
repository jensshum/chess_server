package server.websocket;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serverMessages.Error;
import serverMessages.ServerMessage;
import userGameCommands.JoinObserver;
import userGameCommands.UserGameCommand;
import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand gameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (gameCommand.type()) {
            case JOIN_OBSERVER -> join(gameCommand.type().toString(), session);
//            case EXIT -> exit(action.visitorName());
        }
    }


    private void join(String visitorName, Session session) throws IOException {
        connections.add(visitorName, session);
        var message = String.format("%s has joined the game", visitorName);
        ServerMessage notification = new ServerMessage();
        connections.broadcast(visitorName, notification);
    }

//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }

//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(ex.getMessage());
//        }
//    }
}