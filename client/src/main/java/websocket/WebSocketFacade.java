package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import serverMessages.LoadGame;
import serverMessages.ServerMessage;
import userGameCommands.*;
import webSocketMessages.Action;
import webSocketMessages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch(serverMessage.getType())
                    {
                        case NOTIFICATION -> {
                            Notification notification = new Gson().fromJson(message, Notification.class);
                            notificationHandler.notify(notification);
                        }
                        case LOAD_GAME -> {
                            LoadGame notification = new Gson().fromJson(message, LoadGame.class);
                            notificationHandler.updateGame(notification);

                        }
                        case ERROR -> {
                            Notification error = new Gson().fromJson(message, Notification.class);
                            notificationHandler.notify(error);
                        }
                    }

                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException( ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinObserver(int gameId, String authToken, String username) throws ResponseException {
        try {
            UserGameCommand enterCommand = new JoinObserver(gameId, authToken, username);
            this.session.getBasicRemote().sendText(new Gson().toJson(enterCommand));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void joinPlayer(int gameId, String authToken, String username, ChessGame.TeamColor color) throws ResponseException {
        try {
            UserGameCommand enterCommand = new JoinPlayer(gameId, authToken, username, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(enterCommand));
        }
        catch (IOException ex) {
            throw new ResponseException(ex.getMessage());

        }
    }

    public void makeMove(int gameId, String authToken, String username, ChessMove move) throws ResponseException {
        try {
            UserGameCommand moveCommand = new MakeMove(gameId, authToken, username, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(moveCommand));
        }
        catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }

    public void Leave(int gameId, String authToken, String username) throws ResponseException {
        try {
            UserGameCommand leaveCommand = new Leave(gameId, username, authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(leaveCommand));
        }
        catch (Exception e) {
            throw new ResponseException(e.getMessage());
        }
    }


    public void leaveChess(String visitorName) throws ResponseException {
        try {
            var action = new Action(Action.Type.EXIT, visitorName);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

}