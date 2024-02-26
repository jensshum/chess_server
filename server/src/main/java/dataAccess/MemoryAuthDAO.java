package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MemoryAuthDAO implements AuthDAO {
    private static int nextId = 1;
    private static int authId = 1;
    private static int gameId = 1;
    final static private HashMap<Integer, UserData> users = new HashMap<>();
    final static private HashMap<Integer, AuthData> authMap = new HashMap<>();
    final static private HashMap<Integer, GameData> games = new HashMap<>();


    @Override
    public UserData insertUser(UserData newUser) {
        users.put(nextId, newUser);
        nextId++;
        return newUser;
    }

    @Override
    public UserData selectUser(UserData user) {
        for (Map.Entry<Integer, UserData> entry : users.entrySet()) {
            UserData selectedUser = entry.getValue();
            if (Objects.equals(entry.getValue().username(), user.username())) {
                return selectedUser;
            }
        }
        return null;
    }

    @Override
    public AuthData insertToken(AuthData auth) {

        authMap.put(authId, auth);
        authId++;
        return auth;
    }

    @Override
    public UserData loginUser(UserData user) {

        for (Map.Entry<Integer, UserData> entry : users.entrySet()) {
            UserData userData = entry.getValue();
            if (Objects.equals(userData.username(), user.username())) {
                if (Objects.equals(userData.password(), user.password())) {
                    return userData;
                }
            }
        }
        return null;
    }

    @Override
    public void clear() {
        users.clear();
        authMap.clear();
    }

    @Override
    public AuthData checkToken(AuthData auth) {
        for (Map.Entry<Integer, AuthData> entry : authMap.entrySet()) {
            AuthData authData = entry.getValue();
            if (Objects.equals(authData.authToken(), auth.authToken())) {
                return authData;
            }
        }
        return null;
    };

    @Override
    public AuthData removeUser(AuthData auth) {
        Iterator<Map.Entry<Integer, AuthData>> iterator = authMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, AuthData> entry = iterator.next();
            if (entry.getValue().equals(auth)) {
                iterator.remove();
            }
        }
        return auth;
    }

    @Override
    public GameData createGame(String gameName) {
        ChessGame newChessGame = new ChessGame();
        GameData game = new GameData(gameId, "", "", gameName, newChessGame);
        games.put(gameId, game);
        gameId++;
        return game;
    }

    @Override
    public HashMap<Integer, GameData> games() {
        return games;
    }

    @Override
    public GameData gameJoin(String username, JoinGameData joinGameData) {
        for (Map.Entry<Integer, GameData> entry : games.entrySet()) {
            GameData gameEntry = entry.getValue();
            if (Objects.equals(gameEntry.gameID(), joinGameData.gameID())) {
                if (Objects.equals(joinGameData.playerColor(), "BLACK")) {
                    gameEntry.blackUsername();
                }
            }

        }
        return null;
    }

}
