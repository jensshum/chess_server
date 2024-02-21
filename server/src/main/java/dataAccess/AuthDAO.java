package dataAccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface AuthDAO {

    public UserData insertUser(UserData user);
    public UserData selectUser(UserData user);

}
