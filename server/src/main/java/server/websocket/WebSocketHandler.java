package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import serverMessages.LoadGame;
import serverMessages.Notification;
import serverMessages.ServerMessage;
import userGameCommands.JoinObserver;
import userGameCommands.JoinPlayer;
import userGameCommands.MakeMove;
import userGameCommands.UserGameCommand;

import dataAccess.SQLAuthDAO;


import static chess.ChessGame.TeamColor.BLACK;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    int currGameID;
    ChessGame currGame;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println(message);
        UserGameCommand gameCommand = new Gson().fromJson(message, UserGameCommand.class);
        String authToken = gameCommand.getAuthToken();
        switch (gameCommand.type()) {
            case JOIN_OBSERVER -> {
                JoinObserver newObserver = new Gson().fromJson(message, JoinObserver.class);
                join(newObserver.getGameID(), gameCommand.getUsername(), session, false, authToken, null);
            }
            case JOIN_PLAYER -> {
                JoinPlayer player = new Gson().fromJson(message, JoinPlayer.class);
                ChessGame.TeamColor color = player.getPlayerColor();
                join(player.getGameID(), gameCommand.getUsername(), session, true, authToken, color);
            }
            case LEAVE -> {

            }
            case MAKE_MOVE -> {
                MakeMove move = new Gson().fromJson(message, MakeMove.class);
                makeMove(gameCommand.getUsername(), session, move);
            }
            case RESIGN -> {

            }
//            case EXIT -> exit(action.visitorName());
        }
    }

    private ChessGame getGameFromDatabase(int gameID) throws Exception {
        SQLAuthDAO dataAccess = new SQLAuthDAO();
        GameData game = dataAccess.selectGame("", gameID);
        return game.getGame();
    }

    private void join(int gameId, String visitorName, Session session, boolean player, String authToken, ChessGame.TeamColor color) throws Exception {


        currGame = getGameFromDatabase(gameId);
        currGameID = gameId;
        Connection rootClient = new Connection(visitorName, session, authToken);
        ServerMessage loadMessage = new LoadGame(currGame);
        rootClient.send(loadMessage);
        int thing = connections.connections.size();
        connections.add(visitorName, session, authToken);

        if (player) {
            String colorString;
            if (color == BLACK) {
                colorString = "black";
            }
            else {
                colorString = "white";
            }
            var message = String.format("%s has joined the game as %s", visitorName, colorString);
            ServerMessage notification = new Notification(message);
            connections.broadcast(visitorName, notification);
        }
        else {
            var message = String.format("%s has joined the game as an observer.", visitorName);
            ServerMessage notification = new Notification(message);
            connections.broadcast(visitorName, notification);
        }
    }

    private void makeMove(String username, Session session, MakeMove move){
        if (!assertGame(move.getGameID())) {
            System.out.print("gameID error.");
        }


    }

    private boolean assertGame(int gameID) {
        return currGameID == gameID;
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