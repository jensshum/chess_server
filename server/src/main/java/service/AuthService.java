package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.SQLAuthDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.client.AuthenticationProtocolHandler;

import java.util.Map;
import java.util.UUID;

public class AuthService {

    private AuthDAO dataAccess;
    public AuthService() throws Exception{
         dataAccess = new SQLAuthDAO();
    }

    public UserData getUser(UserData user) throws Exception {
        return dataAccess.selectUser(user);
    }

    private String createAuthToken() {
        final String authToken = UUID.randomUUID().toString();
        return authToken;
    }
    public AuthData register(UserData user) throws Exception {
        UserData insertedUser = dataAccess.insertUser(user);
        AuthData newAuth = new AuthData(createAuthToken(), insertedUser.username());
        AuthData insertedAuth = dataAccess.insertToken(newAuth);
        return insertedAuth;
    };

    public AuthData login(UserData user) throws Exception{
        UserData loggedInUser = dataAccess.loginUser(user);
        if (loggedInUser != null) {
            String authToken = createAuthToken();
            AuthData newAuth = new AuthData(authToken, loggedInUser.username());
            AuthData insertedAuth = dataAccess.insertToken(newAuth);
            return insertedAuth;
        }
        else {
            return null;
        }
    }

    public void clearDatabase() throws DataAccessException {
        dataAccess.clear();
    }

    public AuthData verifyToken(AuthData auth) throws Exception{
        AuthData authUser = dataAccess.checkToken(auth);
        if (authUser == null) {
            return null;
        }
        else {
            return authUser;
        }
    }

    public AuthData logoutUser(AuthData auth) throws Exception{
        AuthData authUser = dataAccess.checkToken(auth);
        if (authUser != null) {
            return dataAccess.removeUser(authUser);
        }
        else {
            return null;
        }
    }



}
