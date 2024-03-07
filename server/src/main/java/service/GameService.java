package service;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import java.util.HashMap;

public class GameService {

    private AuthDAO dataAccess;

    public GameService() throws Exception{
        dataAccess = new SQLAuthDAO();
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

    public HashMap<Integer, GameData> getGames() throws Exception{
        return dataAccess.games();
    }

    public GameData joinGame(String username, JoinGameData joinGameData) throws Exception{
        return dataAccess.gameJoin(username, joinGameData);
    }



}
