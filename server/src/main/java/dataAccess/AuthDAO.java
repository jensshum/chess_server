package dataAccess;

import model.AuthData;
import model.GameData;
import model.JoinGameData;
import model.UserData;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jetty.server.Authentication;

import javax.xml.crypto.Data;

public interface AuthDAO {

    public UserData insertUser(UserData user) throws Exception;
    public UserData selectUser(UserData user) throws Exception;
    public AuthData insertToken(AuthData auth) throws Exception;
    public UserData loginUser(UserData user) throws Exception;
    public void clear() throws DataAccessException;
    public AuthData checkToken(AuthData auth) throws Exception;
    public AuthData removeUser(AuthData auth) throws Exception;
    public GameData createGame(String gameName) throws Exception;
    public HashMap<Integer, GameData> games() throws Exception;
    public GameData gameJoin(String username, JoinGameData joinGameData) throws Exception;
}
