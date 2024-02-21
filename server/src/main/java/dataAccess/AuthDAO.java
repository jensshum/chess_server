package dataAccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

public interface AuthDAO {

    public UserData createUser(String username, String password, String email);



}
