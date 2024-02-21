package service;

import dataAccess.AuthDAO;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import model.UserData;
import dataAccess.DataAccessException;

public class AuthService {

    private AuthDAO authDAO;
    public AuthService() {
        AuthDAO authDAO = new MemoryAuthDAO();
    }

    public UserData register(String username, String password, String email){
        return new UserData("jensshum", "fling", "email.com");
    };
}
