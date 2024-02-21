package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private static int nextId = 1;
    private static int authId = 1;
    final static private HashMap<Integer, UserData> users = new HashMap<>();
    final static private HashMap<Integer, AuthData> authMap = new HashMap<>();
//    public UserData registerUser(String username, String password, String email) {
//        return new UserData(username, password, email);
//    }
    @Override
    public UserData insertUser(UserData newUser) {
        users.put(nextId, newUser);
        nextId++;
        return newUser;
    }

    @Override
    public UserData selectUser(UserData user) {
        for (Map.Entry<Integer, UserData> entry : users.entrySet()) {
            if (entry.getValue().equals(user)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public AuthData createToken(AuthData auth) {

        authMap.put(authId, auth);
        authId++;
        return auth;
    }

}
