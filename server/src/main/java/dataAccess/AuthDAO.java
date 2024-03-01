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
    public UserData selectUser(UserData user);
    public AuthData insertToken(AuthData auth);
    public UserData loginUser(UserData user);
    public void clear();
    public AuthData checkToken(AuthData auth);
    public AuthData removeUser(AuthData auth);
    public GameData createGame(String gameName);
    public HashMap<Integer, GameData> games();
    public GameData gameJoin(String username, JoinGameData joinGameData);
}
