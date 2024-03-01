package service;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import java.util.HashMap;

public class GameService {

    private AuthDAO dataAccess;

    public GameService() {
        dataAccess = new MemoryAuthDAO();
    }

    public GameData createGame(String authToken, GameData game) throws Exception{
        AuthData auth = new AuthData(authToken, "");
        if (dataAccess.checkToken(auth) != null) {
            GameData gameObj = dataAccess.createGame(game.getGameName());
            return gameObj;
        }
        else
        {
            return null;
        }
    }

    public HashMap<Integer, GameData> getGames() {
        return dataAccess.games();
    }

    public GameData joinGame(String username, JoinGameData joinGameData) {
        return dataAccess.gameJoin(username, joinGameData);
    }



}
