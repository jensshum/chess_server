package dataAccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            UserData selectedUser = entry.getValue();
            if (Objects.equals(entry.getValue().username(), user.username())) {
                return selectedUser;
            }
        }
        return null;
    }

    @Override
    public AuthData insertToken(AuthData auth) {

        authMap.put(authId, auth);
        authId++;
        return auth;
    }

    @Override
    public UserData loginUser(UserData user) {

        for (Map.Entry<Integer, UserData> entry : users.entrySet()) {
            UserData userData = entry.getValue();
            if (Objects.equals(userData.username(), user.username())) {
                if (Objects.equals(userData.password(), user.password())) {
                    return userData;
                }
            }
        }
        return null;
    }

    @Override
    public void clear() {
        users.clear();
        authMap.clear();
    }

}
