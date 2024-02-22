package dataAccess;

import model.AuthData;
import model.UserData;
import java.util.Map;
import org.eclipse.jetty.server.Authentication;

public interface AuthDAO {

    public UserData insertUser(UserData user);
    public UserData selectUser(UserData user);
    public AuthData insertToken(AuthData auth);
    public UserData loginUser(UserData user);
    public void clear();
    public AuthData checkToken(AuthData auth);
    public AuthData removeUser(AuthData auth);
}
