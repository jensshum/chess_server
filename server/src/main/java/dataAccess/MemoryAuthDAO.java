package dataAccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private int nextId = 1;
    final static private HashMap<Integer, UserData> users = new HashMap<>();
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

}
