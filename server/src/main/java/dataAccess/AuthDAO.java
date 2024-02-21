package dataAccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface AuthDAO {

    public UserData insertUser(UserData user);
    public UserData selectUser(UserData user);
    public AuthData createToken(AuthData auth);

}
