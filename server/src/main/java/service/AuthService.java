package service;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

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
        AuthData insertedAuth = dataAccess.createToken(newAuth);
        return insertedAuth;
    };


}
