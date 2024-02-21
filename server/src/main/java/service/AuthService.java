package service;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import model.UserData;
import dataAccess.DataAccessException;

public class AuthService {

    private AuthDAO dataAccess;
    public AuthService() {
         dataAccess = new MemoryAuthDAO();
    }

    public UserData getUser(UserData user) throws DataAccessException {
        return dataAccess.selectUser(user);
    }
    public UserData register(UserData user) throws DataAccessException{
        return dataAccess.insertUser(user);
    };
}
