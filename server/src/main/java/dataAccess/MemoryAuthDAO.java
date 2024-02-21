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
    public UserData createUser(String username, String password, String email) {
        UserData newUser = new UserData(username, password, email);
        users.put(nextId, newUser);
        nextId++;

        for (Map.Entry<Integer, UserData> entry : users.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        return newUser;
    }

}
