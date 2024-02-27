package service;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.client.AuthenticationProtocolHandler;

import java.util.Map;
import java.util.UUID;

public class AuthService {

    private AuthDAO dataAccess;
    public AuthService() {
         dataAccess = new MemoryAuthDAO();
    }

    public UserData getUser(UserData user) throws ResponseException {
        return dataAccess.selectUser(user);
    }

    private String createAuthToken() {
        final String authToken = UUID.randomUUID().toString();
        return authToken;
    }
    public AuthData register(UserData user) throws ResponseException {
        UserData insertedUser = dataAccess.insertUser(user);
        AuthData newAuth = new AuthData(createAuthToken(), insertedUser.username());
        AuthData insertedAuth = dataAccess.insertToken(newAuth);
        return insertedAuth;
    };

    public AuthData login(UserData user) {
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

    public void clearDatabase() {
        dataAccess.clear();
    }

    public AuthData verifyToken(AuthData auth) {
        AuthData authUser = dataAccess.checkToken(auth);
        if (authUser == null) {
            return null;
        }
        else {
            return authUser;
        }
    }

    public AuthData logoutUser(AuthData auth) {
        AuthData authUser = dataAccess.checkToken(auth);
        if (authUser != null) {
            return dataAccess.removeUser(authUser);
        }
        else {
            return null;
        }
    }



}
